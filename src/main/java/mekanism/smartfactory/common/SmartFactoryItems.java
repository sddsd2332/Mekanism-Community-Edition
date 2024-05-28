package mekanism.smartfactory.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(MekanismSmartFactory.MODID)
public class SmartFactoryItems {

    public static void registerItems(IForgeRegistry<Item> registry) {

    }

    public static Item init(Item item, String name) {
        return item.setTranslationKey(name).setRegistryName(new ResourceLocation(MekanismSmartFactory.MODID, name));
    }
}
