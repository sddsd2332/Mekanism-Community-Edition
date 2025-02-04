package mekanism.common.content.gear.mekasuit;

import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IHUDElement;
import mekanism.api.gear.IModule;
import mekanism.common.Mekanism;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.util.MekanismUtils;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.radiation.RadiationHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class ModuleGeigerUnit implements ICustomModule<ModuleGeigerUnit> {

    private static final ResourceLocation icon = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_HUD, "geiger_counter.png");

    @Override
    public void addHUDElements(IModule<ModuleGeigerUnit> module, EntityPlayer player, Consumer<IHUDElement> hudElementAdder) {
        if (module.isEnabled()) {
            double radiation = getRadsPercentage(player);
            hudElementAdder.accept(ModuleHelper.get().hudElement(icon, getChunkRad(player), radiation < 30 ? IHUDElement.HUDColor.REGULAR : (radiation < 60 ? IHUDElement.HUDColor.WARNING : IHUDElement.HUDColor.DANGER)));
        }
    }

    private String getChunkRad(EntityPlayer player) {
        if (Mekanism.hooks.NuclearCraft) {
            return getChunkNCRad(player);
        }
        return "";
    }

    @Optional.Method(modid = MekanismHooks.NuclearCraft_MOD_ID)
    private String getChunkNCRad(EntityPlayer player) {
        Chunk chunk = player.getEntityWorld().getChunk(new BlockPos(player));
        if (chunk != null && chunk.isLoaded()) {
            IRadiationSource source = chunk.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
            if (source != null) {
                return RadiationHelper.radsPrefix(source.getRadiationLevel(), false);
            }
        }
        return "0 Rad";
    }

    private double getRadsPercentage(EntityPlayer player) {
        if (Mekanism.hooks.NuclearCraft) {
            return getNCRadsPercentage(player);
        }
        return 0;
    }

    @Optional.Method(modid = MekanismHooks.NuclearCraft_MOD_ID)
    private double getNCRadsPercentage(EntityPlayer player) {
        Chunk chunk = player.getEntityWorld().getChunk(new BlockPos(player));
        if (chunk != null && chunk.isLoaded()) {
            IRadiationSource source = chunk.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
            if (source != null) {
                double getConfig = NCConfig.radiation_chunk_limit >= 0D ? NCConfig.radiation_chunk_limit : Double.MAX_VALUE;
                return Math.min(100.0, 100.0 * source.getRadiationLevel() / getConfig);
            }
        }
        return 0;
    }

}
