package mekanism.common.recipe.machines;

import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTank;
import mekanism.common.recipe.inputs.CompositeInput;
import mekanism.common.recipe.outputs.CompositeOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class DigitalAssemblyTableRecipe extends MachineRecipe<CompositeInput, CompositeOutput, DigitalAssemblyTableRecipe> {

    public double extraEnergy;

    public int ticks;

    public DigitalAssemblyTableRecipe(ItemStack input, ItemStack input2, ItemStack input3, ItemStack input4, ItemStack input5, ItemStack input6, ItemStack input7, ItemStack input8, ItemStack input9,
                                      FluidStack inputFluid, GasStack inputGas,
                                      ItemStack output, FluidStack outputFluid, GasStack outputGas, double energy, int duration) {
        this(new CompositeInput(
                input, input2, input3, input4, input5, input6, input7, input8, input9,
                inputFluid, inputGas), new CompositeOutput(output, outputFluid, outputGas), energy, duration);
    }

    public DigitalAssemblyTableRecipe(CompositeInput pressurizedInput, CompositeOutput pressurizedProducts, double energy, int duration) {
        super(pressurizedInput, pressurizedProducts);
        extraEnergy = energy;
        ticks = duration;
    }

    public DigitalAssemblyTableRecipe(CompositeInput pressurizedInput, CompositeOutput pressurizedProducts, NBTTagCompound extraNBT) {
        super(pressurizedInput, pressurizedProducts);
        extraEnergy = extraNBT.getDouble("extraEnergy");
        ticks = extraNBT.getInteger("duration");
    }

    @Override
    public DigitalAssemblyTableRecipe copy() {
        return new DigitalAssemblyTableRecipe(getInput().copy(), getOutput().copy(), extraEnergy, ticks);
    }

    public boolean canOperate(NonNullList<ItemStack> inventory,
                              int index, int index2, int index3, int index4, int index5, int index6, int index7, int index8, int index9,
                              FluidTank inputFluidTank, GasTank inputGasTank, int outputIndex, FluidTank outputFluidTank,GasTank outputGasTank) {
        return getInput().use(inventory, index, index2, index3, index4, index5, index6, index7, index8, index9, inputFluidTank, inputGasTank, false)
                && getOutput().applyOutputs(inventory, outputIndex, outputGasTank, false) && getOutput().applyFluidOutputs(outputFluidTank,false);
    }

    public void operate(NonNullList<ItemStack> inventory,
                        int index, int index2, int index3, int index4, int index5, int index6, int index7, int index8, int index9,
                        FluidTank inputFluidTank, GasTank inputGasTank, int outputIndex, FluidTank outputFluidTank,GasTank outputGasTank) {
        if (getInput().use(inventory, index, index2, index3, index4, index5, index6, index7, index8, index9,  inputFluidTank, inputGasTank,  true)) {
            getOutput().applyOutputs(inventory, outputIndex, outputGasTank, true);
            getOutput().applyFluidOutputs(outputFluidTank,true);
        }
    }
}
