package mekanism.common.item.armor;


import cofh.redstoneflux.api.IEnergyContainerItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import mekanism.api.EnumColor;
import mekanism.api.energy.IEnergizedItem;
import mekanism.common.Mekanism;
import mekanism.common.base.IModuleUpgrade;
import mekanism.common.capabilities.ItemCapabilityWrapper;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.integration.forgeenergy.ForgeEnergyItemWrapper;
import mekanism.common.integration.ic2.IC2ItemManager;
import mekanism.common.integration.redstoneflux.RFIntegration;
import mekanism.common.integration.tesla.TeslaItemWrapper;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Optional.InterfaceList({
        @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = MekanismHooks.IC2_MOD_ID),
        @Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyContainerItem", modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
})
public abstract class ItemMekaSuitArmor extends ItemArmor implements IEnergizedItem, ISpecialElectricItem, IEnergyContainerItem, ISpecialArmor, IModuleUpgrade {

    private final float absorption;

    public ItemMekaSuitArmor(EntityEquipmentSlot slot) {
        super(EnumHelper.addArmorMaterial("MEKASUIT", "mekasuit", 0, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0), 3, slot);
        setMaxStackSize(1);
        setCreativeTab(Mekanism.tabMekanism);
        switch (armorType) {
            case HEAD -> {
                absorption = 0.15F;
            }
            case CHEST -> {
                absorption = 0.4F;
            }
            case LEGS -> {
                absorption = 0.3F;
            }
            case FEET -> {
                absorption = 0.15F;
            }
            default -> throw new IllegalArgumentException("Unknown Equipment Slot Type");
        }
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
        if (ItemDataUtils.hasData(itemstack, "module")) {
            Map<moduleUpgrade, Integer> module = moduleUpgrade.buildMap(ItemDataUtils.getDataMap(itemstack));
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                list.add(LangUtils.localize("tooltip.hold") + " " + EnumColor.AQUA + "shift" + EnumColor.GREY + " " + LangUtils.localize("tooltip.forDetails"));
            } else {
                list.add(EnumColor.ORANGE + LangUtils.localize("tooltip.hold_for_modules") + ": ");
                for (Map.Entry<moduleUpgrade, Integer> entry : module.entrySet()) {
                    list.add("- " + entry.getKey().getLangName() + (entry.getKey().canMultiply() ? ": " + EnumColor.GREY + "x" + entry.getValue() : ""));
                }
            }
        }
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
            if (dataMap.isEmpty()) {
                itemStack.setTagCompound(null);
            }
        } else {
            ItemDataUtils.setDouble(itemStack, "energyStored", Math.max(Math.min(amount, getMaxEnergy(itemStack)), 0));
        }
    }

    @Override
    public double getMaxEnergy(ItemStack itemStack) {
        //  return 4096000000D;
        return ItemDataUtils.hasData(itemStack, "module") ? MekanismUtils.getModuleMaxEnergy(itemStack, 16000000D) : 16000000D;
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
            upgrades.put(upgrade, upgrade.getMax());
            moduleUpgrade.saveMap(upgrades, ItemDataUtils.getDataMap(fullUpgrade));
        }
        upgrades.clear();
        setEnergy(fullUpgrade, ((IEnergizedItem) fullUpgrade.getItem()).getMaxEnergy(fullUpgrade));
        list.add(fullUpgrade);

    }

    @Override
    public double getMaxTransfer(ItemStack itemStack) {
        return getMaxEnergy(itemStack) * 0.005;
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
        return true;
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
        }};
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (!world.isRemote) {
            if (itemStack.getItem() instanceof ItemMekaSuitArmor) {
                if (ItemDataUtils.hasData(itemStack, "module")) {
                    Map<moduleUpgrade, Integer> module = moduleUpgrade.buildMap(ItemDataUtils.getDataMap(itemStack));
                    upgrades.putAll(module);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return false;
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
                        if (upgrade == moduleUpgrade.InhalationPurificationUnit && details.armor.isUpgradeInstalled(stack, moduleUpgrade.InhalationPurificationUnit)) {
                            absorption = 1F;
                            ratioAbsorbed += absorbDamage(details.usageInfo, amount, absorption, ratioAbsorbed, 1000D);
                        }
                        if (upgrade == moduleUpgrade.GEOTHERMAL_GENERATOR_UNIT && details.armor.isUpgradeInstalled(stack, moduleUpgrade.GEOTHERMAL_GENERATOR_UNIT)) {
                            absorption = (float) (0.8 * (details.armor.getUpgrades(moduleUpgrade.GEOTHERMAL_GENERATOR_UNIT) / 8));
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
                        absorbRatio = 1F;
                    }
                    float absorption = details.armor.absorption * absorbRatio;
                    ratioAbsorbed += absorbDamage(details.usageInfo, amount, absorption, ratioAbsorbed, 100000D);
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


    private static class FoundArmorDetails {

        private double energyContainer;
        private final EnergyUsageInfo usageInfo;
        private final ItemMekaSuitArmor armor;

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
}
