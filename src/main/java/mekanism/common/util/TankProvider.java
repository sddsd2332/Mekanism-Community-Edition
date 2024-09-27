package mekanism.common.util;

import mekanism.api.gas.GasTank;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface TankProvider {

    int getTankCapacity();

    int getTankAmount();

    class Fluid implements TankProvider {

        private final FluidTank handler;

        public Fluid(final FluidTank handler) {
            this.handler = handler;
        }

        @Override
        public int getTankCapacity() {
            return handler.getCapacity();
        }

        @Override
        public int getTankAmount() {
            return handler.getFluidAmount();
        }
    }

    class Gas implements TankProvider {
        
        private final GasTank handler;

        public Gas(final GasTank handler) {
            this.handler = handler;
        }

        @Override
        public int getTankCapacity() {
            return handler.getMaxGas();
        }

        @Override
        public int getTankAmount() {
            return handler.getStored();
        }

    }

}
