package mekanism.client.model;

import mekanism.client.HolidayManager;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.MekanismRenderer.GlowInfo;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelDigitalMiner extends ModelBase {

    public static ResourceLocation OVERLAY_ON = MekanismUtils.getResource(ResourceType.RENDER, "DigitalMiner_OverlayOn.png");
    public static ResourceLocation OVERLAY_OFF = MekanismUtils.getResource(ResourceType.RENDER, "DigitalMiner_OverlayOff.png");

    ModelRenderer keyboard;
    ModelRenderer keyboardBottom;
    ModelRenderer keyboardSupportExt1;
    ModelRenderer keyboardSupportExt2;
    ModelRenderer keyboardSupport1;
    ModelRenderer keyboardSupport2;
    ModelRenderer monitor1back;
    ModelRenderer monitor2back;
    ModelRenderer monitor3back;
    ModelRenderer monitorBar1;
    ModelRenderer monitorBar2;
    ModelRenderer led1;
    ModelRenderer led2;
    ModelRenderer led3;
    ModelRenderer monitorMount1;
    ModelRenderer monitorMount2;
    ModelRenderer frame1;
    ModelRenderer frame3;
    ModelRenderer plate5;
    ModelRenderer bracket1;
    ModelRenderer bracket2;
    ModelRenderer bracket3;
    ModelRenderer bracket4;
    ModelRenderer bracket5;
    ModelRenderer bracket6;
    ModelRenderer bracket7;
    ModelRenderer bracket8;
    ModelRenderer bracketPlate1;
    ModelRenderer bracketPlate2;
    ModelRenderer bracketPlate3;
    ModelRenderer bracketPlate4;
    ModelRenderer supportBeam1;
    ModelRenderer supportBeam2;
    ModelRenderer supportBeam3;
    ModelRenderer supportBeam4;
    ModelRenderer foot1;
    ModelRenderer foot2;
    ModelRenderer foot3;
    ModelRenderer foot4;
    ModelRenderer core;
    ModelRenderer powerCable1a;
    ModelRenderer powerCable1b;
    ModelRenderer powerCable2;
    ModelRenderer powerCable3;
    ModelRenderer powerConnector1;
    ModelRenderer powerConnector2a;
    ModelRenderer powerConnector2b;
    ModelRenderer powerCpnnector3a;
    ModelRenderer powerConnector3b;
    ModelRenderer frame2a;
    ModelRenderer frame2b;
    ModelRenderer frame2c;
    ModelRenderer frame2d;
    ModelRenderer monitor1;
    ModelRenderer monitor2;
    ModelRenderer monitor3;

    public ModelDigitalMiner() {
        textureWidth = 256;
        textureHeight = 128;

        keyboard = new ModelRenderer(this, 120, 20);
        keyboard.addBox(0F, -3F, -1F, 10, 5, 1);
        keyboard.setRotationPoint(-5F, 14F, -5F);
        keyboard.setTextureSize(256, 128);
        keyboard.mirror = true;
        setRotation(keyboard, -1.082104F, 0.0174533F, 0F);
        keyboardBottom = new ModelRenderer(this, 120, 26);
        keyboardBottom.addBox(0F, -2.5F, -0.5F, 8, 4, 1);
        keyboardBottom.setRotationPoint(-4F, 14F, -5F);
        keyboardBottom.setTextureSize(256, 128);
        keyboardBottom.mirror = true;
        setRotation(keyboardBottom, -0.9075712F, 0F, 0F);
        keyboardSupportExt1 = new ModelRenderer(this, 138, 26);
        keyboardSupportExt1.addBox(0F, 0F, -1F, 1, 1, 1);
        keyboardSupportExt1.setRotationPoint(2F, 14F, -5F);
        keyboardSupportExt1.setTextureSize(256, 128);
        keyboardSupportExt1.mirror = true;
        setRotation(keyboardSupportExt1, 0F, 0F, 0F);
        keyboardSupportExt2 = new ModelRenderer(this, 138, 26);
        keyboardSupportExt2.addBox(0F, 0F, -1F, 1, 1, 1);
        keyboardSupportExt2.setRotationPoint(-3F, 14F, -5F);
        keyboardSupportExt2.setTextureSize(256, 128);
        keyboardSupportExt2.mirror = true;
        setRotation(keyboardSupportExt2, 0F, 0F, 0F);
        keyboardSupport1 = new ModelRenderer(this, 142, 20);
        keyboardSupport1.addBox(0F, -1F, 0F, 1, 2, 4);
        keyboardSupport1.setRotationPoint(-3F, 14F, -5F);
        keyboardSupport1.setTextureSize(256, 128);
        keyboardSupport1.mirror = true;
        setRotation(keyboardSupport1, 0F, 0F, 0F);
        keyboardSupport2 = new ModelRenderer(this, 142, 20);
        keyboardSupport2.addBox(0F, -1F, 0F, 1, 2, 4);
        keyboardSupport2.setRotationPoint(2F, 14F, -5F);
        keyboardSupport2.setTextureSize(256, 128);
        keyboardSupport2.mirror = true;
        setRotation(keyboardSupport2, 0F, 0F, 0F);
        monitor1back = new ModelRenderer(this, 88, 32);
        monitor1back.addBox(-13F, -3F, 0F, 12, 6, 1);
        monitor1back.setRotationPoint(-8F, 3F, -3F);
        monitor1back.setTextureSize(256, 128);
        monitor1back.mirror = true;
        setRotation(monitor1back, 0.0872665F, -0.2094395F, 0F);
        monitor2back = new ModelRenderer(this, 88, 32);
        monitor2back.addBox(0F, -4F, 0F, 12, 6, 1);
        monitor2back.setRotationPoint(-6F, 4F, -3F);
        monitor2back.setTextureSize(256, 128);
        monitor2back.mirror = true;
        setRotation(monitor2back, 0.0872665F, 0F, 0F);
        monitor3back = new ModelRenderer(this, 88, 32);
        monitor3back.addBox(1F, -3F, 0F, 12, 6, 1);
        monitor3back.setRotationPoint(8F, 3F, -3F);
        monitor3back.setTextureSize(256, 128);
        monitor3back.mirror = true;
        setRotation(monitor3back, 0.0872665F, 0.2094395F, 0F);
        monitorBar1 = new ModelRenderer(this, 114, 36);
        monitorBar1.addBox(-3.5F, -2F, -0.2F, 4, 2, 1);
        monitorBar1.setRotationPoint(-6F, 4F, -3F);
        monitorBar1.setTextureSize(256, 128);
        monitorBar1.mirror = true;
        setRotation(monitorBar1, 0.0872665F, -0.0523599F, 0F);
        monitorBar2 = new ModelRenderer(this, 114, 36);
        monitorBar2.addBox(0.5F, -2F, -0.2F, 4, 2, 1);
        monitorBar2.setRotationPoint(5F, 4F, -3F);
        monitorBar2.setTextureSize(256, 128);
        monitorBar2.mirror = true;
        setRotation(monitorBar2, 0.0872665F, 0.0523599F, 0F);
        led1 = new ModelRenderer(this, 0, 0);
        led1.addBox(-2F, 4.5F, -1.9F, 1, 1, 1);
        led1.setRotationPoint(-8F, 3F, -3F);
        led1.setTextureSize(256, 128);
        led1.mirror = true;
        setRotation(led1, 0.0872665F, -0.2094395F, 0F);
        led2 = new ModelRenderer(this, 0, 0);
        led2.addBox(12F, 4.466667F, -1.9F, 1, 1, 1);
        led2.setRotationPoint(-7F, 3F, -3F);
        led2.setTextureSize(256, 128);
        led2.mirror = true;
        setRotation(led2, 0.0872665F, 0F, 0F);
        led3 = new ModelRenderer(this, 0, 0);
        led3.addBox(12F, 4.5F, -1.9F, 1, 1, 1);
        led3.setRotationPoint(8F, 3F, -3F);
        led3.setTextureSize(256, 128);
        led3.mirror = true;
        setRotation(led3, 0.0872665F, 0.2094395F, 0F);
        monitorMount1 = new ModelRenderer(this, 114, 32);
        monitorMount1.addBox(0F, -1F, 0F, 2, 2, 2);
        monitorMount1.setRotationPoint(-4F, 3F, -3F);
        monitorMount1.setTextureSize(256, 128);
        monitorMount1.mirror = true;
        setRotation(monitorMount1, 0F, 0F, 0F);
        monitorMount2 = new ModelRenderer(this, 114, 32);
        monitorMount2.addBox(0F, -1F, 0F, 2, 2, 2);
        monitorMount2.setRotationPoint(2F, 3F, -3F);
        monitorMount2.setTextureSize(256, 128);
        monitorMount2.mirror = true;
        setRotation(monitorMount2, 0F, 0F, 0F);
        frame1 = new ModelRenderer(this, 0, 0);
        frame1.addBox(0F, 0F, 0F, 32, 29, 12);
        frame1.setRotationPoint(-16F, -8F, -1F);
        frame1.setTextureSize(256, 128);
        frame1.mirror = true;
        setRotation(frame1, 0F, 0F, 0F);
        frame3 = new ModelRenderer(this, 0, 0);
        frame3.addBox(0F, 0F, 0F, 32, 29, 12);
        frame3.setRotationPoint(-16F, -8F, 28F);
        frame3.setTextureSize(256, 128);
        frame3.mirror = true;
        setRotation(frame3, 0F, 0F, 0F);
        plate5 = new ModelRenderer(this, 88, 90);
        plate5.addBox(0F, 0F, 0F, 32, 5, 15);
        plate5.setRotationPoint(-16F, 16F, 12F);
        plate5.setTextureSize(256, 128);
        plate5.mirror = true;
        setRotation(plate5, 0F, 0F, 0F);
        bracket1 = new ModelRenderer(this, 16, 85);
        bracket1.addBox(0F, 0F, 0F, 5, 5, 2);
        bracket1.setRotationPoint(-21F, -5F, 0F);
        bracket1.setTextureSize(256, 128);
        bracket1.mirror = true;
        setRotation(bracket1, 0F, 0F, 0F);
        bracket2 = new ModelRenderer(this, 16, 85);
        bracket2.addBox(0F, 0F, 0F, 5, 5, 2);
        bracket2.setRotationPoint(-21F, -5F, 8F);
        bracket2.setTextureSize(256, 128);
        bracket2.mirror = true;
        setRotation(bracket2, 0F, 0F, 0F);
        bracket3 = new ModelRenderer(this, 16, 85);
        bracket3.addBox(0F, 0F, 0F, 5, 5, 2);
        bracket3.setRotationPoint(-21F, -5F, 29F);
        bracket3.setTextureSize(256, 128);
        bracket3.mirror = true;
        setRotation(bracket3, 0F, 0F, 0F);
        bracket4 = new ModelRenderer(this, 16, 85);
        bracket4.addBox(0F, 0F, 0F, 5, 5, 2);
        bracket4.setRotationPoint(-21F, -5F, 37F);
        bracket4.setTextureSize(256, 128);
        bracket4.mirror = true;
        setRotation(bracket4, 0F, 0F, 0F);
        bracket5 = new ModelRenderer(this, 16, 85);
        bracket5.addBox(0F, 0F, 0F, 5, 5, 2);
        bracket5.setRotationPoint(16F, -5F, 0F);
        bracket5.setTextureSize(256, 128);
        bracket5.mirror = true;
        setRotation(bracket5, 0F, 0F, 0F);
        bracket5.mirror = false;
        bracket6 = new ModelRenderer(this, 16, 85);
        bracket6.addBox(0F, 0F, 0F, 5, 5, 2);
        bracket6.setRotationPoint(16F, -5F, 8F);
        bracket6.setTextureSize(256, 128);
        bracket6.mirror = true;
        setRotation(bracket6, 0F, 0F, 0F);
        bracket7 = new ModelRenderer(this, 16, 85);
        bracket7.addBox(0F, 0F, 0F, 5, 5, 2);
        bracket7.setRotationPoint(16F, -5F, 29F);
        bracket7.setTextureSize(256, 128);
        bracket7.mirror = true;
        setRotation(bracket7, 0F, 0F, 0F);
        bracket8 = new ModelRenderer(this, 16, 85);
        bracket8.addBox(0F, 0F, 0F, 5, 5, 2);
        bracket8.setRotationPoint(16F, -5F, 37F);
        bracket8.setTextureSize(256, 128);
        bracket8.mirror = true;
        setRotation(bracket8, 0F, 0F, 0F);
        bracket8.mirror = false;
        bracketPlate1 = new ModelRenderer(this, 30, 85);
        bracketPlate1.addBox(0F, 0F, 0F, 1, 5, 6);
        bracketPlate1.setRotationPoint(-17F, -5F, 2F);
        bracketPlate1.setTextureSize(256, 128);
        bracketPlate1.mirror = true;
        setRotation(bracketPlate1, 0F, 0F, 0F);
        bracketPlate2 = new ModelRenderer(this, 30, 85);
        bracketPlate2.addBox(0F, 0F, 0F, 1, 5, 6);
        bracketPlate2.setRotationPoint(-17F, -5F, 31F);
        bracketPlate2.setTextureSize(256, 128);
        bracketPlate2.mirror = true;
        setRotation(bracketPlate2, 0F, 0F, 0F);
        bracketPlate3 = new ModelRenderer(this, 30, 85);
        bracketPlate3.addBox(0F, 0F, 0F, 1, 5, 6);
        bracketPlate3.setRotationPoint(16F, -5F, 2F);
        bracketPlate3.setTextureSize(256, 128);
        bracketPlate3.mirror = true;
        setRotation(bracketPlate3, 0F, 0F, 0F);
        bracketPlate4 = new ModelRenderer(this, 30, 85);
        bracketPlate4.addBox(0F, 0F, 0F, 1, 5, 6);
        bracketPlate4.setRotationPoint(16F, -5F, 31F);
        bracketPlate4.setTextureSize(256, 128);
        bracketPlate4.mirror = true;
        setRotation(bracketPlate4, 0F, 0F, 0F);
        supportBeam1 = new ModelRenderer(this, 0, 85);
        supportBeam1.addBox(0F, 0F, 0F, 4, 28, 8);
        supportBeam1.setRotationPoint(-22F, -6F, 1F);
        supportBeam1.setTextureSize(256, 128);
        supportBeam1.mirror = true;
        setRotation(supportBeam1, 0F, 0F, 0F);
        supportBeam2 = new ModelRenderer(this, 0, 85);
        supportBeam2.addBox(0F, 0F, 0F, 4, 28, 8);
        supportBeam2.setRotationPoint(-22F, -6F, 30F);
        supportBeam2.setTextureSize(256, 128);
        supportBeam2.mirror = true;
        setRotation(supportBeam2, 0F, 0F, 0F);
        supportBeam3 = new ModelRenderer(this, 0, 85);
        supportBeam3.addBox(0F, 0F, 0F, 4, 28, 8);
        supportBeam3.setRotationPoint(18F, -6F, 1F);
        supportBeam3.setTextureSize(256, 128);
        supportBeam3.mirror = true;
        setRotation(supportBeam3, 0F, 0F, 0F);
        supportBeam4 = new ModelRenderer(this, 0, 85);
        supportBeam4.addBox(0F, 0F, 0F, 4, 28, 8);
        supportBeam4.setRotationPoint(18F, -6F, 30F);
        supportBeam4.setTextureSize(256, 128);
        supportBeam4.mirror = true;
        setRotation(supportBeam4, 0F, 0F, 0F);
        supportBeam4.mirror = false;
        foot1 = new ModelRenderer(this, 44, 85);
        foot1.addBox(0F, 0F, 0F, 7, 2, 10);
        foot1.setRotationPoint(-23F, 22F, 0F);
        foot1.setTextureSize(256, 128);
        foot1.mirror = true;
        setRotation(foot1, 0F, 0F, 0F);
        foot2 = new ModelRenderer(this, 44, 85);
        foot2.addBox(0F, 0F, 0F, 7, 2, 10);
        foot2.setRotationPoint(-23F, 22F, 29F);
        foot2.setTextureSize(256, 128);
        foot2.mirror = true;
        setRotation(foot2, 0F, 0F, 0F);
        foot3 = new ModelRenderer(this, 44, 85);
        foot3.addBox(0F, 0F, 0F, 7, 2, 10);
        foot3.setRotationPoint(16F, 22F, 29F);
        foot3.setTextureSize(256, 128);
        foot3.mirror = true;
        setRotation(foot3, 0F, 0F, 0F);
        foot4 = new ModelRenderer(this, 44, 85);
        foot4.addBox(0F, 0F, 0F, 7, 2, 10);
        foot4.setRotationPoint(16F, 22F, 0F);
        foot4.setTextureSize(256, 128);
        foot4.mirror = true;
        setRotation(foot4, 0F, 0F, 0F);
        core = new ModelRenderer(this, 0, 41);
        core.addBox(0F, 0F, 0F, 30, 27, 17);
        core.setRotationPoint(-15F, -7F, 11F);
        core.setTextureSize(256, 128);
        core.mirror = true;
        setRotation(core, 0F, 0F, 0F);
        powerCable1a = new ModelRenderer(this, 88, 39);
        powerCable1a.addBox(0F, 0F, 0F, 6, 2, 11);
        powerCable1a.setRotationPoint(-3F, 20F, 2F);
        powerCable1a.setTextureSize(256, 128);
        powerCable1a.mirror = true;
        setRotation(powerCable1a, 0F, 0F, 0F);
        powerCable1b = new ModelRenderer(this, 94, 52);
        powerCable1b.addBox(0F, 0F, 0F, 6, 3, 6);
        powerCable1b.setRotationPoint(-3F, 20F, 13F);
        powerCable1b.setTextureSize(256, 128);
        powerCable1b.mirror = true;
        setRotation(powerCable1b, 0F, 0F, 0F);
        powerCable2 = new ModelRenderer(this, 42, 109);
        powerCable2.addBox(0F, 0F, 0F, 9, 6, 6);
        powerCable2.setRotationPoint(14F, 13F, 13F);
        powerCable2.setTextureSize(256, 128);
        powerCable2.mirror = true;
        setRotation(powerCable2, 0F, 0F, 0F);
        powerCable3 = new ModelRenderer(this, 42, 109);
        powerCable3.addBox(0F, 0F, 0F, 9, 6, 6);
        powerCable3.setRotationPoint(-23F, 13F, 13F);
        powerCable3.setTextureSize(256, 128);
        powerCable3.mirror = true;
        setRotation(powerCable3, 0F, 0F, 0F);
        powerConnector1 = new ModelRenderer(this, 94, 61);
        powerConnector1.addBox(0F, 0F, 0F, 8, 1, 8);
        powerConnector1.setRotationPoint(-4F, 23F, 12F);
        powerConnector1.setTextureSize(256, 128);
        powerConnector1.mirror = true;
        setRotation(powerConnector1, 0F, 0F, 0F);
        powerConnector2a = new ModelRenderer(this, 24, 105);
        powerConnector2a.addBox(0F, 0F, 0F, 1, 8, 8);
        powerConnector2a.setRotationPoint(23F, 12F, 12F);
        powerConnector2a.setTextureSize(256, 128);
        powerConnector2a.mirror = true;
        setRotation(powerConnector2a, 0F, 0F, 0F);
        powerConnector2b = new ModelRenderer(this, 24, 105);
        powerConnector2b.addBox(0F, 0F, 0F, 1, 8, 8);
        powerConnector2b.setRotationPoint(16F, 12F, 12F);
        powerConnector2b.setTextureSize(256, 128);
        powerConnector2b.mirror = true;
        setRotation(powerConnector2b, 0F, 0F, 0F);
        powerCpnnector3a = new ModelRenderer(this, 24, 105);
        powerCpnnector3a.addBox(0F, 0F, 0F, 1, 8, 8);
        powerCpnnector3a.setRotationPoint(-24F, 12F, 12F);
        powerCpnnector3a.setTextureSize(256, 128);
        powerCpnnector3a.mirror = true;
        setRotation(powerCpnnector3a, 0F, 0F, 0F);
        powerConnector3b = new ModelRenderer(this, 24, 105);
        powerConnector3b.addBox(0F, 0F, 0F, 1, 8, 8);
        powerConnector3b.setRotationPoint(-17F, 12F, 12F);
        powerConnector3b.setTextureSize(256, 128);
        powerConnector3b.mirror = true;
        setRotation(powerConnector3b, 0F, 0F, 0F);
        frame2a = new ModelRenderer(this, 88, 0);
        frame2a.addBox(0F, 0F, 0F, 32, 5, 15);
        frame2a.setRotationPoint(-16F, -8F, 12F);
        frame2a.setTextureSize(256, 128);
        frame2a.mirror = true;
        setRotation(frame2a, 0F, 0F, 0F);
        frame2b = new ModelRenderer(this, 126, 50);
        frame2b.addBox(0F, 0F, 0F, 32, 5, 15);
        frame2b.setRotationPoint(-16F, -2F, 12F);
        frame2b.setTextureSize(256, 128);
        frame2b.mirror = true;
        setRotation(frame2b, 0F, 0F, 0F);
        frame2c = new ModelRenderer(this, 126, 50);
        frame2c.addBox(0F, 0F, 0F, 32, 5, 15);
        frame2c.setRotationPoint(-16F, 4F, 12F);
        frame2c.setTextureSize(256, 128);
        frame2c.mirror = true;
        setRotation(frame2c, 0F, 0F, 0F);
        frame2d = new ModelRenderer(this, 88, 70);
        frame2d.addBox(0F, 0F, 0F, 32, 5, 15);
        frame2d.setRotationPoint(-16F, 10F, 12F);
        frame2d.setTextureSize(256, 128);
        frame2d.mirror = true;
        setRotation(frame2d, 0F, 0F, 0F);
        monitor1 = new ModelRenderer(this, 88, 20);
        monitor1.addBox(-14F, -5F, -2F, 14, 10, 2);
        monitor1.setRotationPoint(-8F, 3F, -3F);
        monitor1.setTextureSize(256, 128);
        monitor1.mirror = true;
        setRotation(monitor1, 0.0872665F, -0.2094395F, 0F);
        monitor2 = new ModelRenderer(this, 152, 20);
        monitor2.addBox(0F, -5F, -2F, 14, 10, 2);
        monitor2.setRotationPoint(-7F, 3F, -3F);
        monitor2.setTextureSize(256, 128);
        monitor2.mirror = true;
        setRotation(monitor2, 0.0872665F, 0F, 0F);
        monitor3 = new ModelRenderer(this, 152, 32);
        monitor3.addBox(0F, -5F, -2F, 14, 10, 2);
        monitor3.setRotationPoint(8F, 3F, -3F);
        monitor3.setTextureSize(256, 128);
        monitor3.mirror = true;
        setRotation(monitor3, 0.0872665F, 0.2094395F, 0F);
    }

    public ResourceLocation isON(double tick) {
        if (MekanismConfig.current().client.holidays.val()) {
            if (HolidayManager.MAY_4.isToday()) {
                return MekanismUtils.getResource(ResourceType.RENDER, "DigitalMiner_OverlayOn_MAY_4TH.png");
            } else if (HolidayManager.APRIL_FOOLS.isToday()) {
                return MekanismUtils.getResource(ResourceType.RENDER, "DigitalMiner_OverlayOn_APRIL_FOOLS.png");
            }
        }
        return MekanismUtils.getResource(ResourceType.RENDER, "DigitalMiner_OverlayOn_" + getTick(tick) + ".png");
    }

    public void render(double tick, float size, boolean on, TextureManager manager) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
        doRender(size);
        manager.bindTexture(on ? isON(tick) : OVERLAY_OFF);
        GlStateManager.scale(1.001F, 1.001F, 1.001F);
        GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
        GlowInfo glowInfo = MekanismRenderer.enableGlow();
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
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
        manager.bindTexture(on ? isON(tick) : OVERLAY_OFF);
        GlStateManager.scale(1.001F, 1.001F, 1.001F);
        GlStateManager.translate(-0.0011F, -0.0011F, -0.0011F);
        GlowInfo glowInfo = MekanismRenderer.enableGlow();
        doRender(size);
        MekanismRenderer.disableGlow(glowInfo);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }

    private void doRender(float size) {
        keyboard.render(size);
        keyboardBottom.render(size);
        keyboardSupportExt1.render(size);
        keyboardSupportExt2.render(size);
        keyboardSupport1.render(size);
        keyboardSupport2.render(size);
        monitor1back.render(size);
        monitor2back.render(size);
        monitor3back.render(size);
        monitorBar1.render(size);
        monitorBar2.render(size);
        led1.render(size);
        led2.render(size);
        led3.render(size);
        monitor1.render(size);
        monitor2.render(size);
        monitor3.render(size);
        monitorMount1.render(size);
        monitorMount2.render(size);
        frame1.render(size);
        frame3.render(size);
        plate5.render(size);
        bracket1.render(size);
        bracket2.render(size);
        bracket3.render(size);
        bracket4.render(size);
        bracket5.render(size);
        bracket6.render(size);
        bracket7.render(size);
        bracket8.render(size);
        bracketPlate1.render(size);
        bracketPlate2.render(size);
        bracketPlate3.render(size);
        bracketPlate4.render(size);
        supportBeam1.render(size);
        supportBeam2.render(size);
        supportBeam3.render(size);
        supportBeam4.render(size);
        foot1.render(size);
        foot2.render(size);
        foot3.render(size);
        foot4.render(size);
        core.render(size);
        powerCable1a.render(size);
        powerCable1b.render(size);
        powerCable2.render(size);
        powerCable3.render(size);
        powerConnector1.render(size);
        powerConnector2a.render(size);
        powerConnector2b.render(size);
        powerCpnnector3a.render(size);
        powerConnector3b.render(size);
        frame2a.render(size);
        frame2b.render(size);
        frame2c.render(size);
        frame2d.render(size);
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
