package mekanism.common.inventory.container;

import mekanism.common.base.IModuleUpgrade;
import mekanism.common.base.IModuleUpgradeItem;
import mekanism.common.inventory.slot.SlotEnergy;
import mekanism.common.tile.TileEntityModificationStation;
import mekanism.common.util.ChargeUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerModificationStation extends ContainerMekanism<TileEntityModificationStation> {


    public ContainerModificationStation(InventoryPlayer inventory, TileEntityModificationStation tile) {
        super(tile, inventory);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        ItemStack stack = ItemStack.EMPTY;
        Slot currentSlot = inventorySlots.get(slotID);
        if (currentSlot != null && currentSlot.getHasStack()) {
            ItemStack slotStack = currentSlot.getStack();
            stack = slotStack.copy();
            if (ChargeUtils.canBeDischarged(slotStack)) {
                if (slotID != 1) {
                    if (!mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!mergeItemStack(slotStack, 3, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
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

    @Override
    protected void addSlots() {
        addSlotToContainer(new SlotEnergy.SlotDischarge(tileEntity, 1, 141, 34));
        addSlotToContainer(new Slot(tileEntity, 2, 26, 34) {
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return itemstack.getItem() instanceof IModuleUpgradeItem;
            }
        });
        addSlotToContainer(new Slot(tileEntity, 3, 116, 34) {
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return itemstack.getItem() instanceof IModuleUpgrade;
            }
        });
    }
}
