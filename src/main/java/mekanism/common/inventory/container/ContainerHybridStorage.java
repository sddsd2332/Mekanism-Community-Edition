package mekanism.common.inventory.container;


import mekanism.common.tile.TileEntityHybridStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerHybridStorage extends ContainerMekanism<TileEntityHybridStorage> {

    public ContainerHybridStorage(InventoryPlayer inventory, TileEntityHybridStorage tile) {
        super(tile, inventory);
    }

    @Override
    protected void addSlots() {
        for (int slotY = 0; slotY < 10; slotY++) {
            for (int slotX = 0; slotX < 12; slotX++) {
                addSlotToContainer(new Slot(tileEntity, slotX + slotY * 12, 8 + slotX * 18, 26 + slotY * 18));
            }
        }
    }

    @Override
    protected int getInventorYOffset() {
        return 220;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        ItemStack stack = ItemStack.EMPTY;
        Slot currentSlot = inventorySlots.get(slotID);
        if (currentSlot != null && currentSlot.getHasStack()) {
            ItemStack slotStack = currentSlot.getStack();
            stack = slotStack.copy();
            if (slotID < 120) {
                if (!mergeItemStack(slotStack, 120, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 0, 120, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.getCount() == 0) {
                currentSlot.putStack(ItemStack.EMPTY);
            } else {
                currentSlot.onSlotChanged();
            }
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            currentSlot.onTake(player, slotStack);
        }
        return stack;
    }


}
