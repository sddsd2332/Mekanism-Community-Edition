package mekanism.common.content.gear.mekasuit;

import mekanism.api.gear.IHUDElement;
import mekanism.client.MekKeyHandler;
import mekanism.client.MekanismKeyHandler;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.ModuleConfigItem.EnumData;
import mekanism.common.content.gear.Modules;
import mekanism.common.content.gear.mekasuit.ModuleLocomotiveBoostingUnit.SprintBoost;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ModuleGravitationalModulatingUnit extends Module {

    private static final ResourceLocation icon = MekanismUtils.getResource(ResourceType.GUI_HUD, "gravitational_modulation_unit.png");

    // we share with locomotive boosting unit
    private ModuleConfigItem<SprintBoost> speedBoost;

    @Override
    public void init() {
        speedBoost = addConfigItem(new ModuleConfigItem<>(this, "speed_boost", MekanismLang.MODULE_SPEED_BOOST, new EnumData<>(SprintBoost.class), SprintBoost.LOW));
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
        toggleEnabled(player, MekanismLang.MODULE_GRAVITATIONAL_MODULATION.translate());
    }

    public float getBoost() {
        return speedBoost.get().getBoost();
    }

    @Override
    public void tickClient(EntityPlayer player) {
        super.tickClient(player);
        //Client side handling of boost as movement needs to be applied on both the server and the client
        if (player.capabilities.isFlying && MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.MekAsuitFeetModeSwitchKey) && canUseEnergy(player, MekanismConfig.current().meka.mekaSuitEnergyUsageGravitationalModulation.val() * 4, false)) {
            float boost = getBoost();
            if (boost > 0) {
                player.moveRelative(boost, 0, 0, 1);
            }
        }
    }
}