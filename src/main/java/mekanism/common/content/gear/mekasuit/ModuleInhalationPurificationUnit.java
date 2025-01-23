package mekanism.common.content.gear.mekasuit;

import mekanism.api.energy.IEnergizedItem;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.ModuleConfigItem.BooleanData;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class ModuleInhalationPurificationUnit extends Module {

    private static final ModuleDamageAbsorbInfo INHALATION_ABSORB_INFO = new ModuleDamageAbsorbInfo(MekanismConfig.current().meka.mekaSuitMagicDamageRatio,
            MekanismConfig.current().meka.mekaSuitEnergyUsageMagicReduce.val());

    private ModuleConfigItem<Boolean> beneficialEffects;
    private ModuleConfigItem<Boolean> harmfulEffects;

    @Override
    public void init() {
        beneficialEffects = addConfigItem(new ModuleConfigItem<>(this, "beneficial_effects", MekanismLang.MODULE_PURIFICATION_BENEFICIAL, new BooleanData(), false));
        harmfulEffects = addConfigItem(new ModuleConfigItem<>(this, "harmful_effects", MekanismLang.MODULE_PURIFICATION_HARMFUL, new BooleanData(), true));
    }

    @Override
    public void tickClient(EntityPlayer player) {
        super.tickClient(player);
        //Messy rough estimate version of tickServer so that the timer actually properly updates
        if (!player.isSpectator()) {
            double usage = MekanismConfig.current().meka.mekaSuitEnergyUsagePotionTick.val();
            boolean free = usage == 0 || player.isCreative();
            double energy = free ? 0 : getContainerEnergy();
            if (free || energy >= usage) {
                //Gather all the active effects that we can handle, so that we have them in their own list and
                // don't run into any issues related to CMEs
                List<PotionEffect> effects = player.getActivePotionEffects().stream().filter(effect -> canHandle(effect.getPotion().isBadEffect())).collect(Collectors.toList());
                for (PotionEffect effect : effects) {
                    if (free) {
                        speedupEffect(player, effect);
                    } else {
                        energy = energy - (usage);
                        speedupEffect(player, effect);
                        if (energy < usage) {
                            //If after using energy, our remaining energy is now smaller than how much we need to use, exit
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tickServer(EntityPlayer player) {
        super.tickServer(player);
        double usage = MekanismConfig.current().meka.mekaSuitEnergyUsagePotionTick.val();
        boolean free = usage == 0 || player.isCreative();
        IEnergizedItem energyContainer = free ? null : getEnergyContainer();
        if (free || (energyContainer != null && energyContainer.getEnergy(getContainer()) >= (usage))) {
            //Gather all the active effects that we can handle, so that we have them in their own list and
            // don't run into any issues related to CMEs
            List<PotionEffect> effects = player.getActivePotionEffects().stream()
                    .filter(effect -> canHandle(effect.getPotion().isBadEffect()))
                    .collect(Collectors.toList());
            for (PotionEffect effect : effects) {
                if (free) {
                    speedupEffect(player, effect);
                } else if (useEnergy(player, energyContainer, usage, true) == 0) {
                    //If we can't actually extract energy, exit
                    break;
                } else {
                    speedupEffect(player, effect);
                    if (energyContainer.getEnergy(getContainer()) < (usage)) {
                        //If after using energy, our remaining energy is now smaller than how much we need to use, exit
                        break;
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public ModuleDamageAbsorbInfo getDamageAbsorbInfo(DamageSource damageSource) {
        return damageSource.isMagicDamage() ? INHALATION_ABSORB_INFO : null;
    }

    private void speedupEffect(EntityPlayer player, PotionEffect effect) {
        for (int i = 0; i < 9; i++) {
            MekanismUtils.speedUpEffectSafely(player, effect);
        }
    }

    private boolean canHandle(boolean effectType) {
        if (effectType) {
            return harmfulEffects.get();
        } else {
            return beneficialEffects.get();
        }
    }
}