package mekanism.common.integration;

import mcjty.theoneprobe.api.*;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasTankInfo;
import mekanism.api.gas.IGasHandler;
import mekanism.common.Mekanism;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.function.Function;

@SuppressWarnings("unused")//IMC bound
public class TOPProvider implements Function<ITheOneProbe, Void>, IProbeInfoProvider {

    @Override
    public Void apply(ITheOneProbe iTheOneProbe) {
        iTheOneProbe.registerProvider(this);
        return null;
    }

    @Override
    public String getID() {
        return Mekanism.MODID;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (!MekanismConfig.current().client.GasTOP.val()) {
            if (mode != ProbeMode.EXTENDED) {
                return;
            }
        }

        final TileEntity tile = world.getTileEntity(data.getPos());

        if (tile == null || !tile.hasCapability(Capabilities.GAS_HANDLER_CAPABILITY, null)) {
            return;
        }
        IGasHandler handler = tile.getCapability(Capabilities.GAS_HANDLER_CAPABILITY, null);
        if (handler == null) {
            return;
        }
        GasTankInfo[] tanks = handler.getTankInfo();
        for (GasTankInfo tank : tanks) {
            IProgressStyle style = probeInfo.defaultProgressStyle().suffix("mB");
            if (tank.getGas() != null) {
                Gas gas = tank.getGas().getGas();
                probeInfo.text(TextStyleClass.NAME + I18n.translateToLocal("gui.gas:") + gas.getLocalizedName());
                int tint = gas.getTint();
                // TOP respects transparency so we need to filter out the transparent layer
                // if the gas has one. (Currently they are all fully transparent)
                if ((tint & 0xFF000000) == 0) {
                    tint = 0xFF000000 | tint;
                }
                if (tint != 0xFFFFFFFF) {
                    // TOP bugs out with full white background so just use default instead
                    // The default is a slightly off white color so is better for readability

                    // Calculate darker and lighter colors
                    int darkerTint = darkenColor(tint, 0.8); // Make the color 20% darker
                    int lighterTint = lightenColor(tint, 0.8); // Make the color 20% lighter

                    style = style.filledColor(tint)
                            .alternateFilledColor(lighterTint)
                            .borderColor(darkerTint)
                            .numberFormat(NumberFormat.COMPACT);
                }
            }
            probeInfo.progress(tank.getStored(), tank.getMaxGas(), style);
        }
    }

    // Utility methods to darken and lighten colors

    private static int darkenColor(int color, double factor) {
        int a = (color >> 24) & 0xFF;
        int r = (int)(((color >> 16) & 0xFF) * factor);
        int g = (int)(((color >> 8) & 0xFF) * factor);
        int b = (int)((color & 0xFF) * factor);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int lightenColor(int color, double factor) {
        int a = (color >> 24) & 0xFF;
        int r = Math.min(255, (int)(((color >> 16) & 0xFF) / factor));
        int g = Math.min(255, (int)(((color >> 8) & 0xFF) / factor));
        int b = Math.min(255, (int)((color & 0xFF) / factor));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
