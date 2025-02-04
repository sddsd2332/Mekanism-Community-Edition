package mekanism.common.inventory.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InventoryContainerSlot extends Slot implements IInsertableSlot {
    public InventoryContainerSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @NotNull
    @Override
    public ItemStack insertItem(@NotNull ItemStack stack, boolean action) {
        return null;
    }
}
