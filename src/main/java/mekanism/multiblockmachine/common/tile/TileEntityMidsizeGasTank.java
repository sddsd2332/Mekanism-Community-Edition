package mekanism.multiblockmachine.common.tile;

import ic2.api.energy.tile.IEnergyEmitter;
import io.netty.buffer.ByteBuf;
import mekanism.api.Coord4D;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.*;
import mekanism.common.Mekanism;
import mekanism.common.base.IAdvancedBoundingBlock;
import mekanism.common.base.IComparatorSupport;
import mekanism.common.base.IRedstoneControl;
import mekanism.common.base.ITankManager;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.integration.computer.IComputerIntegration;
import mekanism.common.network.PacketTileEntity;
import mekanism.common.security.ISecurityTile;
import mekanism.common.tile.TileEntityGasTank.GasMode;
import mekanism.common.tile.component.TileComponentSecurity;
import mekanism.common.tile.prefab.TileEntityContainerBlock;
import mekanism.common.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Collections;

public class TileEntityMidsizeGasTank extends TileEntityContainerBlock implements IGasHandler, IRedstoneControl, ISecurityTile, IComputerIntegration, IComparatorSupport, IAdvancedBoundingBlock, ITankManager {

    private static final String[] methods = new String[]{"getMaxGas", "getStoredGas", "getGas"};

    public GasTank gasTank;
    public GasMode dumping = GasMode.IDLE;
    public int GasOut;
    public int GasStorage;
    public int currentGasAmount;

    public int currentRedstoneLevel;
    public RedstoneControl controlType;
    public TileComponentSecurity securityComponent;
    public int numPowering;

    public TileEntityMidsizeGasTank(String name, int maxGas, int gasOut) {
        super(name);
        inventory = NonNullListSynchronized.withSize(2, ItemStack.EMPTY);
        gasTank = new GasTank(maxGas);
        GasOut = gasOut;
        GasStorage = maxGas;
        securityComponent = new TileComponentSecurity(this);
        controlType = RedstoneControl.DISABLED;
    }

    public TileEntityMidsizeGasTank() {
        this("MidsizeGasTank", MekanismConfig.current().multiblock.MidsizeGasTankStorage.val(), MekanismConfig.current().multiblock.MidsizeGasTankOutput.val());
    }

    @Override
    public void onUpdate() {
        if (!world.isRemote) {
            TileUtils.drawGas(inventory.get(0), gasTank, true);
            TileUtils.receiveGasItem(inventory.get(1), gasTank, inventory.get(1).getItem() instanceof IGasItem item && !item.getGas(inventory.get(1)).getGas().isRadiation());
            if (gasTank.getGas() != null && MekanismUtils.canFunction(this) && dumping != GasMode.DUMPING) {
                Mekanism.EXECUTE_MANAGER.addSyncTask(() -> {
                    handleTank(gasTank, getOutputTank());
                });
            }
            if (dumping == GasMode.DUMPING) {
                gasTank.draw(GasStorage / 400, true);
            }
            if (dumping == GasMode.DUMPING_EXCESS && gasTank.getNeeded() < GasOut) {
                gasTank.draw(GasOut - gasTank.getNeeded(), true);
            }

            int newGasAmount = gasTank.getStored();
            if (newGasAmount != currentGasAmount) {
                MekanismUtils.saveChunk(this);
            }
            currentGasAmount = newGasAmount;
            int newRedstoneLevel = getRedstoneLevel();
            if (newRedstoneLevel != currentRedstoneLevel) {
                markNoUpdateSync();
                currentRedstoneLevel = newRedstoneLevel;
            }
        }
    }


    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        Item item = itemstack.getItem();
        if (slotID == 1) {
            if (item instanceof IGasItem gasItem && gasItem.getGas(itemstack) != null && gasItem.getGas(itemstack).getGas().isRadiation()) {
                return false;
            } else {
                return item instanceof IGasItem gasItem && gasItem.getGas(itemstack) == null;
            }
        } else if (slotID == 0) {
            return itemstack.getItem() instanceof IGasItem gasItem && gasItem.getGas(itemstack) != null &&
                    gasItem.getGas(itemstack).amount == gasItem.getMaxGas(itemstack);
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, @Nonnull ItemStack itemstack) {
        Item item = itemstack.getItem();
        if (slotID == 0) {
            return itemstack.getItem() instanceof IGasItem gasItem && (gasTank.getGas() == null || gasItem.canReceiveGas(itemstack, gasTank.getGas().getGas()));
        } else if (slotID == 1) {
            if (item instanceof IGasItem gasItem) {
                GasStack gas = gasItem.getGas(itemstack);
                if (gas == null) {
                    return true;
                }
                Gas type = gas.getGas();
                if (type.isRadiation()) {
                    return false;
                } else {
                    return gasTank.getGas() == null || gasItem.canProvideGas(itemstack, gasTank.getGas().getGas());
                }
            }
            return false;
        }
        return false;
    }

