package mekanism.common.recipe;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.infuse.InfuseType;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.recipe.inputs.*;
import mekanism.common.recipe.machines.*;
import mekanism.common.recipe.outputs.*;
import mekanism.common.util.StackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.Map.Entry;

/**
 * Class used to handle machine recipes. This is used for both adding and fetching recipes.
 *
 * @author AidanBrady, unpairedbracket
 */
public final class RecipeHandler {

    public static <INPUT extends MachineInput<INPUT>, OUTPUT extends MachineOutput<OUTPUT>, RECIPE extends MachineRecipe<INPUT, OUTPUT, RECIPE>>
    void addRecipe(@Nonnull Recipe<INPUT, OUTPUT, RECIPE> recipeMap, @Nonnull RECIPE recipe) {
        recipeMap.put(recipe);
    }

    public static <INPUT extends MachineInput<INPUT>, OUTPUT extends MachineOutput<OUTPUT>, RECIPE extends MachineRecipe<INPUT, OUTPUT, RECIPE>>
    void removeRecipe(@Nonnull Recipe<INPUT, OUTPUT, RECIPE> recipeMap, @Nonnull RECIPE recipe) {
        List<INPUT> toRemove = new ArrayList<>();
        for (INPUT iterInput : recipeMap.get().keySet()) {
            if (iterInput.testEquality(recipe.getInput())) {
                toRemove.add(iterInput);
            }
        }
        for (INPUT iterInput : toRemove) {
            recipeMap.get().remove(iterInput);
        }
    }

    /**
     * Add an Enrichment Chamber recipe.
     *
     * @param input  - input ItemStack
     * @param output - output ItemStack
     */
    public static void addEnrichmentChamberRecipe(ItemStack input, ItemStack output) {
        addRecipe(Recipe.ENRICHMENT_CHAMBER, new EnrichmentRecipe(input, output));
    }

    /**
     * Add an Osmium Compressor recipe.
     *
     * @param input  - input ItemStack
     * @param output - output ItemStack
     */
    public static void addOsmiumCompressorRecipe(ItemStack input, ItemStack output) {
        addRecipe(Recipe.OSMIUM_COMPRESSOR, new OsmiumCompressorRecipe(input, output));
    }

    /**
     * Add a Combiner recipe.
     *
     * @param input  - input ItemStack
     * @param output - output ItemStack
     * @deprecated Replaced by {@link #addCombinerRecipe(ItemStack, ItemStack, ItemStack)}. May be removed with Minecraft 1.13.
     */
    @Deprecated
    public static void addCombinerRecipe(ItemStack input, ItemStack output) {
        addRecipe(Recipe.COMBINER, new CombinerRecipe(input, output));
    }

    /**
     * Add a Combiner recipe.
     *
     * @param input  - input ItemStack
     * @param extra  - extra ItemStack
     * @param output - output ItemStack
     */
    public static void addCombinerRecipe(ItemStack input, ItemStack extra, ItemStack output) {
        addRecipe(Recipe.COMBINER, new CombinerRecipe(input, extra, output));
    }

    /**
     * Add a Crusher recipe.
     *
     * @param input  - input ItemStack
     * @param output - output ItemStack
     */
    public static void addCrusherRecipe(ItemStack input, ItemStack output) {
        addRecipe(Recipe.CRUSHER, new CrusherRecipe(input, output));
    }

    /**
     * Add a Purification Chamber recipe.
     *
     * @param input  - input ItemStack
     * @param output - output ItemStack
     */
    public static void addPurificationChamberRecipe(ItemStack input, ItemStack output) {
        addRecipe(Recipe.PURIFICATION_CHAMBER, new PurificationRecipe(input, output));
    }

    /**
     * Add a Metallurgic Infuser recipe.
     *
     * @param infuse - which Infuse to use
     * @param amount - how much of the Infuse to use
     * @param input  - input ItemStack
     * @param output - output ItemStack
     */
    public static void addMetallurgicInfuserRecipe(InfuseType infuse, int amount, ItemStack input, ItemStack output) {
        addRecipe(Recipe.METALLURGIC_INFUSER, new MetallurgicInfuserRecipe(new InfusionInput(infuse, amount, input), output));
    }

    /**
     * Add a Chemical Infuser recipe.
     *
     * @param leftInput  - left GasStack to input
     * @param rightInput - right GasStack to input
     * @param output     - output GasStack
     */
    public static void addChemicalInfuserRecipe(GasStack leftInput, GasStack rightInput, GasStack output) {
        addRecipe(Recipe.CHEMICAL_INFUSER, new ChemicalInfuserRecipe(leftInput, rightInput, output));
    }

