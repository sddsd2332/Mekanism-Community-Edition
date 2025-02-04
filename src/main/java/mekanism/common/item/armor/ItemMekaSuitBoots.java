package mekanism.common.item.armor;

import com.google.common.collect.Multimap;
import mekanism.client.model.mekasuitarmour.ModelMekAsuitBoot;
import mekanism.common.MekanismItems;
import mekanism.common.config.MekanismConfig;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ItemMekaSuitBoots extends ItemMekaSuitArmor {

    public ItemMekaSuitBoots() {
        super(EntityEquipmentSlot.FEET);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return ModelMekAsuitBoot.boot;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        UUID uuid = new UUID((getTranslationKey(stack) + slot).hashCode(), 0);
        if (slot == EntityEquipmentSlot.FEET) {
            multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(uuid, "Terrasteel modifier " + EntityEquipmentSlot.FEET, 1.5D, 0));
        }
        return multimap;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @NotNull ItemStack armor, DamageSource source, double damage, int slot) {
        ArmorProperties properties = new ArmorProperties(0, 0, 0);
        if (this == MekanismItems.MEKASUIT_BOOTS) {
            properties = new ArmorProperties(1, MekanismConfig.current().meka.MekaSuitBootsDamageRatio.val(), MekanismConfig.current().meka.MekaSuitBootsDamageMax.val());
            properties.Toughness = MekanismConfig.current().meka.mekaSuitToughness.val();
        }

        return properties;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @NotNull ItemStack armor, int slot) {
        if (armor.getItem() == MekanismItems.MEKASUIT_BOOTS) {
            return MekanismConfig.current().meka.mekaSuitBootsArmor.val();
        }
        return 0;
    }
}
