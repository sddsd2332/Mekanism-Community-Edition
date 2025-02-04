package mekanism.common.network;

import io.netty.buffer.ByteBuf;
import mekanism.common.PacketHandler;
import mekanism.common.network.PacketStepHeightSync.StepHeightSyncMessage;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketStepHeightSync implements IMessageHandler<StepHeightSyncMessage, IMessage> {

    @Override
    public IMessage onMessage(StepHeightSyncMessage message, MessageContext context) {
        EntityPlayer player = PacketHandler.getPlayer(context);
        if (player == null) {
            return null;
        }
        PacketHandler.handlePacket(() -> {
            if (player instanceof EntityPlayerSP p) {
                p.stepHeight = message.stepHeight;
            }
        }, player);
        return null;
    }

    public static class StepHeightSyncMessage implements IMessage {

        private float stepHeight;

        public StepHeightSyncMessage() {
        }

        public StepHeightSyncMessage(float stepHeight) {
            this.stepHeight = stepHeight;
        }

        @Override
        public void fromBytes(ByteBuf dataStream) {
            stepHeight = dataStream.readFloat();
        }

        @Override
        public void toBytes(ByteBuf dataStream) {
            dataStream.writeFloat(stepHeight);
        }
    }
}
