package mekanism.common.item;


import mekanism.api.EnumColor;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import mekanism.common.MekanismFluids;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemCanteen extends ItemMekanism implements IGasItem {
    public static final int TRANSFER_RATE = 100;
    public static final int ItemStack = 50;

    public ItemCanteen() {
        this.setMaxStackSize(1);
        this.setNoRepair();
    }

    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack itemstack) {
        return EnumColor.YELLOW + super.getItemStackDisplayName(itemstack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
        GasStack gasStack = getGas(itemstack);
        if (gasStack == null) {
            list.add(LangUtils.localize("tooltip.noGas") + ".");
            list.addAll(MekanismUtils.splitTooltip(LangUtils.localize("tooltip.canteen1"), itemstack));
            list.addAll(MekanismUtils.splitTooltip(LangUtils.localize("tooltip.canteen2"), itemstack));
        } else {
            list.add(LangUtils.localize("tooltip.stored") + " " + gasStack.getGas().getLocalizedName() + ": " + gasStack.amount);
        }

    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1D - ((getGas(stack) != null ? (double) getGas(stack).amount : 0D) / (double) getMaxGas(stack));
    }

    @Override
    public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
        return MathHelper.hsvToRGB(Math.max(0.0F, (float) (1 - getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
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
    public int getMaxGas(ItemStack itemstack) {
        return MekanismConfig.current().general.maxCanteen.val();
    }

    @Override
    public int getRate(ItemStack itemstack) {
        return TRANSFER_RATE;
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

    public int getStored(ItemStack itemstack) {
        return getGas(itemstack) != null ? getGas(itemstack).amount : 0;
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
    public void getSubItems(@Nonnull CreativeTabs tabs, @Nonnull NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tabs)) {
            return;
        }
        ItemStack empty = new ItemStack(this);
        setGas(empty, null);
        list.add(empty);
        ItemStack filled = new ItemStack(this);
        setGas(filled, new GasStack(MekanismFluids.NutritionalPaste, ((IGasItem) filled.getItem()).getMaxGas(filled)));
        list.add(filled);
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (!worldIn.isRemote && entityLiving instanceof EntityPlayer player) {
            int needed = Math.min(20 - player.getFoodStats().getFoodLevel(), getGas(stack).amount / 50);
            if (needed > 0) {
                player.getFoodStats().addStats(needed, 0.8F);
                useGas(stack, needed * 50);
                if (MekanismConfig.current().mekce.EnableBuff.val()){
                    player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 2000, 5));
                    player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 4000, 5));
                    player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 4000, 5));
                    player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 20, 5));
                    player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 2000, 5));
                    player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 4000, 5));
                    player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 4000, 5));
                    player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 4000, 5));
                    player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION,  2000, 5));
                    player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 4000, 5));
                    player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 4000, 5));
                }
            }
        }
        return stack;
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.DRINK;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 32;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (player.canEat(false)) {
            player.setActiveHand(hand);
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
        }
        return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
    }
}
