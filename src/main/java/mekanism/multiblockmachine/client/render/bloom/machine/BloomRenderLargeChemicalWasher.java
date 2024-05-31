package mekanism.multiblockmachine.client.render.bloom.machine;

import mekanism.common.util.BloomEffect;
import mekanism.multiblockmachine.client.model.machine.ModelLargeChemicalWasher;
import mekanism.multiblockmachine.common.tile.machine.TileEntityLargeChemicalWasher;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BloomRenderLargeChemicalWasher extends BloomEffect<TileEntityLargeChemicalWasher> {

    private final TileEntityLargeChemicalWasher tile;
    private ModelLargeChemicalWasher model = new ModelLargeChemicalWasher();

    public BloomRenderLargeChemicalWasher(TileEntityLargeChemicalWasher tile) {
        super(tile, 0, 180, 90, 270);
        this.tile = tile;
    }

    @Override
    protected void RenderModelBloom() {
        model.renderBloom(getTime(), 0.0625F, tile.getActive(), Minecraft.getMinecraft().renderEngine);
    }
}
