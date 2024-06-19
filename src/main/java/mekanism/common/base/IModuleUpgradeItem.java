package mekanism.common.base;

import mekanism.common.moduleUpgrade;
import net.minecraft.item.ItemStack;

public interface IModuleUpgradeItem {
    moduleUpgrade getmoduleUpgrade(ItemStack stack);
}
