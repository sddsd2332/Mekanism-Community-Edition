package mekanism.multiblockmachine.common.item;

import cofh.redstoneflux.api.IEnergyContainerItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import mekanism.api.EnumColor;
import mekanism.api.energy.IEnergizedItem;
import mekanism.client.MekKeyHandler;
import mekanism.client.MekanismClient;
import mekanism.client.MekanismKeyHandler;
import mekanism.common.Upgrade;
import mekanism.common.base.*;
import mekanism.common.capabilities.ItemCapabilityWrapper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.integration.forgeenergy.ForgeEnergyItemWrapper;
import mekanism.common.integration.ic2.IC2ItemManager;
import mekanism.common.integration.redstoneflux.RFIntegration;
import mekanism.common.integration.tesla.TeslaItemWrapper;
import mekanism.common.security.ISecurityItem;
import mekanism.common.security.ISecurityTile;
import mekanism.common.tile.prefab.TileEntityBasicBlock;
import mekanism.common.tile.prefab.TileEntityElectricBlock;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.SecurityUtils;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine.MultiblockMachineType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Optional.InterfaceList({
        @Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyContainerItem", modid = MekanismHooks.REDSTONEFLUX_MOD_ID),
        @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = MekanismHooks.IC2_MOD_ID)
})
public class ItemBlockMultiblockMachine extends ItemBlock implements IEnergizedItem, ISpecialElectricItem, ISustainedInventory, ISustainedTank, IEnergyContainerItem, ISecurityItem {
    public Block metaBlock;

    public ItemBlockMultiblockMachine(Block block) {
        super(block);
        metaBlock = block;
        setHasSubtypes(true);
        setNoRepair();
      //  setMaxStackSize(1);
    }

