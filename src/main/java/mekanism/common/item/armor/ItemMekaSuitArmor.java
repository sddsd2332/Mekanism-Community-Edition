package mekanism.common.item.armor;

import cofh.redstoneflux.api.IEnergyContainerItem;
import com.brandon3055.draconicevolution.items.armor.ICustomArmor;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.IHazmatLike;
import ic2.api.item.ISpecialElectricItem;
import mekanism.api.EnumColor;
import mekanism.api.energy.IEnergizedItem;
import mekanism.api.functions.FloatSupplier;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.ICustomModule.ModuleDamageAbsorbInfo;
import mekanism.api.gear.IModule;
import mekanism.api.gear.ModuleData;
import mekanism.client.MekKeyHandler;
import mekanism.client.MekanismKeyHandler;
import mekanism.common.Mekanism;
import mekanism.common.MekanismFluids;
import mekanism.common.MekanismItems;
import mekanism.common.MekanismModules;
import mekanism.common.capabilities.ItemCapabilityWrapper;
import mekanism.common.capabilities.laser.item.LaserDissipationHandler;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.IModuleContainerItem;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.content.gear.shared.ModuleEnergyUnit;
import mekanism.common.entity.EntityMekaSuitArmor;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.integration.forgeenergy.ForgeEnergyItemWrapper;
import mekanism.common.integration.ic2.IC2ItemManager;
import mekanism.common.integration.redstoneflux.RFIntegration;
import mekanism.common.integration.tesla.TeslaItemWrapper;
import mekanism.common.item.interfaces.IModeItem;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.ItemNBTHelper;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@Optional.InterfaceList({
        @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = MekanismHooks.IC2_MOD_ID),
        @Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyContainerItem", modid = MekanismHooks.REDSTONEFLUX_MOD_ID),
        @Optional.Interface(iface = "ic2.api.item.IHazmatLike", modid = MekanismHooks.IC2_MOD_ID),
        @Optional.Interface(iface = "com.brandon3055.draconicevolution.items.armor.ICustomArmor", modid = MekanismHooks.DraconicEvolution_MOD_ID)
})
public abstract class ItemMekaSuitArmor extends ItemArmor implements IEnergizedItem, ISpecialArmor, IModuleContainerItem, IModeItem,
        ISpecialElectricItem, IEnergyContainerItem, IHazmatLike, ICustomArmor {

    private static final Set<DamageSource> ALWAYS_SUPPORTED_SOURCES = new LinkedHashSet<>(Arrays.asList(
            DamageSource.ANVIL, DamageSource.CACTUS, DamageSource.CRAMMING, DamageSource.DRAGON_BREATH,
            DamageSource.FALL, DamageSource.FALLING_BLOCK, DamageSource.FLY_INTO_WALL, DamageSource.GENERIC,
            DamageSource.HOT_FLOOR, DamageSource.IN_FIRE, DamageSource.IN_WALL, DamageSource.LAVA, DamageSource.LIGHTNING_BOLT,
            DamageSource.ON_FIRE, DamageSource.WITHER));

    public static Set<DamageSource> getSupportedSources() {
        return Collections.unmodifiableSet(ALWAYS_SUPPORTED_SOURCES);
    }

    private final float absorption;
    //Full laser dissipation causes 3/4 of the energy to be dissipated and the remaining energy to be refracted
    private final double laserDissipation;
    private final double laserRefraction;


    public ItemMekaSuitArmor(EntityEquipmentSlot slot) {
        super(EnumHelper.addArmorMaterial("MEKASUIT", "mekasuit", 0, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0), 3, slot);
        setNoRepair();
        setMaxStackSize(1);
        if (slot == EntityEquipmentSlot.HEAD) {
            absorption = 0.15F;
            laserDissipation = 0.15;
            laserRefraction = 0.2;
        } else if (slot == EntityEquipmentSlot.CHEST) {
            absorption = 0.4F;
            laserDissipation = 0.3;
            laserRefraction = 0.4;
        } else if (slot == EntityEquipmentSlot.LEGS) {
            absorption = 0.3F;
            laserDissipation = 0.1875;
            laserRefraction = 0.25;
        } else if (slot == EntityEquipmentSlot.FEET) {
            absorption = 0.15F;
            laserDissipation = 0.1125;
            laserRefraction = 0.15;
        } else {
            throw new IllegalArgumentException("Unknown Equipment Slot Type");
        }
    }

    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot Type, Entity entity) {
        return Type == armorType;
    }


    @Override
    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        if (Mekanism.hooks.IC2Loaded) {
            if (isModuleEnabled(stack, MekanismModules.RADIATION_SHIELDING_UNIT)) {
                Potion radiation = Potion.getPotionFromResourceLocation("ic2:radiation");
                if (radiation != null && entity.isPotionActive(radiation)) {
                    entity.removePotionEffect(radiation);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.sneakKey)) {
            addModuleDetails(stack, tooltip);
        } else {
            tooltip.add(EnumColor.AQUA + LangUtils.localize("tooltip.storedEnergy") + ": " + EnumColor.GREY + MekanismUtils.getEnergyDisplay(getEnergy(stack), getMaxEnergy(stack)));
            addInformation(stack, world, tooltip);
            tooltip.add(LangUtils.localize("tooltip.hold") + " " + EnumColor.INDIGO + GameSettings.getKeyDisplayString(MekanismKeyHandler.sneakKey.getKeyCode()) + EnumColor.GREY + " " + LangUtils.localize("tooltip.forDetails") + ".");
        }
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip) {

    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getEnergy(stack) > 0;
    }


    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1D - (getEnergy(stack) / getMaxEnergy(stack));
    }

    @Override
    public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
        return MathHelper.hsvToRGB(Math.max(0.0F, (float) (1 - getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return !stack.isEmpty() && super.hasEffect(stack) && IModuleContainerItem.hasOtherEnchants(stack);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tabs, @Nonnull NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tabs)) {
            return;
        }
        ItemStack discharged = new ItemStack(this);
        items.add(discharged);

        ItemStack stack = new ItemStack(this);
        setEnergy(stack, ((IEnergizedItem) stack.getItem()).getMaxEnergy(stack));
        items.add(stack);

        ItemStack FullStack = new ItemStack(this);
        for (ModuleData<?> module : ModuleHelper.get().getAll()) {
            if (ModuleHelper.get().getSupported(FullStack).contains(module)) {
                setModule(FullStack, module);
            }
        }
        setEnergy(FullStack, ((IEnergizedItem) FullStack.getItem()).getMaxEnergy(FullStack));
        if (FullStack.getItem() instanceof IGasItem gasItem) {
            if (FullStack.getItem() == MekanismItems.MEKASUIT_HELMET) {
                gasItem.setGas(FullStack, new GasStack(MekanismFluids.NutritionalPaste, gasItem.getMaxGas(FullStack)));
            } else if (FullStack.getItem() == MekanismItems.MEKASUIT_BODYARMOR) {
                gasItem.setGas(FullStack, new GasStack(MekanismFluids.Hydrogen, gasItem.getMaxGas(FullStack)));
            }
        }
        if (Mekanism.hooks.DraconicEvolution) {
            ItemNBTHelper.setFloat(FullStack, "ProtectionPoints", getProtectionPoints(FullStack));
        }
        items.add(FullStack);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        super.onArmorTick(world, player, stack);
        for (Module<?> module : getModules(stack)) {
            module.tick(player);
        }
    }

    @Override
    public void changeMode(@NotNull EntityPlayer player, @NotNull ItemStack stack, int shift, boolean displayChangeMessage) {
        for (Module<?> module : getModules(stack)) {
            if (module.handlesModeChange()) {
                module.changeMode(player, stack, shift, displayChangeMessage);
                return;
            }
        }
    }


    @Override
    public boolean supportsSlotType(ItemStack stack, @Nonnull EntityEquipmentSlot slotType) {
        return slotType == armorType && getModules(stack).stream().anyMatch(Module::handlesModeChange);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "mekanism:render/MekAsuit.png";
    }

    @Override
    public double getMaxEnergy(ItemStack stack) {
        IModule<ModuleEnergyUnit> module = getModule(stack, MekanismModules.ENERGY_UNIT);
        return module == null ? MekanismConfig.current().meka.mekaSuitBaseEnergyCapacity.val() : module.getCustomInstance().getEnergyCapacity(module);
    }

    @Override
    public double getMaxTransfer(ItemStack stack) {
        IModule<ModuleEnergyUnit> module = getModule(stack, MekanismModules.ENERGY_UNIT);
        return module == null ? MekanismConfig.current().meka.mekaSuitBaseChargeRate.val() : module.getCustomInstance().getChargeRate(module);
    }


    public static float getDamageAbsorbed(EntityPlayer player, DamageSource source, float amount) {
        return getDamageAbsorbed(player, source, amount, null);
    }

    public static boolean tryAbsorbAll(EntityPlayer player, DamageSource source, float amount) {
        List<Runnable> energyUsageCallbacks = new ArrayList<>(4);
        if (getDamageAbsorbed(player, source, amount, energyUsageCallbacks) >= 1) {
            //If we can fully absorb it, actually use the energy from the various pieces and then return that we absorbed it all
            for (Runnable energyUsageCallback : energyUsageCallbacks) {
                energyUsageCallback.run();
            }
            return true;
        }
        return false;
    }


    private static float getDamageAbsorbed(EntityPlayer player, DamageSource source, float amount, @Nullable List<Runnable> energyUseCallbacks) {
        if (amount <= 0) {
            return 0;
        }
        float ratioAbsorbed = 0;
        List<FoundArmorDetails> armorDetails = new ArrayList<>();
        //Start by looping the armor, allowing modules to absorb damage if they can
        for (ItemStack stack : player.getArmorInventoryList()) {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemMekaSuitArmor armor) {
                IEnergizedItem energyContainer = armor;
                if (energyContainer != null) {
                    FoundArmorDetails details = new FoundArmorDetails(energyContainer, armor.getEnergy(stack), armor);
                    armorDetails.add(details);
                    for (Module<?> module : details.armor.getModules(stack)) {
                        if (module.isEnabled()) {
                            ModuleDamageAbsorbInfo damageAbsorbInfo = getModuleDamageAbsorbInfo(module, source);
                            if (damageAbsorbInfo != null) {
                                float absorption = damageAbsorbInfo.getAbsorptionRatio().getAsFloat();
                                ratioAbsorbed += absorbDamage(details.usageInfo, amount, absorption, ratioAbsorbed, damageAbsorbInfo.getEnergyCost());
                                if (ratioAbsorbed >= 1) {
                                    //If we have fully absorbed the damage, stop checking/trying to absorb more
                                    break;
                                }
                            }
                        }
                    }
                    if (ratioAbsorbed >= 1) {
                        //If we have fully absorbed the damage, stop checking/trying to absorb more
                        break;
                    }
                }
            }
        }
        if (ratioAbsorbed < 1) {
            //If we haven't fully absorbed it check the individual pieces of armor for if they can absorb any
            FloatSupplier absorbRatio = null;
            for (FoundArmorDetails details : armorDetails) {
                if (absorbRatio == null) {
                    //If we haven't looked up yet if we can absorb the damage type and if we can't
                    // stop checking if the armor is able to
                    if (!ALWAYS_SUPPORTED_SOURCES.contains(source) && source.isUnblockable()) {
                        break;
                    }
                    // Next lookup the ratio at which we can absorb the given damage type from the config
                    absorbRatio = MekanismConfig.current().meka.mekaSuitDamageRatios.getOrDefault(source, MekanismConfig.current().meka.mekaSuitUnspecifiedDamageRatio);
                    if (absorbRatio.getAsFloat() == 0) {
                        //If the config specifies that the damage type shouldn't be blocked at all
                        // stop checking if the armor is able to
                        break;
                    }
                }
                float absorption = details.armor.absorption * absorbRatio.getAsFloat();
                ratioAbsorbed += absorbDamage(details.usageInfo, amount, absorption, ratioAbsorbed, MekanismConfig.current().meka.mekaSuitEnergyUsageDamage.val());
                if (ratioAbsorbed >= 1) {
                    //If we have fully absorbed the damage, stop checking/trying to absorb more
                    break;
                }
            }
        }
        for (FoundArmorDetails details : armorDetails) {
            //Use energy/or enqueue usage for each piece as needed
            if (details.usageInfo.energyUsed != 0) {
                for (ItemStack stack : player.getArmorInventoryList()) {
                    if (stack.getItem() == details.armor) {
                        if (energyUseCallbacks == null) {
                            details.energyContainer.extract(stack, details.usageInfo.energyUsed, true);
                        } else {
                            energyUseCallbacks.add(() -> details.energyContainer.extract(stack, details.usageInfo.energyUsed, true));
                        }
                    }
                }
            }
        }
        return Math.min(ratioAbsorbed, 1);
    }

    @Nullable
    private static <MODULE extends ICustomModule<MODULE>> ModuleDamageAbsorbInfo getModuleDamageAbsorbInfo(IModule<MODULE> module, DamageSource damageSource) {
        return module.getCustomInstance().getDamageAbsorbInfo(module, damageSource);
    }

    private static float absorbDamage(EnergyUsageInfo usageInfo, float amount, float absorption, float currentAbsorbed, double energyCost) {
        //Cap the amount that we can absorb to how much we have left to absorb
        absorption = Math.min(1 - currentAbsorbed, absorption);
        float toAbsorb = amount * absorption;
        if (toAbsorb > 0) {
            double usage = energyCost * toAbsorb;
            if (usage == 0) {
                //No energy is actually needed to absorb the damage, either because of the config
                // or how small the amount to absorb is
                return absorption;
            } else if (usageInfo.energyAvailable >= usage) {
                //If we have more energy available than we need, increase how much energy we "used"
                // and decrease how much we have available.
                usageInfo.energyUsed = usageInfo.energyUsed + (usage);
                usageInfo.energyAvailable = usageInfo.energyAvailable - (usage);
                return absorption;
            } else if (usageInfo.energyAvailable != 0) {
                //Otherwise, if we have energy available but not as much as needed to fully absorb it
                // then we calculate what ratio we are able to block
                float absorbedPercent = (float) (usageInfo.energyAvailable / (usage));
                usageInfo.energyUsed = usageInfo.energyUsed + (usageInfo.energyAvailable);
                usageInfo.energyAvailable = 0;
                return absorption * absorbedPercent;
            }
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.IC2_MOD_ID)
    public IElectricItemManager getManager(ItemStack itemStack) {
        return IC2ItemManager.getManager(this);
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int receiveEnergy(ItemStack stack, int energy, boolean simulate) {
        if (canReceive(stack)) {
            double energyNeeded = getMaxEnergy(stack) - getEnergy(stack);
            double toReceive = Math.min(RFIntegration.fromRF(energy), energyNeeded);
            if (!simulate) {
                setEnergy(stack, getEnergy(stack) + toReceive);
            }
            return RFIntegration.toRF(toReceive);
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int extractEnergy(ItemStack theItem, int energy, boolean simulate) {
        if (canSend(theItem)) {
            double energyRemaining = getEnergy(theItem);
            double toSend = Math.min(RFIntegration.fromRF(energy), energyRemaining);
            if (!simulate) {
                setEnergy(theItem, getEnergy(theItem) - toSend);
            }
            return RFIntegration.toRF(toSend);
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int getEnergyStored(ItemStack theItem) {
        return RFIntegration.toRF(getEnergy(theItem));
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int getMaxEnergyStored(ItemStack theItem) {
        return RFIntegration.toRF(getMaxEnergy(theItem));
    }

    @Override
    @Optional.Method(modid = MekanismHooks.IC2_MOD_ID)
    public boolean addsProtection(EntityLivingBase entity, EntityEquipmentSlot slotType, ItemStack stack) {
        return isModuleEnabled(stack, MekanismModules.RADIATION_SHIELDING_UNIT);
    }

    @Override
    public boolean handleUnblockableDamage(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, double damage, int slot) {
        if (isModuleEnabled(stack, MekanismModules.RADIATION_SHIELDING_UNIT)) {
            return source.damageType.equals("radiation") || source.damageType.equals("sulphuric_acid") || source.damageType.equals("acid_burn") || source.damageType.equals("corium_burn") || source.damageType.equals("hot_coolant_burn");
        }
        return false;
    }

    //TODO - 1.18: Switch this to a record
    private static class FoundArmorDetails {
        private final IEnergizedItem energyContainer;
        private final EnergyUsageInfo usageInfo;
        private final ItemMekaSuitArmor armor;

        public FoundArmorDetails(IEnergizedItem energyContainer, double energy, ItemMekaSuitArmor armor) {
            this.energyContainer = energyContainer;
            this.usageInfo = new EnergyUsageInfo(energy);
            this.armor = armor;
        }

    }

    private static class EnergyUsageInfo {

        private double energyAvailable;
        private double energyUsed = 0;

        public EnergyUsageInfo(double energyAvailable) {
            //Copy it so we can just use minusEquals without worry
            this.energyAvailable = energyAvailable;
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public double getEnergy(ItemStack itemStack) {
        return ItemDataUtils.getDouble(itemStack, "energyStored");
    }

    @Override
    public void setEnergy(ItemStack itemStack, double amount) {
        if (amount == 0) {
            NBTTagCompound dataMap = ItemDataUtils.getDataMap(itemStack);
            dataMap.removeTag("energyStored");
            if (dataMap.isEmpty() && itemStack.getTagCompound() != null) {
                itemStack.getTagCompound().removeTag(ItemDataUtils.DATA_ID);
            }
        } else {
            ItemDataUtils.setDouble(itemStack, "energyStored", Math.max(Math.min(amount, getMaxEnergy(itemStack)), 0));
        }
    }


    @Override
    public boolean canReceive(ItemStack itemStack) {
        return getNeeded(itemStack) > 0;
    }

    @Override
    public boolean canSend(ItemStack itemStack) {
        return false;
    }


    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new ItemCapabilityWrapper(stack, new TeslaItemWrapper(), new ForgeEnergyItemWrapper(),
                LaserDissipationHandler.create(item -> isModuleEnabled(item, MekanismModules.LASER_DISSIPATION_UNIT) ? laserDissipation : 0,
                        item -> isModuleEnabled(item, MekanismModules.LASER_DISSIPATION_UNIT) ? laserRefraction : 0));
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        EntityItem item = new EntityMekaSuitArmor(world, location, itemstack);
        item.isImmuneToFire = true;
        return item;
    }

    private float getProtectionShare() {
        return switch (armorType) {
            case HEAD, FEET -> 0.15F;
            case CHEST -> 0.40F;
            case LEGS -> 0.30F;
            default -> 0;
        };
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public float getProtectionPoints(ItemStack stack) {
        IModule<?> module = getModule(stack, MekanismModules.ENERGY_SHIELD_UNIT);
        if (module != null) {
            int upgradeLevel = module.getInstalledCount();
            if (module.isEnabled()) {
                if (MekanismConfig.current().meka.mekaSuitShield.val()) {
                    return MekanismConfig.current().meka.mekaSuitShieldCapacity.val() * absorption * (int) Math.pow(2, upgradeLevel);
                } else {
                    return MekanismConfig.current().meka.mekaSuitShieldCapacity.val() * getProtectionShare() * upgradeLevel;
                }
            } else {
                return ItemNBTHelper.getFloat(stack, "ProtectionPoints", 0);
            }
        } else {
            return 0.0F;
        }
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public float getRecoveryRate(ItemStack stack) {
        IModule<?> module = getModule(stack, MekanismModules.ENERGY_SHIELD_UNIT);
        if (module != null && module.isEnabled()) {
            int upgradeLevel = module.getInstalledCount();
            if (MekanismConfig.current().meka.mekaSuitRecovery.val()) {
                return MekanismConfig.current().meka.mekaSuitRecoveryRate.val() * (int) Math.pow(2, upgradeLevel);
            } else {
                return MekanismConfig.current().meka.mekaSuitRecoveryRate.val() * (1.0F + upgradeLevel);
            }
        } else {
            return 0.0F;
        }
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public float getSpeedModifier(ItemStack stack, EntityPlayer player) {
        return 0.0F;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public float getJumpModifier(ItemStack stack, EntityPlayer player) {
        return 0.0F;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean hasHillStep(ItemStack stack, EntityPlayer player) {
        return false;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public float getFireResistance(ItemStack stack) {
        return 0.0F;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean[] hasFlight(ItemStack stack) {
        return new boolean[]{false, false, false};
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public float getFlightSpeedModifier(ItemStack stack, EntityPlayer player) {
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public float getFlightVModifier(ItemStack stack, EntityPlayer player) {
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public int getEnergyPerProtectionPoint() {
        return MekanismConfig.current().meka.mekaSuitShieldRestoresEnergy.val();
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public void modifyEnergy(ItemStack stack, int modify) {
        IModule<?> module = getModule(stack, MekanismModules.ENERGY_SHIELD_UNIT);
        if (module != null) {
            double energy = getEnergy(stack);
            energy += modify;
            if (energy > getMaxEnergy(stack)) {
                energy = getMaxEnergy(stack);
            } else if (energy < 0) {
                energy = 0;
            }
            setEnergy(stack, energy);
        }
    }
}
