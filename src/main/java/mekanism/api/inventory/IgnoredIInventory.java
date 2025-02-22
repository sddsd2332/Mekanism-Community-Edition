package mekanism.api.inventory;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

/**
 * NO-OP IInventory
 */
@MethodsReturnNonnullByDefault
public class IgnoredIInventory implements IInventory {

    public static final IgnoredIInventory INSTANCE = new IgnoredIInventory();

    private IgnoredIInventory() {
    }

    @Override
    public int getSizeInventory() {
        return getContainerSize();
    }

    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return getItem(index);
    }

    public ItemStack getItem(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return removeItem(index, count);
    }

    public ItemStack removeItem(int index, int count) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return removeItemNoUpdate(index);
    }

    public ItemStack removeItemNoUpdate(int index) {
        return ItemStack.EMPTY;
    }


    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        setItem(index, stack);
    }

    public void setItem(int index, @Nonnull ItemStack stack) {
    }


    @Override
    public int getInventoryStackLimit() {
        return getMaxStackSize();
    }

    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void markDirty() {
        setChanged();
    }

    public void setChanged() {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return stillValid(player);
    }

    public boolean stillValid(@Nonnull EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        startOpen(player);
    }

    public void startOpen(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        stopOpen(player);
    }

    public void stopOpen(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }


    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        clearContent();
    }


    public void clearContent() {
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }


}
