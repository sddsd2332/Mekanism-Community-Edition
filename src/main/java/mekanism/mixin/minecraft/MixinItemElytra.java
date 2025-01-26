package mekanism.mixin.minecraft;

import mekanism.api.mixninapi.ElytraMixinHelp;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemElytra.class)
public abstract class MixinItemElytra extends Item implements ElytraMixinHelp {


    @Shadow
    public static boolean isUsable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canElytraFly(ItemStack stack, EntityLivingBase entity) {
        return isUsable(stack);
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, EntityLivingBase entity, int flightTicks) {
        if (!entity.world.isRemote && (flightTicks + 1) % 20 == 0) {
            stack.damageItem(1, entity);
        }
        return true;
    }

}
