package mekanism.common.content.gear.mekasuit;

import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IHUDElement;
import mekanism.api.gear.IModule;
import mekanism.api.gear.IModuleHelper;
import mekanism.api.gear.config.IModuleConfigItem;
import mekanism.api.gear.config.ModuleConfigItemCreator;
import mekanism.api.gear.config.ModuleEnumData;
import mekanism.client.MekKeyHandler;
import mekanism.client.MekanismKeyHandler;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.content.gear.mekasuit.ModuleLocomotiveBoostingUnit.SprintBoost;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

@ParametersAreNotNullByDefault
public class ModuleGravitationalModulatingUnit implements ICustomModule<ModuleGravitationalModulatingUnit> {

    private static final ResourceLocation icon = MekanismUtils.getResource(ResourceType.GUI_HUD, "gravitational_modulation_unit.png");

    // we share with locomotive boosting unit
    private IModuleConfigItem<SprintBoost> speedBoost;

    @Override
    public void init(IModule<ModuleGravitationalModulatingUnit> module, ModuleConfigItemCreator configItemCreator) {
        speedBoost = configItemCreator.createConfigItem("speed_boost", MekanismLang.MODULE_SPEED_BOOST, new ModuleEnumData<>(SprintBoost.LOW));
    }

    @Override
    public void addHUDElements(IModule<ModuleGravitationalModulatingUnit> module, EntityPlayer player, Consumer<IHUDElement> hudElementAdder) {
        hudElementAdder.accept(ModuleHelper.get().hudElementEnabled(icon, module.isEnabled()));
    }

    @Override
    public boolean canChangeModeWhenDisabled(IModule<ModuleGravitationalModulatingUnit> module) {
        return true;
    }

    @Override
    public void changeMode(IModule<ModuleGravitationalModulatingUnit> module, EntityPlayer player, ItemStack stack, int shift, boolean displayChangeMessage) {
        module.toggleEnabled(player, MekanismLang.MODULE_GRAVITATIONAL_MODULATION.translate());
    }

    public float getBoost() {
        return speedBoost.get().getBoost();
    }

    @Override
    public void tickClient(IModule<ModuleGravitationalModulatingUnit> module, EntityPlayer player) {
        //Client side handling of boost as movement needs to be applied on both the server and the client
        if (player.capabilities.isFlying && MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.boostKey) &&
                module.canUseEnergy(player, MekanismConfig.current().meka.mekaSuitEnergyUsageGravitationalModulation.val() * (4), false)) {
            float boost = getBoost();
            if (boost > 0) {
                player.moveRelative(boost, 0, 0, 1);
            }
        }
    }
}