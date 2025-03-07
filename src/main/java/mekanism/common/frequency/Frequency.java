package mekanism.common.frequency;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mekanism.api.Coord4D;
import mekanism.api.TileNetworkList;
import mekanism.common.PacketHandler;
import mekanism.common.util.MekanismUtils;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

public class Frequency {

    public static final String TELEPORTER = "Teleporter";

    public String name;
    public UUID ownerUUID;
    public String clientOwner;

    public boolean valid = true;

    public boolean publicFreq;

    public Set<Coord4D> activeCoords = new ObjectOpenHashSet<>();

    public Frequency(String n, UUID uuid) {
        name = n;
        ownerUUID = uuid;
    }

    public Frequency(NBTTagCompound nbtTags) {
        read(nbtTags);
    }

    public Frequency(ByteBuf dataStream) {
        read(dataStream);
    }

    public boolean isPublic() {
        return publicFreq;
    }

    public Frequency setPublic(boolean isPublic) {
        publicFreq = isPublic;
        return this;
    }

    public boolean isPrivate() {
        return !publicFreq;
    }

    public Coord4D getClosestCoords(Coord4D coord) {
        Coord4D closest = null;
        for (Coord4D iterCoord : activeCoords) {
            if (iterCoord.equals(coord)) {
                continue;
            }
            if (closest == null) {
                closest = iterCoord;
                continue;
            }

            if (coord.dimensionId != closest.dimensionId && coord.dimensionId == iterCoord.dimensionId) {
                closest = iterCoord;
            } else if (coord.dimensionId != closest.dimensionId || coord.dimensionId == iterCoord.dimensionId) {
                if (coord.distanceTo(closest) > coord.distanceTo(iterCoord)) {
                    closest = iterCoord;
                }
            }
        }
        return closest;
    }

    public void write(NBTTagCompound nbtTags) {
        nbtTags.setString("name", name);
        nbtTags.setString("ownerUUID", ownerUUID.toString());
        nbtTags.setBoolean("publicFreq", publicFreq);
    }

    protected void read(NBTTagCompound nbtTags) {
        name = nbtTags.getString("name");
        ownerUUID = UUID.fromString(nbtTags.getString("ownerUUID"));
        publicFreq = nbtTags.getBoolean("publicFreq");
    }

    public void write(TileNetworkList data) {
        data.add(name);
        data.add(ownerUUID.toString());
        data.add(MekanismUtils.getLastKnownUsername(ownerUUID));
        data.add(publicFreq);
    }

    protected void read(ByteBuf dataStream) {
        name = PacketHandler.readString(dataStream);
        ownerUUID = UUID.fromString(PacketHandler.readString(dataStream));
        clientOwner = PacketHandler.readString(dataStream);
        publicFreq = dataStream.readBoolean();
    }

    @Override
    public int hashCode() {
        int code = 1;
        code = 31 * code + name.hashCode();
        code = 31 * code + ownerUUID.hashCode();
        code = 31 * code + (publicFreq ? 1 : 0);
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Frequency frequency && frequency.name.equals(name) && frequency.ownerUUID.equals(ownerUUID) && frequency.publicFreq == publicFreq;
    }

    public Identity getIdentity() {
        return new Identity(name, publicFreq);
    }

    public static class Identity {

        public String name;
        public boolean publicFreq;

        private Identity(String name, boolean publicFreq) {
            this.name = name;
            this.publicFreq = publicFreq;
        }

        @Nullable
        public static Identity load(NBTTagCompound data) {
            if (!data.getString("name").isEmpty()) {
                return new Identity(data.getString("name"), data.getBoolean("publicFreq"));
            }
            return null;
        }

        public NBTTagCompound serialize() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("name", name);
            tag.setBoolean("publicFreq", publicFreq);
            return tag;
        }
    }
}
