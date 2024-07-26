package mekanism.multiblockmachine.common.tile;

import mekanism.api.Coord4D;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.util.MekanismUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class TileEntityMultiblockGasTank extends  TileEntityMidsizeGasTank{

    public TileEntityMultiblockGasTank() {
        super("MultiblockGasTank",819200*3,512000 * 3);
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
