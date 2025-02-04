package mekanism.common.content.gear.mekatool;

import mekanism.api.gear.EnchantmentBasedModule;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;

import javax.annotation.Nonnull;

public class ModuleSilkTouchUnit extends EnchantmentBasedModule<ModuleSilkTouchUnit> {

    @Nonnull
    @Override
    public Enchantment getEnchantment() {
        return Enchantments.SILK_TOUCH;
    }
}