package mekanism.generators.client.gui;

import mekanism.api.Coord4D;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.common.Mekanism;
import mekanism.common.network.PacketSimpleGui.SimpleGuiMessage;
import mekanism.generators.common.tile.reactor.TileEntityReactorController;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public abstract class GuiReactorInfo extends GuiMekanismTile<TileEntityReactorController> {

    private GuiButton backButton;

    public GuiReactorInfo(TileEntityReactorController tile, Container container) {
        super(tile, container);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(backButton = new GuiDisableableButton(0, guiLeft + 6, guiTop + 6, 14, 14).with(GuiDisableableButton.ImageOverlay.BACK));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == backButton.id) {
            Mekanism.packetHandler.sendToServer(new SimpleGuiMessage(Coord4D.get(tileEntity), 1, 10));
        }
    }

}
