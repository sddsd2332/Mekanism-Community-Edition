package mekanism.common.item.armor;

import com.google.common.collect.Multimap;
import mekanism.client.model.mekasuitarmour.ModelMekAsuitBoot;
import mekanism.common.Mekanism;
import mekanism.common.MekanismItems;
import mekanism.common.config.MekanismConfig;
import mekanism.common.item.interfaces.IItemHUDProvider;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UpgradeHelper;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class ItemMekAsuitFeetArmour extends ItemMekaSuitArmor implements IItemHUDProvider {

    public ItemMekAsuitFeetArmour() {
        super(EntityEquipmentSlot.FEET);
    }

    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        return armorType == EntityEquipmentSlot.FEET;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        ModelMekAsuitBoot armorModel = ModelMekAsuitBoot.boot;
        return armorModel;
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
        if (this == MekanismItems.MekAsuitBoots) {
            properties = new ArmorProperties(1, MekanismConfig.current().general.MekaSuitBootsDamageRatio.val(), MekanismConfig.current().general.MekaSuitBootsDamageMax.val());
            properties.Toughness = 3.0F;
        }

        return properties;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @NotNull ItemStack armor, int slot) {
        if (armor.getItem() == MekanismItems.MekAsuitBoots) {
            return 3;
        }
        return 0;
    }


    @Override
    public List<moduleUpgrade> getValidModule(ItemStack stack) {
        List<moduleUpgrade> list = super.getValidModule(stack);
        list.add(moduleUpgrade.HYDRAULIC_PROPULSION_UNIT);
        list.add(moduleUpgrade.FROST_WALKER_UNIT);
        return list;
    }


    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);
        if (!world.isRemote) {
            ItemStack feetStack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (feetStack.getItem() instanceof ItemMekAsuitFeetArmour armour) {
                if (UpgradeHelper.isUpgradeInstalled(feetStack, moduleUpgrade.FROST_WALKER_UNIT)) {
                    if (armour.getEnergy(feetStack) > 500D) {
                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FROST_WALKER, feetStack) == 0) {
                            feetStack.addEnchantment(Enchantments.FROST_WALKER, UpgradeHelper.getUpgradeLevel(feetStack, moduleUpgrade.FROST_WALKER_UNIT));
                            armour.setEnergy(feetStack, armour.getEnergy(feetStack) - 500D);
                        }
                    } else {
                        removeEnchantment(feetStack);
                    }
                }
            }
        }
    }

    @Override
    double getShieldingByArmor() {
        return 15;
    }


    public void removeEnchantment(ItemStack stack) {
        NBTTagList list = stack.getEnchantmentTagList();
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound compound = list.getCompoundTagAt(i);
            int id = compound.getShort("id");
            Enchantment e = Enchantment.getEnchantmentByID(id);
            if (e == Enchantments.FROST_WALKER) {
                list.removeTag(i);
            }
        }
    }


    public JumpBoost getJumpBoostMode(ItemStack itemStack) {
        return JumpBoost.values()[ItemDataUtils.getInt(itemStack, "JumpBoostMode")];
    }

    public void setJumpBoostMode(ItemStack itemStack, JumpBoost mode) {
        ItemDataUtils.setInt(itemStack, "JumpBoostMode", mode.ordinal());
    }

    public void incrementJumpBoostMode(ItemStack itemStack) {
        setJumpBoostMode(itemStack, getJumpBoostMode(itemStack).increment());
    }

    public StepAssist getStepAssistMode(ItemStack itemStack) {
        return StepAssist.values()[ItemDataUtils.getInt(itemStack, "StepAssistMode")];
    }

    public void setStepAssistMode(ItemStack itemStack, StepAssist mode) {
        ItemDataUtils.setInt(itemStack, "StepAssistMode", mode.ordinal());
    }

    public void incrementStepAssistMode(ItemStack itemStack) {
        setStepAssistMode(itemStack, getStepAssistMode(itemStack).increment());
    }

    @Override
    public void addHUDStrings(List<String> list, EntityPlayer player, ItemStack stack, EntityEquipmentSlot slotType) {
        if (slotType == getEquipmentSlot()) {
            if (UpgradeHelper.isUpgradeInstalled(stack, moduleUpgrade.HYDRAULIC_PROPULSION_UNIT)) {
                list.add(LangUtils.localize("tooltip.module.jump_boost.name") + " " + getJumpBoostMode(stack).getBoost());
                list.add(LangUtils.localize("tooltip.module.step_assist.name") + " " + getStepAssistMode(stack).getHeight());
            }
            if (!Mekanism.hooks.DraconicEvolution) {
                list.add(LangUtils.localize("tooltip.meka_feet.storedEnergy") + " " + MekanismUtils.getEnergyDisplay(getEnergy(stack)));
            }
        }
    }

    public enum JumpBoost {
        OFF(0),
        LOW(0.5F),
        MED(1),
        HIGH(3),
        ULTRA(5);
        private final float boost;
        private final String label;

        JumpBoost(float boost) {
            this.boost = boost;
            this.label = Float.toString(boost);
        }

        public String getTextComponent() {
            return label;
        }

        public float getBoost() {
            return boost;
        }

        public JumpBoost increment() {
            return ordinal() < values().length - 1 ? values()[ordinal() + 1] : values()[0];
        }
    }

    public enum StepAssist {
        OFF(0),
        LOW(0.5F),
        MED(1),
        HIGH(1.5F),
        ULTRA(2);

        private final float height;
        private final String label;

        StepAssist(float height) {
            this.height = height;
            this.label = Float.toString(height);
        }

        public String getTextComponent() {
            return label;
        }

        public float getHeight() {
            return height;
        }

        public StepAssist increment() {
            return ordinal() < values().length - 1 ? values()[ordinal() + 1] : values()[0];
        }
    }


}
