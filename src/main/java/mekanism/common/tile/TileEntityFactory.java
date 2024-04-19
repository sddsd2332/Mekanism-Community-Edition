package mekanism.common.tile;

import io.netty.buffer.ByteBuf;
import mekanism.api.EnumColor;
import mekanism.api.IConfigCardAccess.ISpecialConfigData;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.*;
import mekanism.api.infuse.InfuseObject;
import mekanism.api.infuse.InfuseRegistry;
import mekanism.api.tier.BaseTier;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.*;
import mekanism.common.base.*;
import mekanism.common.base.IFactory.MachineFuelType;
import mekanism.common.base.IFactory.RecipeType;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.computer.IComputerIntegration;
import mekanism.common.item.ItemBlockMachine;
import mekanism.common.recipe.GasConversionHandler;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.inputs.*;
import mekanism.common.recipe.machines.*;
import mekanism.common.recipe.outputs.ChanceOutput;
import mekanism.common.recipe.outputs.ChanceOutput2;
import mekanism.common.recipe.outputs.ItemStackOutput;
import mekanism.common.recipe.outputs.PressurizedOutput;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityMachine;
import mekanism.common.util.*;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static mekanism.common.tile.TileEntityChemicalDissolutionChamber.BASE_INJECT_USAGE;
import static mekanism.common.tile.TileEntityChemicalWasher.WATER_USAGE;

