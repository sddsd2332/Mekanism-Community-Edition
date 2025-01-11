package mekanism.common.network;

import baubles.api.BaublesApi;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mekanism.common.Mekanism;
import mekanism.common.PacketHandler;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.item.interfaces.IJetpackItem;
import mekanism.common.item.interfaces.IJetpackItem.JetpackMode;
import mekanism.common.network.PacketJetpackData.JetpackDataMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.IItemHandler;

import java.util.Set;
import java.util.UUID;

public class PacketJetpackData implements IMessageHandler<JetpackDataMessage, IMessage> {

    @Override
    public IMessage onMessage(JetpackDataMessage message, MessageContext context) {
        // Queue up the processing on the central thread
        EntityPlayer player = PacketHandler.getPlayer(context);
        PacketHandler.handlePacket(() -> {
            if (message.packetType == JetpackPacket.UPDATE) {
                Mekanism.playerState.setJetpackState(message.uuid, message.value, false);
                // If we got this packet on the server, propagate it out to all players in the same
                // dimension
                // TODO: Why is this a dimensional thing?!
                // because we dont send a packet when a player starts tracking another player (net.minecraftforge.event.entity.player.PlayerEvent.StartTracking)
                if (!player.world.isRemote) {
                    Mekanism.packetHandler.sendToDimension(message, player.world.provider.getDimension());
                }
            } else if (message.packetType == JetpackPacket.MODE) {
                // Use has changed the mode of their jetpack; update it
                ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (!stack.isEmpty() && stack.getItem() instanceof IJetpackItem jetpackItem) {
                    JetpackMode mode = jetpackItem.getJetpackMode(stack);
                    JetpackMode newMode = mode.adjust(!message.value  ? 1 : -1);
                    jetpackItem.setMode(stack,newMode);
                }else if (Mekanism.hooks.Baubles){
                    getBaublesJetpackMode(message,player);
                }
            } else if (message.packetType == JetpackPacket.FULL) {
                // This is a full sync; merge it into our player state
                Mekanism.playerState.setActiveJetpacks(message.activeJetpacks);
            }
        }, player);
        return null;
    }

    @Optional.Method(modid = MekanismHooks.Baubles_MOD_ID)
    public void getBaublesJetpackMode(JetpackDataMessage message,EntityPlayer player) {
        IItemHandler baubles = BaublesApi.getBaublesHandler(player);
        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            if (stack.getItem() instanceof IJetpackItem jetpack) {
                JetpackMode mode = jetpack.getJetpackMode(stack);
                JetpackMode newMode = mode.adjust(!message.value  ? 1 : -1);
                jetpack.setMode(stack,newMode);
            }
        }
    }

    public enum JetpackPacket {
        UPDATE,
        FULL,
        MODE
    }

    public static class JetpackDataMessage implements IMessage {

        protected JetpackPacket packetType;

        protected Set<UUID> activeJetpacks;

        protected UUID uuid;
        protected boolean value;

        public JetpackDataMessage() {
        }

        public JetpackDataMessage(JetpackPacket type) {
            packetType = type;
        }

        public static JetpackDataMessage MODE_CHANGE(boolean change) {
            JetpackDataMessage m = new JetpackDataMessage(JetpackPacket.MODE);
            m.value = change;
            return m;
        }

        public static JetpackDataMessage UPDATE(UUID uuid, boolean state) {
            JetpackDataMessage m = new JetpackDataMessage(JetpackPacket.UPDATE);
            m.uuid = uuid;
            m.value = state;
            return m;
        }

        public static JetpackDataMessage FULL(Set<UUID> activeNames) {
            JetpackDataMessage m = new JetpackDataMessage(JetpackPacket.FULL);
            m.activeJetpacks = activeNames;
            return m;
        }

        @Override
        public void toBytes(ByteBuf dataStream) {
            dataStream.writeInt(packetType.ordinal());
            if (packetType == JetpackPacket.MODE) {
                dataStream.writeBoolean(value);
            } else if (packetType == JetpackPacket.UPDATE) {
                PacketHandler.writeUUID(dataStream, uuid);
                dataStream.writeBoolean(value);
            } else if (packetType == JetpackPacket.FULL) {
                dataStream.writeInt(activeJetpacks.size());
                for (UUID uuid : activeJetpacks) {
                    PacketHandler.writeUUID(dataStream, uuid);
                }
            }
        }

        @Override
        public void fromBytes(ByteBuf dataStream) {
            packetType = JetpackPacket.values()[dataStream.readInt()];
            if (packetType == JetpackPacket.MODE) {
                value = dataStream.readBoolean();
            } else if (packetType == JetpackPacket.UPDATE) {
                uuid = PacketHandler.readUUID(dataStream);
                value = dataStream.readBoolean();
            } else if (packetType == JetpackPacket.FULL) {
                activeJetpacks = new ObjectOpenHashSet<>();

                int amount = dataStream.readInt();
                for (int i = 0; i < amount; i++) {
                    activeJetpacks.add(PacketHandler.readUUID(dataStream));
                }
            }
        }
    }
}
