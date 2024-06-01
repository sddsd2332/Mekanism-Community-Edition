package mekanism.common.item;

import mekanism.common.base.IMetaItem;
import mekanism.common.tier.BaseTier;
import mekanism.common.util.LangUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class ItemControlCircuit extends ItemMekanism implements IMetaItem {

    public ItemControlCircuit() {
        super();
        setHasSubtypes(true);
    }

    @Override
    public String getTexture(int meta) {
        return BaseTier.values()[meta].getSimpleName() + "ControlCircuit";
    }

    @Override
    public int getVariants() {
        return BaseTier.values().length;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tabs, @Nonnull NonNullList<ItemStack> itemList) {
        if (isInCreativeTab(tabs)) {
            for (BaseTier tier : BaseTier.values()) {
                itemList.add(new ItemStack(this, 1, tier.ordinal()));
            }
        }
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack item) {
        return "item." + BaseTier.values()[item.getItemDamage()].getSimpleName() + "ControlCircuit";
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack itemstack) {
        BaseTier tier = BaseTier.values()[itemstack.getItemDamage()];
        return tier.getColor() + LangUtils.localize("item." + tier.getSimpleName() + "ControlCircuit.name");
    }
}
