package mekanism.multiblockmachine.client.render.bloom.machine;

import mekanism.common.util.BloomEffect;
import mekanism.multiblockmachine.client.model.machine.ModelLargeChemicalInfuser;
import mekanism.multiblockmachine.common.tile.machine.TileEntityLargeChemicalInfuser;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BloomRenderLargeChemicalInfuser extends BloomEffect<TileEntityLargeChemicalInfuser> {

    private final TileEntityLargeChemicalInfuser tile;
    private ModelLargeChemicalInfuser model = new ModelLargeChemicalInfuser();

    public BloomRenderLargeChemicalInfuser(TileEntityLargeChemicalInfuser tile) {
        super(tile, 0, 180, 90, 270);
        this.tile = tile;
    }

    @Override
    protected void RenderModelBloom() {
        model.renderBloom(getTime(), 0.0625F, tile.getActive(), Minecraft.getMinecraft().renderEngine, tile.getScaledGasTankLevel(), tile.getScaledLeftTankGasLevel(), tile.getScaledRightTankGasLevel());
    }
}
