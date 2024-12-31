package mekanism.client.render.hud;

import mekanism.client.gui.element.GuiUtils;
import mekanism.common.item.armor.ItemMekaSuitArmor;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MekaSuitEnergyLevel {

    private static final ResourceLocation BAR = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_BAR, "Base2.png");
    private static final ResourceLocation POWER_BAR = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_BAR, "horizontal_power_long.png");

    public static void onDrawScreenPre(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.getType() == RenderGameOverlayEvent.ElementType.ARMOR) {
            double capacity = 0, stored = 0;
            for (ItemStack stack : mc.player.getArmorInventoryList()) {
                if (stack.getItem() instanceof ItemMekaSuitArmor armor) {
                    capacity += armor.getMaxEnergy(stack);
                    stored += armor.getEnergy(stack);
                }
            }
            if (capacity != 0) {
                int x = event.getResolution().getScaledWidth() / 2 - 91;
                int y = event.getResolution().getScaledHeight() - 59;
                int length = (int) Math.round((stored / capacity) * 79);
                GuiUtils.renderExtendedTexture(BAR, 2, 2, x, y, 81, 6);
                blit(POWER_BAR, x + 1, y + 1, length, 4, 0, 0, length, 4, 79, 4);
                mc.renderEngine.bindTexture(Gui.ICONS);
            }
        }
    }

    public static void blit(ResourceLocation pAtlasLocation, int pX, int pY, int pWidth, int pHeight, float pUOffset, float pVOffset, int pUWidth, int pVHeight, int pTextureWidth, int pTextureHeight) {
        blit(pAtlasLocation, pX, pX + pWidth, pY, pY + pHeight, 0, pUWidth, pVHeight, pUOffset, pVOffset, pTextureWidth, pTextureHeight);
    }


    static void blit(ResourceLocation pAtlasLocation, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, int pUWidth, int pVHeight, float pUOffset, float pVOffset, int pTextureWidth, int pTextureHeight) {
        innerBlit(pAtlasLocation, pX1, pX2, pY1, pY2, pBlitOffset, (pUOffset + 0.0F) / (float) pTextureWidth, (pUOffset + (float) pUWidth) / (float) pTextureWidth, (pVOffset + 0.0F) / (float) pTextureHeight, (pVOffset + (float) pVHeight) / (float) pTextureHeight);
    }

    static void innerBlit(ResourceLocation pAtlasLocation, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.renderEngine.bindTexture(pAtlasLocation);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((float) pX1, (float) pY1, (float) pBlitOffset).tex(pMinU, pMinV).endVertex();
        bufferbuilder.pos((float) pX1, (float) pY2, (float) pBlitOffset).tex(pMinU, pMaxV).endVertex();
        bufferbuilder.pos((float) pX2, (float) pY2, (float) pBlitOffset).tex(pMaxU, pMaxV).endVertex();
        bufferbuilder.pos((float) pX2, (float) pY1, (float) pBlitOffset).tex(pMaxU, pMinV).endVertex();
        tessellator.draw();
    }


}
