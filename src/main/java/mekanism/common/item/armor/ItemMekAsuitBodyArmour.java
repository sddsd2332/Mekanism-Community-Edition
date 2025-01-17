package mekanism.common.item.armor;

import codechicken.lib.math.MathHelper;
import com.brandon3055.draconicevolution.DEConfig;
import com.brandon3055.draconicevolution.api.itemconfig.BooleanConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.IntegerConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.ItemConfigFieldRegistry;
import com.brandon3055.draconicevolution.api.itemconfig.ToolConfigHelper;
import com.google.common.collect.Multimap;
import mekanism.api.EnumColor;
import mekanism.api.NBTConstants;
import mekanism.api.energy.IEnergizedItem;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import mekanism.client.model.mekasuitarmour.ModelMekAsuitBody;
import mekanism.client.model.mekasuitarmour.ModuleGravitational;
import mekanism.client.model.mekasuitarmour.ModuleJetpack;
import mekanism.common.Mekanism;
import mekanism.common.MekanismFluids;
import mekanism.common.MekanismItems;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;

import mekanism.common.item.interfaces.IJetpackItem;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.ItemNBTHelper;
import mekanism.common.util.LangUtils;
import mekanism.common.util.UpgradeHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

import static com.brandon3055.draconicevolution.api.itemconfig.IItemConfigField.EnumControlType.SLIDER;

public class ItemMekAsuitBodyArmour extends ItemMekaSuitArmor implements IGasItem, IJetpackItem {

    public ItemMekAsuitBodyArmour() {
        super(EntityEquipmentSlot.CHEST);
    }

