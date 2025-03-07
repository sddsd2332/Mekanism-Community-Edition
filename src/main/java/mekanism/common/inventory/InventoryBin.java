package mekanism.common.inventory;

import mekanism.common.base.ITierItem;
import mekanism.common.block.states.BlockStateBasic.BasicBlockType;
import mekanism.common.config.MekanismConfig;
import mekanism.common.tier.BinTier;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.StackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemHandlerHelper;

public class InventoryBin {

    public ItemStack bin;

    public InventoryBin(ItemStack stack) {
        bin = stack;
    }

    public ItemStack getStack() {
        if (getItemCount() > 0 && !getItemType().isEmpty()) {
            ItemStack ret = getItemType().copy();
            ret.setCount(Math.min(getItemType().getMaxStackSize(), getItemCount()));

            return ret;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack removeStack() {
        if (!MekanismConfig.current().mekce.BinRecipeRemovesItem.val()){
            ItemStack stack = getStack();
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
            if (getTier() != BinTier.CREATIVE) {
                setItemCount(getItemCount() - stack.getCount());
            }
            return stack.copy();
        }
        return ItemStack.EMPTY;
    }

    public ItemStack add(ItemStack stack) {
        if (isValid(stack) && (getTier() == BinTier.CREATIVE || getItemCount() != getMaxStorage())) {
            if (getItemType().isEmpty()) {
                setItemType(stack);
            }
            if (getTier() != BinTier.CREATIVE) {
                if (getItemCount() + stack.getCount() <= getMaxStorage()) {
                    setItemCount(getItemCount() + stack.getCount());
                    return ItemStack.EMPTY;
                } else {
                    ItemStack rejects = getItemType().copy();
                    rejects.setCount((getItemCount() + stack.getCount()) - getMaxStorage());
                    setItemCount(getMaxStorage());
                    return rejects;
                }
            } else {
                setItemCount(Integer.MAX_VALUE);
            }
        }
        return stack;
    }

    public boolean isValid(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() <= 0 || BasicBlockType.get(stack) == BasicBlockType.BIN) {
            return false;
        }
        if (getItemType().isEmpty()) {
            return true;
        }
        return ItemHandlerHelper.canItemStacksStack(stack, getItemType());
    }

    public int getMaxStorage() {
        return getTier().getStorage();
    }

    public BinTier getTier() {
        return BinTier.values()[((ITierItem) bin.getItem()).getBaseTier(bin).ordinal()];
    }

    public int getItemCount() {
        return ItemDataUtils.getInt(bin, "itemCount");
    }

    public void setItemCount(int count) {
        ItemDataUtils.setInt(bin, "itemCount", Math.max(0, count));
        if (getItemCount() == 0) {
            setItemType(ItemStack.EMPTY);
        }
    }

    public ItemStack getItemType() {
        if (getItemCount() == 0) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(ItemDataUtils.getCompound(bin, "storedItem"));
    }

    public void setItemType(ItemStack stack) {
        if (stack.isEmpty()) {
            ItemDataUtils.removeData(bin, "storedItem");
            return;
        }
        ItemDataUtils.setCompound(bin, "storedItem", StackUtils.size(stack, 1).writeToNBT(new NBTTagCompound()));
    }
}
