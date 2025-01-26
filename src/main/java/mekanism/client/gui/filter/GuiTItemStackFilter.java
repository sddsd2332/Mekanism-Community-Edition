package mekanism.client.gui.filter;

import mekanism.api.Coord4D;
import mekanism.api.EnumColor;
import mekanism.client.gui.button.GuiColorButton;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.GuiPlayerSlot;
import mekanism.client.gui.element.GuiSlot;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.content.transporter.TItemStackFilter;
import mekanism.common.network.PacketEditFilter.EditFilterMessage;
import mekanism.common.network.PacketLogisticalSorterGui.LogisticalSorterGuiMessage;
import mekanism.common.network.PacketLogisticalSorterGui.SorterGuiPacket;
import mekanism.common.network.PacketNewFilter.NewFilterMessage;
import mekanism.common.tile.TileEntityLogisticalSorter;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiTItemStackFilter extends GuiItemStackFilter<TItemStackFilter, TileEntityLogisticalSorter> {

    private GuiTextField minField;
    private GuiTextField maxField;
    private GuiButton sizeButton;

    public GuiTItemStackFilter(EntityPlayer player, TileEntityLogisticalSorter tile, int index) {
        super(player, tile);
        origFilter = (TItemStackFilter) tileEntity.filters.get(index);
        filter = ((TItemStackFilter) tileEntity.filters.get(index)).clone();
        addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, getGuiLocation(), 11, 18));
        addGuiElement(new GuiInnerScreen(this, getGuiLocation(), 33, 18, 93, 43));
        addGuiElement(new GuiPlayerSlot(this, getGuiLocation()));
    }

    public GuiTItemStackFilter(EntityPlayer player, TileEntityLogisticalSorter tile) {
        super(player, tile);
        isNew = true;
        filter = new TItemStackFilter();
        addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, getGuiLocation(), 11, 18));
        addGuiElement(new GuiInnerScreen(this, getGuiLocation(), 33, 18, 93, 43));
        addGuiElement(new GuiPlayerSlot(this, getGuiLocation()));
    }

    @Override
    protected void addButtons() {
        buttonList.add(saveButton = new GuiDisableableButton(0, guiLeft + 47, guiTop + 62, 60, 20, LangUtils.localize("gui.save")));
        buttonList.add(deleteButton = new GuiDisableableButton(1, guiLeft + 109, guiTop + 62, 60, 20, LangUtils.localize("gui.delete")));
        buttonList.add(backButton = new GuiDisableableButton(2, guiLeft + 5, guiTop + 5, 11, 11).with(GuiDisableableButton.ImageOverlay.SMALL_BACK));
        buttonList.add(defaultButton = new GuiDisableableButton(3, guiLeft + 11, guiTop + 64, 11, 11).with(GuiDisableableButton.ImageOverlay.DEFAULT));
        buttonList.add(colorButton = new GuiColorButton(4, guiLeft + 12, guiTop + 44, () -> filter.color));
        buttonList.add(sizeButton = new GuiDisableableButton(5, guiLeft + 128, guiTop + 44, 11, 11).with(GuiDisableableButton.ImageOverlay.SILK_TOUCH));
    }

    @Override
    public void initGui() {
        super.initGui();
        minField = new GuiTextField(2, fontRenderer, guiLeft + 149, guiTop + 19, 20, 11);
        minField.setMaxStringLength(2);
        minField.setText("" + filter.min);
        maxField = new GuiTextField(3, fontRenderer, guiLeft + 149, guiTop + 31, 20, 11);
        maxField.setMaxStringLength(2);
        maxField.setText("" + filter.max);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == saveButton.id) {
            if (!filter.getItemStack().isEmpty() && !minField.getText().isEmpty() && !maxField.getText().isEmpty()) {
                int min = Integer.parseInt(minField.getText());
                int max = Integer.parseInt(maxField.getText());
                if (max >= min && max <= 64) {
                    filter.min = Integer.parseInt(minField.getText());
                    filter.max = Integer.parseInt(maxField.getText());
                    if (isNew) {
                        Mekanism.packetHandler.sendToServer(new NewFilterMessage(Coord4D.get(tileEntity), filter));
                    } else {
                        Mekanism.packetHandler.sendToServer(new EditFilterMessage(Coord4D.get(tileEntity), false, origFilter, filter));
                    }
                    sendPacketToServer(0);
                } else if (min > max) {
                    status = EnumColor.DARK_RED + "Max<min";
                    ticker = 20;
                } else { //if(max > 64 || min > 64)
                    status = EnumColor.DARK_RED + "Max>64";
                    ticker = 20;
                }
            } else if (filter.getItemStack().isEmpty()) {
                status = EnumColor.DARK_RED + "No item";
                ticker = 20;
            } else if (minField.getText().isEmpty() || maxField.getText().isEmpty()) {
                status = EnumColor.DARK_RED + "Max/min";
                ticker = 20;
            }
        } else if (guibutton.id == sizeButton.id) {
            filter.sizeMode = !filter.sizeMode;
        } else {
            actionPerformedTransporter(guibutton, filter);
        }
    }

    @Override
    public void keyTyped(char c, int i) throws IOException {
        if ((!minField.isFocused() && !maxField.isFocused()) || i == Keyboard.KEY_ESCAPE) {
            super.keyTyped(c, i);
        }
        if (Character.isDigit(c) || isTextboxKey(c, i)) {
            minField.textboxKeyTyped(c, i);
            maxField.textboxKeyTyped(c, i);
        }
    }

    @Override
    protected void drawForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(LangUtils.localize("gui.itemFilter.min") + ":", 128, 20, 0x404040);
        fontRenderer.drawString(LangUtils.localize("gui.itemFilter.max") + ":", 128, 32, 0x404040);
        String sizeModeString = LangUtils.transOnOff(filter.sizeMode);
        if (tileEntity.singleItem && filter.sizeMode) {
            sizeModeString = EnumColor.RED + sizeModeString + "!";
        }

        fontRenderer.drawString(sizeModeString, 141, 46, 0x404040);
        drawTransporterForegroundLayer(mouseX, mouseY, filter.getItemStack());
        if (!filter.getItemStack().isEmpty()) {
            renderScaledText(filter.getItemStack().getDisplayName(), 35, 41, 0x00CD00, 89);
        }

        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (sizeButton.isMouseOver()) {
            String sizeModeTooltip = LangUtils.localize("gui.sizeMode");
            if (tileEntity.singleItem && filter.sizeMode) {
                sizeModeTooltip += " - " + LangUtils.localize("mekanism.gui.sizeModeConflict");
            }
            this.olddisplayTooltips(MekanismUtils.splitTooltip(sizeModeTooltip, ItemStack.EMPTY), xAxis, yAxis);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        minField.updateCursorCounter();
        maxField.updateCursorCounter();
    }

    @Override
    protected void drawItemStackBackground(int xAxis, int yAxis) {
        minField.drawTextBox();
        maxField.drawTextBox();
    }

    @Override
    protected void sendPacketToServer(int guiID) {
        Mekanism.packetHandler.sendToServer(new LogisticalSorterGuiMessage(SorterGuiPacket.SERVER, Coord4D.get(tileEntity), guiID, 0, 0));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);
        minField.mouseClicked(mouseX, mouseY, button);
        maxField.mouseClicked(mouseX, mouseY, button);
        if (button == 0 && overTypeInput(mouseX - guiLeft, mouseY - guiTop)) {
            ItemStack stack = mc.player.inventory.getItemStack();
            if (!stack.isEmpty() && !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                filter.setItemStack(stack.copy());
                filter.getItemStack().setCount(1);
            } else if (stack.isEmpty() && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                filter.setItemStack(ItemStack.EMPTY);
            }
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
        } else {
            transporterMouseClicked(button, filter);
        }
    }

}
