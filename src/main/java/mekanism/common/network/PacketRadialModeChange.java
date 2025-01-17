package mekanism.common.network;

import io.netty.buffer.ByteBuf;
import mekanism.common.PacketHandler;
import mekanism.common.item.interfaces.IRadialModeItem;
import mekanism.common.item.interfaces.IRadialSelectorEnum;
import mekanism.common.network.PacketRadialModeChange.RadialModeChangeMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRadialModeChange implements IMessageHandler<RadialModeChangeMessage, IMessage> {


    @Override
    public IMessage onMessage(RadialModeChangeMessage message, MessageContext context) {
        EntityPlayer player = PacketHandler.getPlayer(context);
        if (player == null) {
            return null;
        }
        PacketHandler.handlePacket(() -> {
            ItemStack stack = player.getItemStackFromSlot(message.slot);
            if (!stack.isEmpty() && stack.getItem() instanceof IRadialModeItem) {
                setMode(stack, (IRadialModeItem<?>) stack.getItem(), player, message);
            }
        }, player);
        return null;
    }


    public <TYPE extends Enum<TYPE> & IRadialSelectorEnum<TYPE>> void setMode(ItemStack stack, IRadialModeItem<TYPE> item, EntityPlayer player, RadialModeChangeMessage message) {
        item.setMode(stack, player, item.getModeByIndex(message.change));
    }

    public static class RadialModeChangeMessage implements IMessage {

        private EntityEquipmentSlot slot;
        private int change;

        public RadialModeChangeMessage() {
        }

        public RadialModeChangeMessage(EntityEquipmentSlot slot, int change) {
            this.slot = slot;
            this.change = change;
        }

        @Override
        public void toBytes(ByteBuf dataStream) {
            dataStream.writeInt(slot.ordinal());
            dataStream.writeInt(change);
        }

        @Override
        public void fromBytes(ByteBuf dataStream) {
            slot = EntityEquipmentSlot.values()[dataStream.readInt()];
            change = dataStream.readInt();
        }


    }
}
