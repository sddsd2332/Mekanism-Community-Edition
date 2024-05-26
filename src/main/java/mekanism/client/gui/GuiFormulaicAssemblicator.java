package mekanism.client.gui;

import mekanism.api.EnumColor;
import mekanism.api.TileNetworkList;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.client.gui.element.tab.GuiTransporterConfigTab;
import mekanism.client.gui.element.tab.GuiUpgradeTab;
import mekanism.common.Mekanism;
import mekanism.common.inventory.container.ContainerFormulaicAssemblicator;
import mekanism.common.item.ItemCraftingFormula;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.tile.machine.TileEntityFormulaicAssemblicator;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiFormulaicAssemblicator extends GuiMekanismTile<TileEntityFormulaicAssemblicator> {

    private GuiButton encodeFormulaButton;
    private GuiButton stockControlButton;
    private GuiButton fillEmptyButton;
    private GuiButton craftSingleButton;
    private GuiButton craftAvailableButton;
    private GuiButton autoModeButton;

    public GuiFormulaicAssemblicator(InventoryPlayer inventory, TileEntityFormulaicAssemblicator tile) {
        super(tile, new ContainerFormulaicAssemblicator(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource));
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiTransporterConfigTab(this, 34, tileEntity, resource));
        addGuiElement(new GuiPowerBar(this, tileEntity, resource, 159, 15));
        addGuiElement(new GuiEnergyInfo(() -> {
            String multiplier = MekanismUtils.getEnergyDisplay(tileEntity.energyPerTick);
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + multiplier + "/t",
                    LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, resource));
        addGuiElement(new GuiSlot(SlotType.POWER, this, resource, 151, 75).with(SlotOverlay.POWER));
        ySize += 64;
        addGuiElement(new GuiPlayerSlot(this, resource, 7, 147));
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 9; x++) {
                addGuiElement(new GuiSlot(SlotType.INPUT, this, resource, 7 + x * 18, 97 + y * 18));
            }
        }
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                addGuiElement(new GuiSlot(SlotType.NORMAL, this, getGuiLocation(), 25 + x * 18, 16 + y * 18));
            }
        }
        addGuiElement(new GuiSlot(SlotType.OUTPUT_LARGE_WIDE, this, getGuiLocation(), 115, 16));
        addGuiElement(new GuiProgress(new GuiProgress.IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return (double) tileEntity.operatingTicks / tileEntity.ticksRequired;
            }
        }, GuiProgress.ProgressBar.TALL_RIGHT, this, getGuiLocation(), 85, 42));
        addGuiElement(new GuiSlot(SlotType.EXTRA, this, getGuiLocation(), 5, 25).with(SlotOverlay.FORMULA));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(encodeFormulaButton = new GuiDisableableButton(0, guiLeft + 7, guiTop + 45, 14, 14).with(GuiDisableableButton.ImageOverlay.ENCODE_FORMULA));
        buttonList.add(stockControlButton = new GuiDisableableButton(1, guiLeft + 26, guiTop + 75, 16, 16).with(GuiDisableableButton.ImageOverlay.STOCK_CONTROL));
        buttonList.add(fillEmptyButton = new GuiDisableableButton(2, guiLeft + 44, guiTop + 75, 16, 16).with(GuiDisableableButton.ImageOverlay.FILL_EMPTY));
        buttonList.add(craftSingleButton = new GuiDisableableButton(3, guiLeft + 71, guiTop + 75, 16, 16).with(GuiDisableableButton.ImageOverlay.CRAFT_SINGLE));
        buttonList.add(craftAvailableButton = new GuiDisableableButton(4, guiLeft + 89, guiTop + 75, 16, 16).with(GuiDisableableButton.ImageOverlay.CRAFT_AVAILABLE));
        buttonList.add(autoModeButton = new GuiDisableableButton(5, guiLeft + 107, guiTop + 75, 16, 16).with(GuiDisableableButton.ImageOverlay.AUTO_TOGGLE));
        updateEnabledButtons();
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == encodeFormulaButton.id) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(1)));
        } else if (guibutton.id == stockControlButton.id) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(5)));
        } else if (guibutton.id == fillEmptyButton.id) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(4)));
        } else if (guibutton.id == craftSingleButton.id) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(2)));
        } else if (guibutton.id == craftAvailableButton.id) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(3)));
        } else if (guibutton.id == autoModeButton.id) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(0)));
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        updateEnabledButtons();
    }

    private void updateEnabledButtons() {
        encodeFormulaButton.enabled = !tileEntity.autoMode && tileEntity.isRecipe && canEncode();
        stockControlButton.enabled = tileEntity.formula != null;
        fillEmptyButton.enabled = !tileEntity.autoMode;
        craftSingleButton.enabled = !tileEntity.autoMode && tileEntity.isRecipe;
        craftAvailableButton.enabled = !tileEntity.autoMode && tileEntity.isRecipe;
        autoModeButton.enabled = tileEntity.formula != null;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (fillEmptyButton.isMouseOver() && fillEmptyButton.enabled) {
            displayTooltip(LangUtils.localize("gui.fillEmpty"), xAxis, yAxis);
        } else if (encodeFormulaButton.isMouseOver() && encodeFormulaButton.enabled) {
            displayTooltip(LangUtils.localize("gui.encodeFormula"), xAxis, yAxis);
        } else if (craftSingleButton.isMouseOver() && craftSingleButton.enabled) {
            displayTooltip(LangUtils.localize("gui.craftSingle"), xAxis, yAxis);
        } else if (craftAvailableButton.isMouseOver() && craftAvailableButton.enabled) {
            displayTooltip(LangUtils.localize("gui.craftAvailable"), xAxis, yAxis);
        } else if (autoModeButton.isMouseOver() && autoModeButton.enabled) {
            displayTooltip(LangUtils.localize("gui.autoModeToggle") + ": " + LangUtils.transOnOff(tileEntity.autoMode), xAxis, yAxis);
        } else if (stockControlButton.isMouseOver() && stockControlButton.enabled) {
            displayTooltip(LangUtils.localize("gui.stockControl") + ": " + LangUtils.transOnOff(tileEntity.stockControl), xAxis, yAxis);
        } else if (xAxis >= -21 && xAxis <= -3 && yAxis >= 116 && yAxis <= 134) {
            List<String> info = new ArrayList<>();
            boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
            if (energy) {
                info.add(LangUtils.localize("gui.no_energy"));
            }
            if (energy) {
                displayTooltips(info, xAxis, yAxis);
            }
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.SLOT, "Slot_Icon.png"));
        drawTexturedModalRect(guiLeft + 90, guiTop + 25, tileEntity.isRecipe ? 2 : 20, 39, 14, 12);
        if (tileEntity.formula != null) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = tileEntity.formula.input.get(i);
                if (!stack.isEmpty()) {
                    Slot slot = inventorySlots.inventorySlots.get(i + 20);
                    int guiX = guiLeft + slot.xPos;
                    int guiY = guiTop + slot.yPos;
                    if (slot.getStack().isEmpty() || !tileEntity.formula.isIngredientInPos(tileEntity.getWorld(), slot.getStack(), i)) {
                        drawColorIcon(guiX, guiY, EnumColor.DARK_RED, 0.8F);
                    }
                    renderItem(stack, guiX, guiY);
                }
            }
        }
        boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
        if (energy) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 112, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), false));
        }
    }

    private boolean canEncode() {
        if (tileEntity.formula != null) {
            return false;
        }
        ItemStack formulaStack = tileEntity.inventory.get(TileEntityFormulaicAssemblicator.SLOT_FORMULA);
        return !formulaStack.isEmpty() && formulaStack.getItem() instanceof ItemCraftingFormula && ((ItemCraftingFormula) formulaStack.getItem()).getInventory(formulaStack) == null;
    }

}
