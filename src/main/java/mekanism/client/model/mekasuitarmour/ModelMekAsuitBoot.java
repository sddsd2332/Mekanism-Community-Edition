package mekanism.client.model.mekasuitarmour;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelMekAsuitBoot extends ModelBiped {

    public static final ModelMekAsuitBoot boot = new ModelMekAsuitBoot();
    ModelRenderer right_boot_armor;
    ModelRenderer shared_boots_leggings_right_leg_exo_frame_r1;
    ModelRenderer shared_boots_leggings_right_leg_exo_plate_r1;
    ModelRenderer boots_right_leg_plate1_r1;
    ModelRenderer boots_right_leg_shin_plate_r1;
    ModelRenderer left_boot_armor;
    ModelRenderer shared_boots_leggings_left_leg_exo_frame_r1;
    ModelRenderer shared_boots_leggings_left_leg_exo_plate_r1;
    ModelRenderer boots_left_leg_plate1_r1;
    ModelRenderer boots_left_leg_shin_plate_r1;

    public ModelMekAsuitBoot() {
        super(0, 0, 128, 128);
        textureWidth = 128;
        textureHeight = 128;

        right_boot_armor = new ModelRenderer(this);
        right_boot_armor.setRotationPoint(-2.0F, 12.5F, 0.0F);
        right_boot_armor.cubeList.add(new ModelBox(right_boot_armor, 56, 49, -0.505F, -3.5F, -2.0F, 4, 1, 2, 0.0F, false));
        right_boot_armor.cubeList.add(new ModelBox(right_boot_armor, 8, 51, 0.0F, -2.0F, -3.5F, 3, 1, 1, 0.0F, false));
        right_boot_armor.cubeList.add(new ModelBox(right_boot_armor, 14, 57, -0.01F, -6.5F, -2.01F, 4, 4, 4, 0.0F, false));
        right_boot_armor.cubeList.add(new ModelBox(right_boot_armor, 27, 52, 0.01F, -2.492F, -2.05F, 4, 2, 5, 0.0F, false));
        right_boot_armor.cubeList.add(new ModelBox(right_boot_armor, 40, 26, -1.01F, -1.495F, -3.0F, 4, 1, 6, 0.0F, false));

        shared_boots_leggings_right_leg_exo_frame_r1 = new ModelRenderer(this);
        shared_boots_leggings_right_leg_exo_frame_r1.setRotationPoint(0.0F, 6.0F, 3.0F);
        right_boot_armor.addChild(shared_boots_leggings_right_leg_exo_frame_r1);
        setRotationAngle(shared_boots_leggings_right_leg_exo_frame_r1, -0.0873F, 0.0F, 0.0F);
        shared_boots_leggings_right_leg_exo_frame_r1.cubeList.add(new ModelBox(shared_boots_leggings_right_leg_exo_frame_r1, 70, 79, 1.0F, -12.4524F, -2.5899F, 2, 6, 2, 0.0F, false));

        shared_boots_leggings_right_leg_exo_plate_r1 = new ModelRenderer(this);
        shared_boots_leggings_right_leg_exo_plate_r1.setRotationPoint(-5.0F, 3.5305F, 0.2618F);
        right_boot_armor.addChild(shared_boots_leggings_right_leg_exo_plate_r1);
        setRotationAngle(shared_boots_leggings_right_leg_exo_plate_r1, -0.0873F, 0.0F, 0.0F);
        shared_boots_leggings_right_leg_exo_plate_r1.cubeList.add(new ModelBox(shared_boots_leggings_right_leg_exo_plate_r1, 70, 25, 4.0F, -9.4486F, -1.0899F, 3, 5, 3, 0.0F, false));

        boots_right_leg_plate1_r1 = new ModelRenderer(this);
        boots_right_leg_plate1_r1.setRotationPoint(-0.5F, 10.5F, -3.0F);
        right_boot_armor.addChild(boots_right_leg_plate1_r1);
        setRotationAngle(boots_right_leg_plate1_r1, 0.7854F, 0.0F, 0.0F);
        boots_right_leg_plate1_r1.cubeList.add(new ModelBox(boots_right_leg_plate1_r1, 47, 15, -0.005F, -9.3388F, 8.8389F, 4, 2, 1, 0.0F, false));

        boots_right_leg_shin_plate_r1 = new ModelRenderer(this);
        boots_right_leg_shin_plate_r1.setRotationPoint(0.0F, 8.5F, -1.5F);
        right_boot_armor.addChild(boots_right_leg_shin_plate_r1);
        setRotationAngle(boots_right_leg_shin_plate_r1, 0.1745F, 0.0F, 0.0F);
        boots_right_leg_shin_plate_r1.cubeList.add(new ModelBox(boots_right_leg_shin_plate_r1, 6, 82, 0.5F, -14.4102F, 1.1702F, 3, 4, 2, 0.0F, false));

        left_boot_armor = new ModelRenderer(this);
        left_boot_armor.setRotationPoint(2.0F, 12.5F, 0.0F);
        left_boot_armor.cubeList.add(new ModelBox(left_boot_armor, 79, 40, -3.505F, -3.5F, -2.005F, 4, 1, 2, 0.0F, false));
        left_boot_armor.cubeList.add(new ModelBox(left_boot_armor, 58, 37, -3.0F, -2.0F, -3.5F, 3, 1, 1, 0.0F, false));
        left_boot_armor.cubeList.add(new ModelBox(left_boot_armor, 58, 29, -4.01F, -6.5F, -2.01F, 4, 4, 4, 0.0F, false));
        left_boot_armor.cubeList.add(new ModelBox(left_boot_armor, 51, 52, -4.01F, -2.492F, -2.05F, 4, 2, 5, 0.0F, false));
        left_boot_armor.cubeList.add(new ModelBox(left_boot_armor, 44, 33, -3.01F, -1.495F, -3.0F, 4, 1, 6, 0.0F, false));

        shared_boots_leggings_left_leg_exo_frame_r1 = new ModelRenderer(this);
        shared_boots_leggings_left_leg_exo_frame_r1.setRotationPoint(0.0F, 6.0F, 3.0F);
        left_boot_armor.addChild(shared_boots_leggings_left_leg_exo_frame_r1);
        setRotationAngle(shared_boots_leggings_left_leg_exo_frame_r1, -0.0873F, 0.0F, 0.0F);
        shared_boots_leggings_left_leg_exo_frame_r1.cubeList.add(new ModelBox(shared_boots_leggings_left_leg_exo_frame_r1, 16, 83, -3.0F, -12.4524F, -2.5899F, 2, 6, 2, 0.0F, false));

        shared_boots_leggings_left_leg_exo_plate_r1 = new ModelRenderer(this);
        shared_boots_leggings_left_leg_exo_plate_r1.setRotationPoint(-2.0F, 3.5305F, 0.2618F);
        left_boot_armor.addChild(shared_boots_leggings_left_leg_exo_plate_r1);
        setRotationAngle(shared_boots_leggings_left_leg_exo_plate_r1, -0.0873F, 0.0F, 0.0F);
        shared_boots_leggings_left_leg_exo_plate_r1.cubeList.add(new ModelBox(shared_boots_leggings_left_leg_exo_plate_r1, 76, 43, 0.0F, -9.4486F, -1.0899F, 3, 5, 3, 0.0F, false));

        boots_left_leg_plate1_r1 = new ModelRenderer(this);
        boots_left_leg_plate1_r1.setRotationPoint(0.5F, 10.5F, -3.0F);
        left_boot_armor.addChild(boots_left_leg_plate1_r1);
        setRotationAngle(boots_left_leg_plate1_r1, 0.7854F, 0.0F, 0.0F);
        boots_left_leg_plate1_r1.cubeList.add(new ModelBox(boots_left_leg_plate1_r1, 34, 59, -4.005F, -9.3388F, 8.8389F, 4, 2, 1, 0.0F, false));

        boots_left_leg_shin_plate_r1 = new ModelRenderer(this);
        boots_left_leg_shin_plate_r1.setRotationPoint(0.0F, 8.5F, -1.5F);
        left_boot_armor.addChild(boots_left_leg_shin_plate_r1);
        setRotationAngle(boots_left_leg_shin_plate_r1, 0.1745F, 0.0F, 0.0F);
        boots_left_leg_shin_plate_r1.cubeList.add(new ModelBox(boots_left_leg_shin_plate_r1, 82, 26, -3.5F, -14.4102F, 1.1702F, 3, 4, 2, 0.0F, false));

        bipedLeftLeg.cubeList.clear();
        bipedLeftLeg.addChild(left_boot_armor);
        bipedRightLeg.cubeList.clear();
        bipedRightLeg.addChild(right_boot_armor);


    }


    public void render(float size) {
        right_boot_armor.render(size);
        left_boot_armor.render(size);
    }

    public void renderLeft(float size) {
        left_boot_armor.render(size);
    }

    public void renderRight(float size) {
        right_boot_armor.render(size);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
