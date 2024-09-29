package mekanism.common.tile;

import io.netty.buffer.ByteBuf;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.*;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.Mekanism;
import mekanism.common.SideData;
import mekanism.common.base.IComparatorSupport;
import mekanism.common.base.IRedstoneControl;
import mekanism.common.base.ISideConfiguration;
import mekanism.common.base.ITierUpgradeable;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.integration.computer.IComputerIntegration;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.security.ISecurityTile;
import mekanism.common.tier.BaseTier;
import mekanism.common.tier.GasTankTier;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.TileComponentSecurity;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.prefab.TileEntityContainerBlock;
import mekanism.common.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;

public class TileEntityGasTank extends TileEntityContainerBlock implements IGasHandler, IRedstoneControl, ISideConfiguration, ISecurityTile, ITierUpgradeable,
        IComputerIntegration, IComparatorSupport {

    private static final String[] methods = new String[]{"getMaxGas", "getStoredGas", "getGas"};
    /**
     * The type of gas stored in this tank.
     */
    public GasTank gasTank;

    public GasTankTier tier = GasTankTier.BASIC;

    public GasMode dumping;

    public int currentGasAmount;

    public int currentRedstoneLevel;

    /**
     * This machine's current RedstoneControl type.
     */
    public RedstoneControl controlType;

    public TileComponentEjector ejectorComponent;
    public TileComponentConfig configComponent;
    public TileComponentSecurity securityComponent;

    public TileEntityGasTank() {
        super("GasTank");
        configComponent = new TileComponentConfig(this, TransmissionType.GAS, TransmissionType.ITEM);

        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.NONE, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.INPUT, new int[]{0}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.OUTPUT, new int[]{1}));
        configComponent.setConfig(TransmissionType.ITEM, new byte[]{1, 1, 2, 1, 1, 1});
        configComponent.setCanEject(TransmissionType.ITEM, false);

        configComponent.setIOConfig(TransmissionType.GAS);
        configComponent.setEjecting(TransmissionType.GAS, true);

        gasTank = new GasTank(tier.getStorage());
        inventory = NonNullListSynchronized.withSize(2, ItemStack.EMPTY);
        dumping = GasMode.IDLE;
        controlType = RedstoneControl.DISABLED;

        ejectorComponent = new TileComponentEjector(this);

        securityComponent = new TileComponentSecurity(this);
    }

    @Override
    public void onUpdate() {
        if (!world.isRemote) {
            TileUtils.drawGas(inventory.get(0), gasTank, tier != GasTankTier.CREATIVE);
            if (TileUtils.receiveGas(inventory.get(1), gasTank) && tier == GasTankTier.CREATIVE && gasTank.getGas() != null) {
                gasTank.getGas().amount = Integer.MAX_VALUE;
            }
            Mekanism.EXECUTE_MANAGER.addSyncTask(() -> {
                handTank();
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
            });
        }
    }

    public void handTank() {
        if (gasTank.getGas() != null && MekanismUtils.canFunction(this) && (tier == GasTankTier.CREATIVE || dumping != GasMode.DUMPING)) {
            if (configComponent.isEjecting(TransmissionType.GAS)) {
                if (gasTank.getGas().getGas() != null) {
                    GasStack toSend = gasTank.getGas().copy().withAmount(Math.min(gasTank.getStored(), tier.getOutput()));
                    gasTank.draw(GasUtils.emit(toSend, this, configComponent.getSidesForData(TransmissionType.GAS, facing, 2)), tier != GasTankTier.CREATIVE);
                }
            }
        }
        if (tier != GasTankTier.CREATIVE) {
            if (dumping == GasMode.DUMPING) {
                gasTank.draw(tier.getStorage() / 400, true);
            }
            if (dumping == GasMode.DUMPING_EXCESS && gasTank.getNeeded() < tier.getOutput()) {
                gasTank.draw(tier.getOutput() - gasTank.getNeeded(), true);
            }
        }
    }

    @Override
    public boolean upgrade(BaseTier upgradeTier) {
        if (upgradeTier.ordinal() != tier.ordinal() + 1) {
            return false;
        }
        tier = GasTankTier.values()[upgradeTier.ordinal()];
        gasTank.setMaxGas(tier.getStorage());
        Mekanism.packetHandler.sendUpdatePacket(this);
        markNoUpdateSync();
        return true;
    }

    @Nonnull
    @Override
    public String getName() {
        return LangUtils.localize("tile.GasTank" + tier.getBaseTier().getSimpleName() + ".name");
    }

    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        Item item = itemstack.getItem();
        if (slotID == 1) {
            if (tier != GasTankTier.CREATIVE && item instanceof IGasItem gasItem && gasItem.getGas(itemstack) != null && gasItem.getGas(itemstack).getGas().isRadiation()) {
                return false;
            } else {
                return item instanceof IGasItem gasItem && gasItem.getGas(itemstack) == null;
            }
        } else if (slotID == 0) {
            return item instanceof IGasItem gasItem && gasItem.getGas(itemstack) != null &&
                    gasItem.getGas(itemstack).amount == gasItem.getMaxGas(itemstack);
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, @Nonnull ItemStack itemstack) {
        Item item = itemstack.getItem();
        if (slotID == 0) {
            return item instanceof IGasItem gasItem && (gasTank.getGas() == null || gasItem.canReceiveGas(itemstack, gasTank.getGas().getGas()));
        } else if (slotID == 1) {
            if (tier == GasTankTier.CREATIVE) {
                return item instanceof IGasItem gasItem && (gasTank.getGas() == null || gasItem.canProvideGas(itemstack, gasTank.getGas().getGas()));
            }
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

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return configComponent.getOutput(TransmissionType.ITEM, side, facing).availableSlots;
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {

        if (tier == GasTankTier.CREATIVE) {
            return stack != null ? stack.amount : 0;
        }
        if (stack.getGas().isRadiation()) {
            return gasTank.receive(stack, false);
        } else {
            return gasTank.receive(stack, doTransfer);
        }
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        if (canDrawGas(side, null)) {
            return gasTank.draw(amount, doTransfer && tier != GasTankTier.CREATIVE);
        }
        return null;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        if (configComponent.hasSideForData(TransmissionType.GAS, facing, 2, side)) {
            return gasTank.canDraw(type);
        }
        return false;
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        if (tier != GasTankTier.CREATIVE && (type.isRadiation())) {
            return false;
        } else if (configComponent.hasSideForData(TransmissionType.GAS, facing, 1, side)) {
            return gasTank.canReceive(type);
        }
        return false;
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
        return configComponent.isCapabilityDisabled(capability, side, facing) || super.isCapabilityDisabled(capability, side);
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
                Mekanism.packetHandler.sendTo(new TileEntityMessage(this), (EntityPlayerMP) player);
            }

            return;
        }
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            GasTankTier prevTier = tier;
            tier = GasTankTier.values()[dataStream.readInt()];
            gasTank.setMaxGas(tier.getStorage());
            TileUtils.readTankData(dataStream, gasTank);
            dumping = GasMode.values()[dataStream.readInt()];
            controlType = RedstoneControl.values()[dataStream.readInt()];
            if (prevTier != tier) {
                MekanismUtils.updateBlock(world, getPos());
            }
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        tier = GasTankTier.values()[nbtTags.getInteger("tier")];
        gasTank.read(nbtTags.getCompoundTag("gasTank"));
        dumping = GasMode.values()[nbtTags.getInteger("dumping")];
        controlType = RedstoneControl.values()[nbtTags.getInteger("controlType")];
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setInteger("tier", tier.ordinal());
        nbtTags.setTag("gasTank", gasTank.write(new NBTTagCompound()));
        nbtTags.setInteger("dumping", dumping.ordinal());
        nbtTags.setInteger("controlType", controlType.ordinal());
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(tier.ordinal());
        TileUtils.addTankData(data, gasTank);
        data.add(dumping.ordinal());
        data.add(controlType.ordinal());
        return data;
    }

    @Override
    public boolean canSetFacing(@Nonnull EnumFacing facing) {
        return facing != EnumFacing.DOWN && facing != EnumFacing.UP;
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
    public TileComponentEjector getEjector() {
        return ejectorComponent;
    }

    @Override
    public TileComponentConfig getConfig() {
        return configComponent;
    }

    @Override
    public EnumFacing getOrientation() {
        return facing;
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

    public enum GasMode {
        IDLE("gui.idle"),
        DUMPING_EXCESS("gui.dumping_excess"),
        DUMPING("gui.dumping");

        private final String langKey;

        GasMode(String langKey) {
            this.langKey = langKey;
        }

        public static <T> T chooseByMode(GasMode dumping, T idleOption, T dumpingOption, T dumpingExcessOption) {
            return switch (dumping) {
                case IDLE -> idleOption;
                case DUMPING -> dumpingOption;
                case DUMPING_EXCESS -> dumpingExcessOption;
            };
        }

        public String getLangKey() {
            return langKey;
        }
    }
}
