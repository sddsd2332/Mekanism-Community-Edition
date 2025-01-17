package mekanism.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import mekanism.api.IClientTicker;
import mekanism.api.gas.GasStack;
import mekanism.client.gui.GuiRadialSelector;
import mekanism.client.render.RenderTickHandler;
import mekanism.common.CommonPlayerTickHandler;
import mekanism.common.KeySync;
import mekanism.common.Mekanism;
import mekanism.common.config.MekanismConfig;
import mekanism.common.frequency.Frequency;
import mekanism.common.item.ItemFlamethrower;
import mekanism.common.item.ItemFreeRunners;
import mekanism.common.item.ItemScubaTank;
import mekanism.common.item.armor.ItemMekAsuitFeetArmour;
import mekanism.common.item.armor.ItemMekAsuitHeadArmour;
import mekanism.common.item.armor.ItemMekaSuitArmor;
import mekanism.common.item.interfaces.IJetpackItem;
import mekanism.common.item.interfaces.IJetpackItem.JetpackMode;
import mekanism.common.item.interfaces.IModeItem;
import mekanism.common.item.interfaces.IRadialModeItem;
import mekanism.common.item.interfaces.IRadialSelectorEnum;
import mekanism.common.moduleUpgrade;
import mekanism.common.network.PacketJumpBoostData;
import mekanism.common.network.PacketModeChange.ModeChangMessage;
import mekanism.common.network.PacketPortableTeleporter.PortableTeleporterMessage;
import mekanism.common.network.PacketPortableTeleporter.PortableTeleporterPacketType;
import mekanism.common.network.PacketRadialModeChange.RadialModeChangeMessage;
import mekanism.common.network.PacketStepAssistData;
import mekanism.common.util.UpgradeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.util.*;
import java.util.Map.Entry;

/**
 * Client-side tick handler for Mekanism. Used mainly for the update check upon startup.
 *
 * @author AidanBrady
 */
@SideOnly(Side.CLIENT)
public class ClientTickHandler {

    public static Minecraft mc = FMLClientHandler.instance().getClient();
    public static Random rand = new Random();
    public static Set<IClientTicker> tickingSet = new ReferenceOpenHashSet<>();
    public static Map<EntityPlayer, TeleportData> portableTeleports = new Object2ObjectOpenHashMap<>();
    public static int wheelStatus = 0;
    public static boolean visionEnhancement = false;
    public boolean initHoliday = false;
    public boolean shouldReset = false;
    private static long lastScrollTime = -1;
    private static double scrollDelta;

    public static void killDeadNetworks() {
        tickingSet.removeIf(iClientTicker -> !iClientTicker.needsTicks());
    }

