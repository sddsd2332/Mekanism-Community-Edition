package mekanism.multiblockmachine.common.inventory.container.machine;

import mekanism.common.inventory.container.ContainerMekanism;
import mekanism.common.inventory.slot.SlotEnergy;
import mekanism.common.inventory.slot.SlotOutput;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.util.ChargeUtils;
import mekanism.multiblockmachine.common.MultiblockMachineItems;
import mekanism.multiblockmachine.common.tile.machine.TileEntityDigitalAssemblyTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerDigitalAssemblyTable extends ContainerMekanism<TileEntityDigitalAssemblyTable> {

    public ContainerDigitalAssemblyTable(InventoryPlayer inventory, TileEntityDigitalAssemblyTable tile) {
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
            if (slotID == 14) {
                if (!mergeItemStack(slotStack, 15, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (ChargeUtils.canBeDischarged(slotStack)) {
                if (slotID != 1) {
                    if (!mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!mergeItemStack(slotStack, 2, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (RecipeHandler.isInDigitalAssemblyRecipe(slotStack)) {
                if (slotID != 2 && slotID != 3 && slotID != 4 && slotID != 5 && slotID != 6 && slotID != 7 && slotID != 8 && slotID != 9 && slotID != 10 && slotID != 11 && slotID != 12 && slotID != 13) {
                    if (!mergeItemStack(slotStack, 2, 14, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!mergeItemStack(slotStack, 14, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotID >= 15 && slotID <= 43) {
                if (!mergeItemStack(slotStack, 44, inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotID > 43) {
                if (!mergeItemStack(slotStack, 15, 43, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 15, inventorySlots.size(), true)) {
                return ItemStack.EMPTY;
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

    @Override
    protected int getInventorYOffset() {
        return 94;
    }

    @Override
    protected int getInventorXOffset() {
        return 34;
    }

    @Override
    protected void addSlots() {
        addSlotToContainer(new SlotEnergy.SlotDischarge(tileEntity, 1, 200, 94));
        addSlotToContainer(new Slot(tileEntity, 2, 67, 16));
        addSlotToContainer(new Slot(tileEntity, 3, 67 + 18, 16));
        addSlotToContainer(new Slot(tileEntity, 4, 67 + 2 * 18, 16));
        addSlotToContainer(new Slot(tileEntity, 5, 67, 16 + 18));
        addSlotToContainer(new Slot(tileEntity, 6, 67 + 18, 16 + 18));
        addSlotToContainer(new Slot(tileEntity, 7, 67 + 2 * 18, 16 + 18));
        addSlotToContainer(new Slot(tileEntity, 8, 67, 16 + 2 * 18));
        addSlotToContainer(new Slot(tileEntity, 9, 67 + 18, 16 + 2 * 18));
        addSlotToContainer(new Slot(tileEntity, 10, 67 + 2 * 18, 16 + 2 * 18));
        addSlotToContainer(new Slot(tileEntity, 11, 49, 16) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == MultiblockMachineItems.PlasmaCutterNozzles;
            }
        });
        addSlotToContainer(new Slot(tileEntity, 12, 49, 16 + 18) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == MultiblockMachineItems.DrillBit;
            }
        });
        addSlotToContainer(new Slot(tileEntity, 13, 49, 16 + 2 * 18) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == MultiblockMachineItems.LaserLenses;
            }
        });
        addSlotToContainer(new SlotOutput(tileEntity, 14, 163, 34));
    }
}
