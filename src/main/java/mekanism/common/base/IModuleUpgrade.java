package mekanism.common.base;


import mekanism.common.moduleUpgrade;
import mekanism.common.util.ItemDataUtils;
import net.minecraft.item.ItemStack;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public interface IModuleUpgrade {

     Map<moduleUpgrade, Integer> upgrades = new EnumMap<>(moduleUpgrade.class);

     default int getUpgrades(moduleUpgrade upgrade) {
        return upgrades.getOrDefault(upgrade, 0);
    }

     Set<moduleUpgrade> supported = EnumSet.noneOf(moduleUpgrade.class);

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

     default void setSupported(moduleUpgrade upgrade) {
        setSupported(upgrade, true);
    }

     default void setSupported(moduleUpgrade upgrade, boolean isSupported) {
        if (isSupported) {
            supported.add(upgrade);
        } else {
            supported.remove(upgrade);
        }
    }

     default void clearSupportedTypes() {
        supported.clear();
    }

     default boolean supports(moduleUpgrade upgrade) {
        return supported.contains(upgrade);
    }

     default Set<moduleUpgrade> getSupportedTypes() {
        return supported;
    }

     default boolean isUpgradeInstalled(ItemStack stack, moduleUpgrade upgrade) {
        if (ItemDataUtils.hasData(stack, "module")) {
            Map<moduleUpgrade, Integer> module = moduleUpgrade.buildMap(ItemDataUtils.getDataMap(stack));
            return module.containsKey(upgrade);
        }
        return false;
    }
}
