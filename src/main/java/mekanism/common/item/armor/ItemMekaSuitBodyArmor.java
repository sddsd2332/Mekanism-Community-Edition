package mekanism.common.item.armor;

import com.google.common.collect.Multimap;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import mekanism.client.model.mekasuitarmour.ModelMekAsuitBody;
import mekanism.client.model.mekasuitarmour.ModuleGravitational;
import mekanism.client.model.mekasuitarmour.ModuleJetpack;
import mekanism.common.MekanismFluids;
import mekanism.common.MekanismItems;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.Modules;
import mekanism.common.content.gear.mekasuit.ModuleJetpackUnit;
import mekanism.common.item.interfaces.IJetpackItem;
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

public class ItemMekaSuitBodyArmor extends ItemMekaSuitArmor implements IGasItem, IJetpackItem {

    public ItemMekaSuitBodyArmor() {
        super(EntityEquipmentSlot.CHEST);
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @NotNull ItemStack armor, DamageSource source, double damage, int slot) {
        ArmorProperties properties = new ArmorProperties(0, 0, 0);
        if (this == MekanismItems.MEKASUIT_BODYARMOR) {
            properties = new ArmorProperties(1, MekanismConfig.current().meka.MekaSuitBodyarmorDamageRatio.val(), MekanismConfig.current().meka.MekaSuitBodyarmorDamageMax.val());
            properties.Toughness = MekanismConfig.current().meka.mekaSuitToughness.val();
        }

        return properties;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @NotNull ItemStack armor, int slot) {
        if (armor.getItem() == MekanismItems.MEKASUIT_BODYARMOR) {
            return MekanismConfig.current().meka.mekaSuitBodyArmorArmor.val();
        }
        return 0;
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
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        ModelMekAsuitBody armorModel = ModelMekAsuitBody.armorModel;
        ModuleJetpack jetpack = ModuleJetpack.jetpacks;
        ModuleGravitational gravitational = ModuleGravitational.gravitational;

        if (isModuleEnabled(itemStack, Modules.JETPACK_UNIT)) {
            if (!armorModel.bipedBody.childModels.contains(jetpack.jetpack)) {
                armorModel.bipedBody.addChild(jetpack.jetpack);
            }
        } else {
            armorModel.bipedBody.childModels.remove(jetpack.jetpack);
        }
        if (isModuleEnabled(itemStack, Modules.GRAVITATIONAL_MODULATING_UNIT)) {
            if (!armorModel.bipedBody.childModels.contains(gravitational.gravitational_modulator)) {
                armorModel.bipedBody.addChild(gravitational.gravitational_modulator);
            }
        } else {
            armorModel.bipedBody.childModels.remove(gravitational.gravitational_modulator);
        }

        return armorModel;
    }


    @Override
    public int getRate(ItemStack itemstack) {
        return MekanismConfig.current().meka.mekaSuitJetpackTransferRate.val();
    }

    @Override
    public int addGas(ItemStack itemstack, GasStack stack) {
        if (!hasModule(itemstack, Modules.JETPACK_UNIT)) {
            return 0;
        }
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

    public int getStored(ItemStack itemstack) {
        return getGas(itemstack) != null ? getGas(itemstack).amount : 0;
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


    @Override
    public boolean canUseJetpack(ItemStack stack) {
        return armorType == EntityEquipmentSlot.CHEST && isModuleEnabled(stack,Modules.JETPACK_UNIT) ? getStored(stack) > 0 :getModules(stack).stream().allMatch(module -> module.isEnabled() && module.getData().isExclusive(Modules.ExclusiveFlag.OVERRIDE_JUMP.getMask()));
    }

    @Override
    public JetpackMode getJetpackMode(ItemStack stack) {
        ModuleJetpackUnit module = getModule(stack, Modules.JETPACK_UNIT);
        if (module != null && module.isEnabled()) {
            return module.getMode();
        }
        return JetpackMode.DISABLED;
    }

    @Override
    public void useJetpackFuel(ItemStack stack) {
        GasStack gas = getGas(stack);
        if (gas != null) {
            setGas(stack, new GasStack(gas.getGas(), gas.amount - 1));
        }
    }
}
