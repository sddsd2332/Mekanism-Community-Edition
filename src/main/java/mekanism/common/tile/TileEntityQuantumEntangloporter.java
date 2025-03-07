package mekanism.common.tile;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mekanism.api.Chunk3D;
import mekanism.api.Coord4D;
import mekanism.api.IHeatTransfer;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTankInfo;
import mekanism.api.gas.IGasHandler;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.Mekanism;
import mekanism.common.PacketHandler;
import mekanism.common.SideData;
import mekanism.common.SideData.IOState;
import mekanism.common.Upgrade;
import mekanism.common.base.*;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.chunkloading.IChunkLoader;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.entangloporter.InventoryFrequency;
import mekanism.common.frequency.Frequency;
import mekanism.common.frequency.FrequencyManager;
import mekanism.common.frequency.IFrequencyHandler;
import mekanism.common.integration.computer.IComputerIntegration;
import mekanism.common.security.ISecurityTile;
import mekanism.common.tile.component.*;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.prefab.TileEntityElectricBlock;
import mekanism.common.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TileEntityQuantumEntangloporter extends TileEntityElectricBlock implements ISideConfiguration, ITankManager, IFluidHandlerWrapper, IFrequencyHandler,
        IGasHandler, IHeatTransfer, IComputerIntegration, ISecurityTile, IChunkLoader, IUpgradeTile {

    private static final int INV_SIZE = 1;//this.inventory size, used for upgrades. Manually handled
    private static final String[] methods = {"setFrequency"};
    public InventoryFrequency frequency;
    public double heatToAbsorb = 0;
    public double lastTransferLoss;
    public double lastEnvironmentLoss;
    public List<Frequency> publicCache = new ArrayList<>();
    public List<Frequency> privateCache = new ArrayList<>();
    public TileComponentEjector ejectorComponent;
    public TileComponentConfig configComponent;
    public TileComponentSecurity securityComponent;
    public TileComponentChunkLoader chunkLoaderComponent;
    public TileComponentUpgrade upgradeComponent;

    public TileEntityQuantumEntangloporter() {
        super("QuantumEntangloporter", 0);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID, TransmissionType.GAS, TransmissionType.ENERGY, TransmissionType.HEAT);

        for (TransmissionType type : TransmissionType.values()) {
            if (type != TransmissionType.HEAT) {
                configComponent.setIOConfig(type);
            } else {
                configComponent.addOutput(TransmissionType.HEAT, new SideData(DataType.NONE, IOState.OFF));
                configComponent.addOutput(TransmissionType.HEAT, new SideData(DataType.INPUT_OUTPUT, IOState.INPUT));
                configComponent.fillConfig(TransmissionType.HEAT, 1);
                configComponent.setCanEject(TransmissionType.HEAT, false);
            }
        }

        inventory = NonNullListSynchronized.withSize(INV_SIZE, ItemStack.EMPTY);

        configComponent.getOutputs(TransmissionType.ITEM).get(2).availableSlots = new int[]{0};
        configComponent.getOutputs(TransmissionType.FLUID).get(2).availableSlots = new int[]{0};
        configComponent.getOutputs(TransmissionType.GAS).get(2).availableSlots = new int[]{1};

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(TransmissionType.ITEM, configComponent.getOutputs(TransmissionType.ITEM).get(2));
        ejectorComponent.setOutputData(TransmissionType.FLUID, configComponent.getOutputs(TransmissionType.FLUID).get(2));
        ejectorComponent.setOutputData(TransmissionType.GAS, configComponent.getOutputs(TransmissionType.GAS).get(2));

        securityComponent = new TileComponentSecurity(this);
        chunkLoaderComponent = new TileComponentChunkLoader(this);

        upgradeComponent = new TileComponentUpgrade(this, 0);
        upgradeComponent.clearSupportedTypes();
        upgradeComponent.setSupported(Upgrade.ANCHOR);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!world.isRemote) {
            if (configComponent.isEjecting(TransmissionType.ENERGY)) {
                CableUtils.emit(this);
            }
            double[] loss = simulateHeat();
            applyTemperatureChange();

            lastTransferLoss = loss[0];
            lastEnvironmentLoss = loss[1];

            FrequencyManager manager = getManager(frequency);
            Frequency lastFreq = frequency;

            if (manager != null) {
                if (frequency != null && !frequency.valid) {
                    frequency = (InventoryFrequency) manager.validateFrequency(securityComponent.getOwnerUUID(), Coord4D.get(this), frequency);
                    markNoUpdateSync();
                }

                if (frequency != null) {
                    frequency = (InventoryFrequency) manager.update(Coord4D.get(this), frequency);
                    if (frequency == null) {
                        markNoUpdateSync();
                    }
                }
            } else {
                frequency = null;
                if (lastFreq != null) {
                    markNoUpdateSync();
                }
            }
        }
    }

    @Override
    public boolean supportsAsync() {
        return false;
    }

    private boolean hasFrequency() {
        return frequency != null && frequency.valid;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (!world.isRemote) {
            if (frequency != null) {
                FrequencyManager manager = getManager(frequency);
                if (manager != null) {
                    manager.deactivate(Coord4D.get(this));
                }
            }
        }
    }

    @Override
    public Frequency getFrequency(FrequencyManager manager) {
        if (manager == Mekanism.securityFrequencies) {
            return securityComponent.getFrequency();
        }
        return frequency;
    }

    public FrequencyManager getManager(Frequency freq) {
        if (securityComponent.getOwnerUUID() == null || freq == null) {
            return null;
        }
        if (freq.isPublic()) {
            return Mekanism.publicEntangloporters;
        } else if (!Mekanism.privateEntangloporters.containsKey(securityComponent.getOwnerUUID())) {
            FrequencyManager manager = new FrequencyManager(InventoryFrequency.class, InventoryFrequency.ENTANGLOPORTER, securityComponent.getOwnerUUID());
            Mekanism.privateEntangloporters.put(securityComponent.getOwnerUUID(), manager);
            manager.createOrLoad(world);
        }
        return Mekanism.privateEntangloporters.get(securityComponent.getOwnerUUID());
    }

    public void setFrequency(String name, boolean publicFreq) {
        FrequencyManager manager = getManager(new InventoryFrequency(name, null).setPublic(publicFreq));
        manager.deactivate(Coord4D.get(this));
        for (Frequency freq : manager.getFrequencies()) {
            if (freq.name.equals(name)) {
                frequency = (InventoryFrequency) freq;
                frequency.activeCoords.add(Coord4D.get(this));
                markNoUpdateSync();
                return;
            }
        }

        Frequency freq = new InventoryFrequency(name, securityComponent.getOwnerUUID()).setPublic(publicFreq);
        freq.activeCoords.add(Coord4D.get(this));
        manager.addFrequency(freq);
        frequency = (InventoryFrequency) freq;
//        MekanismUtils.saveChunk(this);
        markNoUpdateSync();
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        if (nbtTags.hasKey("frequency")) {
            frequency = new InventoryFrequency(nbtTags.getCompoundTag("frequency"));
            frequency.valid = false;
        }

        NBTTagList tagList = nbtTags.getTagList("upgradesInv", Constants.NBT.TAG_COMPOUND);
        inventory = NonNullListSynchronized.withSize(INV_SIZE, ItemStack.EMPTY);
        for (int tagCount = 0; tagCount < tagList.tagCount(); tagCount++) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(tagCount);
            byte slotID = tagCompound.getByte("Slot");
            if (slotID >= 0 && slotID < inventory.size()) {
                inventory.set(slotID, new ItemStack(tagCompound));
            }
        }

    }


    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        if (frequency != null) {
            NBTTagCompound frequencyTag = new NBTTagCompound();
            frequency.write(frequencyTag);
            nbtTags.setTag("frequency", frequencyTag);
        }

        //Upgrades inventory
        NBTTagList tagList = new NBTTagList();
        for (int slotCount = 0; slotCount < inventory.size(); slotCount++) {
            ItemStack stackInSlot = inventory.get(slotCount);
            if (!stackInSlot.isEmpty()) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) slotCount);
                stackInSlot.writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        nbtTags.setTag("upgradesInv", tagList);
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            int type = dataStream.readInt();
            if (type == 0) {
                String name = PacketHandler.readString(dataStream);
                boolean isPublic = dataStream.readBoolean();
                setFrequency(name, isPublic);
            } else if (type == 1) {
                String freq = PacketHandler.readString(dataStream);
                boolean isPublic = dataStream.readBoolean();
                FrequencyManager manager = getManager(new InventoryFrequency(freq, null).setPublic(isPublic));
                if (manager != null) {
                    manager.remove(freq, securityComponent.getOwnerUUID());
                }
            }
            return;
        }

        super.handlePacketData(dataStream);

        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            lastTransferLoss = dataStream.readDouble();
            lastEnvironmentLoss = dataStream.readDouble();
            if (dataStream.readBoolean()) {
                frequency = new InventoryFrequency(dataStream);
            } else {
                frequency = null;
            }

            publicCache.clear();
            privateCache.clear();

            int amount = dataStream.readInt();
            for (int i = 0; i < amount; i++) {
                publicCache.add(new InventoryFrequency(dataStream));
            }
            amount = dataStream.readInt();
            for (int i = 0; i < amount; i++) {
                privateCache.add(new InventoryFrequency(dataStream));
            }
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(lastTransferLoss);
        data.add(lastEnvironmentLoss);

        if (frequency != null) {
            data.add(true);
            frequency.write(data);
        } else {
            data.add(false);
        }

        data.add(Mekanism.publicEntangloporters.getFrequencies().size());
        for (Frequency freq : Mekanism.publicEntangloporters.getFrequencies()) {
            freq.write(data);
        }

        FrequencyManager manager = getManager(new InventoryFrequency(null, null).setPublic(false));
        if (manager != null) {
            data.add(manager.getFrequencies().size());
            for (Frequency freq : manager.getFrequencies()) {
                freq.write(data);
            }
        } else {
            data.add(0);
        }
        return data;
    }

    @Override
    public boolean sideIsOutput(EnumFacing side) {
        if (!hasFrequency()) {
            return false;
        }
        return configComponent.hasSideForData(TransmissionType.ENERGY, facing, 2, side);
    }

    @Override
    public boolean sideIsConsumer(EnumFacing side) {
        if (!hasFrequency()) {
            return false;
        }
        return configComponent.hasSideForData(TransmissionType.ENERGY, facing, 1, side);
    }

    @Override
    public double getMaxOutput() {
        return !hasFrequency() ? 0 : MekanismConfig.current().general.quantumEntangloporterEnergyTransfer.val();
    }

    @Override
    public double getEnergy() {
        return !hasFrequency() ? 0 : frequency.storedEnergy;
    }

    @Override
    public void setEnergy(double energy) {
        if (hasFrequency()) {
            frequency.storedEnergy = Math.min(MekanismConfig.current().general.quantumEntangloporterEnergyTransfer.val(), energy);
        }
    }

    @Override
    public double getMaxEnergy() {
        return !hasFrequency() ? 0 : MekanismConfig.current().general.quantumEntangloporterEnergyTransfer.val();
    }

    @Override
    public int fill(EnumFacing from, @Nonnull FluidStack resource, boolean doFill) {
        return frequency.storedFluid.fill(resource, doFill);
    }

    @Override
    @Nullable
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return frequency.storedFluid.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, @Nonnull FluidStack fluid) {
        if (hasFrequency() && configComponent.getOutput(TransmissionType.FLUID, from, facing).ioState == IOState.INPUT) {
            return FluidContainerUtils.canFill(frequency.storedFluid.getFluid(), fluid);
        }
        return false;
    }

    @Override
    public boolean canDrain(EnumFacing from, @Nullable FluidStack fluid) {
        if (hasFrequency() && configComponent.getOutput(TransmissionType.FLUID, from, facing).ioState == IOState.OUTPUT) {
            return FluidContainerUtils.canDrain(frequency.storedFluid.getFluid(), fluid);
        }
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        if (hasFrequency()) {
            if (configComponent.getOutput(TransmissionType.FLUID, from, facing).ioState != IOState.OFF) {
                return new FluidTankInfo[]{frequency.storedFluid.getInfo()};
            }
        }
        return PipeUtils.EMPTY;
    }

    @Override
    public FluidTankInfo[] getAllTanks() {
        return hasFrequency() ? new FluidTankInfo[]{frequency.storedFluid.getInfo()} : PipeUtils.EMPTY;
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        return !hasFrequency() ? 0 : frequency.storedGas.receive(stack, doTransfer);
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        return !hasFrequency() ? null : frequency.storedGas.draw(amount, doTransfer);
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        if (hasFrequency() && configComponent.getOutput(TransmissionType.GAS, side, facing).ioState == IOState.INPUT) {
            return frequency.storedGas.getGasType() == null || type == frequency.storedGas.getGasType();
        }
        return false;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        if (hasFrequency() && configComponent.getOutput(TransmissionType.GAS, side, facing).ioState == IOState.OUTPUT) {
            return frequency.storedGas.getGasType() == null || type == frequency.storedGas.getGasType();
        }
        return false;
    }

    @Nonnull
    @Override
    public GasTankInfo[] getTankInfo() {
        return hasFrequency() ? new GasTankInfo[]{frequency.storedGas} : IGasHandler.NONE;
    }

    @Override
    public boolean handleInventory() {
        return false;
    }

    @Override
    public NonNullListSynchronized<ItemStack> getInventory() {
        return hasFrequency() ? frequency.inventory : null;
    }

    @Override
    public double getTemp() {
        return hasFrequency() ? frequency.temperature : 0;
    }

    @Override
    public double getInverseConductionCoefficient() {
        return 1;
    }

    @Override
    public double getInsulationCoefficient(EnumFacing side) {
        return 1000;
    }

    @Override
    public void transferHeatTo(double heat) {
        heatToAbsorb += heat;
    }

    @Override
    public double[] simulateHeat() {
        return HeatUtils.simulate(this);
    }

    @Override
    public double applyTemperatureChange() {
        if (hasFrequency()) {
            frequency.temperature += heatToAbsorb;
        }
        heatToAbsorb = 0;
        return hasFrequency() ? frequency.temperature : 0;
    }

    @Override
    public boolean canConnectHeat(EnumFacing side) {
        return hasFrequency() && configComponent.getOutput(TransmissionType.HEAT, side, facing).ioState != IOState.OFF;
    }

    @Override
    public IHeatTransfer getAdjacent(EnumFacing side) {
        TileEntity adj = Coord4D.get(this).offset(side).getTileEntity(world);
        if (hasFrequency() && configComponent.getOutput(TransmissionType.HEAT, side, facing).ioState == IOState.INPUT) {
            if (CapabilityUtils.hasCapability(adj, Capabilities.HEAT_TRANSFER_CAPABILITY, side.getOpposite())) {
                return CapabilityUtils.getCapability(adj, Capabilities.HEAT_TRANSFER_CAPABILITY, side.getOpposite());
            }
        }
        return null;
    }

    @Override
    public boolean canInsertItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        return hasFrequency() && configComponent.getOutput(TransmissionType.ITEM, side, facing).ioState == IOState.INPUT;
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        if (hasFrequency() && configComponent.getOutput(TransmissionType.ITEM, side, facing).ioState != IOState.OFF) {
            return new int[]{0};
        }
        return InventoryUtils.EMPTY;
    }

    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        return hasFrequency() && configComponent.getOutput(TransmissionType.ITEM, side, facing).ioState == IOState.OUTPUT;
    }

    @Override
    public Object[] getTanks() {
        if (!hasFrequency()) {
            return null;
        }
        return new Object[]{frequency.storedFluid, frequency.storedGas};
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
    public TileComponentSecurity getSecurity() {
        return securityComponent;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return false;
        }
        return capability == Capabilities.GAS_HANDLER_CAPABILITY || capability == Capabilities.HEAT_TRANSFER_CAPABILITY
                || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return null;
        }
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY || capability == Capabilities.HEAT_TRANSFER_CAPABILITY) {
            return (T) this;
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FluidHandlerWrapper(this, side));
        }
        return super.getCapability(capability, side);
    }

    @Override
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side) {
        if (configComponent.isCapabilityDisabled(capability, side, facing)) {
            return true;
        } else if (capability == Capabilities.GAS_HANDLER_CAPABILITY || capability == Capabilities.HEAT_TRANSFER_CAPABILITY ||
                capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return side != null && (!hasFrequency() || configComponent.isCapabilityDisabled(capability, side, facing));
        }
        return super.isCapabilityDisabled(capability, side);
    }

    @Override
    public String[] getMethods() {
        return methods;
    }

    @Override
    public Object[] invoke(int method, Object[] arguments) throws NoSuchMethodException {
        if (method == 0) {
            if (!(arguments[0] instanceof String) || !(arguments[1] instanceof Boolean)) {
                return new Object[]{"Invalid parameters."};
            }
            String freq = ((String) arguments[0]).trim();
            boolean isPublic = (Boolean) arguments[1];
            setFrequency(freq, isPublic);
            return new Object[]{"Frequency set."};
        }
        throw new NoSuchMethodException();
    }

    @Override
    public TileComponentChunkLoader getChunkLoader() {
        return chunkLoaderComponent;
    }

    @Override
    public Set<ChunkPos> getChunkSet() {
        Set<ChunkPos> ret = new ObjectOpenHashSet<>();
        ret.add(new Chunk3D(Coord4D.get(this)).getPos());
        return ret;
    }

    @Override
    public TileComponentUpgrade getComponent() {
        return this.upgradeComponent;
    }
}
