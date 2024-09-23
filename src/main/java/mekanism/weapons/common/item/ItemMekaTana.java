package mekanism.weapons.common.item;

import com.google.common.collect.Multimap;
import mekanism.api.energy.IEnergizedItem;
import mekanism.common.base.IModuleUpgrade;
import mekanism.common.config.MekanismConfig;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UpgradeHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;


public class ItemMekaTana extends ItemMekaEnergyBase implements IModuleUpgrade {

    public ItemMekaTana() {
        super();
    }

    @Override
    public boolean canHarvestBlock(@Nonnull IBlockState state, ItemStack stack) {
        return state.getBlock() == Blocks.WEB;
    }

    @Override
    public int getItemEnchantability() {
        return 5;
    }

    @Override
    public double getMaxTransfer(ItemStack itemStack) {
        return MekanismConfig.current().weapons.mekaTanaBaseChargeRate.val();
    }

    @Override
    public List<moduleUpgrade> getValidModule(ItemStack stack) {
        ArrayList<moduleUpgrade> list = new ArrayList<>();
        list.add(moduleUpgrade.EnergyUnit);
        list.add(moduleUpgrade.ATTACK_AMPLIFICATION_UNIT);
        return list;
    }

    @Override
    public double getMaxEnergy(ItemStack itemStack) {
        return MekanismUtils.getModuleMaxEnergy(itemStack, MekanismConfig.current().weapons.mekaTanaBaseEnergyCapacity.val());
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase target, EntityLivingBase attacker) {
        double energy = getEnergy(itemstack);
        double energyCost = MekanismConfig.current().weapons.mekaTanaEnergyUsage.val();
       /* float damage = getAttackDamage(itemstack);

        if (attacker instanceof EntityPlayer) {
            target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), damage);
        } else {
            target.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage);
        }

       */
        if (attacker instanceof EntityPlayer player) {
            if (energy > 0 && !player.capabilities.isCreativeMode) {
                setEnergy(itemstack, energy - energyCost);
            }
        }
        return true;
    }

    public int getAttackDamage(ItemStack itemStack) {
        int damage = MekanismConfig.current().weapons.mekaTanaBaseDamage.val();
        int numUpgrades = UpgradeHelper.getUpgradeLevel(itemStack, moduleUpgrade.ATTACK_AMPLIFICATION_UNIT);
        if (numUpgrades == 0) {
            NBTTagCompound dataMap = ItemDataUtils.getDataMap(itemStack);
            if (dataMap.isEmpty() && itemStack.getTagCompound() != null) {
                itemStack.getTagCompound().removeTag(ItemDataUtils.DATA_ID);
            }
        }
        for (int i = 0; i < numUpgrades; i++) {
            damage += MekanismConfig.current().weapons.mekaTanaBaseDamage.val();
        }
        return damage;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityliving) {
        setEnergy(itemstack, getEnergy(itemstack) - 200);
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack itemstack, IBlockState state) {
        return getEnergy(itemstack) != 0 ? MekanismConfig.current().weapons.mekaTanaAttackSpeed.val() : super.getDestroySpeed(itemstack, state);
    }


    @Nonnull
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multiMap = super.getAttributeModifiers(slot, stack);
        if (slot == EntityEquipmentSlot.MAINHAND) {
            multiMap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getAttackDamage(stack), 0));
            multiMap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", MekanismConfig.current().weapons.mekaTanaAttackSpeed.val(), 0));
        }
        return multiMap;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getEnergy(stack) > 0;
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
    public void getSubItems(@Nonnull CreativeTabs tabs, @Nonnull NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tabs)) {
            return;
        }
        ItemStack discharged = new ItemStack(this);
        list.add(discharged);

        ItemStack charged = new ItemStack(this);
        setEnergy(charged, ((IEnergizedItem) charged.getItem()).getMaxEnergy(charged));
        list.add(charged);

        ItemStack fullupgrade = new ItemStack(this);
        for (moduleUpgrade upgrade : getValidModule(fullupgrade)) {
            UpgradeHelper.setUpgradeLevel(fullupgrade, upgrade, upgrade.getMax());
        }
        setEnergy(fullupgrade, ((IEnergizedItem) fullupgrade.getItem()).getMaxEnergy(fullupgrade));
        list.add(fullupgrade);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(itemstack, world, list, flag);
        list.addAll(UpgradeHelper.getUpgradeStats(itemstack));
    }


}
