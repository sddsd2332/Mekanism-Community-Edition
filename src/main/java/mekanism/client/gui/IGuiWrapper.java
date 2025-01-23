package mekanism.client.gui;

import mekanism.common.Mekanism;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BooleanSupplier;

@SideOnly(Side.CLIENT)
public interface IGuiWrapper {

    void drawTexturedRect(int x, int y, int u, int v, int w, int h);

    void drawTexturedRectFromIcon(int x, int y, TextureAtlasSprite icon, int w, int h);

    void displayTooltip(String s, int xAxis, int yAxis);

    void displayTooltips(List<String> list, int xAxis, int yAxis);

    @Nullable
    FontRenderer getFont();

    default int getLeft() {
        if (this instanceof GuiContainer) {
            return ((GuiContainer) this).getGuiLeft();
        }
        return 0;
    }

    default int getTop() {
        if (this instanceof GuiContainer) {
            return ((GuiContainer) this).getGuiTop();
        }
        return 0;
    }

    default int getWidth() {
        if (this instanceof GuiContainer) {
            return ((GuiContainer) this).getXSize();
        }
        return 0;
    }

    default int getHeight() {
        if (this instanceof GuiContainer) {
            return ((GuiContainer) this).getYSize();
        }
        return 0;
    }

    @Nonnull
    default BooleanSupplier trackWarning(@Nonnull WarningType type, @Nonnull BooleanSupplier warningSupplier) {
        Mekanism.logger.error("Tried to call 'trackWarning' but unsupported in {}", getClass().getName());
        return warningSupplier;
    }
}
