
package mekanism.common.inventory;

import mekanism.common.content.gear.IModuleContainerItem;
import mekanism.common.inventory.slot.ArmorSlot;
import mekanism.common.inventory.slot.HotBarSlot;
import mekanism.common.inventory.slot.OffhandSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ModuleTweakerContainer extends Container {

    public static final EntityEquipmentSlot[] EQUIPMENT_SLOT_TYPES = EntityEquipmentSlot.values();

    public ModuleTweakerContainer(InventoryPlayer inventory) {
        addInventorySlots(inventory);
    }

    protected void addInventorySlots(@Nonnull InventoryPlayer inv) {
        /*
        for (int slotY = 0; slotY < inv.getHotbarSize(); slotY++) {
            addSlotToContainer(new HotBarSlot(inv, slotY, 43 + slotY * 18, 161) {
                @Override
                public boolean canTakeStack(@NotNull EntityPlayer player) {
                    return false;
                }
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return false;
                }
            });
        }
         */
        addSlotToContainer(new OffhandSlot(inv, 40, 8, 29 + 18 * 4) {
            @Override
            public boolean canTakeStack(@NotNull EntityPlayer player) {
                return false;
            }

            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        int armorInventorySize = inv.armorInventory.size();
        for (int index = 0; index < armorInventorySize; index++) {
            EntityEquipmentSlot slotType = EQUIPMENT_SLOT_TYPES[2 + armorInventorySize - index - 1];
            addSlotToContainer(new ArmorSlot(inv, 36 + slotType.ordinal() - 2, 8, 21 + index * 18, slotType) {
                @Override
                public boolean canTakeStack(@NotNull EntityPlayer player) {
                    return false;
                }

                @Override
                public boolean isItemValid(ItemStack stack) {
                    return false;
                }
            });
        }
    }

    public static boolean isTweakableItem(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof IModuleContainerItem;
    }

    public static boolean hasTweakableItem(EntityPlayer player) {
        for (int slot = 0; slot < InventoryPlayer.getHotbarSize(); slot++) {
            if (isTweakableItem(player.inventory.mainInventory.get(slot))) {
                return true;
            }
        }
        return player.inventory.armorInventory.stream().anyMatch(ModuleTweakerContainer::isTweakableItem) ||
                player.inventory.offHandInventory.stream().anyMatch(ModuleTweakerContainer::isTweakableItem);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}

