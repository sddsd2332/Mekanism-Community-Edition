/**
 * https://github.com/mekanism/Mekanism/commit/8c15b6f9ecb697b0093f3964a13fdffc1e016774
 * Modify based on the above submissions
 */
package mekanism.client.render;

import mekanism.client.model.mekasuitarmour.ModelMekAsuitBody;
import mekanism.common.config.MekanismConfig;
import mekanism.common.item.armor.ItemMekAsuitBodyArmour;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.init.Items;
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
        if (!MekanismConfig.current().client.enableFirstPersonMekaSuitArms.val()){
            return;
        }
        AbstractClientPlayer player = Minecraft.getMinecraft().player;
        if (player != null && !player.isInvisible() && !player.isSpectator()) {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (chestStack.getItem() instanceof ItemMekAsuitBodyArmour) {
                ItemStack stack = event.getItemStack();
                EnumHand hand = event.getHand();
                if (stack.isEmpty()) {
                    if (hand == EnumHand.MAIN_HAND) {
                        renderFirstPersonHand(player, chestStack, player.getPrimaryHand() == EnumHandSide.RIGHT, event.getSwingProgress(), event.getEquipProgress());
                        event.setCanceled(true);
                    }
                } else if (stack.getItem() == Items.FILLED_MAP) {
                    if (hand == EnumHand.MAIN_HAND && player.getHeldItemOffhand().isEmpty()) {
                        renderTwoHandedMap(player, chestStack, event.getSwingProgress(), event.getEquipProgress(), event.getInterpolatedPitch(), stack);
                    } else {
                        renderOneHandedMap(player, chestStack, event.getSwingProgress(), event.getEquipProgress(), hand, stack);
                    }
                    event.setCanceled(true);
                }
            }
        }

    }

    private void renderArm(boolean rightHand, AbstractClientPlayer player, ItemStack chestStack) {
        ModelMekAsuitBody armor = ((ItemMekAsuitBodyArmour) chestStack.getItem()).getGearModel();
        armor.setVisible(true);
        if (rightHand) {
            armor.rightArmPose = ModelBiped.ArmPose.EMPTY;
        } else {
            armor.leftArmPose = ModelBiped.ArmPose.EMPTY;
        }
        GlStateManager.enableBlend();
        armor.isSneak = false;
        armor.swingProgress = 0.0F;
        armor.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F,player);
        MekanismRenderer.bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.RENDER, "MekAsuit.png"));
        if (rightHand) {
            armor.rightArmRender(0.0625F);
        }else {
            armor.leftArmRender(0.0625F);
        }

        GlStateManager.disableBlend();
    }


    /**
     * Copy of FirstPersonRenderer#renderArmFirstPerson but tweaked to render the MekaSuit's arm
     */

    private void renderFirstPersonHand(AbstractClientPlayer player, ItemStack chestStack, boolean rightHand, float swingProgress, float equipProgress) {
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

        renderArm(rightHand, player, chestStack);

        GlStateManager.popMatrix();
    }


    /**
     * Copy of FirstPersonRenderer#renderMapFirstPerson but tweaked to render the MekaSuit's arms with some extra rotations of renderMapHand factored up to this level.
     */

    private void renderTwoHandedMap(AbstractClientPlayer player, ItemStack chestStack, float swingProgress, float equipProgress, float interpolatedPitch, ItemStack map) {
        ItemRenderer firstPersonRenderer = Minecraft.getMinecraft().getItemRenderer();
        GlStateManager.pushMatrix();
        float f = MathHelper.sqrt(swingProgress);
        float f1 = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
        float f2 = -0.4F * MathHelper.sin(f * (float) Math.PI);
        GlStateManager.translate(0.0D, -f1 / 2.0F, f2);
        float f3 = firstPersonRenderer.getMapAngleFromPitch(interpolatedPitch);
        GlStateManager.translate(0.0D, 0.04F + equipProgress * -1.2F + f3 * -0.5F, -0.72F);
        GlStateManager.rotate(f3 * -85.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.pushMatrix();
        renderMapHand(true, player, chestStack);
        renderMapHand(false, player, chestStack);
        GlStateManager.popMatrix();

        float f4 = MathHelper.sin(f * (float) Math.PI);
        GlStateManager.rotate(f4 * 20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        firstPersonRenderer.renderMapFirstPerson(map);
        GlStateManager.popMatrix();
    }


    /**
     * Copy of ItemRenderer#renderArm but tweaked to render the MekaSuit's arm and with the extra rotations factored one level up.
     */

    private void renderMapHand(boolean rightHand, AbstractClientPlayer player, ItemStack chestStack) {
        GlStateManager.pushMatrix();
        float f = rightHand ? 1.0F : -1.0F;
        GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -41.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(f * 0.3F, -1.1F, 0.45F);
        renderArm(rightHand, player, chestStack);
        GlStateManager.popMatrix();
    }


    /**
     * Copy of ItemRenderer#renderMapFirstPersonSide but tweaked to render the MekaSuit's arm
     */
    /**
     * renderOneHandedMap
     */

    private void renderOneHandedMap(AbstractClientPlayer player, ItemStack chestStack, float swingProgress,
                                    float equipProgress, EnumHand hand, ItemStack map) {
        boolean rightHand = (player.getPrimaryHand() == EnumHandSide.RIGHT) == (hand == EnumHand.MAIN_HAND);
        float f = rightHand ? 1.0F : -1.0F;
        GlStateManager.pushMatrix();
        GlStateManager.translate(f * 0.125F, -0.125F, 0.0F);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(f * 10.0F, 0.0F, 0.0F, 1.0F);
        renderFirstPersonHand(player, chestStack, rightHand, swingProgress, equipProgress);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(f * 0.51F, -0.08F + equipProgress * -1.2F, -0.75D);
        float f1 = MathHelper.sqrt(swingProgress);
        float f2 = MathHelper.sin(f1 * (float) Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f5 = -0.3F * MathHelper.sin(swingProgress * (float) Math.PI);
        GlStateManager.translate(f * f3, f4 - 0.3F * f2, f5);
        GlStateManager.rotate(f2 * -45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * f2 * -30.0F, 0.0F, 1.0F, 0.0F);
        Minecraft.getMinecraft().getItemRenderer().renderMapFirstPerson(map);
        GlStateManager.popMatrix();
    }
}


