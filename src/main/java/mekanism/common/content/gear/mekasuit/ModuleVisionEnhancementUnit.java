package mekanism.common.content.gear.mekasuit;

import mekanism.api.gear.IHUDElement;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.Modules;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ModuleVisionEnhancementUnit extends Module {

    private static final ResourceLocation icon = MekanismUtils.getResource(ResourceType.GUI_HUD, "vision_enhancement_unit.png");

    @Override
    public void tickServer(EntityPlayer player) {
        super.tickServer(player);
        useEnergy(player, MekanismConfig.current().meka.mekaSuitEnergyUsageVisionEnhancement.val());
    }

    @Override
    public void addHUDElements(EntityPlayer player, List<IHUDElement> hudElementAdder) {
        hudElementAdder.add(Modules.hudElementEnabled(icon, isEnabled()));
    }

    @Override
    public boolean canChangeModeWhenDisabled() {
        return true;
    }

    @Override
    public void changeMode(EntityPlayer player, ItemStack stack, int shift, boolean displayChangeMessage) {
        toggleEnabled(player, MekanismLang.MODULE_VISION_ENHANCEMENT.translate());
    }
}