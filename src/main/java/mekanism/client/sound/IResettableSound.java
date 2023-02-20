package mekanism.client.sound;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.ITickableSound;

@SideOnly(Side.CLIENT)
public interface IResettableSound extends ITickableSound
{
	public void reset();
}
