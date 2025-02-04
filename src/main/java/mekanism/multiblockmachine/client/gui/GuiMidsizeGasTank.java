package mekanism.multiblockmachine.client.gui;

import mekanism.api.TileNetworkList;
import mekanism.api.gas.GasStack;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiUtils;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.bar.GuiBar;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.item.ItemGaugeDropper;
import mekanism.common.network.PacketTileEntity;
import mekanism.common.util.LangUtils;
import mekanism.multiblockmachine.common.inventory.container.ContainerMidsizeGasTank;
import mekanism.multiblockmachine.common.tile.TileEntityMidsizeGasTank;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiMidsizeGasTank extends GuiMekanismTile<TileEntityMidsizeGasTank> {

    public GuiDisableableButton mode;

    public GuiMidsizeGasTank(InventoryPlayer inventory,TileEntityMidsizeGasTank tile) {
        super(tile, new ContainerMidsizeGasTank(inventory,tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.INPUT, this, resource, 15, 16).with(GuiSlot.SlotOverlay.PLUS));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.OUTPUT, this, resource, 15, 46).with(GuiSlot.SlotOverlay.MINUS));
        addGuiElement(new GuiInnerScreen(this, resource, 42, 37, 118, 27));
        addGuiElement(new GuiPlayerSlot(this, resource));
        addGuiElement(new GuiPlayerArmmorSlot(this, resource, -26, 62, true));
        addGuiElement(new GuiBar(this, getGuiLocation(), 42, 16, 118, 12));
    }



    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String stored = "" + (tileEntity.gasTank.getStored() == Integer.MAX_VALUE ? LangUtils.localize("gui.infinite") : tileEntity.gasTank.getStored());
        String capacityInfo = stored + " / " + (tileEntity.GasStorage == Integer.MAX_VALUE ? LangUtils.localize("gui.infinite") : tileEntity.GasStorage);
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        renderScaledText(LangUtils.localize("gui.gas") + ": " + (tileEntity.gasTank.getGas() != null ? tileEntity.gasTank.getGas().getGas().getLocalizedName()
                : LangUtils.localize("gui.none")), 45, 40, 0x33ff99, 112);
        fontRenderer.drawString(capacityInfo, 45, 49, 0x33ff99);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, ySize - 96 + 2, 0x404040);
        String name = LangUtils.localize(tileEntity.dumping.getLangKey());
        fontRenderer.drawString(name, 156 - fontRenderer.getStringWidth(name), 73, 0x404040);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= 42 && xAxis <= 42 + 118 && yAxis >= 16 && yAxis <= 16 + 12) {
            this.displayTooltip(tileEntity.gasTank.getGas() != null ? tileEntity.gasTank.getGas().getGas().getLocalizedName() + ": " + (tileEntity.gasTank.getStored() == Integer.MAX_VALUE ? LangUtils.localize("gui.infinite") : tileEntity.gasTank.getStored()) : LangUtils.localize("gui.none"), xAxis, yAxis);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }


    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(mode = new GuiDisableableButton(0, guiLeft + 159, guiTop + 72, 10, 10, () -> tileEntity.dumping.ordinal()).with(GuiDisableableButton.ImageOverlay.GAS_MOD));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        GasStack gas = tileEntity.gasTank.getGas();
        if (gas != null) {
            MekanismRenderer.color(gas);
            int scale = (int) (((double) tileEntity.gasTank.getStored() / tileEntity.GasStorage) * 116);
            GuiUtils.drawGasBarSprite(guiLeft + 42, guiTop + 16, 118, 12, scale, gas, false);
            MekanismRenderer.resetColor();
        }
    }


    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == mode.id) {
            Mekanism.packetHandler.sendToServer(new PacketTileEntity.TileEntityMessage(tileEntity, TileNetworkList.withContents(0)));
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);
        if (button == 0 || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            int xAxis = x - guiLeft;
            int yAxis = y - guiTop;
            if (xAxis >= 42 && xAxis <= 42 + 118 && yAxis >= 16 && yAxis <= 16 + 12) {
                ItemStack stack = mc.player.inventory.getItemStack();
                if (!stack.isEmpty() && stack.getItem() instanceof ItemGaugeDropper) {
                    TileNetworkList data = TileNetworkList.withContents(1);
                    Mekanism.packetHandler.sendToServer(new PacketTileEntity.TileEntityMessage(tileEntity, data));
                    SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
                }
            }
        }
    }
}
