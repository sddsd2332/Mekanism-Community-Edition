package mekanism.mixin.jei;

import mekanism.api.transmitters.TransmissionType;
import mekanism.client.gui.element.GuiUtils;
import mekanism.common.block.states.BlockStateTransmitter;
import mekanism.common.item.ItemBlockTransmitter;
import mekanism.common.tier.BaseTier;
import mekanism.common.util.MekanismUtils;
import mezz.jei.gui.ingredients.IIngredientListElement;
import mezz.jei.render.IngredientRenderer;
import mezz.jei.render.ItemStackFastRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(ItemStackFastRenderer.class)
public abstract class MixinItemStackFastRenderer extends IngredientRenderer<ItemStack> {

    @Shadow(remap = false)
    protected abstract IBakedModel getBakedModel();

    @Shadow(remap = false)
    protected abstract void renderEffect(IBakedModel model);

    public MixinItemStackFastRenderer(IIngredientListElement<ItemStack> element) {
        super(element);
    }

    /**
     * @author
     * @reason
     */
    @Inject(method = "uncheckedRenderItemAndEffectIntoGUI",at = @At("TAIL"), remap = false)
    private void uncheckedRenderItemAndEffectIntoGUI(CallbackInfo ci) {

        ItemStack itemStack = element.getIngredient();
        if (itemStack.getItem() instanceof ItemBlockTransmitter transmitter) {
            TransmissionType transmission = BlockStateTransmitter.TransmitterType.values()[itemStack.getItemDamage()].getTransmission();
            if (transmission == TransmissionType.GAS || transmission == TransmissionType.HEAT || transmission == TransmissionType.ENERGY) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 200);
                BlockStateTransmitter.TransmitterType type = BlockStateTransmitter.TransmitterType.get(itemStack.getItemDamage());
                String name = type.getTranslationKey();
                if (type.hasTiers()) {
                    BaseTier tier = transmitter.getBaseTier(itemStack);
                    name = tier.getSimpleName() + name;
                }
                Minecraft.getMinecraft().renderEngine.bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_ICONS, name.toLowerCase(Locale.ROOT) + ".png"));
                GuiUtils.blit(area.x + padding, area.y + padding, 0, 0, 16, 16, 16, 16);
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                GlStateManager.popMatrix();
            }
        }
    }
}

