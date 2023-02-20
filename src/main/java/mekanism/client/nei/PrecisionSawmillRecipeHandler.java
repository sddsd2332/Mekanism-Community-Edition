package mekanism.client.nei;

import mekanism.client.gui.GuiPrecisionSawmill;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.machines.SawmillRecipe;
import mekanism.common.util.LangUtils;

import java.util.Collection;

public class PrecisionSawmillRecipeHandler extends ChanceMachineRecipeHandler
{	
	@Override
	public String getRecipeName()
	{
		return LangUtils.localize("tile.MachineBlock2.PrecisionSawmill.name");
	}

	@Override
	public String getRecipeId()
	{
		return "mekanism.precisionsawmill";
	}

	@Override
	public String getOverlayIdentifier()
	{
		return "precisionsawmill";
	}

	@Override
	public Collection<SawmillRecipe> getRecipes()
	{
		return Recipe.PRECISION_SAWMILL.get().values();
	}
	
	@Override
	public ProgressBar getProgressType()
	{
		return ProgressBar.PURPLE;
	}

	@Override
	public Class getGuiClass()
	{
		return GuiPrecisionSawmill.class;
	}
}
