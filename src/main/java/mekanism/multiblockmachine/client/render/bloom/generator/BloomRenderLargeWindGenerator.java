package mekanism.multiblockmachine.client.render.bloom.generator;

import mekanism.common.util.BloomEffect;
import mekanism.multiblockmachine.client.model.generator.ModelLargeWindGenerator;
import mekanism.multiblockmachine.client.render.generator.RenderLargeWindGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeWindGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BloomRenderLargeWindGenerator extends BloomEffect<TileEntityLargeWindGenerator> {

    private final TileEntityLargeWindGenerator tile;

    public BloomRenderLargeWindGenerator(TileEntityLargeWindGenerator tile) {
        super(tile, 0, 180, 90, 270);
        this.tile = tile;
    }

    @Override
    protected void RenderModelBloom() {
        RenderLargeWindGenerator renderer = (RenderLargeWindGenerator) TileEntityRendererDispatcher.instance.renderers.get(TileEntityLargeWindGenerator.class);
        ModelLargeWindGenerator model = renderer.getModel();
        model.renderBloom(renderer.getTime(), 0.0625F, renderer.angle(tile, Minecraft.getMinecraft().isGamePaused() ? 0 : Minecraft.getMinecraft().getRenderPartialTicks()), tile.getActive(), Minecraft.getMinecraft().renderEngine);
    }


}
