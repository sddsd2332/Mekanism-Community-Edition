package mekanism.client.newgui.element.scroll;

import mekanism.api.EnumColor;
import mekanism.api.gear.IModule;
import mekanism.api.gear.ModuleData;
import mekanism.api.gear.ModuleData.ExclusiveFlag;
import mekanism.api.text.TextComponentGroup;
import mekanism.client.gui.element.GuiUtils;
import mekanism.client.newgui.IGuiWrapper;
import mekanism.client.newgui.element.GuiElement;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.MekanismLang;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.item.ItemModule;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;

public class GuiModuleScrollList extends GuiScrollList {

    private static final ResourceLocation MODULE_SELECTION = MekanismUtils.getResource(ResourceType.GUI, "module_selection.png");
    public static final ResourceLocation HOLDER = MekanismUtils.getResource(ResourceType.GUI, "element_holder.png");
    private static final int TEXTURE_WIDTH = 100;
    private static final int TEXTURE_HEIGHT = 36;

    private int selectIndex = -1;

    private final Consumer<Module<?>> callback;
    private final List<ModuleData<?>> currentList = new ArrayList<>();
    private final Supplier<ItemStack> itemSupplier;
    private ItemStack currentItem;

    public GuiModuleScrollList(IGuiWrapper gui, int x, int y, int width, int height, Supplier<ItemStack> itemSupplier, Consumer<Module<?>> callback) {
        super(gui, x, y, width, height, TEXTURE_HEIGHT / 3, HOLDER, 32);
        this.itemSupplier = itemSupplier;
        this.callback = callback;
        updateList(itemSupplier.get(), true);
    }

    public void updateList(ItemStack currentItem, boolean forceReset) {
        ModuleData<?> prevSelect = getSelection();
        this.currentItem = currentItem;
        currentList.clear();
        currentList.addAll(ModuleHelper.get().loadAllTypes(currentItem));
        boolean selected = false;
        if (!forceReset && prevSelect != null) {
            for (int i = 0, size = currentList.size(); i < size; i++) {
                if (currentList.get(i) == prevSelect) {
                    setSelected(i);
                    selected = true;
                    break;
                }
            }
        }
        if (!selected) {
            clearSelection();
        }
    }

    @Override
    protected int getMaxElements() {
        return currentList.size();
    }

    @Override
    public boolean hasSelection() {
        return selectIndex != -1;
    }

    @Override
    protected void setSelected(int index) {
        if (index >= 0 && index < currentList.size()) {
            selectIndex = index;
            callback.accept(ModuleHelper.get().load(currentItem, currentList.get(index)));
        }
    }

    @Nullable
    public ModuleData<?> getSelection() {
        return selectIndex == -1 ? null : currentList.get(selectIndex);
    }

    @Override
    public void clearSelection() {
        selectIndex = -1;
        callback.accept(null);
    }

    @Override
    public void renderForeground(int mouseX, int mouseY) {
        super.renderForeground(mouseX, mouseY);
        ItemStack stack = itemSupplier.get();
        if (!ItemStack.areItemStacksEqual(currentItem, stack)) {
            updateList(stack, false);
        }
        forEachModule((module, multipliedElement) -> {
            IModule<?> instance = ModuleHelper.get().load(currentItem, module);
            if (instance != null) {
                int color = module.isExclusive(ExclusiveFlag.ANY) ? (instance.isEnabled() ? 0x635BD4 : 0x2E2A69) : (instance.isEnabled() ? titleTextColor() : 0x5E1D1D);
                drawScaledTextScaledBound(new TextComponentGroup().translation(module.getTranslationKey()), relativeX + 13, relativeY + 3 + multipliedElement, color, 86, 0.7F);
            }
        });
    }

    @Override
    public void renderToolTip(int mouseX, int mouseY) {
        super.renderToolTip(mouseX, mouseY);
        if (mouseX >= relativeX + 1 && mouseX < relativeX + barXShift - 1) {
            forEachModule((module, multipliedElement) -> {
                IModule<?> instance = ModuleHelper.get().load(currentItem, module);
                if (instance != null && mouseY >= relativeY + 1 + multipliedElement && mouseY < relativeY + 1 + multipliedElement + elementHeight) {
                    displayTooltip(new TextComponentGroup().translation(MekanismLang.MODULE_INSTALLED.getTranslationKey()).translation(instance.getInstalledCount() + "/" + module.getMaxStackSize(), EnumColor.DARK_GREY.textFormatting), mouseX, mouseY, getGuiWidth());
                }
            });
        }
    }

    @Override
    public void renderElements(int mouseX, int mouseY, float partialTicks) {
        //Draw elements
        minecraft.renderEngine.bindTexture(MODULE_SELECTION);
        forEachModule((module, multipliedElement) -> {
            int shiftedY = y + 1 + multipliedElement;
            int j = 1;
            if (module == getSelection()) {
                j = 2;
            } else if (mouseX >= x + 1 && mouseX < barX - 1 && mouseY >= shiftedY && mouseY < shiftedY + elementHeight) {
                j = 0;
            }
            GuiUtils.blit(x + 1, shiftedY, 0, elementHeight * j, TEXTURE_WIDTH, elementHeight, TEXTURE_WIDTH, TEXTURE_HEIGHT);
            MekanismRenderer.resetColor();
        });
        //Note: This needs to be in its own loop as rendering the items is likely to cause the texture manager to be bound to a different texture
        // and thus would make the selection area background get all screwed up
        forEachModule((module, multipliedElement) -> gui().renderItem(module.getStack(), x + 3, y + 3 + multipliedElement, 0.5F));
    }

    private void forEachModule(ObjIntConsumer<ModuleData<?>> consumer) {
        for (int i = 0; i < getFocusedElements(); i++) {
            int index = getCurrentSelection() + i;
            if (index > currentList.size() - 1) {
                break;
            }
            consumer.accept(currentList.get(index), elementHeight * i);
        }
    }

    @Override
    public void syncFrom(GuiElement element) {
        super.syncFrom(element);
        GuiModuleScrollList old = (GuiModuleScrollList) element;
        setSelected(old.selectIndex);
    }
}