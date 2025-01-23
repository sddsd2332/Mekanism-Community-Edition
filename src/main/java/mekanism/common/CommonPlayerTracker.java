package mekanism.common;

import mekanism.common.config.MekanismConfig;
import mekanism.common.network.PacketBoxBlacklist.BoxBlacklistMessage;
import mekanism.common.network.PacketConfigSync.ConfigSyncMessage;
import mekanism.common.network.PacketPlayerData.PlayerDataMessage;
import mekanism.common.network.PacketSecurityUpdate.SecurityPacket;
import mekanism.common.network.PacketSecurityUpdate.SecurityUpdateMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class CommonPlayerTracker {

    public CommonPlayerTracker() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerLoginEvent(PlayerLoggedInEvent event) {
        MinecraftServer server = event.player.getServer();
        if (!event.player.world.isRemote) {
            if (server == null || !server.isSinglePlayer()) {
                Mekanism.packetHandler.sendTo(new ConfigSyncMessage(MekanismConfig.local()), (EntityPlayerMP) event.player);
                Mekanism.logger.info("Sent config to '" + event.player.getDisplayNameString() + ".'");
            }
            Mekanism.packetHandler.sendTo(new BoxBlacklistMessage(), (EntityPlayerMP) event.player);
            Mekanism.packetHandler.sendTo(new SecurityUpdateMessage(SecurityPacket.FULL, null, null), (EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onPlayerLogoutEvent(PlayerLoggedOutEvent event) {
        Mekanism.playerState.clearPlayer(event.player.getUniqueID(), false);
        Mekanism.playerState.clearPlayerServerSideOnly(event.player.getUniqueID());
    }

    @SubscribeEvent
    public void onPlayerDimChangedEvent(PlayerChangedDimensionEvent event) {
        Mekanism.playerState.clearPlayer(event.player.getUniqueID(), false);
        Mekanism.playerState.reapplyServerSideOnly(event.player);
    }


    @SubscribeEvent
    public void onPlayerStartTrackingEvent(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof EntityPlayer && event.getEntityPlayer() instanceof EntityPlayerMP) {
            Mekanism.packetHandler.sendTo(new PlayerDataMessage(event.getTarget().getUniqueID()), (EntityPlayerMP) event.getEntityPlayer());
        }
    }
}
