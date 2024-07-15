package mekanism.client.render;

import mekanism.api.Coord4D;
import mekanism.api.EnumColor;
import mekanism.api.MekanismAPI;
import mekanism.api.Pos3D;
import mekanism.client.ClientTickHandler;
import mekanism.client.render.particle.EntityJetpackFlameFX;
import mekanism.client.render.particle.EntityJetpackSmokeFX;
import mekanism.client.render.particle.EntityScubaBubbleFX;
import mekanism.common.ColourRGBA;
import mekanism.common.Mekanism;
import mekanism.common.config.MekanismConfig;
import mekanism.common.item.*;
import mekanism.common.item.ItemConfigurator.ConfiguratorMode;
import mekanism.common.item.interfaces.IJetpackItem;
import mekanism.common.util.LangUtils;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class RenderTickHandler {

    public static int modeSwitchTimer = 0;
    public Random rand = new Random();
    public Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void tickEnd(RenderTickEvent event) {
        if (event.phase == Phase.END) {
            if (mc.player != null && mc.world != null && !mc.isGamePaused()) {
                FontRenderer font = mc.fontRenderer;
                if (font == null) {
                    return;
                }

                EntityPlayer player = mc.player;
                World world = mc.player.world;
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

                //todo use vanilla status bar text?
                if (modeSwitchTimer > 1 && mc.currentScreen == null && player.getHeldItemMainhand().getItem() instanceof ItemConfigurator) {
                    ItemStack stack = player.getHeldItemMainhand();
                    ScaledResolution scaledresolution = new ScaledResolution(mc);
                    ConfiguratorMode mode = ((ItemConfigurator) stack.getItem()).getState(stack);

                    int x = scaledresolution.getScaledWidth();
                    int y = scaledresolution.getScaledHeight();
                    int stringWidth = font.getStringWidth(mode.getName());
                    int color = new ColourRGBA(1, 1, 1, (float) modeSwitchTimer / 100F).argb();
                    font.drawString(mode.getColor() + mode.getName(), x / 2 - stringWidth / 2, y - 60, color);
                }

                modeSwitchTimer = Math.max(modeSwitchTimer - 1, 0);

                if (modeSwitchTimer == 0) {
                    ClientTickHandler.wheelStatus = 0;
                }

                if (mc.currentScreen == null && !mc.gameSettings.hideGUI && !player.isSpectator() &&
                        (!player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() ||
                                !player.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty() ||
                                !player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty() ||
                                !player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty()) &&
                        MekanismConfig.current().client.enableHUD.val()
                ) {

                    ItemStack cheststack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                    ItemStack feetstack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                    ItemStack mainhandstack = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                    ItemStack offhandstack = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

                    ScaledResolution scaledresolution = new ScaledResolution(mc);

                    int y = scaledresolution.getScaledHeight();
                    boolean alignLeft = MekanismConfig.current().client.alignHUDLeft.val();

                    if (feetstack.getItem() instanceof ItemFreeRunners FreeRunners) {
                        drawString(scaledresolution, LangUtils.localize("tooltip.free_runners.mode") + " " + FreeRunners.getMode(feetstack).getName(), alignLeft, y - 20, 0xc8c8c8);
                        drawString(scaledresolution, LangUtils.localize("tooltip.free_runners.stored") + " " + MekanismUtils.getEnergyDisplay(FreeRunners.getEnergy(feetstack)), alignLeft, y - 11, 0xc8c8c8);
                        y -= 18;
                    }

                    if (cheststack.getItem() instanceof IJetpackItem jetpack && jetpack.canRendered(cheststack)) {
                        drawString(scaledresolution, LangUtils.localize("tooltip.jetpack.mode") + " " + jetpack.getMode(cheststack).getName(), alignLeft, y - 20, 0xc8c8c8);
                        drawString(scaledresolution, LangUtils.localize("tooltip.jetpack.stored") + " " + EnumColor.ORANGE + jetpack.getStored(cheststack), alignLeft, y - 11, 0xc8c8c8);
                        y -= 18;
                    } else if (cheststack.getItem() instanceof ItemScubaTank scubaTank) {
                        String state = scubaTank.getFlowing(cheststack) ? EnumColor.DARK_GREEN + LangUtils.localize("gui.on") : EnumColor.DARK_RED + LangUtils.localize("gui.off");
                        drawString(scaledresolution, LangUtils.localize("tooltip.scuba_tank.mode") + " " + state, alignLeft, y - 20, 0xc8c8c8);
                        drawString(scaledresolution, scubaTank.getStored(cheststack) == 0 ? (LangUtils.localize("tooltip.noGas") + ".") : (scubaTank.getGas(cheststack).getGas().getLocalizedName() + ": " + scubaTank.getStored(cheststack)), alignLeft, y - 11, 0xc8c8c8);
                        y -= 18;
                    }

                    if (mainhandstack.getItem() instanceof ItemElectricBow) {
                        ItemElectricBow ElectricBow = (ItemElectricBow) mainhandstack.getItem();
                        drawString(scaledresolution, EnumColor.PINK + LangUtils.localizeWithFormat("mekanism.tooltip.fireMode", LangUtils.transOnOff(ElectricBow.getFireState(mainhandstack))), alignLeft, y - 11, 0xc8c8c8);
                        y -= 9;
                    } else if (mainhandstack.getItem() instanceof ItemFlamethrower Flamethrower) {
                        drawString(scaledresolution, LangUtils.localize("tooltip.flamethrower.mode") + " " + Flamethrower.getMode(mainhandstack).getName(), alignLeft, y - 20, 0xc8c8c8);
                        drawString(scaledresolution, LangUtils.localize("tooltip.flamethrower.stored") + " " + EnumColor.ORANGE + Flamethrower.getStored(mainhandstack), alignLeft, y - 11, 0xc8c8c8);
                        y -= 18;
                    } else if (mainhandstack.getItem() instanceof ItemAtomicDisassembler AtomicDisassembler) {
                        drawString(scaledresolution, LangUtils.localize("tooltip.mode") + ": " + EnumColor.INDIGO + AtomicDisassembler.getMode(mainhandstack).getModeName(), alignLeft, y - 20, 0xc8c8c8);
                        drawString(scaledresolution, LangUtils.localize("tooltip.efficiency") + ": " + EnumColor.INDIGO + AtomicDisassembler.getMode(mainhandstack).getEfficiency(), alignLeft, y - 11, 0xc8c8c8);
                        y -= 18;
                    }  else if (mainhandstack.getItem() instanceof ItemMekTool MekTool) {
                        drawString(scaledresolution, LangUtils.localize("tooltip.mode") + ": " + EnumColor.INDIGO + MekTool.getMode(mainhandstack).getModeName(), alignLeft, y - 20, 0xc8c8c8);
                        drawString(scaledresolution, LangUtils.localize("tooltip.efficiency") + ": " + EnumColor.INDIGO + MekTool.getMode(mainhandstack).getEfficiency(), alignLeft, y - 11, 0xc8c8c8);
                        y -= 18;
                    } else if (mainhandstack.getItem() instanceof ItemConfigurator Configurator) {
                        drawString(scaledresolution, EnumColor.PINK + LangUtils.localize("tooltip.mode") + ": " + Configurator.getColor(Configurator.getState(mainhandstack)) + Configurator.getStateDisplay(Configurator.getState(mainhandstack)), alignLeft, y - 11, 0xc8c8c8);
                        y -= 9;
                    }

                    if (offhandstack.getItem() instanceof ItemElectricBow) {
                        ItemElectricBow ElectricBow = (ItemElectricBow) offhandstack.getItem();
                        drawString(scaledresolution, EnumColor.PINK + LangUtils.localizeWithFormat("mekanism.tooltip.fireMode", LangUtils.transOnOff(ElectricBow.getFireState(offhandstack))), alignLeft, y - 11, 0xc8c8c8);
                    } else if (offhandstack.getItem() instanceof ItemFlamethrower Flamethrower) {
                        drawString(scaledresolution, LangUtils.localize("tooltip.flamethrower.mode") + " " + Flamethrower.getMode(offhandstack).getName(), alignLeft, y - 20, 0xc8c8c8);
                        drawString(scaledresolution, LangUtils.localize("tooltip.flamethrower.stored") + " " + EnumColor.ORANGE + Flamethrower.getStored(offhandstack), alignLeft, y - 11, 0xc8c8c8);
                    } else if (offhandstack.getItem() instanceof ItemAtomicDisassembler AtomicDisassembler) {
                        drawString(scaledresolution, LangUtils.localize("tooltip.mode") + ": " + EnumColor.INDIGO + AtomicDisassembler.getMode(offhandstack).getModeName(), alignLeft, y - 20, 0xc8c8c8);
                        drawString(scaledresolution, LangUtils.localize("tooltip.efficiency") + ": " + EnumColor.INDIGO + AtomicDisassembler.getMode(offhandstack).getEfficiency(), alignLeft, y - 11, 0xc8c8c8);
                    } else if (offhandstack.getItem() instanceof ItemMekTool MekTool) {
                        drawString(scaledresolution, LangUtils.localize("tooltip.mode") + ": " + EnumColor.INDIGO + MekTool.getMode(offhandstack).getModeName(), alignLeft, y - 20, 0xc8c8c8);
                        drawString(scaledresolution, LangUtils.localize("tooltip.efficiency") + ": " + EnumColor.INDIGO + MekTool.getMode(offhandstack).getEfficiency(), alignLeft, y - 11, 0xc8c8c8);
                    } else if (offhandstack.getItem() instanceof ItemConfigurator Configurator) {
                        drawString(scaledresolution, EnumColor.PINK + LangUtils.localize("tooltip.mode") + ": " + Configurator.getColor(Configurator.getState(offhandstack)) + Configurator.getStateDisplay(Configurator.getState(offhandstack)), alignLeft, y - 11, 0xc8c8c8);
                    }
                }
                // Traverse a copy of jetpack state and do animations
                for (UUID uuid : Mekanism.playerState.getActiveJetpacks()) {
                    EntityPlayer p = mc.world.getPlayerEntityByUUID(uuid);

                    if (p == null) {
                        continue;
                    }

                    Pos3D playerPos = new Pos3D(p).translate(0, 1.7, 0);

                    float random = (rand.nextFloat() - 0.5F) * 0.1F;

                    Pos3D vLeft = new Pos3D(-0.43, -0.55, -0.54).rotatePitch(p.isSneaking() ? 20 : 0).rotateYaw(p.renderYawOffset);
                    Pos3D vRight = new Pos3D(0.43, -0.55, -0.54).rotatePitch(p.isSneaking() ? 20 : 0).rotateYaw(p.renderYawOffset);
                    Pos3D vCenter = new Pos3D((rand.nextFloat() - 0.5F) * 0.4F, -0.86, -0.30).rotatePitch(p.isSneaking() ? 25 : 0).rotateYaw(p.renderYawOffset);

                    Pos3D rLeft = vLeft.scale(random);
                    Pos3D rRight = vRight.scale(random);

                    Pos3D mLeft = vLeft.scale(0.2).translate(new Pos3D(p.motionX, p.motionY, p.motionZ));
                    Pos3D mRight = vRight.scale(0.2).translate(new Pos3D(p.motionX, p.motionY, p.motionZ));
                    Pos3D mCenter = vCenter.scale(0.2).translate(new Pos3D(p.motionX, p.motionY, p.motionZ));

                    mLeft = mLeft.translate(rLeft);
                    mRight = mRight.translate(rRight);

                    Pos3D v = playerPos.translate(vLeft).translate(new Pos3D(p.motionX, p.motionY, p.motionZ));
                    spawnAndSetParticle(EnumParticleTypes.FLAME, world, v.x, v.y, v.z, mLeft.x, mLeft.y, mLeft.z);
                    spawnAndSetParticle(EnumParticleTypes.SMOKE_NORMAL, world, v.x, v.y, v.z, mLeft.x, mLeft.y, mLeft.z);

                    v = playerPos.translate(vRight).translate(new Pos3D(p.motionX, p.motionY, p.motionZ));
                    spawnAndSetParticle(EnumParticleTypes.FLAME, world, v.x, v.y, v.z, mRight.x, mRight.y, mRight.z);
                    spawnAndSetParticle(EnumParticleTypes.SMOKE_NORMAL, world, v.x, v.y, v.z, mRight.x, mRight.y, mRight.z);

                    v = playerPos.translate(vCenter).translate(new Pos3D(p.motionX, p.motionY, p.motionZ));
                    spawnAndSetParticle(EnumParticleTypes.FLAME, world, v.x, v.y, v.z, mCenter.x, mCenter.y, mCenter.z);
                    spawnAndSetParticle(EnumParticleTypes.SMOKE_NORMAL, world, v.x, v.y, v.z, mCenter.x, mCenter.y, mCenter.z);
                }

                // Traverse a copy of gasmask state and do animations
                if (world.getWorldTime() % 4 == 0) {
                    for (UUID uuid : Mekanism.playerState.getActiveGasmasks()) {
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
