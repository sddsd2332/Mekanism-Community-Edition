package mekanism.common.network;

import io.netty.buffer.ByteBuf;
import mekanism.api.Coord4D;
import mekanism.api.gear.ModuleData;
import mekanism.common.PacketHandler;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.network.PacketRemoveModule.RemoveModuleMessage;
import mekanism.common.tile.TileEntityModificationStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRemoveModule implements IMessageHandler<RemoveModuleMessage, IMessage> {


    @Override
    public IMessage onMessage(RemoveModuleMessage message, MessageContext context) {
        EntityPlayer player = PacketHandler.getPlayer(context);
        if (player == null) {
            return null;
        }
        PacketHandler.handlePacket(() -> {
            if (player != null) {
                TileEntity tileEntity = message.coord4D.getTileEntity(player.world);
                if (tileEntity instanceof TileEntityModificationStation tile){
                    tile.removeModule(player, message.moduleType);
                }
            }
        },player);
        return null;
    }

    public static class RemoveModuleMessage implements IMessage {

        public Coord4D coord4D;
        private ModuleData<?> moduleType;

        public RemoveModuleMessage() {
        }

        private RemoveModuleMessage(Coord4D coord, ModuleData<?> moduleType) {
            coord4D = coord;
            this.moduleType = moduleType;
        }

        @Override
        public void toBytes(ByteBuf dataStream) {
            coord4D.write(dataStream);
            new PacketBuffer(dataStream).writeString(moduleType.getModuleData().getName());
        }

        @Override
        public void fromBytes(ByteBuf dataStream) {
            coord4D = Coord4D.read(dataStream);
            moduleType = ModuleHelper.get().getModuleTypeFromName(new PacketBuffer(dataStream).readString(32767));
        }

    }
}
