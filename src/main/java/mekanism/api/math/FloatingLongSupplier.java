package mekanism.api.math;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@FunctionalInterface
public interface FloatingLongSupplier extends Supplier<FloatingLong>, NonNullSupplier<FloatingLong> {

    @NotNull
    @Override
    FloatingLong get();
}