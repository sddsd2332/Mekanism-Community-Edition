package mekanism.client.gui;

import mekanism.client.gui.element.*;
import mekanism.client.gui.element.GuiProgress.IProgressInfoHandler;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.gui.element.bar.GuiBar;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.client.gui.element.tab.GuiTransporterConfigTab;
import mekanism.client.gui.element.tab.GuiUpgradeTab;
import mekanism.common.inventory.container.ContainerAdvancedElectricMachine;
import mekanism.common.recipe.machines.AdvancedMachineRecipe;
import mekanism.common.tile.prefab.TileEntityAdvancedElectricMachine;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SideOnly(Side.CLIENT)
public class GuiAdvancedElectricMachine<RECIPE extends AdvancedMachineRecipe<RECIPE>> extends GuiMekanismTile<TileEntityAdvancedElectricMachine<RECIPE>> {

    public GuiAdvancedElectricMachine(InventoryPlayer inventory, TileEntityAdvancedElectricMachine<RECIPE> tile) {
        super(tile, new ContainerAdvancedElectricMachine<>(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiTransporterConfigTab(this, 34, tileEntity, resource));
        addGuiElement(new GuiPowerBar(this, tileEntity, resource, 164, 15));
        addGuiElement(new GuiBar(this, getGuiLocation(), 60, 36, 8, 14));
        addGuiElement(new GuiEnergyInfo(() -> {
            String multiplier = MekanismUtils.getEnergyDisplay(tileEntity.energyPerTick);
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + multiplier + "/t",
                    LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, resource));
        addGuiElement(new GuiSlot(SlotType.INPUT, this, resource, 55, 16));
        addGuiElement(new GuiSlot(SlotType.POWER, this, resource, 30, 34).with(SlotOverlay.POWER));
        addGuiElement(new GuiSlot(SlotType.EXTRA, this, resource, 55, 52));
        addGuiElement(new GuiSlot(SlotType.OUTPUT_LARGE, this, resource, 111, 30));
        addGuiElement(new GuiProgress(new IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return tileEntity.getScaledProgress();
            }
        }, getProgressType(), this, resource, 77, 37));
        addGuiElement(new GuiPlayerSlot(this, resource));
    }

    public ProgressBar getProgressType() {
        return ProgressBar.BAR;
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
            boolean inputgas = (tileEntity.gasTank.getStored() == 0) && (tileEntity.inventory.get(0).getCount() != 0);
            boolean outslot = tileEntity.inventory.get(2).getCount() == tileEntity.inventory.get(2).getMaxStackSize();
            if (energy) {
                info.add(LangUtils.localize("gui.no_energy"));
            }
            if (inputgas) {
                info.add(LangUtils.localize("gui.no_gas"));
            }
            if (outslot) {
                info.add(LangUtils.localize("gui.item_no_space"));
            }
            if (inputgas || energy || outslot) {
                displayTooltips(info, xAxis, yAxis);
            }
        } else if (xAxis >= 60 && xAxis <= 60 + 8 && yAxis >= 36 && yAxis <= 36 + 14) {
            displayTooltip(tileEntity.gasTank.getGas() != null ? tileEntity.gasTank.getGas().getGas().getLocalizedName() + ": " + (tileEntity.gasTank.getStored() == Integer.MAX_VALUE ? LangUtils.localize("gui.infinite") : tileEntity.gasTank.getStored()) : LangUtils.localize("gui.none"), xAxis, yAxis);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        GuiUtils.drawGasBarSprite(guiLeft + 60, guiTop + 36, 8, 14, tileEntity.getScaledGasLevel(12), tileEntity.gasTank.getGas(), true);
        boolean inputgas = (tileEntity.gasTank.getStored() == 0) && (tileEntity.inventory.get(0).getCount() != 0);
        boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
        boolean outslot = tileEntity.inventory.get(2).getCount() == tileEntity.inventory.get(2).getMaxStackSize();

        if (outslot) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.SLOT, "Slot_Icon.png"));
            drawTexturedModalRect(guiLeft + 111, guiTop + 30, 176, 0, 26, 26);
        }
        if (inputgas) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Warning_Background.png"));
            drawTexturedModalRect(guiLeft + 61, guiTop + 37, 0, 0, 6, 12);
        }
        if (outslot || inputgas || energy) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 112, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), false));
        }
    }

}
