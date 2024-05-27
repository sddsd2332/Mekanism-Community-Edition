package mekanism.generators.client.jei.machine.other;

import mekanism.client.jei.machine.MekanismRecipeWrapper;
import mekanism.common.recipe.machines.FusionCoolingRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;

public class FusionCoolingRecipeWrapper<RECIPE extends FusionCoolingRecipe> extends MekanismRecipeWrapper<RECIPE> {

    public FusionCoolingRecipeWrapper(RECIPE recipe) {
        super(recipe);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.FLUID, recipe.getInput().ingredient);
        ingredients.setOutput(VanillaTypes.FLUID, recipe.getOutput().output);
    }
}
