package mekanism.client.newgui;

import mekanism.common.inventory.container.slot.IVirtualSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public abstract class VirtualSlotContainerScreen<T extends Container> extends GuiContainer {

    public VirtualSlotContainerScreen(Container container) {
        super(container);
    }

    protected abstract boolean isHovering(@Nonnull Slot slot, int mouseX, int mouseY);

    @Nullable
    @Override
    @Deprecated//Don't use directly, this is normally private in ContainerScreen
    public Slot getSlotAtPosition(int mouseX, int mouseY) {
        for (Slot slot : inventorySlots.inventorySlots) {
            //Like super.getSelectedSlot except uses our isMouseOverSlot so
            // that our redirection doesn't break this
            if (isMouseOverSlot(slot, mouseX, mouseY) && slot.isEnabled()) {
                return slot;
            }
        }
        return null;
    }

    @Override
    @Deprecated//Don't use directly, this is normally private in ContainerScreen
    public final boolean isMouseOverSlot(@Nonnull Slot slot, int mouseX, int mouseY) {
        boolean mouseOver = isHovering(slot, mouseX, mouseY);
        if (mouseOver && slot instanceof IVirtualSlot) {
            //Fake that the slot is "not" selected so that when this is called by render
            // we don't render hover mask as it will be in the incorrect position
            if (hoveredSlot == null && slot.isEnabled()) {
                //If needed though we do make sure to update the hovered slot for use elsewhere
                hoveredSlot = slot;
            }
            return false;
        }
        return mouseOver;
    }

    @Override
    @Deprecated//Don't use directly, this is normally private in ContainerScreen
    public final void drawItemStack(@Nonnull ItemStack stack, int x, int y, @Nullable String altText) {
        if (!stack.isEmpty()) {
            if (stack == this.returningStack && this.returningStackDestSlot instanceof IVirtualSlot) {
                //Use an instance equality check to see if we are rendering the returning stack (used in touch screens)
                // if we are and the slot we are returning to is a virtual one, so the position may be changing
                // then recalculate where the stack actually is/should be to send it to the correct position
                float f = (float) (Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;
                if (f >= 1.0F) {
                    //I don't think this should ever happen given we validated it isn't the case before entering
                    // drawItemStack, but just in case it is, update the returningStack and exit
                    this.returningStack = ItemStack.EMPTY;
                    return;
                }
                //Recalculate the x and y values to make sure they are the correct values
                IVirtualSlot returningVirtualSlot = (IVirtualSlot) this.returningStackDestSlot;
                int xOffset = returningVirtualSlot.getActualX() - this.touchUpX;
                int yOffset = returningVirtualSlot.getActualY() - this.touchUpY;
                x = this.touchUpX + (int) (xOffset * f);
                y = this.touchUpY + (int) (yOffset * f);
            }
            //noinspection ConstantConditions, altText can be null, just is marked as caught as nonnull by mojang's class level stuff
            super.drawItemStack(stack, x, y, altText);
        }
    }

    @Override
    @Deprecated//Don't use directly, this is normally private in ContainerScreen
    public final void drawSlot(@Nonnull Slot slot) {
        if (!(slot instanceof IVirtualSlot)) {
            //If we are not a virtual slot, the super method is good enough
            super.drawSlot(slot);
            return;
        }
        //Basically a copy of super.moveItems, except with the rendering at the bottom adjusted
        // for if we are a virtual slot
        ItemStack currentStack = slot.getStack();
        boolean shouldDrawOverlay = false;
        boolean skipStackRendering = slot == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
        ItemStack heldStack = mc.player.inventory.getItemStack();
        String s = null;
        if (slot == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !currentStack.isEmpty()) {
            currentStack = currentStack.copy();
            currentStack.setCount(currentStack.getCount() / 2);
        } else if (dragSplitting && dragSplittingSlots.contains(slot) && !heldStack.isEmpty()) {
            if (dragSplittingSlots.size() == 1) {
                return;
            }
            if (Container.canAddItemToSlot(slot, heldStack, true) && this.inventorySlots.canDragIntoSlot(slot)) {
                currentStack = heldStack.copy();
                shouldDrawOverlay = true;
                Container.computeStackSize(dragSplittingSlots, this.dragSplittingLimit, currentStack, slot.getStack().isEmpty() ? 0 : slot.getStack().getCount());
                int k = Math.min(currentStack.getMaxStackSize(), slot.getItemStackLimit(currentStack));
                if (currentStack.getCount() > k) {
                    s = TextFormatting.YELLOW.toString() + k;
                    currentStack.setCount(k);
                }
            } else {
                dragSplittingSlots.remove(slot);
                updateDragSplitting();
            }
        }
        //If the slot is a virtual slot, have the GuiSlot that corresponds to it handle the rendering
        ((IVirtualSlot) slot).updateRenderInfo(skipStackRendering ? ItemStack.EMPTY : currentStack, shouldDrawOverlay, s);
    }

    public boolean slotClicked(@Nonnull Slot slot, int button) {
        //Copy of super.mouseClicked, minus the call to all the sub elements as we know how we are interacting with it
        boolean pickBlockButton = mc.gameSettings.keyBindPickBlock.isActiveAndMatches(button - 100);
        long time = Minecraft.getSystemTime();
        this.doubleClick = this.lastClickSlot == slot && time - this.lastClickTime < 250L && this.lastClickButton == button;
        this.ignoreMouseUp = false;
        if (button != 0 && button != 1 && !pickBlockButton) {
            checkHotbarKeys(button);
        } else if (slot.slotNumber != -1) {
            if (mc.gameSettings.touchscreen) {
                if (slot.getHasStack()) {
                    this.clickedSlot = slot;
                    this.draggedStack = ItemStack.EMPTY;
                    this.isRightMouseClick = button == 1;
                } else {
                    this.clickedSlot = null;
                }
            } else if (!this.dragSplitting) {
                if (mc.player.inventory.getItemStack().isEmpty()) {
                    if (pickBlockButton) {
                        this.handleMouseClick(slot, slot.slotNumber, button, ClickType.CLONE);
                    } else {
                        ClickType clicktype = ClickType.PICKUP;
                        if (GuiScreen.isShiftKeyDown()) {
                            this.shiftClickedSlot = slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY;
                            clicktype = ClickType.QUICK_MOVE;
                        }
                        this.handleMouseClick(slot, slot.slotNumber, button, clicktype);
                    }
                    this.ignoreMouseUp = true;
                } else {
                    this.dragSplitting = true;
                    this.dragSplittingButton = button;
                    this.dragSplittingSlots.clear();
                    if (button == 0) {
                        this.dragSplittingLimit = 0;
                    } else if (button == 1) {
                        this.dragSplittingLimit = 1;
                    } else if (pickBlockButton) {
                        this.dragSplittingLimit = 2;
                    }
                }
            }
        }
        this.lastClickSlot = slot;
        this.lastClickTime = time;
        this.lastClickButton = button;
        return true;
    }
}
