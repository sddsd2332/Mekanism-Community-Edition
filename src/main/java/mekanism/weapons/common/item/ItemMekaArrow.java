package mekanism.weapons.common.item;

import mekanism.common.moduleUpgrade;
import mekanism.common.util.ItemDataUtils;
import mekanism.weapons.common.entity.EntityMekaArrow;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Map;

public class ItemMekaArrow extends ItemWeapons {

    public ItemMekaArrow() {

    }

    public EntityMekaArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityMekaArrow mekaArrow = new EntityMekaArrow(worldIn, shooter);
        return mekaArrow;
    }




}
