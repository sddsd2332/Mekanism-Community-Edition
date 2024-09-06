package mekanism.common.recipe.ingredients;

import mekanism.common.OreDictCache;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class OredictMekIngredient implements IMekanismIngredient<ItemStack> {

    private final String oreDict;

    public OredictMekIngredient(@Nonnull String oreDict) {
        this.oreDict = oreDict;
    }

    @Nonnull
    @Override
    public List<ItemStack> getMatching() {
        return OreDictCache.getOreDictStacks(oreDict, false);
    }

    @Override
    public boolean contains(@Nonnull ItemStack stack) {
        return OreDictCache.getOreDictName(stack).contains(oreDict);
    }

    @Override
    public int hashCode() {
        return oreDict.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof OredictMekIngredient ore && oreDict.equals(ore.oreDict);
    }
}
