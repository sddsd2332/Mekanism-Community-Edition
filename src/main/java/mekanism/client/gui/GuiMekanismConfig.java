package mekanism.client.gui;

import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;
import mekanism.common.Mekanism;
import mekanism.common.util.LangUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class GuiMekanismConfig extends GuiConfig
{
	public GuiMekanismConfig(GuiScreen parent)
	{
		super(parent, getConfigElements(), "Mekanism", false, false, "Mekanism");
	}

	private static List<IConfigElement> getConfigElements()
	{
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		list.add(new DummyCategoryElement(LangUtils.localize("mekanism.configgui.ctgy.general"), "mekanism.configgui.ctgy.general", GeneralEntry.class));
		list.add(new DummyCategoryElement(LangUtils.localize("mekanism.configgui.ctgy.machines"), "mekanism.configgui.ctgy.machines", MachinesEntry.class));
		list.add(new DummyCategoryElement(LangUtils.localize("mekanism.configgui.ctgy.tier"), "mekanism.configgui.ctgy.tier", TierEntry.class));
		list.add(new DummyCategoryElement(LangUtils.localize("mekanism.configgui.ctgy.usage"), "mekanism.configgui.ctgy.usage", UsageEntry.class));
		list.add(new DummyCategoryElement(LangUtils.localize("mekanism.configgui.ctgy.client"), "mekanism.configgui.ctgy.client", ClientEntry.class));
		list.add(new DummyCategoryElement(LangUtils.localize("mekanism.configgui.ctgy.mekce"), "mekanism.configgui.ctgy.mekce", MEKCEEntry.class));
		list.add(new DummyCategoryElement(LangUtils.localize("mekanism.configgui.ctgy.recipe"), "mekanism.configgui.ctgy.recipe", RecipeEntry.class));
		list.add(new DummyCategoryElement(LangUtils.localize("mekanism.configgui.ctgy.mekce_client"), "mekanism.configgui.ctgy.mekce_client", MEKCEClientEntry.class));
		return list;
	}

	public static class GeneralEntry extends CategoryEntry
	{
		public GeneralEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
		{
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen()
		{
			return new GuiConfig(owningScreen,
					new ConfigElement(Mekanism.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
					owningScreen.modID, Configuration.CATEGORY_GENERAL, false, false,
					GuiConfig.getAbridgedConfigPath(Mekanism.configuration.toString()));
		}
	}

	public static class MEKCEEntry extends CategoryEntry
	{
		public MEKCEEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
		{
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen()
		{
			return new GuiConfig(owningScreen,
					new ConfigElement(Mekanism.configurationce.getCategory("mekce")).getChildElements(),
					owningScreen.modID, Configuration.CATEGORY_GENERAL, false, false,
					GuiConfig.getAbridgedConfigPath(Mekanism.configurationce.toString()));
		}
	}
	
	public static class MachinesEntry extends CategoryEntry
	{
		public MachinesEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
		{
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen()
		{
			return new GuiConfig(owningScreen,
					new ConfigElement(Mekanism.configuration.getCategory("machines")).getChildElements(),
					owningScreen.modID, Configuration.CATEGORY_GENERAL, false, false,
					GuiConfig.getAbridgedConfigPath(Mekanism.configuration.toString()));
		}
	}
	
	public static class TierEntry extends CategoryEntry
	{
		public TierEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
		{
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen()
		{
			return new GuiConfig(owningScreen,
					new ConfigElement(Mekanism.configuration.getCategory("tier")).getChildElements(),
					owningScreen.modID, Configuration.CATEGORY_GENERAL, false, false,
					GuiConfig.getAbridgedConfigPath(Mekanism.configuration.toString()));
		}
	}

	public static class UsageEntry extends CategoryEntry
	{
		public UsageEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
		{
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen()
		{
			return new GuiConfig(owningScreen,
					new ConfigElement(Mekanism.configuration.getCategory("usage")).getChildElements(),
					owningScreen.modID, Configuration.CATEGORY_GENERAL, false, false,
					GuiConfig.getAbridgedConfigPath(Mekanism.configuration.toString()));
		}
	}

	public static class ClientEntry extends CategoryEntry
	{
		public ClientEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
		{
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen()
		{
			return new GuiConfig(owningScreen,
					new ConfigElement(Mekanism.configuration.getCategory("client")).getChildElements(),
					owningScreen.modID, "client", false, false,
					GuiConfig.getAbridgedConfigPath(Mekanism.configuration.toString()));
		}
	}

	public static class MEKCEClientEntry extends CategoryEntry
	{
		public MEKCEClientEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
		{
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen()
		{
			return new GuiConfig(owningScreen,
					new ConfigElement(Mekanism.configurationce.getCategory("mekce_client")).getChildElements(),
					owningScreen.modID, "mekce_client", false, false,
					GuiConfig.getAbridgedConfigPath(Mekanism.configurationce.toString()));
		}
	}
	public static class RecipeEntry extends CategoryEntry
	{
		public RecipeEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
		{
			super(owningScreen, owningEntryList, prop);
		}

		@Override
		protected GuiScreen buildChildScreen()
		{
			return new GuiConfig(owningScreen,
					new ConfigElement(Mekanism.configurationrecipes.getCategory("Recipes")).getChildElements(),
					owningScreen.modID, "Recipes", false, false,
					GuiConfig.getAbridgedConfigPath(Mekanism.configurationrecipes.toString()));
		}
	}
}
