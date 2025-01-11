package mekanism.client;

import baubles.api.BaublesApi;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.MekanismSounds;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.item.*;
import mekanism.common.item.armor.ItemMekAsuitFeetArmour;
import mekanism.common.item.interfaces.IJetpackItem;
import mekanism.common.item.interfaces.IJetpackItem.JetpackMode;
import mekanism.common.item.interfaces.IModeItem;
import mekanism.common.moduleUpgrade;
import mekanism.common.network.PacketFlamethrowerData.FlamethrowerDataMessage;
import mekanism.common.network.PacketFreeRunnerData;
import mekanism.common.network.PacketFreeRunnerData.FreeRunnerDataMessage;
import mekanism.common.network.PacketItemStack.ItemStackMessage;
import mekanism.common.network.PacketJetpackData.JetpackDataMessage;
import mekanism.common.network.PacketJumpBoostData;
import mekanism.common.network.PacketJumpBoostData.JumpBoostDataMessage;
import mekanism.common.network.PacketModeChange.ModeChangMessage;
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
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
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

            if (IModeItem.isModeItem(player, EntityEquipmentSlot.MAINHAND, false)) {
                Mekanism.packetHandler.sendToServer(new ModeChangMessage(EntityEquipmentSlot.MAINHAND, player.isSneaking()));
            } else if (!IModeItem.isModeItem(player, EntityEquipmentSlot.MAINHAND) && IModeItem.isModeItem(player, EntityEquipmentSlot.OFFHAND)) {
                Mekanism.packetHandler.sendToServer(new ModeChangMessage(EntityEquipmentSlot.OFFHAND, player.isSneaking()));
            }

            if (player.isSneaking()) {
                /*
                if (item instanceof ItemConfigurator configurator) {
                    ConfiguratorMode mode = configurator.getMode(toolStack);
                    ConfiguratorMode newMode = mode.adjust(player.isSneaking() ? -1 : 1);
                    configurator.setMode(toolStack, newMode);
                    Mekanism.packetHandler.sendToServer(new ItemStackMessage(EnumHand.MAIN_HAND, Collections.singletonList(newMode)));
                    player.sendMessage(new TextComponentGroup(TextFormatting.GRAY).string(Mekanism.LOG_TAG, TextFormatting.DARK_BLUE).string(" ").translation("mekanism.tooltip.configureState", LangUtils.withColor(newMode.getShortText(), newMode.getColor().textFormatting)));

                }*/
                if (item instanceof ItemBlockMachine machine) {
                    if (MachineType.get(toolStack) == MachineType.FLUID_TANK) {
                        boolean newBucketMode = !machine.getBucketMode(toolStack);
                        machine.setBucketMode(toolStack, newBucketMode);
                        Mekanism.packetHandler.sendToServer(new ItemStackMessage(EnumHand.MAIN_HAND, Collections.singletonList(machine.getBucketMode(toolStack))));
                        player.sendMessage(new TextComponentGroup(TextFormatting.GRAY).string(Mekanism.LOG_TAG, TextFormatting.DARK_BLUE).string(" ").translation("mekanism.tooltip.portableTank.bucketMode", LangUtils.onOffColoured(newBucketMode)));
                    }
                } else if (item instanceof ItemWalkieTalkie wt) {
                    if (wt.getOn(toolStack)) {
                        int newChan = wt.getChannel(toolStack) + 1;
                        if (newChan == 9) {
                            newChan = 1;
                        }
                        wt.setChannel(toolStack, newChan);
                        Mekanism.packetHandler.sendToServer(new ItemStackMessage(EnumHand.MAIN_HAND, Collections.singletonList(newChan)));
                    }
                } else if (item instanceof ItemFlamethrower flamethrower) {
                    flamethrower.incrementMode(toolStack);
                    Mekanism.packetHandler.sendToServer(FlamethrowerDataMessage.MODE_CHANGE(EnumHand.MAIN_HAND));
                    player.sendMessage(new TextComponentGroup(TextFormatting.GRAY).string(Mekanism.LOG_TAG, TextFormatting.DARK_BLUE).string(" ").translation("mekanism.tooltip.flamethrower.modeBump", flamethrower.getMode(toolStack).getTextComponent()));

                }
            }
        } else if (kb == armorModeSwitchKey) {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            Item chestItem = chestStack.getItem();
            if (chestItem instanceof IJetpackItem jetpack) {
                JetpackMode mode = jetpack.getJetpackMode(chestStack);
                JetpackMode newMode = mode.adjust(player.isSneaking() ? -1 : 1);
                jetpack.setMode(chestStack, newMode);
                Mekanism.packetHandler.sendToServer(JetpackDataMessage.MODE_CHANGE(player.isSneaking()));
                SoundHandler.playSound(MekanismSounds.HYDRAULIC);
            } else if (Mekanism.hooks.Baubles) {
                setBaublesJetpackMode(player);
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
            } else if (feetStack.getItem() instanceof ItemMekAsuitFeetArmour freeArmour && UpgradeHelper.isUpgradeInstalled(feetStack, moduleUpgrade.HYDRAULIC_PROPULSION_UNIT) && !Mekanism.hooks.DraconicEvolution) {
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
            if (feetStack.getItem() instanceof ItemMekAsuitFeetArmour freeArmour && UpgradeHelper.isUpgradeInstalled(feetStack, moduleUpgrade.HYDRAULIC_PROPULSION_UNIT) && !Mekanism.hooks.DraconicEvolution) {
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

    @Optional.Method(modid = MekanismHooks.Baubles_MOD_ID)
    public void setBaublesJetpackMode(EntityPlayer player) {
        IItemHandler baubles = BaublesApi.getBaublesHandler(player);
        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            if (stack.getItem() instanceof IJetpackItem jetpack) {
                JetpackMode mode = jetpack.getJetpackMode(stack);
                JetpackMode newMode = mode.adjust(player.isSneaking() ? -1 : 1);
                jetpack.setMode(stack, newMode);
                Mekanism.packetHandler.sendToServer(JetpackDataMessage.MODE_CHANGE(player.isSneaking()));
                SoundHandler.playSound(MekanismSounds.HYDRAULIC);
            }
        }
    }


    @Override
    public void keyUp(KeyBinding kb) {
    }
}
