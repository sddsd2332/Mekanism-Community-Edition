package mekanism.common.inventory.container.slot;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IInsertableSlot {

    //TODO: Improve these java docs at some point
    @Nonnull
    ItemStack insertItem(@Nonnull ItemStack stack, boolean action);

    /**
     * Used for determining if this slot can merge with the given stack when the stack is double-clicked.
     */
    default boolean canMergeWith(@Nonnull ItemStack stack) {
        return true;
    }


}