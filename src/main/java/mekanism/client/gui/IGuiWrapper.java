package mekanism.client.gui;

import mekanism.common.Mekanism;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;

@SideOnly(Side.CLIENT)
public interface IGuiWrapper {

    void drawTexturedRect(int x, int y, int u, int v, int w, int h);

    void drawTexturedRectFromIcon(int x, int y, TextureAtlasSprite icon, int w, int h);

    void olddisplayTooltip(String s, int xAxis, int yAxis);

    void olddisplayTooltips(List<String> list, int xAxis, int yAxis);


    default void displayTooltip(ITextComponent component, int x, int y, int maxWidth) {
        this.displayTooltips(Collections.singletonList(component.getFormattedText()), x, y, maxWidth);
    }

    default void displayTooltip(ITextComponent component, int x, int y) {
        this.displayTooltips(Collections.singletonList(component.getFormattedText()), x, y);
    }

    default void displayTooltips(List<String> components, int xAxis, int yAxis) {
        displayTooltips(components, xAxis, yAxis, -1);
    }

    default void displayTooltips(List<String> components, int xAxis, int yAxis, int maxWidth) {
        //TODO - 1.18: Re-evaluate some form of this that wraps further along for use in Gui Windows (such as viewing descriptions of supported upgrades)
        GuiUtils.drawHoveringText(components, xAxis, yAxis, getWidth(), getHeight(), maxWidth, getFont());
    }

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
