package mekanism.common.interfaces;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IElytraFlyingProvider {
     boolean isElytraFlying(@Nonnull EntityLivingBase entity, @Nonnull ItemStack itemstack);
}