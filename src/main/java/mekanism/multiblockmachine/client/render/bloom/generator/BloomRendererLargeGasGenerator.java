package mekanism.multiblockmachine.client.render.bloom.generator;

import mekanism.common.util.BloomEffect;
import mekanism.multiblockmachine.client.model.generator.ModelLargeGasGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeGasGenerator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BloomRendererLargeGasGenerator extends BloomEffect<TileEntityLargeGasGenerator> {

    private final ModelLargeGasGenerator model = new ModelLargeGasGenerator();
    private final TileEntityLargeGasGenerator gasGenerator;

    public BloomRendererLargeGasGenerator(TileEntityLargeGasGenerator gasGenerator) {
        super(gasGenerator,0, 180, 90, 270);
        this.gasGenerator = gasGenerator;
    }

    @Override
    protected void RenderModelBloom() {
        model.renderBloom(getTime(), 0.0625F, tile.getActive(), Minecraft.getMinecraft().renderEngine);
    }

}
