package mekanism.common;

import mekanism.api.EnumColor;
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
    EnergyUnit("EnergyUnit", 8, EnumColor.GREY);

    private String name;
    private int maxStack;
    private EnumColor color;

    moduleUpgrade(String s, int max, EnumColor c) {
        name = s;
        maxStack = max;
        color = c;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return LangUtils.localize("module." + name + ".desc");
    }

    public int getMax() {
        return maxStack;
    }

    public EnumColor getColor() {
        return color;
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
        for (Entry<moduleUpgrade, Integer> entry :module.entrySet()){
            list.appendTag(getTagFor(entry.getKey(), entry.getValue()));
        }
        nbtTags.setTag("module", list);
    }

    public static NBTTagCompound getTagFor(moduleUpgrade module, int amount) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("type",module.ordinal());
        compound.setInteger("amount", amount);
        return compound;
    }

}
