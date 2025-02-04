package mekanism.client.gui;

import mekanism.api.EnumColor;
import mekanism.api.TileNetworkList;
import mekanism.client.ClientTickHandler;
import mekanism.client.MekanismClient;
import mekanism.client.gui.button.GuiColorButton;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.GuiPowerBar.IPowerInfoHandler;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiUpgradeTab;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.frequency.Frequency;
import mekanism.common.frequency.FrequencyManager;
import mekanism.common.inventory.container.ContainerNull;
import mekanism.common.inventory.container.ContainerTeleporter;
import mekanism.common.item.ItemPortableTeleporter;
import mekanism.common.network.PacketPortableTeleporter.PortableTeleporterMessage;
import mekanism.common.network.PacketPortableTeleporter.PortableTeleporterPacketType;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.security.IOwnerItem;
import mekanism.common.tile.TileEntityTeleporter;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class GuiTeleporter extends GuiMekanismTile<TileEntityTeleporter> {

    private final boolean isPortable;
    private EnumHand currentHand;
    private ItemStack itemStack = ItemStack.EMPTY;
    private EntityPlayer entityPlayer;
    private GuiDisableableButton publicButton;
    private GuiDisableableButton privateButton;
    private GuiDisableableButton setButton;
    private GuiDisableableButton deleteButton;
    private GuiDisableableButton teleportButton;
    private GuiDisableableButton checkboxButton;

    private GuiColorButton colorButton;
    private GuiScrollList scrollList;
    private GuiTextField frequencyField;
    private boolean privateMode;
    private Frequency clientFreq;
    private byte clientStatus;
    private List<Frequency> clientPublicCache = new ArrayList<>();
    private List<Frequency> clientPrivateCache = new ArrayList<>();
    private boolean isInit = true;

    private int yStart;

    public GuiTeleporter(InventoryPlayer inventory, TileEntityTeleporter tile) {
        super(tile, new ContainerTeleporter(inventory, tile));
        isPortable = false;
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource));
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiPowerBar(this, new IPowerInfoHandler() {
            @Override
            public String getTooltip() {
                return MekanismUtils.getEnergyDisplay(getEnergy(), getMaxEnergy());
            }

            @Override
            public double getLevel() {
                return getEnergy() / getMaxEnergy();
            }

            @Override
            public boolean powerbarWarning() {
                return tileEntity.getEnergy() == 0;
            }
        }, resource, 158, 26));

        addGuiElement(new GuiSlot(SlotType.POWER, this, resource, 152, 6).with(SlotOverlay.POWER));
        addGuiElement(scrollList = new GuiScrollList(this, resource, 28, 37, 120, 4));
        if (tileEntity.frequency != null) {
            privateMode = !tileEntity.frequency.publicFreq;
        }
        ySize += 74;
        addGuiElement(new GuiPlayerSlot(this, resource, 7, 157));
        addGuiElement(new GuiElementScreen(this, getGuiLocation(), 27, 36, 122, 42).isFrame());
        addGuiElement(new GuiInnerScreen(this, getGuiLocation(), 48, 111, 101, 13));
        yStart = 14;
    }

    public GuiTeleporter(EntityPlayer player, EnumHand hand, ItemStack stack) {
        super(null, new ContainerNull());
        isPortable = true;
        currentHand = hand;
        itemStack = stack;
        entityPlayer = player;
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiPowerBar(this, new IPowerInfoHandler() {
            @Override
            public String getTooltip() {
                return MekanismUtils.getEnergyDisplay(getEnergy(), getMaxEnergy());
            }

            @Override
            public double getLevel() {
                return getEnergy() / getMaxEnergy();
            }
        }, resource, 158, 26));
        addGuiElement(scrollList = new GuiScrollList(this, resource, 28, 37, 120, 4));
        ItemPortableTeleporter item = (ItemPortableTeleporter) itemStack.getItem();
        if (item.getFrequency(stack) != null) {
            privateMode = !item.getFrequency(stack).publicFreq;
            setFrequency(item.getFrequency(stack).name);
        } else {
            Mekanism.packetHandler.sendToServer(new PortableTeleporterMessage(PortableTeleporterPacketType.DATA_REQUEST, currentHand, clientFreq));
        }
        ySize = 172;
        addGuiElement(new GuiElementScreen(this, getGuiLocation(), 27, 36, 122, 42).isFrame());
        addGuiElement(new GuiInnerScreen(this, getGuiLocation(), 48, 111, 101, 13));
        yStart = 14;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(publicButton = new GuiDisableableButton(0, guiLeft + 27, guiTop + yStart, 60, 20, LangUtils.localize("gui.public")));
        buttonList.add(privateButton = new GuiDisableableButton(1, guiLeft + 89, guiTop + yStart, 60, 20, LangUtils.localize("gui.private")));
        int buttonWidth = isPortable ? 60 : 50;
        buttonList.add(setButton = new GuiDisableableButton(2, guiLeft + 27, guiTop + yStart + 113, buttonWidth, 18, LangUtils.localize("gui.set")));
        buttonList.add(deleteButton = new GuiDisableableButton(3, guiLeft + 29 + buttonWidth, guiTop + yStart + 113, buttonWidth, 18, LangUtils.localize("gui.delete")));
        if (!itemStack.isEmpty()) {
            buttonList.add(teleportButton = new GuiDisableableButton(4, guiLeft + 42, guiTop + 147, 92, 20, LangUtils.localize("gui.teleport")));
        }

        if (!isPortable) {
            buttonList.add(colorButton = new GuiColorButton(7, guiLeft + 132, guiTop + yStart + 114, () -> tileEntity.color));
        }
        frequencyField = new GuiTextField(5, fontRenderer, guiLeft + 50, guiTop + yStart + 99, 98, 11);
        frequencyField.setMaxStringLength(FrequencyManager.MAX_FREQ_LENGTH);
        frequencyField.setEnableBackgroundDrawing(false);
        buttonList.add(checkboxButton = new GuiDisableableButton(6, guiLeft + 137, guiTop + yStart + 98, 11, 11).with(GuiDisableableButton.ImageOverlay.CHECKMARK));
        updateButtons();
        if (!itemStack.isEmpty()) {
            if (!isInit) {
                Mekanism.packetHandler.sendToServer(new PortableTeleporterMessage(PortableTeleporterPacketType.DATA_REQUEST, currentHand, clientFreq));
            } else {
                isInit = false;
            }
        }
    }

    public String getSecurity(Frequency freq) {
        return !freq.publicFreq ? EnumColor.DARK_RED + LangUtils.localize("gui.private") : EnumColor.BRIGHT_GREEN + LangUtils.localize("gui.public");
    }

    public void updateButtons() {
        if (getOwner() == null) {
            return;
        }
        List<String> text = new ArrayList<>();
        if (privateMode) {
            for (Frequency freq : getPrivateCache()) {
                text.add(freq.name);
            }
        } else {
            for (Frequency freq : getPublicCache()) {
                text.add(freq.name + " (" + freq.clientOwner + ")");
            }
        }
        scrollList.setText(text);
        if (privateMode) {
            publicButton.enabled = true;
            privateButton.enabled = false;
        } else {
            publicButton.enabled = false;
            privateButton.enabled = true;
        }
        if (scrollList.hasSelection()) {
            Frequency freq = privateMode ? getPrivateCache().get(scrollList.getSelection()) : getPublicCache().get(scrollList.getSelection());
            setButton.enabled = getFrequency() == null || !getFrequency().equals(freq);
            deleteButton.enabled = getOwner().equals(freq.ownerUUID);
        } else {
            setButton.enabled = false;
            deleteButton.enabled = false;
        }
        if (!itemStack.isEmpty()) {
            teleportButton.enabled = clientFreq != null && clientStatus == 1;
        }
        checkboxButton.enabled = !frequencyField.getText().isEmpty();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        updateButtons();
        frequencyField.updateCursorCounter();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);
        updateButtons();
        frequencyField.mouseClicked(mouseX, mouseY, button);
        if (tileEntity != null && colorButton.isMouseOver() && button == 1) {
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(2, 1)));
            SoundHandler.playSound(net.minecraft.init.SoundEvents.UI_BUTTON_CLICK);
        }
    }


    @Override
    public void keyTyped(char c, int i) throws IOException {
        if (!frequencyField.isFocused() || i == Keyboard.KEY_ESCAPE) {
            super.keyTyped(c, i);
        }
        if (i == Keyboard.KEY_RETURN) {
            if (frequencyField.isFocused()) {
                setFrequency(frequencyField.getText());
                frequencyField.setText("");
            }
        }
        if (Character.isDigit(c) || Character.isLetter(c) || isTextboxKey(c, i) || FrequencyManager.SPECIAL_CHARS.contains(c)) {
            frequencyField.textboxKeyTyped(c, i);
        }
        updateButtons();
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == publicButton.id) {
            privateMode = false;
        } else if (guibutton.id == privateButton.id) {
            privateMode = true;
        } else if (guibutton.id == setButton.id) {
            int selection = scrollList.getSelection();
            if (selection != -1) {
                Frequency freq = privateMode ? getPrivateCache().get(selection) : getPublicCache().get(selection);
                setFrequency(freq.name);
            }
        } else if (guibutton.id == deleteButton.id) {
            int selection = scrollList.getSelection();
            if (selection != -1) {
                Frequency freq = privateMode ? getPrivateCache().get(selection) : getPublicCache().get(selection);
                if (tileEntity != null) {
                    TileNetworkList data = TileNetworkList.withContents(1, freq.name, freq.publicFreq);
                    Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, data));
                } else {
                    Mekanism.packetHandler.sendToServer(new PortableTeleporterMessage(PortableTeleporterPacketType.DEL_FREQ, currentHand, freq));
                    Mekanism.packetHandler.sendToServer(new PortableTeleporterMessage(PortableTeleporterPacketType.DATA_REQUEST, currentHand, null));
                }
                scrollList.clearSelection();
            }
        } else if (guibutton.id == 4) {
            if (clientFreq != null && clientStatus == 1) {
                mc.setIngameFocus();
                ClientTickHandler.portableTeleport(entityPlayer, currentHand, clientFreq);
            }
        } else if (guibutton.id == checkboxButton.id) {
            setFrequency(frequencyField.getText());
            frequencyField.setText("");
        } else if (guibutton.id == colorButton.id) {
            if (tileEntity != null) {
                Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, TileNetworkList.withContents(2, Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 2 : 0)));
            }
        }
        updateButtons();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(getName(), (xSize / 2) - (fontRenderer.getStringWidth(getName()) / 2), 4, 0x404040);

        fontRenderer.drawString(LangUtils.localize("gui.freq") + ":", 27, yStart + 67, 0x404040);
        fontRenderer.drawString(" " + (getFrequency() != null ? getFrequency().name : EnumColor.DARK_RED + LangUtils.localize("gui.none")), 27 + fontRenderer.getStringWidth(LangUtils.localize("gui.freq") + ":"), yStart + 67, 0x797979);

        fontRenderer.drawString((isPortable ? LangUtils.localize("gui.itemowner") : LangUtils.localize("gui.owner")) + ": " + (getFrequency() != null ? getOwnerUsername() : EnumColor.DARK_RED + LangUtils.localize("gui.none")), 27, yStart + 77, 0x404040);

        fontRenderer.drawString(LangUtils.localize("gui.security") + ":", 27, yStart + 87, 0x404040);
        fontRenderer.drawString(" " + (getFrequency() != null ? getSecurity(getFrequency()) : EnumColor.DARK_RED + LangUtils.localize("gui.none")),
                27 + fontRenderer.getStringWidth(LangUtils.localize("gui.security") + ":"), yStart + 87, 0x797979);
        String str = LangUtils.localize("gui.set") + ":";
        renderScaledText(str, 27, yStart + 100, 0x404040, 20);
        if (itemStack.isEmpty()) {
            fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 4, 0x404040);
        }
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= 6 && xAxis <= 24 && yAxis >= 6 && yAxis <= 24) {
            if (getFrequency() == null) {
                this.displayTooltip(EnumColor.DARK_RED + LangUtils.localize("gui.teleporter.noFreq"), xAxis, yAxis);
            } else {
                this.displayTooltip(getStatusDisplay(), xAxis, yAxis);
            }
        } else if (xAxis >= -21 && xAxis <= -3 && yAxis >= 116 && yAxis <= 134) {
            List<String> info = new ArrayList<>();
            boolean energy = getEnergy() == 0;
            if (energy) {
                info.add(LangUtils.localize("gui.no_energy"));
            }
            if (energy) {
                this.displayTooltips(info, xAxis, yAxis);
            }
        } else if (!isPortable && colorButton.isMouseOver()) {
            if (tileEntity != null) {
                List<String> info = new ArrayList<>();
                info.add(LangUtils.localize("gui.Teleportercolor"));
                info.add(LangUtils.localize("tooltip.configurator.viewColor") + ":" + tileEntity.color.getColoredName());
                this.displayTooltips(info, xAxis, yAxis);
            }
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        mc.getTextureManager().bindTexture(MekanismUtils.getResource(ResourceType.GUI, "Teleporter_Icon.png"));
        int y = getFrequency() == null ? 72 : getStatus() == 2 ? 0 : getStatus() == 3 ? 18 : getStatus() == 4 ? 36 : 54;
        drawTexturedModalRect(guiLeft + 6, guiTop + 6, 0, y, 18, 18);
        frequencyField.drawTextBox();
        MekanismRenderer.resetColor();
        boolean energy = getEnergy() == 0;
        if (energy) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 112, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), false));
        }
    }

    public String getStatusDisplay() {
        return switch (getStatus()) {
            case 1 -> EnumColor.DARK_GREEN + LangUtils.localize("gui.teleporter.ready");
            case 2 -> EnumColor.DARK_RED + LangUtils.localize("gui.teleporter.noFrame");
            case 3 -> EnumColor.DARK_RED + LangUtils.localize("gui.teleporter.noLink");
            case 4 -> EnumColor.DARK_RED + LangUtils.localize("gui.teleporter.needsEnergy");
            default -> EnumColor.DARK_RED + LangUtils.localize("gui.teleporter.noLink");
        };
    }

    private UUID getOwner() {
        if (tileEntity != null) {
            return tileEntity.getSecurity().getOwnerUUID();
        }
        return ((IOwnerItem) itemStack.getItem()).getOwnerUUID(itemStack);
    }

    private String getOwnerUsername() {
        if (tileEntity != null) {
            if (privateMode) {
                return EnumColor.BRIGHT_GREEN + tileEntity.getSecurity().getClientOwner();
            } else {
                return (Objects.equals(tileEntity.getSecurity().getClientOwner(), tileEntity.frequency.clientOwner) ? EnumColor.BRIGHT_GREEN : EnumColor.DARK_RED) + tileEntity.frequency.clientOwner;
            }
        }
        return EnumColor.BRIGHT_GREEN + MekanismClient.clientUUIDMap.get(((IOwnerItem) itemStack.getItem()).getOwnerUUID(itemStack));
    }

    private byte getStatus() {
        return tileEntity != null ? tileEntity.status : clientStatus;
    }

    public void setStatus(byte status) {
        clientStatus = status;
    }

    private List<Frequency> getPublicCache() {
        return tileEntity != null ? tileEntity.publicCache : clientPublicCache;
    }

    public void setPublicCache(List<Frequency> cache) {
        clientPublicCache = cache;
    }

    private List<Frequency> getPrivateCache() {
        return tileEntity != null ? tileEntity.privateCache : clientPrivateCache;
    }

    public void setPrivateCache(List<Frequency> cache) {
        clientPrivateCache = cache;
    }

    private Frequency getFrequency() {
        return tileEntity != null ? tileEntity.frequency : clientFreq;
    }

    public void setFrequency(Frequency newFrequency) {
        clientFreq = newFrequency;
    }

    public void setFrequency(String freq) {
        if (freq.isEmpty()) {
            return;
        }
        if (tileEntity != null) {
            TileNetworkList data = TileNetworkList.withContents(0, freq, !privateMode);
            Mekanism.packetHandler.sendToServer(new TileEntityMessage(tileEntity, data));
        } else {
            Frequency newFreq = new Frequency(freq, null).setPublic(!privateMode);
            Mekanism.packetHandler.sendToServer(new PortableTeleporterMessage(PortableTeleporterPacketType.SET_FREQ, currentHand, newFreq));
        }
    }

    private String getName() {
        return tileEntity != null ? tileEntity.getName() : itemStack.getDisplayName();
    }

    private double getEnergy() {
        if (!itemStack.isEmpty()) {
            return ((ItemPortableTeleporter) itemStack.getItem()).getEnergy(itemStack);
        }
        return tileEntity.getEnergy();
    }

    private double getMaxEnergy() {
        if (!itemStack.isEmpty()) {
            return ((ItemPortableTeleporter) itemStack.getItem()).getMaxEnergy(itemStack);
        }
        return tileEntity.getMaxEnergy();
    }

    public boolean isStackEmpty() {
        return itemStack.isEmpty();
    }
}
