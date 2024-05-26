package mekanism.client.gui.button;

import mekanism.client.render.MekanismRenderer;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.function.Supplier;


@SideOnly(Side.CLIENT)
public class GuiDisableableButton extends GuiButton {

    private final ResourceLocation Button = MekanismUtils.getResource(MekanismUtils.ResourceType.BUTTON, "Button.png");

    private final ResourceLocation Button_BLACK = MekanismUtils.getResource(MekanismUtils.ResourceType.BUTTON, "Button_Black.png");
    private final ResourceLocation Button_Digital = MekanismUtils.getResource(MekanismUtils.ResourceType.BUTTON, "Button_Digital.png");
    private final ResourceLocation ButtonIcon = MekanismUtils.getResource(MekanismUtils.ResourceType.BUTTON, "Button_Icon.png");
    private ImageOverlay overlay = null;
    private boolean Toggleborders = false;
    private boolean scale;
    private float textscale;
    private int enabledColor = 0xFFFFFFFF;
    private int disabledColor = 0xFFA0A0A0;
    private int hoveredOrFocusedColor = 0xFFFFFFFF;

    private Supplier<Integer> mod;

    public GuiDisableableButton(int id, int x, int y, int size) {
        this(id, x, y, size, size, "");
    }

    public GuiDisableableButton(int id, int x, int y, int width, int height) {
        this(id, x, y, width, height, "");
    }

    public GuiDisableableButton(int id, int x, int y, int width, int height, Supplier<Integer> mod) {
        this(id, x, y, width, height, "");
        this.mod = mod;
    }

    public GuiDisableableButton(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
    }

    public GuiDisableableButton with(ImageOverlay overlay) {
        this.overlay = overlay;
        return this;
    }

    public GuiDisableableButton handoff(boolean Toggleborders) {
        this.Toggleborders = Toggleborders;
        return this;
    }

    public GuiDisableableButton textscale(boolean scale, float textscale) {
        this.textscale = textscale;
        this.scale = scale;
        return this;
    }

    public GuiDisableableButton enabledTextColor(int color) {
        this.enabledColor = color;
        return this;
    }

    public GuiDisableableButton disabledTextColor(int color) {
        this.disabledColor = color;
        return this;
    }

    public GuiDisableableButton hoveredtextColor(int color) {
        this.hoveredOrFocusedColor = color;
        return this;
    }

    @Override
    protected int getHoverState(boolean hoveredOrFocused) {
        if (!enabled) {
            return 0;
        } else if (hoveredOrFocused) {
            return 2;
        }
        return 1;
    }

    protected int GetMod() {
        if (mod != null) {
            return mod.get() * 18;
        } else {
            return 0;
        }
    }

