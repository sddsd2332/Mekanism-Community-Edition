package mekanism.client.gui.robit;

import mekanism.client.gui.GuiMekanism;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.element.GuiPlayerSlot;
import mekanism.client.gui.element.tab.GuiSideHolder;
import mekanism.common.Mekanism;
import mekanism.common.entity.EntityRobit;
import mekanism.common.network.PacketRobit.RobitMessage;
import mekanism.common.util.LangUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public abstract class GuiRobit extends GuiMekanism {

    protected final EntityRobit robit;
    private GuiDisableableButton mainButton;
    private GuiDisableableButton craftingButton;
    private GuiDisableableButton inventoryButton;
    private GuiDisableableButton smeltingButton;
    private GuiDisableableButton repairButton;

    protected GuiRobit(EntityRobit robit, Container container) {
        super(container);
        this.robit = robit;
        addGuiElement(new GuiPlayerSlot(this, getGuiLocation()));
        addGuiElement(new GuiSideHolder(this, getGuiLocation(), 176, 6, 25, 106));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(mainButton = new GuiDisableableButton(0, guiLeft + 179, guiTop + 10, 18, 18).with(GuiDisableableButton.ImageOverlay.MAIN));
        buttonList.add(craftingButton = new GuiDisableableButton(1, guiLeft + 179, guiTop + 30, 18, 18).with(GuiDisableableButton.ImageOverlay.CRAFTING));
        buttonList.add(inventoryButton = new GuiDisableableButton(2, guiLeft + 179, guiTop + 50, 18, 18).with(GuiDisableableButton.ImageOverlay.INVENTORY));
        buttonList.add(smeltingButton = new GuiDisableableButton(3, guiLeft + 179, guiTop + 70, 18, 18).with(GuiDisableableButton.ImageOverlay.SMELTING));
        buttonList.add(repairButton = new GuiDisableableButton(4, guiLeft + 179, guiTop + 90, 18, 18).with(GuiDisableableButton.ImageOverlay.REPAIR));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (!shouldOpenGui(guibutton.id)) {
            //Don't do anything when the button is the same one as the one we are on
            return;
        }
        if (guibutton.id == mainButton.id) {
            Mekanism.packetHandler.sendToServer(new RobitMessage(robit.getEntityId(), 21));
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
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (mainButton.isMouseOver()) {
            this.olddisplayTooltip(LangUtils.localize("gui.robit"), xAxis, yAxis);
        } else if (craftingButton.isMouseOver()) {
            this.olddisplayTooltip(LangUtils.localize("gui.robit.crafting"), xAxis, yAxis);
        } else if (inventoryButton.isMouseOver()) {
            this.olddisplayTooltip(LangUtils.localize("gui.robit.inventory"), xAxis, yAxis);
        } else if (smeltingButton.isMouseOver()) {
            this.olddisplayTooltip(LangUtils.localize("gui.robit.smelting"), xAxis, yAxis);
        } else if (repairButton.isMouseOver()) {
            this.olddisplayTooltip(LangUtils.localize("gui.robit.repair"), xAxis, yAxis);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    protected abstract boolean shouldOpenGui(int id);
}
