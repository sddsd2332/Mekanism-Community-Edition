package mekanism.weapons.client.gui;

import mekanism.weapons.common.MekanismWeapons;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class GuiWeaponsConfig  extends GuiConfig {

    public GuiWeaponsConfig(GuiScreen parent) {
        super(parent, getConfigElements(), MekanismWeapons.MODID,false, false,"MekanismWeapons");
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();
        return list;
    }
}
