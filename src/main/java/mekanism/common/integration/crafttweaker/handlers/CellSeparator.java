package mekanism.common.integration.crafttweaker.handlers;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import java.util.ArrayList;
import java.util.List;
import mekanism.common.Mekanism;
import mekanism.common.integration.crafttweaker.CrafttweakerIntegration;
import mekanism.common.integration.crafttweaker.helpers.IngredientHelper;
import mekanism.common.integration.crafttweaker.util.AddMekanismRecipe;
import mekanism.common.integration.crafttweaker.util.IngredientWrapper;
import mekanism.common.integration.crafttweaker.util.RemoveAllMekanismRecipe;
import mekanism.common.integration.crafttweaker.util.RemoveMekanismRecipe;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.machines.CellSeparatorRecipe;
import mekanism.common.recipe.outputs.ChanceOutput;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.cellseparator")
@ZenRegister
public class CellSeparator {

    public static final String NAME = Mekanism.MOD_NAME + " CellSeparator";

    @ZenMethod
    public static void addRecipe(IIngredient ingredientInput, IItemStack itemOutput, @Optional IItemStack optionalItemOutput, @Optional double optionalChance) {
        if (IngredientHelper.checkNotNull(NAME, ingredientInput, itemOutput)) {
            ChanceOutput output = optionalItemOutput == null ? new ChanceOutput(CraftTweakerMC.getItemStack(itemOutput)) : new ChanceOutput(CraftTweakerMC.getItemStack(itemOutput),
                    CraftTweakerMC.getItemStack(optionalItemOutput), optionalChance);
            List<CellSeparatorRecipe> recipes = new ArrayList<>();
            for (ItemStack stack : CraftTweakerMC.getIngredient(ingredientInput).getMatchingStacks()) {
                recipes.add(new CellSeparatorRecipe(new ItemStackInput(stack), output));
            }
            CrafttweakerIntegration.LATE_ADDITIONS.add(new AddMekanismRecipe<>(NAME, Recipe.CELL_SEPARATOR, recipes));
        }
    }

    @ZenMethod
    public static void removeRecipe(IIngredient itemInput, @Optional IIngredient itemOutput, @Optional IIngredient optionalItemOutput) {
        if (IngredientHelper.checkNotNull(NAME, itemInput)) {
            CrafttweakerIntegration.LATE_REMOVALS.add(new RemoveMekanismRecipe<>(NAME, Recipe.CELL_SEPARATOR, new IngredientWrapper(itemOutput, optionalItemOutput),
                    new IngredientWrapper(itemInput)));
        }
    }

    @ZenMethod
    public static void removeAllRecipes() {
        CrafttweakerIntegration.LATE_REMOVALS.add(new RemoveAllMekanismRecipe<>(NAME, Recipe.CELL_SEPARATOR));
    }
}
