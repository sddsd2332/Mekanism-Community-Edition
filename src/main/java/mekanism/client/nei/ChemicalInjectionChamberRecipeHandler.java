package mekanism.client.nei;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasifyableItems;
import mekanism.client.gui.GuiChemicalInjectionChamber;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.common.Tier.GasTankTier;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.machines.InjectionRecipe;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChemicalInjectionChamberRecipeHandler extends AdvancedMachineRecipeHandler
{
	@Override
	public String getRecipeName()
	{
		return LangUtils.localize("nei.chemicalInjectionChamber");
	}

	@Override
	public String getRecipeId()
	{
		return "mekanism.chemicalinjectionchamber";
	}

	@Override
	public String getOverlayIdentifier()
	{
		return "chemicalinjectionchamber";
	}

	@Override
	public Collection<InjectionRecipe> getRecipes()
	{
		return Recipe.CHEMICAL_INJECTION_CHAMBER.get().values();
	}

	@Override
	public List<ItemStack> getFuelStacks(Gas gasType)
	{
		if(GasifyableItems.isGasValidGasifyable(gasType)) {

			String oredict = GasifyableItems.getItemFromGas(gasType);
			List<ItemStack> fuels = new ArrayList<>();

			if (oredict != null && !OreDictionary.getOres(oredict).isEmpty())
				fuels.addAll(OreDictionary.getOres(oredict));

			if (gasType.isVisible()) {

				for (GasTankTier tier : GasTankTier.values())
					fuels.add(MekanismUtils.getFullGasTank(tier, gasType));
			}

			return fuels;
		}

		return new ArrayList<>();
	}
	
	@Override
	public ProgressBar getProgressType()
	{
		return ProgressBar.YELLOW;
	}

	@Override
	public Class getGuiClass()
	{
		return GuiChemicalInjectionChamber.class;
	}
}
