package mekanism.multiblockmachine.common;

import mekanism.multiblockmachine.common.item.ItemMultiblockMachine;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(MekanismMultiblockMachine.MODID)
public class MultiblockMachineItems {

    public static final Item gas_adsorption_fractionation_module = new ItemMultiblockMachine().setItemMaxDamage(100000).setMaxStackSize(1);
    public static final Item high_frequency_fusion_molding_module = new ItemMultiblockMachine().setItemMaxDamage(100000).setMaxStackSize(1);
    public static final Item LaserLenses = new ItemMultiblockMachine().setItemMaxDamage(100000).setMaxStackSize(1);
    public static final Item advanced_electrolysis_core = new ItemMultiblockMachine();


    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.register(init(gas_adsorption_fractionation_module, "gas_adsorption_fractionation_module"));
        registry.register(init(high_frequency_fusion_molding_module, "high_frequency_fusion_molding_module"));
        registry.register(init(LaserLenses, "LaserLenses"));
        registry.register(init(advanced_electrolysis_core,"advanced_electrolysis_core"));
    }

    public static Item init(Item item, String name) {
        return item.setTranslationKey(name).setRegistryName(new ResourceLocation(MekanismMultiblockMachine.MODID, name));
    }
}
