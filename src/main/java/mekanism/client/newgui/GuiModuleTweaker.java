package mekanism.client.newgui;


import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import mekanism.client.newgui.element.custom.GuiModuleScreen;
import mekanism.client.newgui.element.scroll.GuiModuleScrollList;
import mekanism.client.newgui.element.slot.GuiSlot;
import mekanism.client.newgui.element.slot.SlotType;
import mekanism.common.MekanismLang;
import mekanism.common.content.gear.Module;
import mekanism.common.inventory.ModuleTweakerContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiModuleTweaker extends GuiMekanism<ModuleTweakerContainer> {

    private GuiModuleScrollList scrollList;
    private GuiModuleScreen moduleScreen;
    private InventoryPlayer container;
    private int selected = -1;

    public GuiModuleTweaker(InventoryPlayer inventory) {
        super(new ModuleTweakerContainer(inventory));
        container =inventory;
        xSize = 302;
        ySize += 66;
    }


    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        moduleScreen = addButton(new GuiModuleScreen(this, 192, 20, () -> inventorySlots.inventorySlots.get(selected).getSlotIndex()));
        scrollList = addButton(new GuiModuleScrollList(this, 84, 20, 108, 204, () -> getStack(selected), this::onModuleSelected));
        int size = inventorySlots.inventorySlots.size();
        for (int i = 0; i < size; i++) {
            Slot slot = inventorySlots.inventorySlots.get(i);
            final int index = i;
            // initialize selected item
            if (selected == -1 && isValidItem(index)) {
                select(index);
            }
            addButton(new GuiSlot(SlotType.NORMAL, this, slot.xPos - 1, slot.yPos - 1)
                    .click((e, x, y) -> select(index))
                    .overlayColor(isValidItem(index) ? null : () -> 0xCC333333)
                    .with(() -> index == selected ? SlotOverlay.SELECT : null));
        }
    }

    private void onModuleSelected(Module<?> module) {
        moduleScreen.setModule(module);
    }

    @Override
    public void keyTyped(char c, int keyCode) throws IOException {
        super.keyTyped(c, keyCode);
        if (selected != -1 && (isPreviousButton(keyCode) || isNextButton(keyCode))) {
            int curIndex = -1;
            IntList selectable = new IntArrayList();
            for (int index = 0, slots = inventorySlots.inventorySlots.size(); index < slots; index++) {
                if (isValidItem(index)) {
                    selectable.add(index);
                    if (index == selected) {
                        curIndex = selectable.size() - 1;
                    }
                }
            }
            int targetIndex;
            if (isPreviousButton(keyCode)) {
                targetIndex = curIndex == 0 ? selectable.size() - 1 : curIndex - 1;
            } else {//isNextButton
                targetIndex = curIndex + 1;
            }
            select(selectable.getInt(targetIndex % selectable.size()));
        }
    }

    private boolean isPreviousButton(int key) {
        return key == Keyboard.KEY_UP || key == Keyboard.KEY_LEFT;
    }

    private boolean isNextButton(int key) {
        return key == Keyboard.KEY_DOWN || key == Keyboard.KEY_RIGHT;
    }


    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        // make sure we get the release event
        moduleScreen.onRelease(mouseX, mouseY);
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void drawForegroundText(int mouseX, int mouseY) {
        drawTitleText(MekanismLang.MODULE_TWEAKER.translate(), 6);
        super.drawForegroundText(mouseX, mouseY);
    }

    private void select(int index) {
        if (isValidItem(index)) {
            selected = index;
            ItemStack stack = getStack(index);
            scrollList.updateList(stack, true);
            //   optionsButton.active = stack.getItem() == MekanismItems.MEKASUIT_HELMET.get();
        }
    }


    private boolean isValidItem(int index) {
        return ModuleTweakerContainer.isTweakableItem(getStack(index));
    }

    private ItemStack getStack(int index) {
        if (index == -1) {
            return ItemStack.EMPTY;
        }
        return inventorySlots.inventorySlots.get(index).getStack();
    }

    @Override
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        //Overlays prevent clicks to pick up items
    }
}
