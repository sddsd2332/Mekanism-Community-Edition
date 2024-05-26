package mekanism.common.inventory.container;


import ic2.api.item.IElectricItem;
import mekanism.common.inventory.slot.SlotEnergy;
import mekanism.common.inventory.slot.SlotOutput;
import mekanism.common.inventory.slot.SlotStorageTank;
import mekanism.common.tile.TileEntityHybridStorage;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerHybridStorage extends ContainerMekanism<TileEntityHybridStorage> {

    public ContainerHybridStorage(InventoryPlayer inventory, TileEntityHybridStorage tile) {
        super(tile, inventory);
    }

    @Override
    protected void addSlots() {
        for (int slotY = 0; slotY < 8; slotY++) {
            for (int slotX = 0; slotX < 15; slotX++) {
                addSlotToContainer(new Slot(tileEntity, slotX + slotY * 15, 8 + slotX * 18, 26 + slotY * 18));
            }
        }
        addSlotToContainer(new SlotStorageTank(tileEntity, 120, 8, 179));
        addSlotToContainer(new SlotStorageTank(tileEntity, 121, 8, 259));
        addSlotToContainer(new SlotStorageTank(tileEntity, 122, 35, 179));
        addSlotToContainer(new SlotStorageTank(tileEntity, 123, 35, 259));
        addSlotToContainer(new Slot(tileEntity, 124, 233, 179));
        addSlotToContainer(new SlotOutput(tileEntity, 125, 233, 259));
        addSlotToContainer(new SlotEnergy.SlotCharge(tileEntity, 126, 260, 179));
        addSlotToContainer(new SlotEnergy.SlotDischarge(tileEntity, 127, 260, 259));
    }

    @Override
    protected int getInventorYOffset() {
        return 201;
    }

    @Override
    protected int getInventorXOffset() {
        return 62;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        ItemStack stack = ItemStack.EMPTY;
        Slot currentSlot = inventorySlots.get(slotID);
        if (currentSlot != null && currentSlot.getHasStack()) {
            ItemStack slotStack = currentSlot.getStack();
            stack = slotStack.copy();
         //TODO:I don't know how to write this
             /*
             if (ChargeUtils.canBeCharged(slotStack) || ChargeUtils.canBeDischarged(slotStack)) {
                if (slotStack.getItem() == Items.REDSTONE || slotStack.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_BLOCK)) {
                    if (slotID != 127) {
                        if (!mergeItemStack(slotStack, 0, 128, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!mergeItemStack(slotStack, 128, inventorySlots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (slotID != 127 && slotID != 126) {
                        if (ChargeUtils.canBeDischarged(slotStack)) {
                            if (!mergeItemStack(slotStack, 0, 128, false)) {
                                if (canTransfer(slotStack)) {
                                    if (!mergeItemStack(slotStack, 0, 128, false)) {
                                        return ItemStack.EMPTY;
                                    }
                                }
                            }
                        } else if (canTransfer(slotStack)) {
                            if (!mergeItemStack(slotStack, 0, 128, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    } else if (slotID == 127) {
                        if (canTransfer(slotStack)) {
                            if (!mergeItemStack(slotStack, 0, 128, false)) {
                                if (!mergeItemStack(slotStack, 128, inventorySlots.size(), true)) {
                                    return ItemStack.EMPTY;
                                }
                            }
                        } else if (!mergeItemStack(slotStack, 128, inventorySlots.size(), true)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!mergeItemStack(slotStack, 128, inventorySlots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (slotStack.getItem() instanceof IGasItem) {
                if (slotID != 120 && slotID != 121 && slotID != 122 && slotID != 123) {
                    if (!mergeItemStack(slotStack, 0, 128, false)) {
                        if (!mergeItemStack(slotStack, 0, 128, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                } else if (!mergeItemStack(slotStack, 128, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }  else if (FluidContainerUtils.isFluidContainer(slotStack)) {
                if (slotID != 124 && slotID != 125) {
                    if (!mergeItemStack(slotStack, 0, 128, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!mergeItemStack(slotStack, 128, inventorySlots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else
             */
            if (slotID < 120) {
                if (!mergeItemStack(slotStack, 120, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 0, 120, false)) {
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

    private boolean canTransfer(ItemStack slotStack) {
        return MekanismUtils.useIC2() && slotStack.getItem() instanceof IElectricItem;
    }
}
