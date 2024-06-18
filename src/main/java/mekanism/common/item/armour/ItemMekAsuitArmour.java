package mekanism.common.item.armour;


import mekanism.common.Mekanism;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class ItemMekAsuitArmour extends ItemArmor {

    public ItemMekAsuitArmour(EntityEquipmentSlot slot) {
        super(EnumHelper.addArmorMaterial("MEKASUIT", "mekasuit", 0, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0), 3, slot);
        setCreativeTab(Mekanism.tabMekanism);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "mekanism:render/MekAsuit.png";
    }


}
