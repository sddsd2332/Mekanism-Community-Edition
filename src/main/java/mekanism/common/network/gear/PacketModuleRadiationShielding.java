package mekanism.common.network.gear;

import io.netty.buffer.ByteBuf;
import mekanism.api.gear.IModule;
import mekanism.common.MekanismModules;
import mekanism.common.PacketHandler;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.network.gear.PacketModuleRadiationShielding.ModuleRadiationShieldingMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketModuleRadiationShielding implements IMessageHandler<ModuleRadiationShieldingMessage, IMessage> {

    @Override
    public IMessage onMessage(ModuleRadiationShieldingMessage message, MessageContext context) {
        EntityPlayer player = PacketHandler.getPlayer(context);
        if (player == null) {
            return null;
        }
        PacketHandler.handlePacket(() -> {
            ItemStack stack = player.getItemStackFromSlot(message.slot);
            IModule<?> radiation = ModuleHelper.get().load(stack, MekanismModules.RADIATION_SHIELDING_UNIT);
            if (radiation != null) {
          //      radiation.getData().set(!radiation.isEnabled(), null);
            }
        }, player);
        return null;
    }


    public static class ModuleRadiationShieldingMessage implements IMessage {

        private EntityEquipmentSlot slot;


        public ModuleRadiationShieldingMessage() {
        }


        public ModuleRadiationShieldingMessage(EntityEquipmentSlot slot) {
            this.slot = slot;
        }


        @Override
        public void toBytes(ByteBuf dataStream) {
            dataStream.writeInt(slot.ordinal());

        }

        @Override
        public void fromBytes(ByteBuf dataStream) {
            slot = EntityEquipmentSlot.values()[dataStream.readInt()];
        }
    }
}
