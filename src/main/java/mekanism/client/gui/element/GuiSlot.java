package mekanism.client.gui.element;

import mekanism.client.gui.IGuiWrapper;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSlot extends GuiElement {

    private final int xLocation;
    private final int yLocation;
    private final int textureX;
    private final int textureY;
    private final int width;
    private final int height;
    private SlotOverlay overlay = null;

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

    @Override
    public Rectangle4i getBounds(int guiWidth, int guiHeight) {
        return new Rectangle4i(guiWidth + xLocation, guiHeight + yLocation, width, height);
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        mc.renderEngine.bindTexture(RESOURCE);
        guiObj.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, textureX, textureY, width, height);
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
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {
    }

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {
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
        WORD(18,18,72,54);

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
        WIND_OFF(12, 12, 0, 88),
        WIND_ON(12, 12, 12, 88),
        NO_SUN(12, 12, 24, 88),
        SEES_SUN(12, 12, 36, 88);

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
