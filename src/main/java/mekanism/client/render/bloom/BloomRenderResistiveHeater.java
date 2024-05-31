package mekanism.client.render.bloom;

import mekanism.client.model.ModelResistiveHeater;
import mekanism.client.render.tileentity.RenderResistiveHeater;
import mekanism.common.tile.TileEntityResistiveHeater;
import mekanism.common.util.BloomEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BloomRenderResistiveHeater extends BloomEffect<TileEntityResistiveHeater> {

    private final TileEntityResistiveHeater tile;

    public BloomRenderResistiveHeater(TileEntityResistiveHeater tile) {
        super(tile, 0, 180, 90, 270);
        this.tile = tile;
    }

    @Override
    protected void RenderModelBloom() {
        RenderResistiveHeater renderer = (RenderResistiveHeater) TileEntityRendererDispatcher.instance.renderers.get(TileEntityResistiveHeater.class);
        ModelResistiveHeater model = renderer.getModel();
        model.renderBloom(0.0625F, tile.getActive(), Minecraft.getMinecraft().renderEngine);
    }
}
