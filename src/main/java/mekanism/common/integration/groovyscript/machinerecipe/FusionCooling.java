package mekanism.common.integration.groovyscript.machinerecipe;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.compat.mods.mekanism.recipe.VirtualizedMekanismRegistry;
import com.cleanroommc.groovyscript.helper.ingredient.IngredientHelper;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import mekanism.common.integration.groovyscript.GrSMekanismAdd;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.inputs.FluidInput;
import mekanism.common.recipe.machines.FusionCoolingRecipe;
import mekanism.generators.common.MekanismGenerators;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import org.jetbrains.annotations.Nullable;

public class FusionCooling extends VirtualizedMekanismRegistry<FusionCoolingRecipe> {

    public FusionCooling() {
        super(RecipeHandler.Recipe.FUSION_COOLING);
    }

    public RecipeBuilder recipeBuilder() {
        return new RecipeBuilder();
    }


    public FusionCoolingRecipe add(FluidStack input, FluidStack output) {
        GroovyLog.Msg msg = GroovyLog.msg("Error adding MekanismGenerators Fusion Reactor recipe").error();
        msg.add(IngredientHelper.isEmpty(input), () -> "input must not be empty");
        msg.add(IngredientHelper.isEmpty(output), () -> "output must not be empty");
        if (msg.postIfNotEmpty()) return null;
        FusionCoolingRecipe recipe = new FusionCoolingRecipe(input.copy(), output.copy());
        recipeRegistry.put(recipe);
        addScripted(recipe);
        return recipe;
    }

    public boolean removeByInput(FluidStack input) {
        GroovyLog.Msg msg = GroovyLog.msg("Error removing MekanismGenerators Fusion Reactor  recipe").error();
        msg.add(IngredientHelper.isEmpty(input), () -> "input must not be empty");
        if (msg.postIfNotEmpty()) return false;
        FusionCoolingRecipe recipe = recipeRegistry.get().remove(new FluidInput(input));
        if (recipe != null) {
            addBackup(recipe);
            return true;
        }
        removeError("could not find recipe for %", input);
        return false;
    }

    public static class RecipeBuilder extends AbstractRecipeBuilder<FusionCoolingRecipe> {

        @Override
        public String getErrorMsg() {
            return "Error adding Mekanism Generators Fusion Reacto recipe";
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            validateItems(msg);
            validateFluids(msg, 1, 1, 1, 1);
        }

        @Override
        public @Nullable FusionCoolingRecipe register() {
            if (!validate()) return null;
            FusionCoolingRecipe recipe = new FusionCoolingRecipe(fluidInput.get(0), fluidOutput.get(0));
            GrSMekanismAdd.get().fusionCooling.add(recipe);
            return recipe;
        }
    }

    @Override
    public boolean isEnabled() {
        return Loader.isModLoaded(MekanismGenerators.MODID);
    }

}
