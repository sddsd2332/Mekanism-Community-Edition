package mekanism.multiblockmachine.client.render.bloom.generator;

import mekanism.common.util.BloomEffect;
import mekanism.multiblockmachine.client.model.generator.ModelLargeHeatGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeHeatGenerator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BloomRenderLargeHeatGenerator extends BloomEffect<TileEntityLargeHeatGenerator> {

    private final TileEntityLargeHeatGenerator tile;
    private ModelLargeHeatGenerator model = new ModelLargeHeatGenerator();

    public BloomRenderLargeHeatGenerator(TileEntityLargeHeatGenerator tile) {
        super(tile,180, 0, 270, 90);
        this.tile = tile;
    }

    @Override
    protected void RenderModelBloom() {
        model.renderBloom(getTime(), 0.0625F, tile.getActive(), Minecraft.getMinecraft().renderEngine);
    }
}
