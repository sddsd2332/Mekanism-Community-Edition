package mekanism.client.gui;

import mekanism.client.gui.element.*;
import mekanism.common.inventory.container.ContainerModificationStation;
import mekanism.common.tile.TileEntityModificationStation;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class GuiModificationStation extends GuiMekanismTile<TileEntityModificationStation> {

    public GuiModificationStation(InventoryPlayer inventory, TileEntityModificationStation tile) {
        super(tile, new ContainerModificationStation(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiPowerBar(this, tileEntity, resource, 164, 15));
        addGuiElement(new GuiEnergyInfo(() -> {
            String multiplier = MekanismUtils.getEnergyDisplay(tileEntity.energyPerTick);
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + multiplier + "/t",
                    LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, resource));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.INPUT, this, resource, 25, 33).with(GuiSlot.SlotOverlay.MODULE));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.POWER, this, resource, 140, 33).with(GuiSlot.SlotOverlay.POWER));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.OUTPUT, this, resource, 115, 33));
        addGuiElement(new GuiProgress(new GuiProgress.IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return tileEntity.getScaledProgress();
            }
        }, GuiProgress.ProgressBar.LARGE_RIGHT, this, resource, 53, 37));
        addGuiElement(new GuiPlayerSlot(this, resource));
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}
