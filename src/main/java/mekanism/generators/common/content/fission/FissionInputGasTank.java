package mekanism.generators.common.content.fission;

import mekanism.api.gas.GasStack;
import mekanism.generators.common.tile.fission.TileEntityFissionCasing;


import javax.annotation.Nullable;

public class FissionInputGasTank extends FissionGasTank{
    public FissionInputGasTank(TileEntityFissionCasing tileEntity) {
        super(tileEntity);
    }

    @Override
    @Nullable
    public GasStack getGas() {
        return multiblock.structure != null ? multiblock.structure.InputGas : null;
    }

    @Override
    public void setGas(GasStack stack) {
        if (multiblock.structure != null) {
            multiblock.structure.InputGas = stack;
        }
    }

    @Override
    public int getMaxGas() {
        return multiblock.structure != null ? multiblock.structure.waterVolume * FissionUpdateProtocol.WATER_PER_TANK : 0;
    }
}
