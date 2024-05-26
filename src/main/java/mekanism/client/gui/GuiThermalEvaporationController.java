package mekanism.client.gui;

import mekanism.client.gui.element.*;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.ContainerThermalEvaporationController;
import mekanism.common.tile.multiblock.TileEntityThermalEvaporationController;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiThermalEvaporationController extends GuiMekanismTile<TileEntityThermalEvaporationController> {

    public GuiThermalEvaporationController(InventoryPlayer inventory, TileEntityThermalEvaporationController tile) {
        super(tile, new ContainerThermalEvaporationController(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiFluidGauge(() -> tileEntity.inputTank, GuiGauge.Type.STANDARD, this, resource, 6, 13));
        addGuiElement(new GuiFluidGauge(() -> tileEntity.outputTank, GuiGauge.Type.STANDARD, this, resource, 152, 13));
        addGuiElement(new GuiHeatInfo(() -> {
            TemperatureUnit unit = TemperatureUnit.values()[MekanismConfig.current().general.tempUnit.val().ordinal()];
            String environment = UnitDisplayUtils.getDisplayShort(tileEntity.totalLoss * unit.intervalSize, false, unit);
            return Collections.singletonList(LangUtils.localize("gui.dissipated") + ": " + environment + "/t");
        }, this, resource));
        addGuiElement(new GuiRateBarHorizontal(this, new GuiRateBarHorizontal.IRateInfoHandler() {
            @Override
            public String getTooltip() {
                return LangUtils.localize("gui.temp") + ": " + getTemp();
            }

            @Override
            public double getLevel() {
                return Math.min(1, tileEntity.getTemperature() / MekanismConfig.current().general.evaporationMaxTemp.val());
            }
        }, resource, 46, 62));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, resource, 27, 19));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, resource, 27, 50));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, resource, 131, 19));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, resource, 131, 50));
        addGuiElement(new GuiInnerScreen(this, resource, 48, 19, 80, 40));
        addGuiElement(new GuiPlayerSlot(this, resource));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Other_Icon.png"));
        drawTexturedModalRect(guiLeft + 32, guiTop + 39, 13, 0, 8, 9);
        drawTexturedModalRect(guiLeft + 136, guiTop + 39, 13, 0, 8, 9);
        boolean outputfluid = tileEntity.outputTank.getFluidAmount() == tileEntity.outputTank.getCapacity();
        if (outputfluid) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Warning.png"));
            drawTexturedModalRect(guiLeft + 152 + 9, guiTop + 13 + 1, 9, 1, 8, 29);
            drawTexturedModalRect(guiLeft + 152 + 9, guiTop + 13 + 31, 9, 32, 8, 28);
        }
        if (outputfluid) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 86, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), true));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 4, 0x404040);
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(getStruct(), 50, 21, 0xFF3CFE9A);
        fontRenderer.drawString(LangUtils.localize("gui.height") + ": " + tileEntity.height, 50, 30, 0xFF3CFE9A);
        fontRenderer.drawString(LangUtils.localize("gui.temp") + ": " + getTemp(), 50, 39, 0xFF3CFE9A);
        renderScaledText(LangUtils.localize("gui.production") + ": " + Math.round(tileEntity.lastGain * 100D) / 100D + " mB/t", 50, 48, 0xFF3CFE9A, 76);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= 7 && xAxis <= 23 && yAxis >= 14 && yAxis <= 72) {
            FluidStack fluid = tileEntity.inputTank.getFluid();
            displayTooltip(fluid != null ? LangUtils.localizeFluidStack(fluid) + ": " + tileEntity.inputTank.getFluidAmount() : LangUtils.localize("gui.empty"), xAxis, yAxis);
        } else if (xAxis >= 153 && xAxis <= 169 && yAxis >= 14 && yAxis <= 72) {
            FluidStack fluid = tileEntity.outputTank.getFluid();
            displayTooltip(fluid != null ? LangUtils.localizeFluidStack(fluid) + ": " + tileEntity.outputTank.getFluidAmount() : LangUtils.localize("gui.empty"), xAxis, yAxis);
        } else if (xAxis >= -21 && xAxis <= -3 && yAxis >= 90 && yAxis <= 108) {
            List<String> info = new ArrayList<>();
            boolean outputfluid = tileEntity.outputTank.getFluidAmount() == tileEntity.outputTank.getCapacity();
            if (outputfluid) {
                info.add(LangUtils.localize("gui.fluid_no_space"));
            }
            if (outputfluid) {
                displayTooltips(info, xAxis, yAxis);
            }
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    private String getStruct() {
        if (tileEntity.structured) {
            return LangUtils.localize("gui.formed");
        } else if (tileEntity.controllerConflict) {
            return LangUtils.localize("gui.conflict");
        }
        return LangUtils.localize("gui.incomplete");
    }

    private String getTemp() {
        return MekanismUtils.getTemperatureDisplay(tileEntity.getTemperature(), TemperatureUnit.AMBIENT);
    }

}
