package mekanism.common.recipe.machines;

import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTank;
import mekanism.common.recipe.inputs.PressurizedInput;
import mekanism.common.recipe.outputs.PressurizedOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class PressurizedRecipe extends MachineRecipe<PressurizedInput, PressurizedOutput, PressurizedRecipe> {

    public double extraEnergy;

    public int ticks;

    public PressurizedRecipe(ItemStack inputSolid, FluidStack inputFluid, GasStack inputGas, ItemStack outputSolid, GasStack outputGas, double energy, int duration) {
        this(new PressurizedInput(inputSolid, inputFluid, inputGas), new PressurizedOutput(outputSolid, outputGas), energy, duration);
    }

    public PressurizedRecipe(PressurizedInput pressurizedInput, PressurizedOutput pressurizedProducts, double energy, int duration) {
        super(pressurizedInput, pressurizedProducts);
        extraEnergy = energy;
        ticks = duration;
    }

    public PressurizedRecipe(PressurizedInput pressurizedInput, PressurizedOutput pressurizedProducts, NBTTagCompound extraNBT) {
        super(pressurizedInput, pressurizedProducts);
        extraEnergy = extraNBT.getDouble("extraEnergy");
        ticks = extraNBT.getInteger("duration");
    }

    @Override
    public PressurizedRecipe copy() {
        return new PressurizedRecipe(getInput().copy(), getOutput().copy(), extraEnergy, ticks);
    }

    public boolean canOperate(NonNullList<ItemStack> inventory, int inputIndex, FluidTank inputFluidTank, GasTank inputGasTank, GasTank outputGasTank, int outputIndex) {
        return getInput().use(inventory, inputIndex, inputFluidTank, inputGasTank, false) && getOutput().applyOutputs(inventory, outputIndex, outputGasTank, false);
    }

    public void operate(NonNullList<ItemStack> inventory, int inputIndex, FluidTank inputFluidTank, GasTank inputGasTank, GasTank outputGasTank, int outputIndex) {
        operate(inventory,inputIndex,inputFluidTank,inputGasTank,outputGasTank,outputIndex,true);
    }

    public void operate(NonNullList<ItemStack> inventory, int inputIndex, FluidTank inputFluidTank, GasTank inputGasTank, GasTank outputGasTank, int outputIndex,boolean deplete) {
        if (getInput().use(inventory, inputIndex, inputFluidTank, inputGasTank, deplete)) {
            getOutput().applyOutputs(inventory, outputIndex, outputGasTank, true);
        }
    }
}
