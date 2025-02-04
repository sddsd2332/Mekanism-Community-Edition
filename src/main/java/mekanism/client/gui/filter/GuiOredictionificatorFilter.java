package mekanism.client.gui.filter;

import mekanism.api.Coord4D;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.GuiPlayerSlot;
import mekanism.client.gui.element.GuiSlot;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
import mekanism.common.inventory.container.ContainerFilter;
import mekanism.common.network.PacketEditFilter.EditFilterMessage;
import mekanism.common.network.PacketNewFilter.NewFilterMessage;
import mekanism.common.network.PacketSimpleGui.SimpleGuiMessage;
import mekanism.common.tile.machine.TileEntityOredictionificator;
import mekanism.common.tile.machine.TileEntityOredictionificator.OredictionificatorFilter;
import mekanism.common.util.ItemRegistryUtils;
import mekanism.common.util.LangUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiOredictionificatorFilter extends GuiTextFilterBase<OredictionificatorFilter, TileEntityOredictionificator> {

    private GuiButton prevButton;
    private GuiButton nextButton;
    private GuiButton checkboxButton;

    public GuiOredictionificatorFilter(EntityPlayer player, TileEntityOredictionificator tile, int index) {
        super(tile, new ContainerFilter(player.inventory, tile));
        origFilter = tileEntity.filters.get(index);
        filter = tileEntity.filters.get(index).clone();
        updateRenderStack();
        addGuiElement(new GuiPlayerSlot(this, getGuiLocation()));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, getGuiLocation(), 44, 18));
    }

    public GuiOredictionificatorFilter(EntityPlayer player, TileEntityOredictionificator tile) {
        super(tile, new ContainerFilter(player.inventory, tile));
        filter = new OredictionificatorFilter();
        isNew = true;
        addGuiElement(new GuiPlayerSlot(this, getGuiLocation()));
        addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, getGuiLocation(), 44, 18));
    }

    @Override
    protected void addButtons() {
        buttonList.add(saveButton = new GuiDisableableButton(0, guiLeft + 31, guiTop + 62, 54, 20, LangUtils.localize("gui.save")));
        buttonList.add(deleteButton = new GuiDisableableButton(1, guiLeft + 89, guiTop + 62, 54, 20, LangUtils.localize("gui.delete")));
        buttonList.add(backButton = new GuiDisableableButton(2, guiLeft + 5, guiTop + 5, 11, 11).with(GuiDisableableButton.ImageOverlay.SMALL_BACK));
        buttonList.add(prevButton = new GuiDisableableButton(3, guiLeft + 31, guiTop + 21, 12, 12).with(GuiDisableableButton.ImageOverlay.LEFT));
        buttonList.add(nextButton = new GuiDisableableButton(4, guiLeft + 63, guiTop + 21, 12, 12).with(GuiDisableableButton.ImageOverlay.RIGHT));
        buttonList.add(checkboxButton = new GuiDisableableButton(5, guiLeft + 130, guiTop + 48, 11, 11).with(GuiDisableableButton.ImageOverlay.CHECKMARK_DIGITAL));
    }

    @Override
    protected void sendPacketToServer(int guiID) {
        Mekanism.packetHandler.sendToServer(new SimpleGuiMessage(Coord4D.get(tileEntity), 0, guiID));
    }

    @Override
    public void setText() {
        String newFilter = text.getText();
        if (TileEntityOredictionificator.possibleFilters.stream().anyMatch(newFilter::startsWith)) {
            filter.filter = newFilter;
            filter.index = 0;
            text.setText("");
            updateRenderStack();
        }
        updateButtons();
    }

    public void updateButtons() {
        saveButton.enabled = filter.filter != null && !filter.filter.isEmpty();
        checkboxButton.enabled = !text.getText().isEmpty();
        deleteButton.enabled = !isNew;
    }

    @Override
    protected GuiTextField createTextField() {
        return new GuiTextField(2, fontRenderer, guiLeft + 34, guiTop + 48, 109, 11);
    }

    @Override
    public void initGui() {
        super.initGui();
        text.setEnableBackgroundDrawing(true);
        text.setTextColor(14737632);
        updateButtons();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String text = (isNew ? LangUtils.localize("gui.new") : LangUtils.localize("gui.edit")) + " " + LangUtils.localize("gui.filter");
        fontRenderer.drawString(text, (xSize / 2) - (fontRenderer.getStringWidth(text) / 2), 6, 0x404040);
        fontRenderer.drawString(LangUtils.localize("gui.index") + ": " + filter.index, 79, 23, 0x404040);
        if (filter.filter != null) {
            renderScaledText(filter.filter, 32, 38, 0x404040, 111);
        }
        renderItem(renderStack, 45, 19);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= 31 && xAxis <= 43 && yAxis >= 21 && yAxis <= 33) {
            this.displayTooltip(LangUtils.localize("gui.lastItem"), xAxis, yAxis);
        } else if (xAxis >= 63 && xAxis <= 75 && yAxis >= 21 && yAxis <= 33) {
            this.displayTooltip(LangUtils.localize("gui.nextItem"), xAxis, yAxis);
        } else if (xAxis >= 33 && xAxis <= 129 && yAxis >= 48 && yAxis <= 60) {
            this.displayTooltip(LangUtils.localize("gui.oreDictCompat"), xAxis, yAxis);
        } else if (xAxis >= 45 && xAxis <= 61 && yAxis >= 19 && yAxis <= 35) {
            if (!renderStack.isEmpty()) {
                String name = ItemRegistryUtils.getMod(renderStack);
                String extra = name.equals("null") ? "" : " (" + name + ")";
                this.displayTooltip(renderStack.getDisplayName() + extra, xAxis, yAxis);
            }
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        text.drawTextBox();
        MekanismRenderer.resetColor();
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == saveButton.id) {
            if (!text.getText().isEmpty()) {
                setText();
            }
            if (filter.filter != null && !filter.filter.isEmpty()) {
                if (isNew) {
                    Mekanism.packetHandler.sendToServer(new NewFilterMessage(Coord4D.get(tileEntity), filter));
                } else {
                    Mekanism.packetHandler.sendToServer(new EditFilterMessage(Coord4D.get(tileEntity), false, origFilter, filter));
                }
                sendPacketToServer(52);
            }
        } else if (guibutton.id == deleteButton.id) {
            Mekanism.packetHandler.sendToServer(new EditFilterMessage(Coord4D.get(tileEntity), true, origFilter, null));
            sendPacketToServer(52);
        } else if (guibutton.id == backButton.id) {
            sendPacketToServer(52);
        } else if (guibutton.id == prevButton.id) {
            if (filter.filter != null) {
                List<ItemStack> ores = OreDictionary.getOres(filter.filter, false);
                if (filter.index > 0) {
                    filter.index--;
                } else {
                    filter.index = ores.size() - 1;
                }
                updateRenderStack();
            }
        } else if (guibutton.id == nextButton.id) {
            if (filter.filter != null) {
                List<ItemStack> ores = OreDictionary.getOres(filter.filter, false);
                if (filter.index < ores.size() - 1) {
                    filter.index++;
                } else {
                    filter.index = 0;
                }
                updateRenderStack();
            }
        } else if (guibutton.id == checkboxButton.id) {
            setText();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);
        text.mouseClicked(mouseX, mouseY, button);
    }


    private void updateRenderStack() {
        if (filter.filter == null || filter.filter.isEmpty()) {
            renderStack = ItemStack.EMPTY;
            return;
        }
        List<ItemStack> stacks = OreDictionary.getOres(filter.filter, false);
        if (stacks.isEmpty()) {
            renderStack = ItemStack.EMPTY;
            return;
        }
        if (stacks.size() - 1 >= filter.index) {
            renderStack = stacks.get(filter.index).copy();
        } else {
            renderStack = ItemStack.EMPTY;
        }
    }
}
