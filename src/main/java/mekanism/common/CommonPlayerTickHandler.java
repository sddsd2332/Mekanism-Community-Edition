package mekanism.common;

import mekanism.api.energy.IEnergizedItem;
import mekanism.api.functions.FloatSupplier;
import mekanism.api.gas.GasStack;
import mekanism.api.gear.IModule;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.content.gear.mekasuit.ModuleGravitationalModulatingUnit;
import mekanism.common.content.gear.mekasuit.ModuleHydraulicPropulsionUnit;
import mekanism.common.content.gear.mekasuit.ModuleLocomotiveBoostingUnit;
import mekanism.common.entity.EntityFlame;
import mekanism.common.item.ItemFlamethrower;
import mekanism.common.item.ItemFreeRunners;
import mekanism.common.item.ItemGasMask;
import mekanism.common.item.ItemScubaTank;
import mekanism.common.item.armor.ItemMekaSuitArmor;
import mekanism.common.item.interfaces.IJetpackItem;
import mekanism.common.item.interfaces.IJetpackItem.JetpackMode;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommonPlayerTickHandler {

    public static final List<UUID> FLYING_PLAYERS = new ArrayList<>();


    public static boolean isOnGroundOrSleeping(EntityPlayer player) {
        return player.onGround || player.isSneaking()|| player.capabilities.isFlying;
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
        if (!stack.isEmpty() && !player.isSneaking()) {
            if (stack.getItem() instanceof ItemFreeRunners freeRunners) {
                if (freeRunners.getMode(stack) == ItemFreeRunners.FreeRunnerMode.NORMAL) {
                    return 0.5F;
                }
            }
            IModule<ModuleHydraulicPropulsionUnit> module = ModuleHelper.get().load(stack, MekanismModules.HYDRAULIC_PROPULSION_UNIT);
            if (module != null && module.isEnabled()) {
                return module.getCustomInstance().getStepHeight();
            }
        }
        return 0;
    }

    @SubscribeEvent
    public void onTick(PlayerTickEvent event) {
        if (event.phase == Phase.END && event.side.isServer()) {
            tickEnd(event.player);
        }
    }

    public void tickEnd(EntityPlayer player) {
        Mekanism.playerState.updateStepAssist(player);

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
                IJetpackItem jetpackItem = (IJetpackItem) primaryJetpack.getItem();
                JetpackMode primaryMode = jetpackItem.getJetpackMode(primaryJetpack);
                JetpackMode mode = IJetpackItem.getPlayerJetpackMode(player, primaryMode, p -> Mekanism.keyMap.has(p.getUniqueID(), KeySync.ASCEND));
                if (mode != JetpackMode.DISABLED) {
                    double jetpackThrust = jetpackItem.getJetpackThrust(primaryJetpack);
                    if (IJetpackItem.handleJetpackMotion(player, mode, jetpackThrust, p -> Mekanism.keyMap.has(p.getUniqueID(), KeySync.ASCEND))) {
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
                        MekanismUtils.speedUpEffectSafely(player, effect);
                    }
                }
            }
        }
        Mekanism.playerState.updateFlightInfo(player);
    }


    public static boolean isGravitationalModulationReady(EntityPlayer player) {
        if (MekanismUtils.isPlayingMode(player)) {
            IModule<ModuleGravitationalModulatingUnit> module = ModuleHelper.get().load(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), MekanismModules.GRAVITATIONAL_MODULATING_UNIT);
            double usage = MekanismConfig.current().meka.mekaSuitEnergyUsageGravitationalModulation.val();
            return module != null && module.isEnabled() && module.getContainerEnergy() - (usage) >= 0;
        }
        return false;
    }

    public static boolean isGravitationalModulationOn(EntityPlayer player) {
        return isGravitationalModulationReady(player) && player.capabilities.isFlying;
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
            if (info != null && tryAbsorbAll(event, entity, info.container, info.damageRatio, info.energyCost)) {
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
            if (info != null && handleDamage(event, entity, info.container, info.damageRatio, info.energyCost)) {
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


    private boolean tryAbsorbAll(LivingAttackEvent event, EntityLivingBase entityLivingBase, IEnergizedItem energyContainer, FloatSupplier absorptionRatio, double energyCost) {
        if (energyContainer != null && absorptionRatio.getAsFloat() == 1) {
            double energyRequirement = energyCost * event.getAmount();
            if (energyRequirement == 0) {
                event.setCanceled(true);
                return true;
            }
            ItemStack stack = entityLivingBase.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            double simulatedExtract = energyContainer.extract(stack, energyRequirement, false);
            if (simulatedExtract == energyRequirement) {
                //If we could fully negate the damage cancel the event and extract it
                energyContainer.extract(stack, energyRequirement, true);
                event.setCanceled(true);
                return true;
            }
        }
        return false;
    }

    private boolean handleDamage(LivingHurtEvent event, EntityLivingBase base, IEnergizedItem energyContainer, FloatSupplier absorptionRatio, double energyCost) {
        if (energyContainer != null) {
            ItemStack feetStack = base.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            float absorption = absorptionRatio.getAsFloat();
            float amount = event.getAmount() * absorption;
            double energyRequirement = energyCost * amount;
            float ratioAbsorbed;
            if (energyRequirement == 0) {
                //No energy is actually needed to absorb the damage, either because of the config
                // or how small the amount to absorb is
                ratioAbsorbed = absorption;
            } else {
                ratioAbsorbed = (float) (absorption * energyContainer.extract(feetStack, energyRequirement, true) / (amount));
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

    @SubscribeEvent
    public void onLivingJump(LivingJumpEvent event) {
        if (event.getEntity() instanceof EntityPlayer player) {
            IModule<ModuleHydraulicPropulsionUnit> module = ModuleHelper.get().load(player.getItemStackFromSlot(EntityEquipmentSlot.FEET), MekanismModules.HYDRAULIC_PROPULSION_UNIT);
            if (module != null && module.isEnabled() && Mekanism.keyMap.has(player.getUniqueID(), KeySync.BOOST)) {
                float boost = module.getCustomInstance().getBoost();
                double usage = MekanismConfig.current().meka.mekaSuitBaseJumpEnergyUsage.val() * (boost / 0.1F);
                IEnergizedItem energyContainer = module.getEnergyContainer();
                if (module.canUseEnergy(player, energyContainer, usage, false)) {
                    // if we're sprinting with the boost module, limit the height
                    IModule<ModuleLocomotiveBoostingUnit> boostModule = ModuleHelper.get().load(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS), MekanismModules.LOCOMOTIVE_BOOSTING_UNIT);
                    if (boostModule != null && boostModule.isEnabled() && boostModule.getCustomInstance().canFunction(boostModule, player)) {
                        boost = (float) Math.sqrt(boost);
                    }
                    player.motionY += boost;
                    module.useEnergy(player, energyContainer, usage, true);
                }
            }
        }
    }


    @Nullable
    private FallEnergyInfo getFallAbsorptionEnergyInfo(EntityLivingBase base) {
        ItemStack feetStack = base.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (!feetStack.isEmpty()) {
            if (feetStack.getItem() instanceof ItemFreeRunners boots) {
                if (boots.getMode(feetStack).preventsFallDamage()) {
                    return new FallEnergyInfo(boots, MekanismConfig.current().mekce.freeRunnerFallDamageRatio, MekanismConfig.current().mekce.freeRunnerFallEnergyCost.val());
                }
            } else if (feetStack.getItem() instanceof ItemMekaSuitArmor armor) {
                return new FallEnergyInfo(armor, MekanismConfig.current().meka.mekaSuitFallDamageRatio, MekanismConfig.current().meka.mekaSuitEnergyUsageFall.val());
            }
        }
        return null;
    }


    @SubscribeEvent
    public static void playerLoggedOut(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {
        removeFlyingPlayer(event.player.getUniqueID());
    }

    private static void removeFlyingPlayer(UUID playerUUID) {
        FLYING_PLAYERS.removeIf(uuid -> uuid.equals(playerUUID));
    }


    @SubscribeEvent
    public void getBreakSpeed(PlayerEvent.BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        float speed = event.getNewSpeed();
        //Gyroscopic stabilization check
        ItemStack legs = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        if (!legs.isEmpty() && ModuleHelper.get().isEnabled(legs, MekanismModules.GYROSCOPIC_STABILIZATION_UNIT)) {
            if (player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(player)) {
                speed *= 5.0F;
            }
            if (!player.onGround) {
                speed *= 5.0F;
            }
        }
        event.setNewSpeed(speed);
    }


    private static class FallEnergyInfo {

        @Nullable
        private final IEnergizedItem container;
        private final FloatSupplier damageRatio;
        private final double energyCost;

        public FallEnergyInfo(@Nullable IEnergizedItem container, FloatSupplier damageRatio, float energyCost) {
            this.container = container;
            this.damageRatio = damageRatio;
            this.energyCost = energyCost;
        }

    }

    /*
    //When the player dies
    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer player) {
            ItemStack head = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (!head.isEmpty() && head.getItem() instanceof ItemOldMekAsuitHeadArmour armour &&
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
                if (!player.isEntityAlive() && !head.isEmpty() && head.getItem() instanceof ItemOldMekAsuitHeadArmour armour &&
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


 */


}
