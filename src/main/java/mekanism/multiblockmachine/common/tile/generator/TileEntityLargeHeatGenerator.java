package mekanism.multiblockmachine.common.tile.generator;

import io.netty.buffer.ByteBuf;
import mekanism.api.Coord4D;
import mekanism.api.IHeatTransfer;
import mekanism.api.TileNetworkList;
import mekanism.common.base.*;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class TileEntityLargeHeatGenerator extends TileEntityMultiblockGenerator implements IAdvancedBoundingBlock, IFluidHandlerWrapper, ISustainedData, IHeatTransfer, IComparatorSupport {

    private static final String[] methods = new String[]{"getEnergy", "getOutput", "getMaxEnergy", "getEnergyNeeded", "getFuel", "getFuelNeeded"};

    /**
     * The FluidTank for this generator.
     */
    public FluidTank lavaTank = new FluidTank(288000);
    public double temperature = 0;
    public double thermalEfficiency = 0.5D;
    public double invHeatCapacity = 1;
    public double heatToAbsorb = 0;
    public double producingEnergy;
    public double lastTransferLoss;
    public double lastEnvironmentLoss;
    private int currentRedstoneLevel;
    public int numPowering;

    public TileEntityLargeHeatGenerator() {
        super("heat", "LargeHeatGenerator", MekanismConfig.current().multiblock.largeHeatGeneratorStorage.val(), MekanismConfig.current().multiblock.largeHeatGeneration.val());
        inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            ChargeUtils.charge(1, this);
            if (!inventory.get(0).isEmpty()) {
                if (FluidContainerUtils.isFluidContainer(inventory.get(0))) {
                    lavaTank.fill(FluidContainerUtils.extractFluid(lavaTank, this, 0, FluidContainerUtils.FluidChecker.check(FluidRegistry.LAVA)), true);
                } else {
                    int fuel = getFuel(inventory.get(0));
                    if (fuel > 0) {
                        int fuelNeeded = lavaTank.getCapacity() - (lavaTank.getFluid() != null ? lavaTank.getFluid().amount : 0);
                        if (fuel <= fuelNeeded) {
                            lavaTank.fill(new FluidStack(FluidRegistry.LAVA, fuel), true);
                            if (!inventory.get(0).getItem().getContainerItem(inventory.get(0)).isEmpty()) {
                                inventory.set(0, inventory.get(0).getItem().getContainerItem(inventory.get(0)));
                            } else {
                                inventory.get(0).shrink(1);
                            }
                        }
                    }
                }
            }
            double prev = getEnergy();
            transferHeatTo(getBoost());
            if (canOperate()) {
                setActive(true);
                lavaTank.drain(10, true);
                transferHeatTo(MekanismConfig.current().multiblock.largeHeatGeneration.val());
            } else {
                setActive(false);
            }
            double[] loss = simulateHeat();
            applyTemperatureChange();
            lastTransferLoss = loss[0];
            lastEnvironmentLoss = loss[1];
            producingEnergy = getEnergy() - prev;
            int newRedstoneLevel = getRedstoneLevel();
            if (newRedstoneLevel != currentRedstoneLevel) {
                world.updateComparatorOutputLevel(pos, getBlockType());
                currentRedstoneLevel = newRedstoneLevel;
            }
            if (MekanismUtils.canFunction(this)) {
                CableUtils.emit(this, 2);
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int slotID, @Nonnull ItemStack itemstack) {
        if (slotID == 0) {
            if (getFuel(itemstack) > 0) {
                return true;
            }
            FluidStack fluidContained = FluidUtil.getFluidContained(itemstack);
            return fluidContained != null && fluidContained.getFluid() == FluidRegistry.LAVA;
        } else if (slotID == 1) {
            return ChargeUtils.canBeCharged(itemstack);
        }
        return true;
    }

    @Override
    public boolean canOperate() {
        return electricityStored < BASE_MAX_ENERGY && lavaTank.getFluid() != null && lavaTank.getFluid().amount >= 10 && MekanismUtils.canFunction(this);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        if (nbtTags.hasKey("lavaTank")) {
            lavaTank.readFromNBT(nbtTags.getCompoundTag("lavaTank"));
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        if (lavaTank.getFluid() != null) {
            nbtTags.setTag("lavaTank", lavaTank.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        if (slotID == 1) {
            return ChargeUtils.canBeOutputted(itemstack, true);
        } else if (slotID == 0) {
            return FluidUtil.getFluidContained(itemstack) == null;
        }
        return false;
    }

    public double getBoost() {
        int lavaBoost = 0;
        double netherBoost = 0D;
        for (EnumFacing side : EnumFacing.VALUES) {
            Coord4D coord = Coord4D.get(this).offset(side, 2);
            if (isLava(coord.getPos())) {
                lavaBoost++;
            }
        }
        if (world.provider.getDimension() == -1) {
            netherBoost = MekanismConfig.current().multiblock.largeHeatGenerationNether.val();
        }
        return (MekanismConfig.current().multiblock.largeHeatGenerationLava.val() * lavaBoost) + netherBoost;
    }

    private boolean isLava(BlockPos pos) {
        return world.getBlockState(pos).getBlock() == Blocks.LAVA;
    }

    public int getFuel(ItemStack itemstack) {
        return TileEntityFurnace.getItemBurnTime(itemstack);
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return side == MekanismUtils.getRight(facing) ? new int[]{1} : new int[]{0};
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            producingEnergy = dataStream.readDouble();
            numPowering = dataStream.readInt();
            lastTransferLoss = dataStream.readDouble();
            lastEnvironmentLoss = dataStream.readDouble();
            TileUtils.readTankData(dataStream, lavaTank);
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(producingEnergy);
        data.add(numPowering);
        data.add(lastTransferLoss);
        data.add(lastEnvironmentLoss);
        TileUtils.addTankData(data, lavaTank);
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
            case 3 -> new Object[]{BASE_MAX_ENERGY - electricityStored};
            case 4 -> new Object[]{lavaTank.getFluid() != null ? lavaTank.getFluid().amount : 0};
            case 5 ->
                    new Object[]{lavaTank.getCapacity() - (lavaTank.getFluid() != null ? lavaTank.getFluid().amount : 0)};
            default -> throw new NoSuchMethodException();
        };
    }

    @Override
    public int fill(EnumFacing from, @Nonnull FluidStack resource, boolean doFill) {
        return lavaTank.fill(resource, doFill);
    }

    @Override
    public boolean canFill(EnumFacing from, @Nonnull FluidStack fluid) {
        return fluid.getFluid().equals(FluidRegistry.LAVA) && from != facing;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        if (from == facing) {
            return PipeUtils.EMPTY;
        }
        return new FluidTankInfo[]{lavaTank.getInfo()};
    }

    @Override
    public FluidTankInfo[] getAllTanks() {
        return getTankInfo(null);
    }

    @Override
    public void writeSustainedData(ItemStack itemStack) {
        if (lavaTank.getFluid() != null) {
            ItemDataUtils.setCompound(itemStack, "lavaTank", lavaTank.getFluid().writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void readSustainedData(ItemStack itemStack) {
        lavaTank.setFluid(FluidStack.loadFluidStackFromNBT(ItemDataUtils.getCompound(itemStack, "lavaTank")));
    }

    @Override
    public double getTemp() {
        return temperature;
    }

    @Override
    public double getInverseConductionCoefficient() {
        return 1;
    }

    @Override
    public double getInsulationCoefficient(EnumFacing side) {
        return canConnectHeat(side) ? 0 : 10000;
    }

    @Override
    public void transferHeatTo(double heat) {
        heatToAbsorb += heat;
    }

    @Override
    public double[] simulateHeat() {
        if (getTemp() > 0) {
            double carnotEfficiency = getTemp() / (getTemp() + IHeatTransfer.AMBIENT_TEMP);
            double heatLost = thermalEfficiency * getTemp();
            double workDone = heatLost * carnotEfficiency;
            transferHeatTo(-heatLost);
            setEnergy(getEnergy() + workDone);
        }
        return HeatUtils.simulate(this);
    }

    @Override
    public double applyTemperatureChange() {
        temperature += invHeatCapacity * heatToAbsorb;
        heatToAbsorb = 0;

        return temperature;
    }

    @Override
    public boolean canConnectHeat(EnumFacing side) {
        return side == facing;
    }


    @Override
    public IHeatTransfer getAdjacent(EnumFacing side) {
        if (canConnectHeat(side)) {
            BlockPos pos = getPos().up().offset(side, 2);
            BlockPos pos2 = getPos().up(2).offset(side, 2);
            if (world.isBlockLoaded(pos)) {
                TileEntity adj = world.getTileEntity(pos);
                if (CapabilityUtils.hasCapability(adj, Capabilities.HEAT_TRANSFER_CAPABILITY, side.getOpposite())) {
                    return CapabilityUtils.getCapability(adj, Capabilities.HEAT_TRANSFER_CAPABILITY, side.getOpposite());
                }
            }
            if (world.isBlockLoaded(pos2)) {
                TileEntity adj = world.getTileEntity(pos2);
                if (CapabilityUtils.hasCapability(adj, Capabilities.HEAT_TRANSFER_CAPABILITY, side.getOpposite())) {
                    return CapabilityUtils.getCapability(adj, Capabilities.HEAT_TRANSFER_CAPABILITY, side.getOpposite());
                }
            }
        }
        return null;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        return capability == Capabilities.HEAT_TRANSFER_CAPABILITY || (side != facing && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if (capability == Capabilities.HEAT_TRANSFER_CAPABILITY) {
            return Capabilities.HEAT_TRANSFER_CAPABILITY.cast(this);
        }
        if (side != facing && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FluidHandlerWrapper(this, side));
        }

        return super.getCapability(capability, side);
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(lavaTank.getFluidAmount(), lavaTank.getCapacity());
    }

    @Override
    public boolean canBoundReceiveEnergy(BlockPos location, EnumFacing side) {
        return false;
    }


    @Override
    public boolean canBoundOutPutEnergy(BlockPos coord, EnumFacing side) {
        if (coord.equals(getPos().offset(facing))) {
            return side == facing;
        }
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
    public boolean isPowered() {
        return redstone || numPowering > 0;
    }

    @Override
    public NBTTagCompound getConfigurationData(NBTTagCompound nbtTags) {
        return nbtTags;
    }

    @Override
    public void setConfigurationData(NBTTagCompound nbtTags) {

    }

    @Override
    public String getDataType() {
        return getBlockType().getTranslationKey() + "." + fullName + ".name";
    }

    @Override
    public void onPlace() {
        for (int y = 0; y <= 2; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(x, y, z), Coord4D.get(this));
                    world.notifyNeighborsOfStateChange(getPos().add(x, y, z), getBlockType(), true);
                }
            }
        }
    }

    @Override
    public void onBreak() {
        for (int y = 0; y <= 2; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    world.setBlockToAir(getPos().add(x, y, z));
                }
            }
        }
    }

    @Override
    public boolean renderUpdate() {
        return false;
    }

    @Override
    public boolean lightUpdate() {
        return false;
    }

    @Override
    public boolean hasOffsetCapability(@NotNull Capability<?> capability, @Nullable EnumFacing side, @NotNull Vec3i offset) {
        if (isOffsetCapabilityDisabled(capability, side, offset)) {
            return false;
        }
        if (isStrictEnergy(capability) || capability == CapabilityEnergy.ENERGY || isTesla(capability, side) || capability == Capabilities.HEAT_TRANSFER_CAPABILITY || (side != facing && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
            return true;
        }
        return hasCapability(capability, side);
    }

    @Nullable
    @Override
    public <T> T getOffsetCapability(@NotNull Capability<T> capability, @Nullable EnumFacing side, @NotNull Vec3i offset) {
        if (isOffsetCapabilityDisabled(capability, side, offset)) {
            return null;
        } else if (isStrictEnergy(capability)) {
            return (T) this;
        } else if (isTesla(capability, side)) {
            return (T) getTeslaEnergyWrapper(side);
        } else if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(getForgeEnergyWrapper(side));
        } else if (capability == Capabilities.HEAT_TRANSFER_CAPABILITY) {
            return Capabilities.HEAT_TRANSFER_CAPABILITY.cast(this);
        } else if (side != facing && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FluidHandlerWrapper(this, side));
        }
        return getCapability(capability, side);
    }

    @Override
    public boolean isOffsetCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side, @Nonnull Vec3i offset) {
        if (isStrictEnergy(capability) || capability == CapabilityEnergy.ENERGY || isTesla(capability, side)) {
            if (offset.equals(new Vec3i(facing.getXOffset(), 0, facing.getZOffset()))) {
                return side != facing;
            }
            return true;
        } else if (capability == Capabilities.HEAT_TRANSFER_CAPABILITY || (side != facing && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
            if (offset.equals(new Vec3i(facing.getXOffset(), 1, facing.getZOffset()))) {
                return side != facing;
            }
            if (offset.equals(new Vec3i(facing.getXOffset(), 2, facing.getZOffset()))) {
                return side != facing;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side) {
        if (isStrictEnergy(capability) || capability == CapabilityEnergy.ENERGY || isTesla(capability, side) || capability == Capabilities.HEAT_TRANSFER_CAPABILITY || (side != facing && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
            return true;
        }
        return super.isCapabilityDisabled(capability, side);
    }

}
