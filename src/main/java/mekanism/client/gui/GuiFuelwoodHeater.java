package mekanism.client.gui;

import java.util.Collections;

import mekanism.client.gui.element.*;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.ContainerFuelwoodHeater;
import mekanism.common.tile.TileEntityFuelwoodHeater;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import mekanism.common.util.UnitDisplayUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFuelwoodHeater extends GuiMekanismTile<TileEntityFuelwoodHeater> {

    public GuiFuelwoodHeater(InventoryPlayer inventory, TileEntityFuelwoodHeater tile) {
        super(tile, new ContainerFuelwoodHeater(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiSlot(SlotType.NORMAL, this, resource, 14, 28));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource, 0, 0));
        addGuiElement(new GuiHeatInfo(() -> {
            TemperatureUnit unit = TemperatureUnit.values()[MekanismConfig.current().general.tempUnit.val().ordinal()];
            String environment = UnitDisplayUtils.getDisplayShort(tileEntity.lastEnvironmentLoss * unit.intervalSize, false, unit);
            return Collections.singletonList(LangUtils.localize("gui.dissipated") + ": " + environment + "/t");
        }, this, resource));
        addGuiElement(new GuiBlackScreen(GuiBlackScreen.BlackScreen.FUELWOOD,this,resource,48,23));
        addGuiElement(new GuiRateBar(this, new GuiRateBar.IRateInfoHandler() {
            @Override
            public String getTooltip() {
                return LangUtils.localize("gui.temp") + ": " + getTemp();
            }
            @Override
            public double getLevel() {
                return Math.min(1, tileEntity.temperature / MekanismConfig.current().general.evaporationMaxTemp.val());
            }
        }, resource, 161, 11));
        addGuiElement(new GuiRateBarHorizontal(this, new GuiRateBarHorizontal.IRateInfoHandler() {
            @Override
            public String getTooltip() {
                return LangUtils.localize("gui.fuel") + ": " + tileEntity.burnTime + " / " + tileEntity.maxBurnTime;
            }
            @Override
            public double getLevel() {
                return (double) tileEntity.burnTime / tileEntity.maxBurnTime;
            }
        },resource,46,60));

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 6, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 94) + 2, 0x404040);
        renderScaledText(LangUtils.localize("gui.temp") + ": " + getTemp(), 50, 25, 0x00CD00, 76);
        renderScaledText(LangUtils.localize("gui.fuel") + ": " + tileEntity.burnTime, 50, 41, 0x00CD00, 76);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        drawTexturedModalRect(guiLeft + 137, guiTop + 29,99,167,14,14);
        if (tileEntity.burnTime > 0) {
            int displayInt = tileEntity.burnTime * 13 / tileEntity.maxBurnTime;
            drawTexturedModalRect(guiLeft + 137, guiTop + 29 + 12 - displayInt, 84, 179 - displayInt, 14, displayInt + 1);
        }
    }

    @Override
    protected ResourceLocation getGuiLocation() {
        return MekanismUtils.getResource(ResourceType.GUI, "GuiBlankIcon.png");
    }

    private String getTemp() {
        return MekanismUtils.getTemperatureDisplay(tileEntity.getTemp(), TemperatureUnit.AMBIENT);
    }
}