package mekanism.generators.client.jei.machine.other;

import mekanism.client.gui.element.GuiProgress;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.machines.FusionCoolingRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;

public class FusionCoolingRecipeCategory<WRAPPER extends FusionCoolingRecipeWrapper<FusionCoolingRecipe>> extends BaseRecipeCategory<WRAPPER> {

    public FusionCoolingRecipeCategory(IGuiHelper helper) {
        super(helper, "mekanism:gui/Null.png",
                Recipe.FUSION_COOLING.getJEICategory(), "gui.FusionCooling", ProgressBar.LARGE_RIGHT, 24, 12, 130, 63);
    }

    @Override
    protected void addGuiElements() {
        guiElements.add(GuiFluidGauge.getDummy(GuiGauge.Type.STANDARD, this, guiLocation, 25, 13).withColor(GuiGauge.TypeColor.RED));
        guiElements.add(GuiFluidGauge.getDummy(GuiGauge.Type.STANDARD, this, guiLocation, 133, 13).withColor(GuiGauge.TypeColor.BLUE));
        guiElements.add(new GuiProgress(new GuiProgress.IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return (float) timer.getValue() / 20F;
            }
        }, progressBar, this, guiLocation, 62, 38));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, WRAPPER recipeWrapper, IIngredients ingredients) {
        FusionCoolingRecipe tempRecipe = recipeWrapper.getRecipe();
        IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
        fluidStacks.init(0, true, 26 - xOffset, 14 - yOffset, 16, 58, tempRecipe.getInput().ingredient.amount, false, fluidOverlayLarge);
        fluidStacks.init(1, false, 134 - xOffset, 14 - yOffset, 16, 58, tempRecipe.getOutput().output.amount, false, fluidOverlayLarge);
        fluidStacks.set(0, tempRecipe.recipeInput.ingredient);
        fluidStacks.set(1, tempRecipe.recipeOutput.output);
    }
}
