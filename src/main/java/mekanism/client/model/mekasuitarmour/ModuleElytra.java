package mekanism.client.model.mekasuitarmour;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModuleElytra extends ModelBase {
	public static final ModuleElytra Elytra = new ModuleElytra();

	public final ModelRenderer elytra;
	public final ModelRenderer inactive_elytra_right_wing;
	 ModelRenderer inactive_elytra_right_arm_right_wing_energy_cover6_r1;
	ModelRenderer inactive_elytra_right_arm_right_wing_energy_cover5_r1;
	public final ModelRenderer inactive_elytra_left_wing;
	ModelRenderer inactive_elytra_left_arm_left_wing_energy_cover6_r1;
	ModelRenderer inactive_elytra_left_arm_left_wing_energy_cover5_r1;

	public ModuleElytra() {
		textureWidth = 128;
		textureHeight = 128;

		elytra = new ModelRenderer(this);
		elytra.setRotationPoint(-4.076F, 0.588F, 2.699F);
		

		inactive_elytra_right_wing = new ModelRenderer(this);
		inactive_elytra_right_wing.setRotationPoint(-3.174F, -0.838F, -0.699F);
		elytra.addChild(inactive_elytra_right_wing);
		setRotationAngle(inactive_elytra_right_wing, -2.3562F, -0.3316F, 0.0175F);
		inactive_elytra_right_wing.cubeList.add(new ModelBox(inactive_elytra_right_wing, 0, 86, -1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F, false));
		inactive_elytra_right_wing.cubeList.add(new ModelBox(inactive_elytra_right_wing, 32, 86, -0.5F, -6.0F, -0.75F, 1, 5, 2, 0.0F, false));
		inactive_elytra_right_wing.cubeList.add(new ModelBox(inactive_elytra_right_wing, 23, 90, -1.0F, -6.0F, 0.0F, 2, 5, 1, 0.0F, false));
		inactive_elytra_right_wing.cubeList.add(new ModelBox(inactive_elytra_right_wing, 11, 92, -1.0F, -3.0F, -1.0F, 2, 2, 1, 0.0F, false));

		inactive_elytra_right_arm_right_wing_energy_cover6_r1 = new ModelRenderer(this);
		inactive_elytra_right_arm_right_wing_energy_cover6_r1.setRotationPoint(0.0F, -12.6569F, 5.1569F);
		inactive_elytra_right_wing.addChild(inactive_elytra_right_arm_right_wing_energy_cover6_r1);
		setRotationAngle(inactive_elytra_right_arm_right_wing_energy_cover6_r1, -1.5708F, 0.0F, 0.0F);
		inactive_elytra_right_arm_right_wing_energy_cover6_r1.cubeList.add(new ModelBox(inactive_elytra_right_arm_right_wing_energy_cover6_r1, 12, 92, -0.5F, -6.0F, 0.0F, 1, 2, 1, 0.0F, false));
		inactive_elytra_right_arm_right_wing_energy_cover6_r1.cubeList.add(new ModelBox(inactive_elytra_right_arm_right_wing_energy_cover6_r1, 6, 97, -1.0F, -7.0F, 0.5F, 2, 6, 1, 0.0F, false));
		inactive_elytra_right_arm_right_wing_energy_cover6_r1.cubeList.add(new ModelBox(inactive_elytra_right_arm_right_wing_energy_cover6_r1, 8, 88, -0.5F, -4.0F, 0.0F, 1, 4, 1, 0.0F, false));

		inactive_elytra_right_arm_right_wing_energy_cover5_r1 = new ModelRenderer(this);
		inactive_elytra_right_arm_right_wing_energy_cover5_r1.setRotationPoint(0.0F, -7.0F, -0.5F);
		inactive_elytra_right_wing.addChild(inactive_elytra_right_arm_right_wing_energy_cover5_r1);
		setRotationAngle(inactive_elytra_right_arm_right_wing_energy_cover5_r1, -0.7854F, 0.0F, 0.0F);
		inactive_elytra_right_arm_right_wing_energy_cover5_r1.cubeList.add(new ModelBox(inactive_elytra_right_arm_right_wing_energy_cover5_r1, 11, 92, -1.0F, -9.0F, 0.0F, 2, 2, 1, 0.0F, false));
		inactive_elytra_right_arm_right_wing_energy_cover5_r1.cubeList.add(new ModelBox(inactive_elytra_right_arm_right_wing_energy_cover5_r1, 11, 92, -1.0F, -6.0F, 0.0F, 2, 1, 1, 0.0F, false));
		inactive_elytra_right_arm_right_wing_energy_cover5_r1.cubeList.add(new ModelBox(inactive_elytra_right_arm_right_wing_energy_cover5_r1, 11, 92, -1.0F, -3.0F, 0.0F, 2, 1, 1, 0.0F, false));
		inactive_elytra_right_arm_right_wing_energy_cover5_r1.cubeList.add(new ModelBox(inactive_elytra_right_arm_right_wing_energy_cover5_r1, 11, 92, -1.0F, -1.0F, 0.0F, 2, 2, 1, 0.0F, false));
		inactive_elytra_right_arm_right_wing_energy_cover5_r1.cubeList.add(new ModelBox(inactive_elytra_right_arm_right_wing_energy_cover5_r1, 0, 97, -1.0F, -9.0F, 1.0F, 2, 10, 1, 0.0F, false));
		inactive_elytra_right_arm_right_wing_energy_cover5_r1.cubeList.add(new ModelBox(inactive_elytra_right_arm_right_wing_energy_cover5_r1, 32, 86, -0.5F, -8.0F, 0.25F, 1, 8, 2, 0.0F, false));

		inactive_elytra_left_wing = new ModelRenderer(this);
		inactive_elytra_left_wing.setRotationPoint(11.326F, -0.838F, -0.699F);
		elytra.addChild(inactive_elytra_left_wing);
		setRotationAngle(inactive_elytra_left_wing, -2.3562F, 0.3316F, -0.0175F);
		inactive_elytra_left_wing.cubeList.add(new ModelBox(inactive_elytra_left_wing, 0, 86, -1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F, false));
		inactive_elytra_left_wing.cubeList.add(new ModelBox(inactive_elytra_left_wing, 32, 86, -0.5F, -6.0F, -0.75F, 1, 5, 2, 0.0F, false));
		inactive_elytra_left_wing.cubeList.add(new ModelBox(inactive_elytra_left_wing, 23, 90, -1.0F, -6.0F, 0.0F, 2, 5, 1, 0.0F, false));
		inactive_elytra_left_wing.cubeList.add(new ModelBox(inactive_elytra_left_wing, 11, 92, -1.0F, -3.0F, -1.0F, 2, 2, 1, 0.0F, false));

		inactive_elytra_left_arm_left_wing_energy_cover6_r1 = new ModelRenderer(this);
		inactive_elytra_left_arm_left_wing_energy_cover6_r1.setRotationPoint(0.0F, -12.6569F, 5.1569F);
		inactive_elytra_left_wing.addChild(inactive_elytra_left_arm_left_wing_energy_cover6_r1);
		setRotationAngle(inactive_elytra_left_arm_left_wing_energy_cover6_r1, -1.5708F, 0.0F, 0.0F);
		inactive_elytra_left_arm_left_wing_energy_cover6_r1.cubeList.add(new ModelBox(inactive_elytra_left_arm_left_wing_energy_cover6_r1, 12, 92, -0.5F, -6.0F, 0.0F, 1, 2, 1, 0.0F, false));
		inactive_elytra_left_arm_left_wing_energy_cover6_r1.cubeList.add(new ModelBox(inactive_elytra_left_arm_left_wing_energy_cover6_r1, 6, 97, -1.0F, -7.0F, 0.5F, 2, 6, 1, 0.0F, false));
		inactive_elytra_left_arm_left_wing_energy_cover6_r1.cubeList.add(new ModelBox(inactive_elytra_left_arm_left_wing_energy_cover6_r1, 8, 88, -0.5F, -4.0F, 0.0F, 1, 4, 1, 0.0F, false));

		inactive_elytra_left_arm_left_wing_energy_cover5_r1 = new ModelRenderer(this);
		inactive_elytra_left_arm_left_wing_energy_cover5_r1.setRotationPoint(0.0F, -7.0F, -0.5F);
		inactive_elytra_left_wing.addChild(inactive_elytra_left_arm_left_wing_energy_cover5_r1);
		setRotationAngle(inactive_elytra_left_arm_left_wing_energy_cover5_r1, -0.7854F, 0.0F, 0.0F);
		inactive_elytra_left_arm_left_wing_energy_cover5_r1.cubeList.add(new ModelBox(inactive_elytra_left_arm_left_wing_energy_cover5_r1, 11, 92, -1.0F, -9.0F, 0.0F, 2, 2, 1, 0.0F, false));
		inactive_elytra_left_arm_left_wing_energy_cover5_r1.cubeList.add(new ModelBox(inactive_elytra_left_arm_left_wing_energy_cover5_r1, 11, 92, -1.0F, -6.0F, 0.0F, 2, 1, 1, 0.0F, false));
		inactive_elytra_left_arm_left_wing_energy_cover5_r1.cubeList.add(new ModelBox(inactive_elytra_left_arm_left_wing_energy_cover5_r1, 11, 92, -1.0F, -3.0F, 0.0F, 2, 1, 1, 0.0F, false));
		inactive_elytra_left_arm_left_wing_energy_cover5_r1.cubeList.add(new ModelBox(inactive_elytra_left_arm_left_wing_energy_cover5_r1, 11, 92, -1.0F, -1.0F, 0.0F, 2, 2, 1, 0.0F, false));
		inactive_elytra_left_arm_left_wing_energy_cover5_r1.cubeList.add(new ModelBox(inactive_elytra_left_arm_left_wing_energy_cover5_r1, 0, 97, -1.0F, -9.0F, 1.0F, 2, 10, 1, 0.0F, false));
		inactive_elytra_left_arm_left_wing_energy_cover5_r1.cubeList.add(new ModelBox(inactive_elytra_left_arm_left_wing_energy_cover5_r1, 32, 86, -0.5F, -8.0F, 0.25F, 1, 8, 2, 0.0F, false));
	}


	public void render(float size) {
		elytra.render(size);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}