    @NotNull
    @Override
    public int[] getSlotsForFace(@NotNull EnumFacing side) {
        return InventoryUtils.EMPTY;
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        if (canReceiveGas(side, stack != null ? stack.getGas() : null)) {
            return gasTank.receive(stack, doTransfer);
        }
        return 0;
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        if (canDrawGas(side, null)) {
            return gasTank.draw(amount, doTransfer);
        }
        return null;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        return gasTank.canDraw(type) && side == facing && !type.isRadiation();
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        return gasTank.canReceive(type) && side == MekanismUtils.getBack(facing) && !type.isRadiation();
    }

    @Nonnull
    @Override
    public GasTankInfo[] getTankInfo() {
        return new GasTankInfo[]{gasTank};
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return false;
        }
        return capability == Capabilities.GAS_HANDLER_CAPABILITY || super.hasCapability(capability, side);
    }


    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return null;
        } else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
        }
        return super.getCapability(capability, side);
    }

    @Override
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side) {
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return side != MekanismUtils.getBack(facing);
        }
        return super.isCapabilityDisabled(capability, side);
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            int type = dataStream.readInt();
            if (type == 0) {
                int index = (dumping.ordinal() + 1) % GasMode.values().length;
                dumping = GasMode.values()[index];
            }
            if (type == 1) {
                gasTank.setGas(null);
            }
            for (EntityPlayer player : playersUsing) {
                Mekanism.packetHandler.sendTo(new PacketTileEntity.TileEntityMessage(this), (EntityPlayerMP) player);
            }

            return;
        }
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            gasTank.setMaxGas(GasStorage);
            TileUtils.readTankData(dataStream, gasTank);
            dumping = GasMode.values()[dataStream.readInt()];
            controlType = RedstoneControl.values()[dataStream.readInt()];
            numPowering = dataStream.readInt();
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        gasTank.read(nbtTags.getCompoundTag("gasTank"));
        dumping = GasMode.values()[nbtTags.getInteger("dumping")];
        controlType = RedstoneControl.values()[nbtTags.getInteger("controlType")];
        numPowering = nbtTags.getInteger("numPowering");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setTag("gasTank", gasTank.write(new NBTTagCompound()));
        nbtTags.setInteger("dumping", dumping.ordinal());
        nbtTags.setInteger("controlType", controlType.ordinal());
        nbtTags.setInteger("numPowering", numPowering);
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        TileUtils.addTankData(data, gasTank);
        data.add(dumping.ordinal());
        data.add(controlType.ordinal());
        data.add(numPowering);
        return data;
    }

    @Override
    public boolean canSetFacing(@Nonnull EnumFacing facing) {
        return facing != EnumFacing.DOWN && facing != EnumFacing.UP;
    }


    public TileEntity getOutputTank() {
        BlockPos pos = getPos().up();
        if (world.getTileEntity(pos) != null) {
            return world.getTileEntity(pos);
        }
        return null;
    }

    private void handleTank(GasTank tank, TileEntity tile) {
        if (tank.getGas() != null) {
            GasStack toSend = new GasStack(tank.getGas().getGas(), Math.min(tank.getStored(), GasOut));
            tank.draw(GasUtils.emit(toSend, tile, Collections.singleton(facing)), true);
        }
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(gasTank.getStored(), gasTank.getMaxGas());
    }


    @Override
    public RedstoneControl getControlType() {
        return controlType;
    }

    @Override
    public void setControlType(RedstoneControl type) {
        controlType = type;
    }

    @Override
    public boolean canPulse() {
        return false;
    }

    @Override
    public TileComponentSecurity getSecurity() {
        return securityComponent;
    }

    @Override
    public String[] getMethods() {
        return methods;
    }

    @Override
    public Object[] invoke(int method, Object[] arguments) throws NoSuchMethodException {
        return switch (method) {
            case 0 -> new Object[]{gasTank.getMaxGas()};
            case 1 -> new Object[]{gasTank.getStored()};
            case 2 -> new Object[]{gasTank.getGas()};
            default -> throw new NoSuchMethodException();
        };
    }


    @Override
    public boolean canBoundReceiveEnergy(BlockPos location, EnumFacing side) {
        return false;
    }

    @Override
    public boolean canBoundOutPutEnergy(BlockPos location, EnumFacing side) {
        return false;
    }

    @Override
    public void onPower() {
        numPowering++;
    }

    @Override
    public void onNoPower() {
        numPowering--;
    }


    @Override
    public NBTTagCompound getConfigurationData(NBTTagCompound nbtTags) {
        return nbtTags;
    }

    @Override
    public void setConfigurationData(NBTTagCompound nbtTags) {

    }

    @Override
    public Object[] getTanks() {
        return new Object[]{gasTank};
    }

    @Override
    public String getDataType() {
        return getBlockType().getTranslationKey() + "." + fullName + ".name";
    }

    @Override
    public void onPlace() {
        Coord4D current = Coord4D.get(this);
        MekanismUtils.makeAdvancedBoundingBlock(world, getPos().up(), current);
    }

    @Override
    public void onBreak() {
        world.setBlockToAir(getPos().up());
        world.setBlockToAir(getPos());
    }


    @Override
    public boolean hasOffsetCapability(@NotNull Capability<?> capability, @Nullable EnumFacing side, @NotNull Vec3i offset) {
        if (isOffsetCapabilityDisabled(capability, side, offset)) {
            return false;
        }
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return true;
        }
        return hasCapability(capability, side);
    }

    @Nullable
    @Override
    public <T> T getOffsetCapability(@NotNull Capability<T> capability, @Nullable EnumFacing side, @NotNull Vec3i offset) {
        if (isOffsetCapabilityDisabled(capability, side, offset)) {
            return null;
        } else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
        }
        return getCapability(capability, side);
    }

    @Override
    public boolean isOffsetCapabilityDisabled(@NotNull Capability<?> capability, @Nullable EnumFacing side, @NotNull Vec3i offset) {
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            if (offset.equals(new Vec3i(0, 1, 0))) {
                return side != facing;
            }
        }
        return false;
    }

    @Override
    public double getEnergy() {
        return 0;
    }

    @Override
    public void setEnergy(double energy) {

    }

    @Override
    public double getMaxEnergy() {
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int extractEnergy(EnumFacing enumFacing, int i, boolean b) {
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int receiveEnergy(EnumFacing enumFacing, int i, boolean b) {
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int getEnergyStored(EnumFacing enumFacing) {
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int getMaxEnergyStored(EnumFacing enumFacing) {
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public boolean canConnectEnergy(EnumFacing enumFacing) {
        return false;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.IC2_MOD_ID)
    public double getDemandedEnergy() {
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.IC2_MOD_ID)
    public int getSinkTier() {
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.IC2_MOD_ID)
    public double injectEnergy(EnumFacing enumFacing, double v, double v1) {
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.IC2_MOD_ID)
    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing enumFacing) {
        return false;
    }

    @Override
    public double acceptEnergy(EnumFacing side, double amount, boolean simulate) {
        return 0;
    }

    @Override
    public boolean canReceiveEnergy(EnumFacing side) {
        return false;
    }

    @Override
    public double pullEnergy(EnumFacing side, double amount, boolean simulate) {
        return 0;
    }

    @Override
    public boolean canOutputEnergy(EnumFacing side) {
        return false;
    }
}
