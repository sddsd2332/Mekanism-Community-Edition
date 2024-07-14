package mekanism.common;

import mekanism.common.util.LangUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

public enum moduleUpgrade {

    //  EMPTY("base",1,),
    EnergyUnit("EnergyUnit", 8),
    ATTACK_AMPLIFICATION_UNIT("attack_damage", 4),
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
    //feet
    HYDRAULIC_PROPULSION_UNIT("hydraulic_propulsion_unit", 4),
    MAGNETIC_ATTRACTION_UNIT("magnetic_attraction_unit", 4),
    FROST_WALKER_UNIT("frost_walker_unit", 2),

    //mekaBow
    ARROWENERGY_UNIT("arrowenergy_unit", 1),
    ARROWVELOCITY_UNIT("arrowvelocity_unit", 8),
    AUTOFIRE_UNIT("autofire_unit", 1),
    DRAWSPEED_UNIT("drawspeed_unit", 3),
    MultipleArrowsUnit("MultiplearrowsUnit", 4),
    ;


    private String name;
    private int maxStack;


    moduleUpgrade(String s, int max) {
        name = s;
        maxStack = max;
    }

    public static Map<moduleUpgrade, Integer> buildMap(@Nullable NBTTagCompound nbtTags) {
        Map<moduleUpgrade, Integer> modules = new EnumMap<>(moduleUpgrade.class);
        if (nbtTags != null) {
            if (nbtTags.hasKey("module")) {
                NBTTagList list = nbtTags.getTagList("module", Constants.NBT.TAG_COMPOUND);
                for (int tagCount = 0; tagCount < list.tagCount(); tagCount++) {
                    NBTTagCompound compound = list.getCompoundTagAt(tagCount);
                    moduleUpgrade module = moduleUpgrade.values()[compound.getInteger("type")];
                    modules.put(module, compound.getInteger("amount"));
                }
            }
        }
        return modules;
    }

    public static void saveMap(Map<moduleUpgrade, Integer> module, NBTTagCompound nbtTags) {
        NBTTagList list = new NBTTagList();
        for (Entry<moduleUpgrade, Integer> entry : module.entrySet()) {
            list.appendTag(getTagFor(entry.getKey(), entry.getValue()));
        }
        nbtTags.setTag("module", list);
    }

    public static NBTTagCompound getTagFor(moduleUpgrade module, int amount) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("type", module.ordinal());
        compound.setInteger("amount", amount);
        return compound;
    }

    public String getName() {
        return name;
    }

    public String getLangName() {
        return LangUtils.localize("item.module." + name + ".name");
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
