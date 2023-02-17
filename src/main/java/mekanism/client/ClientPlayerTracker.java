package mekanism.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mekanism.common.Mekanism;

@SideOnly(Side.CLIENT)
public class ClientPlayerTracker
{
	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerChangedDimensionEvent event)
	{
		Mekanism.jetpackOn.remove(event.player);
		Mekanism.gasmaskOn.remove(event.player);
		Mekanism.flamethrowerActive.remove(event.player);
	}
}
