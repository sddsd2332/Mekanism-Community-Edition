package mekanism.client.newgui.element;

import java.util.List;
import mekanism.client.newgui.IGuiWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class GuiTexturedElement extends GuiElement {

    protected final ResourceLocation resource;

    public GuiTexturedElement(ResourceLocation resource, IGuiWrapper gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
        this.resource = resource;
    }

    protected ResourceLocation getResource() {
        return resource;
    }

    public interface IInfoHandler {

        List<ITextComponent> getInfo();
    }
}