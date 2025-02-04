package mekanism.client.newgui.element.slot;

import mekanism.api.EnumColor;
import mekanism.client.gui.element.GuiUtils;
import mekanism.client.newgui.IGuiWrapper;
import mekanism.client.newgui.element.GuiElement;
import mekanism.client.newgui.element.GuiTexturedElement;
import mekanism.client.newgui.warning.WarningTracker.WarningType;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class GuiSlot extends GuiTexturedElement {

    private static final int INVALID_SLOT_COLOR = MekanismRenderer.getColorARGB(EnumColor.DARK_RED, 0.8F);
    public static final int DEFAULT_HOVER_COLOR = 0x80FFFFFF;
    private final SlotType slotType;
    private Supplier<ItemStack> validityCheck;
    private Supplier<ItemStack> storedStackSupplier;
    private Supplier<SlotOverlay> overlaySupplier;
    @Nullable
    private BooleanSupplier warningSupplier;
    @Nullable
    private IntSupplier overlayColorSupplier;
    @Nullable
    private SlotOverlay overlay;
    @Nullable
    private IHoverable onHover;
    @Nullable
    private GuiElement.IClickable onClick;
    private boolean renderHover;
    private boolean renderAboveSlots;

    public GuiSlot(SlotType type, IGuiWrapper gui, int x, int y) {
        super(type.getTexture(), gui, x, y, type.getWidth(), type.getHeight());
        this.slotType = type;
        active = false;
    }

    public GuiSlot validity(Supplier<ItemStack> validityCheck) {
        //TODO - WARNING SYSTEM: Evaluate if any of these validity things should be moved to the warning system
        this.validityCheck = validityCheck;
        return this;
    }

    //TODO - WARNING SYSTEM: Hook up usage of warnings
    public GuiSlot warning(@Nonnull WarningType type, @Nonnull BooleanSupplier warningSupplier) {
        this.warningSupplier = gui().trackWarning(type, warningSupplier);
        return this;
    }

    /**
     * @apiNote For use when there is no validity check and this is a "fake" slot in that the container screen doesn't render the item by default.
     */
    public GuiSlot stored(Supplier<ItemStack> storedStackSupplier) {
        this.storedStackSupplier = storedStackSupplier;
        return this;
    }

    public GuiSlot hover(IHoverable onHover) {
        this.onHover = onHover;
        return this;
    }

    public GuiSlot click(IClickable onClick) {
        this.onClick = onClick;
        return this;
    }

    public GuiSlot with(SlotOverlay overlay) {
        this.overlay = overlay;
        return this;
    }

    public GuiSlot overlayColor(IntSupplier colorSupplier) {
        overlayColorSupplier = colorSupplier;
        return this;
    }

    public GuiSlot with(Supplier<SlotOverlay> overlaySupplier) {
        this.overlaySupplier = overlaySupplier;
        return this;
    }

    public GuiSlot setRenderHover(boolean renderHover) {
        this.renderHover = renderHover;
        return this;
    }


    public GuiSlot setRenderAboveSlots() {
        this.renderAboveSlots = true;
        return this;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        if (!renderAboveSlots) {
            draw();
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float partialTicks) {
        if (renderAboveSlots) {
            draw();
        }
    }

    private void draw() {
        if (warningSupplier != null && warningSupplier.getAsBoolean()) {
            minecraft.renderEngine.bindTexture(slotType.getWarningTexture());
        } else {
            minecraft.renderEngine.bindTexture(getResource());
        }
        GuiUtils.blit(x, y, 0, 0, width, height, width, height);
        if (overlaySupplier != null) {
            overlay = overlaySupplier.get();
        }
        if (overlay != null) {
            minecraft.renderEngine.bindTexture(overlay.getTexture());
            GuiUtils.blit(x, y, 0, 0, overlay.getWidth(), overlay.getHeight(), overlay.getWidth(), overlay.getHeight());
        }
        drawContents();
    }

    protected void drawContents() {
        if (validityCheck != null) {
            ItemStack invalid = validityCheck.get();
            if (!invalid.isEmpty()) {
                int xPos = x + 1;
                int yPos = y + 1;
                GuiUtils.fill(xPos, yPos, xPos + 16, yPos + 16, INVALID_SLOT_COLOR);
                MekanismRenderer.resetColor();
                gui().renderItem(invalid, xPos, yPos);
            }
        } else if (storedStackSupplier != null) {
            ItemStack stored = storedStackSupplier.get();
            if (!stored.isEmpty()) {
                gui().renderItem(stored, x + 1, y + 1);
            }
        }
    }

    @Override
    public void renderForeground(int mouseX, int mouseY) {
        if (renderHover && isHovered()) {
            int xPos = relativeX + 1;
            int yPos = relativeY + 1;
            GuiUtils.fill(xPos, yPos, xPos + 16, yPos + 16, DEFAULT_HOVER_COLOR);
            MekanismRenderer.resetColor();
        }
        if (overlayColorSupplier != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 10);
            int xPos = relativeX + 1;
            int yPos = relativeY + 1;
            GuiUtils.fill(xPos, yPos, xPos + 16, yPos + 16, overlayColorSupplier.getAsInt());
            GlStateManager.popMatrix();
            MekanismRenderer.resetColor();
        }
        if (isHovered()) {
            //TODO: Should it pass it the proper mouseX and mouseY. Probably, though buttons may have to be redone slightly then
            renderToolTip(mouseX - getGuiLeft(), mouseY - getGuiTop());
        }
    }

    @Override
    public void renderToolTip(int mouseX, int mouseY) {
        super.renderToolTip(mouseX, mouseY);
        if (onHover != null) {
            onHover.onHover(this, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (onClick != null && isValidClickButton(button)) {
            if (mouseX >= x + borderSize() && mouseY >= y + borderSize() && mouseX < x + width - borderSize() && mouseY < y + height - borderSize()) {
                onClick.onClick(this, (int) mouseX, (int) mouseY);
                playDownSound(Minecraft.getMinecraft().getSoundHandler());
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }


    public int borderSize() {
        return 1;
    }
}
