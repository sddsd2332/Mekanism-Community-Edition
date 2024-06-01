package mekanism.common.tier;

import mekanism.api.EnumColor;

public enum AlloyTier implements ITier {
    INFUSED("infused", BaseTier.ADVANCED, EnumColor.RED),
    REINFORCED("reinforced", BaseTier.ELITE, EnumColor.AQUA),
    ATOMIC("atomic", BaseTier.ULTIMATE, EnumColor.PURPLE);

    private final BaseTier baseTier;
    private final String name;
    private final EnumColor color;

    AlloyTier(String name, BaseTier base, EnumColor color) {
        baseTier = base;
        this.name = name;
        this.color = color;
    }

    /**
     * Gets the name of this alloy tier.
     */
    public String getName() {
        return name;
    }


    @Override
    public BaseTier getBaseTier() {
        return baseTier;
    }

    public EnumColor getColor() {
        return color;
    }
}
