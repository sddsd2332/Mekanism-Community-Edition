package mekanism.client.jei.machine.other;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.client.SpecialColors;
import mekanism.client.jei.MekanismJEI;
import mekanism.common.MekanismLang;
import mekanism.common.util.MekanismUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SPSRecipeWrapper implements IRecipeWrapper {

    private Gas inputGas;
    private Gas outputGas;

    public SPSRecipeWrapper(Gas inputGas, Gas outputGas) {
        this.inputGas = inputGas;
        this.outputGas = outputGas;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(MekanismJEI.TYPE_GAS, new GasStack(inputGas, 1));
        ingredients.setInput(MekanismJEI.TYPE_GAS, new GasStack(outputGas, 1));
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        List<String> list = new ArrayList<>();
        list.add(MekanismLang.STATUS.getTranslationKey() + MekanismLang.ACTIVE.getTranslationKey());
        list.add(MekanismLang.SPS_ENERGY_INPUT.getTranslationKey() + MekanismUtils.getEnergyDisplay(1000 * 1000000));
        list.add(MekanismLang.PROCESS_RATE_MB.getTranslationKey() + 1.0 + "mB/t");
        int listSize = list.size();
        int totalHeight = listSize * 8 + 3 * (listSize - 1);
        int startY = (60 - totalHeight) / 2 + 4;
        for (String text : list) {
            minecraft.fontRenderer.drawString(text, 26 + 3, startY, SpecialColors.TEXT_SCREEN.argb());
            startY += 8 + 3;
        }
    }

    public Gas getInputGas() {
        return inputGas;
    }

    public Gas getOutputGas() {
        return outputGas;
    }
}
