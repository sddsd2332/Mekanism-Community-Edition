package mekanism.api.transmitters;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import mekanism.api.Coord4D;
import mekanism.api.IClientTicker;
import mekanism.api.Range4D;
import mekanism.common.Mekanism;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

public abstract class DynamicNetwork<ACCEPTOR, NETWORK extends DynamicNetwork<ACCEPTOR, NETWORK, BUFFER>, BUFFER> implements IClientTicker, INetworkDataHandler {

    protected Set<IGridTransmitter<ACCEPTOR, NETWORK, BUFFER>> transmitters = new ReferenceOpenHashSet<>();
    protected Set<IGridTransmitter<ACCEPTOR, NETWORK, BUFFER>> transmittersToAdd = new ReferenceOpenHashSet<>();
    protected Set<IGridTransmitter<ACCEPTOR, NETWORK, BUFFER>> transmittersAdded = new ReferenceOpenHashSet<>();

    protected Set<Coord4D> possibleAcceptors = new ObjectOpenHashSet<>();
    protected Map<Coord4D, EnumSet<EnumFacing>> acceptorDirections = new Object2ObjectOpenHashMap<>();
    protected Map<IGridTransmitter<ACCEPTOR, NETWORK, BUFFER>, EnumSet<EnumFacing>> changedAcceptors = new Reference2ObjectOpenHashMap<>();
    protected Range4D packetRange = null;
    protected int capacity = 0;
    protected double doubleCapacity = 0;
    @Deprecated
    protected double meanCapacity = 0;
    protected boolean needsUpdate = false;
    protected int updateDelay = 0;
    protected boolean firstUpdate = true;
    protected World world = null;
    private Set<DelayQueue> updateQueue = new LinkedHashSet<>();

    public void addNewTransmitters(Collection<IGridTransmitter<ACCEPTOR, NETWORK, BUFFER>> newTransmitters) {
        transmittersToAdd.addAll(newTransmitters);
    }

    public void commit() {
        if (!transmittersToAdd.isEmpty()) {
            for (IGridTransmitter<ACCEPTOR, NETWORK, BUFFER> transmitter : transmittersToAdd) {
                if (transmitter.isValid()) {
                    if (world == null) {
                        world = transmitter.world();
                    }

                    for (EnumFacing side : EnumFacing.VALUES) {
                        updateTransmitterOnSide(transmitter, side);
                    }

                    transmitter.setTransmitterNetwork((NETWORK) this);
                    absorbBuffer(transmitter);
                    transmitters.add(transmitter);
                }
            }

            updateCapacity();
            clampBuffer();
            queueClientUpdate(transmittersToAdd);
            transmittersToAdd.clear();
        }

        if (!changedAcceptors.isEmpty()) {
            for (Entry<IGridTransmitter<ACCEPTOR, NETWORK, BUFFER>, EnumSet<EnumFacing>> entry : changedAcceptors.entrySet()) {
                IGridTransmitter<ACCEPTOR, NETWORK, BUFFER> transmitter = entry.getKey();
                if (transmitter.isValid()) {
                    //Update all the changed directions
                    for (EnumFacing side : entry.getValue()) {
                        updateTransmitterOnSide(transmitter, side);
                    }
                }
            }
            changedAcceptors.clear();
        }
    }

    public void updateTransmitterOnSide(IGridTransmitter<ACCEPTOR, NETWORK, BUFFER> transmitter, EnumFacing side) {
        ACCEPTOR acceptor = transmitter.getAcceptor(side);
        Coord4D acceptorCoord = transmitter.coord().offset(side);
        EnumSet<EnumFacing> directions = acceptorDirections.get(acceptorCoord);

        if (acceptor != null) {
            possibleAcceptors.add(acceptorCoord);
            if (directions != null) {
                directions.add(side.getOpposite());
            } else {
                acceptorDirections.put(acceptorCoord, EnumSet.of(side.getOpposite()));
            }
        } else if (directions != null) {
            directions.remove(side.getOpposite());

            if (directions.isEmpty()) {
                possibleAcceptors.remove(acceptorCoord);
                acceptorDirections.remove(acceptorCoord);
            }
        } else {
            possibleAcceptors.remove(acceptorCoord);
            acceptorDirections.remove(acceptorCoord);
        }
    }

    @Nullable public BUFFER getBuffer() {
        return null;
    }

    public abstract void absorbBuffer(IGridTransmitter<ACCEPTOR, NETWORK, BUFFER> transmitter);

    public abstract void clampBuffer();

