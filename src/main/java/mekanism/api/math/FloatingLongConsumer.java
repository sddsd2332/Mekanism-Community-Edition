package mekanism.api.math;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@FunctionalInterface
public interface FloatingLongConsumer extends Consumer<FloatingLong> {

    @Override
    void accept(@NotNull FloatingLong floatingLong);
}
