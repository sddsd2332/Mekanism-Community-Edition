package mekanism.client.gui;

import mekanism.client.gui.element.*;
import mekanism.client.gui.element.GuiProgress.IProgressInfoHandler;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.client.gui.element.tab.GuiTransporterConfigTab;
import mekanism.client.gui.element.tab.GuiUpgradeTab;
import mekanism.common.inventory.container.ContainerPRC;
import mekanism.common.tile.machine.TileEntityPRC;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiPRC extends GuiMekanismTile<TileEntityPRC> {

    public GuiPRC(InventoryPlayer inventory, TileEntityPRC tile) {
        super(tile, new ContainerPRC(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiTransporterConfigTab(this, 34, tileEntity, resource));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource));
        addGuiElement(new GuiEnergyInfo(() -> {
            double extra = tileEntity.getRecipe() != null ? tileEntity.getRecipe().extraEnergy : 0;
            String multiplier = MekanismUtils.getEnergyDisplay(MekanismUtils.getEnergyPerTick(tileEntity, tileEntity.BASE_ENERGY_PER_TICK + extra));
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + multiplier + "/t",
                    LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, resource));
        addGuiElement(new GuiFluidGauge(() -> tileEntity.inputFluidTank, GuiGauge.Type.STANDARD, this, resource, 5, 10).withColor(GuiGauge.TypeColor.YELLOW));
        addGuiElement(new GuiGasGauge(() -> tileEntity.inputGasTank, GuiGauge.Type.STANDARD, this, resource, 28, 10).withColor(GuiGauge.TypeColor.RED));
        addGuiElement(new GuiGasGauge(() -> tileEntity.outputGasTank, GuiGauge.Type.SMALL, this, resource, 140, 40).withColor(GuiGauge.TypeColor.BLUE));
        addGuiElement(new GuiPowerBar(this, tileEntity, resource, 164, 15));
        addGuiElement(new GuiSlot(SlotType.INPUT, this, resource, 53, 34));
        addGuiElement(new GuiSlot(SlotType.POWER, this, resource, 140, 18).with(SlotOverlay.POWER));
        addGuiElement(new GuiSlot(SlotType.OUTPUT, this, resource, 115, 34));
        addGuiElement(new GuiProgress(new IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return tileEntity.getScaledProgress();
            }
        }, ProgressBar.MEDIUM, this, resource, 75, 37));
        addGuiElement(new GuiPlayerSlot(this, resource));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);

        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= -21 && xAxis <= -3 && yAxis >= 116 && yAxis <= 134) {
            List<String> info = new ArrayList<>();
            boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
            boolean inputgas = (tileEntity.inputGasTank.getStored() == 0) && (tileEntity.inventory.get(0).getCount() != 0);
            boolean inputfluid = (tileEntity.inputFluidTank.getFluidAmount() == 0) && (tileEntity.inventory.get(0).getCount() != 0);
            boolean outputgas = tileEntity.outputGasTank.getStored() == tileEntity.outputGasTank.getMaxGas();
            boolean outslot = tileEntity.inventory.get(2).getCount() == tileEntity.inventory.get(2).getMaxStackSize();
            if (energy) {
                info.add(LangUtils.localize("gui.no_energy"));
            }
            if (inputgas) {
                info.add(LangUtils.localize("gui.no_gas"));
            }
            if (inputfluid) {
                info.add(LangUtils.localize("gui.no_fluid"));
            }
            if (outslot) {
                info.add(LangUtils.localize("gui.item_no_space"));
            }
            if (outputgas) {
                info.add(LangUtils.localize("gui.gas_no_space"));
            }
            if (inputgas || inputfluid || energy || outslot || outputgas) {
                displayTooltips(info, xAxis, yAxis);
            }
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
        boolean inputgas = (tileEntity.inputGasTank.getStored() == 0) && (tileEntity.inventory.get(0).getCount() != 0);
        boolean inputfluid = (tileEntity.inputFluidTank.getFluidAmount() == 0) && (tileEntity.inventory.get(0).getCount() != 0);
        boolean outputgas = tileEntity.outputGasTank.getStored() == tileEntity.outputGasTank.getMaxGas();
        boolean outslot = tileEntity.inventory.get(2).getCount() == tileEntity.inventory.get(2).getMaxStackSize();
        if (inputgas) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Warning.png"));
            drawTexturedModalRect(guiLeft + 28 + 9, guiTop + 10 + 1, 9, 1, 8, 29);
            drawTexturedModalRect(guiLeft + 28 + 9, guiTop + 10 + 31, 9, 32, 8, 28);
        }
        if (inputfluid) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Warning.png"));
            drawTexturedModalRect(guiLeft + 5 + 9, guiTop + 10 + 1, 9, 1, 8, 29);
            drawTexturedModalRect(guiLeft + 5 + 9, guiTop + 10 + 31, 9, 32, 8, 28);
        }
        if (outputgas) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Warning.png"));
            drawTexturedModalRect(guiLeft + 140 + 10, guiTop + 40 + 1, 0, 0, 7, 28);
        }
        if (outslot) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(ResourceType.SLOT, "Slot_Icon.png"));
            drawTexturedModalRect(guiLeft + 115, guiTop + 34, 158, 0, 18, 18);
        }
        if (outslot || inputfluid || outputgas || inputgas || energy) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 112, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), false));
        }
    }
}
