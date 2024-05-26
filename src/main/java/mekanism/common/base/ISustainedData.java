package mekanism.common.base;

import net.minecraft.item.ItemStack;

/**
 * 持续数据，一般用于气体存储
 */
public interface ISustainedData {

    void writeSustainedData(ItemStack itemStack);

    void readSustainedData(ItemStack itemStack);
}
