package mekanism.multiblockmachine.common;

import mekanism.api.gas.GasStack;
import mekanism.common.MekanismBlocks;
import mekanism.common.MekanismFluids;
import mekanism.common.MekanismItems;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.RecipeHandler;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

public class MekanismMultiblockMachineRecipe {

    public static void addRecipes() {

        if (MekanismConfig.current().multiblock.multiblockmachinesManager.isEnabled(BlockStateMultiblockMachine.MultiblockMachineType.DIGITAL_ASSEMBLY_TABLE)) {
            RecipeHandler.addDigitalAssemblyTableRecipe(
                    new ItemStack(MultiblockMachineBlocks.MidsizeGasTank, 4), new ItemStack(MekanismBlocks.BasicBlock, 36, 10), new ItemStack(MultiblockMachineBlocks.MidsizeGasTank, 4),
                    new ItemStack(MekanismBlocks.BasicBlock, 36, 9), new ItemStack(MultiblockMachineItems.advanced_electrolysis_core, 8), new ItemStack(MekanismBlocks.BasicBlock, 36, 9),
                    new ItemStack(MekanismBlocks.BasicBlock, 16, 5), new ItemStack(MekanismBlocks.BasicBlock, 9, 8), new ItemStack(MekanismBlocks.BasicBlock, 16, 5),
                    FluidRegistry.getFluidStack("water", 20000), new GasStack(MekanismFluids.Oxygen, 10000),
                    new ItemStack(MultiblockMachineBlocks.MultiblockMachine, 1, 0), FluidRegistry.getFluidStack("water", 1000), new GasStack(MekanismFluids.Hydrogen, 1000),
                    100, 2400);

            RecipeHandler.addDigitalAssemblyTableRecipe(
                    new ItemStack(MekanismBlocks.BasicBlock, 36, 9), new ItemStack(MekanismItems.AtomicAlloy, 36), new ItemStack(MekanismBlocks.BasicBlock, 36, 9),
                    new ItemStack(MekanismBlocks.BasicBlock, 18, 8), new ItemStack(MekanismBlocks.BasicBlock2, 27, 5), new ItemStack(MekanismBlocks.BasicBlock, 18, 8),
                    new ItemStack(MekanismBlocks.BasicBlock, 10, 5), new ItemStack(MekanismBlocks.BasicBlock, 10, 5), new ItemStack(MekanismBlocks.BasicBlock, 10, 5),
                    FluidRegistry.getFluidStack("water", 20000), new GasStack(MekanismFluids.Oxygen, 10000),
                    new ItemStack(MultiblockMachineBlocks.MultiblockGenerator, 1, 1), FluidRegistry.getFluidStack("water", 1000), new GasStack(MekanismFluids.Hydrogen, 1000)
                    , 100, 2400);

            RecipeHandler.addDigitalAssemblyTableRecipe(
                    new ItemStack(MekanismBlocks.BasicBlock, 16, 8), new ItemStack(MekanismBlocks.BasicBlock, 16, 10), new ItemStack(MekanismBlocks.BasicBlock, 16, 8),
                    new ItemStack(MultiblockMachineBlocks.MultiblockGasTank, 4), new ItemStack(MultiblockMachineItems.advanced_electrolysis_core, 16), new ItemStack(MultiblockMachineBlocks.MultiblockGasTank, 4),
                    new ItemStack(MekanismBlocks.BasicBlock, 16, 5), new ItemStack(MekanismItems.AtomicAlloy, 64), new ItemStack(MekanismBlocks.BasicBlock, 16, 5),
                    FluidRegistry.getFluidStack("water", 20000), new GasStack(MekanismFluids.Oxygen, 10000),
                    new ItemStack(MultiblockMachineBlocks.MultiblockGenerator, 1, 2), FluidRegistry.getFluidStack("water", 1000), new GasStack(MekanismFluids.Hydrogen, 1000),
                    100, 2400);

        }
    }

}
