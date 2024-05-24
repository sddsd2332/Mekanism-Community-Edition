package mekanism.multiblockmachine.common;

import mekanism.multiblockmachine.common.block.BlockMultiblockMachine;
import mekanism.multiblockmachine.common.block.BlockMultiblockMachineGenerator;
import mekanism.multiblockmachine.common.item.ItemBlockMultiblockGenerator;
import mekanism.multiblockmachine.common.item.ItemBlockMultiblockMachine;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import static mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine.MultiblockMachineBlock.MULTI_BLOCK_MACHINE_BLOCK_1;
import static mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachineGenerator.MultiblockMachineGeneratorBlock.MULTIBLOCK_MACHINE_GENERATOR_BLOCK_1;

@GameRegistry.ObjectHolder(MekanismMultiblockMachine.MODID)
public class MultiblockMachineBlocks {

    public static final Block MultiblockGenerator = BlockMultiblockMachineGenerator.getGeneratorBlock(MULTIBLOCK_MACHINE_GENERATOR_BLOCK_1);
    public static final Block MultiblockMachine = BlockMultiblockMachine.getBlockMachine(MULTI_BLOCK_MACHINE_BLOCK_1);

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        registry.register(init(MultiblockGenerator, "MultiblockGenerator"));
        registry.register(init(MultiblockMachine, "MultiblockMachine"));
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.register(MultiblockMachineItems.init(new ItemBlockMultiblockGenerator(MultiblockGenerator), "MultiblockGenerator"));
        registry.register(MultiblockMachineItems.init(new ItemBlockMultiblockMachine(MultiblockMachine), "MultiblockMachine"));
    }

    public static Block init(Block block, String name) {
        return block.setTranslationKey(name).setRegistryName(new ResourceLocation(MekanismMultiblockMachine.MODID, name));
    }
}
