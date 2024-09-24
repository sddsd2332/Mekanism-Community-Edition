package mekanism.common.item.armor;


import cofh.redstoneflux.api.IEnergyContainerItem;
import com.brandon3055.draconicevolution.items.armor.ICustomArmor;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.IHazmatLike;
import ic2.api.item.ISpecialElectricItem;
import mekanism.api.EnumColor;
import mekanism.api.energy.IEnergizedItem;
import mekanism.common.Mekanism;
import mekanism.common.base.IModuleUpgrade;
import mekanism.common.capabilities.ItemCapabilityWrapper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.integration.forgeenergy.ForgeEnergyItemWrapper;
import mekanism.common.integration.ic2.IC2ItemManager;
import mekanism.common.integration.redstoneflux.RFIntegration;
import mekanism.common.integration.tesla.TeslaItemWrapper;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UpgradeHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
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
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Optional.InterfaceList({
        @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = MekanismHooks.IC2_MOD_ID),
        @Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyContainerItem", modid = MekanismHooks.REDSTONEFLUX_MOD_ID),
        @Optional.Interface(iface = "ic2.api.item.IHazmatLike", modid = MekanismHooks.IC2_MOD_ID),
        @Optional.Interface(iface = "com.brandon3055.draconicevolution.items.armor.ICustomArmor", modid = MekanismHooks.DraconicEvolution_MOD_ID)
})
public abstract class ItemMekaSuitArmor extends ItemArmor implements IEnergizedItem,
        ISpecialElectricItem, IEnergyContainerItem, ISpecialArmor, IModuleUpgrade, IHazmatLike, ICustomArmor {

    private final float absorption;

    public ItemMekaSuitArmor(EntityEquipmentSlot slot) {
        super(EnumHelper.addArmorMaterial("MEKASUIT", "mekasuit", 0, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0), 3, slot);
        setMaxStackSize(1);
        setCreativeTab(Mekanism.tabMekanism);
        switch (armorType) {
            case HEAD, FEET -> absorption = 0.15F;
            case CHEST -> absorption = 0.4F;
            case LEGS -> absorption = 0.3F;
            default -> throw new IllegalArgumentException("Unknown Equipment Slot Type");
        }
    }

    public static float getDamageAbsorbed(EntityPlayer player, DamageSource source, float amount) {
        return getDamageAbsorbed(player, source, amount, null);
    }

    public static boolean tryAbsorbAll(EntityPlayer player, DamageSource source, float amount) {
        List<Runnable> energyUsageCallbacks = new ArrayList<>(4);
        if (getDamageAbsorbed(player, source, amount, energyUsageCallbacks) >= 1) {
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
        for (ItemStack stack : player.getArmorInventoryList()) {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemMekaSuitArmor armor) {
                double energyContainer = armor.getEnergy(stack);
                if (energyContainer > 0) {
                    FoundArmorDetails details = new FoundArmorDetails(energyContainer, armor);
                    armorDetails.add(details);
                    for (moduleUpgrade upgrade : details.armor.getValidModule(stack)) {
                        float absorption;
                        if (upgrade == moduleUpgrade.InhalationPurificationUnit && UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.InhalationPurificationUnit)) {
                            absorption = MekanismConfig.current().meka.mekaSuitMagicDamageRatio.val();
                            ratioAbsorbed += absorbDamage(details.usageInfo, amount, absorption, ratioAbsorbed, MekanismConfig.current().meka.mekaSuitEnergyUsageMagicReduce.val());
                        }
                        if (upgrade == moduleUpgrade.GEOTHERMAL_GENERATOR_UNIT && UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.GEOTHERMAL_GENERATOR_UNIT)) {
                            absorption = (float) (MekanismConfig.current().meka.mekaSuitHeatDamageReductionRatio.val() * ((double) UpgradeHelper.getUpgradeLevel(stack, moduleUpgrade.GEOTHERMAL_GENERATOR_UNIT) / 8));
                            ratioAbsorbed += absorbDamage(details.usageInfo, amount, absorption, ratioAbsorbed, 0);
                        }
                        if (ratioAbsorbed >= 1) {
                            break;
                        }
                    }
                    if (ratioAbsorbed >= 1) {
                        break;
                    }
                }
            }
        }
        if (ratioAbsorbed < 1) {
            Float absorbRatio = null;
            for (FoundArmorDetails details : armorDetails) {
                if (absorbRatio == null) {
                    //If we haven't looked up yet if we can absorb the damage type and if we can't
                    // stop checking if the armor is able to
                    if (!isSource(source)) {
                        break;
                    }
                    if (Originaltype(source)) {
                        absorbRatio = 0.75F;
                    }
                    if (absorbRatio == null) {
                        absorbRatio = MekanismConfig.current().meka.mekaSuitUnspecifiedDamageRatio.val();
                    }
                    float absorption = details.armor.absorption * absorbRatio;
                    ratioAbsorbed += absorbDamage(details.usageInfo, amount, absorption, ratioAbsorbed, MekanismConfig.current().meka.mekaSuitEnergyUsageDamage.val());
                    if (ratioAbsorbed >= 1) {
                        //If we have fully absorbed the damage, stop checking/trying to absorb more
                        break;
                    }
                }
            }
            for (FoundArmorDetails details : armorDetails) {
                if (details.usageInfo.energyUsed != 0) {
                    if (energyUseCallbacks == null) {
                        for (ItemStack stack : player.getArmorInventoryList()) {
                            details.armor.setEnergy(stack, details.energyContainer - details.usageInfo.energyUsed);
                        }
                    } else {
                        energyUseCallbacks.add(() -> {
                            for (ItemStack stack : player.getArmorInventoryList()) {
                                details.armor.setEnergy(stack, details.energyContainer - details.usageInfo.energyUsed);
                            }
                        });
                    }

                }
            }
        }
        return Math.min(ratioAbsorbed, 1);
    }

    private static boolean isSource(DamageSource source) {
        return (source == DamageSource.ANVIL || source == DamageSource.CACTUS ||
                source == DamageSource.CRAMMING || source == DamageSource.DRAGON_BREATH ||
                source == DamageSource.DROWN || source == DamageSource.FALL ||
                source == DamageSource.FALLING_BLOCK || source == DamageSource.FLY_INTO_WALL ||
                source == DamageSource.GENERIC || source == DamageSource.HOT_FLOOR ||
                source == DamageSource.IN_FIRE || source == DamageSource.IN_WALL ||
                source == DamageSource.LAVA || source == DamageSource.LIGHTNING_BOLT ||
                source == DamageSource.ON_FIRE || source == DamageSource.WITHER);
    }

    private static boolean Originaltype(DamageSource source) {
        return source == DamageSource.STARVE || source == DamageSource.OUT_OF_WORLD || source == DamageSource.MAGIC || source == DamageSource.FIREWORKS;
    }

    private static float absorbDamage(EnergyUsageInfo usageInfo, float amount, float absorption, float currentAbsorbed, double energyCost) {
        absorption = Math.min(1 - currentAbsorbed, absorption);
        float toAbsorb = amount * absorption;
        if (toAbsorb > 0) {
            double usage = energyCost * toAbsorb;
            if (usage == 0) {
                //No energy is actually needed to absorb the damage, either because of the config
                // or how small the amount to absorb is
                return absorption;
            } else if (usageInfo.energyAvailable >= usage) {
                usageInfo.energyUsed += usage;
                usageInfo.energyAvailable = usageInfo.energyAvailable - usage;
                return absorption;
            } else if (usageInfo.energyAvailable != 0) {
                float absorbedPercent = (float) (usageInfo.energyAvailable / usage);
                usageInfo.energyUsed += usageInfo.energyAvailable;
                usageInfo.energyAvailable = 0;
                return absorption * absorbedPercent;
            }
        }
        return 0;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "mekanism:render/MekAsuit.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(itemstack, world, list, flag);
        list.add(EnumColor.AQUA + LangUtils.localize("tooltip.storedEnergy") + ": " + EnumColor.GREY + MekanismUtils.getEnergyDisplay(getEnergy(itemstack), getMaxEnergy(itemstack)));
        list.addAll(UpgradeHelper.getUpgradeStats(itemstack));
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
    public double getMaxEnergy(ItemStack itemStack) {
        return MekanismUtils.getModuleMaxEnergy(itemStack, MekanismConfig.current().meka.mekaSuitBaseEnergyCapacity.val());
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tabs, @Nonnull NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tabs)) {
            return;
        }
        ItemStack discharged = new ItemStack(this);
        list.add(discharged);
        ItemStack charged = new ItemStack(this);

        setEnergy(charged, ((IEnergizedItem) charged.getItem()).getMaxEnergy(charged));
        list.add(charged);

        ItemStack fullUpgrade = new ItemStack(this);
        for (moduleUpgrade upgrade : getValidModule(fullUpgrade)) {
            UpgradeHelper.setUpgradeLevel(fullUpgrade, upgrade, upgrade.getMax());
        }

        setEnergy(fullUpgrade, ((IEnergizedItem) fullUpgrade.getItem()).getMaxEnergy(fullUpgrade));
        list.add(fullUpgrade);

    }

    @Override
    public double getMaxTransfer(ItemStack itemStack) {
        return MekanismUtils.getModuleMaxEnergy(itemStack, MekanismConfig.current().meka.mekaSuitBaseChargeRate.val());
    }

    @Override
    public boolean canReceive(ItemStack itemStack) {
        return getMaxEnergy(itemStack) - getEnergy(itemStack) > 0;
    }

    @Override
    public boolean canSend(ItemStack itemStack) {
        return false;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.IC2_MOD_ID)
    public IElectricItemManager getManager(ItemStack itemStack) {
        return IC2ItemManager.getManager(this);
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int receiveEnergy(ItemStack theItem, int energy, boolean simulate) {
        if (canReceive(theItem)) {
            double energyNeeded = getMaxEnergy(theItem) - getEnergy(theItem);
            double toReceive = Math.min(RFIntegration.fromRF(energy), energyNeeded);
            if (!simulate) {
                setEnergy(theItem, getEnergy(theItem) + toReceive);
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
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new ItemCapabilityWrapper(stack, new TeslaItemWrapper(), new ForgeEnergyItemWrapper());
    }

    @Override
    public List<moduleUpgrade> getValidModule(ItemStack stack) {
        return new ArrayList<>() {{
            add(moduleUpgrade.EnergyUnit);
            add(moduleUpgrade.RADIATION_SHIELDING_UNIT);
            add(moduleUpgrade.ENERGY_SHIELD_UNIT);
        }};
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (!world.isRemote) {
            if (itemStack.getItem() instanceof ItemMekaSuitArmor) {
                Shielding(itemStack);
            }
        }
    }

    public void Shielding(ItemStack stack) {
        if (Mekanism.hooks.NuclearCraft) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            double nc = 0;
            if (tag.hasKey("ncRadiationResistance")) {
                nc = tag.getDouble("ncRadiationResistance");
            }
            if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.RADIATION_SHIELDING_UNIT) && nc != getShieldingByArmor()) {
                tag.setDouble("ncRadiationResistance", getShieldingByArmor());
            }
            if (nc == 0 && tag.isEmpty()) {
                stack.getTagCompound().removeTag("ncRadiationResistance");
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.IC2_MOD_ID)
    public boolean addsProtection(EntityLivingBase entityLivingBase, EntityEquipmentSlot slotType, ItemStack stack) {
        return UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.RADIATION_SHIELDING_UNIT);
    }

    abstract double getShieldingByArmor();

    @Override
    public boolean handleUnblockableDamage(EntityLivingBase entity, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
        if (UpgradeHelper.isUpgradeInstalled(armor, moduleUpgrade.RADIATION_SHIELDING_UNIT)) {
            return source.damageType.equals("radiation") || source.damageType.equals("sulphuric_acid") || source.damageType.equals("acid_burn") || source.damageType.equals("corium_burn") || source.damageType.equals("hot_coolant_burn");
        }
        return false;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        if (Mekanism.hooks.IC2Loaded) {
            if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.RADIATION_SHIELDING_UNIT)) {
                Potion radiation = Potion.getPotionFromResourceLocation("ic2:radiation");
                if (radiation != null && entity.isPotionActive(radiation)) {
                    entity.removePotionEffect(radiation);
                }
            }
        }
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack itemstack) {
        return EnumColor.ORANGE + super.getItemStackDisplayName(itemstack);
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public float getProtectionPoints(ItemStack stack) {
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.ENERGY_SHIELD_UNIT)) {
            int upgradeLevel = UpgradeHelper.getUpgradeLevel(stack, moduleUpgrade.ENERGY_SHIELD_UNIT);
            if (MekanismConfig.current().meka.mekaSuitShield.val()) {
                return MekanismConfig.current().meka.mekaSuitShieldCapacity.val() * getProtectionShare() * (int) Math.pow(2, upgradeLevel);
            } else {
                return MekanismConfig.current().meka.mekaSuitShieldCapacity.val() * this.getProtectionShare() * upgradeLevel;
            }
        } else {
            return 0.0F;
        }
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
    public float getRecoveryRate(ItemStack stack) {
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.ENERGY_SHIELD_UNIT)) {
            int upgradeLevel = UpgradeHelper.getUpgradeLevel(stack, moduleUpgrade.ENERGY_SHIELD_UNIT);
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
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.ENERGY_SHIELD_UNIT)) {
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

    private static class FoundArmorDetails {

        private final EnergyUsageInfo usageInfo;
        private final ItemMekaSuitArmor armor;
        private double energyContainer;

        public FoundArmorDetails(double energyContainer, ItemMekaSuitArmor armor) {
            this.energyContainer = energyContainer;
            this.usageInfo = new EnergyUsageInfo(energyContainer);
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
}
