package mekanism.client.render.tileentity;

import mekanism.api.gas.GasStack;
import mekanism.client.model.ModelNutritionalLiquifier;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.tile.machine.TileEntityNutritionalLiquifier;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.EnumMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderNutritionalLiquifier extends TileEntitySpecialRenderer<TileEntityNutritionalLiquifier> {

    private static final int stages = 10000;
    private ModelNutritionalLiquifier model = new ModelNutritionalLiquifier();
    private Map<EnumFacing, MekanismRenderer.DisplayInteger[]> energyDisplays = new EnumMap<>(EnumFacing.class);

    @SuppressWarnings("incomplete-switch")
    @Override
    public void render(TileEntityNutritionalLiquifier tileEntity, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
        ItemStack stack = tileEntity.getStackInSlot(0);

        if (!stack.isEmpty() && tileEntity.gasTank.getStored() < tileEntity.gasTank.getMaxGas() / 2) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5D, y + 0.65D, z + 0.5D);
            float scale = stack.getItem() instanceof ItemBlock ? 0.425F : 0.325F;
            GlStateManager.scale(scale, scale, scale);
            double tick = Minecraft.getSystemTime() / 800.0D;
            GlStateManager.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            GlStateManager.rotate((float) (((tick * 40.0D) % 360)), 0, 1, 0);
            GlStateManager.disableLighting();
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }

        if (tileEntity.gasTank.getStored() > 0) {
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
            MekanismRenderer.color(tileEntity.gasTank.getGas());
            getDisplayList(tileEntity.facing, tileEntity.gasTank.getGas())[tileEntity.getScaledFuelLevel(stages - 1)].render();
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
        bindTexture(MekanismUtils.getResource(ResourceType.RENDER, "NutritionalLiquifier.png"));
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


            model3D.minZ = 0.0625;
            model3D.maxZ = 0.9375;

            model3D.minX = 0.0625;
            model3D.maxX = 0.9375;

            model3D.minY = 0.3125 + 0.001;  //prevent z fighting at low fuel levels
            model3D.maxY = 0.3125 + ((float) i / stages) * 0.625 + 0.001;

            MekanismRenderer.renderObject(model3D);
            MekanismRenderer.DisplayInteger.endList();
        }

        energyDisplays.put(side, displays);
        return displays;
    }
}
