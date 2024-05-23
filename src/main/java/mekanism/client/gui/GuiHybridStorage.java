package mekanism.client.gui;


import mekanism.client.gui.element.*;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.common.inventory.container.ContainerHybridStorage;
import mekanism.common.tile.TileEntityHybridStorage;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class GuiHybridStorage extends GuiMekanismTile<TileEntityHybridStorage> {

    public GuiHybridStorage(InventoryPlayer inventory, TileEntityHybridStorage tile) {
        super(tile, new ContainerHybridStorage(inventory, tile));
        xSize += 108;
        ySize += 117;
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource, 108, 0));
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource,108,89));
        addGuiElement(new GuiContainerEditMode(this, tileEntity, resource,108,62));
        addGuiElement(new GuiPlayerSlot(this, resource, 61, 200));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiGasGauge(() -> tileEntity.gasTank1, GuiGauge.Type.STANDARD, this, resource, 7, 197).withColor(GuiGauge.TypeColor.BLUE));
        addGuiElement(new GuiGasGauge(() -> tileEntity.gasTank2, GuiGauge.Type.STANDARD, this, resource, 34, 197).withColor(GuiGauge.TypeColor.ORANGE));
        addGuiElement(new GuiFluidGauge(() -> tileEntity.fluidTank, GuiGauge.Type.STANDARD, this, resource, 232, 197).withColor(GuiGauge.TypeColor.YELLOW));
        addGuiElement(new GuiEnergyGauge(() -> tileEntity, GuiEnergyGauge.Type.STANDARD, this, resource, 259, 197).withColor(GuiGauge.TypeColor.AQUA));
        addGuiElement(new GuiEnergyInfo(() -> Arrays.asList(LangUtils.localize("gui.storing") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getEnergy(), tileEntity.getMaxEnergy()),
                LangUtils.localize("gui.maxOutput") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxOutput()) + "/t"), this, resource));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.INPUT, this, resource, 7, 178).with(GuiSlot.SlotOverlay.PLUS));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.OUTPUT, this, resource, 7, 258).with(GuiSlot.SlotOverlay.MINUS));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.INPUT, this, resource, 34, 178).with(GuiSlot.SlotOverlay.PLUS));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.OUTPUT, this, resource, 34, 258).with(GuiSlot.SlotOverlay.MINUS));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, resource, 232, 178).with(GuiSlot.SlotOverlay.INPUT));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, resource, 232, 258).with(GuiSlot.SlotOverlay.OUTPUT));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.INPUT, this, resource, 259, 178).with(GuiSlot.SlotOverlay.MINUS));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.OUTPUT, this, resource, 259, 258).with(GuiSlot.SlotOverlay.PLUS));

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 15; x++) {
                addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, resource, 7 + x * 18, 25 + y * 18));
            }
        }

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 61, (ySize - 96) + 2, 0x404040);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}
