package mekanism.common.inventory.container;

import mekanism.common.inventory.slot.SlotEnergy.SlotDischarge;
import mekanism.common.inventory.slot.SlotOutput;
import mekanism.common.recipe.inputs.AdvancedMachineInput;
import mekanism.common.recipe.machines.FarmMachineRecipe;
import mekanism.common.tile.prefab.TileEntityFarmMachine;
import mekanism.common.util.ChargeUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class ContainerFarmMachine<RECIPE extends FarmMachineRecipe<RECIPE>> extends ContainerMekanism<TileEntityFarmMachine<RECIPE>> {

    public ContainerFarmMachine(InventoryPlayer inventory, TileEntityFarmMachine<RECIPE> tile) {
        super(tile, inventory);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        ItemStack stack = ItemStack.EMPTY;
        Slot currentSlot = inventorySlots.get(slotID);
        if (currentSlot != null && currentSlot.getHasStack()) {
            ItemStack slotStack = currentSlot.getStack();
            stack = slotStack.copy();
            if (ChargeUtils.canBeDischarged(slotStack)) { //energy
                if (slotID != 2) {
                    if (!mergeItemStack(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!mergeItemStack(slotStack, tileEntity.inventory.size() - 1, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotID == 3 || slotID == 4) { //OUT
                if (!mergeItemStack(slotStack, tileEntity.inventory.size() - 1, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (tileEntity.getItemGas(slotStack) != null) { //GAS
                if (slotID != 1) {
                    if (!mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!mergeItemStack(slotStack, tileEntity.inventory.size() - 1, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (isInputItem(slotStack)) { //IN
                if (slotID != 0) {
                    if (!mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!mergeItemStack(slotStack, tileEntity.inventory.size() - 1, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                int slotEnd = tileEntity.inventory.size() - 1;
                if (slotID >= slotEnd && slotID <= (slotEnd + 26)) {
                    if (!mergeItemStack(slotStack, slotEnd + 27, inventorySlots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotID > (slotEnd + 26)) {
                    if (!mergeItemStack(slotStack, slotEnd, slotEnd + 26, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!mergeItemStack(slotStack, slotEnd, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            if (slotStack.getCount() == 0) {
                currentSlot.putStack(ItemStack.EMPTY);
            } else {
                currentSlot.onSlotChanged();
            }
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            currentSlot.onTake(player, slotStack);
        }
        return stack;
    }

    private boolean isInputItem(ItemStack itemstack) {
        for (AdvancedMachineInput input : tileEntity.getRecipes().keySet()) {
            if (ItemHandlerHelper.canItemStacksStack(input.itemStack, itemstack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void addSlots() {
        addSlotToContainer(new Slot(tileEntity, 0, 56, 17){
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return isInputItem(itemstack);
            }
        });
        addSlotToContainer(new Slot(tileEntity, 1, 56, 53));
        addSlotToContainer(new SlotDischarge(tileEntity, 2, 31, 35));
        addSlotToContainer(new SlotOutput(tileEntity, 3, 116, 35));
        addSlotToContainer(new SlotOutput(tileEntity, 4, 132, 35));

    }

}
