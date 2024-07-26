package mekanism.multiblockmachine.client.jei;

import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.recipe.outputs.MachineOutput;
import mekanism.multiblockmachine.client.gui.machine.GuiDigitalAssemblyTable;
import mekanism.multiblockmachine.client.gui.machine.GuiLargeChemicalInfuser;
import mekanism.multiblockmachine.client.gui.machine.GuiLargeChemicalWasher;
import mekanism.multiblockmachine.client.gui.machine.GuiLargeElectrolyticSeparator;
import mekanism.multiblockmachine.client.jei.machine.other.DigitalAssemblyTableRecipeWrapper;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine.MultiblockMachineType;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

import java.util.stream.Collectors;

public class MultiblockRecipeRegistryHelper {

    public static void registerLargeSeparator(IModRegistry registry) {
        if (!MultiblockMachineType.LARGE_ELECTROLYTIC_SEPARATOR.isEnabled()) {
            return;
        }
        registry.addRecipeClickArea(GuiLargeElectrolyticSeparator.class, 80, 30, 16, 6, RecipeHandler.Recipe.ELECTROLYTIC_SEPARATOR.getJEICategory());
        registerRecipeItem(registry, MultiblockMachineType.LARGE_ELECTROLYTIC_SEPARATOR, RecipeHandler.Recipe.ELECTROLYTIC_SEPARATOR);
    }

    public static void registerLargeChemicalInfuser(IModRegistry registry) {
        if (!MultiblockMachineType.LARGE_CHEMICAL_INFUSER.isEnabled()) {
            return;
        }
        registry.addRecipeClickArea(GuiLargeChemicalInfuser.class, 47, 39 + 11, 28, 8, RecipeHandler.Recipe.CHEMICAL_INFUSER.getJEICategory());
        registry.addRecipeClickArea(GuiLargeChemicalInfuser.class, 101, 39 + 11, 28, 8, RecipeHandler.Recipe.CHEMICAL_INFUSER.getJEICategory());
        registerRecipeItem(registry, MultiblockMachineType.LARGE_CHEMICAL_INFUSER, RecipeHandler.Recipe.CHEMICAL_INFUSER);
    }

    public static void registerLargeChemicalWasher(IModRegistry registry) {
        if (!MultiblockMachineType.LARGE_CHEMICAL_WASHER.isEnabled()) {
            return;
        }
        registry.addRecipeClickArea(GuiLargeChemicalWasher.class, 61, 39, 55, 8, RecipeHandler.Recipe.CHEMICAL_WASHER.getJEICategory());
        registerRecipeItem(registry, MultiblockMachineType.LARGE_CHEMICAL_WASHER, RecipeHandler.Recipe.CHEMICAL_WASHER);
    }


    public static void registerDigitalAssemblyTable(IModRegistry registry) {
        if (!MultiblockMachineType.DIGITAL_ASSEMBLY_TABLE.isEnabled()) {
            return;
        }
        addRecipes(registry, RecipeHandler.Recipe.DIGITAL_ASSEMBLY_TABLE, DigitalAssemblyTableRecipeWrapper::new);
        registry.addRecipeClickArea(GuiDigitalAssemblyTable.class, 123, 37, 36, 10, RecipeHandler.Recipe.DIGITAL_ASSEMBLY_TABLE.getJEICategory());
        registerRecipeItem(registry, MultiblockMachineType.DIGITAL_ASSEMBLY_TABLE, RecipeHandler.Recipe.DIGITAL_ASSEMBLY_TABLE);
    }

    private static void registerRecipeItem(IModRegistry registry, MultiblockMachineType type, RecipeHandler.Recipe recipe) {
        registry.addRecipeCatalyst(type.getStack(), recipe.getJEICategory());
    }

    private static <INPUT extends MachineInput<INPUT>, OUTPUT extends MachineOutput<OUTPUT>, RECIPE extends MachineRecipe<INPUT, OUTPUT, RECIPE>>
    void addRecipes(IModRegistry registry, RecipeHandler.Recipe<INPUT, OUTPUT, RECIPE> type, IRecipeWrapperFactory<RECIPE> factory) {
        String recipeCategoryUid = type.getJEICategory();
        registry.handleRecipes(type.getRecipeClass(), factory, recipeCategoryUid);
        registry.addRecipes(type.get().values().stream().map(factory::getRecipeWrapper).collect(Collectors.toList()), recipeCategoryUid);
    }
}
