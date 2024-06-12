package mekanism.multiblockmachine.common.item;

import mekanism.common.util.ItemDataUtils;
import mekanism.multiblockmachine.common.MekanismMultiblockMachine;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMultiblockMachine extends Item {

    public static int MaxDamage;

    public ItemMultiblockMachine() {
        super();
        setCreativeTab(MekanismMultiblockMachine.tabMekanismMultiblockMachine);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity ent, int slot, boolean hand) {
        if (ent instanceof EntityPlayer player)
            if (super.getDamage(stack) != 0) {
                ItemStack fixed = new ItemStack(this);
                ItemDataUtils.setInt(fixed, "graphDmg", stack.getItemDamage());
                player.inventory.setInventorySlotContents(slot, fixed);
            }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return ItemDataUtils.getInt(stack, "graphDmg") / (double) MaxDamage;
    }

    public ItemMultiblockMachine setItemMaxDamage(int maxDamage){
         MaxDamage = maxDamage;
        return this;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return MaxDamage;
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return ItemDataUtils.getInt(stack, "graphDmg") > 0;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return ItemDataUtils.getInt(stack, "graphDmg");
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        ItemDataUtils.setInt(stack, "graphDmg", damage);
    }
}
