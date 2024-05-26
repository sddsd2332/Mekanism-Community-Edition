package mekanism.client.gui.element.tab;

import mekanism.api.TileNetworkList;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiTileEntityElement;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSortingTab extends GuiTileEntityElement<TileEntityFactory> {

    public GuiSortingTab(IGuiWrapper gui, TileEntityFactory tile, ResourceLocation def) {
        super(gui, def, tile, -26, 58, 26, 54, -21, 62, 18, 18);
    }


    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        super.renderBackground(xAxis, yAxis, guiWidth, guiHeight);
        mc.renderEngine.bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.BUTTON_TAB, "button_tab_icon.png"));
        guiObj.drawTexturedRect(guiWidth - 21, guiHeight + 62, 216, 18, 18, 18);
        mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "State.png"));
        guiObj.drawTexturedRect(guiWidth - 22, guiHeight + 81, 6, 6, 8, 8);
        guiObj.drawTexturedRect(guiWidth - 21, guiHeight + 82, tileEntity.sorting ? 0 : 6, 0, 6, 6);
        mc.renderEngine.bindTexture(defaultLocation);
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {
        if (inBounds(xAxis, yAxis)) {
            List<String> info = new ArrayList<>();
            info.add(LangUtils.localize("gui.factory.autoSort") + ":" + LangUtils.transOnOff(tileEntity.sorting));
            if (tileEntity.Factoryoldsorting) {
                info.add(LangUtils.localize("gui.factory.autoSort.isold"));
            }
            displayTooltips(info, xAxis, yAxis);
        }
        mc.renderEngine.bindTexture(defaultLocation);
        MekanismRenderer.resetColor();
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {
    }

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {
        if (button == 0 && inBounds(xAxis, yAxis)) {
            TileNetworkList data = TileNetworkList.withContents(0);
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, data));
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
        }
    }
}
