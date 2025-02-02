package mekanism.client.gui.element;

import mekanism.client.gui.IGuiWrapper;
import mekanism.common.util.MekanismUtils;
import net.minecraft.util.ResourceLocation;

public class GuiModuleScrollList  extends GuiElement{

    private final int xPosition;
    private final int yPosition;
    private final int xSize;
    private final int ySize;

    public GuiModuleScrollList(IGuiWrapper gui, ResourceLocation def, int x, int y,int sizeX, int sizeY) {
        super(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Null.png"), gui, def);
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
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {

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

}
