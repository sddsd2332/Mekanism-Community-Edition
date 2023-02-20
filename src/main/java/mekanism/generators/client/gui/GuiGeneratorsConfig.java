package mekanism.generators.client.gui;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import mekanism.client.gui.GuiMekanismConfig;
import mekanism.common.Mekanism;
import mekanism.common.util.LangUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 27/06/14.
 */
public class GuiGeneratorsConfig extends GuiConfig
{
	public GuiGeneratorsConfig(GuiScreen parent)
	{
		super(parent, getConfigElements(), "MekanismGenerators", false, false, "MekanismGenerators");
	}

	private static List<IConfigElement> getConfigElements()
	{
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		list.add(new DummyConfigElement.DummyCategoryElement(LangUtils.localize("mekanism.configgui.ctgy.gengeneral"), "mekanism.configgui.ctgy.gengeneral", GuiGeneratorsConfig.GENGeneralEntry.class));
		list.add(new DummyConfigElement.DummyCategoryElement(LangUtils.localize("mekanism.configgui.ctgy.mekcegen"), "mekanism.configgui.ctgy.mekcegen", GuiGeneratorsConfig.MEKCEGENEntry.class));
		return list;
	}
	public static class GENGeneralEntry extends GuiConfigEntries.CategoryEntry
	{
		public GENGeneralEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
		{
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen()
		{
			return new GuiConfig(owningScreen,
					new ConfigElement(Mekanism.configurationgenerators.getCategory("generation")).getChildElements(),
					owningScreen.modID, Configuration.CATEGORY_GENERAL, false, false,
					GuiConfig.getAbridgedConfigPath(Mekanism.configurationgenerators.toString()));
		}
	}

	public static class MEKCEGENEntry extends GuiConfigEntries.CategoryEntry
	{
		public MEKCEGENEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
		{
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen()
		{
			return new GuiConfig(owningScreen,
					new ConfigElement(Mekanism.configurationce.getCategory("mekce_generation")).getChildElements(),
					owningScreen.modID, Configuration.CATEGORY_GENERAL, false, false,
					GuiConfig.getAbridgedConfigPath(Mekanism.configurationce.toString()));
		}
	}
}
