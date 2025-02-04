package mekanism.client.newgui.warning;

import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BooleanSupplier;
import mekanism.client.newgui.warning.WarningTracker.WarningType;

public interface IWarningTracker {

    BooleanSupplier trackWarning(@Nonnull WarningType type, @Nonnull BooleanSupplier warningSupplier);

    boolean hasWarning();

    List<ITextComponent> getWarnings();

    void clearTrackedWarnings();
}
