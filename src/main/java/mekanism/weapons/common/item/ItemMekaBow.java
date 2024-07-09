package mekanism.weapons.common.item;

import mekanism.common.base.IModuleUpgrade;
import mekanism.common.config.MekanismConfig;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.weapons.common.entity.EntityMekaArrow;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class ItemMekaBow extends ItemMekaEnergyBase implements IModuleUpgrade {

    public ItemMekaBow() {
        super();
        setSupported(moduleUpgrade.EnergyUnit);
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

    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityLivingBase entityLiving, int itemUseCount) {
        if (entityLiving instanceof EntityPlayer player && getEnergy(itemstack) > 0) {
            boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, itemstack) > 0 || isUpgradeInstalled(itemstack,moduleUpgrade.ARROWENERGY_UNIT);
            ItemStack ammo = findAmmo(player);
            int maxItemUse = getMaxItemUseDuration(itemstack) - itemUseCount;
            maxItemUse = ForgeEventFactory.onArrowLoose(itemstack, world, player, maxItemUse, !itemstack.isEmpty() || flag);
            if (maxItemUse < 0) {
                return;
            }
            if (flag || !ammo.isEmpty()) {
                if (ammo.isEmpty()) {
                    ammo = new ItemStack(Items.ARROW);
                }
                float f = maxItemUse / 20F;
                f = (f * f + f * 2.0F) / 3F;
                if (f < 0.1D) {
                    return;
                }
                if (f > 1.0F) {
                    f = 1.0F;
                }
                boolean noConsume = flag && itemstack.getItem() instanceof ItemArrow;
                if (!world.isRemote) {
                    ItemArrow itemarrow = (ItemArrow) (ammo.getItem() instanceof ItemArrow ? ammo.getItem() : Items.ARROW);
                    EntityMekaArrow entityarrow = new EntityMekaArrow(world,player);
                    entityarrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                    int damage = getDamage(itemstack);
                    entityarrow.setDamage(damage / 3D);
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
        Map<moduleUpgrade, Integer> module = moduleUpgrade.buildMap(ItemDataUtils.getDataMap(itemStack));
        int damage = MekanismConfig.current().weapons.mekaBowBaseDamage.val();
        int numUpgrades = module.get(moduleUpgrade.ATTACK_AMPLIFICATION_UNIT) == null ? 0 : module.get(moduleUpgrade.ATTACK_AMPLIFICATION_UNIT);
        for (int i = 0; i < numUpgrades; i++) {
            damage += MekanismConfig.current().weapons.mekaBowBaseDamage.val();
        }
        return damage;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 72000;
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.BOW;
    }

    private ItemStack findAmmo(EntityPlayer player) {
        if (isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = player.inventory.getStackInSlot(i);
            if (isArrow(itemstack)) {
                return itemstack;
            }
        }
        return ItemStack.EMPTY;
    }

    protected boolean isArrow(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemArrow;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        boolean flag = !findAmmo(playerIn).isEmpty();
        ActionResult<ItemStack> ret = ForgeEventFactory.onArrowNock(itemStackIn, worldIn, playerIn, hand, flag);
        if (ret != null) {
            return ret;
        }
        if (!playerIn.capabilities.isCreativeMode && !flag) {
            return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
        }
        playerIn.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
    }

}
