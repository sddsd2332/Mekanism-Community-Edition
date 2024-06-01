package mekanism.common.tier;

import mekanism.common.config.MekanismConfig;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum GasTankTier implements ITier, IStringSerializable {
    BASIC(64000, 1000),
    ADVANCED(256000, 16000),
    ELITE(1024000, 128000),
    ULTIMATE(8192000, 512000),
    CREATIVE(Integer.MAX_VALUE, Integer.MAX_VALUE / 2);

    private final int baseStorage;
    private final int baseOutput;
    private final BaseTier baseTier;

    GasTankTier(int s, int o) {
        baseStorage = s;
        baseOutput = o;
        baseTier = BaseTier.values()[ordinal()];
    }

    @Override
    public BaseTier getBaseTier() {
        return baseTier;
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public int getStorage() {
        return MekanismConfig.current().general.tiers.get(baseTier).GasTankStorage.val();
    }

    public int getOutput() {
        return MekanismConfig.current().general.tiers.get(baseTier).GasTankOutput.val();
    }

    public int getBaseStorage() {
        return baseStorage;
    }

    public int getBaseOutput() {
        return baseOutput;
    }
}
