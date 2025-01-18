package mekanism.client;

import baubles.api.BaublesApi;
import mekanism.api.gas.IGasItem;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.MekanismSounds;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.item.armor.ItemMekAsuitFeetArmour;
import mekanism.common.item.interfaces.IModeItem;
import mekanism.common.moduleUpgrade;
import mekanism.common.network.PacketBaublesModeChange.BaublesModeChangMessage;
import mekanism.common.network.PacketJumpBoostData;
import mekanism.common.network.PacketJumpBoostData.JumpBoostDataMessage;
import mekanism.common.network.PacketModeChange.ModeChangMessage;
import mekanism.common.network.PacketStepAssistData;
import mekanism.common.network.PacketStepAssistData.StepAssistDataMessage;
import mekanism.common.util.UpgradeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
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
            if (IModeItem.isModeItem(player, EntityEquipmentSlot.MAINHAND, false)) {
                Mekanism.packetHandler.sendToServer(new ModeChangMessage(EntityEquipmentSlot.MAINHAND, player.isSneaking()));
            } else if (!IModeItem.isModeItem(player, EntityEquipmentSlot.MAINHAND) && IModeItem.isModeItem(player, EntityEquipmentSlot.OFFHAND)) {
                Mekanism.packetHandler.sendToServer(new ModeChangMessage(EntityEquipmentSlot.OFFHAND, player.isSneaking()));
            }
        } else if (kb == armorModeSwitchKey) {
            handlePotentialModeItem(EntityEquipmentSlot.CHEST);
        } else if (kb == freeRunnerModeSwitchKey) {
            handlePotentialModeItem(EntityEquipmentSlot.FEET);
            ItemStack feetStack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (feetStack.getItem() instanceof ItemMekAsuitFeetArmour freeArmour && UpgradeHelper.isUpgradeInstalled(feetStack, moduleUpgrade.HYDRAULIC_PROPULSION_UNIT) && !Mekanism.hooks.DraconicEvolution) {
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

    private static void handlePotentialModeItem(EntityEquipmentSlot slot) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            if (IModeItem.isModeItem(player, slot)) {
                Mekanism.packetHandler.sendToServer(new ModeChangMessage(slot, player.isSneaking()));
                SoundHandler.playSound(MekanismSounds.HYDRAULIC);
            } else if (Mekanism.hooks.Baubles) {
                setBaublesMode(player, slot);
            }
        }
    }


    @Optional.Method(modid = MekanismHooks.Baubles_MOD_ID)
    public static void setBaublesMode(EntityPlayer player, EntityEquipmentSlot slot) {
        IItemHandler baubles = BaublesApi.getBaublesHandler(player);
        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            if (stack.getItem().isValidArmor(stack, slot, player) && IModeItem.isModeItem(stack, slot)) {
                if (!(stack.getItem() instanceof IGasItem item) || item.getGas(stack) != null) {
                    Mekanism.packetHandler.sendToServer(new BaublesModeChangMessage(i, player.isSneaking()));
                    SoundHandler.playSound(MekanismSounds.HYDRAULIC);
                }
            }
        }
    }


    @Override
    public void keyUp(KeyBinding kb) {

    }
}
