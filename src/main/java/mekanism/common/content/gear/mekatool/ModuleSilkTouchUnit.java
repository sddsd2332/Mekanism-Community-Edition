package mekanism.common.content.gear.mekatool;

import mekanism.common.content.gear.EnchantmentBasedModule;
import net.minecraft.enchantment.Enchantment;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ModuleSilkTouchUnit extends EnchantmentBasedModule {

    @Nonnull
    @Override
    public Enchantment getEnchantment() {
        return Objects.requireNonNull(Enchantment.getEnchantmentByID(33));
    }
}