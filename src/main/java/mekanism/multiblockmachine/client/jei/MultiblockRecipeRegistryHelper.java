package mekanism.multiblockmachine.client.jei;

import mekanism.common.recipe.RecipeHandler;
import mekanism.multiblockmachine.client.gui.machine.GuiLargeElectrolyticSeparator;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine.*;
import mezz.jei.api.IModRegistry;

public class MultiblockRecipeRegistryHelper {

    public static void registerLargeSeparator(IModRegistry registry) {
        if (!MultiblockMachineType.LARGE_ELECTROLYTIC_SEPARATOR.isEnabled()) {
            return;
        }
        registry.addRecipeClickArea(GuiLargeElectrolyticSeparator.class, 80, 30, 16, 6, RecipeHandler.Recipe.ELECTROLYTIC_SEPARATOR.getJEICategory());
        registerRecipeItem(registry,MultiblockMachineType.LARGE_ELECTROLYTIC_SEPARATOR, RecipeHandler.Recipe.ELECTROLYTIC_SEPARATOR);
    }



    private static void registerRecipeItem(IModRegistry registry,MultiblockMachineType type, RecipeHandler.Recipe recipe) {
        registry.addRecipeCatalyst(type.getStack(), recipe.getJEICategory());

    }

}
