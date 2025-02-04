package mekanism.client.gui.element;

import mekanism.client.gui.IGuiWrapper;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.sound.SoundHandler;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public class GuiSlot extends GuiElement {

    private final int xLocation;
    private final int yLocation;
    private final int textureX;
    private final int textureY;
    private final int width;
    private final int height;
    private SlotOverlay overlay = null;
    @Nullable
    private IClickable onClick;
    @Nullable
    private IntSupplier overlayColorSupplier;
    private Supplier<SlotOverlay> overlaySupplier;

    public GuiSlot(SlotType type, IGuiWrapper gui, ResourceLocation def, int x, int y) {
        super(MekanismUtils.getResource(ResourceType.SLOT, "Slot_Icon.png"), gui, def);

        xLocation = x;
        yLocation = y;

        width = type.width;
        height = type.height;

        textureX = type.textureX;
        textureY = type.textureY;
    }

    public GuiSlot with(SlotOverlay overlay) {
        this.overlay = overlay;
        return this;
    }


    public GuiSlot with(Supplier<SlotOverlay> overlaySupplier) {
        this.overlaySupplier = overlaySupplier;
        return this;
    }

    @Override
    public Rectangle4i getBounds(int guiWidth, int guiHeight) {
        return new Rectangle4i(guiWidth + xLocation, guiHeight + yLocation, width, height);
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        mc.renderEngine.bindTexture(RESOURCE);
        guiObj.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, textureX, textureY, width, height);
        if (overlaySupplier != null) {
            overlay = overlaySupplier.get();
        }
        if (overlay != null) {
            int w = overlay.width;
            int h = overlay.height;
            int xLocationOverlay = xLocation + (width - w) / 2;
            int yLocationOverlay = yLocation + (height - h) / 2;
            guiObj.drawTexturedRect(guiWidth + xLocationOverlay, guiHeight + yLocationOverlay, overlay.textureX, overlay.textureY, w, h);
        }
        mc.renderEngine.bindTexture(defaultLocation);
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {
        if (overlayColorSupplier != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 10);
            int xPos = xLocation + 1;
            int yPos = yLocation + 1;
            GuiUtils.fill( xPos, yPos, xPos + 16, yPos + 16, overlayColorSupplier.getAsInt());
            GlStateManager.popMatrix();
            MekanismRenderer.resetColor();
        }
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {
    }

    public GuiSlot click(IClickable onClick) {
        this.onClick = onClick;
        return this;
    }

    public GuiSlot overlayColor(IntSupplier colorSupplier) {
        overlayColorSupplier = colorSupplier;
        return this;
    }

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {
        if (onClick != null && button == 0) {
            if (xAxis >= xLocation + 1 && yAxis >= yLocation + 1 && xAxis < xLocation + width - 1 && yAxis < yLocation + height - 1) {
                onClick.onClick(this, xAxis, yAxis);
                SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
            }
        }
    }

    public enum SlotType {
        NORMAL(18, 18, 0, 0),
        POWER(18, 18, 18, 0),
        INPUT(18, 18, 36, 0),
        EXTRA(18, 18, 54, 0),
        OUTPUT(18, 18, 72, 0),
        AQUA(18, 18, 36, 54),
        OUTPUT_LARGE(26, 26, 90, 0),
        NORMAL_LARGE(26, 26, 90, 26),
        OUTPUT_WIDE(42, 26, 116, 0),
        OUTPUT_LARGE_WIDE(36, 54, 116, 26),
        STATE_HOLDER(16, 16, 0, 72),
        WORD(18, 18, 72, 54);

        public final int width;
        public final int height;

        public final int textureX;
        public final int textureY;

        SlotType(int w, int h, int x, int y) {
            width = w;
            height = h;

            textureX = x;
            textureY = y;
        }
    }

    public enum SlotOverlay {
        MINUS(18, 18, 0, 18),
        PLUS(18, 18, 18, 18),
        POWER(18, 18, 36, 18),
        INPUT(18, 18, 54, 18),
        OUTPUT(18, 18, 72, 18),
        CHECK(18, 18, 0, 36),
        FORMULA(18, 18, 36, 36),
        UPGRADE(18, 18, 54, 36),
        MODULE(18, 18, 72, 36),
        WIND_OFF(12, 12, 0, 88),
        WIND_ON(12, 12, 12, 88),
        NO_SUN(12, 12, 24, 88),
        SEES_SUN(12, 12, 36, 88),
        SELECT(18, 18, 54, 54),
        ;

        public final int width;
        public final int height;

        public final int textureX;
        public final int textureY;

        SlotOverlay(int w, int h, int x, int y) {
            width = w;
            height = h;

            textureX = x;
            textureY = y;
        }
    }
}
