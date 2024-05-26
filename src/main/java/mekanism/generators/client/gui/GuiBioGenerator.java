package mekanism.generators.client.gui;

import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.gui.element.bar.GuiBar;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.MekanismFluids;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.generators.common.inventory.container.ContainerBioGenerator;
import mekanism.generators.common.tile.TileEntityBioGenerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class GuiBioGenerator extends GuiMekanismTile<TileEntityBioGenerator> {

    public GuiBioGenerator(InventoryPlayer inventory, TileEntityBioGenerator tile) {
        super(tile, new ContainerBioGenerator(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiEnergyInfo(() -> Arrays.asList(
                LangUtils.localize("gui.producing") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getActive() ? MekanismConfig.current().generators.bioGeneration.val() : 0) + "/t",
                LangUtils.localize("gui.maxOutput") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxOutput()) + "/t"), this, resource));
        addGuiElement(new GuiPowerBar(this, tileEntity, resource, 164, 15));
        addGuiElement(new GuiSlot(SlotType.NORMAL, this, resource, 16, 34));
        addGuiElement(new GuiSlot(SlotType.POWER, this, resource, 142, 34).with(SlotOverlay.POWER));
        addGuiElement(new GuiInnerScreen(this, resource, 48, 23, 80, 40));
        addGuiElement(new GuiPlayerSlot(this, getGuiLocation()));
        addGuiElement(new GuiBar(this, getGuiLocation(), 6, 16, 6, 54));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        fontRenderer.drawString(MekanismUtils.getEnergyDisplay(tileEntity.getEnergy()), 51, 26, 0xFF3CFE9A);
        fontRenderer.drawString(LangUtils.localize("gui.bioGenerator.bioFuel") + ": " + tileEntity.bioFuelSlot.fluidStored, 51, 35, 0xFF3CFE9A);
        fontRenderer.drawString(LangUtils.localize("gui.out") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxOutput()) + "/t", 51, 44, 0xFF3CFE9A);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= 6 && xAxis <= 6 + 6 && yAxis >= 16 && yAxis <= 16 + 54) {
            displayTooltip(tileEntity.bioFuelSlot.fluidStored > 0 ? LangUtils.localize("gui.bioGenerator.bioFuel") + ":" + tileEntity.bioFuelSlot.fluidStored : LangUtils.localize("gui.empty"), xAxis, yAxis);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        MekanismRenderer.color(MekanismFluids.Biofuel);
        GuiUtils.drawBarSprite(guiLeft + 6, guiTop + 16, 6, 54, tileEntity.getScaledFuelLevel(52), MekanismFluids.Biofuel.getSprite(), true);
    }

}
