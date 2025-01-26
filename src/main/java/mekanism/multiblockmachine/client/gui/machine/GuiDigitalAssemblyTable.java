package mekanism.multiblockmachine.client.gui.machine;

import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.bar.GuiBar;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiUpgradeTab;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.multiblockmachine.common.inventory.container.machine.ContainerDigitalAssemblyTable;
import mekanism.multiblockmachine.common.tile.machine.TileEntityDigitalAssemblyTable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class GuiDigitalAssemblyTable extends GuiMekanismTile<TileEntityDigitalAssemblyTable> {

    public GuiDigitalAssemblyTable(InventoryPlayer inventory, TileEntityDigitalAssemblyTable tile) {
        super(tile, new ContainerDigitalAssemblyTable(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        ySize += 11;
        xSize += 52;
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource,52,0));
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource,52,0));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource,52,0));
        addGuiElement(new GuiEnergyInfo(() -> {
            double extra = tileEntity.getRecipe() != null ? tileEntity.getRecipe().extraEnergy : 0;
            String multiplier = MekanismUtils.getEnergyDisplay(MekanismUtils.getEnergyPerTick(tileEntity, tileEntity.BASE_ENERGY_PER_TICK + extra));
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + multiplier + "/t",
                    LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, resource));
        addGuiElement(new GuiFluidGauge(() -> tileEntity.inputFluidTank, GuiGauge.Type.STANDARD, this, resource, 6, 12).withColor(GuiGauge.TypeColor.RED));
        addGuiElement(new GuiGasGauge(() -> tileEntity.inputGasTank, GuiGauge.Type.STANDARD, this, resource, 27, 12).withColor(GuiGauge.TypeColor.YELLOW));
        addGuiElement(new GuiGasGauge(() -> tileEntity.outputGasTank, GuiGauge.Type.STANDARD, this, resource, 183, 12).withColor(GuiGauge.TypeColor.ORANGE));
        addGuiElement(new GuiFluidGauge(() -> tileEntity.outputFluidTank, GuiGauge.Type.STANDARD, this, resource, 204, 12).withColor(GuiGauge.TypeColor.BLUE));
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                addGuiElement(new GuiSlot(GuiSlot.SlotType.INPUT, this, resource, 66 + x * 18, 15 + y * 18));
            }
        }
        for (int y = 0; y < 3; y++) {
            addGuiElement(new GuiSlot(GuiSlot.SlotType.EXTRA, this, resource, 48, 15 + y * 18));
        }
        addGuiElement(new GuiSlot(GuiSlot.SlotType.OUTPUT, this, resource, 162, 33));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.POWER, this, resource, 199, 93));
        addGuiElement(new GuiProgress(new GuiProgress.IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return tileEntity.getScaledProgress();
            }
        }, GuiProgress.ProgressBar.MEDIUM, this, resource, 125, 38));
        addGuiElement(new GuiBar(this, getGuiLocation(), 11, 77, 206, 6));//Energy bar
        addGuiElement(new GuiPlayerSlot(this, resource, 33, 93));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 33, (ySize - 96) + 2, 0x404040);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= 12 && xAxis <= 215 && yAxis >= 78 && yAxis <= 81) {
            this.olddisplayTooltip(MekanismUtils.getEnergyDisplay(tileEntity.getEnergy(), tileEntity.getMaxEnergy()), xAxis, yAxis);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }



    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.SLOT, "Slot_Icon.png"));
        drawTexturedModalRect(guiLeft + 11, guiTop + 93, 72, 54, 18, 18);
        drawTexturedModalRect(guiLeft + 14, guiTop + 96, tileEntity.getActive() ? 12 : 0, 88, 12, 12);
        mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_BAR, "Power_Bar_Horizontal.png"));
        drawTexturedModalRect(guiLeft + 12, guiTop + 78, 0, 9, tileEntity.getScaledEnergyLevel(204), 4);
    }

}
