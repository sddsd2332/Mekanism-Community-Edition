package mekanism.multiblockmachine.common;

import mekanism.api.gas.GasStack;
import mekanism.common.MekanismBlocks;
import mekanism.common.MekanismFluids;
import mekanism.common.MekanismItems;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.RecipeHandler;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
                    new ItemStack(MekanismItems.AtomicAlloy, 36), new ItemStack(MekanismBlocks.BasicBlock, 36, 10), new ItemStack(MekanismItems.AtomicAlloy, 36),
                    new ItemStack(MultiblockMachineBlocks.MidsizeGasTank, 4), new ItemStack(MekanismBlocks.BasicBlock, 36, 9), new ItemStack(MultiblockMachineBlocks.MidsizeGasTank, 4),
                    new ItemStack(MekanismBlocks.BasicBlock, 16, 5), new ItemStack(MekanismBlocks.BasicBlock, 16, 8), new ItemStack(MekanismBlocks.BasicBlock, 16, 5),
                    FluidRegistry.getFluidStack("water", 20000), new GasStack(MekanismFluids.Oxygen, 10000),
                    new ItemStack(MultiblockMachineBlocks.MultiblockMachine, 1, 1), FluidRegistry.getFluidStack("water", 1000), new GasStack(MekanismFluids.Hydrogen, 1000),
                    100, 2400);

            ItemStack FluidTankStack = new ItemStack(MekanismBlocks.MachineBlock2, 4, 11);
            if (!FluidTankStack.hasTagCompound()) {
                FluidTankStack.setTagCompound(new NBTTagCompound());
            }
            FluidTankStack.getTagCompound().setInteger("tier", 3);

            RecipeHandler.addDigitalAssemblyTableRecipe(
                    new ItemStack(MekanismItems.AtomicAlloy, 36), new ItemStack(MekanismBlocks.BasicBlock, 36, 10), new ItemStack(MekanismItems.AtomicAlloy, 36),
                    new ItemStack(MekanismBlocks.BasicBlock, 16, 8), FluidTankStack, new ItemStack(MekanismBlocks.BasicBlock, 16, 8),
                    new ItemStack(MekanismBlocks.BasicBlock, 16, 5), new ItemStack(MultiblockMachineBlocks.MidsizeGasTank, 4), new ItemStack(MekanismBlocks.BasicBlock, 16, 5),
                    FluidRegistry.getFluidStack("water", 20000), new GasStack(MekanismFluids.Oxygen, 10000),
                    new ItemStack(MultiblockMachineBlocks.MultiblockMachine, 1, 2), FluidRegistry.getFluidStack("water", 1000), new GasStack(MekanismFluids.Hydrogen, 1000),
                    100, 2400);

            ItemStack EnergyCubeStack = new ItemStack(MekanismBlocks.EnergyCube,4);
            if (!EnergyCubeStack.hasTagCompound()) {
                EnergyCubeStack.setTagCompound(new NBTTagCompound());
            }
            EnergyCubeStack.getTagCompound().setInteger("tier", 3);


            RecipeHandler.addDigitalAssemblyTableRecipe(
                    new ItemStack(MekanismBlocks.BasicBlock, 36, 0), new ItemStack(MekanismBlocks.BasicBlock, 64, 5), new ItemStack(MekanismBlocks.BasicBlock, 36, 0),
                    new ItemStack(MekanismBlocks.BasicBlock, 36, 8), new ItemStack(MekanismItems.AtomicAlloy, 36), new ItemStack(MekanismBlocks.BasicBlock, 36, 8),
                    EnergyCubeStack, new ItemStack(MekanismItems.ControlCircuit, 16, 3), EnergyCubeStack,
                    FluidRegistry.getFluidStack("water", 20000), new GasStack(MekanismFluids.Oxygen, 10000),
                    new ItemStack(MultiblockMachineBlocks.MultiblockGenerator, 1, 0), FluidRegistry.getFluidStack("water", 1000), new GasStack(MekanismFluids.Hydrogen, 1000)
                    , 100, 2400);


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

            ItemStack GasTankStack = new ItemStack(MekanismBlocks.GasTank);
            if (!GasTankStack.hasTagCompound()) {
                GasTankStack.setTagCompound(new NBTTagCompound());
            }
            GasTankStack.getTagCompound().setInteger("tier", 3);

            RecipeHandler.addDigitalAssemblyTableRecipe(
                    new ItemStack(MekanismBlocks.BasicBlock, 1, 9), new ItemStack(MekanismBlocks.BasicBlock, 1, 9), new ItemStack(MekanismBlocks.BasicBlock, 1, 9),
                    new ItemStack(MekanismBlocks.BasicBlock, 1, 9), GasTankStack, new ItemStack(MekanismBlocks.BasicBlock, 1, 9),
                    new ItemStack(MekanismBlocks.BasicBlock, 1, 9), GasTankStack, new ItemStack(MekanismBlocks.BasicBlock, 1, 9),
                    FluidRegistry.getFluidStack("water", 20000), new GasStack(MekanismFluids.Oxygen, 10000),
                    new ItemStack(MultiblockMachineBlocks.MidsizeGasTank, 1), FluidRegistry.getFluidStack("water", 1000), new GasStack(MekanismFluids.Hydrogen, 1000), 100, 2400);

            RecipeHandler.addDigitalAssemblyTableRecipe(
                    new ItemStack(MekanismBlocks.BasicBlock, 1, 9), new ItemStack(MekanismBlocks.BasicBlock, 1, 9), new ItemStack(MekanismBlocks.BasicBlock, 1, 9),
                    new ItemStack(MekanismBlocks.BasicBlock, 1, 9), GasTankStack, new ItemStack(MekanismBlocks.BasicBlock, 1, 9),
                    new ItemStack(MekanismBlocks.BasicBlock, 1, 9), new ItemStack(MultiblockMachineBlocks.MidsizeGasTank, 1), new ItemStack(MekanismBlocks.BasicBlock, 1, 9),
                    FluidRegistry.getFluidStack("water", 20000), new GasStack(MekanismFluids.Oxygen, 10000),
                    new ItemStack(MultiblockMachineBlocks.MultiblockGasTank, 1), FluidRegistry.getFluidStack("water", 1000), new GasStack(MekanismFluids.Hydrogen, 1000), 100, 2400);

            RecipeHandler.addDigitalAssemblyTableRecipe(
                    new ItemStack(MekanismItems.Ingot, 64, 4), new ItemStack(MekanismItems.ControlCircuit, 64, 3), new ItemStack(MekanismItems.Ingot, 64, 4),
                    new ItemStack(MekanismItems.TierInstaller, 1),  new ItemStack(MekanismItems.ModuleBase, 1), new ItemStack(MekanismItems.TierInstaller, 1),
                    new ItemStack(MekanismItems.AtomicAlloy, 64), new ItemStack(MekanismItems.EnergyTablet, 4), new ItemStack(MekanismItems.AtomicAlloy, 64),
                    FluidRegistry.getFluidStack("water", 20000), new GasStack(MekanismFluids.Oxygen, 10000),
                    new ItemStack(MekanismItems.ThreadUpgrade), FluidRegistry.getFluidStack("water", 1000), new GasStack(MekanismFluids.Hydrogen, 1000), 100, 2400);

        }
    }

}
