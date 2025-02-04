
package mekanism.common.inventory;

import mekanism.common.content.gear.IModuleContainerItem;
import mekanism.common.inventory.slot.ArmorSlot;
import mekanism.common.inventory.slot.HotBarSlot;
import mekanism.common.inventory.slot.OffhandSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
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

        addSlotToContainer(new OffhandSlot(inv, 40, 61, 20) {
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
            addSlotToContainer(new ArmorSlot(inv, 36 + slotType.ordinal() - 2, 7 + index * 18, 41, slotType) {
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
        for (int slotY = 0; slotY < 3; slotY++) {
            for (int slotX = 0; slotX < 9; slotX++) {
                addSlotToContainer(new HotBarSlot(inv, slotX + slotY * 9 + 9, 25 + slotY * 18, 62 + slotX * 18) {
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

        for (int slotY = 0; slotY < 9; slotY++) {
            addSlotToContainer(new HotBarSlot(inv, slotY, 7, 62 + slotY * 18) {
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
        return player.inventory.armorInventory.stream().anyMatch(ModuleTweakerContainer::isTweakableItem) || player.inventory.offHandInventory.stream().anyMatch(ModuleTweakerContainer::isTweakableItem) || player.inventory.mainInventory.stream().anyMatch(ModuleTweakerContainer::isTweakableItem);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        return ItemStack.EMPTY;
    }


    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        return ItemStack.EMPTY;
    }
}

