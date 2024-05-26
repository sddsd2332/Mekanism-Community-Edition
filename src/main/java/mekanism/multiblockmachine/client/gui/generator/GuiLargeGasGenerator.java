package mekanism.multiblockmachine.client.gui.generator;

import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.multiblockmachine.common.inventory.container.generator.ContainerLargeGasGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeGasGenerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class GuiLargeGasGenerator extends GuiMekanismTile<TileEntityLargeGasGenerator> {

    public GuiLargeGasGenerator(InventoryPlayer inventory, TileEntityLargeGasGenerator tile) {
        super(tile, new ContainerLargeGasGenerator(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiEnergyInfo(() -> Arrays.asList(
                LangUtils.localize("gui.producing") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.generationRate * tileEntity.clientUsed) + "/t",
                LangUtils.localize("gui.maxOutput") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxOutput()) + "/t"), this, resource));
        addGuiElement(new GuiGasGauge(() -> tileEntity.fuelTank, GuiGauge.Type.WIDE, this, resource, 55, 18));
        addGuiElement(new GuiPowerBar(this, tileEntity, resource, 164, 15));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, resource, 16, 34).with(GuiSlot.SlotOverlay.MINUS));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.POWER, this, resource, 142, 34).with(GuiSlot.SlotOverlay.POWER));
        addGuiElement(new GuiPlayerSlot(this, getGuiLocation()));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 6, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        String s = LangUtils.localize("gui.burnRate") + ": " + tileEntity.clientUsed;
        fontRenderer.drawString(s, xSize - 8 - fontRenderer.getStringWidth(s), (ySize - 96) + 2, 0x404040);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}
