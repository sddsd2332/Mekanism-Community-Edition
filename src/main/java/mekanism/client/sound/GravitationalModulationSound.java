package mekanism.client.sound;

import mekanism.client.ClientTickHandler;
import mekanism.common.Mekanism;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class GravitationalModulationSound extends PlayerSound {

    private static final ResourceLocation SOUND = new ResourceLocation(Mekanism.MODID, "item.gravitational_modulation_unit");

    public GravitationalModulationSound(@Nonnull EntityPlayer player) {
        super(player, SOUND);
    }

    @Override
    public boolean shouldPlaySound(@Nonnull EntityPlayer player) {
        return ClientTickHandler.isGravitationalModulationOn(player);
    }
}
