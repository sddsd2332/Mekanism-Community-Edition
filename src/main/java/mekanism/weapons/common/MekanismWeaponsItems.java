package mekanism.weapons.common;

import mekanism.weapons.common.item.ItemMekaArrow;
import mekanism.weapons.common.item.ItemMekaBow;
import mekanism.weapons.common.item.ItemMekaTana;
import mekanism.weapons.common.item.ItemWeapons;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(MekanismWeapons.MODID)
public class MekanismWeaponsItems {
    public static final ItemMekaBow mekaBow = new ItemMekaBow();
    public static final ItemMekaArrow mekaArrow = new ItemMekaArrow();
    public static final ItemMekaTana mekaTana = new ItemMekaTana();
    public static final Item katana_blade  = new ItemWeapons();
    public static final Item bow_riser  = new ItemWeapons();
    public static final Item bow_limb = new ItemWeapons();


    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.register(init(mekaBow, "mekabow"));
        registry.register(init(mekaArrow,"mekaarrow"));
        registry.register(init(mekaTana,"mekatana"));
        registry.register(init(katana_blade,"katana_blade"));
        registry.register(init(bow_riser,"bow_riser"));
        registry.register(init(bow_limb,"bow_limb"));
    }

    public static Item init(Item item, String name) {
        return item.setTranslationKey(name).setRegistryName(new ResourceLocation(MekanismWeapons.MODID, name));
    }
}
