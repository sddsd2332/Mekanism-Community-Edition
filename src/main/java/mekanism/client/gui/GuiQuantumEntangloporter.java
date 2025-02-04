package mekanism.client.gui;

import mekanism.api.EnumColor;
import mekanism.api.TileNetworkList;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.client.gui.element.tab.GuiTransporterConfigTab;
import mekanism.client.gui.element.tab.GuiUpgradeTab;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
import mekanism.common.config.MekanismConfig;
import mekanism.common.frequency.Frequency;
import mekanism.common.frequency.FrequencyManager;
import mekanism.common.inventory.container.ContainerQuantumEntangloporter;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.tile.TileEntityQuantumEntangloporter;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SideOnly(Side.CLIENT)
public class GuiQuantumEntangloporter extends GuiMekanismTile<TileEntityQuantumEntangloporter> {

    private GuiDisableableButton publicButton;
    private GuiDisableableButton privateButton;
    private GuiDisableableButton setButton;
    private GuiDisableableButton deleteButton;
    private GuiDisableableButton checkboxButton;
    private GuiScrollList scrollList;
    private GuiTextField frequencyField;
    private boolean privateMode;

    private Frequency clientFreq;

    private int yStart;

    public GuiQuantumEntangloporter(InventoryPlayer inventory, TileEntityQuantumEntangloporter tile) {
        super(tile, new ContainerQuantumEntangloporter(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(scrollList = new GuiScrollList(this, resource, 28, 37, 120, 4));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiTransporterConfigTab(this, 34, tileEntity, resource));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiEnergyInfo(() -> Arrays.asList(LangUtils.localize("gui.storing") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getEnergy(), tileEntity.getMaxEnergy()),
                LangUtils.localize("gui.maxOutput") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxOutput()) + "/t"), this, resource));
        addGuiElement(new GuiHeatInfo(() -> {
            UnitDisplayUtils.TemperatureUnit unit = UnitDisplayUtils.TemperatureUnit.values()[MekanismConfig.current().general.tempUnit.val().ordinal()];
            String transfer = UnitDisplayUtils.getDisplayShort(tileEntity.lastTransferLoss, false, unit);
            String environment = UnitDisplayUtils.getDisplayShort(tileEntity.lastEnvironmentLoss, false, unit);
            return Arrays.asList(LangUtils.localize("gui.transferred") + ": " + transfer + "/t", LangUtils.localize("gui.dissipated") + ": " + environment + "/t");
        }, this, resource));
        if (tileEntity.frequency != null) {
            privateMode = !tileEntity.frequency.publicFreq;
        }
        addGuiElement(new GuiPlayerSlot(this, resource, 7, 157));
        addGuiElement(new GuiElementScreen(this, getGuiLocation(), 27, 36, 122, 42).isFrame());
        addGuiElement(new GuiInnerScreen(this, getGuiLocation(), 48, 111, 101, 13));
        ySize += 74;
        yStart = 14;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(publicButton = new GuiDisableableButton(0, guiLeft + 27, guiTop + yStart, 60, 20, LangUtils.localize("gui.public")));
        buttonList.add(privateButton = new GuiDisableableButton(1, guiLeft + 89, guiTop + yStart, 60, 20, LangUtils.localize("gui.private")));
        buttonList.add(setButton = new GuiDisableableButton(2, guiLeft + 27, guiTop + yStart + 113, 60, 20, LangUtils.localize("gui.set")));
        buttonList.add(deleteButton = new GuiDisableableButton(3, guiLeft + 89, guiTop + yStart + 113, 60, 20, LangUtils.localize("gui.delete")));
        frequencyField = new GuiTextField(4, fontRenderer, guiLeft + 50, guiTop + yStart + 99, 98, 11);
        frequencyField.setMaxStringLength(FrequencyManager.MAX_FREQ_LENGTH);
        frequencyField.setEnableBackgroundDrawing(false);
        buttonList.add(checkboxButton = new GuiDisableableButton(5, guiLeft + 137, guiTop + yStart + 98, 11, 11).with(GuiDisableableButton.ImageOverlay.CHECKMARK));
        updateButtons();
    }

    public String getSecurity(Frequency freq) {
        return !freq.publicFreq ? EnumColor.DARK_RED + LangUtils.localize("gui.private") : EnumColor.BRIGHT_GREEN + LangUtils.localize("gui.public");
    }

    public void updateButtons() {
        if (tileEntity.getSecurity().getClientOwner() == null) {
            return;
        }
        List<String> text = new ArrayList<>();
        if (privateMode) {
            for (Frequency freq : tileEntity.privateCache) {
                text.add(freq.name);
            }
        } else {
            for (Frequency freq : tileEntity.publicCache) {
                text.add(freq.name + " (" + freq.clientOwner + ")");
            }
        }
        scrollList.setText(text);
        if (privateMode) {
            publicButton.enabled = true;
            privateButton.enabled = false;
        } else {
            publicButton.enabled = false;
            privateButton.enabled = true;
        }
        if (scrollList.hasSelection()) {
            Frequency freq = privateMode ? tileEntity.privateCache.get(scrollList.getSelection()) : tileEntity.publicCache.get(scrollList.getSelection());
            setButton.enabled = tileEntity.getFrequency(null) == null || !tileEntity.getFrequency(null).equals(freq);
            deleteButton.enabled = tileEntity.getSecurity().getOwnerUUID().equals(freq.ownerUUID);
        } else {
            setButton.enabled = false;
            deleteButton.enabled = false;
        }
        checkboxButton.enabled = !frequencyField.getText().isEmpty();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        updateButtons();
        frequencyField.updateCursorCounter();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);
        updateButtons();
        frequencyField.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public void keyTyped(char c, int i) throws IOException {
        if (!frequencyField.isFocused() || i == Keyboard.KEY_ESCAPE) {
            super.keyTyped(c, i);
        }
        if (i == Keyboard.KEY_RETURN) {
            if (frequencyField.isFocused()) {
                setFrequency(frequencyField.getText());
                frequencyField.setText("");
            }
        }
        if (Character.isDigit(c) || Character.isLetter(c) || isTextboxKey(c, i) || FrequencyManager.SPECIAL_CHARS.contains(c)) {
            frequencyField.textboxKeyTyped(c, i);
        }
        updateButtons();
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == publicButton.id) {
            privateMode = false;
        } else if (guibutton.id == privateButton.id) {
            privateMode = true;
        } else if (guibutton.id == setButton.id) {
            int selection = scrollList.getSelection();
            if (selection != -1) {
                Frequency freq = privateMode ? tileEntity.privateCache.get(selection) : tileEntity.publicCache.get(selection);
                setFrequency(freq.name);
            }
        } else if (guibutton.id == deleteButton.id) {
            int selection = scrollList.getSelection();
            if (selection != -1) {
                Frequency freq = privateMode ? tileEntity.privateCache.get(selection) : tileEntity.publicCache.get(selection);
                TileNetworkList data = TileNetworkList.withContents(1, freq.name, freq.publicFreq);
                Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, data));
                scrollList.clearSelection();
            }
        } else if (guibutton.id == checkboxButton.id) {
            setFrequency(frequencyField.getText());
            frequencyField.setText("");
        }
        updateButtons();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 4, 0x404040);
        Frequency frequency = tileEntity.getFrequency(null);
        fontRenderer.drawString(LangUtils.localize("gui.owner") + ": " + (getFrequency() != null ? getOwnerUsername() : EnumColor.DARK_RED + LangUtils.localize("gui.none")), 27, yStart + 77, 0x404040);

        fontRenderer.drawString(LangUtils.localize("gui.freq") + ":", 27, yStart + 67, 0x404040);
        fontRenderer.drawString(" " + (frequency != null ? frequency.name : EnumColor.DARK_RED + LangUtils.localize("gui.none")), 27 + fontRenderer.getStringWidth(LangUtils.localize("gui.freq") + ":"), yStart + 67, 0x797979);

        fontRenderer.drawString(LangUtils.localize("gui.security") + ":", 27, yStart + 87, 0x404040);
        fontRenderer.drawString(" " + (frequency != null ? getSecurity(frequency) : EnumColor.DARK_RED + LangUtils.localize("gui.none")), 27 + fontRenderer.getStringWidth(LangUtils.localize("gui.security") + ":"), yStart + 87, 0x797979);

        String str = LangUtils.localize("gui.set") + ":";
        renderScaledText(str, 27, yStart + 100, 0x404040, 20);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= -21 && xAxis <= -3 && yAxis >= 90 && yAxis <= 108) {
            List<String> info = new ArrayList<>();
            boolean freq = frequency != null;
            if (!freq) {
                info.add(LangUtils.localize("gui.no_freq"));
            }
            if (!freq) {
                this.displayTooltips(info, xAxis, yAxis);
            }
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        frequencyField.drawTextBox();
        MekanismRenderer.resetColor();
        Frequency frequency = tileEntity.getFrequency(null);
        boolean freq = frequency != null;
        if (!freq) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 86, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), true));
        }
    }

    private Frequency getFrequency() {
        return tileEntity != null ? tileEntity.frequency : clientFreq;
    }

    public void setFrequency(String freq) {
        if (freq.isEmpty()) {
            return;
        }
        TileNetworkList data = TileNetworkList.withContents(0, freq, !privateMode);
        Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, data));
    }

    private String getOwnerUsername() {
        if (privateMode) {
            return EnumColor.BRIGHT_GREEN + tileEntity.getSecurity().getClientOwner();
        } else {
            return (Objects.equals(tileEntity.getSecurity().getClientOwner(), tileEntity.frequency.clientOwner) ? EnumColor.BRIGHT_GREEN : EnumColor.DARK_RED) + tileEntity.frequency.clientOwner;
        }
    }

}
