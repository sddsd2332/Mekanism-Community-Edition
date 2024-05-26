package mekanism.common.inventory.container;

import mekanism.api.infuse.InfuseRegistry;
import mekanism.common.base.IFactory;
import mekanism.common.base.IFactory.RecipeType;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.inventory.slot.SlotEnergy.SlotDischarge;
import mekanism.common.inventory.slot.SlotOutput;
import mekanism.common.item.ItemBlockMachine;
import mekanism.common.recipe.inputs.*;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.util.ChargeUtils;
import mekanism.common.util.StackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.Map;

public class ContainerFactory extends ContainerMekanism<TileEntityFactory> {

    public ContainerFactory(InventoryPlayer inventory, TileEntityFactory tile) {
        super(tile, inventory);
    }

    @Override
    protected void addSlots() {
        addSlotToContainer(new SlotDischarge(tileEntity, 1, 7, 13));
        int xTypeSlot = tileEntity.tier == FactoryTier.CREATIVE ? 252 : tileEntity.tier == FactoryTier.ULTIMATE ? 214 : 180;
        addSlotToContainer(new Slot(tileEntity, 2, xTypeSlot, 75) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                MachineType swapType = MachineType.get(stack);
                return swapType != null && !swapType.isFactory();
            }
        });
        addSlotToContainer(new SlotOutput(tileEntity, 3, xTypeSlot, 112));
        addSlotToContainer(new FactoryExtraSlot(tileEntity, 4, 7, 57) {
        });

        int xOffset = tileEntity.tier == FactoryTier.BASIC ? 55 : tileEntity.tier == FactoryTier.ADVANCED ? 35 : tileEntity.tier == FactoryTier.ELITE ? 29 : 27;
        int xDistance = tileEntity.tier == FactoryTier.BASIC ? 38 : tileEntity.tier == FactoryTier.ADVANCED ? 26 : 19;

        for (int i = 0; i < tileEntity.tier.processes; i++) {
            if (tileEntity.NoItemInputMachine()) {
                addSlotToContainer(new FactoryInputSlot(tileEntity, getInputSlotIndex(i), 7, 35, i, false, false));
            } else {
                addSlotToContainer(new FactoryInputSlot(tileEntity, getInputSlotIndex(i), xOffset + (i * xDistance), 13, i, true, true));
            }
        }
        for (int i = 0; i < tileEntity.tier.processes; i++) {
            if (tileEntity.GasOutputMachine()) {
                addSlotToContainer(new FactoryOutputSlot(tileEntity, getOutputSlotIndex(i), 7, 35, false));
            } else {
                addSlotToContainer(new FactoryOutputSlot(tileEntity, getOutputSlotIndex(i), xOffset + (i * xDistance), 57, true));
            }
        }

        for (int i = 0; i < tileEntity.tier.processes; i++) {
            if (tileEntity.getRecipeType().getFuelType() == IFactory.MachineFuelType.FARM || tileEntity.getRecipeType().getFuelType() == IFactory.MachineFuelType.CHANCE) {
                addSlotToContainer(new FactoryOutputSlot(tileEntity, getSecondaryOutputSlotIndex(i), xOffset + (i * xDistance), 78, true));
            } else {
                addSlotToContainer(new FactoryOutputSlot(tileEntity, getSecondaryOutputSlotIndex(i), 7, 35, false) {
                }); //Secondary output slots are reserved to prevent errors
            }
        }
    }

    @Override
    protected int getInventorYOffset() {
        if (tileEntity.getRecipeType().getFuelType() == IFactory.MachineFuelType.ADVANCED || tileEntity.getRecipeType() == RecipeType.INFUSING || tileEntity.getRecipeType() == RecipeType.Dissolution || tileEntity.getRecipeType() == RecipeType.WASHER || tileEntity.getRecipeType() == RecipeType.NUCLEOSYNTHESIZER) {
            return 95;
        } else if (tileEntity.getRecipeType() == RecipeType.PRC) {
            return 113;
        } else if (tileEntity.getRecipeType() == RecipeType.Crystallizer) {
            return 91;
        } else if (tileEntity.getRecipeType().getFuelType() == IFactory.MachineFuelType.FARM) {
            return 116;
        } else if (tileEntity.getRecipeType().getFuelType() == IFactory.MachineFuelType.CHANCE) {
            return 105;
        } else {
            return 84;
        }
    }

    @Override
    protected int getInventorXOffset() {
        return tileEntity.tier == FactoryTier.CREATIVE ? 44 : tileEntity.tier == FactoryTier.ULTIMATE ? 27 : 8;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        ItemStack stack = ItemStack.EMPTY;
        Slot currentSlot = inventorySlots.get(slotID);
        if (currentSlot != null && currentSlot.getHasStack()) {
            ItemStack slotStack = currentSlot.getStack();
            stack = slotStack.copy();
            if (isOutputSlot(slotID)) {
                if (!mergeItemStack(slotStack, tileEntity.inventory.size() - 1, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (isSecondaryOutputSlot(slotID)) {
                if (!mergeItemStack(slotStack, tileEntity.inventory.size() - 1, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotID != 1 && slotID != 2 && isProperMachine(slotStack) && !ItemHandlerHelper.canItemStacksStack(slotStack, tileEntity.getMachineStack())) {
                if (!mergeItemStack(slotStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotID == 2) {
                if (!mergeItemStack(slotStack, tileEntity.inventory.size() - 1, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (tileEntity.getRecipeType().getAnyRecipe(slotStack, inventorySlots.get(4).getStack(), tileEntity.gasTank.getGasType(), tileEntity.infuseStored, tileEntity.gasTank.getGas(), tileEntity.fluidTank.getFluid()) != null) {
                if (isInputSlot(slotID)) {
                    if (!mergeItemStack(slotStack, tileEntity.inventory.size() - 1, inventorySlots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!mergeItemStack(slotStack, 4, 4 + tileEntity.tier.processes, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (ChargeUtils.canBeDischarged(slotStack)) {
                if (slotID == 0) {
                    if (!mergeItemStack(slotStack, tileEntity.inventory.size() - 1, inventorySlots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!mergeItemStack(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (tileEntity.getItemGas(slotStack) != null) {
                if (transferExtraSlot(slotID, slotStack)) {
                    return ItemStack.EMPTY;
                }
            } else if (tileEntity.getRecipeType() == RecipeType.INFUSING && InfuseRegistry.getObject(slotStack) != null
                    && (tileEntity.infuseStored.getType() == null || tileEntity.infuseStored.getType() == InfuseRegistry.getObject(slotStack).type)) {
                if (transferExtraSlot(slotID, slotStack)) {
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

    private boolean transferExtraSlot(int slotID, ItemStack slotStack) {
        if (slotID >= tileEntity.inventory.size() - 1) {
            return !mergeItemStack(slotStack, 3, 4, false);
        }
        return !mergeItemStack(slotStack, tileEntity.inventory.size() - 1, inventorySlots.size(), true);
    }

    public boolean isProperMachine(ItemStack itemStack) {
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemBlockMachine) {
            for (RecipeType type : RecipeType.values()) {
                if (ItemHandlerHelper.canItemStacksStack(itemStack, type.getStack())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInputSlot(int slot) {
        return slot >= 4 && slot < 4 + tileEntity.tier.processes;
    }

    public boolean isOutputSlot(int slot) {
        return slot >= 4 + tileEntity.tier.processes && slot < 4 + tileEntity.tier.processes * 2;
    }

    public boolean isSecondaryOutputSlot(int slot) {
        return slot >= 4 + tileEntity.tier.processes * 2 && slot < 4 + tileEntity.tier.processes * 3;
    }

    private int getOutputSlotIndex(int processNumber) {
        return tileEntity.tier.processes + getInputSlotIndex(processNumber);
    }

    private int getSecondaryOutputSlotIndex(int processNumber) {
        return tileEntity.tier.processes * 2 + getInputSlotIndex(processNumber);
    }

    private int getInputSlotIndex(int processNumber) {
        return 5 + processNumber;
    }

    private class FactoryInputSlot extends Slot {
        /**
         * The index of the processes slot. 0 <= processNumber < tileEntity.tier.processes For matching the input to output slot
         */
        private final int processNumber;
        public boolean itemValid;
        public boolean enabled;
        private FactoryInputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, int processNumber, boolean itemValid, boolean enabled) {
            super(inventoryIn, index, xPosition, yPosition);
            this.processNumber = processNumber;
            this.itemValid = itemValid;
            this.enabled = enabled;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            ItemStack inputSlotStack = tileEntity.inventory.get(getOutputSlotIndex(this.processNumber));
            return /* tileEntity.inputProducesOutput(getInputSlotIndex(this.processNumber), stack, inputSlotStack, false) && itemValid ||*/ isInputItem(stack);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public boolean isEnabled() {
            return enabled;
        }

        private boolean isInputItem(ItemStack itemstack){
            if (!tileEntity.NoItemInputMachine()){
                for(Object obj : tileEntity.getRecipeType().getrecipe().get().entrySet()){
                    if (((Map.Entry<?, ?>) obj).getKey() instanceof AdvancedMachineInput) {
                        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                        ItemStack stack = ((AdvancedMachineInput) entry.getKey()).itemStack;
                        if (ItemHandlerHelper.canItemStacksStack(stack, itemstack)) {
                            return true;
                        }
                    }
                    if (((Map.Entry<?, ?>) obj).getKey() instanceof ItemStackInput){
                        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                        ItemStack stack = ((ItemStackInput) entry.getKey()).ingredient;
                        if (StackUtils.equalsWildcardWithNBT(stack, itemstack)) {
                            return true;
                        }
                    }
                    if (((Map.Entry<?, ?>) obj).getKey() instanceof DoubleMachineInput){
                        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                        ItemStack stack = ((DoubleMachineInput) entry.getKey()).itemStack;
                        if (ItemHandlerHelper.canItemStacksStack(stack, itemstack)) {
                            return true;
                        }
                    }
                    if (((Map.Entry<?, ?>) obj).getKey() instanceof InfusionInput){
                        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                        ItemStack stack = ((InfusionInput) entry.getKey()).inputStack;
                        if (ItemHandlerHelper.canItemStacksStack(stack, itemstack)) {
                            return true;
                        }
                    }
                    if (((Map.Entry<?, ?>) obj).getKey() instanceof NucleosynthesizerInput){
                        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                        ItemStack stack = ((NucleosynthesizerInput) entry.getKey()).getSolid();
                        if (ItemHandlerHelper.canItemStacksStack(stack, itemstack)) {
                            return true;
                        }
                    }
                    if (((Map.Entry<?, ?>) obj).getKey() instanceof PressurizedInput){
                        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                        ItemStack stack = ((PressurizedInput) entry.getKey()).getSolid();
                        if (ItemHandlerHelper.canItemStacksStack(stack, itemstack)) {
                            return true;
                        }
                    }
                }
                return false;
            }
           return false;
        }
    }






    private class FactoryOutputSlot extends SlotOutput {
        public boolean Enabled;
        public FactoryOutputSlot(IInventory inventory, int index, int x, int y, boolean enabled) {
            super(inventory, index, x, y);
            Enabled = enabled;
        }
        @Override
        @SideOnly(Side.CLIENT)
        public boolean isEnabled() {
            return Enabled;
        }
    }




    private class FactoryExtraSlot extends Slot {

        public FactoryExtraSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return (tileEntity.getRecipeType().getFuelType() == IFactory.MachineFuelType.DOUBLE || tileEntity.getRecipeType() == RecipeType.INFUSING || tileEntity.GasAdvancedInputMachine() || tileEntity.GasInputMachine());
        }

        @Override
        @SideOnly(Side.CLIENT)
        public boolean isEnabled() {
            return (tileEntity.getRecipeType().getFuelType() == IFactory.MachineFuelType.DOUBLE || tileEntity.getRecipeType() == RecipeType.INFUSING || tileEntity.GasAdvancedInputMachine() || tileEntity.GasInputMachine());
        }

    }

}
