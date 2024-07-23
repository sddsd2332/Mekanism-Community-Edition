package mekanism.generators.common.content.fission;

import mekanism.generators.common.tile.fission.TileEntityFissionCasing;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class FissionSteamTank extends FissionTank{

    public FissionSteamTank(TileEntityFissionCasing tileEntity) {
        super(tileEntity);
    }

    @Override
    @Nullable
    public FluidStack getFluid() {
        return multiblock.structure != null ? multiblock.structure.steamStored : null;
    }

    @Override
    public void setFluid(FluidStack stack) {
        if (multiblock.structure != null) {
            multiblock.structure.steamStored = stack;
        }
    }

    @Override
    public int getCapacity() {
        return multiblock.structure != null ? multiblock.structure.steamVolume * FissionUpdateProtocol.STEAM_PER_TANK : 0;
    }
}
