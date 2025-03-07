package mekanism.common.tile.machine;

import io.netty.buffer.ByteBuf;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.*;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.Mekanism;
import mekanism.common.MekanismFluids;
import mekanism.common.SideData;
import mekanism.common.Upgrade;
import mekanism.common.Upgrade.IUpgradeInfoHandler;
import mekanism.common.base.FluidHandlerWrapper;
import mekanism.common.base.IFluidHandlerWrapper;
import mekanism.common.base.ISustainedData;
import mekanism.common.base.ITankManager;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.FluidInput;
import mekanism.common.recipe.machines.SeparatorRecipe;
import mekanism.common.recipe.outputs.ChemicalPairOutput;
import mekanism.common.tile.TileEntityGasTank.GasMode;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.prefab.TileEntityBasicMachine;
import mekanism.common.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TileEntityElectrolyticSeparator extends TileEntityBasicMachine<FluidInput, ChemicalPairOutput, SeparatorRecipe>
        implements IFluidHandlerWrapper, ISustainedData, IGasHandler,
        IUpgradeInfoHandler, ITankManager {

    private static final String[] methods = new String[]{"getEnergy", "getOutput", "getMaxEnergy", "getEnergyNeeded", "getWater", "getWaterNeeded", "getHydrogen",
            "getHydrogenNeeded", "getOxygen", "getOxygenNeeded"};
    private final EjectSpeedController gasSpeedController = new EjectSpeedController();
    /**
     * This separator's water slot.
     */
    public FluidTank fluidTank = new FluidTankSync(24000);
    /**
     * The maximum amount of gas this block can store.
     */
    public int MAX_GAS = 2400;
    /**
     * The amount of oxygen this block is storing.
     */
    public GasTank leftTank = new GasTank(MAX_GAS);
    /**
     * The amount of hydrogen this block is storing.
     */
    public GasTank rightTank = new GasTank(MAX_GAS);
    /**
     * How fast this block can output gas.
     */
    public int output = 512;
    /**
     * The type of gas this block is outputting.
     */
    public GasMode dumpLeft = GasMode.IDLE;
    /**
     * Type type of gas this block is dumping.
     */
    public GasMode dumpRight = GasMode.IDLE;
    public SeparatorRecipe cachedRecipe;
    public double clientEnergyUsed;
    private int currentRedstoneLevel;

    public TileEntityElectrolyticSeparator() {
        super("electrolyticseparator", MachineType.ELECTROLYTIC_SEPARATOR, 4, 1);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY, TransmissionType.GAS, TransmissionType.FLUID);
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.NONE, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.INPUT, new int[]{0}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.OUTPUT_1, new int[]{1}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.OUTPUT_2, new int[]{2}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.ENERGY, new int[]{3}));
        configComponent.setConfig(TransmissionType.ITEM, new byte[]{0, 0, 1, 4, 2, 3});
        configComponent.setCanEject(TransmissionType.ITEM, false);

        configComponent.setInputConfig(TransmissionType.FLUID);

        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.NONE, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.OUTPUT_1, new int[]{1}));
        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.OUTPUT_2, new int[]{2}));
        configComponent.setConfig(TransmissionType.GAS, new byte[]{0, 0, 0, 0, 1, 2});

        configComponent.setInputConfig(TransmissionType.ENERGY);
        ejectorComponent = new TileComponentEjector(this);
        inventory = NonNullListSynchronized.withSize(5, ItemStack.EMPTY);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            ChargeUtils.discharge(3, this);
            if (!inventory.get(0).isEmpty()) {
                if (Recipe.ELECTROLYTIC_SEPARATOR.containsRecipe(inventory.get(0))) {
                    if (FluidContainerUtils.isFluidContainer(inventory.get(0))) {
                        fluidTank.fill(FluidContainerUtils.extractFluid(fluidTank, this, 0), true);
                    }
                }
            }

            if (!inventory.get(1).isEmpty() && leftTank.getStored() > 0) {
                leftTank.draw(GasUtils.addGas(inventory.get(1), leftTank.getGas()), true);
                MekanismUtils.saveChunk(this);
            }
            if (!inventory.get(2).isEmpty() && rightTank.getStored() > 0) {
                rightTank.draw(GasUtils.addGas(inventory.get(2), rightTank.getGas()), true);
                MekanismUtils.saveChunk(this);
            }
            SeparatorRecipe recipe = getRecipe();

            if (canOperate(recipe) && getEnergy() >= energyPerTick && MekanismUtils.canFunction(this)) {
                setActive(true);
                boolean update = BASE_ENERGY_PER_TICK != recipe.energyUsage;
                BASE_ENERGY_PER_TICK = recipe.energyUsage;
                if (update) {
                    recalculateUpgradables(Upgrade.ENERGY);
                }
                operatingTicks++;
                if (operatingTicks >= ticksRequired) {
                    operate(recipe);
                    operatingTicks = 0;
                }
                double prev = getEnergy();
                setEnergy(getEnergy() - energyPerTick * getUpgradedUsage(recipe));
                clientEnergyUsed = prev - getEnergy();
            } else if (prevEnergy >= getEnergy()) {
                setActive(false);
            }
            prevEnergy = getEnergy();
            int dumpAmount = 8 * Math.min((int) Math.pow(2, upgradeComponent.getUpgrades(Upgrade.SPEED)), MekanismConfig.current().mekce.MAXspeedmachines.val());
            Mekanism.EXECUTE_MANAGER.addSyncTask(() -> {
                this.gasSpeedController.ensureSize(2, () -> Arrays.asList(new TankProvider.Gas(leftTank), new TankProvider.Gas(rightTank)));
                handleTank(leftTank, dumpLeft, configComponent.getSidesForData(TransmissionType.GAS, facing, 1), dumpAmount, 0);
                handleTank(rightTank, dumpRight, configComponent.getSidesForData(TransmissionType.GAS, facing, 2), dumpAmount, 1);
                int newRedstoneLevel = getRedstoneLevel();
                if (newRedstoneLevel != currentRedstoneLevel) {
                    updateComparatorOutputLevelSync();
                    currentRedstoneLevel = newRedstoneLevel;
                }
            });
        }
    }

    private void handleTank(GasTank tank, GasMode mode, Set<EnumFacing> side, int dumpAmount, int tankidx) {
        if (tank.getGas() != null) {
            if (mode != GasMode.DUMPING) {
                if (configComponent.isEjecting(TransmissionType.GAS)) {
                    ejectGas(side, tank, this.gasSpeedController, tankidx);
                }
            } else {
                tank.draw(dumpAmount, true);
            }
            if (mode == GasMode.DUMPING_EXCESS && tank.getNeeded() < output) {
                tank.draw(output - tank.getNeeded(), true);
            }
        }
    }

    private void ejectGas(Set<EnumFacing> outputSides, GasTank tank, EjectSpeedController speedController, int tankIdx) {
        speedController.record(tankIdx);
        if (tank.getGas() == null || tank.getStored() <= 0 || tank.getGas().getGas() == null) {
            return;
        }
        if (!speedController.canEject(tankIdx)) {
            return;
        }
        GasStack toEmit = tank.getGas().copy().withAmount(Math.min(tank.getMaxGas(), tank.getStored()));
        int emitted = GasUtils.emit(toEmit, this, outputSides);
        speedController.eject(tankIdx, emitted);
        if (emitted <= 0) {
            return;
        }
        tank.draw(emitted, true);
    }

    public int getUpgradedUsage(SeparatorRecipe recipe) {
        int possibleProcess;
        if (leftTank.getGasType() == recipe.recipeOutput.leftGas.getGas()) {
            possibleProcess = leftTank.getNeeded() / recipe.recipeOutput.leftGas.amount;
            possibleProcess = Math.min(rightTank.getNeeded() / recipe.recipeOutput.rightGas.amount, possibleProcess);
        } else {
            possibleProcess = leftTank.getNeeded() / recipe.recipeOutput.rightGas.amount;
            possibleProcess = Math.min(rightTank.getNeeded() / recipe.recipeOutput.leftGas.amount, possibleProcess);
        }
        possibleProcess = Math.min(Math.min((int) Math.pow(2, upgradeComponent.getUpgrades(Upgrade.SPEED)), MekanismConfig.current().mekce.MAXspeedmachines.val()), possibleProcess);
        possibleProcess = Math.min((int) (getEnergy() / energyPerTick), possibleProcess);
        return Math.min(fluidTank.getFluidAmount() / recipe.recipeInput.ingredient.amount, possibleProcess);
    }

    public SeparatorRecipe getRecipe() {
        FluidInput input = getInput();
        if (cachedRecipe == null || !input.testEquality(cachedRecipe.getInput())) {
            cachedRecipe = RecipeHandler.getElectrolyticSeparatorRecipe(getInput());
        }
        return cachedRecipe;
    }

    public FluidInput getInput() {
        return new FluidInput(fluidTank.getFluid());
    }

    public boolean canOperate(SeparatorRecipe recipe) {
        return recipe != null && recipe.canOperate(fluidTank, leftTank, rightTank);
    }

    @Override
    public void operate(SeparatorRecipe recipe) {
        recipe.operate(fluidTank, leftTank, rightTank, getUpgradedUsage(recipe));
    }

    @Override
    public Map<FluidInput, SeparatorRecipe> getRecipes() {
        return Recipe.ELECTROLYTIC_SEPARATOR.get();
    }


    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        if (slotID == 3) {
            return ChargeUtils.canBeOutputted(itemstack, false);
        } else if (slotID == 0) {
            return FluidUtil.getFluidContained(itemstack) == null;
        } else if (slotID == 1 || slotID == 2) {
            return itemstack.getItem() instanceof IGasItem gasItem && gasItem.getGas(itemstack) != null
                    && gasItem.getGas(itemstack).amount == gasItem.getMaxGas(itemstack);
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, @Nonnull ItemStack itemstack) {
        if (slotID == 0) {
            return Recipe.ELECTROLYTIC_SEPARATOR.containsRecipe(itemstack);
        } else if (slotID == 1) {
            return itemstack.getItem() instanceof IGasItem gasItem &&
                    (gasItem.getGas(itemstack) == null || gasItem.getGas(itemstack).getGas() == MekanismFluids.Hydrogen);
        } else if (slotID == 2) {
            return itemstack.getItem() instanceof IGasItem gasItem &&
                    (gasItem.getGas(itemstack) == null || gasItem.getGas(itemstack).getGas() == MekanismFluids.Oxygen);
        } else if (slotID == 3) {
            return ChargeUtils.canBeDischarged(itemstack);
        }
        return true;
    }


    @Override
    public void handlePacketData(ByteBuf dataStream) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            byte type = dataStream.readByte();
            if (type == 0) {
                dumpLeft = GasMode.values()[dumpLeft.ordinal() == GasMode.values().length - 1 ? 0 : dumpLeft.ordinal() + 1];
            } else if (type == 1) {
                dumpRight = GasMode.values()[dumpRight.ordinal() == GasMode.values().length - 1 ? 0 : dumpRight.ordinal() + 1];
            }
            return;
        }

        super.handlePacketData(dataStream);

        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            TileUtils.readTankData(dataStream, fluidTank);
            TileUtils.readTankData(dataStream, leftTank);
            TileUtils.readTankData(dataStream, rightTank);
            dumpLeft = GasMode.values()[dataStream.readInt()];
            dumpRight = GasMode.values()[dataStream.readInt()];
            clientEnergyUsed = dataStream.readDouble();
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        TileUtils.addTankData(data, fluidTank);
        TileUtils.addTankData(data, leftTank);
        TileUtils.addTankData(data, rightTank);
        data.add(dumpLeft.ordinal());
        data.add(dumpRight.ordinal());
        data.add(clientEnergyUsed);
        return data;
    }

    public GasTank getTank(EnumFacing side) {
        if (configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(1)) {
            return leftTank;
        } else if (configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(2)) {
            return rightTank;
        }
        return null;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        if (nbtTags.hasKey("fluidTank")) {
            fluidTank.readFromNBT(nbtTags.getCompoundTag("fluidTank"));
        }
        leftTank.read(nbtTags.getCompoundTag("leftTank"));
        rightTank.read(nbtTags.getCompoundTag("rightTank"));
        dumpLeft = GasMode.values()[nbtTags.getInteger("dumpLeft")];
        dumpRight = GasMode.values()[nbtTags.getInteger("dumpRight")];
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        if (fluidTank.getFluid() != null) {
            nbtTags.setTag("fluidTank", fluidTank.writeToNBT(new NBTTagCompound()));
        }
        nbtTags.setTag("leftTank", leftTank.write(new NBTTagCompound()));
        nbtTags.setTag("rightTank", rightTank.write(new NBTTagCompound()));
        nbtTags.setInteger("dumpLeft", dumpLeft.ordinal());
        nbtTags.setInteger("dumpRight", dumpRight.ordinal());

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
            case 4 -> new Object[]{fluidTank.getFluid() != null ? fluidTank.getFluid().amount : 0};
            case 5 ->
                    new Object[]{fluidTank.getFluid() != null ? (fluidTank.getCapacity() - fluidTank.getFluid().amount) : 0};
            case 6 -> new Object[]{leftTank.getStored()};
            case 7 -> new Object[]{leftTank.getNeeded()};
            case 8 -> new Object[]{rightTank.getStored()};
            case 9 -> new Object[]{rightTank.getNeeded()};
            default -> throw new NoSuchMethodException();
        };
    }

    @Override
    public void writeSustainedData(ItemStack itemStack) {
        if (fluidTank.getFluid() != null) {
            ItemDataUtils.setCompound(itemStack, "fluidTank", fluidTank.getFluid().writeToNBT(new NBTTagCompound()));
        }
        if (leftTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "leftTank", leftTank.getGas().write(new NBTTagCompound()));
        }
        if (rightTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "rightTank", rightTank.getGas().write(new NBTTagCompound()));
        }
    }

    @Override
    public void readSustainedData(ItemStack itemStack) {
        fluidTank.setFluid(FluidStack.loadFluidStackFromNBT(ItemDataUtils.getCompound(itemStack, "fluidTank")));
        leftTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "leftTank")));
        rightTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "rightTank")));
    }

    @Override
    public boolean canFill(EnumFacing from, @Nonnull FluidStack fluid) {
        if (configComponent.getOutput(TransmissionType.FLUID, from, facing).ioState == SideData.IOState.INPUT) {
            return FluidContainerUtils.canFill(fluidTank.getFluid(), fluid) && Recipe.ELECTROLYTIC_SEPARATOR.containsRecipe(fluid.getFluid());
        }
        return false;
    }

    @Override
    public int fill(EnumFacing from, @Nonnull FluidStack resource, boolean doFill) {
        return fluidTank.fill(resource, doFill);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        if (configComponent.getOutput(TransmissionType.FLUID, from, facing).ioState != SideData.IOState.OFF) {
            return new FluidTankInfo[]{fluidTank.getInfo()};
        }
        return PipeUtils.EMPTY;
    }

    @Override
    public FluidTankInfo[] getAllTanks() {
        return getTankInfo(null);
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        return 0;
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        if (configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(1)) {
            return leftTank.draw(amount, doTransfer);
        } else if (configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(2)) {
            return rightTank.draw(amount, doTransfer);
        }
        return null;
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        return false;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        if (configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(1)) {
            return leftTank.getGas() != null && leftTank.getGas().getGas() == type;
        } else if (configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(2)) {
            return rightTank.getGas() != null && rightTank.getGas().getGas() == type;
        }
        return false;
    }

    @Nonnull
    @Override
    public GasTankInfo[] getTankInfo() {
        return new GasTankInfo[]{leftTank, rightTank};
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return false;
        }
        return capability == Capabilities.GAS_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return null;
        } else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FluidHandlerWrapper(this, side));
        }
        return super.getCapability(capability, side);
    }

    @Override
    public List<String> getInfo(Upgrade upgrade) {
        return upgrade == Upgrade.SPEED ? upgrade.getExpScaledInfo(this) : upgrade.getMultScaledInfo(this);
    }

    @Override
    public Object[] getTanks() {
        return new Object[]{fluidTank, leftTank, rightTank};
    }

    @Override
    public void recalculateUpgradables(Upgrade upgrade) {
        super.recalculateUpgradables(upgrade);
        if (upgrade == Upgrade.ENERGY) {
            maxEnergy = MekanismUtils.getMaxEnergy(this, BASE_MAX_ENERGY);
            energyPerTick = MachineType.ELECTROLYTIC_SEPARATOR.getUsage();
            setEnergy(Math.min(getMaxEnergy(), getEnergy()));
        }
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(fluidTank.getFluidAmount(), fluidTank.getCapacity());
    }

}
