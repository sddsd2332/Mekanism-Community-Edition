package mekanism.weapons.common;

import mekanism.common.Mekanism;
import mekanism.common.config.MekanismConfig;

public class MekanismWeaponsCommonProxy {

    public void loadConfiguration() {
        MekanismConfig.current().weapons.load(Mekanism.configurationmekaweapons);
        if (Mekanism.configurationmekaweapons.hasChanged()) {
            Mekanism.configurationmekaweapons.save();
        }
    }

    public void registerItemRenders() {
    }

    public void preInit() {

    }

}
