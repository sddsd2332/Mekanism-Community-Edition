package mekanism.client.gui;

import mekanism.api.EnumColor;
import mekanism.api.TileNetworkList;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.GuiElementScreen;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.GuiPlayerSlot;
import mekanism.client.gui.element.GuiScrollList;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
import mekanism.common.inventory.container.ContainerSecurityDesk;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.security.ISecurityTile.SecurityMode;
import mekanism.common.tile.TileEntitySecurityDesk;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSecurityDesk extends GuiMekanismTile<TileEntitySecurityDesk> {

    private static final List<Character> SPECIAL_CHARS = Arrays.asList('-', '|', '_');
    private static final int MAX_LENGTH = 24;
    private GuiDisableableButton removeButton;
    private GuiDisableableButton publicButton;
    private GuiDisableableButton privateButton;
    private GuiDisableableButton trustedButton;
    private GuiDisableableButton checkboxButton;
    private GuiDisableableButton overrideButton;
    private GuiScrollList scrollList;
    private GuiTextField trustedField;

    public GuiSecurityDesk(InventoryPlayer inventory, TileEntitySecurityDesk tile) {
        super(tile, new ContainerSecurityDesk(inventory, tile));
        addGuiElement(new GuiElementScreen(this, getGuiLocation(), 13, 13, 122, 42).isFrame());
        addGuiElement(scrollList = new GuiScrollList(this, getGuiLocation(), 14, 14, 120, 4));
        ySize += 64;
        addGuiElement(new GuiPlayerSlot(this, getGuiLocation(), 7, 147));
        addGuiElement(new GuiInnerScreen(this, getGuiLocation(), 34, 67, 101, 13));
        addGuiElement(new GuiElementScreen(this, getGuiLocation(), 141, 13, 26, 37));
        addGuiElement(new GuiElementScreen(this, getGuiLocation(), 141, 54, 26, 34));
        addGuiElement(new GuiElementScreen(this, getGuiLocation(), 141, 92, 26, 37));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(removeButton = new GuiDisableableButton(0, guiLeft + 13, guiTop + 81, 122, 20, LangUtils.localize("gui.remove")));
        trustedField = new GuiTextField(1, fontRenderer, guiLeft + 35, guiTop + 69, 101, 13);
        trustedField.setMaxStringLength(MAX_LENGTH);
        trustedField.setEnableBackgroundDrawing(false);
        buttonList.add(publicButton = new GuiDisableableButton(2, guiLeft + 13, guiTop + 113, 40, 16).with(GuiDisableableButton.ImageOverlay.PUBLIC));
        buttonList.add(privateButton = new GuiDisableableButton(3, guiLeft + 54, guiTop + 113, 40, 16).with(GuiDisableableButton.ImageOverlay.PRIVATE));
        buttonList.add(trustedButton = new GuiDisableableButton(4, guiLeft + 95, guiTop + 113, 40, 16).with(GuiDisableableButton.ImageOverlay.TRUSTED));
        buttonList.add(checkboxButton = new GuiDisableableButton(5, guiLeft + 123, guiTop + 68, 11, 11).with(GuiDisableableButton.ImageOverlay.CHECKMARK));
        buttonList.add(overrideButton = new GuiDisableableButton(6, guiLeft + 146, guiTop + 59, 16, 16).with(GuiDisableableButton.ImageOverlay.EXCLAMATION));
        updateButtons();
    }

    public void addTrusted(String trusted) {
        if (trusted.isEmpty()) {
            return;
        }
        TileNetworkList data = TileNetworkList.withContents(0, trusted);
        Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, data));
    }

    public void updateButtons() {
        if (tileEntity.ownerUUID != null) {
            List<String> text = new ArrayList<>();
            if (tileEntity.frequency != null) {
                for (String s : tileEntity.frequency.trusted) {
                    text.add(s);
                }
            }
            scrollList.setText(text);
            removeButton.enabled = scrollList.hasSelection();
        }

        if (tileEntity.frequency != null && tileEntity.ownerUUID != null && tileEntity.ownerUUID.equals(mc.player.getUniqueID())) {
            publicButton.enabled = tileEntity.frequency.securityMode != SecurityMode.PUBLIC;
            privateButton.enabled = tileEntity.frequency.securityMode != SecurityMode.PRIVATE;
            trustedButton.enabled = tileEntity.frequency.securityMode != SecurityMode.TRUSTED;
            checkboxButton.enabled = !trustedField.getText().isEmpty();
            overrideButton.enabled = true;
        } else {
            publicButton.enabled = false;
            privateButton.enabled = false;
            trustedButton.enabled = false;
            checkboxButton.enabled = false;
            overrideButton.enabled = false;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        updateButtons();
        trustedField.updateCursorCounter();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);
        updateButtons();
        trustedField.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void keyTyped(char c, int i) throws IOException {
        if (!trustedField.isFocused() || i == Keyboard.KEY_ESCAPE) {
            super.keyTyped(c, i);
        }
        if (i == Keyboard.KEY_RETURN) {
            if (trustedField.isFocused()) {
                addTrusted(trustedField.getText());
                trustedField.setText("");
            }
        }
        if (SPECIAL_CHARS.contains(c) || Character.isDigit(c) || Character.isLetter(c) || isTextboxKey(c, i)) {
            trustedField.textboxKeyTyped(c, i);
        }
        updateButtons();
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == removeButton.id) {
            int selection = scrollList.getSelection();
            if (tileEntity.frequency != null && selection != -1) {
                TileNetworkList data = TileNetworkList.withContents(1, tileEntity.frequency.trusted.get(selection));
                Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, data));
                scrollList.clearSelection();
            }
        } else if (guibutton.id == publicButton.id) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(3, 0)));
        } else if (guibutton.id == privateButton.id) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(3, 1)));
        } else if (guibutton.id == trustedButton.id) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(3, 2)));
        } else if (guibutton.id == checkboxButton.id) {
            addTrusted(trustedField.getText());
            trustedField.setText("");
        } else if (guibutton.id == overrideButton.id) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(2)));
        }
        updateButtons();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String ownerText = tileEntity.clientOwner != null ? (LangUtils.localize("gui.owner") + ": " + tileEntity.clientOwner) : EnumColor.RED + LangUtils.localize("gui.noOwner");
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(ownerText, xSize - 7 - fontRenderer.getStringWidth(ownerText), (ySize - 96) + 2, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        String trusted = LangUtils.localize("gui.trustedPlayers");
        fontRenderer.drawString(trusted, 74 - (fontRenderer.getStringWidth(trusted) / 2), 57, 0x787878);
        String security = EnumColor.RED + LangUtils.localize("gui.securityOffline");
        if (tileEntity.frequency != null) {
            security = LangUtils.localize("gui.security") + ": " + tileEntity.frequency.securityMode.getDisplay();
        }
        fontRenderer.drawString(security, 13, 103, 0x404040);
        renderScaledText(LangUtils.localize("gui.add") + ":", 13, 70, 0x404040, 20);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (tileEntity.frequency != null && overrideButton.isMouseOver()) {
            this.displayTooltip(LangUtils.localize("gui.securityOverride") + ": " + LangUtils.transOnOff(tileEntity.frequency.override), xAxis, yAxis);
        } else if (publicButton.isMouseOver()) {
            this.displayTooltip(LangUtils.localize("gui.publicMode"), xAxis, yAxis);
        } else if (privateButton.isMouseOver()) {
            this.displayTooltip(LangUtils.localize("gui.privateMode"), xAxis, yAxis);
        } else if (trustedButton.isMouseOver()) {
            this.displayTooltip(LangUtils.localize("gui.trustedMode"), xAxis, yAxis);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);

        mc.getTextureManager().bindTexture(MekanismUtils.getResource(ResourceType.BUTTON, "Button_Icon.png"));
        drawTexturedModalRect(guiLeft + 151, guiTop + 37, 222, 23, 6, 7);
        drawTexturedModalRect(guiLeft + 151, guiTop + 117, 204, 24, 6, 6);

        mc.getTextureManager().bindTexture(MekanismUtils.getResource(ResourceType.GUI_BAR, "Base.png"));
        drawTexturedModalRect(guiLeft + 146, guiTop + 18, 3, 3, 16, 16);
        drawTexturedModalRect(guiLeft + 146, guiTop + 97, 3, 3, 16, 16);

        mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "State.png"));
        drawTexturedModalRect(guiLeft + 144, guiTop + 77, 6, 6, 8, 8);
        if (tileEntity.frequency != null && tileEntity.ownerUUID != null && tileEntity.ownerUUID.equals(mc.player.getUniqueID())) {
            drawTexturedModalRect(guiLeft + 145, guiTop + 78, tileEntity.frequency.override ? 0 : 6, 0, 6, 6);
        } else {
            drawTexturedModalRect(guiLeft + 145, guiTop + 78, 0, 6, 6, 6);
        }
        trustedField.drawTextBox();
        MekanismRenderer.resetColor();
    }
}
