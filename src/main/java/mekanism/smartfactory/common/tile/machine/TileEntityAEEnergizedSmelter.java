package mekanism.smartfactory.common.tile.machine;

import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.inputs.ItemStackInput;
import mekanism.common.recipe.machines.SmeltingRecipe;
import mekanism.smartfactory.common.block.states.BlockStateSmartFactoryMachine;
import mekanism.smartfactory.common.tile.prefab.TileEntityAEElectricMachine;

import java.util.Map;

public class TileEntityAEEnergizedSmelter extends TileEntityAEElectricMachine<SmeltingRecipe> {

    public TileEntityAEEnergizedSmelter() {
        super("smelter", BlockStateSmartFactoryMachine.SmartFactoryMachineType.AE_ENERGIZED_SMELTER, 200);
    }

    @Override
    public Map<ItemStackInput, SmeltingRecipe> getRecipes() {
        return RecipeHandler.Recipe.ENERGIZED_SMELTER.get();
    }
}
