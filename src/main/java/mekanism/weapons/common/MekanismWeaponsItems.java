package mekanism.weapons.common;

import mekanism.weapons.common.item.ItemMekaBow;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(MekanismWeapons.MODID)
public class MekanismWeaponsItems {
    public static final ItemMekaBow mekaBow = new ItemMekaBow();

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.register(init(mekaBow, "mekabow"));
    }

    public static Item init(Item item, String name) {
        return item.setTranslationKey(name).setRegistryName(new ResourceLocation(MekanismWeapons.MODID, name));
    }
}
