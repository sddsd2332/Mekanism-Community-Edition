package mekanism.generators.common;

import mekanism.api.gear.ModuleData;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.generators.common.content.gear.mekasuit.ModuleGeothermalGeneratorUnit;
import net.minecraft.item.EnumRarity;

public class GeneratorsModules {

    //Pants
    public static final ModuleData<ModuleGeothermalGeneratorUnit> GEOTHERMAL_GENERATOR_UNIT = ModuleHelper.register("geothermal_generator_unit",
            ModuleGeothermalGeneratorUnit::new, builder -> builder.maxStackSize(8).rarity(EnumRarity.RARE));

}
