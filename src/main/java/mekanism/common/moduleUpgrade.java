package mekanism.common;

import mekanism.common.util.LangUtils;

import java.util.Locale;

public enum moduleUpgrade {

    //EMPTY("base",1,),
    EnergyUnit("EnergyUnit", 8),
    ATTACK_AMPLIFICATION_UNIT("attack_damage", 4),
    RADIATION_SHIELDING_UNIT("radiation_shielding_unit", 1),
    //head
    SolarRechargingUnit("SolarRechargingUnit", 8),
    ElectrolyticBreathingUnit("ElectrolyticBreathingUnit", 4),
    VisionEnhancementUnit("VisionEnhancementUnit", 1),
    InhalationPurificationUnit("InhalationPurificationUnit", 1),
    NutritionalInjectionUnit("NutritionalInjectionUnit", 1),
    //Body
    JETPACK_UNIT("jetpack_unit", 1),
    CHARGE_DISTRIBUTION_UNIT("charge_distribution_unit", 1),
    //legs
    GYROSCOPIC_STABILIZATION_UNIT("gyroscopic_stabilization_unit", 1),
    GEOTHERMAL_GENERATOR_UNIT("geothermal_generator_unit", 8),
    HYDROSTATIC_REPULSOR_UNIT("hydrostatic_repulsor_unit", 4),
    LOCOMOTIVE_BOOSTING_UNIT("locomotive_boosting_unit", 4),
    //feet
    HYDRAULIC_PROPULSION_UNIT("hydraulic_propulsion_unit", 4),
    FROST_WALKER_UNIT("frost_walker_unit", 2),

    //mekaBow
    ARROWENERGY_UNIT("arrowenergy_unit", 1),
    ARROWVELOCITY_UNIT("arrowvelocity_unit", 8),
    AUTOFIRE_UNIT("autofire_unit", 1),
    DRAWSPEED_UNIT("drawspeed_unit", 3),
    MultipleArrowsUnit("MultiplearrowsUnit", 4),
    //Body
    GRAVITATIONAL_MODULATING_UNIT("gravitational_modulating_unit", 1),
    HEALTH_REGENERATION("Health_regeneration", 10),
    //head
    EMERGENCY_RESCUE("Emergency_rescue", 10),
    ;
    //ALL



    private String name;
    private int maxStack;

    moduleUpgrade(String s, int max) {
        name = s;
        maxStack = max;
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

    public boolean canMultiply() {
        return getMax() > 1;
    }
}
