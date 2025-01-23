/*
package mekanism.client.gui;

import mekanism.client.gui.element.GuiModuleScreen;
import mekanism.common.Mekanism;
import mekanism.common.inventory.ModuleTweakerContainer;
import mekanism.common.network.PacketUpdateInventorySlot.UpdateInventorySlotMessage;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiModuleTweaker extends GuiMekanism {

    private GuiModuleScreen moduleScreen;
    private InventoryPlayer player;

    private int selected = -1;


    public GuiModuleTweaker(InventoryPlayer inventory) {
        super(new ModuleTweakerContainer(inventory));
        xSize = 248;
        ySize += 20;
        ResourceLocation resource = getGuiLocation();
        player = inventory;
        addGuiElement(moduleScreen = new GuiModuleScreen(this, resource,138, 20, stack -> {
            int slotId = inventorySlots.getSlot(selected).getSlotIndex();
            Mekanism.packetHandler.sendToServer(new UpdateInventorySlotMessage(slotId));
            player.setInventorySlotContents(slotId, stack);
        }));
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    private void onModuleSelected(oldModule oldModule) {
        moduleScreen.setModule(oldModule);
    }


    @Override
    public void mouseReleased(int mouseX, int mouseY, int type) {
        // make sure we get the release event
        moduleScreen.preMouseClicked(mouseX, mouseY,type);
        super.mouseReleased(mouseX, mouseY, type);
    }


}


 */


