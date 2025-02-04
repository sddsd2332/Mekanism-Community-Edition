package mekanism.common.content.gear.mekasuit;

import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.common.config.MekanismConfig;
import net.minecraft.entity.player.EntityPlayer;


@ParametersAreNotNullByDefault
public class ModuleHealthRegenerationUnit implements ICustomModule<ModuleHealthRegenerationUnit> {


    @Override
    public void tickServer(IModule<ModuleHealthRegenerationUnit> module, EntityPlayer player) {
        if (player.getHealth() < player.getMaxHealth() && module.hasEnoughEnergy(MekanismConfig.current().meka.mekaEnergyUsageHealthRegeneration.val())) {
            player.heal(module.getInstalledCount());
            module.useEnergy(player, MekanismConfig.current().meka.mekaEnergyUsageHealthRegeneration.val());
        }
    }

}