    /**
     * Add a Chemical Oxidizer recipe.
     *
     * @param input  - input ItemStack
     * @param output - output GasStack
     */
    public static void addChemicalOxidizerRecipe(ItemStack input, GasStack output) {
        addRecipe(Recipe.CHEMICAL_OXIDIZER, new OxidationRecipe(input, output));
    }

    /**
     * Add a Chemical Injection Chamber recipe.
     *
     * @param input  - input ItemStack
     * @param output - output ItemStack
     */
    public static void addChemicalInjectionChamberRecipe(ItemStack input, Gas gas, ItemStack output) {
        addRecipe(Recipe.CHEMICAL_INJECTION_CHAMBER, new InjectionRecipe(input, gas, output));
    }

    /**
     * Add an Electrolytic Separator recipe.
     *
     * @param fluid       - FluidStack to electrolyze
     * @param leftOutput  - left gas to produce when the fluid is electrolyzed
     * @param rightOutput - right gas to produce when the fluid is electrolyzed
     */
    public static void addElectrolyticSeparatorRecipe(FluidStack fluid, double energy, GasStack leftOutput, GasStack rightOutput) {
        addRecipe(Recipe.ELECTROLYTIC_SEPARATOR, new SeparatorRecipe(fluid, energy, leftOutput, rightOutput));
    }

