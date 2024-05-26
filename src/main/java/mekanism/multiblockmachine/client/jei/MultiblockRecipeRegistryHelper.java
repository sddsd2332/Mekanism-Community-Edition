package mekanism.multiblockmachine.client.jei;

import mekanism.common.recipe.RecipeHandler;
import mekanism.multiblockmachine.client.gui.machine.GuiLargeChemicalInfuser;
import mekanism.multiblockmachine.client.gui.machine.GuiLargeElectrolyticSeparator;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine.MultiblockMachineType;
import mezz.jei.api.IModRegistry;

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


    private static void registerRecipeItem(IModRegistry registry, MultiblockMachineType type, RecipeHandler.Recipe recipe) {
        registry.addRecipeCatalyst(type.getStack(), recipe.getJEICategory());

    }

}
