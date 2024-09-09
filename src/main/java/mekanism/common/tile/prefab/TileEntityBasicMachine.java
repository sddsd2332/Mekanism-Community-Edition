package mekanism.common.tile.prefab;

import mekanism.api.IConfigCardAccess;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.Mekanism;
import mekanism.common.base.IElectricMachine;
import mekanism.common.base.ISideConfiguration;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.integration.computer.IComputerIntegration;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.recipe.outputs.MachineOutput;
import mekanism.common.tile.component.SideConfig;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.MekanismUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

/**
 * 基本类型机器方块
 * @param <INPUT> 用于机器的输入
 * @param <OUTPUT> 用于机器的输出
 * @param <RECIPE> 用于机器的配方
 */

public abstract class TileEntityBasicMachine<INPUT extends MachineInput<INPUT>, OUTPUT extends MachineOutput<OUTPUT>, RECIPE extends MachineRecipe<INPUT, OUTPUT, RECIPE>> extends
        TileEntityOperationalMachine implements IElectricMachine<INPUT, OUTPUT, RECIPE>, IComputerIntegration, ISideConfiguration, IConfigCardAccess {

    protected int successCounter = 0;
    protected boolean inventoryChanged = false;

    public RECIPE cachedRecipe = null;

    public TileComponentEjector ejectorComponent;
    public TileComponentConfig configComponent;

    /**
     * The foundation of all machines - a simple tile entity with a facing, active state, initialized state, sound effect, and animated texture.
     *
     * @param soundPath         - location of the sound effect
     * @param type              - the type of this machine
     * @param baseTicksRequired - how many ticks it takes to run a cycle
     */
    public TileEntityBasicMachine(String soundPath, MachineType type, int upgradeSlot, int baseTicksRequired) {
        super("machine." + soundPath, type, upgradeSlot, baseTicksRequired);
    }

    @Override
    public boolean sideIsConsumer(EnumFacing side) {
        return configComponent.hasSideForData(TransmissionType.ENERGY, facing, 1, side);
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return configComponent.getOutput(TransmissionType.ITEM, side, facing).availableSlots;
    }

    @Override
    public TileComponentConfig getConfig() {
        return configComponent;
    }

    @Override
    public EnumFacing getOrientation() {
        return facing;
    }

    @Override
    public TileComponentEjector getEjector() {
        return ejectorComponent;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return false;
        }
        return capability == Capabilities.CONFIG_CARD_CAPABILITY || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return null;
        }
        if (capability == Capabilities.CONFIG_CARD_CAPABILITY) {
            return Capabilities.CONFIG_CARD_CAPABILITY.cast(this);
        }
        return super.getCapability(capability, side);
    }

    @Override
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side) {
        return configComponent.isCapabilityDisabled(capability, side, facing) || super.isCapabilityDisabled(capability, side);
    }

    protected void AutomaticallyExtractItems(int dataIndex, int inputSlotID) {
        if (getWorld().isRemote || !canWork(5, 60)) {
            return;
        }
        InputItems(dataIndex, inputSlotID);
    }

    protected void BetterEjectingItem(int dataIndex, int outputSlotID) {
        if (getWorld().isRemote || !canWork(5, 60)) {
            return;
        }
        outputItems(dataIndex, outputSlotID);
    }

    private void outputItems(int dataIndex, int outputSlotID) {
        SideConfig config = configComponent.getConfig(TransmissionType.ITEM);
        EnumFacing[] translatedFacings = MekanismUtils.getBaseOrientations(facing);
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (config.get(translatedFacings[facing.ordinal()]) == dataIndex) {
                BlockPos offset = getPos().offset(facing);
                TileEntity te = getWorld().getTileEntity(offset);
                if (te == null) {
                    continue;
                }
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
                if (itemHandler == null) {
                    continue;
                }
                try {
                    outputToExternal(itemHandler, outputSlotID);
                } catch (Exception e) {
                    Mekanism.logger.error("Exception when insert item: ", e);
                }
            }
        }
    }

    private synchronized void outputToExternal(IItemHandler external, int outputSlotID) {
        for (int externalSlotId = 0; externalSlotId < external.getSlots(); externalSlotId++) {
            if (!configComponent.isEjecting(TransmissionType.ITEM)) {
                break;
            }
            ItemStack externalStack = external.getStackInSlot(externalSlotId);
            int slotLimit = external.getSlotLimit(externalSlotId);
            if (!externalStack.isEmpty() && externalStack.getCount() >= slotLimit) {
                continue;
            }
            ItemStack internalStack = inventory.get(outputSlotID);
            if (internalStack.isEmpty()) {
                continue;
            }
            if (externalStack.isEmpty()) {
                ItemStack notInserted = external.insertItem(externalSlotId, internalStack, false);
                // Safeguard against Storage Drawers virtual slot
                if (notInserted.getCount() == internalStack.getCount()) {
                    break;
                }
                inventory.set(outputSlotID, notInserted);
                if (notInserted.isEmpty()) {
                    break;
                }
                continue;
            }
            if (!matchStacks(internalStack, externalStack)) {
                continue;
            }
            // Extract internal item to external.
            ItemStack notInserted = external.insertItem(externalSlotId, internalStack, false);
            inventory.set(outputSlotID, notInserted);
            if (notInserted.isEmpty()) {
                break;
            }
        }
    }


    private void InputItems(int dataIndex, int inputSlotID) {
        SideConfig config = configComponent.getConfig(TransmissionType.ITEM);
        EnumFacing[] translatedFacings = MekanismUtils.getBaseOrientations(facing);
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (config.get(translatedFacings[facing.ordinal()]) == dataIndex) {
                BlockPos offset = getPos().offset(facing);
                TileEntity te = getWorld().getTileEntity(offset);
                if (te == null) {
                    continue;
                }
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
                if (itemHandler == null) {
                    continue;
                }
                inputFromExternal(itemHandler, inputSlotID);
            }
        }
    }

    private synchronized void inputFromExternal(IItemHandler external, int inputSlotID) {
        boolean successAtLeastOnce = false;
        external:
        for (int externalSlotId = 0; externalSlotId < external.getSlots(); externalSlotId++) {
            ItemStack externalStack = external.getStackInSlot(externalSlotId);
            if (externalStack.isEmpty()) {
                continue;
            }
            ItemStack internalStack = inventory.get(inputSlotID);
            int maxCanExtract = Math.min(externalStack.getCount(), externalStack.getMaxStackSize());
            if (internalStack.isEmpty()) {
                // Extract external item and insert to internal.
                ItemStack extracted = external.extractItem(externalSlotId, maxCanExtract, false);
                inventory.set(inputSlotID, extracted);
                successAtLeastOnce = true;
                // If there are no more items in the current slot, check the next external slot.
                if (external.getStackInSlot(externalSlotId).isEmpty()) {
                    continue external;
                }
                continue;
            }

            if (internalStack.getCount() >= internalStack.getMaxStackSize() || !matchStacks(internalStack, externalStack)) {
                continue;
            }

            int extractAmt = Math.min(
                    internalStack.getMaxStackSize() - internalStack.getCount(),
                    maxCanExtract);

            // Extract external item and insert to internal.
            ItemStack extracted = external.extractItem(externalSlotId, extractAmt, false);
            inventory.set(inputSlotID, copyStackWithSize(extracted, internalStack.getCount() + extracted.getCount()));
            successAtLeastOnce = true;
            // If there are no more items in the current slot, check the next external slot.
            if (external.getStackInSlot(externalSlotId).isEmpty()) {
                continue external;
            }
        }


        if (successAtLeastOnce) {
            incrementSuccessCounter(60, 5);
            markNoUpdate();
        } else {
            decrementSuccessCounter();
        }
    }

    public static ItemStack copyStackWithSize(ItemStack stack, int amount) {
        if (stack.isEmpty() || amount <= 0) return ItemStack.EMPTY;
        ItemStack s = stack.copy();
        s.setCount(amount);
        return s;
    }

    public static boolean matchStacks(@Nonnull ItemStack stack, @Nonnull ItemStack other) {
        if (!ItemStack.areItemsEqual(stack, other)) return false;
        return ItemStack.areItemStackTagsEqual(stack, other);
    }

    protected boolean canWork(int minWorkDelay, int maxWorkDelay) {
        if (inventoryChanged) {
            inventoryChanged = false;
            return true;
        }

        if (successCounter <= 0) {
            return ticksExisted % maxWorkDelay == 0;
        }
        int workDelay = Math.max(minWorkDelay, maxWorkDelay - (successCounter * 5));
        return ticksExisted % workDelay == 0;
    }

    protected void incrementSuccessCounter(int maxWorkDelay, int minWorkDelay) {
        int max = (maxWorkDelay - minWorkDelay) / 5;
        if (successCounter < max) {
            successCounter++;
        }
    }

    protected void decrementSuccessCounter() {
        if (successCounter > 0) {
            successCounter--;
        }
    }


}
