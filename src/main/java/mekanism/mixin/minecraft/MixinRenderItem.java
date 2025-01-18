package mekanism.mixin.minecraft;

import mekanism.api.transmitters.TransmissionType;
import mekanism.client.gui.element.GuiUtils;
import mekanism.common.block.states.BlockStateTransmitter;
import mekanism.common.item.ItemBlockTransmitter;
import mekanism.common.tier.BaseTier;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(RenderItem.class)
@SideOnly(Side.CLIENT)
public abstract class MixinRenderItem implements IResourceManagerReloadListener {

    @Shadow
    public float zLevel;


    @Inject(method = "renderItemAndEffectIntoGUI(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;II)V", at = @At("TAIL"))
    public void renderItemAndEffectIntoGUI(EntityLivingBase entityLivingBase, ItemStack stack, int xPosition, int yPosition, CallbackInfo ci) {
        if (!stack.isEmpty()) {
            this.zLevel += 50.0F;
            if (stack.getItem() instanceof ItemBlockTransmitter transmitter) {
                TransmissionType transmission = BlockStateTransmitter.TransmitterType.values()[stack.getItemDamage()].getTransmission();
                if (transmission == TransmissionType.GAS || transmission == TransmissionType.HEAT || transmission == TransmissionType.ENERGY) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0, 0, 200);
                    BlockStateTransmitter.TransmitterType type = BlockStateTransmitter.TransmitterType.get(stack.getItemDamage());
                    String name = type.getTranslationKey();
                    if (type.hasTiers()) {
                        BaseTier tier = transmitter.getBaseTier(stack);
                        name = tier.getSimpleName() + name;
                    }
                    Minecraft.getMinecraft().renderEngine.bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_ICONS, name.toLowerCase(Locale.ROOT) + ".png"));
                    GuiUtils.blit(xPosition, yPosition, 0, 0, 16, 16, 16, 16);
                    GlStateManager.popMatrix();
                }
            }
            this.zLevel -= 50.0F;
        }
    }


}
