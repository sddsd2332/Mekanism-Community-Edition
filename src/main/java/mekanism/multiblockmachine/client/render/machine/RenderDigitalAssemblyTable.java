package mekanism.multiblockmachine.client.render.machine;

import mekanism.client.Utils.RenderTileEntityTime;
import mekanism.client.render.MekanismRenderer;
import mekanism.multiblockmachine.client.model.machine.ModelDigitalAssemblyTable;
import mekanism.multiblockmachine.common.tile.machine.TileEntityDigitalAssemblyTable;
import mekanism.multiblockmachine.common.util.MekanismMultiblockMachineUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDigitalAssemblyTable extends RenderTileEntityTime<TileEntityDigitalAssemblyTable> {

    private ModelDigitalAssemblyTable model = new ModelDigitalAssemblyTable();

    @Override
    public void render(TileEntityDigitalAssemblyTable tileEntity, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "DigitalAssemblyTable/DigitalAssemblyTable.png"));
        MekanismRenderer.rotate(tileEntity.facing, 0, 180, 90, 270);
        GlStateManager.rotate(180, 0, 0, 1);
        model.render(0.0625F);
        GlStateManager.popMatrix();
    }


}
