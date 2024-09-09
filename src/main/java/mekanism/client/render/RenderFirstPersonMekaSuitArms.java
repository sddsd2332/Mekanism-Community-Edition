/*
package mekanism.client.render;

import mekanism.client.model.mekasuitarmour.ModelMekAsuitBody;
import mekanism.common.item.armor.ItemMekaSuitArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
*/
/**
 * https://github.com/mekanism/Mekanism/commit/8c15b6f9ecb697b0093f3964a13fdffc1e016774
 */

/*
public class RenderFirstPersonMekaSuitArms {

    @SubscribeEvent
    public void renderHand(RenderSpecificHandEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null && !player.isInvisible() && !player.isSpectator()) {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (chestStack.getItem() instanceof ItemMekaSuitArmor) {
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

    private void renderArm(MatrixStack matrix, IRenderTypeBuffer renderer, int light, boolean rightHand, ClientPlayerEntity player, ItemStack chestStack) {
        ModelMekAsuitBody armor = (ModelMekAsuitBody) ((ItemMekaSuitArmor) chestStack.getItem()).getGearModel();
        armor.setVisible(true);
        if (rightHand) {
            armor.rightArmPose = ModelBiped.ArmPose.EMPTY;
        } else {
            armor.leftArmPose = ModelBiped.ArmPose.EMPTY;
        }
        armor.isSneak = false;
        armor.swingProgress = 0.0F;
        armor.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F,player);
        armor.renderArm(matrix, renderer, light, OverlayTexture.NO_OVERLAY, chestStack.hasFoil(), player, rightHand);
    }

*/
    /**
     * Copy of FirstPersonRenderer#renderPlayerArm but tweaked to render the MekaSuit's arm
     */
    /*
    private void renderFirstPersonHand(AbstractClientPlayer player, ItemStack chestStack, boolean rightHand,
                                       float swingProgress, float equipProgress) {
        float f = rightHand ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(swingProgress);
        float f2 = -0.3F * MathHelper.sin(f1 * (float) Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(swingProgress * (float) Math.PI);
        matrix.translate(f * (f2 + 0.64000005F), f3 - 0.6F + equipProgress * -0.6F, f4 - 0.71999997F);
        matrix.mulPose(Vector3f.YP.rotationDegrees(f * 45.0F));
        float f5 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        float f6 = MathHelper.sin(f1 * (float) Math.PI);
        matrix.mulPose(Vector3f.YP.rotationDegrees(f * f6 * 70.0F));
        matrix.mulPose(Vector3f.ZP.rotationDegrees(f * f5 * -20.0F));

        matrix.translate(f * -1.0F, 3.6F, 3.5D);
        matrix.mulPose(Vector3f.ZP.rotationDegrees(f * 120.0F));
        matrix.mulPose(Vector3f.XP.rotationDegrees(200.0F));
        matrix.mulPose(Vector3f.YP.rotationDegrees(f * -135.0F));
        matrix.translate(f * 5.6F, 0.0D, 0.0D);

        renderArm(matrix, renderer, light, rightHand, player, chestStack);

        matrix.popPose();
    }
    */

    /**
     * Copy of FirstPersonRenderer#renderTwoHandedMap but tweaked to render the MekaSuit's arms with some extra rotations of renderMapHand factored up to this level.
     */
    /*
    private void renderTwoHandedMap(AbstractClientPlayer player, ItemStack chestStack, float swingProgress,
                                    float equipProgress, float interpolatedPitch, ItemStack map) {
        FirstPersonRenderer firstPersonRenderer = Minecraft.getInstance().getItemInHandRenderer();
        matrix.pushPose();
        float f = MathHelper.sqrt(swingProgress);
        float f1 = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
        float f2 = -0.4F * MathHelper.sin(f * (float) Math.PI);
        matrix.translate(0.0D, -f1 / 2.0F, f2);
        float f3 = firstPersonRenderer.calculateMapTilt(interpolatedPitch);
        matrix.translate(0.0D, 0.04F + equipProgress * -1.2F + f3 * -0.5F, -0.72F);
        matrix.mulPose(Vector3f.XP.rotationDegrees(f3 * -85.0F));
        matrix.pushPose();
        matrix.mulPose(Vector3f.YP.rotationDegrees(90.0F));

        matrix.mulPose(Vector3f.YP.rotationDegrees(92.0F));
        matrix.mulPose(Vector3f.XP.rotationDegrees(45.0F));

        renderMapHand(matrix, renderer, light, true, player, chestStack);
        renderMapHand(matrix, renderer, light, false, player, chestStack);
        matrix.popPose();

        float f4 = MathHelper.sin(f * (float) Math.PI);
        matrix.mulPose(Vector3f.XP.rotationDegrees(f4 * 20.0F));
        matrix.scale(2.0F, 2.0F, 2.0F);
        firstPersonRenderer.renderMap(matrix, renderer, light, map);
        matrix.popPose();
    }
    */

    /**
     * Copy of FirstPersonRenderer#renderMapHand but tweaked to render the MekaSuit's arm and with the extra rotations factored one level up.
     */
    /*
    private void renderMapHand(MatrixStack matrix, IRenderTypeBuffer renderer, int light, boolean rightHand, ClientPlayerEntity player, ItemStack chestStack) {
        matrix.pushPose();
        float f = rightHand ? 1.0F : -1.0F;
        //matrix.mulPose(Vector3f.YP.rotationDegrees(92.0F));
        //matrix.mulPose(Vector3f.XP.rotationDegrees(45.0F));
        matrix.mulPose(Vector3f.ZP.rotationDegrees(f * -41.0F));
        matrix.translate(f * 0.3F, -1.1F, 0.45F);
        renderArm(matrix, renderer, light, rightHand, player, chestStack);
        matrix.popPose();
    }
    */

    /**
     * Copy of ItemRenderer#renderMapFirstPersonSide but tweaked to render the MekaSuit's arm
     */
    /**
     * renderOneHandedMap
     */
    /*
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
*/