    public void invalidate() {
        //Remove invalid transmitters first for share calculations
        transmitters.removeIf(transmitter -> !transmitter.isValid());

        //Clamp the new buffer
        clampBuffer();

        //Update all shares
        for (IGridTransmitter<ACCEPTOR, NETWORK, BUFFER> transmitter : transmitters) {
            transmitter.updateShare();
        }

        //Now invalidate the transmitters
        for (IGridTransmitter<ACCEPTOR, NETWORK, BUFFER> transmitter : transmitters) {
            try {
                invalidateTransmitter(transmitter);
            } catch (Exception e) {
                Mekanism.logger.error("Something went wrong: {}", transmitter);
                Mekanism.logger.error("World type : {}", world);
                Mekanism.logger.error("Whether it works : {}", transmitter.isValid());
            }

        }

        transmitters.clear();
        deregister();
    }

    public void invalidateTransmitter(IGridTransmitter<ACCEPTOR, NETWORK, BUFFER> transmitter) {
        if (!world.isRemote && transmitter.isValid()) {
            transmitter.takeShare();
            transmitter.setTransmitterNetwork(null);
            TransmitterNetworkRegistry.registerOrphanTransmitter(transmitter);
        }
    }

    public void acceptorChanged(IGridTransmitter<ACCEPTOR, NETWORK, BUFFER> transmitter, EnumFacing side) {
        EnumSet<EnumFacing> directions = changedAcceptors.get(transmitter);
        if (directions != null) {
            directions.add(side);
        } else {
            changedAcceptors.put(transmitter, EnumSet.of(side));
        }
        TransmitterNetworkRegistry.registerChangedNetwork(this);
    }

    public void adoptTransmittersAndAcceptorsFrom(NETWORK net) {
        for (IGridTransmitter<ACCEPTOR, NETWORK, BUFFER> transmitter : net.transmitters) {
            transmitter.setTransmitterNetwork((NETWORK) this);
            transmitters.add(transmitter);
            transmittersAdded.add(transmitter);
        }

        transmittersToAdd.addAll(net.transmittersToAdd);
        possibleAcceptors.addAll(net.possibleAcceptors);

        for (Entry<Coord4D, EnumSet<EnumFacing>> entry : net.acceptorDirections.entrySet()) {
            Coord4D coord = entry.getKey();
            if (acceptorDirections.containsKey(coord)) {
                acceptorDirections.get(coord).addAll(entry.getValue());
            } else {
                acceptorDirections.put(coord, entry.getValue());
            }
        }
    }

    public Range4D getPacketRange() {
        return packetRange == null ? genPacketRange() : packetRange;
    }

    protected Range4D genPacketRange() {
        if (getSize() == 0) {
            deregister();
            return null;
        }

        IGridTransmitter<ACCEPTOR, NETWORK, BUFFER> initTransmitter = transmitters.iterator().next();
        Coord4D initCoord = initTransmitter.coord();

        int minX = initCoord.x;
        int minY = initCoord.y;
        int minZ = initCoord.z;
        int maxX = initCoord.x;
        int maxY = initCoord.y;
        int maxZ = initCoord.z;

        for (IGridTransmitter transmitter : transmitters) {
            Coord4D coord = transmitter.coord();
            if (coord.x < minX) {
                minX = coord.x;
            } else if (coord.x > maxX) {
                maxX = coord.x;
            }
            if (coord.y < minY) {
                minY = coord.y;
            } else if (coord.y > maxY) {
                maxY = coord.y;
            }
            if (coord.z < minZ) {
                minZ = coord.z;
            } else if (coord.x > maxZ) {
                maxZ = coord.z;
            }
        }
        return new Range4D(minX, minY, minZ, maxX, maxY, maxZ, initTransmitter.world().provider.getDimension());
    }

