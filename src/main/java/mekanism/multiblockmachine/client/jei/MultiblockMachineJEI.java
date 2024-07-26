package mekanism.multiblockmachine.client.jei;

import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.GuiElementHandler;
import mekanism.client.jei.MekanismJEI;
import mekanism.multiblockmachine.client.jei.machine.other.DigitalAssemblyTableRecipeCategory;
import mekanism.multiblockmachine.common.MultiblockMachineBlocks;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine.*;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.Item;

@JEIPlugin
public class MultiblockMachineJEI implements IModPlugin {
    @Override
    public void registerItemSubtypes(ISubtypeRegistry registry) {
        registry.registerSubtypeInterpreter(Item.getItemFromBlock(MultiblockMachineBlocks.MultiblockGenerator), MekanismJEI.NBT_INTERPRETER);
        registry.registerSubtypeInterpreter(Item.getItemFromBlock(MultiblockMachineBlocks.MultiblockMachine), MekanismJEI.NBT_INTERPRETER);
        registry.registerSubtypeInterpreter(Item.getItemFromBlock(MultiblockMachineBlocks.MidsizeGasTank),MekanismJEI.NBT_INTERPRETER);
        registry.registerSubtypeInterpreter(Item.getItemFromBlock(MultiblockMachineBlocks.MultiblockGasTank),MekanismJEI.NBT_INTERPRETER);
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addAdvancedGuiHandlers(new GuiElementHandler());
        MultiblockRecipeRegistryHelper.registerLargeSeparator(registry);
        MultiblockRecipeRegistryHelper.registerLargeChemicalInfuser(registry);
        MultiblockRecipeRegistryHelper.registerLargeChemicalWasher(registry);
        MultiblockRecipeRegistryHelper.registerDigitalAssemblyTable(registry);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        addRecipeCategory(registry, MultiblockMachineType.DIGITAL_ASSEMBLY_TABLE,new DigitalAssemblyTableRecipeCategory(guiHelper));
    }

    private void addRecipeCategory(IRecipeCategoryRegistration registry, MultiblockMachineType type, BaseRecipeCategory category){
        if (type.isEnabled()) {
            registry.addRecipeCategories(category);
        }
    }

}
