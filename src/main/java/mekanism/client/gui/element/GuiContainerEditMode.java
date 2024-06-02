package mekanism.client.gui.element;

import mekanism.api.Coord4D;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.base.IFluidContainerManager;
import mekanism.common.network.PacketContainerEditMode.ContainerEditModeMessage;
import mekanism.common.tile.prefab.TileEntityContainerBlock;
import mekanism.common.util.FluidContainerUtils.ContainerEditMode;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiContainerEditMode extends GuiTileEntityElement<TileEntityContainerBlock> {

    private final int xLocation;
    private final int yLocation;

    public GuiContainerEditMode(IGuiWrapper gui, TileEntityContainerBlock tile, ResourceLocation def, int x, int y) {
        super(gui, def, tile, 176 + x, 138 + y, 26, 26,179 + x, 142 +y, 18, 18);
        xLocation = x;
        yLocation = y;
    }

    public GuiContainerEditMode(IGuiWrapper gui, TileEntityContainerBlock tile, ResourceLocation def) {
        super(gui, def, tile, 176, 138, 26, 26, 179, 142, 18, 18);
        xLocation = 0;
        yLocation = 0;
    }


    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        MekanismRenderer.color(0xFF366BD0);
        super.renderBackground(xAxis, yAxis, guiWidth, guiHeight);
        MekanismRenderer.resetColor();
        IFluidContainerManager control = (IFluidContainerManager) tileEntity;
        int renderX = 72 + (18 * control.getContainerEditMode().ordinal());
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.BUTTON_TAB, "button_tab_icon.png"));
        guiObj.drawTexturedRect(guiWidth + 179 + xLocation, guiHeight + 142 + yLocation, renderX, 0, 18, 18);
        mc.renderEngine.bindTexture(defaultLocation);
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {
        mc.renderEngine.bindTexture(RESOURCE);
        if (inBounds(xAxis, yAxis)) {
            displayTooltip(((IFluidContainerManager) tileEntity).getContainerEditMode().getDisplay(), xAxis, yAxis);
        }
        mc.renderEngine.bindTexture(defaultLocation);
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {
    }

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {
        IFluidContainerManager manager = (IFluidContainerManager) tileEntity;
        if (button == 0 && inBounds(xAxis, yAxis)) {
            ContainerEditMode current = manager.getContainerEditMode();
            int ordinalToSet = current.ordinal() < (ContainerEditMode.values().length - 1) ? current.ordinal() + 1 : 0;
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
            Mekanism.packetHandler.sendToServer(new ContainerEditModeMessage(Coord4D.get(tileEntity), ContainerEditMode.values()[ordinalToSet]));
        }
    }
}
