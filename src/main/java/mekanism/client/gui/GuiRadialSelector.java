package mekanism.client.gui;

import mekanism.api.IDisableableEnum;
import mekanism.client.gui.element.GuiUtils;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.item.interfaces.IRadialSelectorEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GuiRadialSelector<TYPE extends Enum<TYPE> & IRadialSelectorEnum<TYPE>> extends GuiScreen {

    private static final float DRAWS = 300;

    private static final float INNER = 40, OUTER = 100;
    private static final float SELECT_RADIUS = 10;

    private final Class<TYPE> enumClass;
    private final TYPE[] types;
    private final Supplier<TYPE> curSupplier;
    private final Consumer<TYPE> changeHandler;
    private final boolean isDisableable;

    private TYPE selection = null;

    public GuiRadialSelector(Class<TYPE> enumClass, Supplier<TYPE> curSupplier, Consumer<TYPE> changeHandler) {
        //  super(MekanismLang.RADIAL_SCREEN.translate());
        this.enumClass = enumClass;
        this.curSupplier = curSupplier;
        this.changeHandler = changeHandler;
        isDisableable = IDisableableEnum.class.isAssignableFrom(enumClass);
        types = enumClass.getEnumConstants();
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
        // center of screen
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        float centerX = scaledResolution.getScaledWidth() / 2F;
        float centerY = scaledResolution.getScaledHeight() / 2F;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.translate(centerX, centerY, 0);
        GlStateManager.disableTexture2D();

        // Calculate number of available modes to switch between
        int activeModes;
        if (isDisableable) {
            activeModes = (int) Arrays.stream(types).filter(type -> ((IDisableableEnum) type).isEnabled()).count();
        } else {
            activeModes = types.length;
        }

        // draw base
        GlStateManager.color(0.3F, 0.3F, 0.3F, 0.5F);
        drawTorus(0, 360);

        TYPE cur = curSupplier.get();
        // Draw segments
        if (cur != null) {
            // draw current selected
            if (cur.getColor() == null) {
                GlStateManager.color(0.4F, 0.4F, 0.4F, 0.7F);
            } else {
                MekanismRenderer.color(cur.getColor(), 0.3F);
            }
            int section;
            if (isDisableable) {
                //Calculate the proper section to highlight as green in case one of the earlier ones is disabled
                section = 0;
                for (TYPE type : types) {
                    if (((IDisableableEnum) type).isEnabled()) {
                        if (type == cur) {
                            break;
                        }
                        section++;
                    }
                }
            } else {
                section = cur.ordinal();
            }
            drawTorus(-90F + 360F * (-0.5F + section) / activeModes, 360F / activeModes);
            double xDiff = mouseX - centerX;
            double yDiff = mouseY - centerY;
            if (Math.sqrt(xDiff * xDiff + yDiff * yDiff) >= SELECT_RADIUS) {
                // draw mouse selection highlight
                float angle = (float) Math.toDegrees(Math.atan2(yDiff, xDiff));
                GlStateManager.color(0.8F, 0.8F, 0.8F, 0.3F);
                drawTorus(360F * (-0.5F / activeModes) + angle, 360F / activeModes);

                float selectionAngle = angle + 90F + (360F * (0.5F / activeModes));
                while (selectionAngle < 0) {
                    selectionAngle += 360F;
                }
                int selectionDrawnPos = (int) (selectionAngle * (activeModes / 360F));
                if (isDisableable) {
                    int count = 0;
                    for (TYPE type : types) {
                        if (((IDisableableEnum) type).isEnabled()) {
                            if (count == selectionDrawnPos) {
                                selection = type;
                                break;
                            }
                            count++;
                        }
                    }
                } else {
                    selection = types[selectionDrawnPos];
                }
                // draw hovered selection
                GlStateManager.color(0.6F, 0.6F, 0.6F, 0.7F);
                drawTorus(-90F + 360F * (-0.5F + selectionDrawnPos) / activeModes, 360F / activeModes);
            } else {
                selection = null;
            }
        }

        MekanismRenderer.resetColor();
        // Icons & Labels
        GlStateManager.enableTexture2D();
        int position = 0;
        for (TYPE type : types) {
            if (isDisableable && !((IDisableableEnum) type).isEnabled()) {
                // Mode disabled, skip it.
                continue;
            }

            double angle = Math.toRadians(270 + 360 * ((float) position / activeModes));
            float x = (float) Math.cos(angle) * (INNER + OUTER) / 2F;
            float y = (float) Math.sin(angle) * (INNER + OUTER) / 2F;
            // draw icon
            Minecraft.getMinecraft().renderEngine.bindTexture(type.getIcon());
            GuiUtils.blit(Math.round(x - 12), Math.round(y - 20), 24, 24, 0, 0, 18, 18, 18, 18);
            // draw label
            GlStateManager.pushMatrix();
            int width = fontRenderer.getStringWidth(type.getShortText().getFormattedText());
            GlStateManager.translate(x, y, 0);
            GlStateManager.scale(0.6F, 0.6F, 0.6F);
            fontRenderer.drawStringWithShadow(type.getShortText().getFormattedText(), -width / 2F, 8, 0xCCFFFFFF);
            GlStateManager.popMatrix();
            position++;
        }
        MekanismRenderer.resetColor();
        GlStateManager.popMatrix();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        updateSelection();
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        updateSelection();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void drawTorus(float startAngle, float sizeAngle) {
        BufferBuilder vertexBuffer = Tessellator.getInstance().getBuffer();
        vertexBuffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION);
        float draws = DRAWS * (sizeAngle / 360F);
        for (int i = 0; i <= draws; i++) {
            float angle = (float) Math.toRadians(startAngle + (i / DRAWS) * 360);
            vertexBuffer.pos((float) (OUTER * Math.cos(angle)), (float) (OUTER * Math.sin(angle)), 0).endVertex();
            vertexBuffer.pos((float) (INNER * Math.cos(angle)), (float) (INNER * Math.sin(angle)), 0).endVertex();
        }
        Tessellator.getInstance().draw();
    }

    public void updateSelection() {
        if (selection != null) {
            changeHandler.accept(selection);
        }
    }

    public Class<TYPE> getEnumClass() {
        return enumClass;
    }
}