    @Override
    public int getMetadata(int i) {
        return i;
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack itemstack) {
        if (MultiblockMachineType.get(itemstack) != null) {
            return getTranslationKey() + "." + MultiblockMachineType.get(itemstack).blockName;
        }
        return "null";
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack itemstack) {
        return super.getItemStackDisplayName(itemstack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack itemstack, World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flag) {
        MultiblockMachineType type = MultiblockMachineType.get(itemstack);
        if (type != null) { // If the block is null, no information is added
            if (!MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.sneakKey)) {
                list.add(LangUtils.localize("tooltip.hold") + " " + EnumColor.INDIGO + GameSettings.getKeyDisplayString(MekanismKeyHandler.sneakKey.getKeyCode()) +
                        EnumColor.GREY + " " + LangUtils.localize("tooltip.forDetails") + ".");
                list.add(LangUtils.localize("tooltip.hold") + " " + EnumColor.AQUA + GameSettings.getKeyDisplayString(MekanismKeyHandler.sneakKey.getKeyCode()) +
                        EnumColor.GREY + " " + LangUtils.localize("tooltip.and") + " " + EnumColor.AQUA +
                        GameSettings.getKeyDisplayString(MekanismKeyHandler.handModeSwitchKey.getKeyCode()) + EnumColor.GREY + " " + LangUtils.localize("tooltip.forDesc") + ".");
            } else if (!MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.handModeSwitchKey)) {
                if (hasSecurity(itemstack)) {
                    list.add(SecurityUtils.getOwnerDisplay(Minecraft.getMinecraft().player, MekanismClient.clientUUIDMap.get(getOwnerUUID(itemstack))));
                    list.add(EnumColor.GREY + LangUtils.localize("gui.security") + ": " + SecurityUtils.getSecurityDisplay(itemstack, Side.CLIENT));
                    if (SecurityUtils.isOverridden(itemstack, Side.CLIENT)) {
                        list.add(EnumColor.RED + "(" + LangUtils.localize("gui.overridden") + ")");
                    }
                }
                if (type.isElectric && itemstack.getCount() <= 1) {
                    list.add(EnumColor.BRIGHT_GREEN + LangUtils.localize("tooltip.storedEnergy") + ": " + EnumColor.GREY
                            + MekanismUtils.getEnergyDisplay(getEnergy(itemstack), getMaxEnergy(itemstack)));
                }

                list.add(EnumColor.AQUA + LangUtils.localize("tooltip.inventory") + ": " + EnumColor.GREY + LangUtils.transYesNo(getInventory(itemstack) != null && getInventory(itemstack).tagCount() != 0));

                if (type.supportsUpgrades && ItemDataUtils.hasData(itemstack, "upgrades")) {
                    Map<Upgrade, Integer> upgrades = Upgrade.buildMap(ItemDataUtils.getDataMap(itemstack));
                    for (Map.Entry<Upgrade, Integer> entry : upgrades.entrySet()) {
                        list.add(entry.getKey().getColor() + "- " + entry.getKey().getName() + (entry.getKey().canMultiply() ? ": " + EnumColor.GREY + "x" + entry.getValue() : ""));
                    }
                }

            } else {
                list.addAll(MekanismUtils.splitTooltip(type.getDescription(), itemstack));
            }
        }
    }


    @Override
    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState state) {
        if (stack.getCount() > 1 && MekanismConfig.current().mekce.StackingPlacementLimits.val()) {
            return false;
        }
        boolean place = true;
        MultiblockMachineType type = MultiblockMachineType.get(stack);
        if (type == MultiblockMachineType.LARGE_ELECTROLYTIC_SEPARATOR || type == MultiblockMachineType.LARGE_CHEMICAL_INFUSER) {
            BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();
            for (int yPos = 0; yPos <= 1; yPos++) {
                for (int xPos = -1; xPos <= 1; xPos++) {
                    for (int zPos = -1; zPos <= 1; zPos++) {
                        testPos.setPos(pos.getX() + xPos, pos.getY() + yPos, pos.getZ() + zPos);
                        Block b = world.getBlockState(testPos).getBlock();
                        if (!world.isValid(testPos) || !world.isBlockLoaded(testPos, false) || !b.isReplaceable(world, testPos)) {
                            place = false;
                        }
                    }
                }
            }
        } else if (type == MultiblockMachineType.LARGE_CHEMICAL_WASHER) {
            BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();
            for (int yPos = 0; yPos <= 2; yPos++) {
                for (int xPos = -1; xPos <= 1; xPos++) {
                    for (int zPos = -1; zPos <= 1; zPos++) {
                        testPos.setPos(pos.getX() + xPos, pos.getY() + yPos, pos.getZ() + zPos);
                        Block b = world.getBlockState(testPos).getBlock();
                        if (!world.isValid(testPos) || !world.isBlockLoaded(testPos, false) || !b.isReplaceable(world, testPos)) {
                            place = false;
                        }
                    }
                }
            }
        } else if (type == MultiblockMachineType.DIGITAL_ASSEMBLY_TABLE) {
            BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();
            for (int yPos = 0; yPos <= 4; yPos++) {
                for (int xPos = -5; xPos <= 5; xPos++) {
                    for (int zPos = -5; zPos <= 5; zPos++) {
                        testPos.setPos(pos.getX() + xPos, pos.getY() + yPos, pos.getZ() + zPos);
                        Block b = world.getBlockState(testPos).getBlock();
                        if (!world.isValid(testPos) || !world.isBlockLoaded(testPos, false) || !b.isReplaceable(world, testPos)) {
                            place = false;
                        }
                    }
                }
            }
        }
        if (place && super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state)) {
            TileEntityBasicBlock tileEntity = (TileEntityBasicBlock) world.getTileEntity(pos);
            if (tileEntity instanceof ISecurityTile security) {
                security.getSecurity().setOwnerUUID(getOwnerUUID(stack));
                if (hasSecurity(stack)) {
                    security.getSecurity().setMode(getSecurity(stack));
                }
                if (getOwnerUUID(stack) == null) {
                    security.getSecurity().setOwnerUUID(player.getUniqueID());
                }
            }

            if (tileEntity instanceof IUpgradeTile upgradeTile) {
                if (ItemDataUtils.hasData(stack, "upgrades")) {
                    upgradeTile.getComponent().read(ItemDataUtils.getDataMap(stack));
                }
            }

            if (tileEntity instanceof ISustainedData data) {
                if (stack.getTagCompound() != null) {
                    data.readSustainedData(stack);
                }
            }

            if (tileEntity instanceof IRedstoneControl control) {
                if (ItemDataUtils.hasData(stack, "controlType")) {
                    control.setControlType(IRedstoneControl.RedstoneControl.values()[ItemDataUtils.getInt(stack, "controlType")]);
                }
            }

            if (tileEntity instanceof ISustainedTank tank) {
                if (hasTank(stack) && getFluidStack(stack) != null) {
                    tank.setFluidStack(getFluidStack(stack));
                }
            }
            if (tileEntity instanceof ISustainedInventory inventory) {
                inventory.setInventory(getInventory(stack));
            }
            if (tileEntity instanceof TileEntityElectricBlock entityElectricBlock) {
                entityElectricBlock.electricityStored.set(getEnergy(stack));
            }
            return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityplayer, @Nonnull EnumHand hand) {
        ItemStack itemstack = entityplayer.getHeldItem(hand);
        MultiblockMachineType type = MultiblockMachineType.get(itemstack);
        return new ActionResult<>(EnumActionResult.PASS, itemstack);
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
    public void setFluidStack(FluidStack fluidStack, Object... data) {
        if (data[0] instanceof ItemStack itemStack) {
            if (fluidStack == null || fluidStack.amount == 0) {
                ItemDataUtils.removeData(itemStack, "fluidTank");
            } else {
                ItemDataUtils.setCompound(itemStack, "fluidTank", fluidStack.writeToNBT(new NBTTagCompound()));
            }
        }
    }

    @Override
    public FluidStack getFluidStack(Object... data) {
        if (data[0] instanceof ItemStack itemStack) {
            if (!ItemDataUtils.hasData(itemStack, "fluidTank")) {
                return null;
            }
            return FluidStack.loadFluidStackFromNBT(ItemDataUtils.getCompound(itemStack, "fluidTank"));
        }
        return null;
    }

    @Override
    public boolean hasTank(Object... data) {
        return data[0] instanceof ItemStack stack && stack.getItem() instanceof ISustainedTank && (stack.getItemDamage() == 2);
    }

    @Override
    public double getEnergy(ItemStack itemStack) {
        if (itemStack.getCount() > 1) {
            return 0;
        }
        if (!MultiblockMachineType.get(itemStack).isElectric) {
            return 0;
        }
        return ItemDataUtils.getDouble(itemStack, "energyStored");
    }

    @Override
    public void setEnergy(ItemStack itemStack, double amount) {
        if (!MultiblockMachineType.get(itemStack).isElectric) {
            return;
        }
        if (amount == 0) {
            NBTTagCompound dataMap = ItemDataUtils.getDataMap(itemStack);
            dataMap.removeTag("energyStored");
            if (dataMap.isEmpty() && itemStack.getTagCompound() != null) {
                itemStack.getTagCompound().removeTag(ItemDataUtils.DATA_ID);
            }
        } else {
            ItemDataUtils.setDouble(itemStack, "energyStored", Math.max(Math.min(amount, getMaxEnergy(itemStack)), 0));
        }
    }

    @Override
    public double getMaxEnergy(ItemStack itemStack) {
        MultiblockMachineType type = MultiblockMachineType.get(itemStack);
        return ItemDataUtils.hasData(itemStack, "upgrades") ? MekanismUtils.getMaxEnergy(itemStack, type.getStorage()) : type.getStorage();
    }

    @Override
    public double getMaxTransfer(ItemStack itemStack) {
        if (itemStack.getCount() > 1) {
            return 0;
        }
        return getMaxEnergy(itemStack) * 0.005;
    }

    @Override
    public boolean canReceive(ItemStack itemStack) {
        return MultiblockMachineType.get(itemStack).isElectric && itemStack.getCount() == 1;
    }

    @Override
    public boolean canSend(ItemStack itemStack) {
        return false;
    }

    @Override
    @Optional.Method(modid = MekanismHooks.REDSTONEFLUX_MOD_ID)
    public int receiveEnergy(ItemStack theItem, int energy, boolean simulate) {
        if (theItem.getCount() > 1) {
            return 0;
        }
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
        if (theItem.getCount() > 1) {
            return 0;
        }
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
            return;
        }
        ItemDataUtils.setString(stack, "ownerUUID", owner.toString());
    }

    @Override
    public ISecurityTile.SecurityMode getSecurity(ItemStack stack) {
        if (!MekanismConfig.current().general.allowProtection.val()) {
            return ISecurityTile.SecurityMode.PUBLIC;
        }
        return ISecurityTile.SecurityMode.values()[ItemDataUtils.getInt(stack, "security")];
    }

    @Override
    public void setSecurity(ItemStack stack, ISecurityTile.SecurityMode mode) {
        if (getOwnerUUID(stack) == null) {
            ItemDataUtils.removeData(stack, "security");
        } else {
            ItemDataUtils.setInt(stack, "security", mode.ordinal());
        }
    }

    @Override
    public boolean hasSecurity(ItemStack stack) {
        MultiblockMachineType type = MultiblockMachineType.get(stack);
        return true;
    }

    @Override
    public boolean hasOwner(ItemStack stack) {
        return hasSecurity(stack);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new ItemCapabilityWrapper(stack, new TeslaItemWrapper(), new ForgeEnergyItemWrapper());
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
}
