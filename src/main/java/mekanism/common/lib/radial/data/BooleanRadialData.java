package mekanism.common.lib.radial.data;

import com.google.common.collect.Lists;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.radial.IRadialDataHelper.BooleanRadialModes;
import mekanism.api.radial.RadialData;
import mekanism.api.radial.mode.IRadialMode;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@NothingNullByDefault
public class BooleanRadialData extends RadialData<IRadialMode> {

    private final List<IRadialMode> modes;
    private final BooleanRadialModes rawModes;
    private final boolean defaultMode;

    BooleanRadialData(ResourceLocation identifier, BooleanRadialModes modes, boolean defaultMode) {
        super(identifier);
        this.rawModes = Objects.requireNonNull(modes, "Boolean modes cannot be null.");
        this.modes = Lists.newArrayList(this.rawModes.falseMode(), this.rawModes.trueMode());
        this.defaultMode = defaultMode;
    }

    @Nullable
    @Override
    public IRadialMode getDefaultMode(List<IRadialMode> modes) {
        return rawModes.get(defaultMode);
    }

    @Override
    public List<IRadialMode> getModes() {
        return modes;
    }

    @Override
    public int index(List<IRadialMode> modes, IRadialMode mode) {
        return getNetworkRepresentation(mode);
    }

    @Override
    public int tryGetNetworkRepresentation(IRadialMode mode) {
        return getNetworkRepresentation(mode);
    }

    @Override
    public int getNetworkRepresentation(IRadialMode mode) {
        if (mode.equals(rawModes.falseMode())) {
            return 0;
        } else if (mode.equals(rawModes.trueMode())) {
            return 1;
        }
        return -1;
    }

    @Nullable
    @Override
    public IRadialMode fromNetworkRepresentation(int networkRepresentation) {
        if (networkRepresentation == 0) {
            return rawModes.falseMode();
        } else if (networkRepresentation == 1) {
            return rawModes.trueMode();
        }
        return null;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (other == this) {
            return true;
        } else if (other == null || getClass() != other.getClass() || !super.equals(other)) {
            return false;
        }
        return rawModes.equals(((BooleanRadialData) other).rawModes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rawModes);
    }
}