package mekanism.client.gui;

import mekanism.api.TileNetworkList;
import mekanism.client.gui.button.GuiDisableableButton;
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
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.inventory.container.ContainerMetallurgicInfuser;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.tile.machine.TileEntityMetallurgicInfuser;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiMetallurgicInfuser extends GuiMekanismTile<TileEntityMetallurgicInfuser> {

    private GuiDisableableButton toggleButton;

    public GuiMetallurgicInfuser(InventoryPlayer inventory, TileEntityMetallurgicInfuser tile) {
        super(tile, new ContainerMetallurgicInfuser(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiTransporterConfigTab(this, 34, tileEntity, resource));
        addGuiElement(new GuiPowerBar(this, tileEntity, resource, 164, 15));
        addGuiElement(new GuiEnergyInfo(() -> {
            String multiplier = MekanismUtils.getEnergyDisplay(tileEntity.energyPerTick);
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + multiplier + "/t",
                    LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, resource));
        addGuiElement(new GuiSlot(SlotType.EXTRA, this, resource, 16, 34));
        addGuiElement(new GuiSlot(SlotType.INPUT, this, resource, 50, 42));
        addGuiElement(new GuiSlot(SlotType.POWER, this, resource, 142, 34).with(SlotOverlay.POWER));
        addGuiElement(new GuiSlot(SlotType.OUTPUT, this, resource, 108, 42));
        addGuiElement(new GuiProgress(new IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return tileEntity.getScaledProgress();
            }
        }, ProgressBar.MEDIUM, this, resource, 70, 46));
        addGuiElement(new GuiPlayerSlot(this, resource));
        addGuiElement(new GuiBar(this, getGuiLocation(), 6, 16, 6, 54));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(toggleButton = new GuiDisableableButton(0, guiLeft + 147, guiTop + 72, 21, 10).with(GuiDisableableButton.ImageOverlay.DUMP));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == toggleButton.id) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(0)));
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
        }
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (toggleButton.isMouseOver()) {
            displayTooltip(LangUtils.localize("gui.remove"), xAxis, yAxis);
        } else if (xAxis >= -21 && xAxis <= -3 && yAxis >= 116 && yAxis <= 134) {
            List<String> info = new ArrayList<>();
            boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
            boolean input = (tileEntity.infuseStored.getAmount() == 0) && (tileEntity.inventory.get(2).getCount() != 0);
            boolean outslot = tileEntity.inventory.get(3).getCount() == tileEntity.inventory.get(3).getMaxStackSize();
            if (energy) {
                info.add(LangUtils.localize("gui.no_energy"));
            }
            if (input) {
                info.add(LangUtils.localize("gui.infuse_no_item"));
            }
            if (outslot) {
                info.add(LangUtils.localize("gui.item_no_space"));
            }
            if (input || energy || outslot) {
                displayTooltips(info, xAxis, yAxis);
            }
        } else if (xAxis >= 6 && xAxis <= 6 + 6 && yAxis >= 16 && yAxis <= 16 + 54) {
            displayTooltip(tileEntity.infuseStored.getType() != null ? tileEntity.infuseStored.getType().getLocalizedName() + ": " + tileEntity.infuseStored.getAmount() : LangUtils.localize("gui.empty"), xAxis, yAxis);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        drawTexturedModalRect(guiLeft + 6, guiTop + 16, 177, 0, 6, 54);
        if (tileEntity.infuseStored.getType() != null) {
            mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            int displayInt = tileEntity.getScaledInfuseLevel(52);
            GuiUtils.drawTiledSprite(guiLeft + 7, guiTop + 17, 52, 4, displayInt, tileEntity.infuseStored.getType().sprite, GuiUtils.TilingDirection.DOWN_RIGHT);
        }

        boolean input = (tileEntity.infuseStored.getAmount() == 0) && (tileEntity.inventory.get(2).getCount() != 0);
        boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
        boolean outslot = tileEntity.inventory.get(3).getCount() == tileEntity.inventory.get(3).getMaxStackSize();
        if (outslot) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(ResourceType.SLOT, "Slot_Icon.png"));
            drawTexturedModalRect(guiLeft + 108, guiTop + 42, 158, 0, 18, 18);
        }
        if (input) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Warning_Background.png"));
            drawTexturedModalRect(guiLeft + 6 + 1, guiTop + 16 + 1, 0, 0, 4, 52);
        }
        if (outslot || input || energy) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 112, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), false));
        }
    }

}
