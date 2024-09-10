package mekanism.client.model.mekasuitarmour;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMekAsuitBody extends ModelBiped {

    public static final ModelMekAsuitBody armorModel = new ModelMekAsuitBody();

    ModelRenderer legs_chest_exo;
    ModelRenderer left_arm_armor;
    ModelRenderer chest_left_arm_exo_brace1_r1;
    ModelRenderer chest_left_arm_exo_brace2_r1;
    ModelRenderer chest_left_arm_exo_brace3_r1;
    ModelRenderer right_arm_armor;
    ModelRenderer chest_right_arm_exo_brace1_r1;
    ModelRenderer chest_right_arm_exo_brace2_r1;
    ModelRenderer chest_right_arm_exo_brace3_r1;
    ModelRenderer chest_armor;
    ModelRenderer chest_body_plate5_r1;
    ModelRenderer chest_body_plate3_r1;
    ModelRenderer chest_body_plate2_r1;
    ModelRenderer chest_body_plate1_r1;
    ModelRenderer chest_body_led2_r1;
    ModelRenderer chest_body_brace2_r1;
    ModelRenderer chest_body_brace1_r1;
    ModelRenderer chest_utilities;
    ModelRenderer chest_body_lower_plate2_r1;
    ModelRenderer chest_body_lower_plate1_r1;
    ModelRenderer chest_body_satchel3_r1;
    ModelRenderer chest_body_satchel2_r1;
    ModelRenderer chest_body_satchel1_r1;
    ModelRenderer back_exo;
    ModelRenderer chest_body_back_exo_brace_mid_r1;
    ModelRenderer shoulder_exo;
    ModelRenderer chest_body_shoulder_exo_brace_right_r1;
    ModelRenderer chest_body_shoulder_exo_brace_left_r1;

    public ModelMekAsuitBody() {
        // super(0, 0, 128, 128);
        textureWidth = 128;
        textureHeight = 128;

        legs_chest_exo = new ModelRenderer(this);
        legs_chest_exo.setRotationPoint(0.0F, 11.5228F, 2.5229F);
        legs_chest_exo.cubeList.add(new ModelBox(legs_chest_exo, 24, 6, -5.0F, -2.0228F, -2.0191F, 10, 2, 2, 0.0F, false));
        legs_chest_exo.cubeList.add(new ModelBox(legs_chest_exo, 80, 51, -2.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F, false));

        left_arm_armor = new ModelRenderer(this);
        left_arm_armor.setRotationPoint(1.0F, 0.5F, 0.0F);
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 23, 32, 1.05F, 2.5F, -2.05F, 1, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 56, 59, -0.3454F, -2.8145F, -2.5F, 3, 3, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 8, 74, 0.5F, 5.0F, -2.5F, 2, 4, 4, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 9, 65, -2.5F, 1.0F, -2.505F, 2, 4, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 67, 64, -0.5F, 1.0F, -2.75F, 1, 4, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 74, 15, 0.5F, 1.75F, -2.5F, 2, 1, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 57, 73, 0.5F, 3.25F, -2.5F, 2, 1, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 62, 3, -0.5F, 9.0F, -2.5F, 3, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 64, 67, -0.5F, 9.0F, -0.5F, 3, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 53, 73, -0.5F, 9.0F, -1.5F, 3, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 64, 55, -0.5F, 9.0F, 0.5F, 3, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 0, 35, -2.5F, 7.0F, -1.5F, 1, 3, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 16, 41, -2.5F, 7.0F, -0.5F, 1, 3, 1, 0.0F, false));

        chest_left_arm_exo_brace1_r1 = new ModelRenderer(this);
        chest_left_arm_exo_brace1_r1.setRotationPoint(2.418F, 0.7177F, 0.995F);
        left_arm_armor.addChild(chest_left_arm_exo_brace1_r1);
        setRotationAngle(chest_left_arm_exo_brace1_r1, 0.0F, 0.0F, -0.0873F);
        chest_left_arm_exo_brace1_r1.cubeList.add(new ModelBox(chest_left_arm_exo_brace1_r1, 54, 79, -0.4564F, -3.4981F, -1.5F, 1, 6, 3, 0.0F, false));

        chest_left_arm_exo_brace2_r1 = new ModelRenderer(this);
        chest_left_arm_exo_brace2_r1.setRotationPoint(1.5F, 4.6772F, 2.1713F);
        left_arm_armor.addChild(chest_left_arm_exo_brace2_r1);
        setRotationAngle(chest_left_arm_exo_brace2_r1, 0.0873F, 0.0F, 0.0F);
        chest_left_arm_exo_brace2_r1.cubeList.add(new ModelBox(chest_left_arm_exo_brace2_r1, 79, 9, -1.5F, -1.4981F, -1.4564F, 3, 2, 3, 0.0F, false));

        chest_left_arm_exo_brace3_r1 = new ModelRenderer(this);
        chest_left_arm_exo_brace3_r1.setRotationPoint(1.55F, 7.5669F, 1.8402F);
        left_arm_armor.addChild(chest_left_arm_exo_brace3_r1);
        setRotationAngle(chest_left_arm_exo_brace3_r1, -0.2618F, 0.0F, 0.0F);
        chest_left_arm_exo_brace3_r1.cubeList.add(new ModelBox(chest_left_arm_exo_brace3_r1, 24, 83, -1.0F, -2.983F, -1.1294F, 2, 5, 2, 0.0F, false));

        right_arm_armor = new ModelRenderer(this);
        right_arm_armor.setRotationPoint(-1.0F, 0.5F, 0.0F);
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 24, 3, -2.05F, 2.5F, -2.05F, 1, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 40, 57, -2.7449F, -2.808F, -2.5F, 3, 3, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 45, 73, -2.5F, 5.0F, -2.5F, 2, 4, 4, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 25, 60, 0.5F, 1.0F, -2.505F, 2, 4, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 67, 55, -0.5F, 1.0F, -2.75F, 1, 4, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 35, 71, -2.5F, 1.75F, -2.5F, 2, 1, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 70, 9, -2.5F, 3.25F, -2.5F, 2, 1, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 0, 51, -2.5F, 9.0F, -2.5F, 3, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 47, 0, -2.5F, 9.0F, -0.5F, 3, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 16, 39, -2.5F, 9.0F, -1.5F, 3, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 38, 26, -2.5F, 9.0F, 0.5F, 3, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 0, 28, 1.5F, 7.0F, -1.5F, 1, 3, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 0, 0, 1.5F, 7.0F, -0.5F, 1, 3, 1, 0.0F, false));

        chest_right_arm_exo_brace1_r1 = new ModelRenderer(this);
        chest_right_arm_exo_brace1_r1.setRotationPoint(-2.582F, 0.7177F, 0.995F);
        right_arm_armor.addChild(chest_right_arm_exo_brace1_r1);
        setRotationAngle(chest_right_arm_exo_brace1_r1, 0.0F, 0.0F, 0.0873F);
        chest_right_arm_exo_brace1_r1.cubeList.add(new ModelBox(chest_right_arm_exo_brace1_r1, 40, 78, -0.4696F, -3.4981F, -1.5F, 1, 6, 3, 0.0F, false));

        chest_right_arm_exo_brace2_r1 = new ModelRenderer(this);
        chest_right_arm_exo_brace2_r1.setRotationPoint(-1.5F, 4.6772F, 2.1713F);
        right_arm_armor.addChild(chest_right_arm_exo_brace2_r1);
        setRotationAngle(chest_right_arm_exo_brace2_r1, 0.0873F, 0.0F, 0.0F);
        chest_right_arm_exo_brace2_r1.cubeList.add(new ModelBox(chest_right_arm_exo_brace2_r1, 20, 78, -1.5F, -1.4981F, -1.4564F, 3, 2, 3, 0.0F, false));

        chest_right_arm_exo_brace3_r1 = new ModelRenderer(this);
        chest_right_arm_exo_brace3_r1.setRotationPoint(-1.45F, 7.5669F, 1.8402F);
        right_arm_armor.addChild(chest_right_arm_exo_brace3_r1);
        setRotationAngle(chest_right_arm_exo_brace3_r1, -0.2618F, 0.0F, 0.0F);
        chest_right_arm_exo_brace3_r1.cubeList.add(new ModelBox(chest_right_arm_exo_brace3_r1, 32, 45, -1.0F, -2.983F, -1.1294F, 2, 5, 2, 0.0F, false));

        chest_armor = new ModelRenderer(this);
        chest_armor.setRotationPoint(-2.0F, 5.0F, -2.0F);
        chest_armor.cubeList.add(new ModelBox(chest_armor, 64, 49, 0.0F, -6.05F, 0.0F, 4, 2, 4, 0.0F, false));
        chest_armor.cubeList.add(new ModelBox(chest_armor, 19, 18, -2.5F, -2.9F, 3.25F, 9, 2, 1, 0.0F, false));
        chest_armor.cubeList.add(new ModelBox(chest_armor, 19, 11, -2.5F, 2.0F, -0.5F, 9, 2, 5, 0.0F, false));
        chest_armor.cubeList.add(new ModelBox(chest_armor, 0, 0, -2.0F, -5.0F, -0.005F, 8, 12, 4, 0.0F, false));

        chest_body_plate5_r1 = new ModelRenderer(this);
        chest_body_plate5_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        chest_armor.addChild(chest_body_plate5_r1);
        setRotationAngle(chest_body_plate5_r1, 0.1745F, 0.0F, 0.0F);
        chest_body_plate5_r1.cubeList.add(new ModelBox(chest_body_plate5_r1, 18, 65, 2.15F, -0.9924F, -1.4132F, 2, 3, 1, 0.0F, false));
        chest_body_plate5_r1.cubeList.add(new ModelBox(chest_body_plate5_r1, 45, 65, -0.15F, -0.9924F, -1.4132F, 2, 3, 1, 0.0F, false));

        chest_body_plate3_r1 = new ModelRenderer(this);
        chest_body_plate3_r1.setRotationPoint(2.0F, -1.3777F, -0.3473F);
        chest_armor.addChild(chest_body_plate3_r1);
        setRotationAngle(chest_body_plate3_r1, 0.1745F, 0.0F, 0.0F);
        chest_body_plate3_r1.cubeList.add(new ModelBox(chest_body_plate3_r1, 70, 6, -3.5F, -0.4924F, -0.9132F, 7, 2, 1, 0.0F, false));

        chest_body_plate2_r1 = new ModelRenderer(this);
        chest_body_plate2_r1.setRotationPoint(2.0F, -3.0F, 0.0F);
        chest_armor.addChild(chest_body_plate2_r1);
        setRotationAngle(chest_body_plate2_r1, -0.1745F, 0.0F, 0.0F);
        chest_body_plate2_r1.cubeList.add(new ModelBox(chest_body_plate2_r1, 12, 35, -3.5F, -1.4924F, -1.0868F, 7, 3, 1, 0.0F, false));

        chest_body_plate1_r1 = new ModelRenderer(this);
        chest_body_plate1_r1.setRotationPoint(1.0F, -2.0F, -1.0F);
        chest_armor.addChild(chest_body_plate1_r1);
        setRotationAngle(chest_body_plate1_r1, 0.0873F, 0.0F, 0.0F);
        chest_body_plate1_r1.cubeList.add(new ModelBox(chest_body_plate1_r1, 74, 55, -1.5F, -1.6981F, -0.9564F, 5, 3, 2, 0.0F, false));

        chest_body_led2_r1 = new ModelRenderer(this);
        chest_body_led2_r1.setRotationPoint(1.0F, -1.6F, -1.6F);
        chest_armor.addChild(chest_body_led2_r1);
        setRotationAngle(chest_body_led2_r1, -0.0873F, 0.0F, 0.0F);
        chest_body_led2_r1.cubeList.add(new ModelBox(chest_body_led2_r1, 56, 44, -0.5F, -0.9981F, -0.5136F, 1, 1, 1, 0.0F, false));
        chest_body_led2_r1.cubeList.add(new ModelBox(chest_body_led2_r1, 26, 59, 1.5F, -0.9981F, -0.5136F, 1, 1, 1, 0.0F, false));

        chest_body_brace2_r1 = new ModelRenderer(this);
        chest_body_brace2_r1.setRotationPoint(3.0F, -3.7452F, 0.3947F);
        chest_armor.addChild(chest_body_brace2_r1);
        setRotationAngle(chest_body_brace2_r1, 0.2618F, 0.0F, 0.0F);
        chest_body_brace2_r1.cubeList.add(new ModelBox(chest_body_brace2_r1, 24, 0, -5.5F, 2.6122F, -2.3706F, 9, 1, 5, 0.0F, false));

        chest_body_brace1_r1 = new ModelRenderer(this);
        chest_body_brace1_r1.setRotationPoint(3.0F, -5.694F, 1.9982F);
        chest_armor.addChild(chest_body_brace1_r1);
        setRotationAngle(chest_body_brace1_r1, -0.1745F, 0.0F, 0.0F);
        chest_body_brace1_r1.cubeList.add(new ModelBox(chest_body_brace1_r1, 0, 29, -5.5F, 2.5016F, -2.685F, 9, 1, 5, 0.0F, false));

        chest_utilities = new ModelRenderer(this);
        chest_utilities.setRotationPoint(2.0F, 5.0F, 0.0F);
        chest_armor.addChild(chest_utilities);


        chest_body_lower_plate2_r1 = new ModelRenderer(this);
        chest_body_lower_plate2_r1.setRotationPoint(-1.0F, -1.0F, 0.0F);
        chest_utilities.addChild(chest_body_lower_plate2_r1);
        setRotationAngle(chest_body_lower_plate2_r1, -0.1745F, 0.0F, 0.0F);
        chest_body_lower_plate2_r1.cubeList.add(new ModelBox(chest_body_lower_plate2_r1, 79, 60, -1.5F, -1.4924F, -1.0868F, 5, 3, 1, 0.0F, false));

        chest_body_lower_plate1_r1 = new ModelRenderer(this);
        chest_body_lower_plate1_r1.setRotationPoint(-2.0F, -3.0F, -2.0F);
        chest_utilities.addChild(chest_body_lower_plate1_r1);
        setRotationAngle(chest_body_lower_plate1_r1, 0.1745F, 0.0F, 0.0F);
        chest_body_lower_plate1_r1.cubeList.add(new ModelBox(chest_body_lower_plate1_r1, 40, 29, 1.0F, 3.5076F, 0.5868F, 2, 2, 1, 0.0F, false));

        chest_body_satchel3_r1 = new ModelRenderer(this);
        chest_body_satchel3_r1.setRotationPoint(-1.9414F, -1.5891F, -0.8755F);
        chest_utilities.addChild(chest_body_satchel3_r1);
        setRotationAngle(chest_body_satchel3_r1, 0.0873F, 0.0873F, 0.0F);
        chest_body_satchel3_r1.cubeList.add(new ModelBox(chest_body_satchel3_r1, 0, 16, -1.5586F, -1.9981F, -0.4564F, 2, 3, 1, 0.0F, false));

        chest_body_satchel2_r1 = new ModelRenderer(this);
        chest_body_satchel2_r1.setRotationPoint(0.5377F, -2.0F, -2.042F);
        chest_utilities.addChild(chest_body_satchel2_r1);
        setRotationAngle(chest_body_satchel2_r1, 0.0873F, -0.0873F, 0.0F);
        chest_body_satchel2_r1.cubeList.add(new ModelBox(chest_body_satchel2_r1, 44, 33, 0.9623F, -1.4981F, 0.5436F, 2, 3, 1, 0.0F, false));

        chest_body_satchel1_r1 = new ModelRenderer(this);
        chest_body_satchel1_r1.setRotationPoint(-2.0F, -2.0F, -2.0F);
        chest_utilities.addChild(chest_body_satchel1_r1);
        setRotationAngle(chest_body_satchel1_r1, 0.0873F, 0.0F, 0.0F);
        chest_body_satchel1_r1.cubeList.add(new ModelBox(chest_body_satchel1_r1, 16, 74, 1.0F, -1.4981F, 0.5436F, 2, 3, 1, 0.0F, false));

        back_exo = new ModelRenderer(this);
        back_exo.setRotationPoint(2.0F, 0.8299F, 4.683F);
        chest_armor.addChild(back_exo);
        back_exo.cubeList.add(new ModelBox(back_exo, 0, 70, -1.0F, -6.8299F, -1.683F, 2, 11, 2, 0.0F, false));
        back_exo.cubeList.add(new ModelBox(back_exo, 38, 18, -3.5F, -5.8249F, -1.1792F, 7, 5, 3, 0.0F, false));
        back_exo.cubeList.add(new ModelBox(back_exo, 23, 69, -2.0F, -9.8299F, 0.817F, 4, 7, 2, 0.0F, false));

        chest_body_back_exo_brace_mid_r1 = new ModelRenderer(this);
        chest_body_back_exo_brace_mid_r1.setRotationPoint(0.0F, -0.8782F, -0.1155F);
        back_exo.addChild(chest_body_back_exo_brace_mid_r1);
        setRotationAngle(chest_body_back_exo_brace_mid_r1, -0.0873F, 0.0F, 0.0F);
        chest_body_back_exo_brace_mid_r1.cubeList.add(new ModelBox(chest_body_back_exo_brace_mid_r1, 65, 37, -2.5F, -3.9981F, -1.0436F, 5, 7, 2, 0.0F, false));

        shoulder_exo = new ModelRenderer(this);
        shoulder_exo.setRotationPoint(2.0F, -4.412F, 4.699F);
        chest_armor.addChild(shoulder_exo);


        chest_body_shoulder_exo_brace_right_r1 = new ModelRenderer(this);
        chest_body_shoulder_exo_brace_right_r1.setRotationPoint(-4.076F, 0.0F, 0.0F);
        shoulder_exo.addChild(chest_body_shoulder_exo_brace_right_r1);
        setRotationAngle(chest_body_shoulder_exo_brace_right_r1, 0.0F, -0.2618F, 0.0873F);
        chest_body_shoulder_exo_brace_right_r1.cubeList.add(new ModelBox(chest_body_shoulder_exo_brace_right_r1, 62, 0, -4.0421F, -1.4981F, -0.4887F, 8, 2, 1, 0.0F, false));

        chest_body_shoulder_exo_brace_left_r1 = new ModelRenderer(this);
        chest_body_shoulder_exo_brace_left_r1.setRotationPoint(4.0541F, 0.0F, 0.0F);
        shoulder_exo.addChild(chest_body_shoulder_exo_brace_left_r1);
        setRotationAngle(chest_body_shoulder_exo_brace_left_r1, 0.0F, 0.2618F, -0.0873F);
        chest_body_shoulder_exo_brace_left_r1.cubeList.add(new ModelBox(chest_body_shoulder_exo_brace_left_r1, 70, 3, -3.9579F, -1.4981F, -0.4887F, 8, 2, 1, 0.0F, false));


        bipedBody.cubeList.clear();
        bipedBody.addChild(chest_armor);
        bipedBody.addChild(legs_chest_exo);

        bipedLeftArm.cubeList.clear();
        bipedLeftArm.addChild(left_arm_armor);

        bipedRightArm.cubeList.clear();
        bipedRightArm.addChild(right_arm_armor);

    }

    public void render(float size) {
        chest_armor.render(size);
        left_arm_armor.render(size);
        right_arm_armor.render(size);
        legs_chest_exo.render(size);
    }

    public void leftArmRender(float size) {
        left_arm_armor.rotateAngleX = 0.0F;
        left_arm_armor.render(size);
    }

    public void rightArmRender(float size) {
        right_arm_armor.rotateAngleX = 0.0F;
        right_arm_armor.render(size);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }


}
