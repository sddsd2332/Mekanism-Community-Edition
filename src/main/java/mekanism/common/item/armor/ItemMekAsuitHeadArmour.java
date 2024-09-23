package mekanism.common.item.armor;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import mekanism.api.EnumColor;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import mekanism.client.model.mekasuitarmour.ModelMekAsuitHead;
import mekanism.client.model.mekasuitarmour.ModuleSolarHelmet;
import mekanism.common.Mekanism;
import mekanism.common.MekanismFluids;
import mekanism.common.MekanismItems;
import mekanism.common.config.MekanismConfig;
import mekanism.common.item.interfaces.IItemHUDProvider;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UpgradeHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class ItemMekAsuitHeadArmour extends ItemMekaSuitArmor implements IGasItem, IItemHUDProvider {

    public ItemMekAsuitHeadArmour() {
        super(EntityEquipmentSlot.HEAD);
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
        if (UpgradeHelper.isUpgradeInstalled(itemStack, moduleUpgrade.SolarRechargingUnit)) {
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
            properties = new ArmorProperties(1, MekanismConfig.current().general.MekaSuitHelmetDamageRatio.val(), MekanismConfig.current().general.MekaSuitHelmetDamageMax.val());
            properties.Toughness = 3.0F;
        }
        return properties;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @NotNull ItemStack armor, int slot) {
        if (armor.getItem() == MekanismItems.MekAsuitHelmet) {
            return 3;
        }
        return 0;
    }


    @Override
    public int getRate(ItemStack itemstack) {
        return 256;
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
        return 128000;
    }

    public int getStored(ItemStack itemstack) {
        return getGas(itemstack) != null ? getGas(itemstack).amount : 0;
    }

    public void useGas(ItemStack itemstack) {
        GasStack gas = getGas(itemstack);
        if (gas != null) {
            setGas(itemstack, new GasStack(gas.getGas(), gas.amount - 1));
        }
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
            PotionEffect nv = player.getActivePotionEffect(MobEffects.NIGHT_VISION);
            if (headStack.getItem() instanceof ItemMekAsuitHeadArmour item) {
                if (UpgradeHelper.isUpgradeInstalled(itemStack, moduleUpgrade.NutritionalInjectionUnit)) {
                    if (player.canEat(false) && item.getGas(headStack) != null) {
                        int needed = Math.min(20 - player.getFoodStats().getFoodLevel(), item.getStored(headStack) / 50);
                        int toFeed = Math.min(20000, needed);
                        if (toFeed > 0 && item.getGas(headStack).amount > needed) {
                            item.setEnergy(headStack, item.getEnergy(headStack) - toFeed);
                            item.useGas(headStack);
                            item.useGas(headStack, needed * 50);
                            player.getFoodStats().addStats(needed, 0.8F);
                        }
                    }
                }

                if (UpgradeHelper.isUpgradeInstalled(itemStack, moduleUpgrade.ElectrolyticBreathingUnit)) {
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
                    //if ()
                    for (PotionEffect potion : Collections2.filter(effects, potion -> potion.getPotion().isBadEffect())) {
                        item.setEnergy(headStack, item.getEnergy(headStack) - 40000);
                        player.removePotionEffect(potion.getPotion());
                    }
                /*if ()
                for (PotionEffect potion : Collections2.filter(effects, potion -> !potion.getPotion().isBadEffect())) {
                    item.setEnergy(headStack, item.getEnergy(headStack) - 40000);
                    player.removePotionEffect(potion.getPotion());
                }
                 */
                }

                if (UpgradeHelper.isUpgradeInstalled(itemStack, moduleUpgrade.VisionEnhancementUnit)) {
                    if (!player.getEntityWorld().isDaytime() && !player.getEntityWorld().provider.isNether()) {
                        if (nv == null) {
                            player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
                        } else {
                            nv.duration = Integer.MAX_VALUE;
                        }
                        item.setEnergy(headStack, item.getEnergy(headStack) - 200);
                    } else if (nv != null) {
                        nv.duration = 0;
                    }
                }

                if (UpgradeHelper.isUpgradeInstalled(headStack, moduleUpgrade.SolarRechargingUnit)) {
                    if (player.getEntityWorld().isDaytime() && player.getEntityWorld().canSeeSky(player.getPosition())) {
                        Biome b = player.getEntityWorld().provider.getBiomeForCoords(player.getPosition());
                        float tempEff = 0.3f * (0.8f - b.getTemperature(player.getPosition()));
                        float humidityEff = -0.3f * (b.canRain() ? b.getRainfall() : 0.0f);
                        boolean needsRainCheck = b.canRain();
                        double peakOutput = 500 * (1.0f + tempEff + humidityEff);
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
    double getShieldingByArmor() {
        return 25;
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
        return list;
    }


    @Override
    public void addHUDStrings(List<String> list, EntityPlayer player, ItemStack stack, EntityEquipmentSlot slotType) {
        if (slotType == getEquipmentSlot()) {
            if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.NutritionalInjectionUnit)) {
                list.add(LangUtils.localize("tooltip.autoeatgas.stored") + " " + EnumColor.ORANGE + (getStored(stack) > 0 ? getStored(stack) : LangUtils.localize("tooltip.noGas")));
            }
            if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.EMERGENCY_RESCUE)) {
                list.add(LangUtils.localize("tooltip.meka_head.Emergency_rescue") + " " + UpgradeHelper.getUpgradeLevel(stack, moduleUpgrade.EMERGENCY_RESCUE));
            }
            if (!Mekanism.hooks.DraconicEvolution) {
                list.add(LangUtils.localize("tooltip.meka_head.storedEnergy") + " " + MekanismUtils.getEnergyDisplay(getEnergy(stack)));
            }

        }
    }
}
