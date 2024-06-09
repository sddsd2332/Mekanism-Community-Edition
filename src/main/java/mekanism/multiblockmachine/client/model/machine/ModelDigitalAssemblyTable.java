package mekanism.multiblockmachine.client.model.machine;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelDigitalAssemblyTable extends ModelBase {
    private final ModelRenderer footpads;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer bone5;
    private final ModelRenderer side;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer side2;
    private final ModelRenderer cube_r8;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer cube_r11;
    private final ModelRenderer baffle;
    private final ModelRenderer cube_r12;
    private final ModelRenderer cube_r13;
    private final ModelRenderer cube_r14;
    private final ModelRenderer cube_r15;
    private final ModelRenderer energy;
    private final ModelRenderer cube_r16;
    private final ModelRenderer cube_r17;
    private final ModelRenderer cube_r18;
    private final ModelRenderer cube_r19;
    private final ModelRenderer energy_in;
    private final ModelRenderer energy2;
    private final ModelRenderer cube_r20;
    private final ModelRenderer cube_r21;
    private final ModelRenderer cube_r22;
    private final ModelRenderer cube_r23;
    private final ModelRenderer energy_in2;
    private final ModelRenderer fluid;
    private final ModelRenderer cube_r24;
    private final ModelRenderer cube_r25;
    private final ModelRenderer cube_r26;
    private final ModelRenderer cube_r27;
    private final ModelRenderer fluid_in;
    private final ModelRenderer gas;
    private final ModelRenderer cube_r28;
    private final ModelRenderer cube_r29;
    private final ModelRenderer cube_r30;
    private final ModelRenderer cube_r31;
    private final ModelRenderer gas_in2;
    private final ModelRenderer fluid2;
    private final ModelRenderer cube_r32;
    private final ModelRenderer cube_r33;
    private final ModelRenderer cube_r34;
    private final ModelRenderer cube_r35;
    private final ModelRenderer fluid_in2;
    private final ModelRenderer gas2;
    private final ModelRenderer cube_r36;
    private final ModelRenderer cube_r37;
    private final ModelRenderer cube_r38;
    private final ModelRenderer cube_r39;
    private final ModelRenderer gas_in3;
    private final ModelRenderer bone;
    private final ModelRenderer cube_r40;
    private final ModelRenderer cube_r41;
    private final ModelRenderer cube_r42;
    private final ModelRenderer cube_r43;
    private final ModelRenderer conveyorbelts;
    private final ModelRenderer Roboticarm;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer baseTurn2_r1;
    private final ModelRenderer Roboticarm2;
    private final ModelRenderer bone4;
    private final ModelRenderer bone6;
    private final ModelRenderer baseTurn2_r2;
    private final ModelRenderer Roboticarm3;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer baseTurn2_r3;
    private final ModelRenderer Roboticarm4;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer baseTurn2_r4;
    private final ModelRenderer platform;
    private final ModelRenderer platform2;
    private final ModelRenderer glass;
    private final ModelRenderer glass1;
    private final ModelRenderer glass2;
    private final ModelRenderer glass3;
    private final ModelRenderer doorpanels;
    private final ModelRenderer cube_r44;
    private final ModelRenderer cube_r45;
    private final ModelRenderer cube_r46;
    private final ModelRenderer cube_r47;
    private final ModelRenderer cube_r48;
    private final ModelRenderer cube_r49;
    private final ModelRenderer door;
    private final ModelRenderer cube_r50;
    private final ModelRenderer cube_r51;
    private final ModelRenderer door2;
    private final ModelRenderer cube_r52;
    private final ModelRenderer cube_r53;
    private final ModelRenderer door3;
    private final ModelRenderer cube_r54;
    private final ModelRenderer cube_r55;
    private final ModelRenderer side3;
    private final ModelRenderer glass4;
    private final ModelRenderer Warninglights;
    private final ModelRenderer pm;
    private final ModelRenderer cube_r56;
    private final ModelRenderer cube_r57;
    private final ModelRenderer cube_r58;
    private final ModelRenderer cube_r59;
    private final ModelRenderer cube_r60;
    private final ModelRenderer cube_r61;
    private final ModelRenderer cube_r62;
    private final ModelRenderer cube_r63;
    private final ModelRenderer cube_r64;
    private final ModelRenderer cube_r65;
    private final ModelRenderer cube_r66;
    private final ModelRenderer Leftscreen;
    private final ModelRenderer cube_r67;
    private final ModelRenderer cube_r68;
    private final ModelRenderer cube_r69;
    private final ModelRenderer cube_r70;
    private final ModelRenderer cube_r71;
    private final ModelRenderer cube_r72;
    private final ModelRenderer Rightscreen;
    private final ModelRenderer cube_r73;
    private final ModelRenderer cube_r74;
    private final ModelRenderer cube_r75;
    private final ModelRenderer cube_r76;
    private final ModelRenderer cube_r77;
    private final ModelRenderer cube_r78;
    private final ModelRenderer pm_progress;
    private final ModelRenderer cube_r79;
    private final ModelRenderer cube_r80;
    private final ModelRenderer cube_r81;
    private final ModelRenderer bb_main;

    public ModelDigitalAssemblyTable() {
        textureWidth = 1024;
        textureHeight = 1024;

        footpads = new ModelRenderer(this);
        footpads.setRotationPoint(67.0F, 24.0F, -68.0F);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-121.0F, -3.0F, 71.0F);
        footpads.addChild(cube_r1);
        setRotationAngle(cube_r1, 1.5708F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 148, 561, -13.0F, -14.0F, -1.0F, 14, 14, 2, 0.0F, false));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 180, 561, 107.0F, -14.0F, -1.0F, 14, 14, 2, 0.0F, false));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-121.0F, -3.0F, 135.0F);
        footpads.addChild(cube_r2);
        setRotationAngle(cube_r2, 1.5708F, 0.0F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 212, 561, -13.0F, -14.0F, -1.0F, 14, 14, 2, 0.0F, false));
        cube_r2.cubeList.add(new ModelBox(cube_r2, 566, 0, 107.0F, -14.0F, -1.0F, 14, 14, 2, 0.0F, false));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(-1.0F, -3.0F, 14.0F);
        footpads.addChild(cube_r3);
        setRotationAngle(cube_r3, 1.5708F, 0.0F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 566, 16, -13.0F, -14.0F, -1.0F, 14, 14, 2, 0.0F, false));
        cube_r3.cubeList.add(new ModelBox(cube_r3, 566, 32, -134.0F, -14.0F, -1.0F, 14, 14, 2, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, 0.0F, 121.0F);
        footpads.addChild(bone5);
        setRotationAngle(bone5, -1.5708F, 0.0F, 0.0F);
        bone5.cubeList.add(new ModelBox(bone5, 108, 319, -136.0F, 106.0F, -2.0F, 16, 16, 2, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 34, 319, -135.0F, 49.0F, -2.0F, 16, 16, 2, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 274, 287, -135.0F, -15.0F, -2.0F, 16, 16, 2, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 108, 287, -15.0F, 106.0F, -2.0F, 16, 16, 2, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 34, 287, -15.0F, 49.0F, -2.0F, 16, 16, 2, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 102, 0, -15.0F, -15.0F, -2.0F, 16, 16, 2, 0.0F, false));

        side = new ModelRenderer(this);
        side.setRotationPoint(0.0F, -18.0F, 0.0F);
        setRotationAngle(side, 0.0F, 1.5708F, 0.0F);


        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(0.1213F, 0.0F, 0.1213F);
        side.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -0.7854F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 544, 155, 22.4558F, 73.3675F, -70.0F, 3, 3, 140, 0.0F, false));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(0.1213F, 0.0F, 0.1213F);
        side.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, -0.7854F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 544, 298, 73.3675F, 22.4558F, -70.0F, 3, 3, 140, 0.0F, false));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(-0.1287F, 0.0F, -0.0287F);
        side.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, -0.7854F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 566, 5, -25.4558F, -76.3675F, -70.0F, 3, 3, 140, 0.0F, false));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(-0.1287F, 0.0F, -0.1287F);
        side.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, -0.7854F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 0, 585, -76.3675F, -25.4558F, -70.0F, 3, 3, 140, 0.0F, false));

        side2 = new ModelRenderer(this);
        side2.setRotationPoint(0.0F, -18.0F, 0.0F);


        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(71.2929F, -35.2929F, -69.0F);
        side2.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.0F, -0.7854F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 420, 2, -2.0F, -3.0F, -1.0F, 3, 3, 140, 0.0F, false));

        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(71.2929F, 36.7071F, -69.0F);
        side2.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.0F, -0.7854F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 148, 448, -2.0F, -3.0F, -1.0F, 3, 3, 140, 0.0F, false));

        cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(-68.4645F, 36.7071F, -69.0F);
        side2.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.0F, -0.7854F);
        cube_r10.cubeList.add(new ModelBox(cube_r10, 294, 459, -2.0F, -3.0F, -1.0F, 3, 3, 140, 0.0F, false));

        cube_r11 = new ModelRenderer(this);
        cube_r11.setRotationPoint(-68.4645F, -35.2929F, -69.0F);
        side2.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, 0.0F, -0.7854F);
        cube_r11.cubeList.add(new ModelBox(cube_r11, 440, 462, -2.0F, -3.0F, -1.0F, 3, 3, 140, 0.0F, false));

        baffle = new ModelRenderer(this);
        baffle.setRotationPoint(0.0F, 24.0F, 0.0F);
        baffle.cubeList.add(new ModelBox(baffle, 586, 475, -70.13F, -17.0F, 67.1287F, 140, 11, 5, 0.0F, false));
        baffle.cubeList.add(new ModelBox(baffle, 394, 308, -72.0F, -17.0F, -70.0F, 5, 11, 140, 0.0F, false));
        baffle.cubeList.add(new ModelBox(baffle, 244, 297, 67.0F, -17.0F, -70.0F, 5, 11, 140, 0.0F, false));
        baffle.cubeList.add(new ModelBox(baffle, 586, 459, -70.13F, -17.0F, -71.8713F, 140, 11, 5, 0.0F, false));

        cube_r12 = new ModelRenderer(this);
        cube_r12.setRotationPoint(-69.8787F, -6.0F, 69.3003F);
        baffle.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0F, 0.7854F, 0.0F);
        cube_r12.cubeList.add(new ModelBox(cube_r12, 110, 43, -2.0F, -72.0F, -1.0F, 3, 72, 3, 0.0F, false));

        cube_r13 = new ModelRenderer(this);
        cube_r13.setRotationPoint(69.8713F, -42.0F, 70.0074F);
        baffle.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.0F, 0.7854F, 0.0F);
        cube_r13.cubeList.add(new ModelBox(cube_r13, 122, 43, -1.5F, -36.0F, -1.5F, 3, 72, 3, 0.0F, false));

        cube_r14 = new ModelRenderer(this);
        cube_r14.setRotationPoint(69.9213F, -6.0F, -70.7071F);
        baffle.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.0F, 0.7854F, 0.0F);
        cube_r14.cubeList.add(new ModelBox(cube_r14, 120, 189, -2.0F, -72.0F, -1.0F, 3, 72, 3, 0.0F, false));

        cube_r15 = new ModelRenderer(this);
        cube_r15.setRotationPoint(-69.8787F, -6.0F, -70.7071F);
        baffle.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.0F, 0.7854F, 0.0F);
        cube_r15.cubeList.add(new ModelBox(cube_r15, 0, 670, -2.0F, -72.0F, -1.0F, 3, 72, 3, 0.0F, false));

        energy = new ModelRenderer(this);
        energy.setRotationPoint(-62.0F, -36.0F, 87.9784F);
        setRotationAngle(energy, 0.0F, 3.1416F, 0.0F);
        energy.cubeList.add(new ModelBox(energy, 272, 524, 4.0F, 3.0F, -0.0784F, 2, 36, 2, 0.0F, false));
        energy.cubeList.add(new ModelBox(energy, 544, 210, -3.0F, 3.0F, 2.9216F, 6, 36, 7, 0.0F, false));
        energy.cubeList.add(new ModelBox(energy, 120, 523, -6.0F, 3.0F, 10.9216F, 2, 36, 2, 0.0F, false));
        energy.cubeList.add(new ModelBox(energy, 522, 174, 4.0F, 3.0F, 10.9216F, 2, 36, 2, 0.0F, false));
        energy.cubeList.add(new ModelBox(energy, 478, 521, -6.0F, 3.0F, -0.0784F, 2, 36, 2, 0.0F, false));
        energy.cubeList.add(new ModelBox(energy, 440, 521, -6.0F, 3.0F, 12.9216F, 12, 36, 3, 0.0F, false));

        cube_r16 = new ModelRenderer(this);
        cube_r16.setRotationPoint(0.0F, 8.0F, 0.9216F);
        energy.addChild(cube_r16);
        setRotationAngle(cube_r16, -1.5708F, 0.0F, 0.0F);
        cube_r16.cubeList.add(new ModelBox(cube_r16, 394, 402, -6.0F, -15.0F, -8.0F, 12, 16, 3, 0.0F, false));

        cube_r17 = new ModelRenderer(this);
        cube_r17.setRotationPoint(5.0F, 46.9F, 9.9216F);
        energy.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.0F, -1.5708F, -1.5708F);
        cube_r17.cubeList.add(new ModelBox(cube_r17, 544, 272, -10.0F, -11.0F, -7.9F, 16, 12, 3, 0.0F, false));

        cube_r18 = new ModelRenderer(this);
        cube_r18.setRotationPoint(0.0F, 0.0F, -0.1F);
        energy.addChild(cube_r18);
        setRotationAngle(cube_r18, -0.8421F, 0.0F, 0.0F);
        cube_r18.cubeList.add(new ModelBox(cube_r18, 566, 48, -6.0F, -6.0153F, 0.0153F, 12, 6, 4, 0.0F, false));

        cube_r19 = new ModelRenderer(this);
        cube_r19.setRotationPoint(0.0F, 2.0F, 6.3966F);
        energy.addChild(cube_r19);
        setRotationAngle(cube_r19, -1.5708F, 0.0F, 0.0F);
        cube_r19.cubeList.add(new ModelBox(cube_r19, 64, 559, -6.0F, -10.0F, -6.0F, 12, 12, 4, 0.0F, false));

        energy_in = new ModelRenderer(this);
        energy_in.setRotationPoint(0.0F, 45.4167F, 6.0216F);
        energy.addChild(energy_in);
        setRotationAngle(energy_in, 3.1416F, 0.0F, 0.0F);
        energy_in.cubeList.add(new ModelBox(energy_in, 0, 294, -3.0F, 2.5833F, -3.0F, 6, 1, 6, 0.0F, false));
        energy_in.cubeList.add(new ModelBox(energy_in, 560, 186, -4.0F, 1.5833F, -4.0F, 8, 1, 8, 0.0F, false));
        energy_in.cubeList.add(new ModelBox(energy_in, 544, 396, -5.0F, 3.1333F, -5.0F, 10, 1, 10, 0.0F, false));

        energy2 = new ModelRenderer(this);
        energy2.setRotationPoint(62.0F, -36.0F, 87.9784F);
        setRotationAngle(energy2, 0.0F, 3.1416F, 0.0F);
        energy2.cubeList.add(new ModelBox(energy2, 470, 521, 4.0F, 3.0F, -0.0784F, 2, 36, 2, 0.0F, false));
        energy2.cubeList.add(new ModelBox(energy2, 506, 324, -3.0F, 3.0F, 2.9216F, 6, 36, 7, 0.0F, false));
        energy2.cubeList.add(new ModelBox(energy2, 280, 514, -6.0F, 3.0F, 10.9216F, 2, 36, 2, 0.0F, false));
        energy2.cubeList.add(new ModelBox(energy2, 514, 174, 4.0F, 3.0F, 10.9216F, 2, 36, 2, 0.0F, false));
        energy2.cubeList.add(new ModelBox(energy2, 128, 513, -6.0F, 3.0F, -0.0784F, 2, 36, 2, 0.0F, false));
        energy2.cubeList.add(new ModelBox(energy2, 294, 510, -6.0F, 3.0F, 12.9216F, 12, 36, 3, 0.0F, false));

        cube_r20 = new ModelRenderer(this);
        cube_r20.setRotationPoint(0.0F, 8.0F, 0.9216F);
        energy2.addChild(cube_r20);
        setRotationAngle(cube_r20, -1.5708F, 0.0F, 0.0F);
        cube_r20.cubeList.add(new ModelBox(cube_r20, 285, 380, -6.0F, -15.0F, -8.0F, 12, 16, 3, 0.0F, false));

        cube_r21 = new ModelRenderer(this);
        cube_r21.setRotationPoint(5.0F, 46.9F, 9.9216F);
        energy2.addChild(cube_r21);
        setRotationAngle(cube_r21, 0.0F, -1.5708F, -1.5708F);
        cube_r21.cubeList.add(new ModelBox(cube_r21, 38, 544, -10.0F, -11.0F, -7.9F, 16, 12, 3, 0.0F, false));

        cube_r22 = new ModelRenderer(this);
        cube_r22.setRotationPoint(0.0F, 0.0F, -0.1F);
        energy2.addChild(cube_r22);
        setRotationAngle(cube_r22, -0.8421F, 0.0F, 0.0F);
        cube_r22.cubeList.add(new ModelBox(cube_r22, 557, 347, -6.0F, -6.0153F, 0.0153F, 12, 6, 4, 0.0F, false));

        cube_r23 = new ModelRenderer(this);
        cube_r23.setRotationPoint(0.0F, 2.0F, 6.3966F);
        energy2.addChild(cube_r23);
        setRotationAngle(cube_r23, -1.5708F, 0.0F, 0.0F);
        cube_r23.cubeList.add(new ModelBox(cube_r23, 32, 559, -6.0F, -10.0F, -6.0F, 12, 12, 4, 0.0F, false));

        energy_in2 = new ModelRenderer(this);
        energy_in2.setRotationPoint(0.0F, 45.4167F, 6.0216F);
        energy2.addChild(energy_in2);
        setRotationAngle(energy_in2, 3.1416F, 0.0F, 0.0F);
        energy_in2.cubeList.add(new ModelBox(energy_in2, 240, 287, -3.0F, 2.5833F, -3.0F, 6, 1, 6, 0.0F, false));
        energy_in2.cubeList.add(new ModelBox(energy_in2, 294, 549, -4.0F, 1.5833F, -4.0F, 8, 1, 8, 0.0F, false));
        energy_in2.cubeList.add(new ModelBox(energy_in2, 540, 510, -5.0F, 3.1333F, -5.0F, 10, 1, 10, 0.0F, false));

        fluid = new ModelRenderer(this);
        fluid.setRotationPoint(88.0F, -36.0F, 61.9784F);
        setRotationAngle(fluid, 0.0F, -1.5708F, 0.0F);
        fluid.cubeList.add(new ModelBox(fluid, 332, 510, 4.0F, 3.0F, -0.0784F, 2, 36, 2, 0.0F, false));
        fluid.cubeList.add(new ModelBox(fluid, 148, 499, -3.0F, 3.0F, 2.9216F, 6, 36, 7, 0.0F, false));
        fluid.cubeList.add(new ModelBox(fluid, 324, 510, -6.0F, 3.0F, 10.9216F, 2, 36, 2, 0.0F, false));
        fluid.cubeList.add(new ModelBox(fluid, 478, 459, 4.0F, 3.0F, 10.9216F, 2, 36, 2, 0.0F, false));
        fluid.cubeList.add(new ModelBox(fluid, 128, 475, -6.0F, 3.0F, -0.0784F, 2, 36, 2, 0.0F, false));
        fluid.cubeList.add(new ModelBox(fluid, 440, 459, -6.0F, 3.0F, 12.9216F, 12, 36, 3, 0.0F, false));

        cube_r24 = new ModelRenderer(this);
        cube_r24.setRotationPoint(0.0F, 8.0F, 0.9216F);
        fluid.addChild(cube_r24);
        setRotationAngle(cube_r24, -1.5708F, 0.0F, 0.0F);
        cube_r24.cubeList.add(new ModelBox(cube_r24, 240, 380, -6.0F, -15.0F, -8.0F, 12, 16, 3, 0.0F, false));

        cube_r25 = new ModelRenderer(this);
        cube_r25.setRotationPoint(5.0F, 46.9F, 9.9216F);
        fluid.addChild(cube_r25);
        setRotationAngle(cube_r25, 0.0F, -1.5708F, -1.5708F);
        cube_r25.cubeList.add(new ModelBox(cube_r25, 0, 544, -10.0F, -11.0F, -7.9F, 16, 12, 3, 0.0F, false));

        cube_r26 = new ModelRenderer(this);
        cube_r26.setRotationPoint(0.0F, 0.0F, -0.1F);
        fluid.addChild(cube_r26);
        setRotationAngle(cube_r26, -0.8421F, 0.0F, 0.0F);
        cube_r26.cubeList.add(new ModelBox(cube_r26, 557, 298, -6.0F, -6.0153F, 0.0153F, 12, 6, 4, 0.0F, false));

        cube_r27 = new ModelRenderer(this);
        cube_r27.setRotationPoint(0.0F, 2.0F, 6.3966F);
        fluid.addChild(cube_r27);
        setRotationAngle(cube_r27, -1.5708F, 0.0F, 0.0F);
        cube_r27.cubeList.add(new ModelBox(cube_r27, 0, 559, -6.0F, -10.0F, -6.0F, 12, 12, 4, 0.0F, false));

        fluid_in = new ModelRenderer(this);
        fluid_in.setRotationPoint(0.0F, 45.4167F, 6.0216F);
        fluid.addChild(fluid_in);
        setRotationAngle(fluid_in, 3.1416F, 0.0F, 0.0F);
        fluid_in.cubeList.add(new ModelBox(fluid_in, 74, 287, -3.0F, 2.5833F, -3.0F, 6, 1, 6, 0.0F, false));
        fluid_in.cubeList.add(new ModelBox(fluid_in, 440, 498, -4.0F, 1.5833F, -4.0F, 8, 1, 8, 0.0F, false));
        fluid_in.cubeList.add(new ModelBox(fluid_in, 540, 499, -5.0F, 3.1333F, -5.0F, 10, 1, 10, 0.0F, false));

        gas = new ModelRenderer(this);
        gas.setRotationPoint(88.0F, -36.0F, 29.9784F);
        setRotationAngle(gas, 0.0F, -1.5708F, 0.0F);
        gas.cubeList.add(new ModelBox(gas, 470, 459, 4.0F, 3.0F, -0.0784F, 2, 36, 2, 0.0F, false));
        gas.cubeList.add(new ModelBox(gas, 294, 448, -3.0F, 3.0F, 2.9216F, 6, 36, 7, 0.0F, false));
        gas.cubeList.add(new ModelBox(gas, 458, 0, -6.0F, 3.0F, 10.9216F, 2, 36, 2, 0.0F, false));
        gas.cubeList.add(new ModelBox(gas, 450, 0, 4.0F, 3.0F, 10.9216F, 2, 36, 2, 0.0F, false));
        gas.cubeList.add(new ModelBox(gas, 186, 437, -6.0F, 3.0F, -0.0784F, 2, 36, 2, 0.0F, false));
        gas.cubeList.add(new ModelBox(gas, 148, 437, -6.0F, 3.0F, 12.9216F, 12, 36, 3, 0.0F, false));

        cube_r28 = new ModelRenderer(this);
        cube_r28.setRotationPoint(0.0F, 8.0F, 0.9216F);
        gas.addChild(cube_r28);
        setRotationAngle(cube_r28, -1.5708F, 0.0F, 0.0F);
        cube_r28.cubeList.add(new ModelBox(cube_r28, 45, 380, -6.0F, -15.0F, -8.0F, 12, 16, 3, 0.0F, false));

        cube_r29 = new ModelRenderer(this);
        cube_r29.setRotationPoint(5.0F, 46.9F, 9.9216F);
        gas.addChild(cube_r29);
        setRotationAngle(cube_r29, 0.0F, -1.5708F, -1.5708F);
        cube_r29.cubeList.add(new ModelBox(cube_r29, 346, 535, -10.0F, -11.0F, -7.9F, 16, 12, 3, 0.0F, false));

        cube_r30 = new ModelRenderer(this);
        cube_r30.setRotationPoint(0.0F, 0.0F, -0.1F);
        gas.addChild(cube_r30);
        setRotationAngle(cube_r30, -0.8421F, 0.0F, 0.0F);
        cube_r30.cubeList.add(new ModelBox(cube_r30, 544, 423, -6.0F, -6.0153F, 0.0153F, 12, 6, 4, 0.0F, false));

        cube_r31 = new ModelRenderer(this);
        cube_r31.setRotationPoint(0.0F, 2.0F, 6.3966F);
        gas.addChild(cube_r31);
        setRotationAngle(cube_r31, -1.5708F, 0.0F, 0.0F);
        cube_r31.cubeList.add(new ModelBox(cube_r31, 492, 546, -6.0F, -10.0F, -6.0F, 12, 12, 4, 0.0F, false));

        gas_in2 = new ModelRenderer(this);
        gas_in2.setRotationPoint(0.0F, 45.4167F, 6.0216F);
        gas.addChild(gas_in2);
        setRotationAngle(gas_in2, 3.1416F, 0.0F, 0.0F);
        gas_in2.cubeList.add(new ModelBox(gas_in2, 0, 287, -3.0F, 2.5833F, -3.0F, 6, 1, 6, 0.0F, false));
        gas_in2.cubeList.add(new ModelBox(gas_in2, 148, 476, -4.0F, 1.5833F, -4.0F, 8, 1, 8, 0.0F, false));
        gas_in2.cubeList.add(new ModelBox(gas_in2, 504, 295, -5.0F, 3.1333F, -5.0F, 10, 1, 10, 0.0F, false));

        fluid2 = new ModelRenderer(this);
        fluid2.setRotationPoint(-88.0F, -36.0F, 61.9784F);
        setRotationAngle(fluid2, 0.0F, 1.5708F, 0.0F);
        fluid2.cubeList.add(new ModelBox(fluid2, 178, 437, 4.0F, 3.0F, -0.0784F, 2, 36, 2, 0.0F, false));
        fluid2.cubeList.add(new ModelBox(fluid2, 0, 437, -3.0F, 3.0F, 2.9216F, 6, 36, 7, 0.0F, false));
        fluid2.cubeList.add(new ModelBox(fluid2, 130, 437, -6.0F, 3.0F, 10.9216F, 2, 36, 2, 0.0F, false));
        fluid2.cubeList.add(new ModelBox(fluid2, 34, 437, 4.0F, 3.0F, 10.9216F, 2, 36, 2, 0.0F, false));
        fluid2.cubeList.add(new ModelBox(fluid2, 26, 437, -6.0F, 3.0F, -0.0784F, 2, 36, 2, 0.0F, false));
        fluid2.cubeList.add(new ModelBox(fluid2, 420, 0, -6.0F, 3.0F, 12.9216F, 12, 36, 3, 0.0F, false));

        cube_r32 = new ModelRenderer(this);
        cube_r32.setRotationPoint(0.0F, 8.0F, 0.9216F);
        fluid2.addChild(cube_r32);
        setRotationAngle(cube_r32, -1.5708F, 0.0F, 0.0F);
        cube_r32.cubeList.add(new ModelBox(cube_r32, 0, 380, -6.0F, -15.0F, -8.0F, 12, 16, 3, 0.0F, false));

        cube_r33 = new ModelRenderer(this);
        cube_r33.setRotationPoint(5.0F, 46.9F, 9.9216F);
        fluid2.addChild(cube_r33);
        setRotationAngle(cube_r33, 0.0F, -1.5708F, -1.5708F);
        cube_r33.cubeList.add(new ModelBox(cube_r33, 528, 484, -10.0F, -11.0F, -7.9F, 16, 12, 3, 0.0F, false));

        cube_r34 = new ModelRenderer(this);
        cube_r34.setRotationPoint(0.0F, 0.0F, -0.1F);
        fluid2.addChild(cube_r34);
        setRotationAngle(cube_r34, -0.8421F, 0.0F, 0.0F);
        cube_r34.cubeList.add(new ModelBox(cube_r34, 435, 264, -6.0F, -6.0153F, 0.0153F, 12, 6, 4, 0.0F, false));

        cube_r35 = new ModelRenderer(this);
        cube_r35.setRotationPoint(0.0F, 2.0F, 6.3966F);
        fluid2.addChild(cube_r35);
        setRotationAngle(cube_r35, -1.5708F, 0.0F, 0.0F);
        cube_r35.cubeList.add(new ModelBox(cube_r35, 544, 407, -6.0F, -10.0F, -6.0F, 12, 12, 4, 0.0F, false));

        fluid_in2 = new ModelRenderer(this);
        fluid_in2.setRotationPoint(0.0F, 45.4167F, 6.0216F);
        fluid2.addChild(fluid_in2);
        setRotationAngle(fluid_in2, 3.1416F, 0.0F, 0.0F);
        fluid_in2.cubeList.add(new ModelBox(fluid_in2, 110, 268, -3.0F, 2.5833F, -3.0F, 6, 1, 6, 0.0F, false));
        fluid_in2.cubeList.add(new ModelBox(fluid_in2, 352, 421, -4.0F, 1.5833F, -4.0F, 8, 1, 8, 0.0F, false));
        fluid_in2.cubeList.add(new ModelBox(fluid_in2, 346, 448, -5.0F, 3.1333F, -5.0F, 10, 1, 10, 0.0F, false));

        gas2 = new ModelRenderer(this);
        gas2.setRotationPoint(-88.0F, -36.0F, 29.9784F);
        setRotationAngle(gas2, 0.0F, 1.5708F, 0.0F);
        gas2.cubeList.add(new ModelBox(gas2, 428, 295, 4.0F, 3.0F, -0.0784F, 2, 36, 2, 0.0F, false));
        gas2.cubeList.add(new ModelBox(gas2, 394, 295, -3.0F, 3.0F, 2.9216F, 6, 36, 7, 0.0F, false));
        gas2.cubeList.add(new ModelBox(gas2, 420, 295, -6.0F, 3.0F, 10.9216F, 2, 36, 2, 0.0F, false));
        gas2.cubeList.add(new ModelBox(gas2, 376, 380, 4.0F, 3.0F, 10.9216F, 2, 36, 2, 0.0F, false));
        gas2.cubeList.add(new ModelBox(gas2, 110, 192, -6.0F, 3.0F, -0.0784F, 2, 36, 2, 0.0F, false));
        gas2.cubeList.add(new ModelBox(gas2, 90, 380, -6.0F, 3.0F, 12.9216F, 12, 36, 3, 0.0F, false));

        cube_r36 = new ModelRenderer(this);
        cube_r36.setRotationPoint(0.0F, 8.0F, 0.9216F);
        gas2.addChild(cube_r36);
        setRotationAngle(cube_r36, -1.5708F, 0.0F, 0.0F);
        cube_r36.cubeList.add(new ModelBox(cube_r36, 348, 319, -6.0F, -15.0F, -8.0F, 12, 16, 3, 0.0F, false));

        cube_r37 = new ModelRenderer(this);
        cube_r37.setRotationPoint(5.0F, 46.9F, 9.9216F);
        gas2.addChild(cube_r37);
        setRotationAngle(cube_r37, 0.0F, -1.5708F, -1.5708F);
        cube_r37.cubeList.add(new ModelBox(cube_r37, 102, 18, -10.0F, -11.0F, -7.9F, 16, 12, 3, 0.0F, false));

        cube_r38 = new ModelRenderer(this);
        cube_r38.setRotationPoint(0.0F, 0.0F, -0.1F);
        gas2.addChild(cube_r38);
        setRotationAngle(cube_r38, -0.8421F, 0.0F, 0.0F);
        cube_r38.cubeList.add(new ModelBox(cube_r38, 348, 297, -6.0F, -6.0153F, 0.0153F, 12, 6, 4, 0.0F, false));

        cube_r39 = new ModelRenderer(this);
        cube_r39.setRotationPoint(0.0F, 2.0F, 6.3966F);
        gas2.addChild(cube_r39);
        setRotationAngle(cube_r39, -1.5708F, 0.0F, 0.0F);
        cube_r39.cubeList.add(new ModelBox(cube_r39, 240, 399, -6.0F, -10.0F, -6.0F, 12, 12, 4, 0.0F, false));

        gas_in3 = new ModelRenderer(this);
        gas_in3.setRotationPoint(0.0F, 45.4167F, 6.0216F);
        gas2.addChild(gas_in3);
        setRotationAngle(gas_in3, 3.1416F, 0.0F, 0.0F);
        gas_in3.cubeList.add(new ModelBox(gas_in3, 57, 178, -3.0F, 2.5833F, -3.0F, 6, 1, 6, 0.0F, false));
        gas_in3.cubeList.add(new ModelBox(gas_in3, 350, 412, -4.0F, 1.5833F, -4.0F, 8, 1, 8, 0.0F, false));
        gas_in3.cubeList.add(new ModelBox(gas_in3, 200, 437, -5.0F, 3.1333F, -5.0F, 10, 1, 10, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 0, 142, 70.0F, -78.0F, 17.0F, 2, 61, 53, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -72.0F, -78.0F, 17.0F, 2, 61, 53, 0.0F, false));

        cube_r40 = new ModelRenderer(this);
        cube_r40.setRotationPoint(96.0F, -39.0F, 73.0F);
        bone.addChild(cube_r40);
        setRotationAngle(cube_r40, 0.0F, -1.5708F, 0.0F);
        cube_r40.cubeList.add(new ModelBox(cube_r40, 420, 0, -3.0F, -39.0F, 26.0F, 2, 61, 46, 0.0F, false));

        cube_r41 = new ModelRenderer(this);
        cube_r41.setRotationPoint(2.0F, -39.0F, -69.0F);
        bone.addChild(cube_r41);
        setRotationAngle(cube_r41, 0.0F, -1.5708F, 0.0F);
        cube_r41.cubeList.add(new ModelBox(cube_r41, 402, 145, -3.0F, -39.0F, 26.0F, 2, 61, 46, 0.0F, false));

        cube_r42 = new ModelRenderer(this);
        cube_r42.setRotationPoint(96.0F, -39.0F, -69.0F);
        bone.addChild(cube_r42);
        setRotationAngle(cube_r42, 0.0F, -1.5708F, 0.0F);
        cube_r42.cubeList.add(new ModelBox(cube_r42, 394, 295, -3.0F, -39.0F, 26.0F, 2, 61, 46, 0.0F, false));

        cube_r43 = new ModelRenderer(this);
        cube_r43.setRotationPoint(2.0F, -39.0F, 73.0F);
        bone.addChild(cube_r43);
        setRotationAngle(cube_r43, 0.0F, -1.5708F, 0.0F);
        cube_r43.cubeList.add(new ModelBox(cube_r43, 0, 437, -3.0F, -39.0F, 26.0F, 2, 61, 46, 0.0F, false));

        conveyorbelts = new ModelRenderer(this);
        conveyorbelts.setRotationPoint(0.0F, 24.0F, 0.0F);
        conveyorbelts.cubeList.add(new ModelBox(conveyorbelts, 0, 287, -24.0F, -23.0F, -72.0F, 48, 6, 144, 0.0F, false));
        conveyorbelts.cubeList.add(new ModelBox(conveyorbelts, 0, 437, -28.0F, -25.0F, -70.0F, 4, 8, 140, 0.0F, false));
        conveyorbelts.cubeList.add(new ModelBox(conveyorbelts, 396, 147, 24.0F, -25.0F, -70.0F, 4, 8, 140, 0.0F, false));

        Roboticarm = new ModelRenderer(this);
        Roboticarm.setRotationPoint(0.0F, 24.0F, 0.0F);
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 346, 459, 39.0F, -26.0F, 25.0F, 20, 9, 20, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 200, 524, 44.0F, -31.0F, 30.0F, 10, 5, 10, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 492, 521, 39.0F, -35.0F, 25.0F, 20, 5, 20, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 402, 264, 43.5F, -36.0F, 29.5F, 11, 1, 11, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 240, 319, 50.0F, -60.0F, 10.0F, 5, 8, 24, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 274, 319, 42.0F, -60.0F, 10.0F, 8, 10, 8, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 348, 287, 40.0F, -50.0F, 10.0F, 12, 2, 8, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 33, 174, 50.0F, -48.0F, 10.0F, 2, 3, 8, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 57, 167, 40.0F, -48.0F, 10.0F, 2, 3, 8, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
        Roboticarm.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 45, 399, 45.0F, -44.0F, 31.0F, 8, 8, 8, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 314, 319, 40.0F, -44.0F, 31.0F, 5, 8, 24, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone2.addChild(bone3);


        baseTurn2_r1 = new ModelRenderer(this);
        baseTurn2_r1.setRotationPoint(48.0F, -46.0F, 33.0F);
        bone3.addChild(baseTurn2_r1);
        setRotationAngle(baseTurn2_r1, -0.6109F, 0.0F, 0.0F);
        baseTurn2_r1.cubeList.add(new ModelBox(baseTurn2_r1, 45, 388, -3.0F, -10.0F, -13.0F, 5, 8, 35, 0.0F, false));

        Roboticarm2 = new ModelRenderer(this);
        Roboticarm2.setRotationPoint(-96.0F, 24.0F, 0.0F);
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 452, 145, 39.0F, -26.0F, 25.0F, 20, 9, 20, 0.0F, false));
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 470, 25, 44.0F, -31.0F, 30.0F, 10, 5, 10, 0.0F, false));
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 346, 510, 39.0F, -35.0F, 25.0F, 20, 5, 20, 0.0F, false));
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 332, 392, 43.5F, -36.0F, 29.5F, 11, 1, 11, 0.0F, false));
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 0, 319, 50.0F, -60.0F, 10.0F, 5, 8, 24, 0.0F, false));
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 99, 167, 42.0F, -60.0F, 10.0F, 8, 10, 8, 0.0F, false));
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 0, 9, 44.0F, -50.0F, 12.0F, 4, 1, 4, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, 0.0F, 0.0F);
        Roboticarm2.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 0, 399, 45.0F, -44.0F, 31.0F, 8, 8, 8, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 74, 319, 40.0F, -44.0F, 31.0F, 5, 8, 24, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone4.addChild(bone6);


        baseTurn2_r2 = new ModelRenderer(this);
        baseTurn2_r2.setRotationPoint(48.0F, -46.0F, 33.0F);
        bone6.addChild(baseTurn2_r2);
        setRotationAngle(baseTurn2_r2, -0.6109F, 0.0F, 0.0F);
        baseTurn2_r2.cubeList.add(new ModelBox(baseTurn2_r2, 240, 380, -3.0F, -10.0F, -13.0F, 5, 8, 35, 0.0F, false));

        Roboticarm3 = new ModelRenderer(this);
        Roboticarm3.setRotationPoint(0.0F, 24.0F, -70.0F);
        setRotationAngle(Roboticarm3, 0.0F, 3.1416F, 0.0F);
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 200, 448, -57.0F, -26.0F, -45.0F, 20, 9, 20, 0.0F, false));
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 50, 466, -52.0F, -31.0F, -40.0F, 10, 5, 10, 0.0F, false));
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 200, 499, -57.0F, -35.0F, -45.0F, 20, 5, 20, 0.0F, false));
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 332, 380, -52.5F, -36.0F, -40.5F, 11, 1, 11, 0.0F, false));
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 240, 287, -46.0F, -60.0F, -60.0F, 5, 8, 24, 0.0F, false));
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 99, 142, -54.0F, -60.0F, -60.0F, 8, 10, 8, 0.0F, false));
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 38, 16, -51.0F, -50.0F, -57.0F, 2, 10, 2, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(-96.0F, 0.0F, -70.0F);
        Roboticarm3.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 21, 142, 45.0F, -44.0F, 31.0F, 8, 8, 8, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 314, 287, 40.0F, -44.0F, 31.0F, 5, 8, 24, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone7.addChild(bone8);


        baseTurn2_r3 = new ModelRenderer(this);
        baseTurn2_r3.setRotationPoint(48.0F, -46.0F, 33.0F);
        bone8.addChild(baseTurn2_r3);
        setRotationAngle(baseTurn2_r3, -0.6109F, 0.0F, 0.0F);
        baseTurn2_r3.cubeList.add(new ModelBox(baseTurn2_r3, 0, 380, -3.0F, -10.0F, -13.0F, 5, 8, 35, 0.0F, false));

        Roboticarm4 = new ModelRenderer(this);
        Roboticarm4.setRotationPoint(-94.0F, 24.0F, -70.0F);
        setRotationAngle(Roboticarm4, 0.0F, 3.1416F, 0.0F);
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 444, 295, -57.0F, -26.0F, -45.0F, 20, 9, 20, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 452, 174, -52.0F, -31.0F, -40.0F, 10, 5, 10, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 492, 459, -57.0F, -35.0F, -45.0F, 20, 5, 20, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 0, 182, -52.5F, -36.0F, -40.5F, 11, 1, 11, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 0, 287, -46.0F, -60.0F, -60.0F, 5, 8, 24, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 57, 0, -54.0F, -60.0F, -60.0F, 8, 10, 8, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 21, 0, -43.0F, -58.0F, -60.0F, 8, 8, 8, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 57, 43, -56.0F, -50.0F, -60.0F, 12, 2, 8, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 57, 153, -46.0F, -48.0F, -60.0F, 2, 3, 8, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 57, 142, -56.0F, -48.0F, -60.0F, 2, 3, 8, 0.0F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(-96.0F, 0.0F, -70.0F);
        Roboticarm4.addChild(bone9);
        bone9.cubeList.add(new ModelBox(bone9, 57, 18, 45.0F, -44.0F, 31.0F, 8, 8, 8, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 74, 287, 40.0F, -44.0F, 31.0F, 5, 8, 24, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone9.addChild(bone10);


        baseTurn2_r4 = new ModelRenderer(this);
        baseTurn2_r4.setRotationPoint(48.0F, -46.0F, 33.0F);
        bone10.addChild(baseTurn2_r4);
        setRotationAngle(baseTurn2_r4, -0.6109F, 0.0F, 0.0F);
        baseTurn2_r4.cubeList.add(new ModelBox(baseTurn2_r4, 57, 0, -3.0F, -10.0F, -13.0F, 5, 8, 35, 0.0F, false));

        platform = new ModelRenderer(this);
        platform.setRotationPoint(-94.0F, 24.0F, -35.0F);
        setRotationAngle(platform, 0.0F, 3.1416F, 0.0F);
        platform.cubeList.add(new ModelBox(platform, 441, 402, -57.0F, -26.0F, -45.0F, 20, 9, 20, 0.0F, false));
        platform.cubeList.add(new ModelBox(platform, 444, 324, -52.0F, -31.0F, -40.0F, 10, 5, 10, 0.0F, false));
        platform.cubeList.add(new ModelBox(platform, 470, 0, -57.0F, -35.0F, -45.0F, 20, 5, 20, 0.0F, false));

        platform2 = new ModelRenderer(this);
        platform2.setRotationPoint(2.0F, 24.0F, -35.0F);
        setRotationAngle(platform2, 0.0F, 3.1416F, 0.0F);
        platform2.cubeList.add(new ModelBox(platform2, 50, 437, -57.0F, -26.0F, -45.0F, 20, 9, 20, 0.0F, false));
        platform2.cubeList.add(new ModelBox(platform2, 320, 412, -52.0F, -31.0F, -40.0F, 10, 5, 10, 0.0F, false));
        platform2.cubeList.add(new ModelBox(platform2, 467, 107, -57.0F, -35.0F, -45.0F, 20, 5, 20, 0.0F, false));

        glass = new ModelRenderer(this);
        glass.setRotationPoint(0.0F, 24.0F, 196.0F);


        glass1 = new ModelRenderer(this);
        glass1.setRotationPoint(0.0F, 0.0F, 0.0F);
        glass.addChild(glass1);
        setRotationAngle(glass1, 0.0F, 3.1416F, 0.0F);
        glass1.cubeList.add(new ModelBox(glass1, 174, 499, -67.0F, -59.0F, 108.92F, 10, 38, 1, 0.0F, false));
        glass1.cubeList.add(new ModelBox(glass1, 544, 145, 56.5F, -59.0F, 109.42F, 1, 38, 11, 0.0F, false));
        glass1.cubeList.add(new ModelBox(glass1, 544, 298, 66.5F, -59.0F, 109.42F, 1, 38, 11, 0.0F, false));
        glass1.cubeList.add(new ModelBox(glass1, 406, 488, 57.0F, -59.0F, 108.92F, 10, 38, 1, 0.0F, false));
        glass1.cubeList.add(new ModelBox(glass1, 540, 546, -57.5F, -59.0F, 109.42F, 1, 38, 11, 0.0F, false));
        glass1.cubeList.add(new ModelBox(glass1, 544, 347, -67.5F, -59.0F, 109.42F, 1, 38, 11, 0.0F, false));

        glass2 = new ModelRenderer(this);
        glass2.setRotationPoint(196.0F, 0.0F, -196.0F);
        glass.addChild(glass2);
        setRotationAngle(glass2, 0.0F, -1.5708F, 0.0F);
        glass2.cubeList.add(new ModelBox(glass2, 394, 535, 56.5F, -59.0F, 109.42F, 1, 38, 11, 0.0F, false));
        glass2.cubeList.add(new ModelBox(glass2, 320, 448, 25.0F, -59.0F, 108.92F, 10, 38, 1, 0.0F, false));
        glass2.cubeList.add(new ModelBox(glass2, 96, 523, 34.5F, -59.0F, 109.42F, 1, 38, 11, 0.0F, false));
        glass2.cubeList.add(new ModelBox(glass2, 248, 524, 24.5F, -59.0F, 109.42F, 1, 38, 11, 0.0F, false));
        glass2.cubeList.add(new ModelBox(glass2, 527, 74, 66.5F, -59.0F, 109.42F, 1, 38, 11, 0.0F, false));
        glass2.cubeList.add(new ModelBox(glass2, 260, 477, 57.0F, -59.0F, 108.92F, 10, 38, 1, 0.0F, false));

        glass3 = new ModelRenderer(this);
        glass3.setRotationPoint(-196.0F, 0.0F, -104.0F);
        glass.addChild(glass3);
        setRotationAngle(glass3, 0.0F, 1.5708F, 0.0F);
        glass3.cubeList.add(new ModelBox(glass3, 516, 25, 56.5F, -59.0F, 109.42F, 1, 38, 11, 0.0F, false));
        glass3.cubeList.add(new ModelBox(glass3, 426, 145, 25.0F, -59.0F, 108.92F, 10, 38, 1, 0.0F, false));
        glass3.cubeList.add(new ModelBox(glass3, 503, 220, 34.5F, -59.0F, 109.42F, 1, 38, 11, 0.0F, false));
        glass3.cubeList.add(new ModelBox(glass3, 501, 370, 24.5F, -59.0F, 109.42F, 1, 38, 11, 0.0F, false));
        glass3.cubeList.add(new ModelBox(glass3, 402, 142, 66.5F, -59.0F, 109.42F, 1, 38, 11, 0.0F, false));
        glass3.cubeList.add(new ModelBox(glass3, 120, 380, 57.0F, -59.0F, 108.92F, 10, 38, 1, 0.0F, false));

        doorpanels = new ModelRenderer(this);
        doorpanels.setRotationPoint(47.0F, -99.0F, -70.0F);


        cube_r44 = new ModelRenderer(this);
        cube_r44.setRotationPoint(-45.0F, 76.0F, 141.0F);
        doorpanels.addChild(cube_r44);
        setRotationAngle(cube_r44, 0.0F, -1.5708F, 0.0F);
        cube_r44.cubeList.add(new ModelBox(cube_r44, 112, 466, -5.0F, -31.0F, 26.0F, 4, 53, 4, 0.0F, false));

        cube_r45 = new ModelRenderer(this);
        cube_r45.setRotationPoint(7.0F, 76.0F, 141.0F);
        doorpanels.addChild(cube_r45);
        setRotationAngle(cube_r45, 0.0F, -1.5708F, 0.0F);
        cube_r45.cubeList.add(new ModelBox(cube_r45, 96, 466, -5.0F, -31.0F, 26.0F, 4, 53, 4, 0.0F, false));

        cube_r46 = new ModelRenderer(this);
        cube_r46.setRotationPoint(-45.0F, 76.0F, 5.0F);
        doorpanels.addChild(cube_r46);
        setRotationAngle(cube_r46, 0.0F, -1.5708F, 0.0F);
        cube_r46.cubeList.add(new ModelBox(cube_r46, 490, 324, -5.0F, -31.0F, 26.0F, 4, 53, 4, 0.0F, false));

        cube_r47 = new ModelRenderer(this);
        cube_r47.setRotationPoint(7.0F, 76.0F, 5.0F);
        doorpanels.addChild(cube_r47);
        setRotationAngle(cube_r47, 0.0F, -1.5708F, 0.0F);
        cube_r47.cubeList.add(new ModelBox(cube_r47, 498, 174, -5.0F, -31.0F, 26.0F, 4, 53, 4, 0.0F, false));

        cube_r48 = new ModelRenderer(this);
        cube_r48.setRotationPoint(3.0F, 37.0F, 143.0F);
        doorpanels.addChild(cube_r48);
        setRotationAngle(cube_r48, 0.0F, -1.5708F, 0.0F);
        cube_r48.cubeList.add(new ModelBox(cube_r48, 544, 148, -3.0F, 8.0F, 26.0F, 2, 14, 48, 0.0F, false));

        cube_r49 = new ModelRenderer(this);
        cube_r49.setRotationPoint(3.0F, 37.0F, 1.0F);
        doorpanels.addChild(cube_r49);
        setRotationAngle(cube_r49, 0.0F, -1.5708F, 0.0F);
        cube_r49.cubeList.add(new ModelBox(cube_r49, 544, 210, -3.0F, 8.0F, 26.0F, 2, 14, 48, 0.0F, false));

        door = new ModelRenderer(this);
        door.setRotationPoint(0.0F, 25.0F, 2.0F);


        cube_r50 = new ModelRenderer(this);
        cube_r50.setRotationPoint(50.0F, -74.0F, 69.0F);
        door.addChild(cube_r50);
        setRotationAngle(cube_r50, 0.0F, -1.5708F, 0.0F);
        cube_r50.cubeList.add(new ModelBox(cube_r50, 440, 459, -3.0F, 8.0F, 26.0F, 2, 14, 48, 0.0F, false));

        cube_r51 = new ModelRenderer(this);
        cube_r51.setRotationPoint(50.0F, -74.0F, -69.0F);
        door.addChild(cube_r51);
        setRotationAngle(cube_r51, 0.0F, -1.5708F, 0.0F);
        cube_r51.cubeList.add(new ModelBox(cube_r51, 440, 521, -3.0F, 8.0F, 26.0F, 2, 14, 48, 0.0F, false));

        door2 = new ModelRenderer(this);
        door2.setRotationPoint(0.0F, 25.0F, 140.0F);


        cube_r52 = new ModelRenderer(this);
        cube_r52.setRotationPoint(50.0F, -60.0F, -69.0F);
        door2.addChild(cube_r52);
        setRotationAngle(cube_r52, 0.0F, -1.5708F, 0.0F);
        cube_r52.cubeList.add(new ModelBox(cube_r52, 294, 448, -3.0F, 8.0F, 26.0F, 2, 14, 48, 0.0F, false));

        cube_r53 = new ModelRenderer(this);
        cube_r53.setRotationPoint(50.0F, -60.0F, -207.0F);
        door2.addChild(cube_r53);
        setRotationAngle(cube_r53, 0.0F, -1.5708F, 0.0F);
        cube_r53.cubeList.add(new ModelBox(cube_r53, 294, 510, -3.0F, 8.0F, 26.0F, 2, 14, 48, 0.0F, false));

        door3 = new ModelRenderer(this);
        door3.setRotationPoint(0.0F, 25.0F, 140.0F);


        cube_r54 = new ModelRenderer(this);
        cube_r54.setRotationPoint(50.0F, -46.0F, -69.0F);
        door3.addChild(cube_r54);
        setRotationAngle(cube_r54, 0.0F, -1.5708F, 0.0F);
        cube_r54.cubeList.add(new ModelBox(cube_r54, 148, 437, -3.0F, 8.0F, 26.0F, 2, 14, 48, 0.0F, false));

        cube_r55 = new ModelRenderer(this);
        cube_r55.setRotationPoint(50.0F, -46.0F, -207.0F);
        door3.addChild(cube_r55);
        setRotationAngle(cube_r55, 0.0F, -1.5708F, 0.0F);
        cube_r55.cubeList.add(new ModelBox(cube_r55, 148, 499, -3.0F, 8.0F, 26.0F, 2, 14, 48, 0.0F, false));

        side3 = new ModelRenderer(this);
        side3.setRotationPoint(0.0F, 24.0F, -92.0F);
        side3.cubeList.add(new ModelBox(side3, 502, 605, 70.0F, -36.0F, 22.0F, 2, 19, 87, 0.0F, false));
        side3.cubeList.add(new ModelBox(side3, 324, 602, 70.0F, -78.0F, 22.0F, 2, 19, 87, 0.0F, false));
        side3.cubeList.add(new ModelBox(side3, 146, 591, -72.0F, -36.0F, 22.0F, 2, 19, 87, 0.0F, false));
        side3.cubeList.add(new ModelBox(side3, 586, 491, -72.0F, -78.0F, 22.0F, 2, 19, 87, 0.0F, false));
        side3.cubeList.add(new ModelBox(side3, 0, 142, -72.0F, -59.0F, 22.0F, 2, 23, 17, 0.0F, false));
        side3.cubeList.add(new ModelBox(side3, 0, 0, 70.0F, -59.0F, 22.0F, 2, 23, 17, 0.0F, false));

        glass4 = new ModelRenderer(this);
        glass4.setRotationPoint(-58.0F, 1.0F, 0.0F);
        glass4.cubeList.add(new ModelBox(glass4, 240, 287, -14.0F, -36.0F, -53.0F, 2, 23, 70, 0.0F, false));
        glass4.cubeList.add(new ModelBox(glass4, 0, 287, 128.0F, -36.0F, -53.0F, 2, 23, 70, 0.0F, false));

        Warninglights = new ModelRenderer(this);
        Warninglights.setRotationPoint(0.0F, -42.0F, -60.0F);
        Warninglights.cubeList.add(new ModelBox(Warninglights, 89, 43, 58.0F, -16.0F, -4.0F, 5, 2, 5, 0.0F, false));
        Warninglights.cubeList.add(new ModelBox(Warninglights, 38, 35, 59.5F, -24.0F, -2.25F, 2, 8, 2, 0.0F, false));
        Warninglights.cubeList.add(new ModelBox(Warninglights, 0, 0, 59.0F, -30.0F, -2.75F, 3, 6, 3, 0.0F, false));

        pm = new ModelRenderer(this);
        pm.setRotationPoint(54.9213F, -16.75F, -76.8713F);


        cube_r56 = new ModelRenderer(this);
        cube_r56.setRotationPoint(-1.0F, 24.0F, -4.75F);
        pm.addChild(cube_r56);
        setRotationAngle(cube_r56, 0.3491F, 0.0F, 0.0F);
        cube_r56.cubeList.add(new ModelBox(cube_r56, 402, 252, -16.0F, -0.5F, -5.5F, 26, 1, 11, 0.0F, false));

        cube_r57 = new ModelRenderer(this);
        cube_r57.setRotationPoint(-8.0F, 0.0F, 0.0F);
        pm.addChild(cube_r57);
        setRotationAngle(cube_r57, 1.9635F, 0.0F, 0.0F);
        cube_r57.cubeList.add(new ModelBox(cube_r57, 0, 40, -4.0F, -0.5F, -5.5F, 14, 1, 10, 0.0F, false));

        cube_r58 = new ModelRenderer(this);
        cube_r58.setRotationPoint(-16.0F, -5.9F, 3.85F);
        pm.addChild(cube_r58);
        setRotationAngle(cube_r58, 1.5708F, 0.0F, 0.0F);
        cube_r58.cubeList.add(new ModelBox(cube_r58, 57, 43, 8.0F, -3.5F, -5.5F, 2, 4, 2, 0.0F, false));
        cube_r58.cubeList.add(new ModelBox(cube_r58, 81, 0, 12.0F, -3.5F, -5.5F, 2, 4, 2, 0.0F, false));

        cube_r59 = new ModelRenderer(this);
        cube_r59.setRotationPoint(-1.5F, 20.45F, -6.15F);
        pm.addChild(cube_r59);
        setRotationAngle(cube_r59, 1.5708F, 0.0F, 0.0F);
        cube_r59.cubeList.add(new ModelBox(cube_r59, 62, 265, -16.0F, -2.5F, -5.5F, 26, 8, 1, 0.0F, false));

        cube_r60 = new ModelRenderer(this);
        cube_r60.setRotationPoint(0.0F, 19.425F, 1.6F);
        pm.addChild(cube_r60);
        setRotationAngle(cube_r60, 1.5708F, 0.0F, 0.0F);
        cube_r60.cubeList.add(new ModelBox(cube_r60, 0, 265, -18.0F, -2.5F, -6.5F, 28, 3, 3, 0.0F, false));

        cube_r61 = new ModelRenderer(this);
        cube_r61.setRotationPoint(-13.0F, 19.35F, 3.85F);
        pm.addChild(cube_r61);
        setRotationAngle(cube_r61, 1.5708F, 0.0F, 0.0F);
        cube_r61.cubeList.add(new ModelBox(cube_r61, 38, 28, 6.0F, -4.5F, -5.5F, 4, 5, 2, 0.0F, false));

        cube_r62 = new ModelRenderer(this);
        cube_r62.setRotationPoint(-16.0F, -3.15F, 3.85F);
        pm.addChild(cube_r62);
        setRotationAngle(cube_r62, 1.5708F, 0.0F, 0.0F);
        cube_r62.cubeList.add(new ModelBox(cube_r62, 81, 18, 8.0F, -2.5F, -5.5F, 2, 3, 2, 0.0F, false));
        cube_r62.cubeList.add(new ModelBox(cube_r62, 0, 114, 12.0F, -2.5F, -5.5F, 2, 3, 2, 0.0F, false));

        cube_r63 = new ModelRenderer(this);
        cube_r63.setRotationPoint(-11.0F, 19.0F, 4.25F);
        pm.addChild(cube_r63);
        setRotationAngle(cube_r63, 1.5708F, 0.0F, 0.0F);
        cube_r63.cubeList.add(new ModelBox(cube_r63, 394, 402, 2.0F, -0.5F, -5.5F, 8, 1, 31, 0.0F, false));

        cube_r64 = new ModelRenderer(this);
        cube_r64.setRotationPoint(-9.0F, 12.0F, 3.25F);
        pm.addChild(cube_r64);
        setRotationAngle(cube_r64, 1.5708F, 0.0F, 0.0F);
        cube_r64.cubeList.add(new ModelBox(cube_r64, 96, 119, -2.0F, -0.5F, -5.5F, 12, 1, 8, 0.0F, false));

        cube_r65 = new ModelRenderer(this);
        cube_r65.setRotationPoint(-9.0F, -1.25F, 0.5F);
        pm.addChild(cube_r65);
        setRotationAngle(cube_r65, 1.9635F, 0.0F, 0.0F);
        cube_r65.cubeList.add(new ModelBox(cube_r65, 86, 256, -2.0F, -0.5F, -5.5F, 12, 1, 8, 0.0F, false));

        cube_r66 = new ModelRenderer(this);
        cube_r66.setRotationPoint(-8.0F, 13.0F, 2.25F);
        pm.addChild(cube_r66);
        setRotationAngle(cube_r66, 1.5708F, 0.0F, 0.0F);
        cube_r66.cubeList.add(new ModelBox(cube_r66, 90, 128, -4.0F, -0.5F, -5.5F, 14, 1, 10, 0.0F, false));

        Leftscreen = new ModelRenderer(this);
        Leftscreen.setRotationPoint(45.0787F, -22.5F, 75.3713F);
        pm.addChild(Leftscreen);
        setRotationAngle(Leftscreen, 0.0F, 0.0F, -3.1416F);


        cube_r67 = new ModelRenderer(this);
        cube_r67.setRotationPoint(54.6713F, -34.9F, -72.5213F);
        Leftscreen.addChild(cube_r67);
        setRotationAngle(cube_r67, 1.5708F, 0.0F, 0.0F);
        cube_r67.cubeList.add(new ModelBox(cube_r67, 45, 0, 8.0F, -4.5F, -5.5F, 2, 5, 2, 0.0F, false));

        cube_r68 = new ModelRenderer(this);
        cube_r68.setRotationPoint(54.6713F, -26.4F, -72.5213F);
        Leftscreen.addChild(cube_r68);
        setRotationAngle(cube_r68, 1.5708F, 0.0F, 0.0F);
        cube_r68.cubeList.add(new ModelBox(cube_r68, 57, 0, 8.0F, -4.5F, -5.5F, 2, 5, 2, 0.0F, false));

        cube_r69 = new ModelRenderer(this);
        cube_r69.setRotationPoint(54.6713F, -43.4F, -72.5213F);
        Leftscreen.addChild(cube_r69);
        setRotationAngle(cube_r69, 1.5708F, 0.0F, 0.0F);
        cube_r69.cubeList.add(new ModelBox(cube_r69, 57, 18, 8.0F, -4.5F, -5.5F, 2, 5, 2, 0.0F, false));

        cube_r70 = new ModelRenderer(this);
        cube_r70.setRotationPoint(56.9213F, -21.75F, -72.1213F);
        Leftscreen.addChild(cube_r70);
        setRotationAngle(cube_r70, 1.5708F, 0.0F, 0.0F);
        cube_r70.cubeList.add(new ModelBox(cube_r70, 420, 107, 2.0F, -0.5F, -5.5F, 8, 1, 31, 0.0F, false));

        cube_r71 = new ModelRenderer(this);
        cube_r71.setRotationPoint(62.1713F, -30.75F, -74.9713F);
        Leftscreen.addChild(cube_r71);
        setRotationAngle(cube_r71, 1.5708F, 0.7854F, 0.0F);
        cube_r71.cubeList.add(new ModelBox(cube_r71, 454, 252, -2.5F, -0.5F, -11.5F, 7, 1, 22, 0.0F, false));

        cube_r72 = new ModelRenderer(this);
        cube_r72.setRotationPoint(62.1713F, -29.75F, -76.3713F);
        Leftscreen.addChild(cube_r72);
        setRotationAngle(cube_r72, 1.5708F, 0.7854F, 0.0F);
        cube_r72.cubeList.add(new ModelBox(cube_r72, 57, 167, -4.5F, -0.5F, -11.5F, 9, 1, 24, 0.0F, false));

        Rightscreen = new ModelRenderer(this);
        Rightscreen.setRotationPoint(-54.9213F, 40.75F, 76.3713F);
        pm.addChild(Rightscreen);


        cube_r73 = new ModelRenderer(this);
        cube_r73.setRotationPoint(54.6713F, -37.9F, -72.5213F);
        Rightscreen.addChild(cube_r73);
        setRotationAngle(cube_r73, 1.5708F, 0.0F, 0.0F);
        cube_r73.cubeList.add(new ModelBox(cube_r73, 21, 0, 8.0F, -4.5F, -5.5F, 2, 5, 2, 0.0F, false));

        cube_r74 = new ModelRenderer(this);
        cube_r74.setRotationPoint(54.6713F, -29.4F, -72.5213F);
        Rightscreen.addChild(cube_r74);
        setRotationAngle(cube_r74, 1.5708F, 0.0F, 0.0F);
        cube_r74.cubeList.add(new ModelBox(cube_r74, 0, 40, 8.0F, -4.5F, -5.5F, 2, 5, 2, 0.0F, false));

        cube_r75 = new ModelRenderer(this);
        cube_r75.setRotationPoint(54.6713F, -46.4F, -72.5213F);
        Rightscreen.addChild(cube_r75);
        setRotationAngle(cube_r75, 1.5708F, 0.0F, 0.0F);
        cube_r75.cubeList.add(new ModelBox(cube_r75, 44, 43, 8.0F, -4.5F, -5.5F, 2, 5, 2, 0.0F, false));

        cube_r76 = new ModelRenderer(this);
        cube_r76.setRotationPoint(56.9213F, -21.75F, -72.1213F);
        Rightscreen.addChild(cube_r76);
        setRotationAngle(cube_r76, 1.5708F, 0.0F, 0.0F);
        cube_r76.cubeList.add(new ModelBox(cube_r76, 285, 380, 2.0F, -0.5F, -5.5F, 8, 1, 31, 0.0F, false));

        cube_r77 = new ModelRenderer(this);
        cube_r77.setRotationPoint(62.1713F, -33.75F, -74.9713F);
        Rightscreen.addChild(cube_r77);
        setRotationAngle(cube_r77, 1.5708F, 0.7854F, 0.0F);
        cube_r77.cubeList.add(new ModelBox(cube_r77, 492, 484, -2.5F, -0.5F, -11.5F, 7, 1, 22, 0.0F, false));

        cube_r78 = new ModelRenderer(this);
        cube_r78.setRotationPoint(62.1713F, -32.75F, -76.3713F);
        Rightscreen.addChild(cube_r78);
        setRotationAngle(cube_r78, 1.5708F, 0.7854F, 0.0F);
        cube_r78.cubeList.add(new ModelBox(cube_r78, 57, 142, -4.5F, -0.5F, -11.5F, 9, 1, 24, 0.0F, false));

        pm_progress = new ModelRenderer(this);
        pm_progress.setRotationPoint(54.9213F, -23.75F, -76.8713F);


        cube_r79 = new ModelRenderer(this);
        cube_r79.setRotationPoint(-111.0F, 8.0F, 4.5F);
        pm_progress.addChild(cube_r79);
        setRotationAngle(cube_r79, 1.5708F, 0.0F, 0.0F);
        cube_r79.cubeList.add(new ModelBox(cube_r79, 0, 114, -11.0F, -0.5F, -0.5F, 40, 1, 12, 0.0F, false));

        cube_r80 = new ModelRenderer(this);
        cube_r80.setRotationPoint(-108.25F, 1.0F, 3.5F);
        pm_progress.addChild(cube_r80);
        setRotationAngle(cube_r80, 1.5708F, 0.0F, 0.0F);
        cube_r80.cubeList.add(new ModelBox(cube_r80, 0, 256, -11.0F, -0.5F, -5.5F, 35, 1, 8, 0.0F, false));

        cube_r81 = new ModelRenderer(this);
        cube_r81.setRotationPoint(-108.0F, 2.0F, 2.5F);
        pm_progress.addChild(cube_r81);
        setRotationAngle(cube_r81, 1.5708F, 0.0F, 0.0F);
        cube_r81.cubeList.add(new ModelBox(cube_r81, 0, 127, -14.0F, -0.5F, -5.5F, 40, 1, 10, 0.0F, false));

        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 142, -67.0F, -17.0F, -66.8713F, 134, 11, 134, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -70.1213F, -80.1213F, -70.0F, 140, 2, 140, 0.0F, false));
    }

    public void render(float size){
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        doRender(size);
        GlStateManager.popMatrix();
    }


    public void doRender(float size) {
        footpads.render(size);
        side.render(size);
        side2.render(size);
        baffle.render(size);
        energy.render(size);
        energy2.render(size);
        fluid.render(size);
        gas.render(size);
        fluid2.render(size);
        gas2.render(size);
        bone.render(size);
        conveyorbelts.render(size);
        Roboticarm.render(size);
        Roboticarm2.render(size);
        Roboticarm3.render(size);
        Roboticarm4.render(size);
        platform.render(size);
        platform2.render(size);
        glass.render(size);
        doorpanels.render(size);
        door.render(size);
        door2.render(size);
        door3.render(size);
        side3.render(size);
        glass4.render(size);
        Warninglights.render(size);
        pm.render(size);
        pm_progress.render(size);
        bb_main.render(size);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}