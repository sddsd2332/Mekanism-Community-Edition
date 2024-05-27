package mekanism.multiblockmachine.client.jei;

import mekanism.client.jei.MekanismJEI;
import mekanism.multiblockmachine.common.MultiblockMachineBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.Item;

@JEIPlugin
public class MultiblockMachineJEI implements IModPlugin {
    @Override
    public void registerItemSubtypes(ISubtypeRegistry registry) {
        registry.registerSubtypeInterpreter(Item.getItemFromBlock(MultiblockMachineBlocks.MultiblockGenerator), MekanismJEI.NBT_INTERPRETER);
        registry.registerSubtypeInterpreter(Item.getItemFromBlock(MultiblockMachineBlocks.MultiblockMachine), MekanismJEI.NBT_INTERPRETER);
    }

    @Override
    public void register(IModRegistry registry) {
        MultiblockRecipeRegistryHelper.registerLargeSeparator(registry);
        MultiblockRecipeRegistryHelper.registerLargeChemicalInfuser(registry);
    }
}
