package mekanism.common.tile.machine;

import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.machines.CellExtractorRecipe;
import mekanism.common.tile.prefab.TileEntityChanceMachine;

import java.util.Map;

public class TileEntityCellExtractor extends TileEntityChanceMachine<CellExtractorRecipe> {

    public TileEntityCellExtractor() {
        super("sawmill", MachineType.CELL_EXTRACTOR, 200);
    }

    @Override
    public Map<ItemStackInput, CellExtractorRecipe> getRecipes() {
        return Recipe.CELL_EXTRACTOR.get();
    }
}
