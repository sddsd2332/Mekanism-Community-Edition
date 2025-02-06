package mekanism.client.gui;

import mekanism.client.gui.element.GuiEnergyInfo;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.GuiPlayerSlot;
import mekanism.client.gui.element.GuiRedstoneControl;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.ContainerSPS;
import mekanism.common.tile.TileEntitySPS;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiSPS extends GuiMekanismTile<TileEntitySPS> {


    public GuiSPS(InventoryPlayer inventory, TileEntitySPS tile) {
        super(tile, new ContainerSPS(inventory, tile));
        ySize += 5;
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiEnergyInfo(() -> {
            String usage = MekanismUtils.getEnergyDisplay(tileEntity.lastReceivedEnergy);
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + usage + "/t",
                    LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, resource));
        addGuiElement(new GuiGasGauge(() -> tileEntity.inputTank, GuiGauge.Type.STANDARD, this, resource, 7, 17).withColor(GuiGauge.TypeColor.RED));
        addGuiElement(new GuiGasGauge(() -> tileEntity.outputTank, GuiGauge.Type.STANDARD, this, resource, 151, 17).withColor(GuiGauge.TypeColor.RED));
        addGuiElement(new GuiPlayerSlot(this, resource, 7, ySize - 83));
        addGuiElement(new GuiInnerScreen(this, resource, 27, 17, 122, 60, () -> {
            List<String> list = new ArrayList<>();
            boolean active = tile.lastProcessed > 0;
            list.add(MekanismLang.STATUS.getTranslationKey() + (active ? MekanismLang.ACTIVE.getTranslationKey() : MekanismLang.IDLE.getTranslationKey()));
            if (active) {
                list.add(MekanismLang.SPS_ENERGY_INPUT.getTranslationKey() + MekanismUtils.getEnergyDisplay(tileEntity.lastReceivedEnergy));
                list.add(MekanismLang.PROCESS_RATE_MB.getTranslationKey() + tile.getProcessRate() + "mB/t");
            }
            return list;
        }));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 6, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, ySize - 92, 0x404040);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

}
