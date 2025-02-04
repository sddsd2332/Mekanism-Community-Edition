package mekanism.client.model.mekasuitarmour;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMekAsuitBodyArm extends ModelBiped {

    public static final ModelMekAsuitBodyArm armorModel = new ModelMekAsuitBodyArm();
    public final ModelRenderer left_arm_armor;
    ModelRenderer chest_left_arm_exo_brace1_r1;
    ModelRenderer chest_left_arm_exo_brace2_r1;
    ModelRenderer chest_left_arm_exo_brace3_r1;
    public final ModelRenderer right_arm_armor;
    ModelRenderer chest_right_arm_exo_brace1_r1;
    ModelRenderer chest_right_arm_exo_brace2_r1;
    ModelRenderer chest_right_arm_exo_brace3_r1;

    public ModelMekAsuitBodyArm() {
        textureWidth = 128;
        textureHeight = 128;

        left_arm_armor = new ModelRenderer(this);
        left_arm_armor.setRotationPoint(6.0F, 2.5F, 0.0F);
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 23, 32, 1.05F, 3.0F, -2.05F, 1, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 56, 59, -0.3454F, -2.3145F, -2.5F, 3, 3, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 8, 74, 0.5F, 5.5F, -2.5F, 2, 4, 4, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 9, 65, -2.5F, 1.5F, -2.505F, 2, 4, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 67, 64, -0.5F, 1.5F, -2.75F, 1, 4, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 74, 15, 0.5F, 2.25F, -2.5F, 2, 1, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 57, 73, 0.5F, 3.75F, -2.5F, 2, 1, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 62, 3, -0.5F, 9.5F, -2.5F, 3, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 64, 67, -0.5F, 9.5F, -0.5F, 3, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 53, 73, -0.5F, 9.5F, -1.5F, 3, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 64, 55, -0.5F, 9.5F, 0.5F, 3, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 0, 35, -2.5F, 7.5F, -1.5F, 1, 3, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 16, 41, -2.5F, 7.5F, -0.5F, 1, 3, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 40, 41, -2.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F, false));

        chest_left_arm_exo_brace1_r1 = new ModelRenderer(this);
        chest_left_arm_exo_brace1_r1.setRotationPoint(1.418F, 1.2177F, 0.995F);
        left_arm_armor.addChild(chest_left_arm_exo_brace1_r1);
        setRotationAngle(chest_left_arm_exo_brace1_r1, 0.0F, 0.0F, -0.0873F);
        chest_left_arm_exo_brace1_r1.cubeList.add(new ModelBox(chest_left_arm_exo_brace1_r1, 54, 79, 0.5436F, -3.4981F, -1.5F, 1, 6, 3, 0.0F, false));

        chest_left_arm_exo_brace2_r1 = new ModelRenderer(this);
        chest_left_arm_exo_brace2_r1.setRotationPoint(0.5F, 5.1772F, 2.1713F);
        left_arm_armor.addChild(chest_left_arm_exo_brace2_r1);
        setRotationAngle(chest_left_arm_exo_brace2_r1, 0.0873F, 0.0F, 0.0F);
        chest_left_arm_exo_brace2_r1.cubeList.add(new ModelBox(chest_left_arm_exo_brace2_r1, 79, 9, -0.5F, -1.4981F, -1.4564F, 3, 2, 3, 0.0F, false));

        chest_left_arm_exo_brace3_r1 = new ModelRenderer(this);
        chest_left_arm_exo_brace3_r1.setRotationPoint(0.55F, 8.0669F, 1.8402F);
        left_arm_armor.addChild(chest_left_arm_exo_brace3_r1);
        setRotationAngle(chest_left_arm_exo_brace3_r1, -0.2618F, 0.0F, 0.0F);
        chest_left_arm_exo_brace3_r1.cubeList.add(new ModelBox(chest_left_arm_exo_brace3_r1, 24, 83, 0.0F, -2.983F, -1.1294F, 2, 5, 2, 0.0F, false));

        right_arm_armor = new ModelRenderer(this);
        right_arm_armor.setRotationPoint(-6.0F, 2.5F, 0.0F);
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 24, 3, -2.05F, 3.0F, -2.05F, 1, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 40, 57, -2.7449F, -2.308F, -2.5F, 3, 3, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 45, 73, -2.5F, 5.5F, -2.5F, 2, 4, 4, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 25, 60, 0.5F, 1.5F, -2.505F, 2, 4, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 67, 55, -0.5F, 1.5F, -2.75F, 1, 4, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 35, 71, -2.5F, 2.25F, -2.5F, 2, 1, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 70, 9, -2.5F, 3.75F, -2.5F, 2, 1, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 0, 51, -2.5F, 9.5F, -2.5F, 3, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 47, 0, -2.5F, 9.5F, -0.5F, 3, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 16, 39, -2.5F, 9.5F, -1.5F, 3, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 38, 26, -2.5F, 9.5F, 0.5F, 3, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 0, 28, 1.5F, 7.5F, -1.5F, 1, 3, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 0, 0, 1.5F, 7.5F, -0.5F, 1, 3, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 16, 41, -2.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F, false));

        chest_right_arm_exo_brace1_r1 = new ModelRenderer(this);
        chest_right_arm_exo_brace1_r1.setRotationPoint(-2.582F, 1.2177F, 0.995F);
        right_arm_armor.addChild(chest_right_arm_exo_brace1_r1);
        setRotationAngle(chest_right_arm_exo_brace1_r1, 0.0F, 0.0F, 0.0873F);
        chest_right_arm_exo_brace1_r1.cubeList.add(new ModelBox(chest_right_arm_exo_brace1_r1, 40, 78, -0.4696F, -3.4981F, -1.5F, 1, 6, 3, 0.0F, false));

        chest_right_arm_exo_brace2_r1 = new ModelRenderer(this);
        chest_right_arm_exo_brace2_r1.setRotationPoint(-1.5F, 5.1772F, 2.1713F);
        right_arm_armor.addChild(chest_right_arm_exo_brace2_r1);
        setRotationAngle(chest_right_arm_exo_brace2_r1, 0.0873F, 0.0F, 0.0F);
        chest_right_arm_exo_brace2_r1.cubeList.add(new ModelBox(chest_right_arm_exo_brace2_r1, 20, 78, -1.5F, -1.4981F, -1.4564F, 3, 2, 3, 0.0F, false));

        chest_right_arm_exo_brace3_r1 = new ModelRenderer(this);
        chest_right_arm_exo_brace3_r1.setRotationPoint(-1.45F, 8.0669F, 1.8402F);
        right_arm_armor.addChild(chest_right_arm_exo_brace3_r1);
        setRotationAngle(chest_right_arm_exo_brace3_r1, -0.2618F, 0.0F, 0.0F);
        chest_right_arm_exo_brace3_r1.cubeList.add(new ModelBox(chest_right_arm_exo_brace3_r1, 32, 45, -1.0F, -2.983F, -1.1294F, 2, 5, 2, 0.0F, false));
    }

    public void render(float size) {
        left_arm_armor.render(size);
        right_arm_armor.render(size);
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