package mekanism.generators.client.gui;

import mekanism.api.EnumColor;
import mekanism.api.TileNetworkList;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.inventory.container.ContainerNull;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.generators.client.gui.button.GuiReactorLogicButton;
import mekanism.generators.common.tile.reactor.TileEntityReactorLogicAdapter;
import mekanism.generators.common.tile.reactor.TileEntityReactorLogicAdapter.ReactorLogic;
import mekanism.generators.common.util.MekanismGeneratorUtils;
import mekanism.generators.common.util.MekanismGeneratorUtils.ResourceType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiReactorLogicAdapter extends GuiMekanismTile<TileEntityReactorLogicAdapter> {

    private List<GuiReactorLogicButton> typeButtons = new ArrayList<>();
    private int buttonID = 0;

    public GuiReactorLogicAdapter(InventoryPlayer inventory, final TileEntityReactorLogicAdapter tile) {
        super(tile, new ContainerNull(inventory.player, tile));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        for (ReactorLogic type : ReactorLogic.values()) {
            int typeShift = 22 * type.ordinal();
            GuiReactorLogicButton button = new GuiReactorLogicButton(buttonID++, guiLeft + 24, guiTop + 32 + typeShift, type, tileEntity, getGuiLocation());
            buttonList.add(button);
            typeButtons.add(button);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        for (GuiReactorLogicButton button : typeButtons) {
            if (guibutton.id == button.id) {
                Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(1, button.getType().ordinal())));
                break;
            }
        }
    }

    protected boolean inBounds(int xAxis, int yAxis) {
        return xAxis > 23 && xAxis < 33 && yAxis > 19 && yAxis < 29;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 6, 0x404040);
        renderScaledText(LangUtils.localize("gui.coolingMeasurements") + ": " + EnumColor.RED + LangUtils.transOnOff(tileEntity.activeCooled), 36, 20, 0x404040, 117);
        renderScaledText(LangUtils.localize("gui.redstoneOutputMode") + ": " + EnumColor.RED + tileEntity.logicType.getLocalizedName(), 23, 123, 0x404040, 130);
        String text = LangUtils.localize("gui.status") + ": " + EnumColor.RED + LangUtils.localize("gui." + (tileEntity.checkMode() ? "outputting" : "idle"));
        fontRenderer.drawString(text, (xSize / 2) - (fontRenderer.getStringWidth(text) / 2), 136, 0x404040);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        for (GuiReactorLogicButton button : typeButtons) {
            ReactorLogic type = button.getType();
            int typeOffset = 22 * type.ordinal();
            renderItem(type.getRenderStack(), 27, 35 + typeOffset);
            fontRenderer.drawString(EnumColor.WHITE + type.getLocalizedName(), 46, 34 + typeOffset, 0x404040);
            if (button.isMouseOver()) {
                this.olddisplayTooltips(MekanismUtils.splitTooltip(type.getDescription(), ItemStack.EMPTY), xAxis, yAxis);
            }
        }
        if (inBounds(xAxis, yAxis)) {
            this.olddisplayTooltip(LangUtils.localize("gui.toggleCooling"), xAxis, yAxis);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        ResourceLocation resource = getGuiLocation();
        mc.renderEngine.bindTexture(resource);
        int activecoolingButton = 176;
        if (tileEntity.activeCooled) {
            activecoolingButton += 11;
        }
        drawTexturedModalRect(guiLeft + 23, guiTop + 19, activecoolingButton, inBounds(xAxis, yAxis) ? 0 : 11, 11, 11);
    }

    @Override
    protected ResourceLocation getGuiLocation() {
        return MekanismGeneratorUtils.getResource(ResourceType.GUI, "GuiReactorLogicAdapter.png");
    }

    @Override
    public void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);
        int xAxis = x - guiLeft;
        int yAxis = y - guiTop;
        if (inBounds(xAxis, yAxis)) {
            TileNetworkList data = TileNetworkList.withContents(0);
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, data));
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
        }
    }

}
