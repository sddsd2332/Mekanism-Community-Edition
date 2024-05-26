package mekanism.client.gui.robit;

import mekanism.client.gui.GuiMekanism;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.GuiPlayerSlot;
import mekanism.client.gui.element.GuiSlot;
import mekanism.client.gui.element.bar.GuiBar;
import mekanism.client.gui.element.tab.GuiSideHolder;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
import mekanism.common.entity.EntityRobit;
import mekanism.common.inventory.container.robit.ContainerRobitMain;
import mekanism.common.network.PacketRobit.RobitMessage;
import mekanism.common.network.PacketRobit.RobitPacketType;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiRobitMain extends GuiMekanism {

    private final EntityRobit robit;

    private boolean displayNameChange;
    private GuiTextField nameChangeField;
    private GuiDisableableButton confirmName;
    private GuiDisableableButton teleportHomeButton;
    private GuiDisableableButton pickupButton;
    private GuiDisableableButton renameButton;
    private GuiDisableableButton followButton;
    private GuiDisableableButton mainButton;
    private GuiDisableableButton craftingButton;
    private GuiDisableableButton inventoryButton;
    private GuiDisableableButton smeltingButton;
    private GuiDisableableButton repairButton;

    public GuiRobitMain(InventoryPlayer inventory, EntityRobit entity) {
        super(new ContainerRobitMain(inventory, entity));
        robit = entity;
        addGuiElement(new GuiSlot(GuiSlot.SlotType.POWER, this, getGuiLocation(), 152, 16).with(GuiSlot.SlotOverlay.POWER));
        addGuiElement(new GuiPlayerSlot(this, getGuiLocation()));
        addGuiElement(new GuiInnerScreen(this, getGuiLocation(), 27, 16, 122, 56));
        addGuiElement(new GuiSideHolder(this, getGuiLocation(), 176, 6, 25, 106));
        addGuiElement(new GuiBar(this, getGuiLocation(), 27, 74, 122, 6));
    }

    private void toggleNameChange() {
        displayNameChange = !displayNameChange;
        confirmName.visible = displayNameChange;
        nameChangeField.setFocused(displayNameChange);
    }

    private void changeName() {
        if (!nameChangeField.getText().isEmpty()) {
            Mekanism.packetHandler.sendToServer(new RobitMessage(robit.getEntityId(), nameChangeField.getText()));
            toggleNameChange();
            nameChangeField.setText("");
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.id == confirmName.id) {
            changeName();
        } else if (guibutton.id == teleportHomeButton.id) {
            Mekanism.packetHandler.sendToServer(new RobitMessage(RobitPacketType.GO_HOME, robit.getEntityId()));
            mc.displayGuiScreen(null);
        } else if (guibutton.id == pickupButton.id) {
            Mekanism.packetHandler.sendToServer(new RobitMessage(RobitPacketType.DROP_PICKUP, robit.getEntityId()));
        } else if (guibutton.id == renameButton.id) {
            toggleNameChange();
        } else if (guibutton.id == followButton.id) {
            Mekanism.packetHandler.sendToServer(new RobitMessage(RobitPacketType.FOLLOW, robit.getEntityId()));
        } else if (guibutton.id == mainButton.id) {
            //Clicking main button doesn't do anything while already on the main GUI
        } else if (guibutton.id == craftingButton.id) {
            Mekanism.packetHandler.sendToServer(new RobitMessage(robit.getEntityId(), 22));
        } else if (guibutton.id == inventoryButton.id) {
            Mekanism.packetHandler.sendToServer(new RobitMessage(robit.getEntityId(), 23));
        } else if (guibutton.id == smeltingButton.id) {
            Mekanism.packetHandler.sendToServer(new RobitMessage(robit.getEntityId(), 24));
        } else if (guibutton.id == repairButton.id) {
            Mekanism.packetHandler.sendToServer(new RobitMessage(robit.getEntityId(), 25));
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(confirmName = new GuiDisableableButton(0, guiLeft + 58, guiTop + 47, 60, 20, LangUtils.localize("gui.confirm")));
        confirmName.visible = displayNameChange;

        nameChangeField = new GuiTextField(1, fontRenderer, guiLeft + 48, guiTop + 21, 80, 12);
        nameChangeField.setMaxStringLength(12);
        nameChangeField.setFocused(true);

        buttonList.add(teleportHomeButton = new GuiDisableableButton(2, guiLeft + 6, guiTop + 16, 18, 18).with(GuiDisableableButton.ImageOverlay.HOME));
        buttonList.add(pickupButton = new GuiDisableableButton(3, guiLeft + 6, guiTop + 35, 18, 18).with(GuiDisableableButton.ImageOverlay.DROP));
        buttonList.add(renameButton = new GuiDisableableButton(4, guiLeft + 6, guiTop + 54, 18, 18).with(GuiDisableableButton.ImageOverlay.RENAME));
        buttonList.add(followButton = new GuiDisableableButton(5, guiLeft + 152, guiTop + 54, 18, 18).with(GuiDisableableButton.ImageOverlay.FOLLOW));
        buttonList.add(mainButton = new GuiDisableableButton(6, guiLeft + 179, guiTop + 10, 18, 18).with(GuiDisableableButton.ImageOverlay.MAIN));
        buttonList.add(craftingButton = new GuiDisableableButton(7, guiLeft + 179, guiTop + 30, 18, 18).with(GuiDisableableButton.ImageOverlay.CRAFTING));
        buttonList.add(inventoryButton = new GuiDisableableButton(8, guiLeft + 179, guiTop + 50, 18, 18).with(GuiDisableableButton.ImageOverlay.INVENTORY));
        buttonList.add(smeltingButton = new GuiDisableableButton(9, guiLeft + 179, guiTop + 70, 18, 18).with(GuiDisableableButton.ImageOverlay.SMELTING));
        buttonList.add(repairButton = new GuiDisableableButton(10, guiLeft + 179, guiTop + 90, 18, 18).with(GuiDisableableButton.ImageOverlay.REPAIR));
    }

    @Override
    public void keyTyped(char c, int i) throws IOException {
        if (!displayNameChange) {
            super.keyTyped(c, i);
        } else {
            if (i == Keyboard.KEY_RETURN) {
                changeName();
            } else if (i == Keyboard.KEY_ESCAPE) {
                mc.player.closeScreen();
            }
            nameChangeField.textboxKeyTyped(c, i);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(LangUtils.localize("gui.robit"), 76, 6, 0x404040);

        if (!displayNameChange) {
            CharSequence owner = robit.getOwnerName().length() > 14 ? robit.getOwnerName().subSequence(0, 14) : robit.getOwnerName();
            fontRenderer.drawString(LangUtils.localize("gui.robit.greeting") + robit.getName() + LangUtils.localize("!"), 29, 18, 0xFF3CFE9A);
            fontRenderer.drawString(LangUtils.localize("gui.energy") + ": " + MekanismUtils.getEnergyDisplay(robit.getEnergy(), robit.MAX_ELECTRICITY), 29, 36 - 4, 0xFF3CFE9A);
            fontRenderer.drawString(LangUtils.localize("gui.robit.following") + " : " + robit.getFollowing(), 29, 45 - 4, 0xFF3CFE9A);
            fontRenderer.drawString(LangUtils.localize("gui.robit.dropPickup") + " : " + robit.getDropPickup(), 29, 54 - 4, 0xFF3CFE9A);
            fontRenderer.drawString(LangUtils.localize("gui.robit.owner") + " : " + owner, 29, 63 - 4, 0xFF3CFE9A);
        }

        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (followButton.isMouseOver()) {
            displayTooltip(LangUtils.localize("gui.robit.toggleFollow"), xAxis, yAxis);
        } else if (renameButton.isMouseOver()) {
            displayTooltip(LangUtils.localize("gui.robit.rename"), xAxis, yAxis);
        } else if (teleportHomeButton.isMouseOver()) {
            displayTooltip(LangUtils.localize("gui.robit.teleport"), xAxis, yAxis);
        } else if (pickupButton.isMouseOver()) {
            displayTooltip(LangUtils.localize("gui.robit.togglePickup"), xAxis, yAxis);
        } else if (mainButton.isMouseOver()) {
            displayTooltip(LangUtils.localize("gui.robit"), xAxis, yAxis);
        } else if (craftingButton.isMouseOver()) {
            displayTooltip(LangUtils.localize("gui.robit.crafting"), xAxis, yAxis);
        } else if (inventoryButton.isMouseOver()) {
            displayTooltip(LangUtils.localize("gui.robit.inventory"), xAxis, yAxis);
        } else if (smeltingButton.isMouseOver()) {
            displayTooltip(LangUtils.localize("gui.robit.smelting"), xAxis, yAxis);
        } else if (repairButton.isMouseOver()) {
            displayTooltip(LangUtils.localize("gui.robit.repair"), xAxis, yAxis);
        } else if (xAxis >= 27 && xAxis <= 27 + 112 && yAxis >= 74 && yAxis <= 74 + 6) {
            displayTooltip(MekanismUtils.getEnergyDisplay(robit.getEnergy(), robit.MAX_ELECTRICITY), xAxis, yAxis);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        mc.getTextureManager().bindTexture(MekanismUtils.getResource(ResourceType.GUI, "Robit_Icon.png"));
        drawTexturedModalRect(guiLeft + 28, guiTop + 75, 0, 110, getScaledEnergyLevel(120), 4);
        if (displayNameChange) {
            drawTexturedModalRect(guiLeft + 28, guiTop + 17, 0, 114, 120, 54);
            nameChangeField.drawTextBox();
            MekanismRenderer.resetColor();
        }
    }

    private int getScaledEnergyLevel(int i) {
        return (int) (robit.getEnergy() * i / robit.MAX_ELECTRICITY);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        nameChangeField.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);
        nameChangeField.mouseClicked(mouseX, mouseY, button);
    }

}
