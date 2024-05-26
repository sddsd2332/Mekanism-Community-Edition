package mekanism.client.gui;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.Coord4D;
import mekanism.api.EnumColor;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.GuiProgress.IProgressInfoHandler;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.client.gui.element.tab.GuiTransporterConfigTab;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.inventory.container.ContainerOredictionificator;
import mekanism.common.network.PacketOredictionificatorGui.OredictionificatorGuiMessage;
import mekanism.common.network.PacketOredictionificatorGui.OredictionificatorGuiPacket;
import mekanism.common.tile.machine.TileEntityOredictionificator;
import mekanism.common.tile.machine.TileEntityOredictionificator.OredictionificatorFilter;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GuiOredictionificator extends GuiMekanismTile<TileEntityOredictionificator> {

    private Map<OredictionificatorFilter, ItemStack> renderStacks = new Object2ObjectOpenHashMap<>();
    private boolean isDragging = false;
    private int dragOffset = 0;
    private float scroll;

    public GuiOredictionificator(InventoryPlayer inventory, TileEntityOredictionificator tile) {
        super(tile, new ContainerOredictionificator(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiProgress(new IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return tileEntity.didProcess ? 1 : 0;
            }
        }, ProgressBar.LARGE_RIGHT, this, resource, 62, 118));
        addGuiElement(new GuiSlot(SlotType.INPUT, this, resource, 25, 114));
        addGuiElement(new GuiSlot(SlotType.OUTPUT, this, resource, 133, 114));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiTransporterConfigTab(this, 34, tileEntity, resource));
        ySize += 64;
        addGuiElement(new GuiPlayerSlot(this, resource, 7, 147));
        addGuiElement(new GuiElementScreen(this, resource, 9, 17, 144, 68));
        addGuiElement(new GuiElementScreen(this, resource, 9, 85, 144, 22));
        addGuiElement(new GuiElementScreen(this, resource, 153, 17, 14, 90));
    }

    private boolean overFilter(int xAxis, int yAxis, int yStart) {
        return xAxis > 10 && xAxis <= 152 && yAxis > yStart && yAxis <= yStart + 22;
    }

    private int getScroll() {
        return Math.max(Math.min((int) (scroll * 73), 73), 0);
    }

    private int getFilterIndex() {
        return tileEntity.filters.size() <= 3 ? 0 : (int) (tileEntity.filters.size() * scroll - (3F / (float) tileEntity.filters.size()) * scroll);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(new GuiDisableableButton(0, guiLeft + 10, guiTop + 86, 142, 20, LangUtils.localize("gui.newFilter")));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == 0) {
            Mekanism.packetHandler.sendToServer(new OredictionificatorGuiMessage(OredictionificatorGuiPacket.SERVER, Coord4D.get(tileEntity), 1, 0, 0));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        for (int i = 0; i < 3; i++) {
            if (tileEntity.filters.get(getFilterIndex() + i) != null) {
                OredictionificatorFilter filter = tileEntity.filters.get(getFilterIndex() + i);
                if (!renderStacks.containsKey(filter)) {
                    updateRenderStacks();
                }
                int yStart = i * 22 + 18;
                renderItem(renderStacks.get(filter), 13, yStart + 3);
                fontRenderer.drawString(LangUtils.localize("gui.filter"), 32, yStart + 2, 0x404040);
                renderScaledText(filter.filter, 32, yStart + 2 + 9, 0x404040, 117);
            }
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Scroll_Icon.png"));
        drawTexturedModalRect(guiLeft + 154, guiTop + 18 + getScroll(), 232 + (tileEntity.filters.size() > 3 ? 0 : 12), 0, 12, 15);
        for (int i = 0; i < 3; i++) {
            if (tileEntity.filters.get(getFilterIndex() + i) != null) {
                int yStart = i * 22 + 18;
                boolean mouseOver = overFilter(xAxis, yAxis, yStart);
                if (mouseOver) {
                    MekanismRenderer.color(EnumColor.GREY);
                }
                drawTexturedModalRect(guiLeft + 10, guiTop + yStart, 0, 30, 142, 22);
                if (mouseOver) {
                    MekanismRenderer.resetColor();
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);
        if (button == 0) {
            int xAxis = mouseX - guiLeft;
            int yAxis = mouseY - guiTop;
            if (xAxis >= 154 && xAxis <= 166 && yAxis >= getScroll() + 18 && yAxis <= getScroll() + 18 + 15) {
                if (tileEntity.filters.size() > 3) {
                    dragOffset = yAxis - (getScroll() + 18);
                    isDragging = true;
                } else {
                    scroll = 0;
                }
            }

            for (int i = 0; i < 3; i++) {
                if (tileEntity.filters.get(getFilterIndex() + i) != null && overFilter(xAxis, yAxis, i * 22 + 18)) {
                    SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
                    Mekanism.packetHandler.sendToServer(new OredictionificatorGuiMessage(OredictionificatorGuiPacket.SERVER_INDEX, Coord4D.get(tileEntity), 1, getFilterIndex() + i, 0));
                }
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int button, long ticks) {
        super.mouseClickMove(mouseX, mouseY, button, ticks);
        if (isDragging) {
            int yAxis = mouseY - (height - ySize) / 2;
            scroll = Math.min(Math.max((float) (yAxis - 18 - dragOffset) / 73F, 0), 1);
        }
    }

    @Override
    protected void mouseReleased(int x, int y, int type) {
        super.mouseReleased(x, y, type);
        if (type == 0 && isDragging) {
            dragOffset = 0;
            isDragging = false;
        }
    }


    public void updateRenderStacks() {
        renderStacks.clear();
        for (OredictionificatorFilter filter : tileEntity.filters) {
            if (filter.filter == null || filter.filter.isEmpty()) {
                renderStacks.put(filter, ItemStack.EMPTY);
                continue;
            }
            List<ItemStack> stacks = OreDictionary.getOres(filter.filter, false);
            if (stacks.isEmpty()) {
                renderStacks.put(filter, ItemStack.EMPTY);
                continue;
            }
            if (stacks.size() - 1 >= filter.index) {
                renderStacks.put(filter, stacks.get(filter.index).copy());
            } else {
                renderStacks.put(filter, ItemStack.EMPTY);
            }
        }
    }
}
