package mekanism.client.gui;

import mekanism.client.gui.element.GuiElementScreen;
import mekanism.client.gui.element.GuiModuleScreen;
import mekanism.client.gui.element.GuiSlot;
import mekanism.common.content.gear.Module;
import mekanism.common.inventory.ModuleTweakerContainer;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiModuleTweaker extends GuiMekanism {

    private GuiModuleScreen moduleScreen;
    public ModuleTweakerContainer container;
    private int selected = -1;


    public GuiModuleTweaker(InventoryPlayer inventory) {
        super(new ModuleTweakerContainer(inventory));
        xSize = 248;
        ySize += 20;
        container = new ModuleTweakerContainer(inventory);
        ResourceLocation resource = getGuiLocation();
        addGuiElement(moduleScreen = new GuiModuleScreen(this, resource, 138, 20, () -> container.inventorySlots.get(selected).getSlotIndex()));
        addGuiElement(new GuiElementScreen(this, getGuiLocation(), 30, 20, 108, 134));
        // addGuiElement(new GuiElementScreen(this, resource, 30, 136, 108, 18));
        int size = container.inventorySlots.size();
        for (int i = 0; i < size; i++) {
            Slot slot = container.inventorySlots.get(i);
            final int index = i;
            // initialize selected item
            if (selected == -1 && isValidItem(index)) {
                select(index);
            }
            addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, resource, slot.xPos - 1, slot.yPos - 1)
                    .click((e, x, y) -> select(index))
                    .overlayColor(isValidItem(index) ? null : () -> 0xCC333333)
                    .with(() -> index == selected ? GuiSlot.SlotOverlay.SELECT : null));
        }
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    private void onModuleSelected(Module<?> oldModule) {
        moduleScreen.setModule(oldModule);
    }


    @Override
    public void mouseReleased(int mouseX, int mouseY, int type) {
        // make sure we get the release event
        moduleScreen.preMouseClicked(mouseX, mouseY, type);
        super.mouseReleased(mouseX, mouseY, type);
    }


    private boolean isValidItem(int index) {
        return ModuleTweakerContainer.isTweakableItem(getStack(index));
    }

    private void select(int index) {
        if (isValidItem(index)) {
            selected = index;
            ItemStack stack = getStack(index);
            //scrollList.updateList(stack, true);
        }
    }

    private ItemStack getStack(int index) {
        if (index == -1) {
            return ItemStack.EMPTY;
        }
        return container.inventorySlots.get(index).getStack();
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        mc.renderEngine.bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_ELEMENT, "GuiScrollList.png"));
        drawTexturedModalRect(guiLeft + 131, guiTop + 21, 31, 0, 6, 132);
    }
}




