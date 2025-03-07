package mekanism.common.inventory.container;

import mekanism.api.gas.IGasItem;
import mekanism.common.MekanismFluids;
import mekanism.common.inventory.slot.SlotEnergy.SlotDischarge;
import mekanism.common.inventory.slot.SlotStorageTank;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.tile.machine.TileEntityElectrolyticSeparator;
import mekanism.common.util.ChargeUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerElectrolyticSeparator extends ContainerMekanism<TileEntityElectrolyticSeparator> {

    public ContainerElectrolyticSeparator(InventoryPlayer inventory, TileEntityElectrolyticSeparator tile) {
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
            if (slotID != 0 && slotID != 1 && slotID != 2 && slotID != 3) {
                if (isCorrectFluid(slotStack)) {
                    if (!mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotStack.getItem() instanceof IGasItem gasItem) {
                    if (gasItem.getGas(slotStack) != null) {
                        if (gasItem.getGas(slotStack).getGas() == MekanismFluids.Hydrogen) {
                            if (!mergeItemStack(slotStack, 1, 2, false)) {
                                return ItemStack.EMPTY;
                            }
                        } else if (gasItem.getGas(slotStack).getGas() == MekanismFluids.Oxygen) {
                            if (!mergeItemStack(slotStack, 2, 3, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    } else if (gasItem.getGas(slotStack) == null) {
                        if (!mergeItemStack(slotStack, 1, 2, false)) {
                            if (!mergeItemStack(slotStack, 2, 3, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                } else if (ChargeUtils.canBeDischarged(slotStack)) {
                    if (!mergeItemStack(slotStack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotID <= 30) {
                    if (!mergeItemStack(slotStack, 31, inventorySlots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!mergeItemStack(slotStack, 4, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 4, inventorySlots.size(), true)) {
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

    public boolean isCorrectFluid(ItemStack itemStack) {
        return Recipe.ELECTROLYTIC_SEPARATOR.containsRecipe(itemStack);
    }

    @Override
    protected void addSlots() {
        addSlotToContainer(new Slot(tileEntity, 0, 26, 35){
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return isCorrectFluid(itemstack);
            }
        });
        addSlotToContainer(new SlotStorageTank(tileEntity, 1, 59, 52));
        addSlotToContainer(new SlotStorageTank(tileEntity, 2, 101, 52));
        addSlotToContainer(new SlotDischarge(tileEntity, 3, 143, 35));
    }
}
