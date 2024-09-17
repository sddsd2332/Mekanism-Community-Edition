package mekanism.common.tile.multiblock;

import io.netty.buffer.ByteBuf;
import mekanism.api.EnumColor;
import mekanism.api.IConfigurable;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTankInfo;
import mekanism.api.gas.IGasHandler;
import mekanism.common.Mekanism;
import mekanism.common.base.FluidHandlerWrapper;
import mekanism.common.base.IComparatorSupport;
import mekanism.common.base.IFluidHandlerWrapper;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.content.tank.DynamicFluidTank;
import mekanism.common.content.tank.DynamicGasTank;
import mekanism.common.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

public class TileEntityDynamicValve extends TileEntityDynamicTank implements IFluidHandlerWrapper, IGasHandler, IComparatorSupport, IConfigurable {

    public DynamicFluidTank fluidTank;
    public DynamicGasTank gasTank;
    private int currentRedstoneLevel;

    public boolean eject;

    public TileEntityDynamicValve() {
        super("Dynamic Valve");
        fluidTank = new DynamicFluidTank(this);
        gasTank = new DynamicGasTank(this);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        eject = nbtTags.getBoolean("eject");
    }


    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setBoolean("eject", eject);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            int newRedstoneLevel = getRedstoneLevel();
            if (newRedstoneLevel != currentRedstoneLevel) {
                updateComparatorOutputLevelSync();
                currentRedstoneLevel = newRedstoneLevel;
            }

            if (structure != null && eject) {
                if (fluidTank.getFluid() != null && fluidTank.getFluid().getFluid() != null) {
                    IFluidTank tank = fluidTank;
                    EmitUtils.forEachSide(getWorld(), getPos(), EnumSet.allOf(EnumFacing.class), (tile, side) -> {
                        if (!(tile instanceof TileEntityDynamicValve)) {
                            IFluidHandler handler = CapabilityUtils.getCapability(tile, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
                            if (handler != null && PipeUtils.canFill(handler, tank.getFluid())) {
                                tank.drain(handler.fill(tank.getFluid(), true), true);
                            }
                        }
                    });
                }
                Mekanism.EXECUTE_MANAGER.addSyncTask(() -> {
                    if (gasTank.getGas() != null && gasTank.getGas().getGas() != null) {
                        GasStack toSend = gasTank.getGas().copy().withAmount(Math.min(gasTank.getMaxGas(), gasTank.getGasAmount()));
                        gasTank.output(GasUtils.emit(toSend, this, EnumSet.allOf(EnumFacing.class)), true);
                    }
                });
            }
        }
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return ((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) ? new FluidTankInfo[]{fluidTank.getInfo()} : PipeUtils.EMPTY;
    }

    @Override
    public FluidTankInfo[] getAllTanks() {
        return getTankInfo(null);
    }

    @Override
    public int fill(EnumFacing from, @Nonnull FluidStack resource, boolean doFill) {
        return fluidTank.fill(resource, doFill);
    }

    @Override
    @Nullable
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return fluidTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, @Nonnull FluidStack fluid) {
        return ((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) && !eject;
    }

    @Override
    public boolean canDrain(EnumFacing from, @Nullable FluidStack fluid) {
        return ((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) && FluidContainerUtils.canDrain(structure.fluidStored, fluid);
    }

    @Nonnull
    @Override
    public String getName() {
        return LangUtils.localize("gui.dynamicTank");
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        if ((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) {
            if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == Capabilities.GAS_HANDLER_CAPABILITY || capability == Capabilities.CONFIGURABLE_CAPABILITY) {
                return true;
            }
        }
        return super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if ((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) {
            if (capability == Capabilities.CONFIGURABLE_CAPABILITY || capability == Capabilities.GAS_HANDLER_CAPABILITY) {
                return (T) this;
            }
            if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FluidHandlerWrapper(this, side));
            }
        }
        return super.getCapability(capability, side);
    }

    @Override
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return !world.isRemote ? structure == null : !clientHasStructure;
        }
        return super.isCapabilityDisabled(capability, side);
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return (!world.isRemote && structure != null) || (world.isRemote && clientHasStructure) ? SLOTS : InventoryUtils.EMPTY;
    }

    @Override
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack) {
        //can be filled/emptied
        return slot == 0 && FluidContainerUtils.isFluidContainer(stack);
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(fluidTank.getFluidAmount(), fluidTank.getCapacity());
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        return gasTank.input(stack, doTransfer);
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        return gasTank.output(amount, doTransfer);
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        return ((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) && !eject;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        return ((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) && GasUtils.canDrain(structure.gasstored, type);
    }

    @Nonnull
    @Override
    public GasTankInfo[] getTankInfo() {
        return ((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) ? new GasTankInfo[]{gasTank.getInfo()} : IGasHandler.NONE;
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            boolean prevEject = eject;
            eject = dataStream.readBoolean();
            if (prevEject != eject) {
                MekanismUtils.updateBlock(world, getPos());
            }
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(eject);
        return data;
    }

    @Override
    public EnumActionResult onSneakRightClick(EntityPlayer player, EnumFacing side) {
        if (!world.isRemote) {
            eject = !eject;
            String modeText = " " + (eject ? EnumColor.DARK_RED : EnumColor.DARK_GREEN) + LangUtils.transOutputInput(eject) + ".";
            player.sendMessage(new TextComponentString(EnumColor.DARK_BLUE + Mekanism.LOG_TAG + " " + EnumColor.GREY +
                    LangUtils.localize("tooltip.configurator.reactorPortEject") + modeText));
            Mekanism.packetHandler.sendUpdatePacket(this);
            markNoUpdateSync();
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public EnumActionResult onRightClick(EntityPlayer player, EnumFacing side) {
        return EnumActionResult.PASS;
    }
}
