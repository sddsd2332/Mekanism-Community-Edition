package mekanism.common.item.armor;

import com.google.common.collect.Multimap;
import mekanism.client.model.mekasuitarmour.ModelMekAsuitLeg;
import mekanism.common.MekanismItems;
import mekanism.common.config.MekanismConfig;
import mekanism.common.item.interfaces.IItemHUDProvider;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

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
        ModelMekAsuitLeg armorModel = ModelMekAsuitLeg.leg;
        Render<AbstractClientPlayer> render = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(entityLiving);
        if (render instanceof RenderPlayer) {
            armorModel.setModelAttributes(_default);
        }
        return armorModel;
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
            properties = new ArmorProperties(1, MekanismConfig.current().general.MekaSuitPantsDamageRatio.val(), MekanismConfig.current().general.MekaSuitPantsDamageMax.val());
            properties.Toughness = 3.0F;
        }

        return properties;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @NotNull ItemStack armor, int slot) {
        if (armor.getItem() == MekanismItems.MekAsuitLeggings) {
            return 6;
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
                if (isUpgradeInstalled(legStack, moduleUpgrade.GEOTHERMAL_GENERATOR_UNIT)) {
                    if (player.isInLava() || player.isBurning()) {
                        armour.setEnergy(legStack, armour.getEnergy(legStack) + 10.5 * armour.getUpgrades(moduleUpgrade.GEOTHERMAL_GENERATOR_UNIT) * 200);
                    }
                }
                if (isUpgradeInstalled(legStack, moduleUpgrade.HYDROSTATIC_REPULSOR_UNIT)) {
                    if (player.isInsideOfMaterial(Material.WATER) && !player.canBreatheUnderwater() && armour.getEnergy(legStack) > 500D) {
                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, legStack) == 0) {
                            legStack.addEnchantment(Enchantments.DEPTH_STRIDER, armour.getUpgrades(moduleUpgrade.HYDROSTATIC_REPULSOR_UNIT));
                            hasEffect(legStack);
                        }
                        armour.setEnergy(legStack, armour.getEnergy(legStack) - 500D);
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
    double getShieldingByArmor() {
        return 20;
    }

    @Override
    public void addHUDStrings(List<String> list, EntityPlayer player, ItemStack stack, EntityEquipmentSlot slotType) {
        if (slotType == getEquipmentSlot()){
            list.add(LangUtils.localize("tooltip.meka_legs.storedEnergy") + " " + MekanismUtils.getEnergyDisplay(getEnergy(stack)));
        }
    }
}
