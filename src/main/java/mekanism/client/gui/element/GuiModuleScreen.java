/*
package mekanism.client.gui.element;

import mekanism.api.text.IHasTextComponent;
import mekanism.client.SpecialColors;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.sound.SoundHandler;
import mekanism.common.MekanismLang;
import mekanism.common.MekanismSounds;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GuiModuleScreen extends GuiElement {

    private static final ResourceLocation RADIO = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "radio_button.png");
    private static final ResourceLocation SLIDER = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "slider.png");

    private final int TEXT_COLOR = SpecialColors.TEXT_SCREEN.argb();

    private final GuiInnerScreen background;
    private final Consumer<ItemStack> callback;

    protected int relativeX;
    protected int relativeY;

    private oldModule currentOldModule;
    private List<MiniElement> miniElements = new ArrayList<>();

    public GuiModuleScreen(IGuiWrapper gui, ResourceLocation def, int x, int y, Consumer<ItemStack> callback) {
        super(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "Null.png"), gui, def);
        background = new GuiInnerScreen(gui, def, x, y, 102, 134);
        this.callback = callback;
        this.relativeX = x;
        this.relativeY = y;
    }

    @SuppressWarnings("unchecked")
    public void setModule(oldModule oldModule) {
        List<MiniElement> newElements = new ArrayList<>();

        if (oldModule != null) {
            int startY = 3;
            if (oldModule.getData().isExclusive(oldModule.getData().getExclusiveFlags())) {
                startY += 13;
            }
            if (oldModule.getData().getMaxStackSize() > 1) {
                startY += 13;
            }
            for (int i = 0; i < oldModule.getConfigItems().size(); i++) {
                oldModuleConfigItem<?> configItem = oldModule.getConfigItems().get(i);
                // Don't show the enabled option if this is enabled by default
                if (configItem.getData() instanceof oldModuleConfigItem.BooleanData && (!configItem.getName().equals(oldModule.ENABLED_KEY) || !oldModule.getData().isNoDisable())) {
                    newElements.add(new BooleanToggle((oldModuleConfigItem<Boolean>) configItem, 2, startY));
                    startY += 24;
                } else if (configItem.getData() instanceof oldModuleConfigItem.EnumData) {
                    EnumToggle toggle = new EnumToggle((oldModuleConfigItem<Enum<? extends IHasTextComponent>>) configItem, 2, startY);
                    newElements.add(toggle);
                    startY += 34;
                    // allow the dragger to continue sliding, even when we reset the config element
                    if (currentOldModule != null && currentOldModule.getData() == oldModule.getData() && miniElements.get(i) instanceof EnumToggle) {
                        toggle.dragging = ((EnumToggle) miniElements.get(i)).dragging;
                    }
                }
            }
        }

        currentOldModule = oldModule;
        miniElements = newElements;
    }
    @Override
    public void renderBackground(int xAxis, int yAxis,int guiWidth, int guiHeight) {
        for (MiniElement element : miniElements) {
            element.renderBackground(xAxis, yAxis);
        }
    }

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {
        for (MiniElement element : miniElements) {
            element.click(xAxis, yAxis);
        }
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {
        for (MiniElement element : miniElements) {
            element.release(xAxis, yAxis);
        }
    }


    public int getRelativeX() {
        return relativeX;
    }

    public int getRelativeY() {
        return relativeY;
    }

    @Override
    public Rectangle4i getBounds(int guiWidth, int guiHeight) {
        return null;
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {

    }



    abstract class MiniElement {

        final int xPos, yPos;

        public MiniElement(int xPos, int yPos) {
            this.xPos = xPos;
            this.yPos = yPos;
        }

        abstract void renderBackground(int mouseX, int mouseY);

        abstract void renderForeground(int mouseX, int mouseY);

        abstract void click(double mouseX, double mouseY);

        void release(double mouseX, double mouseY) {
        }

        int getRelativeX() {
            return relativeX + xPos;
        }

        int getRelativeY() {
            return relativeY + yPos;
        }

        int getX() {
            return xPos;
        }

        int getY() {
            return yPos;
        }
    }

    private void renderText(String text, int x, int y, int color, float scale) {
        float yAdd = 4 - (scale * 8) / 2F;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + yAdd, 0);
        GlStateManager.scale(scale, scale, scale);
        mc.fontRenderer.drawString(text, (int) ((1F / scale) * x), (int) ((1F / scale) * y), color);
        GlStateManager.popMatrix();
        MekanismRenderer.resetColor();

    }

    class BooleanToggle extends MiniElement {

        final oldModuleConfigItem<Boolean> data;

        BooleanToggle(oldModuleConfigItem<Boolean> data, int xPos, int yPos) {
            super(xPos, yPos);
            this.data = data;
        }

        @Override
        public void renderBackground(int mouseX, int mouseY) {
            mc.getRenderManager().renderEngine.bindTexture(RADIO);
            boolean hover = mouseX >= getX() + 4 && mouseX < getX() + 12 && mouseY >= getY() + 11 && mouseY < getY() + 19;
            if (data.get()) {
                GuiUtils.blit(getX() + 4, getY() + 11, 0, 8, 8, 8, 16, 16);
            } else {
                GuiUtils.blit(getX() + 4, getY() + 11, hover ? 8 : 0, 0, 8, 8, 16, 16);
            }
            hover = mouseX >= getX() + 50 && mouseX < getX() + 58 && mouseY >= getY() + 11 && mouseY < getY() + 19;
            if (!data.get()) {
                GuiUtils.blit(getX() + 50, getY() + 11, 8, 8, 8, 8, 16, 16);
            } else {
                GuiUtils.blit(getX() + 50, getY() + 11, hover ? 8 : 0, 0, 8, 8, 16, 16);
            }
        }


        @Override
        public void renderForeground(int mouseX, int mouseY) {
            renderText(data.getDescription().getTranslationKey(), getRelativeX() + 3, getRelativeY(), TEXT_COLOR, 0.8F);
            renderText(MekanismLang.TRUE.translate().getFormattedText(), getRelativeX() + 16, getRelativeY() + 11, TEXT_COLOR, 0.8F);
            renderText(MekanismLang.FALSE.translate().getFormattedText(), getRelativeX() + 62, getRelativeY() + 11, TEXT_COLOR, 0.8F);
        }

        @Override
        public void click(double mouseX, double mouseY) {
            if (!data.get() && mouseX >= getX() + 4 && mouseX < getX() + 12 && mouseY >= getY() + 11 && mouseY < getY() + 19) {
                data.set(true, callback);
                SoundHandler.playSound(MekanismSounds.BEEP);
            }

            if (data.get() && mouseX >= getX() + 50 && mouseX < getX() + 58 && mouseY >= getY() + 11 && mouseY < getY() + 19) {
                data.set(true, callback);
                SoundHandler.playSound(MekanismSounds.BEEP);
            }
        }
    }

    class EnumToggle extends MiniElement {

        final int BAR_LENGTH = 134 - 24;
        final int BAR_START = 10;
        final float TEXT_SCALE = 0.7F;
        final oldModuleConfigItem<Enum<? extends IHasTextComponent>> data;
        boolean dragging = false;

        EnumToggle(oldModuleConfigItem<Enum<? extends IHasTextComponent>> data, int xPos, int yPos) {
            super(xPos, yPos);
            this.data = data;
        }

        @Override
        public void renderBackground(int mouseX, int mouseY) {
            mc.getRenderManager().renderEngine.bindTexture(SLIDER);
            int count = ((oldModuleConfigItem.EnumData<?>) data.getData()).getSelectableCount();
            int center = (BAR_LENGTH / (count - 1)) * data.get().ordinal();
            GuiUtils.blit( getX() + BAR_START + center - 2, getY() + 11, 0, 0, 5, 6, 8, 8);
            GuiUtils.blit( getX() + BAR_START, getY() + 17, 0, 6, BAR_LENGTH, 2, 8, 8);
        }

        @Override
        public void renderForeground(int mouseX, int mouseY) {
            oldModuleConfigItem.EnumData<?> enumData = (oldModuleConfigItem.EnumData<?>) data.getData();
            renderText(data.getDescription().getTranslationKey(), getRelativeX() + 3, getRelativeY(), TEXT_COLOR, 0.8F);
            Enum<? extends IHasTextComponent>[] arr = enumData.getEnums();
            int count = enumData.getSelectableCount();
            for (int i = 0; i < count; i++) {
                int diffFromCenter = ((BAR_LENGTH / (count - 1)) * i) - (BAR_LENGTH / 2);
                float diffScale = 1 - (1 - TEXT_SCALE) / 2F;
                int textCenter = getRelativeX() + BAR_START + (BAR_LENGTH / 2) + (int) (diffFromCenter * diffScale);
                drawScaledCenteredText( ((IHasTextComponent) arr[i]).getTextComponent(), textCenter, getRelativeY() + 20, TEXT_COLOR, TEXT_SCALE);
            }

            if (dragging) {
                int cur = (int) Math.round(((double) (mouseX - getX() - BAR_START) / (double) BAR_LENGTH) * (count - 1));
                cur = Math.min(count - 1, Math.max(0, cur));
                if (cur != data.get().ordinal()) {
                    data.set(arr[cur], callback);
                }
            }
        }

        @Override
        public void click(double mouseX, double mouseY) {
            int count = ((oldModuleConfigItem.EnumData<?>) data.getData()).getSelectableCount();
            if (!dragging) {
                int center = (BAR_LENGTH / (count - 1)) * data.get().ordinal();
                if (mouseX >= getX() + BAR_START + center - 2 && mouseX < getX() + BAR_START + center + 3 && mouseY >= getY() + 11 && mouseY < getY() + 17) {
                    dragging = true;
                }
            }
            if (!dragging) {
                Enum<? extends IHasTextComponent>[] arr = ((oldModuleConfigItem.EnumData<?>) data.getData()).getEnums();
                if (mouseX >= getX() + BAR_START && mouseX < getX() + BAR_START + BAR_LENGTH && mouseY >= getY() + 10 && mouseY < getY() + 22) {
                    int cur = (int) Math.round(((mouseX - getX() - BAR_START) / BAR_LENGTH) * (count - 1));
                    cur = Math.min(count - 1, Math.max(0, cur));
                    if (cur != data.get().ordinal()) {
                        data.set(arr[cur], callback);
                    }
                }
            }
        }

        @Override
        public void release(double mouseX, double mouseY) {
            dragging = false;
        }
    }

     void drawScaledCenteredText(ITextComponent text, float left, float y, int color, float scale) {
        int textWidth = getFontRenderer().getStringWidth(text.getFormattedText());
        float centerX = left - (textWidth / 2F) * scale;
         renderText(text.getFormattedText(), (int) centerX, (int) y, color, scale);
    }
}

 */
