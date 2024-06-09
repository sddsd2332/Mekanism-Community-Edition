package mekanism.multiblockmachine.common;

import mekanism.api.gas.GasStack;
import mekanism.common.MekanismBlocks;
import mekanism.common.MekanismFluids;
import mekanism.common.MekanismItems;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.RecipeHandler;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;

public class MekanismMultiblockMachineRecipe {

    public static void addRecipes() {
        ItemStack EnergyCubeStack = new ItemStack(MekanismBlocks.EnergyCube);
        if (!EnergyCubeStack.hasTagCompound()) {
            EnergyCubeStack.setTagCompound(new NBTTagCompound());
        }
        EnergyCubeStack.getTagCompound().setInteger("tier", 3);

        ItemStack GasTankStack = new ItemStack(MekanismBlocks.GasTank);
        if (!GasTankStack.hasTagCompound()) {
            GasTankStack.setTagCompound(new NBTTagCompound());
        }
        GasTankStack.getTagCompound().setInteger("tier", 3);

        ItemStack FluidTankStack = new ItemStack(MekanismBlocks.MachineBlock2, 1, 11);
        if (!FluidTankStack.hasTagCompound()) {
            FluidTankStack.setTagCompound(new NBTTagCompound());
        }
        FluidTankStack.getTagCompound().setInteger("tier", 3);


        if (MekanismConfig.current().multiblock.multiblockmachinesManager.isEnabled(BlockStateMultiblockMachine.MultiblockMachineType.DIGITAL_ASSEMBLY_TABLE)) {
            RecipeHandler.addDigitalAssemblyTableRecipe(
                    GasTankStack, FluidTankStack, GasTankStack,
                    new ItemStack(MekanismItems.AtomicAlloy, 9), new ItemStack(MekanismItems.ElectrolyticCore, 9), new ItemStack(MekanismItems.AtomicAlloy, 9),
                    new ItemStack(MekanismBlocks.BasicBlock, 9, 5), new ItemStack(MekanismBlocks.BasicBlock, 9, 8), new ItemStack(MekanismBlocks.BasicBlock, 9, 5),
                    FluidRegistry.getFluidStack("water", 10000), new GasStack(MekanismFluids.NutrientSolution, 10000),
                    new ItemStack(MultiblockMachineBlocks.MultiblockMachine, 1, 0), FluidRegistry.getFluidStack("lava", 10000), new GasStack(MekanismFluids.Hydrogen, 10000),
                    100, 2400
            );
        }
    }

}
