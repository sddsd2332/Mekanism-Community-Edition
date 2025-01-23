package mekanism.common.content.gear;

import mekanism.api.EnumColor;
import mekanism.api.NBTConstants;
import mekanism.api.energy.IEnergizedItem;
import mekanism.api.functions.FloatSupplier;
import mekanism.api.gear.IHUDElement;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.ILangEntry;
import mekanism.common.MekanismLang;
import mekanism.common.content.gear.ModuleConfigItem.BooleanData;
import mekanism.common.content.gear.Modules.ModuleData;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class Module {

    public static final String ENABLED_KEY = "enabled";
    public static final String HANDLE_MODE_CHANGE_KEY = "handleModeChange";

    protected final List<ModuleConfigItem<?>> configItems = new ArrayList<>();

    private ModuleData<?> data;
    private ItemStack container;

    private ModuleConfigItem<Boolean> enabled;
    private ModuleConfigItem<Boolean> handleModeChange;
    private ModuleConfigItem<Boolean> renderHUD;

    private int installed = 1;

    public void init(ModuleData<?> data, ItemStack container) {
        this.data = data;
        this.container = container;
    }

    public void init() {
        enabled = addConfigItem(new ModuleConfigItem<>(this, ENABLED_KEY, MekanismLang.MODULE_ENABLED, new BooleanData(), !data.isDisabledByDefault()));
        if (data.handlesModeChange()) {
            handleModeChange = addConfigItem(new ModuleConfigItem<>(this, HANDLE_MODE_CHANGE_KEY, MekanismLang.MODULE_HANDLE_MODE_CHANGE, new BooleanData(), true));
        }
        if (data.rendersHUD()) {
            renderHUD = addConfigItem(new ModuleConfigItem<>(this, "renderHUD", MekanismLang.MODULE_RENDER_HUD, new BooleanData(), true));
        }
    }

    protected <T> ModuleConfigItem<T> addConfigItem(ModuleConfigItem<T> item) {
        configItems.add(item);
        return item;
    }

    public void tick(EntityPlayer player) {
        if (!player.world.isRemote) {
            if (isEnabled()) {
                tickServer(player);
            }
        } else {
            if (isEnabled()) {
                tickClient(player);
            }
        }
    }

    public IEnergizedItem getEnergyContainer() {
        if (getContainer().getItem() instanceof IEnergizedItem item) {
            return item;
        }
        return null;
    }


    public double getContainerEnergy() {
        IEnergizedItem energyContainer = getEnergyContainer();
        return energyContainer == null ? 0 : energyContainer.getEnergy(getContainer());
    }

    public boolean canUseEnergy(EntityLivingBase wearer, double energy) {
        //Note: This is subtly different than how useEnergy does it so that we can get to useEnergy when in creative
        return canUseEnergy(wearer, energy, false);
    }

    public boolean canUseEnergy(EntityLivingBase wearer, double energy, boolean ignoreCreative) {
        return canUseEnergy(wearer, getEnergyContainer(), energy, ignoreCreative);
    }

    public boolean canUseEnergy(EntityLivingBase wearer, @Nullable IEnergizedItem energyContainer, double energy, boolean ignoreCreative) {
        if (energyContainer != null && wearer instanceof EntityPlayer player && !player.isSpectator()) {
            //Don't check spectators in general
            if (!ignoreCreative || !player.isCreative()) {
                return energyContainer.extract(getContainer(), energy, false) == (energy);
            }
        }
        return false;
    }

    public double useEnergy(EntityLivingBase wearer, double energy) {
        return useEnergy(wearer, energy, true);
    }

    public double useEnergy(EntityLivingBase wearer, double energy, boolean freeCreative) {
        return useEnergy(wearer, getEnergyContainer(), energy, freeCreative);
    }

    public double useEnergy(EntityLivingBase wearer, @Nullable IEnergizedItem energyContainer, double energy, boolean freeCreative) {
        if (energyContainer != null) {
            //Use from spectators if this is called due to the various edge cases that exist for when things are calculated manually
            if (!freeCreative || !(wearer instanceof EntityPlayer player) || MekanismUtils.isPlayingMode(player)) {
                return energyContainer.extract(getContainer(), energy, true);
            }
        }
        return 0;
    }

    protected void tickServer(EntityPlayer player) {
    }

    protected void tickClient(EntityPlayer player) {
    }

    public final void read(NBTTagCompound nbt) {
        if (nbt.hasKey(NBTConstants.AMOUNT)) {
            installed = nbt.getInteger(NBTConstants.AMOUNT);
        }
        init();
        for (ModuleConfigItem<?> item : configItems) {
            item.read(nbt);
        }
    }

    /**
     * Save this module on the container ItemStack. Will create proper NBT structure if it does not yet exist.
     *
     * @param callback - will run after the NBT data is saved
     */
    public final void save(Consumer<ItemStack> callback) {
        NBTTagCompound modulesTag = ItemDataUtils.getCompound(container, NBTConstants.MODULES);
        NBTTagCompound nbt = modulesTag.getCompoundTag(data.getName());

        nbt.setInteger(NBTConstants.AMOUNT, installed);
        for (ModuleConfigItem<?> item : configItems) {
            item.write(nbt);
        }

        modulesTag.setTag(data.getName(), nbt);
        ItemDataUtils.setCompound(container, NBTConstants.MODULES, modulesTag);

        if (callback != null) {
            callback.accept(container);
        }
    }

    public String getName() {
        return data.getName();
    }

    public ModuleData<?> getData() {
        return data;
    }

    public int getInstalledCount() {
        return installed;
    }

    public void setInstalledCount(int installed) {
        this.installed = installed;
    }

    public boolean isEnabled() {
        if (enabled != null) {
            return enabled.get();
        } else {
            return false;
        }
    }

    public void setDisabledForce() {
        enabled.getData().set(false);
        save(null);
    }

    protected ItemStack getContainer() {
        return container;
    }

    public List<ModuleConfigItem<?>> getConfigItems() {
        return configItems;
    }

    public void addHUDStrings(EntityPlayer player, List<String> list) {
    }

    public void addHUDElements(EntityPlayer player, List<IHUDElement> list) {

    }

    public void changeMode(@Nonnull EntityPlayer player, @Nonnull ItemStack stack, int shift, boolean displayChangeMessage) {
    }

    public boolean canChangeModeWhenDisabled() {
        return false;
    }

    public boolean handlesModeChange() {
        return data.handlesModeChange() && handleModeChange.get() && (isEnabled() || canChangeModeWhenDisabled());
    }

    public void setModeHandlingDisabledForce() {
        if (data.handlesModeChange()) {
            handleModeChange.getData().set(false);
            save(null);
        }
    }

    public boolean renderHUD() {
        return data.rendersHUD() && renderHUD.get();
    }

    public void onAdded(boolean first) {
        for (Module module : Modules.loadAll(getContainer())) {
            if (module.getData() != getData()) {
                // disable other exclusive modules if this is an exclusive module, as this one will now be active
                if (getData().isExclusive(module.getData().getExclusiveFlags())) {
                    module.setDisabledForce();
                }
                if (handlesModeChange() && module.handlesModeChange()) {
                    module.setModeHandlingDisabledForce();
                }
            }
        }
        onAddedModule(first);
    }

    public void onAddedModule(boolean first) {

    }

    public void onRemoved(boolean last) {
    }

    protected void displayModeChange(EntityPlayer player, ITextComponent modeName, IHasTextComponent mode) {
        player.sendMessage(MekanismLang.LOG_FORMAT.translateColored(EnumColor.DARK_BLUE, MekanismLang.MEKANISM,
                MekanismLang.MODULE_MODE_CHANGE.translateColored(EnumColor.GREY, modeName, EnumColor.INDIGO, mode.getTextComponent())));
    }

    protected void toggleEnabled(EntityPlayer player, ITextComponent modeName) {
        enabled.set(!isEnabled(), null);
        ILangEntry lang = isEnabled() ? MekanismLang.MODULE_ENABLED_LOWER : MekanismLang.MODULE_DISABLED_LOWER;
        player.sendMessage(MekanismLang.LOG_FORMAT.translateColored(EnumColor.DARK_BLUE, MekanismLang.MEKANISM,
                MekanismLang.GENERIC_STORED.translateColored(EnumColor.GREY, EnumColor.GREY, modeName, isEnabled() ? EnumColor.BRIGHT_GREEN : EnumColor.DARK_RED, lang.translate())));
    }

    public ModuleDamageAbsorbInfo getDamageAbsorbInfo(DamageSource damageSource) {
        return null;
    }

    public static class ModuleDamageAbsorbInfo {

        private final FloatSupplier absorptionRatio;
        private final double energyCost;

        /**
         * @param absorptionRatio Ratio of damage this module can absorb up to, returns a value between zero and one.
         * @param energyCost      Energy cost per point of damage reduced.
         */
        public ModuleDamageAbsorbInfo(FloatSupplier absorptionRatio, double energyCost) {
            this.absorptionRatio = Objects.requireNonNull(absorptionRatio, "Absorption ratio supplier cannot be null");
            this.energyCost = Objects.requireNonNull(energyCost, "Energy cost supplier cannot be null");
        }

        /**
         * Gets the ratio of damage this module can absorb up to, returns a value between zero and one.
         */
        public FloatSupplier getAbsorptionRatio() {
            return absorptionRatio;
        }

        /**
         * Gets the energy cost per point of damage reduced.
         */
        public double getEnergyCost() {
            return energyCost;
        }
    }
}
