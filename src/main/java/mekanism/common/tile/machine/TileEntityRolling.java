package mekanism.common.tile.machine;

import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.machines.RollingRecipe;
import mekanism.common.tile.prefab.TileEntityElectricMachine;

import java.util.Map;

public class TileEntityRolling extends TileEntityElectricMachine<RollingRecipe> {

    public TileEntityRolling() {
        super("rolling", MachineType.ROLLING, 200);
    }

    @Override
    public Map<ItemStackInput, RollingRecipe> getRecipes() {
        return Recipe.ROLLING.get();
    }
}
