package mekanism.multiblockmachine.client.render.bloom.machine;

import mekanism.common.util.BloomEffect;
import mekanism.multiblockmachine.client.model.machine.ModelDigitalAssemblyTable;
import mekanism.multiblockmachine.client.render.machine.RenderDigitalAssemblyTable;
import mekanism.multiblockmachine.common.tile.machine.TileEntityDigitalAssemblyTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BloomRenderDigitalAssemblyTable extends BloomEffect<TileEntityDigitalAssemblyTable> {

    private final TileEntityDigitalAssemblyTable tile;

    public BloomRenderDigitalAssemblyTable(TileEntityDigitalAssemblyTable tile) {
        super(tile, 0, 180, 90, 270);
        this.tile = tile;
    }

    @Override
    protected void RenderModelBloom() {
        RenderDigitalAssemblyTable renderer = (RenderDigitalAssemblyTable) TileEntityRendererDispatcher.instance.renderers.get(TileEntityDigitalAssemblyTable.class);
        ModelDigitalAssemblyTable model = renderer.getModel();
        model.renderBloom(0.0625F, tile.getEnergy() != 0, tile.getActive(), Minecraft.getMinecraft().renderEngine, renderer.getTime());
    }
}
