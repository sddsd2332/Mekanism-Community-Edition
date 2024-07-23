package mekanism.generators.common.content.fission;

import mekanism.api.Coord4D;
import mekanism.common.base.MultiblockFluidTank;
import mekanism.common.content.tank.SynchronizedTankData.ValveData;
import mekanism.generators.common.tile.fission.TileEntityFissionCasing;

public abstract class FissionTank extends MultiblockFluidTank<TileEntityFissionCasing> {

    public FissionTank(TileEntityFissionCasing tileEntity) {
        super(tileEntity);
    }

    @Override
    protected void updateValveData() {
        if (multiblock.structure != null) {
            Coord4D coord4D = Coord4D.get(multiblock);
            for (ValveData data : multiblock.structure.valves) {
                if (coord4D.equals(data.location)) {
                    data.onTransfer();
                }
            }
        }
    }

}
