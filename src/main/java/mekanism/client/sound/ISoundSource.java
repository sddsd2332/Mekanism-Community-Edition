package mekanism.client.sound;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mekanism.api.Pos3D;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.util.ResourceLocation;

public interface ISoundSource
{
	@SideOnly(Side.CLIENT)
	public ResourceLocation getSoundLocation();

	@SideOnly(Side.CLIENT)
	public float getVolume();

	@SideOnly(Side.CLIENT)
	public float getPitch();

	@SideOnly(Side.CLIENT)
	public Pos3D getSoundPosition();

	@SideOnly(Side.CLIENT)
	public boolean shouldRepeat();

	@SideOnly(Side.CLIENT)
	public int getRepeatDelay();

	@SideOnly(Side.CLIENT)
	public AttenuationType getAttenuation();
}
