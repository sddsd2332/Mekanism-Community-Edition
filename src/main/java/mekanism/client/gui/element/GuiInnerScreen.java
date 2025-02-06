package mekanism.client.gui.element;

import mekanism.client.SpecialColors;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public class GuiInnerScreen extends GuiElement {

    private final int xPosition;
    private final int yPosition;
    private final int xSize;
    private final int ySize;

    private boolean overlay;
    private Supplier<List<String>> renderStrings;
    private boolean centerY;
    private int spacing = 1;
    private int padding = 3;
    private float textScale = 1.0F;

    public GuiInnerScreen(IGuiWrapper gui, ResourceLocation def, int x, int y, int sizeX, int sizeY) {
        super(MekanismUtils.getResource(ResourceType.GUI, "Inner_Screen.png"), gui, def);
        xPosition = x;
        yPosition = y;
        xSize = sizeX;
        ySize = sizeY;
    }

    public GuiInnerScreen(IGuiWrapper gui,  ResourceLocation def, int x, int y, int width, int height, Supplier<List<String>> renderStrings) {
        this(gui,def,x,y,width,height);
        this.renderStrings = renderStrings;
        defaultFormat();
    }

    @Override
    public Rectangle4i getBounds(int guiWidth, int guiHeight) {
        return new Rectangle4i(guiWidth + xPosition, guiHeight + yPosition, xSize, ySize);
    }

    @Override
    protected boolean inBounds(int xAxis, int yAxis) {
        return xAxis >= xPosition && xAxis <= xPosition + xSize && yAxis >= yPosition && yAxis <= yPosition + ySize;
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        mc.renderEngine.bindTexture(RESOURCE);
        drawBlack(guiWidth, guiHeight);
        if (overlay) {
            mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.SLOT, "Slot_Icon.png"));
            guiObj.drawTexturedRect(guiWidth + xPosition + xSize - 18, guiHeight + yPosition, 0, 54, 18, 18);
        }
        mc.renderEngine.bindTexture(defaultLocation);
    }

    public GuiInnerScreen spacing(int spacing) {
        this.spacing = spacing;
        return this;
    }

    public GuiInnerScreen padding(int padding) {
        this.padding = padding;
        return this;
    }

    public GuiInnerScreen textScale(float textScale) {
        this.textScale = textScale;
        return this;
    }

    public GuiInnerScreen centerY() {
        centerY = true;
        return this;
    }

    public GuiInnerScreen clearFormat() {
        centerY = false;
        return this;
    }

    public GuiInnerScreen defaultFormat() {
        return padding(5).spacing(3).textScale(0.8F).centerY();
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {
        if (renderStrings != null) {
            List<String> list = renderStrings.get();
            int startY = yPosition + padding;
            if (centerY) {
                int listSize = list.size();
                int totalHeight = listSize * 8 + spacing * (listSize - 1);
                startY = yPosition + (ySize - totalHeight) / 2;
            }
            for (String text : renderStrings.get()) {
                getFontRenderer().drawString(text, xPosition + padding, startY, SpecialColors.TEXT_SCREEN.argb());
                startY += 8 + spacing;
            }
        }
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {
    }

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {
    }

    public GuiInnerScreen with(boolean overlay) {
        this.overlay = overlay;
        return this;
    }

    public void drawBlack(int guiWidth, int guiHeight) {
        int halfWidthLeft = xSize / 2;
        int halfWidthRight = xSize % 2 == 0 ? halfWidthLeft : halfWidthLeft + 1;
        int halfHeightTop = ySize / 2;
        int halfHeight = ySize % 2 == 0 ? halfHeightTop : halfHeightTop + 1;
        MekanismRenderer.resetColor();
        guiObj.drawTexturedRect(guiWidth + xPosition, guiHeight + yPosition, 0, 0, halfWidthLeft, halfHeightTop);
        guiObj.drawTexturedRect(guiWidth + xPosition, guiHeight + yPosition + halfHeightTop, 0, 256 - halfHeight, halfWidthLeft, halfHeight);
        guiObj.drawTexturedRect(guiWidth + xPosition + halfWidthLeft, guiHeight + yPosition, 256 - halfWidthRight, 0, halfWidthRight, halfHeightTop);
        guiObj.drawTexturedRect(guiWidth + xPosition + halfWidthLeft, guiHeight + yPosition + halfHeightTop, 256 - halfWidthRight, 256 - halfHeight, halfWidthRight, halfHeight);
    }


}
