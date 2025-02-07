package mekanism.api.radial.mode;


import mekanism.api.EnumColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface representing a mode for usage in a radial menu.
 *
 * @since 10.3.2
 */
public interface IRadialMode {

    /**
     * @return Name to display in this mode's slice of the radial menu.
     */
    @NotNull
    ITextComponent sliceName();

    /**
     * @return Asset location of the icon to draw in this mode's slice of the radial menu.
     */
    @NotNull
    ResourceLocation icon();

    /**
     * Gets the color to that is used for rendering this mode's slice of the radial menu when this mode is currently selected.
     *
     * @return Selection color of this mode's slice.
     */
    @Nullable
    default EnumColor color() {
        return null;
    }
}