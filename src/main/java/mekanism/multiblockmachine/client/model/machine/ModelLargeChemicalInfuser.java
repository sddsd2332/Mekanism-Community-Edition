package mekanism.multiblockmachine.client.model.machine;


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
public class ModelLargeChemicalInfuser extends ModelBase {

    public static ResourceLocation OVERLAY_OFF = MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ChemicalInfuser/ChemicalInfuser_OFF.png");

    ModelRenderer Console;
    ModelRenderer portRight_r1;
    ModelRenderer pm;
    ModelRenderer cube_r1;
    ModelRenderer cube_r2;
    ModelRenderer portRight_r2;
    ModelRenderer portRight_r3;
    ModelRenderer portRight_r4;
    ModelRenderer bb_main;

    public ModelLargeChemicalInfuser() {
        textureWidth = 256;
        textureHeight = 256;

        Console = new ModelRenderer(this);
        Console.setRotationPoint(-0.5F, 16.0F, 20.0F);
        Console.cubeList.add(new ModelBox(Console, 11, 19, -2.0F, -14.0F, -36.5F, 5, 17, 1, 0.0F, false));
        Console.cubeList.add(new ModelBox(Console, 96, 27, -8.0F, -2.0F, -40.5F, 17, 1, 2, 0.0F, false));
        Console.cubeList.add(new ModelBox(Console, 112, 23, -8.0F, -1.0F, -40.5F, 1, 3, 2, 0.0F, false));
        Console.cubeList.add(new ModelBox(Console, 13, 32, -9.0F, 2.0F, -40.5F, 3, 1, 3, 0.0F, false));
        Console.cubeList.add(new ModelBox(Console, 111, 21, 8.0F, -1.0F, -40.5F, 1, 3, 2, 0.0F, false));
        Console.cubeList.add(new ModelBox(Console, 12, 32, 7.0F, 2.0F, -40.5F, 3, 1, 3, 0.0F, false));
        Console.cubeList.add(new ModelBox(Console, 13, 33, -2.0F, -2.0F, -42.75F, 5, 1, 2, 0.0F, false));

        portRight_r1 = new ModelRenderer(this);
        portRight_r1.setRotationPoint(4.5F, -2.1606F, -41.2336F);
        Console.addChild(portRight_r1);
        setRotationAngle(portRight_r1, 0.3927F, 0.0F, 0.0F);
        portRight_r1.cubeList.add(new ModelBox(portRight_r1, 112, 96, -11.0F, -0.75F, -2.5F, 14, 1, 6, 0.0F, false));

        pm = new ModelRenderer(this);
        pm.setRotationPoint(-7.498F, -10.0F, -30.5975F);
        Console.addChild(pm);
        pm.cubeList.add(new ModelBox(pm, 0, 34, -0.4215F, -2.0F, -5.9658F, 17, 4, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 17, 18, 10.8403F, -1.5F, -6.7817F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 18, 18, 10.8403F, 0.5F, -6.7817F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 16, 14, 7.8403F, -0.5F, -6.7817F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 27, 13, 4.8403F, -1.5F, -6.7817F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 16, 12, 4.8403F, 0.5F, -6.7817F, 1, 1, 1, 0.0F, false));
        pm.cubeList.add(new ModelBox(pm, 138, 11, 0.9128F, -5.0F, -7.299F, 14, 10, 1, 0.0F, false));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-5.8122F, 0.0F, -8.0F);
        pm.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.3927F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 138, 0, 19.6F, -5.0F, 8.876F, 14, 10, 1, 0.0F, false));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-5.8122F, 0.0F, -8.0F);
        pm.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, -0.3927F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 66, 128, -8.0F, -5.0F, -1.724F, 14, 10, 1, 0.0F, false));

        portRight_r2 = new ModelRenderer(this);
        portRight_r2.setRotationPoint(8.3403F, 0.0F, -6.2817F);
        pm.addChild(portRight_r2);
        setRotationAngle(portRight_r2, 0.0F, 0.3927F, 0.0F);
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 20, 10, 10.0F, 0.5F, 2.5F, 1, 1, 1, 0.0F, false));
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 21, 17, 10.0F, -1.5F, 2.5F, 1, 1, 1, 0.0F, false));
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 23, 23, 16.0F, -1.5F, 2.5F, 1, 1, 1, 0.0F, false));
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 16, 20, 16.0F, 0.5F, 2.5F, 1, 1, 1, 0.0F, false));
        portRight_r2.cubeList.add(new ModelBox(portRight_r2, 18, 8, 13.0F, -0.5F, 2.5F, 1, 1, 1, 0.0F, false));

        portRight_r3 = new ModelRenderer(this);
        portRight_r3.setRotationPoint(8.0785F, 2.75F, -6.4998F);
        pm.addChild(portRight_r3);
        setRotationAngle(portRight_r3, 0.0F, 0.3927F, 0.0F);
        portRight_r3.cubeList.add(new ModelBox(portRight_r3, 6, 31, 7.3543F, -4.75F, 3.7053F, 11, 4, 1, 0.0F, false));

        portRight_r4 = new ModelRenderer(this);
        portRight_r4.setRotationPoint(8.0785F, 2.75F, -6.4998F);
        pm.addChild(portRight_r4);
        setRotationAngle(portRight_r4, 0.0F, -0.3927F, 0.0F);
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 8, 8, -14.8543F, -3.25F, 2.7053F, 1, 1, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 21, 14, -11.8543F, -2.25F, 2.7053F, 1, 1, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 19, 12, -17.8543F, -2.25F, 2.7053F, 1, 1, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 15, 8, -17.8543F, -4.25F, 2.7053F, 1, 1, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 6, 32, -18.3543F, -4.75F, 3.7053F, 11, 4, 1, 0.0F, false));
        portRight_r4.cubeList.add(new ModelBox(portRight_r4, 16, 4, -11.8543F, -4.25F, 2.7053F, 1, 1, 1, 0.0F, false));

        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.cubeList.add(new ModelBox(bb_main, 52, 152, -4.0F, -12.0F, 23.0F, 8, 8, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 18, 152, 12.0F, -12.0F, 23.0F, 8, 8, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 152, -20.0F, -12.0F, 23.0F, 8, 8, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 148, 108, 12.0F, -12.0F, -24.0F, 8, 8, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 85, 147, -20.0F, -12.0F, -24.0F, 8, 8, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 155, 156, -19.0F, -11.0F, 21.0F, 6, 6, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 138, 42, -5.0F, -26.0F, 21.0F, 10, 1, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 36, 20, -5.0F, -25.0F, 21.0F, 1, 19, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 36, 0, 4.0F, -25.0F, 21.0F, 1, 19, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 128, 108, -5.0F, -6.0F, 21.0F, 10, 1, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 102, 65, -3.0F, -21.0F, 19.5F, 6, 2, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 99, -3.0F, -24.0F, 19.5F, 6, 2, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 70, 155, -3.0F, -18.0F, 19.25F, 6, 6, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 155, 51, 13.0F, -11.0F, 21.0F, 6, 6, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 123, 149, -23.0F, -11.0F, 13.0F, 2, 6, 6, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 53, 161, -12.0F, -32.0F, 8.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 20, 161, -20.0F, -32.0F, 19.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 126, 51, -20.0F, -32.0F, 16.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 138, 35, -17.0F, -31.0F, 11.0F, 6, 1, 6, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 112, 87, 19.0F, -32.0F, 16.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 102, 51, 8.0F, -32.0F, 16.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 72, 87, 8.0F, -32.0F, 9.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 10, 161, 8.0F, -32.0F, 8.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 48, 87, 19.0F, -32.0F, 9.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 161, 16.0F, -32.0F, 8.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 159, 43, 8.0F, -32.0F, 19.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 102, 51, 10.0F, -32.0F, 10.0F, 8, 1, 8, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 145, 158, 16.0F, -32.0F, 19.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 48, 96, 11.0F, -31.0F, 11.0F, 6, 1, 6, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 48, 87, -18.0F, -32.0F, 10.0F, 8, 1, 8, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 10, 51, -20.0F, -32.0F, 9.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 70, 152, -20.0F, -32.0F, 8.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 51, -9.0F, -32.0F, 16.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 119, 149, -12.0F, -32.0F, 19.0F, 4, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 37, 37, -9.0F, -32.0F, 9.0F, 1, 2, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 87, 21.0F, -11.0F, 13.0F, 2, 6, 6, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 21, 6.125F, -22.0F, 2.0F, 14, 17, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -20.0F, -22.0F, 2.0F, 14, 17, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -23.0F, -5.0F, -23.0F, 46, 5, 46, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 88, 131, 23.0F, -12.0F, 12.0F, 1, 8, 8, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 51, -24.0F, -12.0F, 12.0F, 1, 8, 8, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 64, 87, 6.0F, -30.0F, 6.0F, 16, 25, 16, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 87, -22.0F, -30.0F, 6.0F, 16, 25, 16, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 51, -21.0F, -23.0F, -15.0F, 42, 18, 18, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 112, 87, -15.0F, -23.5F, -10.0F, 30, 1, 8, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 128, -13.0F, -31.0F, -2.0F, 26, 7, 7, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 10, 30, 3.0F, -29.0F, -4.0F, 5, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 12, 28, -2.0F, -30.0F, 7.0F, 4, 6, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 14, 30, -2.0F, -30.0F, 4.0F, 4, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 14, 30, 12.0F, -29.0F, 0.0F, 3, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 8, 25, 15.0F, -29.0F, 0.0F, 3, 3, 7, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 14, 25, -15.0F, -29.0F, 0.0F, 3, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 7, 23, -18.0F, -29.0F, 0.0F, 3, 3, 7, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 10, 27, -8.0F, -29.0F, -8.0F, 5, 6, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 10, 26, 3.0F, -29.0F, -8.0F, 5, 6, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 9, 28, -8.0F, -29.0F, -4.0F, 5, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 15, 25, 7.5F, -24.0F, -13.5F, 3, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 13, 32, 1.5F, -24.0F, -13.5F, 3, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 13, 32, -4.5F, -24.0F, -13.5F, 3, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 14, 30, -10.5F, -24.0F, -13.5F, 3, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 43, 160, -17.0F, -23.25F, -5.0F, 2, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 110, 159, -17.0F, -23.25F, -10.0F, 2, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 33, 158, 15.0F, -23.25F, -5.0F, 2, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 78, 139, 15.0F, -23.25F, -10.0F, 2, 3, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 139, 149, -3.0F, -11.0F, 20.0F, 6, 6, 3, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 138, 22, -19.0F, -11.0F, -22.0F, 6, 6, 7, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 138, 22, 13.0F, -11.0F, -22.0F, 6, 6, 7, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 120, 51, -5.0F, -26.0F, 6.0F, 10, 21, 15, 0.0F, false));
    }

    public void render(double tick, float size, boolean on, TextureManager manager, double gasTank, double leftTank, double rightTank) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        doRender(size); //渲染本体
        manager.bindTexture(on ? MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ChemicalInfuser/LED/LED_" + getTick(tick) + ".png") : OVERLAY_OFF);
        GlStateManager.scale(1.001F, 1.001F, 1.001F);
        GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
        MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
        doRender(size); //渲染灯光
        if (on) {
            GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
            manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ChemicalInfuser/Screen/Screen_" + getTick(tick) + ".png"));
            doRender(size);  //渲染屏幕
            GlStateManager.translate(0F, 0F, -0.0002F);
            if (gasTank > 0) {
                manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ChemicalInfuser/Gas/Gas_" + getNumberTanks(gasTank) + ".png"));
                doRender(size);//渲染屏幕上的流体数量
            }
            if (leftTank > 0) {
                manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ChemicalInfuser/left/left_" + getNumberTanks(leftTank) + ".png"));
                doRender(size);//渲染屏幕上的流体数量
            }
            if (rightTank > 0) {
                manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ChemicalInfuser/right/right_" + getNumberTanks(rightTank) + ".png"));
                doRender(size);//渲染屏幕上的流体数量
            }
        }
        MekanismRenderer.disableGlow(glowInfo);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }

    public void renderBloom(double tick, float size, boolean on, TextureManager manager, double gasTank, double leftTank, double rightTank) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        manager.bindTexture(on ? MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ChemicalInfuser/LED/LED_" + getTick(tick) + ".png") : OVERLAY_OFF);
        GlStateManager.scale(1.001F, 1.001F, 1.001F);
        GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
        MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
        doRender(size); //渲染灯光
        if (on) {
            GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
            manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ChemicalInfuser/Screen/Screen_" + getTick(tick) + ".png"));
            doRender(size);  //渲染屏幕
            GlStateManager.translate(0F, 0F, -0.0002F);
            if (gasTank > 0) {
                manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ChemicalInfuser/Gas/Gas_" + getNumberTanks(gasTank) + ".png"));
                doRender(size);//渲染屏幕上的流体数量
            }
            if (leftTank > 0) {
                manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ChemicalInfuser/left/left_" + getNumberTanks(leftTank) + ".png"));
                doRender(size);//渲染屏幕上的流体数量
            }
            if (rightTank > 0) {
                manager.bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "ChemicalInfuser/right/right_" + getNumberTanks(rightTank) + ".png"));
                doRender(size);//渲染屏幕上的流体数量
            }
        }
        MekanismRenderer.disableGlow(glowInfo);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }

    public void doRender(float f5) {
        Console.render(f5);
        bb_main.render(f5);
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
        }else if (number>=1F){
            return 9;
        }
        return 0;
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