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
import mekanism.common.MekanismFluids;
import mekanism.common.base.FluidHandlerWrapper;
import mekanism.common.base.IComparatorSupport;
import mekanism.common.base.IFluidHandlerWrapper;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.content.boiler.*;
import mekanism.common.integration.computer.IComputerIntegration;
import mekanism.common.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

public class TileEntityBoilerValve extends TileEntityBoilerCasing implements IFluidHandlerWrapper, IComputerIntegration, IComparatorSupport, IGasHandler, IConfigurable {

    private static final String[] methods = new String[]{"isFormed", "getSteam", "getWater", "getBoilRate", "getMaxBoilRate", "getTemp"};
    public BoilerTank waterTank;
    public BoilerTank steamTank;
    public BoilerGasTank inputTank;
    public BoilerGasTank outputTank;
    private int currentRedstoneLevel;

    public boolean Eject;

    public TileEntityBoilerValve() {
        super("BoilerValve");
        waterTank = new BoilerWaterTank(this);
        steamTank = new BoilerSteamTank(this);
        inputTank = new BoilerInputGasTank(this);
        outputTank = new BoilerOutputGasTank(this);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        Eject = nbtTags.getBoolean("Eject");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setBoolean("Eject", Eject);

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            if (structure != null && structure.upperRenderLocation != null && getPos().getY() >= structure.upperRenderLocation.y - 1) {
                if (structure.steamStored != null && structure.steamStored.amount > 0 && Eject) {
                    EmitUtils.forEachSide(getWorld(), getPos(), EnumSet.allOf(EnumFacing.class), (tile, side) -> {
                        if (!(tile instanceof TileEntityBoilerValve)) {
                            IFluidHandler handler = CapabilityUtils.getCapability(tile, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
                            if (handler != null && PipeUtils.canFill(handler, structure.steamStored)) {
                                structure.steamStored.amount -= handler.fill(structure.steamStored, true);
                                if (structure.steamStored.amount <= 0) {
                                    structure.steamStored = null;
                                }
                            }
                        }
                    });
                }

                Mekanism.EXECUTE_MANAGER.addSyncTask(() -> {
                    if (outputTank.getGas() != null && outputTank.getGas().getGas() != null && Eject) {
                        GasStack toSend = outputTank.getGas().copy().withAmount(Math.min(outputTank.getMaxGas(), outputTank.getGasAmount()));
                        outputTank.output(GasUtils.emit(toSend, this, EnumSet.allOf(EnumFacing.class)), true);
                    }
                });


                int newRedstoneLevel = getRedstoneLevel();
                if (newRedstoneLevel != currentRedstoneLevel) {
                    updateComparatorOutputLevelSync();
                    currentRedstoneLevel = newRedstoneLevel;
                }
            }
        }
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        if ((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) {
            if (structure.upperRenderLocation != null && getPos().getY() >= structure.upperRenderLocation.y - 1) {
                return new FluidTankInfo[]{steamTank.getInfo()};
            }
            return new FluidTankInfo[]{waterTank.getInfo()};
        }
        return PipeUtils.EMPTY;
    }

    @Override
    public FluidTankInfo[] getAllTanks() {
        return new FluidTankInfo[]{steamTank.getInfo(), waterTank.getInfo()};
    }

    @Override
    public int fill(EnumFacing from, @Nonnull FluidStack resource, boolean doFill) {
        return waterTank.fill(resource, doFill);
    }

    @Override
    @Nullable
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return steamTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, @Nonnull FluidStack fluid) {
        if (((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) && !Eject) {
            return structure.upperRenderLocation != null && getPos().getY() < structure.upperRenderLocation.y - 1 && fluid.getFluid() == FluidRegistry.WATER;
        }
        return false;
    }

    @Override
    public boolean canDrain(EnumFacing from, @Nullable FluidStack fluid) {
        if (((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) && Eject) {
            return structure.upperRenderLocation != null && getPos().getY() >= structure.upperRenderLocation.y - 1 && FluidContainerUtils.canDrain(structure.steamStored, fluid);
        }
        return false;
    }

    @Override
    public String[] getMethods() {
        return methods;
    }

    @Override
    public Object[] invoke(int method, Object[] arguments) throws NoSuchMethodException {
        if (method == 0) {
            return new Object[]{structure != null};
        } else {
            if (structure == null) {
                return new Object[]{"Unformed"};
            }
            switch (method) {
                case 1 -> {
                    return new Object[]{structure.steamStored != null ? structure.steamStored.amount : 0};
                }
                case 2 -> {
                    return new Object[]{structure.waterStored != null ? structure.waterStored.amount : 0};
                }
                case 3 -> {
                    return new Object[]{structure.lastBoilRate};
                }
                case 4 -> {
                    return new Object[]{structure.lastMaxBoil};
                }
                case 5 -> {
                    return new Object[]{structure.temperature};
                }
            }
        }
        throw new NoSuchMethodException();
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
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(waterTank.getFluidAmount(), waterTank.getCapacity());
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        return inputTank.input(stack, doTransfer);
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        return outputTank.output(amount, doTransfer);
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        if (((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) && !Eject) {
            return structure.upperRenderLocation != null && getPos().getY() < structure.upperRenderLocation.y - 1 && type == MekanismFluids.SuperheatedSodium;
        }
        return false;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        if (((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) && Eject) {
            return structure.upperRenderLocation != null && getPos().getY() >= structure.upperRenderLocation.y - 1 && GasUtils.canDrain(structure.OutputGas, type);
        }
        return false;
    }

    @Nonnull
    @Override
    public GasTankInfo[] getTankInfo() {
        return ((!world.isRemote && structure != null) || (world.isRemote && clientHasStructure)) ? new GasTankInfo[]{inputTank.getInfo(), outputTank.getInfo()} : IGasHandler.NONE;
    }


    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            boolean prevEject = Eject;
            Eject = dataStream.readBoolean();
            if (prevEject != Eject) {
                MekanismUtils.updateBlock(world, getPos());
            }
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(Eject);
        return data;
    }

    @Override
    public EnumActionResult onSneakRightClick(EntityPlayer player, EnumFacing side) {
        if (!world.isRemote) {
            Eject = !Eject;
            String modeText = " " + (Eject ? EnumColor.DARK_RED : EnumColor.DARK_GREEN) + LangUtils.transOutputInput(Eject) + ".";
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
