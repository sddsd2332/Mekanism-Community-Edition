package mekanism.multiblockmachine.common.tile;

import mekanism.api.Coord4D;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.MekanismUtils;

public class TileEntityMultiblockGasTank extends  TileEntityMidsizeGasTank{

    public TileEntityMultiblockGasTank() {
        super("MultiblockGasTank", MekanismConfig.current().multiblock.MultiblockGasTankStorage.val(), MekanismConfig.current().multiblock.MultiblockGasTankOutput.val());
    }

    @Override
    public void onPlace() {
        super.onPlace();
        Coord4D current = Coord4D.get(this);
        MekanismUtils.makeBoundingBlock(world, getPos().down(), current);
    }


    @Override
    public void onBreak() {
        super.onBreak();
        world.setBlockToAir(getPos().down());
    }

}
