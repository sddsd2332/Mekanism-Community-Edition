package mekanism.common.config.listener;

import mekanism.api.annotations.NonNullSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ConfigBasedCachedSupplier<VALUE> implements NonNullSupplier<VALUE>, Supplier<VALUE> {

    private final NonNullSupplier<VALUE> resolver;
    @Nullable
    private VALUE cachedValue;

    public ConfigBasedCachedSupplier(NonNullSupplier<VALUE> resolver) {
        this.resolver = resolver;
    }

    protected final void refresh() {
        this.cachedValue = resolver.get();
    }


    @Override
    public @NotNull VALUE get() {
        if (cachedValue == null) {
            //Lazily initialize the cached value so that we don't accidentally query values before they are initially set
            refresh();
        }
        return cachedValue;
    }
}
