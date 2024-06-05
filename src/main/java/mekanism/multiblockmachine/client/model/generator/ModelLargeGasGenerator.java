package mekanism.multiblockmachine.client.model.generator;

import mekanism.client.render.MekanismRenderer;
import mekanism.multiblockmachine.common.util.MekanismMultiblockMachineUtils;
import mekanism.multiblockmachine.common.util.MekanismMultiblockMachineUtils.ResourceType;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelLargeGasGenerator extends ModelBase {

    public static ResourceLocation OVERLAY_OFF = MekanismMultiblockMachineUtils.getResource(ResourceType.RENDER, "GasGenerator/LargeGasGenerator_OFF.png");

    ModelRenderer on45;
    ModelRenderer bone;
    ModelRenderer cube_r1;
    ModelRenderer bone2;
    ModelRenderer cube_r2;
    ModelRenderer bone3;
    ModelRenderer cube_r3;
    ModelRenderer bone4;
    ModelRenderer cube_r4;
    ModelRenderer mid45;
    ModelRenderer bone8;
    ModelRenderer cube_r5;
    ModelRenderer bone7;
    ModelRenderer cube_r6;
    ModelRenderer bone5;
    ModelRenderer cube_r7;
    ModelRenderer bone6;
    ModelRenderer cube_r8;
    ModelRenderer bone9;
    ModelRenderer cube_r9;
    ModelRenderer bone22;
    ModelRenderer cube_r10;
    ModelRenderer cube_r11;
    ModelRenderer cube_r12;
    ModelRenderer bone21;
    ModelRenderer cube_r13;
    ModelRenderer cube_r14;
    ModelRenderer cube_r15;
    ModelRenderer bone20;
    ModelRenderer cube_r16;
    ModelRenderer cube_r17;
    ModelRenderer cube_r18;
    ModelRenderer bone19;
    ModelRenderer cube_r19;
    ModelRenderer cube_r20;
    ModelRenderer cube_r21;
    ModelRenderer bb_main;
    ModelRenderer cube_r23;
    ModelRenderer cube_r24;
    ModelRenderer cube_r25;
    ModelRenderer cube_r26;
    ModelRenderer cube_r22;
    ModelRenderer portRight_r1;
    ModelRenderer portRight_r2;
    ModelRenderer portRight_r3;

    public ModelLargeGasGenerator() {
        textureWidth = 512;
        textureHeight = 256;

        on45 = new ModelRenderer(this);
        on45.setRotationPoint(0.0F, 22.0F, 0.0F);


        bone = new ModelRenderer(this);
        bone.setRotationPoint(16.0F, -24.0F, 0.0F);
        on45.addChild(bone);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, -16.0F, 0.0F);
        bone.addChild(cube_r1);
        setRotation(cube_r1, 0.0F, 0.0F, -0.7854F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 225, 188, -5.0F, -6.0F, -12.0F, 5, 6, 24, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
        on45.addChild(bone2);
        setRotation(bone2, 0.0F, -1.5708F, 0.0F);


        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(16.0F, -24.0F, 0.0F);
        bone2.addChild(cube_r2);
        setRotation(cube_r2, 0.0F, 0.0F, -0.7854F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 225, 188, 6.3137F, -17.3137F, -12.0F, 5, 6, 24, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, 0.0F, 0.0F);
        on45.addChild(bone3);
        setRotation(bone3, 0.0F, 3.1416F, 0.0F);


        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(16.0F, -24.0F, 0.0F);
        bone3.addChild(cube_r3);
        setRotation(cube_r3, 0.0F, 0.0F, -0.7854F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 225, 188, 6.3137F, -17.3137F, -12.0F, 5, 6, 24, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, 0.0F, 0.0F);
        on45.addChild(bone4);
        setRotation(bone4, 0.0F, 1.5708F, 0.0F);


        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(16.0F, -24.0F, 0.0F);
        bone4.addChild(cube_r4);
        setRotation(cube_r4, 0.0F, 0.0F, -0.7854F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 225, 188, 6.3137F, -17.3137F, -12.0F, 5, 6, 24, 0.0F, false));

        mid45 = new ModelRenderer(this);
        mid45.setRotationPoint(0.0F, 23.0F, 0.0F);


        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, 0.0F, 0.0F);
        mid45.addChild(bone8);
        setRotation(bone8, 0.0F, 1.5708F, 0.0F);


        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(21.0F, -14.0F, 0.0F);
        bone8.addChild(cube_r5);
        setRotation(cube_r5, 0.0F, 0.0F, -0.7854F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 144, 227, 10.8137F, -18.3137F, -9.0F, 1, 6, 18, 0.0F, false));
        cube_r5.cubeList.add(new ModelBox(cube_r5, 202, 156, 6.3137F, -19.3137F, -12.0F, 5, 8, 24, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, 0.0F, 0.0F);
        mid45.addChild(bone7);
        setRotation(bone7, 0.0F, 3.1416F, 0.0F);


        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(21.0F, -14.0F, 0.0F);
        bone7.addChild(cube_r6);
        setRotation(cube_r6, 0.0F, 0.0F, -0.7854F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 168, 175, 6.3137F, -19.3137F, -12.0F, 5, 8, 24, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, 0.0F, 0.0F);
        mid45.addChild(bone5);
        setRotation(bone5, 0.0F, -1.5708F, 0.0F);


        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(21.0F, -14.0F, 0.0F);
        bone5.addChild(cube_r7);
        setRotation(cube_r7, 0.0F, 0.0F, -0.7854F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 168, 175, 6.3137F, -19.3137F, -12.0F, 5, 8, 24, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, 0.0F, 0.0F);
        mid45.addChild(bone6);


        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(21.0F, -30.0F, 0.0F);
        bone6.addChild(cube_r8);
        setRotation(cube_r8, 0.0F, 0.0F, -0.7854F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 168, 175, -5.0F, -8.0F, -12.0F, 5, 8, 24, 0.0F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(20.75F, 5.0F, -23.0F);


        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(0.0F, -16.0F, 0.0F);
        bone9.addChild(cube_r9);
        setRotation(cube_r9, 0.0F, 1.5708F, 0.0F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 444, 242, -2.5F, 13.0F, -18.75F, 1, 7, 7, 0.0F, false));
        cube_r9.cubeList.add(new ModelBox(cube_r9, 380, 242, -2.5F, 5.0F, -18.75F, 1, 7, 7, 0.0F, false));
        cube_r9.cubeList.add(new ModelBox(cube_r9, 412, 242, -2.5F, 21.0F, -18.75F, 1, 7, 7, 0.0F, false));
        cube_r9.cubeList.add(new ModelBox(cube_r9, 396, 242, -2.5F, 21.0F, -29.75F, 1, 7, 7, 0.0F, false));
        cube_r9.cubeList.add(new ModelBox(cube_r9, 428, 242, -2.5F, 13.0F, -29.75F, 1, 7, 7, 0.0F, false));
        cube_r9.cubeList.add(new ModelBox(cube_r9, 364, 242, -2.5F, 5.0F, -29.75F, 1, 7, 7, 0.0F, false));

        bone22 = new ModelRenderer(this);
        bone22.setRotationPoint(-16.5F, 33.0F, 37.0F);
        bone22.cubeList.add(new ModelBox(bone22, 7, 10, -5.5F, -54.0F, -16.0F, 9, 1, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 45, 13, -3.5F, -55.5F, -18.0F, 2, 1, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 45, 13, -3.5F, -55.5F, -19.0F, 1, 1, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 43, 14, -2.5F, -55.5F, -21.0F, 3, 1, 3, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 45, 13, -0.5F, -55.5F, -18.0F, 2, 1, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 45, 13, 0.5F, -55.5F, -21.0F, 1, 1, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 45, 13, 0.5F, -55.5F, -19.0F, 1, 1, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 45, 13, -0.5F, -55.5F, -22.0F, 2, 1, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 45, 13, -3.5F, -55.5F, -22.0F, 2, 1, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 45, 13, -3.5F, -55.5F, -21.0F, 1, 1, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 10, 13, -5.5F, -54.0F, -23.0F, 1, 1, 7, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 11, 7, -5.5F, -56.0F, -24.0F, 1, 2, 3, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 9, 5, -4.5F, -56.0F, -24.0F, 2, 2, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 7, 10, -5.5F, -54.0F, -24.0F, 9, 1, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 10, 13, 2.5F, -54.0F, -23.0F, 1, 1, 7, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 200, 226, -2.0F, -54.5F, -20.5F, 2, 1, 2, 0.0F, false));

        cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(0.5F, -8.0F, -21.0F);
        bone22.addChild(cube_r10);
        setRotation(cube_r10, 0.0F, -1.5708F, 0.0F);
        cube_r10.cubeList.add(new ModelBox(cube_r10, 6, 22, -2.0F, -48.0F, -3.0F, 2, 2, 1, 0.0F, false));
        cube_r10.cubeList.add(new ModelBox(cube_r10, 8, 24, -3.0F, -48.0F, -3.0F, 1, 2, 3, 0.0F, false));

        cube_r11 = new ModelRenderer(this);
        cube_r11.setRotationPoint(0.5F, -8.0F, -21.0F);
        bone22.addChild(cube_r11);
        setRotation(cube_r11, 0.0F, 1.5708F, 0.0F);
        cube_r11.cubeList.add(new ModelBox(cube_r11, 14, 14, -6.0F, -48.0F, -6.0F, 1, 2, 3, 0.0F, false));
        cube_r11.cubeList.add(new ModelBox(cube_r11, 12, 12, -5.0F, -48.0F, -6.0F, 2, 2, 1, 0.0F, false));

        cube_r12 = new ModelRenderer(this);
        cube_r12.setRotationPoint(0.5F, -8.0F, -21.0F);
        bone22.addChild(cube_r12);
        setRotation(cube_r12, 0.0F, 3.1416F, 0.0F);
        cube_r12.cubeList.add(new ModelBox(cube_r12, 16, 10, -3.0F, -48.0F, -6.0F, 1, 2, 3, 0.0F, false));
        cube_r12.cubeList.add(new ModelBox(cube_r12, 14, 8, -2.0F, -48.0F, -6.0F, 2, 2, 1, 0.0F, false));

        bone21 = new ModelRenderer(this);
        bone21.setRotationPoint(35.0F, 0.0F, -35.0F);
        bone22.addChild(bone21);
        bone21.cubeList.add(new ModelBox(bone21, 7, 10, -40.5F, -54.0F, -16.0F, 9, 1, 1, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 45, 13, -38.5F, -55.5F, -18.0F, 2, 1, 1, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 45, 13, -38.5F, -55.5F, -19.0F, 1, 1, 1, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 43, 14, -37.5F, -55.5F, -21.0F, 3, 1, 3, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 45, 13, -35.5F, -55.5F, -18.0F, 2, 1, 1, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 45, 13, -34.5F, -55.5F, -21.0F, 1, 1, 1, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 45, 13, -34.5F, -55.5F, -19.0F, 1, 1, 1, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 45, 13, -35.5F, -55.5F, -22.0F, 2, 1, 1, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 45, 13, -38.5F, -55.5F, -22.0F, 2, 1, 1, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 45, 13, -38.5F, -55.5F, -21.0F, 1, 1, 1, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 10, 13, -40.5F, -54.0F, -23.0F, 1, 1, 7, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 11, 7, -40.5F, -56.0F, -24.0F, 1, 2, 3, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 9, 5, -39.5F, -56.0F, -24.0F, 2, 2, 1, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 7, 10, -40.5F, -54.0F, -24.0F, 9, 1, 1, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 10, 13, -32.5F, -54.0F, -23.0F, 1, 1, 7, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 200, 226, -37.0F, -54.5F, -20.5F, 2, 1, 2, 0.0F, false));

        cube_r13 = new ModelRenderer(this);
        cube_r13.setRotationPoint(-34.5F, -8.0F, -21.0F);
        bone21.addChild(cube_r13);
        setRotation(cube_r13, 0.0F, -1.5708F, 0.0F);
        cube_r13.cubeList.add(new ModelBox(cube_r13, 6, 22, -2.0F, -48.0F, -3.0F, 2, 2, 1, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 8, 24, -3.0F, -48.0F, -3.0F, 1, 2, 3, 0.0F, false));

        cube_r14 = new ModelRenderer(this);
        cube_r14.setRotationPoint(-34.5F, -8.0F, -21.0F);
        bone21.addChild(cube_r14);
        setRotation(cube_r14, 0.0F, 1.5708F, 0.0F);
        cube_r14.cubeList.add(new ModelBox(cube_r14, 14, 14, -6.0F, -48.0F, -6.0F, 1, 2, 3, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 12, 12, -5.0F, -48.0F, -6.0F, 2, 2, 1, 0.0F, false));

        cube_r15 = new ModelRenderer(this);
        cube_r15.setRotationPoint(-34.5F, -8.0F, -21.0F);
        bone21.addChild(cube_r15);
        setRotation(cube_r15, 0.0F, 3.1416F, 0.0F);
        cube_r15.cubeList.add(new ModelBox(cube_r15, 16, 10, -3.0F, -48.0F, -6.0F, 1, 2, 3, 0.0F, false));
        cube_r15.cubeList.add(new ModelBox(cube_r15, 14, 8, -2.0F, -48.0F, -6.0F, 2, 2, 1, 0.0F, false));

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(35.0F, 0.0F, -35.0F);
        bone22.addChild(bone20);
        bone20.cubeList.add(new ModelBox(bone20, 7, 10, -5.5F, -54.0F, 19.0F, 9, 1, 1, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 45, 13, -3.5F, -55.5F, 17.0F, 2, 1, 1, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 45, 13, -3.5F, -55.5F, 16.0F, 1, 1, 1, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 43, 14, -2.5F, -55.5F, 14.0F, 3, 1, 3, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 45, 13, -0.5F, -55.5F, 17.0F, 2, 1, 1, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 45, 13, 0.5F, -55.5F, 14.0F, 1, 1, 1, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 45, 13, 0.5F, -55.5F, 16.0F, 1, 1, 1, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 45, 13, -0.5F, -55.5F, 13.0F, 2, 1, 1, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 45, 13, -3.5F, -55.5F, 13.0F, 2, 1, 1, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 45, 13, -3.5F, -55.5F, 14.0F, 1, 1, 1, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 10, 13, -5.5F, -54.0F, 12.0F, 1, 1, 7, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 11, 7, -5.5F, -56.0F, 11.0F, 1, 2, 3, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 9, 5, -4.5F, -56.0F, 11.0F, 2, 2, 1, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 7, 10, -5.5F, -54.0F, 11.0F, 9, 1, 1, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 10, 13, 2.5F, -54.0F, 12.0F, 1, 1, 7, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 200, 226, -2.0F, -54.5F, 14.5F, 2, 1, 2, 0.0F, false));

        cube_r16 = new ModelRenderer(this);
        cube_r16.setRotationPoint(0.5F, -8.0F, 14.0F);
        bone20.addChild(cube_r16);
        setRotation(cube_r16, 0.0F, -1.5708F, 0.0F);
        cube_r16.cubeList.add(new ModelBox(cube_r16, 6, 22, -2.0F, -48.0F, -3.0F, 2, 2, 1, 0.0F, false));
        cube_r16.cubeList.add(new ModelBox(cube_r16, 8, 24, -3.0F, -48.0F, -3.0F, 1, 2, 3, 0.0F, false));

        cube_r17 = new ModelRenderer(this);
        cube_r17.setRotationPoint(0.5F, -8.0F, 14.0F);
        bone20.addChild(cube_r17);
        setRotation(cube_r17, 0.0F, 1.5708F, 0.0F);
        cube_r17.cubeList.add(new ModelBox(cube_r17, 14, 14, -6.0F, -48.0F, -6.0F, 1, 2, 3, 0.0F, false));
        cube_r17.cubeList.add(new ModelBox(cube_r17, 12, 12, -5.0F, -48.0F, -6.0F, 2, 2, 1, 0.0F, false));

        cube_r18 = new ModelRenderer(this);
        cube_r18.setRotationPoint(0.5F, -8.0F, 14.0F);
        bone20.addChild(cube_r18);
        setRotation(cube_r18, 0.0F, 3.1416F, 0.0F);
        cube_r18.cubeList.add(new ModelBox(cube_r18, 16, 10, -3.0F, -48.0F, -6.0F, 1, 2, 3, 0.0F, false));
        cube_r18.cubeList.add(new ModelBox(cube_r18, 14, 8, -2.0F, -48.0F, -6.0F, 2, 2, 1, 0.0F, false));

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(35.0F, 0.0F, -35.0F);
        bone22.addChild(bone19);
        bone19.cubeList.add(new ModelBox(bone19, 7, 10, -5.5F, -54.0F, -16.0F, 9, 1, 1, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 45, 13, -3.5F, -55.5F, -18.0F, 2, 1, 1, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 45, 13, -3.5F, -55.5F, -19.0F, 1, 1, 1, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 43, 14, -2.5F, -55.5F, -21.0F, 3, 1, 3, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 45, 13, -0.5F, -55.5F, -18.0F, 2, 1, 1, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 45, 13, 0.5F, -55.5F, -21.0F, 1, 1, 1, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 45, 13, 0.5F, -55.5F, -19.0F, 1, 1, 1, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 45, 13, -0.5F, -55.5F, -22.0F, 2, 1, 1, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 45, 13, -3.5F, -55.5F, -22.0F, 2, 1, 1, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 45, 13, -3.5F, -55.5F, -21.0F, 1, 1, 1, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 10, 13, -5.5F, -54.0F, -23.0F, 1, 1, 7, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 11, 7, -5.5F, -56.0F, -24.0F, 1, 2, 3, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 9, 5, -4.5F, -56.0F, -24.0F, 2, 2, 1, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 7, 10, -5.5F, -54.0F, -24.0F, 9, 1, 1, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 10, 13, 2.5F, -54.0F, -23.0F, 1, 1, 7, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 200, 226, -2.0F, -54.5F, -20.5F, 2, 1, 2, 0.0F, false));

        cube_r19 = new ModelRenderer(this);
        cube_r19.setRotationPoint(0.5F, -8.0F, -21.0F);
        bone19.addChild(cube_r19);
        setRotation(cube_r19, 0.0F, -1.5708F, 0.0F);
        cube_r19.cubeList.add(new ModelBox(cube_r19, 6, 22, -2.0F, -48.0F, -3.0F, 2, 2, 1, 0.0F, false));
        cube_r19.cubeList.add(new ModelBox(cube_r19, 8, 24, -3.0F, -48.0F, -3.0F, 1, 2, 3, 0.0F, false));

        cube_r20 = new ModelRenderer(this);
        cube_r20.setRotationPoint(0.5F, -8.0F, -21.0F);
        bone19.addChild(cube_r20);
        setRotation(cube_r20, 0.0F, 1.5708F, 0.0F);
        cube_r20.cubeList.add(new ModelBox(cube_r20, 14, 14, -6.0F, -48.0F, -6.0F, 1, 2, 3, 0.0F, false));
        cube_r20.cubeList.add(new ModelBox(cube_r20, 12, 12, -5.0F, -48.0F, -6.0F, 2, 2, 1, 0.0F, false));

        cube_r21 = new ModelRenderer(this);
        cube_r21.setRotationPoint(0.5F, -8.0F, -21.0F);
        bone19.addChild(cube_r21);
        setRotation(cube_r21, 0.0F, 3.1416F, 0.0F);
        cube_r21.cubeList.add(new ModelBox(cube_r21, 16, 10, -3.0F, -48.0F, -6.0F, 1, 2, 3, 0.0F, false));
        cube_r21.cubeList.add(new ModelBox(cube_r21, 14, 8, -2.0F, -48.0F, -6.0F, 2, 2, 1, 0.0F, false));

        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.cubeList.add(new ModelBox(bb_main, 192, 207, 12.0F, -44.0F, -23.0F, 11, 38, 11, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 132, 45, -4.0F, -28.0F, 23.0F, 8, 8, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -3.0F, -27.0F, 20.5F, 6, 6, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -3.0F, -11.0F, 20.5F, 6, 6, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 132, 45, -4.0F, -12.0F, 23.0F, 8, 7, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 203, -24.0F, -5.0F, -24.0F, 48, 5, 48, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 236, 218, -16.0F, -42.0F, -16.0F, 32, 6, 32, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 126, 146, -12.0F, -47.0F, -12.0F, 24, 5, 24, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 134, -21.0F, -31.0F, -21.0F, 42, 26, 42, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 192, 207, -23.0F, -44.0F, -23.0F, 11, 38, 11, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 192, 207, -23.0F, -44.0F, 12.0F, 11, 38, 11, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 192, 207, 12.0F, -44.0F, 12.0F, 11, 38, 11, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 244, 239, 14.0F, -18.0F, -23.25F, 7, 10, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 244, 239, 14.0F, -30.0F, -23.25F, 7, 10, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 244, 239, 14.0F, -42.0F, -23.25F, 7, 10, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 244, 239, -21.0F, -42.0F, -23.25F, 7, 10, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 244, 239, -21.0F, -30.0F, -23.25F, 7, 10, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 244, 239, -21.0F, -18.0F, -23.25F, 7, 10, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 244, 239, -21.0F, -18.0F, -23.25F, 7, 10, 1, 0.0F, true));
        bb_main.cubeList.add(new ModelBox(bb_main, 244, 239, -21.0F, -30.0F, -23.25F, 7, 10, 1, 0.0F, true));
        bb_main.cubeList.add(new ModelBox(bb_main, 244, 239, -21.0F, -42.0F, -23.25F, 7, 10, 1, 0.0F, true));
        bb_main.cubeList.add(new ModelBox(bb_main, 244, 239, 14.0F, -42.0F, -23.25F, 7, 10, 1, 0.0F, true));
        bb_main.cubeList.add(new ModelBox(bb_main, 244, 239, 14.0F, -30.0F, -23.25F, 7, 10, 1, 0.0F, true));
        bb_main.cubeList.add(new ModelBox(bb_main, 244, 239, 14.0F, -18.0F, -23.25F, 7, 10, 1, 0.0F, true));

        cube_r23 = new ModelRenderer(this);
        cube_r23.setRotationPoint(0.0F, -16.0F, 0.0F);
        bb_main.addChild(cube_r23);
        setRotation(cube_r23, 0.0F, -1.5708F, 0.0F);
        cube_r23.cubeList.add(new ModelBox(cube_r23, 244, 239, 14.0F, -2.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r23.cubeList.add(new ModelBox(cube_r23, 244, 239, 14.0F, -14.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r23.cubeList.add(new ModelBox(cube_r23, 244, 239, 14.0F, -26.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r23.cubeList.add(new ModelBox(cube_r23, 244, 239, -21.0F, -26.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r23.cubeList.add(new ModelBox(cube_r23, 244, 239, -21.0F, -14.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r23.cubeList.add(new ModelBox(cube_r23, 244, 239, -21.0F, -2.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r23.cubeList.add(new ModelBox(cube_r23, 244, 239, -21.0F, -2.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r23.cubeList.add(new ModelBox(cube_r23, 244, 239, -21.0F, -14.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r23.cubeList.add(new ModelBox(cube_r23, 244, 239, -21.0F, -26.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r23.cubeList.add(new ModelBox(cube_r23, 244, 239, 14.0F, -26.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r23.cubeList.add(new ModelBox(cube_r23, 244, 239, 14.0F, -14.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r23.cubeList.add(new ModelBox(cube_r23, 244, 239, 14.0F, -2.0F, -23.25F, 7, 10, 1, 0.0F, false));

        cube_r24 = new ModelRenderer(this);
        cube_r24.setRotationPoint(0.0F, -16.0F, 0.0F);
        bb_main.addChild(cube_r24);
        setRotation(cube_r24, 0.0F, 1.5708F, 0.0F);
        cube_r24.cubeList.add(new ModelBox(cube_r24, 244, 239, 14.0F, -2.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r24.cubeList.add(new ModelBox(cube_r24, 244, 239, 14.0F, -14.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r24.cubeList.add(new ModelBox(cube_r24, 244, 239, 14.0F, -26.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r24.cubeList.add(new ModelBox(cube_r24, 244, 239, -21.0F, -26.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r24.cubeList.add(new ModelBox(cube_r24, 244, 239, -21.0F, -14.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r24.cubeList.add(new ModelBox(cube_r24, 244, 239, -21.0F, -2.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r24.cubeList.add(new ModelBox(cube_r24, 244, 239, -21.0F, -2.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r24.cubeList.add(new ModelBox(cube_r24, 244, 239, -21.0F, -14.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r24.cubeList.add(new ModelBox(cube_r24, 244, 239, -21.0F, -26.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r24.cubeList.add(new ModelBox(cube_r24, 244, 239, 14.0F, -26.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r24.cubeList.add(new ModelBox(cube_r24, 244, 239, 14.0F, -14.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r24.cubeList.add(new ModelBox(cube_r24, 244, 239, 14.0F, -2.0F, -23.25F, 7, 10, 1, 0.0F, false));

        cube_r25 = new ModelRenderer(this);
        cube_r25.setRotationPoint(0.0F, -16.0F, 0.0F);
        bb_main.addChild(cube_r25);
        setRotation(cube_r25, 0.0F, -3.1416F, 0.0F);
        cube_r25.cubeList.add(new ModelBox(cube_r25, 244, 239, 14.0F, -26.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r25.cubeList.add(new ModelBox(cube_r25, 244, 239, 14.0F, -14.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r25.cubeList.add(new ModelBox(cube_r25, 244, 239, 14.0F, -2.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r25.cubeList.add(new ModelBox(cube_r25, 244, 239, -21.0F, -14.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r25.cubeList.add(new ModelBox(cube_r25, 244, 239, -21.0F, -26.0F, -23.25F, 7, 10, 1, 0.0F, true));
        cube_r25.cubeList.add(new ModelBox(cube_r25, 244, 239, -21.0F, -2.0F, -23.25F, 7, 10, 1, 0.0F, true));

        cube_r26 = new ModelRenderer(this);
        cube_r26.setRotationPoint(0.0F, -16.0F, 0.0F);
        bb_main.addChild(cube_r26);
        setRotation(cube_r26, 0.0F, 3.1416F, 0.0F);
        cube_r26.cubeList.add(new ModelBox(cube_r26, 244, 239, -21.0F, -26.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r26.cubeList.add(new ModelBox(cube_r26, 244, 239, -21.0F, -14.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r26.cubeList.add(new ModelBox(cube_r26, 244, 239, -21.0F, -2.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r26.cubeList.add(new ModelBox(cube_r26, 244, 239, 14.0F, -14.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r26.cubeList.add(new ModelBox(cube_r26, 244, 239, 14.0F, -26.0F, -23.25F, 7, 10, 1, 0.0F, false));
        cube_r26.cubeList.add(new ModelBox(cube_r26, 244, 239, 14.0F, -2.0F, -23.25F, 7, 10, 1, 0.0F, false));

        cube_r22 = new ModelRenderer(this);
        cube_r22.setRotationPoint(20.75F, -35.0F, -23.0F);
        bb_main.addChild(cube_r22);
        setRotation(cube_r22, 0.0F, 1.5708F, 0.0F);
        cube_r22.cubeList.add(new ModelBox(cube_r22, 236, 230, -8.75F, -5.0F, -14.75F, 2, 2, 2, 0.0F, false));
        cube_r22.cubeList.add(new ModelBox(cube_r22, 236, 234, -8.75F, -5.0F, -19.75F, 2, 2, 2, 0.0F, false));
        cube_r22.cubeList.add(new ModelBox(cube_r22, 236, 238, -8.75F, -5.0F, -22.75F, 2, 2, 2, 0.0F, false));
        cube_r22.cubeList.add(new ModelBox(cube_r22, 236, 242, -8.75F, -5.0F, -25.75F, 2, 2, 2, 0.0F, false));
        cube_r22.cubeList.add(new ModelBox(cube_r22, 236, 246, -8.75F, -5.0F, -28.75F, 2, 2, 2, 0.0F, false));

        portRight_r1 = new ModelRenderer(this);
        portRight_r1.setRotationPoint(0.0F, -48.0F, 22.75F);
        bb_main.addChild(portRight_r1);
        setRotation(portRight_r1, 1.5708F, 0.0F, 0.0F);
        portRight_r1.cubeList.add(new ModelBox(portRight_r1, 132, 27, -4.0F, -27.0F, -1.0F, 8, 8, 1, 0.0F, false));

        portRight_r2 = new ModelRenderer(this);
        portRight_r2.setRotationPoint(0.0F, -25.0F, 0.0F);
        bb_main.addChild(portRight_r2);
        setRotation(portRight_r2, 0.0F, 1.5708F, 0.0F);
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 132, 45, -4.0F, 13.0F, 23.0F, 8, 7, 1, 0.0F, false));
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 0, 0, -3.0F, 14.0F, 20.5F, 6, 6, 3, 0.0F, false));
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 0, 0, -3.0F, -2.0F, 20.5F, 6, 6, 3, 0.0F, false));
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 132, 45, -4.0F, -3.0F, 23.0F, 8, 8, 1, 0.0F, false));

        portRight_r3 = new ModelRenderer(this);
        portRight_r3.setRotationPoint(0.0F, -24.0F, 0.0F);
        bb_main.addChild(portRight_r3);
        setRotation(portRight_r3, 0.0F, -1.5708F, 0.0F);
        portRight_r3.cubeList.add(new ModelBox(portRight_r3, 132, 45, -4.0F, 12.0F, 23.0F, 8, 7, 1, 0.0F, false));
        portRight_r3.cubeList.add(new ModelBox(portRight_r3, 0, 0, -3.0F, 13.0F, 20.5F, 6, 6, 3, 0.0F, false));
        portRight_r3.cubeList.add(new ModelBox(portRight_r3, 0, 0, -3.0F, -3.0F, 20.5F, 6, 6, 3, 0.0F, false));
        portRight_r3.cubeList.add(new ModelBox(portRight_r3, 132, 45, -4.0F, -4.0F, 23.0F, 8, 8, 1, 0.0F, false));
    }

    public void render(double tick, float size, boolean on, TextureManager manager) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        doRender(size);
        manager.bindTexture(on ? MekanismMultiblockMachineUtils.getResource(ResourceType.RENDER, "GasGenerator/LargeGasGenerator_ON_" + getTick(tick) + ".png") : OVERLAY_OFF);
        GlStateManager.scale(1.001F, 1.001F, 1.001F);
        GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
        MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
        doRender(size);
        MekanismRenderer.disableGlow(glowInfo);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }

    public void renderBloom(double tick, float size, boolean on, TextureManager manager) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        manager.bindTexture(on ? MekanismMultiblockMachineUtils.getResource(ResourceType.RENDER, "GasGenerator/LargeGasGenerator_ON_" + getTick(tick) + ".png") : OVERLAY_OFF);
        GlStateManager.scale(1.0011F, 1.0011F, 1.0011F);
        GlStateManager.translate(-0.0012F, -0.0012F, -0.0012F);
        MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
        doRender(size);
        MekanismRenderer.disableGlow(glowInfo);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }

    public void doRender(float size) {
        on45.render(size);
        mid45.render(size);
        bone9.render(size);
        bone22.render(size);
        bb_main.render(size);
    }

    public void setRotation(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public int getTick(double tick) {
        if (tick >= 0.1F && tick < 0.2F || tick >= 0.6F && tick < 0.7F) {
            return 0;
        } else if (tick >= 0.2F && tick < 0.3F || tick >= 0.7F && tick < 0.8F) {
            return 1;
        } else if (tick >= 0.3F && tick < 0.4F || tick >= 0.8F && tick < 0.9F) {
            return 2;
        } else
            return 3;
    }

}