    public static boolean isJetpackInUse(EntityPlayer player, ItemStack jetpack) {
        /*
        if (player != mc.player) {
            return Mekanism.playerState.isJetpackOn(player);
        }
         */
        if (!player.isSpectator() && !jetpack.isEmpty()) {
            JetpackMode mode = ((IJetpackItem) jetpack.getItem()).getJetpackMode(jetpack);
            boolean guiOpen = mc.currentScreen != null;
            boolean ascending = mc.player.movementInput.jump;
            boolean rising = ascending && !guiOpen;
            if (mode == JetpackMode.NORMAL) {
                return rising;
            } else if (mode == JetpackMode.HOVER) {
                boolean descending = mc.player.movementInput.sneak;
                if (!rising || descending) {
                    return !CommonPlayerTickHandler.isOnGroundOrSleeping(player);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isGasMaskOn(EntityPlayer player) {
        if (player != mc.player) {
            return Mekanism.playerState.isScubaMaskOn(player);
        }
        return CommonPlayerTickHandler.isScubaMaskOn(player, player.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
    }


    public static boolean isJumpBooston(EntityPlayer player) {
        if (player != mc.player) {
            return Mekanism.jumpBoostOn.contains(player.getUniqueID());
        }
        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (!stack.isEmpty() && stack.getItem() instanceof ItemMekAsuitFeetArmour feet) {
            return feet.getJumpBoostMode(stack) == ItemMekAsuitFeetArmour.JumpBoost.OFF;
        }
        return false;
    }

    public static boolean isStepAssist(EntityPlayer player) {
        if (player != mc.player) {
            return Mekanism.stepAssistOn.contains(player.getUniqueID());
        }
        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (!stack.isEmpty() && stack.getItem() instanceof ItemMekAsuitFeetArmour feet) {
            return feet.getStepAssistMode(stack) == ItemMekAsuitFeetArmour.StepAssist.OFF;
        }
        return false;
    }

    public static boolean isFlamethrowerOn(EntityPlayer player) {
        if (player != mc.player) {
            return Mekanism.playerState.isFlamethrowerOn(player);
        }
        return hasFlamethrower(player) && mc.gameSettings.keyBindUseItem.isKeyDown();
    }

    public static boolean hasFlamethrower(EntityPlayer player) {
        ItemStack currentItem = player.inventory.getCurrentItem();
        return !currentItem.isEmpty() && currentItem.getItem() instanceof ItemFlamethrower flamethrower && flamethrower.getGas(currentItem) != null;
    }

    public static void portableTeleport(EntityPlayer player, EnumHand hand, Frequency freq) {
        int delay = MekanismConfig.current().general.portableTeleporterDelay.val();
        if (delay == 0) {
            Mekanism.packetHandler.sendToServer(new PortableTeleporterMessage(PortableTeleporterPacketType.TELEPORT, hand, freq));
        } else {
            portableTeleports.put(player, new TeleportData(hand, freq, mc.world.getWorldTime() + delay));
        }
    }

    private static void setModelVisibility(EntityPlayer entity, Render<?> entityModel, boolean showModel) {
        if (entityModel instanceof RenderPlayer renderPlayer) {
            if (entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemMekaSuitArmor) {
                renderPlayer.getMainModel().bipedHead.showModel = showModel;
                renderPlayer.getMainModel().bipedHeadwear.showModel = showModel;
                renderPlayer.getMainModel().bipedHead.isHidden = !showModel;
                renderPlayer.getMainModel().bipedHeadwear.isHidden = !showModel;
            }
            if (entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemMekaSuitArmor) {
                renderPlayer.getMainModel().bipedLeftArmwear.showModel = showModel;
                renderPlayer.getMainModel().bipedRightArmwear.showModel = showModel;
                renderPlayer.getMainModel().bipedBodyWear.showModel = showModel;
                renderPlayer.getMainModel().bipedLeftArmwear.isHidden = !showModel;
                renderPlayer.getMainModel().bipedRightArmwear.isHidden = !showModel;
                renderPlayer.getMainModel().bipedBodyWear.isHidden = !showModel;
            }
            if (entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemMekaSuitArmor) {
                renderPlayer.getMainModel().bipedLeftLegwear.showModel = showModel;
                renderPlayer.getMainModel().bipedRightLegwear.showModel = showModel;
                renderPlayer.getMainModel().bipedBodyWear.showModel = showModel;
                renderPlayer.getMainModel().bipedLeftLegwear.isHidden = !showModel;
                renderPlayer.getMainModel().bipedRightLegwear.isHidden = !showModel;
                renderPlayer.getMainModel().bipedBodyWear.isHidden = !showModel;
            }
        }
    }

    public static boolean isVisionEnhancementOn(EntityPlayer player) {
        ItemStack head = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        return !head.isEmpty() && head.getItem() instanceof ItemMekAsuitHeadArmour armour && UpgradeHelper.isUpgradeInstalled(head, moduleUpgrade.VisionEnhancementUnit) && armour.isVision;
    }

    @SubscribeEvent
    public void renderEntityPre(RenderPlayerEvent.Pre evt) {
        setModelVisibility(evt.getEntityPlayer(), evt.getRenderer(), false);
    }

    @SubscribeEvent
    public void renderEntityPost(RenderPlayerEvent.Post evt) {
        setModelVisibility(evt.getEntityPlayer(), evt.getRenderer(), true);
    }

    //Maybe it works
    @SubscribeEvent
    public void remove(WorldEvent.Unload event) {
        portableTeleports.remove(mc.player);
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if (event.phase == Phase.START) {
            tickStart();
        }
    }

    public void tickStart() {
        MekanismClient.ticksPassed++;

        if (!Mekanism.proxy.isPaused()) {
            for (Iterator<IClientTicker> iter = tickingSet.iterator(); iter.hasNext(); ) {
                IClientTicker ticker = iter.next();

                if (ticker.needsTicks()) {
                    ticker.clientTick();
                } else {
                    iter.remove();
                }
            }
        }

        if (mc.world != null) {
            shouldReset = true;
        } else if (shouldReset) {
            MekanismClient.reset();
            shouldReset = false;
        }

        if (mc.world != null && mc.player != null && !Mekanism.proxy.isPaused()) {
            if (!initHoliday || MekanismClient.ticksPassed % 1200 == 0) {
                HolidayManager.check();
                initHoliday = true;
            }

            if (mc.world.getWorldTime() - lastScrollTime > 20) {
                scrollDelta = 0;
            }

            UUID playerUUID = mc.player.getUniqueID();

            boolean jumpBoostonOn = isJumpBooston(mc.player);
            if (Mekanism.jumpBoostOn.contains(playerUUID) != jumpBoostonOn) {
                if (jumpBoostonOn && mc.currentScreen == null) {
                    Mekanism.jumpBoostOn.add(playerUUID);
                } else {
                    Mekanism.jumpBoostOn.remove(playerUUID);
                }
                Mekanism.packetHandler.sendToServer(new PacketJumpBoostData.JumpBoostDataMessage(PacketJumpBoostData.JumpBoostPacket.UPDATE, playerUUID, jumpBoostonOn));
            }

            boolean stepAssistOn = isStepAssist(mc.player);
            if (Mekanism.stepAssistOn.contains(playerUUID) != stepAssistOn) {
                if (stepAssistOn && mc.currentScreen == null) {
                    Mekanism.stepAssistOn.add(playerUUID);
                } else {
                    Mekanism.stepAssistOn.remove(playerUUID);
                }
                Mekanism.packetHandler.sendToServer(new PacketStepAssistData.StepAssistDataMessage(PacketStepAssistData.StepAssistPacket.UPDATE, playerUUID, stepAssistOn));
            }

            mc.player.stepHeight = CommonPlayerTickHandler.getStepBoost(mc.player);

            // Update player's state for various items; this also automatically notifies server if something changed and
            // kicks off sounds as necessary
            ItemStack jetpack = IJetpackItem.getActiveJetpack(mc.player);
            boolean jetpackInUse = isJetpackInUse(mc.player, jetpack);
            Mekanism.playerState.setJetpackState(playerUUID, jetpackInUse, true);
            Mekanism.playerState.setScubaMaskState(playerUUID, isGasMaskOn(mc.player), true);
            Mekanism.playerState.setFlamethrowerState(playerUUID, hasFlamethrower(mc.player), isFlamethrowerOn(mc.player), true);

            for (Iterator<Entry<EntityPlayer, TeleportData>> iter = portableTeleports.entrySet().iterator(); iter.hasNext(); ) {
                Entry<EntityPlayer, TeleportData> entry = iter.next();
                EntityPlayer player = entry.getKey();
                for (int i = 0; i < 100; i++) {
                    double x = player.posX + rand.nextDouble() - 0.5D;
                    double y = player.posY + rand.nextDouble() * 2 - 2D;
                    double z = player.posZ + rand.nextDouble() - 0.5D;
                    mc.world.spawnParticle(EnumParticleTypes.PORTAL, x, y, z, 0, 1, 0);
                }

                if (mc.world.getWorldTime() == entry.getValue().teleportTime) {
                    Mekanism.packetHandler.sendToServer(new PortableTeleporterMessage(PortableTeleporterPacketType.TELEPORT, entry.getValue().hand, entry.getValue().freq));
                    iter.remove();
                }
            }

            ItemStack chestStack = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

            if (!mc.player.isCreative() && !mc.player.isSpectator()) {
                if (isFlamethrowerOn(mc.player)) {
                    ItemFlamethrower flamethrower = (ItemFlamethrower) mc.player.inventory.getCurrentItem().getItem();
                    flamethrower.useGas(mc.player.inventory.getCurrentItem());
                }
            }

            if (!jetpack.isEmpty()) {
                ItemStack primaryJetpack = IJetpackItem.getPrimaryJetpack(mc.player);
                if (!primaryJetpack.isEmpty()) {
                    JetpackMode primaryMode = ((IJetpackItem) primaryJetpack.getItem()).getJetpackMode(primaryJetpack);
                    JetpackMode mode = IJetpackItem.getPlayerJetpackMode(mc.player, primaryMode, () -> mc.player.movementInput.jump);
                    MekanismClient.updateKey(mc.player.movementInput.jump, KeySync.ASCEND);
                    if (jetpackInUse && IJetpackItem.handleJetpackMotion(mc.player, mode, () -> mc.player.movementInput.jump)) {
                        mc.player.fallDistance = 0.0F;
                    }
                }
            }

            if (isGasMaskOn(mc.player)) {
                ItemScubaTank tank = (ItemScubaTank) chestStack.getItem();
                final int max = 300;
                tank.useGas(chestStack);
                GasStack received = tank.useGas(chestStack, max - mc.player.getAir());

                if (received != null) {
                    mc.player.setAir(mc.player.getAir() + received.amount);
                }
                if (mc.player.getAir() == max) {
                    for (PotionEffect effect : mc.player.getActivePotionEffects()) {
                        for (int i = 0; i < 9; i++) {
                            effect.onUpdate(mc.player);
                        }
                    }
                }
            }

            if (isVisionEnhancementOn(mc.player)) {
                visionEnhancement = true;
                // adds if it doesn't exist, otherwise tops off duration to 220. equal or less than 200 will make vision flickers
                mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 220, 0, false, false));
            } else if (visionEnhancement) {
                visionEnhancement = false;
                PotionEffect effect = mc.player.getActivePotionEffect(MobEffects.NIGHT_VISION);
                if (effect != null && effect.getDuration() <= 220) {
                    //Only remove it if it is our effect and not one that has a longer remaining duration
                    mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
                }
            }

            ItemStack stack = mc.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
            if (MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.modeSwitchKey) && stack.getItem() instanceof IRadialModeItem) {
                if (mc.currentScreen == null || mc.currentScreen instanceof GuiRadialSelector) {
                    updateSelectorRenderer((IRadialModeItem<?>) stack.getItem());
                }
            } else if (mc.currentScreen instanceof GuiRadialSelector) {
                mc.displayGuiScreen(null);
            }
        }
    }

    private <TYPE extends Enum<TYPE> & IRadialSelectorEnum<TYPE>> void updateSelectorRenderer(IRadialModeItem<TYPE> modeItem) {
        Class<TYPE> modeClass = modeItem.getModeClass();
        if (!(mc.currentScreen instanceof GuiRadialSelector) || ((GuiRadialSelector<?>) mc.currentScreen).getEnumClass() != modeClass) {
            mc.displayGuiScreen(new GuiRadialSelector<>(modeClass, () -> {
                if (mc.player != null) {
                    ItemStack s = mc.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                    if (s.getItem() instanceof IRadialModeItem) {
                        return ((IRadialModeItem<TYPE>) s.getItem()).getMode(s);
                    }
                }
                return modeItem.getDefaultMode();
            }, type -> {
                if (mc.player != null) {
                    Mekanism.packetHandler.sendToServer(new RadialModeChangeMessage(EntityEquipmentSlot.MAINHAND, type.ordinal()));
                }
            }));
        }
    }


    private void handleModeScroll(Event event, double delta) {
        if (delta != 0 && IModeItem.isModeItem(mc.player, EntityEquipmentSlot.MAINHAND)) {
            wheelStatus += Mouse.getEventDWheel();
            int shift = wheelStatus / 120;
            wheelStatus = wheelStatus % 120;
            int handoff = 0;
            if (shift > 0) {
                handoff = -1;
            } else if (shift < 0) {
                handoff = 1;
            }
            RenderTickHandler.modeSwitchTimer = 100;
            Mekanism.packetHandler.sendToServer(new ModeChangMessage(EntityEquipmentSlot.MAINHAND, handoff));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (mc.player != null && mc.player.isSneaking()) {
            if (MekanismConfig.current().client.allowModeScroll.val()) {
                handleModeScroll(event, Mouse.getEventDWheel());
            }
        }
    }

    @SubscribeEvent
    public void GuiScreenEvent(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (mc.world.getWorldInfo().isHardcoreModeEnabled()) {
                return;
            }
            if (mc.player instanceof EntityPlayerSP) {
                ItemStack head = mc.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (!mc.player.isEntityAlive() && !head.isEmpty() && head.getItem() instanceof ItemMekAsuitHeadArmour armour && ((UpgradeHelper.isUpgradeInstalled(head, moduleUpgrade.EMERGENCY_RESCUE) && armour.getEmergency(head)) || (UpgradeHelper.isUpgradeInstalled(head, moduleUpgrade.ADVANCED_INTERCEPTION_SYSTEM_UNIT) && armour.getInterception(head)))) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFogLighting(EntityViewRenderEvent.FogColors event) {
        if (visionEnhancement) {
            float oldRatio = 0.1F;
            float newRatio = 1 - oldRatio;
            float red = oldRatio * event.getRed();
            float green = oldRatio * event.getGreen();
            float blue = oldRatio * event.getBlue();
            event.setRed(red + newRatio * 0.4F);
            event.setGreen(green + newRatio * 0.8F);
            event.setBlue(blue + newRatio * 0.4F);
        }
    }

    @SubscribeEvent
    public void onFog(EntityViewRenderEvent.RenderFogEvent event) {
        if (visionEnhancement) {
            float fog = 0.1F;
            ItemStack head = mc.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (!head.isEmpty() && head.getItem() instanceof ItemMekAsuitHeadArmour && UpgradeHelper.isUpgradeInstalled(head, moduleUpgrade.VisionEnhancementUnit)) {
                fog -= UpgradeHelper.getUpgradeLevel(head, moduleUpgrade.VisionEnhancementUnit) * 0.022F;
            }
            GlStateManager.setFogDensity(fog);
            GlStateManager.setFog(GlStateManager.FogMode.EXP2);
        }
    }

    private static class TeleportData {

        private EnumHand hand;
        private Frequency freq;
        private long teleportTime;

        public TeleportData(EnumHand h, Frequency f, long t) {
            hand = h;
            freq = f;
            teleportTime = t;
        }
    }

}
