package mekanism.multiblockmachine.client.model.generator;


import mekanism.client.render.MekanismRenderer;
import mekanism.multiblockmachine.common.util.MekanismMultiblockMachineUtils;
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
public class ModelLargeHeatGenerator extends ModelBase {

    public static ResourceLocation OVERLAY_OFF = MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER, "HeatGenerator/LargeHeatGenerator_OFF.png");

    ModelRenderer drum;
    ModelRenderer ring1;
    ModelRenderer ring2;
    ModelRenderer back;
    ModelRenderer bar1;
    ModelRenderer bar2;
    ModelRenderer plate;
    ModelRenderer fin8;
    ModelRenderer fin7;
    ModelRenderer fin1;
    ModelRenderer fin2;
    ModelRenderer fin3;
    ModelRenderer fin4;
    ModelRenderer fin5;
    ModelRenderer fin6;
    ModelRenderer base;
    ModelRenderer tank;
    ModelRenderer cube_r1;
    ModelRenderer cube_r2;
    ModelRenderer cube_r3;
    ModelRenderer cube_r4;
    ModelRenderer cube_r5;
    ModelRenderer io;
    ModelRenderer Heat_sinks;
    ModelRenderer Heat_sinks2;
    ModelRenderer bone;
    ModelRenderer cube_r6;
    ModelRenderer cube_r7;
    ModelRenderer bb_main;
    ModelRenderer cube_r8;

    public ModelLargeHeatGenerator() {
        textureWidth = 512;
        textureHeight = 256;

        drum = new ModelRenderer(this);
        drum.setRotationPoint(-8.0F, 8.5F, -7.5F);


        ring1 = new ModelRenderer(this);
        ring1.setRotationPoint(3.0F, 8.0F, -8.0F);


        ring2 = new ModelRenderer(this);
        ring2.setRotationPoint(-5.0F, 8.0F, -8.0F);


        back = new ModelRenderer(this);
        back.setRotationPoint(-8.0F, 8.0F, 2.0F);


        bar1 = new ModelRenderer(this);
        bar1.setRotationPoint(3.0F, 9.0F, 6.0F);


        bar2 = new ModelRenderer(this);
        bar2.setRotationPoint(-5.0F, 9.0F, 6.0F);


        plate = new ModelRenderer(this);
        plate.setRotationPoint(-4.0F, 12.0F, 6.0F);


        fin8 = new ModelRenderer(this);
        fin8.setRotationPoint(-8.0F, 8.0F, 6.0F);


        fin7 = new ModelRenderer(this);
        fin7.setRotationPoint(-8.0F, 10.0F, 6.0F);


        fin1 = new ModelRenderer(this);
        fin1.setRotationPoint(4.0F, 12.0F, 6.0F);


        fin2 = new ModelRenderer(this);
        fin2.setRotationPoint(4.0F, 14.0F, 6.0F);


        fin3 = new ModelRenderer(this);
        fin3.setRotationPoint(4.0F, 16.0F, 6.0F);


        fin4 = new ModelRenderer(this);
        fin4.setRotationPoint(-8.0F, 12.0F, 6.0F);


        fin5 = new ModelRenderer(this);
        fin5.setRotationPoint(-8.0F, 14.0F, 6.0F);


        fin6 = new ModelRenderer(this);
        fin6.setRotationPoint(-8.0F, 16.0F, 6.0F);


        base = new ModelRenderer(this);
        base.setRotationPoint(-8.0F, 18.0F, -8.0F);


        tank = new ModelRenderer(this);
        tank.setRotationPoint(0.0F, 8.0F, 0.0F);
        tank.cubeList.add(new ModelBox(tank, 0, 230, 10.0F, -13.0F, -23.25F, 11, 12, 1, 0.0F, false));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        tank.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, 1.5708F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 79, -20.0F, -8.0F, -24.0F, 5, 32, 32, 0.0F, false));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 79, -3.0F, -8.0F, -24.0F, 5, 32, 32, 0.0F, false));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 362, 0, -30.0F, -9.0F, -22.0F, 42, 30, 30, 0.0F, false));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 400, 229, -30.0F, -5.0F, -23.5F, 9, 26, 1, 0.0F, false));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 400, 229, 3.0F, -5.0F, -23.5F, 9, 26, 1, 0.0F, false));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 143, -32.0F, -7.0F, -23.0F, 46, 30, 30, 0.0F, false));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
        tank.addChild(cube_r2);
        setRotationAngle(cube_r2, 1.5708F, 0.0F, 1.5708F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 400, 229, -30.0F, -21.0F, -23.5F, 9, 26, 1, 0.0F, false));
        cube_r2.cubeList.add(new ModelBox(cube_r2, 400, 229, 3.0F, -21.0F, -23.5F, 9, 26, 1, 0.0F, false));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(0.0F, 1.0F, 0.0F);
        tank.addChild(cube_r3);
        setRotationAngle(cube_r3, 1.5708F, 0.0F, 1.5708F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 422, 225, -15.0F, -21.0F, -23.5F, 10, 26, 1, 0.0F, false));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(0.0F, 0.0F, 0.0F);
        tank.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 1.5708F, 1.5708F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 422, 225, -5.0F, -5.0F, -32.5F, 26, 26, 1, 0.0F, false));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(0.0F, 1.0F, 0.0F);
        tank.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, 1.5708F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 422, 225, -15.0F, -5.0F, -23.5F, 10, 26, 1, 0.0F, false));

        io = new ModelRenderer(this);
        io.setRotationPoint(-8.0503F, -21.0F, 26.0502F);
        io.cubeList.add(new ModelBox(io, 0, 0, 5.0503F, 34.0F, -5.5502F, 6, 6, 3, 0.0F, false));
        io.cubeList.add(new ModelBox(io, 0, 0, 5.0503F, 2.0F, -5.5502F, 6, 6, 3, 0.0F, false));
        io.cubeList.add(new ModelBox(io, 82, 245, 5.0503F, 18.0F, -5.0502F, 6, 6, 2, 0.0F, false));
        io.cubeList.add(new ModelBox(io, 132, 27, 4.0503F, 33.0F, -3.0502F, 8, 8, 1, 0.0F, false));
        io.cubeList.add(new ModelBox(io, 132, 36, 4.0503F, 17.0F, -3.0502F, 8, 8, 1, 0.0F, false));
        io.cubeList.add(new ModelBox(io, 132, 36, 4.0503F, 1.0F, -3.0502F, 8, 8, 1, 0.0F, false));
        io.cubeList.add(new ModelBox(io, 451, 36, 4.0503F, 33.0F, -5.8002F, 8, 8, 1, 0.0F, false));
        io.cubeList.add(new ModelBox(io, 387, 121, 4.0503F, 17.0F, -5.8002F, 8, 8, 1, 0.0F, false));
        io.cubeList.add(new ModelBox(io, 451, 36, 4.0503F, 1.0F, -5.8002F, 8, 8, 1, 0.0F, false));

        Heat_sinks = new ModelRenderer(this);
        Heat_sinks.setRotationPoint(0.0F, 24.0F, 0.0F);
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 440, 108, 8.0F, -4.0F, 21.5F, 14, 2, 2, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 363, 105, 8.0F, -46.0F, 21.5F, 14, 2, 2, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 508, 126, 20.0F, -44.0F, 21.75F, 1, 40, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 508, 126, 18.0F, -44.0F, 21.75F, 1, 40, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 508, 126, 16.0F, -44.0F, 21.75F, 1, 40, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 508, 126, 9.0F, -44.0F, 21.75F, 1, 40, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 508, 126, 13.0F, -44.0F, 21.75F, 1, 40, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 508, 126, 11.0F, -44.0F, 21.75F, 1, 40, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -6.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -8.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -12.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -10.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -16.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -14.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -20.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -18.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -43.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -41.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -39.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -37.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -35.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -33.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -31.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -29.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -22.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -27.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks.cubeList.add(new ModelBox(Heat_sinks, 364, 124, 8.0F, -24.0F, 22.0F, 14, 1, 1, 0.0F, false));

        Heat_sinks2 = new ModelRenderer(this);
        Heat_sinks2.setRotationPoint(0.0F, 24.0F, 0.0F);
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 440, 108, -22.0F, -4.0F, 21.5F, 14, 2, 2, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 363, 105, -22.0F, -46.0F, 21.5F, 14, 2, 2, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 508, 126, -10.0F, -44.0F, 21.75F, 1, 40, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 508, 126, -12.0F, -44.0F, 21.75F, 1, 40, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 508, 126, -14.0F, -44.0F, 21.75F, 1, 40, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 508, 126, -21.0F, -44.0F, 21.75F, 1, 40, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 508, 126, -17.0F, -44.0F, 21.75F, 1, 40, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 508, 126, -19.0F, -44.0F, 21.75F, 1, 40, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -6.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -8.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -12.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -10.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -16.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -14.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -20.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -18.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -43.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -41.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -39.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -37.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -35.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -33.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -31.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -29.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -22.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -27.0F, 22.0F, 14, 1, 1, 0.0F, false));
        Heat_sinks2.cubeList.add(new ModelBox(Heat_sinks2, 364, 124, -22.0F, -24.0F, 22.0F, 14, 1, 1, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(19.2929F, -23.0F, -3.8787F);
        bone.cubeList.add(new ModelBox(bone, 469, 0, 2.7061F, 27.0F, -6.1213F, 2, 17, 8, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 471, 2, 2.7061F, 25.0F, -4.1213F, 2, 2, 6, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 473, 4, 2.7061F, 23.0F, -2.1213F, 2, 2, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 471, 2, 2.7061F, -0.999F, -2.1213F, 2, 45, 6, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 471, 2, -2.2929F, -0.999F, -2.1213F, 5, 2, 6, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 473, 4, -4.2929F, -0.999F, -0.1213F, 2, 2, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 474, 5, -11.2929F, -0.999F, 0.8787F, 7, 2, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 472, 2, 2.7071F, -1.0F, 3.8787F, 2, 45, 8, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 466, 27, -11.2929F, -1.0F, 3.8787F, 14, 2, 8, 0.0F, false));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(-3.0F, 0.0F, 0.0F);
        bone.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.7854F, 0.0F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 474, 5, -3.0F, -0.999F, -1.0F, 5, 2, 3, 0.0F, false));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(3.7071F, 26.4166F, -4.1237F);
        bone.addChild(cube_r7);
        setRotationAngle(cube_r7, -0.7854F, 0.0F, 0.0F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 474, 5, -1.001F, -8.175F, -1.0F, 2, 10, 3, 0.0F, false));

        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.cubeList.add(new ModelBox(bb_main, 450, 2, 7.0F, -48.0F, 8.0F, 17, 45, 14, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 450, 0, -24.0F, -48.0F, 8.0F, 17, 45, 14, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 284, 197, -7.0F, -48.0F, 8.0F, 14, 45, 13, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 144, 179, 8.0F, -47.0F, -23.0F, 15, 44, 31, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 447, 45, -7.0F, -48.0F, 21.0F, 14, 3, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 496, 200, 22.0F, -48.0F, 22.0F, 2, 46, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 504, 200, -24.0F, -48.0F, 22.0F, 2, 46, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 420, 252, -22.0F, -48.0F, 22.0F, 44, 2, 2, 0.0F, false));

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(0.0F, -1.0F, -1.0F);
        bb_main.addChild(cube_r8);
        setRotationAngle(cube_r8, -3.1416F, 0.0F, 0.0F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 124, 155, -24.0F, -1.0F, -23.0F, 31, 3, 15, 0.0F, false));
        cube_r8.cubeList.add(new ModelBox(cube_r8, 313, 191, -24.0F, -1.0F, -25.0F, 48, 2, 2, 0.0F, false));
        cube_r8.cubeList.add(new ModelBox(cube_r8, 190, 163, -24.0F, -1.0F, -8.0F, 31, 2, 31, 0.0F, false));
        cube_r8.cubeList.add(new ModelBox(cube_r8, 2, 207, 7.0F, -1.0F, -23.0F, 17, 3, 46, 0.0F, false));
    }

    public void render(double tick,float size, boolean active, TextureManager manager) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        doRender(size);
        manager.bindTexture(active ? MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER, "HeatGenerator/LargeHeatGenerator_ON_" + getTick(tick) + ".png") : OVERLAY_OFF);
        GlStateManager.scale(1.001F, 1.001F, 1.001F);
        GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
        MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
        doRender(size);
        MekanismRenderer.disableGlow(glowInfo);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }



    public void renderBloom(double tick,float size, boolean active, TextureManager manager) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        manager.bindTexture(active ? MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER, "HeatGenerator/LargeHeatGenerator_ON_" + getTick(tick) + ".png") : OVERLAY_OFF);
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
        drum.render(size);
        ring1.render(size);
        ring2.render(size);
        back.render(size);
        bar1.render(size);
        bar2.render(size);
        plate.render(size);
        fin8.render(size);
        fin7.render(size);
        fin1.render(size);
        fin2.render(size);
        fin3.render(size);
        fin4.render(size);
        fin5.render(size);
        fin6.render(size);
        base.render(size);
        tank.render(size);
        io.render(size);
        Heat_sinks.render(size);
        Heat_sinks2.render(size);
        bone.render(size);
        bb_main.render(size);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
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
