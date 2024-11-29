package mekanism.common.network;

import io.netty.buffer.ByteBuf;
import mekanism.common.Mekanism;
import mekanism.common.PacketHandler;
import mekanism.common.item.armor.ItemMekAsuitFeetArmour;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.UpgradeHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketJumpBoostData implements IMessageHandler<PacketJumpBoostData.JumpBoostDataMessage, IMessage> {

    @Override
    public IMessage onMessage(JumpBoostDataMessage message, MessageContext ctx) {
        EntityPlayer entityPlayer = PacketHandler.getPlayer(ctx);

        PacketHandler.handlePacket(() -> {
            if (message.packetType == JumpBoostPacket.UPDATE) {
                if (message.value) {
                    Mekanism.jumpBoostOn.add(message.uuid);
                } else {
                    Mekanism.jumpBoostOn.remove(message.uuid);
                }
                if (!entityPlayer.world.isRemote) {
                    Mekanism.packetHandler.sendToDimension(new JumpBoostDataMessage(JumpBoostPacket.UPDATE, message.uuid, message.value),
                            entityPlayer.world.provider.getDimension());
                }
            } else if (message.packetType == JumpBoostPacket.MODE) {
                ItemStack stack = entityPlayer.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                if (!stack.isEmpty() && stack.getItem() instanceof ItemMekAsuitFeetArmour feet && UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.HYDRAULIC_PROPULSION_UNIT) && !Mekanism.hooks.DraconicEvolution) {
                    if (!message.value) {
                        feet.incrementJumpBoostMode(stack);
                    } else {
                        feet.setJumpBoostMode(stack, ItemMekAsuitFeetArmour.JumpBoost.OFF);
                    }
                }
            }
        }, entityPlayer);
        return null;
    }

    public enum JumpBoostPacket {
        UPDATE,
        FULL,
        MODE
    }

    public static class JumpBoostDataMessage implements IMessage {

        public JumpBoostPacket packetType;

        public UUID uuid;
        public boolean value;

        public JumpBoostDataMessage() {
        }

        public JumpBoostDataMessage(JumpBoostPacket packetType, UUID uuid, boolean value) {
            this.packetType = packetType;
            this.value = value;
            if (packetType == JumpBoostPacket.UPDATE) {
                this.uuid = uuid;
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(packetType.ordinal());
            if (packetType == JumpBoostPacket.MODE) {
                buf.writeBoolean(value);
            } else if (packetType == JumpBoostPacket.UPDATE) {
                PacketHandler.writeUUID(buf, uuid);
                buf.writeBoolean(value);
            } else if (packetType == JumpBoostPacket.FULL) {
                buf.writeInt(Mekanism.jumpBoostOn.size());
                synchronized (Mekanism.jumpBoostOn) {
                    for (UUID uuidToSend : Mekanism.jumpBoostOn) {
                        PacketHandler.writeUUID(buf, uuidToSend);
                    }
                }
            }
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            packetType = JumpBoostPacket.values()[buf.readInt()];
            if (packetType == JumpBoostPacket.MODE) {
                value = buf.readBoolean();
            } else if (packetType == JumpBoostPacket.UPDATE) {
                uuid = PacketHandler.readUUID(buf);
                value = buf.readBoolean();
            } else if (packetType == JumpBoostPacket.FULL) {
                Mekanism.jumpBoostOn.clear();
                int amount = buf.readInt();
                for (int i = 0; i < amount; i++) {
                    Mekanism.jumpBoostOn.add(PacketHandler.readUUID(buf));
                }
            }
        }
    }
}
