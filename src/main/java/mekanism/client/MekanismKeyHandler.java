package mekanism.client;

import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.MekanismSounds;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.config.MekanismConfig;
import mekanism.common.item.*;
import mekanism.common.item.ItemConfigurator.ConfiguratorMode;
import mekanism.common.item.armor.ItemMekAsuitFeetArmour;
import mekanism.common.item.interfaces.IJetpackItem;
import mekanism.common.item.interfaces.IJetpackItem.JetpackMode;
import mekanism.common.moduleUpgrade;
import mekanism.common.network.PacketFlamethrowerData.FlamethrowerDataMessage;
import mekanism.common.network.PacketFreeRunnerData;
import mekanism.common.network.PacketFreeRunnerData.FreeRunnerDataMessage;
import mekanism.common.network.PacketItemStack.ItemStackMessage;
import mekanism.common.network.PacketJetpackData.JetpackDataMessage;
import mekanism.common.network.PacketJumpBoostData;
import mekanism.common.network.PacketJumpBoostData.JumpBoostDataMessage;
import mekanism.common.network.PacketScubaTankData.ScubaTankDataMessage;
import mekanism.common.network.PacketStepAssistData;
import mekanism.common.network.PacketStepAssistData.StepAssistDataMessage;
import mekanism.common.util.LangUtils;
import mekanism.common.util.TextComponentGroup;
import mekanism.common.util.UpgradeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.Collections;

@SideOnly(Side.CLIENT)
public class MekanismKeyHandler extends MekKeyHandler {

    public static final String keybindCategory = Mekanism.MOD_NAME;
    public static KeyBinding modeSwitchKey = new KeyBinding("mekanism.key.mode", Keyboard.KEY_M, keybindCategory);
    public static KeyBinding armorModeSwitchKey = new KeyBinding("mekanism.key.armorMode", Keyboard.KEY_G, keybindCategory);
    public static KeyBinding freeRunnerModeSwitchKey = new KeyBinding("mekanism.key.feetMode", Keyboard.KEY_B, keybindCategory);
    public static KeyBinding MekAsuitFeetModeSwitchKey = new KeyBinding("mekanism.key.MekAsuitFeetMode", Keyboard.KEY_N, keybindCategory);
    public static KeyBinding voiceKey = new KeyBinding("mekanism.key.voice", Keyboard.KEY_U, keybindCategory);

    public static KeyBinding enableHUDkEY = new KeyBinding("mekanism.key.enableHUD", Keyboard.KEY_H, keybindCategory);

    public static KeyBinding sneakKey = Minecraft.getMinecraft().gameSettings.keyBindSneak;
    public static KeyBinding jumpKey = Minecraft.getMinecraft().gameSettings.keyBindJump;

    private static Builder BINDINGS = new Builder()
            .addBinding(modeSwitchKey, false)
            .addBinding(armorModeSwitchKey, false)
            .addBinding(freeRunnerModeSwitchKey, false)
            .addBinding(MekAsuitFeetModeSwitchKey, false)
            .addBinding(voiceKey, true)
            .addBinding(enableHUDkEY, false);

