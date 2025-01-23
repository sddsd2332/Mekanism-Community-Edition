package mekanism.common.content.gear;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;

import javax.annotation.Nonnull;
import java.util.Map;

public abstract class EnchantmentBasedModule extends Module {

    @Nonnull
    public abstract Enchantment getEnchantment();

    @Override
    public void onAddedModule(boolean first) {
        if (isEnabled()) {
            if (first) {
                getContainer().addEnchantment(getEnchantment(), 1);
            } else {
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(getContainer());
                enchantments.put(getEnchantment(), getInstalledCount());
                EnchantmentHelper.setEnchantments(enchantments, getContainer());
            }
        }
    }

    @Override
    public void onRemoved(boolean last) {
        if (isEnabled()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(getContainer());
            if (last) {
                enchantments.remove(getEnchantment());
            } else {
                enchantments.put(getEnchantment(), getInstalledCount());
            }
            EnchantmentHelper.setEnchantments(enchantments, getContainer());
        }
    }

}
