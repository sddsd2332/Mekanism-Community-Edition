package mekanism.multiblockmachine.client.render.bloom.generator;

import mekanism.common.util.BloomEffect;
import mekanism.multiblockmachine.client.model.generator.ModelLargeWindGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeWindGenerator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BloomRenderLargeWindGenerator extends BloomEffect<TileEntityLargeWindGenerator> {

    private final TileEntityLargeWindGenerator tile;
    private ModelLargeWindGenerator model = new ModelLargeWindGenerator();

    public BloomRenderLargeWindGenerator(TileEntityLargeWindGenerator tile) {
        super(tile, 0, 180, 90, 270);
        this.tile = tile;
    }

    @Override
    protected void RenderModelBloom() {
        model.renderBloom(getTime(), 0.0625F, tile.getActive(), Minecraft.getMinecraft().renderEngine);
    }
}
