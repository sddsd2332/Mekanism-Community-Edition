package mekanism.common.item;

import mekanism.api.EnumColor;
import mekanism.common.Mekanism;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemMekanism extends Item {

    private EnumRarity rarity;
    private boolean isRarity = false;
    private EnumColor color;
    private boolean isColor = false;

    public ItemMekanism() {
        super();
        setCreativeTab(Mekanism.tabMekanism);
    }

    public ItemMekanism setRarity(EnumRarity rarity) {
        isRarity = true;
        this.rarity = rarity;
        return this;
    }

    public ItemMekanism setNameColor(EnumColor color) {
        isColor = true;
        this.color = color;
        return this;
    }


    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack itemstack) {
        if (isColor) {
            return color + super.getItemStackDisplayName(itemstack);
        } else if (isRarity) {
            return rarity.getColor() + super.getItemStackDisplayName(itemstack);
        } else {
            return super.getItemStackDisplayName(itemstack);
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return isRarity ? rarity : super.getRarity(stack);
    }
}
