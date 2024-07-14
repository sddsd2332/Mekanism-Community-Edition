// Made with Blockbench 4.10.3
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


public class ModelMekAsuitHead extends ModelBase {
	private final ModelRenderer helmet_armor;
	private final ModelRenderer hide;
	private final ModelRenderer helmet_head_center3_r1;
	private final ModelRenderer helmet_head_center1_r1;
	private final ModelRenderer helmet_head_visor_upper_r1;
	private final ModelRenderer helmet_head_visor_lower_r1;
	private final ModelRenderer helmet_head_chin2_r1;

	public ModelMekAsuitHead() {
		textureWidth = 128;
		textureHeight = 128;

		helmet_armor = new ModelRenderer(this);
		helmet_armor.setRotationPoint(0.0F, -2.0F, 1.0F);
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 0, 53, -2.0F, -1.0F, 1.5F, 1, 1, 1, 0.0F, false));
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 24, 39, 1.0F, -1.0F, 1.5F, 1, 1, 1, 0.0F, false));
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 84, 81, 3.0F, -2.0F, -2.0F, 1, 2, 3, 0.0F, false));
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 84, 76, -4.0F, -2.0F, -2.0F, 1, 2, 3, 0.0F, false));
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 42, 10, -3.5F, 1.0F, -6.0F, 7, 1, 4, 0.0F, false));
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 0, 20, -1.0F, 0.25F, -6.5F, 2, 2, 1, 0.0F, false));
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 34, 65, 1.0F, -4.0F, -3.0F, 3, 1, 5, 0.0F, false));
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 47, 2, 3.0F, -3.0F, 1.0F, 1, 2, 1, 0.0F, false));
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 63, 19, -4.0F, -4.0F, -3.0F, 3, 1, 5, 0.0F, false));
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 42, 10, -4.0F, -3.0F, 1.0F, 1, 2, 1, 0.0F, false));
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 74, 33, -3.0F, -4.0F, 2.0F, 6, 3, 1, 0.0F, false));
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 0, 16, -3.0F, -4.0F, -5.0F, 6, 5, 7, 0.0F, false));
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 55, 19, 3.005F, -3.0F, -1.005F, 1, 1, 1, 0.0F, false));
		helmet_armor.cubeList.add(new ModelBox(helmet_armor, 0, 55, -3.995F, -3.0F, -1.005F, 1, 1, 1, 0.0F, false));

		hide = new ModelRenderer(this);
		hide.setRotationPoint(0.0F, 0.0F, 0.0F);
		helmet_armor.addChild(hide);
		hide.cubeList.add(new ModelBox(hide, 71, 73, -1.0F, -6.0F, -4.0F, 2, 1, 5, 0.0F, false));
		hide.cubeList.add(new ModelBox(hide, 55, 0, -1.0F, -5.0F, 2.0F, 2, 1, 1, 0.0F, false));
		hide.cubeList.add(new ModelBox(hide, 54, 19, 1.0F, -5.5F, -4.5F, 1, 2, 7, 0.0F, false));
		hide.cubeList.add(new ModelBox(hide, 56, 40, -2.0F, -5.5F, -4.5F, 1, 2, 7, 0.0F, false));
		hide.cubeList.add(new ModelBox(hide, 19, 21, -3.0F, -5.0F, -5.0F, 6, 1, 7, 0.0F, false));
		hide.cubeList.add(new ModelBox(hide, 19, 21, -3.0F, -5.0F, -6.0F, 2, 1, 1, 0.0F, false));
		hide.cubeList.add(new ModelBox(hide, 44, 37, 1.0F, -5.0F, -6.0F, 2, 1, 1, 0.0F, false));

		helmet_head_center3_r1 = new ModelRenderer(this);
		helmet_head_center3_r1.setRotationPoint(-3.0F, 0.8076F, -2.1959F);
		hide.addChild(helmet_head_center3_r1);
		setRotationAngle(helmet_head_center3_r1, -0.6109F, 0.0F, 0.0F);
		helmet_head_center3_r1.cubeList.add(new ModelBox(helmet_head_center3_r1, 23, 29, 2.0F, -7.4096F, -1.2868F, 2, 1, 2, 0.0F, false));

		helmet_head_center1_r1 = new ModelRenderer(this);
		helmet_head_center1_r1.setRotationPoint(-3.0F, 2.5346F, -4.6669F);
		hide.addChild(helmet_head_center1_r1);
		setRotationAngle(helmet_head_center1_r1, 0.4363F, 0.0F, 0.0F);
		helmet_head_center1_r1.cubeList.add(new ModelBox(helmet_head_center1_r1, 54, 28, 2.0F, -7.4532F, 2.2113F, 2, 1, 2, 0.0F, false));

		helmet_head_visor_upper_r1 = new ModelRenderer(this);
		helmet_head_visor_upper_r1.setRotationPoint(-1.0F, 1.249F, -4.2677F);
		helmet_armor.addChild(helmet_head_visor_upper_r1);
		setRotationAngle(helmet_head_visor_upper_r1, -0.4363F, 0.0F, 0.0F);
		helmet_head_visor_upper_r1.cubeList.add(new ModelBox(helmet_head_visor_upper_r1, 79, 37, -2.0F, -4.4532F, -3.2113F, 6, 2, 1, 0.0F, false));

		helmet_head_visor_lower_r1 = new ModelRenderer(this);
		helmet_head_visor_lower_r1.setRotationPoint(-1.0F, 1.5F, -2.0F);
		helmet_armor.addChild(helmet_head_visor_lower_r1);
		setRotationAngle(helmet_head_visor_lower_r1, 0.1745F, 0.0F, 0.0F);
		helmet_head_visor_lower_r1.cubeList.add(new ModelBox(helmet_head_visor_lower_r1, 50, 67, -2.0F, -4.4924F, -3.4132F, 6, 4, 2, 0.0F, false));

		helmet_head_chin2_r1 = new ModelRenderer(this);
		helmet_head_chin2_r1.setRotationPoint(-1.0F, 2.1464F, 4.0104F);
		helmet_armor.addChild(helmet_head_chin2_r1);
		setRotationAngle(helmet_head_chin2_r1, 0.7854F, 0.0F, 0.0F);
		helmet_head_chin2_r1.cubeList.add(new ModelBox(helmet_head_chin2_r1, 55, 15, -2.5F, -5.3536F, -4.1464F, 7, 1, 3, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		helmet_armor.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}