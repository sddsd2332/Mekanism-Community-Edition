package mekanism.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelIsotopicCentrifuge extends ModelBase {

    ModelRenderer Base;
    ModelRenderer decoration;
    ModelRenderer decoration1;
    ModelRenderer decoration2;
    ModelRenderer decoration3;
    ModelRenderer Partition;
    ModelRenderer Partition2;
    ModelRenderer Partition3;
    ModelRenderer Partition4;
    ModelRenderer Connect;
    ModelRenderer Entry;
    ModelRenderer decoration4;
    ModelRenderer decoration5;
    ModelRenderer decoration6;
    ModelRenderer decoration7;
    ModelRenderer inside2;
    ModelRenderer inside3;
    ModelRenderer inside4;
    ModelRenderer Subject;
    ModelRenderer Subject2;
    ModelRenderer Subject3;
    ModelRenderer Subject4;
    ModelRenderer Subject5;
    ModelRenderer Base2;
    ModelRenderer Glass;
    ModelRenderer Glass2;
    ModelRenderer Glass3;
    ModelRenderer Glass4;
    ModelRenderer Base3;


    public ModelIsotopicCentrifuge() {
        textureWidth = 128;
        textureHeight = 128;

        Base = new ModelRenderer(this, 0, 0);
        Base.addBox(-8.0F, -2.0F, -8.0F, 16, 2, 16);
        Base.setRotationPoint(0.0F, 24.0F, 0.0F);
        Base.setTextureSize(128, 128);
        Base.mirror = true;
        setRotation(Base, 0F, 0F, 0F);

        decoration = new ModelRenderer(this, 50, 19);
        decoration.addBox(14.0F, 5.0F, -15.0F, 1, 1, 1);
        decoration.setRotationPoint(-8.0F, 16.0F, 8.0F);
        decoration.setTextureSize(128, 128);
        decoration.mirror = true;
        setRotation(decoration, 0F, 0F, 0F);

        decoration1 = new ModelRenderer(this, 49, 28);
        decoration1.setRotationPoint(-8.0F, 16.0F, 8.0F);
        decoration1.addBox(1.0F, 5.0F, -2.0F, 1, 1, 1);
        decoration1.setTextureSize(128, 128);
        decoration1.mirror = true;
        setRotation(decoration1, 0F, 0F, 0F);

        decoration2 = new ModelRenderer(this, 49, 7);
        decoration2.setRotationPoint(-8.0F, 16.0F, 8.0F);
        decoration2.addBox(14.0F, 5.0F, -2.0F, 1, 1, 1);
        decoration2.setTextureSize(128, 128);
        decoration2.mirror = true;
        setRotation(decoration2, 0F, 0F, 0F);

        decoration3 = new ModelRenderer(this, 49, 4);
        decoration3.setRotationPoint(-8.0F, 16.0F, 8.0F);
        decoration3.addBox(1.0F, 5.0F, -15.0F, 1, 1, 1);
        decoration3.setTextureSize(128, 128);
        decoration3.mirror = true;
        setRotation(decoration3, 0F, 0F, 0F);

        Partition = new ModelRenderer(this, 49, 0);
        Partition.setRotationPoint(-8.0F, 16.0F, 8.0F);
        Partition.addBox(2.0F, 4.0F, -14.0F, 12, 1, 12);
        Partition.setTextureSize(128, 128);
        Partition.mirror = true;
        setRotation(Partition, 0F, 0F, 0F);

        Partition2 = new ModelRenderer(this, 37, 47);
        Partition2.setRotationPoint(-8.0F, 16.0F, 8.0F);
        Partition2.addBox(2.0F, 2.0F, -14.0F, 12, 1, 12);
        Partition2.setTextureSize(128, 128);
        Partition2.mirror = true;
        setRotation(Partition2, 0F, 0F, 0F);

        Partition3 = new ModelRenderer(this, 0, 45);
        Partition3.setRotationPoint(-8.0F, 16.0F, 8.0F);
        Partition3.addBox(2.0F, 0.0F, -14.0F, 12, 1, 12);
        Partition3.setTextureSize(128, 128);
        Partition3.mirror = true;
        setRotation(Partition3, 0F, 0F, 0F);

        Partition4 = new ModelRenderer(this, 29, 31);
        Partition4.setRotationPoint(-8.0F, 16.0F, 8.0F);
        Partition4.addBox(2.0F, -2.0F, -14.0F, 12, 1, 12);
        Partition4.setTextureSize(128, 128);
        Partition4.mirror = true;
        setRotation(Partition4, 0F, 0F, 0F);

        Connect = new ModelRenderer(this, 61, 61);
        Connect.setRotationPoint(-8.0F, 16.0F, 8.0F);
        Connect.addBox(5.0F, -3.0F, -15.0F, 6, 6, 2);
        Connect.setTextureSize(128, 128);
        Connect.mirror = true;
        setRotation(Connect, 0F, 0F, 0F);

        Entry = new ModelRenderer(this, 31, 19);
        Entry.setRotationPoint(-8.0F, 16.0F, 8.0F);
        Entry.addBox(4.0F, -4.0F, -16.0F, 8, 8, 1);
        Entry.setTextureSize(128, 128);
        Entry.mirror = true;
        setRotation(Entry, 0F, 0F, 0F);

        decoration4 = new ModelRenderer(this, 5, 19);
        decoration4.setRotationPoint(-8.0F, 16.0F, 8.0F);
        decoration4.addBox(13.0F, -6.0F, -6.0F, 1, 4, 1);
        decoration4.setTextureSize(128, 128);
        decoration4.mirror = true;
        setRotation(decoration4, 0F, 0F, 0F);

        decoration5 = new ModelRenderer(this, 37, 45);
        decoration5.setRotationPoint(-8.0F, 16.0F, 8.0F);
        decoration5.addBox(10.0F, -24.0F, -12.0F, 2, 2, 2);
        decoration5.setTextureSize(128, 128);
        decoration5.mirror = true;
        setRotation(decoration5, 0F, 0F, 0F);

        decoration6 = new ModelRenderer(this, 61, 70);
        decoration6.setRotationPoint(-8.0F, 16.0F, 8.0F);
        decoration6.addBox(6.0F, -23.0F, -10.0F, 4, 1, 4);
        decoration6.setTextureSize(128, 128);
        decoration6.mirror = true;
        setRotation(decoration6, 0F, 0F, 0F);

        decoration7 = new ModelRenderer(this, 49, 0);
        decoration7.setRotationPoint(-8.0F, 16.0F, 8.0F);
        decoration7.addBox(10.0F, -24.0F, -6.0F, 1, 2, 1);
        decoration7.setTextureSize(128, 128);
        decoration7.mirror = true;
        setRotation(decoration7, 0F, 0F, 0F);

        inside2 = new ModelRenderer(this, 5, 24);
        inside2.setRotationPoint(-8.0F, 16.0F, 8.0F);
        inside2.addBox(7.0F, -9.0F, -9.0F, 1, 2, 1);
        inside2.setTextureSize(128, 128);
        inside2.mirror = true;
        setRotation(inside2, 0F, 0F, 0F);

        inside3 = new ModelRenderer(this, 0, 19);
        inside3.setRotationPoint(-8.0F, 16.0F, 8.0F);
        inside3.addBox(9.0F, -16.0F, -9.0F, 1, 8, 1);
        inside3.setTextureSize(128, 128);
        inside3.mirror = true;
        setRotation(inside3, 0F, 0F, 0F);

        inside4 = new ModelRenderer(this, 10, 0);
        inside4.setRotationPoint(-8.0F, 16.0F, 8.0F);
        inside4.addBox(8.0F, -9.0F, -9.0F, 1, 1, 1);
        inside4.setTextureSize(128, 128);
        inside4.mirror = true;
        setRotation(inside4, 0F, 0F, 0F);

        Subject = new ModelRenderer(this, 0, 19);
        Subject.setRotationPoint(-8.0F, 16.0F, 8.0F);
        Subject.addBox(3.0F, -7.0F, -13.0F, 10, 13, 10);
        Subject.setTextureSize(128, 128);
        Subject.mirror = true;
        setRotation(Subject, 0F, 0F, 0F);

        Subject2 = new ModelRenderer(this, 26, 59);
        Subject2.setRotationPoint(-8.0F, 3.0F, 8.0F);
        Subject2.addBox(10.0F, -6.0F, -13.0F, 3, 12, 3);
        Subject2.setTextureSize(128, 128);
        Subject2.mirror = true;
        setRotation(Subject2, 0F, 0F, 0F);

        Subject3 = new ModelRenderer(this, 13, 59);
        Subject3.setRotationPoint(-8.0F, 3.0F, 8.0F);
        Subject3.addBox(3.0F, -6.0F, -6.0F, 3, 12, 3);
        Subject3.setTextureSize(128, 128);
        Subject3.mirror = true;
        setRotation(Subject3, 0F, 0F, 0F);

        Subject4 = new ModelRenderer(this, 0, 59);
        Subject4.setRotationPoint(-4.0F, 3.0F, 8.0F);
        Subject4.addBox(-1.0F, -6.0F, -13.0F, 3, 12, 3);
        Subject4.setTextureSize(128, 128);
        Subject4.mirror = true;
        setRotation(Subject4, 0F, 0F, 0F);

        Subject5 = new ModelRenderer(this, 0, 0);
        Subject5.setRotationPoint(-4.0F, 3.0F, 8.0F);
        Subject5.addBox(6.0F, -6.0F, -6.0F, 3, 12, 3);
        Subject5.setTextureSize(128, 128);
        Subject5.mirror = true;
        setRotation(Subject5, 0F, 0F, 0F);

        Base2 = new ModelRenderer(this, 55, 14);
        Base2.setRotationPoint(0.0F, 24.0F, 0.0F);
        Base2.addBox(-5.0F, -30.0F, -5.0F, 10, 3, 10);
        Base2.setTextureSize(128, 128);
        Base2.mirror = true;
        setRotation(Base2, 0F, 0F, 0F);

        Glass = new ModelRenderer(this, 66, 28);
        Glass.setRotationPoint(0.0F, 24.0F, 0.0F);
        Glass.addBox(-2.0F, -27.0F, 3.0F, 4, 12, 1);
        Glass.setTextureSize(128, 128);
        Glass.mirror = true;
        setRotation(Glass, 0F, 0F, 0F);

        Glass2 = new ModelRenderer(this, 0, 43);
        Glass2.setRotationPoint(0.0F, 24.0F, 0.0F);
        Glass2.addBox(-2.0F, -27.0F, -4.0F, 4, 12, 1);
        Glass2.setTextureSize(128, 128);
        Glass2.mirror = true;
        setRotation(Glass2, 0F, 0F, 0F);

        Glass3 = new ModelRenderer(this, 50, 61);
        Glass3.setRotationPoint(0.0F, 24.0F, 0.0F);
        Glass3.addBox(-4.0F, -27.0F, -2.0F, 1, 12, 4);
        Glass3.setTextureSize(128, 128);
        Glass3.mirror = true;
        setRotation(Glass3, 0F, 0F, 0F);

        Glass4 = new ModelRenderer(this, 39, 61);
        Glass4.setRotationPoint(0.0F, 24.0F, 0.0F);
        Glass4.addBox(3.0F, -27.0F, -2.0F, 1, 12, 4);
        Glass4.setTextureSize(128, 128);
        Glass4.mirror = true;
        setRotation(Glass4, 0F, 0F, 0F);


        Base3 = new ModelRenderer(this, 55, 14);
        Base3.setRotationPoint(0.0F, 24.0F, 0.0F);
        Base3.addBox(2.0F, -16.0F, -2.0F, 3, 1, 1);
        Base3.setTextureSize(128, 128);
        Base3.mirror = true;
        setRotation(Base3, 0F, 0F, 0F);
    }

    public void render(float size) {
        Base.render(size);
        decoration.render(size);
        decoration1.render(size);
        decoration2.render(size);
        decoration3.render(size);
        Partition.render(size);
        Partition2.render(size);
        Partition3.render(size);
        Partition4.render(size);
        Connect.render(size);
        Entry.render(size);
        decoration4.render(size);
        decoration5.render(size);
        decoration6.render(size);
        decoration7.render(size);
        inside2.render(size);
        inside3.render(size);
        inside4.render(size);
        Subject.render(size);
        Subject2.render(size);
        Subject3.render(size);
        Subject4.render(size);
        Subject5.render(size);
        Base2.render(size);
        Base3.render(size);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Glass.render(size);
        Glass2.render(size);
        Glass3.render(size);
        Glass4.render(size);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();


    }

    public void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
