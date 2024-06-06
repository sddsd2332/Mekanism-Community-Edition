package mekanism.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelAntiprotonicNucleosynthesizer extends ModelBase {

    ModelRenderer cube;
    ModelRenderer cube2;
    ModelRenderer cube3;
    ModelRenderer cube4;
    ModelRenderer cube5;
    ModelRenderer cube6;
    ModelRenderer cube7;
    ModelRenderer cube8;
    ModelRenderer cube9;
    ModelRenderer cube10;
    ModelRenderer cube11;
    ModelRenderer cube12;
    ModelRenderer cube13;
    ModelRenderer cube14;
    ModelRenderer cube15;
    ModelRenderer cube16;
    ModelRenderer cube17;
    ModelRenderer cube18;
    ModelRenderer cube19;
    ModelRenderer cube20;
    ModelRenderer cube21;
    ModelRenderer cube22;
    ModelRenderer cube23;
    ModelRenderer cube24;
    ModelRenderer Glass;
    ModelRenderer Glass2;


    public ModelAntiprotonicNucleosynthesizer() {
        textureWidth = 128;
        textureHeight = 128;

        cube = new ModelRenderer(this, 0, 0);
        cube.addBox(-8.0F, -3.0F, -8.0F, 16, 3, 16);
        cube.setRotationPoint(0.0F, 24.0F, 0.0F);
        cube.setTextureSize(128, 128);
        cube.mirror = true;
        setRotation(cube, 0F, 0F, 0F);

        cube2 = new ModelRenderer(this, 0, 39);
        cube2.addBox(-7.0F, -5.0F, -7.0F, 14, 2, 14);
        cube2.setRotationPoint(0.0F, 24.0F, 0.0F);
        cube2.setTextureSize(128, 128);
        cube2.mirror = true;
        setRotation(cube2, 0F, 0F, 0F);

        cube3 = new ModelRenderer(this, 0, 20);
        cube3.addBox(-8.0F, -8.0F, -8.0F, 16, 3, 15);
        cube3.setRotationPoint(0.0F, 24.0F, 0.0F);
        cube3.setTextureSize(128, 128);
        cube3.mirror = true;
        setRotation(cube3, 0F, 0F, 0F);

        cube4 = new ModelRenderer(this, 52, 7);
        cube4.addBox(4.0F, -10.0F, -8.0F, 4, 5, 13);
        cube4.setRotationPoint(0.0F, 21.0F, 0.0F);
        cube4.setTextureSize(128, 128);
        cube4.mirror = true;
        setRotation(cube4, 0F, 0F, 0F);

        cube5 = new ModelRenderer(this, 44, 49);
        cube5.addBox(-8.0F, -10.0F, -8.0F, 4, 5, 13);
        cube5.setRotationPoint(0.0F, 21.0F, 0.0F);
        cube5.setTextureSize(128, 128);
        cube5.mirror = true;
        setRotation(cube5, 0F, 0F, 0F);

        cube6 = new ModelRenderer(this, 52, 68);
        cube6.addBox(-4.0F, -10.0F, 2.0F, 8, 5, 3);
        cube6.setRotationPoint(0.0F, 21.0F, 0.0F);
        cube6.setTextureSize(128, 128);
        cube6.mirror = true;
        setRotation(cube6, 0F, 0F, 0F);

        cube7 = new ModelRenderer(this, 0, 39);
        cube7.addBox(3.0F, -11.0F, -4.0F, 1, 5, 5);
        cube7.setRotationPoint(0.0F, 21.0F, 0.0F);
        cube7.setTextureSize(128, 128);
        cube7.mirror = true;
        setRotation(cube7, 0F, 0F, 0F);

        cube8 = new ModelRenderer(this, 0, 20);
        cube8.addBox(-4.0F, -11.0F, -4.0F, 1, 5, 5);
        cube8.setRotationPoint(0.0F, 21.0F, 0.0F);
        cube8.setTextureSize(128, 128);
        cube8.mirror = true;
        setRotation(cube8, 0F, 0F, 0F);

        cube9 = new ModelRenderer(this, 8, 20);
        cube9.addBox(-3.0F, -9.0F, -2.0F, 1, 1, 1);
        cube9.setRotationPoint(0.0F, 21.0F, 0.0F);
        cube9.setTextureSize(128, 128);
        cube9.mirror = true;
        setRotation(cube9, 0F, 0F, 0F);

        cube10 = new ModelRenderer(this, 0, 20);
        cube10.addBox(2.0F, -9.0F, -2.0F, 1, 1, 1);
        cube10.setRotationPoint(0.0F, 21.0F, 0.0F);
        cube10.setTextureSize(128, 128);
        cube10.mirror = true;
        setRotation(cube10, 0F, 0F, 0F);

        cube11 = new ModelRenderer(this, 0, 66);
        cube11.addBox(-8.0F, -13.0F, -8.0F, 4, 3, 10);
        cube11.setRotationPoint(0.0F, 21.0F, 0.0F);
        cube11.setTextureSize(128, 128);
        cube11.mirror = true;
        setRotation(cube11, 0F, 0F, 0F);

        cube12 = new ModelRenderer(this, 23, 58);
        cube12.addBox(4.0F, -13.0F, -8.0F, 4, 3, 10);
        cube12.setRotationPoint(0.0F, 21.0F, 0.0F);
        cube12.setTextureSize(128, 128);
        cube12.mirror = true;
        setRotation(cube12, 0F, 0F, 0F);

        cube13 = new ModelRenderer(this, 43, 39);
        cube13.addBox(-8.0F, -13.0F, -10.0F, 16, 3, 6);
        cube13.setRotationPoint(0.0F, 21.0F, 12.0F);
        cube13.setTextureSize(128, 128);
        cube13.mirror = true;
        setRotation(cube13, 0F, 0F, 0F);

        cube14 = new ModelRenderer(this, 0, 12);
        cube14.addBox(4.0F, -11.0F, -2.0F, 4, 1, 1);
        cube14.setRotationPoint(-6.0F, 31.0F, 9.0F);
        cube14.setTextureSize(128, 128);
        cube14.mirror = true;
        setRotation(cube14, 0F, 0F, 0F);

        cube15 = new ModelRenderer(this, 0, 9);
        cube15.addBox(4.0F, -20.0F, -2.0F, 4, 1, 1);
        cube15.setRotationPoint(-6.0F, 31.0F, 9.0F);
        cube15.setTextureSize(128, 128);
        cube15.mirror = true;
        setRotation(cube15, 0F, 0F, 0F);

        cube16 = new ModelRenderer(this, 66, 49);
        cube16.addBox(0.0F, -27.0F, -2.0F, 8, 8, 1);
        cube16.setRotationPoint(-4.0F, 39.0F, 9.0F);
        cube16.setTextureSize(128, 128);
        cube16.mirror = true;
        setRotation(cube16, 0F, 0F, 0F);

        cube17 = new ModelRenderer(this, 0, 0);
        cube17.addBox(4.0F, -20.0F, -5.0F, 4, 5, 3);
        cube17.setRotationPoint(-6.0F, 31.0F, 9.0F);
        cube17.setTextureSize(128, 128);
        cube17.mirror = true;
        setRotation(cube17, 0F, 0F, 0F);

        cube18 = new ModelRenderer(this, 48, 30);
        cube18.addBox(-1.0F, -17.0F, -4.0F, 14, 1, 2);
        cube18.setRotationPoint(-6.0F, 31.0F, 9.0F);
        cube18.setTextureSize(128, 128);
        cube18.mirror = true;
        setRotation(cube18, 0F, 0F, 0F);

        cube19 = new ModelRenderer(this, 48, 26);
        cube19.addBox(-1.0F, -19.0F, -4.0F, 14, 1, 2);
        cube19.setRotationPoint(-6.0F, 31.0F, 9.0F);
        cube19.setTextureSize(128, 128);
        cube19.mirror = true;
        setRotation(cube19, 0F, 0F, 0F);

        cube20 = new ModelRenderer(this, 0, 56);
        cube20.addBox(-6.0F, -10.0F, 5.0F, 1, 5, 1);
        cube20.setRotationPoint(0.0F, 21.0F, 0.0F);
        cube20.setTextureSize(128, 128);
        cube20.mirror = true;
        setRotation(cube20, 0F, 0F, 0F);

        cube21 = new ModelRenderer(this, 53, 9);
        cube21.addBox(-6.0F, -10.0F, 5.0F, 1, 5, 1);
        cube21.setRotationPoint(2.0F, 21.0F, 0.0F);
        cube21.setTextureSize(128, 128);
        cube21.mirror = true;
        setRotation(cube21, 0F, 0F, 0F);

        cube22 = new ModelRenderer(this, 49, 3);
        cube22.addBox(1.0F, -10.0F, 5.0F, 1, 5, 1);
        cube22.setRotationPoint(2.0F, 21.0F, 0.0F);
        cube22.setTextureSize(128, 128);
        cube22.mirror = true;
        setRotation(cube22, 0F, 0F, 0F);

        cube23 = new ModelRenderer(this, 11, 9);
        cube23.addBox(3.0F, -10.0F, 5.0F, 1, 5, 1);
        cube23.setRotationPoint(2.0F, 21.0F, 0.0F);
        cube23.setTextureSize(128, 128);
        cube23.mirror = true;
        setRotation(cube23, 0F, 0F, 0F);

        cube24 = new ModelRenderer(this, 49, 0);
        cube24.addBox(2.0F, -22.0F, -16.0F, 8, 1, 1);
        cube24.setRotationPoint(-6.0F, 31.0F, 9.0F);
        cube24.setTextureSize(128, 128);
        cube24.mirror = true;
        setRotation(cube24, 0F, 0F, 0F);


        Glass = new ModelRenderer(this, 0, 56);
        Glass.setRotationPoint(4.0F, 10.0F, -6.0F);
        Glass.addBox(-8.0F, -1.0F, 0.0F, 8, 1, 8);
        Glass.setTextureSize(128, 128);
        Glass.mirror = true;
        setRotation(Glass, 0F, 0F, 0F);

        Glass2 = new ModelRenderer(this, 29, 72);
        Glass2.setRotationPoint(4.0F, 16.0F, -7.0F);
        Glass2.addBox(-8.0F, -6.0F, 0.0F, 8, 6, 1);
        Glass2.setTextureSize(128, 128);
        Glass2.mirror = true;
        setRotation(Glass2, 0F, 0F, 0F);

    }

    public void render(float size, boolean isEnableGlow) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        doRender(size);
        if (!isEnableGlow) {
            Glass.render(size);
            Glass2.render(size);
        }
        GlStateManager.popMatrix();
        if (isEnableGlow) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            Glass.render(size);
            Glass2.render(size);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
        }
    }

    private void doRender(float size) {
        cube.render(size);
        cube2.render(size);
        cube3.render(size);
        cube4.render(size);
        cube5.render(size);
        cube6.render(size);
        cube7.render(size);
        cube8.render(size);
        cube9.render(size);
        cube10.render(size);
        cube11.render(size);
        cube12.render(size);
        cube13.render(size);
        cube14.render(size);
        cube15.render(size);
        cube16.render(size);
        cube17.render(size);
        cube18.render(size);
        cube19.render(size);
        cube20.render(size);
        cube21.render(size);
        cube22.render(size);
        cube23.render(size);
        cube24.render(size);
    }

    public void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
