package mekanism.multiblockmachine.client.render.item.generator;

import mekanism.client.render.MekanismRenderer;
import mekanism.common.config.MekanismConfig;
import mekanism.multiblockmachine.client.model.generator.ModelLargeWindGenerator;
import mekanism.multiblockmachine.common.util.MekanismMultiblockMachineUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;


@SideOnly(Side.CLIENT)
public class RenderLargeWindGeneratorItem {

    private static ModelLargeWindGenerator windGenerator = new ModelLargeWindGenerator();
    private static double angle = 0;
    private static float lastTicksUpdated = 0;

    public static void renderStack(@Nonnull ItemStack stack, TransformType transformType) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(180, 0, 0, 1);
        if (transformType == TransformType.THIRD_PERSON_RIGHT_HAND || transformType == TransformType.THIRD_PERSON_LEFT_HAND) {
            GlStateManager.rotate(180, 0, 1, 0);
            GlStateManager.translate(0, 0.4F, 0);
            if (transformType == TransformType.THIRD_PERSON_LEFT_HAND) {
                GlStateManager.rotate(-45, 0, 1, 0);
            } else {
                GlStateManager.rotate(45, 0, 1, 0);
            }
            GlStateManager.rotate(50, 1, 0, 0);
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            GlStateManager.translate(0, -0.4F, 0);
        } else {
            if (transformType == TransformType.GUI) {
                GlStateManager.rotate(90, 0, 1, 0);
                GlStateManager.translate(0,0.4F,0);
            } else if (transformType == TransformType.FIRST_PERSON_RIGHT_HAND) {
                GlStateManager.rotate(180, 0, 1, 0);
            }
            GlStateManager.translate(0, 0.4F, 0);
        }
        MekanismRenderer.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER, "WindGenerator/LargeWindGenerator.png"));

        if (MekanismConfig.current().generators.windGeneratorItem.val()) {
            if (lastTicksUpdated != Minecraft.getMinecraft().getRenderPartialTicks()) {
                angle = (angle + 2) % 360;
                lastTicksUpdated = Minecraft.getMinecraft().getRenderPartialTicks();
            }
        } else {
            angle = 0;
        }
        windGenerator.render(0,0.002F, angle,false,Minecraft.getMinecraft().renderEngine);
        GlStateManager.popMatrix();
    }
}
