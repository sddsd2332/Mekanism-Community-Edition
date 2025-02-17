package mekanism.client.newgui;

import com.google.common.collect.Lists;
import mekanism.api.text.ILangEntry;
import mekanism.client.gui.element.GuiUtils;
import mekanism.client.newgui.element.GuiElement;
import mekanism.client.newgui.element.GuiElement.IHoverable;
import mekanism.client.newgui.element.IGuiEventListener;
import mekanism.client.newgui.element.Widget;
import mekanism.client.newgui.warning.IWarningTracker;
import mekanism.client.newgui.warning.WarningTracker;
import mekanism.client.newgui.warning.WarningTracker.WarningType;
import mekanism.client.render.IFancyFontRenderer;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
import mekanism.common.inventory.container.slot.IVirtualSlot;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public abstract class GuiMekanism<CONTAINER extends Container> extends VirtualSlotContainerScreen<CONTAINER> implements IGuiWrapper, IFancyFontRenderer {

    public static final ResourceLocation BASE_BACKGROUND = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "base.png");
    public static final ResourceLocation SHADOW = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "shadow.png");
    public static final ResourceLocation BLUR = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "blur.png");
    //TODO: Look into defaulting this to true
    protected boolean dynamicSlots;
    //  protected final LRU<GuiWindow> windows = new LRU<>();
    protected final List<GuiElement> focusListeners = new ArrayList<>();
    protected final List<IGuiEventListener> children = Lists.newArrayList();
    protected final List<Widget> buttons = Lists.newArrayList();
    public boolean switchingToJEI;
    @Nullable
    private IWarningTracker warningTracker;

    private boolean hasClicked = false;

    public static int maxZOffset;


    protected GuiMekanism(CONTAINER container) {
        super(container);
    }


    @Nonnull
    @Override
    public BooleanSupplier trackWarning(@Nonnull WarningType type, @Nonnull BooleanSupplier warningSupplier) {
        if (warningTracker == null) {
            warningTracker = new WarningTracker();
        }
        return warningTracker.trackWarning(type, warningSupplier);
    }

    @Override
    public void onGuiClosed() {
        if (!switchingToJEI) {
            //If we are not switching to JEI then run the super close method
            // which will exit the container. We don't want to mark the
            // container as exited if it will be revived when leaving JEI
            // Note: We start by closing all open windows so that any cleanup
            // they need to have done such as saving positions can be done
            //     windows.forEach(GuiWindow::close);
            super.onGuiClosed();
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        if (warningTracker != null) {
            //If our warning tracker isn't null (so this isn't the first time we are initializing, such as after resizing)
            // clear out any tracked warnings, so we don't have duplicates being tracked when we add our elements again
            warningTracker.clearTrackedWarnings();
        }
        addGuiElements();
        if (warningTracker != null) {
            //If we have a warning tracker add it as a button, we do so via a method in case any of the sub GUIs need to reposition where it ends up
            addWarningTab(warningTracker);
        }
    }

    protected void addWarningTab(IWarningTracker warningTracker) {
        //TODO - WARNING SYSTEM: Move this for any GUIs that also have a heat tab (81 y would be above heat)
        //   addButton(new GuiWarningTab(this, warningTracker, 109));
    }

    /**
     * Called to add gui elements to the GUI. Add elements before calling super if they should be before the slots, and after if they should be after the slots. Most
     * elements can and should be added after the slots.
     */
    protected void addGuiElements() {
        if (dynamicSlots) {
            //   addSlots();
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        buttons.stream().filter(child -> child instanceof GuiElement).map(child -> (GuiElement) child).forEach(GuiElement::tick);
        //  windows.forEach(GuiWindow::tick);
    }

    protected IHoverable getOnHover(ILangEntry translationHelper) {
        return getOnHover((Supplier<ITextComponent>) translationHelper::translate);
    }

    protected IHoverable getOnHover(Supplier<ITextComponent> componentSupplier) {
        return (onHover, xAxis, yAxis) -> displayTooltip(componentSupplier.get().getFormattedText(), xAxis, yAxis);
    }

    protected ResourceLocation getButtonLocation(String name) {
        return MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_BUTTON, name + ".png");
    }

    @Override
    public void addFocusListener(GuiElement element) {
        focusListeners.add(element);
    }

    @Override
    public void removeFocusListener(GuiElement element) {
        focusListeners.remove(element);
    }

    @Override
    public void focusChange(GuiElement changed) {
        focusListeners.stream().filter(e -> e != changed).forEach(e -> e.setFocused(false));
    }

    @Override
    public void incrementFocus(GuiElement current) {
        int index = focusListeners.indexOf(current);
        if (index != -1) {
            GuiElement next = focusListeners.get((index + 1) % focusListeners.size());
            next.setFocused(true);
            focusChange(next);
        }
    }

    /*
    @Override
    protected boolean hasClickedOutside(int mouseX, int mouseY, int guiLeftIn, int guiTopIn) {
        return getWindowHovering(mouseX, mouseY) == null && super.hasClickedOutside(mouseX, mouseY, guiLeftIn, guiTopIn);
    }

     */

    @Override
    public void setWorldAndResolution(@Nonnull Minecraft minecraft, int width, int height) {
        //Mark that we are not switching to JEI if we start being initialized again
        switchingToJEI = false;
        //Note: We are forced to do the logic that normally would be inside the "resize" method
        // here in init, as when mods like JEI take over the screen to show recipes, and then
        // return the screen to the "state" it was beforehand it does not actually properly
        // transfer the state from the previous instance to the new instance. If we run the
        // code we normally would run for when things get resized, we then are able to
        // properly reinstate/transfer the states of the various elements
        List<Pair<Integer, GuiElement>> prevElements = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i++) {
            Widget widget = buttons.get(i);
            if (widget instanceof GuiElement && ((GuiElement) widget).hasPersistentData()) {
                prevElements.add(Pair.of(i, (GuiElement) widget));
            }
        }
        // flush the focus listeners list unless it's an overlay
        focusListeners.removeIf(element -> !element.isOverlay);
        int prevLeft = guiLeft, prevTop = guiTop;

        java.util.function.Consumer<Widget> remove = buttons::remove;
        if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Pre(this, this.buttonList))) {
            this.buttons.clear();
        }
        super.setWorldAndResolution(minecraft, width, height);
        // windows.forEach(window -> window.resize(prevLeft, prevTop, leftPos, topPos));
        prevElements.forEach(e -> {
            if (e.getLeft() < buttons.size()) {
                Widget widget = buttons.get(e.getLeft());
                // we're forced to assume that the children list is the same before and after the resize.
                // for verification, we run a lightweight class equality check
                // Note: We do not perform an instance check on widget to ensure it is a GuiElement, as that is
                // ensured by the class comparison, and the restrictions of what can go in prevElements
                if (widget.getClass() == e.getRight().getClass()) {
                    ((GuiElement) widget).syncFrom(e.getRight());
                }
            }
        });
    }



    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.translate(0, 0, 300);
        GlStateManager.translate(-guiLeft, -guiTop, 0);
        MekanismRenderer.resetColor();
        buttons.stream().filter(c -> c instanceof GuiElement).forEach(c -> ((GuiElement) c).onDrawBackground(mouseX, mouseY, MekanismRenderer.getPartialTick()));
        MekanismRenderer.resetColor();
        GlStateManager.translate(guiLeft, guiTop, 0);
        drawForegroundText(mouseX, mouseY);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        // first render general foregrounds
        maxZOffset = 200;
        int zOffset = 200;
        for (Widget widget : this.buttons) {
            if (widget instanceof GuiElement) {
                GlStateManager.pushMatrix();
                ((GuiElement) widget).onRenderForeground(mouseX, mouseY, zOffset, zOffset);
                GlStateManager.popMatrix();
            }
        }
        // now render overlays in reverse-order (i.e. back to front)
        zOffset = maxZOffset;
        /*
        for (LRU<GuiWindow>.LRUIterator iter = getWindowsDescendingIterator(); iter.hasNext(); ) {
            GuiWindow overlay = iter.next();
            zOffset += 150;
            GlStateManager.pushMatrix();
            overlay.onRenderForeground(mouseX, mouseY, zOffset, zOffset);
            if (iter.hasNext()) {
                // if this isn't the focused window, render a 'blur' effect over it
                overlay.renderBlur(matrix);
            }
            GlStateManager.popMatrix();
        }
        GuiElement tooltipElement = getWindowHovering(mouseX, mouseY);
        if (tooltipElement == null) {
            for (int i = buttons.size() - 1; i >= 0; i--) {
                Widget widget = buttons.get(i);
                if (widget instanceof GuiElement && widget.isMouseOver(mouseX, mouseY)) {
                    tooltipElement = (GuiElement) widget;
                    break;
                }
            }
        }

         */

        // translate forwards using RenderSystem. this should never have to happen as we do all the necessary translations with MatrixStacks,
        // but Minecraft has decided to not fully adopt MatrixStacks for many crucial ContainerScreen render operations. should be re-evaluated
        // when mc updates related logic on their end (IMPORTANT)
        GlStateManager.translate(0, 0, maxZOffset);

        /*
        if (tooltipElement != null) {
            tooltipElement.renderToolTip(xAxis, yAxis);
        }

         */

        // render item tooltips
        GlStateManager.translate(-guiLeft, -guiTop, 0);
        renderHoveredToolTip(mouseX, mouseY);
        GlStateManager.translate(guiLeft, guiTop, 0);

        // IMPORTANT: additional hacky translation so held items render okay. re-evaluate as discussed above
        GlStateManager.translate(0, 0, 200);

    }

    protected void drawForegroundText(int mouseX, int mouseY) {
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        hasClicked = true;
        // first try to send the mouse event to our overlays
        /*
        GuiWindow top = windows.isEmpty() ? null : windows.iterator().next();
        GuiWindow focused = windows.stream().filter(overlay -> overlay.mouseClicked(mouseX, mouseY, button)).findFirst().orElse(null);
        if (focused != null) {
            if (windows.contains(focused)) {
                //Validate that the focused window is still one of our windows, as if it wasn't focused/on top, and
                // it is being closed, we don't want to update and mark it as focused, as our defocusing code won't
                // run as we ran it when we pressed the button
                setFocused(focused);
                if (button == 0) {
                    setDragging(true);
                }
                // this check prevents us from moving the window to the top of the stack if the clicked window opened up an additional window
                if (top != focused) {
                    top.onFocusLost();
                    windows.moveUp(focused);
                    focused.onFocused();
                }
            }
        }
         */
        // otherwise, we send it to the current element
        for (int i = buttons.size() - 1; i >= 0; i--) {
            IGuiEventListener listener = buttons.get(i);
            if (listener.mouseClicked(mouseX, mouseY, button)) {
                setFocused(listener.mouseClicked(mouseX, mouseY, button));
                if (button == 0) {
                    // setDragging(true);
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        if (hasClicked) {
            // always pass mouse released events to windows for drag checks
            // windows.forEach(w -> w.onRelease(mouseX, mouseY));
            super.mouseReleased(mouseX, mouseY, button);
        }
    }

    /* TODO
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return windows.stream().anyMatch(window -> window.keyPressed(typedChar, keyCode)) ||
                GuiUtils.checkChildren(buttons, child -> child.keyPressed(typedChar, keyCode)) || super.keyTyped(typedChar, keyCode);
    }

     */

    @Override
    public void keyTyped(char c, int keyCode) throws IOException {
        GuiUtils.checkChildren(buttons, child -> child.charTyped(c, keyCode));
        super.keyTyped(c, keyCode);
    }


    /*
     * @apiNote mouseXOld and mouseYOld are just guessed mappings I couldn't find any usage from a quick glance.
     */
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        buttons.forEach(element -> element.mouseDragged(mouseX, mouseY,0,clickedMouseButton, timeSinceLastClick));
    }


    /*
    @Nullable
    @Override
    @Deprecated//Don't use directly, this is normally private in ContainerScreen
    public Slot getSlotAtPosition(int mouseX, int mouseY) {
        //We override the implementation we have in VirtualSlotContainerScreen so that we can cache getting our window
        // and have some general performance improvements given we can batch a bunch of lookups together
        boolean checkedWindow = false;
        boolean overNoButtons = false;
        //  GuiWindow window = null;
        for (Slot slot : inventorySlots.inventorySlots) {
            boolean virtual = slot instanceof IVirtualSlot;
            int xPos = slot.xPos;
            int yPos = slot.yPos;
            if (virtual) {
                //Virtual slots need special handling to allow for matching them to the window they may be attached to
                IVirtualSlot virtualSlot = (IVirtualSlot) slot;
                xPos = virtualSlot.getActualX();
                yPos = virtualSlot.getActualY();
            }
            if (super.isPointInRegion(xPos, yPos, 16, 16, mouseX, mouseY)) {
                if (!checkedWindow) {
                    //Only lookup the window once
                    checkedWindow = true;
                    window = getWindowHovering(mouseX, mouseY);
                    overNoButtons = overNoButtons(window, mouseX, mouseY);
                }
                if (overNoButtons && slot.isEnabled()) {
                    if (window == null) {
                        return slot;
                    } else if (virtual && window.childrenContainsElement(element -> element instanceof GuiVirtualSlot && ((GuiVirtualSlot) element).isElementForSlot((IVirtualSlot) slot))) {
                        return slot;
                    }
                }
            }
        }
        return null;
    }

     */

    @Override
    public boolean isHovering(@Nonnull Slot slot, int mouseX, int mouseY) {
        if (slot instanceof IVirtualSlot virtualSlot) {
            //Virtual slots need special handling to allow for matching them to the window they may be attached to
            int xPos = virtualSlot.getActualX();
            int yPos = virtualSlot.getActualY();
            if (super.isPointInRegion(xPos, yPos, 16, 16, mouseX, mouseY)) {
                //GuiWindow window = getWindowHovering(mouseX, mouseY);
                //If we are hovering over a window, check if the virtual slot is a child of the window
                //if (window == null || window.childrenContainsElement(element -> element instanceof GuiVirtualSlot && ((GuiVirtualSlot) element).isElementForSlot(virtualSlot))) {
                //return overNoButtons(window, mouseX, mouseY);
                //}
            }
            return false;
        }
        return isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseY);
    }

    /*
    private boolean overNoButtons(@Nullable GuiWindow window, double mouseX, double mouseY) {
        if (window == null) {
            return buttons.stream().noneMatch(button -> button.isMouseOver(mouseX, mouseY));
        }
        return !window.childrenContainsElement(e -> e.isMouseOver(mouseX, mouseY));
    }
    */

    @Override
    protected boolean isPointInRegion(int x, int y, int width, int height, int mouseX, int mouseY) {
        // overridden to prevent slot interactions when a GuiElement is blocking
        return super.isPointInRegion(x, y, width, height, mouseX, mouseY) /*&& getWindowHovering(mouseX, mouseY) == null && overNoButtons(null, mouseX, mouseY)*/;
    }


    /*
    protected void addSlots() {
        int size = inventorySlots.inventorySlots.size();
        for (int i = 0; i < size; i++) {
            Slot slot = inventorySlots.inventorySlots.get(i);
            if (slot instanceof InventoryContainerSlot) {
                InventoryContainerSlot containerSlot = (InventoryContainerSlot) slot;
                ContainerSlotType slotType = containerSlot.getSlotType();
                DataType dataType = findDataType(containerSlot);
                //Shift the slots by one as the elements include the border of the slot
                SlotType type;
                if (dataType != null) {
                    type = SlotType.get(dataType);
                } else if (slotType == ContainerSlotType.INPUT || slotType == ContainerSlotType.OUTPUT || slotType == ContainerSlotType.EXTRA) {
                    type = SlotType.NORMAL;
                } else if (slotType == ContainerSlotType.POWER) {
                    type = SlotType.POWER;
                } else if (slotType == ContainerSlotType.NORMAL || slotType == ContainerSlotType.VALIDITY) {
                    type = SlotType.NORMAL;
                } else {//slotType == ContainerSlotType.IGNORED: don't do anything
                    continue;
                }
                GuiSlot guiSlot = new GuiSlot(type, this, slot.x - 1, slot.y - 1);
                SlotOverlay slotOverlay = containerSlot.getSlotOverlay();
                if (slotOverlay != null) {
                    guiSlot.with(slotOverlay);
                }
                if (slotType == ContainerSlotType.VALIDITY) {
                    int index = i;
                    guiSlot.validity(() -> checkValidity(index));
                }
                addButton(guiSlot);
            } else {
                addButton(new GuiSlot(SlotType.NORMAL, this, slot.x - 1, slot.y - 1));
            }
        }
    }

     */


    /*
    @Nullable
    protected DataType findDataType(InventoryContainerSlot slot) {
        if (menu instanceof MekanismTileContainer) {
            TileEntityMekanism tileEntity = ((MekanismTileContainer<?>) menu).getTileEntity();
            if (tileEntity instanceof ISideConfiguration) {
                return ((ISideConfiguration) tileEntity).getActiveDataType(slot.getInventorySlot());
            }
        }
        return null;
    }

     */


    protected ItemStack checkValidity(int slotIndex) {
        return ItemStack.EMPTY;
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        //Ensure the GL color is white as mods adding an overlay (such as JEI for bookmarks), might have left
        // it in an unexpected state.
        MekanismRenderer.resetColor();
        if (width < 8 || height < 8) {
            Mekanism.logger.warn("Gui: {}, was too small to draw the background of. Unable to draw a background for a gui smaller than 8 by 8.", getClass().getSimpleName());
            return;
        }
        GuiUtils.renderBackgroundTexture(BASE_BACKGROUND, 4, 4, guiLeft, guiTop, xSize, ySize, 256, 256);
        MekanismRenderer.resetColor();
        for (Widget button : this.buttons) {
            button.render(mouseX, mouseY, partialTick);
        }
        MekanismRenderer.resetColor();
    }

    @Override
    public FontRenderer getFont() {
        return fontRenderer;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.translate(0, 0, -500);
        GlStateManager.pushMatrix();
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.popMatrix();
        GlStateManager.translate(0, 0, 500);
    }

    @Override
    public void renderItemTooltip(@Nonnull ItemStack stack, int xAxis, int yAxis) {
        renderToolTip(stack, xAxis, yAxis);
    }

    @Override
    public void renderItemTooltipWithExtra(@Nonnull ItemStack stack, int xAxis, int yAxis, List<String> toAppend) {
        if (toAppend.isEmpty()) {
            renderItemTooltip(stack, xAxis, yAxis);
        } else {
            FontRenderer font = stack.getItem().getFontRenderer(stack);
            net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
            List<String> tooltip = new ArrayList<>(getItemToolTip(stack));
            tooltip.addAll(toAppend);
            drawHoveringText(tooltip, xAxis, yAxis, (font == null ? this.fontRenderer : font));
            net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
        }
    }

    @Override
    public RenderItem getItemRenderer() {
        return itemRender;
    }

    @Override
    public boolean currentlyQuickCrafting() {
        return dragSplitting && !dragSplittingSlots.isEmpty();
    }


    protected <T extends Widget> T addButton(T buttonIn) {
        this.buttons.add(buttonIn);
        return buttonIn;
    }

    /*
    @Override
    public void addWindow(GuiWindow window) {
        GuiWindow top = windows.isEmpty() ? null : windows.iterator().next();
        if (top != null) {
            top.onFocusLost();
        }
        windows.add(window);
        window.onFocused();
    }

    @Override
    public void removeWindow(GuiWindow window) {
        if (!windows.isEmpty()) {
            GuiWindow top = windows.iterator().next();
            windows.remove(window);
            if (window == top) {
                //If the window was the top window, make it lose focus
                window.onFocusLost();
                //Amd check if a new window is now in focus
                GuiWindow newTop = windows.isEmpty() ? null : windows.iterator().next();
                if (newTop == null) {
                    //If there isn't any because they have all been removed
                    // fire an "event" for any post all windows being closed
                    lastWindowRemoved();
                } else {
                    //Otherwise, mark the new window as being focused
                    newTop.onFocused();
                }
                //Update the listener to being the window that is now selected or null if none are
                setFocused(newTop);
            }
        }
    }

    protected void lastWindowRemoved() {
        //Mark that no windows are now selected
        if (menu instanceof MekanismContainer) {
            ((MekanismContainer) menu).setSelectedWindow(null);
        }
    }

    @Override
    public void setSelectedWindow(SelectedWindowData selectedWindow) {
        if (menu instanceof MekanismContainer) {
            ((MekanismContainer) menu).setSelectedWindow(selectedWindow);
        }
    }

    @Nullable
    @Override
    public GuiWindow getWindowHovering(double mouseX, double mouseY) {
        return windows.stream().filter(w -> w.isMouseOver(mouseX, mouseY)).findFirst().orElse(null);
    }

    public Collection<GuiWindow> getWindows() {
        return windows;
    }

    public LRU<GuiWindow>.LRUIterator getWindowsDescendingIterator() {
        return windows.descendingIterator();
    }

     */

}
