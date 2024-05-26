package mekanism.client.gui;

import mekanism.api.Coord4D;
import mekanism.api.TileNetworkList;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.GuiElementScreen;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.sound.SoundHandler;
import mekanism.common.HashList;
import mekanism.common.Mekanism;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.filter.*;
import mekanism.common.content.miner.MinerFilter;
import mekanism.common.inventory.container.ContainerNull;
import mekanism.common.network.PacketDigitalMinerGui.DigitalMinerGuiMessage;
import mekanism.common.network.PacketDigitalMinerGui.MinerGuiPacket;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.tile.machine.TileEntityDigitalMiner;
import mekanism.common.util.LangUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiDigitalMinerConfig extends GuiFilterHolder<TileEntityDigitalMiner, MinerFilter> {

    private GuiTextColorField radiusField;
    private GuiTextColorField minField;
    private GuiTextColorField maxField;
    private GuiDisableableButton newFilterButton;
    private GuiDisableableButton backButton;
    private GuiDisableableButton setRadiButton;
    private GuiDisableableButton setMinButton;
    private GuiDisableableButton setMaxButton;
    private GuiDisableableButton inverseButton;

    public GuiDigitalMinerConfig(EntityPlayer player, TileEntityDigitalMiner tile) {
        super(tile, new ContainerNull(player, tile));
        addGuiElement(new GuiInnerScreen(this, getGuiLocation(), 10, 17, 45, 140));
        addGuiElement(new GuiElementScreen(this, getGuiLocation(), 55, 17, 98, 118));
        addGuiElement(new GuiElementScreen(this, getGuiLocation(), 55, 135, 98, 22));
        addGuiElement(new GuiElementScreen(this, getGuiLocation(), 153, 17, 14, 140));
    }

    @Override
    protected HashList<MinerFilter> getFilters() {
        return tileEntity.filters;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        radiusField.updateCursorCounter();
        minField.updateCursorCounter();
        maxField.updateCursorCounter();
        updateEnabledButtons();
    }

    private void updateEnabledButtons() {
        setRadiButton.enabled = !radiusField.getText().isEmpty();
        setMinButton.enabled = !minField.getText().isEmpty();
        setMaxButton.enabled = !maxField.getText().isEmpty();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);

        radiusField.mouseClicked(mouseX, mouseY, button);
        minField.mouseClicked(mouseX, mouseY, button);
        maxField.mouseClicked(mouseX, mouseY, button);

        if (button == 0) {
            int xAxis = mouseX - guiLeft;
            int yAxis = mouseY - guiTop;

            if (xAxis >= 154 && xAxis <= 166 && yAxis >= getScroll() + 18 && yAxis <= getScroll() + 18 + 15) {
                if (needsScrollBars()) {
                    dragOffset = yAxis - (getScroll() + 18);
                    isDragging = true;
                } else {
                    scroll = 0;
                }
            }

            //Check for filter interaction
            for (int i = 0; i < 4; i++) {
                int index = getFilterIndex() + i;
                IFilter filter = tileEntity.filters.get(index);
                if (filter != null) {
                    int yStart = i * filterH + filterY;
                    if (xAxis >= filterX && xAxis <= filterX + filterW && yAxis >= yStart && yAxis <= yStart + filterH) {
                        //Check for sorting button
                        int arrowX = filterX + filterW - 12;
                        if (index > 0) {
                            if (xAxis >= arrowX && xAxis <= arrowX + 10 && yAxis >= yStart + 1 && yAxis <= yStart + 7) {
                                //Process up button click
                                sendDataFromClick(TileNetworkList.withContents(11, index));
                                return;
                            }
                        }
                        if (index < tileEntity.filters.size() - 1) {
                            if (xAxis >= arrowX && xAxis <= arrowX + 10 && yAxis >= yStart + 21 && yAxis <= yStart + 27) {
                                //Process down button click
                                sendDataFromClick(TileNetworkList.withContents(12, index));
                                return;
                            }
                        }
                        if (filter instanceof IItemStackFilter) {
                            sendPacket(MinerGuiPacket.SERVER_INDEX, 1, index, SoundEvents.UI_BUTTON_CLICK);
                        } else if (filter instanceof IOreDictFilter) {
                            sendPacket(MinerGuiPacket.SERVER_INDEX, 2, index, SoundEvents.UI_BUTTON_CLICK);
                        } else if (filter instanceof IMaterialFilter) {
                            sendPacket(MinerGuiPacket.SERVER_INDEX, 3, index, SoundEvents.UI_BUTTON_CLICK);
                        } else if (filter instanceof IModIDFilter) {
                            sendPacket(MinerGuiPacket.SERVER_INDEX, 6, index, SoundEvents.UI_BUTTON_CLICK);
                        }
                    }
                }
            }
        }
    }

    private void sendPacket(MinerGuiPacket type, int guiID, int extra, @Nullable SoundEvent sound) {
        Mekanism.packetHandler.sendToServer(new DigitalMinerGuiMessage(type, Coord4D.get(tileEntity), guiID, extra, 0));
        if (sound != null) {
            SoundHandler.playSound(sound);
        }
    }


    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(newFilterButton = new GuiDisableableButton(BUTTON_NEW, guiLeft + filterX, guiTop + 136, filterW, 20, LangUtils.localize("gui.newFilter")));
        buttonList.add(backButton = new GuiDisableableButton(1, guiLeft + 5, guiTop + 5, 11, 11).with(GuiDisableableButton.ImageOverlay.SMALL_BACK));
        buttonList.add(setRadiButton = new GuiDisableableButton(2, guiLeft + 39, guiTop + 67, 11, 11).with(GuiDisableableButton.ImageOverlay.CHECKMARK_DIGITAL));
        buttonList.add(setMinButton = new GuiDisableableButton(3, guiLeft + 39, guiTop + 92, 11, 11).with(GuiDisableableButton.ImageOverlay.CHECKMARK_DIGITAL));
        buttonList.add(setMaxButton = new GuiDisableableButton(4, guiLeft + 39, guiTop + 117, 11, 11).with(GuiDisableableButton.ImageOverlay.CHECKMARK_DIGITAL));
        buttonList.add(inverseButton = new GuiDisableableButton(5, guiLeft + 11, guiTop + 141, 14, 14).with(GuiDisableableButton.ImageOverlay.INVERSE));


        String prevRad = radiusField != null ? radiusField.getText() : "";
        String prevMin = minField != null ? minField.getText() : "";
        String prevMax = maxField != null ? maxField.getText() : "";

        radiusField = new GuiTextColorField(1, fontRenderer, guiLeft + 12, guiTop + 67, 38, 11);
        radiusField.setMaxStringLength(Integer.toString(MekanismConfig.current().general.digitalMinerMaxRadius.val()).length());
        radiusField.setFocusedboxbordercolor(0xFF24985C);
        radiusField.setBoxbordercolor(0xFF3CFE9A);
        radiusField.setTextColor(0xFF3CFE9A);
        radiusField.setText(prevRad);

        minField = new GuiTextColorField(2, fontRenderer, guiLeft + 12, guiTop + 92, 38, 11);
        minField.setMaxStringLength(3);
        minField.setFocusedboxbordercolor(0xFF24985C);
        minField.setBoxbordercolor(0xFF3CFE9A);
        minField.setTextColor(0xFF3CFE9A);
        minField.setText(prevMin);

        maxField = new GuiTextColorField(3, fontRenderer, guiLeft + 12, guiTop + 117, 38, 11);
        maxField.setMaxStringLength(3);
        maxField.setFocusedboxbordercolor(0xFF24985C);
        maxField.setBoxbordercolor(0xFF3CFE9A);
        maxField.setTextColor(0xFF3CFE9A);
        maxField.setText(prevMax);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == newFilterButton.id) {
            sendPacket(MinerGuiPacket.SERVER, 5, 0, null);
        } else if (guibutton.id == backButton.id) {
            sendPacket(MinerGuiPacket.SERVER, 4, 0, null);
        } else if (guibutton.id == setRadiButton.id) {
            setRadius();
        } else if (guibutton.id == setMinButton.id) {
            setMinY();
        } else if (guibutton.id == setMaxButton.id) {
            setMaxY();
        } else if (guibutton.id == inverseButton.id) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(10)));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String config = LangUtils.localize("gui.digitalMinerConfig");
        fontRenderer.drawString(config, (xSize / 2) - (fontRenderer.getStringWidth(config) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("gui.filters") + ":", 11, 19, 0xFF3CFE9A);
        fontRenderer.drawString("T: " + tileEntity.filters.size(), 11, 28, 0xFF3CFE9A);
        fontRenderer.drawString("I: " + LangUtils.transOnOff(tileEntity.inverse), 11, 131, 0xFF3CFE9A);
        fontRenderer.drawString(LangUtils.localize("gui.radi") + ": " + tileEntity.getRadius(), 11, 58, 0xFF3CFE9A);
        fontRenderer.drawString(LangUtils.localize("gui.min") + ": " + tileEntity.minY, 11, 83, 0xFF3CFE9A);
        fontRenderer.drawString(LangUtils.localize("gui.max") + ": " + +tileEntity.maxY, 11, 108, 0xFF3CFE9A);

        for (int i = 0; i < 4; i++) {
            IFilter filter = tileEntity.filters.get(getFilterIndex() + i);
            if (filter != null) {
                int yStart = i * filterH + filterY;
                if (filter instanceof IItemStackFilter) {
                    renderItem(((IItemStackFilter) filter).getItemStack(), 59, yStart + 3);
                    fontRenderer.drawString(LangUtils.localize("gui.itemFilter"), 78, yStart + 2, 0x404040);
                } else if (filter instanceof IOreDictFilter oreFilter) {
                    if (!oreDictStacks.containsKey(oreFilter)) {
                        updateStackList(oreFilter);
                    }
                    renderItem(oreDictStacks.get(filter).renderStack, 59, yStart + 3);
                    fontRenderer.drawString(LangUtils.localize("gui.oredictFilter"), 78, yStart + 2, 0x404040);
                } else if (filter instanceof IMaterialFilter) {
                    renderItem(((IMaterialFilter) filter).getMaterialItem(), 59, yStart + 3);
                    fontRenderer.drawString(LangUtils.localize("gui.materialFilter"), 78, yStart + 2, 0x404040);
                } else if (filter instanceof IModIDFilter modFilter) {
                    if (!modIDStacks.containsKey(modFilter)) {
                        updateStackList(modFilter);
                    }
                    renderItem(modIDStacks.get(filter).renderStack, 59, yStart + 3);
                    fontRenderer.drawString(LangUtils.localize("gui.modIDFilter"), 78, yStart + 2, 0x404040);
                }
            }
        }
        if (inverseButton.isMouseOver()) {
            displayTooltip(LangUtils.localize("gui.digitalMiner.inverse"), mouseX - guiLeft, mouseY - guiTop);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        radiusField.drawTextBox();
        minField.drawTextBox();
        maxField.drawTextBox();
        MekanismRenderer.resetColor();
    }

    @Override
    public void keyTyped(char c, int i) throws IOException {
        if ((!radiusField.isFocused() && !minField.isFocused() && !maxField.isFocused()) || i == Keyboard.KEY_ESCAPE) {
            super.keyTyped(c, i);
        }
        if (i == Keyboard.KEY_RETURN) {
            if (radiusField.isFocused()) {
                setRadius();
            } else if (minField.isFocused()) {
                setMinY();
            } else if (maxField.isFocused()) {
                setMaxY();
            }
        }
        if (Character.isDigit(c) || isTextboxKey(c, i)) {
            radiusField.textboxKeyTyped(c, i);
            minField.textboxKeyTyped(c, i);
            maxField.textboxKeyTyped(c, i);
        }
    }

    private void setRadius() {
        if (!radiusField.getText().isEmpty()) {
            int toUse = Math.max(0, Math.min(Integer.parseInt(radiusField.getText()), MekanismConfig.current().general.digitalMinerMaxRadius.val()));
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(6, toUse)));
            radiusField.setText("");
        }
    }

    private void setMinY() {
        if (!minField.getText().isEmpty()) {
            int toUse = Math.max(0, Math.min(Integer.parseInt(minField.getText()), tileEntity.maxY));
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(7, toUse)));
            minField.setText("");
        }
    }

    private void setMaxY() {
        if (!maxField.getText().isEmpty()) {
            int toUse = Math.max(tileEntity.minY, Math.min(Integer.parseInt(maxField.getText()), 255));
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(8, toUse)));
            maxField.setText("");
        }
    }
}
