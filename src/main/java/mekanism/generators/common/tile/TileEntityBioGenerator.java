package mekanism.generators.common.tile;

import io.netty.buffer.ByteBuf;
import mekanism.api.TileNetworkList;
import mekanism.common.FluidSlot;
import mekanism.common.MekanismItems;
import mekanism.common.base.FluidHandlerWrapper;
import mekanism.common.base.IComparatorSupport;
import mekanism.common.base.IFluidHandlerWrapper;
import mekanism.common.base.ISustainedData;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.*;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;

public class TileEntityBioGenerator extends TileEntityGenerator implements IFluidHandlerWrapper, ISustainedData, IComparatorSupport {

    private static final String[] methods = new String[]{"getEnergy", "getOutput", "getMaxEnergy", "getEnergyNeeded", "getBioFuel", "getBioFuelNeeded"};
    private static FluidTankInfo[] ALL_TANKS = new FluidTankInfo[0];
    /**
     * The FluidSlot biofuel instance for this generator.
     */
    public FluidSlot bioFuelSlot = new FluidSlot(24000, -1);

    private int currentRedstoneLevel;

    public TileEntityBioGenerator() {
        super("bio", "BioGenerator", MekanismConfig.current().generators.bioGeneratorStorage.val(), MekanismConfig.current().generators.bioGeneration.val() * 2);
        inventory = NonNullListSynchronized.withSize(2, ItemStack.EMPTY);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            ChargeUtils.charge(1, this);
            if (!inventory.get(0).isEmpty()) {
                FluidStack fluid = FluidUtil.getFluidContained(inventory.get(0));
                if (fluid != null && FluidRegistry.isFluidRegistered("bioethanol")) {
                    if (fluid.getFluid() == FluidRegistry.getFluid("bioethanol")) {
                        IFluidHandler handler = FluidUtil.getFluidHandler(inventory.get(0));
                        FluidStack drained = handler.drain(bioFuelSlot.MAX_FLUID - bioFuelSlot.fluidStored, true);
                        if (drained != null) {
                            bioFuelSlot.fluidStored += drained.amount;
                        }
                    }
                } else {
                    int fuel = getFuel(inventory.get(0));
                    if (fuel > 0) {
                        int fuelNeeded = bioFuelSlot.MAX_FLUID - bioFuelSlot.fluidStored;
                        if (fuel <= fuelNeeded) {
                            bioFuelSlot.fluidStored += fuel;
                            if (!inventory.get(0).getItem().getContainerItem(inventory.get(0)).isEmpty()) {
                                inventory.set(0, inventory.get(0).getItem().getContainerItem(inventory.get(0)));
                            } else {
                                inventory.get(0).shrink(1);
                            }
                        }
                    }
                }
            }
            if (canOperate()) {
                setActive(true);
                bioFuelSlot.setFluid(bioFuelSlot.fluidStored - 1);
                setEnergy(electricityStored.get() + MekanismConfig.current().generators.bioGeneration.val());
            } else {
                setActive(false);
            }
            int newRedstoneLevel = getRedstoneLevel();
            if (newRedstoneLevel != currentRedstoneLevel) {
                world.updateComparatorOutputLevel(pos, getBlockType());
                currentRedstoneLevel = newRedstoneLevel;
            }

        }
    }

    @Override
    public boolean isItemValidForSlot(int slotID, @Nonnull ItemStack itemstack) {
        if (slotID == 0) {
            if (getFuel(itemstack) > 0) {
                return true;
            } else if (FluidRegistry.isFluidRegistered("bioethanol")) {
                FluidStack fluidContained = FluidUtil.getFluidContained(itemstack);
                if (fluidContained != null) {
                    return fluidContained.getFluid() == FluidRegistry.getFluid("bioethanol");
                }
            }
            return false;
        } else if (slotID == 1) {
            return ChargeUtils.canBeCharged(itemstack);
        }
        return true;
    }

    @Override
    public boolean canOperate() {
        return electricityStored.get() < BASE_MAX_ENERGY && bioFuelSlot.fluidStored > 0 && MekanismUtils.canFunction(this);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        bioFuelSlot.fluidStored = nbtTags.getInteger("bioFuelStored");
    }

    @Override
   public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setInteger("bioFuelStored", bioFuelSlot.fluidStored);
    }

    public int getFuel(ItemStack itemstack) {
        return itemstack.getItem() == MekanismItems.BioFuel ? 200 : 0;
    }

    /**
     * Gets the scaled fuel level for the GUI.
     *
     * @param i - multiplier
     * @return Scaled fuel level
     */
    public int getScaledFuelLevel(int i) {
        return bioFuelSlot.fluidStored * i / bioFuelSlot.MAX_FLUID;
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return side == MekanismUtils.getRight(facing) ? new int[]{1} : new int[]{0};
    }

    @Override
    public boolean canSetFacing(@Nonnull EnumFacing facing) {
        return super.canSetFacing(facing);
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            bioFuelSlot.fluidStored = dataStream.readInt();
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(bioFuelSlot.fluidStored);
        return data;
    }

    @Override
    public String[] getMethods() {
        return methods;
    }

    @Override
    public Object[] invoke(int method, Object[] arguments) throws NoSuchMethodException {
        return switch (method) {
            case 0 -> new Object[]{electricityStored};
            case 1 -> new Object[]{output};
            case 2 -> new Object[]{BASE_MAX_ENERGY};
            case 3 -> new Object[]{BASE_MAX_ENERGY - electricityStored.get()};
            case 4 -> new Object[]{bioFuelSlot.fluidStored};
            case 5 -> new Object[]{bioFuelSlot.MAX_FLUID - bioFuelSlot.fluidStored};
            default -> throw new NoSuchMethodException();
        };
    }

    @Override
    public int fill(EnumFacing from, @Nonnull FluidStack resource, boolean doFill) {
        int fuelNeeded = bioFuelSlot.MAX_FLUID - bioFuelSlot.fluidStored;
        int fuelTransfer = Math.min(resource.amount, fuelNeeded);
        if (doFill) {
            bioFuelSlot.setFluid(bioFuelSlot.fluidStored + fuelTransfer);
        }
        return fuelTransfer;
    }

    @Override
    public boolean canFill(EnumFacing from, @Nonnull FluidStack fluid) {
        return from != facing && fluid.getFluid() == FluidRegistry.getFluid("bioethanol");
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return PipeUtils.EMPTY;
    }

    @Override
    public void writeSustainedData(ItemStack itemStack) {
        ItemDataUtils.setInt(itemStack, "fluidStored", bioFuelSlot.fluidStored);
    }

    @Override
    public void readSustainedData(ItemStack itemStack) {
        bioFuelSlot.setFluid(ItemDataUtils.getInt(itemStack, "fluidStored"));
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        return (side != facing && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if (side != facing && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FluidHandlerWrapper(this, side));
        }
        return super.getCapability(capability, side);
    }

    @Override
    public FluidTankInfo[] getAllTanks() {
        return ALL_TANKS;
    }

    @Override
    public int getRedstoneLevel() {
        return Container.calcRedstoneFromInventory(this);
    }
}
