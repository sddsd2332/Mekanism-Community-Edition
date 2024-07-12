package mekanism.weapons.common;

import mekanism.weapons.common.item.ItemMekaArrow;
import mekanism.weapons.common.item.ItemMekaBow;
import mekanism.weapons.common.item.ItemMekaTana;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(MekanismWeapons.MODID)
public class MekanismWeaponsItems {
    public static final ItemMekaBow mekaBow = new ItemMekaBow();
    public static final ItemMekaArrow mekArrow = new ItemMekaArrow();
    public static final ItemMekaTana mekaTana = new ItemMekaTana();

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.register(init(mekaBow, "mekabow"));
        registry.register(init(mekArrow,"mekarrow"));
        registry.register(init(mekaTana,"mekatana"));
    }

    public static Item init(Item item, String name) {
        return item.setTranslationKey(name).setRegistryName(new ResourceLocation(MekanismWeapons.MODID, name));
    }
}
