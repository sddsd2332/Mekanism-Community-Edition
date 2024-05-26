package mekanism.multiblockmachine.client.gui;

import mekanism.common.Mekanism;
import mekanism.common.util.LangUtils;
import mekanism.multiblockmachine.common.MekanismMultiblockMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiMultiblockMachineConfig extends GuiConfig {

    public GuiMultiblockMachineConfig(GuiScreen parent) {
        super(parent, getConfigElements(), MekanismMultiblockMachine.MODID, false, false, "MekanismMultiblockMachine");
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();
        list.add(new DummyConfigElement.DummyCategoryElement(LangUtils.localize("mekanism.configgui.ctgy.multiblock.multiblock"), "mekanism.configgui.ctgy.multiblock.multiblock", MultiblockMachineGenerationEntry.class));
        list.add(new DummyConfigElement.DummyCategoryElement(LangUtils.localize("mekanism.configgui.ctgy.multiblock.multiblockmachinegenerators"), "mekanism.configgui.ctgy.multiblock.multiblockmachinegenerators", MultiblockMachineGeneratorsEntry.class));
        return list;
    }

    public static class MultiblockMachineGeneratorsEntry extends CategoryEntry {

        public MultiblockMachineGeneratorsEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
            super(owningScreen, owningEntryList, prop);
        }

        @Override
        protected GuiScreen buildChildScreen() {
            return new GuiConfig(owningScreen, new ConfigElement(Mekanism.configurationMultiblockMachine.getCategory("multiblockmachinegenerators")).getChildElements(), owningScreen.modID,
                    Configuration.CATEGORY_GENERAL, false, false, GuiConfig.getAbridgedConfigPath(Mekanism.configurationMultiblockMachine.toString()));
        }
    }

    public static class MultiblockMachineGenerationEntry extends CategoryEntry {

        public MultiblockMachineGenerationEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
            super(owningScreen, owningEntryList, prop);
        }

        @Override
        protected GuiScreen buildChildScreen() {
            return new GuiConfig(owningScreen, new ConfigElement(Mekanism.configurationMultiblockMachine.getCategory("multiblock")).getChildElements(), owningScreen.modID,
                    Configuration.CATEGORY_GENERAL, false, false, GuiConfig.getAbridgedConfigPath(Mekanism.configurationMultiblockMachine.toString()));
        }
    }
}
