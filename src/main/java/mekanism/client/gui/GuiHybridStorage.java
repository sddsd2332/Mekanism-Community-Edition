package mekanism.client.gui;


import mekanism.client.gui.element.GuiEnergyInfo;
import mekanism.client.gui.element.GuiPlayerSlot;
import mekanism.client.gui.element.GuiSlot;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.common.inventory.container.ContainerHybridStorage;
import mekanism.common.tile.TileEntityHybridStorage;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class GuiHybridStorage extends GuiMekanismTile<TileEntityHybridStorage> {

    public GuiHybridStorage(InventoryPlayer inventory, TileEntityHybridStorage tile) {
        super(tile, new ContainerHybridStorage(inventory, tile));
        xSize += 54;
        ySize += 136;
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource,54,0));
        addGuiElement(new GuiPlayerSlot(this, resource, 7, 219));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiGasGauge(() -> tileEntity.gasTank1, GuiGauge.Type.STANDARD, this, resource, -14, 25).withColor(GuiGauge.TypeColor.BLUE));
        addGuiElement(new GuiGasGauge(() -> tileEntity.gasTank2, GuiGauge.Type.STANDARD, this, resource, -14, 88).withColor(GuiGauge.TypeColor.ORANGE));
        addGuiElement(new GuiFluidGauge(() -> tileEntity.fluidTank, GuiGauge.Type.STANDARD, this, resource, -14, 151).withColor(GuiGauge.TypeColor.YELLOW));
        addGuiElement(new GuiEnergyGauge(() -> tileEntity, GuiEnergyGauge.Type.STANDARD, this, resource, -14, 214).withColor(GuiGauge.TypeColor.AQUA));
        addGuiElement(new GuiEnergyInfo(() -> Arrays.asList(LangUtils.localize("gui.storing") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getEnergy(), tileEntity.getMaxEnergy()),
                LangUtils.localize("gui.maxOutput") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxOutput()) + "/t"), this, resource));

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 12; x++) {
                addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, resource, 7 + x * 18, 25 + y * 18));
            }
        }

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}
