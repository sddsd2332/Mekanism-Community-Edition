package mekanism.common.item;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemGaugeDropper extends ItemMekanism implements IGasItem {

    public static final int TRANSFER_RATE = 16;
    public static int CAPACITY = Fluid.BUCKET_VOLUME;

    public ItemGaugeDropper() {
        super();
        setMaxStackSize(1);
        setRarity(EnumRarity.UNCOMMON);
    }

    public ItemStack getEmptyItem() {
        ItemStack empty = new ItemStack(this);
        setGas(empty, null);
        return empty;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tabs, @Nonnull NonNullList<ItemStack> list) {
        if (isInCreativeTab(tabs)) {
            list.add(getEmptyItem());
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getGas(stack) != null || FluidUtil.getFluidContained(stack) != null;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        double gasRatio = (getGas(stack) != null ? (double) getGas(stack).amount : 0D) / (double) CAPACITY;
        double fluidRatio = (FluidUtil.getFluidContained(stack) != null ? (double) FluidUtil.getFluidContained(stack).amount : 0D) / (double) CAPACITY;
        return 1D - Math.max(gasRatio, fluidRatio);
    }

    @Override
    public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
        GasStack gas = getGas(stack);
        FluidStack fluidStack = FluidUtil.getFluidContained(stack);
        if (gas != null) {
            MekanismRenderer.color(gas);
            return gas.getGas().getTint();
        } else if (fluidStack != null && fluidStack.getFluid().getColor() != 0xFFFFFFFF) { //Because it is possible that the liquid is not colored
            return fluidStack.getFluid().getColor();
        } else
            return MathHelper.hsvToRGB(Math.max(0.0F, (float) (1 - getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking() && !world.isRemote) {
            setGas(stack, null);
            FluidUtil.getFluidHandler(stack).drain(CAPACITY, true);
            ((EntityPlayerMP) player).sendContainerToPlayer(player.openContainer);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
        GasStack gasStack = getGas(itemstack);
        FluidStack fluidStack = FluidUtil.getFluidContained(itemstack);
        if (gasStack == null && fluidStack == null) {
            list.add(LangUtils.localize("gui.empty") + ".");
        } else if (gasStack != null) {
            list.add(LangUtils.localize("tooltip.stored") + " " + gasStack.getGas().getLocalizedName() + ": " + gasStack.amount);
        } else {
            list.add(LangUtils.localize("tooltip.stored") + " " + fluidStack.getFluid().getLocalizedName(fluidStack) + ": " + fluidStack.amount);
        }
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
        int toUse = Math.min(getMaxGas(itemstack) - getStored(itemstack), Math.min(getRate(itemstack), stack.amount));
        setGas(itemstack, new GasStack(stack.getGas(), getStored(itemstack) + toUse));
        return toUse;
    }

    @Override
    public GasStack removeGas(ItemStack itemstack, int amount) {
        if (getGas(itemstack) == null) {
            return null;
        }
        Gas type = getGas(itemstack).getGas();
        int gasToUse = Math.min(getStored(itemstack), Math.min(getRate(itemstack), amount));
        setGas(itemstack, new GasStack(type, getStored(itemstack) - gasToUse));
        return new GasStack(type, gasToUse);
    }

    private int getStored(ItemStack itemstack) {
        return getGas(itemstack) != null ? getGas(itemstack).amount : 0;
    }

    @Override
    public boolean canReceiveGas(ItemStack itemstack, Gas type) {
        return getGas(itemstack) == null || getGas(itemstack).getGas() == type;
    }

    @Override
    public boolean canProvideGas(ItemStack itemstack, Gas type) {
        return getGas(itemstack) != null && (type == null || getGas(itemstack).getGas() == type);
    }

    private GasStack getGas_do(ItemStack itemstack) {
        return GasStack.readFromNBT(ItemDataUtils.getCompound(itemstack, "gasStack"));
    }

    @Override
    public GasStack getGas(ItemStack itemstack) {
        return getGas_do(itemstack);
    }

    @Override
    public void setGas(ItemStack itemstack, GasStack stack) {
        if (stack == null || stack.amount == 0) {
            ItemDataUtils.removeData(itemstack, "gasStack");
        } else {
            int amount = Math.max(0, Math.min(stack.amount, getMaxGas(itemstack)));
            GasStack gasStack = new GasStack(stack.getGas(), amount);
            ItemDataUtils.setCompound(itemstack, "gasStack", gasStack.write(new NBTTagCompound()));
        }
    }

    @Override
    public int getMaxGas(ItemStack itemstack) {
        return CAPACITY;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new FluidHandlerItemStack(stack, CAPACITY);
    }
}
