package mekanism.common.tile.machine;

import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.machines.TurningRecipe;
import mekanism.common.tile.prefab.TileEntityElectricMachine;

import java.util.Map;

public class TileEntityTurning extends TileEntityElectricMachine<TurningRecipe> {

    public TileEntityTurning() {
        super("turning", MachineType.TURNING, 200);
    }

    @Override
    public Map<ItemStackInput, TurningRecipe> getRecipes() {
        return Recipe.TURNING.get();
    }
}
