package mekanism.multiblockmachine.client.gui.generator;

import mekanism.api.EnumColor;
import mekanism.api.TileNetworkList;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideHolder;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.base.IRedstoneControl;
import mekanism.common.config.MekanismConfig;
import mekanism.common.network.PacketTileEntity;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.multiblockmachine.common.inventory.container.generator.ContainerLargeWindGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeWindGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
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
public class GuiLargeWindGenerator extends GuiMekanismTile<TileEntityLargeWindGenerator> {

    private GuiButton ForcedRun;
    private GuiButton updateRun;

    public GuiLargeWindGenerator(InventoryPlayer inventory, TileEntityLargeWindGenerator tile) {
        super(tile, new ContainerLargeWindGenerator(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiEnergyInfo(() -> Arrays.asList(
                LangUtils.localize("gui.producing") + ": " +
                        MekanismUtils.getEnergyDisplay(tileEntity.getActive() ? MekanismConfig.current().generators.windGenerationMin.val() * tileEntity.getCurrentMultiplier() : 0) + "/t",
                LangUtils.localize("gui.maxOutput") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxOutput()) + "/t"), this, resource));
        addGuiElement(new GuiPowerBarLong(this, tileEntity, resource, 164, 9));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.POWER, this, resource, 142, 34 + 2).with(GuiSlot.SlotOverlay.POWER));
        addGuiElement(new GuiPlayerSlot(this, resource, 7, 83 + 9));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.STATE_HOLDER, this, resource, 18, 35 + 2));
        addGuiElement(new GuiInnerScreen(this, resource, 48, 21, 80, 62));
        ySize += 9;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(ForcedRun = new GuiDisableableButton(1, guiLeft - 21, guiTop + 90, 18, 18) {
            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
                if (tileEntity.getBladeDamage()) {
                    super.drawButton(mc, mouseX, mouseY, partialTicks);
                }
            }

            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                return tileEntity.getBladeDamage() && super.mousePressed(mc, mouseX, mouseY);
            }
        }.with(GuiDisableableButton.ImageOverlay.FORCE_RUN));

        buttonList.add(updateRun = new GuiDisableableButton(2, guiLeft - 21, guiTop + 64, 18, 18) {
            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
                if (tileEntity.getMachineStop2()) {
                    super.drawButton(mc, mouseX, mouseY, partialTicks);
                }
            }

            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                return tileEntity.getMachineStop2() && super.mousePressed(mc, mouseX, mouseY);
            }
        }.with(GuiDisableableButton.ImageOverlay.FORCE_RUN));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        fontRenderer.drawString(MekanismUtils.getEnergyDisplay(tileEntity.getEnergy(), tileEntity.getMaxEnergy()), 51, 26, 0xFF3CFE9A);
        fontRenderer.drawString(LangUtils.localize("gui.power") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getActive() ?
                MekanismConfig.current().generators.windGenerationMin.val() * tileEntity.getCurrentMultiplier() : 0) + "/t", 51, 35, 0xFF3CFE9A);
        fontRenderer.drawString(LangUtils.localize("gui.out") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxOutput()) + "/t", 51, 44, 0xFF3CFE9A);
        int size = 44;
        boolean isblacklist = tileEntity.isBlacklistDimension();
        if (!tileEntity.getActive()) {
            String info = "gui.skyBlocked";
            size += 9;
            if (isblacklist) {
                info = "gui.noWind";
            }
            if (tileEntity.getMachineStop() && !isblacklist) {
                info = "gui.protection";
            }
            if (tileEntity.getMachineStop2() && !isblacklist) {
                info = "gui.protection2";
            }

            if (!tileEntity.getMachineStop() && !isblacklist && tileEntity.getBladeDamage() && tileEntity.getActive()) {
                info = "gui.protection.off";
            }
            if (tileEntity.controlType == IRedstoneControl.RedstoneControl.HIGH && !tileEntity.redstone && !isblacklist && !tileEntity.getMachineStop()) {
                info = "control.high.desc";
            }
            if (tileEntity.controlType == IRedstoneControl.RedstoneControl.LOW && tileEntity.redstone && !isblacklist && !tileEntity.getMachineStop()) {
                info = "control.low.desc";
            }
            fontRenderer.drawString(EnumColor.DARK_RED + LangUtils.localize(info), 51, size, 0x00CD00);

        } else if (!tileEntity.getMachineStop() && !isblacklist && tileEntity.getBladeDamage() && tileEntity.getActive()) {
            size += 9;
            fontRenderer.drawString(EnumColor.DARK_RED + LangUtils.localize("gui.protection.off"), 51, size, 0x00CD00);
        }
        if (tileEntity.getBladeDamage()) {
            size += 9;
            fontRenderer.drawString(EnumColor.DARK_RED + LangUtils.localize("gui.Blades_damaged"), 51, size, 0x00CD00);
            size += 9;
            fontRenderer.drawString(EnumColor.DARK_RED + LangUtils.localize("gui.Blades_damaged_number") + ": " + tileEntity.getBladeDamageNumber(), 51, size, 0x00CD00);
        }
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= -21 && xAxis <= -3 && yAxis >= 116 && yAxis <= 134) {
            if (tileEntity.getBladeDamage()) {
                List<String> info = new ArrayList<>();
                info.add(LangUtils.localize("gui.Blades_damaged"));
                info.add(LangUtils.localize("gui.Blades_damaged_number") + ": " + tileEntity.getBladeDamageNumber());
                displayTooltips(info, xAxis, yAxis);
            }
        } else if (ForcedRun.isMouseOver()) {
            displayTooltip(tileEntity.getMachineStop() ? LangUtils.localize("gui.forced_run") : LangUtils.localize("gui.forced_run_off"), xAxis, yAxis);
        } else if (updateRun.isMouseOver() && tileEntity.getMachineStop2()) {
            displayTooltip(LangUtils.localize("gui.updateRun"), xAxis, yAxis);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.SLOT, "Slot_Icon.png"));
        drawTexturedModalRect(guiLeft + 20, guiTop + 37 + 2, tileEntity.getActive() ? 12 : 0, 88, 12, 12);
        if (tileEntity.getBladeDamage()) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 112, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), false));
            addGuiElement(new GuiSideHolder(this, getGuiLocation(), -26, 86, 26, 26, true));
        }
        if (tileEntity.getMachineStop2()) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.BUTTON_TAB, "holder_left.png"));
            int halfWidthLeft = 26 / 2;
            int halfHeightTop = 26 / 2;
            drawTexturedModalRect(guiLeft - 26,guiTop + 60, 0, 0, halfWidthLeft, halfHeightTop);
            drawTexturedRect(guiLeft - 26, guiTop + 60 + halfHeightTop, 0, 256 - halfHeightTop, halfWidthLeft, halfHeightTop);
            drawTexturedRect(guiLeft - 26 + halfWidthLeft, guiTop + 60, 256 - halfWidthLeft, 0, halfWidthLeft, halfHeightTop);
            drawTexturedRect(guiLeft - 26 + halfWidthLeft, guiTop + 60 + halfHeightTop, 256 - halfWidthLeft, 256 - halfHeightTop, halfWidthLeft, halfHeightTop);
        }

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button == ForcedRun) {
            TileNetworkList data = TileNetworkList.withContents(1);
            Mekanism.packetHandler.sendToServer(new PacketTileEntity.TileEntityMessage(tileEntity, data));
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
        } else if (button == updateRun) {
            TileNetworkList data = TileNetworkList.withContents(2);
            Mekanism.packetHandler.sendToServer(new PacketTileEntity.TileEntityMessage(tileEntity, data));
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
        }
    }
}
