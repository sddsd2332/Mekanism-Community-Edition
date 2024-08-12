package mekanism.multiblockmachine.client.model.machine;

import mekanism.client.render.MekanismRenderer;
import mekanism.multiblockmachine.common.util.MekanismMultiblockMachineUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelDigitalAssemblyTable extends ModelBase {
    ModelRenderer base;
    ModelRenderer footpads;
    ModelRenderer bone;
    ModelRenderer bone3;
    ModelRenderer bone4;
    ModelRenderer bone5;
    ModelRenderer side;
    ModelRenderer bone9;
    ModelRenderer bone7;
    ModelRenderer cube_r1;
    ModelRenderer cube_r2;
    ModelRenderer bone6;
    ModelRenderer cube_r3;
    ModelRenderer cube_r4;
    ModelRenderer bone10;
    ModelRenderer bone11;
    ModelRenderer cube_r5;
    ModelRenderer cube_r6;
    ModelRenderer bone12;
    ModelRenderer cube_r7;
    ModelRenderer cube_r8;
    ModelRenderer bone8;
    ModelRenderer cube_r9;
    ModelRenderer cube_r10;
    ModelRenderer cube_r11;
    ModelRenderer cube_r12;
    ModelRenderer conveyorbelts;
    ModelRenderer bone2;
    ModelRenderer doorpanels;
    ModelRenderer door1;
    ModelRenderer door2;
    ModelRenderer door3;
    ModelRenderer bone13;
    ModelRenderer pm;
    ModelRenderer cube_r13;
    ModelRenderer cube_r14;
    ModelRenderer cube_r15;
    ModelRenderer cube_r16;
    ModelRenderer cube_r17;
    ModelRenderer cube_r18;
    ModelRenderer cube_r19;
    ModelRenderer cube_r20;
    ModelRenderer cube_r21;
    ModelRenderer cube_r22;
    ModelRenderer cube_r23;
    ModelRenderer Leftscreen;
    ModelRenderer cube_r24;
    ModelRenderer cube_r25;
    ModelRenderer cube_r26;
    ModelRenderer cube_r27;
    ModelRenderer cube_r28;
    ModelRenderer cube_r29;
    ModelRenderer Rightscreen;
    ModelRenderer cube_r30;
    ModelRenderer cube_r31;
    ModelRenderer cube_r32;
    ModelRenderer cube_r33;
    ModelRenderer cube_r34;
    ModelRenderer cube_r35;
    ModelRenderer pm_progress;
    ModelRenderer cube_r36;
    ModelRenderer cube_r37;
    ModelRenderer cube_r38;
    ModelRenderer platform;
    ModelRenderer platform2;
    ModelRenderer Roboticarm;
    ModelRenderer bone14;
    ModelRenderer bone15;
    ModelRenderer baseTurn2_r1;
    ModelRenderer Roboticarm2;
    ModelRenderer bone16;
    ModelRenderer bone17;
    ModelRenderer baseTurn2_r2;
    ModelRenderer Roboticarm3;
    ModelRenderer bone18;
    ModelRenderer bone19;
    ModelRenderer baseTurn2_r3;
    ModelRenderer Roboticarm4;
    ModelRenderer bone20;
    ModelRenderer bone21;
    ModelRenderer baseTurn2_r4;
    ModelRenderer port;
    ModelRenderer energy;
    ModelRenderer cube_r39;
    ModelRenderer cube_r40;
    ModelRenderer cube_r41;
    ModelRenderer cube_r42;
    ModelRenderer energy_in;
    ModelRenderer energy2;
    ModelRenderer cube_r43;
    ModelRenderer cube_r44;
    ModelRenderer cube_r45;
    ModelRenderer cube_r46;
    ModelRenderer energy_in2;
    ModelRenderer gas;
    ModelRenderer cube_r47;
    ModelRenderer cube_r48;
    ModelRenderer cube_r49;
    ModelRenderer cube_r50;
    ModelRenderer gas_in2;
    ModelRenderer fluid;
    ModelRenderer cube_r51;
    ModelRenderer cube_r52;
    ModelRenderer cube_r53;
    ModelRenderer cube_r54;
    ModelRenderer fluid_in;
    ModelRenderer gas2;
    ModelRenderer cube_r55;
    ModelRenderer cube_r56;
    ModelRenderer cube_r57;
    ModelRenderer cube_r58;
    ModelRenderer gas_in3;
    ModelRenderer fluid2;
    ModelRenderer cube_r59;
    ModelRenderer cube_r60;
    ModelRenderer cube_r61;
    ModelRenderer cube_r62;
    ModelRenderer fluid_in2;
    ModelRenderer glass;
    ModelRenderer glass1;
    ModelRenderer glass2;
    ModelRenderer glass3;
    ModelRenderer glass4;
    ModelRenderer Warninglights;
    ModelRenderer Warninglights2;
    ModelRenderer ornament;

    public ModelDigitalAssemblyTable() {
        textureWidth = 1024;
        textureHeight = 1024;

        base = new ModelRenderer(this);
        base.setRotationPoint(0.0F, 24.0F, 0.0F);
        base.cubeList.add(new ModelBox(base, 0, 146, -70.0F, -80.2426F, -70.0F, 140, 6, 140, 0.0F, false));
        base.cubeList.add(new ModelBox(base, 0, 0, -70.0F, -10.0F, -70.0F, 140, 6, 140, 0.0F, false));

        footpads = new ModelRenderer(this);
        footpads.setRotationPoint(0.0F, 24.0F, 0.0F);


        bone = new ModelRenderer(this);
        bone.setRotationPoint(-62.0F, 0.0F, 62.0F);
        footpads.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 312, 330, -8.0F, -2.0F, -8.0F, 16, 2, 16, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 471, 516, -7.0F, -4.0F, -7.0F, 14, 2, 14, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(-62.0F, 0.0F, -62.0F);
        footpads.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 240, 330, -8.0F, -2.0F, -8.0F, 16, 2, 16, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 312, 348, -7.0F, -4.0F, -7.0F, 14, 2, 14, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(62.0F, 0.0F, 62.0F);
        footpads.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 72, 324, -8.0F, -2.0F, -8.0F, 16, 2, 16, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 240, 348, -7.0F, -4.0F, -7.0F, 14, 2, 14, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(62.0F, 0.0F, -62.0F);
        footpads.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 68, 178, -8.0F, -2.0F, -8.0F, 16, 2, 16, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 72, 342, -7.0F, -4.0F, -7.0F, 14, 2, 14, 0.0F, false));

        side = new ModelRenderer(this);
        side.setRotationPoint(0.0F, 24.0F, 0.0F);


        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(0.0F, 0.0F, 0.0F);
        side.addChild(bone9);


        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone9.addChild(bone7);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(70.0F, -4.0F, 0.0F);
        bone7.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, 0.7854F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 392, 442, -3.0F, -3.0F, -70.0F, 3, 3, 140, 0.0F, false));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-70.0F, -4.0F, 0.0F);
        bone7.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, 0.7854F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 146, 452, -3.0F, -3.0F, -70.0F, 3, 3, 140, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone9.addChild(bone6);


        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(0.0F, -5.4142F, 68.5858F);
        bone6.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.7854F, 0.0F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 540, 296, -70.0F, -3.0F, -1.0F, 140, 3, 3, 0.0F, false));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(0.0F, -5.4142F, -71.4142F);
        bone6.addChild(cube_r4);
        setRotationAngle(cube_r4, -0.7854F, 0.0F, 0.0F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 540, 302, -70.0F, -3.0F, -1.0F, 140, 3, 3, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone9.addChild(bone10);


        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone10.addChild(bone11);


        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(70.0F, -76.0F, 0.0F);
        bone11.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, 0.7854F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 420, 6, -3.0F, -3.0F, -70.0F, 3, 3, 140, 0.0F, false));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(-70.0F, -76.0F, 0.0F);
        bone11.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, 0.7854F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 0, 442, -3.0F, -3.0F, -70.0F, 3, 3, 140, 0.0F, false));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone10.addChild(bone12);


        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(0.0F, -77.4142F, 68.5858F);
        bone12.addChild(cube_r7);
        setRotationAngle(cube_r7, -0.7854F, 0.0F, 0.0F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 240, 292, -70.0F, -3.0F, -1.0F, 140, 3, 3, 0.0F, false));

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(0.0F, -77.4142F, -71.4142F);
        bone12.addChild(cube_r8);
        setRotationAngle(cube_r8, -0.7854F, 0.0F, 0.0F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 420, 0, -70.0F, -3.0F, -1.0F, 140, 3, 3, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, 0.0F, 0.0F);
        side.addChild(bone8);


        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(70.7071F, -6.1213F, 70.0F);
        bone8.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, -0.7854F, 0.0F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 372, 452, -2.0F, -72.0F, -1.0F, 3, 72, 3, 0.0F, false));

        cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(70.7071F, -6.1213F, -70.0F);
        bone8.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, -0.7854F, 0.0F);
        cube_r10.cubeList.add(new ModelBox(cube_r10, 490, 298, -2.0F, -72.0F, -1.0F, 3, 72, 3, 0.0F, false));

        cube_r11 = new ModelRenderer(this);
        cube_r11.setRotationPoint(-69.2929F, -6.1213F, -70.0F);
        bone8.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, -0.7854F, 0.0F);
        cube_r11.cubeList.add(new ModelBox(cube_r11, 498, 149, -2.0F, -72.0F, -1.0F, 3, 72, 3, 0.0F, false));

        cube_r12 = new ModelRenderer(this);
        cube_r12.setRotationPoint(-69.2929F, -6.1213F, 70.0F);
        bone8.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0F, -0.7854F, 0.0F);
        cube_r12.cubeList.add(new ModelBox(cube_r12, 502, 298, -2.0F, -72.0F, -1.0F, 3, 72, 3, 0.0F, false));

        conveyorbelts = new ModelRenderer(this);
        conveyorbelts.setRotationPoint(0.0F, 31.0F, 0.0F);
        conveyorbelts.cubeList.add(new ModelBox(conveyorbelts, 0, 292, -24.0F, -23.0F, -72.0F, 48, 6, 144, 0.0F, false));
        conveyorbelts.cubeList.add(new ModelBox(conveyorbelts, 392, 292, -28.0F, -27.0F, -70.0F, 4, 10, 140, 0.0F, false));
        conveyorbelts.cubeList.add(new ModelBox(conveyorbelts, 244, 302, 24.0F, -27.0F, -70.0F, 4, 10, 140, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone2.cubeList.add(new ModelBox(bone2, 540, 326, -70.0F, -10.1213F, 70.1213F, 140, 4, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 540, 320, -70.0F, -10.1213F, -72.1213F, 140, 4, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 0, 585, 70.1213F, -10.1213F, -70.0F, 2, 4, 140, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 566, 0, -72.1213F, -10.1213F, -70.0F, 2, 4, 140, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 538, 445, -72.1213F, -78.1213F, -70.0F, 2, 4, 140, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 540, 152, 70.1213F, -78.1213F, -70.0F, 2, 4, 140, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 540, 308, -70.0F, -78.1213F, -72.1213F, 140, 4, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 540, 314, -70.0F, -78.1213F, 70.1213F, 140, 4, 2, 0.0F, false));

        doorpanels = new ModelRenderer(this);
        doorpanels.setRotationPoint(0.0F, 24.0F, 0.0F);
        doorpanels.cubeList.add(new ModelBox(doorpanels, 146, 471, -24.0F, -75.0F, 69.1213F, 48, 11, 3, 0.0F, false));
        doorpanels.cubeList.add(new ModelBox(doorpanels, 0, 272, -24.0F, -75.0F, -72.1213F, 48, 11, 3, 0.0F, false));
        doorpanels.cubeList.add(new ModelBox(doorpanels, 490, 373, 24.0F, -75.0F, -70.0F, 4, 55, 3, 0.0F, false));
        doorpanels.cubeList.add(new ModelBox(doorpanels, 266, 452, -28.0F, -75.0F, -70.0F, 4, 55, 3, 0.0F, false));
        doorpanels.cubeList.add(new ModelBox(doorpanels, 125, 376, 24.0F, -75.0F, 67.0F, 4, 55, 3, 0.0F, false));
        doorpanels.cubeList.add(new ModelBox(doorpanels, 120, 442, -28.0F, -75.0F, 67.0F, 4, 55, 3, 0.0F, false));

        door1 = new ModelRenderer(this);
        door1.setRotationPoint(0.0F, 24.0F, 0.0F);
        door1.cubeList.add(new ModelBox(door1, 0, 461, -24.0F, -64.0F, 67.1213F, 48, 16, 3, 0.0F, false));
        door1.cubeList.add(new ModelBox(door1, 391, 452, -24.0F, -64.0F, -70.1213F, 48, 16, 3, 0.0F, false));

        door2 = new ModelRenderer(this);
        door2.setRotationPoint(0.0F, 40.0F, 0.0F);
        door2.cubeList.add(new ModelBox(door2, 146, 452, -24.0F, -64.0F, 67.1213F, 48, 16, 3, 0.0F, false));
        door2.cubeList.add(new ModelBox(door2, 0, 442, -24.0F, -64.0F, -70.1213F, 48, 16, 3, 0.0F, false));

        door3 = new ModelRenderer(this);
        door3.setRotationPoint(0.0F, 56.0F, 0.0F);
        door3.cubeList.add(new ModelBox(door3, 420, 266, -24.0F, -64.0F, 67.1213F, 48, 16, 3, 0.0F, false));
        door3.cubeList.add(new ModelBox(door3, 420, 247, -24.0F, -64.0F, -70.1213F, 48, 16, 3, 0.0F, false));

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone13.cubeList.add(new ModelBox(bone13, 420, 73, -70.0F, -74.1213F, -72.1213F, 46, 64, 3, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 420, 6, 24.0F, -74.1213F, -72.1213F, 46, 64, 3, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 392, 365, -70.0F, -74.1213F, 69.1213F, 46, 64, 3, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 392, 298, 24.0F, -74.1213F, 69.1213F, 46, 64, 3, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 0, 146, 69.1213F, -74.1213F, 8.0F, 3, 64, 62, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 0, 0, -72.1213F, -74.1213F, 8.0F, 3, 64, 62, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 600, 614, 69.1213F, -74.1213F, -70.0F, 3, 25, 78, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 144, 595, 69.1213F, -35.1213F, -70.0F, 3, 25, 78, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 36, 32, 69.1213F, -49.1213F, -70.0F, 3, 14, 4, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 34, 0, 69.1213F, -49.1213F, 4.0F, 3, 14, 4, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 0, 0, -72.1213F, -49.1213F, -70.0F, 3, 14, 4, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 354, 585, -72.1213F, -74.1213F, -70.0F, 3, 25, 78, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 0, 32, -72.1213F, -49.1213F, 4.0F, 3, 14, 4, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 516, 589, -72.1213F, -35.1213F, -70.0F, 3, 25, 78, 0.0F, false));

        pm = new ModelRenderer(this);
        pm.setRotationPoint(54.9213F, -16.75F, -76.8713F);


        cube_r13 = new ModelRenderer(this);
        cube_r13.setRotationPoint(-1.0F, 24.0F, -4.75F);
        pm.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.3491F, 0.0F, 0.0F);
        cube_r13.cubeList.add(new ModelBox(cube_r13, 511, 274, -16.0F, -0.5F, -5.5F, 26, 1, 11, 0.0F, false));

        cube_r14 = new ModelRenderer(this);
        cube_r14.setRotationPoint(-8.0F, 0.0F, 0.0F);
        pm.addChild(cube_r14);
        setRotationAngle(cube_r14, 1.9635F, 0.0F, 0.0F);
        cube_r14.cubeList.add(new ModelBox(cube_r14, 92, 126, -4.0F, -0.5F, -5.5F, 14, 1, 10, 0.0F, false));

        cube_r15 = new ModelRenderer(this);
        cube_r15.setRotationPoint(-16.0F, -5.9F, 3.85F);
        pm.addChild(cube_r15);
        setRotationAngle(cube_r15, 1.5708F, 0.0F, 0.0F);
        cube_r15.cubeList.add(new ModelBox(cube_r15, 14, 12, 8.0F, -3.5F, -5.5F, 2, 4, 2, 0.0F, false));
        cube_r15.cubeList.add(new ModelBox(cube_r15, 0, 126, 12.0F, -3.5F, -5.5F, 2, 4, 2, 0.0F, false));

        cube_r16 = new ModelRenderer(this);
        cube_r16.setRotationPoint(-1.5F, 20.45F, -6.15F);
        pm.addChild(cube_r16);
        setRotationAngle(cube_r16, 1.5708F, 0.0F, 0.0F);
        cube_r16.cubeList.add(new ModelBox(cube_r16, 68, 50, -16.0F, -2.5F, -5.5F, 26, 8, 1, 0.0F, false));

        cube_r17 = new ModelRenderer(this);
        cube_r17.setRotationPoint(0.0F, 19.425F, 1.6F);
        pm.addChild(cube_r17);
        setRotationAngle(cube_r17, 1.5708F, 0.0F, 0.0F);
        cube_r17.cubeList.add(new ModelBox(cube_r17, 0, 55, -18.0F, -2.5F, -6.5F, 28, 3, 3, 0.0F, false));

        cube_r18 = new ModelRenderer(this);
        cube_r18.setRotationPoint(-13.0F, 19.35F, 3.85F);
        pm.addChild(cube_r18);
        setRotationAngle(cube_r18, 1.5708F, 0.0F, 0.0F);
        cube_r18.cubeList.add(new ModelBox(cube_r18, 80, 0, 6.0F, -4.5F, -5.5F, 4, 5, 2, 0.0F, false));

        cube_r19 = new ModelRenderer(this);
        cube_r19.setRotationPoint(-16.0F, -3.15F, 3.85F);
        pm.addChild(cube_r19);
        setRotationAngle(cube_r19, 1.5708F, 0.0F, 0.0F);
        cube_r19.cubeList.add(new ModelBox(cube_r19, 34, 19, 8.0F, -2.5F, -5.5F, 2, 3, 2, 0.0F, false));
        cube_r19.cubeList.add(new ModelBox(cube_r19, 120, 57, 12.0F, -2.5F, -5.5F, 2, 3, 2, 0.0F, false));

        cube_r20 = new ModelRenderer(this);
        cube_r20.setRotationPoint(-11.0F, 19.0F, 4.25F);
        pm.addChild(cube_r20);
        setRotationAngle(cube_r20, 1.5708F, 0.0F, 0.0F);
        cube_r20.cubeList.add(new ModelBox(cube_r20, 420, 181, 2.0F, -0.5F, -5.5F, 8, 1, 31, 0.0F, false));

        cube_r21 = new ModelRenderer(this);
        cube_r21.setRotationPoint(-9.0F, 12.0F, 3.25F);
        pm.addChild(cube_r21);
        setRotationAngle(cube_r21, 1.5708F, 0.0F, 0.0F);
        cube_r21.cubeList.add(new ModelBox(cube_r21, 86, 427, -2.0F, -0.5F, -5.5F, 12, 1, 8, 0.0F, false));

        cube_r22 = new ModelRenderer(this);
        cube_r22.setRotationPoint(-9.0F, -1.25F, 0.5F);
        pm.addChild(cube_r22);
        setRotationAngle(cube_r22, 1.9635F, 0.0F, 0.0F);
        cube_r22.cubeList.add(new ModelBox(cube_r22, 60, 480, -2.0F, -0.5F, -5.5F, 12, 1, 8, 0.0F, false));

        cube_r23 = new ModelRenderer(this);
        cube_r23.setRotationPoint(-8.0F, 13.0F, 2.25F);
        pm.addChild(cube_r23);
        setRotationAngle(cube_r23, 1.5708F, 0.0F, 0.0F);
        cube_r23.cubeList.add(new ModelBox(cube_r23, 68, 196, -4.0F, -0.5F, -5.5F, 14, 1, 10, 0.0F, false));

        Leftscreen = new ModelRenderer(this);
        Leftscreen.setRotationPoint(45.0787F, -22.5F, 75.3713F);
        pm.addChild(Leftscreen);
        setRotationAngle(Leftscreen, 0.0F, 0.0F, -3.1416F);


        cube_r24 = new ModelRenderer(this);
        cube_r24.setRotationPoint(54.6713F, -34.9F, -72.5213F);
        Leftscreen.addChild(cube_r24);
        setRotationAngle(cube_r24, 1.5708F, 0.0F, 0.0F);
        cube_r24.cubeList.add(new ModelBox(cube_r24, 68, 11, 8.0F, -4.5F, -5.5F, 2, 5, 2, 0.0F, false));

        cube_r25 = new ModelRenderer(this);
        cube_r25.setRotationPoint(54.6713F, -26.4F, -72.5213F);
        Leftscreen.addChild(cube_r25);
        setRotationAngle(cube_r25, 1.5708F, 0.0F, 0.0F);
        cube_r25.cubeList.add(new ModelBox(cube_r25, 80, 11, 8.0F, -4.5F, -5.5F, 2, 5, 2, 0.0F, false));

        cube_r26 = new ModelRenderer(this);
        cube_r26.setRotationPoint(54.6713F, -43.4F, -72.5213F);
        Leftscreen.addChild(cube_r26);
        setRotationAngle(cube_r26, 1.5708F, 0.0F, 0.0F);
        cube_r26.cubeList.add(new ModelBox(cube_r26, 122, 50, 8.0F, -4.5F, -5.5F, 2, 5, 2, 0.0F, false));

        cube_r27 = new ModelRenderer(this);
        cube_r27.setRotationPoint(56.9213F, -21.75F, -72.1213F);
        Leftscreen.addChild(cube_r27);
        setRotationAngle(cube_r27, 1.5708F, 0.0F, 0.0F);
        cube_r27.cubeList.add(new ModelBox(cube_r27, 420, 213, 2.0F, -0.5F, -5.5F, 8, 1, 31, 0.0F, false));

        cube_r28 = new ModelRenderer(this);
        cube_r28.setRotationPoint(62.1713F, -30.75F, -74.9713F);
        Leftscreen.addChild(cube_r28);
        setRotationAngle(cube_r28, 1.5708F, 0.7854F, 0.0F);
        cube_r28.cubeList.add(new ModelBox(cube_r28, 0, 178, -2.5F, -0.5F, -11.5F, 7, 1, 22, 0.0F, false));

        cube_r29 = new ModelRenderer(this);
        cube_r29.setRotationPoint(62.1713F, -29.75F, -76.3713F);
        Leftscreen.addChild(cube_r29);
        setRotationAngle(cube_r29, 1.5708F, 0.7854F, 0.0F);
        cube_r29.cubeList.add(new ModelBox(cube_r29, 68, 25, -4.5F, -0.5F, -11.5F, 9, 1, 24, 0.0F, false));

        Rightscreen = new ModelRenderer(this);
        Rightscreen.setRotationPoint(-54.9213F, 40.75F, 76.3713F);
        pm.addChild(Rightscreen);


        cube_r30 = new ModelRenderer(this);
        cube_r30.setRotationPoint(54.6713F, -37.9F, -72.5213F);
        Rightscreen.addChild(cube_r30);
        setRotationAngle(cube_r30, 1.5708F, 0.0F, 0.0F);
        cube_r30.cubeList.add(new ModelBox(cube_r30, 52, 9, 8.0F, -4.5F, -5.5F, 2, 5, 2, 0.0F, false));

        cube_r31 = new ModelRenderer(this);
        cube_r31.setRotationPoint(54.6713F, -29.4F, -72.5213F);
        Rightscreen.addChild(cube_r31);
        setRotationAngle(cube_r31, 1.5708F, 0.0F, 0.0F);
        cube_r31.cubeList.add(new ModelBox(cube_r31, 54, 41, 8.0F, -4.5F, -5.5F, 2, 5, 2, 0.0F, false));

        cube_r32 = new ModelRenderer(this);
        cube_r32.setRotationPoint(54.6713F, -46.4F, -72.5213F);
        Rightscreen.addChild(cube_r32);
        setRotationAngle(cube_r32, 1.5708F, 0.0F, 0.0F);
        cube_r32.cubeList.add(new ModelBox(cube_r32, 68, 0, 8.0F, -4.5F, -5.5F, 2, 5, 2, 0.0F, false));

        cube_r33 = new ModelRenderer(this);
        cube_r33.setRotationPoint(56.9213F, -21.75F, -72.1213F);
        Rightscreen.addChild(cube_r33);
        setRotationAngle(cube_r33, 1.5708F, 0.0F, 0.0F);
        cube_r33.cubeList.add(new ModelBox(cube_r33, 420, 149, 2.0F, -0.5F, -5.5F, 8, 1, 31, 0.0F, false));

        cube_r34 = new ModelRenderer(this);
        cube_r34.setRotationPoint(62.1713F, -33.75F, -74.9713F);
        Rightscreen.addChild(cube_r34);
        setRotationAngle(cube_r34, 1.5708F, 0.7854F, 0.0F);
        cube_r34.cubeList.add(new ModelBox(cube_r34, 0, 32, -2.5F, -0.5F, -11.5F, 7, 1, 22, 0.0F, false));

        cube_r35 = new ModelRenderer(this);
        cube_r35.setRotationPoint(62.1713F, -32.75F, -76.3713F);
        Rightscreen.addChild(cube_r35);
        setRotationAngle(cube_r35, 1.5708F, 0.7854F, 0.0F);
        cube_r35.cubeList.add(new ModelBox(cube_r35, 68, 0, -4.5F, -0.5F, -11.5F, 9, 1, 24, 0.0F, false));

        pm_progress = new ModelRenderer(this);
        pm_progress.setRotationPoint(54.9213F, -23.75F, -76.8713F);


        cube_r36 = new ModelRenderer(this);
        cube_r36.setRotationPoint(-111.0F, 8.0F, 4.5F);
        pm_progress.addChild(cube_r36);
        setRotationAngle(cube_r36, 1.5708F, 0.0F, 0.0F);
        cube_r36.cubeList.add(new ModelBox(cube_r36, 0, 126, -11.0F, -0.5F, -0.5F, 40, 1, 12, 0.0F, false));

        cube_r37 = new ModelRenderer(this);
        cube_r37.setRotationPoint(-108.25F, 1.0F, 3.5F);
        pm_progress.addChild(cube_r37);
        setRotationAngle(cube_r37, 1.5708F, 0.0F, 0.0F);
        cube_r37.cubeList.add(new ModelBox(cube_r37, 0, 427, -11.0F, -0.5F, -5.5F, 35, 1, 8, 0.0F, false));

        cube_r38 = new ModelRenderer(this);
        cube_r38.setRotationPoint(-108.0F, 2.0F, 2.5F);
        pm_progress.addChild(cube_r38);
        setRotationAngle(cube_r38, 1.5708F, 0.0F, 0.0F);
        cube_r38.cubeList.add(new ModelBox(cube_r38, 391, 471, -14.0F, -0.5F, -5.5F, 40, 1, 10, 0.0F, false));

        platform = new ModelRenderer(this);
        platform.setRotationPoint(-94.0F, 31.0F, -35.0F);
        setRotationAngle(platform, 0.0F, 3.1416F, 0.0F);
        platform.cubeList.add(new ModelBox(platform, 146, 485, -57.0F, -26.0F, -45.0F, 20, 9, 20, 0.0F, false));
        platform.cubeList.add(new ModelBox(platform, 540, 347, -52.0F, -31.0F, -40.0F, 10, 5, 10, 0.0F, false));
        platform.cubeList.add(new ModelBox(platform, 391, 511, -57.0F, -35.0F, -45.0F, 20, 5, 20, 0.0F, false));

        platform2 = new ModelRenderer(this);
        platform2.setRotationPoint(2.0F, 31.0F, -35.0F);
        setRotationAngle(platform2, 0.0F, 3.1416F, 0.0F);
        platform2.cubeList.add(new ModelBox(platform2, 60, 489, -57.0F, -26.0F, -45.0F, 20, 9, 20, 0.0F, false));
        platform2.cubeList.add(new ModelBox(platform2, 540, 362, -52.0F, -31.0F, -40.0F, 10, 5, 10, 0.0F, false));
        platform2.cubeList.add(new ModelBox(platform2, 146, 514, -57.0F, -35.0F, -45.0F, 20, 5, 20, 0.0F, false));

        Roboticarm = new ModelRenderer(this);
        Roboticarm.setRotationPoint(0.0F, 31.0F, 0.0F);
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 391, 482, 39.0F, -26.0F, 25.0F, 20, 9, 20, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 540, 332, 44.0F, -31.0F, 30.0F, 10, 5, 10, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 292, 510, 39.0F, -35.0F, 25.0F, 20, 5, 20, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 292, 535, 43.5F, -36.0F, 29.5F, 11, 1, 11, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 312, 298, 50.0F, -60.0F, 10.0F, 5, 8, 24, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 0, 376, 42.0F, -60.0F, 10.0F, 8, 10, 8, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 186, 442, 40.0F, -50.0F, 10.0F, 12, 2, 8, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 68, 11, 50.0F, -48.0F, 10.0F, 2, 3, 8, 0.0F, false));
        Roboticarm.cubeList.add(new ModelBox(Roboticarm, 68, 0, 40.0F, -48.0F, 10.0F, 2, 3, 8, 0.0F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(0.0F, 0.0F, 0.0F);
        Roboticarm.addChild(bone14);
        bone14.cubeList.add(new ModelBox(bone14, 370, 549, 45.0F, -44.0F, 31.0F, 8, 8, 8, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 0, 324, 40.0F, -44.0F, 31.0F, 5, 8, 24, 0.0F, false));

        bone15 = new ModelRenderer(this);
        bone15.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone14.addChild(bone15);


        baseTurn2_r1 = new ModelRenderer(this);
        baseTurn2_r1.setRotationPoint(48.0F, -46.0F, 33.0F);
        bone15.addChild(baseTurn2_r1);
        setRotationAngle(baseTurn2_r1, -0.6109F, 0.0F, 0.0F);
        baseTurn2_r1.cubeList.add(new ModelBox(baseTurn2_r1, 285, 390, -3.0F, -10.0F, -13.0F, 5, 8, 35, 0.0F, false));

        Roboticarm2 = new ModelRenderer(this);
        Roboticarm2.setRotationPoint(-96.0F, 31.0F, 0.0F);
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 292, 481, 39.0F, -26.0F, 25.0F, 20, 9, 20, 0.0F, false));
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 540, 209, 44.0F, -31.0F, 30.0F, 10, 5, 10, 0.0F, false));
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 0, 509, 39.0F, -35.0F, 25.0F, 20, 5, 20, 0.0F, false));
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 529, 228, 43.5F, -36.0F, 29.5F, 11, 1, 11, 0.0F, false));
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 72, 292, 50.0F, -60.0F, 10.0F, 5, 8, 24, 0.0F, false));
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 34, 324, 42.0F, -60.0F, 10.0F, 8, 10, 8, 0.0F, false));
        Roboticarm2.cubeList.add(new ModelBox(Roboticarm2, 34, 157, 44.0F, -50.0F, 12.0F, 4, 1, 4, 0.0F, false));

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(0.0F, 0.0F, 0.0F);
        Roboticarm2.addChild(bone16);
        bone16.cubeList.add(new ModelBox(bone16, 285, 401, 45.0F, -44.0F, 31.0F, 8, 8, 8, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 240, 298, 40.0F, -44.0F, 31.0F, 5, 8, 24, 0.0F, false));

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone16.addChild(bone17);


        baseTurn2_r2 = new ModelRenderer(this);
        baseTurn2_r2.setRotationPoint(48.0F, -46.0F, 33.0F);
        bone17.addChild(baseTurn2_r2);
        setRotationAngle(baseTurn2_r2, -0.6109F, 0.0F, 0.0F);
        baseTurn2_r2.cubeList.add(new ModelBox(baseTurn2_r2, 45, 384, -3.0F, -10.0F, -13.0F, 5, 8, 35, 0.0F, false));

        Roboticarm3 = new ModelRenderer(this);
        Roboticarm3.setRotationPoint(0.0F, 31.0F, -70.0F);
        setRotationAngle(Roboticarm3, 0.0F, 3.1416F, 0.0F);
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 0, 480, -57.0F, -26.0F, -45.0F, 20, 9, 20, 0.0F, false));
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 540, 194, -52.0F, -31.0F, -40.0F, 10, 5, 10, 0.0F, false));
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 206, 494, -57.0F, -35.0F, -45.0F, 20, 5, 20, 0.0F, false));
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 519, 255, -52.5F, -36.0F, -40.5F, 11, 1, 11, 0.0F, false));
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 68, 146, -46.0F, -60.0F, -60.0F, 5, 8, 24, 0.0F, false));
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 274, 298, -54.0F, -60.0F, -60.0F, 8, 10, 8, 0.0F, false));
        Roboticarm3.cubeList.add(new ModelBox(Roboticarm3, 14, 0, -51.0F, -50.0F, -57.0F, 2, 10, 2, 0.0F, false));

        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(-96.0F, 0.0F, -70.0F);
        Roboticarm3.addChild(bone18);
        bone18.cubeList.add(new ModelBox(bone18, 240, 401, 45.0F, -44.0F, 31.0F, 8, 8, 8, 0.0F, false));
        bone18.cubeList.add(new ModelBox(bone18, 0, 292, 40.0F, -44.0F, 31.0F, 5, 8, 24, 0.0F, false));

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone18.addChild(bone19);


        baseTurn2_r3 = new ModelRenderer(this);
        baseTurn2_r3.setRotationPoint(48.0F, -46.0F, 33.0F);
        bone19.addChild(baseTurn2_r3);
        setRotationAngle(baseTurn2_r3, -0.6109F, 0.0F, 0.0F);
        baseTurn2_r3.cubeList.add(new ModelBox(baseTurn2_r3, 240, 382, -3.0F, -10.0F, -13.0F, 5, 8, 35, 0.0F, false));

        Roboticarm4 = new ModelRenderer(this);
        Roboticarm4.setRotationPoint(-94.0F, 31.0F, -70.0F);
        setRotationAngle(Roboticarm4, 0.0F, 3.1416F, 0.0F);
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 292, 452, -57.0F, -26.0F, -45.0F, 20, 9, 20, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 182, 539, -52.0F, -31.0F, -40.0F, 10, 5, 10, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 451, 491, -57.0F, -35.0F, -45.0F, 20, 5, 20, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 467, 224, -52.5F, -36.0F, -40.5F, 11, 1, 11, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 0, 0, -46.0F, -60.0F, -60.0F, 5, 8, 24, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 34, 292, -54.0F, -60.0F, -60.0F, 8, 10, 8, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 146, 442, -56.0F, -50.0F, -60.0F, 12, 2, 8, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 42, 42, -46.0F, -48.0F, -60.0F, 2, 3, 8, 0.0F, false));
        Roboticarm4.cubeList.add(new ModelBox(Roboticarm4, 40, 10, -56.0F, -48.0F, -60.0F, 2, 3, 8, 0.0F, false));

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(-96.0F, 0.0F, -70.0F);
        Roboticarm4.addChild(bone20);
        bone20.cubeList.add(new ModelBox(bone20, 45, 395, 45.0F, -44.0F, 31.0F, 8, 8, 8, 0.0F, false));
        bone20.cubeList.add(new ModelBox(bone20, 0, 146, 40.0F, -44.0F, 31.0F, 5, 8, 24, 0.0F, false));

        bone21 = new ModelRenderer(this);
        bone21.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone20.addChild(bone21);


        baseTurn2_r4 = new ModelRenderer(this);
        baseTurn2_r4.setRotationPoint(48.0F, -46.0F, 33.0F);
        bone21.addChild(baseTurn2_r4);
        setRotationAngle(baseTurn2_r4, -0.6109F, 0.0F, 0.0F);
        baseTurn2_r4.cubeList.add(new ModelBox(baseTurn2_r4, 0, 376, -3.0F, -10.0F, -13.0F, 5, 8, 35, 0.0F, false));

        port = new ModelRenderer(this);
        port.setRotationPoint(0.0F, 24.0F, 0.0F);


        energy = new ModelRenderer(this);
        energy.setRotationPoint(-58.0F, -60.0F, 83.9784F);
        port.addChild(energy);
        setRotationAngle(energy, 0.0F, 3.1416F, 0.0F);
        energy.cubeList.add(new ModelBox(energy, 574, 442, -6.0F, 3.1667F, -2.1216F, 2, 36, 2, 0.0F, false));
        energy.cubeList.add(new ModelBox(energy, 256, 519, -13.0F, 3.1667F, 0.8784F, 6, 36, 7, 0.0F, false));
        energy.cubeList.add(new ModelBox(energy, 574, 82, -16.0F, 3.1667F, 8.8784F, 2, 36, 2, 0.0F, false));
        energy.cubeList.add(new ModelBox(energy, 574, 44, -6.0F, 3.1667F, 8.8784F, 2, 36, 2, 0.0F, false));
        energy.cubeList.add(new ModelBox(energy, 574, 6, -16.0F, 3.1667F, -2.1216F, 2, 36, 2, 0.0F, false));
        energy.cubeList.add(new ModelBox(energy, 226, 519, -16.0F, 3.1667F, 10.8784F, 12, 36, 3, 0.0F, false));

        cube_r39 = new ModelRenderer(this);
        cube_r39.setRotationPoint(-10.0F, 8.1667F, -1.1216F);
        energy.addChild(cube_r39);
        setRotationAngle(cube_r39, -1.5708F, 0.0F, 0.0F);
        cube_r39.cubeList.add(new ModelBox(cube_r39, 285, 382, -6.0F, -15.0F, -8.0F, 12, 16, 3, 0.0F, false));

        cube_r40 = new ModelRenderer(this);
        cube_r40.setRotationPoint(-5.0F, 47.0667F, 7.8784F);
        energy.addChild(cube_r40);
        setRotationAngle(cube_r40, 0.0F, -1.5708F, -1.5708F);
        cube_r40.cubeList.add(new ModelBox(cube_r40, 292, 549, -10.0F, -11.0F, -7.9F, 16, 12, 3, 0.0F, false));

        cube_r41 = new ModelRenderer(this);
        cube_r41.setRotationPoint(-10.0F, 0.1667F, -2.1431F);
        energy.addChild(cube_r41);
        setRotationAngle(cube_r41, -0.8421F, 0.0F, 0.0F);
        cube_r41.cubeList.add(new ModelBox(cube_r41, 558, 556, -6.0F, -6.0153F, 0.0153F, 12, 6, 4, 0.0F, false));

        cube_r42 = new ModelRenderer(this);
        cube_r42.setRotationPoint(-10.0F, 2.1667F, 4.3534F);
        energy.addChild(cube_r42);
        setRotationAngle(cube_r42, -1.5708F, 0.0F, 0.0F);
        cube_r42.cubeList.add(new ModelBox(cube_r42, 214, 558, -6.0F, -10.0F, -6.0F, 12, 12, 4, 0.0F, false));

        energy_in = new ModelRenderer(this);
        energy_in.setRotationPoint(0.0F, 45.4167F, 6.0216F);
        energy.addChild(energy_in);
        setRotationAngle(energy_in, 3.1416F, 0.0F, 0.0F);
        energy_in.cubeList.add(new ModelBox(energy_in, 68, 146, -13.0F, 2.4167F, -0.9569F, 6, 1, 6, 0.0F, false));
        energy_in.cubeList.add(new ModelBox(energy_in, 346, 561, -14.0F, 1.4167F, -1.9569F, 8, 1, 8, 0.0F, false));
        energy_in.cubeList.add(new ModelBox(energy_in, 330, 549, -15.0F, 2.9667F, -2.9569F, 10, 1, 10, 0.0F, false));

        energy2 = new ModelRenderer(this);
        energy2.setRotationPoint(-62.0F, -60.0F, 79.9784F);
        port.addChild(energy2);
        setRotationAngle(energy2, 0.0F, 3.1416F, 0.0F);
        energy2.cubeList.add(new ModelBox(energy2, 568, 149, -106.0F, 3.1667F, -6.1216F, 2, 36, 2, 0.0F, false));
        energy2.cubeList.add(new ModelBox(energy2, 110, 518, -113.0F, 3.1667F, -3.1216F, 6, 36, 7, 0.0F, false));
        energy2.cubeList.add(new ModelBox(energy2, 566, 518, -116.0F, 3.1667F, 4.8784F, 2, 36, 2, 0.0F, false));
        energy2.cubeList.add(new ModelBox(energy2, 566, 480, -106.0F, 3.1667F, 4.8784F, 2, 36, 2, 0.0F, false));
        energy2.cubeList.add(new ModelBox(energy2, 566, 442, -116.0F, 3.1667F, -6.1216F, 2, 36, 2, 0.0F, false));
        energy2.cubeList.add(new ModelBox(energy2, 518, 6, -116.0F, 3.1667F, 6.8784F, 12, 36, 3, 0.0F, false));

        cube_r43 = new ModelRenderer(this);
        cube_r43.setRotationPoint(-110.0F, 8.1667F, -5.1216F);
        energy2.addChild(cube_r43);
        setRotationAngle(cube_r43, -1.5708F, 0.0F, 0.0F);
        cube_r43.cubeList.add(new ModelBox(cube_r43, 240, 382, -6.0F, -15.0F, -8.0F, 12, 16, 3, 0.0F, false));

        cube_r44 = new ModelRenderer(this);
        cube_r44.setRotationPoint(-105.0F, 47.0667F, 3.8784F);
        energy2.addChild(cube_r44);
        setRotationAngle(cube_r44, 0.0F, -1.5708F, -1.5708F);
        cube_r44.cubeList.add(new ModelBox(cube_r44, 540, 407, -10.0F, -11.0F, -7.9F, 16, 12, 3, 0.0F, false));

        cube_r45 = new ModelRenderer(this);
        cube_r45.setRotationPoint(-110.0F, 0.1667F, -6.1431F);
        energy2.addChild(cube_r45);
        setRotationAngle(cube_r45, -0.8421F, 0.0F, 0.0F);
        cube_r45.cubeList.add(new ModelBox(cube_r45, 540, 422, -6.0F, -6.0153F, 0.0153F, 12, 6, 4, 0.0F, false));

        cube_r46 = new ModelRenderer(this);
        cube_r46.setRotationPoint(-110.0F, 2.1667F, 0.3534F);
        energy2.addChild(cube_r46);
        setRotationAngle(cube_r46, -1.5708F, 0.0F, 0.0F);
        cube_r46.cubeList.add(new ModelBox(cube_r46, 80, 557, -6.0F, -10.0F, -6.0F, 12, 12, 4, 0.0F, false));

        energy_in2 = new ModelRenderer(this);
        energy_in2.setRotationPoint(0.0F, 45.4167F, 6.0216F);
        energy2.addChild(energy_in2);
        setRotationAngle(energy_in2, 3.1416F, 0.0F, 0.0F);
        energy_in2.cubeList.add(new ModelBox(energy_in2, 34, 146, -113.0F, 2.4167F, 3.0431F, 6, 1, 6, 0.0F, false));
        energy_in2.cubeList.add(new ModelBox(energy_in2, 322, 560, -114.0F, 1.4167F, 2.0431F, 8, 1, 8, 0.0F, false));
        energy_in2.cubeList.add(new ModelBox(energy_in2, 431, 536, -115.0F, 2.9667F, 1.0431F, 10, 1, 10, 0.0F, false));

        gas = new ModelRenderer(this);
        gas.setRotationPoint(84.0F, -60.0F, 33.9784F);
        port.addChild(gas);
        setRotationAngle(gas, 0.0F, -1.5708F, 0.0F);
        gas.cubeList.add(new ModelBox(gas, 130, 240, -13.9784F, 3.1667F, -2.1F, 2, 36, 2, 0.0F, false));
        gas.cubeList.add(new ModelBox(gas, 356, 382, -20.9784F, 3.1667F, 0.9F, 6, 36, 7, 0.0F, false));
        gas.cubeList.add(new ModelBox(gas, 548, 44, -23.9784F, 3.1667F, 8.9F, 2, 36, 2, 0.0F, false));
        gas.cubeList.add(new ModelBox(gas, 548, 6, -13.9784F, 3.1667F, 8.9F, 2, 36, 2, 0.0F, false));
        gas.cubeList.add(new ModelBox(gas, 130, 202, -23.9784F, 3.1667F, -2.1F, 2, 36, 2, 0.0F, false));
        gas.cubeList.add(new ModelBox(gas, 493, 452, -23.9784F, 3.1667F, 10.9F, 12, 36, 3, 0.0F, false));

        cube_r47 = new ModelRenderer(this);
        cube_r47.setRotationPoint(-17.9784F, 8.1667F, -1.1F);
        gas.addChild(cube_r47);
        setRotationAngle(cube_r47, -1.5708F, 0.0F, 0.0F);
        cube_r47.cubeList.add(new ModelBox(cube_r47, 110, 25, -6.0F, -15.0F, -8.0F, 12, 16, 3, 0.0F, false));

        cube_r48 = new ModelRenderer(this);
        cube_r48.setRotationPoint(-12.9784F, 47.0667F, 7.9F);
        gas.addChild(cube_r48);
        setRotationAngle(cube_r48, 0.0F, -1.5708F, -1.5708F);
        cube_r48.cubeList.add(new ModelBox(cube_r48, 346, 298, -10.0F, -11.0F, -7.9F, 16, 12, 3, 0.0F, false));

        cube_r49 = new ModelRenderer(this);
        cube_r49.setRotationPoint(-17.9784F, 0.1667F, -2.1216F);
        gas.addChild(cube_r49);
        setRotationAngle(cube_r49, -0.8421F, 0.0F, 0.0F);
        cube_r49.cubeList.add(new ModelBox(cube_r49, 102, 274, -6.0F, -6.0153F, 0.0153F, 12, 6, 4, 0.0F, false));

        cube_r50 = new ModelRenderer(this);
        cube_r50.setRotationPoint(-17.9784F, 2.1667F, 4.375F);
        gas.addChild(cube_r50);
        setRotationAngle(cube_r50, -1.5708F, 0.0F, 0.0F);
        cube_r50.cubeList.add(new ModelBox(cube_r50, 434, 549, -6.0F, -10.0F, -6.0F, 12, 12, 4, 0.0F, false));

        gas_in2 = new ModelRenderer(this);
        gas_in2.setRotationPoint(0.0F, 45.4167F, 6.0216F);
        gas.addChild(gas_in2);
        setRotationAngle(gas_in2, 3.1416F, 0.0F, 0.0F);
        gas_in2.cubeList.add(new ModelBox(gas_in2, 68, 32, -20.9784F, 2.4167F, -0.9784F, 6, 1, 6, 0.0F, false));
        gas_in2.cubeList.add(new ModelBox(gas_in2, 206, 485, -21.9784F, 1.4167F, -1.9784F, 8, 1, 8, 0.0F, false));
        gas_in2.cubeList.add(new ModelBox(gas_in2, 518, 127, -22.9784F, 2.9667F, -2.9784F, 10, 1, 10, 0.0F, false));

        fluid = new ModelRenderer(this);
        fluid.setRotationPoint(80.0F, -60.0F, 61.9784F);
        port.addChild(fluid);
        setRotationAngle(fluid, 0.0F, 1.5708F, 3.1416F);
        fluid.cubeList.add(new ModelBox(fluid, 544, 84, 17.9784F, -36.8333F, -6.1F, 2, 36, 2, 0.0F, false));
        fluid.cubeList.add(new ModelBox(fluid, 330, 382, 10.9784F, -36.8333F, -3.1F, 6, 36, 7, 0.0F, false));
        fluid.cubeList.add(new ModelBox(fluid, 523, 452, 7.9784F, -36.8333F, 4.9F, 2, 36, 2, 0.0F, false));
        fluid.cubeList.add(new ModelBox(fluid, 376, 313, 17.9784F, -36.8333F, 4.9F, 2, 36, 2, 0.0F, false));
        fluid.cubeList.add(new ModelBox(fluid, 136, 307, 7.9784F, -36.8333F, -6.1F, 2, 36, 2, 0.0F, false));
        fluid.cubeList.add(new ModelBox(fluid, 90, 376, 7.9784F, -36.8333F, 6.9F, 12, 36, 3, 0.0F, false));

        cube_r51 = new ModelRenderer(this);
        cube_r51.setRotationPoint(-17.9784F, 8.1667F, -1.1F);
        fluid.addChild(cube_r51);
        setRotationAngle(cube_r51, -1.5708F, 0.0F, 0.0F);
        cube_r51.cubeList.add(new ModelBox(cube_r51, 110, 0, 25.9568F, -10.9999F, -48.0F, 12, 16, 3, 0.0F, false));

        cube_r52 = new ModelRenderer(this);
        cube_r52.setRotationPoint(-12.9784F, 47.0667F, 7.9F);
        fluid.addChild(cube_r52);
        setRotationAngle(cube_r52, 0.0F, -1.5708F, -1.5708F);
        cube_r52.cubeList.add(new ModelBox(cube_r52, 106, 292, -14.0001F, 20.9569F, -47.8999F, 16, 12, 3, 0.0F, false));

        cube_r53 = new ModelRenderer(this);
        cube_r53.setRotationPoint(-17.9784F, 0.1667F, -2.1216F);
        fluid.addChild(cube_r53);
        setRotationAngle(cube_r53, -0.8421F, 0.0F, 0.0F);
        cube_r53.cubeList.add(new ModelBox(cube_r53, 451, 516, 25.9568F, -29.6671F, -32.49F, 12, 6, 4, 0.0F, false));

        cube_r54 = new ModelRenderer(this);
        cube_r54.setRotationPoint(-17.9784F, 2.1667F, 4.375F);
        fluid.addChild(cube_r54);
        setRotationAngle(cube_r54, -1.5708F, 0.0F, 0.0F);
        cube_r54.cubeList.add(new ModelBox(cube_r54, 402, 549, 25.9568F, -5.9999F, -46.0F, 12, 12, 4, 0.0F, false));

        fluid_in = new ModelRenderer(this);
        fluid_in.setRotationPoint(0.0F, 45.4167F, 6.0216F);
        fluid.addChild(fluid_in);
        setRotationAngle(fluid_in, 3.1416F, 0.0F, 0.0F);
        fluid_in.cubeList.add(new ModelBox(fluid_in, 68, 25, 10.9784F, 42.4168F, 3.0213F, 6, 1, 6, 0.0F, false));
        fluid_in.cubeList.add(new ModelBox(fluid_in, 451, 482, 9.9784F, 41.4168F, 2.0213F, 8, 1, 8, 0.0F, false));
        fluid_in.cubeList.add(new ModelBox(fluid_in, 240, 425, 8.9784F, 42.9668F, 1.0213F, 10, 1, 10, 0.0F, false));

        gas2 = new ModelRenderer(this);
        gas2.setRotationPoint(-83.5F, -60.0F, 25.9784F);
        port.addChild(gas2);
        setRotationAngle(gas2, 0.0F, -1.5708F, 3.1416F);
        gas2.cubeList.add(new ModelBox(gas2, 566, 82, -5.9784F, -36.8333F, -2.6F, 2, 36, 2, 0.0F, false));
        gas2.cubeList.add(new ModelBox(gas2, 518, 84, -12.9784F, -36.8333F, 0.4F, 6, 36, 7, 0.0F, false));
        gas2.cubeList.add(new ModelBox(gas2, 566, 44, -15.9784F, -36.8333F, 8.4F, 2, 36, 2, 0.0F, false));
        gas2.cubeList.add(new ModelBox(gas2, 566, 6, -5.9784F, -36.8333F, 8.4F, 2, 36, 2, 0.0F, false));
        gas2.cubeList.add(new ModelBox(gas2, 560, 149, -15.9784F, -36.8333F, -2.6F, 2, 36, 2, 0.0F, false));
        gas2.cubeList.add(new ModelBox(gas2, 80, 518, -15.9784F, -36.8333F, 10.4F, 12, 36, 3, 0.0F, false));

        cube_r55 = new ModelRenderer(this);
        cube_r55.setRotationPoint(9.9784F, 8.1667F, -1.1F);
        gas2.addChild(cube_r55);
        setRotationAngle(cube_r55, -1.5708F, 0.0F, 0.0F);
        cube_r55.cubeList.add(new ModelBox(cube_r55, 45, 376, -25.9568F, -14.4998F, -48.0F, 12, 16, 3, 0.0F, false));

        cube_r56 = new ModelRenderer(this);
        cube_r56.setRotationPoint(14.9784F, 47.0667F, 7.9F);
        gas2.addChild(cube_r56);
        setRotationAngle(cube_r56, 0.0F, -1.5708F, -1.5708F);
        cube_r56.cubeList.add(new ModelBox(cube_r56, 540, 392, -10.5002F, -30.9567F, -47.9001F, 16, 12, 3, 0.0F, false));

        cube_r57 = new ModelRenderer(this);
        cube_r57.setRotationPoint(9.9784F, 0.1667F, -2.1216F);
        gas2.addChild(cube_r57);
        setRotationAngle(cube_r57, -0.8421F, 0.0F, 0.0F);
        cube_r57.cubeList.add(new ModelBox(cube_r57, 355, 535, -25.9568F, -32.2782F, -30.1595F, 12, 6, 4, 0.0F, false));

        cube_r58 = new ModelRenderer(this);
        cube_r58.setRotationPoint(9.9784F, 2.1667F, 4.375F);
        gas2.addChild(cube_r58);
        setRotationAngle(cube_r58, -1.5708F, 0.0F, 0.0F);
        cube_r58.cubeList.add(new ModelBox(cube_r58, 182, 554, -25.9568F, -9.4998F, -46.0F, 12, 12, 4, 0.0F, false));

        gas_in3 = new ModelRenderer(this);
        gas_in3.setRotationPoint(0.0F, 45.4167F, 6.0216F);
        gas2.addChild(gas_in3);
        setRotationAngle(gas_in3, 3.1416F, 0.0F, 0.0F);
        gas_in3.cubeList.add(new ModelBox(gas_in3, 0, 146, -12.9784F, 42.4167F, -0.4787F, 6, 1, 6, 0.0F, false));
        gas_in3.cubeList.add(new ModelBox(gas_in3, 463, 532, -13.9784F, 41.4167F, -1.4787F, 8, 1, 8, 0.0F, false));
        gas_in3.cubeList.add(new ModelBox(gas_in3, 391, 536, -14.9784F, 42.9667F, -2.4787F, 10, 1, 10, 0.0F, false));

        fluid2 = new ModelRenderer(this);
        fluid2.setRotationPoint(-88.0F, -60.0F, 61.9784F);
        port.addChild(fluid2);
        setRotationAngle(fluid2, 0.0F, 1.5708F, 0.0F);
        fluid2.cubeList.add(new ModelBox(fluid2, 558, 518, 17.9784F, 3.1667F, 1.9F, 2, 36, 2, 0.0F, false));
        fluid2.cubeList.add(new ModelBox(fluid2, 504, 373, 10.9784F, 3.1667F, 4.9F, 6, 36, 7, 0.0F, false));
        fluid2.cubeList.add(new ModelBox(fluid2, 558, 480, 7.9784F, 3.1667F, 12.9F, 2, 36, 2, 0.0F, false));
        fluid2.cubeList.add(new ModelBox(fluid2, 558, 442, 17.9784F, 3.1667F, 12.9F, 2, 36, 2, 0.0F, false));
        fluid2.cubeList.add(new ModelBox(fluid2, 552, 82, 7.9784F, 3.1667F, 1.9F, 2, 36, 2, 0.0F, false));
        fluid2.cubeList.add(new ModelBox(fluid2, 518, 45, 7.9784F, 3.1667F, 14.9F, 12, 36, 3, 0.0F, false));

        cube_r59 = new ModelRenderer(this);
        cube_r59.setRotationPoint(9.9784F, 8.1667F, -1.1F);
        fluid2.addChild(cube_r59);
        setRotationAngle(cube_r59, -1.5708F, 0.0F, 0.0F);
        cube_r59.cubeList.add(new ModelBox(cube_r59, 102, 146, -2.0F, -19.0F, -8.0F, 12, 16, 3, 0.0F, false));

        cube_r60 = new ModelRenderer(this);
        cube_r60.setRotationPoint(14.9784F, 47.0667F, 7.9F);
        fluid2.addChild(cube_r60);
        setRotationAngle(cube_r60, 0.0F, -1.5708F, -1.5708F);
        cube_r60.cubeList.add(new ModelBox(cube_r60, 540, 377, -6.0F, -7.0F, -7.9F, 16, 12, 3, 0.0F, false));

        cube_r61 = new ModelRenderer(this);
        cube_r61.setRotationPoint(9.9784F, 0.1667F, -2.1216F);
        fluid2.addChild(cube_r61);
        setRotationAngle(cube_r61, -0.8421F, 0.0F, 0.0F);
        cube_r61.cubeList.add(new ModelBox(cube_r61, 559, 263, -2.0F, -8.9995F, 2.6788F, 12, 6, 4, 0.0F, false));

        cube_r62 = new ModelRenderer(this);
        cube_r62.setRotationPoint(9.9784F, 2.1667F, 4.375F);
        fluid2.addChild(cube_r62);
        setRotationAngle(cube_r62, -1.5708F, 0.0F, 0.0F);
        cube_r62.cubeList.add(new ModelBox(cube_r62, 552, 247, -2.0F, -14.0F, -6.0F, 12, 12, 4, 0.0F, false));

        fluid_in2 = new ModelRenderer(this);
        fluid_in2.setRotationPoint(0.0F, 45.4167F, 6.0216F);
        fluid2.addChild(fluid_in2);
        setRotationAngle(fluid_in2, 3.1416F, 0.0F, 0.0F);
        fluid_in2.cubeList.add(new ModelBox(fluid_in2, 68, 39, 10.9784F, 2.4167F, -4.9784F, 6, 1, 6, 0.0F, false));
        fluid_in2.cubeList.add(new ModelBox(fluid_in2, 496, 423, 9.9784F, 1.4167F, -5.9784F, 8, 1, 8, 0.0F, false));
        fluid_in2.cubeList.add(new ModelBox(fluid_in2, 325, 535, 8.9784F, 2.9667F, -6.9784F, 10, 1, 10, 0.0F, false));

        glass = new ModelRenderer(this);
        glass.setRotationPoint(0.0F, 24.0F, 196.0F);


        glass1 = new ModelRenderer(this);
        glass1.setRotationPoint(0.0F, 0.0F, 0.0F);
        glass.addChild(glass1);
        setRotationAngle(glass1, 0.0F, 3.1416F, 0.0F);
        glass1.cubeList.add(new ModelBox(glass1, 164, 539, -52.0F, -56.8333F, 110.4F, 8, 36, 1, 0.0F, false));
        glass1.cubeList.add(new ModelBox(glass1, 540, 149, 42.5F, -56.8333F, 111.9F, 1, 36, 9, 0.0F, false));
        glass1.cubeList.add(new ModelBox(glass1, 538, 532, 52.5F, -56.8333F, 111.9F, 1, 36, 9, 0.0F, false));
        glass1.cubeList.add(new ModelBox(glass1, 146, 539, 44.0F, -56.8333F, 110.4F, 8, 36, 1, 0.0F, false));
        glass1.cubeList.add(new ModelBox(glass1, 538, 487, -43.5F, -56.8333F, 111.9F, 1, 36, 9, 0.0F, false));
        glass1.cubeList.add(new ModelBox(glass1, 538, 442, -53.5F, -56.8333F, 111.9F, 1, 36, 9, 0.0F, false));

        glass2 = new ModelRenderer(this);
        glass2.setRotationPoint(196.0F, 0.0F, -196.5F);
        glass.addChild(glass2);
        setRotationAngle(glass2, 0.0F, -1.5708F, 0.0F);
        glass2.cubeList.add(new ModelBox(glass2, 60, 534, 43.0004F, -59.1667F, 111.8999F, 1, 36, 9, 0.0F, false));
        glass2.cubeList.add(new ModelBox(glass2, 40, 534, 21.0F, -56.8333F, 111.9F, 1, 36, 9, 0.0F, false));
        glass2.cubeList.add(new ModelBox(glass2, 20, 534, 11.0F, -56.8333F, 111.9F, 1, 36, 9, 0.0F, false));
        glass2.cubeList.add(new ModelBox(glass2, 0, 534, 53.0004F, -59.1667F, 111.8998F, 1, 36, 9, 0.0F, false));
        glass2.cubeList.add(new ModelBox(glass2, 248, 452, 44.5004F, -59.1667F, 110.3998F, 8, 36, 1, 0.0F, false));
        glass2.cubeList.add(new ModelBox(glass2, 248, 452, 12.5F, -56.8333F, 110.4F, 8, 36, 1, 0.0F, false));

        glass3 = new ModelRenderer(this);
        glass3.setRotationPoint(-196.0F, 0.0F, -104.0F);
        glass.addChild(glass3);
        setRotationAngle(glass3, 0.0F, 1.5708F, 0.0F);
        glass3.cubeList.add(new ModelBox(glass3, 506, 532, 70.4996F, -59.1667F, 111.9003F, 1, 36, 9, 0.0F, false));
        glass3.cubeList.add(new ModelBox(glass3, 248, 452, 40.0F, -56.8333F, 110.4F, 8, 36, 1, 0.0F, false));
        glass3.cubeList.add(new ModelBox(glass3, 486, 532, 48.5F, -56.8333F, 111.9F, 1, 36, 9, 0.0F, false));
        glass3.cubeList.add(new ModelBox(glass3, 520, 194, 38.5F, -56.8333F, 111.9F, 1, 36, 9, 0.0F, false));
        glass3.cubeList.add(new ModelBox(glass3, 520, 149, 80.4996F, -59.1667F, 111.9003F, 1, 36, 9, 0.0F, false));
        glass3.cubeList.add(new ModelBox(glass3, 102, 442, 71.9996F, -59.1667F, 110.4003F, 8, 36, 1, 0.0F, false));

        glass4 = new ModelRenderer(this);
        glass4.setRotationPoint(0.0F, 24.0F, 0.0F);
        glass4.cubeList.add(new ModelBox(glass4, 240, 298, 70.1213F, -49.1213F, -66.0F, 1, 14, 70, 0.0F, false));
        glass4.cubeList.add(new ModelBox(glass4, 0, 292, -71.1213F, -49.1213F, -66.0F, 1, 14, 70, 0.0F, false));

        Warninglights = new ModelRenderer(this);
        Warninglights.setRotationPoint(-2.0F, -41.0F, -62.5F);
        Warninglights.cubeList.add(new ModelBox(Warninglights, 68, 153, 63.5F, -17.0F, -4.0F, 5, 2, 5, 0.0F, false));
        Warninglights.cubeList.add(new ModelBox(Warninglights, 14, 42, 65.0F, -25.0F, -2.25F, 2, 8, 2, 0.0F, false));
        Warninglights.cubeList.add(new ModelBox(Warninglights, 50, 32, 64.5F, -31.0F, -2.75F, 3, 6, 3, 0.0F, false));

        Warninglights2 = new ModelRenderer(this);
        Warninglights2.setRotationPoint(-4.5F, -41.0F, -60.0F);
        Warninglights2.cubeList.add(new ModelBox(Warninglights2, 0, 153, -62.0F, -17.0F, 121.5F, 5, 2, 5, 0.0F, false));
        Warninglights2.cubeList.add(new ModelBox(Warninglights2, 14, 32, -60.5F, -25.0F, 123.25F, 2, 8, 2, 0.0F, false));
        Warninglights2.cubeList.add(new ModelBox(Warninglights2, 48, 0, -61.0F, -31.0F, 122.75F, 3, 6, 3, 0.0F, false));

        ornament = new ModelRenderer(this);
        ornament.setRotationPoint(0.0F, 24.0F, 0.0F);
        ornament.cubeList.add(new ModelBox(ornament, 132, 146, -44.0F, -63.8333F, 67.125F, 2, 54, 2, 0.0F, false));
        ornament.cubeList.add(new ModelBox(ornament, 130, 50, 42.0F, -63.8333F, 67.125F, 2, 54, 2, 0.0F, false));
        ornament.cubeList.add(new ModelBox(ornament, 30, 203, 28.0F, -11.8333F, 67.125F, 14, 2, 2, 0.0F, false));
        ornament.cubeList.add(new ModelBox(ornament, 0, 201, -42.0F, -11.8333F, 67.125F, 14, 2, 2, 0.0F, false));
        ornament.cubeList.add(new ModelBox(ornament, 420, 149, -30.0F, -11.8333F, -28.875F, 2, 2, 96, 0.0F, false));
        ornament.cubeList.add(new ModelBox(ornament, 292, 452, 28.0F, -11.8333F, -27.875F, 2, 2, 95, 0.0F, false));
        ornament.cubeList.add(new ModelBox(ornament, 0, 18, 30.0F, -12.0F, 27.0F, 9, 2, 2, 0.0F, false));
        ornament.cubeList.add(new ModelBox(ornament, 34, 153, 28.0F, -12.0F, -29.0F, 11, 2, 2, 0.0F, false));
        ornament.cubeList.add(new ModelBox(ornament, 110, 44, -41.0F, -12.0F, 27.0F, 11, 2, 2, 0.0F, false));
        ornament.cubeList.add(new ModelBox(ornament, 110, 19, -41.0F, -12.0F, -29.0F, 13, 2, 2, 0.0F, false));
    }

    public void renderWithPiston(float piston, float size, boolean power, boolean isActive, TextureManager manager, double progress) {
        door1.rotationPointY = 24 - (piston * 16);
        door2.rotationPointY = 40 - (piston * 16 * 2);
        door3.rotationPointY = 56 - (piston * 16 * 3);
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        doRender(size);
        GlStateManager.popMatrix();
        if (power) {
            GlStateManager.pushMatrix();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "DigitalAssemblyTable/Led.png"));
            GlStateManager.scale(1.001F, 1.001F, 1.001F);
            GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
            MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
            doRender(size);
            MekanismRenderer.disableGlow(glowInfo);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
        }

        if (isActive) {
            GlStateManager.pushMatrix();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.scale(1.002F, 1.002F, 1.002F);
            GlStateManager.translate(-0.0012F, -0.0012F, -0.0012F);
            MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
            manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "DigitalAssemblyTable/ON.png"));
            pm.render(size);
            manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "DigitalAssemblyTable/Progress_" + getTick(progress) + ".png"));
            pm_progress.render(size);
            MekanismRenderer.disableGlow(glowInfo);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
        }
    }

    public void renderBloom(float size, boolean power, boolean isActive, TextureManager manager, double progress) {
        if (power) {
            GlStateManager.pushMatrix();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "DigitalAssemblyTable/Led.png"));
            GlStateManager.scale(1.001F, 1.001F, 1.001F);
            GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
            MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
            doRender(size);
            MekanismRenderer.disableGlow(glowInfo);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
        }
        if (isActive) {
            GlStateManager.pushMatrix();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.scale(1.002F, 1.002F, 1.002F);
            GlStateManager.translate(-0.0012F, -0.0012F, -0.0012F);
            MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
            manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "DigitalAssemblyTable/ON.png"));
            pm.render(size);
            manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "DigitalAssemblyTable/Progress_" + getTick(progress) + ".png"));
            pm_progress.render(size);
            MekanismRenderer.disableGlow(glowInfo);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
        }
    }

    public void renderItem(float size) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        doRender(size);
        glass.render(size);
        glass4.render(size);
        GlStateManager.popMatrix();

    }


    public void renderGlass(float size) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        glass.render(size);
        glass4.render(size);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }

    public void doRender(float size) {
        base.render(size);
        footpads.render(size);
        side.render(size);
        conveyorbelts.render(size);
        bone2.render(size);
        doorpanels.render(size);

        door1.render(size);
        door2.render(size);
        door3.render(size);

        bone13.render(size);
        pm.render(size);
        pm_progress.render(size);
        platform.render(size);
        platform2.render(size);
        Roboticarm.render(size);
        Roboticarm2.render(size);
        Roboticarm3.render(size);
        Roboticarm4.render(size);
        port.render(size);

        Warninglights.render(size);
        Warninglights2.render(size);
        ornament.render(size);
    }



    public int getTick(double tick) {
        if (tick >= 0 && tick < 0.125D) {
            return 1;
        } else if (tick >= 0.125D && tick < 0.25D) {
            return 2;
        } else if (tick >= 0.25F && tick < 0.375D) {
            return 3;
        } else if (tick >= 0.375D && tick < 0.5D) {
            return 4;
        } else if (tick >= 0.5D && tick < 0.625D) {
            return 5;
        } else if (tick >= 0.625D && tick < 0.75D) {
            return 6;
        } else if (tick >= 0.75D && tick < 0.875D) {
            return 7;
        } else if (tick >= 0.875D) {
            return 8;
        }
        return 0;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}