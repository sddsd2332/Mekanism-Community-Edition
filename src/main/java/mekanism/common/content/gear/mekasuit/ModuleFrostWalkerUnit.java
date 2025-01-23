package mekanism.common.content.gear.mekasuit;

import mekanism.common.content.gear.EnchantmentBasedModule;
import net.minecraft.enchantment.Enchantment;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ModuleFrostWalkerUnit extends EnchantmentBasedModule {

    @Nonnull
    @Override
    public Enchantment getEnchantment() {
        return Objects.requireNonNull(Enchantment.getEnchantmentByID(9));
    }
}