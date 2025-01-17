package mekanism.client.sound;

import mekanism.client.ClientTickHandler;
import mekanism.common.Mekanism;
import mekanism.common.item.interfaces.IJetpackItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class JetpackSound extends PlayerSound {

    private static final ResourceLocation SOUND = new ResourceLocation(Mekanism.MODID, "item.jetpack");

    public JetpackSound(@Nonnull EntityPlayer player) {
        super(player, SOUND);
    }

    @Override
    public boolean shouldPlaySound(@Nonnull EntityPlayer player) {
        return Mekanism.playerState.isJetpackOn(player);
    }
}
