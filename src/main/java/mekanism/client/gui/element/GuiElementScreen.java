package mekanism.client.gui.element;

import mekanism.client.gui.IGuiWrapper;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiElementScreen extends GuiElement {

    private final int xPosition;
    private final int yPosition;
    private final int xSize;
    private final int ySize;
    private boolean isFrame = false;

    public GuiElementScreen(IGuiWrapper gui, ResourceLocation def, int x, int y, int sizeX, int sizeY) {
        super(MekanismUtils.getResource(ResourceType.GUI, "Null.png"), gui, def);
        xPosition = x;
        yPosition = y;
        xSize = sizeX;
        ySize = sizeY;
    }

    @Override
    public Rectangle4i getBounds(int guiWidth, int guiHeight) {
        return new Rectangle4i(guiWidth + xPosition, guiHeight + yPosition, xSize, ySize);
    }

    @Override
    protected boolean inBounds(int xAxis, int yAxis) {
        return xAxis >= xPosition && xAxis <= xPosition + xSize && yAxis >= yPosition && yAxis <= yPosition + ySize;
    }

    public GuiElementScreen isFrame() {
        this.isFrame = true;
        return this;
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.GUI, isFrame ? "Element_Holder_Frame.png" : "Element_Holder.png"));
        drawBlack(guiWidth, guiHeight);
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
