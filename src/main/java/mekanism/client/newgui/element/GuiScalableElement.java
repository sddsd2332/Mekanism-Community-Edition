package mekanism.client.newgui.element;

import mekanism.client.newgui.IGuiWrapper;
import net.minecraft.util.ResourceLocation;

public abstract class GuiScalableElement extends GuiTexturedElement {

    protected final int sideWidth;
    protected final int sideHeight;

    protected GuiScalableElement(ResourceLocation resource, IGuiWrapper gui, int x, int y, int width, int height, int sideWidth, int sideHeight) {
        super(resource, gui, x, y, width, height);
        active = false;
        this.sideWidth = sideWidth;
        this.sideHeight = sideHeight;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(mouseX, mouseY, partialTicks);
        renderBackgroundTexture(getResource(), sideWidth, sideHeight);
    }
}