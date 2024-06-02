package mekanism.multiblockmachine.client.render.bloom.machine;

import mekanism.common.util.BloomEffect;
import mekanism.multiblockmachine.client.model.machine.ModelLargeElectrolyticSeparator;
import mekanism.multiblockmachine.client.render.machine.RenderLargeElectrolyticSeparator;
import mekanism.multiblockmachine.common.tile.machine.TileEntityLargeElectrolyticSeparator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BloomRenderLargeElectrolyticSeparator extends BloomEffect<TileEntityLargeElectrolyticSeparator> {

    private final TileEntityLargeElectrolyticSeparator tile;
    private ModelLargeElectrolyticSeparator model = new ModelLargeElectrolyticSeparator();

    public BloomRenderLargeElectrolyticSeparator(TileEntityLargeElectrolyticSeparator tile) {
        super(tile, 0, 180, 90, 270);
        this.tile = tile;
    }

    @Override
    protected void RenderModelBloom() {
        RenderLargeElectrolyticSeparator renderer = (RenderLargeElectrolyticSeparator) TileEntityRendererDispatcher.instance.renderers.get(TileEntityLargeElectrolyticSeparator.class);
        ModelLargeElectrolyticSeparator model = renderer.getModel();
        model.renderBloom(renderer.getTime(), 0.0625F, tile.getActive(), Minecraft.getMinecraft().renderEngine, tile.getScaledFluidTankLevel(), tile.getScaledLeftTankGasLevel(), tile.getScaledRightTankGasLevel());
    }


}
