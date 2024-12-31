package mekanism.common.item.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IItemHUDProvider {
    void addHUDStrings(List<String> list, EntityPlayer player, ItemStack stack, EntityEquipmentSlot slotType);

    default void addBaublesHUDStrings(List<String> list, EntityPlayer player, ItemStack stack) {
        if (this instanceof ItemArmor armor) {
            addHUDStrings(list, player, stack, armor.getEquipmentSlot());
        }
    }
}
