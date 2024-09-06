package mekanism.common;

import io.netty.buffer.ByteBuf;
import mekanism.api.Coord4D;
import mekanism.api.Range4D;
import mekanism.common.base.ITileNetwork;
import mekanism.common.config.MekanismConfig;
import mekanism.common.network.*;
import mekanism.common.network.PacketBoxBlacklist.BoxBlacklistMessage;
import mekanism.common.network.PacketConfigSync.ConfigSyncMessage;
import mekanism.common.network.PacketConfigurationUpdate.ConfigurationUpdateMessage;
import mekanism.common.network.PacketContainerEditMode.ContainerEditModeMessage;
import mekanism.common.network.PacketDataRequest.DataRequestMessage;
import mekanism.common.network.PacketDigitalMinerGui.DigitalMinerGuiMessage;
import mekanism.common.network.PacketDropperUse.DropperUseMessage;
import mekanism.common.network.PacketEditFilter.EditFilterMessage;
import mekanism.common.network.PacketEntityMove.EntityMoveMessage;
import mekanism.common.network.PacketFlamethrowerData.FlamethrowerDataMessage;
import mekanism.common.network.PacketFreeRunnerData.FreeRunnerDataMessage;
import mekanism.common.network.PacketItemStack.ItemStackMessage;
import mekanism.common.network.PacketJetpackData.JetpackDataMessage;
import mekanism.common.network.PacketJumpBoostData.JumpBoostDataMessage;
import mekanism.common.network.PacketKey.KeyMessage;
import mekanism.common.network.PacketLogisticalSorterGui.LogisticalSorterGuiMessage;
import mekanism.common.network.PacketNewFilter.NewFilterMessage;
import mekanism.common.network.PacketOredictionificatorGui.OredictionificatorGuiMessage;
import mekanism.common.network.PacketPortableTeleporter.PortableTeleporterMessage;
import mekanism.common.network.PacketPortalFX.PortalFXMessage;
import mekanism.common.network.PacketRedstoneControl.RedstoneControlMessage;
import mekanism.common.network.PacketRemoveUpgrade.RemoveUpgradeMessage;
import mekanism.common.network.PacketRobit.RobitMessage;
import mekanism.common.network.PacketScubaTankData.ScubaTankDataMessage;
import mekanism.common.network.PacketSecurityMode.SecurityModeMessage;
import mekanism.common.network.PacketSecurityUpdate.SecurityUpdateMessage;
import mekanism.common.network.PacketSimpleGui.SimpleGuiMessage;
import mekanism.common.network.PacketStepAssistData.StepAssistDataMessage;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.network.PacketTransmitterUpdate.TransmitterUpdateMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * Mekanism packet handler. As always, use packets sparingly!
 *
 * @author AidanBrady
 */
public class PacketHandler {

    public final SimpleNetworkWrapper netHandler = NetworkRegistry.INSTANCE.newSimpleChannel("MEK");

    /**
     * Encodes an Object[] of data into a DataOutputStream.
     *
     * @param dataValues - an Object[] of data to encode
     * @param output     - the output stream to write to
     */
    public static void encode(Object[] dataValues, ByteBuf output) {
        for (Object data : dataValues) {
            if (data instanceof Byte b) {
                output.writeByte(b);
            } else if (data instanceof Integer i) {
                output.writeInt(i);
            } else if (data instanceof Short s) {
                output.writeShort(s);
            } else if (data instanceof Long l) {
                output.writeLong(l);
            } else if (data instanceof Boolean b) {
                output.writeBoolean(b);
            } else if (data instanceof Double d) {
                output.writeDouble(d);
            } else if (data instanceof Float f) {
                output.writeFloat(f);
            } else if (data instanceof String s) {
                writeString(output, s);
            } else if (data instanceof EnumFacing f) {
                output.writeInt(f.ordinal());
            } else if (data instanceof ItemStack s) {
                writeStack(output, s);
            } else if (data instanceof NBTTagCompound nbt) {
                writeNBT(output, nbt);
            } else if (data instanceof int[] d) {
                for (int i : d) {
                    output.writeInt(i);
                }
            } else if (data instanceof byte[] d) {
                for (byte b :d) {
                    output.writeByte(b);
                }
            } else if (data instanceof ArrayList<?> a) {
                encode(a.toArray(), output);
            } else if (data instanceof NonNullList<?> d ) {
                encode(d.toArray(), output);
            } else {
                throw new RuntimeException("Un-encodable data passed to encode(): " + data + ", full data: " + Arrays.toString(dataValues));
            }
        }
    }

    public static void writeString(ByteBuf output, String s) {
        ByteBufUtils.writeUTF8String(output, s);
    }

    public static String readString(ByteBuf input) {
        return ByteBufUtils.readUTF8String(input);
    }

