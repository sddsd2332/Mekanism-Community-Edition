package mekanism.common.tile.machine;

import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.machines.CellSeparatorRecipe;
import mekanism.common.tile.prefab.TileEntityChanceMachine;

import java.util.Map;

public class TileEntityCellSeparator extends TileEntityChanceMachine<CellSeparatorRecipe> {

    public TileEntityCellSeparator() {
        super("sawmill", MachineType.CELL_SEPARATOR, 200);
    }

    @Override
    public Map<ItemStackInput, CellSeparatorRecipe> getRecipes() {
        return Recipe.CELL_SEPARATOR.get();
    }
}