    /**
     * Add a Precision Sawmill recipe.
     *
     * @param input           - input ItemStack
     * @param primaryOutput   - guaranteed output
     * @param secondaryOutput - possible extra output
     * @param chance          - probability of obtaining extra output
     */
    public static void addPrecisionSawmillRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, double chance) {
        addRecipe(Recipe.PRECISION_SAWMILL, new SawmillRecipe(input, primaryOutput, secondaryOutput, chance));
    }

    /**
     * Add a Precision Sawmill recipe with no chance output
     *
     * @param input         - input ItemStack
     * @param primaryOutput - guaranteed output
     */
    public static void addPrecisionSawmillRecipe(ItemStack input, ItemStack primaryOutput) {
        addRecipe(Recipe.PRECISION_SAWMILL, new SawmillRecipe(input, primaryOutput));
    }

    /**
     * Add a Chemical Dissolution Chamber recipe.
     *
     * @param input  - input ItemStack
     * @param output - output GasStack
     */
    public static void addChemicalDissolutionChamberRecipe(ItemStack input, GasStack output) {
        addRecipe(Recipe.CHEMICAL_DISSOLUTION_CHAMBER, new DissolutionRecipe(input, output));
    }

    /**
     * Add a Chemical Washer recipe.
     *
     * @param input  - input GasStack
     * @param output - output GasStack
     */
    public static void addChemicalWasherRecipe(GasStack input, GasStack output) {
        addRecipe(Recipe.CHEMICAL_WASHER, new WasherRecipe(input, output));
    }

    /**
     * Add a Chemical Crystallizer recipe.
     *
     * @param input  - input GasStack
     * @param output - output ItemStack
     */
    public static void addChemicalCrystallizerRecipe(GasStack input, ItemStack output) {
        addRecipe(Recipe.CHEMICAL_CRYSTALLIZER, new CrystallizerRecipe(input, output));
    }

    /**
     * Add a Pressurized Reaction Chamber recipe.
     *
     * @param inputSolid  - input ItemStack
     * @param inputFluid  - input FluidStack
     * @param inputGas    - input GasStack
     * @param outputSolid - output ItemStack
     * @param outputGas   - output GasStack
     * @param extraEnergy - extra energy needed by the recipe
     * @param ticks       - amount of ticks it takes for this recipe to complete
     */
    public static void addPRCRecipe(ItemStack inputSolid, FluidStack inputFluid, GasStack inputGas, ItemStack outputSolid, GasStack outputGas, double extraEnergy, int ticks) {
        addRecipe(Recipe.PRESSURIZED_REACTION_CHAMBER, new PressurizedRecipe(inputSolid, inputFluid, inputGas, outputSolid, outputGas, extraEnergy, ticks));
    }

    public static void addThermalEvaporationRecipe(FluidStack inputFluid, FluidStack outputFluid) {
        addRecipe(Recipe.THERMAL_EVAPORATION_PLANT, new ThermalEvaporationRecipe(inputFluid, outputFluid));
    }

    public static void addSolarNeutronRecipe(GasStack inputGas, GasStack outputGas) {
        addRecipe(Recipe.SOLAR_NEUTRON_ACTIVATOR, new SolarNeutronRecipe(inputGas, outputGas));
    }

    public static void addAmbientGas(int dimensionID, GasStack outputGas, double chance) {
        addRecipe(Recipe.AMBIENT_ACCUMULATOR, new AmbientGasRecipe(dimensionID, outputGas,chance));
        addRecipe(Recipe.AMBIENT_ACCUMULATOR_ENERGY, new AmbientGasRecipe(dimensionID, outputGas,chance));
    }


    /**
     * Add Start
     */

    public static void addIsotopicRecipe(GasStack inputGas, GasStack outputGas) {
        addRecipe(Recipe.ISOTOPIC_CENTRIFUGE, new IsotopicRecipe(inputGas, outputGas));
    }

    /**
     * Add a Nutritional Liquifier recipe.
     *
     * @param input  - input ItemStack
     * @param output - output GasStack
     */
    public static void addNutritionalLiquifierRecipe(ItemStack input, GasStack output) {
        addRecipe(Recipe.NUTRITIONAL_LIQUIFIER, new NutritionalRecipe(input, output));
    }

    public static void addOrganicFarmRecipe(ItemStack input, Gas gas, ItemStack primaryOutput, ItemStack secondaryOutput, double chance) {
        addRecipe(Recipe.ORGANIC_FARM, new FarmRecipe(input, gas, primaryOutput, secondaryOutput, chance));
    }


    public static void addOrganicFarmRecipe(ItemStack input, Gas gas, ItemStack primaryOutput) {
        addRecipe(Recipe.ORGANIC_FARM, new FarmRecipe(input, gas, primaryOutput));
    }


    public static void addNucleosynthesizerRecipe(ItemStack inputSolid, GasStack inputGas, ItemStack outputSolid, double extraEnergy, int ticks) {
        addRecipe(Recipe.ANTIPROTONIC_NUCLEOSYNTHESIZER, new NucleosynthesizerRecipe(inputSolid, inputGas, outputSolid, extraEnergy, ticks));
    }

    public static void addStampingRecipe(ItemStack input, ItemStack output) {
        addRecipe(Recipe.STAMPING, new StampingRecipe(input, output));
    }

    public static void addRollingRecipe(ItemStack input, ItemStack output) {
        addRecipe(Recipe.ROLLING, new RollingRecipe(input, output));
    }

    public static void addBrushedRecipe(ItemStack input, ItemStack output) {
        addRecipe(Recipe.BRUSHED, new BrushedRecipe(input, output));
    }

    public static void addTurningRecipe(ItemStack input, ItemStack output) {
        addRecipe(Recipe.TURNING, new TurningRecipe(input, output));
    }

    public static void addAlloyRecipe(ItemStack input, ItemStack extra, ItemStack output) {
        addRecipe(Recipe.ALLOY, new AlloyRecipe(input, extra, output));
    }

    public static void addCellExtractorRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, double chance) {
        addRecipe(Recipe.CELL_EXTRACTOR, new CellExtractorRecipe(input, primaryOutput, secondaryOutput, chance));
    }

    public static void addCellExtractorRecipe(ItemStack input, ItemStack primaryOutput) {
        addRecipe(Recipe.CELL_EXTRACTOR, new CellExtractorRecipe(input, primaryOutput));
    }

    public static void addCellSeparatorRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, double chance) {
        addRecipe(Recipe.CELL_SEPARATOR, new CellSeparatorRecipe(input, primaryOutput, secondaryOutput, chance));
    }

    public static void addCellSeparatorRecipe(ItemStack input, ItemStack primaryOutput) {
        addRecipe(Recipe.CELL_SEPARATOR, new CellSeparatorRecipe(input, primaryOutput));
    }

    public static void addRecyclerRecipe(ItemStack input, ItemStack primaryOutput, double chance) {
        addRecipe(Recipe.RECYCLER, new RecyclerRecipe(input, primaryOutput, chance));
    }

    public static void addSmeltingRecipe(ItemStack input, ItemStack output) {
        addRecipe(Recipe.ENERGIZED_SMELTER, new SmeltingRecipe(input, output));
    }

    public static void addFusionCoolingRecipe(FluidStack inputFluid, FluidStack outputFluid) {
        addRecipe(Recipe.FUSION_COOLING, new FusionCoolingRecipe(inputFluid, outputFluid));
    }

    /**
     * Add End
     */


    /**
     * Gets the Metallurgic Infuser Recipe for the InfusionInput in the parameters.
     *
     * @param input - input Infusion
     * @return MetallurgicInfuserRecipe
     */
    @Nullable
    public static MetallurgicInfuserRecipe getMetallurgicInfuserRecipe(@Nonnull InfusionInput input) {
        return getRecipe(input, Recipe.METALLURGIC_INFUSER);
    }

    /**
     * Gets the Chemical Infuser Recipe of the ChemicalPairInput in the parameters.
     *
     * @param input - the pair of gases to infuse
     * @return ChemicalInfuserRecipe
     */
    @Nullable
    public static ChemicalInfuserRecipe getChemicalInfuserRecipe(@Nonnull ChemicalPairInput input) {
        return getRecipe(input, Recipe.CHEMICAL_INFUSER);
    }

    /**
     * Gets the Chemical Crystallizer Recipe for the defined Gas input.
     *
     * @param input - GasInput
     * @return CrystallizerRecipe
     */
    @Nullable
    public static CrystallizerRecipe getChemicalCrystallizerRecipe(@Nonnull GasInput input) {
        return getRecipe(input, Recipe.CHEMICAL_CRYSTALLIZER);
    }

    /**
     * Gets the Chemical Washer Recipe for the defined Gas input.
     *
     * @param input - GasInput
     * @return WasherRecipe
     */
    @Nullable
    public static WasherRecipe getChemicalWasherRecipe(@Nonnull GasInput input) {
        return getRecipe(input, Recipe.CHEMICAL_WASHER);
    }

    /**
     * Gets the Chemical Dissolution Chamber of the ItemStackInput in the parameters
     *
     * @param input - ItemStackInput
     * @return DissolutionRecipe
     */
    @Nullable
    public static DissolutionRecipe getDissolutionRecipe(@Nonnull ItemStackInput input) {
        return getRecipe(input, Recipe.CHEMICAL_DISSOLUTION_CHAMBER);
    }

    /**
     * Gets the Chemical Oxidizer Recipe for the ItemStackInput in the parameters.
     *
     * @param input - ItemStackInput
     * @return OxidationRecipe
     */
    @Nullable
    public static OxidationRecipe getOxidizerRecipe(@Nonnull ItemStackInput input) {
        return getRecipe(input, Recipe.CHEMICAL_OXIDIZER);
    }

    /**
     * Gets the ChanceMachineRecipe of the ItemStackInput in the parameters, using the map in the parameters.
     *
     * @param input   - ItemStackInput
     * @param recipes - Map of recipes
     * @return ChanceRecipe
     */
    @Nullable
    public static <RECIPE extends ChanceMachineRecipe<RECIPE>> RECIPE getChanceRecipe(@Nonnull ItemStackInput input, @Nonnull Map<ItemStackInput, RECIPE> recipes) {
        return getRecipe(input, recipes);
    }


    @Nullable
    public static <RECIPE extends FarmMachineRecipe<RECIPE>> RECIPE getFarmRecipe(@Nonnull AdvancedMachineInput input, @Nonnull Map<AdvancedMachineInput, RECIPE> recipes) {
        return getRecipe(input, recipes);
    }


    /**
     * Gets the Recipe of the given Input in the parameters, using the map in the parameters.
     *
     * @param input   - Input
     * @param recipes - Map of recipes
     * @return Recipe
     */
    @Nullable
    public static <INPUT extends MachineInput<INPUT>, RECIPE extends MachineRecipe<INPUT, ?, RECIPE>>
    RECIPE getRecipe(@Nonnull INPUT input, @Nonnull Map<INPUT, RECIPE> recipes) {
        if (input.isValid()) {
            RECIPE recipe = recipes.get(input);
            if (recipe == null && input instanceof IWildInput) {
                //noinspection unchecked
                IWildInput<INPUT> wildInput = (IWildInput<INPUT>) input;
                recipe = recipes.get(wildInput.wildCopy());
            }
            return recipe == null ? null : recipe.copy();
        }
        return null;
    }

    @Nullable
    public static <INPUT extends MachineInput<INPUT>, RECIPE extends MachineRecipe<INPUT, ?, RECIPE>>
    RECIPE getRecipe(@Nonnull INPUT input, @Nonnull Recipe<INPUT, ?, RECIPE> type) {
        return getRecipe(input, type.get());
    }

    /**
     * Get the Electrolytic Separator Recipe corresponding to electrolysing a given fluid.
     *
     * @param input - the FluidInput to electrolyse fluid from
     * @return SeparatorRecipe
     */
    @Nullable
    public static SeparatorRecipe getElectrolyticSeparatorRecipe(@Nonnull FluidInput input) {
        return getRecipe(input, Recipe.ELECTROLYTIC_SEPARATOR);
    }

    @Nullable
    public static ThermalEvaporationRecipe getThermalEvaporationRecipe(@Nonnull FluidInput input) {
        return getRecipe(input, Recipe.THERMAL_EVAPORATION_PLANT);
    }

    @Nullable
    public static SolarNeutronRecipe getSolarNeutronRecipe(@Nonnull GasInput input) {
        return getRecipe(input, Recipe.SOLAR_NEUTRON_ACTIVATOR);
    }

    @Nullable
    public static PressurizedRecipe getPRCRecipe(@Nonnull PressurizedInput input) {
        return getRecipe(input, Recipe.PRESSURIZED_REACTION_CHAMBER);
    }

    @Nullable
    public static AmbientGasRecipe getDimensionGas(IntegerInput input) {
        return getRecipe(input, Recipe.AMBIENT_ACCUMULATOR);
    }

    /**
     * Add Start
     */

    @Nullable
    public static IsotopicRecipe getIsotopicRecipe(@Nonnull GasInput input) {
        return getRecipe(input, Recipe.ISOTOPIC_CENTRIFUGE);
    }

    @Nullable
    public static NucleosynthesizerRecipe getNucleosynthesizerRecipe(@Nonnull NucleosynthesizerInput input) {
        return getRecipe(input, Recipe.ANTIPROTONIC_NUCLEOSYNTHESIZER);
    }


    /**
     * Gets the Nutritional Liquifier Recipe for the ItemStackInput in the parameters.
     *
     * @param input - ItemStackInput
     * @return NutritionalRecipe
     */
    @Nullable
    public static NutritionalRecipe getNutritionalRecipe(@Nonnull ItemStackInput input) {
        return getRecipe(input, Recipe.NUTRITIONAL_LIQUIFIER);
    }

    @Nullable
    public static FusionCoolingRecipe getFusionCoolingRecipe(@Nonnull FluidInput input) {
        return getRecipe(input, Recipe.FUSION_COOLING);
    }

    @Nullable
    public static <RECIPE extends Chance2MachineRecipe<RECIPE>> RECIPE getChance2Recipe(@Nonnull ItemStackInput input, @Nonnull Map<ItemStackInput, RECIPE> recipes) {
        return getRecipe(input, recipes);
    }


    /**
     * Gets the whether the input ItemStack is in a recipe
     *
     * @param itemstack - input ItemStack
     * @param recipes   - Map of recipes
     * @return whether the item can be used in a recipe
     */
    public static <RECIPE extends MachineRecipe<ItemStackInput, ?, RECIPE>> boolean isInRecipe(@Nonnull ItemStack itemstack, @Nonnull Map<ItemStackInput, RECIPE> recipes) {
        if (!itemstack.isEmpty()) {
            for (RECIPE recipe : recipes.values()) {
                ItemStackInput required = recipe.getInput();
                NonNullList<ItemStack> list = NonNullList.create();
                list.add(itemstack);
                if (required.useItemStackFromInventory(list, 0, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInPressurizedRecipe(@Nonnull ItemStack stack) {
        if (!stack.isEmpty()) {
            for (PressurizedInput key : Recipe.PRESSURIZED_REACTION_CHAMBER.get().keySet()) {
                if (key.containsType(stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInNucleosynthesizerRecipe(@Nonnull ItemStack stack) {
        if (!stack.isEmpty()) {
            for (NucleosynthesizerInput key : Recipe.ANTIPROTONIC_NUCLEOSYNTHESIZER.get().keySet()) {
                if (key.containsType(stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static class Recipe<INPUT extends MachineInput<INPUT>, OUTPUT extends MachineOutput<OUTPUT>, RECIPE extends MachineRecipe<INPUT, OUTPUT, RECIPE>> {

        private static List<Recipe<?, ?, ?>> values = new ArrayList<>();

        public static final Recipe<ItemStackInput, ItemStackOutput, SmeltingRecipe> ENERGIZED_SMELTER = new Recipe<>(
                MachineType.ENERGIZED_SMELTER, ItemStackInput.class, ItemStackOutput.class, SmeltingRecipe.class);

        public static final Recipe<ItemStackInput, ItemStackOutput, EnrichmentRecipe> ENRICHMENT_CHAMBER = new Recipe<>(
                MachineType.ENRICHMENT_CHAMBER, ItemStackInput.class, ItemStackOutput.class, EnrichmentRecipe.class);

        public static final Recipe<AdvancedMachineInput, ItemStackOutput, OsmiumCompressorRecipe> OSMIUM_COMPRESSOR = new Recipe<>(
                MachineType.OSMIUM_COMPRESSOR, AdvancedMachineInput.class, ItemStackOutput.class, OsmiumCompressorRecipe.class);

        public static final Recipe<DoubleMachineInput, ItemStackOutput, CombinerRecipe> COMBINER = new Recipe<>(
                MachineType.COMBINER, DoubleMachineInput.class, ItemStackOutput.class, CombinerRecipe.class);

        public static final Recipe<ItemStackInput, ItemStackOutput, CrusherRecipe> CRUSHER = new Recipe<>(
                MachineType.CRUSHER, ItemStackInput.class, ItemStackOutput.class, CrusherRecipe.class);

        public static final Recipe<AdvancedMachineInput, ItemStackOutput, PurificationRecipe> PURIFICATION_CHAMBER = new Recipe<>(
                MachineType.PURIFICATION_CHAMBER, AdvancedMachineInput.class, ItemStackOutput.class, PurificationRecipe.class);

        public static final Recipe<InfusionInput, ItemStackOutput, MetallurgicInfuserRecipe> METALLURGIC_INFUSER = new Recipe<>(
                MachineType.METALLURGIC_INFUSER, InfusionInput.class, ItemStackOutput.class, MetallurgicInfuserRecipe.class);

        public static final Recipe<ChemicalPairInput, GasOutput, ChemicalInfuserRecipe> CHEMICAL_INFUSER = new Recipe<>(
                MachineType.CHEMICAL_INFUSER, ChemicalPairInput.class, GasOutput.class, ChemicalInfuserRecipe.class);

        public static final Recipe<ItemStackInput, GasOutput, OxidationRecipe> CHEMICAL_OXIDIZER = new Recipe<>(
                MachineType.CHEMICAL_OXIDIZER, ItemStackInput.class, GasOutput.class, OxidationRecipe.class);

        public static final Recipe<AdvancedMachineInput, ItemStackOutput, InjectionRecipe> CHEMICAL_INJECTION_CHAMBER = new Recipe<>(
                MachineType.CHEMICAL_INJECTION_CHAMBER, AdvancedMachineInput.class, ItemStackOutput.class, InjectionRecipe.class);

        public static final Recipe<FluidInput, ChemicalPairOutput, SeparatorRecipe> ELECTROLYTIC_SEPARATOR = new Recipe<>(
                MachineType.ELECTROLYTIC_SEPARATOR, FluidInput.class, ChemicalPairOutput.class, SeparatorRecipe.class);

        public static final Recipe<ItemStackInput, ChanceOutput, SawmillRecipe> PRECISION_SAWMILL = new Recipe<>(
                MachineType.PRECISION_SAWMILL, ItemStackInput.class, ChanceOutput.class, SawmillRecipe.class);

        public static final Recipe<ItemStackInput, GasOutput, DissolutionRecipe> CHEMICAL_DISSOLUTION_CHAMBER = new Recipe<>(
                MachineType.CHEMICAL_DISSOLUTION_CHAMBER, ItemStackInput.class, GasOutput.class, DissolutionRecipe.class);

        public static final Recipe<GasInput, GasOutput, WasherRecipe> CHEMICAL_WASHER = new Recipe<>(
                MachineType.CHEMICAL_WASHER, GasInput.class, GasOutput.class, WasherRecipe.class);

        public static final Recipe<GasInput, ItemStackOutput, CrystallizerRecipe> CHEMICAL_CRYSTALLIZER = new Recipe<>(
                MachineType.CHEMICAL_CRYSTALLIZER, GasInput.class, ItemStackOutput.class, CrystallizerRecipe.class);

        public static final Recipe<PressurizedInput, PressurizedOutput, PressurizedRecipe> PRESSURIZED_REACTION_CHAMBER = new Recipe<>(
                MachineType.PRESSURIZED_REACTION_CHAMBER, PressurizedInput.class, PressurizedOutput.class, PressurizedRecipe.class);

        public static final Recipe<IntegerInput, ChanceGasOutput, AmbientGasRecipe> AMBIENT_ACCUMULATOR = new Recipe<>(
                MachineType.AMBIENT_ACCUMULATOR, IntegerInput.class, ChanceGasOutput.class, AmbientGasRecipe.class);


        public static final Recipe<IntegerInput, ChanceGasOutput, AmbientGasRecipe> AMBIENT_ACCUMULATOR_ENERGY = new Recipe<>(
                MachineType.AMBIENT_ACCUMULATOR_ENERGY, IntegerInput.class, ChanceGasOutput.class, AmbientGasRecipe.class);


        public static final Recipe<FluidInput, FluidOutput, ThermalEvaporationRecipe> THERMAL_EVAPORATION_PLANT = new Recipe<>(
                "ThermalEvaporationPlant", FluidInput.class, FluidOutput.class, ThermalEvaporationRecipe.class);

        public static final Recipe<GasInput, GasOutput, SolarNeutronRecipe> SOLAR_NEUTRON_ACTIVATOR = new Recipe<>(
                MachineType.SOLAR_NEUTRON_ACTIVATOR, GasInput.class, GasOutput.class, SolarNeutronRecipe.class);

        /**
         * Add Start
         */

        public static final Recipe<GasInput, GasOutput, IsotopicRecipe> ISOTOPIC_CENTRIFUGE = new Recipe<>(
                MachineType.ISOTOPIC_CENTRIFUGE, GasInput.class, GasOutput.class, IsotopicRecipe.class);

        public static final Recipe<ItemStackInput, GasOutput, NutritionalRecipe> NUTRITIONAL_LIQUIFIER = new Recipe<>(
                MachineType.NUTRITIONAL_LIQUIFIER, ItemStackInput.class, GasOutput.class, NutritionalRecipe.class);

        public static final Recipe<AdvancedMachineInput, ChanceOutput, FarmRecipe> ORGANIC_FARM = new Recipe<>(
                MachineType.ORGANIC_FARM, AdvancedMachineInput.class, ChanceOutput.class, FarmRecipe.class);

        public static final Recipe<NucleosynthesizerInput, ItemStackOutput, NucleosynthesizerRecipe> ANTIPROTONIC_NUCLEOSYNTHESIZER = new Recipe<>(
                MachineType.ANTIPROTONIC_NUCLEOSYNTHESIZER, NucleosynthesizerInput.class, ItemStackOutput.class, NucleosynthesizerRecipe.class);

        public static final Recipe<ItemStackInput, ItemStackOutput, StampingRecipe> STAMPING = new Recipe<>(
                MachineType.STAMPING, ItemStackInput.class, ItemStackOutput.class, StampingRecipe.class);

        public static final Recipe<ItemStackInput, ItemStackOutput, RollingRecipe> ROLLING = new Recipe<>(
                MachineType.ROLLING, ItemStackInput.class, ItemStackOutput.class, RollingRecipe.class);

        public static final Recipe<ItemStackInput, ItemStackOutput, BrushedRecipe> BRUSHED = new Recipe<>(
                MachineType.BRUSHED, ItemStackInput.class, ItemStackOutput.class, BrushedRecipe.class);

        public static final Recipe<ItemStackInput, ItemStackOutput, TurningRecipe> TURNING = new Recipe<>(
                MachineType.TURNING, ItemStackInput.class, ItemStackOutput.class, TurningRecipe.class);

        public static final Recipe<DoubleMachineInput, ItemStackOutput, AlloyRecipe> ALLOY = new Recipe<>(
                MachineType.ALLOY, DoubleMachineInput.class, ItemStackOutput.class, AlloyRecipe.class);

        public static final Recipe<ItemStackInput, ChanceOutput, CellExtractorRecipe> CELL_EXTRACTOR = new Recipe<>(
                MachineType.CELL_EXTRACTOR, ItemStackInput.class, ChanceOutput.class, CellExtractorRecipe.class);

        public static final Recipe<ItemStackInput, ChanceOutput, CellSeparatorRecipe> CELL_SEPARATOR = new Recipe<>(
                MachineType.CELL_SEPARATOR, ItemStackInput.class, ChanceOutput.class, CellSeparatorRecipe.class);

        public static final Recipe<ItemStackInput, ChanceOutput2, RecyclerRecipe> RECYCLER = new Recipe<>(
                MachineType.RECYCLER, ItemStackInput.class, ChanceOutput2.class, RecyclerRecipe.class);

        public static final Recipe<FluidInput, FluidOutput, FusionCoolingRecipe> FUSION_COOLING = new Recipe<>(
                "FusionCooling", FluidInput.class, FluidOutput.class, FusionCoolingRecipe.class);
        /**
         * ADD END
         */
        static {
            values = ImmutableList.copyOf(values);
        }

        public static Iterable<Recipe<?, ?, ?>> values() {
            return values;
        }

        private final Object2ObjectOpenHashMap<INPUT, RECIPE> recipes = new Object2ObjectOpenHashMap<>();
        private final String recipeName;
        @Nonnull
        private final String jeiCategory;

        private Class<INPUT> inputClass;
        private Class<OUTPUT> outputClass;
        private Class<RECIPE> recipeClass;

        private Recipe(MachineType type, Class<INPUT> input, Class<OUTPUT> output, Class<RECIPE> recipe) {
            this(type.getBlockName(), input, output, recipe);
        }

        private Recipe(String name, Class<INPUT> input, Class<OUTPUT> output, Class<RECIPE> recipe) {
            recipeName = name;
            jeiCategory = "mekanism." + recipeName.toLowerCase(Locale.ROOT);

            inputClass = input;
            outputClass = output;
            recipeClass = recipe;

            values.add(this);
        }

        public void put(@Nonnull RECIPE recipe) {
            recipes.put(recipe.getInput(), recipe);
        }

        public void remove(@Nonnull RECIPE recipe) {
            recipes.remove(recipe.getInput());
        }

        public String getRecipeName() {
            return recipeName;
        }

        @Nonnull
        public String getJEICategory() {
            return jeiCategory;
        }

        @Nullable
        public INPUT createInput(NBTTagCompound nbtTags) {
            try {
                INPUT input = inputClass.newInstance();
                input.load(nbtTags);
                return input;
            } catch (Exception e) {
                return null;
            }
        }

        @Nullable
        public RECIPE createRecipe(INPUT input, NBTTagCompound nbtTags) {
            try {
                OUTPUT output = outputClass.newInstance();
                output.load(nbtTags);
                try {
                    Constructor<RECIPE> construct = recipeClass.getDeclaredConstructor(inputClass, outputClass);
                    return construct.newInstance(input, output);
                } catch (Exception e) {
                    Constructor<RECIPE> construct = recipeClass.getDeclaredConstructor(inputClass, outputClass, NBTTagCompound.class);
                    return construct.newInstance(input, output, nbtTags);
                }
            } catch (Exception e) {
                return null;
            }
        }

        public boolean containsRecipe(ItemStack input) {
            //TODO: Support other input types
            for (Entry<INPUT, RECIPE> entry : recipes.entrySet()) {
                if (entry.getKey() instanceof ItemStackInput) {
                    ItemStack stack = ((ItemStackInput) entry.getKey()).ingredient;
                    if (StackUtils.equalsWildcard(stack, input)) {
                        return true;
                    }
                } else if (entry.getKey() instanceof FluidInput) {
                    if (((FluidInput) entry.getKey()).ingredient.isFluidEqual(input)) {
                        return true;
                    }
                } else if (entry.getKey() instanceof AdvancedMachineInput) {
                    ItemStack stack = ((AdvancedMachineInput) entry.getKey()).itemStack;
                    if (StackUtils.equalsWildcard(stack, input)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean containsRecipe(Fluid input) {
            //TODO: Support other input types
            for (Entry<INPUT, RECIPE> entry : recipes.entrySet()) {
                if (entry.getKey() instanceof FluidInput) {
                    if (((FluidInput) entry.getKey()).ingredient.getFluid() == input) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean containsRecipe(Gas input) {
            //TODO: Support other input types
            for (Entry<INPUT, RECIPE> entry : recipes.entrySet()) {
                Gas toCheck = null;
                if (entry.getKey() instanceof GasInput) {
                    toCheck = ((GasInput) entry.getKey()).ingredient.getGas();
                } else if (entry.getKey() instanceof AdvancedMachineInput) {
                    toCheck = ((AdvancedMachineInput) entry.getKey()).gasType;
                }else if (entry.getKey() instanceof  PressurizedInput){
                    toCheck = ((PressurizedInput)entry.getKey()).getGas().getGas();
                }
                if (toCheck == input) {
                    return true;
                }
            }
            return false;
        }


        public Class<RECIPE> getRecipeClass() {
            return recipeClass;
        }

        // N.B. Must return a HashMap, not Map as Unidict expects the stronger type
        @Nonnull
        public Object2ObjectOpenHashMap<INPUT, RECIPE> get() {
            return recipes;
        }
    }
}
