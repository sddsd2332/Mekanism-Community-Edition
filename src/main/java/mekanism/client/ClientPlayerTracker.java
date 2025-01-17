package mekanism.client;

import mekanism.common.Mekanism;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientPlayerTracker {

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
       Mekanism.playerState.clearPlayer(event.player.getUniqueID(),false);
        Mekanism.jumpBoostOn.remove(event.player.getUniqueID());
        Mekanism.stepAssistOn.remove(event.player.getUniqueID());
    }

}
