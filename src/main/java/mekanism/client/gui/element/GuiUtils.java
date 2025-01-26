package mekanism.client.gui.element;

import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.ints.IntIterator;
import mekanism.api.gas.GasStack;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.InfuseStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.NoSuchElementException;


@SideOnly(Side.CLIENT)
public class GuiUtils {

    public static void renderExtendedTexture(ResourceLocation resource, int sideWidth, int sideHeight, int left, int top, int width, int height) {
        int textureWidth = 2 * sideWidth + 1;
        int textureHeight = 2 * sideHeight + 1;
        blitNineSlicedSized(resource, left, top, width, height, sideWidth, sideHeight, textureWidth, textureHeight, 0, 0, textureWidth, textureHeight);
    }

    public static void renderBackgroundTexture( ResourceLocation resource, int texSideWidth, int texSideHeight, int left, int top, int width,
                                               int height, int textureWidth, int textureHeight) {
        blitNineSlicedSized( resource, left, top, width, height, texSideWidth, texSideHeight, textureWidth, textureHeight, 0, 0, textureWidth, textureHeight);
    }

    public static void drawBarSprite(int xPos, int yPos, int sizeX, int sizeY, int displayInt, TextureAtlasSprite textureSprite, boolean vertical) {
        if (displayInt > 0) {
            if (textureSprite != null) {
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                if (vertical) {
                    drawTiledSprite(xPos + 1, yPos + 1, sizeY - 2, sizeX - 2, displayInt, textureSprite, TilingDirection.DOWN_RIGHT);
                } else {
                    drawTiledSprite(xPos + 1, yPos + 1, sizeY - 2, displayInt, sizeY - 2, textureSprite, TilingDirection.DOWN_RIGHT);
                }
                MekanismRenderer.resetColor();
            }
        }
    }

    public static void drawGasBarSprite(int xPos, int yPos, int sizeX, int sizeY, int displayInt, GasStack gasStack, boolean vertical) {
        if (displayInt > 0) {
            if (gasStack != null) {
                MekanismRenderer.color(gasStack);
                drawBarSprite(xPos, yPos, sizeX, sizeY, displayInt, gasStack.getGas().getSprite(), vertical);
            }
        }
    }

    public static void drawFluidBarSprite(int xPos, int yPos, int sizeX, int sizeY, int displayInt, FluidStack fluidStack, boolean vertical) {
        if (displayInt > 0) {
            if (fluidStack != null) {
                MekanismRenderer.color(fluidStack);
                drawBarSprite(xPos, yPos, sizeX, sizeY, displayInt, MekanismRenderer.getFluidTexture(fluidStack, MekanismRenderer.FluidType.STILL), vertical);
            }
        }
    }


    public static void drawInfuseBarSprite(int xPos, int yPos, int sizeX, int sizeY, int displayInt, InfuseStorage infuseStorage, boolean vertical) {
        if (displayInt > 0) {
            if (infuseStorage.getType() != null) {
                drawBarSprite(xPos, yPos, sizeX, sizeY, displayInt, infuseStorage.getType().sprite, vertical);
            }
        }
    }


    public static void drawTiledSprite(int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, TilingDirection tilingDirection) {
        drawTiledSprite(xPosition, yPosition, yOffset, desiredWidth, desiredHeight, sprite, 16, 16, 0, tilingDirection);
    }

    public static void drawTiledSprite(int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel, TilingDirection tilingDirection) {
        drawTiledSprite(xPosition, yPosition, yOffset, desiredWidth, desiredHeight, sprite, textureWidth, textureHeight, zLevel, tilingDirection, true);
    }