    public static void writeStack(ByteBuf output, ItemStack stack) {
        ByteBufUtils.writeItemStack(output, stack);
    }

    public static ItemStack readStack(ByteBuf input) {
        return ByteBufUtils.readItemStack(input);
    }

    public static void writeNBT(ByteBuf output, NBTTagCompound nbtTags) {
        ByteBufUtils.writeTag(output, nbtTags);
    }

    public static NBTTagCompound readNBT(ByteBuf input) {
        return ByteBufUtils.readTag(input);
    }

    public static void log(String log) {
        if (MekanismConfig.current().general.logPackets.val()) {
            Mekanism.logger.info(log);
        }
    }

    public static EntityPlayer getPlayer(MessageContext context) {
        return Mekanism.proxy.getPlayer(context);
    }

    public static void handlePacket(Runnable runnable, EntityPlayer player) {
        Mekanism.proxy.handlePacket(runnable, player);
    }

    public void initialize() {
        netHandler.registerMessage(PacketRobit.class, RobitMessage.class, 0, Side.SERVER);
        netHandler.registerMessage(PacketTransmitterUpdate.class, TransmitterUpdateMessage.class, 1, Side.CLIENT);
        //FREE ID 2
        //FREE ID 3
        netHandler.registerMessage(PacketItemStack.class, ItemStackMessage.class, 4, Side.SERVER);
        netHandler.registerMessage(PacketTileEntity.class, TileEntityMessage.class, 5, Side.CLIENT);
        netHandler.registerMessage(PacketTileEntity.class, TileEntityMessage.class, 5, Side.SERVER);
        netHandler.registerMessage(PacketPortalFX.class, PortalFXMessage.class, 6, Side.CLIENT);
        netHandler.registerMessage(PacketDataRequest.class, DataRequestMessage.class, 7, Side.SERVER);
        netHandler.registerMessage(PacketOredictionificatorGui.class, OredictionificatorGuiMessage.class, 8, Side.CLIENT);
        netHandler.registerMessage(PacketOredictionificatorGui.class, OredictionificatorGuiMessage.class, 8, Side.SERVER);
        netHandler.registerMessage(PacketSecurityMode.class, SecurityModeMessage.class, 9, Side.SERVER);
        netHandler.registerMessage(PacketPortableTeleporter.class, PortableTeleporterMessage.class, 10, Side.CLIENT);
        netHandler.registerMessage(PacketPortableTeleporter.class, PortableTeleporterMessage.class, 10, Side.SERVER);
        netHandler.registerMessage(PacketRemoveUpgrade.class, RemoveUpgradeMessage.class, 11, Side.SERVER);
        netHandler.registerMessage(PacketRedstoneControl.class, RedstoneControlMessage.class, 12, Side.SERVER);
        //FREE ID 13
        netHandler.registerMessage(PacketLogisticalSorterGui.class, LogisticalSorterGuiMessage.class, 14, Side.CLIENT);
        netHandler.registerMessage(PacketLogisticalSorterGui.class, LogisticalSorterGuiMessage.class, 14, Side.SERVER);
        netHandler.registerMessage(PacketNewFilter.class, NewFilterMessage.class, 15, Side.SERVER);
        netHandler.registerMessage(PacketEditFilter.class, EditFilterMessage.class, 16, Side.SERVER);
        netHandler.registerMessage(PacketConfigurationUpdate.class, ConfigurationUpdateMessage.class, 17, Side.SERVER);
        netHandler.registerMessage(PacketSimpleGui.class, SimpleGuiMessage.class, 18, Side.CLIENT);
        netHandler.registerMessage(PacketSimpleGui.class, SimpleGuiMessage.class, 18, Side.SERVER);
        netHandler.registerMessage(PacketDigitalMinerGui.class, DigitalMinerGuiMessage.class, 19, Side.CLIENT);
        netHandler.registerMessage(PacketDigitalMinerGui.class, DigitalMinerGuiMessage.class, 19, Side.SERVER);
        netHandler.registerMessage(PacketJetpackData.class, JetpackDataMessage.class, 20, Side.CLIENT);
        netHandler.registerMessage(PacketJetpackData.class, JetpackDataMessage.class, 20, Side.SERVER);
        netHandler.registerMessage(PacketKey.class, KeyMessage.class, 21, Side.SERVER);
        netHandler.registerMessage(PacketScubaTankData.class, ScubaTankDataMessage.class, 22, Side.CLIENT);
        netHandler.registerMessage(PacketScubaTankData.class, ScubaTankDataMessage.class, 22, Side.SERVER);
        netHandler.registerMessage(PacketConfigSync.class, ConfigSyncMessage.class, 23, Side.CLIENT);
        netHandler.registerMessage(PacketBoxBlacklist.class, BoxBlacklistMessage.class, 24, Side.CLIENT);
        //FREE ID 25
        netHandler.registerMessage(PacketContainerEditMode.class, ContainerEditModeMessage.class, 26, Side.SERVER);
        netHandler.registerMessage(PacketFlamethrowerData.class, FlamethrowerDataMessage.class, 27, Side.CLIENT);
        netHandler.registerMessage(PacketFlamethrowerData.class, FlamethrowerDataMessage.class, 27, Side.SERVER);
        netHandler.registerMessage(PacketDropperUse.class, DropperUseMessage.class, 28, Side.SERVER);
        netHandler.registerMessage(PacketEntityMove.class, EntityMoveMessage.class, 29, Side.CLIENT);
        netHandler.registerMessage(PacketSecurityUpdate.class, SecurityUpdateMessage.class, 30, Side.CLIENT);
        netHandler.registerMessage(PacketFreeRunnerData.class, FreeRunnerDataMessage.class, 31, Side.CLIENT);
        netHandler.registerMessage(PacketFreeRunnerData.class, FreeRunnerDataMessage.class, 31, Side.SERVER);
        netHandler.registerMessage(PacketJumpBoostData.class, JumpBoostDataMessage.class,32,Side.CLIENT);
        netHandler.registerMessage(PacketJumpBoostData.class, JumpBoostDataMessage.class,32,Side.SERVER);
        netHandler.registerMessage(PacketStepAssistData.class, StepAssistDataMessage.class,33,Side.CLIENT);
        netHandler.registerMessage(PacketStepAssistData.class, StepAssistDataMessage.class,33,Side.SERVER);
    }

