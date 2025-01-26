package mekanism.multiblockmachine.client.gui.machine;

import mekanism.api.util.time.Timeticks;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiUpgradeTab;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.multiblockmachine.common.inventory.container.machine.ContainerLargeChemicalWasher;
import mekanism.multiblockmachine.common.tile.machine.TileEntityLargeChemicalWasher;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiLargeChemicalWasher extends GuiMekanismTile<TileEntityLargeChemicalWasher> {
    protected Timeticks time;

    public GuiLargeChemicalWasher(InventoryPlayer inventory, TileEntityLargeChemicalWasher tile) {
        super(tile, new ContainerLargeChemicalWasher(inventory, tile));
        time = new Timeticks(20, 20, false);
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource));
        addGuiElement(new GuiPowerBarHorizontal(this, tileEntity, resource, 115 - 2, 74));
        addGuiElement(new GuiEnergyInfo(() -> {
            String usage = MekanismUtils.getEnergyDisplay(tileEntity.clientEnergyUsed);
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + usage + "/t",
                    LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, resource));
        addGuiElement(new GuiFluidGauge(() -> tileEntity.fluidTank, GuiGauge.Type.STANDARD, this, resource, 5, 4).withColor(GuiGauge.TypeColor.RED));
        addGuiElement(new GuiGasGauge(() -> tileEntity.inputTank, GuiGauge.Type.STANDARD, this, resource, 26, 13).withColor(GuiGauge.TypeColor.RED));
        addGuiElement(new GuiGasGauge(() -> tileEntity.outputTank, GuiGauge.Type.STANDARD, this, resource, 133, 13).withColor(GuiGauge.TypeColor.BLUE));

        addGuiElement(new GuiBucketIO(this, resource));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.POWER, this, resource, 154, 13).with(GuiSlot.SlotOverlay.POWER));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.OUTPUT, this, resource, 154, 55).with(GuiSlot.SlotOverlay.MINUS));
        addGuiElement(new GuiProgress(new GuiProgress.IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return tileEntity.getActive() ? (double) time.getValue() / 20F : 0;
            }
        }, GuiProgress.ProgressBar.LARGE_RIGHT, this, resource, 62, 38));
        addGuiElement(new GuiPlayerSlot(this, resource));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= -21 && xAxis <= -3 && yAxis >= 116 && yAxis <= 134) {
            List<String> info = new ArrayList<>();
            boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
            boolean output = tileEntity.outputTank.getStored() == tileEntity.outputTank.getMaxGas();
            if (energy) {
                info.add(LangUtils.localize("gui.no_energy"));
            }
            if (output) {
                info.add(LangUtils.localize("gui.gas_no_space"));
            }
            if (output || energy) {
                this.olddisplayTooltips(info, xAxis, yAxis);
            }
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Other_Icon.png"));
        drawTexturedModalRect(guiLeft + 8, guiTop + 66, 0, 0, 12, 13);
        boolean output = tileEntity.outputTank.getStored() == tileEntity.outputTank.getMaxGas();
        boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
        if (output) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Warning.png"));
            drawTexturedModalRect(guiLeft + 133 + 9, guiTop + 13 + 1, 9, 1, 8, 29);
            drawTexturedModalRect(guiLeft + 133 + 9, guiTop + 13 + 31, 9, 32, 8, 28);
        }
        if (output || energy) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 112, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), false));
        }
    }
}
