package mekanism.common.recipe.machines;

import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTank;
import mekanism.common.recipe.inputs.GasInput;
import mekanism.common.recipe.outputs.ItemStackOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CrystallizerRecipe extends MachineRecipe<GasInput, ItemStackOutput, CrystallizerRecipe> {

    public CrystallizerRecipe(GasInput input, ItemStackOutput output) {
        super(input, output);
    }

    public CrystallizerRecipe(GasStack input, ItemStack output) {
        this(new GasInput(input), new ItemStackOutput(output));
    }

    public boolean canOperate(GasTank gasTank, NonNullList<ItemStack> inventory, int outputIndex) {
        return getInput().useGas(gasTank, false, 1) && getOutput().applyOutputs(inventory, outputIndex, false);
    }

    public void operate(GasTank inputTank, NonNullList<ItemStack> inventory, int outputIndex) {
        operate(inputTank,inventory,outputIndex,true);
    }

    public void operate(GasTank inputTank, NonNullList<ItemStack> inventory, int outputIndex, boolean deplete) {
        if (getInput().useGas(inputTank, deplete, 1)) {
            getOutput().applyOutputs(inventory, outputIndex, true);
        }
    }

    @Override
    public CrystallizerRecipe copy() {
        return new CrystallizerRecipe(getInput().copy(), getOutput().copy());
    }
}
