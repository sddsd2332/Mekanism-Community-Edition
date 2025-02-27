package mekanism.common.integration;

import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.api.IProbeConfig.ConfigMode;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasTankInfo;
import mekanism.api.gas.IGasHandler;
import mekanism.api.text.TextComponentGroup;
import mekanism.common.Mekanism;
import mekanism.common.block.BlockBounding;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.lookingat.LookingAtHelper;
import mekanism.common.integration.lookingat.LookingAtUtils;
import mekanism.common.integration.lookingat.theoneprobe.ProbeConfigProvider;
import mekanism.common.integration.lookingat.theoneprobe.TOPEnergyElement;
import mekanism.common.util.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.function.Function;

@SuppressWarnings("unused")//IMC bound
public class TOPProvider implements Function<ITheOneProbe, Void>, IProbeInfoProvider {

    private ConfigMode tankMode = ConfigMode.EXTENDED;
    public static int ENERGY_ELEMENT_ID;

    @Override
    public Void apply(ITheOneProbe probe) {
        probe.registerProvider(this);
        probe.registerProbeConfigProvider(ProbeConfigProvider.INSTANCE);
        ENERGY_ELEMENT_ID = probe.registerElementFactory(TOPEnergyElement::new);

        //Grab the default view settings
        IProbeConfig probeConfig = probe.createProbeConfig();
        tankMode = probeConfig.getShowTankSetting();
        return null;
    }

    @Override
    public String getID() {
        return Mekanism.MODID;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        BlockPos pos = data.getPos();
        if (blockState.getBlock() instanceof BlockBounding) {
            //If we are a bounding block that has a position set, redirect the probe to the main location
            BlockPos mainPos = BlockBounding.getMainBlockPos(world, pos);
            if (mainPos != null) {
                pos = mainPos;
                //If we end up needing the blockstate at some point lower down, then uncomment this line
                // until we do though there is no point in bothering to query the world to get it
                //blockState = world.getBlockState(mainPos);
            }
        }
        TileEntity energyTile = WorldUtils.getTileEntity(world, pos);
        if (energyTile != null) {
            LookingAtUtils.addInfo(new TOPLookingAtHelper(info), energyTile);
        }

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
            IProgressStyle style = info.defaultProgressStyle().suffix("mB");
            if (tank.getGas() != null) {
                Gas gas = tank.getGas().getGas();
                info.text(TextStyleClass.NAME + I18n.translateToLocal("gui.gas:") + gas.getLocalizedName());
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
            info.progress(tank.getStored(), tank.getMaxGas(), style);
        }
    }

    // Utility methods to darken and lighten colors

    private static int darkenColor(int color, double factor) {
        int a = (color >> 24) & 0xFF;
        int r = (int) (((color >> 16) & 0xFF) * factor);
        int g = (int) (((color >> 8) & 0xFF) * factor);
        int b = (int) ((color & 0xFF) * factor);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int lightenColor(int color, double factor) {
        int a = (color >> 24) & 0xFF;
        int r = Math.min(255, (int) (((color >> 16) & 0xFF) / factor));
        int g = Math.min(255, (int) (((color >> 8) & 0xFF) / factor));
        int b = Math.min(255, (int) ((color & 0xFF) / factor));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }


    private static class TOPLookingAtHelper implements LookingAtHelper {

        private final IProbeInfo info;

        public TOPLookingAtHelper(IProbeInfo info) {
            this.info = info;
        }

        @Override
        public void addEnergyElement(double energy, double maxEnergy) {
            info.element(new TOPEnergyElement(energy, maxEnergy));
        }

    }


}
