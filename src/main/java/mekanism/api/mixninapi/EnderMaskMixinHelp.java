package mekanism.api.mixninapi;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface EnderMaskMixinHelp {

    boolean isEnderMask(ItemStack stack, EntityPlayer player, EntityEnderman endermanEntity);

}
