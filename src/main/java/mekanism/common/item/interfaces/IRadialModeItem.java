package mekanism.common.item.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IRadialModeItem<TYPE extends Enum<TYPE> & IRadialSelectorEnum<TYPE>> extends IModeItem {

    Class<TYPE> getModeClass();

    default TYPE getDefaultMode() {
        return getModeClass().getEnumConstants()[0];
    }

    TYPE getModeByIndex(int ordinal);

    TYPE getMode(ItemStack stack);

    void setMode(ItemStack stack, EntityPlayer player, TYPE mode);
}
