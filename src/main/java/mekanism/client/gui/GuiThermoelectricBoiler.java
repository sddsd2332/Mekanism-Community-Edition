package mekanism.client.gui;

import mekanism.client.gui.element.*;
import mekanism.client.gui.element.GuiRateBar.IRateInfoHandler;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.gauge.GuiNumberGauge;
import mekanism.client.gui.element.tab.GuiBoilerTab;
import mekanism.client.gui.element.tab.GuiBoilerTab.BoilerTab;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.boiler.SynchronizedBoilerData;
import mekanism.common.inventory.container.ContainerThermoelectricBoiler;
import mekanism.common.tile.multiblock.TileEntityBoilerCasing;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiThermoelectricBoiler extends GuiMekanismTile<TileEntityBoilerCasing> {

    public GuiThermoelectricBoiler(InventoryPlayer inventory, TileEntityBoilerCasing tile) {
        super(tile, new ContainerThermoelectricBoiler(inventory, tile));
        xSize += 40;
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiBoilerTab(this, tileEntity, BoilerTab.STAT, resource));
        addGuiElement(new GuiRateBar(this, new IRateInfoHandler() {
            @Override
            public String getTooltip() {
                return LangUtils.localize("gui.boilRate") + ": " + tileEntity.getLastBoilRate() + " mB/t";
            }

            @Override
            public double getLevel() {
                return tileEntity.structure == null ? 0 : (double) tileEntity.getLastBoilRate() / (double) tileEntity.structure.lastMaxBoil;
            }
        }, resource, 44, 13));
        addGuiElement(new GuiRateBar(this, new IRateInfoHandler() {
            @Override
            public String getTooltip() {
                return LangUtils.localize("gui.maxBoil") + ": " + tileEntity.getLastMaxBoil() + " mB/t";
            }

            @Override
            public double getLevel() {
                return tileEntity.structure == null ? 0 : tileEntity.getLastMaxBoil() * SynchronizedBoilerData.getHeatEnthalpy() /
                        (tileEntity.structure.superheatingElements * MekanismConfig.current().general.superheatingHeatTransfer.val());
            }
        }, resource, 164, 13));
        addGuiElement(new GuiHeatInfo(() -> {
            TemperatureUnit unit = TemperatureUnit.values()[MekanismConfig.current().general.tempUnit.val().ordinal()];
            String environment = UnitDisplayUtils.getDisplayShort(tileEntity.getLastEnvironmentLoss() * unit.intervalSize, false, unit);
            return Collections.singletonList(LangUtils.localize("gui.dissipated") + ": " + environment + "/t");
        }, this, resource));
        addGuiElement(new GuiInnerScreen(this, resource, 60, 23, 96, 40));
        addGuiElement(new GuiPlayerSlot(this, resource, 26));
        addGuiElement(new GuiNumberGauge(new GuiNumberGauge.INumberInfoHandler() {

            @Override
            public TextureAtlasSprite getIcon() {
                return MekanismRenderer.getFluidTexture(tileEntity.structure != null ? tileEntity.structure.waterStored : null, MekanismRenderer.FluidType.STILL);
            }

            @Override
            public double getLevel() {
                if (tileEntity.structure != null && tileEntity.structure.waterStored != null) {
                    return tileEntity.structure.waterStored.amount;
                } else {
                    return 0;
                }
            }

            @Override
            public double getMaxLevel() {
                if (tileEntity.structure != null && tileEntity.structure.waterStored != null) {
                    return tileEntity.clientWaterCapacity;
                } else {
                    return 0;
                }
            }

            @Override
            public String getText(double level) {
                return tileEntity.structure != null ? (tileEntity.structure.waterStored != null ? LangUtils.localizeFluidStack(tileEntity.structure.waterStored) + ": " + tileEntity.structure.waterStored.amount + "mB" : LangUtils.localize("gui.empty")) : "";
            }
        }, GuiGauge.Type.STANDARD, this, resource, 26, 13));
        addGuiElement(new GuiNumberGauge(new GuiNumberGauge.INumberInfoHandler() {

            @Override
            public TextureAtlasSprite getIcon() {
                return MekanismRenderer.getFluidTexture(tileEntity.structure != null ? tileEntity.structure.steamStored : null, MekanismRenderer.FluidType.STILL);
            }

            @Override
            public double getLevel() {
                if (tileEntity.structure != null && tileEntity.structure.steamStored != null) {
                    return tileEntity.structure.steamStored.amount;
                } else {
                    return 0;
                }
            }

            @Override
            public double getMaxLevel() {
                if (tileEntity.structure != null && tileEntity.structure.steamStored != null) {
                    return tileEntity.clientSteamCapacity;
                } else {
                    return 0;
                }
            }

            @Override
            public String getText(double level) {
                return tileEntity.structure != null ? (tileEntity.structure.steamStored != null ? LangUtils.localizeFluidStack(tileEntity.structure.steamStored) + ": " + tileEntity.structure.steamStored.amount + "mB" : LangUtils.localize("gui.empty")) : "";
            }
        }, GuiGauge.Type.STANDARD, this, resource, 172, 13));
        addGuiElement(new GuiNumberGauge(new GuiNumberGauge.INumberInfoHandler() {

            @Override
            public TextureAtlasSprite getIcon() {
                if (tileEntity.structure != null) {
                    MekanismRenderer.color(tileEntity.structure.InputGas.getGas());
                    return tileEntity.structure.InputGas.getGas().getSprite();
                }
                return null;
            }

            @Override
            public double getLevel() {
                if (tileEntity.structure != null && tileEntity.structure.InputGas != null) {
                    return tileEntity.structure.InputGas.amount;
                } else {
                    return 0;
                }
            }

            @Override
            public double getMaxLevel() {
                if (tileEntity.structure != null && tileEntity.structure.InputGas != null) {
                    return tileEntity.clientWaterCapacity;
                } else {
                    return 0;
                }
            }

            @Override
            public String getText(double level) {
                return tileEntity.structure != null ? (tileEntity.structure.InputGas != null ? tileEntity.structure.InputGas.getGas().getLocalizedName() + ": " + tileEntity.structure.InputGas.amount + "mB" : LangUtils.localize("gui.empty")) : "";
            }
        }, GuiGauge.Type.STANDARD, this, resource, 6, 13));
        addGuiElement(new GuiNumberGauge(new GuiNumberGauge.INumberInfoHandler() {

            @Override
            public TextureAtlasSprite getIcon() {
                if (tileEntity.structure != null) {
                    MekanismRenderer.color(tileEntity.structure.OutputGas.getGas());
                    return tileEntity.structure.OutputGas.getGas().getSprite();
                }
                return null;
            }

            @Override
            public double getLevel() {
                if (tileEntity.structure != null && tileEntity.structure.OutputGas != null) {
                    return tileEntity.structure.OutputGas.amount;
                } else {
                    return 0;
                }
            }

            @Override
            public double getMaxLevel() {
                if (tileEntity.structure != null && tileEntity.structure.OutputGas != null) {
                    return tileEntity.clientSteamCapacity;
                } else {
                    return 0;
                }
            }

            @Override
            public String getText(double level) {
                return tileEntity.structure != null ? (tileEntity.structure.OutputGas != null ? tileEntity.structure.OutputGas.getGas().getLocalizedName() + ": " + tileEntity.structure.OutputGas.amount + "mB" : LangUtils.localize("gui.empty")) : "";
            }
        }, GuiGauge.Type.STANDARD, this, resource, 192, 13));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 4, 0x404040);
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        renderScaledText(LangUtils.localize("gui.temp") + ": " +
                MekanismUtils.getTemperatureDisplay(tileEntity.getTemperature(), TemperatureUnit.AMBIENT), 63, 30, 0xFF3CFE9A, 90);
        renderScaledText(LangUtils.localize("gui.boilRate") + ": " + tileEntity.getLastBoilRate() + " mB/t", 63, 39, 0xFF3CFE9A, 90);
        renderScaledText(LangUtils.localize("gui.maxBoil") + ": " + tileEntity.getLastMaxBoil() + " mB/t", 63, 48, 0xFF3CFE9A, 90);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= -21 && xAxis <= -3 && yAxis >= 90 && yAxis <= 108) {
            List<String> info = new ArrayList<>();
            boolean Steam = tileEntity.structure != null && tileEntity.structure.steamStored != null && tileEntity.structure.steamStored.amount == tileEntity.clientSteamCapacity;
            if (Steam) {
                info.add(LangUtils.localize("gui.steam_no_space"));
            }
            if (Steam) {
                this.displayTooltips(info, xAxis, yAxis);
            }
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        boolean Steam = tileEntity.structure != null && tileEntity.structure.steamStored != null && tileEntity.structure.steamStored.amount == tileEntity.clientSteamCapacity;
        boolean Output = tileEntity.structure != null && tileEntity.structure.OutputGas != null && tileEntity.structure.OutputGas.amount == tileEntity.clientSteamCapacity;
        if (Steam) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Warning.png"));
            drawTexturedModalRect(guiLeft + 172 + 9, guiTop + 13 + 1, 9, 1, 8, 29);
            drawTexturedModalRect(guiLeft + 172 + 9, guiTop + 13 + 31, 9, 32, 8, 28);
        }
        if (Output) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Warning.png"));
            drawTexturedModalRect(guiLeft + 192 + 9, guiTop + 13 + 1, 9, 1, 8, 29);
            drawTexturedModalRect(guiLeft + 192 + 9, guiTop + 13 + 31, 9, 32, 8, 28);
        }
        if (Steam || Output) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 86, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), true));
        }
    }

}
