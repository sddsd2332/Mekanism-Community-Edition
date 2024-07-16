package mekanism.common;

import com.github.bsideup.jabel.Desugar;
import mekanism.api.gas.GasStack;
import mekanism.common.entity.EntityFlame;
import mekanism.common.item.ItemFlamethrower;
import mekanism.common.item.ItemFreeRunners;
import mekanism.common.item.ItemGasMask;
import mekanism.common.item.ItemScubaTank;
import mekanism.common.item.armor.ItemMekAsuitHeadArmour;
import mekanism.common.item.armor.ItemMekaSuitArmor;
import mekanism.common.item.interfaces.IJetpackItem;
import mekanism.common.item.interfaces.IJetpackItem.JetpackMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.Nullable;

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


    @SubscribeEvent
    public void onEntityAttacked(LivingAttackEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (event.getAmount() <= 0 || !entity.isEntityAlive()) {
            //If some mod does weird things and causes the damage value to be negative or zero then exit
            // as our logic assumes there is actually damage happening and can crash if someone tries to
            // use a negative number as the damage value. We also check to make sure that we don't do
            // anything if the entity is dead as living attack is still fired when the entity is dead
            // for things like fall damage if the entity dies before hitting the ground, and then energy
            // would be depleted regardless if keep inventory is on even if no damage was stopped as the
            // entity can't take damage while dead
            return;
        }
        //Gas Mask checks
        if (event.getSource().isMagicDamage()) {
            ItemStack headStack = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (!headStack.isEmpty() && headStack.getItem() instanceof ItemGasMask) {
                ItemStack chestStack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (!chestStack.isEmpty() && chestStack.getItem() instanceof ItemScubaTank tank && tank.getFlowing(chestStack) && tank.getGas(chestStack) != null) {
                    event.setCanceled(true);
                    return;
                }
            }
        }
        //Note: We have this here in addition to listening to LivingHurt, so as if we can fully block the damage
        // then we don't play the hurt effect/sound, as cancelling LivingHurtEvent still causes that to happen
        if (event.getSource() == DamageSource.FALL) {
            //Free runner checks
            FallEnergyInfo info = getFallAbsorptionEnergyInfo(entity);
            if (info != null && tryAbsorbAll(event, entity.getItemStackFromSlot(EntityEquipmentSlot.FEET), info.damageRatio, info.energyCost)) {
                return;
            }
        }
        if (entity instanceof EntityPlayer player) {
            if (ItemMekaSuitArmor.tryAbsorbAll(player, event.getSource(), event.getAmount())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (event.getAmount() <= 0 || !entity.isEntityAlive()) {
            //If some mod does weird things and causes the damage value to be negative or zero then exit
            // as our logic assumes there is actually damage happening and can crash if someone tries to
            // use a negative number as the damage value. We also check to make sure that we don't do
            // anything if the entity is dead as living attack is still fired when the entity is dead
            // for things like fall damage if the entity dies before hitting the ground, and then energy
            // would be depleted regardless if keep inventory is on even if no damage was stopped as the
            // entity can't take damage while dead. While living hurt is not fired, we catch this case
            // just in case anyway because it is a simple boolean check and there is no guarantee that
            // other mods may not be firing the event manually even when the entity is dead
            return;
        }
        if (event.getSource() == DamageSource.FALL) {
            FallEnergyInfo info = getFallAbsorptionEnergyInfo(entity);
            if (info != null && handleDamage(event, entity.getItemStackFromSlot(EntityEquipmentSlot.FEET), info.damageRatio, info.energyCost)) {
                return;
            }
        }

        if (entity instanceof EntityPlayer player) {
            float ratioAbsorbed = ItemMekaSuitArmor.getDamageAbsorbed(player, event.getSource(), event.getAmount());
            if (ratioAbsorbed > 0) {
                float damageRemaining = event.getAmount() * Math.max(0, 1 - ratioAbsorbed);
                if (damageRemaining <= 0) {
                    event.setCanceled(true);
                } else {
                    event.setAmount(damageRemaining);
                }
            }
        }
    }


    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (event.getAmount() <= 0 || !entity.isEntityAlive()) {
            return;
        }
        if (entity instanceof EntityPlayer player) {
            float ratioAbsorbed = ItemMekaSuitArmor.getDamageAbsorbed(player, event.getSource(), event.getAmount());
            if (ratioAbsorbed > 0) {
                float damageRemaining = event.getAmount() * Math.max(0, 1 - ratioAbsorbed);
                if (damageRemaining <= 0) {
                    event.setCanceled(true);
                } else {
                    event.setAmount(damageRemaining);
                }
            }
        }
    }

    private boolean handleDamage(LivingHurtEvent event, ItemStack energyContainer, float absorptionRatio, float energyCost) {
        if (!energyContainer.isEmpty()) {
            float absorption = absorptionRatio;
            float amount = event.getAmount() * absorption;
            float energyRequirement = energyCost * amount;
            float ratioAbsorbed = 0;
            if (energyRequirement == 0) {
                ratioAbsorbed = absorption;
            } else {
                if (energyContainer.getItem() instanceof ItemFreeRunners boots) {
                    ratioAbsorbed = (float) (absorption * ((boots.getEnergy(energyContainer) - energyRequirement) / amount));
                    boots.setEnergy(energyContainer, boots.getEnergy(energyContainer) - ((boots.getEnergy(energyContainer) - energyRequirement) / amount));
                } else if (energyContainer.getItem() instanceof ItemMekaSuitArmor armor) {
                    ratioAbsorbed = (float) (absorption * ((armor.getEnergy(energyContainer) - energyRequirement) / amount));
                    armor.setEnergy(energyContainer, armor.getEnergy(energyContainer) - ((armor.getEnergy(energyContainer) - energyRequirement) / amount));
                }
            }
            if (ratioAbsorbed > 0) {
                float damageRemaining = event.getAmount() * Math.max(0, 1 - ratioAbsorbed);
                if (damageRemaining <= 0) {
                    event.setCanceled(true);
                    return true;
                } else {
                    event.setAmount(damageRemaining);
                }
            }
        }
        return false;
    }

    @Nullable
    private FallEnergyInfo getFallAbsorptionEnergyInfo(EntityLivingBase base) {
        ItemStack feetStack = base.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (!feetStack.isEmpty()) {
            if (feetStack.getItem() instanceof ItemFreeRunners boots) {
                if (boots.getMode(feetStack).preventsFallDamage()) {
                    return new FallEnergyInfo(feetStack, 1, 50);
                }
            } else if (feetStack.getItem() instanceof ItemMekaSuitArmor) {
                return new FallEnergyInfo(feetStack, 1, 50);
            }
        }
        return null;
    }

    private boolean tryAbsorbAll(LivingAttackEvent event, ItemStack stack, float absorptionRatio, float energyCost) {
        if (!stack.isEmpty() && absorptionRatio == 1) {
            double energyRequirement = energyCost * event.getAmount();
            if (energyRequirement == 0) {
                event.setCanceled(true);
                return true;
            }
            if (stack.getItem() instanceof ItemFreeRunners boot) {
                if (boot.getEnergy(stack) > energyRequirement) {
                    boot.setEnergy(stack, boot.getEnergy(stack) - energyRequirement);
                    event.setCanceled(true);
                    return true;
                }
            } else if (stack.getItem() instanceof ItemMekaSuitArmor meka) {
                if (meka.getEnergy(stack) > energyRequirement) {
                    meka.setEnergy(stack, meka.getEnergy(stack) - energyRequirement);
                    event.setCanceled(true);
                    return true;
                }
            }


        }
        return false;
    }

    @Desugar
    private record FallEnergyInfo(ItemStack stack, float damageRatio, float energyCost) {
    }
}
