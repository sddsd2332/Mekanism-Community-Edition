package mekanism.common.integration.forgeenergy;

import mekanism.common.config.MekanismConfig;
import mekanism.common.util.MekanismUtils;

public class FEIntegration {

    public static double fromfe(int rf) {
        return rf * MekanismConfig.current().general.FROM_FORGE.val();
    }

    public static double fromfe(double rf) {
        return rf * MekanismConfig.current().general.FROM_FORGE.val();
    }

    public static int tofe(double joules) {
        return MekanismUtils.clampToInt(joules * MekanismConfig.current().general.TO_FORGE.val());
    }

    public static long tofeAsLong(double joules) {
        return Math.round(joules * MekanismConfig.current().general.TO_FORGE.val());
    }

    public static double tofeAsDouble(double joules) {
        return joules * MekanismConfig.current().general.TO_FORGE.val();
    }
}
