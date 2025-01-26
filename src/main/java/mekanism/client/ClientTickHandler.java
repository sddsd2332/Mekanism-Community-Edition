package mekanism.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import mekanism.api.IClientTicker;
import mekanism.api.gear.IModule;
import mekanism.client.gui.GuiRadialSelector;
import mekanism.client.render.RenderTickHandler;
import mekanism.common.CommonPlayerTickHandler;
import mekanism.common.KeySync;
import mekanism.common.Mekanism;
import mekanism.common.MekanismModules;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.content.gear.mekasuit.ModuleVisionEnhancementUnit;
import mekanism.common.frequency.Frequency;
import mekanism.common.item.ItemFlamethrower;
import mekanism.common.item.armor.ItemMekaSuitBodyArmor;
import mekanism.common.item.armor.ItemMekaSuitHelmet;
import mekanism.common.item.armor.ItemMekaSuitPants;
import mekanism.common.item.interfaces.IJetpackItem;
import mekanism.common.item.interfaces.IJetpackItem.JetpackMode;
import mekanism.common.item.interfaces.IModeItem;
import mekanism.common.item.interfaces.IRadialModeItem;
import mekanism.common.item.interfaces.IRadialSelectorEnum;
import mekanism.common.network.PacketModeChange.ModeChangMessage;
import mekanism.common.network.PacketPortableTeleporter.PortableTeleporterMessage;
import mekanism.common.network.PacketPortableTeleporter.PortableTeleporterPacketType;
import mekanism.common.network.PacketRadialModeChange.RadialModeChangeMessage;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
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

    public static Minecraft minecraft = Minecraft.getMinecraft();
    public static Random rand = new Random();
    public static Set<IClientTicker> tickingSet = new ReferenceOpenHashSet<>();
    public static Map<EntityPlayer, TeleportData> portableTeleports = new Object2ObjectOpenHashMap<>();
    public static int wheelStatus = 0;
    public static boolean visionEnhancement = false;

    public boolean initHoliday = false;
    public boolean shouldReset = false;

    private static long lastScrollTime = -1;
    private static double scrollDelta;

    public static boolean isJetpackInUse(EntityPlayer player, ItemStack jetpack) {
        if (!player.isSpectator() && !jetpack.isEmpty()) {
            JetpackMode mode = ((IJetpackItem) jetpack.getItem()).getJetpackMode(jetpack);
            boolean guiOpen = minecraft.currentScreen != null;
            boolean ascending = minecraft.player.movementInput.jump;
            boolean rising = ascending && !guiOpen;
            if (mode == JetpackMode.NORMAL) {
                return rising;
            } else if (mode == JetpackMode.HOVER) {
                boolean descending = minecraft.player.movementInput.sneak;
                if (!rising || descending) {
                    return !CommonPlayerTickHandler.isOnGroundOrSleeping(player);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isScubaMaskOn(EntityPlayer player) {
        if (player != minecraft.player) {
            return Mekanism.playerState.isScubaMaskOn(player);
        }
        return CommonPlayerTickHandler.isScubaMaskOn(player, player.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
    }

    public static boolean isGravitationalModulationOn(EntityPlayer player) {
        if (player != minecraft.player) {
            return Mekanism.playerState.isGravitationalModulationOn(player);
        }
        return CommonPlayerTickHandler.isGravitationalModulationOn(player);
    }

    public static boolean isVisionEnhancementOn(EntityPlayer player) {
        IModule<ModuleVisionEnhancementUnit> module = ModuleHelper.get().load(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD), MekanismModules.VISION_ENHANCEMENT_UNIT);
        return module != null && module.isEnabled() && module.getContainerEnergy() > MekanismConfig.current().meka.mekaSuitEnergyUsageVisionEnhancement.val();
    }


    public static boolean isFlamethrowerOn(EntityPlayer player) {
        if (player != minecraft.player) {
            return Mekanism.playerState.isFlamethrowerOn(player);
        }
        return hasFlamethrower(player) && minecraft.gameSettings.keyBindUseItem.isKeyDown();
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
            portableTeleports.put(player, new TeleportData(hand, freq, minecraft.world.getWorldTime() + delay));
        }
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

        if (minecraft.world != null) {
            shouldReset = true;
        } else if (shouldReset) {
            MekanismClient.reset();
            shouldReset = false;
        }


        if (minecraft.world != null && minecraft.player != null && !Mekanism.proxy.isPaused()) {
            if (!initHoliday || MekanismClient.ticksPassed % 1200 == 0) {
                HolidayManager.check();
                initHoliday = true;
            }

            if (minecraft.world.getWorldTime() - lastScrollTime > 20) {
                scrollDelta = 0;
            }

            UUID playerUUID = minecraft.player.getUniqueID();


            // Update player's state for various items; this also automatically notifies server if something changed and
            // kicks off sounds as necessary
            ItemStack jetpack = IJetpackItem.getActiveJetpack(minecraft.player);
            boolean jetpackInUse = isJetpackInUse(minecraft.player, jetpack);
            Mekanism.playerState.setJetpackState(playerUUID, jetpackInUse, true);
            Mekanism.playerState.setScubaMaskState(playerUUID, isScubaMaskOn(minecraft.player), true);
            Mekanism.playerState.setGravitationalModulationState(playerUUID, isGravitationalModulationOn(minecraft.player), true);
            Mekanism.playerState.setFlamethrowerState(playerUUID, hasFlamethrower(minecraft.player), isFlamethrowerOn(minecraft.player), true);

            for (Iterator<Entry<EntityPlayer, TeleportData>> iter = portableTeleports.entrySet().iterator(); iter.hasNext(); ) {
                Entry<EntityPlayer, TeleportData> entry = iter.next();
                EntityPlayer player = entry.getKey();
                for (int i = 0; i < 100; i++) {
                    double x = player.posX + rand.nextDouble() - 0.5D;
                    double y = player.posY + rand.nextDouble() * 2 - 2D;
                    double z = player.posZ + rand.nextDouble() - 0.5D;
                    minecraft.world.spawnParticle(EnumParticleTypes.PORTAL, x, y, z, 0, 1, 0);
                }
                TeleportData data = entry.getValue();
                if (minecraft.world.getWorldTime() == data.teleportTime) {
                    Mekanism.packetHandler.sendToServer(new PortableTeleporterMessage(PortableTeleporterPacketType.TELEPORT, data.hand, data.freq));
                    iter.remove();
                }
            }

            if (!jetpack.isEmpty()) {
                ItemStack primaryJetpack = IJetpackItem.getPrimaryJetpack(minecraft.player);
                if (!primaryJetpack.isEmpty()) {
                    JetpackMode primaryMode = ((IJetpackItem) primaryJetpack.getItem()).getJetpackMode(primaryJetpack);
                    JetpackMode mode = IJetpackItem.getPlayerJetpackMode(minecraft.player, primaryMode, () -> minecraft.player.movementInput.jump);
                    MekanismClient.updateKey(minecraft.player.movementInput.jump, KeySync.ASCEND);
                    if (jetpackInUse && IJetpackItem.handleJetpackMotion(minecraft.player, mode, () -> minecraft.player.movementInput.jump)) {
                        minecraft.player.fallDistance = 0.0F;
                    }
                }
            }

            if (isScubaMaskOn(minecraft.player) && minecraft.player.getAir() == 300) {
                for (PotionEffect effect : minecraft.player.getActivePotionEffects()) {
                    if (MekanismUtils.shouldSpeedUpEffect(effect)) {
                        for (int i = 0; i < 9; i++) {
                            MekanismUtils.speedUpEffectSafely(minecraft.player, effect);
                        }
                    }
                }
            }

            if (isVisionEnhancementOn(minecraft.player)) {
                visionEnhancement = true;
                // adds if it doesn't exist, otherwise tops off duration to 220. equal or less than 200 will make vision flickers
                minecraft.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 220, 0, false, false));
            } else if (visionEnhancement) {
                visionEnhancement = false;
                PotionEffect effect = minecraft.player.getActivePotionEffect(MobEffects.NIGHT_VISION);
                if (effect != null && effect.getDuration() <= 220) {
                    //Only remove it if it is our effect and not one that has a longer remaining duration
                    minecraft.player.removePotionEffect(MobEffects.NIGHT_VISION);
                }
            }

            ItemStack stack = minecraft.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
            if (MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.handModeSwitchKey) && stack.getItem() instanceof IRadialModeItem) {
                if (minecraft.currentScreen == null || minecraft.currentScreen instanceof GuiRadialSelector) {
                    updateSelectorRenderer((IRadialModeItem<?>) stack.getItem());
                }
            } else if (minecraft.currentScreen instanceof GuiRadialSelector) {
                minecraft.displayGuiScreen(null);
            }

        }
    }

    private <TYPE extends Enum<TYPE> & IRadialSelectorEnum<TYPE>> void updateSelectorRenderer(IRadialModeItem<TYPE> modeItem) {
        Class<TYPE> modeClass = modeItem.getModeClass();
        if (!(minecraft.currentScreen instanceof GuiRadialSelector) || ((GuiRadialSelector<?>) minecraft.currentScreen).getEnumClass() != modeClass) {
            minecraft.displayGuiScreen(new GuiRadialSelector<>(modeClass, () -> {
                if (minecraft.player != null) {
                    ItemStack s = minecraft.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                    if (s.getItem() instanceof IRadialModeItem) {
                        return ((IRadialModeItem<TYPE>) s.getItem()).getMode(s);
                    }
                }
                return modeItem.getDefaultMode();
            }, type -> {
                if (minecraft.player != null) {
                    Mekanism.packetHandler.sendToServer(new RadialModeChangeMessage(EntityEquipmentSlot.MAINHAND, type.ordinal()));
                }
            }));
        }
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (minecraft.player != null && minecraft.player.isSneaking()) {
            if (MekanismConfig.current().client.allowModeScroll.val()) {
                handleModeScroll(event, Mouse.getEventDWheel());
            }
        }
    }

    private void handleModeScroll(Event event, double delta) {
        if (delta != 0 && IModeItem.isModeItem(minecraft.player, EntityEquipmentSlot.MAINHAND)) {
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
            IModule<ModuleVisionEnhancementUnit> module = ModuleHelper.get().load(minecraft.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD), MekanismModules.VISION_ENHANCEMENT_UNIT);
            if (module != null) {
                fog -= module.getInstalledCount() * 0.022F;
            }
            GlStateManager.setFogDensity(fog);
            GlStateManager.setFog(GlStateManager.FogMode.EXP2);
        }
    }

    @SubscribeEvent
    public void renderEntityPre(RenderPlayerEvent.Pre evt) {
        setModelVisibility(evt.getEntityPlayer(), evt.getRenderer(), false);
    }

    @SubscribeEvent
    public void renderEntityPost(RenderPlayerEvent.Post evt) {
        setModelVisibility(evt.getEntityPlayer(), evt.getRenderer(), true);
    }

    private static void setModelVisibility(EntityPlayer entity, Render<?> entityModel, boolean showModel) {
        if (entityModel instanceof RenderPlayer renderPlayer) {
            if (entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemMekaSuitHelmet) {
                renderPlayer.getMainModel().bipedHead.showModel = showModel;
                renderPlayer.getMainModel().bipedHeadwear.showModel = showModel;
                renderPlayer.getMainModel().bipedHead.isHidden = !showModel;
                renderPlayer.getMainModel().bipedHeadwear.isHidden = !showModel;
            }
            if (entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemMekaSuitBodyArmor) {
                renderPlayer.getMainModel().bipedLeftArmwear.showModel = showModel;
                renderPlayer.getMainModel().bipedRightArmwear.showModel = showModel;
                renderPlayer.getMainModel().bipedBodyWear.showModel = showModel;
                renderPlayer.getMainModel().bipedLeftArmwear.isHidden = !showModel;
                renderPlayer.getMainModel().bipedRightArmwear.isHidden = !showModel;
                renderPlayer.getMainModel().bipedBodyWear.isHidden = !showModel;
            }
            if (entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemMekaSuitPants) {
                renderPlayer.getMainModel().bipedLeftLegwear.showModel = showModel;
                renderPlayer.getMainModel().bipedRightLegwear.showModel = showModel;
                renderPlayer.getMainModel().bipedBodyWear.showModel = showModel;
                renderPlayer.getMainModel().bipedLeftLegwear.isHidden = !showModel;
                renderPlayer.getMainModel().bipedRightLegwear.isHidden = !showModel;
                renderPlayer.getMainModel().bipedBodyWear.isHidden = !showModel;
            }
        }
    }

    //Maybe it works
    @SubscribeEvent
    public void remove(WorldEvent.Unload event) {
        portableTeleports.remove(minecraft.player);
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

    /*
    @SubscribeEvent
    public void GuiScreenEvent(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (minecraft.world.getWorldInfo().isHardcoreModeEnabled()) {
                return;
            }
            if (minecraft.player instanceof EntityPlayerSP) {
                ItemStack head = minecraft.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (!minecraft.player.isEntityAlive() && !head.isEmpty() && head.getItem() instanceof ItemOldMekAsuitHeadArmour armour && ((UpgradeHelper.isUpgradeInstalled(head, moduleUpgrade.EMERGENCY_RESCUE) && armour.getEmergency(head)) || (UpgradeHelper.isUpgradeInstalled(head, moduleUpgrade.ADVANCED_INTERCEPTION_SYSTEM_UNIT) && armour.getInterception(head)))) {
                    event.setCanceled(true);
                }
            }
        }
    }
     */
}
