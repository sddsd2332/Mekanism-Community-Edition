package mekanism.common.item.interfaces;

import mekanism.common.lib.radial.IGenericRadialModeItem;
import mekanism.common.lib.radial.IRadialModeItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public interface IModeItem {

    void changeMode(@NotNull EntityPlayer player, @NotNull ItemStack stack, int shift, boolean displayChangeMessage);

    default boolean supportsSlotType(ItemStack stack, @NotNull EntityEquipmentSlot slotType) {
        return slotType == EntityEquipmentSlot.MAINHAND || slotType == EntityEquipmentSlot.OFFHAND;
    }

    @Nullable
    default ITextComponent getScrollTextComponent(@NotNull ItemStack stack) {
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