    /**
     * Send this message to the specified player.
     *
     * @param message - the message to send
     * @param player  - the player to send it to
     */
    public synchronized void sendTo(IMessage message, EntityPlayerMP player) {
        netHandler.sendTo(message, player);
    }

    /**
     * Send this message to everyone connected to the server.
     *
     * @param message - message to send
     */
    public synchronized void sendToAll(IMessage message) {
        netHandler.sendToAll(message);
    }

    /**
     * Send this message to everyone within a certain range of a point.
     *
     * @param message - the message to send
     * @param point   - the TargetPoint around which to send
     */
    public synchronized void sendToAllAround(IMessage message, TargetPoint point) {
        netHandler.sendToAllAround(message, point);
    }

    /**
     * Send this message to everyone within the supplied dimension.
     *
     * @param message     - the message to send
     * @param dimensionId - the dimension id to target
     */
    public synchronized void sendToDimension(IMessage message, int dimensionId) {
        netHandler.sendToDimension(message, dimensionId);
    }

    /**
     * Send this message to the server.
     *
     * @param message - the message to send
     */
    public void sendToServer(IMessage message) {
        netHandler.sendToServer(message);
    }

    /**
     * Send this message to all players within a defined AABB cuboid.
     *
     * @param message - the message to send
     * @param cuboid  - the AABB cuboid to send the packet in
     * @param dimId   - the dimension the cuboid is in
     */
    public synchronized void sendToCuboid(IMessage message, AxisAlignedBB cuboid, int dimId) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null && cuboid != null) {
            for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
                if (player.dimension == dimId && cuboid.contains(new Vec3d(player.posX, player.posY, player.posZ))) {
                    sendTo(message, player);
                }
            }
        }
    }

    public <TILE extends TileEntity & ITileNetwork> void sendUpdatePacket(TILE tile) {
        sendToAllTracking(new TileEntityMessage(tile), tile);
    }

    public void sendToAllTracking(IMessage message, TileEntity tile) {
        BlockPos pos = tile.getPos();
        sendToAllTracking(message, tile.getWorld().provider.getDimension(), pos.getX(), pos.getY(), pos.getZ());
    }

    public void sendToAllTracking(IMessage message, Coord4D point) {
        sendToAllTracking(message, point.dimensionId, point.x, point.y, point.z);
    }

    public void sendToAllTracking(IMessage message, int dimension, double x, double y, double z) {
        //Range is ignored for sendToAllTracking, and only gets sent to clients that have the location loaded
        sendToAllTracking(message, new TargetPoint(dimension, x, y, z, 1));
    }

    public synchronized void sendToAllTracking(IMessage message, TargetPoint point) {
        netHandler.sendToAllTracking(message, point);
    }

    //TODO: change Network stuff over to using this
    public void sendToReceivers(IMessage message, Range4D range) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null) {
            for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
                if (range.hasPlayerInRange(player)) {
                    sendTo(message, player);
                }
            }
        }
    }

    @Nonnull
    public static UUID readUUID(ByteBuf dataStream) {
        return new UUID(dataStream.readLong(), dataStream.readLong());
    }

    public static void writeUUID(ByteBuf dataStream, UUID uuid) {
        dataStream.writeLong(uuid.getMostSignificantBits());
        dataStream.writeLong(uuid.getLeastSignificantBits());
    }
}
