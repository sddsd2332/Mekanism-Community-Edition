package mekanism.common.item.armor;

import com.brandon3055.draconicevolution.api.itemconfig.BooleanConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.ItemConfigFieldRegistry;
import com.brandon3055.draconicevolution.api.itemconfig.ToolConfigHelper;
import com.google.common.collect.Multimap;
import mekanism.client.model.mekasuitarmour.ModelMekAsuitLeg;
import mekanism.common.Mekanism;
import mekanism.common.MekanismItems;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.item.interfaces.IItemHUDProvider;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UpgradeHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static mekanism.common.util.UpgradeHelper.isUpgradeInstalled;

public class ItemMekAsuitLegsArmour extends ItemMekaSuitArmor implements IItemHUDProvider {

    public ItemMekAsuitLegsArmour() {
        super(EntityEquipmentSlot.LEGS);
    }

    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        return armorType == EntityEquipmentSlot.LEGS;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return ModelMekAsuitLeg.leg;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        UUID uuid = new UUID((getTranslationKey(stack) + slot).hashCode(), 0);
        if (slot == EntityEquipmentSlot.LEGS) {
            multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(uuid, "Terrasteel modifier " + EntityEquipmentSlot.LEGS, 3D, 0));
        }
        return multimap;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @NotNull ItemStack armor, DamageSource source, double damage, int slot) {
        ArmorProperties properties = new ArmorProperties(0, 0, 0);
        if (this == MekanismItems.MekAsuitLeggings) {
            properties = new ArmorProperties(1, MekanismConfig.current().meka.MekaSuitPantsDamageRatio.val(), MekanismConfig.current().meka.MekaSuitPantsDamageMax.val());
            properties.Toughness = MekanismConfig.current().meka.mekaSuitToughness.val();
        }

        return properties;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @NotNull ItemStack armor, int slot) {
        if (armor.getItem() == MekanismItems.MekAsuitLeggings) {
            return MekanismConfig.current().meka.mekaSuitPantsArmor.val();
        }
        return 0;
    }


    @Override
    public List<moduleUpgrade> getValidModule(ItemStack stack) {
        List<moduleUpgrade> list = super.getValidModule(stack);
        list.add(moduleUpgrade.GYROSCOPIC_STABILIZATION_UNIT);
        list.add(moduleUpgrade.GEOTHERMAL_GENERATOR_UNIT);
        list.add(moduleUpgrade.LOCOMOTIVE_BOOSTING_UNIT);
        list.add(moduleUpgrade.HYDROSTATIC_REPULSOR_UNIT);
        return list;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);
        if (!world.isRemote) {
            ItemStack legStack = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
            if (legStack.getItem() instanceof ItemMekAsuitLegsArmour armour) {
                if (isUpgradeInstalled(legStack, moduleUpgrade.GEOTHERMAL_GENERATOR_UNIT) && getGeothermal(legStack)) {
                    if (player.isInLava() || player.isBurning()) {
                        armour.setEnergy(legStack, armour.getEnergy(legStack) + MekanismConfig.current().meka.mekaSuitGeothermalChargingRate.val() * UpgradeHelper.getUpgradeLevel(legStack, moduleUpgrade.GEOTHERMAL_GENERATOR_UNIT) * 200);
                    }
                }
                if (isUpgradeInstalled(legStack, moduleUpgrade.HYDROSTATIC_REPULSOR_UNIT)) {
                    if (gethydrostatic(legStack) && player.isInsideOfMaterial(Material.WATER) && !player.canBreatheUnderwater() && armour.getEnergy(legStack) > MekanismConfig.current().meka.mekaSuitEnergyUsageHydrostaticRepulsion.val()) {
                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, legStack) == 0) {
                            legStack.addEnchantment(Enchantments.DEPTH_STRIDER, UpgradeHelper.getUpgradeLevel(legStack, moduleUpgrade.HYDROSTATIC_REPULSOR_UNIT));
                        }
                        armour.setEnergy(legStack, armour.getEnergy(legStack) - MekanismConfig.current().meka.mekaSuitEnergyUsageHydrostaticRepulsion.val());
                    } else {
                        NBTTagList list = legStack.getEnchantmentTagList();
                        for (int i = 0; i < list.tagCount(); i++) {
                            NBTTagCompound compound = list.getCompoundTagAt(i);
                            int id = compound.getShort("id");
                            Enchantment e = Enchantment.getEnchantmentByID(id);
                            if (e == Enchantments.DEPTH_STRIDER) {
                                list.removeTag(i);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public double getShieldingByArmor() {
        return MekanismConfig.current().meka.mekaSuitPantsShielding.val();
    }

    @Override
    public void addHUDStrings(List<String> list, EntityPlayer player, ItemStack stack, EntityEquipmentSlot slotType) {
        if (slotType == getEquipmentSlot()) {
            if (!Mekanism.hooks.DraconicEvolution) {
                list.add(LangUtils.localize("tooltip.meka_legs.storedEnergy") + " " + MekanismUtils.getEnergyDisplay(getEnergy(stack)));
            }
        }
    }


    @Override
    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public ItemConfigFieldRegistry getFields(ItemStack stack, ItemConfigFieldRegistry registry) {
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.GYROSCOPIC_STABILIZATION_UNIT)) {
            registry.register(stack, new BooleanConfigField("Gyroscopic", true, "config.field.Gyroscopic.description"));
        }
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.GEOTHERMAL_GENERATOR_UNIT)) {
            registry.register(stack, new BooleanConfigField("Geothermal", true, "config.field.Geothermal.description"));
        }
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.LOCOMOTIVE_BOOSTING_UNIT)) {
            registry.register(stack, new BooleanConfigField("Locomotive", true, "config.field.Locomotive.description"));
        }
        if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.HYDROSTATIC_REPULSOR_UNIT)) {
            registry.register(stack, new BooleanConfigField("hydrostatic", true, "config.field.hydrostatic.description"));
        }
        super.getFields(stack, registry);
        return registry;
    }

    public boolean getGyroscopic(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDEGyroscopic(stack);
        } else {
            return true;
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDEGyroscopic(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("Gyroscopic", stack);
    }

    public boolean getGeothermal(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDEGeothermal(stack);
        } else {
            return true;
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDEGeothermal(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("Geothermal", stack);
    }

    public boolean getLocomotive(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDELocomotive(stack);
        } else {
            return true;
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDELocomotive(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("Locomotive", stack);
    }

    public boolean gethydrostatic(ItemStack stack) {
        if (Mekanism.hooks.DraconicEvolution) {
            return getDEhydrostatic(stack);
        } else {
            return true;
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public boolean getDEhydrostatic(ItemStack stack) {
        return ToolConfigHelper.getBooleanField("hydrostatic", stack);
    }
}
