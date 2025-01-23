/*
package mekanism.client.gui;

import mekanism.client.gui.element.GuiModuleScreen;
import mekanism.common.Mekanism;
import mekanism.common.content.gear.Module;
import mekanism.common.inventory.ModuleTweakerContainer;
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
    }

    @Override
    public void initGui() {
        super.initGui();
        addButton(moduleScreen = new GuiModuleScreen(this, 138, 20, stack -> {
            int slotId = inventorySlots.getSlot(selected).getSlotIndex();
            Mekanism.packetHandler.sendToServer(new PacketUpdateInventorySlot(stack, slotId));
            player.setInventorySlotContents(slotId, stack);
        }));
    }

    private void onModuleSelected(Module module) {
        moduleScreen.setModule(module);
    }


}

 */

