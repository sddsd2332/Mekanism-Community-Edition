package mekanism.generators.common.content.fission;

import mekanism.generators.common.tile.fission.TileEntityFissionCasing;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class FissionWaterTank extends FissionTank{


    public FissionWaterTank(TileEntityFissionCasing tileEntity) {
        super(tileEntity);
    }

    @Override
    @Nullable
    public FluidStack getFluid() {
        return multiblock.structure != null ? multiblock.structure.waterStored : null;
    }

    @Override
    public void setFluid(FluidStack stack) {
        if (multiblock.structure != null) {
            multiblock.structure.waterStored = stack;
        }
    }

    @Override
    public int getCapacity() {
        return multiblock.structure != null ? multiblock.structure.waterVolume * FissionUpdateProtocol.WATER_PER_TANK : 0;
    }
}
