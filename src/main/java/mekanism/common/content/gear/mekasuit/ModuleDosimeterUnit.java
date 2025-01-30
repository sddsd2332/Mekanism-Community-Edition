package mekanism.common.content.gear.mekasuit;

import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IHUDElement;
import mekanism.api.gear.IHUDElement.HUDColor;
import mekanism.api.gear.IModule;
import mekanism.common.Mekanism;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.util.MekanismUtils;
import nc.capability.radiation.entity.IEntityRads;
import nc.radiation.RadiationHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class ModuleDosimeterUnit implements ICustomModule<ModuleDosimeterUnit> {

    private static final ResourceLocation icon = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_HUD, "dosimeter.png");

    @Override
    public void addHUDElements(IModule<ModuleDosimeterUnit> module, EntityPlayer player, Consumer<IHUDElement> hudElementAdder) {
        if (module.isEnabled()) {
            double radiation = getRadsPercentage(player);
            hudElementAdder.accept(ModuleHelper.get().hudElement(icon, getPlayerRad(player), radiation < 30 ? HUDColor.REGULAR : (radiation < 60 ? HUDColor.WARNING : HUDColor.DANGER)));
        }
    }


    private String getPlayerRad(EntityPlayer player) {
        if (Mekanism.hooks.NuclearCraft) {
            return getPlayerNCRad(player);
        }
        return "";
    }


    @Optional.Method(modid = MekanismHooks.NuclearCraft_MOD_ID)
    private String getPlayerNCRad(EntityPlayer player) {
        IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
        if (playerRads != null) {
            return (playerRads.isTotalRadsNegligible() ? "0 Rad" : RadiationHelper.radsPrefix(playerRads.getTotalRads(), false));
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
        IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
        if (playerRads != null) {
            return playerRads.getRadsPercentage();
        }
        return 0;
    }
}
