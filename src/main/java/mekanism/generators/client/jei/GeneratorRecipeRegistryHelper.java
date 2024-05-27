package mekanism.generators.client.jei;

import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.recipe.outputs.MachineOutput;
import mekanism.generators.client.gui.GuiReactorHeat;
import mekanism.generators.client.jei.machine.other.FusionCoolingRecipeWrapper;
import mekanism.generators.common.block.states.BlockStateReactor;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

import java.util.stream.Collectors;


public class GeneratorRecipeRegistryHelper {


    public static void registerFusionCooling(IModRegistry registry) {
        addRecipes(registry, RecipeHandler.Recipe.FUSION_COOLING, FusionCoolingRecipeWrapper::new);
        registry.addRecipeClickArea(GuiReactorHeat.class, 133, 84, 18, 30, RecipeHandler.Recipe.FUSION_COOLING.getJEICategory());
        registry.addRecipeCatalyst(BlockStateReactor.ReactorBlockType.REACTOR_CONTROLLER.getStack(1), RecipeHandler.Recipe.FUSION_COOLING.getJEICategory());
        registry.addRecipeCatalyst(BlockStateReactor.ReactorBlockType.REACTOR_PORT.getStack(1), RecipeHandler.Recipe.FUSION_COOLING.getJEICategory());
    }


    private static <INPUT extends MachineInput<INPUT>, OUTPUT extends MachineOutput<OUTPUT>, RECIPE extends MachineRecipe<INPUT, OUTPUT, RECIPE>>
    void addRecipes(IModRegistry registry, RecipeHandler.Recipe<INPUT, OUTPUT, RECIPE> type, IRecipeWrapperFactory<RECIPE> factory) {
        String recipeCategoryUid = type.getJEICategory();
        registry.handleRecipes(type.getRecipeClass(), factory, recipeCategoryUid);
        registry.addRecipes(type.get().values().stream().map(factory::getRecipeWrapper).collect(Collectors.toList()), recipeCategoryUid);
    }
}