public class TileEntityFactory extends TileEntityMachine implements IComputerIntegration, ISideConfiguration, IGasHandler, ISpecialConfigData, ITierUpgradeable,
        ISustainedData, IComparatorSupport, ITankManager, IFluidHandlerWrapper {

    private static final String[] methods = new String[]{"getEnergy", "getProgress", "facing", "canOperate", "getMaxEnergy", "getEnergyNeeded"};
    /**
     * How long it takes this factory to switch recipe types.
     */
    private static final int RECIPE_TICKS_REQUIRED = 40;
    public static int BASE_MAX_TANK = 10000;
    /**
     * The amount of infuse this machine has stored.
     */
    public final InfuseStorage infuseStored = new InfuseStorage();
    private final MachineRecipe<?, ?, ?>[] cachedRecipe;
    private final FactoryInvSorter inventorySorter = new FactoryInvSorter(this);
    public GasTank gasTank;
    public GasTank gasOutTank;
    public FluidTank fluidTank;
    /**
     * This Factory's tier.
     */
    public FactoryTier tier;
    /**
     * An int[] used to track all current operations' progress.
     */
    public int[] progress;
    public int BASE_MAX_INFUSE = 1000;
    public int maxInfuse;
    /**
     * How many ticks it takes, by default, to run an operation.
     */
    public int BASE_TICKS_REQUIRED;
    /**
     * How many ticks it takes, with upgrades, to run an operation
     */
    public int ticksRequired = 200;
    public boolean sorting;
    public boolean upgraded;
    public double lastUsage;
    public TileComponentEjector ejectorComponent;
    public TileComponentConfig configComponent;
    public boolean Factoryoldsorting;
    /**
     * This machine's recipe type.
     */

    public FluidInput waterInput = new FluidInput(new FluidStack(FluidRegistry.WATER, WATER_USAGE));
    /**
     * How much secondary energy each operation consumes per tick
     */
    private double secondaryEnergyPerTick = 0;
    private int secondaryEnergyThisTick;
    /**
     * How many recipe ticks have progressed.
     */
    private int recipeTicks;
    @Nonnull
    private RecipeType recipeType = RecipeType.SMELTING;

    public TileEntityFactory() {
        this(FactoryTier.BASIC, MachineType.BASIC_FACTORY);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY, TransmissionType.GAS, TransmissionType.FLUID);

        configComponent.addOutput(TransmissionType.ITEM, new SideData("None", EnumColor.GREY, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.ITEM, new SideData("Input", EnumColor.RED, new int[]{5, 6, 7}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData("Output", EnumColor.INDIGO, new int[]{8, 9, 10, 11, 12, 13}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData("Energy", EnumColor.BRIGHT_GREEN, new int[]{1}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData("Extra", EnumColor.ORANGE, new int[]{4}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData("Input_Extra", EnumColor.ORANGE, new int[]{4, 5, 6, 7}));
        configComponent.setConfig(TransmissionType.ITEM, new byte[]{4, 1, 1, 3, 1, 2});

        configComponent.setInputConfig(TransmissionType.FLUID);

        configComponent.addOutput(TransmissionType.GAS, new SideData("None", EnumColor.GREY, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.GAS, new SideData("Input", EnumColor.RED, new int[]{1}));
        configComponent.addOutput(TransmissionType.GAS, new SideData("Output", EnumColor.INDIGO, new int[]{2}));
        configComponent.setConfig(TransmissionType.GAS, new byte[]{1, 1, 1, 1, 1, 2});

        configComponent.setInputConfig(TransmissionType.ENERGY);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(TransmissionType.ITEM, configComponent.getOutputs(TransmissionType.ITEM).get(2));
        ejectorComponent.setOutputData(TransmissionType.GAS, configComponent.getOutputs(TransmissionType.GAS).get(2));
    }

    public TileEntityFactory(FactoryTier type, MachineType machine) {
        super("null", machine, 0);
        tier = type;
        inventory = NonNullList.withSize(5 + type.processes * 3, ItemStack.EMPTY);
        progress = new int[type.processes];
        isActive = false;
        cachedRecipe = new MachineRecipe[tier.processes];
        gasTank = new GasTank(tier == FactoryTier.CREATIVE ? Integer.MAX_VALUE : BASE_MAX_TANK * tier.processes);
        gasOutTank = new GasTank(tier == FactoryTier.CREATIVE ? Integer.MAX_VALUE : BASE_MAX_TANK * tier.processes);
        fluidTank = new FluidTank(tier == FactoryTier.CREATIVE ? Integer.MAX_VALUE : BASE_MAX_TANK * tier.processes);
        maxInfuse = tier != FactoryTier.CREATIVE ? BASE_MAX_INFUSE * tier.processes : Integer.MAX_VALUE;
        BASE_TICKS_REQUIRED = tier != FactoryTier.CREATIVE ? 200 : 1;
        if (tier == FactoryTier.CREATIVE) {
            maxEnergy = Double.MAX_VALUE;
        }
        setRecipeType(recipeType);
    }

    public static ItemStack getRecipeInput(MachineRecipe<?, ?, ?> recipe) {
        if (recipe.recipeInput instanceof ItemStackInput) {
            return ((ItemStackInput) recipe.recipeInput).ingredient;
        } else if (recipe.recipeInput instanceof AdvancedMachineInput advancedInput) {
            return advancedInput.itemStack;
        } else if (recipe.recipeInput instanceof DoubleMachineInput doubleMachineInput) {
            return doubleMachineInput.itemStack;
        } else if (recipe.recipeInput instanceof InfusionInput infusionInput) {
            return infusionInput.inputStack;
        } else if (recipe.recipeInput instanceof PressurizedInput pressurizedInput) {
            return pressurizedInput.getSolid();
        } else if (recipe.recipeInput instanceof NucleosynthesizerInput input) {
            return input.getSolid();
        } else {
            return ItemStack.EMPTY;
        }
    }

    private static int[] getSlotsWithTier(FactoryTier tier) {
        return switch (tier) {
            case BASIC -> new int[]{5, 6, 7};
            case ADVANCED -> new int[]{5, 6, 7, 8, 9};
            case ELITE -> new int[]{5, 6, 7, 8, 9, 10, 11};
            case ULTIMATE -> new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13};
            case CREATIVE -> new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        };
    }

    public static ItemStack copyStackWithSize(ItemStack stack, int amount) {
        if (stack.isEmpty() || amount <= 0) return ItemStack.EMPTY;
        ItemStack s = stack.copy();
        s.setCount(amount);
        return s;
    }


    public static boolean matchStacks(@Nonnull ItemStack stack, @Nonnull ItemStack other) {
        if (!ItemStack.areItemsEqual(stack, other)) return false;
        return ItemStack.areItemStackTagsEqual(stack, other);
    }

    @Override
    public boolean upgrade(BaseTier upgradeTier) {
        if (tier == FactoryTier.ELITE || tier == FactoryTier.ULTIMATE) {
            if (upgradeTier.ordinal() != tier.ordinal() + 1) {
                return false;
            }
            world.setBlockToAir(getPos());
            world.setBlockState(getPos(), MekanismBlocks.MachineBlock3.getStateFromMeta(4 + tier.ordinal() + 1), 3);
        } else if (tier == FactoryTier.BASIC || tier == FactoryTier.ADVANCED) {
            if (upgradeTier.ordinal() != tier.ordinal() + 1) {
                return false;
            }
            world.setBlockToAir(getPos());
            world.setBlockState(getPos(), MekanismBlocks.MachineBlock.getStateFromMeta(5 + tier.ordinal() + 1), 3);
        } else return false;

        TileEntityFactory factory = Objects.requireNonNull((TileEntityFactory) world.getTileEntity(getPos()));

        //Basic
        factory.facing = facing;
        factory.clientFacing = clientFacing;
        factory.ticker = ticker;
        factory.redstone = redstone;
        factory.redstoneLastTick = redstoneLastTick;
        factory.doAutoSync = doAutoSync;

        //Electric
        factory.electricityStored = electricityStored;

        //Factory
        System.arraycopy(progress, 0, factory.progress, 0, tier.processes);

        factory.recipeTicks = recipeTicks;
        factory.isActive = isActive;
        factory.prevEnergy = prevEnergy;
        factory.gasTank.setGas(gasTank.getGas());
        factory.gasOutTank.setGas(gasOutTank.getGas());
        factory.fluidTank.setFluid(fluidTank.getFluid());
        factory.sorting = sorting;
        factory.Factoryoldsorting = Factoryoldsorting;
        factory.setControlType(getControlType());
        factory.upgradeComponent.readFrom(upgradeComponent);
        factory.ejectorComponent.readFrom(ejectorComponent);
        factory.configComponent.readFrom(configComponent);
        factory.ejectorComponent.setOutputData(TransmissionType.ITEM, factory.configComponent.getOutputs(TransmissionType.ITEM).get(2));
        factory.setRecipeType(recipeType);
        factory.upgradeComponent.setSupported(Upgrade.GAS, recipeType.fuelEnergyUpgrades());
        factory.securityComponent.readFrom(securityComponent);
        factory.infuseStored.copyFrom(infuseStored);

        for (int i = 0; i < tier.processes + 5; i++) {
            factory.inventory.set(i, inventory.get(i));
        }

        for (int i = 0; i < tier.processes; i++) {
            int output = getOutputSlot(i);
            if (!inventory.get(output).isEmpty()) {
                int newOutput = 5 + factory.tier.processes + i;
                factory.inventory.set(newOutput, inventory.get(output));
            }
        }

        for (int i = 0; i < tier.processes; i++) {
            int SecondaryOutput = getSecondaryOutputSlot(i);
            if (!inventory.get(SecondaryOutput).isEmpty()) {
                int newSecondaryOutput = 5 + tier.processes * 2 + i;
                factory.inventory.set(newSecondaryOutput, inventory.get(SecondaryOutput));
            }
        }

        for (Upgrade upgrade : factory.upgradeComponent.getSupportedTypes()) {
            factory.recalculateUpgradables(upgrade);
        }

        factory.upgraded = true;
        factory.markDirty();
        Mekanism.packetHandler.sendUpdatePacket(factory);
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            if (ticker == 1) {
                world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
            }
            if (MekanismConfig.current().mekce.EnableUpgradeConfigure.val()) {
                MekanismUtils.inject.accept(ticksRequired, this::onUpdate);
            }
            ChargeUtils.discharge(1, this);
            handleSecondaryFuel();
            if (!NoItemInputMachine()) {
                if (Factoryoldsorting) {
                    sortInventory(); //Keeping the old sort prevents some problems
                } else {
                    inventorySorter.sort();
                }
            }

            MachineTypeSwitching();
            double prev = getEnergy();
            if (tier == FactoryTier.CREATIVE) {
                energyPerTick = 0;
                electricityStored = Double.MAX_VALUE;
            }

            if (recipeType == RecipeType.Dissolution) {
                secondaryEnergyThisTick = Math.max(BASE_INJECT_USAGE * tier.processes, StatUtils.inversePoisson(BASE_INJECT_USAGE * tier.processes));
            } else {
                secondaryEnergyThisTick = recipeType.fuelEnergyUpgrades() ? StatUtils.inversePoisson(secondaryEnergyPerTick) : (int) Math.ceil(secondaryEnergyPerTick);
            }

            for (int process = 0; process < tier.processes; process++) {
                PressurizedRecipe PRCrecipe = recipeType.getPressurizedRecipe(inventory.get(getInputSlot(process)), fluidTank.getFluid(), gasTank.getGas());
                NucleosynthesizerRecipe NnRecipe = recipeType.getNucleosynthesizerRecipe(inventory.get(getInputSlot(process)), gasTank.getGas());
                double Exenery = 0;
                if (MekanismUtils.canFunction(this) && canOperate(getInputSlot(process), getOutputSlot(process), getSecondaryOutputSlot(process)) && getEnergy() >= energyPerTick && gasTank.getStored() >= secondaryEnergyThisTick) {

                    if (tier != FactoryTier.CREATIVE) {
                        if (recipeType == RecipeType.PRC || recipeType == RecipeType.NUCLEOSYNTHESIZER) {
                            Exenery = recipeType == RecipeType.PRC ? PRCrecipe.extraEnergy : NnRecipe.extraEnergy;
                            boolean update = BASE_TICKS_REQUIRED != (recipeType == RecipeType.PRC ? PRCrecipe.ticks : NnRecipe.ticks);
                            BASE_TICKS_REQUIRED = recipeType == RecipeType.PRC ? PRCrecipe.ticks : NnRecipe.ticks;
                            if (update) {
                                recalculateUpgradables(Upgrade.SPEED);
                            }
                        }
                        if (recipeType == RecipeType.WASHER) {
                            BASE_TICKS_REQUIRED = 1;
                        }
                    }

                    if ((progress[process] + 1) < ticksRequired) {
                        progress[process]++;
                        gasTank.draw(secondaryEnergyThisTick, tier != FactoryTier.CREATIVE);
                        if (tier != FactoryTier.CREATIVE) {
                            if (recipeType == RecipeType.PRC || recipeType == RecipeType.NUCLEOSYNTHESIZER) {
                                electricityStored -= MekanismUtils.getEnergyPerTick(this, BASE_ENERGY_PER_TICK + Exenery);
                            } else {
                                electricityStored -= energyPerTick;
                            }
                        } else {
                            inventory.get(getOutputSlot(process)).setCount(inventory.get(getOutputSlot(process)).getMaxStackSize());
                            if (recipeType.getFuelType() == MachineFuelType.FARM || recipeType.getFuelType() == MachineFuelType.CHANCE) {
                                inventory.get(getSecondaryOutputSlot(process)).setCount(inventory.get(getSecondaryOutputSlot(process)).getMaxStackSize());
                            }
                        }
                    } else if ((progress[process] + 1) >= ticksRequired && ((recipeType == RecipeType.PRC || recipeType == RecipeType.NUCLEOSYNTHESIZER) ? getEnergy() >= MekanismUtils.getEnergyPerTick(this, BASE_ENERGY_PER_TICK + Exenery) : getEnergy() >= energyPerTick)) {
                        operate(getInputSlot(process), getOutputSlot(process), getSecondaryOutputSlot(process));
                        progress[process] = 0;
                        gasTank.draw(secondaryEnergyThisTick, tier != FactoryTier.CREATIVE);
                        if (tier != FactoryTier.CREATIVE) {
                            if (recipeType == RecipeType.PRC || recipeType == RecipeType.NUCLEOSYNTHESIZER) {
                                electricityStored -= MekanismUtils.getEnergyPerTick(this, BASE_ENERGY_PER_TICK + Exenery);
                            } else {
                                electricityStored -= energyPerTick;
                            }
                        } else {
                            inventory.get(getOutputSlot(process)).setCount(inventory.get(getOutputSlot(process)).getMaxStackSize());
                            if (recipeType.getFuelType() == MachineFuelType.FARM || recipeType.getFuelType() == MachineFuelType.CHANCE) {
                                inventory.get(getSecondaryOutputSlot(process)).setCount(inventory.get(getSecondaryOutputSlot(process)).getMaxStackSize());
                            }
                        }
                    }
                } else {
                    if (tier != FactoryTier.CREATIVE) {
                        if (recipeType == RecipeType.PRC || recipeType == RecipeType.NUCLEOSYNTHESIZER) {
                            BASE_TICKS_REQUIRED = 200;
                        }
                    }
                }

                if (!canOperate(getInputSlot(process), getOutputSlot(process), getSecondaryOutputSlot(process))) {
                    if (!(GasAdvancedInputMachine() && recipeType.hasRecipe(inventory.get(getInputSlot(process))))) {
                        progress[process] = 0;
                    }
                }
            }

            boolean hasOperation = false;

            for (int i = 0; i < tier.processes; i++) {
                if (canOperate(getInputSlot(i), getOutputSlot(i), getSecondaryOutputSlot(i))) {
                    hasOperation = true;
                    break;
                }
            }

            if (MekanismUtils.canFunction(this) && hasOperation && getEnergy() >= energyPerTick && gasTank.getStored() >= secondaryEnergyThisTick) {
                setActive(true);
            } else if (prevEnergy >= getEnergy()) {
                setActive(false);
            }
            lastUsage = prev - getEnergy();
            prevEnergy = getEnergy();
        }
    }


    private void MachineTypeSwitching() {
        ItemStack machineSwapItem = inventory.get(2);
        if (!machineSwapItem.isEmpty() && machineSwapItem.getItem() instanceof ItemBlockMachine && inventory.get(3).isEmpty()) {
            MachineType swapType = MachineType.get(machineSwapItem);
            if (swapType != null && !swapType.isFactory()) {
                RecipeType toSet = RecipeType.getFromMachineType(swapType);
                if (toSet != null && recipeType != toSet) {
                    if (recipeTicks < RECIPE_TICKS_REQUIRED) {
                        recipeTicks++;
                    } else {
                        recipeTicks = 0;
                        ItemStack returnStack = getMachineStack();

                        upgradeComponent.write(ItemDataUtils.getDataMap(returnStack));
                        upgradeComponent.setSupported(Upgrade.GAS, toSet.fuelEnergyUpgrades());
                        upgradeComponent.read(ItemDataUtils.getDataMapIfPresentNN(machineSwapItem));

                        inventory.set(2, ItemStack.EMPTY);
                        inventory.set(3, returnStack);

                        setRecipeType(toSet);
                        gasTank.setGas(null);
                        gasOutTank.setGas(null);
                        fluidTank.setFluid(null);
                        secondaryEnergyPerTick = getSecondaryEnergyPerTick(recipeType);
                        world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
                        MekanismUtils.saveChunk(this);
                    }
                } else {
                    recipeTicks = 0;
                }
            }
        } else {
            recipeTicks = 0;
        }
    }

    @Nonnull
    public RecipeType getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(@Nonnull RecipeType type) {
        recipeType = Objects.requireNonNull(type);
        BASE_MAX_ENERGY = maxEnergy = tier == FactoryTier.CREATIVE ? Double.MAX_VALUE : tier.processes * Math.max(((recipeType == RecipeType.PRC || recipeType == RecipeType.NUCLEOSYNTHESIZER) ? 1D : 0.5D) * recipeType.getEnergyStorage(), recipeType.getEnergyUsage());
        BASE_ENERGY_PER_TICK = energyPerTick = tier == FactoryTier.CREATIVE ? 0 : recipeType.getEnergyUsage();
        upgradeComponent.setSupported(Upgrade.GAS, recipeType.fuelEnergyUpgrades());
        secondaryEnergyPerTick = getSecondaryEnergyPerTick(recipeType);

        for (Upgrade upgrade : upgradeComponent.getSupportedTypes()) {
            recalculateUpgradables(upgrade);
        }
        if (hasWorld() && world.isRemote) {
            setSoundEvent(type.getSound());
        }
    }

    @Override
    public boolean sideIsConsumer(EnumFacing side) {
        return configComponent.hasSideForData(TransmissionType.ENERGY, facing, 1, side);
    }

    /**
     * If the factory does not have an input machine
     */
    //TODO
    public boolean NoItemInputMachine() {
        return recipeType == RecipeType.Crystallizer || recipeType == RecipeType.WASHER;
    }

    public boolean GasOutputMachine() {
        return recipeType == RecipeType.Dissolution || recipeType == RecipeType.OXIDIZER || recipeType == RecipeType.WASHER;
    }

    public boolean GasInputMachine() {
        return recipeType == RecipeType.Dissolution || recipeType == RecipeType.Crystallizer || recipeType == RecipeType.PRC || recipeType == RecipeType.WASHER;
    }

    public boolean GasAdvancedInputMachine() {
        return recipeType.getFuelType() == MachineFuelType.FARM || recipeType.getFuelType() == MachineFuelType.ADVANCED || recipeType == RecipeType.NUCLEOSYNTHESIZER;
    }

    public boolean GasOutputMachines() {
        return recipeType == RecipeType.Dissolution || recipeType == RecipeType.OXIDIZER || recipeType == RecipeType.WASHER || recipeType == RecipeType.PRC;
    }

    public boolean GasMachine() {
        return recipeType == RecipeType.OXIDIZER || recipeType == RecipeType.Dissolution || recipeType == RecipeType.Crystallizer || recipeType == RecipeType.PRC || recipeType == RecipeType.WASHER || recipeType.getFuelType() == MachineFuelType.FARM || recipeType.getFuelType() == MachineFuelType.ADVANCED || recipeType == RecipeType.NUCLEOSYNTHESIZER;
    }

    public boolean inputFluidMachine() {
        return recipeType == RecipeType.PRC || recipeType == RecipeType.WASHER;
    }

    /**
     * Checks if the cached recipe (or recipe for current factory if the cache is out of date) can produce a specific output.
     *
     * @param slotID        Slot ID to grab the cached recipe of.
     * @param fallbackInput Used if the cached recipe is null or to validate the cached recipe is not out of date.
     * @param output        The output we want.
     * @param updateCache   True to make the cached recipe get updated if it is out of date.
     * @return True if the recipe produces the given output.
     */
    public boolean inputProducesOutput(int slotID, ItemStack fallbackInput, ItemStack output, boolean updateCache) {
        if (output.isEmpty()) {
            return true;
        }
        int process = getOperation(slotID);
        //cached recipe may be invalid
        MachineRecipe<?, ?, ?> cached = cachedRecipe[process];
        ItemStack extra = inventory.get(4);
        if (cached == null) {
            cached = recipeType.getAnyRecipe(fallbackInput, extra, gasTank.getGasType(), infuseStored, gasTank.getGas(), fluidTank.getFluid());
            if (updateCache) {
                cachedRecipe[process] = cached;
            }
        } else {
            ItemStack recipeInput = ItemStack.EMPTY;
            boolean secondaryMatch = true;
            if (cached.recipeInput instanceof ItemStackInput) {
                recipeInput = ((ItemStackInput) cached.recipeInput).ingredient;
            } else if (cached.recipeInput instanceof AdvancedMachineInput advancedInput) {
                recipeInput = advancedInput.itemStack;
                secondaryMatch = gasTank.getGasType() == null || advancedInput.gasType == gasTank.getGasType();
            } else if (cached.recipeInput instanceof DoubleMachineInput doubleMachineInput) {
                recipeInput = doubleMachineInput.itemStack;
                secondaryMatch = extra.isEmpty() || ItemStack.areItemsEqual(doubleMachineInput.extraStack, extra);
            } else if (cached.recipeInput instanceof PressurizedInput pressurizedInput) {
                recipeInput = pressurizedInput.getSolid();
                secondaryMatch = (gasTank.getGas() == null || gasTank.getGas().isGasEqual(pressurizedInput.getGas())) && (fluidTank.getFluid() == null || fluidTank.getFluid() == pressurizedInput.getFluid());
            } else if (cached.recipeInput instanceof InfusionInput infusionInput) {
                recipeInput = infusionInput.inputStack;
                secondaryMatch = infuseStored.getAmount() == 0 || infuseStored.getType() == infusionInput.infuse.getType();
            } else if (cached.recipeInput instanceof GasInput gasInput) {
                secondaryMatch = gasTank.getGasType() == null || gasInput.ingredient.getGas() == gasTank.getGasType();
            } else if (cached.recipeInput instanceof NucleosynthesizerInput input) {
                recipeInput = input.getSolid();
                secondaryMatch = (gasTank.getGas() == null || gasTank.getGas().isGasEqual(input.getGas()));
            }
            //If there is no cached item input or it doesn't match our fallback
            // then it is an out of date cache so we compare against the new one
            // and update the cache while we are at it
            if (recipeInput.isEmpty() || !secondaryMatch || !ItemStack.areItemsEqual(recipeInput, fallbackInput)) {
                cached = recipeType.getAnyRecipe(fallbackInput, extra, gasTank.getGasType(), infuseStored, gasTank.getGas(), fluidTank.getFluid());
                if (updateCache) {
                    cachedRecipe[process] = cached;
                }
            }
        }
        //If there is no recipe found
        if (cached != null) {
            ItemStack recipeOutput = ItemStack.EMPTY;
            if (cached.recipeOutput instanceof ItemStackOutput) {
                recipeOutput = ((ItemStackOutput) cached.recipeOutput).output;
            } else if (cached.recipeOutput instanceof ChanceOutput2) {
                recipeOutput = ((ChanceOutput2) cached.recipeOutput).primaryOutput;
            } else if (cached.recipeOutput instanceof PressurizedOutput) {
                recipeOutput = ((PressurizedOutput) cached.recipeOutput).getItemOutput();
            }
            if (!recipeOutput.isEmpty()) {
                return ItemStack.areItemsEqual(recipeOutput, output);
            }
        }
        return true;
    }

    public double getSecondaryEnergyPerTick(RecipeType type) {
        if (tier == FactoryTier.CREATIVE) {
            return 0;
        } else {
            if (recipeType.getFuelType() == MachineFuelType.FARM || recipeType.getFuelType() == MachineFuelType.ADVANCED) {
                return MekanismUtils.getSecondaryEnergyPerTickMean(this, type.getSecondaryEnergyPerTick());
            } else if (recipeType == RecipeType.Dissolution) {
                return MekanismUtils.getSecondaryEnergyPerTickMean(this, 1);
            } else {
                return 0;
            }
        }
    }

    @Nullable
    public GasStack getItemGas(ItemStack itemStack) {
        return GasConversionHandler.getItemGas(itemStack, gasTank, recipeType::isValidGas);
    }

    public void handleSecondaryFuel() {
        ItemStack extra = inventory.get(4);
        if (!extra.isEmpty()) {
            if ((GasInputMachine() || GasAdvancedInputMachine()) && gasTank.getNeeded() > 0) {
                GasStack gasStack = getItemGas(extra);
                if (gasStack != null) {
                    Gas gas = gasStack.getGas();
                    if (gasTank.canReceive(gas) && gasTank.getNeeded() >= gasStack.amount) {
                        if (extra.getItem() instanceof IGasItem item) {
                            if (tier != FactoryTier.CREATIVE) {
                                gasTank.receive(item.removeGas(extra, gasStack.amount), true);
                            } else {
                                gasTank.setGas(gasStack);
                                gasTank.setMaxGas(Integer.MAX_VALUE);
                                gasTank.stored.amount = gasTank.getMaxGas();
                            }
                        } else {
                            if (tier != FactoryTier.CREATIVE) {
                                gasTank.receive(gasStack, true);
                                extra.shrink(1);
                            } else {
                                gasTank.setGas(gasStack);
                                gasTank.setMaxGas(Integer.MAX_VALUE);
                                gasTank.stored.amount = gasTank.getMaxGas();

                            }
                        }
                    }
                }
            } else if (recipeType == RecipeType.INFUSING) {
                InfuseObject pendingInfusionInput = InfuseRegistry.getObject(extra);
                if (pendingInfusionInput != null) {
                    if (infuseStored.getType() == null || infuseStored.getType() == pendingInfusionInput.type) {
                        if (infuseStored.getAmount() + pendingInfusionInput.stored <= maxInfuse) {
                            if (tier != FactoryTier.CREATIVE) {
                                infuseStored.increase(pendingInfusionInput);
                                extra.shrink(1);
                            } else {
                                infuseStored.setType(pendingInfusionInput.type);
                                infuseStored.setAmount(maxInfuse);
                            }
                        }
                    }
                }
            }
        }
        if ((GasOutputMachine() || recipeType == RecipeType.PRC) && gasOutTank.getNeeded() >= 0 && tier == FactoryTier.CREATIVE) {
            if (gasOutTank.stored != null) {
                gasOutTank.setGas(gasOutTank.getGas());
                gasOutTank.setMaxGas(Integer.MAX_VALUE);
                gasOutTank.stored.amount = gasTank.getMaxGas();
            }
        }
        if ((recipeType == RecipeType.WASHER || recipeType == RecipeType.PRC) && tier == FactoryTier.CREATIVE) {
            if (fluidTank.getFluid() != null) {
                fluidTank.setFluid(fluidTank.getFluid());
                fluidTank.setCapacity(Integer.MAX_VALUE);
                fluidTank.getFluid().amount = fluidTank.getCapacity();
            }
        }

    }

    public ItemStack getMachineStack() {
        return recipeType.getStack();
    }

    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        if (slotID == 1) {
            return ChargeUtils.canBeOutputted(itemstack, false);
        } else if (tier == FactoryTier.BASIC && isOutputSlot(slotID)) {
            return true;
        } else if (tier == FactoryTier.ADVANCED && isOutputSlot(slotID)) {
            return true;
        } else if (tier == FactoryTier.ELITE && isOutputSlot(slotID)) {
            return true;
        } else if (tier == FactoryTier.ULTIMATE && isOutputSlot(slotID)) {
            return true;
        } else if (tier == FactoryTier.CREATIVE && isOutputSlot(slotID)) {
            return true;
        } else
            return (recipeType.getFuelType() == MachineFuelType.CHANCE || recipeType.getFuelType() == MachineFuelType.FARM) && isSecondaryOutputSlot(slotID);
    }

    @Override
    public boolean canInsertItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        if (slotID == 1) {
            return ChargeUtils.canBeDischarged(itemstack);
        } else if (isInputSlot(slotID)) {
            return inputProducesOutput(slotID, itemstack, inventory.get(tier.processes + slotID), false);
        }
        //TODO: Only allow inserting into extra slot if it can go in
        return super.canInsertItem(slotID, itemstack, side);
    }

    private boolean isInputSlot(int slotID) {
        return slotID >= 5 && (tier == FactoryTier.BASIC ? slotID <= 7 : tier == FactoryTier.ADVANCED ? slotID <= 9 : tier == FactoryTier.ELITE ? slotID <= 11 : tier == FactoryTier.ULTIMATE ? slotID <= 13 : tier == FactoryTier.CREATIVE && slotID <= 15);
    }

    private boolean isOutputSlot(int slotID) {
        int slotIDOutput = tier == FactoryTier.BASIC ? 8 : tier == FactoryTier.ADVANCED ? 10 : tier == FactoryTier.ELITE ? 12 : tier == FactoryTier.ULTIMATE ? 14 : 16;
        return slotID >= slotIDOutput && slotID <= slotIDOutput + tier.processes - 1;
    }

    private boolean isSecondaryOutputSlot(int slotID) {
        int slotIDOutput = tier == FactoryTier.BASIC ? 11 : tier == FactoryTier.ADVANCED ? 15 : tier == FactoryTier.ELITE ? 19 : tier == FactoryTier.ULTIMATE ? 23 : 27;
        return slotID >= slotIDOutput && slotID <= slotIDOutput + tier.processes * 2 - 1;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, @Nonnull ItemStack itemstack) {
        if (isOutputSlot(slotID)) {
            return false;
        } else if (isSecondaryOutputSlot(slotID)) {
            return false;
        } else if (isInputSlot(slotID)) {
            return recipeType.getAnyRecipe(itemstack, inventory.get(4), gasTank.getGasType(), infuseStored, gasTank.getGas(), fluidTank.getFluid()) != null;
        }

        if (slotID == 0) {
            return itemstack.getItem() == MekanismItems.SpeedUpgrade || itemstack.getItem() == MekanismItems.EnergyUpgrade;
        } else if (slotID == 1) {
            return ChargeUtils.canBeDischarged(itemstack);
        } else if (slotID == 4) {
            if (GasInputMachine() || GasAdvancedInputMachine()) {
                return getItemGas(itemstack) != null;
            } else if (recipeType.getFuelType() == MachineFuelType.DOUBLE) {
                return recipeType.hasRecipeForExtra(itemstack);
            } else if (recipeType == RecipeType.INFUSING) {
                return InfuseRegistry.getObject(itemstack) != null && (infuseStored.getType() == null || infuseStored.getType() == InfuseRegistry.getObject(itemstack).type);
            }
        }
        return false;
    }

    public double getScaledProgress(int process) {
        if (recipeType == RecipeType.PRC || recipeType == RecipeType.NUCLEOSYNTHESIZER) {
            PressurizedRecipe PRCrecipe = recipeType.getPressurizedRecipe(inventory.get(getInputSlot(process)), fluidTank.getFluid(), gasTank.getGas());
            NucleosynthesizerRecipe NnRecipe = recipeType.getNucleosynthesizerRecipe(inventory.get(getInputSlot(process)), gasTank.getGas());
            if (PRCrecipe != null && recipeType == RecipeType.PRC) {
                boolean update = BASE_TICKS_REQUIRED != PRCrecipe.ticks;
                BASE_TICKS_REQUIRED = PRCrecipe.ticks;
                if (update) {
                    recalculateUpgradables(Upgrade.SPEED);
                }
            } else if (NnRecipe != null && recipeType == RecipeType.NUCLEOSYNTHESIZER) {
                boolean update = BASE_TICKS_REQUIRED != NnRecipe.ticks;
                BASE_TICKS_REQUIRED = NnRecipe.ticks;
                if (update) {
                    recalculateUpgradables(Upgrade.SPEED);
                }
            }
            return Math.min((double) progress[process] / ticksRequired, 1F);
        } else if (recipeType == RecipeType.WASHER) {
            return getActive() ? 1 : 0;
        }
        return (double) progress[process] / ticksRequired;
    }

    public double getScaledInfuseLevel(int i) {
        return (double) infuseStored.getAmount() * i / maxInfuse;
    }

    public double getScaledGasLevel(int i) {
        return (double) gasTank.getStored() * i / gasTank.getMaxGas();
    }

    public double getScaledGasOutlevel(int i) {
        return (double) gasOutTank.getStored() * i / gasOutTank.getMaxGas();
    }

    public double getScaledfluidTanklevel(int i) {
        return (double) fluidTank.getFluidAmount() * i / fluidTank.getCapacity();
    }

    public int getScaledRecipeProgress(int i) {
        return recipeTicks * i / RECIPE_TICKS_REQUIRED;
    }

    public boolean canOperate(int inputSlot, int outputSlot, int SecondaryOutputSlot) {
        if (inventory.get(inputSlot).isEmpty() && !NoItemInputMachine()) {
            return false;
        }

        int process = getOperation(inputSlot);

        if (recipeType.getFuelType() == MachineFuelType.ADVANCED) {
            if (cachedRecipe[process] instanceof AdvancedMachineRecipe &&
                    ((AdvancedMachineRecipe<?>) cachedRecipe[process]).inputMatches(inventory, inputSlot, gasTank, secondaryEnergyThisTick)) {
                return ((AdvancedMachineRecipe<?>) cachedRecipe[process]).canOperate(inventory, inputSlot, outputSlot, gasTank, secondaryEnergyThisTick);
            }
            AdvancedMachineRecipe<?> recipe = recipeType.getRecipe(inventory.get(inputSlot), gasTank.getGasType());
            cachedRecipe[process] = recipe;
            return recipe != null && recipe.canOperate(inventory, inputSlot, outputSlot, gasTank, secondaryEnergyThisTick);
        } else if (recipeType.getFuelType() == MachineFuelType.DOUBLE) {
            if (cachedRecipe[process] instanceof DoubleMachineRecipe && ((DoubleMachineRecipe<?>) cachedRecipe[process]).inputMatches(inventory, inputSlot, 4)) {
                return ((DoubleMachineRecipe<?>) cachedRecipe[process]).canOperate(inventory, inputSlot, 4, outputSlot);
            }
            DoubleMachineRecipe<?> recipe = recipeType.getRecipe(inventory.get(inputSlot), inventory.get(4));
            cachedRecipe[process] = recipe;
            return recipe != null && recipe.canOperate(inventory, inputSlot, 4, outputSlot);
        } else if (recipeType.getFuelType() == MachineFuelType.CHANCE) {
            if (cachedRecipe[process] instanceof ChanceMachineRecipe && ((ChanceMachineRecipe<?>) cachedRecipe[process]).inputMatches(inventory, inputSlot)) {
                return ((ChanceMachineRecipe<?>) cachedRecipe[process]).canOperate(inventory, inputSlot, outputSlot, SecondaryOutputSlot);
            }
            ChanceMachineRecipe<?> recipe = recipeType.getChanceRecipe(inventory.get(inputSlot));
            cachedRecipe[process] = recipe;
            return recipe != null && recipe.canOperate(inventory, inputSlot, outputSlot, SecondaryOutputSlot);
        } else if (recipeType.getFuelType() == MachineFuelType.FARM) {
            if (cachedRecipe[process] instanceof FarmMachineRecipe && ((FarmMachineRecipe<?>) cachedRecipe[process]).inputMatches(inventory, inputSlot, gasTank, secondaryEnergyThisTick)) {
                return ((FarmMachineRecipe<?>) cachedRecipe[process]).canOperate(inventory, inputSlot, gasTank, secondaryEnergyThisTick, outputSlot, SecondaryOutputSlot);
            }
            FarmMachineRecipe<?> recipe = recipeType.getFarmRecipe(inventory.get(inputSlot), gasTank.getGasType());
            cachedRecipe[process] = recipe;
            return recipe != null && recipe.canOperate(inventory, inputSlot, gasTank, secondaryEnergyThisTick, outputSlot, SecondaryOutputSlot);
        } else if (recipeType.getFuelType() == MachineFuelType.CHANCE2) {
            if (cachedRecipe[process] instanceof Chance2MachineRecipe && ((Chance2MachineRecipe<?>) cachedRecipe[process]).inputMatches(inventory, inputSlot)) {
                return ((Chance2MachineRecipe<?>) cachedRecipe[process]).canOperate(inventory, inputSlot, outputSlot);
            }
            Chance2MachineRecipe<?> recipe = recipeType.getChance2Recipe(inventory.get(inputSlot));
            cachedRecipe[process] = recipe;
            return recipe != null && recipe.canOperate(inventory, inputSlot, outputSlot);
        }

        if (recipeType == RecipeType.INFUSING) {
            if (cachedRecipe[process] instanceof MetallurgicInfuserRecipe && ((MetallurgicInfuserRecipe) cachedRecipe[process]).inputMatches(inventory, inputSlot, infuseStored)) {
                return ((MetallurgicInfuserRecipe) cachedRecipe[process]).canOperate(inventory, inputSlot, outputSlot, infuseStored);
            }
            InfusionInput input = new InfusionInput(infuseStored, inventory.get(inputSlot));
            MetallurgicInfuserRecipe recipe = RecipeHandler.getMetallurgicInfuserRecipe(input);
            cachedRecipe[process] = recipe;
            if (recipe == null) {
                return false;
            }
            return recipe.canOperate(inventory, inputSlot, outputSlot, infuseStored);
        }


        if (recipeType == RecipeType.Crystallizer) {
            if (cachedRecipe[process] instanceof CrystallizerRecipe && ((CrystallizerRecipe) cachedRecipe[process]).getInput().useGas(gasTank, false, 1)) {
                return ((CrystallizerRecipe) cachedRecipe[process]).canOperate(gasTank, inventory, outputSlot);
            }
            GasInput input = new GasInput(gasTank.getGas());
            CrystallizerRecipe recipe = RecipeHandler.getChemicalCrystallizerRecipe(input);
            cachedRecipe[process] = recipe;
            if (recipe == null) {
                return false;
            }
            return recipe.canOperate(gasTank, inventory, outputSlot);
        }

        if (recipeType == RecipeType.WASHER) {
            if (cachedRecipe[process] instanceof WasherRecipe && ((WasherRecipe) cachedRecipe[process]).getInput().useGas(gasTank, false, 1) && waterInput.useFluid(fluidTank, false, 1)) {
                return ((WasherRecipe) cachedRecipe[process]).canOperate(gasTank, fluidTank, gasOutTank);
            }
            GasInput input = new GasInput(gasTank.getGas());
            WasherRecipe recipe = RecipeHandler.getChemicalWasherRecipe(input);
            cachedRecipe[process] = recipe;
            if (recipe == null) {
                return false;
            }
            return recipe.canOperate(gasTank, fluidTank, gasOutTank);
        }

        if (recipeType == RecipeType.Dissolution) {
            if (cachedRecipe[process] instanceof DissolutionRecipe && ((DissolutionRecipe) cachedRecipe[process]).getInput().useItemStackFromInventory(inventory, inputSlot, false)) {
                return ((DissolutionRecipe) cachedRecipe[process]).canOperate(inventory, inputSlot, gasOutTank);
            }
            ItemStackInput input = new ItemStackInput(inventory.get(inputSlot));
            DissolutionRecipe recipe = RecipeHandler.getDissolutionRecipe(input);
            cachedRecipe[process] = recipe;
            if (recipe == null) {
                return false;
            }
            return recipe.canOperate(inventory, inputSlot, gasOutTank);
        }

        if (recipeType == RecipeType.OXIDIZER) {
            if (cachedRecipe[process] instanceof OxidationRecipe && ((OxidationRecipe) cachedRecipe[process]).getInput().useItemStackFromInventory(inventory, inputSlot, false)) {
                return ((OxidationRecipe) cachedRecipe[process]).canOperate(inventory, inputSlot, gasOutTank);
            }
            ItemStackInput input = new ItemStackInput(inventory.get(inputSlot));
            OxidationRecipe recipe = RecipeHandler.getOxidizerRecipe(input);
            cachedRecipe[process] = recipe;
            if (recipe == null) {
                return false;
            }
            return recipe.canOperate(inventory, inputSlot, gasOutTank);
        }

        if (recipeType == RecipeType.NUCLEOSYNTHESIZER) {
            if (cachedRecipe[process] instanceof NucleosynthesizerRecipe && ((NucleosynthesizerRecipe) cachedRecipe[process]).getInput().use(inventory, inputSlot, gasTank, false)) {
                return ((NucleosynthesizerRecipe) cachedRecipe[process]).canOperate(inventory, inputSlot, gasTank, outputSlot);
            }
            NucleosynthesizerInput input = new NucleosynthesizerInput(inventory.get(inputSlot), gasTank.getGas());
            NucleosynthesizerRecipe recipe = RecipeHandler.getNucleosynthesizerRecipe(input);
            cachedRecipe[process] = recipe;
            if (recipe == null) {
                return false;
            }
            return recipe.canOperate(inventory, inputSlot, gasTank, outputSlot);
        }

        if (recipeType == RecipeType.PRC) {
            if (cachedRecipe[process] instanceof PressurizedRecipe && ((PressurizedRecipe) cachedRecipe[process]).getInput().use(inventory, inputSlot, fluidTank, gasTank, false)) {
                return ((PressurizedRecipe) cachedRecipe[process]).canOperate(inventory, inputSlot, fluidTank, gasTank, gasOutTank, outputSlot);
            }
            PressurizedInput input = new PressurizedInput(inventory.get(inputSlot), fluidTank.getFluid(), gasTank.getGas());
            PressurizedRecipe recipe = RecipeHandler.getPRCRecipe(input);
            cachedRecipe[process] = recipe;
            if (recipe == null) {
                return false;
            }
            return recipe.canOperate(inventory, inputSlot, fluidTank, gasTank, gasOutTank, outputSlot);
        }

        if (cachedRecipe[process] instanceof BasicMachineRecipe && ((BasicMachineRecipe<?>) cachedRecipe[process]).inputMatches(inventory, inputSlot)) {
            return ((BasicMachineRecipe<?>) cachedRecipe[process]).canOperate(inventory, inputSlot, outputSlot);
        }
        BasicMachineRecipe<?> recipe = recipeType.getRecipe(inventory.get(inputSlot));
        cachedRecipe[process] = recipe;
        return recipe != null && recipe.canOperate(inventory, inputSlot, outputSlot);

    }

    public void operate(int inputSlot, int outputSlot, int secondaryOutputSlot) {
        if (!canOperate(inputSlot, outputSlot, secondaryOutputSlot)) {
            return;
        }
        int process = getOperation(inputSlot);
        if (cachedRecipe[process] == null) {//should never happen, but cant be too sure.
            Mekanism.logger.debug("cachedRecipe was null, but we were asked to operate anyway?! {} @ {}", this, this.pos);
            return;
        }

        if (recipeType.getFuelType() == MachineFuelType.ADVANCED && cachedRecipe[process] instanceof AdvancedMachineRecipe) {
            AdvancedMachineRecipe<?> recipe = (AdvancedMachineRecipe<?>) cachedRecipe[process];
            recipe.operate(inventory, inputSlot, outputSlot, gasTank, secondaryEnergyThisTick);
        } else if (recipeType.getFuelType() == MachineFuelType.DOUBLE && cachedRecipe[process] instanceof DoubleMachineRecipe) {
            DoubleMachineRecipe<?> recipe = (DoubleMachineRecipe<?>) cachedRecipe[process];
            recipe.operate(inventory, inputSlot, 4, outputSlot);
        } else if (recipeType.getFuelType() == MachineFuelType.CHANCE && cachedRecipe[process] instanceof ChanceMachineRecipe) {
            ChanceMachineRecipe<?> recipe = (ChanceMachineRecipe<?>) cachedRecipe[process];
            recipe.operate(inventory, inputSlot, outputSlot, secondaryOutputSlot);
        } else if (recipeType == RecipeType.INFUSING && cachedRecipe[process] instanceof MetallurgicInfuserRecipe) {
            MetallurgicInfuserRecipe recipe = (MetallurgicInfuserRecipe) cachedRecipe[process];
            recipe.output(inventory, inputSlot, outputSlot, infuseStored);
        } else if (recipeType.getFuelType() == MachineFuelType.FARM && cachedRecipe[process] instanceof FarmMachineRecipe) {
            FarmMachineRecipe<?> recipe = (FarmMachineRecipe<?>) cachedRecipe[process];
            recipe.operate(inventory, inputSlot, gasTank, secondaryEnergyThisTick, outputSlot, secondaryOutputSlot);
        } else if (recipeType.getFuelType() == MachineFuelType.CHANCE2 && cachedRecipe[process] instanceof Chance2MachineRecipe) {
            Chance2MachineRecipe<?> recipe = (Chance2MachineRecipe<?>) cachedRecipe[process];
            recipe.operate(inventory, inputSlot, outputSlot);
        } else if (recipeType == RecipeType.Crystallizer && cachedRecipe[process] instanceof CrystallizerRecipe) {
            CrystallizerRecipe recipe = (CrystallizerRecipe) cachedRecipe[process];
            recipe.operate(gasTank, inventory, outputSlot);
        } else if (recipeType == RecipeType.Dissolution && cachedRecipe[process] instanceof DissolutionRecipe) {
            DissolutionRecipe recipe = (DissolutionRecipe) cachedRecipe[process];
            recipe.operate(inventory, inputSlot, gasOutTank);
        } else if (recipeType == RecipeType.OXIDIZER && cachedRecipe[process] instanceof OxidationRecipe) {
            OxidationRecipe recipe = (OxidationRecipe) cachedRecipe[process];
            recipe.operate(inventory, inputSlot, gasOutTank);
        } else if (recipeType == RecipeType.PRC && cachedRecipe[process] instanceof PressurizedRecipe) {
            PressurizedRecipe recipe = (PressurizedRecipe) cachedRecipe[process];
            recipe.operate(inventory, inputSlot, fluidTank, gasTank, gasOutTank, outputSlot);
        } else if (recipeType == RecipeType.NUCLEOSYNTHESIZER && cachedRecipe[process] instanceof NucleosynthesizerRecipe) {
            NucleosynthesizerRecipe recipe = (NucleosynthesizerRecipe) cachedRecipe[process];
            recipe.operate(inventory, inputSlot, gasTank, outputSlot);
        } else if (recipeType == RecipeType.WASHER && cachedRecipe[process] instanceof WasherRecipe) {
            WasherRecipe recipe = (WasherRecipe) cachedRecipe[process];
            int operations = getUpgradedUsage();
            recipe.operate(gasTank, fluidTank, gasOutTank, operations);
        } else {
            BasicMachineRecipe<?> recipe = (BasicMachineRecipe<?>) cachedRecipe[process];
            recipe.operate(inventory, inputSlot, outputSlot);
        }
        markDirty();
    }

    public int getUpgradedUsage() {
        int possibleProcess = Math.min((int) Math.pow(2, upgradeComponent.getUpgrades(Upgrade.SPEED)), MekanismConfig.current().mekce.MAXspeedmachines.val());
        possibleProcess = Math.min(Math.min(gasTank.getStored(), gasOutTank.getNeeded()), possibleProcess);
        possibleProcess = Math.min((int) (getEnergy() / energyPerTick), possibleProcess);
        return Math.min(fluidTank.getFluidAmount() / WATER_USAGE, possibleProcess);
    }


    @Override
    public void handlePacketData(ByteBuf dataStream) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            int type = dataStream.readInt();
            if (type == 0) {
                sorting = !sorting;
            } else if (type == 1) {
                gasTank.setGas(null);
                gasOutTank.setGas(null);
                fluidTank.setFluid(null);
                infuseStored.setEmpty();
            } else if (type == 2) {
                Factoryoldsorting = !Factoryoldsorting;
            }
            return;
        }

        super.handlePacketData(dataStream);

        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            RecipeType oldRecipe = recipeType;
            recipeType = RecipeType.values()[dataStream.readInt()];
            upgradeComponent.setSupported(Upgrade.GAS, recipeType.fuelEnergyUpgrades());
            recipeTicks = dataStream.readInt();
            sorting = dataStream.readBoolean();
            Factoryoldsorting = dataStream.readBoolean();
            upgraded = dataStream.readBoolean();
            lastUsage = dataStream.readDouble();
            int amount = dataStream.readInt();
            if (amount > 0) {
                infuseStored.setAmount(amount);
                infuseStored.setType(InfuseRegistry.get(PacketHandler.readString(dataStream)));
            } else {
                infuseStored.setEmpty();
            }

            if (recipeType != oldRecipe) {
                setRecipeType(recipeType);
                if (!upgraded) {
                    MekanismUtils.updateBlock(world, getPos());
                }
            }

            for (int i = 0; i < tier.processes; i++) {
                progress[i] = dataStream.readInt();
            }
            TileUtils.readTankData(dataStream, fluidTank);
            TileUtils.readTankData(dataStream, gasTank);
            TileUtils.readTankData(dataStream, gasOutTank);
            if (upgraded) {
                markDirty();
                MekanismUtils.updateBlock(world, getPos());
                upgraded = false;
            }
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        setRecipeType(RecipeType.values()[nbtTags.getInteger("recipeType")]);
        upgradeComponent.setSupported(Upgrade.GAS, recipeType.fuelEnergyUpgrades());
        recipeTicks = nbtTags.getInteger("recipeTicks");
        sorting = nbtTags.getBoolean("sorting");
        Factoryoldsorting = nbtTags.getBoolean("factoryoldsorting");
        int amount = nbtTags.getInteger("infuseStored");
        if (amount != 0) {
            infuseStored.setAmount(amount);
            infuseStored.setType(InfuseRegistry.get(nbtTags.getString("type")));
        }
        for (int i = 0; i < tier.processes; i++) {
            progress[i] = nbtTags.getInteger("progress" + i);
        }
        fluidTank.readFromNBT(nbtTags.getCompoundTag("fluidTank"));
        gasTank.read(nbtTags.getCompoundTag("gasTank"));
        gasOutTank.read(nbtTags.getCompoundTag("gasOutTank"));
        GasUtils.clearIfInvalid(gasTank, recipeType::isValidGas);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setInteger("recipeType", recipeType.ordinal());
        nbtTags.setInteger("recipeTicks", recipeTicks);
        nbtTags.setBoolean("sorting", sorting);
        nbtTags.setBoolean("factoryoldsorting", Factoryoldsorting);
        if (infuseStored.getType() != null) {
            nbtTags.setString("type", infuseStored.getType().name);
            nbtTags.setInteger("infuseStored", infuseStored.getAmount());
        } else {
            nbtTags.setString("type", "null");
        }
        for (int i = 0; i < tier.processes; i++) {
            nbtTags.setInteger("progress" + i, progress[i]);
        }
        nbtTags.setTag("fluidTank", fluidTank.writeToNBT(new NBTTagCompound()));
        nbtTags.setTag("gasTank", gasTank.write(new NBTTagCompound()));
        nbtTags.setTag("gasOutTank", gasOutTank.write(new NBTTagCompound()));
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);

        data.add(recipeType.ordinal());
        data.add(recipeTicks);
        data.add(sorting);
        data.add(Factoryoldsorting);
        data.add(upgraded);
        data.add(lastUsage);

        data.add(infuseStored.getAmount());
        if (infuseStored.getAmount() > 0) {
            data.add(infuseStored.getType().name);
        }

        data.add(progress);
        TileUtils.addTankData(data, fluidTank);
        TileUtils.addTankData(data, gasTank);
        TileUtils.addTankData(data, gasOutTank);
        upgraded = false;
        return data;
    }

    public int getInputSlot(int operation) {
        return 5 + operation;
    }

    /* reverse of the above */

    private int getOperation(int inputSlot) {
        return inputSlot - 5;
    }

    public int getOutputSlot(int operation) {
        return 5 + tier.processes + operation;
    }

    public int getSecondaryOutputSlot(int operation) {
        return 5 + tier.processes * 2 + operation;
    }

    @Nonnull
    @Override
    public String getName() {
        if (LangUtils.canLocalize("tile." + tier.getBaseTier().getName() + recipeType.getTranslationKey() + "Factory")) {
            return LangUtils.localize("tile." + tier.getBaseTier().getName() + recipeType.getTranslationKey() + "Factory");
        }
        //TODO:Rename this
        return tier.getBaseTier().getLocalizedName() + recipeType.getLocalizedName() + super.getName();
    }

    @Override
    public String[] getMethods() {
        return methods;
    }

    @Override
    public Object[] invoke(int method, Object[] arguments) throws NoSuchMethodException {
        switch (method) {
            case 0 -> {
                return new Object[]{electricityStored};
            }
            case 1 -> {
                if (arguments[0] == null) {
                    return new Object[]{"Please provide a target operation."};
                }
                if (!(arguments[0] instanceof Double) && !(arguments[0] instanceof Integer)) {
                    return new Object[]{"Invalid characters."};
                }
                if ((Integer) arguments[0] < 0 || (Integer) arguments[0] > progress.length) {
                    return new Object[]{"No such operation found."};
                }
                return new Object[]{progress[(Integer) arguments[0]]};
            }
            case 2 -> {
                return new Object[]{facing};
            }
            case 3 -> {
                if (arguments[0] == null) {
                    return new Object[]{"Please provide a target operation."};
                }
                if (!(arguments[0] instanceof Double) && !(arguments[0] instanceof Integer)) {
                    return new Object[]{"Invalid characters."};
                }
                if ((Integer) arguments[0] < 0 || (Integer) arguments[0] > progress.length) {
                    return new Object[]{"No such operation found."};
                }
                return new Object[]{canOperate(getInputSlot((Integer) arguments[0]), getOutputSlot((Integer) arguments[0]), getSecondaryOutputSlot((Integer) arguments[0]))};
            }
            case 4 -> {
                return new Object[]{getMaxEnergy()};
            }
            case 5 -> {
                return new Object[]{getMaxEnergy() - getEnergy()};
            }
            default -> throw new NoSuchMethodException();
        }
    }

    @Override
    public int fill(EnumFacing from, @Nonnull FluidStack resource, boolean doFill) {
        return fluidTank.fill(resource, doFill);
    }

    @Override
    public boolean canFill(EnumFacing from, @Nonnull FluidStack fluid) {
        if (configComponent.getOutput(TransmissionType.FLUID, from, facing).ioState == SideData.IOState.INPUT) {
            return FluidContainerUtils.canFill(fluidTank.getFluid(), fluid);
        }
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        if (recipeType == RecipeType.PRC || recipeType == RecipeType.WASHER) {
            if (configComponent.getOutput(TransmissionType.FLUID, from, facing).ioState != SideData.IOState.OFF) {
                return new FluidTankInfo[]{fluidTank.getInfo()};
            }
        }
        return PipeUtils.EMPTY;
    }

    @Override
    public FluidTankInfo[] getAllTanks() {
        if (recipeType == RecipeType.PRC || recipeType == RecipeType.WASHER) {
            return new FluidTankInfo[]{fluidTank.getInfo()};
        } else {
            return PipeUtils.EMPTY;
        }
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return configComponent.getOutput(TransmissionType.ITEM, side, facing).availableSlots;
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
    public TileComponentEjector getEjector() {
        return ejectorComponent;
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        if (canReceiveGas(side, stack.getGas())) {
            return gasTank.receive(stack, doTransfer);
        }
        return 0;
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        if (canDrawGas(side, null)) {
            return gasOutTank.draw(amount, doTransfer);
        }
        return null;
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        if (configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(1)) {
            if (recipeType.getFuelType() == MachineFuelType.ADVANCED || recipeType.getFuelType() == MachineFuelType.FARM) {
                return recipeType.canReceiveGas(side, type);
            } else if (recipeType == RecipeType.Crystallizer) {
                return RecipeHandler.Recipe.CHEMICAL_CRYSTALLIZER.containsRecipe(type);
            } else if (recipeType == RecipeType.Dissolution) {
                return type == MekanismFluids.SulfuricAcid;
            } else if (recipeType == RecipeType.PRC || recipeType == RecipeType.NUCLEOSYNTHESIZER) {
                return true;
            } else if (recipeType == RecipeType.WASHER) {
                return RecipeHandler.Recipe.CHEMICAL_WASHER.containsRecipe(type);
            }
        }
        return false;
    }


    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        return configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(2) && gasOutTank.canDraw(type);
    }

    @Nonnull
    @Override
    public GasTankInfo[] getTankInfo() {
        if (recipeType == RecipeType.Dissolution || recipeType == RecipeType.WASHER || recipeType == RecipeType.PRC) { //Only these plants show two gas storages
            return new GasTankInfo[]{gasTank, gasOutTank};
        } else if (recipeType == RecipeType.OXIDIZER) {
            return new GasTankInfo[]{gasOutTank};
        } else {
            return new GasTankInfo[]{gasTank};
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return false;
        }
        return capability == Capabilities.GAS_HANDLER_CAPABILITY || capability == Capabilities.CONFIG_CARD_CAPABILITY || capability == Capabilities.SPECIAL_CONFIG_DATA_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return null;
        }
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY || capability == Capabilities.CONFIG_CARD_CAPABILITY || capability == Capabilities.SPECIAL_CONFIG_DATA_CAPABILITY) {
            return (T) this;
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FluidHandlerWrapper(this, side));
        }
        return super.getCapability(capability, side);
    }

    @Override
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side) {
        if (configComponent.isCapabilityDisabled(capability, side, facing)) {
            return true;
        } else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            // Originally, it was only possible to disable it if the type was specified
            //Now modifying to these can also be disabled
            if (GasInputMachine() || GasAdvancedInputMachine() || recipeType == RecipeType.OXIDIZER) {
                return false;
            } else {
                return true;
            }
        }
        return super.isCapabilityDisabled(capability, side);
    }

    @Override
    public void recalculateUpgradables(Upgrade upgrade) {
        super.recalculateUpgradables(upgrade);
        switch (upgrade) {
            case ENERGY -> {
                if (tier == FactoryTier.CREATIVE) {
                    maxEnergy = BASE_MAX_ENERGY;
                }
                energyPerTick = MekanismUtils.getEnergyPerTick(this, BASE_ENERGY_PER_TICK); // incorporate speed upgrades
            }
            case GAS -> secondaryEnergyPerTick = getSecondaryEnergyPerTick(recipeType);
            case SPEED -> {
                if (tier != FactoryTier.CREATIVE) {
                    energyPerTick = MekanismUtils.getEnergyPerTick(this, BASE_ENERGY_PER_TICK);
                }
                ticksRequired = MekanismUtils.getTicks(this, BASE_TICKS_REQUIRED);
                secondaryEnergyPerTick = getSecondaryEnergyPerTick(recipeType);
            }
            default -> {
            }
        }
    }

    @Override
    public Object[] getTanks() {
        return new Object[]{fluidTank, gasTank, gasOutTank};
    }

    @Override
    public NBTTagCompound getConfigurationData(NBTTagCompound nbtTags) {
        nbtTags.setBoolean("sorting", sorting);
        return nbtTags;
    }

    @Override
    public void setConfigurationData(NBTTagCompound nbtTags) {
        sorting = nbtTags.getBoolean("sorting");
    }

    @Override
    public String getDataType() {
        return tier.getBaseTier().getLocalizedName() + recipeType.getLocalizedName() + super.getName();
    }

    @Override
    public void writeSustainedData(ItemStack itemStack) {
        infuseStored.writeSustainedData(itemStack);
        GasUtils.writeSustainedData(gasTank, itemStack);
        GasUtils.writeSustainedData(gasOutTank, itemStack);
        if (fluidTank.getFluid() != null) {
            ItemDataUtils.setCompound(itemStack, "fluidTank", fluidTank.getFluid().writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void readSustainedData(ItemStack itemStack) {
        infuseStored.readSustainedData(itemStack);
        GasUtils.readSustainedData(gasTank, itemStack);
        GasUtils.readSustainedData(gasOutTank, itemStack);
        fluidTank.setFluid(FluidStack.loadFluidStackFromNBT(ItemDataUtils.getCompound(itemStack, "fluidTank")));
    }

    @Override
    public int getRedstoneLevel() {
        return Container.calcRedstoneFromInventory(this);
    }

    public MachineRecipe<?, ?, ?> getSlotRecipe(int slotID, ItemStack fallbackInput, ItemStack output) {
        int process = getOperation(slotID);
        //cached recipe may be invalid
        MachineRecipe<?, ?, ?> cached = cachedRecipe[process];
        ItemStack extra = inventory.get(4);
        if (cached == null) {
            cached = recipeType.getAnyRecipe(fallbackInput, extra, gasTank.getGasType(), infuseStored, gasTank.getGas(), fluidTank.getFluid());
            if (cached == null) { // We have not enough input probably
                cached = recipeType.getAnyRecipe(StackUtils.size(fallbackInput, fallbackInput.getMaxStackSize()), extra, gasTank.getGasType(), infuseStored, gasTank.getGas(), fluidTank.getFluid());
            }
        } else {
            ItemStack recipeInput = ItemStack.EMPTY;
            boolean secondaryMatch = true;
            if (cached.recipeInput instanceof ItemStackInput) {
                recipeInput = ((ItemStackInput) cached.recipeInput).ingredient;
            } else if (cached.recipeInput instanceof AdvancedMachineInput advancedInput) {
                recipeInput = advancedInput.itemStack;
                secondaryMatch = gasTank.getGasType() == null || advancedInput.gasType == gasTank.getGasType();
            } else if (cached.recipeInput instanceof DoubleMachineInput doubleMachineInput) {
                recipeInput = doubleMachineInput.itemStack;
                secondaryMatch = extra.isEmpty() || ItemStack.areItemsEqual(doubleMachineInput.extraStack, extra);
            } else if (cached.recipeInput instanceof InfusionInput infusionInput) {
                recipeInput = infusionInput.inputStack;
                secondaryMatch = infuseStored.getAmount() == 0 || infuseStored.getType() == infusionInput.infuse.getType();
            } else if (cached.recipeInput instanceof GasInput gasInput) {
                secondaryMatch = gasTank.getGasType() == null || gasInput.ingredient.getGas() == gasTank.getGasType();
            } else if (cached.recipeInput instanceof PressurizedInput pressurizedInput) {
                recipeInput = pressurizedInput.getSolid();
                secondaryMatch = (gasTank.getGas() == null || gasTank.getGas().isGasEqual(pressurizedInput.getGas())) && (fluidTank.getFluid() == null || fluidTank.getFluid() == pressurizedInput.getFluid());
            } else if (cached.recipeInput instanceof NucleosynthesizerInput input) {
                recipeInput = input.getSolid();
                secondaryMatch = (gasTank.getGas() == null || gasTank.getGas().isGasEqual(input.getGas()));
            }
            //If there is no cached item input or it doesn't match our fallback
            // then it is an out of date cache so we compare against the new one
            // and update the cache while we are at it
            if (recipeInput.isEmpty() || !secondaryMatch || !ItemStack.areItemsEqual(recipeInput, fallbackInput)) {
                cached = recipeType.getAnyRecipe(fallbackInput, extra, gasTank.getGasType(), infuseStored, gasTank.getGas(), fluidTank.getFluid());
            }
        }

        if (cached != null) {
            ItemStack recipeOutput = ItemStack.EMPTY;
            if (cached.recipeOutput instanceof ItemStackOutput) {
                recipeOutput = ((ItemStackOutput) cached.recipeOutput).output;
            } else if (cached.recipeOutput instanceof ChanceOutput) {
                recipeOutput = ((ChanceOutput) cached.recipeOutput).primaryOutput;
            } else if (cached.recipeOutput instanceof ChanceOutput2) {
                recipeOutput = ((ChanceOutput2) cached.recipeOutput).primaryOutput;
            } else if (cached.recipeOutput instanceof PressurizedOutput) {
                recipeOutput = ((PressurizedOutput) cached.recipeOutput).getItemOutput();
            }
            if (!recipeOutput.isEmpty()) {
                InventoryUtils.areItemsStackable(recipeOutput, output);
            }
        }
        return cached;
    }

    public void sortInventory() { //old
        if (sorting) {
            int[] inputSlots = getSlotsWithTier(tier);
            if (inputSlots == null) {
                return;
            }
            for (int i = 0; i < inputSlots.length; i++) {
                int slotID = inputSlots[i];
                ItemStack stack = inventory.get(slotID);
                int count = stack.getCount();
                ItemStack output = inventory.get(tier.processes + slotID);
                for (int j = i + 1; j < inputSlots.length; j++) {
                    int checkSlotID = inputSlots[j];
                    ItemStack checkStack = inventory.get(checkSlotID);
                    if (Math.abs(count - checkStack.getCount()) < 2 ||
                            !InventoryUtils.areItemsStackable(stack, checkStack)) {
                        continue;
                    }
                    //Output/Input will not match
                    // Only check if the input spot is empty otherwise assume it works
                    if (stack.isEmpty() && !inputProducesOutput(checkSlotID, checkStack, output, true) ||
                            checkStack.isEmpty() && !inputProducesOutput(slotID, stack, inventory.get(tier.processes + checkSlotID), true)) {
                        continue;
                    }
                    //Balance the two slots
                    int total = count + checkStack.getCount();
                    ItemStack newStack = stack.isEmpty() ? checkStack : stack;
                    inventory.set(slotID, StackUtils.size(newStack, (total + 1) / 2));
                    inventory.set(checkSlotID, StackUtils.size(newStack, total / 2));
                    markDirty();
                    return;
                }
            }
        }
    }


    /**
     * <p>Efficient, intelligent factory sequencing.</p>
     * <p><strong>Non-thread safe。</strong></p>
     * <p>In fact, it still has a lot of room for optimization, limited by the structure of the code, these features are sufficient.</p>
     */
    public static class FactoryInvSorter {
        private final TileEntityFactory factory;
        // Reusable List
        private final List<Tuple<MachineRecipe<?, ?, ?>, ItemStack>> vaildRecipeItemStackList = new ArrayList<>();
        // Reusable List
        private final List<ItemStack> invaildRecipeItemStackList = new ArrayList<>();
        // Reusable List
        private final List<ItemStack> sorted = new ArrayList<>();

        public FactoryInvSorter(TileEntityFactory factory) {
            this.factory = factory;
        }

        /**
         * <p>Add an ItemStack to the item list. </p
         *
         * @param willBeAdded The item that will be added to the list, or merged if the item already exists in the list and has not reached its maximum stack value.
         * @param stackList   The list of items.
         */
        private static void addItemStackToList(ItemStack willBeAdded, List<ItemStack> stackList) {
            boolean isAdded = false;
            for (ItemStack stack : stackList) {
                int maxStackSize = stack.getMaxStackSize();
                int invStackCount = willBeAdded.getCount();

                if (!matchStacks(stack, willBeAdded)) {
                    continue;
                }
                if (stack.getCount() >= maxStackSize) {
                    continue;
                }
                if (stack.getCount() + invStackCount > maxStackSize) {
                    int added = maxStackSize - stack.getCount();
                    stack.setCount(maxStackSize);
                    willBeAdded.setCount(invStackCount - added);
                    continue;
                }
                stack.setCount(stack.getCount() + invStackCount);
                isAdded = true;
            }
            if (!isAdded) {
                stackList.add(willBeAdded);
            }
        }

        /**
         * <p>Add an ItemStack to the item list. </p
         *
         * @param willBeAdded The item that will be added to the list, or merged if the item already exists in the list and has not reached the maximum stack value.
         * @param tupleList   The list of items.
         * @return Returns true if the addition was successful, or false if the list is full.
         */
        private static boolean addItemStackToTupleList(ItemStack willBeAdded, List<Tuple<MachineRecipe<?, ?, ?>, ItemStack>> tupleList) {
            for (Tuple<MachineRecipe<?, ?, ?>, ItemStack> collected : tupleList) {
                ItemStack stack = collected.getSecond();
                int maxStackSize = stack.getMaxStackSize();
                int invStackCount = willBeAdded.getCount();

                if (!matchStacks(stack, willBeAdded)) {
                    continue;
                }
                if (stack.getCount() >= maxStackSize) {
                    continue;
                }
                if (stack.getCount() + invStackCount > maxStackSize) {
                    int added = maxStackSize - stack.getCount();
                    stack.setCount(maxStackSize);
                    willBeAdded.setCount(invStackCount - added);
                    continue;
                }
                stack.setCount(stack.getCount() + invStackCount);
                return true;
            }

            return false;
        }

        /**
         * <h2>Sorting Process Introduction</h2>
         * <ol>
         *     <li>First detects if there is at least one item in the factory, and if there is no item, ends the process early.</li>
         *     <li>When the above condition is met, start sorting the contents into two lists (see {@link FactoryInvSorter#collectInvToList(int[] slotIds)} for the workflow.</li>
         *     <li>After sorting, execute {@link FactoryInvSorter#doSort(int)} to sort the results further and output the results to {@link FactoryInvSorter#sorted}.</li>
         *     <li>Finally, execute {@link FactoryInvSorter#applyResult(List sorted, int[] slotIds)} to apply the result to the machine inventory.</li>
         * </ol>
         */
        public void sort() {
            if (!factory.sorting || factory.getWorld().getWorldTime() % 20 != 0) {
                return;
            }
            int[] slotIds = getSlotsWithTier(factory.tier);
            if (slotIds == null || !hasItem(slotIds)) {
                return;
            }

            vaildRecipeItemStackList.clear();
            invaildRecipeItemStackList.clear();
            sorted.clear();

            collectInvToList(slotIds);

            if (vaildRecipeItemStackList.size() + invaildRecipeItemStackList.size() >= slotIds.length) {
                //The collection size is bigger than equals slotIds size, end sort.
                return;
            }

            doSort(slotIds.length - (vaildRecipeItemStackList.size() + invaildRecipeItemStackList.size()));
            applyResult(sorted, slotIds);
        }

        /**
         * Check if at least one item exists in the mechanical item bar.
         *
         * @param slotIds The slot to check.
         * @return Returns true if at least one item is present, false if neither is present.
         */
        private boolean hasItem(int[] slotIds) {
            for (int slotId : slotIds) {
                if (factory.inventory.get(slotId) != ItemStack.EMPTY) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Apply items from the specified item list to the mechanical item bar, which <strong>must</strong> be larger or equal to the list size.
         *
         * @param sorted  Sorted item list
         * @param slotIds slotIdArray
         */
        private void applyResult(List<ItemStack> sorted, int[] slotIds) {
            if (sorted.isEmpty()) {
                return;
            }

            int index = 0;
            for (int slotId : slotIds) {
                factory.inventory.set(slotId, ItemStack.EMPTY);
                if (index >= sorted.size()) {
                    continue;
                }
                factory.inventory.set(slotId, sorted.get(index));
                index++;
            }
            sorted.clear();
            factory.markDirty();
        }

        /**
         * <p>Sort the sorted results to return a list of items available for use by {@link FactoryInvSorter#applyResult(List itemStackList, int[] slotIds)}.</p>
         * <h2>Additional Features:</h2>
         * <ul>
         *     <li>Automatically calculates the product of the corresponding item and limits the number of divisions to the minimum number of recipes.</li>
         *     <li>Maximize the application of each item slot while implementing the features above.</li>
         *     <li>Minimize the number of slots occupied by invalid items and merge these invalid items where allowed.</li>
         * </ul>
         *
         * @param emptySlotAmount Available empty slots.
         */
        private void doSort(int emptySlotAmount) {
            int availableEmptySlotAmount = emptySlotAmount;
            for (Tuple<MachineRecipe<?, ?, ?>, ItemStack> recipeAndInput : vaildRecipeItemStackList) {
                MachineRecipe<?, ?, ?> recipe = recipeAndInput.getFirst();
                ItemStack invStack = recipeAndInput.getSecond();
                ItemStack recipeInput = TileEntityFactory.getRecipeInput(recipe);

                int invCount = invStack.getCount();
                int minCount = recipeInput.getCount();
                if (invCount <= minCount) {
                    sorted.add(invStack);
                    continue;
                }

                int splitCount = Math.min(availableEmptySlotAmount + 1, invCount / minCount);
                int countAfterSplit = invCount / splitCount;
                int extra = invCount % splitCount;

                sorted.add(copyStackWithSize(invStack, countAfterSplit + extra));

                while (splitCount > 1) {
                    sorted.add(copyStackWithSize(invStack, countAfterSplit));
                    availableEmptySlotAmount--;
                    splitCount--;
                }
            }
            sorted.addAll(invaildRecipeItemStackList);
        }

        /**
         * <p>Iterate through the contents of the mechanical item column with the incoming slot array and sort it into two item lists.</p>
         * <h2>Feature:</h2>
         * <ul>
         *     <li>Automatically determines if an item is ready to run a recipe and adds it to the list {@link FactoryInvSorter#vaildRecipeItemStackList}.</li>
         *     <li>Automatically determines items that cannot perform a recipe or are invalid and adds them to the list {@link FactoryInvSorter#invaildRecipeItemStackList}.</li>
         * </ul>
         *
         * @param slotIds slotIDArray
         */
        private void collectInvToList(int[] slotIds) {
            for (int slotId : slotIds) {
                ItemStack invTmp = factory.inventory.get(slotId);
                if (invTmp == ItemStack.EMPTY) {
                    continue;
                }
                ItemStack invStack = invTmp.copy();

                if (addItemStackToTupleList(invStack, vaildRecipeItemStackList)) {
                    continue;
                }

                ItemStack outStack = factory.inventory.get(slotId + factory.tier.processes);
                MachineRecipe<?, ?, ?> recipe = factory.getSlotRecipe(slotId, invStack, outStack);
                if (recipe != null) {
                    vaildRecipeItemStackList.add(new Tuple<>(recipe, invStack));
                } else {
                    addItemStackToList(invStack, invaildRecipeItemStackList);
                }
            }
        }
    }
}
