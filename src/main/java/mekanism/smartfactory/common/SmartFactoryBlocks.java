package mekanism.smartfactory.common;

import mekanism.smartfactory.common.block.BlockSmartFactoryMachine;
import mekanism.smartfactory.common.item.ItemBlockSmartFactoryMachine;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import static mekanism.smartfactory.common.block.states.BlockStateSmartFactoryMachine.SmartFactoryMachineBlock.SMART_FACTORY_MACHINE_BLOCK_1;

@GameRegistry.ObjectHolder(MekanismSmartFactory.MODID)
public class SmartFactoryBlocks {

    public static final Block SmartFactoryMachine = BlockSmartFactoryMachine.getBlockMachine(SMART_FACTORY_MACHINE_BLOCK_1);

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        registry.register(init(SmartFactoryMachine, "SmartFactoryMachine"));
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.register(SmartFactoryItems.init(new ItemBlockSmartFactoryMachine(SmartFactoryMachine), "SmartFactoryMachine"));
    }

    public static Block init(Block block, String name) {
        return block.setTranslationKey(name).setRegistryName(new ResourceLocation(MekanismSmartFactory.MODID, name));
    }
}
