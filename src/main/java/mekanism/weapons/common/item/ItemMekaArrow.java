package mekanism.weapons.common.item;

import mekanism.weapons.common.entity.EntityMekaArrow;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMekaArrow extends ItemWeapons {

    public ItemMekaArrow() {

    }

    public EntityMekaArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityMekaArrow mekaArrow = new EntityMekaArrow(worldIn, shooter);
        return mekaArrow;
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
        int enchant = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bow);
        return enchant > 0 && this.getClass() == ItemMekaArrow.class;
    }


}