    public void register() {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            TransmitterNetworkRegistry.getInstance().registerNetwork(this);
        } else {
            MinecraftForge.EVENT_BUS.post(new ClientTickUpdate(this, (byte) 1));
        }
    }

    public void deregister() {
        transmitters.clear();
        transmittersToAdd.clear();
        transmittersAdded.clear();

        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            TransmitterNetworkRegistry.getInstance().removeNetwork(this);
        } else {
            MinecraftForge.EVENT_BUS.post(new ClientTickUpdate(this, (byte) 0));
        }
    }

    public int getSize() {
        return transmitters.size();
    }

    public int getAcceptorSize() {
        return possibleAcceptors.size();
    }

    public synchronized void updateCapacity() {
        doubleCapacity = transmitters.stream().mapToDouble(IGridTransmitter::getCapacity).sum();
        capacity = doubleCapacity > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) doubleCapacity;
        //TODO: Remove this at some point, but kept in for now in case something is using the API and requires the meanCapacity
        updateMeanCapacity();
    }

    /**
     * Override this if things can have variable capacity along the network. An 'average' value of capacity. Calculate it how you will.
     */
    @Deprecated
    protected synchronized void updateMeanCapacity() {
        meanCapacity = transmitters.size() > 0 ? doubleCapacity / transmitters.size() : 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getCapacityAsDouble() {
        return doubleCapacity;
    }

    public World getWorld() {
        return world;
    }

    public void onParallelTick() {
    }

    public void preTick() {
    }

    public void tick() {
        onUpdate();
    }

    public void onUpdate() {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            Iterator<DelayQueue> i = updateQueue.iterator();

            try {
                while (i.hasNext()) {
                    DelayQueue q = i.next();
                    if (q.delay > 0) {
                        q.delay--;
                    } else {
                        transmittersAdded.addAll(transmitters);
                        updateDelay = 1;
                        i.remove();
                    }
                }
            } catch (Exception ignored) {
            }

            if (updateDelay > 0) {
                updateDelay--;
                if (updateDelay == 0) {
                    MinecraftForge.EVENT_BUS.post(new TransmittersAddedEvent(this, firstUpdate, (Collection) transmittersAdded));
                    firstUpdate = false;
                    transmittersAdded.clear();
                    needsUpdate = true;
                }
            }
        }
    }

    @Override
    public boolean needsTicks() {
        return getSize() > 0;
    }

    @Override
    public void clientTick() {
    }

    public void queueClientUpdate(Collection<IGridTransmitter<ACCEPTOR, NETWORK, BUFFER>> newTransmitters) {
        transmittersAdded.addAll(newTransmitters);
        updateDelay = 5;
    }

    public void addUpdate(EntityPlayer player) {
        updateQueue.add(new DelayQueue(player));
    }

    public boolean isCompatibleWith(NETWORK other) {
        return true;
    }

    public boolean compatibleWithBuffer(BUFFER buffer) {
        return true;
    }

    public Set<IGridTransmitter<ACCEPTOR, NETWORK, BUFFER>> getTransmitters() {
        return transmitters;
    }

    public boolean addTransmitter(IGridTransmitter<ACCEPTOR, NETWORK, BUFFER> transmitter) {
        return transmitters.add(transmitter);
    }

    public boolean removeTransmitter(IGridTransmitter<ACCEPTOR, NETWORK, BUFFER> transmitter) {
        boolean removed = transmitters.remove(transmitter);
        if (transmitters.isEmpty()) {
            deregister();
        }
        return removed;
    }

    public IGridTransmitter<ACCEPTOR, NETWORK, BUFFER> firstTransmitter() {
        return transmitters.iterator().next();
    }

    public int transmittersSize() {
        return transmitters.size();
    }

    public Set<IGridTransmitter<ACCEPTOR, NETWORK, BUFFER>> getTransmittersToAdd() {
        return transmittersToAdd;
    }

    public Set<IGridTransmitter<ACCEPTOR, NETWORK, BUFFER>> getTransmittersAdded() {
        return transmittersAdded;
    }

    public Set<Coord4D> getPossibleAcceptors() {
        return possibleAcceptors;
    }

    public Map<Coord4D, EnumSet<EnumFacing>> getAcceptorDirections() {
        return acceptorDirections;
    }

    public Map<IGridTransmitter<ACCEPTOR, NETWORK, BUFFER>, EnumSet<EnumFacing>> getChangedAcceptors() {
        return changedAcceptors;
    }

    public static class TransmittersAddedEvent extends Event {

        public DynamicNetwork<?, ?, ?> network;
        public boolean newNetwork;
        public Collection<IGridTransmitter> newTransmitters;

        public TransmittersAddedEvent(DynamicNetwork net, boolean newNet, Collection<IGridTransmitter> added) {
            network = net;
            newNetwork = newNet;
            newTransmitters = added;
        }
    }

    public static class ClientTickUpdate extends Event {

        public DynamicNetwork network;
        public byte operation; /*0 remove, 1 add*/

        public ClientTickUpdate(DynamicNetwork net, byte b) {
            network = net;
            operation = b;
        }
    }

    public static class NetworkClientRequest extends Event {

        public TileEntity tileEntity;

        public NetworkClientRequest(TileEntity tile) {
            tileEntity = tile;
        }
    }

    public static class DelayQueue {

        public EntityPlayer player;
        public int delay;

        public DelayQueue(EntityPlayer p) {
            player = p;
            delay = 5;
        }

        @Override
        public int hashCode() {
            return player.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof DelayQueue && ((DelayQueue) o).player.equals(this.player);
        }
    }
}
