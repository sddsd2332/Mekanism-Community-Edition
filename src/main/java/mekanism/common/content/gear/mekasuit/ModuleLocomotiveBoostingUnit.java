package mekanism.common.content.gear.mekasuit;

import mekanism.api.IIncrementalEnum;
import mekanism.api.math.MathUtils;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.TextComponentGroup;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.ModuleConfigItem.EnumData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModuleLocomotiveBoostingUnit extends Module {

    private ModuleConfigItem<SprintBoost> sprintBoost;

    @Override
    public void init() {
        sprintBoost = addConfigItem(new ModuleConfigItem<>(this, "sprint_boost", MekanismLang.MODULE_SPRINT_BOOST,
                new EnumData<>(SprintBoost.class, getInstalledCount() + 1), SprintBoost.LOW));
    }

    @Override
    public void changeMode(EntityPlayer player, ItemStack stack, int shift, boolean displayChangeMessage) {
        if (isEnabled()) {
            SprintBoost newMode = sprintBoost.get().adjust(shift, v -> v.ordinal() < getInstalledCount() + 1);
            if (sprintBoost.get() != newMode) {
                sprintBoost.set(newMode, null);
                if (displayChangeMessage) {
                    displayModeChange(player, MekanismLang.MODULE_SPRINT_BOOST.translate(), newMode);
                }
            }
        }
    }

    @Override
    public void tickServer(EntityPlayer player) {
        super.tickServer(player);
        if (thisTick(player)) {
            useEnergy(player, MekanismConfig.current().meka.mekaSuitEnergyUsageSprintBoost.val() * (getBoost() / 0.1F));
        }
    }

    @Override
    public void tickClient(EntityPlayer player) {
        super.tickClient(player);
        // leave energy usage up to server
        thisTick(player);
    }

    private boolean thisTick(EntityPlayer player) {
        if (canFunction(player)) {
            float boost = getBoost();
            if (!player.onGround) {
                boost /= 5F; // throttle if we're in the air
            }
            if (player.isInWater()) {
                boost /= 5F; // throttle if we're in the water
            }
            player.moveRelative(boost, 0, 0, 1);
            return true;
        }
        return false;
    }

    public boolean canFunction(EntityPlayer player) {
        //Don't allow boosting unit to work when flying with the elytra, a jetpack should be used instead
        return !player.isElytraFlying() && player.isSprinting() && canUseEnergy(player, MekanismConfig.current().meka.mekaSuitEnergyUsageSprintBoost.val() * (getBoost() / 0.1F));
    }

    public float getBoost() {
        return sprintBoost.get().getBoost();
    }

    public enum SprintBoost implements IHasTextComponent, IIncrementalEnum<SprintBoost> {
        OFF(0),
        LOW(0.05F),
        MED(0.1F),
        HIGH(0.25F),
        ULTRA(0.5F);

        private static final SprintBoost[] MODES = values();

        private final float boost;
        private final ITextComponent label;

        SprintBoost(float boost) {
            this.boost = boost;
            this.label = new TextComponentGroup().getString(Float.toString(boost));
        }

        @Nonnull
        @Override
        public SprintBoost byIndex(int index) {
            return MathUtils.getByIndexMod(MODES, index);
        }

        @Override
        public ITextComponent getTextComponent() {
            return label;
        }

        public float getBoost() {
            return boost;
        }
    }
}