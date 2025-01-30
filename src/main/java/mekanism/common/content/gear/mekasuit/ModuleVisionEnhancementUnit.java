package mekanism.common.content.gear.mekasuit;

import java.util.function.Consumer;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IHUDElement;
import mekanism.api.gear.IModule;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@ParametersAreNotNullByDefault
public class ModuleVisionEnhancementUnit implements ICustomModule<ModuleVisionEnhancementUnit> {

    private static final ResourceLocation icon = MekanismUtils.getResource(ResourceType.GUI_HUD, "vision_enhancement_unit.png");

    @Override
    public void tickServer(IModule<ModuleVisionEnhancementUnit> module, EntityPlayer player) {
        module.useEnergy(player, MekanismConfig.current().meka.mekaSuitEnergyUsageVisionEnhancement.val());
    }

    @Override
    public void addHUDElements(IModule<ModuleVisionEnhancementUnit> module, EntityPlayer player, Consumer<IHUDElement> hudElementAdder) {
        hudElementAdder.accept(ModuleHelper.get().hudElementEnabled(icon, module.isEnabled()));
    }

    @Override
    public boolean canChangeModeWhenDisabled(IModule<ModuleVisionEnhancementUnit> module) {
        return true;
    }

    @Override
    public void changeMode(IModule<ModuleVisionEnhancementUnit> module, EntityPlayer player, ItemStack stack, int shift, boolean displayChangeMessage) {
        module.toggleEnabled(player, MekanismLang.MODULE_VISION_ENHANCEMENT.translate());
    }
}