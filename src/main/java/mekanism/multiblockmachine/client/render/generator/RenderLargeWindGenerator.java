package mekanism.multiblockmachine.client.render.generator;

import mekanism.client.Utils.RenderTileEntityTime;
import mekanism.client.render.MekanismRenderer;
import mekanism.multiblockmachine.client.model.generator.ModelLargeWindGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeWindGenerator;
import mekanism.multiblockmachine.common.util.MekanismMultiblockMachineUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderLargeWindGenerator extends RenderTileEntityTime<TileEntityLargeWindGenerator> {

    private ModelLargeWindGenerator model = new ModelLargeWindGenerator();

    @Override
    public void render(TileEntityLargeWindGenerator tileEntity, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER, "WindGenerator/LargeWindGenerator.png"));
        MekanismRenderer.rotate(tileEntity.facing, 0, 180, 90, 270);
        GlStateManager.rotate(180, 0, 0, 1);
        model.render(getTime(), 0.0625F, angle(tileEntity,partialTick), tileEntity.getActive(), rendererDispatcher.renderEngine,true);
        GlStateManager.popMatrix();
    }

    public double angle(TileEntityLargeWindGenerator tileEntity,float partialTick){
        double angle = tileEntity.getAngle();
        if (tileEntity.getActive()) {
            angle = (tileEntity.getAngle() + ((tileEntity.getPos().getY() + 46F) / TileEntityLargeWindGenerator.SPEED_SCALED) * partialTick) % 360;
        }
        return angle;
    }

    public ModelLargeWindGenerator getModel(){
        return model;
    }

    @Override
    public boolean isGlobalRenderer(TileEntityLargeWindGenerator te) {
        return true;
    }

}
