package mekanism.common.recipe.machines;

import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.outputs.ChanceOutput2;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public abstract class Chance2MachineRecipe<RECIPE extends Chance2MachineRecipe<RECIPE>> extends MachineRecipe<ItemStackInput, ChanceOutput2, RECIPE> {

    public Chance2MachineRecipe(ItemStackInput input, ChanceOutput2 output) {
        super(input, output);
    }

    public boolean inputMatches(NonNullList<ItemStack> inventory, int inputIndex) {
        return getInput().useItemStackFromInventory(inventory, inputIndex, false);
    }

    public boolean canOperate(NonNullList<ItemStack> inventory, int inputIndex, int primaryIndex) {
        return inputMatches(inventory, inputIndex) && getOutput().applyOutputs(inventory, primaryIndex, false);
    }
    public void operate(NonNullList<ItemStack> inventory, int inputIndex, int primaryIndex) {
        operate(inventory,inputIndex,primaryIndex,true);
    }

    public void operate(NonNullList<ItemStack> inventory, int inputIndex, int primaryIndex, boolean deplete) {
        if (getInput().useItemStackFromInventory(inventory, inputIndex, deplete)) {
            getOutput().applyOutputs(inventory, primaryIndex, true);
        }
    }
}
