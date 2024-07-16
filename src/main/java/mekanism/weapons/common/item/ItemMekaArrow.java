package mekanism.weapons.common.item;

import mekanism.weapons.common.entity.EntityMekaArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMekaArrow extends ItemWeapons {

    public ItemMekaArrow() {
    }

    public EntityMekaArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityMekaArrow mekaArrow = new EntityMekaArrow(worldIn, shooter);
        return mekaArrow;
    }


}
