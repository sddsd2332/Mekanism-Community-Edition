package mekanism.common;

import mekanism.api.EnumColor;
import mekanism.common.util.LangUtils;

public enum moduleUpgrade {

  //  EMPTY("base",1,),
    EnergyUnit("EnergyUnit",8,EnumColor.GREY);

    private String name;
    private int maxStack;
    private EnumColor color;

    moduleUpgrade(String s, int max , EnumColor c){
        name = s;
        maxStack = max;
        color = c;
    }
    public String getName() {
        return  name;
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
}