    protected int getTextColor(boolean hoveredOrFocused) {
        if (!enabled) {
            return disabledColor;
        } else if (hoveredOrFocused) {
            return hoveredOrFocusedColor;
        }
        return enabledColor;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            //Ensure the color gets reset. The default GuiButtonImage doesn't so other GuiButton's can have the color leak out of them
            MekanismRenderer.resetColor();
            hovered = mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
            int i = getHoverState(hovered);
            int position = i * 20;
            int halfWidthLeft = width / 2;
            int halfWidthRight = width % 2 == 0 ? halfWidthLeft : halfWidthLeft + 1;
            int halfHeightTop = height / 2;
            int halfHeightBottom = height % 2 == 0 ? halfHeightTop : halfHeightTop + 1;
            ResourceLocation Texture = (overlay == ImageOverlay.CHECKMARK_DIGITAL || Toggleborders) ? Button_Digital : overlay == ImageOverlay.DUMP ? Button_BLACK : Button;
            mc.getTextureManager().bindTexture(Texture);
            GlStateManager.disableDepth();
            drawTexturedModalRect(x, y, 0, position, halfWidthLeft, halfHeightTop);
            drawTexturedModalRect(x, y + halfHeightTop, 0, position + 20 - halfHeightBottom, halfWidthLeft, halfHeightBottom);
            drawTexturedModalRect(x + halfWidthLeft, y, 200 - halfWidthRight, position, halfWidthRight, halfHeightTop);
            drawTexturedModalRect(x + halfWidthLeft, y + halfHeightTop, 200 - halfWidthRight, position + 20 - halfHeightBottom, halfWidthRight, halfHeightBottom);
            if (overlay != null && displayString.isEmpty()) {
                mc.getTextureManager().bindTexture(ButtonIcon);
                int w = overlay.width;
                int h = overlay.height;
                int xLocationOverlay = x + (width - w) / 2;
                int yLocationOverlay = y + (height - h) / 2;
                drawTexturedModalRect(xLocationOverlay, yLocationOverlay, overlay.textureX + GetMod(), overlay.textureY, w, h);
            }
            if (!displayString.isEmpty()) {
                if (!scale) {
                    drawCenteredString(fontrenderer, displayString, x + width / 2, y + height / 2 - 4, getTextColor(hovered));
                } else {
                    int textWidth = fontrenderer.getStringWidth(displayString);
                    float centerX = x - (textWidth / 2F) * textscale;
                    fontrenderer.drawString(displayString, (int) centerX, y, getTextColor(hovered));
                }
            }
            GlStateManager.enableDepth();
        }
    }


    public enum ImageOverlay {
        AUTO_EJECT(18, 18, 0, 0),
        AUTO_PULL(18, 18, 18, 0),
        AUTO_TOGGLE(18, 18, 36, 0),
        BACK(18, 18, 54, 0),
        CHECKMARK(18, 18, 72, 0),
        CHECKMARK_DIGITAL(18, 18, 90, 0),
        CLEAR_SIDES(18, 18, 108, 0),
        CLOSE(18, 18, 126, 0),
        CRAFT_AVAILABLE(18, 18, 144, 0),
        CRAFT_SINGLE(18, 18, 162, 0),
        CRAFTING(18, 18, 180, 0),
        DEFAULT(18, 18, 198, 0),
        DOWN(18, 18, 216, 0),
        DROP(18, 18, 234, 0),
        ENCODE_FORMULA(18, 18, 0, 18),
        EXCLAMATION(18, 18, 18, 18),
        FILL_EMPTY(18, 18, 36, 18),
        FOLLOW(18, 18, 54, 18),
        FUZZY(18, 18, 72, 18),
        HOME(18, 18, 90, 18),
        INVENTORY(18, 18, 108, 18),
        LEFT(18, 18, 126, 18),
        MAIN(18, 18, 144, 18),
        MINUS(18, 18, 162, 18),
        PLUS(18, 18, 180, 18),
        PRIVATE(18, 18, 198, 18),
        PUBLIC(18, 18, 216, 18),
        RENAME(18, 18, 234, 18),
        REPAIR(18, 18, 0, 36),
        RESET(18, 18, 18, 36),
        RIGHT(18, 18, 36, 36),
        ROUND_ROBIN(18, 18, 54, 36),
        SILK_TOUCH(18, 18, 72, 36),
        SINGLE(18, 18, 90, 36),
        SKIN(18, 18, 108, 36),
        SMELTING(18, 18, 126, 36),
        STOCK_CONTROL(18, 18, 144, 36),
        DUMP(36, 18, 162, 36),
        TRUSTED(18, 18, 198, 36),
        UP(18, 18, 216, 36),
        INVERSE(18, 18, 234, 36),
        SMALL_BACK(18, 18, 0, 54),
        TOGGLE(18, 18, 0, 72),
        GAS_MOD(18, 18, 0, 90),
        FORCE_RUN(18,18,18,54)
        ;

        public final int width;
        public final int height;
        public final int textureX;
        public final int textureY;

        ImageOverlay(int w, int h, int x, int y) {
            width = w;
            height = h;

            textureX = x;
            textureY = y;
        }
    }

}
