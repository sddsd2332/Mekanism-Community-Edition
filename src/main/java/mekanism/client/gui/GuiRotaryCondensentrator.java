package mekanism.client.gui;

import mekanism.api.TileNetworkList;
import mekanism.api.util.time.Timeticks;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.GuiProgress.IProgressInfoHandler;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.client.gui.element.tab.GuiTransporterConfigTab;
import mekanism.client.gui.element.tab.GuiUpgradeTab;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.inventory.container.ContainerRotaryCondensentrator;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.tile.machine.TileEntityRotaryCondensentrator;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiRotaryCondensentrator extends GuiMekanismTile<TileEntityRotaryCondensentrator> {

    protected Timeticks time;

    public GuiDisableableButton mode;

    public GuiRotaryCondensentrator(InventoryPlayer inventory, TileEntityRotaryCondensentrator tile) {
        super(tile, new ContainerRotaryCondensentrator(inventory, tile));
        time = new Timeticks(20, 20, false);
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiTransporterConfigTab(this, 34, tileEntity, resource));
        addGuiElement(new GuiSlot(SlotType.INPUT, this, resource, 4, 24).with(SlotOverlay.PLUS));
        addGuiElement(new GuiSlot(SlotType.OUTPUT, this, resource, 4, 55).with(SlotOverlay.MINUS));
        addGuiElement(new GuiSlot(SlotType.INPUT, this, resource, 154, 24));
        addGuiElement(new GuiSlot(SlotType.OUTPUT, this, resource, 154, 55));
        addGuiElement(new GuiSlot(SlotType.POWER, this, resource, 154, 4).with(SlotOverlay.POWER));
        addGuiElement(new GuiPowerBarHorizontal(this, tileEntity, resource, 115 - 2, 74));
        addGuiElement(new GuiEnergyInfo(() -> {
            String usage = MekanismUtils.getEnergyDisplay(tileEntity.clientEnergyUsed);
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + usage + "/t",
                    LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, resource));

        addGuiElement(new GuiFluidGauge(() -> tileEntity.fluidTank, GuiGauge.Type.STANDARD, this, resource, 133, 13).withColor(GuiGauge.TypeColor.RED));
        addGuiElement(new GuiGasGauge(() -> tileEntity.gasTank, GuiGauge.Type.STANDARD, this, resource, 25, 13).withColor(GuiGauge.TypeColor.RED));
        addGuiElement(new GuiProgress(new IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return tileEntity.getActive() ? (double) time.getValue() / 20F : 0;
            }

            @Override
            public boolean isActive() {
                return tileEntity.mode == 0;
            }
        }, ProgressBar.LARGE_RIGHT, this, resource, 62, 38));
        addGuiElement(new GuiProgress(new IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return tileEntity.getActive() ? (double) time.getValue() / 20F : 0;
            }

            @Override
            public boolean isActive() {
                return tileEntity.mode == 1;
            }
        }, ProgressBar.LARGE_LEFT, this, resource, 62, 38));
        addGuiElement(new GuiPlayerSlot(this, resource));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(mode = new GuiDisableableButton(0, guiLeft + 4, guiTop + 4, 18, 18, () -> tileEntity.mode).with(GuiDisableableButton.ImageOverlay.TOGGLE));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(tileEntity.mode == 0 ? LangUtils.localize("gui.condensentrating")
                : LangUtils.localize("gui.decondensentrating"), 6, (ySize - 94) + 2, 0x404040);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (mode.isMouseOver()) {
            displayTooltip(LangUtils.localize("gui.rotaryCondensentrator.toggleOperation"), xAxis, yAxis);
        } else if (xAxis >= 116 && xAxis <= 168 && yAxis >= 76 && yAxis <= 80) {
            displayTooltip(MekanismUtils.getEnergyDisplay(tileEntity.getEnergy(), tileEntity.getMaxEnergy()), xAxis, yAxis);
        } else if (xAxis >= -21 && xAxis <= -3 && yAxis >= 116 && yAxis <= 134) {
            List<String> info = new ArrayList<>();
            boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
            boolean gas = tileEntity.gasTank.getStored() == tileEntity.gasTank.getMaxGas() && tileEntity.mode == 1;
            boolean fluid = tileEntity.fluidTank.getFluidAmount() == tileEntity.fluidTank.getCapacity() && tileEntity.mode == 0;
            if (energy) {
                info.add(LangUtils.localize("gui.no_energy"));
            }
            if (gas) {
                info.add(LangUtils.localize("gui.gas_no_space"));
            }
            if (fluid) {
                info.add(LangUtils.localize("gui.fluid_no_space"));
            }
            if (energy || gas || fluid) {
                displayTooltips(info, xAxis, yAxis);
            }
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
        boolean gas = tileEntity.gasTank.getStored() == tileEntity.gasTank.getMaxGas() && tileEntity.mode == 1;
        boolean fluid = tileEntity.fluidTank.getFluidAmount() == tileEntity.fluidTank.getCapacity() && tileEntity.mode == 0;
        if (gas) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Warning.png"));
            drawTexturedModalRect(guiLeft + 25 + 9, guiTop + 13 + 1, 9, 1, 8, 29);
            drawTexturedModalRect(guiLeft + 25 + 9, guiTop + 13 + 31, 9, 32, 8, 28);
        }
        if (fluid) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Warning.png"));
            drawTexturedModalRect(guiLeft + 133 + 9, guiTop + 13 + 1, 9, 1, 8, 29);
            drawTexturedModalRect(guiLeft + 133 + 9, guiTop + 13 + 31, 9, 32, 8, 28);
        }
        if (energy || gas || fluid) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 112, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), false));
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == mode.id) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(0)));
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
        }
    }
}
