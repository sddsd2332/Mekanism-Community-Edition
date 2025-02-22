package mekanism.coremod;



import mekanism.common.event.ItemGUIRenderEvent;
import mekanism.common.interfaces.IOverlayRenderAware;
import mekanism.common.interfaces.IRenderEffectIntoGUI;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;

public class MekanismCoreMethods {

    public static void renderItemOverlayIntoGUI(@Nonnull ItemStack stack, int xPosition, int yPosition) {
        if (!stack.isEmpty()) {
            resetRenderState();

            if (stack.getItem() instanceof IOverlayRenderAware overlayRenderAware) {
                if (overlayRenderAware.renderItemOverlayIntoGUI(stack, xPosition, yPosition)){
                    resetRenderState();
                }
            }

            MinecraftForge.EVENT_BUS.post(new ItemGUIRenderEvent.Post(stack, xPosition, yPosition));
        }
    }

    private static void resetRenderState(){
        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

    public static void renderItemAndEffectIntoGUI(@Nonnull ItemStack stack, int xPosition, int yPosition) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof IRenderEffectIntoGUI effect) {
                effect.renderItemAndEffectIntoGUI(stack, xPosition, yPosition);
            }
            MinecraftForge.EVENT_BUS.post(new ItemGUIRenderEvent.Pre(stack, xPosition, yPosition));
        }
    }
}
