package mekanism.multiblockmachine.client.jei.machine.other;

import mekanism.api.gas.GasStack;
import mekanism.client.gui.element.GuiProgress;
import mekanism.client.gui.element.GuiSlot;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEI;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.machines.DigitalAssemblyTableRecipe;
import mekanism.multiblockmachine.common.MultiblockMachineItems;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;

public class DigitalAssemblyTableRecipeCategory<WRAPPER extends DigitalAssemblyTableRecipeWrapper<DigitalAssemblyTableRecipe>> extends BaseRecipeCategory<WRAPPER> {

    public DigitalAssemblyTableRecipeCategory(IGuiHelper helper) {
        super(helper, "mekanism:gui/Null.png", RecipeHandler.Recipe.DIGITAL_ASSEMBLY_TABLE.getJEICategory(),
                "tile.MultiblockMachine.mekanismmultiblockmachine.name", GuiProgress.ProgressBar.MEDIUM, 4, 4, 221, 87);
    }

    @Override
    protected void addGuiElements() {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                guiElements.add(new GuiSlot(GuiSlot.SlotType.INPUT, this, guiLocation, 66 + x * 18, 15 + y * 18));
            }
        }
        for (int y = 0; y < 3; y++) {
            guiElements.add(new GuiSlot(GuiSlot.SlotType.EXTRA, this, guiLocation, 48, 15 + y * 18));
        }
        guiElements.add(new GuiSlot(GuiSlot.SlotType.OUTPUT, this, guiLocation, 162, 33));
        guiElements.add(GuiFluidGauge.getDummy(GuiGauge.Type.STANDARD, this, guiLocation, 6, 12).withColor(GuiGauge.TypeColor.RED));
        guiElements.add(GuiGasGauge.getDummy(GuiGauge.Type.STANDARD, this, guiLocation, 27, 12).withColor(GuiGauge.TypeColor.YELLOW));
        guiElements.add(GuiGasGauge.getDummy(GuiGauge.Type.STANDARD, this, guiLocation, 183, 12).withColor(GuiGauge.TypeColor.ORANGE));
        guiElements.add(GuiFluidGauge.getDummy(GuiGauge.Type.STANDARD, this, guiLocation, 204, 12).withColor(GuiGauge.TypeColor.BLUE));
        guiElements.add(new GuiProgress(new GuiProgress.IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return (float) timer.getValue() / 20F;
            }
        }, progressBar, this, guiLocation, 125, 38));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, WRAPPER recipeWrapper, IIngredients ingredients) {
        DigitalAssemblyTableRecipe tempRecipe = recipeWrapper.getRecipe();
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        itemStacks.init(0, true, 66 - xOffset, 15 - yOffset);
        itemStacks.init(1, true, 66 + 18 - xOffset, 15 - yOffset);
        itemStacks.init(2, true, 66 + 2 * 18 - xOffset, 15 - yOffset);
        itemStacks.init(3, true, 66 - xOffset, 15 + 18 - yOffset);
        itemStacks.init(4, true, 66 + 18 - xOffset, 15 + 18 - yOffset);
        itemStacks.init(5, true, 66 + 2 * 18 - xOffset, 15 + 18 - yOffset);
        itemStacks.init(6, true, 66 - xOffset, 15 + 2 * 18 - yOffset);
        itemStacks.init(7, true, 66 + 18 - xOffset, 15 + 2 * 18 - yOffset);
        itemStacks.init(8, true, 66 + 2 * 18 - xOffset, 15 + 2 * 18 - yOffset);

        itemStacks.init(9, true, 48 - xOffset, 15 - yOffset);
        itemStacks.init(10, true, 48 - xOffset, 15 + 18 - yOffset);
        itemStacks.init(11, true, 48 - xOffset, 15 + 2 * 18 - yOffset);
        itemStacks.init(12, false, 162 - xOffset, 33 - yOffset);

        itemStacks.set(0, tempRecipe.recipeInput.itemInput);
        itemStacks.set(1, tempRecipe.recipeInput.itemInput2);
        itemStacks.set(2, tempRecipe.recipeInput.itemInput3);
        itemStacks.set(3, tempRecipe.recipeInput.itemInput4);
        itemStacks.set(4, tempRecipe.recipeInput.itemInput5);
        itemStacks.set(5, tempRecipe.recipeInput.itemInput6);
        itemStacks.set(6, tempRecipe.recipeInput.itemInput7);
        itemStacks.set(7, tempRecipe.recipeInput.itemInput8);
        itemStacks.set(8, tempRecipe.recipeInput.itemInput9);

        itemStacks.set(9, new ItemStack(MultiblockMachineItems.PlasmaCutterNozzles));
        itemStacks.set(10, new ItemStack(MultiblockMachineItems.DrillBit));
        itemStacks.set(11, new ItemStack(MultiblockMachineItems.LaserLenses));

        itemStacks.set(12, tempRecipe.recipeOutput.itemOutput);

        IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
        fluidStacks.init(0, true, 3, 9, 16, 58, tempRecipe.getInput().fluidInput.amount, false, fluidOverlayLarge);
        fluidStacks.init(1, false, 201, 9, 16, 58, tempRecipe.getOutput().fluidOutput.amount, false, fluidOverlayLarge);
        fluidStacks.set(0, tempRecipe.recipeInput.fluidInput);
        fluidStacks.set(1, tempRecipe.recipeOutput.fluidOutput);
        IGuiIngredientGroup<GasStack> gasStacks = recipeLayout.getIngredientsGroup(MekanismJEI.TYPE_GAS);
        initGas(gasStacks, 0, true, 28 - xOffset, 13 - yOffset, 16, 58, tempRecipe.recipeInput.gasInput, true);
        initGas(gasStacks, 1, false, 184 - xOffset, 13 - yOffset, 16, 58, tempRecipe.recipeOutput.gasOutput, true);
    }
}