    public int ArmourTick;

    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        return armorType == EntityEquipmentSlot.CHEST;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tabs, @Nonnull NonNullList<ItemStack> list) {
        super.getSubItems(tabs,list);
        if (!isInCreativeTab(tabs)) {
            return;
        }
        ItemStack fullUpgrades = new ItemStack(this);
        for (moduleUpgrade upgrade : getValidModule(fullUpgrades)) {
            UpgradeHelper.setUpgradeLevel(fullUpgrades, upgrade, upgrade.getMax());
        }
        setGas(fullUpgrades,new GasStack(MekanismFluids.Hydrogen,((IGasItem) fullUpgrades.getItem()).getMaxGas(fullUpgrades)));
        setEnergy(fullUpgrades, ((IEnergizedItem) fullUpgrades.getItem()).getMaxEnergy(fullUpgrades));
        if (Mekanism.hooks.DraconicEvolution){
            ItemNBTHelper.setFloat(fullUpgrades, "ProtectionPoints", getProtectionPoints(fullUpgrades));
        }
        list.add(fullUpgrades);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        ModelMekAsuitBody armorModel = ModelMekAsuitBody.armorModel;
        ModuleJetpack jetpack = ModuleJetpack.jetpacks;
        ModuleGravitational gravitational = ModuleGravitational.gravitational;

        if (UpgradeHelper.isUpgradeInstalled(itemStack, moduleUpgrade.JETPACK_UNIT) && getJetpackMode(itemStack) != JetpackMode.DISABLED) {
            if (!armorModel.bipedBody.childModels.contains(jetpack.jetpack)) {
                armorModel.bipedBody.addChild(jetpack.jetpack);
            }
        } else {
            if (armorModel.bipedBody.childModels.contains(jetpack.jetpack)) {
                armorModel.bipedBody.childModels.remove(jetpack.jetpack);
            }
        }

        if (UpgradeHelper.isUpgradeInstalled(itemStack, moduleUpgrade.GRAVITATIONAL_MODULATING_UNIT) && getJetpackMode(itemStack) == JetpackMode.DISABLED) {
            if (!armorModel.bipedBody.childModels.contains(gravitational.gravitational_modulator)) {
                armorModel.bipedBody.addChild(gravitational.gravitational_modulator);
            }
        } else {
            if (armorModel.bipedBody.childModels.contains(gravitational.gravitational_modulator)) {
                armorModel.bipedBody.childModels.remove(gravitational.gravitational_modulator);
            }
        }
        return armorModel;
    }


    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @NotNull ItemStack armor, DamageSource source, double damage, int slot) {
        ArmorProperties properties = new ArmorProperties(0, 0, 0);
        if (this == MekanismItems.MekAsuitChestplate) {
            properties = new ArmorProperties(1, MekanismConfig.current().meka.MekaSuitBodyarmorDamageRatio.val(), MekanismConfig.current().meka.MekaSuitBodyarmorDamageMax.val());
            properties.Toughness = MekanismConfig.current().meka.mekaSuitToughness.val();
        }

        return properties;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        UUID uuid = new UUID((getTranslationKey(stack) + slot).hashCode(), 0);
        if (slot == EntityEquipmentSlot.CHEST) {
            multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(uuid, "Terrasteel modifier " + EntityEquipmentSlot.CHEST, 4D, 0));
        }
        return multimap;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @NotNull ItemStack armor, int slot) {
        if (armor.getItem() == MekanismItems.MekAsuitChestplate) {
            return MekanismConfig.current().meka.mekaSuitBodyArmorArmor.val();
        }
        return 0;
    }


    @Override
    public List<moduleUpgrade> getValidModule(ItemStack stack) {
        List<moduleUpgrade> list = super.getValidModule(stack);
        list.add(moduleUpgrade.JETPACK_UNIT);
        list.add(moduleUpgrade.CHARGE_DISTRIBUTION_UNIT);
        list.add(moduleUpgrade.GRAVITATIONAL_MODULATING_UNIT);
        list.add(moduleUpgrade.HEALTH_REGENERATION);
        return list;
    }


    private void chargeSuit(EntityPlayer player) { //wip
        double energy;
        double energyMax;
        double headEnergy = 0;
        double headEnergyMax = 0;
        double BodyEnergy = 0;
        double BodyEnergyMax = 0;
        double LegsEnergy = 0;
        double LegsEnergyMax = 0;
        double FeetEnergy = 0;
        double FeetEnergyMax = 0;
        for (ItemStack stack : player.getArmorInventoryList()) {
            if (stack.getItem() instanceof ItemMekAsuitHeadArmour armour) {
                headEnergy = armour.getEnergy(stack);
                headEnergyMax = armour.getMaxEnergy(stack);
            }
            if (stack.getItem() instanceof ItemMekAsuitBodyArmour armour) {
                BodyEnergy = armour.getEnergy(stack);
                BodyEnergyMax = armour.getMaxEnergy(stack);
            }
            if (stack.getItem() instanceof ItemMekAsuitLegsArmour armour) {
                LegsEnergy = armour.getEnergy(stack);
                LegsEnergyMax = armour.getMaxEnergy(stack);
            }
            if (stack.getItem() instanceof ItemMekAsuitFeetArmour armour) {
                FeetEnergy = armour.getEnergy(stack);
                FeetEnergyMax = armour.getMaxEnergy(stack);
            }
        }
        energy = headEnergy + BodyEnergy + LegsEnergy + FeetEnergy;
        energyMax = headEnergyMax + BodyEnergyMax + LegsEnergyMax + FeetEnergyMax;
        double FinalEnergy = energy / energyMax;
        for (ItemStack stack : player.getArmorInventoryList()) {
            if (stack.getItem() instanceof ItemMekAsuitHeadArmour armour) {
                armour.setEnergy(stack, FinalEnergy * headEnergyMax);
            }
            if (stack.getItem() instanceof ItemMekAsuitBodyArmour armour) {
                armour.setEnergy(stack, FinalEnergy * BodyEnergyMax);
            }
            if (stack.getItem() instanceof ItemMekAsuitLegsArmour armour) {
                armour.setEnergy(stack, FinalEnergy * LegsEnergyMax);
            }
            if (stack.getItem() instanceof ItemMekAsuitFeetArmour armour) {
                armour.setEnergy(stack, FinalEnergy * FeetEnergyMax);
            }
        }
    }

    @Override
    public boolean canUseJetpack(ItemStack stack) {
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.JETPACK_UNIT)) {
            return getStored(stack) > 0;
        }
        return false;
    }


    @Override
    public void useJetpackFuel(ItemStack stack) {
        GasStack gas = getGas(stack);
        if (gas != null) {
            setGas(stack, new GasStack(gas.getGas(), gas.amount - 1));
        }
    }


    @Override
    public int getRate(ItemStack itemstack) {
        return MekanismConfig.current().meka.mekaSuitJetpackTransferRate.val();
    }

    @Override
    public int addGas(ItemStack itemstack, GasStack stack) {
        if (getGas(itemstack) != null && getGas(itemstack).getGas() != stack.getGas()) {
            return 0;
        }
        if (stack.getGas() != MekanismFluids.Hydrogen) {
            return 0;
        }
        int toUse = Math.min(getMaxGas(itemstack) - getStored(itemstack), Math.min(getRate(itemstack), stack.amount));
        setGas(itemstack, new GasStack(stack.getGas(), getStored(itemstack) + toUse));
        return toUse;
    }

    @Override
    public GasStack removeGas(ItemStack itemstack, int amount) {
        return null;
    }

    @Override
    public boolean canReceiveGas(ItemStack itemstack, Gas type) {
        return type == MekanismFluids.Hydrogen;
    }

    @Override
    public boolean canProvideGas(ItemStack itemstack, Gas type) {
        return false;
    }

    @Override
    public GasStack getGas(ItemStack itemstack) {
        return GasStack.readFromNBT(ItemDataUtils.getCompound(itemstack, "stored"));
    }

    @Override
    public void setGas(ItemStack itemstack, GasStack stack) {
        if (stack == null || stack.amount == 0) {
            ItemDataUtils.removeData(itemstack, "stored");
        } else {
            int amount = Math.max(0, Math.min(stack.amount, getMaxGas(itemstack)));
            GasStack gasStack = new GasStack(stack.getGas(), amount);
            ItemDataUtils.setCompound(itemstack, "stored", gasStack.write(new NBTTagCompound()));
        }
    }

    @Override
    public int getMaxGas(ItemStack itemstack) {
        return MekanismConfig.current().meka.mekaSuitJetpackMaxStorage.val();
    }


    public int getStored(ItemStack itemstack) {
        return getGas(itemstack) != null ? getGas(itemstack).amount : 0;
    }


    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);
        if (!world.isRemote) {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            ItemStack headStack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            boolean isHealth = false;
            if (headStack.getItem() instanceof ItemMekAsuitHeadArmour armour) {
                isHealth = UpgradeHelper.isUpgradeInstalled(headStack, moduleUpgrade.ADVANCED_INTERCEPTION_SYSTEM_UNIT) && armour.getInterception(headStack);
            }
            if (chestStack.getItem() instanceof ItemMekAsuitBodyArmour) {
                if (UpgradeHelper.isUpgradeInstalled(chestStack, moduleUpgrade.CHARGE_DISTRIBUTION_UNIT) && getCharge(chestStack)) {
                    chargeSuit(player);
                }
                if (UpgradeHelper.isUpgradeInstalled(chestStack, moduleUpgrade.HEALTH_REGENERATION) && gethealth(chestStack)) {
                    if (isHealth) {
                        ArmourTick++;
                        if (player.getHealth() < player.getMaxHealth() && ArmourTick % 20 == 0) {
                            player.heal(UpgradeHelper.getUpgradeLevel(chestStack, moduleUpgrade.HEALTH_REGENERATION));
                            ArmourTick = 0;
                        }
                    } else {
                        if (player.getHealth() < player.getMaxHealth()) {
                            player.heal(player.getMaxHealth());
                        }
                    }
                }
            }
        }
    }

    @Override
    public double getShieldingByArmor() {
        return MekanismConfig.current().meka.mekaSuitBodyShielding.val();
    }

    @Override
    public void addHUDStrings(List<String> list, EntityPlayer player, ItemStack stack, EntityEquipmentSlot slotType) {
        if (slotType == getEquipmentSlot()) {
            if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.JETPACK_UNIT) && getJetpackMode(stack) != JetpackMode.DISABLED) {
                list.add(LangUtils.localize("tooltip.jetpack.mode") + " " + getJetpackMode(stack).getName());
                list.add(LangUtils.localize("tooltip.jetpack.stored") + " " + EnumColor.ORANGE + (getStored(stack) > 0 ? getStored(stack) : LangUtils.localize("tooltip.noGas")));
            }
        }
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public ItemConfigFieldRegistry getFields(ItemStack stack, ItemConfigFieldRegistry registry) {
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.CHARGE_DISTRIBUTION_UNIT)) {
            registry.register(stack, new BooleanConfigField("Charge", true, "config.field.Charge.description"));
        }
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.HEALTH_REGENERATION)) {
            registry.register(stack, new BooleanConfigField("health", true, "config.field.health.description"));
        }
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.GRAVITATIONAL_MODULATING_UNIT)) {
            int speedLimit = MathHelper.clip(DEConfig.flightSpeedLimit != -1 ? DEConfig.flightSpeedLimit : 600, 0, 1200);
            registry.register(stack, new IntegerConfigField("armorFSpeedModifier", 0, 0, getJetpackMode(stack) != JetpackMode.DISABLED ? 0 : speedLimit, "config.field.armorFSpeedModifier.description", SLIDER).setPrefix("+").setExtension("%"));
            registry.register(stack, new IntegerConfigField("armorVFSpeedModifier", 0, 0, getJetpackMode(stack) != JetpackMode.DISABLED ? 0 : speedLimit, "config.field.armorVFSpeedModifier.description", SLIDER).setPrefix("+").setExtension("%"));
            registry.register(stack, new BooleanConfigField("armorInertiaCancel", false, "config.field.armorInertiaCancel.description"));
            registry.register(stack, new BooleanConfigField("armorFlightLock", false, "config.field.armorFlightLock.description"));
        }
        super.getFields(stack, registry);
        return registry;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public float getFlightSpeedModifier(ItemStack stack, EntityPlayer player) {
        if (getJetpackMode(stack) != JetpackMode.DISABLED) {
            return 0;
        }
        int value = ToolConfigHelper.getIntegerField("armorFSpeedModifier", stack);
        if (DEConfig.flightSpeedLimit > -1 && value > DEConfig.flightSpeedLimit) {
            value = DEConfig.flightSpeedLimit;
        }
        float modifier = value / 100F;
        return modifier;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public float getFlightVModifier(ItemStack stack, EntityPlayer player) {
        if (getJetpackMode(stack) != JetpackMode.DISABLED) {
            return 0;
        }
        return ToolConfigHelper.getIntegerField("armorVFSpeedModifier", stack) / 100F;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean[] hasFlight(ItemStack stack) {
        return new boolean[]{getJetpackMode(stack) == JetpackMode.DISABLED && UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.GRAVITATIONAL_MODULATING_UNIT),
                ToolConfigHelper.getBooleanField("armorFlightLock", stack) && getJetpackMode(stack) == JetpackMode.DISABLED && UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.GRAVITATIONAL_MODULATING_UNIT),
                ToolConfigHelper.getBooleanField("armorInertiaCancel", stack) && getJetpackMode(stack) == JetpackMode.DISABLED && UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.GRAVITATIONAL_MODULATING_UNIT)};
    }

    public boolean getCharge(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDECharge(stack);
        } else {
            return true;
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDECharge(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("Charge", stack);
    }

    public boolean gethealth(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDEhealth(stack);
        } else {
            return true;
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDEhealth(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("health", stack);
    }


    @Override
    public JetpackMode getJetpackMode(ItemStack stack) {
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.JETPACK_UNIT)) {
            return JetpackMode.byIndexStatic(ItemDataUtils.getInt(stack, NBTConstants.MODE));
        } else {
            return JetpackMode.DISABLED;
        }
    }
}
