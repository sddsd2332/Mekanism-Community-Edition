package mekanism.multiblockmachine.client.model;


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
public class ModelLargeElectrolyticSeparator extends ModelBase {

    public static ResourceLocation OVERLAY_OFF = MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ElectrolyticSeparator/ElectrolyticSeparator_OFF.png");

    ModelRenderer group;
    ModelRenderer group2;
    ModelRenderer Console;
    ModelRenderer portRight_r1;
    ModelRenderer pm;
    ModelRenderer cube_r1;
    ModelRenderer cube_r2;
    ModelRenderer portRight_r2;
    ModelRenderer portRight_r3;
    ModelRenderer portRight_r4;
    ModelRenderer bb_main;

    public ModelLargeElectrolyticSeparator() {
        textureWidth = 256;
        textureHeight = 256;

        group = new ModelRenderer(this);
        group.setRotationPoint(-8.0F, 3.75F, 18.5F);
        group.cubeList.add(new ModelBox(group, 144, 68, 3.0F, -5.75F, -33.5F, 10, 1, 1, 0.0F, false));
        group.cubeList.add(new ModelBox(group, 64, 144, 12.0F, -5.75F, -32.5F, 1, 1, 7, 0.0F, false));
        group.cubeList.add(new ModelBox(group, 10, 140, 3.0F, -5.75F, -32.5F, 1, 1, 7, 0.0F, false));
        group.cubeList.add(new ModelBox(group, 108, 61, 3.0F, -5.75F, -25.5F, 10, 1, 8, 0.0F, false));
        group.cubeList.add(new ModelBox(group, 36, 20, 3.0F, -4.75F, -33.5F, 1, 19, 1, 0.0F, false));
        group.cubeList.add(new ModelBox(group, 22, 151, 5.0F, -5.25F, -28.5F, 6, 2, 2, 0.0F, false));
        group.cubeList.add(new ModelBox(group, 150, 106, 5.0F, -5.25F, -31.5F, 6, 2, 2, 0.0F, false));
        group.cubeList.add(new ModelBox(group, 0, 96, 5.0F, -3.75F, -32.75F, 6, 12, 2, 0.0F, false));
        group.cubeList.add(new ModelBox(group, 128, 113, 3.0F, 14.25F, -33.5F, 10, 1, 1, 0.0F, false));
        group.cubeList.add(new ModelBox(group, 36, 0, 12.0F, -4.75F, -33.5F, 1, 19, 1, 0.0F, false));
        group.cubeList.add(new ModelBox(group, 113, 116, 3.0F, -4.75F, -32.5F, 10, 20, 15, 0.0F, false));

        group2 = new ModelRenderer(this);
        group2.setRotationPoint(-8.0F, -7.5F, 20.0F);
        group2.cubeList.add(new ModelBox(group2, 160, 128, 5.0F, -0.25F, -8.5F, 6, 1, 1, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 108, 59, -10.0F, -0.5F, -3.0F, 36, 1, 1, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 138, 11, 1.0F, -0.5F, -13.0F, 2, 1, 10, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 108, 57, -10.0F, -0.5F, -14.0F, 36, 1, 1, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 138, 0, 13.0F, -0.5F, -13.0F, 2, 1, 10, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 18, 140, 25.0F, -0.5F, -13.0F, 1, 1, 10, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 138, 102, -10.0F, -0.5F, -13.0F, 1, 1, 10, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 120, 160, -7.0F, -0.25F, -8.5F, 6, 1, 1, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 106, 160, 17.0F, -0.25F, -8.5F, 6, 1, 1, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 158, 93, 5.0F, 0.25F, -11.0F, 6, 1, 1, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 157, 151, 5.0F, 0.25F, -6.0F, 6, 1, 1, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 152, 82, -7.0F, 0.25F, -11.0F, 6, 1, 1, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 138, 16, -2.0F, 0.25F, -10.0F, 1, 1, 4, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 138, 11, -7.0F, 0.25F, -10.0F, 1, 1, 4, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 150, 110, -7.0F, 0.25F, -6.0F, 6, 1, 1, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 0, 110, 17.0F, 0.25F, -11.0F, 6, 1, 1, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 138, 5, 22.0F, 0.25F, -10.0F, 1, 1, 4, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 138, 0, 17.0F, 0.25F, -10.0F, 1, 1, 4, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 0, 69, 17.0F, 0.25F, -6.0F, 6, 1, 1, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 34, 41, 5.0F, 0.25F, -10.0F, 1, 1, 4, 0.0F, false));
        group2.cubeList.add(new ModelBox(group2, 36, 36, 10.0F, 0.25F, -10.0F, 1, 1, 4, 0.0F, false));

        Console = new ModelRenderer(this);
        Console.setRotationPoint(-0.5F, 16.0F, 20.0F);
        Console.cubeList.add(new ModelBox(Console, 9, 8, -2.0F, -8.0F, -36.5F, 5, 5, 1, 0.0F, false));
        Console.cubeList.add(new ModelBox(Console, 100, 29, -8.0F, -2.0F, -40.5F, 17, 1, 2, 0.0F, false));
        Console.cubeList.add(new ModelBox(Console, 110, 18, -8.0F, -1.0F, -40.5F, 1, 3, 2, 0.0F, false));
        Console.cubeList.add(new ModelBox(Console, 8, 14, -9.0F, 2.0F, -40.5F, 3, 1, 3, 0.0F, false));
        Console.cubeList.add(new ModelBox(Console, 114, 15, 8.0F, -1.0F, -40.5F, 1, 3, 2, 0.0F, false));
        Console.cubeList.add(new ModelBox(Console, 13, 14, 7.0F, 2.0F, -40.5F, 3, 1, 3, 0.0F, false));
        Console.cubeList.add(new ModelBox(Console, 12, 12, -2.0F, -2.0F, -42.75F, 5, 1, 2, 0.0F, false));

        portRight_r1 = new ModelRenderer(this);
        portRight_r1.setRotationPoint(4.5F, -2.1606F, -41.2336F);
        Console.addChild(portRight_r1);
        setRotationAngle(portRight_r1, 0.3927F, 0.0F, 0.0F);
        portRight_r1.cubeList.add(new ModelBox(portRight_r1, 136, 61, -11.0F, -0.75F, -2.5F, 14, 1, 6, 0.0F, false));

        pm = new ModelRenderer(this);
        pm.setRotationPoint(-7.498F, -10.0F, -30.5975F);
        Console.addChild(pm);
        pm.cubeList.add(new ModelBox(pm, 0, 13, -0.4215F, -2.0F, -5.9658F, 17, 4, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 18, 13, 10.8403F, -1.5F, -6.7817F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 11, 9, 10.8403F, 0.5F, -6.7817F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 12, 12, 7.8403F, -0.5F, -6.7817F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 18, 20, 4.8403F, -1.5F, -6.7817F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 16, 15, 4.8403F, 0.5F, -6.7817F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 48, 96, 0.9128F, -5.0F, -7.299F, 14, 10, 1, 0.0F, false));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-5.8122F, 0.0F, -8.0F);
        pm.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.3927F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 112, 96, 19.6F, -5.0F, 8.876F, 14, 10, 1, 0.0F, false));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-5.8122F, 0.0F, -8.0F);
        pm.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, -0.3927F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 128, 70, -8.0F, -5.0F, -1.724F, 14, 10, 1, 0.0F, false));

        portRight_r2 = new ModelRenderer(this);
        portRight_r2.setRotationPoint(8.3403F, 0.0F, -6.2817F);
        pm.addChild(portRight_r2);
        setRotationAngle(portRight_r2, 0.0F, 0.3927F, 0.0F);
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 11, 9, 10.0F, 0.5F, 2.5F, 1, 1, 1, 0.0F, false));
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 13, 9, 10.0F, -1.5F, 2.5F, 1, 1, 1, 0.0F, false));
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 19, 12, 16.0F, -1.5F, 2.5F, 1, 1, 1, 0.0F, false));
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 15, 11, 16.0F, 0.5F, 2.5F, 1, 1, 1, 0.0F, false));
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 15, 12, 13.0F, -0.5F, 2.5F, 1, 1, 1, 0.0F, false));

        portRight_r3 = new ModelRenderer(this);
        portRight_r3.setRotationPoint(8.0785F, 2.75F, -6.4998F);
        pm.addChild(portRight_r3);
        setRotationAngle(portRight_r3, 0.0F, 0.3927F, 0.0F);
        portRight_r3.cubeList.add(new ModelBox(portRight_r3, 6, 12, 7.3543F, -4.75F, 3.7053F, 11, 4, 1, 0.0F, false));

        portRight_r4 = new ModelRenderer(this);
        portRight_r4.setRotationPoint(8.0785F, 2.75F, -6.4998F);
        pm.addChild(portRight_r4);
        setRotationAngle(portRight_r4, 0.0F, -0.3927F, 0.0F);
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 10, 7, -14.8543F, -3.25F, 2.7053F, 1, 1, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 16, 14, -11.8543F, -2.25F, 2.7053F, 1, 1, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 15, 10, -17.8543F, -2.25F, 2.7053F, 1, 1, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 9, 11, -17.8543F, -4.25F, 2.7053F, 1, 1, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 7, 12, -18.3543F, -4.75F, 3.7053F, 11, 4, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 22, 14, -11.8543F, -4.25F, 2.7053F, 1, 1, 1, 0.0F, false));

        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -23.0F, -5.0F, -23.0F, 46, 5, 46, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 150, 96, 12.0F, -12.0F, -24.0F, 8, 8, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 148, 113, -20.0F, -12.0F, -24.0F, 8, 8, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 90, 151, 21.0F, -11.0F, 13.0F, 2, 6, 6, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 140, 23.0F, -12.0F, 12.0F, 1, 8, 8, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 52, 153, 12.0F, -12.0F, 23.0F, 8, 8, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 152, 11, -4.0F, -12.0F, 23.0F, 8, 8, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 152, 0, -20.0F, -12.0F, 23.0F, 8, 8, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 28, 157, 13.0F, -11.0F, 21.0F, 6, 6, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 70, 153, -19.0F, -11.0F, 21.0F, 6, 6, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 12, 151, -23.0F, -11.0F, 13.0F, 2, 6, 6, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 138, 28, -24.0F, -12.0F, 12.0F, 1, 8, 8, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 51, -22.0F, -30.0F, 2.0F, 44, 25, 20, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 64, 96, 6.0F, -24.0F, -15.0F, 16, 19, 16, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 96, -22.0F, -24.0F, -15.0F, 16, 19, 16, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 148, 28, 11.0F, -25.0F, -10.0F, 6, 1, 6, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 88, 144, -17.0F, -25.0F, -10.0F, 6, 1, 6, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 108, 54, -18.0F, -31.0F, 16.0F, 36, 1, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 108, 51, -18.0F, -31.0F, 6.0F, 36, 1, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 148, 36, -18.0F, -31.0F, 8.0F, 2, 1, 8, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 32, 148, 16.0F, -31.0F, 8.0F, 2, 1, 8, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 72, 144, 4.0F, -31.0F, 8.0F, 4, 1, 8, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 48, 144, -8.0F, -31.0F, 8.0F, 4, 1, 8, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 21, 6.125F, -22.0F, -1.0F, 14, 17, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 32, 131, -4.0F, -25.0F, -1.0F, 8, 13, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -20.0F, -22.0F, 1.0F, 14, 17, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 160, 35, 12.5F, -30.0F, -8.5F, 3, 5, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 156, -15.5F, -30.0F, -8.5F, 3, 5, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 155, 155, -15.5F, -30.0F, -6.0F, 3, 3, 5, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 155, 155, 12.5F, -30.0F, -6.0F, 3, 3, 5, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 157, 159, -4.0F, -27.0F, -4.0F, 3, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 158, 159, 1.0F, -27.0F, -4.0F, 3, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, -1, 30, 1.0F, -30.0F, -4.0F, 14, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, -2, 32, -15.0F, -30.0F, -4.0F, 14, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 54, 162, -12.0F, -26.0F, -2.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 142, 96, -9.0F, -26.0F, -5.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 19, 140, -20.0F, -26.0F, -5.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 44, 162, -20.0F, -26.0F, -2.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 79, 161, -20.0F, -26.0F, -13.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 140, -20.0F, -26.0F, -12.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 69, 161, -12.0F, -26.0F, -13.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 138, 28, -9.0F, -26.0F, -12.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 131, 8.0F, -26.0F, -12.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 144, 160, 8.0F, -26.0F, -13.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 134, 160, 16.0F, -26.0F, -13.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 128, 81, 19.0F, -26.0F, -12.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 108, 61, 19.0F, -26.0F, -5.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 86, 153, 16.0F, -26.0F, -2.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 52, 131, 8.0F, -26.0F, -2.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 72, 107, 8.0F, -26.0F, -5.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 131, 10.0F, -26.0F, -11.0F, 8, 1, 8, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 128, 81, -18.0F, -26.0F, -11.0F, 8, 1, 8, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 142, 151, -3.0F, -11.0F, 20.0F, 6, 6, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 124, 151, -3.0F, -11.0F, 0.0F, 6, 6, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 106, 151, -3.0F, -11.0F, -16.5F, 6, 6, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 51, -4.0F, -22.0F, 21.6F, 8, 17, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 82, 131, 13.0F, -11.0F, -22.0F, 6, 6, 7, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 82, 131, -19.0F, -11.0F, -22.0F, 6, 6, 7, 0.0F, false));
    }

    public void render(double tick, float size, boolean on, TextureManager manager, double fluidTank, double leftTank, double rightTank) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        doRender(size); //渲染本体
        manager.bindTexture(on ? MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ElectrolyticSeparator/ElectrolyticSeparator_LED_ON_" + getTick(tick) + ".png") : OVERLAY_OFF);
        GlStateManager.scale(1.001F, 1.001F, 1.001F);
        GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
        MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
        doRender(size); //渲染灯光
        if (on) {
            GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
            manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ElectrolyticSeparator/ElectrolyticSeparator_Screen_ON_" + getTick(tick) + ".png"));
            doRender(size);  //渲染屏幕
            manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ElectrolyticSeparator/ElectrolyticSeparator_Screen1_ON_" + getScreenTick(tick) + ".png"));
            doRender(size);  //渲染屏幕2
            if (fluidTank > 0) {
                manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ElectrolyticSeparator/Fluid/FluidTank_" + getNumberTanks(fluidTank) + ".png"));
                doRender(size);//渲染屏幕上的流体数量
            }
            if (leftTank > 0) {
                manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ElectrolyticSeparator/Left/LeftTank_" + getNumberTanks(leftTank) + ".png"));
                doRender(size);//渲染屏幕上的气体左储罐数量
            }
            if (rightTank > 0) {
                manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ElectrolyticSeparator/Right/RightTank_" + getNumberTanks(rightTank) + ".png"));
                doRender(size);//渲染屏幕上的气体右储罐数量
            }
        }
        MekanismRenderer.disableGlow(glowInfo);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }

    public void doRender(float size) {
        group.render(size);
        group2.render(size);
        Console.render(size);
        bb_main.render(size);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public int getNumberTanks(double number) {
        if (number >= 0.1F && number < 0.2F) {
            return 0;
        } else if (number >= 0.2F && number < 0.3F) {
            return 1;
        } else if (number >= 0.3F && number < 0.4F) {
            return 2;
        } else if (number >= 0.4F && number < 0.5F) {
            return 3;
        } else if (number >= 0.5F && number < 0.6F) {
            return 4;
        } else if (number >= 0.6F && number < 0.7F) {
            return 5;
        } else if (number >= 0.7F && number < 0.8F) {
            return 6;
        } else if (number >= 0.8F && number < 0.9F) {
            return 7;
        } else if (number >= 0.9F && number < 1F) {
            return 8;
        }
        return 9;
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

    public int getScreenTick(double tick) {
        if (tick >= 0.2F && tick < 0.3F || tick >= 0.4F && tick < 0.5F || tick >= 0.6F && tick < 0.7F || tick >= 0.8F && tick < 0.9F) {
            return 1;
        }
        return 0;
    }
}