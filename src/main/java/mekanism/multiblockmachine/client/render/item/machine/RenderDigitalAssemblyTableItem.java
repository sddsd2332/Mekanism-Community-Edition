package mekanism.multiblockmachine.client.render.item.machine;

import mekanism.client.render.MekanismRenderer;
import mekanism.multiblockmachine.client.model.machine.ModelDigitalAssemblyTable;
import mekanism.multiblockmachine.common.util.MekanismMultiblockMachineUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderDigitalAssemblyTableItem {
    private static ModelDigitalAssemblyTable model = new ModelDigitalAssemblyTable();

    public static void renderStack(@Nonnull ItemStack stack, ItemCameraTransforms.TransformType transformType) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(180, 0, 0, 1);
        if (transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND) {
            GlStateManager.rotate(-90, 0, 1, 0);
        } else if (transformType != ItemCameraTransforms.TransformType.GUI) {
            GlStateManager.rotate(90, 0, 1, 0);
        }
        GlStateManager.translate(0, 0, 0);
        MekanismRenderer.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "DigitalAssemblyTable/DigitalAssemblyTable.png"));
        model.render(0.022F);
        GlStateManager.popMatrix();
    }
}
