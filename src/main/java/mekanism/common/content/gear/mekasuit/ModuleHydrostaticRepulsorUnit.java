package mekanism.common.content.gear.mekasuit;

import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.gear.EnchantmentBasedModule;
import mekanism.api.gear.IModule;
import mekanism.api.gear.config.IModuleConfigItem;
import mekanism.api.gear.config.ModuleConfigItemCreator;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import org.jetbrains.annotations.NotNull;

@ParametersAreNotNullByDefault
public class ModuleHydrostaticRepulsorUnit extends EnchantmentBasedModule<ModuleHydrostaticRepulsorUnit> {

    public static final int BOOST_STACKS = 3;

    private IModuleConfigItem<Boolean> swimBoost;

    @Override
    public void init(IModule<ModuleHydrostaticRepulsorUnit> module, ModuleConfigItemCreator configItemCreator) {
        swimBoost = configItemCreator.createDisableableConfigItem("swim_boost", MekanismLang.MODULE_SWIM_BOOST, true, () -> module.getInstalledCount() >= BOOST_STACKS);
    }

    @NotNull
    @Override
    public Enchantment getEnchantment() {
        return Enchantments.DEPTH_STRIDER;
    }

    @Override
    public void tickServer(IModule<ModuleHydrostaticRepulsorUnit> module, EntityPlayer player) {
        if (isSwimBoost(module, player)) {
            module.useEnergy(player, MekanismConfig.current().meka.mekaSuitEnergyUsageHydrostaticRepulsion.val());
        }
    }

    public boolean isSwimBoost(IModule<ModuleHydrostaticRepulsorUnit> module, EntityPlayer player) {
        return swimBoost.get() && module.getInstalledCount() >= BOOST_STACKS && !player.isInWater() &&
                module.hasEnoughEnergy(MekanismConfig.current().meka.mekaSuitEnergyUsageHydrostaticRepulsion.val());
    }
}