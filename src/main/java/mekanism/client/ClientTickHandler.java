package mekanism.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mekanism.api.IClientTicker;
import mekanism.api.gas.GasStack;
import mekanism.client.render.RenderTickHandler;
import mekanism.common.CommonPlayerTickHandler;
import mekanism.common.KeySync;
import mekanism.common.Mekanism;
import mekanism.common.config.MekanismConfig;
import mekanism.common.frequency.Frequency;
import mekanism.common.item.*;
import mekanism.common.item.ItemAtomicDisassembler.Mode;
import mekanism.common.item.ItemConfigurator.ConfiguratorMode;
import mekanism.common.item.ItemFlamethrower.FlamethrowerMode;
import mekanism.common.item.ItemJetpack.JetpackMode;
import mekanism.common.item.ItemMekTool.MekToolMode;
import mekanism.common.network.PacketFreeRunnerData;
import mekanism.common.network.PacketItemStack.ItemStackMessage;
import mekanism.common.network.PacketPortableTeleporter.PortableTeleporterMessage;
import mekanism.common.network.PacketPortableTeleporter.PortableTeleporterPacketType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
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
    public static Set<IClientTicker> tickingSet = new ObjectOpenHashSet<>();
    public static Map<EntityPlayer, TeleportData> portableTeleports = new Object2ObjectOpenHashMap<>();
    public static int wheelStatus = 0;
    public boolean initHoliday = false;
    public boolean shouldReset = false;

    public static void killDeadNetworks() {
        tickingSet.removeIf(iClientTicker -> !iClientTicker.needsTicks());
    }

    public static boolean isJetpackActive(EntityPlayer player) {
        if (player != mc.player) {
            return Mekanism.playerState.isJetpackOn(player);
        }
        if (!player.isCreative() && !player.isSpectator()) {
            ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (!chest.isEmpty() && chest.getItem() instanceof ItemJetpack jetpack) {
                if (jetpack.getGas(chest) != null) {
                    JetpackMode mode = jetpack.getMode(chest);
                    if (mode == JetpackMode.NORMAL) {
                        return mc.currentScreen == null && mc.gameSettings.keyBindJump.isKeyDown();
                    } else if (mode == JetpackMode.HOVER) {
                        boolean ascending = mc.gameSettings.keyBindJump.isKeyDown();
                        boolean descending = mc.gameSettings.keyBindSneak.isKeyDown();
                        //if ((!ascending && !descending) || (ascending && descending) || mc.currentScreen != null || (descending && mc.currentScreen == null))
                        //Simplifies to
                        if (!ascending || descending || mc.currentScreen != null) {
                            return !CommonPlayerTickHandler.isOnGround(player);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isGasMaskOn(EntityPlayer player) {
        if (player != mc.player) {
            return Mekanism.playerState.isGasmaskOn(player);
        }
        return CommonPlayerTickHandler.isGasMaskOn(player);
    }

    public static boolean isFreeRunnerOn(EntityPlayer player) {
        if (player != mc.player) {
            return Mekanism.freeRunnerOn.contains(player.getUniqueID());
        }

        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (!stack.isEmpty() && stack.getItem() instanceof ItemFreeRunners freeRunners) {
            /*freeRunners.getEnergy(stack) > 0 && */
            return freeRunners.getMode(stack) == ItemFreeRunners.FreeRunnerMode.NORMAL;
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
        if (!currentItem.isEmpty() && currentItem.getItem() instanceof ItemFlamethrower) {
            return ((ItemFlamethrower) currentItem.getItem()).getGas(currentItem) != null;
        }
        return false;
    }

    public static void portableTeleport(EntityPlayer player, EnumHand hand, Frequency freq) {
        int delay = MekanismConfig.current().general.portableTeleporterDelay.val();
        if (delay == 0) {
            Mekanism.packetHandler.sendToServer(new PortableTeleporterMessage(PortableTeleporterPacketType.TELEPORT, hand, freq));
        } else {
            portableTeleports.put(player, new TeleportData(hand, freq, mc.world.getWorldTime() + delay));
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

            UUID playerUUID = mc.player.getUniqueID();
            boolean freeRunnerOn = isFreeRunnerOn(mc.player);
            if (Mekanism.freeRunnerOn.contains(playerUUID) != freeRunnerOn) {
                if (freeRunnerOn && mc.currentScreen == null) {
                    Mekanism.freeRunnerOn.add(playerUUID);
                } else {
                    Mekanism.freeRunnerOn.remove(playerUUID);
                }
                Mekanism.packetHandler.sendToServer(new PacketFreeRunnerData.FreeRunnerDataMessage(PacketFreeRunnerData.FreeRunnerPacket.UPDATE, playerUUID, freeRunnerOn));
            }

            ItemStack bootStack = mc.player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (!bootStack.isEmpty() && bootStack.getItem() instanceof ItemFreeRunners && freeRunnerOn && !mc.player.isSneaking()) {
                mc.player.stepHeight = 1.002F;
            } else if (mc.player.stepHeight == 1.002F) {
                mc.player.stepHeight = 0.6F;
            }

            // Update player's state for various items; this also automatically notifies server if something changed and
            // kicks off sounds as necessary
            Mekanism.playerState.setJetpackState(playerUUID, isJetpackActive(mc.player), true);
            Mekanism.playerState.setGasmaskState(playerUUID, isGasMaskOn(mc.player), true);
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

            if (!chestStack.isEmpty() && chestStack.getItem() instanceof ItemJetpack) {
                MekanismClient.updateKey(mc.gameSettings.keyBindJump, KeySync.ASCEND);
                MekanismClient.updateKey(mc.gameSettings.keyBindSneak, KeySync.DESCEND);
            }

            if (!mc.player.isCreative() && !mc.player.isSpectator()) {
                if (isFlamethrowerOn(mc.player)) {
                    ItemFlamethrower flamethrower = (ItemFlamethrower) mc.player.inventory.getCurrentItem().getItem();
                    flamethrower.useGas(mc.player.inventory.getCurrentItem());
                }
            }

            if (isJetpackActive(mc.player)) {
                ItemJetpack jetpack = (ItemJetpack) chestStack.getItem();
                JetpackMode mode = jetpack.getMode(chestStack);
                if (mode == JetpackMode.NORMAL) {
                    mc.player.motionY = Math.min(mc.player.motionY + 0.15D, 0.5D);
                    mc.player.fallDistance = 0.0F;
                } else if (mode == JetpackMode.HOVER) {
                    boolean ascending = mc.gameSettings.keyBindJump.isKeyDown();
                    boolean descending = mc.gameSettings.keyBindSneak.isKeyDown();
                    if ((!ascending && !descending) || (ascending && descending) || mc.currentScreen != null) {
                        if (mc.player.motionY > 0) {
                            mc.player.motionY = Math.max(mc.player.motionY - 0.15D, 0);
                        } else if (mc.player.motionY < 0) {
                            if (!CommonPlayerTickHandler.isOnGround(mc.player)) {
                                mc.player.motionY = Math.min(mc.player.motionY + 0.15D, 0);
                            }
                        }
                    } else if (ascending) {
                        mc.player.motionY = Math.min(mc.player.motionY + 0.15D, 0.2D);
                    } else if (!CommonPlayerTickHandler.isOnGround(mc.player)) {
                        mc.player.motionY = Math.max(mc.player.motionY - 0.15D, -0.2D);
                    }
                    mc.player.fallDistance = 0.0F;
                }
                jetpack.useGas(chestStack);
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
        }
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (mc.player != null && mc.player.isSneaking()) {
            ItemStack stack = mc.player.getHeldItemMainhand();
            int delta = Mouse.getEventDWheel();
            if (MekanismConfig.current().client.allowConfiguratorModeScroll.val()) {
                if (stack.getItem() instanceof ItemConfigurator configurator && delta != 0) {
                    RenderTickHandler.modeSwitchTimer = 100;
                    wheelStatus += Mouse.getEventDWheel();
                    int scaledDelta = wheelStatus / 120;
                    wheelStatus = wheelStatus % 120;
                    int newVal = configurator.getState(stack).ordinal() + (scaledDelta % ConfiguratorMode.values().length);

                    if (newVal > 0) {
                        newVal = newVal % ConfiguratorMode.values().length;
                    } else if (newVal < 0) {
                        newVal = ConfiguratorMode.values().length + newVal;
                    }
                    configurator.setState(stack, ConfiguratorMode.values()[newVal]);
                    Mekanism.packetHandler.sendToServer(new ItemStackMessage(EnumHand.MAIN_HAND, Collections.singletonList(newVal)));
                    event.setCanceled(true);
                }
            }
            if (MekanismConfig.current().client.allowFlamethrowerModeScroll.val()) {
                if (stack.getItem() instanceof ItemFlamethrower Flamethrower && delta != 0) {
                    RenderTickHandler.modeSwitchTimer = 100;
                    wheelStatus += Mouse.getEventDWheel();
                    int scaledDelta = wheelStatus / 120;
                    wheelStatus = wheelStatus % 120;
                    int newVal = Flamethrower.getMode(stack).ordinal() + (scaledDelta % FlamethrowerMode.values().length);

                    if (newVal > 0) {
                        newVal = newVal % FlamethrowerMode.values().length;
                    } else if (newVal < 0) {
                        newVal = FlamethrowerMode.values().length + newVal;
                    }
                    Flamethrower.setMode(stack, FlamethrowerMode.values()[newVal]);
                    Mekanism.packetHandler.sendToServer(new ItemStackMessage(EnumHand.MAIN_HAND, Collections.singletonList(newVal)));
                    event.setCanceled(true);
                }
            }
            if (MekanismConfig.current().client.allowAtomicDisassemblerModeScroll.val()) {
                if (stack.getItem() instanceof ItemAtomicDisassembler AtomicDisassembler && delta != 0) {
                    RenderTickHandler.modeSwitchTimer = 100;
                    wheelStatus += Mouse.getEventDWheel();
                    int scaledDelta = wheelStatus / 120;
                    wheelStatus = wheelStatus % 120;
                    int newVal = AtomicDisassembler.getMode(stack).ordinal() + (scaledDelta % Mode.values().length);

                    if (newVal > 0) {
                        newVal = newVal % Mode.values().length;
                    } else if (newVal < 0) {
                        newVal = Mode.values().length + newVal;
                    }
                    AtomicDisassembler.setMode(stack, Mode.values()[newVal]);
                    Mekanism.packetHandler.sendToServer(new ItemStackMessage(EnumHand.MAIN_HAND, Collections.singletonList(newVal)));
                    event.setCanceled(true);
                }
            }
            if (MekanismConfig.current().client.allowMekToolModeScroll.val()){
                if (stack.getItem() instanceof ItemMekTool MekTool && delta != 0) {
                    RenderTickHandler.modeSwitchTimer = 100;
                    wheelStatus += Mouse.getEventDWheel();
                    int scaledDelta = wheelStatus / 120;
                    wheelStatus = wheelStatus % 120;
                    int newVal = MekTool.getMode(stack).ordinal() + (scaledDelta % MekToolMode.values().length);
                    if (newVal > 0) {
                        newVal = newVal % MekToolMode.values().length;
                    } else if (newVal < 0) {
                        newVal = MekToolMode.values().length + newVal;
                    }
                    MekTool.setMode(stack,MekToolMode.values()[newVal]);
                    Mekanism.packetHandler.sendToServer(new ItemStackMessage(EnumHand.MAIN_HAND, Collections.singletonList(newVal)));
                    event.setCanceled(true);
                }
            }
            if (stack.getItem() instanceof ItemElectricBow Bow && delta !=0){
                RenderTickHandler.modeSwitchTimer = 100;
                wheelStatus += Mouse.getEventDWheel();
                int scaledDelta = wheelStatus / 120;
                wheelStatus = wheelStatus % 120;
                if (Math.abs(scaledDelta) % 2 == 1) {
                    boolean newState = !Bow.getFireState(stack);
                    Bow.setFireState(stack, newState);
                    Mekanism.packetHandler.sendToServer(new ItemStackMessage(EnumHand.MAIN_HAND, Collections.singletonList(newState)));
                    event.setCanceled(true);
                }
            }
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
