package mekanism.client.jei.machine.other;

import mekanism.api.gas.GasStack;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEI;
import mekanism.common.MekanismLang;
import mekanism.common.util.MekanismUtils;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;

import java.util.ArrayList;
import java.util.List;

public class SPSRecipeCategory extends BaseRecipeCategory<SPSRecipeWrapper> {

    public SPSRecipeCategory(IGuiHelper helper) {
        super(helper, "mekanism:gui/Null.png", "mekanism.sps", "tile.MachineBlock4.sps.name", null, 3, 12, 168, 63);

    }

    @Override
    protected void addGuiElements() {
        guiElements.add(new GuiInnerScreen(this, guiLocation, 26, 13, 122, 60));
        guiElements.add(GuiGasGauge.getDummy(GuiGauge.Type.STANDARD, this, guiLocation, 6, 13));
        guiElements.add(GuiGasGauge.getDummy(GuiGauge.Type.STANDARD, this, guiLocation, 150, 13));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SPSRecipeWrapper spsRecipeWrapper, IIngredients iIngredients) {
        IGuiIngredientGroup<GasStack> gasStacks = recipeLayout.getIngredientsGroup(MekanismJEI.TYPE_GAS);
        initGas(gasStacks, 0, true, 7 - xOffset, 14 - yOffset, 16, 58, new GasStack(spsRecipeWrapper.getInputGas(), 1000), true);
        initGas(gasStacks, 1, false, 151 - xOffset, 14 - yOffset, 16, 58, new GasStack(spsRecipeWrapper.getOutputGas(), 1), true);
    }
}
