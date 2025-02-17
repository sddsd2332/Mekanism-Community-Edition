package mekanism.common.item;

import mekanism.api.EnumColor;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import mekanism.client.MekKeyHandler;
import mekanism.client.MekanismClient;
import mekanism.client.MekanismKeyHandler;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
import mekanism.common.base.ISideConfiguration;
import mekanism.common.base.ISustainedInventory;
import mekanism.common.base.ITierItem;
import mekanism.common.config.MekanismConfig;
import mekanism.common.security.ISecurityItem;
import mekanism.common.security.ISecurityTile;
import mekanism.common.security.ISecurityTile.SecurityMode;
import mekanism.common.tier.BaseTier;
import mekanism.common.tier.GasTankTier;
import mekanism.common.tile.TileEntityGasTank;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import mekanism.common.util.SecurityUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class ItemBlockGasTank extends ItemBlock implements IGasItem, ISustainedInventory, ITierItem, ISecurityItem {

    /**
     * How fast this tank can transfer gas.
     */
    public static final int TRANSFER_RATE = 256;
    public Block metaBlock;
    /**
     * The maximum amount of gas this tank can hold.
     */
    public int MAX_GAS = 96000;

    public ItemBlockGasTank(Block block) {
        super(block);
        metaBlock = block;
        setHasSubtypes(true);
        //setMaxStackSize(1);
        setCreativeTab(Mekanism.tabMekanism);
    }

    @Override
    public int getMetadata(int i) {
        return i;
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack itemstack) {
        return getBaseTier(itemstack).getColor() + LangUtils.localize("tile.GasTank" + getBaseTier(itemstack).getSimpleName() + ".name");
    }

    @Override
    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY,
                                float hitZ, @Nonnull IBlockState state) {
        if (stack.getCount() > 1 && MekanismConfig.current().mekce.StackingPlacementLimits.val()) {
            return false;
        }
        boolean place = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state);
        if (place) {
            TileEntityGasTank tileEntity = (TileEntityGasTank) world.getTileEntity(pos);
            tileEntity.tier = GasTankTier.values()[getBaseTier(stack).ordinal()];
            tileEntity.gasTank.setMaxGas(tileEntity.tier.getStorage());
            tileEntity.gasTank.setGas(getGas(stack));
            ((ISecurityTile) tileEntity).getSecurity().setOwnerUUID(getOwnerUUID(stack));

            if (hasSecurity(stack)) {
                ((ISecurityTile) tileEntity).getSecurity().setMode(getSecurity(stack));
            }
            if (getOwnerUUID(stack) == null) {
                ((ISecurityTile) tileEntity).getSecurity().setOwnerUUID(player.getUniqueID());
            }

            if (ItemDataUtils.hasData(stack, "sideDataStored")) {
                ((ISideConfiguration) tileEntity).getConfig().read(ItemDataUtils.getDataMap(stack));
                ((ISideConfiguration) tileEntity).getEjector().read(ItemDataUtils.getDataMap(stack));
            }

            ((ISustainedInventory) tileEntity).setInventory(getInventory(stack));
            if (!world.isRemote) {
                Mekanism.packetHandler.sendUpdatePacket(tileEntity);
            }
        }
        return place;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack itemstack, World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flag) {
        GasStack gasStack = getGas(itemstack);
        if (itemstack.getCount() <= 1) {
            if (gasStack == null) {
                list.add(EnumColor.DARK_RED + LangUtils.localize("gui.empty") + ".");
            } else {
                String amount = gasStack.amount == Integer.MAX_VALUE ? LangUtils.localize("gui.infinite") : Integer.toString(gasStack.amount);
                list.add(EnumColor.ORANGE + gasStack.getGas().getLocalizedName() + ": " + EnumColor.GREY + amount);
            }
        }
        int cap = GasTankTier.values()[getBaseTier(itemstack).ordinal()].getStorage();
        list.add(EnumColor.INDIGO + LangUtils.localize("tooltip.capacity") + ": " + EnumColor.GREY + (cap == Integer.MAX_VALUE ? LangUtils.localize("gui.infinite") : cap));

        if (!MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.sneakKey)) {
            list.add(LangUtils.localize("tooltip.hold") + " " + EnumColor.AQUA + GameSettings.getKeyDisplayString(MekanismKeyHandler.sneakKey.getKeyCode()) +
                    EnumColor.GREY + " " + LangUtils.localize("tooltip.forDetails") + ".");
        } else {
            if (hasSecurity(itemstack)) {
                list.add(SecurityUtils.getOwnerDisplay(Minecraft.getMinecraft().player, MekanismClient.clientUUIDMap.get(getOwnerUUID(itemstack))));
                list.add(EnumColor.GREY + LangUtils.localize("gui.security") + ": " + SecurityUtils.getSecurityDisplay(itemstack, Side.CLIENT));
                if (SecurityUtils.isOverridden(itemstack, Side.CLIENT)) {
                    list.add(EnumColor.RED + "(" + LangUtils.localize("gui.overridden") + ")");
                }
            }

            list.add(EnumColor.AQUA + LangUtils.localize("tooltip.inventory") + ": " + EnumColor.GREY +
                    LangUtils.transYesNo(getInventory(itemstack) != null && getInventory(itemstack).tagCount() != 0));
        }
    }

    @Override
    public GasStack getGas(ItemStack itemstack) {
        return GasStack.readFromNBT(ItemDataUtils.getCompound(itemstack, "stored"));
    }

    @Override
    public void setGas(ItemStack itemstack, GasStack stack) {
        if (itemstack.getCount() > 1) {
            return;
        }
        if (stack == null || stack.amount == 0) {
            ItemDataUtils.removeData(itemstack, "stored");
        } else {
            int amount = Math.max(0, Math.min(stack.amount, getMaxGas(itemstack)));
            GasStack gasStack = new GasStack(stack.getGas(), amount);
            ItemDataUtils.setCompound(itemstack, "stored", gasStack.write(new NBTTagCompound()));
        }
    }

    public ItemStack getEmptyItem(GasTankTier tier) {
        ItemStack empty = new ItemStack(this);
        setBaseTier(empty, tier.getBaseTier());
        setGas(empty, null);
        return empty;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tabs, @Nonnull NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tabs)) {
            return;
        }
        for (GasTankTier tier : GasTankTier.values()) {
            ItemStack empty = new ItemStack(this);
            setBaseTier(empty, tier.getBaseTier());
            list.add(empty);
        }
        if (MekanismConfig.current().general.prefilledGasTanks.val()) {
            for (Gas type : GasRegistry.getRegisteredGasses()) {
                if (type.isVisible() || MekanismConfig.current().mekce.ShowHiddenGas.val()) {
                    ItemStack filled = new ItemStack(this);
                    setBaseTier(filled, BaseTier.CREATIVE);
                    setGas(filled, new GasStack(type, ((IGasItem) filled.getItem()).getMaxGas(filled)));
                    list.add(filled);
                }
            }
        }
    }

    @Override
    public BaseTier getBaseTier(ItemStack itemstack) {
        if (!itemstack.hasTagCompound()) {
            return BaseTier.BASIC;
        }
        return BaseTier.values()[itemstack.getTagCompound().getInteger("tier")];
    }

    @Override
    public void setBaseTier(ItemStack itemstack, BaseTier tier) {
        if (!itemstack.hasTagCompound()) {
            itemstack.setTagCompound(new NBTTagCompound());
        }
        itemstack.getTagCompound().setInteger("tier", tier.ordinal());
    }

    @Override
    public int getMaxGas(ItemStack itemstack) {
        if (itemstack.getCount() > 1) {
            return 0;
        }
        return GasTankTier.values()[getBaseTier(itemstack).ordinal()].getStorage();
    }

    @Override
    public int getRate(ItemStack itemstack) {
        if (itemstack.getCount() > 1) {
            return 0;
        }
        return GasTankTier.values()[getBaseTier(itemstack).ordinal()].getOutput();
    }

    @Override
    public int addGas(ItemStack itemstack, GasStack stack) {
        if (itemstack.getCount() > 1) {
            return 0;
        }
        if (getGas(itemstack) != null && getGas(itemstack).getGas() != stack.getGas() && getBaseTier(itemstack) != BaseTier.CREATIVE && getGas(itemstack).getGas().isRadiation()) {
            return 0;
        }
        if (getBaseTier(itemstack) == BaseTier.CREATIVE) {
            setGas(itemstack, new GasStack(stack.getGas(), Integer.MAX_VALUE));
            return stack.amount;
        }
        int toUse = Math.min(getMaxGas(itemstack) - getStored(itemstack), Math.min(getRate(itemstack), stack.amount));
        setGas(itemstack, new GasStack(stack.getGas(), getStored(itemstack) + toUse));
        return toUse;
    }

    @Override
    public GasStack removeGas(ItemStack itemstack, int amount) {
        if (itemstack.getCount() > 1) {
            return null;
        }
        if (getGas(itemstack) == null) {
            return null;
        }
        Gas type = getGas(itemstack).getGas();
        int gasToUse = Math.min(getStored(itemstack), Math.min(getRate(itemstack), amount));
        if (getBaseTier(itemstack) != BaseTier.CREATIVE) {
            setGas(itemstack, new GasStack(type, getStored(itemstack) - gasToUse));
        }
        return new GasStack(type, gasToUse);
    }

    private int getStored(ItemStack itemstack) {
        if (itemstack.getCount() > 1) {
            return 0;
        }
        return getGas(itemstack) != null ? getGas(itemstack).amount : 0;
    }

    @Override
    public boolean canReceiveGas(ItemStack itemstack, Gas type) {
        if (itemstack.getCount() > 1) {
            return false;
        }
        if (getBaseTier(itemstack) != BaseTier.CREATIVE && type != null && type.isRadiation()) {
            return false;
        } else {
            return getGas(itemstack) == null || getGas(itemstack).getGas() == type;
        }
    }

    @Override
    public boolean canProvideGas(ItemStack itemstack, Gas type) {
        if (itemstack.getCount() > 1) {
            return false;
        }
        if (getBaseTier(itemstack) != BaseTier.CREATIVE && (getGas(itemstack) != null && (type == null || getGas(itemstack).getGas().isRadiation()))) {
            return false;
        } else {
            return getGas(itemstack) != null && (type == null || getGas(itemstack).getGas() == type);
        }
    }

    @Override
    public void setInventory(NBTTagList nbtTags, Object... data) {
        if (data[0] instanceof ItemStack stack) {
            ItemDataUtils.setList(stack, "Items", nbtTags);
        }
    }

    @Override
    public NBTTagList getInventory(Object... data) {
        if (data[0] instanceof ItemStack stack) {
            return ItemDataUtils.getList(stack, "Items");
        }
        return null;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getStored(stack) > 0; // No bar for empty containers as bars are drawn on top of stack count number
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1D - ((getGas(stack) != null ? (double) getGas(stack).amount : 0D) / (double) getMaxGas(stack));
    }

    @Override
    public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
        GasStack gas = getGas(stack);
        if (gas != null) {
            MekanismRenderer.color(gas);
            return gas.getGas().getTint();
        } else {
            return MathHelper.hsvToRGB(Math.max(0.0F, (float) (1 - getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public UUID getOwnerUUID(ItemStack stack) {
        if (ItemDataUtils.hasData(stack, "ownerUUID")) {
            return UUID.fromString(ItemDataUtils.getString(stack, "ownerUUID"));
        }
        return null;
    }

    @Override
    public void setOwnerUUID(ItemStack stack, UUID owner) {
        if (owner == null) {
            ItemDataUtils.removeData(stack, "ownerUUID");
        } else {
            ItemDataUtils.setString(stack, "ownerUUID", owner.toString());
        }
    }

    @Override
    public SecurityMode getSecurity(ItemStack stack) {
        if (!MekanismConfig.current().general.allowProtection.val()) {
            return SecurityMode.PUBLIC;
        }
        return SecurityMode.values()[ItemDataUtils.getInt(stack, "security")];
    }

    @Override
    public void setSecurity(ItemStack stack, SecurityMode mode) {
        if (getOwnerUUID(stack) == null) {
            ItemDataUtils.removeData(stack, "security");
        } else {
            ItemDataUtils.setInt(stack, "security", mode.ordinal());
        }
    }

    @Override
    public boolean hasSecurity(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hasOwner(ItemStack stack) {
        return hasSecurity(stack);
    }
}
