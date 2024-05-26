package mekanism.client.gui;

import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.GuiPlayerSlot;
import mekanism.client.gui.element.GuiRedstoneControl;
import mekanism.client.gui.element.GuiSlot;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.gauge.GuiGauge.Type;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.common.inventory.container.ContainerAmbientAccumulator;
import mekanism.common.tile.machine.TileEntityAmbientAccumulator;
import mekanism.common.util.LangUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAmbientAccumulator extends GuiMekanismTile<TileEntityAmbientAccumulator> {

    public GuiAmbientAccumulator(InventoryPlayer inventory, TileEntityAmbientAccumulator tile) {
        super(tile, new ContainerAmbientAccumulator(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiInnerScreen(this, resource, 7, 13, 80, 65));
        addGuiElement(new GuiGasGauge(() -> tileEntity.collectedGas, Type.WIDE, this, getGuiLocation(), 102, 13).withColor(GuiGauge.TypeColor.ORANGE));
        addGuiElement(new GuiSlot(SlotType.OUTPUT, this, resource, 126,66).with(SlotOverlay.PLUS));
        ySize += 5;
        addGuiElement(new GuiPlayerSlot(this, resource,7,88));
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
            float  Chance = Math.round(tileEntity.getRecipe().getOutput().primaryChance * 100);
            fontRenderer.drawString(LangUtils.localize("gui.probability") + ":" + Chance + "%", 8, 59, 0x33ff99);
            fontRenderer.drawString(tileEntity.collectedGas.getStored() + " / " + tileEntity.collectedGas.getMaxGas(), 8, 68, 0x33ff99);
        }else {
            fontRenderer.drawString(LangUtils.localize("gui.dimensionNoGas"), 8, 41, 0x33ff99);
            fontRenderer.drawString(tileEntity.collectedGas.getStored() + " / " + tileEntity.collectedGas.getMaxGas(), 8, 50, 0x33ff99);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

}
