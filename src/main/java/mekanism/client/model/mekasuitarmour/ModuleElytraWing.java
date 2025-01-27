package mekanism.client.model.mekasuitarmour;


import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModuleElytraWing extends ModelBase {
    ModelRenderer elytra_left_wing;
    ModelRenderer elytra_left_wing_energy_mesh_led3_r1;
    ModelRenderer elytra_left_wing_energy_mesh_led2_r1;
    ModelRenderer elytra_left_wing_energy_mesh_led1_r1;
    ModelRenderer elytra_left_wing_energy_cover6_r1;
    ModelRenderer elytra_left_wing_energy_cover5_r1;
    ModelRenderer elytra_right_wing;
    ModelRenderer elytra_right_wing_energy_mesh_led3_r1;
    ModelRenderer elytra_right_wing_energy_mesh_led2_r1;
    ModelRenderer elytra_right_wing_energy_mesh_led1_r1;
    ModelRenderer elytra_right_wing_energy_cover6_r1;
    ModelRenderer elytra_right_wing_energy_cover5_r1;

    public ModuleElytraWing() {
        textureWidth = 128;
        textureHeight = 128;

        elytra_right_wing = new ModelRenderer(this);
        elytra_right_wing.setRotationPoint(7.25F, -0.25F, 2.0F);
        setRotationAngle(elytra_right_wing, -0.7854F, 0.3316F, -0.0175F);
        elytra_right_wing.cubeList.add(new ModelBox(elytra_right_wing, 0, 86, -1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F, false));
        elytra_right_wing.cubeList.add(new ModelBox(elytra_right_wing, 0, 90, -0.5F, -6.0F, -0.75F, 1, 5, 2, 0.0F, false));
        elytra_right_wing.cubeList.add(new ModelBox(elytra_right_wing, 23, 90, -1.0F, -6.0F, 0.0F, 2, 5, 1, 0.0F, false));
        elytra_right_wing.cubeList.add(new ModelBox(elytra_right_wing, 11, 92, -1.0F, -3.0F, -1.0F, 2, 2, 1, 0.0F, false));

        elytra_right_wing_energy_mesh_led3_r1 = new ModelRenderer(this);
        elytra_right_wing_energy_mesh_led3_r1.setRotationPoint(0.0F, -4.1069F, 0.7069F);
        elytra_right_wing.addChild(elytra_right_wing_energy_mesh_led3_r1);
        setRotationAngle(elytra_right_wing_energy_mesh_led3_r1, -1.5708F, 0.0F, 0.0F);
        elytra_right_wing_energy_mesh_led3_r1.cubeList.add(new ModelBox(elytra_right_wing_energy_mesh_led3_r1, 27, 97, 0.0F, -10.0F, -1.0F, 0, 10, 2, 0.0F, false));

        elytra_right_wing_energy_mesh_led2_r1 = new ModelRenderer(this);
        elytra_right_wing_energy_mesh_led2_r1.setRotationPoint(0.0F, -6.5069F, 1.2069F);
        elytra_right_wing.addChild(elytra_right_wing_energy_mesh_led2_r1);
        setRotationAngle(elytra_right_wing_energy_mesh_led2_r1, -1.5708F, 0.0F, 0.0F);
        elytra_right_wing_energy_mesh_led2_r1.cubeList.add(new ModelBox(elytra_right_wing_energy_mesh_led2_r1, 31, 97, 0.0F, -14.0F, -1.0F, 0, 14, 2, 0.0F, false));

        elytra_right_wing_energy_mesh_led1_r1 = new ModelRenderer(this);
        elytra_right_wing_energy_mesh_led1_r1.setRotationPoint(0.0F, -9.4069F, 4.7569F);
        elytra_right_wing.addChild(elytra_right_wing_energy_mesh_led1_r1);
        setRotationAngle(elytra_right_wing_energy_mesh_led1_r1, -1.5708F, 0.0F, 0.0F);
        elytra_right_wing_energy_mesh_led1_r1.cubeList.add(new ModelBox(elytra_right_wing_energy_mesh_led1_r1, 36, 95, 0.0F, -14.0F, -1.5F, 0, 16, 3, 0.0F, false));

        elytra_right_wing_energy_cover6_r1 = new ModelRenderer(this);
        elytra_right_wing_energy_cover6_r1.setRotationPoint(0.0F, -12.6569F, 5.1569F);
        elytra_right_wing.addChild(elytra_right_wing_energy_cover6_r1);
        setRotationAngle(elytra_right_wing_energy_cover6_r1, -1.5708F, 0.0F, 0.0F);
        elytra_right_wing_energy_cover6_r1.cubeList.add(new ModelBox(elytra_right_wing_energy_cover6_r1, 12, 92, -0.5F, -6.0F, 0.0F, 1, 2, 1, 0.0F, false));
        elytra_right_wing_energy_cover6_r1.cubeList.add(new ModelBox(elytra_right_wing_energy_cover6_r1, 6, 97, -1.0F, -7.0F, 0.5F, 2, 6, 1, 0.0F, false));
        elytra_right_wing_energy_cover6_r1.cubeList.add(new ModelBox(elytra_right_wing_energy_cover6_r1, 8, 88, -0.5F, -4.0F, 0.0F, 1, 4, 1, 0.0F, false));

        elytra_right_wing_energy_cover5_r1 = new ModelRenderer(this);
        elytra_right_wing_energy_cover5_r1.setRotationPoint(0.0F, -7.0F, -0.5F);
        elytra_right_wing.addChild(elytra_right_wing_energy_cover5_r1);
        setRotationAngle(elytra_right_wing_energy_cover5_r1, -0.7854F, 0.0F, 0.0F);
        elytra_right_wing_energy_cover5_r1.cubeList.add(new ModelBox(elytra_right_wing_energy_cover5_r1, 11, 92, -1.0F, -9.0F, 0.0F, 2, 2, 1, 0.0F, false));
        elytra_right_wing_energy_cover5_r1.cubeList.add(new ModelBox(elytra_right_wing_energy_cover5_r1, 11, 92, -1.0F, -6.0F, 0.0F, 2, 1, 1, 0.0F, false));
        elytra_right_wing_energy_cover5_r1.cubeList.add(new ModelBox(elytra_right_wing_energy_cover5_r1, 11, 92, -1.0F, -3.0F, 0.0F, 2, 1, 1, 0.0F, false));
        elytra_right_wing_energy_cover5_r1.cubeList.add(new ModelBox(elytra_right_wing_energy_cover5_r1, 11, 92, -1.0F, -1.0F, 0.0F, 2, 2, 1, 0.0F, false));
        elytra_right_wing_energy_cover5_r1.cubeList.add(new ModelBox(elytra_right_wing_energy_cover5_r1, 0, 97, -1.0F, -9.0F, 1.0F, 2, 10, 1, 0.0F, false));
        elytra_right_wing_energy_cover5_r1.cubeList.add(new ModelBox(elytra_right_wing_energy_cover5_r1, 32, 86, -0.5F, -8.0F, 0.25F, 1, 8, 2, 0.0F, false));

        elytra_left_wing = new ModelRenderer(this);
        elytra_left_wing.setRotationPoint(-7.25F, -0.25F, 2.0F);
        setRotationAngle(elytra_left_wing, -0.7854F, -0.3316F, 0.0175F);
        elytra_left_wing.cubeList.add(new ModelBox(elytra_left_wing, 0, 86, -1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F, false));
        elytra_left_wing.cubeList.add(new ModelBox(elytra_left_wing, 0, 90, -0.5F, -6.0F, -0.75F, 1, 5, 2, 0.0F, false));
        elytra_left_wing.cubeList.add(new ModelBox(elytra_left_wing, 23, 90, -1.0F, -6.0F, 0.0F, 2, 5, 1, 0.0F, false));
        elytra_left_wing.cubeList.add(new ModelBox(elytra_left_wing, 11, 92, -1.0F, -3.0F, -1.0F, 2, 2, 1, 0.0F, false));

        elytra_left_wing_energy_mesh_led3_r1 = new ModelRenderer(this);
        elytra_left_wing_energy_mesh_led3_r1.setRotationPoint(0.0F, -4.1069F, 0.7069F);
        elytra_left_wing.addChild(elytra_left_wing_energy_mesh_led3_r1);
        setRotationAngle(elytra_left_wing_energy_mesh_led3_r1, -1.5708F, 0.0F, 0.0F);
        elytra_left_wing_energy_mesh_led3_r1.cubeList.add(new ModelBox(elytra_left_wing_energy_mesh_led3_r1, 27, 97, 0.0F, -10.0F, -1.0F, 0, 10, 2, 0.0F, false));

        elytra_left_wing_energy_mesh_led2_r1 = new ModelRenderer(this);
        elytra_left_wing_energy_mesh_led2_r1.setRotationPoint(0.0F, -6.5069F, 1.2069F);
        elytra_left_wing.addChild(elytra_left_wing_energy_mesh_led2_r1);
        setRotationAngle(elytra_left_wing_energy_mesh_led2_r1, -1.5708F, 0.0F, 0.0F);
        elytra_left_wing_energy_mesh_led2_r1.cubeList.add(new ModelBox(elytra_left_wing_energy_mesh_led2_r1, 31, 97, 0.0F, -14.0F, -1.0F, 0, 14, 2, 0.0F, false));

        elytra_left_wing_energy_mesh_led1_r1 = new ModelRenderer(this);
        elytra_left_wing_energy_mesh_led1_r1.setRotationPoint(0.0F, -9.4069F, 4.7569F);
        elytra_left_wing.addChild(elytra_left_wing_energy_mesh_led1_r1);
        setRotationAngle(elytra_left_wing_energy_mesh_led1_r1, -1.5708F, 0.0F, 0.0F);
        elytra_left_wing_energy_mesh_led1_r1.cubeList.add(new ModelBox(elytra_left_wing_energy_mesh_led1_r1, 36, 95, 0.0F, -13.0F, -1.5F, 0, 16, 3, 0.0F, false));

        elytra_left_wing_energy_cover6_r1 = new ModelRenderer(this);
        elytra_left_wing_energy_cover6_r1.setRotationPoint(0.0F, -12.6569F, 5.1569F);
        elytra_left_wing.addChild(elytra_left_wing_energy_cover6_r1);
        setRotationAngle(elytra_left_wing_energy_cover6_r1, -1.5708F, 0.0F, 0.0F);
        elytra_left_wing_energy_cover6_r1.cubeList.add(new ModelBox(elytra_left_wing_energy_cover6_r1, 12, 92, -0.5F, -6.0F, 0.0F, 1, 2, 1, 0.0F, false));
        elytra_left_wing_energy_cover6_r1.cubeList.add(new ModelBox(elytra_left_wing_energy_cover6_r1, 6, 97, -1.0F, -7.0F, 0.5F, 2, 6, 1, 0.0F, false));
        elytra_left_wing_energy_cover6_r1.cubeList.add(new ModelBox(elytra_left_wing_energy_cover6_r1, 8, 88, -0.5F, -4.0F, 0.0F, 1, 4, 1, 0.0F, false));

        elytra_left_wing_energy_cover5_r1 = new ModelRenderer(this);
        elytra_left_wing_energy_cover5_r1.setRotationPoint(0.0F, -7.0F, -0.5F);
        elytra_left_wing.addChild(elytra_left_wing_energy_cover5_r1);
        setRotationAngle(elytra_left_wing_energy_cover5_r1, -0.7854F, 0.0F, 0.0F);
        elytra_left_wing_energy_cover5_r1.cubeList.add(new ModelBox(elytra_left_wing_energy_cover5_r1, 11, 92, -1.0F, -9.0F, 0.0F, 2, 2, 1, 0.0F, false));
        elytra_left_wing_energy_cover5_r1.cubeList.add(new ModelBox(elytra_left_wing_energy_cover5_r1, 11, 92, -1.0F, -6.0F, 0.0F, 2, 1, 1, 0.0F, false));
        elytra_left_wing_energy_cover5_r1.cubeList.add(new ModelBox(elytra_left_wing_energy_cover5_r1, 11, 92, -1.0F, -3.0F, 0.0F, 2, 1, 1, 0.0F, false));
        elytra_left_wing_energy_cover5_r1.cubeList.add(new ModelBox(elytra_left_wing_energy_cover5_r1, 11, 92, -1.0F, -1.0F, 0.0F, 2, 2, 1, 0.0F, false));
        elytra_left_wing_energy_cover5_r1.cubeList.add(new ModelBox(elytra_left_wing_energy_cover5_r1, 0, 97, -1.0F, -9.0F, 1.0F, 2, 10, 1, 0.0F, false));
        elytra_left_wing_energy_cover5_r1.cubeList.add(new ModelBox(elytra_left_wing_energy_cover5_r1, 32, 86, -0.5F, -8.0F, 0.25F, 1, 8, 2, 0.0F, false));
    }


    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableCull();

        if (entityIn instanceof EntityLivingBase base && base.isChild()) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 1.5F, -0.1F);
            elytra_left_wing.render(scale);
            elytra_right_wing.render(scale);
            GlStateManager.popMatrix();
        } else {
            elytra_left_wing.render(scale);
            elytra_right_wing.render(scale);
        }
    }


    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

        float f = 0.2617994F;
        float f1 = -0.2617994F;
        float f3 = 0.0F;

        if (entity instanceof EntityLivingBase livingBase && livingBase.isElytraFlying()) {
            float f4 = 1.0F;

            if (livingBase.motionY < 0.0D) {
                Vec3d vec3d = (new Vec3d(livingBase.motionX, livingBase.motionY, livingBase.motionZ)).normalize();
                f4 = 1.0F - (float) Math.pow(-vec3d.y, 1.5D);
            }

            f = f4 * 0.34906584F + (1.0F - f4) * f;
            f1 = f4 * -((float) Math.PI / 2F) + (1.0F - f4) * f1;
        }

        if (entity instanceof AbstractClientPlayer player) {
            player.rotateElytraX = (float) ((double) player.rotateElytraX + (double) (f - player.rotateElytraX) * 0.1D);
            player.rotateElytraY = (float) ((double) player.rotateElytraY + (double) (f3 - player.rotateElytraY) * 0.1D);
            player.rotateElytraZ = (float) ((double) player.rotateElytraZ + (double) (f1 - player.rotateElytraZ) * 0.1D);
            elytra_left_wing.rotateAngleX = player.rotateElytraX;
            elytra_left_wing.rotateAngleY = player.rotateElytraY;
            elytra_left_wing.rotateAngleZ = player.rotateElytraZ;
        } else {
            elytra_left_wing.rotateAngleX = f;
            elytra_left_wing.rotateAngleZ = f1;
            elytra_left_wing.rotateAngleY = f3;
        }

        elytra_right_wing.rotateAngleY = -elytra_left_wing.rotateAngleY;
        elytra_right_wing.rotateAngleX = elytra_left_wing.rotateAngleX;
        elytra_right_wing.rotateAngleZ = -elytra_left_wing.rotateAngleZ;

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}