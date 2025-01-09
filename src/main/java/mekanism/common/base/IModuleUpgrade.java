package mekanism.common.base;


import mekanism.common.moduleUpgrade;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IModuleUpgrade {

    List<moduleUpgrade> getValidModule(ItemStack stack);

}
