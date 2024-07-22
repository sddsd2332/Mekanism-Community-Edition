package mekanism.common.recipe.machines;

import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.outputs.ChanceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public abstract class ChanceMachineRecipe<RECIPE extends ChanceMachineRecipe<RECIPE>> extends MachineRecipe<ItemStackInput, ChanceOutput, RECIPE> {

    public ChanceMachineRecipe(ItemStackInput input, ChanceOutput output) {
        super(input, output);
    }

    public boolean inputMatches(NonNullList<ItemStack> inventory, int inputIndex) {
        return getInput().useItemStackFromInventory(inventory, inputIndex, false);
    }

    public boolean canOperate(NonNullList<ItemStack> inventory, int inputIndex, int primaryIndex, int secondaryIndex) {
        return inputMatches(inventory, inputIndex) && getOutput().applyOutputs(inventory, primaryIndex, secondaryIndex, false);
    }

    public void operate(NonNullList<ItemStack> inventory, int inputIndex, int primaryIndex, int secondaryIndex) {
        operate(inventory, inputIndex, primaryIndex, secondaryIndex, true);
    }

    public void operate(NonNullList<ItemStack> inventory, int inputIndex, int primaryIndex, int secondaryIndex, boolean deplete) {
        if (getInput().useItemStackFromInventory(inventory, inputIndex, deplete)) {
            getOutput().applyOutputs(inventory, primaryIndex, secondaryIndex, true);
        }
    }
}
