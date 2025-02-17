package mekanism.common.integration.mysticalagriculture;

import mekanism.common.MekanismFluids;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.inputs.AdvancedMachineInput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import static mekanism.common.integration.MekanismHooks.MYSTICALAGRICULTURE_MOD_ID;


/**
 * This code is obtained through CofhCore.
 */
public class MysticalAgricultureSeed {

    public static void seed() {

        for (int i = 1; i <= 5; i++) {
            ItemStack seeds = getSeeds("tier" + i + "_inferium");
            if (seeds != ItemStack.EMPTY) {
                if (RecipeHandler.Recipe.ORGANIC_FARM.containsRecipe(seeds)) {
                    RecipeHandler.Recipe.ORGANIC_FARM.remove(RecipeHandler.Recipe.ORGANIC_FARM.get().get(new AdvancedMachineInput(seeds, MekanismFluids.NutrientSolution)));
                    RecipeHandler.Recipe.ORGANIC_FARM.remove(RecipeHandler.Recipe.ORGANIC_FARM.get().get(new AdvancedMachineInput(seeds, MekanismFluids.Water)));
                    RecipeHandler.addOrganicFarmRecipe(seeds, MekanismFluids.NutrientSolution, getItemStack("crafting", i * 10, 0), new ItemStack(seeds.getItem(), 4), MekanismConfig.current().mekce.seed.val());
                    RecipeHandler.addOrganicFarmRecipe(seeds, MekanismFluids.Water, getItemStack("crafting", i * 5, 0), seeds, MekanismConfig.current().mekce.seed.val());
                } else if (!RecipeHandler.Recipe.ORGANIC_FARM.containsRecipe(seeds)) {
                    RecipeHandler.addOrganicFarmRecipe(seeds, MekanismFluids.NutrientSolution, getItemStack("crafting", i * 10, 0), new ItemStack(seeds.getItem(), 4), MekanismConfig.current().mekce.seed.val());
                    RecipeHandler.addOrganicFarmRecipe(seeds, MekanismFluids.Water, getItemStack("crafting", i * 5, 0), seeds, MekanismConfig.current().mekce.seed.val());
                }
            }
        }


        if (Loader.isModLoaded("mysticalagradditions")) {
            ItemStack tier6seeds = getSeeds2("tier6_inferium");
            if (tier6seeds != ItemStack.EMPTY) {
                if (RecipeHandler.Recipe.ORGANIC_FARM.containsRecipe(tier6seeds)) {
                    RecipeHandler.Recipe.ORGANIC_FARM.remove(RecipeHandler.Recipe.ORGANIC_FARM.get().get(new AdvancedMachineInput(tier6seeds, MekanismFluids.NutrientSolution)));
                    RecipeHandler.Recipe.ORGANIC_FARM.remove(RecipeHandler.Recipe.ORGANIC_FARM.get().get(new AdvancedMachineInput(tier6seeds, MekanismFluids.Water)));
                    RecipeHandler.addOrganicFarmRecipe(tier6seeds, MekanismFluids.NutrientSolution, getItemStack("crafting", 64, 0), new ItemStack(tier6seeds.getItem(), 4), MekanismConfig.current().mekce.seed.val());
                    RecipeHandler.addOrganicFarmRecipe(tier6seeds, MekanismFluids.Water, getItemStack("crafting", 32, 0), tier6seeds, MekanismConfig.current().mekce.seed.val());
                } else if (!RecipeHandler.Recipe.ORGANIC_FARM.containsRecipe(tier6seeds)) {
                    RecipeHandler.addOrganicFarmRecipe(tier6seeds, MekanismFluids.NutrientSolution, getItemStack("crafting", 64, 0), new ItemStack(tier6seeds.getItem(), 4), MekanismConfig.current().mekce.seed.val());
                    RecipeHandler.addOrganicFarmRecipe(tier6seeds, MekanismFluids.Water, getItemStack("crafting", 32, 0), tier6seeds, MekanismConfig.current().mekce.seed.val());
                }
            }
        }
    }


    protected static ItemStack getItemStack(String id, String name, int amount, int meta) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id + ":" + name));
        return item != null ? new ItemStack(item, amount, meta) : ItemStack.EMPTY;
    }

    protected static ItemStack getSeeds(String name) {
        return getItemStack(MYSTICALAGRICULTURE_MOD_ID, name + "_seeds", 1, 0);
    }

    protected static ItemStack getSeeds2(String name) {
        return getItemStack("mysticalagradditions", name + "_seeds", 1, 0);
    }

    protected static ItemStack getItemStack(String name, int amount, int meta) {
        return getItemStack(MYSTICALAGRICULTURE_MOD_ID, name, amount, meta);
    }
}
