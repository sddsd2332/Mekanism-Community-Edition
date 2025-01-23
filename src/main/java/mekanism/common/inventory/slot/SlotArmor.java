package mekanism.common.inventory.slot;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class SlotArmor extends Slot {

    private final EntityEquipmentSlot armorType;

    public SlotArmor(InventoryPlayer inventory, int index, int x, int y, EntityEquipmentSlot armorType) {
        super(inventory,index,x,y);
        this.armorType = armorType;
    }


    public SlotArmor(InventoryPlayer inventory, EntityEquipmentSlot armorType, int x, int y) {
        super(inventory, 35 + armorType.getSlotIndex(), x, y);
        this.armorType = armorType;
    }

    @Override
    public boolean canTakeStack(@NotNull EntityPlayer playerIn) {
        ItemStack itemstack = this.getStack();
        return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.canTakeStack(playerIn);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        Item item = stack.getItem();
        if (armorType.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
            return item.isValidArmor(stack, armorType, ((InventoryPlayer) inventory).player);
        } else {
            return armorType == EntityEquipmentSlot.OFFHAND;
        }
    }


    @SideOnly(Side.CLIENT)
    public String getSlotTexture() {
        return armorType.getSlotType() == EntityEquipmentSlot.Type.ARMOR ? ItemArmor.EMPTY_SLOT_NAMES[armorType.getIndex()] : "minecraft:items/empty_armor_slot_shield";
    }
}