    public static void drawTiledSprite(int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel, TilingDirection tilingDirection, boolean blend) {
        if (desiredWidth == 0 || desiredHeight == 0 || textureWidth == 0 || textureHeight == 0) {
            return;
        }

        int xTileCount = desiredWidth / textureWidth;
        int xRemainder = desiredWidth - (xTileCount * textureWidth);
        int yTileCount = desiredHeight / textureHeight;
        int yRemainder = desiredHeight - (yTileCount * textureHeight);
        int yStart = yPosition + yOffset;
        float uMin = sprite.getMinU();
        float uMax = sprite.getMaxU();
        float vMin = sprite.getMinV();
        float vMax = sprite.getMaxV();
        float uDif = uMax - uMin;
        float vDif = vMax - vMin;

        //Note: We still use the tesselator as that is what GuiGraphics#innerBlit does
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            int width = (xTile == xTileCount) ? xRemainder : textureWidth;
            if (width == 0) {
                break;
            }
            int x = xPosition + (xTile * textureWidth);
            int maskRight = textureWidth - width;
            int shiftedX = x + textureWidth - maskRight;
            float uLocalDif = uDif * maskRight / textureWidth;
            float uLocalMin;
            float uLocalMax;
            if (tilingDirection.right) {
                uLocalMin = uMin;
                uLocalMax = uMax - uLocalDif;
            } else {
                uLocalMin = uMin + uLocalDif;
                uLocalMax = uMax;
            }
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int height = (yTile == yTileCount) ? yRemainder : textureHeight;
                if (height == 0) {
                    //Note: We don't want to fully break out because our height will be zero if we are looking to
                    // draw the remainder, but there is no remainder as it divided evenly
                    break;
                }
                int y = yStart - ((yTile + 1) * textureHeight);
                int maskTop = textureHeight - height;
                float vLocalDif = vDif * maskTop / textureHeight;
                float vLocalMin;
                float vLocalMax;
                if (tilingDirection.down) {
                    vLocalMin = vMin;
                    vLocalMax = vMax - vLocalDif;
                } else {
                    vLocalMin = vMin + vLocalDif;
                    vLocalMax = vMax;
                }
                bufferbuilder.pos(x, y + textureHeight, zLevel).tex(uLocalMin, vLocalMax).endVertex();
                bufferbuilder.pos(shiftedX, y + textureHeight, zLevel).tex(uLocalMax, vLocalMax).endVertex();
                bufferbuilder.pos(shiftedX, y + maskTop, zLevel).tex(uLocalMax, vLocalMin).endVertex();
                bufferbuilder.pos(x, y + maskTop, zLevel).tex(uLocalMin, vLocalMin).endVertex();
            }
        }
        tessellator.draw();
    }

    public enum TilingDirection {
        /**
         * Textures are being tiled/filled from top left to bottom right.
         */
        DOWN_RIGHT(true, true),
        /**
         * Textures are being tiled/filled from top right to bottom left.
         */
        DOWN_LEFT(true, false),
        /**
         * Textures are being tiled/filled from bottom left to top right.
         */
        UP_RIGHT(false, true),
        /**
         * Textures are being tiled/filled from bottom right to top left.
         */
        UP_LEFT(false, false);

        private final boolean down;
        private final boolean right;

        TilingDirection(boolean down, boolean right) {
            this.down = down;
            this.right = right;
        }
    }

    public static void blitNineSlicedSized(ResourceLocation texture, int x, int y, int width, int height, int sliceWidth, int sliceHeight, int uWidth, int vHeight, int uOffset, int vOffset, int textureWidth, int textureHeight) {
        Minecraft mc = Minecraft.getMinecraft();
        Profiler profiler = mc.profiler;
        mc.renderEngine.bindTexture(texture);
        profiler.startSection("blitting");
        profiler.endSection();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        profiler.startSection("blitting");

        int cornerWidth = sliceWidth;
        int cornerHeight = sliceHeight;
        int edgeWidth = sliceWidth;
        int edgeHeight = sliceHeight;
        cornerWidth = Math.min(cornerWidth, width / 2);
        edgeWidth = Math.min(edgeWidth, width / 2);
        cornerHeight = Math.min(cornerHeight, height / 2);
        edgeHeight = Math.min(edgeHeight, height / 2);
        if (width == uWidth && height == vHeight) {
            blit(bufferbuilder, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight);
        } else if (height == vHeight) {
            blit(bufferbuilder, x, y, uOffset, vOffset, cornerWidth, height, textureWidth, textureHeight);
            blitRepeating(bufferbuilder, x + cornerWidth, y, width - edgeWidth - cornerWidth, height, uOffset + cornerWidth, vOffset, uWidth - edgeWidth - cornerWidth, vHeight, textureWidth, textureHeight);
            blit(bufferbuilder, x + width - edgeWidth, y, uOffset + uWidth - edgeWidth, vOffset, edgeWidth, height, textureWidth, textureHeight);
        } else if (width == uWidth) {
            blit(bufferbuilder, x, y, uOffset, vOffset, width, cornerHeight, textureWidth, textureHeight);
            blitRepeating(bufferbuilder, x, y + cornerHeight, width, height - edgeHeight - cornerHeight, uOffset, vOffset + cornerHeight, uWidth, vHeight - edgeHeight - cornerHeight, textureWidth, textureHeight);
            blit(bufferbuilder, x, y + height - edgeHeight, uOffset, vOffset + vHeight - edgeHeight, width, edgeHeight, textureWidth, textureHeight);
        } else {
            blit(bufferbuilder, x, y, uOffset, vOffset, cornerWidth, cornerHeight, textureWidth, textureHeight);
            blitRepeating(bufferbuilder, x + cornerWidth, y, width - edgeWidth - cornerWidth, cornerHeight, uOffset + cornerWidth, vOffset, uWidth - edgeWidth - cornerWidth, cornerHeight, textureWidth, textureHeight);
            blit(bufferbuilder, x + width - edgeWidth, y, uOffset + uWidth - edgeWidth, vOffset, edgeWidth, cornerHeight, textureWidth, textureHeight);
            blit(bufferbuilder, x, y + height - edgeHeight, uOffset, vOffset + vHeight - edgeHeight, cornerWidth, edgeHeight, textureWidth, textureHeight);
            blitRepeating(bufferbuilder, x + cornerWidth, y + height - edgeHeight, width - edgeWidth - cornerWidth, edgeHeight, uOffset + cornerWidth, vOffset + vHeight - edgeHeight, uWidth - edgeWidth - cornerWidth, edgeHeight, textureWidth, textureHeight);
            blit(bufferbuilder, x + width - edgeWidth, y + height - edgeHeight, uOffset + uWidth - edgeWidth, vOffset + vHeight - edgeHeight, edgeWidth, edgeHeight, textureWidth, textureHeight);
            blitRepeating(bufferbuilder, x, y + cornerHeight, cornerWidth, height - edgeHeight - cornerHeight, uOffset, vOffset + cornerHeight, cornerWidth, vHeight - edgeHeight - cornerHeight, textureWidth, textureHeight);
            blitRepeating(bufferbuilder, x + cornerWidth, y + cornerHeight, width - edgeWidth - cornerWidth, height - edgeHeight - cornerHeight, uOffset + cornerWidth, vOffset + cornerHeight, uWidth - edgeWidth - cornerWidth, vHeight - edgeHeight - cornerHeight, textureWidth, textureHeight);
            blitRepeating(bufferbuilder, x + width - edgeWidth, y + cornerHeight, cornerWidth, height - edgeHeight - cornerHeight, uOffset + uWidth - edgeWidth, vOffset + cornerHeight, edgeWidth, vHeight - edgeHeight - cornerHeight, textureWidth, textureHeight);
        }
        profiler.endSection();

        profiler.startSection("drawing");
        tessellator.draw();

        profiler.endSection();
    }

    private static void blit(BufferBuilder bufferbuilder, int pX, int pY, float pUOffset, float pVOffset, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
        bufferbuilder.pos((float) pX, (float) pY, (float) 0).tex((pUOffset + 0.0F) / (float) pTextureWidth, (pVOffset + 0.0F) / (float) pTextureHeight).endVertex();
        bufferbuilder.pos((float) pX, (float) (pY + pHeight), (float) 0).tex((pUOffset + 0.0F) / (float) pTextureWidth, (pVOffset + (float) pHeight) / (float) pTextureHeight).endVertex();
        bufferbuilder.pos((float) (pX + pWidth), (float) (pY + pHeight), (float) 0).tex((pUOffset + (float) pWidth) / (float) pTextureWidth, (pVOffset + (float) pHeight) / (float) pTextureHeight).endVertex();
        bufferbuilder.pos((float) (pX + pWidth), (float) pY, (float) 0).tex((pUOffset + (float) pWidth) / (float) pTextureWidth, (pVOffset + 0.0F) / (float) pTextureHeight).endVertex();
    }

    private static void blitRepeating(BufferBuilder bufferbuilder, int pX, int pY, int pWidth, int pHeight, int pUOffset, int pVOffset, int pSourceWidth, int pSourceHeight, int textureWidth, int textureHeight) {
        int i = pX;

        int j;
        for (IntIterator intiterator = slices(pWidth, pSourceWidth); intiterator.hasNext(); i += j) {
            j = intiterator.nextInt();
            int k = (pSourceWidth - j) / 2;
            int l = pY;

            int i1;
            for (IntIterator intiterator1 = slices(pHeight, pSourceHeight); intiterator1.hasNext(); l += i1) {
                i1 = intiterator1.nextInt();
                int j1 = (pSourceHeight - i1) / 2;
                blit(bufferbuilder, i, l, pUOffset + k, pVOffset + j1, j, i1, textureWidth, textureHeight);
            }
        }
    }


    /**
     * Returns an iterator for dividing a value into slices of a specified size.
     * <p>
     *
     * @param pTarget the value to be divided.
     * @param pTotal  the size of each slice.
     * @return An iterator for iterating over the slices.
     */
    private static IntIterator slices(int pTarget, int pTotal) {
        int i = -Math.floorDiv(-pTarget, pTotal);
        return new Divisor(pTarget, i);
    }

    public static class Divisor implements IntIterator {
        private final int denominator;
        private final int quotient;
        private final int mod;
        private int returnedParts;
        private int remainder;

        public Divisor(int pNumerator, int pDenominator) {
            this.denominator = pDenominator;
            if (pDenominator > 0) {
                this.quotient = pNumerator / pDenominator;
                this.mod = pNumerator % pDenominator;
            } else {
                this.quotient = 0;
                this.mod = 0;
            }

        }

        public boolean hasNext() {
            return this.returnedParts < this.denominator;
        }

        @Override
        public Integer next() {
            return this.nextInt();
        }

        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                int i = this.quotient;
                this.remainder += this.mod;
                if (this.remainder >= this.denominator) {
                    this.remainder -= this.denominator;
                    ++i;
                }

                ++this.returnedParts;
                return i;
            }
        }

        @Override
        public int skip(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            } else {
                int i = n;

                while (i-- != 0 && this.hasNext()) {
                    this.nextInt();
                }

                return n - i - 1;
            }
        }

        @VisibleForTesting
        public static Iterable<Integer> asIterable(int pNumerator, int pDenominator) {
            return () -> new Divisor(pNumerator, pDenominator);
        }
    }


    public static int drawString(FontRenderer font, String component, float x, float y, int color, boolean drawShadow) {
        return font.drawString(component, x, y, color, drawShadow);
    }


    public static int multiply(int pPackedColourOne, int pPackedColorTwo) {
        return color(alpha(pPackedColourOne) * alpha(pPackedColorTwo) / 255, red(pPackedColourOne) * red(pPackedColorTwo) / 255, green(pPackedColourOne) * green(pPackedColorTwo) / 255, blue(pPackedColourOne) * blue(pPackedColorTwo) / 255);
    }


    public static int alpha(int pPackedColor) {
        return pPackedColor >>> 24;
    }

    public static int red(int pPackedColor) {
        return pPackedColor >> 16 & 255;
    }

    public static int green(int pPackedColor) {
        return pPackedColor >> 8 & 255;
    }

    public static int blue(int pPackedColor) {
        return pPackedColor & 255;
    }


    public static int color(int pAlpha, int pRed, int pGreen, int pBlue) {
        return pAlpha << 24 | pRed << 16 | pGreen << 8 | pBlue;
    }

    public static void blit(int pX, int pY, float pUOffset, float pVOffset, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
        blit(pX, pY, pWidth, pHeight, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
    }

    public static void blit(int pX, int pY, int pWidth, int pHeight, float pUOffset, float pVOffset, int pUWidth, int pVHeight, int pTextureWidth, int pTextureHeight) {
        blit(pX, pX + pWidth, pY, pY + pHeight, 0, pUWidth, pVHeight, pUOffset, pVOffset, pTextureWidth, pTextureHeight);
    }


    public static void blit(int pX1, int pX2, int pY1, int pY2, int pBlitOffset, int pUWidth, int pVHeight, float pUOffset, float pVOffset, int pTextureWidth, int pTextureHeight) {
        innerBlit(pX1, pX2, pY1, pY2, pBlitOffset, (pUOffset + 0.0F) / (float) pTextureWidth, (pUOffset + (float) pUWidth) / (float) pTextureWidth, (pVOffset + 0.0F) / (float) pTextureHeight, (pVOffset + (float) pVHeight) / (float) pTextureHeight);
    }

    public static void innerBlit(int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV) {
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((float) pX1, (float) pY1, (float) pBlitOffset).tex(pMinU, pMinV).endVertex();
        bufferbuilder.pos((float) pX1, (float) pY2, (float) pBlitOffset).tex(pMinU, pMaxV).endVertex();
        bufferbuilder.pos((float) pX2, (float) pY2, (float) pBlitOffset).tex(pMaxU, pMaxV).endVertex();
        bufferbuilder.pos((float) pX2, (float) pY1, (float) pBlitOffset).tex(pMaxU, pMinV).endVertex();
        Tessellator.getInstance().draw();
    }


}
