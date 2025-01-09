package mekanism.common;

import mekanism.common.util.LangUtils;
import net.minecraft.item.EnumRarity;

import java.util.Locale;

import static net.minecraft.item.EnumRarity.*;

public enum moduleUpgrade {

    //EMPTY("base",1,),
    EnergyUnit("EnergyUnit", 8, UNCOMMON),
    ATTACK_AMPLIFICATION_UNIT("attack_damage", 4, UNCOMMON),
    RADIATION_SHIELDING_UNIT("radiation_shielding_unit", 1, UNCOMMON),
    //head
    SolarRechargingUnit("SolarRechargingUnit", 8, RARE),
    ElectrolyticBreathingUnit("ElectrolyticBreathingUnit", 4, UNCOMMON),
    VisionEnhancementUnit("VisionEnhancementUnit", 4, RARE),
    InhalationPurificationUnit("InhalationPurificationUnit", 1, RARE),
    NutritionalInjectionUnit("NutritionalInjectionUnit", 1, RARE),
    //Body
    JETPACK_UNIT("jetpack_unit", 1, RARE),
    CHARGE_DISTRIBUTION_UNIT("charge_distribution_unit", 1, RARE),
    //legs
    GYROSCOPIC_STABILIZATION_UNIT("gyroscopic_stabilization_unit", 1, RARE),
    GEOTHERMAL_GENERATOR_UNIT("geothermal_generator_unit", 8, RARE),
    HYDROSTATIC_REPULSOR_UNIT("hydrostatic_repulsor_unit", 4, RARE),
    LOCOMOTIVE_BOOSTING_UNIT("locomotive_boosting_unit", 4, RARE),
    //feet
    HYDRAULIC_PROPULSION_UNIT("hydraulic_propulsion_unit", 4, RARE),
    FROST_WALKER_UNIT("frost_walker_unit", 2, RARE),

    //mekaBow
    ARROWENERGY_UNIT("arrowenergy_unit", 1),
    ARROWVELOCITY_UNIT("arrowvelocity_unit", 8),
    AUTOFIRE_UNIT("autofire_unit", 1),
    DRAWSPEED_UNIT("drawspeed_unit", 3),
    MultipleArrowsUnit("MultiplearrowsUnit", 4),
    //Body
    GRAVITATIONAL_MODULATING_UNIT("gravitational_modulating_unit", 1, EPIC),
    HEALTH_REGENERATION("Health_regeneration", 10, RARE),
    //head
    EMERGENCY_RESCUE("Emergency_rescue", 10, EPIC),
    //ALL
    ENERGY_SHIELD_UNIT("Energy_shield_unit", 10, RARE),
    ADVANCED_INTERCEPTION_SYSTEM_UNIT("Advanced_interception_system_unit", 1,EPIC),
    MAGNETIZER("Magnetizer_unit", 1,UNCOMMON);

    private String name;
    private int maxStack;
    private EnumRarity rarity;

    moduleUpgrade(String s, int max) {
        name = s;
        maxStack = max;
        rarity = EnumRarity.COMMON;
    }

    moduleUpgrade(String s, int max, EnumRarity rarity) {
        name = s;
        maxStack = max;
        this.rarity = rarity;
    }


    public String getName() {
        return name;
    }

    public String getLangName() {
        return LangUtils.localize("item.module." + name.toLowerCase(Locale.ROOT) + ".name");
    }

    public String getDescription() {
        return LangUtils.localize("module." + name + ".desc");
    }

    public int getMax() {
        return maxStack;
    }

    public EnumRarity getRarity(){
        return rarity;
    }

    public boolean canMultiply() {
        return getMax() > 1;
    }
}
