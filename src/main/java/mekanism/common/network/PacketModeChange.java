package mekanism.common.network;

import io.netty.buffer.ByteBuf;
import mekanism.common.Mekanism;
import mekanism.common.PacketHandler;
import mekanism.common.item.interfaces.IModeItem;
import mekanism.common.network.PacketModeChange.ModeChangMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketModeChange implements IMessageHandler<ModeChangMessage, IMessage> {

    @Override
    public IMessage onMessage(ModeChangMessage message, MessageContext context) {
        EntityPlayer player = PacketHandler.getPlayer(context);
        if (player == null) {
            return null;
        }
        PacketHandler.handlePacket(() -> {
            ItemStack stack = player.getItemStackFromSlot(message.slot);
            if (!stack.isEmpty() && stack.getItem() instanceof IModeItem modeItem) {
                try {
                    modeItem.changeMode(player, stack, message.shift, message.displayChangeMessage);
                } catch (Exception e) {
                    Mekanism.logger.error("FIXME: Packet handling error", e);
                }
            }
        }, player);
        return null;
    }

    public static class ModeChangMessage implements IMessage {

        private EntityEquipmentSlot slot;

        private boolean displayChangeMessage;

        private int shift;

        public ModeChangMessage() {
        }

        public ModeChangMessage(EntityEquipmentSlot slot, boolean holdingShift) {
            this(slot, holdingShift ? -1 : 1, true);
        }

        public ModeChangMessage(EntityEquipmentSlot slot, int shift) {
            this(slot, shift, false);
        }

        public ModeChangMessage(EntityEquipmentSlot slot, int shift, boolean displayChangeMessage) {
            this.slot = slot;
            this.shift = shift;
            this.displayChangeMessage = displayChangeMessage;
        }

        @Override
        public void toBytes(ByteBuf dataStream) {
            dataStream.writeInt(slot.ordinal());
            dataStream.writeInt(shift);
            dataStream.writeBoolean(displayChangeMessage);
        }

        @Override
        public void fromBytes(ByteBuf dataStream) {
            slot = EntityEquipmentSlot.values()[dataStream.readInt()];
            shift = dataStream.readInt();
            displayChangeMessage = dataStream.readBoolean();
        }
    }
}
