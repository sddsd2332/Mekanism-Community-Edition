package mekanism.common.base;


import mekanism.common.moduleUpgrade;
import mekanism.common.util.ItemDataUtils;
import net.minecraft.item.ItemStack;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public interface IModuleUpgrade {

     Map<moduleUpgrade, Integer> upgrades = new EnumMap<>(moduleUpgrade.class);

     default int getUpgrades(moduleUpgrade upgrade) {
        return upgrades.getOrDefault(upgrade, 0);
    }

    List<moduleUpgrade> getValidModule(ItemStack stack);

     default void removeUpgrade(moduleUpgrade upgrade, boolean removeAll) {
        int installed = getUpgrades(upgrade);
        if (installed > 0) {
            int toRemove = removeAll ? installed : 1;
            upgrades.put(upgrade, Math.max(0, getUpgrades(upgrade) - toRemove));
        }
        if (upgrades.get(upgrade) == 0) {
            upgrades.remove(upgrade);
        }
    }



     default boolean isUpgradeInstalled(ItemStack stack, moduleUpgrade upgrade) {
        if (ItemDataUtils.hasData(stack, "module")) {
            Map<moduleUpgrade, Integer> module = moduleUpgrade.buildMap(ItemDataUtils.getDataMap(stack));
            return module.containsKey(upgrade);
        }
        return false;
    }
}
