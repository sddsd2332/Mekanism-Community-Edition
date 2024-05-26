package mekanism.client.render.tileentity;

import mekanism.api.gas.GasStack;
import mekanism.client.model.ModelChemicalCrystallizer;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.tile.machine.TileEntityChemicalCrystallizer;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.renderer.GlStateManager;
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
public class RenderChemicalCrystallizer extends TileEntitySpecialRenderer<TileEntityChemicalCrystallizer> {

    private static final int stages = 10000;
    private ModelChemicalCrystallizer model = new ModelChemicalCrystallizer();
    private Map<EnumFacing, MekanismRenderer.DisplayInteger[]> energyDisplays = new EnumMap<>(EnumFacing.class);

    @Override
    public void render(TileEntityChemicalCrystallizer tileEntity, double x, double y, double z, float partialTick, int destroyStage, float alpha) {

        if (tileEntity.inputTank.getStored() > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableCull();
            GlStateManager.disableLighting();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.translate((float) x, (float) y, (float) z);
            MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
            MekanismRenderer.color(tileEntity.inputTank.getGas());
            getDisplayList(tileEntity.facing, tileEntity.inputTank.getGas())[tileEntity.getScaledFuelLevel(stages - 1)].render();
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
        bindTexture(MekanismUtils.getResource(ResourceType.RENDER, "ChemicalCrystallizer.png"));
        MekanismRenderer.rotate(tileEntity.facing, 0, 180, 90, 270);
        GlStateManager.rotate(180, 0, 0, 1);
        model.render(0.0625F);
        GlStateManager.popMatrix();
        MekanismRenderer.machineRenderer().render(tileEntity, x, y, z, partialTick, destroyStage, alpha);
    }

    @SuppressWarnings("incomplete-switch")
    private MekanismRenderer.DisplayInteger[] getDisplayList(EnumFacing side, GasStack gasStack) {
        if (energyDisplays.containsKey(side)) {
            return energyDisplays.get(side);
        }

        MekanismRenderer.DisplayInteger[] displays = new MekanismRenderer.DisplayInteger[stages];
        MekanismRenderer.Model3D model3D = new MekanismRenderer.Model3D();
        model3D.baseBlock = Blocks.WATER;
        model3D.setTexture(gasStack.getGas().getSprite());
        for (int i = 0; i < stages; i++) {
            displays[i] = MekanismRenderer.DisplayInteger.createAndStart();
            model3D.minZ = 0.125;
            model3D.maxZ = 0.875;
            model3D.minX = 0.125;
            model3D.maxX = 0.875;
            model3D.minY = 0.4375 + 0.001;  //prevent z fighting at low fuel levels
            model3D.maxY = 0.4375 + ((float) i / stages) * 0.25 + 0.001;

            MekanismRenderer.renderObject(model3D);
            MekanismRenderer.DisplayInteger.endList();
        }

        energyDisplays.put(side, displays);
        return displays;
    }
}
