package mekanism.common.tile.machine;

import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.machines.StampingRecipe;
import mekanism.common.tile.prefab.TileEntityElectricMachine;

import java.util.Map;

public class TileEntityStamping extends TileEntityElectricMachine<StampingRecipe> {

    public TileEntityStamping() {
        super("stamping", MachineType.STAMPING, 200);
    }

    @Override
    public Map<ItemStackInput, StampingRecipe> getRecipes() {
        return Recipe.STAMPING.get();
    }
}
