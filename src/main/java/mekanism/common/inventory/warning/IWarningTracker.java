package mekanism.common.inventory.warning;

import java.util.List;
import java.util.function.BooleanSupplier;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import org.jetbrains.annotations.NotNull;

public interface IWarningTracker {

    BooleanSupplier trackWarning(@NotNull WarningType type, @NotNull BooleanSupplier warningSupplier);

    boolean hasWarning();

    List<String> getWarnings();

    void clearTrackedWarnings();
}