package mekanism.multiblockmachine.client.jei.machine.other;

import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.machine.MekanismRecipeWrapper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.machines.DigitalAssemblyTableRecipe;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.multiblockmachine.common.MultiblockMachineItems;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DigitalAssemblyTableRecipeWrapper <RECIPE extends DigitalAssemblyTableRecipe> extends MekanismRecipeWrapper<RECIPE> {


    public DigitalAssemblyTableRecipeWrapper(RECIPE recipe) {
        super(recipe);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, recipe.recipeInput.itemInput);
        ingredients.setInput(VanillaTypes.ITEM, recipe.recipeInput.itemInput2);
        ingredients.setInput(VanillaTypes.ITEM, recipe.recipeInput.itemInput3);
        ingredients.setInput(VanillaTypes.ITEM, recipe.recipeInput.itemInput4);
        ingredients.setInput(VanillaTypes.ITEM, recipe.recipeInput.itemInput5);
        ingredients.setInput(VanillaTypes.ITEM, recipe.recipeInput.itemInput6);
        ingredients.setInput(VanillaTypes.ITEM, recipe.recipeInput.itemInput7);
        ingredients.setInput(VanillaTypes.ITEM, recipe.recipeInput.itemInput8);
        ingredients.setInput(VanillaTypes.ITEM, recipe.recipeInput.itemInput9);
        ingredients.setInput(VanillaTypes.ITEM, new ItemStack(MultiblockMachineItems.PlasmaCutterNozzles));
        ingredients.setInput(VanillaTypes.ITEM, new ItemStack(MultiblockMachineItems.DrillBit));
        ingredients.setInput(VanillaTypes.ITEM, new ItemStack(MultiblockMachineItems.LaserLenses));
        ingredients.setInput(VanillaTypes.FLUID, recipe.recipeInput.fluidInput);
        ingredients.setInput(MekanismJEI.TYPE_GAS, recipe.recipeInput.gasInput);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.recipeOutput.itemOutput);
        ingredients.setOutput(VanillaTypes.FLUID, recipe.recipeOutput.fluidOutput);
        ingredients.setOutput(MekanismJEI.TYPE_GAS, recipe.recipeOutput.gasOutput);
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        List<String> tooltip = new ArrayList<>();
        if (mouseX >= 12 && mouseX < 215 && mouseY >= 78  && mouseY < 6 + 81) {
            tooltip.add(LangUtils.localize("gui.using") + ":" + MekanismUtils.getEnergyDisplay(recipe.extraEnergy + MekanismConfig.current().multiblock.DigitalAssemblyTableUsage.val()) + "/t");
        }
        return tooltip;
    }
}

