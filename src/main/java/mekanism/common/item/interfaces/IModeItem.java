package mekanism.common.item.interfaces;


import mekanism.common.lib.radial.IGenericRadialModeItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IModeItem {

    /**
     * Changes the current mode of the item
     *
     * @param player        The player who made the mode change.
     * @param stack         The stack to change the mode of
     * @param shift         The amount to shift the mode by, may be negative for indicating the mode should decrease.
     * @param displayChange {@code true} if a message should be displayed when the mode changes
     */
    void changeMode(@NotNull EntityPlayer player, @NotNull ItemStack stack, int shift, boolean displayChange);

    default boolean supportsSlotType(ItemStack stack, @NotNull EntityEquipmentSlot slotType) {
        return slotType == EntityEquipmentSlot.MAINHAND || slotType == EntityEquipmentSlot.OFFHAND;
    }

    @Nullable
    default String getScrollTextComponent(@NotNull ItemStack stack) {
        return null;
    }

    static boolean isModeItem(@NotNull EntityPlayer player, @NotNull EntityEquipmentSlot slotType) {
        return isModeItem(player, slotType, true);
    }

    static boolean isModeItem(@NotNull EntityPlayer player, @NotNull EntityEquipmentSlot slotType, boolean allowRadial) {
        return isModeItem(player.getItemStackFromSlot(slotType), slotType, allowRadial);
    }

    static boolean isModeItem(@NotNull ItemStack stack, @NotNull EntityEquipmentSlot slotType) {
        return isModeItem(stack, slotType, true);
    }

    static boolean isModeItem(@NotNull ItemStack stack, @NotNull EntityEquipmentSlot slotType, boolean allowRadial) {
        if (!stack.isEmpty() && stack.getItem() instanceof IModeItem modeItem && modeItem.supportsSlotType(stack, slotType)) {
            return allowRadial || !(modeItem instanceof IGenericRadialModeItem radialModeItem) || radialModeItem.getRadialData(stack) == null;
        }
        return false;
    }

}