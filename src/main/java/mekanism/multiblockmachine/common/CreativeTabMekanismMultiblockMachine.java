package mekanism.multiblockmachine.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CreativeTabMekanismMultiblockMachine extends CreativeTabs {


    public CreativeTabMekanismMultiblockMachine() {
        super("tabMekanismMultiblockMachine");
    }

    @Nonnull
    @Override
    public ItemStack createIcon() {
        return new ItemStack(MultiblockMachineBlocks.MultiblockGenerator, 1, 0);
    }

}