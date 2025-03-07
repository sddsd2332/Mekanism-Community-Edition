package mekanism.client.newgui.element.scroll;

import mekanism.client.gui.element.GuiUtils;
import mekanism.client.newgui.IGuiWrapper;
import mekanism.client.newgui.element.GuiElement;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.util.ResourceLocation;

public abstract class GuiScrollList extends GuiScrollableElement {

    protected static final ResourceLocation SCROLL_LIST = MekanismUtils.getResource(ResourceType.GUI, "scroll_list.png");
    protected static final int TEXTURE_WIDTH = 6;
    protected static final int TEXTURE_HEIGHT = 6;

    private final ResourceLocation background;
    private final int backgroundSideSize;
    protected final int elementHeight;

    protected GuiScrollList(IGuiWrapper gui, int x, int y, int width, int height, int elementHeight, ResourceLocation background, int backgroundSideSize) {
        super(SCROLL_LIST, gui, x, y, width, height, width - 6, 2, 4, 4, height - 4);
        this.elementHeight = elementHeight;
        this.background = background;
        this.backgroundSideSize = backgroundSideSize;
    }

    @Override
    protected int getFocusedElements() {
        return (height - 2) / elementHeight;
    }

    public abstract boolean hasSelection();

    protected abstract void setSelected(int index);

    public abstract void clearSelection();

    protected abstract void renderElements(int mouseX, int mouseY, float partialTicks);

    @Override
    public void drawBackground(int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(mouseX, mouseY, partialTicks);
        //Draw the background
        renderBackgroundTexture(background, backgroundSideSize, backgroundSideSize);
        GuiElement.minecraft.renderEngine.bindTexture(getResource());
        //Draw Scroll
        //Top border
        GuiUtils.blit(barX - 1, barY - 1, 0, 0, TEXTURE_WIDTH, 1, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        //Middle border
        GuiUtils.blit(barX - 1, barY, 6, maxBarHeight, 0, 1, TEXTURE_WIDTH, 1, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        //Bottom border
        GuiUtils.blit(barX - 1, y + maxBarHeight + 2, 0, 0, TEXTURE_WIDTH, 1, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        //Scroll bar
        GuiUtils.blit(barX, barY + getScroll(), 0, 2, barWidth, barHeight, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        //Draw the elements
        renderElements(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        if (mouseX >= x + 1 && mouseX < barX - 1 && mouseY >= y + 1 && mouseY < y + height - 1) {
            int index = getCurrentSelection();
            clearSelection();
            for (int i = 0; i < getFocusedElements(); i++) {
                if (index + i < getMaxElements()) {
                    int shiftedY = y + 1 + elementHeight * i;
                    if (mouseY >= shiftedY && mouseY <= shiftedY + elementHeight) {
                        setSelected(index + i);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return isMouseOver(mouseX, mouseY) && adjustScroll(delta) || super.mouseScrolled(mouseX, mouseY, delta);
    }
}