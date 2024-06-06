package mekanism.generators.client.render;

import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.MekanismRenderer.DisplayInteger;
import mekanism.client.render.MekanismRenderer.GlowInfo;
import mekanism.client.render.MekanismRenderer.Model3D;
import mekanism.common.FluidSlot;
import mekanism.common.MekanismFluids;
import mekanism.generators.client.model.ModelBioGenerator;
import mekanism.generators.common.tile.TileEntityBioGenerator;
import mekanism.generators.common.util.MekanismGeneratorUtils;
import mekanism.generators.common.util.MekanismGeneratorUtils.ResourceType;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.EnumMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderBioGenerator extends TileEntitySpecialRenderer<TileEntityBioGenerator> {

    public static final RenderBioGenerator INSTANCE = new RenderBioGenerator();

    private static Map<EnumFacing, DisplayInteger[]> energyDisplays = new EnumMap<>(EnumFacing.class);

    private ModelBioGenerator model = new ModelBioGenerator();

    private static final int stages = 800;

    public static void resetDisplayInts() {
        energyDisplays.clear();
    }
    @Override
    public void render(TileEntityBioGenerator tileEntity, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
        if (tileEntity.bioFuelSlot.fluidStored > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableCull();
            GlStateManager.disableLighting();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.translate((float) x, (float) y, (float) z);
            GlowInfo glowInfo = MekanismRenderer.enableGlow();
            MekanismRenderer.color(MekanismFluids.Biofuel);
            DisplayInteger[] displayList = getDisplayList(tileEntity.facing);
            displayList[tileEntity.getScaledFuelLevel(stages - 1)].render();
            MekanismRenderer.resetColor();
            MekanismRenderer.disableGlow(glowInfo);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableLighting();
            GlStateManager.disableCull();
            GlStateManager.popMatrix();
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        bindTexture(MekanismGeneratorUtils.getResource(ResourceType.RENDER, "BioGenerator.png"));

        MekanismRenderer.rotate(tileEntity.facing, 180, 0, 270, 90);

        GlStateManager.rotate(180, 0, 0, 1);
        model.render(0.0625F,true);
        GlStateManager.popMatrix();
    }

    @SuppressWarnings("incomplete-switch")
    private DisplayInteger[] getDisplayList(EnumFacing side) {
        if (energyDisplays.containsKey(side)) {
            return energyDisplays.get(side);
        }
        Model3D model3D = new Model3D();
        model3D.baseBlock = Blocks.WATER;
        model3D.setTexture(MekanismFluids.Biofuel.getSprite());

        DisplayInteger[] displays = new DisplayInteger[stages];
        energyDisplays.put(side,displays);

        for (int i = 0; i < stages; i++) {
            displays[i] = DisplayInteger.createAndStart();

            switch (side) {
                case NORTH:
                    model3D.minZ = 0.5;
                    model3D.maxZ = 0.875;

                    model3D.minX = 0.1875;
                    model3D.maxX = 0.8215;
                    break;
                case SOUTH:
                    model3D.minZ = 0.125;
                    model3D.maxZ = 0.5;

                    model3D.minX = 0.1875;
                    model3D.maxX = 0.8215;
                    break;
                case WEST:
                    model3D.minX = 0.5;
                    model3D.maxX = 0.875;

                    model3D.minZ = 0.1875;
                    model3D.maxZ = 0.8215;
                    break;
                case EAST:
                    model3D.minX = 0.125;
                    model3D.maxX = 0.5;

                    model3D.minZ = 0.1875;
                    model3D.maxZ = 0.8215;
                    break;
            }

            model3D.minY = 0.4375 + 0.001;  //prevent z fighting at low fuel levels
            model3D.maxY = 0.4375 + ((float) i / stages) * 0.4375 + 0.001;

            MekanismRenderer.renderObject(model3D);
            DisplayInteger.endList();
        }
        return displays;
    }
}
