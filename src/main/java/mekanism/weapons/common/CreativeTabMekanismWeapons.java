package mekanism.weapons.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabMekanismWeapons extends CreativeTabs {

    public CreativeTabMekanismWeapons() {
        super("tabMekanismWeapons");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(MekanismWeaponsItems.mekaBow, 1);
    }
}
