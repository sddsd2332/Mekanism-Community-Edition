package mekanism.client.render;

import mekanism.api.Coord4D;
import mekanism.api.MekanismAPI;
import mekanism.api.Pos3D;
import mekanism.client.ClientTickHandler;
import mekanism.client.render.particle.EntityJetpackFlameFX;
import mekanism.client.render.particle.EntityJetpackSmokeFX;
import mekanism.client.render.particle.EntityScubaBubbleFX;
import mekanism.common.Mekanism;
import mekanism.common.item.ItemFlamethrower;
import mekanism.common.item.interfaces.IModeItem;
import mekanism.common.lib.Color;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class RenderTickHandler {

    private static final EntityEquipmentSlot[] EQUIPMENT_ORDER = {EntityEquipmentSlot.OFFHAND, EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS,
            EntityEquipmentSlot.FEET};
    public static int modeSwitchTimer = 0;
    public Random rand = new Random();
    public Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void tickEnd(RenderTickEvent event) {
        if (event.phase == Phase.END) {
            if (mc.player != null && mc.world != null && !mc.isGamePaused() && mc.playerController != null) {
                FontRenderer font = mc.fontRenderer;
                if (font == null) {
                    return;
                }

                EntityPlayer player = mc.player;
                World world = mc.player.world;
                renderStatusBar(player);
                RayTraceResult pos = player.rayTrace(40.0D, 1.0F);
                if (pos != null) {
                    Coord4D obj = new Coord4D(pos.getBlockPos(), world);
                    Block block = obj.getBlock(world);

                    if (block != null && MekanismAPI.debug && mc.currentScreen == null
                            && !mc.gameSettings.showDebugInfo) {
                        String tileDisplay = "";

                        if (obj.getTileEntity(world) != null) {
                            if (obj.getTileEntity(world).getClass() != null) {
                                tileDisplay = obj.getTileEntity(world).getClass().getSimpleName();
                            }
                        }

                        font.drawStringWithShadow("Block: " + block.getTranslationKey(), 1, 1, 0x404040);
                        font.drawStringWithShadow("Metadata: " + obj.getBlockState(world), 1, 10, 0x404040);
                        font.drawStringWithShadow("Location: " + MekanismUtils.getCoordDisplay(obj), 1, 19, 0x404040);
                        font.drawStringWithShadow("TileEntity: " + tileDisplay, 1, 28, 0x404040);
                        font.drawStringWithShadow("Side: " + pos.sideHit, 1, 37, 0x404040);
                    }
                }

                /*
                //todo use vanilla status bar text?
                if (modeSwitchTimer > 1 && mc.currentScreen == null && player.getHeldItemMainhand().getItem() instanceof ItemConfigurator) {
                    ItemStack stack = player.getHeldItemMainhand();
                    ScaledResolution scaledresolution = new ScaledResolution(mc);
                    ConfiguratorMode mode = ((ItemConfigurator) stack.getItem()).getMode(stack);

                    int x = scaledresolution.getScaledWidth();
                    int y = scaledresolution.getScaledHeight();
                    int stringWidth = font.getStringWidth(mode.getName());
                    int color = new ColourRGBA(1, 1, 1, (float) modeSwitchTimer / 100F).argb();
                    font.drawString(mode.getColor() + mode.getName(), x / 2 - stringWidth / 2, y - 60, color);
                }
                modeSwitchTimer = Math.max(modeSwitchTimer - 1, 0);
                 */

                if (modeSwitchTimer == 0) {
                    ClientTickHandler.wheelStatus = 0;
                }

                // Traverse a copy of jetpack state and do animations
                for (UUID uuid : Mekanism.playerState.getActiveJetpacks()) {
                    EntityPlayer p = mc.world.getPlayerEntityByUUID(uuid);
                    if (p != null) {
                        Pos3D playerPos = new Pos3D(p).translate(0, p.getEyeHeight(), 0);
                        Vec3d playerMotion = new Vec3d(player.motionX, player.motionY, player.motionZ);
                        float random = (rand.nextFloat() - 0.5F) * 0.1F;
                        //This positioning code is somewhat cursed, but it seems to be mostly working and entity pose code seems cursed in general
                        float xRot;
                        if (p.isSneaking()) {
                            xRot = 20;
                            playerPos = playerPos.translate(0, 0.125, 0);
                        } else {
                            float f = p.getSwingProgress(event.renderTickTime);
                            if (p.isElytraFlying()) {
                                float f1 = (float) p.getTicksElytraFlying() + event.renderTickTime;
                                float f2 = clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
                                xRot = f2 * (-90.0F - p.rotationYaw);
                            } else {
                                float f3 = p.isInWater() ? -90.0F - p.rotationYaw : -90.0F;
                                xRot = lerp(f, 0.0F, f3);
                            }
                            xRot = -xRot;
                            Pos3D eyeAdjustments;
                            if (p.isElytraFlying() && (p != player || mc.gameSettings.thirdPersonView != 0)) {
                                eyeAdjustments = new Pos3D(0, p.getEyeHeight(), 0).rotatePitch(xRot).rotateYaw(p.renderYawOffset);
                            }  /*else if (p.getSwingProgress()) {
                                eyeAdjustments = new Pos3D(0, p.getEyeHeight(), 0).rotatePitch(xRot).rotateYaw(p.renderYawOffset).translate(0, 0.5, 0);
                            } */ else {
                                eyeAdjustments = new Pos3D(0, p.getEyeHeight(), 0).rotatePitch(xRot).rotateYaw(p.renderYawOffset);
                            }
                            playerPos = new Pos3D(p.posX + eyeAdjustments.x, p.posY + eyeAdjustments.y, p.posZ + eyeAdjustments.z);
                        }
                        Pos3D vLeft = new Pos3D(-0.43, -0.55, -0.54).rotatePitch(xRot).rotateYaw(p.renderYawOffset);
                        renderJetpackSmoke(world, playerPos.translate(vLeft).translate(playerMotion), vLeft.scale(0.2).translate(playerMotion).translate(vLeft.scale(random)));
                        Pos3D vRight = new Pos3D(0.43, -0.55, -0.54).rotatePitch(xRot).rotateYaw(p.renderYawOffset);
                        renderJetpackSmoke(world, playerPos.translate(vRight).translate(playerMotion), vRight.scale(0.2).translate(playerMotion).translate(vRight.scale(random)));
                        Pos3D vCenter = new Pos3D((rand.nextFloat() - 0.5) * 0.4, -0.86, -0.30).rotatePitch(xRot).rotateYaw(p.renderYawOffset);
                        renderJetpackSmoke(world, playerPos.translate(vCenter).translate(playerMotion), vCenter.scale(0.2).translate(playerMotion));
                    }
                }

                // Traverse a copy of gasmask state and do animations
                if (world.getWorldTime() % 4 == 0) {
                    for (UUID uuid : Mekanism.playerState.getActiveScubaMask()) {
                        EntityPlayer p = mc.world.getPlayerEntityByUUID(uuid);
                        if (p == null || !p.isInWater()) {
                            continue;
                        }

                        Pos3D playerPos = new Pos3D(p).translate(0, 1.7, 0);

                        float xRand = (rand.nextFloat() - 0.5F) * 0.08F;
                        float yRand = (rand.nextFloat() - 0.5F) * 0.05F;

                        Pos3D vec = new Pos3D(0.4, 0.4, 0.4).multiply(new Pos3D(p.getLook(1))).translate(0, -0.2, 0);
                        Pos3D motion = vec.scale(0.2).translate(new Pos3D(p.motionX, p.motionY, p.motionZ));

                        Pos3D v = playerPos.translate(vec);
                        spawnAndSetParticle(EnumParticleTypes.WATER_BUBBLE, world, v.x, v.y, v.z, motion.x, motion.y + 0.2, motion.z);
                    }
                }

                // Traverse a copy of flamethrower state and do animations
                if (world.getWorldTime() % 4 == 0) {
                    for (EntityPlayer p : world.playerEntities) {
                        if (!Mekanism.playerState.isFlamethrowerOn(p) && !p.isSwingInProgress) {
                            ItemStack currentItem = p.inventory.getCurrentItem();
                            if (!currentItem.isEmpty() && currentItem.getItem() instanceof ItemFlamethrower && ((ItemFlamethrower) currentItem.getItem()).getGas(currentItem) != null) {
                                Pos3D playerPos = new Pos3D(p);
                                Pos3D flameVec;
                                double flameXCoord = 0;
                                double flameYCoord = 1.5;
                                double flameZCoord = 0;
                                Pos3D flameMotion = new Pos3D(p.motionX, p.onGround ? 0 : p.motionY, p.motionZ);
                                if (player == p && mc.gameSettings.thirdPersonView == 0) {
                                    flameVec = new Pos3D(1, 1, 1).multiply(p.getLook(1)).rotateYaw(5).translate(flameXCoord, flameYCoord + 0.1, flameZCoord);
                                } else {
                                    flameXCoord += 0.25F;
                                    flameXCoord -= 0.45F;
                                    flameZCoord += 0.15F;
                                    if (p.isSneaking()) {
                                        flameYCoord -= 0.55F;
                                        flameZCoord -= 0.15F;
                                    }
                                    if (player == p) {
                                        flameYCoord -= 0.5F;
                                    } else {
                                        flameYCoord -= 0.5F;
                                    }
                                    flameZCoord += 1.05F;
                                    flameVec = new Pos3D(flameXCoord, flameYCoord, flameZCoord).rotateYaw(p.renderYawOffset);
                                }
                                Pos3D mergedVec = playerPos.translate(flameVec);
                                spawnAndSetParticle(EnumParticleTypes.FLAME, world, mergedVec.x, mergedVec.y, mergedVec.z, flameMotion.x, flameMotion.y, flameMotion.z);
                            }
                        }
                    }
                }
            }
        }
    }

    private void renderStatusBar(@Nonnull EntityPlayer player) {
        //TODO: use vanilla status bar text? Note, the vanilla status bar text stays a lot longer than we have our message
        // display for, so we would need to somehow modify it. This can be done via ATs but does cause it to always appear
        // to be more faded in color, and blinks to full color just before disappearing
        if (modeSwitchTimer > 1) {
            if (mc.currentScreen == null && mc.fontRenderer != null) {
                ItemStack stack = player.getHeldItemMainhand();
                if (IModeItem.isModeItem(stack, EntityEquipmentSlot.MAINHAND)) {
                    ITextComponent scrollTextComponent = ((IModeItem) stack.getItem()).getScrollTextComponent(stack);
                    if (scrollTextComponent != null) {
                        ScaledResolution scaledResolution = new ScaledResolution(mc);
                        int x = scaledResolution.getScaledWidth();
                        int y = scaledResolution.getScaledHeight();
                        int color = Color.rgbad(1, 1, 1, modeSwitchTimer / 100F).argb();
                        mc.fontRenderer.drawString(scrollTextComponent.getFormattedText(), (x - mc.fontRenderer.getStringWidth(scrollTextComponent.getFormattedText())) / 2, y - 60, color);
                    }
                }
            }
            modeSwitchTimer--;
        }
    }

    private void renderJetpackSmoke(World world, Vec3d pos, Vec3d motion) {
        spawnAndSetParticle(EnumParticleTypes.FLAME, world, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
        spawnAndSetParticle(EnumParticleTypes.SMOKE_NORMAL, world, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
    }


    public static float clamp(float a, float min, float max) {
        return a < min ? min : (Math.min(a, max));
    }

    public static float lerp(float pDelta, float pStart, float pEnd) {
        return pStart + pDelta * (pEnd - pStart);
    }

    public void spawnAndSetParticle(EnumParticleTypes s, World world, double x, double y, double z, double velX, double velY, double velZ) {
        Particle fx = null;
        if (s.equals(EnumParticleTypes.FLAME)) {
            fx = new EntityJetpackFlameFX(world, x, y, z, velX, velY, velZ);
        } else if (s.equals(EnumParticleTypes.SMOKE_NORMAL)) {
            fx = new EntityJetpackSmokeFX(world, x, y, z, velX, velY, velZ);
        } else if (s.equals(EnumParticleTypes.WATER_BUBBLE)) {
            fx = new EntityScubaBubbleFX(world, x, y, z, velX, velY, velZ);
        }
        mc.effectRenderer.addEffect(fx);
    }

    private void drawString(ScaledResolution res, String s, boolean leftSide, int y, int color) {
        FontRenderer font = mc.fontRenderer;
        // Note that we always offset by 2 pixels when left or right aligned
        if (leftSide) {
            font.drawStringWithShadow(s, 2, y, color);
        } else {
            int width = font.getStringWidth(s) + 2;
            font.drawStringWithShadow(s, res.getScaledWidth() - width, y, color);
        }
    }


}
