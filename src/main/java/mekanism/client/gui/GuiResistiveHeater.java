package mekanism.client.gui;

import mekanism.api.TileNetworkList;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.ContainerResistiveHeater;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.tile.TileEntityResistiveHeater;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import mekanism.common.util.UnitDisplayUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
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
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiResistiveHeater extends GuiMekanismTile<TileEntityResistiveHeater> {

    private GuiTextField energyUsageField;
    private GuiDisableableButton checkboxButton;

    public GuiResistiveHeater(InventoryPlayer inventory, TileEntityResistiveHeater tile) {
        super(tile, new ContainerResistiveHeater(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiPowerBar(this, tileEntity, resource, 164, 15));
        addGuiElement(new GuiSlot(SlotType.POWER, this, resource, 14, 34).with(SlotOverlay.POWER));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiEnergyInfo(() -> {
            String multiplier = MekanismUtils.getEnergyDisplay(tileEntity.energyUsage);
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + multiplier + "/t",
                    LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, resource));
        addGuiElement(new GuiHeatInfo(() -> {
            TemperatureUnit unit = TemperatureUnit.values()[MekanismConfig.current().general.tempUnit.val().ordinal()];
            String environment = UnitDisplayUtils.getDisplayShort(tileEntity.lastEnvironmentLoss * unit.intervalSize, false, unit);
            return Collections.singletonList(LangUtils.localize("gui.dissipated") + ": " + environment + "/t");
        }, this, resource));
        addGuiElement(new GuiRateBar(this, new GuiRateBar.IRateInfoHandler() {
            @Override
            public String getTooltip() {
                return LangUtils.localize("gui.temp") + ": " + getTemp();
            }

            @Override
            public double getLevel() {
                return Math.min(1, tileEntity.temperature / MekanismConfig.current().general.evaporationMaxTemp.val());
            }
        }, resource, 153, 13));
        addGuiElement(new GuiPlayerSlot(this, resource));
        addGuiElement(new GuiInnerScreen(this, getGuiLocation(), 48, 23, 80, 40));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        String prevEnergyUsage = energyUsageField != null ? energyUsageField.getText() : "";
        energyUsageField = new GuiTextField(0, fontRenderer, guiLeft + 61, guiTop + 52, 54, 11);
        energyUsageField.setMaxStringLength(7);
        energyUsageField.setEnableBackgroundDrawing(false);
        energyUsageField.setText(prevEnergyUsage);
        energyUsageField.setTextColor(0xFF3CFE9A);
        buttonList.add(checkboxButton = new GuiDisableableButton(1, guiLeft + 115, guiTop + 50, 11, 11).with(GuiDisableableButton.ImageOverlay.CHECKMARK_DIGITAL));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == checkboxButton.id) {
            setEnergyUsage();
        }
    }

    private void updateEnabledButtons() {
        checkboxButton.enabled = !energyUsageField.getText().isEmpty();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 94) + 2, 0x404040);
        renderScaledText(LangUtils.localize("gui.temp") + ": " + getTemp(), 50, 25, 0xFF3CFE9A, 76);
        renderScaledText(LangUtils.localize("gui.usage") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.energyUsage) + "/t", 50, 41, 0xFF3CFE9A, 76);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= -21 && xAxis <= -3 && yAxis >= 90 && yAxis <= 108) {
            List<String> info = new ArrayList<>();
            boolean energy = tileEntity.getEnergy() == 0;
            if (energy) {
                info.add(LangUtils.localize("gui.no_energy"));
            }
            if (energy) {
                this.olddisplayTooltips(info, xAxis, yAxis);
            }
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        energyUsageField.drawTextBox();
        MekanismRenderer.resetColor();
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.SWITCH, "switch_icon.png"));
        drawTexturedModalRect(guiLeft + 53, guiTop + 53, 43, 0, 4, 7);
        boolean energy = tileEntity.getEnergy() == 0;
        if (energy) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 86, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), true));
        }
    }

    private void setEnergyUsage() {
        if (!energyUsageField.getText().isEmpty()) {
            int toUse = Integer.parseInt(energyUsageField.getText());
            TileNetworkList data = TileNetworkList.withContents(toUse);
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, data));
            energyUsageField.setText("");
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        energyUsageField.updateCursorCounter();
        updateEnabledButtons();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);
        energyUsageField.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public void keyTyped(char c, int i) throws IOException {
        if (!energyUsageField.isFocused() || i == Keyboard.KEY_ESCAPE) {
            super.keyTyped(c, i);
        }
        if (energyUsageField.isFocused() && i == Keyboard.KEY_RETURN) {
            setEnergyUsage();
            return;
        }
        if (Character.isDigit(c) || isTextboxKey(c, i)) {
            energyUsageField.textboxKeyTyped(c, i);
        }
    }

    private String getTemp() {
        return MekanismUtils.getTemperatureDisplay(tileEntity.getTemp(), TemperatureUnit.AMBIENT);
    }
}
