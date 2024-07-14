package mekanism.common.config;

import mekanism.common.config.options.BooleanOption;
import mekanism.common.config.options.DoubleOption;
import mekanism.common.config.options.IntOption;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine.MultiblockMachineType;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachineGenerator.MultiblockMachineGeneratorType;

public class MultiblockMachineConfig extends BaseConfig {

    public final BooleanOption destroyDisabledBlocks = new BooleanOption(this, "multiblock", "DestroyDisabledMultiBlockBlocks", true,
            "If machine is disabled in config, do we set its block to air if it is found in world?");

    public final DoubleOption largewindGeneratorStorage = new DoubleOption(this, "multiblock",
            "MultiblockWindGeneratorStorage", 58800000D, "Energy capable of being stored");

    public final DoubleOption largewindGeneratorOut = new DoubleOption(this, "multiblock",
            "MultiblockWindGeneratorOut", 58800000D, "Large wind turbine output");

    public final DoubleOption largewindGenerationMax = new DoubleOption(this, "multiblock", "LargeWindGeneratorMax", 294000D,
            "Maximum base generation value of the Large Wind Generator.");



    public final DoubleOption largewindGenerationMin = new DoubleOption(this, "multiblock", "LargeWindGeneratorMin", 60D,
            "Minimum base generation value of the Large Wind Generator.");

    public final IntOption largewindGenerationMinY = new IntOption(this, "multiblock", "LargeWindGenerationMinY", 50,
            "The minimum Y value that affects the Large Wind Generators Power generation.", 50, 255);

    public final IntOption largewindGenerationMaxY = new IntOption(this, "multiblock", "LargeWindGenerationMaxY", 255,
            "The maximum Y value that affects the Large Wind Generators Power generation.", 50, 255);

    public final IntOption largewindGenerationBlastRadius = new IntOption(this, "multiblock", "largewindGeneratorBlastRadius", 45,
            "The range of a large wind turbine when it explodes.");
    public final IntOption largewindGenerationExplodeCount = new IntOption(this, "multiblock", "largewindGenerationExplodeCount", 100, "An explosion can occur after the entity has been in the blade for a number of ticks.");

    public final BooleanOption largewindGenerationExplode = new BooleanOption(this, "multiblock", "largewindGenerationExplode", false, "An explosion occurs when an entity is inside the rotating blades of a large wind turbine.");

    public final BooleanOption largewindGenerationDamage = new BooleanOption(this,"multiblock","largewindGenerationDamage",false,"Whether or not the organism causes harm when it is inside the leaf while it is working");

    public final BooleanOption largewindGenerationRangeStops = new BooleanOption(this,"multiblock","largewindGenerationRangeStops",true,"Centered on the main body, it detects whether there is an identical wind turbine within a range of 50*50, and stops working if there is");

    public final DoubleOption largeHeatGeneratorStorage = new DoubleOption(this, "multiblock",
            "MultiblockHeatGeneratorStorage", 4320000D, "Energy capable of being stored");

    public final DoubleOption largeHeatGeneratorOut = new DoubleOption(this, "multiblock",
            "largeHeatGeneratorOut", 4320000D, "Large thermal generator output");

    public final DoubleOption largeHeatGeneration = new DoubleOption(this, "multiblock", "largeHeatGeneration", 4050D,
            "Amount of energy in Joules the Heat Generator produces per tick. (heatGenerationLava * heatGenerationLava) + heatGenerationNether");

    public final DoubleOption largeHeatGenerationNether = new DoubleOption(this, "multiblock", "largeHeatGenerationNether", 2700D,
            "Add this amount of Joules to the energy produced by a heat generator if it is in the Nether.");

    public final DoubleOption largeHeatGenerationLava = new DoubleOption(this, "multiblock", "largeHeatGenerationLava", 135D,
            "Multiplier of effectiveness of Lava in the Heat Generator.");

    public TypeConfigManager<MultiblockMachineGeneratorType> multiblockmachinegeneratorsManager = new TypeConfigManager<>(this, "multiblockmachinegenerators", MultiblockMachineGeneratorType.class,
            MultiblockMachineGeneratorType::getGeneratorsForConfig, MultiblockMachineGeneratorType::getBlockName);


    public final DoubleOption largelectrolyticSeparator = new DoubleOption(this, "multiblock", "largeElectrolyticSeparatorStorage", 160000D * 27,
            "Base energy storage (Joules).");

    public final DoubleOption largechemicalInfuserUsage = new DoubleOption(this, "multiblock", "largeChemicalInfuserUsage", 200D,
            "Energy per operation tick (Joules).");

    public final DoubleOption largechemicalInfuserStorage = new DoubleOption(this, "multiblock", "ChemicalInfuserStorage", 80000D * 27,
            "Base energy storage (Joules).");

    public final DoubleOption LargeGasGeneratorStorage = new DoubleOption(this, "multiblock", "LargeGasGeneratorStorage", 80000D * 27,
            "Base energy storage (Joules).");

    public final DoubleOption LargeGasGeneratorOut = new DoubleOption(this, "multiblock", "LargeGasGeneratorOut", 80000D * 27,
            "Large gas generator output.");

    public final DoubleOption LargeChemicalWasherUsage = new DoubleOption(this, "multiblock", "LargeChemicalWasherUsage", 200D,
            "Energy per operation tick (Joules).");

    public final DoubleOption LargeChemicalWasherStorage = new DoubleOption(this, "multiblock", "LargeChemicalWasherStorage", 80000D * 27,
            "Base energy storage (Joules).");

    public final DoubleOption DigitalAssemblyTableUsage = new DoubleOption(this, "multiblock", "DigitalAssemblyTableUsage", 200D,
            "Energy per operation tick (Joules).");

    public final DoubleOption DigitalAssemblyTableStorage = new DoubleOption(this, "multiblock", "LargeChemicalWasherStorage", 80000D * 27,
            "Base energy storage (Joules).");

    public final TypeConfigManager<MultiblockMachineType> multiblockmachinesManager = new TypeConfigManager<>(this, "multiblockmachines", MultiblockMachineType.class,MultiblockMachineType::getValidMachines,MultiblockMachineType::getBlockName);


}
