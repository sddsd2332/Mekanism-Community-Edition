package mekanism.client.render.bloom;

import mekanism.client.model.ModelSecurityDesk;
import mekanism.client.render.tileentity.RenderSecurityDesk;
import mekanism.common.tile.TileEntitySecurityDesk;
import mekanism.common.util.BloomEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BloomRenderSecurityDesk extends BloomEffect<TileEntitySecurityDesk> {

    private final TileEntitySecurityDesk tile;

    public BloomRenderSecurityDesk(TileEntitySecurityDesk tile) {
        super(tile, 0, 180, 90, 270);
        this.tile = tile;
    }


    @Override
    protected void RenderModelBloom() {
        RenderSecurityDesk renderer = (RenderSecurityDesk) TileEntityRendererDispatcher.instance.renderers.get(TileEntitySecurityDesk.class);
        ModelSecurityDesk model = renderer.getModel();
        model.renderBloom(0.0625F, Minecraft.getMinecraft().renderEngine);
    }
}
