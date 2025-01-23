package mekanism.common.content.gear.shared;

import mekanism.api.energy.IEnergizedItem;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.Module;
import mekanism.common.item.armor.ItemMekaSuitArmor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModuleEnergyUnit extends Module {

    public double getEnergyCapacity() {
        double base = getContainer().getItem() instanceof ItemMekaSuitArmor ? MekanismConfig.current().meka.mekaSuitBaseEnergyCapacity.val()
                : MekanismConfig.current().meka.mekaToolBaseEnergyCapacity.val();
        return base * (Math.pow(2, getInstalledCount()));
    }

    public double getChargeRate() {
        double base = getContainer().getItem() instanceof ItemMekaSuitArmor ? MekanismConfig.current().meka.mekaSuitBaseChargeRate.val()
                : MekanismConfig.current().meka.mekaToolBaseChargeRate.val();
        return base * (Math.pow(2, getInstalledCount()));
    }

    @Override
    public void onRemoved(boolean last) {
        IEnergizedItem energyContainer = getEnergyContainer();
        if (energyContainer != null) {
            energyContainer.setEnergy(getContainer(), Math.min(energyContainer.getEnergy(getContainer()), energyContainer.getMaxEnergy(getContainer())));
        }
    }
}