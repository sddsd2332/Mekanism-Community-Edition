package mekanism.common.integration.crafttweaker.gas;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.liquid.ILiquidDefinition;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.common.integration.crafttweaker.CrafttweakerIntegration;
import net.minecraftforge.fluids.Fluid;

public class CraftTweakerGasDefinition implements IGasDefinition {

    private final Gas gas;

    public CraftTweakerGasDefinition(Gas gas) {
        this.gas = gas;
    }

    @Override
    public IGasStack asStack(int mb) {
        return new CraftTweakerGasStack(new GasStack(gas, mb));
    }

    @Override
    public String getName() {
        return gas.getName();
    }

    @Override
    public String getDisplayName() {
        return gas.getLocalizedName();
    }

    @Override
    public ILiquidDefinition getLiquid() {
        return CraftTweakerMC.getILiquidDefinition(gas.getFluid());
    }

    @Override
    public void setLiquid(ILiquidDefinition liquid) {
        CraftTweakerAPI.apply(new SetGasLiquidFormAction(gas, CraftTweakerMC.getFluid(liquid)));
    }

    @Override
    public int getTint() {
        return gas.getTint();
    }

    @Override
    public void setTint(int tint) {
        CraftTweakerAPI.apply(new SetGasTintAction(gas, tint));
    }

    public static class SetGasLiquidFormAction implements IAction {
        private final Gas gas;
        private final Fluid fluid;

        public SetGasLiquidFormAction(Gas gas, Fluid fluid) {
            this.gas = gas;
            this.fluid = fluid;
        }

        @Override
        public void apply() {
            gas.setFluid(fluid);
        }

        @Override
        public String describe() {
            return "Set the liquid form of " + gas.getName() + " to " + fluid.getName();
        }
    }

    public static class SetGasTintAction implements IAction {
        private final Gas gas;
        private final int tint;

        public SetGasTintAction(Gas gas, int tint) {
            this.gas = gas;
            this.tint = tint;
        }

        @Override
        public void apply() {
            gas.setTint(tint);
        }

        @Override
        public String describe() {
            return "Set tint of " + gas.getName() + " to #" + Integer.toHexString(tint);
        }
    }
}
