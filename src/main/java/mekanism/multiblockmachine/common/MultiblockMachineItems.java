package mekanism.multiblockmachine.common;

import mekanism.multiblockmachine.common.item.ItemMultiblockMachine;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(MekanismMultiblockMachine.MODID)
public class MultiblockMachineItems {

    public static final Item PlasmaCutterNozzles = new ItemMultiblockMachine().setItemMaxDamage(100000);
    public static final Item DrillBit = new ItemMultiblockMachine().setItemMaxDamage(100000);
    public static final Item LaserLenses = new ItemMultiblockMachine().setItemMaxDamage(100000);

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.register(init(PlasmaCutterNozzles, "PlasmaCutterNozzles"));
        registry.register(init(DrillBit, "DrillBit"));
        registry.register(init(LaserLenses, "LaserLenses"));
    }

    public static Item init(Item item, String name) {
        return item.setTranslationKey(name).setRegistryName(new ResourceLocation(MekanismMultiblockMachine.MODID, name));
    }
}
