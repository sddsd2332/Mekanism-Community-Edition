package mekanism.common.recipe.machines;

import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTank;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.outputs.GasOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class OxidationRecipe extends MachineRecipe<ItemStackInput, GasOutput, OxidationRecipe> {

    public OxidationRecipe(ItemStackInput input, GasOutput output) {
        super(input, output);
    }

    public OxidationRecipe(ItemStack input, GasStack output) {
        this(new ItemStackInput(input), new GasOutput(output));
    }

    @Override
    public OxidationRecipe copy() {
        return new OxidationRecipe(getInput().copy(), getOutput().copy());
    }

    public boolean canOperate(NonNullList<ItemStack> inventory, int inputIndex, GasTank outputTank) {
        return getInput().useItemStackFromInventory(inventory, inputIndex, false) && getOutput().applyOutputs(outputTank, false, 1);
    }

    public void operate(NonNullList<ItemStack> inventory, int inputIndex, GasTank outputTank) {
        operate(inventory,inputIndex,outputTank,true);
    }

    public void operate(NonNullList<ItemStack> inventory, int inputIndex, GasTank outputTank,boolean deplete) {
        if (getInput().useItemStackFromInventory(inventory, inputIndex, deplete)) {
            getOutput().applyOutputs(outputTank, true, 1);
        }
    }
}
