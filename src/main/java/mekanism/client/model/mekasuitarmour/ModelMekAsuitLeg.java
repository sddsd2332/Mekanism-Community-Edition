package mekanism.client.model.mekasuitarmour;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelMekAsuitLeg extends ModelBiped {

    public static final ModelMekAsuitLeg leg = new ModelMekAsuitLeg();

    ModelRenderer right_leg_armor;
    ModelRenderer leggings_right_leg_exo_brace3_r1;
    ModelRenderer leggings_right_leg_exo_frame1_r1;
    ModelRenderer left_leg_armor;
    ModelRenderer leggings_left_leg_exo_brace3_r1;
    ModelRenderer leggings_left_leg_exo_frame1_r1;

    public ModelMekAsuitLeg() {
        super(0, 0, 128, 128);
        textureWidth = 128;
        textureHeight = 128;

        right_leg_armor = new ModelRenderer(this);
        right_leg_armor.setRotationPoint(-2.0F, 12.5F, 0.0F);
        right_leg_armor.cubeList.add(new ModelBox(right_leg_armor, 48, 2, -0.5F, -8.0F, -1.5F, 5, 4, 4, 0.0F, false));
        right_leg_armor.cubeList.add(new ModelBox(right_leg_armor, 20, 0, 0.505F, -8.0F, -2.5F, 3, 2, 1, 0.0F, false));
        right_leg_armor.cubeList.add(new ModelBox(right_leg_armor, 0, 61, -0.5F, -13.0F, -2.5F, 2, 4, 5, 0.0F, false));
        right_leg_armor.cubeList.add(new ModelBox(right_leg_armor, 80, 69, 1.5F, -13.0F, -2.5F, 1, 2, 4, 0.0F, false));
        right_leg_armor.cubeList.add(new ModelBox(right_leg_armor, 0, 32, -0.25F, -7.5F, -2.25F, 1, 1, 1, 0.0F, false));
        right_leg_armor.cubeList.add(new ModelBox(right_leg_armor, 52, 40, 0.5F, -7.5F, 2.0F, 3, 2, 2, 0.0F, false));
        right_leg_armor.cubeList.add(new ModelBox(right_leg_armor, 74, 64, -1.0F, -7.9772F, 0.0F, 3, 2, 3, 0.0F, false));
        right_leg_armor.cubeList.add(new ModelBox(right_leg_armor, 0, 35, -0.01F, -12.5F, -2.01F, 4, 12, 4, 0.0F, false));

        leggings_right_leg_exo_brace3_r1 = new ModelRenderer(this);
        leggings_right_leg_exo_brace3_r1.setRotationPoint(2.0F, 3.5F, 0.0F);
        right_leg_armor.addChild(leggings_right_leg_exo_brace3_r1);
        setRotationAngle(leggings_right_leg_exo_brace3_r1, 0.0873F, 0.0F, 0.0873F);
        leggings_right_leg_exo_brace3_r1.cubeList.add(new ModelBox(leggings_right_leg_exo_brace3_r1, 32, 77, -4.0975F, -17.0787F, 0.0009F, 1, 6, 3, 0.0F, false));

        leggings_right_leg_exo_frame1_r1 = new ModelRenderer(this);
        leggings_right_leg_exo_frame1_r1.setRotationPoint(0.0F, 6.0F, 3.0F);
        right_leg_armor.addChild(leggings_right_leg_exo_frame1_r1);
        setRotationAngle(leggings_right_leg_exo_frame1_r1, 0.0873F, 0.0F, 0.0F);
        leggings_right_leg_exo_frame1_r1.cubeList.add(new ModelBox(leggings_right_leg_exo_frame1_r1, 78, 79, 1.0F, -21.4524F, 0.5899F, 2, 9, 1, 0.0F, false));

        left_leg_armor = new ModelRenderer(this);
        left_leg_armor.setRotationPoint(2.0F, 12.5F, 0.0F);
        left_leg_armor.cubeList.add(new ModelBox(left_leg_armor, 0, 53, -4.5F, -8.0F, -1.5F, 5, 4, 4, 0.0F, false));
        left_leg_armor.cubeList.add(new ModelBox(left_leg_armor, 51, 59, -3.495F, -8.0F, -2.5F, 3, 2, 1, 0.0F, false));
        left_leg_armor.cubeList.add(new ModelBox(left_leg_armor, 61, 5, -1.5F, -13.0F, -2.5F, 2, 4, 5, 0.0F, false));
        left_leg_armor.cubeList.add(new ModelBox(left_leg_armor, 83, 14, -2.5F, -13.0F, -2.5F, 1, 2, 4, 0.0F, false));
        left_leg_armor.cubeList.add(new ModelBox(left_leg_armor, 59, 45, -0.75F, -7.5F, -2.25F, 1, 1, 1, 0.0F, false));
        left_leg_armor.cubeList.add(new ModelBox(left_leg_armor, 66, 73, -3.5F, -7.5F, 2.0F, 3, 2, 2, 0.0F, false));
        left_leg_armor.cubeList.add(new ModelBox(left_leg_armor, 79, 21, -2.0F, -7.9772F, 0.0F, 3, 2, 3, 0.0F, false));
        left_leg_armor.cubeList.add(new ModelBox(left_leg_armor, 28, 29, -4.01F, -12.5F, -2.01F, 4, 12, 4, 0.0F, false));

        leggings_left_leg_exo_brace3_r1 = new ModelRenderer(this);
        leggings_left_leg_exo_brace3_r1.setRotationPoint(-2.0F, 3.5F, 0.0F);
        left_leg_armor.addChild(leggings_left_leg_exo_brace3_r1);
        setRotationAngle(leggings_left_leg_exo_brace3_r1, 0.0873F, 0.0F, -0.0873F);
        leggings_left_leg_exo_brace3_r1.cubeList.add(new ModelBox(leggings_left_leg_exo_brace3_r1, 62, 79, 3.0975F, -17.0787F, 0.0009F, 1, 6, 3, 0.0F, false));

        leggings_left_leg_exo_frame1_r1 = new ModelRenderer(this);
        leggings_left_leg_exo_frame1_r1.setRotationPoint(0.0F, 6.0F, 3.0F);
        left_leg_armor.addChild(leggings_left_leg_exo_frame1_r1);
        setRotationAngle(leggings_left_leg_exo_frame1_r1, 0.0873F, 0.0F, 0.0F);
        leggings_left_leg_exo_frame1_r1.cubeList.add(new ModelBox(leggings_left_leg_exo_frame1_r1, 48, 81, -3.0F, -21.4524F, 0.5899F, 2, 9, 1, 0.0F, false));

        bipedLeftLeg.cubeList.clear();
        bipedLeftLeg.addChild(left_leg_armor);
        bipedRightLeg.cubeList.clear();
        bipedRightLeg.addChild(right_leg_armor);


    }


    public void render(float size) {
        right_leg_armor.render(size);
        left_leg_armor.render(size);
    }

    public void renderLeft(float size) {
        left_leg_armor.render(size);
    }

    public void renderRight(float size) {
        right_leg_armor.render(size);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
