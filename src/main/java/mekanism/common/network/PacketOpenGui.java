package mekanism.common.network;

import io.netty.buffer.ByteBuf;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.ModuleTweakerContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class PacketOpenGui {




    static class OpenGuiMessage implements IMessage {

        @Override
        public void fromBytes(ByteBuf buf) {

        }

        @Override
        public void toBytes(ByteBuf buf) {

        }
    }
}
