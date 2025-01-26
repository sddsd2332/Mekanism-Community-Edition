package mekanism.client.render.layer;

import mekanism.common.Mekanism;
import mekanism.common.MekanismItems;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MekanismElytraLayer implements LayerRenderer<EntityLivingBase> {

    /**
     * The basic Elytra texture.
     */
    private static final ResourceLocation HDPE_ELYTRA = Mekanism.rl("textures/entities/hdpe_elytra.png");
    protected final RenderLivingBase<?> renderPlayer;
    /**
     * The model used by the Elytra.
     */
    private final ModelElytra modelElytra = new ModelElytra();

    public MekanismElytraLayer(RenderLivingBase<?> renderPlayer) {
        this.renderPlayer = renderPlayer;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack itemstack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (itemstack.getItem() == MekanismItems.HDPE_REINFORCED_ELYTRA) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            if (entity instanceof AbstractClientPlayer player) {
                if (player.isPlayerInfoSet() && player.getLocationElytra() != null) {
                    this.renderPlayer.bindTexture(player.getLocationElytra());
                } else if (player.hasPlayerInfo() && player.getLocationCape() != null && player.isWearing(EnumPlayerModelParts.CAPE)) {
                    this.renderPlayer.bindTexture(player.getLocationCape());
                } else {
                    this.renderPlayer.bindTexture(HDPE_ELYTRA);
                }
            } else {
                this.renderPlayer.bindTexture(HDPE_ELYTRA);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 0.125F);

            this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
            this.modelElytra.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            if (itemstack.isItemEnchanted()) {
                LayerArmorBase.renderEnchantedGlint(this.renderPlayer, entity, this.modelElytra, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
