package mekanism.common.item.armor;

import com.brandon3055.draconicevolution.api.itemconfig.BooleanConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.ItemConfigFieldRegistry;
import com.brandon3055.draconicevolution.api.itemconfig.ToolConfigHelper;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import mekanism.api.EnumColor;
import mekanism.api.energy.IEnergizedItem;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import mekanism.client.model.mekasuitarmour.ModelMekAsuitHead;
import mekanism.client.model.mekasuitarmour.ModuleSolarHelmet;
import mekanism.common.Mekanism;
import mekanism.common.MekanismFluids;
import mekanism.common.MekanismItems;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.item.interfaces.IItemHUDProvider;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.*;
import net.minecraft.block.material.Material;
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
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class ItemMekAsuitHeadArmour extends ItemMekaSuitArmor implements IGasItem, IItemHUDProvider {

    public boolean isVision = false;

    public ItemMekAsuitHeadArmour() {
        super(EntityEquipmentSlot.HEAD);
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
        UpgradeHelper.setUpgradeLevel(fullUpgrades,moduleUpgrade.ADVANCED_INTERCEPTION_SYSTEM_UNIT,moduleUpgrade.ADVANCED_INTERCEPTION_SYSTEM_UNIT.getMax());
        setGas(fullUpgrades,new GasStack(MekanismFluids.NutritionalPaste,((IGasItem) fullUpgrades.getItem()).getMaxGas(fullUpgrades)));
        setEnergy(fullUpgrades, ((IEnergizedItem) fullUpgrades.getItem()).getMaxEnergy(fullUpgrades));
        if (Mekanism.hooks.DraconicEvolution){
            ItemNBTHelper.setFloat(fullUpgrades, "ProtectionPoints", getProtectionPoints(fullUpgrades));
        }
        list.add(fullUpgrades);
    }

    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        return armorType == EntityEquipmentSlot.HEAD;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        ModelMekAsuitHead armorModel = ModelMekAsuitHead.head;
        ModuleSolarHelmet Solar = ModuleSolarHelmet.solar;
        if (UpgradeHelper.isUpgradeInstalled(itemStack, moduleUpgrade.SolarRechargingUnit) && getSolar(itemStack)) {
            if (armorModel.helmet_armor.childModels.contains(armorModel.hide)) {
                armorModel.helmet_armor.childModels.remove(armorModel.hide);
            }
            if (!armorModel.bipedHead.childModels.contains(Solar.solar_helmet)) {
                armorModel.bipedHead.addChild(Solar.solar_helmet);
            }
        } else {
            if (armorModel.bipedHead.childModels.contains(Solar.solar_helmet)) {
                armorModel.bipedHead.childModels.remove(Solar.solar_helmet);
            }
            if (!armorModel.helmet_armor.childModels.contains(armorModel.hide)) {
                armorModel.helmet_armor.childModels.add(armorModel.hide);
            }
        }
        return armorModel;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        UUID uuid = new UUID((getTranslationKey(stack) + slot).hashCode(), 0);
        if (slot == EntityEquipmentSlot.HEAD) {
            multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(uuid, "Terrasteel modifier " + EntityEquipmentSlot.HEAD, 1.5D, 0));
        }
        return multimap;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @NotNull ItemStack armor, DamageSource source, double damage, int slot) {
        ArmorProperties properties = new ArmorProperties(0, 0, 0);
        if (this == MekanismItems.MekAsuitHelmet) {
            properties = new ArmorProperties(1, MekanismConfig.current().meka.MekaSuitHelmetDamageRatio.val(), MekanismConfig.current().meka.MekaSuitHelmetDamageMax.val());
            properties.Toughness = MekanismConfig.current().meka.mekaSuitToughness.val();
        }
        return properties;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @NotNull ItemStack armor, int slot) {
        if (armor.getItem() == MekanismItems.MekAsuitHelmet) {
            return MekanismConfig.current().meka.mekaSuitHelmetArmor.val();
        }
        return 0;
    }


    @Override
    public int getRate(ItemStack itemstack) {
        return MekanismConfig.current().meka.mekaSuitNutritionalTransferRate.val();
    }

    @Override
    public int addGas(ItemStack itemstack, GasStack stack) {
        if (getGas(itemstack) != null && getGas(itemstack).getGas() != stack.getGas()) {
            return 0;
        }
        if (stack.getGas() != MekanismFluids.NutritionalPaste) {
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
        return type == MekanismFluids.NutritionalPaste;
    }

    @Override
    public boolean canProvideGas(ItemStack itemstack, Gas type) {
        return false;
    }

    @Override
    public GasStack getGas(ItemStack itemstack) {
        return GasStack.readFromNBT(ItemDataUtils.getCompound(itemstack, "gasStored"));
    }

    @Override
    public void setGas(ItemStack itemstack, GasStack stack) {
        if (stack == null || stack.amount == 0) {
            ItemDataUtils.removeData(itemstack, "gasStored");
        } else {
            int amount = Math.max(0, Math.min(stack.amount, getMaxGas(itemstack)));
            GasStack gasStack = new GasStack(stack.getGas(), amount);
            ItemDataUtils.setCompound(itemstack, "gasStored", gasStack.write(new NBTTagCompound()));
        }
    }

    @Override
    public int getMaxGas(ItemStack itemstack) {
        return MekanismConfig.current().meka.mekaSuitNutritionalMaxStorage.val();
    }

    public int getStored(ItemStack itemstack) {
        return getGas(itemstack) != null ? getGas(itemstack).amount : 0;
    }


    public GasStack useGas(ItemStack itemstack, int amount) {
        GasStack gas = getGas(itemstack);
        if (gas == null) {
            return null;
        }
        Gas type = gas.getGas();
        int gasToUse = Math.min(gas.amount, Math.min(getRate(itemstack), amount));
        setGas(itemstack, new GasStack(type, gas.amount - gasToUse));
        return new GasStack(type, gasToUse);
    }


    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);
        if (!world.isRemote) {
            ItemStack headStack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

            if (headStack.getItem() instanceof ItemMekAsuitHeadArmour item) {
                if (UpgradeHelper.isUpgradeInstalled(itemStack, moduleUpgrade.NutritionalInjectionUnit) && getNutritional(itemStack)) {
                    if (player.canEat(false) && item.getGas(headStack) != null) {
                        int needed = Math.min(20 - player.getFoodStats().getFoodLevel(), item.getStored(headStack) / 50);
                        int toFeed = Math.min((int) MekanismConfig.current().meka.mekaSuitEnergyUsageNutritionalInjection.val(), needed);
                        if (toFeed > 0 && item.getGas(headStack).amount > needed) {
                            item.setEnergy(headStack, item.getEnergy(headStack) - toFeed);
                            item.useGas(headStack, needed * 50);
                            player.getFoodStats().addStats(needed, 0.8F);
                        }
                    }
                }

                if (UpgradeHelper.isUpgradeInstalled(itemStack, moduleUpgrade.ElectrolyticBreathingUnit) && getElectrolyticBreathing(itemStack)) {
                    if (player.isEntityAlive() && player.isInsideOfMaterial(Material.WATER)) {
                        if (!player.canBreatheUnderwater() && !player.capabilities.disableDamage) {
                            player.setAir(300);
                            item.setEnergy(headStack, item.getEnergy(headStack) - MekanismConfig.current().general.FROM_H2.val() * 2);
                        }
                        if (chestStack.getItem() instanceof ItemMekAsuitBodyArmour armour && UpgradeHelper.isUpgradeInstalled(chestStack, moduleUpgrade.JETPACK_UNIT)) {
                            int productionRate = (int) Math.pow(2, UpgradeHelper.getUpgradeLevel(chestStack, moduleUpgrade.ElectrolyticBreathingUnit));
                            GasStack stack = new GasStack(MekanismFluids.Hydrogen, productionRate * 2);
                            if (armour.getStored(chestStack) < armour.getMaxGas(chestStack)) {
                                armour.addGas(chestStack, stack);
                                item.setEnergy(headStack, item.getEnergy(headStack) - MekanismConfig.current().general.FROM_H2.val() * 2);
                            }
                        }
                    }
                }

                if (UpgradeHelper.isUpgradeInstalled(itemStack, moduleUpgrade.InhalationPurificationUnit)) {
                    List<PotionEffect> effects = Lists.newArrayList(player.getActivePotionEffects());
                    if (getInhalationBad(headStack)) {
                        for (PotionEffect potion : Collections2.filter(effects, potion -> potion.getPotion().isBadEffect())) {
                            item.setEnergy(headStack, item.getEnergy(headStack) - MekanismConfig.current().meka.mekaSuitEnergyUsagePotionTick.val());
                            player.removePotionEffect(potion.getPotion());
                        }
                    }
                    if (getInhalationGood(headStack)) {
                        for (PotionEffect potion : Collections2.filter(effects, potion -> !potion.getPotion().isBadEffect())) {
                            item.setEnergy(headStack, item.getEnergy(headStack) - 40000);
                            player.removePotionEffect(potion.getPotion());
                        }
                    }
                }

                if (UpgradeHelper.isUpgradeInstalled(itemStack, moduleUpgrade.VisionEnhancementUnit) && ((getVisionAuto(itemStack) && !player.getEntityWorld().isDaytime() && !player.getEntityWorld().provider.isNether()) || getVision(itemStack))) {
                    isVision = true;
                    item.setEnergy(headStack, item.getEnergy(headStack) - MekanismConfig.current().meka.mekaSuitEnergyUsageVisionEnhancement.val());
                } else {
                    isVision = false;
                }

                if (UpgradeHelper.isUpgradeInstalled(headStack, moduleUpgrade.SolarRechargingUnit) && getSolar(headStack)) {
                    if (player.getEntityWorld().isDaytime() && player.getEntityWorld().canSeeSky(player.getPosition())) {
                        Biome b = player.getEntityWorld().provider.getBiomeForCoords(player.getPosition());
                        float tempEff = 0.3f * (0.8f - b.getTemperature(player.getPosition()));
                        float humidityEff = -0.3f * (b.canRain() ? b.getRainfall() : 0.0f);
                        boolean needsRainCheck = b.canRain();
                        double peakOutput = MekanismConfig.current().meka.mekaSuitSolarRechargingRate.val() * (1.0f + tempEff + humidityEff);
                        float brightness = player.getEntityWorld().getSunBrightnessFactor(1.0f);
                        double production = peakOutput * brightness;
                        if (needsRainCheck && (world.isRaining() || world.isThundering())) {
                            production *= 0.2;
                        }
                        item.setEnergy(headStack, item.getEnergy(headStack) + production * UpgradeHelper.getUpgradeLevel(headStack, moduleUpgrade.SolarRechargingUnit));
                    }
                }
            }
        }
    }

    @Override
    public double getShieldingByArmor() {
        return MekanismConfig.current().meka.mekaSuitHelmetShielding.val();
    }

    @Override
    public List<moduleUpgrade> getValidModule(ItemStack stack) {
        List<moduleUpgrade> list = super.getValidModule(stack);
        list.add(moduleUpgrade.SolarRechargingUnit);
        list.add(moduleUpgrade.ElectrolyticBreathingUnit);
        list.add(moduleUpgrade.VisionEnhancementUnit);
        list.add(moduleUpgrade.InhalationPurificationUnit);
        list.add(moduleUpgrade.NutritionalInjectionUnit);
        list.add(moduleUpgrade.EMERGENCY_RESCUE);
        if (UpgradeHelper.getUpgradeLevel(stack, moduleUpgrade.EMERGENCY_RESCUE) == moduleUpgrade.EMERGENCY_RESCUE.getMax() && UpgradeHelper.getUpgradeLevel(stack, moduleUpgrade.ENERGY_SHIELD_UNIT) == moduleUpgrade.ENERGY_SHIELD_UNIT.getMax()) {
            list.add(moduleUpgrade.ADVANCED_INTERCEPTION_SYSTEM_UNIT);
        }
        return list;
    }


    @Override
    public void addHUDStrings(List<String> list, EntityPlayer player, ItemStack stack, EntityEquipmentSlot slotType) {
        if (slotType == getEquipmentSlot()) {
            if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.NutritionalInjectionUnit) && getNutritional(stack)) {
                list.add(LangUtils.localize("tooltip.autoeatgas.stored") + " " + EnumColor.ORANGE + (getStored(stack) > 0 ? getStored(stack) : LangUtils.localize("tooltip.noGas")));
            }
            if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.EMERGENCY_RESCUE) && getEmergency(stack)) {
                list.add(LangUtils.localize("tooltip.meka_head.Emergency_rescue") + " " + UpgradeHelper.getUpgradeLevel(stack, moduleUpgrade.EMERGENCY_RESCUE));
            }
        }
    }


    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public ItemConfigFieldRegistry getFields(ItemStack stack, ItemConfigFieldRegistry registry) {
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.SolarRechargingUnit)) {
            registry.register(stack, new BooleanConfigField("SolarRechargingUnit", true, "config.field.SolarRechargingUnit.description"));
        }
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.ElectrolyticBreathingUnit)) {
            registry.register(stack, new BooleanConfigField("ElectrolyticBreathingUnit", true, "config.field.ElectrolyticBreathingUnit.description"));
        }
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.VisionEnhancementUnit)) {
            registry.register(stack, new BooleanConfigField("VisionAuto", true, "config.field.VisionAuto.description"));
            registry.register(stack, new BooleanConfigField("Vision", false, "config.field.Vision.description"));
        }
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.InhalationPurificationUnit)) {
            registry.register(stack, new BooleanConfigField("InhalationBad", true, "config.field.InhalationBad.description"));
            registry.register(stack, new BooleanConfigField("InhalationGood", false, "config.field.InhalationGood.description"));
        }
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.NutritionalInjectionUnit)) {
            registry.register(stack, new BooleanConfigField("Nutritional", true, "config.field.Nutritional.description"));
        }
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.EMERGENCY_RESCUE)) {
            registry.register(stack, new BooleanConfigField("emergencyRescue", true, "config.field.emergencyRescue.description"));
        }
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.ADVANCED_INTERCEPTION_SYSTEM_UNIT)) {
            registry.register(stack, new BooleanConfigField("Interception", true, "config.field.Interception.description"));
        }
        super.getFields(stack, registry);
        return registry;
    }


    public boolean getSolar(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDESolar(stack);
        } else {
            return true;
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDESolar(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("SolarRechargingUnit", stack);
    }


    public boolean getElectrolyticBreathing(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDEElectrolyticBreathing(stack);
        } else {
            return true;
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDEElectrolyticBreathing(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("ElectrolyticBreathingUnit", stack);
    }

    public boolean getVision(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDEVision(stack);
        } else {
            return false;
        }
    }

    public boolean getVisionAuto(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDEVisionAuto(stack);
        } else {
            return true;
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDEVision(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("Vision", stack);
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDEVisionAuto(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("VisionAuto", stack);
    }

    public boolean getInhalationBad(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDEInhalationBad(stack);
        } else {
            return true;
        }
    }

    public boolean getInhalationGood(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDEInhalationGood(stack);
        } else {
            return false;
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDEInhalationBad(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("InhalationBad", stack);
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDEInhalationGood(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("InhalationGood", stack);
    }

    public boolean getNutritional(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDENutritional(stack);
        } else {
            return true;
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDENutritional(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("Nutritional", stack);
    }

    public boolean getEmergency(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDEemergency(stack);
        } else {
            return true;
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDEemergency(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("emergencyRescue", stack);
    }

    public boolean getInterception(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDEInterception(stack);
        } else {
            return true;
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDEInterception(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("Interception", stack);
    }

}
