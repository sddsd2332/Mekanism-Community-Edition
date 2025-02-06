package mekanism.common.recipe.machines;

import mekanism.common.recipe.inputs.FluidInput;
import mekanism.common.recipe.outputs.FluidOutput;
import net.minecraftforge.fluids.FluidStack;

public class FusionCoolingRecipe extends MachineRecipe<FluidInput, FluidOutput, FusionCoolingRecipe> {

    public double extraEnergy;

    public FusionCoolingRecipe(FluidStack input, FluidStack output) {
        this(input, output, 1);
    }

    public FusionCoolingRecipe(FluidStack input, FluidStack output, double energy) {
        super(new FluidInput(input), new FluidOutput(output));
        extraEnergy = energy;
    }

    public FusionCoolingRecipe(FluidInput input, FluidOutput output, double energy) {
        super(input, output);
        extraEnergy = energy;
    }


    @Override
    public FusionCoolingRecipe copy() {
        return new FusionCoolingRecipe(getInput().copy(), getOutput().copy(), extraEnergy);
    }


}
