package mekanism.common;

import com.github.bsideup.jabel.Desugar;
import mekanism.api.gas.GasStack;
import mekanism.common.config.MekanismConfig;
import mekanism.common.entity.EntityFlame;
import mekanism.common.item.ItemFlamethrower;
import mekanism.common.item.ItemFreeRunners;
import mekanism.common.item.ItemGasMask;
import mekanism.common.item.ItemScubaTank;
import mekanism.common.item.armor.*;
import mekanism.common.item.interfaces.IJetpackItem;
import mekanism.common.item.interfaces.IJetpackItem.JetpackMode;
import mekanism.common.util.TextComponentGroup;
import mekanism.common.util.UpgradeHelper;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommonPlayerTickHandler {

    public static final List<UUID> FLYING_PLAYERS = new ArrayList<>();
    boolean isHeadItem = false;

    public static boolean isOnGroundOrSleeping(EntityPlayer player) {
        return player.onGround || player.isSneaking();
        /*
        int x = MathHelper.floor(player.posX);
        int y = MathHelper.floor(player.posY - 0.01);
        int z = MathHelper.floor(player.posZ);
        BlockPos pos = new BlockPos(x, y, z);
        IBlockState s = player.world.getBlockState(pos);
        AxisAlignedBB box = s.getBoundingBox(player.world, pos).offset(pos);
        AxisAlignedBB playerBox = player.getEntityBoundingBox();
        return !s.getBlock().isAir(s, player.world, pos) && playerBox.offset(0, -0.01, 0).intersects(box);
         */
    }

    public static boolean isScubaMaskOn(EntityPlayer player, ItemStack tank) {
        ItemStack mask = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        return !tank.isEmpty() && !mask.isEmpty() && tank.getItem() instanceof ItemScubaTank scubaTank &&
                mask.getItem() instanceof ItemGasMask && scubaTank.getGas(tank) != null && scubaTank.getFlowing(tank);
    }

    public static boolean isFlamethrowerOn(EntityPlayer player, ItemStack currentItem) {
        return Mekanism.playerState.isFlamethrowerOn(player) && !currentItem.isEmpty() && currentItem.getItem() instanceof ItemFlamethrower;
    }

    public static float getStepBoost(EntityPlayer player) {
        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (!stack.isEmpty() && stack.getItem() instanceof ItemFreeRunners freeRunners && freeRunners.getMode(stack).providesStepBoost() && !player.isSpectator()) {
            return 1.002F;
        } else if (!stack.isEmpty() && stack.getItem() instanceof ItemMekAsuitFeetArmour feetArmour && !player.isSpectator()) {
            if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.HYDRAULIC_PROPULSION_UNIT) && !Mekanism.hooks.DraconicEvolution) {
                float height = feetArmour.getStepAssistMode(stack).getHeight();
                if (height == 0.5F) {
                    return 1.002F;
                } else if (height == 1) {
                    return 1.6F;
                } else if (height == 1.5F) {
                    return 2.002F;
                } else if (height == 2) {
                    return 2.6F;
                }
            }
        } else if (player.stepHeight >= 1F) {
            return 0.6F;
        }
        return 0.6F;
    }

    @SubscribeEvent
    public static void playerLoggedOut(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {
        removeFlyingPlayer(event.player.getUniqueID());
    }

    private static void removeFlyingPlayer(UUID playerUUID) {
        FLYING_PLAYERS.removeIf(uuid -> uuid.equals(playerUUID));
    }

    @SubscribeEvent
    public void onTick(PlayerTickEvent event) {
        if (event.phase == Phase.END && event.side == Side.SERVER) {
            tickEnd(event.player);
        }
        if (event.phase == Phase.START) {
            if (!Mekanism.hooks.DraconicEvolution){
                isMekAsuitArmorFlying(event.player);
            }
        }
    }

    public void tickEnd(EntityPlayer player) {
        player.stepHeight = getStepBoost(player);

        ItemStack currentItem = player.inventory.getCurrentItem();
        if (isFlamethrowerOn(player, currentItem)) {
            player.world.spawnEntity(new EntityFlame(player));
            if (!player.isCreative() && !player.isSpectator()) {
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

        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (isScubaMaskOn(player, chest)) {
            ItemScubaTank tank = (ItemScubaTank) chest.getItem();
            final int max = 300;
            tank.useGas(chest, 1);
            GasStack received = tank.useGas(chest, max - player.getAir());
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

    }


    public void isMekAsuitArmorFlying(EntityPlayer player) {
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        PlayerCapabilities capabilities = player.capabilities;
        UUID playerUUID = player.getUniqueID();
        if (!chest.isEmpty() && chest.getItem() instanceof ItemMekAsuitBodyArmour && UpgradeHelper.isUpgradeInstalled(chest, moduleUpgrade.GRAVITATIONAL_MODULATING_UNIT)) {
            if (!capabilities.allowFlying) {
                capabilities.allowFlying = true;
                player.sendPlayerAbilities();
                if (!FLYING_PLAYERS.contains(playerUUID))
                    FLYING_PLAYERS.add(playerUUID);
            }
        } else {
            if (FLYING_PLAYERS.contains(playerUUID)) {
                if (capabilities.allowFlying && !player.isSpectator() && !player.isCreative()) {
                    capabilities.allowFlying = false;
                    capabilities.isFlying = false;
                    player.sendPlayerAbilities();
                }
                removeFlyingPlayer(playerUUID);
            }
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
                return new FallEnergyInfo(feetStack, MekanismConfig.current().meka.mekaSuitFallDamageRatio.val(), MekanismConfig.current().meka.mekaSuitEnergyUsageFall.val());
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

    @SubscribeEvent
    public void onLivingJump(LivingJumpEvent event) {
        if (event.getEntity() instanceof EntityPlayer player) {
            ItemStack feet = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (!feet.isEmpty() && feet.getItem() instanceof ItemMekAsuitFeetArmour feetArmor) {
                if (UpgradeHelper.isUpgradeInstalled(feet, moduleUpgrade.HYDRAULIC_PROPULSION_UNIT) && !Mekanism.hooks.DraconicEvolution) {
                    float boost = feetArmor.getJumpBoostMode(feet).getBoost();
                    double usage = MekanismConfig.current().meka.mekaSuitBaseJumpEnergyUsage.val() * (boost / 0.1F);
                    if (feetArmor.getEnergy(feet) > usage) {
                        ItemStack leg = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
                        if (!leg.isEmpty() && leg.getItem() instanceof ItemMekAsuitLegsArmour armour) {
                            if (UpgradeHelper.isUpgradeInstalled(leg, moduleUpgrade.LOCOMOTIVE_BOOSTING_UNIT) && armour.getLocomotive(leg)) {
                                boost = (float) Math.sqrt(boost);
                            }
                        }
                        player.motionY += boost;
                        feetArmor.setEnergy(feet, feetArmor.getEnergy(feet) - usage);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void getBreakSpeed(PlayerEvent.BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        float speed = event.getNewSpeed();
       // BlockPos position = event.getPos();
        /*
        if (position!=null){
            BlockPos pos = position;
            ItemStack mainHand = player.getHeldItemMainhand();

        }
         */
        ItemStack legs = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        if (!legs.isEmpty() && legs.getItem() instanceof ItemMekAsuitLegsArmour armour && UpgradeHelper.isUpgradeInstalled(legs, moduleUpgrade.GYROSCOPIC_STABILIZATION_UNIT) && armour.getGyroscopic(legs)) {
            if (player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(player)) {
                speed *= 5.0F;
            }
            if (!player.onGround) {
                speed *= 5.0F;
            }
        }
        event.setNewSpeed(speed);
    }

    //When the player dies
    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer player) {
            ItemStack head = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (!head.isEmpty() && head.getItem() instanceof ItemMekAsuitHeadArmour armour &&
                    ((UpgradeHelper.isUpgradeInstalled(head, moduleUpgrade.EMERGENCY_RESCUE) && armour.getEmergency(head)) ||
                            (UpgradeHelper.isUpgradeInstalled(head, moduleUpgrade.ADVANCED_INTERCEPTION_SYSTEM_UNIT) && armour.getInterception(head)))) {
                event.setCanceled(true);
                if (!UpgradeHelper.isUpgradeInstalled(head, moduleUpgrade.ADVANCED_INTERCEPTION_SYSTEM_UNIT)) {
                    int installed = UpgradeHelper.getUpgradeLevel(head, moduleUpgrade.EMERGENCY_RESCUE);
                    int toAdd = Math.max(installed - 1, 0);
                    UpgradeHelper.setUpgradeLevel(head, moduleUpgrade.EMERGENCY_RESCUE, toAdd);
                }
                player.setHealth(5F);
                player.clearActivePotions();
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 800, 2));
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 2));
                player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 2));
                player.setAir(300);
                player.getFoodStats().addStats(20, 20);
                player.sendMessage(new TextComponentGroup(TextFormatting.GRAY).string("[", TextFormatting.RED).translation("item.module.emergency_rescue.name", TextFormatting.RED).string("]", TextFormatting.RED).string(":").translation("module.emergency_rescue.use", TextFormatting.YELLOW));
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        //If the player is affected by setHealth
        //What? Why do you want to go straight to setHealth?
        if (MekanismConfig.current().mekce.MekAsuitOverloadProtection.val()) {
            if (event.getEntityLiving() instanceof EntityPlayer player) {
                ItemStack head = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (!player.isEntityAlive() && !head.isEmpty() && head.getItem() instanceof ItemMekAsuitHeadArmour armour &&
                        ((UpgradeHelper.isUpgradeInstalled(head, moduleUpgrade.EMERGENCY_RESCUE) && armour.getEmergency(head)) ||
                                (UpgradeHelper.isUpgradeInstalled(head, moduleUpgrade.ADVANCED_INTERCEPTION_SYSTEM_UNIT) && armour.getInterception(head)))) {
                    player.hurtResistantTime = 20;
                    player.deathTime = 0;
                    player.isDead = false;
                    player.setHealth(5F);
                    player.clearActivePotions();
                    player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 800, 2));
                    player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 2));
                    player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 2));
                    player.setAir(300);
                    player.getFoodStats().addStats(20, 20);
                    if (!UpgradeHelper.isUpgradeInstalled(head, moduleUpgrade.ADVANCED_INTERCEPTION_SYSTEM_UNIT)) {
                        int installed = UpgradeHelper.getUpgradeLevel(head, moduleUpgrade.EMERGENCY_RESCUE);
                        int toAdd = Math.max(installed - 1, 0);
                        UpgradeHelper.setUpgradeLevel(head, moduleUpgrade.EMERGENCY_RESCUE, toAdd);
                    }
                }
            }
        }
    }

    @Desugar
    private record FallEnergyInfo(ItemStack stack, float damageRatio, float energyCost) {
    }
}
