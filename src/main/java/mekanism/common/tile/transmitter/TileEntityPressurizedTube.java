package mekanism.common.tile.transmitter;

import io.netty.buffer.ByteBuf;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.*;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.block.states.BlockStateTransmitter.TransmitterType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.tier.AlloyTier;
import mekanism.common.tier.BaseTier;
import mekanism.common.tier.TubeTier;
import mekanism.common.transmitters.grid.GasNetwork;
import mekanism.common.util.CapabilityUtils;
import mekanism.common.util.GasUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public class TileEntityPressurizedTube extends TileEntityTransmitter<IGasHandler, GasNetwork, GasStack> implements IGasHandler {

    public TubeTier tier = TubeTier.BASIC;

    public float currentScale;

    public GasTank buffer = new GasTank(getCapacity());

    public GasStack lastWrite;

    private int nextTransfer = 0;

    //Read only handler for support with TOP and getting network data instead of this tube's data
    private IGasHandler nullHandler = new IGasHandler() {
        @Override
        public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
            return 0;
        }

        @Override
        public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
            return null;
        }

        @Override
        public boolean canReceiveGas(EnumFacing side, Gas type) {
            return false;
        }

        @Override
        public boolean canDrawGas(EnumFacing side, Gas type) {
            return false;
        }

        @Nonnull
        @Override
        public GasTankInfo[] getTankInfo() {
            return TileEntityPressurizedTube.this.getTankInfo();
        }
    };

    @Override
    public BaseTier getBaseTier() {
        return tier.getBaseTier();
    }

    @Override
    public void setBaseTier(BaseTier baseTier) {
        tier = TubeTier.get(baseTier);
        buffer.setMaxGas(getCapacity());
    }

    @Override
    public void doRestrictedTick() {
        if (!getWorld().isRemote) {
            updateShare();
            if (nextTransfer <= 0) {
                IGasHandler[] connectedAcceptors = GasUtils.getConnectedAcceptors(getPos(), getWorld(), getConnections(ConnectionType.PULL));
                boolean successAtLeaseOnce = false;
                for (EnumFacing side : getConnections(ConnectionType.PULL)) {
                    IGasHandler container = connectedAcceptors[side.ordinal()];
                    if (container != null) {
                        GasStack received = container.drawGas(side.getOpposite(), getAvailablePull(), false);
                        if (received != null && received.amount != 0 && takeGas(received, false) == received.amount) {
                            container.drawGas(side.getOpposite(), takeGas(received, true), true);
                            successAtLeaseOnce = true;
                        }
                    }
                }
                if (!successAtLeaseOnce) {
                    nextTransfer = 20;
                }
            } else {
                nextTransfer--;
            }
        } else {
            float targetScale = getTransmitter().hasTransmitterNetwork() ? getTransmitter().getTransmitterNetwork().gasScale : (float) buffer.getStored() / (float) buffer.getMaxGas();
            if (Math.abs(currentScale - targetScale) > 0.01) {
                currentScale = (9 * currentScale + targetScale) / 10;
            }
        }
        super.doRestrictedTick();
    }

    public int getAvailablePull() {
        if (getTransmitter().hasTransmitterNetwork()) {
            return Math.min(tier.getTubePullAmount(), getTransmitter().getTransmitterNetwork().getGasNeeded());
        }
        return Math.min(tier.getTubePullAmount(), buffer.getNeeded());
    }

    @Override
    public void updateShare() {
        if (getTransmitter().hasTransmitterNetwork() && getTransmitter().getTransmitterNetworkSize() > 0) {
            GasStack last = getSaveShare();
            if ((last != null && !(lastWrite != null && lastWrite.amount == last.amount && lastWrite.getGas() == last.getGas())) || (last == null && lastWrite != null)) {
                lastWrite = last;
                markChunkDirty();
                //markDirty();
//                this.world.markChunkDirty(this.pos, this);
            }
        }
    }

    private GasStack getSaveShare() {
        if (getTransmitter().hasTransmitterNetwork() && getTransmitter().getTransmitterNetwork().buffer != null) {
            int remain = getTransmitter().getTransmitterNetwork().buffer.amount % getTransmitter().getTransmitterNetwork().transmittersSize();
            int toSave = getTransmitter().getTransmitterNetwork().buffer.amount / getTransmitter().getTransmitterNetwork().transmittersSize();
            if (getTransmitter().getTransmitterNetwork().firstTransmitter().equals(getTransmitter())) {
                toSave += remain;
            }
            return new GasStack(getTransmitter().getTransmitterNetwork().buffer.getGas(), toSave);
        }
        return null;
    }

    @Override
    public void onChunkUnload() {
        if (!getWorld().isRemote && getTransmitter().hasTransmitterNetwork()) {
            if (lastWrite != null && getTransmitter().getTransmitterNetwork().buffer != null) {
                getTransmitter().getTransmitterNetwork().buffer.amount -= lastWrite.amount;
                if (getTransmitter().getTransmitterNetwork().buffer.amount <= 0) {
                    getTransmitter().getTransmitterNetwork().buffer = null;
                }
            }
        }
        super.onChunkUnload();
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        if (nbtTags.hasKey("tier")) {
            tier = TubeTier.values()[nbtTags.getInteger("tier")];
        }
        buffer.setMaxGas(getCapacity());
        if (nbtTags.hasKey("cacheGas")) {
            buffer.setGas(GasStack.readFromNBT(nbtTags.getCompoundTag("cacheGas")));
        } else {
            buffer.setGas(null);
        }
    }


    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        if (lastWrite != null && lastWrite.amount > 0) {
            nbtTags.setTag("cacheGas", lastWrite.write(new NBTTagCompound()));
        } else {
            nbtTags.removeTag("cacheGas");
        }
        nbtTags.setInteger("tier", tier.ordinal());
    }

    @Override
    public TransmissionType getTransmissionType() {
        return TransmissionType.GAS;
    }

    @Override
    public TransmitterType getTransmitterType() {
        return TransmitterType.PRESSURIZED_TUBE;
    }

    @Override
    public boolean isValidAcceptor(TileEntity tile, EnumFacing side) {
        return GasUtils.isValidAcceptorOnSide(tile, side);
    }

    @Override
    public boolean isValidTransmitter(TileEntity tileEntity) {
        if (!super.isValidTransmitter(tileEntity)) {
            return false;
        }
        if (!(tileEntity instanceof TileEntityPressurizedTube tube)) {
            return true;
        }
        GasStack buffer = getBufferWithFallback();
        GasStack otherBuffer = tube.getBufferWithFallback();
        return buffer == null || otherBuffer == null || buffer.isGasEqual(otherBuffer);
    }

    @Override
    public GasNetwork createNewNetwork() {
        return new GasNetwork();
    }

    @Override
    public GasNetwork createNetworkByMerging(Collection<GasNetwork> networks) {
        return new GasNetwork(networks);
    }

    @Override
    protected boolean canHaveIncompatibleNetworks() {
        return true;
    }

    @Override
    public int getCapacity() {
        return tier.getTubeCapacity();
    }

    @Nullable
    @Override
    public GasStack getBuffer() {
        if (buffer == null) {
            return null;
        }
        GasStack gas = buffer.getGas();
        return gas == null || gas.amount == 0 ? null : gas;
    }

    @Override
    public void takeShare() {
        if (getTransmitter().hasTransmitterNetwork() && getTransmitter().getTransmitterNetwork().buffer != null && lastWrite != null) {
            getTransmitter().getTransmitterNetwork().buffer.amount -= lastWrite.amount;
            buffer.setGas(lastWrite);
        }
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        if (getConnectionType(side) == ConnectionType.NORMAL || getConnectionType(side) == ConnectionType.PULL) {
            return takeGas(stack, doTransfer);
        }
        return 0;
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        return null;
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        return getConnectionType(side) == ConnectionType.NORMAL || getConnectionType(side) == ConnectionType.PULL;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        return false;
    }

    public int takeGas(GasStack gasStack, boolean doEmit) {
        if (getTransmitter().hasTransmitterNetwork()) {
            return getTransmitter().getTransmitterNetwork().emit(gasStack, doEmit);
        }
        return buffer.receive(gasStack, doEmit);
    }

    @Nonnull
    @Override
    public GasTankInfo[] getTankInfo() {
        if (getTransmitter().hasTransmitterNetwork()) {
            GasNetwork network = getTransmitter().getTransmitterNetwork();
            GasTank networkTank = new GasTank(network.getCapacity());
            networkTank.setGas(network.getBuffer());
            return new GasTankInfo[]{networkTank};
        }
        return new GasTankInfo[]{buffer};
    }

    @Override
    public IGasHandler getCachedAcceptor(EnumFacing side) {
        TileEntity tile = getCachedTile(side);
        if (CapabilityUtils.hasCapability(tile, Capabilities.GAS_HANDLER_CAPABILITY, side.getOpposite())) {
            return CapabilityUtils.getCapability(tile, Capabilities.GAS_HANDLER_CAPABILITY, side.getOpposite());
        }
        return null;
    }

    @Override
    public boolean upgrade(AlloyTier tierOrdinal) {
        if (tier.ordinal() < BaseTier.ULTIMATE.ordinal() && tierOrdinal.ordinal() == tier.ordinal()) {
            tier = TubeTier.values()[tier.ordinal() + 1];
            markDirtyTransmitters();
            sendDesc = true;
            return true;
        }
        return false;
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) throws Exception {
        tier = TubeTier.values()[dataStream.readInt()];
        super.handlePacketData(dataStream);
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        data.add(tier.ordinal());
        super.getNetworkedData(data);
        return data;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        return capability == Capabilities.GAS_HANDLER_CAPABILITY || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            if (side == null) {
                return Capabilities.GAS_HANDLER_CAPABILITY.cast(nullHandler);
            }
            return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
        }
        return super.getCapability(capability, side);
    }
}
