package mekanism.client.gui.element;

import mekanism.client.gui.IGuiWrapper;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRecipeType extends GuiTileEntityElement<TileEntityFactory> {

    private final int xLocation;
    private final int yLocation;

    public GuiRecipeType(IGuiWrapper gui, TileEntityFactory tile, ResourceLocation def, int x, int y) {
        super(gui, def, tile, 176 + x, 70 + y, 26, 63);
        xLocation = x;
        yLocation = y;
    }


    @Override
    protected boolean inBounds(int xAxis, int yAxis) {
        return xAxis >= 180 + xLocation && xAxis <= 196 + xLocation && (yAxis >= 75 + yLocation && yAxis <= 91 + yLocation || yAxis >= 112 + yLocation && yAxis <= 128 + yLocation);
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        super.renderBackground(xAxis, yAxis, guiWidth, guiHeight);
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.SLOT, "Slot_Icon.png"));
        guiObj.drawTexturedRect(guiWidth + 179 + xLocation, guiHeight + 74 + yLocation, 36, 0, 18, 18);
        guiObj.drawTexturedRect(guiWidth + 179 + xLocation, guiHeight + 111 + yLocation, 72, 0, 18, 18);
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.BUTTON_TAB, "button_tab_icon.png"));
        guiObj.drawTexturedRect(guiWidth + 179 + xLocation, guiHeight + 74 + yLocation, 108, 18, 18, 18);
        guiObj.drawTexturedRect(guiWidth + 179 + xLocation, guiHeight + 111 + yLocation, 126, 18, 18, 18);
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.PROGRESS, "Progress_Icon.png"));
        guiObj.drawTexturedRect(guiWidth + 181 + xLocation, guiHeight + 94 + yLocation, 130, 114, 10, 15);
        int displayInt = tileEntity.getScaledRecipeProgress(15);
        guiObj.drawTexturedRect(guiWidth + 181 + xLocation, guiHeight + 94 + yLocation, 144, 114, 10, displayInt);
        mc.renderEngine.bindTexture(defaultLocation);
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {
        if (button == 0 && inBounds(xAxis, yAxis)) {
            offsetX(26);
        }
    }

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {
        if (button == 0 && inBounds(xAxis, yAxis)) {
            offsetX(-26);
        }
    }
}
