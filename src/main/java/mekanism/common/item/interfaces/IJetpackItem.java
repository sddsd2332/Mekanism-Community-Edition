package mekanism.common.item.interfaces;

import baubles.api.BaublesApi;
import mekanism.api.EnumColor;
import mekanism.api.IIncrementalEnum;
import mekanism.api.math.MathUtils;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.TextComponentGroup;
import mekanism.common.CommonPlayerTickHandler;
import mekanism.common.Mekanism;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public interface IJetpackItem {

    boolean canUseJetpack(ItemStack stack);

    JetpackMode getJetpackMode(ItemStack stack);

    void useJetpackFuel(ItemStack stack);


    enum JetpackMode implements IIncrementalEnum<JetpackMode>, IHasTextComponent {
        NORMAL("tooltip.jetpack.regular", EnumColor.DARK_GREEN, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_HUD, "jetpack_normal.png")),
        HOVER("tooltip.jetpack.hover", EnumColor.DARK_AQUA, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_HUD, "jetpack_hover.png")),
        DISABLED("tooltip.jetpack.disabled", EnumColor.DARK_RED, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_HUD, "jetpack_off.png"));

        private static final JetpackMode[] MODES = values();
        private String unlocalized;
        private EnumColor color;
        private final ResourceLocation hudIcon;

        JetpackMode(String s, EnumColor c, ResourceLocation hudIcon) {
            unlocalized = s;
            color = c;
            this.hudIcon = hudIcon;
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

        @Override
        public ITextComponent getTextComponent() {
            return new TextComponentGroup().string(getName());
        }
    }

    /**
     * Gets the first found active jetpack from an entity, if one is worn.
     * <br>
     * If Curios is loaded, the curio slots will be checked as well.
     *
     * @param entity the entity on which to look for the jetpack
     * @return the jetpack stack if present, otherwise an empty stack
     */
    @NotNull
    static ItemStack getActiveJetpack(EntityLivingBase entity) {
        if (entity.isRiding()) {
            return ItemStack.EMPTY;
        }
        ItemStack jetpack = getJetpack(entity, stack -> stack.getItem() instanceof IJetpackItem jetpackItem && jetpackItem.canUseJetpack(stack));
        if (entity instanceof EntityPlayer player && player.getCooldownTracker().hasCooldown(jetpack.getItem())) {
            return ItemStack.EMPTY;
        }
        return jetpack;
    }


    /**
     * Gets the first found jetpack from an entity, if one is worn. Purpose of this is to get the correct jetpack mode to use.
     * <br>
     * If Curios is loaded, the curio slots will be checked as well.
     *
     * @param entity the entity on which to look for the jetpack
     * @return the jetpack stack if present, otherwise an empty stack
     */
    @NotNull
    static ItemStack getPrimaryJetpack(EntityLivingBase entity) {
        return getJetpack(entity, stack -> stack.getItem() instanceof IJetpackItem);
    }


    static ItemStack getJetpack(EntityLivingBase entity, Predicate<ItemStack> matcher) {
        ItemStack chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (matcher.test(chest)) {
            return chest;
        } else if (Mekanism.hooks.Baubles) {
            return getBaublesJetpack(entity, matcher);
        }
        return ItemStack.EMPTY;
    }

    @Optional.Method(modid = MekanismHooks.Baubles_MOD_ID)
    static ItemStack getBaublesJetpack(EntityLivingBase base, Predicate<ItemStack> matcher) {
        if (base instanceof EntityPlayer player) {
            IItemHandler baubles = BaublesApi.getBaublesHandler(player);
            for (int i = 0; i < baubles.getSlots(); i++) {
                ItemStack stack = baubles.getStackInSlot(i);
                if (matcher.test(stack)) {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * @return If fall distance should get reset or not
     */
    static boolean handleJetpackMotion(EntityPlayer player, JetpackMode mode, BooleanSupplier ascendingSupplier) {
        Vec3d motion = new Vec3d(player.motionX, player.motionY, player.motionZ);
        if (mode == JetpackMode.NORMAL) {
            if (player.isElytraFlying()) {
                Vec3d lookAngle = player.getLookVec();
                Vec3d normalizedLook = lookAngle.normalize();
                double d1x = normalizedLook.x * 0.15;
                double d1y = normalizedLook.y * 0.15;
                double d1z = normalizedLook.z * 0.15;
                player.motionX += lookAngle.x * d1x + (lookAngle.x * 1.5 - motion.x) * 0.5;
                player.motionY += lookAngle.y * d1y + (lookAngle.y * 1.5 - motion.y) * 0.5;
                player.motionZ += lookAngle.z * d1z + (lookAngle.z * 1.5 - motion.z) * 0.5;
                return false;
            } else {
                player.motionY = Math.min(player.motionY + 0.15D, 0.5D);
            }
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


}
