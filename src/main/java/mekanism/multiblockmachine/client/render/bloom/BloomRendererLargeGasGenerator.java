package mekanism.multiblockmachine.client.render.bloom;

import gregtech.client.renderer.IRenderSetup;
import gregtech.client.utils.EffectRenderContext;
import gregtech.client.utils.IBloomEffect;
import mekanism.api.util.time.Timeticks;
import mekanism.client.render.MekanismRenderer;
import mekanism.multiblockmachine.client.model.generator.ModelLargeGasGenerator;
import mekanism.multiblockmachine.client.render.generator.RenderLargeGasGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeGasGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class BloomRendererLargeGasGenerator implements IBloomEffect, IRenderSetup {

    private final TileEntityLargeGasGenerator gasGenerator;

    public BloomRendererLargeGasGenerator(final TileEntityLargeGasGenerator gasGenerator) {
        this.gasGenerator = gasGenerator;
    }

    @Override
    public void preDraw(@NotNull final BufferBuilder bufferBuilder) {

    }

    @Override
    public void postDraw(@NotNull final BufferBuilder bufferBuilder) {

    }

    @Override
    public void renderBloomEffect(@NotNull final BufferBuilder bufferBuilder, @NotNull final EffectRenderContext effectRenderContext) {
        GlStateManager.pushMatrix();
        BlockPos pos = gasGenerator.getPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        double cX = effectRenderContext.cameraX();
        double cY = effectRenderContext.cameraY();
        double cZ = effectRenderContext.cameraZ();
        GlStateManager.translate((x + 0.5F) - cX, (y + 1.5F) - cY, (z + 0.5F) - cZ);
        RenderLargeGasGenerator renderer = (RenderLargeGasGenerator) TileEntityRendererDispatcher.instance.renderers.get(TileEntityLargeGasGenerator.class);
        MekanismRenderer.rotate(gasGenerator.facing, 0, 180, 90, 270);
        GlStateManager.rotate(180, 0, 0, 1);
        Timeticks time = renderer.getTime();
        ModelLargeGasGenerator model = renderer.getModel();
        double tick = time.getValue() / 20F;
        model.renderBloom(tick, 0.0625F, gasGenerator.getActive(), Minecraft.getMinecraft().renderEngine);
        GlStateManager.popMatrix();
    }

}
