package mekanism.generators.common.content.fission;

import mekanism.api.gas.GasStack;
import mekanism.generators.common.tile.fission.TileEntityFissionCasing;

import javax.annotation.Nullable;

public class FissionOutputGasTank extends FissionGasTank{
    public FissionOutputGasTank(TileEntityFissionCasing tileEntity) {
        super(tileEntity);
    }

    @Override
    @Nullable
    public GasStack getGas() {
        return multiblock.structure != null ? multiblock.structure.OutputGas : null;
    }

    @Override
    public void setGas(GasStack stack) {
        if (multiblock.structure != null) {
            multiblock.structure.OutputGas = stack;
        }
    }

    @Override
    public int getMaxGas() {
        return multiblock.structure != null ? multiblock.structure.steamVolume * FissionUpdateProtocol.STEAM_PER_TANK : 0;
    }
}
