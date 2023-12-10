package mekanism.api.inventory;

import mekanism.api.annotations.NothingNullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

@NothingNullByDefault
public final class IgnoredIInventory implements IInventory {

    public static final IgnoredIInventory INSTANCE = new IgnoredIInventory();

    private IgnoredIInventory() {
    }

    @Override
    public int getSizeInventory() { //getContainerSize
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) { //getItem
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) { //removeItem
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) { // removeItemNoUpdate
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) { //setItem

    }

    @Override
    public int getInventoryStackLimit() { //getMaxStackSize
        return 64;
    }

    @Override
    public void markDirty() { //setChanged

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) { //stillValid
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) { //startOpen

    }

    @Override
    public void closeInventory(EntityPlayer player) { //stopOpen

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

    }

    @Override
    public String getName() {
        return null;
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
