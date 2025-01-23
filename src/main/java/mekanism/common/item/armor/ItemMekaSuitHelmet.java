package mekanism.common.item.armor;

import com.google.common.collect.Multimap;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import mekanism.client.model.mekasuitarmour.ModelMekAsuitHead;
import mekanism.client.model.mekasuitarmour.ModuleSolarHelmet;
import mekanism.common.MekanismFluids;
import mekanism.common.MekanismItems;
import mekanism.common.MekanismModules;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.ItemDataUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ItemMekaSuitHelmet extends ItemMekaSuitArmor implements IGasItem {

    public ItemMekaSuitHelmet() {
        super(EntityEquipmentSlot.HEAD);
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @NotNull ItemStack armor, DamageSource source, double damage, int slot) {
        ArmorProperties properties = new ArmorProperties(0, 0, 0);
        if (this == MekanismItems.MEKASUIT_HELMET) {
            properties = new ArmorProperties(1, MekanismConfig.current().meka.MekaSuitHelmetDamageRatio.val(), MekanismConfig.current().meka.MekaSuitHelmetDamageMax.val());
            properties.Toughness = MekanismConfig.current().meka.mekaSuitToughness.val();
        }
        return properties;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @NotNull ItemStack armor, int slot) {
        if (armor.getItem() == MekanismItems.MEKASUIT_HELMET) {
            return MekanismConfig.current().meka.mekaSuitHelmetArmor.val();
        }
        return 0;
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
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        ModelMekAsuitHead armorModel = ModelMekAsuitHead.head;
        ModuleSolarHelmet Solar = ModuleSolarHelmet.solar;

        /*
        if (isModuleEnabled(itemStack, MekanismModules.SOLAR_RECHARGING_UNIT)) {
            armorModel.helmet_armor.childModels.remove(armorModel.hide);
            if (!armorModel.bipedHead.childModels.contains(Solar.solar_helmet)) {
                armorModel.bipedHead.addChild(Solar.solar_helmet);
            }
        } else {
            armorModel.bipedHead.childModels.remove(Solar.solar_helmet);
            if (!armorModel.helmet_armor.childModels.contains(armorModel.hide)) {
                armorModel.helmet_armor.childModels.add(armorModel.hide);
            }
        }

         */
        return armorModel;
    }

    @Override
    public int getRate(ItemStack itemstack) {
        return MekanismConfig.current().meka.mekaSuitNutritionalTransferRate.val();
    }

    @Override
    public int addGas(ItemStack itemstack, GasStack stack) {
        if (!hasModule(itemstack, MekanismModules.NUTRITIONAL_INJECTION_UNIT)) {
            return 0;
        }
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
}
