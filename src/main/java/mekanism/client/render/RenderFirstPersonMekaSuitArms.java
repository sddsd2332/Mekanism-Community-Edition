/**
 * https://github.com/mekanism/Mekanism/commit/8c15b6f
 * Modify based on the above submissions
 */
package mekanism.client.render;

import mekanism.client.model.mekasuitarmour.ModelMekAsuitBodyArm;
import mekanism.common.config.MekanismConfig;
import mekanism.common.item.armor.ItemMekAsuitBodyArmour;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class RenderFirstPersonMekaSuitArms {

    @SubscribeEvent
    public void renderHand(RenderSpecificHandEvent event) {
        if (!MekanismConfig.current().client.enableFirstPersonMekaSuitArms.val()) {
            return;
        }
        AbstractClientPlayer player = Minecraft.getMinecraft().player;
        if (player != null && !player.isInvisible() && !player.isSpectator()) {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (chestStack.getItem() instanceof ItemMekAsuitBodyArmour) {
                ItemStack stack = event.getItemStack();
                EnumHand hand = event.getHand();
                if (stack.isEmpty() && hand == EnumHand.MAIN_HAND) { //Only models with empty hands are rendered
                    renderArmFirstPerson(player, player.getPrimaryHand() == EnumHandSide.RIGHT, event.getSwingProgress(), event.getEquipProgress());
                    event.setCanceled(true);
                }
                //The map is not processed for the time being
            }
        }
    }

    private void renderArm(boolean rightHand, AbstractClientPlayer player) {
        ModelMekAsuitBodyArm armor = ModelMekAsuitBodyArm.armorModel;
        armor.setVisible(true);
        armor.rightArmPose = ModelBiped.ArmPose.EMPTY;
        GlStateManager.enableBlend();
        armor.isSneak = false;
        armor.swingProgress = 0.0F;
        armor.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
        MekanismRenderer.bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.RENDER, "MekAsuit.png"));
        if (rightHand) {
            armor.rightArmRender(0.0625F);
        } else {
            armor.leftArmRender(0.0625F);
        }
        GlStateManager.disableBlend();
    }

    /**
     * Copy of #renderArmFirstPerson but tweaked to render the MekaSuit's arm
     */

    private void renderArmFirstPerson(AbstractClientPlayer player, boolean rightHand, float swingProgress, float equipProgress) {
        float f = rightHand ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(swingProgress);
        float f2 = -0.3F * MathHelper.sin(f1 * (float) Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(swingProgress * (float) Math.PI);
        GlStateManager.translate(f * (f2 + 0.64000005F), f3 - 0.6F + equipProgress * -0.6F, f4 - 0.71999997F);
        GlStateManager.rotate(f * 45.0F, 0.0F, 1.0F, 0.0F);
        float f5 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        float f6 = MathHelper.sin(f1 * (float) Math.PI);
        GlStateManager.rotate(f * f6 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f * f5 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(f * -1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(f * 120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(f * 5.6F, 0.0F, 0.0F);
        GlStateManager.disableCull();
        renderArm(rightHand, player);
        GlStateManager.enableCull();
    }

}


