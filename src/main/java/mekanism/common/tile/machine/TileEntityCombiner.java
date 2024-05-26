package mekanism.common.tile.machine;

import mekanism.common.Upgrade;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.DoubleMachineInput;
import mekanism.common.recipe.machines.CombinerRecipe;
import mekanism.common.tile.prefab.TileEntityDoubleElectricMachine;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Map;

public class TileEntityCombiner extends TileEntityDoubleElectricMachine<CombinerRecipe> {

    public TileEntityCombiner() {
        super("combiner", MachineType.COMBINER, 200);
        upgradeComponent.setSupported(Upgrade.STONE_GENERATOR);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            if (upgradeComponent.isUpgradeInstalled(Upgrade.STONE_GENERATOR)) {
                for (DoubleMachineInput input : getRecipes().keySet()) {
                    if (ItemHandlerHelper.canItemStacksStack(input.extraStack, new ItemStack(Blocks.COBBLESTONE))) {
                        if (input.useItem(inventory, 0, false)) {
                            if (inventory.get(1).isEmpty()) {
                                inventory.set(1, getRecipes().get(input).getInput().extraStack);
                                electricityStored -= getRecipes().get(input).getInput().extraStack.getCount() * 10;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Map<DoubleMachineInput, CombinerRecipe> getRecipes() {
        return Recipe.COMBINER.get();
    }
}
