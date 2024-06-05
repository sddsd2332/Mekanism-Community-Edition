package mekanism.multiblockmachine.client.model.generator;

import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
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
public class ModelLargeWindGenerator extends ModelBase {

    public static ResourceLocation OVERLAY_OFF = MekanismMultiblockMachineUtils.getResource(ResourceType.RENDER, "WindGenerator/LargeWindGenerator_OFF.png");

    ModelRenderer doll_up;
    ModelRenderer cube_r1;
    ModelRenderer cube_r2;
    ModelRenderer cube_r3;
    ModelRenderer cube_r4;
    ModelRenderer bone4;
    ModelRenderer uio_up;
    ModelRenderer cube_r5;
    ModelRenderer cube_r6;
    ModelRenderer cube_r7;
    ModelRenderer cube_r8;
    ModelRenderer bone24;
    ModelRenderer doll_up2;
    ModelRenderer cube_r9;
    ModelRenderer cube_r10;
    ModelRenderer cube_r11;
    ModelRenderer cube_r12;
    ModelRenderer bone25;
    ModelRenderer bolt;
    ModelRenderer bone2;
    ModelRenderer cube_r13;
    ModelRenderer cube_r14;
    ModelRenderer bone;
    ModelRenderer wind_turbine_head;
    ModelRenderer cube_r15;
    ModelRenderer cube_r16;
    ModelRenderer cube_r17;
    ModelRenderer cube_r18;
    ModelRenderer cube_r19;
    ModelRenderer cube_r20;
    ModelRenderer cube_r21;
    ModelRenderer cube_r22;
    ModelRenderer cube_r23;
    ModelRenderer cube_r24;
    ModelRenderer cube_r25;
    ModelRenderer cube_r26;
    ModelRenderer cube_r27;
    ModelRenderer wind_power_middle;
    ModelRenderer cube_r28;
    ModelRenderer cube_r29;
    ModelRenderer cube_r30;
    ModelRenderer cube_r31;
    ModelRenderer doll2;
    ModelRenderer cube_r32;
    ModelRenderer cube_r33;
    ModelRenderer cube_r34;
    ModelRenderer cube_r35;
    ModelRenderer cube_r36;
    ModelRenderer cube_r37;
    ModelRenderer cube_r38;
    ModelRenderer cube_r39;
    ModelRenderer cube_r40;
    ModelRenderer cube_r41;
    ModelRenderer cube_r42;
    ModelRenderer cube_r43;
    ModelRenderer cube_r44;
    ModelRenderer doll;
    ModelRenderer cube_r45;
    ModelRenderer bone3;
    ModelRenderer fans;
    ModelRenderer fan0;
    ModelRenderer fan_blade_tip2;
    ModelRenderer a16;
    ModelRenderer a2_r1;
    ModelRenderer a17;
    ModelRenderer a2_r2;
    ModelRenderer a18;
    ModelRenderer a2_r3;
    ModelRenderer a19;
    ModelRenderer a2_r4;
    ModelRenderer a20;
    ModelRenderer a2_r5;
    ModelRenderer a21;
    ModelRenderer a2_r6;
    ModelRenderer a22;
    ModelRenderer a2_r7;
    ModelRenderer a23;
    ModelRenderer a2_r8;
    ModelRenderer a24;
    ModelRenderer a2_r9;
    ModelRenderer a25;
    ModelRenderer a2_r10;
    ModelRenderer a26;
    ModelRenderer a2_r11;
    ModelRenderer a27;
    ModelRenderer a2_r12;
    ModelRenderer a28;
    ModelRenderer a2_r13;
    ModelRenderer a29;
    ModelRenderer a2_r14;
    ModelRenderer a30;
    ModelRenderer a2_r15;
    ModelRenderer a31;
    ModelRenderer a2_r16;
    ModelRenderer fan_blade_tip;
    ModelRenderer a1;
    ModelRenderer a2_r17;
    ModelRenderer a2;
    ModelRenderer a2_r18;
    ModelRenderer a3;
    ModelRenderer a2_r19;
    ModelRenderer a4;
    ModelRenderer a2_r20;
    ModelRenderer a5;
    ModelRenderer a2_r21;
    ModelRenderer a6;
    ModelRenderer a2_r22;
    ModelRenderer a7;
    ModelRenderer a2_r23;
    ModelRenderer a8;
    ModelRenderer a2_r24;
    ModelRenderer a9;
    ModelRenderer a2_r25;
    ModelRenderer a10;
    ModelRenderer a2_r26;
    ModelRenderer a11;
    ModelRenderer a2_r27;
    ModelRenderer a12;
    ModelRenderer a2_r28;
    ModelRenderer a13;
    ModelRenderer a2_r29;
    ModelRenderer a14;
    ModelRenderer a2_r30;
    ModelRenderer a15;
    ModelRenderer a2_r31;
    ModelRenderer fan_blade_top;
    ModelRenderer a15_r1;
    ModelRenderer a14_r1;
    ModelRenderer a13_r1;
    ModelRenderer a13_r2;
    ModelRenderer a12_r1;
    ModelRenderer a11_r1;
    ModelRenderer a10_r1;
    ModelRenderer a9_r1;
    ModelRenderer a8_r1;
    ModelRenderer a8_r2;
    ModelRenderer a7_r1;
    ModelRenderer a6_r1;
    ModelRenderer a5_r1;
    ModelRenderer a4_r1;
    ModelRenderer a3_r1;
    ModelRenderer a3_r2;
    ModelRenderer a2_r32;
    ModelRenderer fan1;
    ModelRenderer cube_r46;
    ModelRenderer cube_r47;
    ModelRenderer fan2;
    ModelRenderer cube_r48;
    ModelRenderer cube_r49;
    ModelRenderer fan3;
    ModelRenderer cube_r50;
    ModelRenderer cube_r51;
    ModelRenderer fan_blade_mid;
    ModelRenderer a32;
    ModelRenderer bone23;
    ModelRenderer a33;
    ModelRenderer bone5;
    ModelRenderer a34;
    ModelRenderer bone6;
    ModelRenderer a35;
    ModelRenderer bone7;
    ModelRenderer a36;
    ModelRenderer bone8;
    ModelRenderer a37;
    ModelRenderer bone9;
    ModelRenderer a38;
    ModelRenderer bone10;
    ModelRenderer a39;
    ModelRenderer bone11;
    ModelRenderer a40;
    ModelRenderer bone12;
    ModelRenderer a41;
    ModelRenderer bone13;
    ModelRenderer bone22;
    ModelRenderer a42;
    ModelRenderer bone14;
    ModelRenderer a43;
    ModelRenderer bone15;
    ModelRenderer a44;
    ModelRenderer bone16;
    ModelRenderer a45;
    ModelRenderer bone17;
    ModelRenderer a46;
    ModelRenderer bone18;
    ModelRenderer bone28;
    ModelRenderer cube_r52;
    ModelRenderer cube_r53;
    ModelRenderer cube_r54;
    ModelRenderer cube_r55;
    ModelRenderer cube_r56;
    ModelRenderer cube_r57;
    ModelRenderer cube_r58;
    ModelRenderer fan_base2;
    ModelRenderer cube_r59;
    ModelRenderer fan_base;
    ModelRenderer cube_r60;
    ModelRenderer cube_r61;
    ModelRenderer fan_base3;
    ModelRenderer cube_r62;
    ModelRenderer cube_r63;
    ModelRenderer bone19;
    ModelRenderer bone20;
    ModelRenderer cube_r64;
    ModelRenderer cube_r65;
    ModelRenderer cube_r66;
    ModelRenderer bone21;
    ModelRenderer cube_r67;
    ModelRenderer cube_r68;
    ModelRenderer cube_r69;
    ModelRenderer south_controller;
    ModelRenderer cube_r70;
    ModelRenderer cube_r71;
    ModelRenderer pm;
    ModelRenderer cube_r72;
    ModelRenderer cube_r73;
    ModelRenderer portRight_r1;
    ModelRenderer portRight_r2;
    ModelRenderer portRight_r3;
    ModelRenderer portRight_r4;
    ModelRenderer keyboard;
    ModelRenderer cube_r74;
    ModelRenderer cube_r75;
    ModelRenderer west_io;
    ModelRenderer cube_r76;
    ModelRenderer cube_r77;
    ModelRenderer east_io;
    ModelRenderer cube_r78;
    ModelRenderer north_io;
    ModelRenderer cube_r79;
    ModelRenderer down;

