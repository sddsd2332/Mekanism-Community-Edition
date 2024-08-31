package mekanism.common.base;


import mekanism.common.moduleUpgrade;
import mekanism.common.util.ItemDataUtils;
import net.minecraft.item.ItemStack;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public interface IModuleUpgrade {

    List<moduleUpgrade> getValidModule(ItemStack stack);

}
