package mekanism.client.newgui;

import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mekanism.client.gui.element.GuiUtils;
import mekanism.client.newgui.element.GuiElement;
import mekanism.common.Mekanism;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import mekanism.client.newgui.warning.WarningTracker.WarningType;


public interface IGuiWrapper {

    default void displayTooltip(String component, int x, int y, int maxWidth) {
        this.displayTooltips(Collections.singletonList(component), x, y, maxWidth);
    }

    default void displayTooltip(String component, int x, int y) {
        this.displayTooltips(Collections.singletonList(component), x, y);
    }

    default void displayTooltips(List<String> components, int xAxis, int yAxis) {
        displayTooltips(components, xAxis, yAxis, -1);
    }

    default void displayTooltips(List<String> components, int xAxis, int yAxis, int maxWidth) {
        net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(components, xAxis, yAxis, getWidth(), getHeight(), maxWidth, getFont());
    }

    default int getLeft() {
        if (this instanceof GuiContainer container) {
            return container.getGuiLeft();
        }
        return 0;
    }

    default int getTop() {
        if (this instanceof GuiContainer container) {
            return container.getGuiTop();
        }
        return 0;
    }

    default int getWidth() {
        if (this instanceof GuiContainer container) {
            return container.getXSize();
        }
        return 0;
    }

    default int getHeight() {
        if (this instanceof GuiContainer container) {
            return container.getYSize();
        }
        return 0;
    }

    /*
    default void addWindow(GuiWindow window) {
        Mekanism.logger.error("Tried to call 'addWindow' but unsupported in {}", getClass().getName());
    }

    default void removeWindow(GuiWindow window) {
        Mekanism.logger.error("Tried to call 'removeWindow' but unsupported in {}", getClass().getName());
    }

     */

    default boolean currentlyQuickCrafting() {
        return false;
    }

   /*
    @Nullable
    default GuiWindow getWindowHovering(double mouseX, double mouseY) {
        Mekanism.logger.error("Tried to call 'getWindowHovering' but unsupported in {}", getClass().getName());
        return null;
    }

    */

    @Nonnull
    default BooleanSupplier trackWarning(@Nonnull WarningType type, @Nonnull BooleanSupplier warningSupplier) {
        Mekanism.logger.error("Tried to call 'trackWarning' but unsupported in {}", getClass().getName());
        return warningSupplier;
    }

    @Nullable
    FontRenderer getFont();

    default void renderItem(@Nonnull ItemStack stack, int xAxis, int yAxis) {
        renderItem(stack, xAxis, yAxis, 1);
    }

    default void renderItem(@Nonnull ItemStack stack, int xAxis, int yAxis, float scale) {
        GuiUtils.renderItem(getItemRenderer(), stack, xAxis, yAxis, scale, getFont(), null, false);
    }

    RenderItem getItemRenderer();

    default void renderItemTooltip(@Nonnull ItemStack stack, int xAxis, int yAxis) {
        Mekanism.logger.error("Tried to call 'renderItemTooltip' but unsupported in {}", getClass().getName());
    }

    default void renderItemTooltipWithExtra(@Nonnull ItemStack stack, int xAxis, int yAxis, List<String> toAppend) {
        if (toAppend.isEmpty()) {
            renderItemTooltip(stack, xAxis, yAxis);
        } else {
            Mekanism.logger.error("Tried to call 'renderItemTooltipWithExtra' but unsupported in {}", getClass().getName());
        }
    }

    default void renderItemWithOverlay(@Nonnull ItemStack stack, int xAxis, int yAxis, float scale, @Nullable String text) {
        GuiUtils.renderItem(getItemRenderer(), stack, xAxis, yAxis, scale, getFont(), text, true);
    }

    /*
    default void setSelectedWindow(SelectedWindowData selectedWindow) {
        Mekanism.logger.error("Tried to call 'setSelectedWindow' but unsupported in {}", getClass().getName());
    }
     */

    default void addFocusListener(GuiElement element) {
        Mekanism.logger.error("Tried to call 'addFocusListener' but unsupported in {}", getClass().getName());
    }

    default void removeFocusListener(GuiElement element) {
        Mekanism.logger.error("Tried to call 'removeFocusListener' but unsupported in {}", getClass().getName());
    }

    default void focusChange(GuiElement changed) {
        Mekanism.logger.error("Tried to call 'focusChange' but unsupported in {}", getClass().getName());
    }

    default void incrementFocus(GuiElement current) {
        Mekanism.logger.error("Tried to call 'incrementFocus' but unsupported in {}", getClass().getName());
    }
}