    public ModelLargeWindGenerator() {
        textureWidth = 1024;
        textureHeight = 1024;

        doll_up = new ModelRenderer(this);
        doll_up.setRotationPoint(0.0F, 24.0F, -51.0F);
        doll_up.cubeList.add(new ModelBox(doll_up, 484, 374, 14.3358F, -769.0F, 102.0F, 1, 2, 16, 0.0F, false));
        doll_up.cubeList.add(new ModelBox(doll_up, 499, 389, 15.0429F, -769.0F, 117.7071F, 16, 2, 1, 0.0F, false));
        doll_up.cubeList.add(new ModelBox(doll_up, 484, 374, 30.75F, -769.0F, 102.0F, 1, 2, 16, 0.0F, false));
        doll_up.cubeList.add(new ModelBox(doll_up, 499, 389, 15.0429F, -769.0F, 101.2929F, 16, 2, 1, 0.0F, false));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(20.6997F, -768.0F, 96.3431F);
        doll_up.addChild(cube_r1);
        setRotation(cube_r1, 0.0F, 0.7854F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 499, 389, -8.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F, false));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(35.2855F, -768.0F, 97.7574F);
        doll_up.addChild(cube_r2);
        setRotation(cube_r2, 0.0F, 0.7854F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 499, 389, -6.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F, false));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(31.0429F, -768.0F, 118.0F);
        doll_up.addChild(cube_r3);
        setRotation(cube_r3, 0.0F, 0.7854F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 499, 389, -0.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F, false));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(22.114F, -768.0F, 110.9289F);
        doll_up.addChild(cube_r4);
        setRotation(cube_r4, 0.0F, 0.7854F, 0.0F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 499, 389, -10.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, 0.0F, 0.0F);
        doll_up.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 564, 147, 15.0F, -768.9F, 102.0F, 16, 2, 16, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 666, 210, 29.0F, -770.9F, 106.0F, 1, 3, 1, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 666, 210, 16.0F, -770.9F, 113.0F, 1, 3, 1, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 661, 205, 16.0F, -771.15F, 107.0F, 1, 1, 6, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 666, 210, 16.0F, -770.9F, 106.0F, 1, 3, 1, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 666, 210, 29.0F, -770.9F, 113.0F, 1, 3, 1, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 661, 205, 29.0F, -771.15F, 107.0F, 1, 1, 6, 0.0F, false));

        uio_up = new ModelRenderer(this);
        uio_up.setRotationPoint(-24.5F, 24.0F, -52.55F);
        uio_up.cubeList.add(new ModelBox(uio_up, 487, 377, 17.3358F, -769.0F, 102.0F, 1, 2, 13, 0.0F, false));
        uio_up.cubeList.add(new ModelBox(uio_up, 499, 389, 18.0429F, -769.0F, 114.7071F, 13, 2, 1, 0.0F, false));
        uio_up.cubeList.add(new ModelBox(uio_up, 487, 377, 30.75F, -769.0F, 102.0F, 1, 2, 13, 0.0F, false));
        uio_up.cubeList.add(new ModelBox(uio_up, 499, 389, 18.0429F, -769.0F, 101.2929F, 13, 2, 1, 0.0F, false));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(23.6997F, -768.0F, 96.3431F);
        uio_up.addChild(cube_r5);
        setRotation(cube_r5, 0.0F, 0.7854F, 0.0F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 499, 389, -8.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F, false));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(35.2855F, -768.0F, 97.7574F);
        uio_up.addChild(cube_r6);
        setRotation(cube_r6, 0.0F, 0.7854F, 0.0F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 499, 389, -6.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F, false));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(33.1642F, -768.0F, 112.8787F);
        uio_up.addChild(cube_r7);
        setRotation(cube_r7, 0.0F, 0.7854F, 0.0F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 499, 389, -3.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F, false));

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(25.114F, -768.0F, 107.9289F);
        uio_up.addChild(cube_r8);
        setRotation(cube_r8, 0.0F, 0.7854F, 0.0F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 499, 389, -10.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F, false));

        bone24 = new ModelRenderer(this);
        bone24.setRotationPoint(0.0F, 0.0F, 0.0F);
        uio_up.addChild(bone24);
        bone24.cubeList.add(new ModelBox(bone24, 472, 503, 18.0F, -768.9F, 102.0F, 13, 2, 13, 0.0F, false));

        doll_up2 = new ModelRenderer(this);
        doll_up2.setRotationPoint(-46.0F, 24.0F, -51.0F);
        doll_up2.cubeList.add(new ModelBox(doll_up2, 484, 374, 14.3358F, -769.0F, 102.0F, 1, 2, 16, 0.0F, false));
        doll_up2.cubeList.add(new ModelBox(doll_up2, 499, 389, 15.0429F, -769.0F, 117.7071F, 16, 2, 1, 0.0F, false));
        doll_up2.cubeList.add(new ModelBox(doll_up2, 484, 374, 30.75F, -769.0F, 102.0F, 1, 2, 16, 0.0F, false));
        doll_up2.cubeList.add(new ModelBox(doll_up2, 499, 389, 15.0429F, -769.0F, 101.2929F, 16, 2, 1, 0.0F, false));

        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(20.6997F, -768.0F, 96.3431F);
        doll_up2.addChild(cube_r9);
        setRotation(cube_r9, 0.0F, 0.7854F, 0.0F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 499, 389, -8.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F, false));

        cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(35.2855F, -768.0F, 97.7574F);
        doll_up2.addChild(cube_r10);
        setRotation(cube_r10, 0.0F, 0.7854F, 0.0F);
        cube_r10.cubeList.add(new ModelBox(cube_r10, 499, 389, -6.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F, false));

        cube_r11 = new ModelRenderer(this);
        cube_r11.setRotationPoint(31.0429F, -768.0F, 118.0F);
        doll_up2.addChild(cube_r11);
        setRotation(cube_r11, 0.0F, 0.7854F, 0.0F);
        cube_r11.cubeList.add(new ModelBox(cube_r11, 499, 389, -0.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F, false));

        cube_r12 = new ModelRenderer(this);
        cube_r12.setRotationPoint(22.114F, -768.0F, 110.9289F);
        doll_up2.addChild(cube_r12);
        setRotation(cube_r12, 0.0F, 0.7854F, 0.0F);
        cube_r12.cubeList.add(new ModelBox(cube_r12, 499, 389, -10.5F, -1.0F, -0.5F, 1, 2, 1, 0.0F, false));

        bone25 = new ModelRenderer(this);
        bone25.setRotationPoint(0.0F, 0.0F, 0.0F);
        doll_up2.addChild(bone25);
        bone25.cubeList.add(new ModelBox(bone25, 564, 147, 15.0F, -768.9F, 102.0F, 16, 2, 16, 0.0F, false));
        bone25.cubeList.add(new ModelBox(bone25, 666, 210, 29.0F, -770.9F, 106.0F, 1, 3, 1, 0.0F, false));
        bone25.cubeList.add(new ModelBox(bone25, 666, 210, 16.0F, -770.9F, 113.0F, 1, 3, 1, 0.0F, false));
        bone25.cubeList.add(new ModelBox(bone25, 661, 205, 16.0F, -771.15F, 107.0F, 1, 1, 6, 0.0F, false));
        bone25.cubeList.add(new ModelBox(bone25, 666, 210, 16.0F, -770.9F, 106.0F, 1, 3, 1, 0.0F, false));
        bone25.cubeList.add(new ModelBox(bone25, 666, 210, 29.0F, -770.9F, 113.0F, 1, 3, 1, 0.0F, false));
        bone25.cubeList.add(new ModelBox(bone25, 661, 205, 29.0F, -771.15F, 107.0F, 1, 1, 6, 0.0F, false));

        bolt = new ModelRenderer(this);
        bolt.setRotationPoint(0.0F, 16.0F, 0.0F);


        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(-48.0F, 0.0F, 0.0F);
        bolt.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 22.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 56.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 22.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 26.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 30.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 34.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 18.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 14.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 72.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 76.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 80.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 84.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 52.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 47.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 42.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 38.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 68.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 64.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 60.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 56.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 60.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 64.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 68.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 38.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 42.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 47.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 52.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 80.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 76.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 72.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 14.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 18.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 34.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 30.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 716, 165, 26.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));

        cube_r13 = new ModelRenderer(this);
        cube_r13.setRotationPoint(101.0F, -24.0F, 0.0F);
        bone2.addChild(cube_r13);
        setRotation(cube_r13, 0.0F, 3.1416F, 0.0F);
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 61.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 65.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 69.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 73.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 43.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 47.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 52.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 57.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 85.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 89.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 81.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 77.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 19.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 23.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 39.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 35.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 31.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));
        cube_r13.cubeList.add(new ModelBox(cube_r13, 716, 165, 27.0F, -1.0F, 36.0F, 2, 2, 2, 0.0F, false));

        cube_r14 = new ModelRenderer(this);
        cube_r14.setRotationPoint(50.0F, -24.0F, 37.0F);
        bone2.addChild(cube_r14);
        setRotation(cube_r14, 0.0F, 1.5708F, 0.0F);
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 45.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 49.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 53.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 57.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 27.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 31.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 36.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 41.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 73.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 69.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 65.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 61.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 3.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 7.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 23.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 19.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 15.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));
        cube_r14.cubeList.add(new ModelBox(cube_r14, 716, 165, 11.0F, -1.0F, 34.0F, 2, 2, 2, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 0.0F, 0.0F);
        bolt.addChild(bone);
        setRotation(bone, 0.0F, -1.5708F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 716, 165, 8.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, 12.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, 16.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, 20.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, -10.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, -6.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, -1.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, 4.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, 36.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, 32.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, 28.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, 24.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, -34.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, -30.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, -14.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, -18.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, -22.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 716, 165, -26.0F, -25.0F, 36.0F, 2, 2, 2, 0.0F, false));

        wind_turbine_head = new ModelRenderer(this);
        wind_turbine_head.setRotationPoint(-45.6569F, -771.6985F, 22.0F);
        wind_turbine_head.cubeList.add(new ModelBox(wind_turbine_head, 656, 844, 5.6569F, 29.6985F, -52.0F, 80, 76, 104, 0.0F, false));
        wind_turbine_head.cubeList.add(new ModelBox(wind_turbine_head, 966, 892, 5.1569F, 53.6985F, -36.0F, 1, 28, 28, 0.0F, false));
        wind_turbine_head.cubeList.add(new ModelBox(wind_turbine_head, 970, 840, 4.9069F, 54.6985F, -35.0F, 1, 26, 26, 0.0F, false));
        wind_turbine_head.cubeList.add(new ModelBox(wind_turbine_head, 974, 792, 4.6569F, 55.6985F, -34.0F, 1, 24, 24, 0.0F, false));
        wind_turbine_head.cubeList.add(new ModelBox(wind_turbine_head, 107, 0, 7.6569F, 29.6985F, -54.0F, 76, 76, 2, 0.0F, false));
        wind_turbine_head.cubeList.add(new ModelBox(wind_turbine_head, 107, 80, 7.6569F, 29.6985F, 52.0F, 76, 76, 2, 0.0F, false));
        wind_turbine_head.cubeList.add(new ModelBox(wind_turbine_head, 664, 0, 7.6569F, 105.6985F, -52.0F, 76, 2, 104, 0.0F, false));
        wind_turbine_head.cubeList.add(new ModelBox(wind_turbine_head, 824, 0, 19.6569F, 107.6985F, -47.0F, 50, 2, 50, 0.0F, false));
        wind_turbine_head.cubeList.add(new ModelBox(wind_turbine_head, 586, 682, 7.6569F, 27.6985F, -52.0F, 76, 2, 104, 0.0F, false));

        cube_r15 = new ModelRenderer(this);
        cube_r15.setRotationPoint(0.0F, 0.0F, 0.0F);
        wind_turbine_head.addChild(cube_r15);
        setRotation(cube_r15, 0.0F, 0.0F, -0.7854F);
        cube_r15.cubeList.add(new ModelBox(cube_r15, 765, 0, -17.0F, 25.0F, -52.0F, 3, 2, 104, 0.0F, false));

        cube_r16 = new ModelRenderer(this);
        cube_r16.setRotationPoint(-5.6569F, 83.0711F, 0.0F);
        wind_turbine_head.addChild(cube_r16);
        setRotation(cube_r16, 0.0F, 0.0F, -0.7854F);
        cube_r16.cubeList.add(new ModelBox(cube_r16, 765, 0, -8.0F, 24.0F, -52.0F, 2, 3, 104, 0.0F, false));

        cube_r17 = new ModelRenderer(this);
        cube_r17.setRotationPoint(85.6569F, 29.6985F, 0.0F);
        wind_turbine_head.addChild(cube_r17);
        setRotation(cube_r17, 0.0F, 0.0F, -0.7854F);
        cube_r17.cubeList.add(new ModelBox(cube_r17, 765, 0, -2.0F, -3.0F, -52.0F, 2, 3, 104, 0.0F, false));

        cube_r18 = new ModelRenderer(this);
        cube_r18.setRotationPoint(45.6569F, 120.1942F, -64.253F);
        wind_turbine_head.addChild(cube_r18);
        setRotation(cube_r18, -0.7854F, 0.0F, 0.0F);
        cube_r18.cubeList.add(new ModelBox(cube_r18, 477, 1014, -38.0F, -19.5F, -3.0F, 76, 2, 3, 0.0F, false));

        cube_r19 = new ModelRenderer(this);
        cube_r19.setRotationPoint(45.6569F, 30.7591F, 52.9393F);
        wind_turbine_head.addChild(cube_r19);
        setRotation(cube_r19, -0.7854F, 0.0F, 0.0F);
        cube_r19.cubeList.add(new ModelBox(cube_r19, 865, 97, -38.0F, -1.5F, -3.0F, 76, 2, 3, 0.0F, false));

        cube_r20 = new ModelRenderer(this);
        cube_r20.setRotationPoint(45.6569F, 113.1231F, 57.8891F);
        wind_turbine_head.addChild(cube_r20);
        setRotation(cube_r20, -0.7854F, 0.0F, 0.0F);
        cube_r20.cubeList.add(new ModelBox(cube_r20, 867, 102, -38.0F, -2.5F, -10.0F, 76, 3, 2, 0.0F, false));

        cube_r21 = new ModelRenderer(this);
        cube_r21.setRotationPoint(45.6569F, 29.3449F, -53.6464F);
        wind_turbine_head.addChild(cube_r21);
        setRotation(cube_r21, -0.7854F, 0.0F, 0.0F);
        cube_r21.cubeList.add(new ModelBox(cube_r21, 479, 1019, -38.0F, -2.5F, 0.0F, 76, 3, 2, 0.0F, false));

        cube_r22 = new ModelRenderer(this);
        cube_r22.setRotationPoint(-13.2028F, 67.6985F, -84.7591F);
        wind_turbine_head.addChild(cube_r22);
        setRotation(cube_r22, 0.0F, 0.7854F, 0.0F);
        cube_r22.cubeList.add(new ModelBox(cube_r22, 645, 946, -10.0F, -38.0F, 36.5F, 3, 76, 2, 0.0F, false));

        cube_r23 = new ModelRenderer(this);
        cube_r23.setRotationPoint(72.6967F, 67.6985F, 28.8977F);
        wind_turbine_head.addChild(cube_r23);
        setRotation(cube_r23, 0.0F, 0.7854F, 0.0F);
        cube_r23.cubeList.add(new ModelBox(cube_r23, 645, 946, -10.0F, -38.0F, 23.5F, 3, 76, 2, 0.0F, false));

        cube_r24 = new ModelRenderer(this);
        cube_r24.setRotationPoint(-10.3744F, 67.6985F, 34.5546F);
        wind_turbine_head.addChild(cube_r24);
        setRotation(cube_r24, 0.0F, 0.7854F, 0.0F);
        cube_r24.cubeList.add(new ModelBox(cube_r24, 635, 945, -1.0F, -38.0F, 23.5F, 2, 76, 3, 0.0F, false));

        cube_r25 = new ModelRenderer(this);
        cube_r25.setRotationPoint(84.0104F, 67.6985F, -52.2322F);
        wind_turbine_head.addChild(cube_r25);
        setRotation(cube_r25, 0.0F, 0.7854F, 0.0F);
        cube_r25.cubeList.add(new ModelBox(cube_r25, 635, 945, -1.0F, -38.0F, -1.5F, 2, 76, 3, 0.0F, false));

        cube_r26 = new ModelRenderer(this);
        cube_r26.setRotationPoint(85.6569F, 72.6985F, -22.0F);
        wind_turbine_head.addChild(cube_r26);
        setRotation(cube_r26, 0.0F, 3.1416F, 0.0F);
        cube_r26.cubeList.add(new ModelBox(cube_r26, 656, 886, -0.1F, -20.5F, -55.5F, 2, 31, 31, 0.0F, false));
        cube_r26.cubeList.add(new ModelBox(cube_r26, 974, 792, -1.0F, -12.0F, -12.0F, 1, 24, 24, 0.0F, false));
        cube_r26.cubeList.add(new ModelBox(cube_r26, 970, 840, -0.75F, -13.0F, -13.0F, 1, 26, 26, 0.0F, false));
        cube_r26.cubeList.add(new ModelBox(cube_r26, 966, 892, -0.5F, -14.0F, -14.0F, 1, 28, 28, 0.0F, false));

        cube_r27 = new ModelRenderer(this);
        cube_r27.setRotationPoint(67.2721F, 85.8995F, 0.0F);
        wind_turbine_head.addChild(cube_r27);
        setRotation(cube_r27, 0.0F, 0.0F, -0.7854F);
        cube_r27.cubeList.add(new ModelBox(cube_r27, 765, 0, -4.0F, 25.0F, -52.0F, 3, 2, 104, 0.0F, false));

        wind_power_middle = new ModelRenderer(this);
        wind_power_middle.setRotationPoint(0.0F, -27.0F, 35.25F);
        wind_power_middle.cubeList.add(new ModelBox(wind_power_middle, 704, 700, -40.0F, 19.0F, -75.25F, 80, 8, 80, 0.0F, false));

        cube_r28 = new ModelRenderer(this);
        cube_r28.setRotationPoint(-1.0F, -635.0F, -35.25F);
        wind_power_middle.addChild(cube_r28);
        setRotation(cube_r28, -0.0175F, 0.0F, 0.0175F);
        cube_r28.cubeList.add(new ModelBox(cube_r28, 832, 0, -23.975F, -2.0F, -23.975F, 48, 684, 48, 0.0F, false));

        cube_r29 = new ModelRenderer(this);
        cube_r29.setRotationPoint(-1.0F, -635.0F, -35.25F);
        wind_power_middle.addChild(cube_r29);
        setRotation(cube_r29, 0.0175F, 0.0F, -0.0175F);
        cube_r29.cubeList.add(new ModelBox(cube_r29, 832, 0, -24.025F, -2.0F, -24.025F, 48, 684, 48, 0.0F, false));

        cube_r30 = new ModelRenderer(this);
        cube_r30.setRotationPoint(-1.0F, -635.0F, -35.25F);
        wind_power_middle.addChild(cube_r30);
        setRotation(cube_r30, 0.0175F, 0.0F, 0.0175F);
        cube_r30.cubeList.add(new ModelBox(cube_r30, 832, 0, -24.0F, -2.0F, -24.0F, 48, 684, 48, 0.0F, false));

        cube_r31 = new ModelRenderer(this);
        cube_r31.setRotationPoint(-1.0F, -635.0F, -35.25F);
        wind_power_middle.addChild(cube_r31);
        setRotation(cube_r31, -0.0175F, 0.0F, -0.0175F);
        cube_r31.cubeList.add(new ModelBox(cube_r31, 832, 0, -24.0F, -2.0F, -24.0F, 48, 684, 48, 0.0F, false));

        doll2 = new ModelRenderer(this);
        doll2.setRotationPoint(-1.0F, 51.0F, -35.25F);
        wind_power_middle.addChild(doll2);
        setRotation(doll2, 0.0F, 3.1416F, 0.0F);


        cube_r32 = new ModelRenderer(this);
        cube_r32.setRotationPoint(52.1227F, -20.1119F, 35.058F);
        doll2.addChild(cube_r32);
        setRotation(cube_r32, 0.0175F, 0.0F, 0.7854F);
        cube_r32.cubeList.add(new ModelBox(cube_r32, 498, 377, -58.0F, 3.0F, -1.15F, 2, 2, 2, 0.0F, false));

        cube_r33 = new ModelRenderer(this);
        cube_r33.setRotationPoint(0.0F, -51.0F, 35.25F);
        doll2.addChild(cube_r33);
        setRotation(cube_r33, 0.0175F, 0.0F, 0.0F);
        cube_r33.cubeList.add(new ModelBox(cube_r33, 717, 170, -1.0F, -2.0F, 1.35F, 2, 2, 1, 0.0F, false));
        cube_r33.cubeList.add(new ModelBox(cube_r33, 626, 165, -9.0F, -18.0F, -1.15F, 18, 34, 2, 0.0F, false));
        cube_r33.cubeList.add(new ModelBox(cube_r33, 632, 201, -6.0F, -17.0F, 0.35F, 12, 1, 1, 0.0F, false));
        cube_r33.cubeList.add(new ModelBox(cube_r33, 632, 203, -6.0F, 14.0F, 0.35F, 12, 1, 1, 0.0F, false));
        cube_r33.cubeList.add(new ModelBox(cube_r33, 658, 201, -8.0F, -15.0F, 0.35F, 1, 28, 1, 0.0F, false));
        cube_r33.cubeList.add(new ModelBox(cube_r33, 662, 201, 7.0F, -15.0F, 0.35F, 1, 28, 1, 0.0F, false));
        cube_r33.cubeList.add(new ModelBox(cube_r33, 666, 180, -8.0F, 4.0F, 1.35F, 1, 1, 2, 0.0F, false));
        cube_r33.cubeList.add(new ModelBox(cube_r33, 667, 181, -8.0F, -6.0F, 2.35F, 1, 10, 1, 0.0F, false));
        cube_r33.cubeList.add(new ModelBox(cube_r33, 666, 180, -8.0F, -7.0F, 1.35F, 1, 1, 2, 0.0F, false));
        cube_r33.cubeList.add(new ModelBox(cube_r33, 666, 180, 7.0F, 4.0F, 1.35F, 1, 1, 2, 0.0F, false));
        cube_r33.cubeList.add(new ModelBox(cube_r33, 667, 181, 7.0F, -6.0F, 2.35F, 1, 10, 1, 0.0F, false));
        cube_r33.cubeList.add(new ModelBox(cube_r33, 666, 180, 7.0F, -7.0F, 1.35F, 1, 1, 2, 0.0F, false));
        cube_r33.cubeList.add(new ModelBox(cube_r33, 596, 165, -7.0F, -16.0F, 0.35F, 14, 30, 1, 0.0F, false));

        cube_r34 = new ModelRenderer(this);
        cube_r34.setRotationPoint(24.2245F, 0.9557F, 35.5118F);
        doll2.addChild(cube_r34);
        setRotation(cube_r34, 0.0175F, 0.0F, 0.7854F);
        cube_r34.cubeList.add(new ModelBox(cube_r34, 498, 377, -58.0F, -11.0F, -1.15F, 2, 2, 2, 0.0F, false));

        cube_r35 = new ModelRenderer(this);
        cube_r35.setRotationPoint(14.6473F, -26.6215F, 35.5118F);
        doll2.addChild(cube_r35);
        setRotation(cube_r35, 0.0175F, 0.0F, 0.7854F);
        cube_r35.cubeList.add(new ModelBox(cube_r35, 498, 377, -19.0F, -11.0F, -1.15F, 2, 2, 2, 0.0F, false));

        cube_r36 = new ModelRenderer(this);
        cube_r36.setRotationPoint(6.5455F, -47.6891F, 35.058F);
        doll2.addChild(cube_r36);
        setRotation(cube_r36, 0.0175F, 0.0F, 0.7854F);
        cube_r36.cubeList.add(new ModelBox(cube_r36, 498, 377, -19.0F, 3.0F, -1.15F, 2, 2, 2, 0.0F, false));

        cube_r37 = new ModelRenderer(this);
        cube_r37.setRotationPoint(17.476F, -44.9612F, 35.1278F);
        doll2.addChild(cube_r37);
        setRotation(cube_r37, 0.0175F, 0.0F, 0.7854F);
        cube_r37.cubeList.add(new ModelBox(cube_r37, 498, 377, -23.0F, -11.0F, -1.15F, 2, 2, 2, 0.0F, false));

        cube_r38 = new ModelRenderer(this);
        cube_r38.setRotationPoint(-28.1014F, -41.3717F, 35.6863F);
        doll2.addChild(cube_r38);
        setRotation(cube_r38, 0.0175F, 0.0F, 0.7854F);
        cube_r38.cubeList.add(new ModelBox(cube_r38, 498, 377, 16.0F, -11.0F, -1.15F, 2, 2, 2, 0.0F, false));

        cube_r39 = new ModelRenderer(this);
        cube_r39.setRotationPoint(9.374F, -54.8591F, 34.8835F);
        doll2.addChild(cube_r39);
        setRotation(cube_r39, 0.0175F, 0.0F, 0.7854F);
        cube_r39.cubeList.add(new ModelBox(cube_r39, 498, 377, -23.0F, 3.0F, -1.15F, 2, 2, 2, 0.0F, false));

        cube_r40 = new ModelRenderer(this);
        cube_r40.setRotationPoint(-0.2034F, -51.2697F, 35.442F);
        doll2.addChild(cube_r40);
        setRotation(cube_r40, 0.0175F, 0.0F, 0.7854F);
        cube_r40.cubeList.add(new ModelBox(cube_r40, 498, 377, 16.0F, 3.0F, -1.15F, 2, 2, 2, 0.0F, false));

        cube_r41 = new ModelRenderer(this);
        cube_r41.setRotationPoint(0.586F, -70.5828F, 34.9184F);
        doll2.addChild(cube_r41);
        setRotation(cube_r41, 0.0175F, 0.0F, 0.0F);
        cube_r41.cubeList.add(new ModelBox(cube_r41, 570, 336, -11.0F, 3.0F, -1.1834F, 2, 10, 2, 0.0F, false));

        cube_r42 = new ModelRenderer(this);
        cube_r42.setRotationPoint(4.5858F, -49.4147F, 35.3024F);
        doll2.addChild(cube_r42);
        setRotation(cube_r42, 0.0175F, 0.0F, 0.0F);
        cube_r42.cubeList.add(new ModelBox(cube_r42, 570, 336, -15.0F, 3.0F, -1.1924F, 2, 10, 2, 0.0F, false));

        cube_r43 = new ModelRenderer(this);
        cube_r43.setRotationPoint(-6.5858F, -70.5826F, 34.8835F);
        doll2.addChild(cube_r43);
        setRotation(cube_r43, 0.0175F, 0.0F, 0.0F);
        cube_r43.cubeList.add(new ModelBox(cube_r43, 578, 336, 15.0F, 3.0F, -1.15F, 2, 10, 2, 0.0F, false));

        cube_r44 = new ModelRenderer(this);
        cube_r44.setRotationPoint(-2.586F, -49.4145F, 35.2675F);
        doll2.addChild(cube_r44);
        setRotation(cube_r44, 0.0175F, 0.0F, 0.0F);
        cube_r44.cubeList.add(new ModelBox(cube_r44, 578, 336, 11.0F, 3.0F, -1.15F, 2, 10, 2, 0.0F, false));

        doll = new ModelRenderer(this);
        doll.setRotationPoint(0.0F, -51.0F, 35.5F);
        doll2.addChild(doll);


        cube_r45 = new ModelRenderer(this);
        cube_r45.setRotationPoint(0.0F, 0.0F, 0.0F);
        doll.addChild(cube_r45);
        setRotation(cube_r45, 0.0175F, 0.0F, 0.0F);
        cube_r45.cubeList.add(new ModelBox(cube_r45, 675, 167, -1.0F, -2.0F, 2.1F, 2, 2, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 677, 172, -4.0F, -2.0F, 2.1F, 1, 2, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 672, 178, -3.0F, -4.0F, 2.1F, 2, 1, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 666, 168, 1.0F, -2.0F, 1.85F, 1, 2, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 670, 167, -2.0F, -2.0F, 1.85F, 1, 2, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 672, 176, -3.0F, 1.0F, 2.1F, 2, 1, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 671, 165, -2.0F, 2.0F, 2.1F, 4, 1, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 666, 182, 1.0F, 1.0F, 2.1F, 2, 1, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 666, 176, 2.0F, 0.0F, 2.1F, 2, 1, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 666, 165, 3.0F, -2.0F, 2.1F, 1, 2, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 666, 180, -4.0F, -3.0F, 2.1F, 2, 1, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 666, 163, -4.0F, 0.0F, 2.1F, 2, 1, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 666, 174, -2.0F, -3.0F, 1.85F, 4, 1, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 666, 172, -2.0F, 0.0F, 1.85F, 4, 1, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 675, 170, 2.0F, -3.0F, 2.1F, 2, 1, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 666, 178, 1.0F, -4.0F, 2.1F, 2, 1, 1, 0.0F, false));
        cube_r45.cubeList.add(new ModelBox(cube_r45, 671, 165, -2.0F, -5.0F, 2.1F, 4, 1, 1, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, -704.0F, 76.3F);
        bone3.cubeList.add(new ModelBox(bone3, 264, 63, -8.0F, -8.0F, -0.95F, 16, 16, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 716, 169, -1.0F, -1.0F, -2.05F, 2, 2, 2, 0.0F, false));

        fans = new ModelRenderer(this);
        fans.setRotationPoint(0.0F, -704.0F, -84.6F);
        fans.cubeList.add(new ModelBox(fans, 667, 224, -12.5F, -12.5F, 14.2F, 25, 25, 2, 0.0F, false));

        fan0 = new ModelRenderer(this);
        fan0.setRotationPoint(16.0F, 0.0F, 19.0F);
        fans.addChild(fan0);


        fan_blade_tip2 = new ModelRenderer(this);
        fan_blade_tip2.setRotationPoint(-16.0364F, 0.001F, 7.0985F);
        fan0.addChild(fan_blade_tip2);


        a16 = new ModelRenderer(this);
        a16.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a16);
        a16.cubeList.add(new ModelBox(a16, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r1 = new ModelRenderer(this);
        a2_r1.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a16.addChild(a2_r1);
        setRotation(a2_r1, 2.7489F, 0.0F, 0.0F);
        a2_r1.cubeList.add(new ModelBox(a2_r1, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a17 = new ModelRenderer(this);
        a17.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a17);
        setRotation(a17, 0.0F, 0.0F, 0.4189F);
        a17.cubeList.add(new ModelBox(a17, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r2 = new ModelRenderer(this);
        a2_r2.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a17.addChild(a2_r2);
        setRotation(a2_r2, 2.7489F, 0.0F, 0.0F);
        a2_r2.cubeList.add(new ModelBox(a2_r2, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a18 = new ModelRenderer(this);
        a18.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a18);
        setRotation(a18, 0.0F, 0.0F, -0.4189F);
        a18.cubeList.add(new ModelBox(a18, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r3 = new ModelRenderer(this);
        a2_r3.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a18.addChild(a2_r3);
        setRotation(a2_r3, 2.7489F, 0.0F, 0.0F);
        a2_r3.cubeList.add(new ModelBox(a2_r3, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a19 = new ModelRenderer(this);
        a19.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a19);
        setRotation(a19, 0.0F, 0.0F, 0.8378F);
        a19.cubeList.add(new ModelBox(a19, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r4 = new ModelRenderer(this);
        a2_r4.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a19.addChild(a2_r4);
        setRotation(a2_r4, 2.7489F, 0.0F, 0.0F);
        a2_r4.cubeList.add(new ModelBox(a2_r4, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a20 = new ModelRenderer(this);
        a20.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a20);
        setRotation(a20, 0.0F, 0.0F, -0.8378F);
        a20.cubeList.add(new ModelBox(a20, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r5 = new ModelRenderer(this);
        a2_r5.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a20.addChild(a2_r5);
        setRotation(a2_r5, 2.7489F, 0.0F, 0.0F);
        a2_r5.cubeList.add(new ModelBox(a2_r5, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a21 = new ModelRenderer(this);
        a21.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a21);
        setRotation(a21, 0.0F, 0.0F, 1.2566F);
        a21.cubeList.add(new ModelBox(a21, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r6 = new ModelRenderer(this);
        a2_r6.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a21.addChild(a2_r6);
        setRotation(a2_r6, 2.7489F, 0.0F, 0.0F);
        a2_r6.cubeList.add(new ModelBox(a2_r6, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a22 = new ModelRenderer(this);
        a22.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a22);
        setRotation(a22, 0.0F, 0.0F, -1.2566F);
        a22.cubeList.add(new ModelBox(a22, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r7 = new ModelRenderer(this);
        a2_r7.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a22.addChild(a2_r7);
        setRotation(a2_r7, 2.7489F, 0.0F, 0.0F);
        a2_r7.cubeList.add(new ModelBox(a2_r7, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a23 = new ModelRenderer(this);
        a23.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a23);
        setRotation(a23, 0.0F, 0.0F, 1.6755F);
        a23.cubeList.add(new ModelBox(a23, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r8 = new ModelRenderer(this);
        a2_r8.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a23.addChild(a2_r8);
        setRotation(a2_r8, 2.7489F, 0.0F, 0.0F);
        a2_r8.cubeList.add(new ModelBox(a2_r8, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a24 = new ModelRenderer(this);
        a24.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a24);
        setRotation(a24, 0.0F, 0.0F, -1.6755F);
        a24.cubeList.add(new ModelBox(a24, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r9 = new ModelRenderer(this);
        a2_r9.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a24.addChild(a2_r9);
        setRotation(a2_r9, 2.7489F, 0.0F, 0.0F);
        a2_r9.cubeList.add(new ModelBox(a2_r9, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a25 = new ModelRenderer(this);
        a25.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a25);
        setRotation(a25, 0.0F, 0.0F, 2.0944F);
        a25.cubeList.add(new ModelBox(a25, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r10 = new ModelRenderer(this);
        a2_r10.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a25.addChild(a2_r10);
        setRotation(a2_r10, 2.7489F, 0.0F, 0.0F);
        a2_r10.cubeList.add(new ModelBox(a2_r10, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a26 = new ModelRenderer(this);
        a26.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a26);
        setRotation(a26, 0.0F, 0.0F, -2.0944F);
        a26.cubeList.add(new ModelBox(a26, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r11 = new ModelRenderer(this);
        a2_r11.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a26.addChild(a2_r11);
        setRotation(a2_r11, 2.7489F, 0.0F, 0.0F);
        a2_r11.cubeList.add(new ModelBox(a2_r11, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a27 = new ModelRenderer(this);
        a27.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a27);
        setRotation(a27, 0.0F, 0.0F, 2.5133F);
        a27.cubeList.add(new ModelBox(a27, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r12 = new ModelRenderer(this);
        a2_r12.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a27.addChild(a2_r12);
        setRotation(a2_r12, 2.7489F, 0.0F, 0.0F);
        a2_r12.cubeList.add(new ModelBox(a2_r12, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a28 = new ModelRenderer(this);
        a28.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a28);
        setRotation(a28, 0.0F, 0.0F, -2.5133F);
        a28.cubeList.add(new ModelBox(a28, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r13 = new ModelRenderer(this);
        a2_r13.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a28.addChild(a2_r13);
        setRotation(a2_r13, 2.7489F, 0.0F, 0.0F);
        a2_r13.cubeList.add(new ModelBox(a2_r13, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a29 = new ModelRenderer(this);
        a29.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a29);
        setRotation(a29, 0.0F, 0.0F, 2.9322F);
        a29.cubeList.add(new ModelBox(a29, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r14 = new ModelRenderer(this);
        a2_r14.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a29.addChild(a2_r14);
        setRotation(a2_r14, 2.7489F, 0.0F, 0.0F);
        a2_r14.cubeList.add(new ModelBox(a2_r14, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a30 = new ModelRenderer(this);
        a30.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a30);
        setRotation(a30, 0.0F, 0.0F, -0.4189F);
        a30.cubeList.add(new ModelBox(a30, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r15 = new ModelRenderer(this);
        a2_r15.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a30.addChild(a2_r15);
        setRotation(a2_r15, 2.7489F, 0.0F, 0.0F);
        a2_r15.cubeList.add(new ModelBox(a2_r15, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        a31 = new ModelRenderer(this);
        a31.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip2.addChild(a31);
        setRotation(a31, 0.0F, 0.0F, -2.9322F);
        a31.cubeList.add(new ModelBox(a31, 874, 42, -4.0F, -19.0391F, -11.8785F, 8, 22, 6, 0.0F, false));

        a2_r16 = new ModelRenderer(this);
        a2_r16.setRotationPoint(1.0F, -14.6575F, -10.4462F);
        a31.addChild(a2_r16);
        setRotation(a2_r16, 2.7489F, 0.0F, 0.0F);
        a2_r16.cubeList.add(new ModelBox(a2_r16, 874, 42, -5.0F, 3.5F, -3.0F, 8, 4, 6, 0.0F, false));

        fan_blade_tip = new ModelRenderer(this);
        fan_blade_tip.setRotationPoint(-16.0364F, 0.001F, 7.0985F);
        fan0.addChild(fan_blade_tip);


        a1 = new ModelRenderer(this);
        a1.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a1);
        a1.cubeList.add(new ModelBox(a1, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r17 = new ModelRenderer(this);
        a2_r17.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a1.addChild(a2_r17);
        setRotation(a2_r17, 1.9635F, 0.0F, 0.0F);
        a2_r17.cubeList.add(new ModelBox(a2_r17, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a2 = new ModelRenderer(this);
        a2.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a2);
        setRotation(a2, 0.0F, 0.0F, 0.4189F);
        a2.cubeList.add(new ModelBox(a2, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r18 = new ModelRenderer(this);
        a2_r18.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a2.addChild(a2_r18);
        setRotation(a2_r18, 1.9635F, 0.0F, 0.0F);
        a2_r18.cubeList.add(new ModelBox(a2_r18, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a3 = new ModelRenderer(this);
        a3.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a3);
        setRotation(a3, 0.0F, 0.0F, 0.8378F);
        a3.cubeList.add(new ModelBox(a3, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r19 = new ModelRenderer(this);
        a2_r19.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a3.addChild(a2_r19);
        setRotation(a2_r19, 1.9635F, 0.0F, 0.0F);
        a2_r19.cubeList.add(new ModelBox(a2_r19, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a4 = new ModelRenderer(this);
        a4.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a4);
        setRotation(a4, 0.0F, 0.0F, 1.2566F);
        a4.cubeList.add(new ModelBox(a4, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r20 = new ModelRenderer(this);
        a2_r20.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a4.addChild(a2_r20);
        setRotation(a2_r20, 1.9635F, 0.0F, 0.0F);
        a2_r20.cubeList.add(new ModelBox(a2_r20, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a5 = new ModelRenderer(this);
        a5.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a5);
        setRotation(a5, 0.0F, 0.0F, 1.6755F);
        a5.cubeList.add(new ModelBox(a5, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r21 = new ModelRenderer(this);
        a2_r21.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a5.addChild(a2_r21);
        setRotation(a2_r21, 1.9635F, 0.0F, 0.0F);
        a2_r21.cubeList.add(new ModelBox(a2_r21, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a6 = new ModelRenderer(this);
        a6.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a6);
        setRotation(a6, 0.0F, 0.0F, 2.0944F);
        a6.cubeList.add(new ModelBox(a6, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r22 = new ModelRenderer(this);
        a2_r22.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a6.addChild(a2_r22);
        setRotation(a2_r22, 1.9635F, 0.0F, 0.0F);
        a2_r22.cubeList.add(new ModelBox(a2_r22, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a7 = new ModelRenderer(this);
        a7.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a7);
        setRotation(a7, 0.0F, 0.0F, 2.5133F);
        a7.cubeList.add(new ModelBox(a7, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r23 = new ModelRenderer(this);
        a2_r23.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a7.addChild(a2_r23);
        setRotation(a2_r23, 1.9635F, 0.0F, 0.0F);
        a2_r23.cubeList.add(new ModelBox(a2_r23, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a8 = new ModelRenderer(this);
        a8.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a8);
        setRotation(a8, 0.0F, 0.0F, 2.9322F);
        a8.cubeList.add(new ModelBox(a8, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r24 = new ModelRenderer(this);
        a2_r24.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a8.addChild(a2_r24);
        setRotation(a2_r24, 1.9635F, 0.0F, 0.0F);
        a2_r24.cubeList.add(new ModelBox(a2_r24, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a9 = new ModelRenderer(this);
        a9.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a9);
        setRotation(a9, 0.0F, 0.0F, -2.9322F);
        a9.cubeList.add(new ModelBox(a9, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r25 = new ModelRenderer(this);
        a2_r25.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a9.addChild(a2_r25);
        setRotation(a2_r25, 1.9635F, 0.0F, 0.0F);
        a2_r25.cubeList.add(new ModelBox(a2_r25, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a10 = new ModelRenderer(this);
        a10.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a10);
        setRotation(a10, 0.0F, 0.0F, -2.5133F);
        a10.cubeList.add(new ModelBox(a10, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r26 = new ModelRenderer(this);
        a2_r26.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a10.addChild(a2_r26);
        setRotation(a2_r26, 1.9635F, 0.0F, 0.0F);
        a2_r26.cubeList.add(new ModelBox(a2_r26, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a11 = new ModelRenderer(this);
        a11.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a11);
        setRotation(a11, 0.0F, 0.0F, -2.0944F);
        a11.cubeList.add(new ModelBox(a11, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r27 = new ModelRenderer(this);
        a2_r27.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a11.addChild(a2_r27);
        setRotation(a2_r27, 1.9635F, 0.0F, 0.0F);
        a2_r27.cubeList.add(new ModelBox(a2_r27, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a12 = new ModelRenderer(this);
        a12.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a12);
        setRotation(a12, 0.0F, 0.0F, -1.6755F);
        a12.cubeList.add(new ModelBox(a12, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r28 = new ModelRenderer(this);
        a2_r28.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a12.addChild(a2_r28);
        setRotation(a2_r28, 1.9635F, 0.0F, 0.0F);
        a2_r28.cubeList.add(new ModelBox(a2_r28, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a13 = new ModelRenderer(this);
        a13.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a13);
        setRotation(a13, 0.0F, 0.0F, -1.2566F);
        a13.cubeList.add(new ModelBox(a13, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r29 = new ModelRenderer(this);
        a2_r29.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a13.addChild(a2_r29);
        setRotation(a2_r29, 1.9635F, 0.0F, 0.0F);
        a2_r29.cubeList.add(new ModelBox(a2_r29, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a14 = new ModelRenderer(this);
        a14.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a14);
        setRotation(a14, 0.0F, 0.0F, -0.8378F);
        a14.cubeList.add(new ModelBox(a14, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r30 = new ModelRenderer(this);
        a2_r30.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a14.addChild(a2_r30);
        setRotation(a2_r30, 1.9635F, 0.0F, 0.0F);
        a2_r30.cubeList.add(new ModelBox(a2_r30, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        a15 = new ModelRenderer(this);
        a15.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_tip.addChild(a15);
        setRotation(a15, 0.0F, 0.0F, -0.4189F);
        a15.cubeList.add(new ModelBox(a15, 878, 46, -5.0F, -22.7346F, -10.3477F, 10, 23, 2, 0.0F, false));

        a2_r31 = new ModelRenderer(this);
        a2_r31.setRotationPoint(0.0F, -21.6522F, -7.7346F);
        a15.addChild(a2_r31);
        setRotation(a2_r31, 1.9635F, 0.0F, 0.0F);
        a2_r31.cubeList.add(new ModelBox(a2_r31, 878, 46, -4.5F, -2.0F, 0.0F, 9, 2, 2, 0.0F, false));

        fan_blade_top = new ModelRenderer(this);
        fan_blade_top.setRotationPoint(-16.0364F, 0.001F, 7.0985F);
        fan0.addChild(fan_blade_top);
        fan_blade_top.cubeList.add(new ModelBox(fan_blade_top, 862, 30, -5.0F, -23.5F, -8.5F, 10, 24, 18, 0.0F, false));

        a15_r1 = new ModelRenderer(this);
        a15_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_top.addChild(a15_r1);
        setRotation(a15_r1, 0.0F, 0.0F, -0.4189F);
        a15_r1.cubeList.add(new ModelBox(a15_r1, 862, 30, -5.0F, -23.5F, -8.5F, 10, 24, 18, 0.0F, false));

        a14_r1 = new ModelRenderer(this);
        a14_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_top.addChild(a14_r1);
        setRotation(a14_r1, 0.0F, 0.0F, -0.8378F);
        a14_r1.cubeList.add(new ModelBox(a14_r1, 862, 30, -5.0F, -23.5F, -8.5F, 10, 24, 18, 0.0F, false));

        a13_r1 = new ModelRenderer(this);
        a13_r1.setRotationPoint(-4.1117F, -1.3459F, 7.5249F);
        fan_blade_top.addChild(a13_r1);
        setRotation(a13_r1, 3.1416F, 0.0F, -1.2566F);
        a13_r1.cubeList.add(new ModelBox(a13_r1, 631, 35, -7.0F, 20.0F, 0.5F, 1, 1, 1, 0.0F, false));
        a13_r1.cubeList.add(new ModelBox(a13_r1, 631, 35, -7.0F, 22.0F, 0.5F, 1, 1, 1, 0.0F, false));
        a13_r1.cubeList.add(new ModelBox(a13_r1, 631, 35, -7.0F, 24.0F, 0.5F, 1, 1, 1, 0.0F, false));
        a13_r1.cubeList.add(new ModelBox(a13_r1, 631, 35, 6.0F, 20.0F, 0.5F, 1, 1, 1, 0.0F, false));
        a13_r1.cubeList.add(new ModelBox(a13_r1, 631, 35, 6.0F, 22.0F, 0.5F, 1, 1, 1, 0.0F, false));
        a13_r1.cubeList.add(new ModelBox(a13_r1, 631, 35, 6.0F, 24.0F, 0.5F, 1, 1, 1, 0.0F, false));
        a13_r1.cubeList.add(new ModelBox(a13_r1, 220, 854, -8.0F, 2.0F, -1.5F, 16, 24, 2, 0.0F, false));

        a13_r2 = new ModelRenderer(this);
        a13_r2.setRotationPoint(-4.1117F, -1.3459F, 7.5249F);
        fan_blade_top.addChild(a13_r2);
        setRotation(a13_r2, 0.3491F, 0.0F, -1.2566F);
        a13_r2.cubeList.add(new ModelBox(a13_r2, 860, 28, -5.0F, -23.5F, -8.5F, 10, 24, 20, 0.0F, false));

        a12_r1 = new ModelRenderer(this);
        a12_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_top.addChild(a12_r1);
        setRotation(a12_r1, 0.0F, 0.0F, -1.6755F);
        a12_r1.cubeList.add(new ModelBox(a12_r1, 862, 30, -5.0F, -23.5F, -8.5F, 10, 24, 18, 0.0F, false));

        a11_r1 = new ModelRenderer(this);
        a11_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_top.addChild(a11_r1);
        setRotation(a11_r1, 0.0F, 0.0F, -2.0944F);
        a11_r1.cubeList.add(new ModelBox(a11_r1, 862, 30, -5.0F, -23.5F, -8.5F, 10, 24, 18, 0.0F, false));

        a10_r1 = new ModelRenderer(this);
        a10_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_top.addChild(a10_r1);
        setRotation(a10_r1, 0.0F, 0.0F, -2.5133F);
        a10_r1.cubeList.add(new ModelBox(a10_r1, 862, 30, -5.0F, -23.5F, -8.5F, 10, 24, 18, 0.0F, false));

        a9_r1 = new ModelRenderer(this);
        a9_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_top.addChild(a9_r1);
        setRotation(a9_r1, 0.0F, 0.0F, -2.9322F);
        a9_r1.cubeList.add(new ModelBox(a9_r1, 862, 30, -5.0F, -23.5F, -8.5F, 10, 24, 18, 0.0F, false));

        a8_r1 = new ModelRenderer(this);
        a8_r1.setRotationPoint(1.2F, 5.6002F, -22.2499F);
        fan_blade_top.addChild(a8_r1);
        setRotation(a8_r1, 3.1416F, 0.0F, 2.9322F);
        a8_r1.cubeList.add(new ModelBox(a8_r1, 596, 93, -7.0F, 18.5F, -29.0F, 1, 1, 1, 0.0F, false));
        a8_r1.cubeList.add(new ModelBox(a8_r1, 596, 93, -7.0F, 20.5F, -29.0F, 1, 1, 1, 0.0F, false));
        a8_r1.cubeList.add(new ModelBox(a8_r1, 596, 93, -7.0F, 22.5F, -29.0F, 1, 1, 1, 0.0F, false));
        a8_r1.cubeList.add(new ModelBox(a8_r1, 596, 93, 6.0F, 18.5F, -29.0F, 1, 1, 1, 0.0F, false));
        a8_r1.cubeList.add(new ModelBox(a8_r1, 596, 93, 6.0F, 20.5F, -29.0F, 1, 1, 1, 0.0F, false));
        a8_r1.cubeList.add(new ModelBox(a8_r1, 596, 93, 6.0F, 22.5F, -29.0F, 1, 1, 1, 0.0F, false));
        a8_r1.cubeList.add(new ModelBox(a8_r1, 204, 854, -8.0F, 0.5F, -31.0F, 16, 24, 2, 0.0F, false));

        a8_r2 = new ModelRenderer(this);
        a8_r2.setRotationPoint(0.9087F, 4.2299F, 7.5249F);
        fan_blade_top.addChild(a8_r2);
        setRotation(a8_r2, 0.3491F, 0.0F, 2.9322F);
        a8_r2.cubeList.add(new ModelBox(a8_r2, 860, 28, -5.0F, -23.5F, -8.5F, 10, 24, 20, 0.0F, false));

        a7_r1 = new ModelRenderer(this);
        a7_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_top.addChild(a7_r1);
        setRotation(a7_r1, 0.0F, 0.0F, 2.5133F);
        a7_r1.cubeList.add(new ModelBox(a7_r1, 862, 30, -5.0F, -23.5F, -8.5F, 10, 24, 18, 0.0F, false));

        a6_r1 = new ModelRenderer(this);
        a6_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_top.addChild(a6_r1);
        setRotation(a6_r1, 0.0F, 0.0F, 2.0944F);
        a6_r1.cubeList.add(new ModelBox(a6_r1, 862, 30, -5.0F, -23.5F, -8.5F, 10, 24, 18, 0.0F, false));

        a5_r1 = new ModelRenderer(this);
        a5_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_top.addChild(a5_r1);
        setRotation(a5_r1, 0.0F, 0.0F, 1.6755F);
        a5_r1.cubeList.add(new ModelBox(a5_r1, 862, 30, -5.0F, -23.5F, -8.5F, 10, 24, 18, 0.0F, false));

        a4_r1 = new ModelRenderer(this);
        a4_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_top.addChild(a4_r1);
        setRotation(a4_r1, 0.0F, 0.0F, 1.2566F);
        a4_r1.cubeList.add(new ModelBox(a4_r1, 862, 30, -5.0F, -23.5F, -8.5F, 10, 24, 18, 0.0F, false));

        a3_r1 = new ModelRenderer(this);
        a3_r1.setRotationPoint(15.4544F, -13.9279F, -2.302F);
        fan_blade_top.addChild(a3_r1);
        setRotation(a3_r1, 1.5272F, 0.0F, 0.8378F);
        a3_r1.cubeList.add(new ModelBox(a3_r1, 636, 67, -7.0F, 8.0F, 4.25F, 1, 1, 1, 0.0F, false));
        a3_r1.cubeList.add(new ModelBox(a3_r1, 636, 67, -7.0F, 8.0F, 6.25F, 1, 1, 1, 0.0F, false));
        a3_r1.cubeList.add(new ModelBox(a3_r1, 636, 67, -7.0F, 8.0F, 8.25F, 1, 1, 1, 0.0F, false));
        a3_r1.cubeList.add(new ModelBox(a3_r1, 636, 67, 6.0F, 8.0F, 4.25F, 1, 1, 1, 0.0F, false));
        a3_r1.cubeList.add(new ModelBox(a3_r1, 636, 67, 6.0F, 8.0F, 6.25F, 1, 1, 1, 0.0F, false));
        a3_r1.cubeList.add(new ModelBox(a3_r1, 636, 67, 6.0F, 8.0F, 8.25F, 1, 1, 1, 0.0F, false));
        a3_r1.cubeList.add(new ModelBox(a3_r1, 207, 868, -8.0F, 9.0F, -9.75F, 16, 2, 20, 0.0F, false));

        a3_r2 = new ModelRenderer(this);
        a3_r2.setRotationPoint(18.3936F, -17.5703F, -9.3971F);
        fan_blade_top.addChild(a3_r2);
        setRotation(a3_r2, 0.3491F, 0.0F, 0.8378F);
        a3_r2.cubeList.add(new ModelBox(a3_r2, 860, 28, -4.2599F, 2.1228F, 0.0F, 10, 24, 20, 0.0F, false));

        a2_r32 = new ModelRenderer(this);
        a2_r32.setRotationPoint(0.0F, 0.0F, 0.0F);
        fan_blade_top.addChild(a2_r32);
        setRotation(a2_r32, 0.0F, 0.0F, 0.4189F);
        a2_r32.cubeList.add(new ModelBox(a2_r32, 862, 30, -5.0F, -23.5F, -8.5F, 10, 24, 18, 0.0F, false));

        fan1 = new ModelRenderer(this);
        fan1.setRotationPoint(0.0F, 0.0F, 27.0F);
        fans.addChild(fan1);
        setRotation(fan1, 0.0F, 0.0436F, 0.0F);
        fan1.cubeList.add(new ModelBox(fan1, 468, 0, -8.0F, -336.0F, -6.0F, 16, 320, 8, 0.0F, false));

        cube_r46 = new ModelRenderer(this);
        cube_r46.setRotationPoint(-16.0F, -16.0F, -1.0F);
        fan1.addChild(cube_r46);
        setRotation(cube_r46, 0.0F, 0.0F, 0.0262F);
        cube_r46.cubeList.add(new ModelBox(cube_r46, 872, 40, 0.0F, -305.0F, -4.65F, 15, 305, 8, 0.0F, false));

        cube_r47 = new ModelRenderer(this);
        cube_r47.setRotationPoint(-4.0F, -20.0F, 0.0F);
        fan1.addChild(cube_r47);
        setRotation(cube_r47, 0.0F, 0.0F, -0.2182F);
        cube_r47.cubeList.add(new ModelBox(cube_r47, 870, 38, -12.5F, -6.0F, -7.0F, 28, 11, 10, 0.0F, false));

        fan2 = new ModelRenderer(this);
        fan2.setRotationPoint(0.0F, 0.0F, 27.0F);
        fans.addChild(fan2);
        setRotation(fan2, 0.0F, 0.0436F, 2.0944F);
        fan2.cubeList.add(new ModelBox(fan2, 468, 0, -8.0F, -336.0F, -5.0F, 16, 328, 8, 0.0F, false));

        cube_r48 = new ModelRenderer(this);
        cube_r48.setRotationPoint(-16.0F, -8.0F, -1.0F);
        fan2.addChild(cube_r48);
        setRotation(cube_r48, 0.0F, 0.0F, 0.0262F);
        cube_r48.cubeList.add(new ModelBox(cube_r48, 872, 40, 0.0F, -313.0F, -3.65F, 15, 313, 8, 0.0F, false));

        cube_r49 = new ModelRenderer(this);
        cube_r49.setRotationPoint(-4.0F, -20.5F, 0.0F);
        fan2.addChild(cube_r49);
        setRotation(cube_r49, 0.0F, 0.0F, -0.2182F);
        cube_r49.cubeList.add(new ModelBox(cube_r49, 870, 38, -12.5F, -5.5F, -6.0F, 28, 11, 10, 0.0F, false));

        fan3 = new ModelRenderer(this);
        fan3.setRotationPoint(0.0F, 0.0F, 27.0F);
        fans.addChild(fan3);
        setRotation(fan3, 0.0F, 0.0436F, -2.0944F);
        fan3.cubeList.add(new ModelBox(fan3, 468, 0, -8.0F, -336.0F, -5.0F, 16, 332, 8, 0.0F, false));

        cube_r50 = new ModelRenderer(this);
        cube_r50.setRotationPoint(-16.0F, -4.0F, -1.0F);
        fan3.addChild(cube_r50);
        setRotation(cube_r50, 0.0F, 0.0F, 0.0262F);
        cube_r50.cubeList.add(new ModelBox(cube_r50, 872, 40, 0.0F, -317.0F, -3.65F, 15, 317, 8, 0.0F, false));

        cube_r51 = new ModelRenderer(this);
        cube_r51.setRotationPoint(-3.0F, -20.5F, 0.0F);
        fan3.addChild(cube_r51);
        setRotation(cube_r51, 0.0F, 0.0F, -0.2182F);
        cube_r51.cubeList.add(new ModelBox(cube_r51, 870, 38, -13.5F, -5.5F, -6.0F, 28, 11, 10, 0.0F, false));

        fan_blade_mid = new ModelRenderer(this);
        fan_blade_mid.setRotationPoint(0.0F, 728.0F, 84.6F);
        fans.addChild(fan_blade_mid);


        a32 = new ModelRenderer(this);
        a32.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a32);
        a32.cubeList.add(new ModelBox(a32, 666, 165, -7.0074F, -32.9332F, 19.0F, 14, 42, 15, 0.0F, false));
        a32.cubeList.add(new ModelBox(a32, 219, 835, -7.5F, -35.3F, 32.65F, 15, 45, 3, 0.0F, false));

        bone23 = new ModelRenderer(this);
        bone23.setRotationPoint(0.0F, 0.0F, 35.15F);
        a32.addChild(bone23);
        setRotation(bone23, 0.0F, 0.0F, -3.1416F);
        bone23.cubeList.add(new ModelBox(bone23, 717, 167, 4.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone23.cubeList.add(new ModelBox(bone23, 717, 167, 1.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone23.cubeList.add(new ModelBox(bone23, 717, 167, -2.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone23.cubeList.add(new ModelBox(bone23, 717, 167, -5.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));

        a33 = new ModelRenderer(this);
        a33.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a33);
        setRotation(a33, 0.0F, 0.0F, 0.4189F);
        a33.cubeList.add(new ModelBox(a33, 666, 165, -7.007F, -32.9301F, 19.0F, 14, 42, 15, 0.0F, false));
        a33.cubeList.add(new ModelBox(a33, 219, 835, -7.5F, -35.3F, 32.65F, 15, 45, 3, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, 0.0F, 35.15F);
        a33.addChild(bone5);
        setRotation(bone5, 0.0F, 0.0F, -3.1416F);
        bone5.cubeList.add(new ModelBox(bone5, 717, 167, 4.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 717, 167, 1.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 717, 167, -2.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 717, 167, -5.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));

        a34 = new ModelRenderer(this);
        a34.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a34);
        setRotation(a34, 0.0F, 0.0F, -0.4189F);
        a34.cubeList.add(new ModelBox(a34, 666, 165, -7.0064F, -32.9361F, 19.0F, 14, 42, 15, 0.0F, false));
        a34.cubeList.add(new ModelBox(a34, 219, 835, -7.5F, -35.3F, 32.65F, 15, 45, 3, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, 0.0F, 47.15F);
        a34.addChild(bone6);
        setRotation(bone6, 0.0F, 0.0F, -3.1416F);
        bone6.cubeList.add(new ModelBox(bone6, 717, 167, 4.0F, 33.45F, -15.5F, 1, 1, 1, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 717, 167, 1.0F, 33.45F, -15.5F, 1, 1, 1, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 717, 167, -2.0F, 33.45F, -15.5F, 1, 1, 1, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 717, 167, -5.0F, 33.45F, -15.5F, 1, 1, 1, 0.0F, false));

        a35 = new ModelRenderer(this);
        a35.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a35);
        setRotation(a35, 0.0F, 0.0F, 0.8378F);
        a35.cubeList.add(new ModelBox(a35, 666, 165, -7.0055F, -32.9275F, 19.0F, 14, 42, 15, 0.0F, false));
        a35.cubeList.add(new ModelBox(a35, 219, 835, -7.5F, -35.3F, 32.65F, 15, 45, 3, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, 0.0F, 35.15F);
        a35.addChild(bone7);
        setRotation(bone7, 0.0F, 0.0F, -3.1416F);
        bone7.cubeList.add(new ModelBox(bone7, 717, 167, 4.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 717, 167, 1.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 717, 167, -2.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 717, 167, -5.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));

        a36 = new ModelRenderer(this);
        a36.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a36);
        setRotation(a36, 0.0F, 0.0F, -0.8378F);
        a36.cubeList.add(new ModelBox(a36, 666, 165, -7.0044F, -32.9384F, 19.0F, 14, 42, 15, 0.0F, false));
        a36.cubeList.add(new ModelBox(a36, 219, 835, -7.5F, -35.3F, 32.65F, 15, 45, 3, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.5F, -0.4F, 30.15F);
        a36.addChild(bone8);
        setRotation(bone8, 0.0F, 0.0F, -3.1416F);
        bone8.cubeList.add(new ModelBox(bone8, 695, 177, 4.5F, 33.05F, 1.5F, 1, 1, 1, 0.0F, false));
        bone8.cubeList.add(new ModelBox(bone8, 695, 177, 1.5F, 33.05F, 1.5F, 1, 1, 1, 0.0F, false));
        bone8.cubeList.add(new ModelBox(bone8, 695, 177, -1.5F, 33.05F, 1.5F, 1, 1, 1, 0.0F, false));
        bone8.cubeList.add(new ModelBox(bone8, 695, 177, -4.5F, 33.05F, 1.5F, 1, 1, 1, 0.0F, false));

        a37 = new ModelRenderer(this);
        a37.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a37);
        setRotation(a37, 0.0F, 0.0F, 1.2566F);
        a37.cubeList.add(new ModelBox(a37, 666, 165, -7.0F, -32.925F, 19.0F, 14, 42, 15, 0.0F, false));
        a37.cubeList.add(new ModelBox(a37, 666, 165, -7.0F, -32.925F, 19.0F, 14, 42, 15, 0.0F, false));
        a37.cubeList.add(new ModelBox(a37, 219, 835, -7.5F, -35.3F, 32.65F, 15, 45, 3, 0.0F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(0.0F, 0.0F, 35.15F);
        a37.addChild(bone9);
        setRotation(bone9, 0.0F, 0.0F, -3.1416F);
        bone9.cubeList.add(new ModelBox(bone9, 717, 167, 4.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 717, 167, 1.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 717, 167, -2.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 717, 167, -5.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));

        a38 = new ModelRenderer(this);
        a38.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a38);
        setRotation(a38, 0.0F, 0.0F, -1.2566F);
        a38.cubeList.add(new ModelBox(a38, 666, 165, -7.0015F, -32.9397F, 19.0F, 14, 42, 15, 0.0F, false));
        a38.cubeList.add(new ModelBox(a38, 219, 835, -7.5F, -35.3F, 32.65F, 15, 45, 3, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, 0.0F, 35.15F);
        a38.addChild(bone10);
        setRotation(bone10, 0.0F, 0.0F, -3.1416F);
        bone10.cubeList.add(new ModelBox(bone10, 717, 167, 4.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 717, 167, 1.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 717, 167, -2.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 717, 167, -5.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));

        a39 = new ModelRenderer(this);
        a39.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a39);
        setRotation(a39, 0.0F, 0.0F, 1.6755F);
        a39.cubeList.add(new ModelBox(a39, 666, 165, -7.0F, -32.925F, 19.0F, 14, 42, 15, 0.0F, false));
        a39.cubeList.add(new ModelBox(a39, 219, 835, -7.5F, -35.3F, 32.65F, 15, 45, 3, 0.0F, false));

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(0.0F, 0.0F, 35.15F);
        a39.addChild(bone11);
        setRotation(bone11, 0.0F, 0.0F, -3.1416F);
        bone11.cubeList.add(new ModelBox(bone11, 717, 167, 4.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone11.cubeList.add(new ModelBox(bone11, 717, 167, 1.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone11.cubeList.add(new ModelBox(bone11, 717, 167, -2.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone11.cubeList.add(new ModelBox(bone11, 717, 167, -5.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));

        a40 = new ModelRenderer(this);
        a40.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a40);
        setRotation(a40, 0.0F, 0.0F, -1.6755F);
        a40.cubeList.add(new ModelBox(a40, 666, 165, -6.9985F, -32.9397F, 19.0F, 14, 42, 15, 0.0F, false));
        a40.cubeList.add(new ModelBox(a40, 219, 835, -7.5F, -35.3F, 32.65F, 15, 45, 3, 0.0F, false));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(0.0F, 0.0F, 35.15F);
        a40.addChild(bone12);
        setRotation(bone12, 0.0F, 0.0F, -3.1416F);
        bone12.cubeList.add(new ModelBox(bone12, 717, 167, 4.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone12.cubeList.add(new ModelBox(bone12, 717, 167, 1.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone12.cubeList.add(new ModelBox(bone12, 717, 167, -2.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone12.cubeList.add(new ModelBox(bone12, 717, 167, -5.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));

        a41 = new ModelRenderer(this);
        a41.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a41);
        setRotation(a41, 0.0F, 0.0F, 2.0944F);
        a41.cubeList.add(new ModelBox(a41, 666, 165, -6.997F, -32.9256F, 19.0F, 14, 42, 15, 0.0F, false));
        a41.cubeList.add(new ModelBox(a41, 219, 835, -7.5F, -35.35F, 32.65F, 15, 45, 3, 0.0F, false));

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(0.0F, 0.0F, 35.15F);
        a41.addChild(bone13);
        setRotation(bone13, 0.0F, 0.0F, -3.1416F);


        bone22 = new ModelRenderer(this);
        bone22.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone13.addChild(bone22);
        bone22.cubeList.add(new ModelBox(bone22, 717, 167, 4.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 717, 167, 1.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 717, 167, -2.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 717, 167, -5.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));

        a42 = new ModelRenderer(this);
        a42.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a42);
        setRotation(a42, 0.0F, 0.0F, -2.0944F);
        a42.cubeList.add(new ModelBox(a42, 666, 165, -6.9956F, -32.9384F, 19.0F, 14, 42, 15, 0.0F, false));
        a42.cubeList.add(new ModelBox(a42, 219, 835, -7.5F, -35.3F, 32.65F, 15, 45, 3, 0.0F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(0.0F, 0.0F, 35.15F);
        a42.addChild(bone14);
        setRotation(bone14, 0.0F, 0.0F, -3.1416F);
        bone14.cubeList.add(new ModelBox(bone14, 717, 167, 4.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 717, 167, 1.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 717, 167, -2.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 717, 167, -5.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));

        a43 = new ModelRenderer(this);
        a43.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a43);
        setRotation(a43, 0.0F, 0.0F, 2.5133F);
        a43.cubeList.add(new ModelBox(a43, 666, 165, -6.9945F, -32.9275F, 19.0F, 14, 42, 15, 0.0F, false));
        a43.cubeList.add(new ModelBox(a43, 219, 835, -7.5F, -35.35F, 32.65F, 15, 45, 3, 0.0F, false));

        bone15 = new ModelRenderer(this);
        bone15.setRotationPoint(0.0F, 0.0F, 35.15F);
        a43.addChild(bone15);
        setRotation(bone15, 0.0F, 0.0F, -3.1416F);
        bone15.cubeList.add(new ModelBox(bone15, 717, 167, 4.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 717, 167, 1.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 717, 167, -2.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 717, 167, -5.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));

        a44 = new ModelRenderer(this);
        a44.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a44);
        setRotation(a44, 0.0F, 0.0F, -2.5133F);
        a44.cubeList.add(new ModelBox(a44, 666, 165, -6.9936F, -32.9361F, 19.0F, 14, 42, 15, 0.0F, false));
        a44.cubeList.add(new ModelBox(a44, 219, 835, -7.5F, -35.3F, 32.65F, 15, 45, 3, 0.0F, false));

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(0.0F, 0.0F, 35.15F);
        a44.addChild(bone16);
        setRotation(bone16, 0.0F, 0.0F, -3.1416F);
        bone16.cubeList.add(new ModelBox(bone16, 717, 167, 4.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 717, 167, 1.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 717, 167, -2.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 717, 167, -5.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));

        a45 = new ModelRenderer(this);
        a45.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a45);
        setRotation(a45, 0.0F, 0.0F, 2.9322F);
        a45.cubeList.add(new ModelBox(a45, 666, 165, -6.993F, -32.9301F, 19.0F, 14, 42, 15, 0.0F, false));
        a45.cubeList.add(new ModelBox(a45, 219, 835, -7.5F, -35.3F, 32.65F, 15, 45, 3, 0.0F, false));

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(0.0F, 0.0F, 35.15F);
        a45.addChild(bone17);
        setRotation(bone17, 0.0F, 0.0F, -3.1416F);
        bone17.cubeList.add(new ModelBox(bone17, 717, 167, 4.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone17.cubeList.add(new ModelBox(bone17, 717, 167, 1.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone17.cubeList.add(new ModelBox(bone17, 717, 167, -2.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone17.cubeList.add(new ModelBox(bone17, 717, 167, -5.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));

        a46 = new ModelRenderer(this);
        a46.setRotationPoint(0.0F, -728.0F, -68.6F);
        fan_blade_mid.addChild(a46);
        setRotation(a46, 0.0F, 0.0F, -2.9322F);
        a46.cubeList.add(new ModelBox(a46, 666, 165, -6.9926F, -32.9332F, 19.0F, 14, 42, 15, 0.0F, false));
        a46.cubeList.add(new ModelBox(a46, 219, 835, -7.5F, -35.3F, 32.65F, 15, 45, 3, 0.0F, false));

        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(0.0F, 0.0F, 35.15F);
        a46.addChild(bone18);
        setRotation(bone18, 0.0F, 0.0F, -3.1416F);
        bone18.cubeList.add(new ModelBox(bone18, 717, 167, 4.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone18.cubeList.add(new ModelBox(bone18, 717, 167, 1.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone18.cubeList.add(new ModelBox(bone18, 717, 167, -2.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));
        bone18.cubeList.add(new ModelBox(bone18, 717, 167, -5.0F, 33.45F, -3.5F, 1, 1, 1, 0.0F, false));

        bone28 = new ModelRenderer(this);
        bone28.setRotationPoint(0.0F, 0.0F, 32.65F);
        fans.addChild(bone28);
        bone28.cubeList.add(new ModelBox(bone28, 126, 837, -7.0F, -32.5F, 19.0F, 14, 65, 1, 0.0F, false));
        bone28.cubeList.add(new ModelBox(bone28, 126, 837, -7.0F, -32.5F, 19.0F, 14, 65, 1, 0.0F, false));

        cube_r52 = new ModelRenderer(this);
        cube_r52.setRotationPoint(0.0F, 0.0F, 22.5F);
        bone28.addChild(cube_r52);
        setRotation(cube_r52, 0.0F, 0.0F, 0.4014F);
        cube_r52.cubeList.add(new ModelBox(cube_r52, 126, 837, -7.0F, -32.5F, -3.5F, 14, 65, 1, 0.0F, false));

        cube_r53 = new ModelRenderer(this);
        cube_r53.setRotationPoint(0.0F, 0.0F, 22.5F);
        bone28.addChild(cube_r53);
        setRotation(cube_r53, 0.0F, 0.0F, 1.5795F);
        cube_r53.cubeList.add(new ModelBox(cube_r53, 126, 837, -6.0F, -32.5F, -3.5F, 12, 65, 1, 0.0F, false));

        cube_r54 = new ModelRenderer(this);
        cube_r54.setRotationPoint(0.0F, 0.0F, 22.5F);
        bone28.addChild(cube_r54);
        setRotation(cube_r54, 0.0F, 0.0F, -1.2043F);
        cube_r54.cubeList.add(new ModelBox(cube_r54, 126, 837, -7.0F, -32.5F, -3.5F, 14, 65, 1, 0.0F, false));

        cube_r55 = new ModelRenderer(this);
        cube_r55.setRotationPoint(0.0F, 0.0F, 22.5F);
        bone28.addChild(cube_r55);
        setRotation(cube_r55, 0.0F, 0.0F, 1.2043F);
        cube_r55.cubeList.add(new ModelBox(cube_r55, 126, 837, -7.0F, -32.5F, -3.5F, 14, 65, 1, 0.0F, false));

        cube_r56 = new ModelRenderer(this);
        cube_r56.setRotationPoint(0.0F, 0.0F, 22.5F);
        bone28.addChild(cube_r56);
        setRotation(cube_r56, 0.0F, 0.0F, -0.8029F);
        cube_r56.cubeList.add(new ModelBox(cube_r56, 126, 837, -7.0F, -32.5F, -3.5F, 14, 65, 1, 0.0F, false));

        cube_r57 = new ModelRenderer(this);
        cube_r57.setRotationPoint(0.0F, 0.0F, 22.5F);
        bone28.addChild(cube_r57);
        setRotation(cube_r57, 0.0F, 0.0F, 0.8029F);
        cube_r57.cubeList.add(new ModelBox(cube_r57, 126, 837, -7.0F, -32.5F, -3.5F, 14, 65, 1, 0.0F, false));

        cube_r58 = new ModelRenderer(this);
        cube_r58.setRotationPoint(0.0F, 0.0F, 22.5F);
        bone28.addChild(cube_r58);
        setRotation(cube_r58, 0.0F, 0.0F, -0.4014F);
        cube_r58.cubeList.add(new ModelBox(cube_r58, 126, 837, -7.0F, -32.5F, -3.5F, 14, 65, 1, 0.0F, false));

        fan_base2 = new ModelRenderer(this);
        fan_base2.setRotationPoint(0.0F, 0.0F, 0.0F);
        fans.addChild(fan_base2);
        setRotation(fan_base2, 0.0F, 0.0F, 2.0944F);


        cube_r59 = new ModelRenderer(this);
        cube_r59.setRotationPoint(-4.0F, -20.0F, 27.0F);
        fan_base2.addChild(cube_r59);
        setRotation(cube_r59, 0.0F, 0.0436F, -0.2182F);
        cube_r59.cubeList.add(new ModelBox(cube_r59, 716, 165, 14.6F, 0.975F, 1.25F, 2, 2, 2, 0.0F, false));
        cube_r59.cubeList.add(new ModelBox(cube_r59, 716, 165, 14.6F, 0.975F, -4.75F, 2, 2, 2, 0.0F, false));
        cube_r59.cubeList.add(new ModelBox(cube_r59, 716, 165, 14.6F, -5.025F, 1.25F, 2, 2, 2, 0.0F, false));
        cube_r59.cubeList.add(new ModelBox(cube_r59, 716, 165, 14.6F, -2.025F, -1.75F, 2, 2, 2, 0.0F, false));
        cube_r59.cubeList.add(new ModelBox(cube_r59, 716, 165, 14.6F, -5.025F, -4.75F, 2, 2, 2, 0.0F, false));
        cube_r59.cubeList.add(new ModelBox(cube_r59, 716, 165, -13.4F, -2.025F, 1.25F, 2, 2, 2, 0.0F, false));
        cube_r59.cubeList.add(new ModelBox(cube_r59, 716, 165, -13.4F, -5.025F, -1.75F, 2, 2, 2, 0.0F, false));
        cube_r59.cubeList.add(new ModelBox(cube_r59, 716, 165, -13.4F, -2.025F, -4.75F, 2, 2, 2, 0.0F, false));
        cube_r59.cubeList.add(new ModelBox(cube_r59, 558, 985, -12.4F, -6.025F, -5.75F, 28, 11, 10, 0.0F, false));

        fan_base = new ModelRenderer(this);
        fan_base.setRotationPoint(0.0F, 0.0F, 0.0F);
        fans.addChild(fan_base);


        cube_r60 = new ModelRenderer(this);
        cube_r60.setRotationPoint(-4.0F, -20.0F, 27.0F);
        fan_base.addChild(cube_r60);
        setRotation(cube_r60, 0.0F, 0.0436F, -0.2182F);
        cube_r60.cubeList.add(new ModelBox(cube_r60, 716, 165, 14.6F, -5.025F, 0.25F, 2, 2, 2, 0.0F, false));
        cube_r60.cubeList.add(new ModelBox(cube_r60, 716, 165, 14.6F, -5.025F, -5.75F, 2, 2, 2, 0.0F, false));
        cube_r60.cubeList.add(new ModelBox(cube_r60, 716, 165, 14.6F, -2.025F, -2.75F, 2, 2, 2, 0.0F, false));
        cube_r60.cubeList.add(new ModelBox(cube_r60, 716, 165, 14.6F, 0.975F, 0.25F, 2, 2, 2, 0.0F, false));
        cube_r60.cubeList.add(new ModelBox(cube_r60, 716, 165, 14.6F, 0.975F, -5.75F, 2, 2, 2, 0.0F, false));
        cube_r60.cubeList.add(new ModelBox(cube_r60, 716, 165, -13.4F, -5.025F, -2.75F, 2, 2, 2, 0.0F, false));
        cube_r60.cubeList.add(new ModelBox(cube_r60, 716, 165, -13.4F, -2.025F, -5.75F, 2, 2, 2, 0.0F, false));
        cube_r60.cubeList.add(new ModelBox(cube_r60, 716, 165, -13.4F, -2.025F, 0.25F, 2, 2, 2, 0.0F, false));

        cube_r61 = new ModelRenderer(this);
        cube_r61.setRotationPoint(-4.0F, -20.0F, 27.0F);
        fan_base.addChild(cube_r61);
        setRotation(cube_r61, 0.0F, 0.0436F, -0.2182F);
        cube_r61.cubeList.add(new ModelBox(cube_r61, 558, 985, -12.5F, -6.0F, -6.75F, 28, 11, 10, 0.0F, false));

        fan_base3 = new ModelRenderer(this);
        fan_base3.setRotationPoint(0.0F, 0.0F, 0.0F);
        fans.addChild(fan_base3);
        setRotation(fan_base3, 0.0F, 0.0F, -2.0944F);


        cube_r62 = new ModelRenderer(this);
        cube_r62.setRotationPoint(-4.0F, -20.0F, 27.0F);
        fan_base3.addChild(cube_r62);
        setRotation(cube_r62, 0.0F, 0.0436F, -0.2182F);
        cube_r62.cubeList.add(new ModelBox(cube_r62, 716, 165, 14.6F, -4.775F, 1.25F, 2, 2, 2, 0.0F, false));
        cube_r62.cubeList.add(new ModelBox(cube_r62, 716, 165, 14.6F, -4.775F, -4.75F, 2, 2, 2, 0.0F, false));
        cube_r62.cubeList.add(new ModelBox(cube_r62, 716, 165, 14.6F, -1.775F, -1.75F, 2, 2, 2, 0.0F, false));
        cube_r62.cubeList.add(new ModelBox(cube_r62, 716, 165, 14.6F, 1.225F, 1.25F, 2, 2, 2, 0.0F, false));
        cube_r62.cubeList.add(new ModelBox(cube_r62, 716, 165, 14.6F, 1.225F, -4.75F, 2, 2, 2, 0.0F, false));
        cube_r62.cubeList.add(new ModelBox(cube_r62, 716, 165, -13.4F, -4.775F, -1.75F, 2, 2, 2, 0.0F, false));
        cube_r62.cubeList.add(new ModelBox(cube_r62, 716, 165, -13.4F, -1.775F, -4.75F, 2, 2, 2, 0.0F, false));
        cube_r62.cubeList.add(new ModelBox(cube_r62, 716, 165, -13.4F, -1.775F, 1.25F, 2, 2, 2, 0.0F, false));

        cube_r63 = new ModelRenderer(this);
        cube_r63.setRotationPoint(-4.0F, -20.0F, 27.0F);
        fan_base3.addChild(cube_r63);
        setRotation(cube_r63, 0.0F, 0.0436F, -0.2182F);
        cube_r63.cubeList.add(new ModelBox(cube_r63, 558, 985, -12.4F, -5.8F, -5.75F, 28, 11, 10, 0.0F, false));

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(0.0F, 24.0F, 0.0F);


        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone19.addChild(bone20);
        bone20.cubeList.add(new ModelBox(bone20, 138, 863, -45.5F, -25.0F, 41.5F, 4, 17, 4, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 138, 863, -45.5F, -25.0F, -45.5F, 4, 17, 4, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 138, 863, 41.5F, -25.0F, -45.5F, 4, 17, 4, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 138, 863, 41.5F, -25.0F, 41.5F, 4, 17, 4, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 139, 864, 24.5F, -22.0F, 42.0F, 3, 14, 3, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 139, 864, -27.5F, -22.0F, 42.0F, 3, 14, 3, 0.0F, false));

        cube_r64 = new ModelRenderer(this);
        cube_r64.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone20.addChild(cube_r64);
        setRotation(cube_r64, 0.0F, 1.5708F, 0.0F);
        cube_r64.cubeList.add(new ModelBox(cube_r64, 139, 864, -27.5F, -22.0F, 42.0F, 3, 14, 3, 0.0F, false));
        cube_r64.cubeList.add(new ModelBox(cube_r64, 139, 864, 24.5F, -22.0F, 42.0F, 3, 14, 3, 0.0F, false));

        cube_r65 = new ModelRenderer(this);
        cube_r65.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone20.addChild(cube_r65);
        setRotation(cube_r65, 0.0F, 3.1416F, 0.0F);
        cube_r65.cubeList.add(new ModelBox(cube_r65, 139, 864, -27.5F, -22.0F, 42.0F, 3, 14, 3, 0.0F, false));
        cube_r65.cubeList.add(new ModelBox(cube_r65, 139, 864, 24.5F, -22.0F, 42.0F, 3, 14, 3, 0.0F, false));

        cube_r66 = new ModelRenderer(this);
        cube_r66.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone20.addChild(cube_r66);
        setRotation(cube_r66, 0.0F, -1.5708F, 0.0F);
        cube_r66.cubeList.add(new ModelBox(cube_r66, 139, 864, -27.5F, -22.0F, 42.0F, 3, 14, 3, 0.0F, false));
        cube_r66.cubeList.add(new ModelBox(cube_r66, 139, 864, 24.5F, -22.0F, 42.0F, 3, 14, 3, 0.0F, false));

        bone21 = new ModelRenderer(this);
        bone21.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone19.addChild(bone21);
        bone21.cubeList.add(new ModelBox(bone21, 206, 889, -42.5F, -21.0F, 42.5F, 16, 3, 2, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 206, 889, -42.5F, -15.0F, 42.5F, 16, 3, 2, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 206, 889, 26.5F, -15.0F, 42.5F, 16, 3, 2, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 206, 889, 26.5F, -21.0F, 42.5F, 16, 3, 2, 0.0F, false));

        cube_r67 = new ModelRenderer(this);
        cube_r67.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone21.addChild(cube_r67);
        setRotation(cube_r67, 0.0F, 1.5708F, 0.0F);
        cube_r67.cubeList.add(new ModelBox(cube_r67, 206, 889, 26.5F, -21.0F, 42.5F, 16, 3, 2, 0.0F, false));
        cube_r67.cubeList.add(new ModelBox(cube_r67, 206, 889, 26.5F, -15.0F, 42.5F, 16, 3, 2, 0.0F, false));
        cube_r67.cubeList.add(new ModelBox(cube_r67, 206, 889, -42.5F, -21.0F, 42.5F, 16, 3, 2, 0.0F, false));
        cube_r67.cubeList.add(new ModelBox(cube_r67, 206, 889, -42.5F, -15.0F, 42.5F, 16, 3, 2, 0.0F, false));

        cube_r68 = new ModelRenderer(this);
        cube_r68.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone21.addChild(cube_r68);
        setRotation(cube_r68, 0.0F, 3.1416F, 0.0F);
        cube_r68.cubeList.add(new ModelBox(cube_r68, 206, 889, 26.5F, -21.0F, 42.5F, 16, 3, 2, 0.0F, false));
        cube_r68.cubeList.add(new ModelBox(cube_r68, 206, 889, 26.5F, -15.0F, 42.5F, 16, 3, 2, 0.0F, false));
        cube_r68.cubeList.add(new ModelBox(cube_r68, 206, 889, -42.5F, -21.0F, 42.5F, 16, 3, 2, 0.0F, false));
        cube_r68.cubeList.add(new ModelBox(cube_r68, 206, 889, -42.5F, -15.0F, 42.5F, 16, 3, 2, 0.0F, false));

        cube_r69 = new ModelRenderer(this);
        cube_r69.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone21.addChild(cube_r69);
        setRotation(cube_r69, 0.0F, -1.5708F, 0.0F);
        cube_r69.cubeList.add(new ModelBox(cube_r69, 206, 889, 26.5F, -21.0F, 42.5F, 16, 3, 2, 0.0F, false));
        cube_r69.cubeList.add(new ModelBox(cube_r69, 206, 889, 26.5F, -15.0F, 42.5F, 16, 3, 2, 0.0F, false));
        cube_r69.cubeList.add(new ModelBox(cube_r69, 206, 889, -42.5F, -21.0F, 42.5F, 16, 3, 2, 0.0F, false));
        cube_r69.cubeList.add(new ModelBox(cube_r69, 206, 889, -42.5F, -15.0F, 42.5F, 16, 3, 2, 0.0F, false));
        cube_r69.cubeList.add(new ModelBox(cube_r69, 206, 889, 26.5F, -21.0F, 42.5F, 16, 3, 2, 0.0F, false));
        cube_r69.cubeList.add(new ModelBox(cube_r69, 206, 889, 26.5F, -15.0F, 42.5F, 16, 3, 2, 0.0F, false));
        cube_r69.cubeList.add(new ModelBox(cube_r69, 206, 889, -42.5F, -21.0F, 42.5F, 16, 3, 2, 0.0F, false));
        cube_r69.cubeList.add(new ModelBox(cube_r69, 206, 889, -42.5F, -15.0F, 42.5F, 16, 3, 2, 0.0F, false));

        south_controller = new ModelRenderer(this);
        south_controller.setRotationPoint(0.2457F, 24.0707F, 0.0F);
        south_controller.cubeList.add(new ModelBox(south_controller, 456, 496, -22.1213F, -32.1213F, 40.0F, 44, 2, 15, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 460, 500, 22.0F, -30.0F, 44.0F, 2, 15, 11, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 464, 504, 8.0F, -15.0F, 48.0F, 16, 15, 7, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 1004, 900, 11.7543F, -12.0707F, 54.0F, 8, 8, 2, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 464, 504, -24.2457F, -15.0707F, 48.0F, 16, 15, 7, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 1004, 900, -20.2457F, -12.0707F, 54.0F, 8, 8, 2, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 460, 500, -24.2426F, -30.0F, 44.0F, 2, 15, 11, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 680, 318, -8.0957F, -13.0707F, 48.0F, 16, 13, 6, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 218, 865, -7.0957F, -12.0707F, 46.05F, 14, 1, 8, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 656, 327, -4.0957F, -10.0707F, 53.9F, 11, 1, 1, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 664, 324, -2.0957F, -8.0707F, 53.55F, 7, 2, 1, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 664, 324, -2.0957F, -5.0707F, 53.55F, 7, 2, 1, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 656, 329, 5.9043F, -9.0707F, 53.9F, 1, 7, 1, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 660, 329, -3.0957F, -9.0707F, 53.05F, 9, 7, 1, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 656, 327, -4.0957F, -2.0707F, 53.9F, 11, 1, 1, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 652, 329, -4.0957F, -9.0707F, 53.9F, 1, 7, 1, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 688, 306, -7.0957F, -4.0707F, 52.5F, 2, 2, 2, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 688, 310, -7.0957F, -7.0707F, 52.5F, 2, 2, 2, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 688, 314, -7.0957F, -10.0707F, 52.5F, 2, 2, 2, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 467, 507, -24.2426F, -30.0F, 40.0F, 48, 14, 4, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 469, 509, -24.2457F, -16.0707F, 46.0F, 16, 3, 2, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 465, 505, -24.0F, -16.0F, 40.0F, 48, 8, 6, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 469, 509, -24.0F, -13.0F, 46.0F, 48, 5, 2, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 469, 509, 8.0F, -16.0F, 46.0F, 16, 3, 2, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 467, 507, -22.1207F, -31.8207F, 40.0F, 44, 2, 4, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 462, 502, -9.0F, -14.0F, 45.5F, 1, 14, 9, 0.0F, false));
        south_controller.cubeList.add(new ModelBox(south_controller, 462, 502, 7.75F, -14.0F, 45.5F, 1, 14, 9, 0.0F, false));

        cube_r70 = new ModelRenderer(this);
        cube_r70.setRotationPoint(-68.2426F, -34.1012F, 54.0F);
        south_controller.addChild(cube_r70);
        setRotation(cube_r70, 0.0F, 0.0F, -2.3562F);
        cube_r70.cubeList.add(new ModelBox(cube_r70, 456, 496, -36.0127F, 28.2127F, -14.0F, 2, 3, 15, 0.0F, false));

        cube_r71 = new ModelRenderer(this);
        cube_r71.setRotationPoint(23.9293F, -30.0707F, 47.0F);
        south_controller.addChild(cube_r71);
        setRotation(cube_r71, 0.0F, 0.0F, -0.7854F);
        cube_r71.cubeList.add(new ModelBox(cube_r71, 456, 496, -2.0F, -2.9F, -7.0F, 2, 3, 15, 0.0F, false));

        pm = new ModelRenderer(this);
        pm.setRotationPoint(-0.2437F, -23.0707F, 46.9025F);
        south_controller.addChild(pm);
        setRotation(pm, 0.0F, 3.1416F, 0.0F);
        pm.cubeList.add(new ModelBox(pm, 117, 866, -8.4215F, -2.0F, 2.0342F, 17, 4, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 117, 866, -6.4215F, -4.0F, 1.2842F, 13, 8, 0, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 117, 866, 2.8403F, -1.5F, 1.2183F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 117, 866, 2.8403F, 0.5F, 1.2183F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 117, 866, -0.1597F, -0.5F, 1.2183F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 117, 866, -3.1597F, -1.5F, 1.2183F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 117, 866, -3.1597F, 0.5F, 1.2183F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 620, 326, -7.0872F, -5.0F, 0.701F, 14, 10, 1, 0.0F, false));

        cube_r72 = new ModelRenderer(this);
        cube_r72.setRotationPoint(-13.8122F, 0.0F, 0.0F);
        pm.addChild(cube_r72);
        setRotation(cube_r72, 0.0F, 0.3927F, 0.0F);
        cube_r72.cubeList.add(new ModelBox(cube_r72, 620, 326, 19.6F, -5.0F, 8.876F, 14, 10, 1, 0.0F, false));

        cube_r73 = new ModelRenderer(this);
        cube_r73.setRotationPoint(-13.8122F, 0.0F, 0.0F);
        pm.addChild(cube_r73);
        setRotation(cube_r73, 0.0F, -0.3927F, 0.0F);
        cube_r73.cubeList.add(new ModelBox(cube_r73, 620, 326, -8.0F, -5.0F, -1.724F, 14, 10, 1, 0.0F, false));

        portRight_r1 = new ModelRenderer(this);
        portRight_r1.setRotationPoint(0.3403F, 0.0F, 1.7183F);
        pm.addChild(portRight_r1);
        setRotation(portRight_r1, 0.0F, 0.3927F, 0.0F);
        portRight_r1.cubeList.add(new ModelBox(portRight_r1, 117, 866, 10.0F, 0.5F, 2.5F, 1, 1, 1, 0.0F, false));
        portRight_r1.cubeList.add(new ModelBox(portRight_r1, 117, 866, 10.0F, -1.5F, 2.5F, 1, 1, 1, 0.0F, false));
        portRight_r1.cubeList.add(new ModelBox(portRight_r1, 117, 866, 16.0F, -1.5F, 2.5F, 1, 1, 1, 0.0F, false));
        portRight_r1.cubeList.add(new ModelBox(portRight_r1, 117, 866, 16.0F, 0.5F, 2.5F, 1, 1, 1, 0.0F, false));
        portRight_r1.cubeList.add(new ModelBox(portRight_r1, 117, 866, 13.0F, -0.5F, 2.5F, 1, 1, 1, 0.0F, false));

        portRight_r2 = new ModelRenderer(this);
        portRight_r2.setRotationPoint(14.0297F, 0.0F, -1.5312F);
        pm.addChild(portRight_r2);
        setRotation(portRight_r2, 0.0F, 0.3927F, 0.0F);
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 117, 866, -6.5F, -4.0F, -0.0282F, 13, 8, 0, 0.0F, false));

        portRight_r3 = new ModelRenderer(this);
        portRight_r3.setRotationPoint(0.0785F, 2.75F, 1.5002F);
        pm.addChild(portRight_r3);
        setRotation(portRight_r3, 0.0F, 0.3927F, 0.0F);
        portRight_r3.cubeList.add(new ModelBox(portRight_r3, 117, 866, 7.3543F, -4.75F, 3.7053F, 11, 4, 1, 0.0F, false));

        portRight_r4 = new ModelRenderer(this);
        portRight_r4.setRotationPoint(0.0785F, 2.75F, 1.5002F);
        pm.addChild(portRight_r4);
        setRotation(portRight_r4, 0.0F, -0.3927F, 0.0F);
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 117, 866, -14.8543F, -3.25F, 2.7053F, 1, 1, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 117, 866, -11.8543F, -2.25F, 2.7053F, 1, 1, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 117, 866, -17.8543F, -2.25F, 2.7053F, 1, 1, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 117, 866, -17.8543F, -4.25F, 2.7053F, 1, 1, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 117, 866, -18.3543F, -4.75F, 3.7053F, 11, 4, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 117, 866, -11.8543F, -4.25F, 2.7053F, 1, 1, 1, 0.0F, false));

        keyboard = new ModelRenderer(this);
        keyboard.setRotationPoint(-1.7457F, -14.308F, 50.9017F);
        south_controller.addChild(keyboard);
        setRotation(keyboard, 0.0F, 3.1416F, 0.0F);


        cube_r74 = new ModelRenderer(this);
        cube_r74.setRotationPoint(-1.0F, 1.0F, 3.0F);
        keyboard.addChild(cube_r74);
        setRotation(cube_r74, 0.2182F, 0.0F, 0.0F);
        cube_r74.cubeList.add(new ModelBox(cube_r74, 116, 866, -9.15F, -1.25F, -3.0F, 18, 1, 6, 0.0F, false));

        cube_r75 = new ModelRenderer(this);
        cube_r75.setRotationPoint(0.0F, -0.5F, -6.5F);
        keyboard.addChild(cube_r75);
        setRotation(cube_r75, 0.2182F, 0.0F, 0.0F);
        cube_r75.cubeList.add(new ModelBox(cube_r75, 696, 310, -1.4F, 1.5F, 3.5F, 7, 1, 7, 0.0F, false));
        cube_r75.cubeList.add(new ModelBox(cube_r75, 696, 302, -8.9F, 1.5F, 3.5F, 7, 1, 7, 0.0F, false));

        west_io = new ModelRenderer(this);
        west_io.setRotationPoint(0.0F, 24.0F, 0.0F);


        cube_r76 = new ModelRenderer(this);
        cube_r76.setRotationPoint(0.0F, 0.0F, 0.0F);
        west_io.addChild(cube_r76);
        setRotation(cube_r76, 0.0F, -1.5708F, 0.0F);
        cube_r76.cubeList.add(new ModelBox(cube_r76, 1004, 900, -20.0F, -12.0F, 54.0F, 8, 8, 2, 0.0F, false));
        cube_r76.cubeList.add(new ModelBox(cube_r76, 1004, 910, -4.0F, -12.0F, 54.0F, 8, 8, 2, 0.0F, false));
        cube_r76.cubeList.add(new ModelBox(cube_r76, 1004, 900, 12.0F, -12.0F, 54.0F, 8, 8, 2, 0.0F, false));
        cube_r76.cubeList.add(new ModelBox(cube_r76, 464, 521, -24.0F, -16.0F, 48.0F, 48, 16, 6, 0.0F, false));

        cube_r77 = new ModelRenderer(this);
        cube_r77.setRotationPoint(-44.0F, -13.0F, 0.0F);
        west_io.addChild(cube_r77);
        setRotation(cube_r77, 0.0F, 1.5708F, 0.0F);
        cube_r77.cubeList.add(new ModelBox(cube_r77, 280, 515, -24.0F, -5.0F, -4.0F, 48, 10, 96, 0.0F, false));

        east_io = new ModelRenderer(this);
        east_io.setRotationPoint(0.0F, 24.0F, 0.0F);


        cube_r78 = new ModelRenderer(this);
        cube_r78.setRotationPoint(0.0F, 0.0F, 0.0F);
        east_io.addChild(cube_r78);
        setRotation(cube_r78, 0.0F, 1.5708F, 0.0F);
        cube_r78.cubeList.add(new ModelBox(cube_r78, 1004, 900, -20.0F, -12.0F, 54.0F, 8, 8, 2, 0.0F, false));
        cube_r78.cubeList.add(new ModelBox(cube_r78, 1004, 910, -4.0F, -12.0F, 54.0F, 8, 8, 2, 0.0F, false));
        cube_r78.cubeList.add(new ModelBox(cube_r78, 1004, 900, 12.0F, -12.0F, 54.0F, 8, 8, 2, 0.0F, false));
        cube_r78.cubeList.add(new ModelBox(cube_r78, 460, 531, -24.0F, -16.0F, 48.0F, 48, 16, 6, 0.0F, false));

        north_io = new ModelRenderer(this);
        north_io.setRotationPoint(0.0F, 24.0F, 0.0F);


        cube_r79 = new ModelRenderer(this);
        cube_r79.setRotationPoint(0.0F, 0.0F, 0.0F);
        north_io.addChild(cube_r79);
        setRotation(cube_r79, 0.0F, 3.1416F, 0.0F);
        cube_r79.cubeList.add(new ModelBox(cube_r79, 474, 554, -24.0F, -16.0F, 48.0F, 48, 16, 6, 0.0F, false));
        cube_r79.cubeList.add(new ModelBox(cube_r79, 1004, 910, -4.0F, -12.0F, 54.0F, 8, 8, 2, 0.0F, false));
        cube_r79.cubeList.add(new ModelBox(cube_r79, 1004, 900, -20.0F, -12.0F, 54.0F, 8, 8, 2, 0.0F, false));
        cube_r79.cubeList.add(new ModelBox(cube_r79, 1004, 900, 12.0F, -12.0F, 54.0F, 8, 8, 2, 0.0F, false));
        cube_r79.cubeList.add(new ModelBox(cube_r79, 472, 552, -24.0F, -24.0F, 40.0F, 48, 16, 8, 0.0F, false));

        down = new ModelRenderer(this);
        down.setRotationPoint(0.0F, 24.0F, 0.0F);
        down.cubeList.add(new ModelBox(down, 0, 927, -40.0F, -24.0F, -40.0F, 80, 16, 80, 0.0F, false));
        down.cubeList.add(new ModelBox(down, 0, 823, -48.0F, -8.0F, -48.0F, 96, 8, 96, 0.0F, false));
    }


    public void render(double tick,float size, double angle, boolean on, TextureManager manager) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        doRender(size, angle);
        if (!Mekanism.hooks.Bloom) {
            manager.bindTexture(on ? MekanismMultiblockMachineUtils.getResource(ResourceType.RENDER, "WindGenerator/LargeWindGenerator_ON_" + getTick(tick) + ".png") : OVERLAY_OFF);
            GlStateManager.scale(1.001F, 1.001F, 1.001F);
            GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
            MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
            doRender(size, angle);
            MekanismRenderer.disableGlow(glowInfo);
        }
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }

    public void renderBloom(double tick,float size,  double angle,boolean on, TextureManager manager) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        manager.bindTexture(on ? MekanismMultiblockMachineUtils.getResource(ResourceType.RENDER, "WindGenerator/LargeWindGenerator_ON_" + getTick(tick) + ".png") : OVERLAY_OFF);
        GlStateManager.scale(1.001F, 1.001F, 1.001F);
        GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
        MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
        doRender(size, angle);
        MekanismRenderer.disableGlow(glowInfo);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }

    private void doRender(float size){
        doll_up.render(size);
        uio_up.render(size);
        doll_up2.render(size);
        bolt.render(size);
        wind_turbine_head.render(size);
        wind_power_middle.render(size);
        bone3.render(size);
        bone19.render(size);
        south_controller.render(size);
        west_io.render(size);
        east_io.render(size);
        north_io.render(size);
        down.render(size);
    }

    private void doRender(float size, double angle) {
        setRotation(fans, 0F, 0F, getRotation(getAbsoluteAngle(angle)));
        fans.render(size);
        doRender(size);
    }

    public float getRotation(double angle) {
        return ((float) angle / (float) 180) * (float) Math.PI;
    }

    public double getAbsoluteAngle(double angle) {
        return angle % 360;
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public int getTick(double tick) {
        if (tick >= 0.2F && tick < 0.3F || tick >= 0.4F && tick < 0.5F || tick >= 0.6F && tick < 0.7F || tick >= 0.8F && tick < 0.9F) {
            return 1;
        }
        return 0;
    }
}
