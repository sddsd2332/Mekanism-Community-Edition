package mekanism.api.tier;

/**
 * Enum representing the different tiers of alloys.
 */
public enum AlloyTier implements ITier {
    INFUSED("Enriched", BaseTier.ADVANCED),
    REINFORCED("Reinforced", BaseTier.ELITE),
    ATOMIC("Atomic", BaseTier.ULTIMATE),
    Cosmic("Cosmic",BaseTier.CREATIVE);


    private final BaseTier baseTier;
    private final String name;

    AlloyTier(String name, BaseTier base) {
        baseTier = base;
        this.name = name;
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
}