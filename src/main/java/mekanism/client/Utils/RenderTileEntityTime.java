package mekanism.client.Utils;

import mekanism.api.util.time.Timeticks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTileEntityTime<T extends TileEntity> extends TileEntitySpecialRenderer<T> {

    private Timeticks time = new Timeticks(20, 20, false);

    public double getTime() {
        return time.getValue() / 20F;
    }

}
