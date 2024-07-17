package mekanism.common.network;

import io.netty.buffer.ByteBuf;
import mekanism.common.Mekanism;
import mekanism.common.PacketHandler;
import mekanism.common.item.armor.ItemMekAsuitFeetArmour;
import mekanism.common.moduleUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketStepAssistData implements IMessageHandler<PacketStepAssistData.StepAssistDataMessage, IMessage> {

    @Override
    public IMessage onMessage(StepAssistDataMessage message, MessageContext ctx) {
        EntityPlayer entityPlayer = PacketHandler.getPlayer(ctx);

        PacketHandler.handlePacket(() -> {
            if (message.packetType == StepAssistPacket.UPDATE) {
                if (message.value) {
                    Mekanism.stepAssistOn.add(message.uuid);
                } else {
                    Mekanism.stepAssistOn.remove(message.uuid);
                }
                if (!entityPlayer.world.isRemote) {
                    Mekanism.packetHandler.sendToDimension(new StepAssistDataMessage(StepAssistPacket.UPDATE, message.uuid, message.value),
                            entityPlayer.world.provider.getDimension());
                }
            } else if (message.packetType == StepAssistPacket.MODE) {
                ItemStack stack = entityPlayer.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                if (!stack.isEmpty() && stack.getItem() instanceof ItemMekAsuitFeetArmour feet && feet.isUpgradeInstalled(stack, moduleUpgrade.HYDRAULIC_PROPULSION_UNIT)) {
                    if (!message.value) {
                        feet.incrementStepAssistMode(stack);
                    } else {
                        feet.setStepAssistMode(stack, ItemMekAsuitFeetArmour.StepAssist.OFF);
                    }
                }
            }
        }, entityPlayer);
        return null;
    }

    public enum StepAssistPacket {
        UPDATE,
        FULL,
        MODE
    }

    public static class StepAssistDataMessage implements IMessage {

        public StepAssistPacket packetType;

        public UUID uuid;
        public boolean value;

        public StepAssistDataMessage() {
        }

        public StepAssistDataMessage(StepAssistPacket packetType, UUID uuid, boolean value) {
            this.packetType = packetType;
            this.value = value;
            if (packetType == StepAssistPacket.UPDATE) {
                this.uuid = uuid;
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(packetType.ordinal());
            if (packetType == StepAssistPacket.MODE) {
                buf.writeBoolean(value);
            } else if (packetType == StepAssistPacket.UPDATE) {
                PacketHandler.writeUUID(buf, uuid);
                buf.writeBoolean(value);
            } else if (packetType == StepAssistPacket.FULL) {
                buf.writeInt(Mekanism.stepAssistOn.size());
                synchronized (Mekanism.stepAssistOn) {
                    for (UUID uuidToSend : Mekanism.stepAssistOn) {
                        PacketHandler.writeUUID(buf, uuidToSend);
                    }
                }
            }
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            packetType = StepAssistPacket.values()[buf.readInt()];
            if (packetType == StepAssistPacket.MODE) {
                value = buf.readBoolean();
            } else if (packetType == StepAssistPacket.UPDATE) {
                uuid = PacketHandler.readUUID(buf);
                value = buf.readBoolean();
            } else if (packetType == StepAssistPacket.FULL) {
                Mekanism.stepAssistOn.clear();
                int amount = buf.readInt();
                for (int i = 0; i < amount; i++) {
                    Mekanism.stepAssistOn.add(PacketHandler.readUUID(buf));
                }
            }
        }
    }
}