    public MekanismKeyHandler() {
        super(BINDINGS);

        ClientRegistry.registerKeyBinding(modeSwitchKey);
        ClientRegistry.registerKeyBinding(armorModeSwitchKey);
        ClientRegistry.registerKeyBinding(freeRunnerModeSwitchKey);
        ClientRegistry.registerKeyBinding(MekAsuitFeetModeSwitchKey);
        ClientRegistry.registerKeyBinding(voiceKey);
        ClientRegistry.registerKeyBinding(enableHUDkEY);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(InputEvent event) {
        keyTick();
    }

    @Override
    public void keyDown(KeyBinding kb, boolean isRepeat) {
        EntityPlayer player = FMLClientHandler.instance().getClient().player;
        if (kb == modeSwitchKey) {

            ItemStack toolStack = player.inventory.getCurrentItem();
            Item item = toolStack.getItem();

            if (player.isSneaking() && item instanceof ItemConfigurator configurator) {
                ConfiguratorMode configuratorMode = configurator.getState(toolStack);
                int toSet = (configuratorMode.ordinal() + 1) % ConfiguratorMode.values().length;
                configuratorMode = ConfiguratorMode.values()[toSet];
                configurator.setState(toolStack, configuratorMode);
                Mekanism.packetHandler.sendToServer(new ItemStackMessage(EnumHand.MAIN_HAND, Collections.singletonList(toSet)));
                player.sendMessage(new TextComponentGroup(TextFormatting.GRAY).string(Mekanism.LOG_TAG, TextFormatting.DARK_BLUE).string(" ")
                        .translation("mekanism.tooltip.configureState", LangUtils.withColor(configuratorMode.getNameComponent(), configuratorMode.getColor().textFormatting)));
            } else if (player.isSneaking() && item instanceof ItemElectricBow bow) {
                boolean newBowState = !bow.getFireState(toolStack);
                bow.setFireState(toolStack, newBowState);
                Mekanism.packetHandler.sendToServer(new ItemStackMessage(EnumHand.MAIN_HAND, Collections.singletonList(newBowState)));
                player.sendMessage(new TextComponentGroup(TextFormatting.GRAY).string(Mekanism.LOG_TAG, TextFormatting.DARK_BLUE).string(" ")
                        .translation("mekanism.tooltip.fireMode", LangUtils.onOffColoured(newBowState)));
            } else if (player.isSneaking() && item instanceof ItemBlockMachine machine) {
                if (MachineType.get(toolStack) == MachineType.FLUID_TANK) {
                    boolean newBucketMode = !machine.getBucketMode(toolStack);
                    machine.setBucketMode(toolStack, newBucketMode);
                    Mekanism.packetHandler.sendToServer(new ItemStackMessage(EnumHand.MAIN_HAND, Collections.singletonList(machine.getBucketMode(toolStack))));
                    player.sendMessage(new TextComponentGroup(TextFormatting.GRAY).string(Mekanism.LOG_TAG, TextFormatting.DARK_BLUE).string(" ")
                            .translation("mekanism.tooltip.portableTank.bucketMode", LangUtils.onOffColoured(newBucketMode)));
                }
            } else if (player.isSneaking() && item instanceof ItemWalkieTalkie wt) {
                if (wt.getOn(toolStack)) {
                    int newChan = wt.getChannel(toolStack) + 1;
                    if (newChan == 9) {
                        newChan = 1;
                    }
                    wt.setChannel(toolStack, newChan);
                    Mekanism.packetHandler.sendToServer(new ItemStackMessage(EnumHand.MAIN_HAND, Collections.singletonList(newChan)));
                }
            } else if (player.isSneaking() && item instanceof ItemFlamethrower flamethrower) {
                flamethrower.incrementMode(toolStack);
                Mekanism.packetHandler.sendToServer(FlamethrowerDataMessage.MODE_CHANGE(EnumHand.MAIN_HAND));
                player.sendMessage(new TextComponentGroup(TextFormatting.GRAY).string(Mekanism.LOG_TAG, TextFormatting.DARK_BLUE).string(" ")
                        .translation("mekanism.tooltip.flamethrower.modeBump", flamethrower.getMode(toolStack).getTextComponent()));
            }
        } else if (kb == armorModeSwitchKey) {

            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            Item chestItem = chestStack.getItem();

            if (chestItem instanceof IJetpackItem jetpack) {
                if (player.isSneaking()) {
                    jetpack.setMode(chestStack, JetpackMode.DISABLED);
                } else {
                    jetpack.incrementMode(chestStack);
                }
                Mekanism.packetHandler.sendToServer(JetpackDataMessage.MODE_CHANGE(player.isSneaking()));
                SoundHandler.playSound(MekanismSounds.HYDRAULIC);
            } else if (chestItem instanceof ItemScubaTank scubaTank) {
                scubaTank.toggleFlowing(chestStack);
                Mekanism.packetHandler.sendToServer(ScubaTankDataMessage.MODE_CHANGE(false));
                SoundHandler.playSound(MekanismSounds.HYDRAULIC);
            }
        } else if (kb == freeRunnerModeSwitchKey) {
            ItemStack feetStack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (feetStack.getItem() instanceof ItemFreeRunners freeRunners) {
                if (player.isSneaking()) {
                    freeRunners.setMode(feetStack, ItemFreeRunners.FreeRunnerMode.DISABLED);
                } else {
                    freeRunners.incrementMode(feetStack);
                }
                Mekanism.packetHandler.sendToServer(new FreeRunnerDataMessage(PacketFreeRunnerData.FreeRunnerPacket.MODE, null, player.isSneaking()));
                SoundHandler.playSound(MekanismSounds.HYDRAULIC);
            } else if (feetStack.getItem() instanceof ItemMekAsuitFeetArmour freeArmour && UpgradeHelper.isUpgradeInstalled(feetStack, moduleUpgrade.HYDRAULIC_PROPULSION_UNIT)) {
                if (player.isSneaking()) {
                    freeArmour.setJumpBoostMode(feetStack, ItemMekAsuitFeetArmour.JumpBoost.OFF);
                } else {
                    freeArmour.incrementJumpBoostMode(feetStack);
                }
                Mekanism.packetHandler.sendToServer(new JumpBoostDataMessage(PacketJumpBoostData.JumpBoostPacket.MODE, null, player.isSneaking()));
                SoundHandler.playSound(MekanismSounds.HYDRAULIC);
            }
        } else if (kb == MekAsuitFeetModeSwitchKey) {
            ItemStack feetStack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (feetStack.getItem() instanceof ItemMekAsuitFeetArmour freeArmour && UpgradeHelper.isUpgradeInstalled(feetStack, moduleUpgrade.HYDRAULIC_PROPULSION_UNIT)) {
                if (player.isSneaking()) {
                    freeArmour.setStepAssistMode(feetStack, ItemMekAsuitFeetArmour.StepAssist.OFF);
                } else {
                    freeArmour.incrementStepAssistMode(feetStack);
                }
                Mekanism.packetHandler.sendToServer(new StepAssistDataMessage(PacketStepAssistData.StepAssistPacket.MODE, null, player.isSneaking()));
                SoundHandler.playSound(MekanismSounds.HYDRAULIC);
            }
        } else if (kb == enableHUDkEY) {
            MekanismConfig.current().client.enableHUD.set(!MekanismConfig.current().client.enableHUD.val());
        }
    }

    @Override
    public void keyUp(KeyBinding kb) {
    }
}
