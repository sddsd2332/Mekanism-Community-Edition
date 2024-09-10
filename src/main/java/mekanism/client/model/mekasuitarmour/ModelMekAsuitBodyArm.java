package mekanism.client.model.mekasuitarmour;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMekAsuitBodyArm extends ModelBiped {

    public static final ModelMekAsuitBodyArm armorModel = new ModelMekAsuitBodyArm();
    ModelRenderer left_arm_armor;
    ModelRenderer chest_left_arm_exo_brace1_r1;
    ModelRenderer chest_left_arm_exo_brace2_r1;
    ModelRenderer chest_left_arm_exo_brace3_r1;
    ModelRenderer right_arm_armor;
    ModelRenderer chest_right_arm_exo_brace1_r1;
    ModelRenderer chest_right_arm_exo_brace2_r1;
    ModelRenderer chest_right_arm_exo_brace3_r1;

    public ModelMekAsuitBodyArm() {
        super(0, 0, 128, 128);
        textureWidth = 128;
        textureHeight = 128;

        left_arm_armor = new ModelRenderer(this);
        left_arm_armor.setRotationPoint(0.0F, 0.0F, 0.0F);
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 23, 32, 2.0F, 9.0F, -2.0F, 1, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 56, 59, 0.6046F, 3.6855F, -2.45F, 3, 3, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 8, 74, 1.45F, 11.5F, -2.45F, 2, 4, 4, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 9, 65, -1.55F, 7.5F, -2.455F, 2, 4, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 67, 64, 0.45F, 7.5F, -2.7F, 1, 4, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 74, 15, 1.45F, 8.25F, -2.45F, 2, 1, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 57, 73, 1.45F, 9.75F, -2.45F, 2, 1, 5, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 62, 3, 0.45F, 15.5F, -2.45F, 3, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 64, 67, 0.45F, 15.5F, -0.45F, 3, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 53, 73, 0.45F, 15.5F, -1.45F, 3, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 64, 55, 0.45F, 15.5F, 0.55F, 3, 1, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 0, 35, -1.55F, 13.5F, -1.45F, 1, 3, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 16, 41, -1.55F, 13.5F, -0.45F, 1, 3, 1, 0.0F, false));
        left_arm_armor.cubeList.add(new ModelBox(left_arm_armor, 40, 41, -1.05F, 4.0F, -1.95F, 4, 12, 4, 0.0F, false));

        chest_left_arm_exo_brace1_r1 = new ModelRenderer(this);
        chest_left_arm_exo_brace1_r1.setRotationPoint(2.368F, 7.2177F, 1.045F);
        left_arm_armor.addChild(chest_left_arm_exo_brace1_r1);
        setRotationAngle(chest_left_arm_exo_brace1_r1, 0.0F, 0.0F, -0.0873F);
        chest_left_arm_exo_brace1_r1.cubeList.add(new ModelBox(chest_left_arm_exo_brace1_r1, 54, 79, 0.5436F, -3.4981F, -1.5F, 1, 6, 3, 0.0F, false));

        chest_left_arm_exo_brace2_r1 = new ModelRenderer(this);
        chest_left_arm_exo_brace2_r1.setRotationPoint(1.45F, 11.1772F, 2.2213F);
        left_arm_armor.addChild(chest_left_arm_exo_brace2_r1);
        setRotationAngle(chest_left_arm_exo_brace2_r1, 0.0873F, 0.0F, 0.0F);
        chest_left_arm_exo_brace2_r1.cubeList.add(new ModelBox(chest_left_arm_exo_brace2_r1, 79, 9, -0.5F, -1.4981F, -1.4564F, 3, 2, 3, 0.0F, false));

        chest_left_arm_exo_brace3_r1 = new ModelRenderer(this);
        chest_left_arm_exo_brace3_r1.setRotationPoint(1.5F, 14.0669F, 1.8902F);
        left_arm_armor.addChild(chest_left_arm_exo_brace3_r1);
        setRotationAngle(chest_left_arm_exo_brace3_r1, -0.2618F, 0.0F, 0.0F);
        chest_left_arm_exo_brace3_r1.cubeList.add(new ModelBox(chest_left_arm_exo_brace3_r1, 24, 83, 0.0F, -2.983F, -1.1294F, 2, 5, 2, 0.0F, false));

        right_arm_armor = new ModelRenderer(this);
        right_arm_armor.setRotationPoint(0.0F, 0.0F, 0.0F);
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 24, 3, 0.0F, 9.0F, -2.0F, 1, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 40, 57, -0.6949F, 3.692F, -2.45F, 3, 3, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 45, 73, -0.45F, 11.5F, -2.45F, 2, 4, 4, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 25, 60, 2.55F, 7.5F, -2.455F, 2, 4, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 67, 55, 1.55F, 7.5F, -2.7F, 1, 4, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 35, 71, -0.45F, 8.25F, -2.45F, 2, 1, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 70, 9, -0.45F, 9.75F, -2.45F, 2, 1, 5, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 0, 51, -0.45F, 15.5F, -2.45F, 3, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 47, 0, -0.45F, 15.5F, -0.45F, 3, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 16, 39, -0.45F, 15.5F, -1.45F, 3, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 38, 26, -0.45F, 15.5F, 0.55F, 3, 1, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 0, 28, 3.55F, 13.5F, -1.45F, 1, 3, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 0, 0, 3.55F, 13.5F, -0.45F, 1, 3, 1, 0.0F, false));
        right_arm_armor.cubeList.add(new ModelBox(right_arm_armor, 16, 41, 0.05F, 4.0F, -1.95F, 4, 12, 4, 0.0F, false));

        chest_right_arm_exo_brace1_r1 = new ModelRenderer(this);
        chest_right_arm_exo_brace1_r1.setRotationPoint(-0.532F, 7.2177F, 1.045F);
        right_arm_armor.addChild(chest_right_arm_exo_brace1_r1);
        setRotationAngle(chest_right_arm_exo_brace1_r1, 0.0F, 0.0F, 0.0873F);
        chest_right_arm_exo_brace1_r1.cubeList.add(new ModelBox(chest_right_arm_exo_brace1_r1, 40, 78, -0.4696F, -3.4981F, -1.5F, 1, 6, 3, 0.0F, false));

        chest_right_arm_exo_brace2_r1 = new ModelRenderer(this);
        chest_right_arm_exo_brace2_r1.setRotationPoint(0.55F, 11.1772F, 2.2213F);
        right_arm_armor.addChild(chest_right_arm_exo_brace2_r1);
        setRotationAngle(chest_right_arm_exo_brace2_r1, 0.0873F, 0.0F, 0.0F);
        chest_right_arm_exo_brace2_r1.cubeList.add(new ModelBox(chest_right_arm_exo_brace2_r1, 20, 78, -1.5F, -1.4981F, -1.4564F, 3, 2, 3, 0.0F, false));

        chest_right_arm_exo_brace3_r1 = new ModelRenderer(this);
        chest_right_arm_exo_brace3_r1.setRotationPoint(0.6F, 14.0669F, 1.8902F);
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