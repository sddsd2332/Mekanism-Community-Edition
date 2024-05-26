package mekanism.client.gui;

import mekanism.client.gui.element.*;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge.Type;
import mekanism.client.gui.element.gauge.GuiGauge.TypeColor;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.client.gui.element.tab.GuiUpgradeTab;
import mekanism.common.inventory.container.ContainerAmbientAccumulatorEnergy;
import mekanism.common.tile.machine.TileEntityAmbientAccumulatorEnergy;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class GuiAmbientAccumulatorEnergy extends GuiMekanismTile<TileEntityAmbientAccumulatorEnergy>{

    public GuiAmbientAccumulatorEnergy(InventoryPlayer inventory, TileEntityAmbientAccumulatorEnergy tile) {
        super(tile, new ContainerAmbientAccumulatorEnergy(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiEnergyInfo(() -> {
            String usage = MekanismUtils.getEnergyDisplay(tileEntity.clientEnergyUsed);
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + usage + "/t",
                    LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, resource));
        addGuiElement(new GuiGasGauge(() -> tileEntity.outputTank, Type.WIDE, this, getGuiLocation(), 95, 13).withColor(TypeColor.ORANGE));
        addGuiElement(new GuiPowerBarLong(this, tileEntity, resource, 165, 9));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.POWER, this, resource, 135, 66).with(GuiSlot.SlotOverlay.POWER));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.OUTPUT, this, resource, 102, 66).with(GuiSlot.SlotOverlay.PLUS));
        addGuiElement(new GuiInnerScreen(this, resource, 7, 13, 80, 65));
        addGuiElement(new GuiPlayerSlot(this, resource,7,88));
        ySize += 5;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 94) + 2, 0x404040);
        fontRenderer.drawString(LangUtils.localize("gui.dimensionId") + ":" + tileEntity.getWorld().provider.getDimension(), 8, 14, 0x33ff99);
        fontRenderer.drawString(LangUtils.localize("gui.dimensionName") + ":", 8, 23, 0x33ff99);
        fontRenderer.drawString(tileEntity.getWorld().provider.getDimensionType().getName(), 8, 32, 0x33ff99);
        if (tileEntity.getRecipe() != null){
            fontRenderer.drawString(LangUtils.localize("gui.dimensionGas") + ":", 8, 41, 0x33ff99);
            fontRenderer.drawString(tileEntity.getRecipe().getOutput().output.getGas().getLocalizedName(), 8, 50, 0x33ff99);
            float Chance = Math.round(tileEntity.getRecipe().getOutput().primaryChance * 100);
            fontRenderer.drawString(LangUtils.localize("gui.probability") + ":" + Chance + "%", 8, 59, 0x33ff99);
            fontRenderer.drawString(tileEntity.outputTank.getStored() + " / " + tileEntity.outputTank.getMaxGas(), 8, 68, 0x33ff99);
        }else {
            fontRenderer.drawString(LangUtils.localize("gui.dimensionNoGas"), 8, 41, 0x33ff99);
            fontRenderer.drawString(tileEntity.outputTank.getStored() + " / " + tileEntity.outputTank.getMaxGas(), 8, 50, 0x33ff99);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}
