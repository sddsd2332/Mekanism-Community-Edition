package mekanism.common.item.interfaces;

import mekanism.api.EnumColor;
import mekanism.api.IIncrementalEnum;
import mekanism.api.NBTConstants;
import mekanism.api.math.MathUtils;
import mekanism.common.CommonPlayerTickHandler;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public interface IJetpackItem {

    @NotNull
    static ItemStack getActiveJetpack(EntityLivingBase entity) {
        return getJetpack(entity, stack -> {
            if (stack.getItem() instanceof IJetpackItem jetpackItem && jetpackItem.canUseJetpack(stack)) {
                return !(entity instanceof EntityPlayer player) || !player.getCooldownTracker().hasCooldown(stack.getItem());
            }
            return false;
        });
    }

    @NotNull
    static ItemStack getPrimaryJetpack(EntityLivingBase entity) {
        return getJetpack(entity, stack -> stack.getItem() instanceof IJetpackItem);
    }

    static ItemStack getJetpack(EntityLivingBase entity, Predicate<ItemStack> matcher) {
        ItemStack chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (matcher.test(chest)) {
            return chest;
        }
        return ItemStack.EMPTY;
    }

    static boolean handleJetpackMotion(EntityPlayer player, JetpackMode mode, BooleanSupplier ascendingSupplier) {
        if (mode == JetpackMode.NORMAL) {
            player.motionY = Math.min(player.motionY + 0.15D, 0.5D);
        } else if (mode == JetpackMode.HOVER) {
            boolean ascending = ascendingSupplier.getAsBoolean();
            boolean descending = player.isSneaking();
            if (ascending == descending) {
                if (player.motionY > 0) {
                    player.motionY = Math.max(player.motionY - 0.15D, 0);
                } else if (player.motionY < 0) {
                    if (!CommonPlayerTickHandler.isOnGroundOrSleeping(player)) {
                        player.motionY = Math.min(player.motionY + 0.15D, 0);
                    }
                }
            } else if (ascending) {
                player.motionY = Math.min(player.motionY + 0.15D, 0.2D);
            } else if (!CommonPlayerTickHandler.isOnGroundOrSleeping(player)) {
                player.motionY = Math.max(player.motionY - 0.15D, -0.2D);
            }
        }
        return true;
    }


    static JetpackMode getPlayerJetpackMode(EntityPlayer player, JetpackMode mode, BooleanSupplier ascendingSupplier) {
        if (!player.isSpectator()) {
            if (mode != JetpackMode.DISABLED) {
                boolean ascending = ascendingSupplier.getAsBoolean();
                if (mode == JetpackMode.HOVER) {
                    if (ascending && !player.isSneaking() || !CommonPlayerTickHandler.isOnGroundOrSleeping(player)) {
                        return mode;
                    }
                } else if (mode == JetpackMode.NORMAL && ascending) {
                    return mode;
                }
            }
        }
        return JetpackMode.DISABLED;
    }

    boolean canUseJetpack(ItemStack stack);


    default JetpackMode getJetpackMode(ItemStack stack) {
        return JetpackMode.byIndexStatic(ItemDataUtils.getInt(stack, NBTConstants.MODE));
    }

    void useJetpackFuel(ItemStack stack);


    default void setMode(ItemStack stack, JetpackMode mode) {
        ItemDataUtils.setInt(stack, NBTConstants.MODE, mode.ordinal());
    }

    default void incrementMode(ItemStack stack) {
        setMode(stack, getJetpackMode(stack).increment());
    }

    int getStored(ItemStack itemstack);

    enum JetpackMode implements IIncrementalEnum<JetpackMode> {
        NORMAL("tooltip.jetpack.regular", EnumColor.DARK_GREEN, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_HUD, "jetpack_normal.png")),
        HOVER("tooltip.jetpack.hover", EnumColor.DARK_AQUA, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_HUD, "jetpack_hover.png")),
        DISABLED("tooltip.jetpack.disabled",EnumColor.DARK_RED, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_HUD, "jetpack_off.png"));

        private static final JetpackMode[] MODES = values();
        private String unlocalized;
        private EnumColor color;
        private final ResourceLocation hudIcon;

        JetpackMode(String s, EnumColor c,ResourceLocation hudIcon) {
            unlocalized = s;
            color = c;
            this.hudIcon = hudIcon;
        }

        public JetpackMode increment() {
            return ordinal() < values().length - 1 ? values()[ordinal() + 1] : values()[0];
        }

        public String getName() {
            return color + LangUtils.localize(unlocalized);
        }

        @Override
        public JetpackMode byIndex(int index) {
            return byIndexStatic(index);
        }

        public static JetpackMode byIndexStatic(int index) {
            return MathUtils.getByIndexMod(MODES, index);
        }

        public ResourceLocation getHUDIcon() {
            return hudIcon;
        }
    }
}
