package mekanism.multiblockmachine.client.render.bloom.generator;

import mekanism.common.util.BloomEffect;
import mekanism.multiblockmachine.client.model.generator.ModelLargeHeatGenerator;
import mekanism.multiblockmachine.client.render.generator.RenderLargeHeatGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeHeatGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BloomRenderLargeHeatGenerator extends BloomEffect<TileEntityLargeHeatGenerator> {

    private final TileEntityLargeHeatGenerator tile;

    public BloomRenderLargeHeatGenerator(TileEntityLargeHeatGenerator tile) {
        super(tile,180, 0, 270, 90);
        this.tile = tile;
    }

    @Override
    protected void RenderModelBloom() {
        RenderLargeHeatGenerator renderer = (RenderLargeHeatGenerator) TileEntityRendererDispatcher.instance.renderers.get(TileEntityLargeHeatGenerator.class);
        ModelLargeHeatGenerator model = renderer.getModel();
        model.renderBloom(renderer.getTime(), 0.0625F, tile.getActive(), Minecraft.getMinecraft().renderEngine);
    }
}
