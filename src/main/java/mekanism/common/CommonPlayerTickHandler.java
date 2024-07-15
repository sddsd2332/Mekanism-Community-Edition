package mekanism.common;

import mekanism.api.gas.GasStack;
import mekanism.common.entity.EntityFlame;
import mekanism.common.item.ItemFlamethrower;
import mekanism.common.item.ItemFreeRunners;
import mekanism.common.item.ItemGasMask;
import mekanism.common.item.ItemScubaTank;
import mekanism.common.item.armour.ItemMekAsuitHeadArmour;
import mekanism.common.item.interfaces.IJetpackItem;
import mekanism.common.item.interfaces.IJetpackItem.JetpackMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class CommonPlayerTickHandler {

    boolean isHeadItem = false;

    public static boolean isOnGround(EntityPlayer player) {
        int x = MathHelper.floor(player.posX);
        int y = MathHelper.floor(player.posY - 0.01);
        int z = MathHelper.floor(player.posZ);
        BlockPos pos = new BlockPos(x, y, z);
        IBlockState s = player.world.getBlockState(pos);
        AxisAlignedBB box = s.getBoundingBox(player.world, pos).offset(pos);
        AxisAlignedBB playerBox = player.getEntityBoundingBox();
        return !s.getBlock().isAir(s, player.world, pos) && playerBox.offset(0, -0.01, 0).intersects(box);

    }

    public static boolean isGasMaskOn(EntityPlayer player) {
        ItemStack tank = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack mask = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (!tank.isEmpty() && !mask.isEmpty()) {
            if (tank.getItem() instanceof ItemScubaTank scubaTank && mask.getItem() instanceof ItemGasMask) {
                if (scubaTank.getGas(tank) != null) {
                    return scubaTank.getFlowing(tank);
                }
            }
        }
        return false;
    }

    public static boolean isFlamethrowerOn(EntityPlayer player) {
        if (Mekanism.playerState.isFlamethrowerOn(player)) {
            ItemStack currentItem = player.inventory.getCurrentItem();
            return !currentItem.isEmpty() && currentItem.getItem() instanceof ItemFlamethrower;
        }
        return false;
    }

    @SubscribeEvent
    public void onTick(PlayerTickEvent event) {
        if (event.phase == Phase.END && event.side == Side.SERVER) {
            tickEnd(event.player);
        }
    }

    public void tickEnd(EntityPlayer player) {
        ItemStack feetStack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (!feetStack.isEmpty() && feetStack.getItem() instanceof ItemFreeRunners && !player.isSneaking()) {
            player.stepHeight = 1.002F;
        } else if (player.stepHeight == 1.002F) {
            player.stepHeight = 0.6F;
        }

        if (isFlamethrowerOn(player)) {
            player.world.spawnEntity(new EntityFlame(player));
            if (!player.isCreative() && !player.isSpectator()) {
                ItemStack currentItem = player.inventory.getCurrentItem();
                ((ItemFlamethrower) currentItem.getItem()).useGas(currentItem);
            }
        }

        ItemStack jetpack = IJetpackItem.getActiveJetpack(player);
        if (!jetpack.isEmpty()) {
            ItemStack primaryJetpack = IJetpackItem.getPrimaryJetpack(player);
            if (!primaryJetpack.isEmpty()) {
                JetpackMode primaryMode = ((IJetpackItem) primaryJetpack.getItem()).getJetpackMode(primaryJetpack);
                JetpackMode mode = IJetpackItem.getPlayerJetpackMode(player, primaryMode, () -> Mekanism.keyMap.has(player, KeySync.ASCEND));
                if (mode != JetpackMode.DISABLED) {
                    if (IJetpackItem.handleJetpackMotion(player, mode, () -> Mekanism.keyMap.has(player, KeySync.ASCEND))) {
                        player.fallDistance = 0.0F;
                        if (player instanceof EntityPlayerMP serverPlayer) {
                            serverPlayer.connection.floatingTickCount = 0;
                        }
                    }
                    ((IJetpackItem) jetpack.getItem()).useJetpackFuel(jetpack);
                }
            }
        }


        if (isGasMaskOn(player)) {
            ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            ItemScubaTank tank = (ItemScubaTank) stack.getItem();
            final int max = 300;
            tank.useGas(stack);
            GasStack received = tank.useGas(stack, max - player.getAir());
            if (received != null) {
                player.setAir(player.getAir() + received.amount);
            }
            if (player.getAir() == max) {
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    for (int i = 0; i < 9; i++) {
                        effect.onUpdate(player);
                    }
                }
            }
        }

        isMekAsuitArmor(player);
    }

    public void isMekAsuitArmor(EntityPlayer player) {
        ItemStack head = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack legs = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        PotionEffect nv = player.getActivePotionEffect(MobEffects.NIGHT_VISION);
        if (!head.isEmpty()) {
            if (head.getItem() instanceof ItemMekAsuitHeadArmour headArmour) {
                //if()


                isHeadItem = true;
            } else {
                isHeadItem = false;
            }
        }

        if (nv != null && isHeadItem) {
            nv.duration = 0;
        }

    }
}
