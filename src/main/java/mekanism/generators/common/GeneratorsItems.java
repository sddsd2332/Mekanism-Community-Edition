package mekanism.generators.common;

import mekanism.common.MekanismModules;
import mekanism.common.item.ItemModule;
import mekanism.common.util.MekanismUtils;
import mekanism.generators.common.item.ItemHohlraum;
import mekanism.generators.common.item.ItemMekanismGenerators;
import mekanism.generators.common.tile.turbine.TileEntityTurbineRotor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MekanismGenerators.MODID)
public class GeneratorsItems {

    public static final Item SolarPanel = new ItemMekanismGenerators();
    public static final ItemHohlraum Hohlraum = new ItemHohlraum();
    public static final Item TurbineBlade = new ItemMekanismGenerators() {
        @Override
        public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
            return MekanismUtils.getTileEntitySafe(world, pos) instanceof TileEntityTurbineRotor;
        }
    };

    public static final ItemModule MODULE_SOLAR_RECHARGING = new ItemModule(MekanismModules.SOLAR_RECHARGING_UNIT);
    public static final ItemModule MODULE_GEOTHERMAL_GENERATOR = new ItemModule(GeneratorsModules.GEOTHERMAL_GENERATOR_UNIT);

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.register(init(SolarPanel, "SolarPanel"));
        registry.register(init(Hohlraum, "Hohlraum"));
        registry.register(init(TurbineBlade, "TurbineBlade"));
        registry.register(initModule(MODULE_SOLAR_RECHARGING));
        registry.register(initModule(MODULE_GEOTHERMAL_GENERATOR));
    }

    public static Item init(Item item, String name) {
        return item.setTranslationKey(name).setRegistryName(new ResourceLocation(MekanismGenerators.MODID, name));
    }

    public static Item initModule(ItemModule item) {
        String name = "module_" + item.getModuleData().getName();
        return item.setTranslationKey(name).setRegistryName(new ResourceLocation(MekanismGenerators.MODID, name)).setCreativeTab(MekanismGenerators.tabMekanismGenerators);
    }
}
