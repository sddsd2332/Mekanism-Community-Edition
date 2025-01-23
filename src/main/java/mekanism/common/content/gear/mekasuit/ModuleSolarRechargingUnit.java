package mekanism.common.content.gear.mekasuit;

import mekanism.api.energy.IEnergizedItem;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.Module;
import mekanism.common.util.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModuleSolarRechargingUnit extends Module {

    private static final double RAIN_MULTIPLIER = 0.2;

    @Override
    public void tickServer(EntityPlayer player) {
        super.tickServer(player);
        IEnergizedItem energyContainer = getEnergyContainer();
        if (energyContainer != null && energyContainer.getNeeded(getContainer()) != 0) {
            //Use the position that is roughly where the solar panel is
            BlockPos pos = new BlockPos(player.posX, (player.posY + player.getEyeHeight()) + 0.2, player.posZ);
            //Based on how TileEntitySolarGenerator and the rest of our solar things do energy calculations
            if (WorldUtils.canSeeSun(player.getEntityWorld(), pos)) {
                Biome b = player.getEntityWorld().provider.getBiomeForCoords(pos);
                boolean needsRainCheck = b.canRain();
                // Consider the best temperature to be 0.8; biomes that are higher than that
                // will suffer an efficiency loss (semiconductors don't like heat); biomes that are cooler
                // get a boost. We scale the efficiency to around 30% so that it doesn't totally dominate
                float tempEff = 0.3F * (0.8F - b.getTemperature(pos));

                // Treat rainfall as a proxy for humidity; any humidity works as a drag on overall efficiency.
                // As with temperature, we scale it so that it doesn't overwhelm production. Note the signedness
                // on the scaling factor. Also note that we only use rainfall as a proxy if it CAN rain; some dimensions
                // (like the End) have rainfall set, but can't actually support rain.
                float humidityEff = needsRainCheck ? -0.3F * b.getRainfall() : 0.0F;
                double peakOutput = MekanismConfig.current().meka.mekaSuitSolarRechargingRate.val() * (1.0F + tempEff + humidityEff);

                //Get the brightness of the sun; note that there are some implementations that depend on the base
                // brightness function which doesn't take into account the fact that rain can't occur in some biomes.
                float brightness = WorldUtils.getSunBrightness(player.world, 1.0F);

                //Production is a function of the peak possible output in this biome and sun's current brightness
                double production = peakOutput * brightness;
                //If the generator is in a biome where it can rain, and it's raining penalize production by 80%
                if (needsRainCheck && (player.world.isRaining() || player.world.isThundering())) {
                    production = production * (RAIN_MULTIPLIER);
                }
                //Multiply actual production based on how many modules are installed
                energyContainer.insert(getContainer(), production * getInstalledCount(), true);
            }
        }
    }
}