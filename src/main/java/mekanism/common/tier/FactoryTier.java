package mekanism.common.tier;

import java.util.function.Consumer;

import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;
import mekanism.common.block.states.BlockStateMachine.MachineType;

public enum FactoryTier implements ITier {
    BASIC(3),
    ADVANCED(5),
    ELITE(7),
    ULTIMATE(9),
    CREATIVE(11);

    public final int processes;
    private final BaseTier baseTier;

    FactoryTier(int process) {
        processes = process;
        baseTier = BaseTier.values()[ordinal()];
    }

    @Override
    public BaseTier getBaseTier() {
        return baseTier;
    }

    public static void forEnabled(Consumer<FactoryTier> consumer) {
        if (MachineType.BASIC_FACTORY.isEnabled()) {
            consumer.accept(FactoryTier.BASIC);
        }
        if (MachineType.ADVANCED_FACTORY.isEnabled()) {
            consumer.accept(FactoryTier.ADVANCED);
        }
        if (MachineType.ELITE_FACTORY.isEnabled()) {
            consumer.accept(FactoryTier.ELITE);
        }
        if (MachineType.ULTIMATE_FACTORY.isEnabled()) {
            consumer.accept(FactoryTier.ULTIMATE);
        }
        if (MachineType.CREATIVE_FACTORY.isEnabled()) {
            consumer.accept(FactoryTier.CREATIVE);
        }
    }
}
