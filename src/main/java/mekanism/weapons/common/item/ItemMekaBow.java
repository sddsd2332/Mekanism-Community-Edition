package mekanism.weapons.common.item;

import cofh.redstoneflux.api.IEnergyContainerItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import mekanism.api.EnumColor;
import mekanism.api.energy.IEnergizedItem;
import mekanism.common.base.IModuleUpgrade;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.integration.ic2.IC2ItemManager;
import mekanism.common.integration.redstoneflux.RFIntegration;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.weapons.common.MekanismWeapons;
import mekanism.weapons.common.MekanismWeaponsItems;
import mekanism.weapons.common.entity.EntityMekaArrow;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


@Optional.InterfaceList({
        @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = MekanismHooks.IC2_MOD_ID),
        @Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyContainerItem", modid = MekanismHooks.REDSTONEFLUX_MOD_ID),
        @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = MekanismHooks.IC2_MOD_ID)
})
public class ItemMekaBow extends ItemBow implements IModuleUpgrade, IEnergizedItem, ISpecialElectricItem, IEnergyContainerItem {

    public ItemMekaBow() {
        setMaxStackSize(1);
        setCreativeTab(MekanismWeapons.tabMekanismWeapons);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0F;
                } else {
                    return !(entityIn.getActiveItemStack().getItem() instanceof ItemMekaBow) ? 0.0F : (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F;
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    public double getMaxEnergy(ItemStack itemStack) {
        return ItemDataUtils.hasData(itemStack, "module") ? MekanismUtils.getModuleMaxEnergy(itemStack, MekanismConfig.current().weapons.mekaBowBaseEnergyCapacity.val()) : MekanismConfig.current().weapons.mekaBowBaseEnergyCapacity.val();
    }

    @Override
    public double getMaxTransfer(ItemStack itemStack) {
        return MekanismConfig.current().weapons.mekaBowBaseChargeRate.val();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(itemstack, world, list, flag);
        list.add(EnumColor.AQUA + LangUtils.localize("tooltip.storedEnergy") + ": " + EnumColor.GREY + MekanismUtils.getEnergyDisplay(getEnergy(itemstack), getMaxEnergy(itemstack)));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityLivingBase entityLiving, int itemUseCount) {
        if (entityLiving instanceof EntityPlayer player && getEnergy(itemstack) > 0) {
            boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, itemstack) > 0 || isUpgradeInstalled(itemstack, moduleUpgrade.ARROWENERGY_UNIT);
            ItemStack ammo = findAmmo(player);
            int maxItemUse = getMaxItemUseDuration(itemstack) - itemUseCount;
            maxItemUse = ForgeEventFactory.onArrowLoose(itemstack, world, player, maxItemUse, !itemstack.isEmpty() || flag);
            if (maxItemUse < 0) {
                return;
            }
            if (flag || !ammo.isEmpty()) {
                if (ammo.isEmpty()) {
                    ammo = new ItemStack(MekanismWeaponsItems.mekArrow);
                }
                float f = maxItemUse / 20F;
                f = (f * f + f * 2.0F) / 3F;
                if (f < 0.1D) {
                    return;
                }
                if (f > 1.0F) {
                    f = 1.0F;
                }
                boolean noConsume = flag && itemstack.getItem() instanceof ItemMekaArrow;
                if (!world.isRemote) {
                    ItemMekaArrow itemarrow = (ItemMekaArrow) (ammo.getItem() instanceof ItemMekaArrow ? ammo.getItem() : MekanismWeaponsItems.mekArrow);
                    EntityMekaArrow entityarrow = itemarrow.createArrow(world, itemstack, player);
                    entityarrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                    int damage = getDamage(itemstack);
                    entityarrow.setDamage(damage);
                    int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, itemstack);
                    if (power > 0) {
                        entityarrow.setDamage(damage + 0.5 * power + 0.5);
                    }
                    int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, itemstack);
                    if (punch > 0) {
                        entityarrow.setKnockbackStrength(punch);
                    }
                    if (f == 1.0F) {
                        entityarrow.setIsCritical(true);
                    }
                    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, itemstack) > 0) {
                        entityarrow.setFire(100);
                    }

                    if (!player.capabilities.isCreativeMode) {
                        setEnergy(itemstack, getEnergy(itemstack) - MekanismConfig.current().weapons.mekaBowEnergyUsage.val());
                    }
                    if (noConsume) {
                        entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                    }

                    world.spawnEntity(entityarrow);
                }

                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL,
                        1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                if (!noConsume) {
                    ammo.shrink(1);
                    if (ammo.getCount() == 0) {
                        player.inventory.deleteStack(ammo);
                    }
                }
                player.addStat(StatList.getObjectUseStats(this));
            }
        }
    }

    public int getDamage(ItemStack itemStack) {
        int damage = MekanismConfig.current().weapons.mekaBowBaseDamage.val();
        int numUpgrades = MekanismUtils.getModule(itemStack, moduleUpgrade.ATTACK_AMPLIFICATION_UNIT);
        if (numUpgrades == 0) {
            NBTTagCompound dataMap = ItemDataUtils.getDataMap(itemStack);
            if (dataMap.isEmpty()) {
                itemStack.setTagCompound(null);
            }
        }
        for (int i = 0; i < numUpgrades; i++) {
            damage += MekanismConfig.current().weapons.mekaBowBaseDamage.val();
        }
        return damage;
    }



    @Override
    protected boolean isArrow(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemMekaArrow;
    }


    @Override
    public List<moduleUpgrade> getValidModule(ItemStack stack) {
        ArrayList<moduleUpgrade> list = new ArrayList<>();
        list.add(moduleUpgrade.EnergyUnit);
        list.add(moduleUpgrade.ATTACK_AMPLIFICATION_UNIT);
        list.add(moduleUpgrade.ARROWENERGY_UNIT);
        return list;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
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
    }

    @Override
    public int getItemEnchantability() {
        return 5;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int receiveEnergy(ItemStack theItem, int energy, boolean simulate) {
        if (canReceive(theItem)) {
            double energyNeeded = getMaxEnergy(theItem) - getEnergy(theItem);
            double toReceive = Math.min(RFIntegration.fromRF(energy), energyNeeded);
            if (!simulate) {
                setEnergy(theItem, getEnergy(theItem) + toReceive);
            }
            return RFIntegration.toRF(toReceive);
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int extractEnergy(ItemStack theItem, int energy, boolean simulate) {
        if (canSend(theItem)) {
            double energyRemaining = getEnergy(theItem);
            double toSend = Math.min(RFIntegration.fromRF(energy), energyRemaining);
            if (!simulate) {
                setEnergy(theItem, getEnergy(theItem) - toSend);
            }
            return RFIntegration.toRF(toSend);
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int getEnergyStored(ItemStack theItem) {
        return RFIntegration.toRF(getEnergy(theItem));
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int getMaxEnergyStored(ItemStack theItem) {
        return RFIntegration.toRF(getMaxEnergy(theItem));
    }

    @Override
    @Optional.Method(modid = MekanismHooks.IC2_MOD_ID)
    public IElectricItemManager getManager(ItemStack itemStack) {
        return IC2ItemManager.getManager(this);
    }

    @Override
    public double getEnergy(ItemStack itemStack) {
        return ItemDataUtils.getDouble(itemStack, "energyStored");
    }

    @Override
    public void setEnergy(ItemStack itemStack, double amount) {
        if (amount == 0) {
            NBTTagCompound dataMap = ItemDataUtils.getDataMap(itemStack);
            dataMap.removeTag("energyStored");
            if (dataMap.isEmpty()) {
                itemStack.setTagCompound(null);
            }
        } else {
            ItemDataUtils.setDouble(itemStack, "energyStored", Math.max(Math.min(amount, getMaxEnergy(itemStack)), 0));
        }
    }


    @Override
    public boolean canReceive(ItemStack itemStack) {
        return getMaxEnergy(itemStack) - getEnergy(itemStack) > 0;
    }

    @Override
    public boolean canSend(ItemStack itemStack) {
        return false;
    }
}
