package mekanism.client.render.hud;

import baubles.api.BaublesApi;
import mekanism.client.render.HUDRenderer;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.item.armor.ItemMekaSuitHelmet;
import mekanism.common.item.interfaces.IItemHUDProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
public class MekanismHUD {


    private static final EntityEquipmentSlot[] EQUIPMENT_ORDER = {EntityEquipmentSlot.OFFHAND, EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS,
            EntityEquipmentSlot.FEET};

    private static final HUDRenderer hudRenderer = new HUDRenderer();

    public static void onDrawScreenPre(RenderGameOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            EntityPlayer player = minecraft.player;
            if (!minecraft.gameSettings.hideGUI && player != null && !player.isSpectator() && MekanismConfig.current().client.enableHUD.val()) {
                int count = 0;
                List<List<String>> renderStrings = new ArrayList<>();
                for (EntityEquipmentSlot slotType : EQUIPMENT_ORDER) {
                    ItemStack stack = player.getItemStackFromSlot(slotType);
                    if (stack.getItem() instanceof IItemHUDProvider hudProvider) {
                        count += makeComponent(list -> hudProvider.addHUDStrings(list, player, stack, slotType), renderStrings);
                    }
                }
                if (Mekanism.hooks.Baubles) {
                    count += Baubles(player, renderStrings);
                }
                FontRenderer font = minecraft.fontRenderer;
                boolean reverseHud = !MekanismConfig.current().client.alignHUDLeft.val();
                int maxTextHeight = event.getResolution().getScaledHeight();
                if (count > 0) {
                    float hudScale = MekanismConfig.current().client.hudScale.val();
                    int xScale = (int) (event.getResolution().getScaledWidth() / hudScale);
                    int yScale = (int) (event.getResolution().getScaledHeight() / hudScale);
                    int start = (renderStrings.size() * 2) + (count * 9);
                    int y = yScale - start;
                    maxTextHeight = (int) (y * hudScale);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(hudScale, hudScale, hudScale);

                    for (List<String> group : renderStrings) {
                        for (String text : group) {
                            int textWidth = font.getStringWidth(text);
                            //Align text to right if hud is reversed, otherwise align to the left
                            //Note: that we always offset by 2 pixels from the edge of the screen regardless of how it is aligned
                            int x = reverseHud ? xScale - textWidth - 2 : 2;
                            font.drawStringWithShadow(text, MekanismConfig.current().client.hudX.val() + x, MekanismConfig.current().client.hudY.val() + y, 0xFFC8C8C8);
                            y += 9;
                        }
                        y += 2;
                    }
                    GlStateManager.popMatrix();
                }

                if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemMekaSuitHelmet) {
                    hudRenderer.renderHUD(minecraft, font, event.getPartialTicks(), event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight(), maxTextHeight, reverseHud);
                    minecraft.renderEngine.bindTexture(Gui.ICONS);
                }
                MekanismRenderer.resetColor();
            }
        }
    }


    @Optional.Method(modid = MekanismHooks.Baubles_MOD_ID)
    public static int Baubles(EntityPlayer player, List<List<String>> renderStrings) {
        IItemHandler baubles = BaublesApi.getBaublesHandler(player);
        for (int i = 0, slots = baubles.getSlots(); i < slots; i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            if (stack.getItem() instanceof IItemHUDProvider hudProvider) {
                return makeComponent(list -> hudProvider.addBaublesHUDStrings(list, player, stack), renderStrings);
            }
        }
        return 0;
    }


    private static int makeComponent(Consumer<List<String>> adder, List<List<String>> initial) {
        List<String> list = new ArrayList<>();
        adder.accept(list);
        int size = list.size();
        if (size > 0) {
            initial.add(list);
        }
        return size;
    }
}
