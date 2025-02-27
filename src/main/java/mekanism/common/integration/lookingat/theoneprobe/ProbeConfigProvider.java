package mekanism.common.integration.lookingat.theoneprobe;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeConfigProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.util.CapabilityUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ProbeConfigProvider implements IProbeConfigProvider {

    public static final ProbeConfigProvider INSTANCE = new ProbeConfigProvider();

    @Override
    public void getProbeConfig(IProbeConfig iProbeConfig, EntityPlayer entityPlayer, World world, Entity entity, IProbeHitEntityData iProbeHitEntityData) {

    }

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer entityPlayer, World world, IBlockState iBlockState, IProbeHitData data) {
        TileEntity tile = WorldUtils.getTileEntity(world, data.getPos());
        if (CapabilityUtils.hasCapability(tile, Capabilities.ENERGY_STORAGE_CAPABILITY, null)) {
            config.setRFMode(0);
        }
    }
}
