package mekanism.common.content.evaporation;

import mekanism.api.IHeatTransfer;
import mekanism.common.multiblock.SynchronizedData;
import net.minecraft.util.EnumFacing;

public class SynchronizedEvaporationData  extends SynchronizedData<SynchronizedEvaporationData>  implements IHeatTransfer {


    @Override
    public double getTemp() {
        return 0;
    }

    @Override
    public double getInverseConductionCoefficient() {
        return 1;
    }

    @Override
    public double getInsulationCoefficient(EnumFacing side) {
        return 0;
    }

    @Override
    public void transferHeatTo(double heat) {

    }

    @Override
    public double[] simulateHeat() {
        return new double[0];
    }

    @Override
    public double applyTemperatureChange() {
        return 0;
    }

    @Override
    public boolean canConnectHeat(EnumFacing side) {
        return false;
    }

    @Override
    public IHeatTransfer getAdjacent(EnumFacing side) {
        return null;
    }
}
