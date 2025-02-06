package mekanism.common.tile;

import io.netty.buffer.ByteBuf;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.*;
import mekanism.api.math.MathUtils;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.MekanismFluids;
import mekanism.common.SideData;
import mekanism.common.Upgrade;
import mekanism.common.base.ISideConfiguration;
import mekanism.common.base.ISustainedData;
import mekanism.common.base.ITankManager;
import mekanism.common.block.states.BlockStateMachine;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.prefab.TileEntityMachine;
import mekanism.common.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class TileEntitySPS extends TileEntityMachine implements IGasHandler, ISideConfiguration, ISustainedData, ITankManager {

    public double progress;
    public GasTank inputTank = new GasTank(1000);
    public GasTank outputTank = new GasTank(1000);
    public int inputProcessed = 0;
    public double receivedEnergy = 0;
    public double lastReceivedEnergy = 0;
    public double lastProcessed;
    public TileComponentEjector ejectorComponent;
    public TileComponentConfig configComponent;
    public boolean couldOperate;

    public TileEntitySPS() {
        super("machine.sps", BlockStateMachine.MachineType.SPS, 0);
        upgradeComponent.setSupported(Upgrade.SPEED, false);
        upgradeComponent.setSupported(Upgrade.ENERGY, false);
        upgradeComponent.setSupported(Upgrade.MUFFLING, false);

        configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.GAS);

        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.NONE, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.INPUT, new int[]{0}));
        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.OUTPUT, new int[]{1}));
        configComponent.setConfig(TransmissionType.GAS, new byte[]{0, 0, 0, 0, 1, 2});

        configComponent.setInputConfig(TransmissionType.ENERGY);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(TransmissionType.GAS, configComponent.getOutputs(TransmissionType.GAS).get(2));
        inventory = NonNullListSynchronized.withSize(1, ItemStack.EMPTY);
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            double processed = 0;
            couldOperate = canOperate();
            receivedEnergy = getEnergy();
            if (couldOperate && receivedEnergy != 0 && MekanismUtils.canFunction(this)) {
                setActive(true);
                double lastProgress = progress;
                final int inputPerAntimatter = 1000;
                int inputNeeded = (inputPerAntimatter - inputProcessed) + inputPerAntimatter * (outputTank.getNeeded() - 1);
                double processable = receivedEnergy / 1000000;
                if (processable + progress >= inputNeeded) {
                    processed = process(inputNeeded);
                    progress = 0;
                } else {
                    processed = processable;
                    progress += processable;
                    setEnergy(getEnergy() - receivedEnergy);
                    int toProcess = MathUtils.clampToInt(progress);
                    long actualProcessed = process(toProcess);
                    if (actualProcessed < toProcess) {
                        //If we processed less than we intended to we need to adjust how much our values actually changed by
                        long processedDif = toProcess - actualProcessed;
                        progress -= processedDif;
                        processed -= processedDif;
                    }
                    progress %= 1;
                }
                if (lastProgress != progress) {
                    markNoUpdateSync();
                }
            } else {
                setActive(false);
            }

            if (receivedEnergy != lastReceivedEnergy || processed != lastProcessed) {
                // needsPacket = true;
            }
            lastReceivedEnergy = receivedEnergy;
            receivedEnergy = 0;
            lastProcessed = processed;
        }
    }

    private long process(int operations) {
        if (operations == 0) {
            return 0;
        }
        long processed = inputTank.draw(operations, true).amount;
        int lastInputProcessed = inputProcessed;
        //Limit how much input we actually increase the input processed by to how much we were actually able to remove from the input tank
        inputProcessed += MathUtils.clampToInt(processed);
        final int inputPerAntimatter = 1000;
        if (inputProcessed >= inputPerAntimatter) {
            GasStack toAdd = new GasStack(MekanismFluids.Antimatter, inputProcessed / inputPerAntimatter);
            outputTank.receive(toAdd, true);
            inputProcessed %= inputPerAntimatter;
        }
        if (lastInputProcessed != inputProcessed) {
            markDirty();
        }
        return processed;
    }


    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        return false;
    }


    private boolean canOperate() {
        return inputTank.getGas() != null && outputTank.getNeeded() > 0;
    }

    public double getProcessRate() {
        return (double) Math.round((lastProcessed / 1000) * 1000) / 1000;
    }

    public double getScaledProgress() {
        return (inputProcessed + progress) / 1000;
    }


    @Nonnull
    @Override
    public GasTankInfo[] getTankInfo() {
        return new GasTankInfo[]{inputTank, outputTank};
    }


    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        if (canReceiveGas(side, stack.getGas())) {
            return inputTank.receive(stack, doTransfer);
        }
        return 0;
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        if (canDrawGas(side, null)) {
            return outputTank.draw(amount, doTransfer);
        }
        return null;
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        return configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(0) & inputTank.canReceive(type) && type == MekanismFluids.Polonium;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        return configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(1) && outputTank.canDraw(type);
    }

    @Override
    public void writeSustainedData(ItemStack itemStack) {
        if (inputTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "inputTank", inputTank.getGas().write(new NBTTagCompound()));
        }
        if (outputTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "outputTank", outputTank.getGas().write(new NBTTagCompound()));
        }
    }

    @Override
    public void readSustainedData(ItemStack itemStack) {
        inputTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "inputTank")));
        outputTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "outputTank")));
    }

    @Override
    public Object[] getTanks() {
        return new Object[]{inputTank, outputTank};
    }

    @NotNull
    @Override
    public int[] getSlotsForFace(@NotNull EnumFacing side) {
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
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            lastReceivedEnergy = dataStream.readDouble();
            lastProcessed = dataStream.readDouble();
            TileUtils.readTankData(dataStream, inputTank);
            TileUtils.readTankData(dataStream, outputTank);
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(lastReceivedEnergy);
        data.add(lastProcessed);
        TileUtils.addTankData(data, inputTank);
        TileUtils.addTankData(data, outputTank);
        return data;
    }


    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        lastReceivedEnergy = nbtTags.getDouble("lastReceivedEnergy");
        lastProcessed = nbtTags.getDouble("lastProcessed");
        inputTank.read(nbtTags.getCompoundTag("inputTank"));
        outputTank.read(nbtTags.getCompoundTag("outputTank"));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setDouble("lastReceivedEnergy", lastReceivedEnergy);
        nbtTags.setDouble("lastProcessed", lastProcessed);
        nbtTags.setTag("inputTank", inputTank.write(new NBTTagCompound()));
        nbtTags.setTag("outputTank", outputTank.write(new NBTTagCompound()));
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return false;
        }
        return capability == Capabilities.GAS_HANDLER_CAPABILITY || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return null;
        } else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
        }
        return super.getCapability(capability, side);
    }

    @Override
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side) {
        return configComponent.isCapabilityDisabled(capability, side, facing) || super.isCapabilityDisabled(capability, side);
    }
}
