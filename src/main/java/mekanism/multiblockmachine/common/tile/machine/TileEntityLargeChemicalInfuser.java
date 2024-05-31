package mekanism.multiblockmachine.common.tile.machine;

import gregtech.client.shader.postprocessing.BloomType;
import gregtech.client.utils.BloomEffectUtil;
import io.netty.buffer.ByteBuf;
import mekanism.api.Coord4D;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.*;
import mekanism.common.Mekanism;
import mekanism.common.Upgrade;
import mekanism.common.base.IAdvancedBoundingBlock;
import mekanism.common.base.ISustainedData;
import mekanism.common.base.ITankManager;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.inputs.ChemicalPairInput;
import mekanism.common.recipe.machines.ChemicalInfuserRecipe;
import mekanism.common.recipe.outputs.GasOutput;
import mekanism.common.tier.GasTankTier;
import mekanism.common.util.*;
import mekanism.multiblockmachine.client.render.bloom.machine.BloomRenderLargeChemicalInfuser;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine;
import mekanism.multiblockmachine.common.tile.machine.prefab.TileEntityMultiblockBasicMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class TileEntityLargeChemicalInfuser extends TileEntityMultiblockBasicMachine<ChemicalPairInput, GasOutput, ChemicalInfuserRecipe>
        implements IGasHandler, ISustainedData, Upgrade.IUpgradeInfoHandler, ITankManager, IAdvancedBoundingBlock {

    public GasTank leftTank = new GasTank(GasTankTier.ULTIMATE.getStorage());
    public GasTank rightTank = new GasTank(GasTankTier.ULTIMATE.getStorage());
    public GasTank centerTank = new GasTank(GasTankTier.ULTIMATE.getStorage());
    public int gasOutput = 256;
    public ChemicalInfuserRecipe cachedRecipe;
    public int updateDelay;
    public boolean needsPacket;
    public int numPowering;
    public double clientEnergyUsed;
    public int output = 512;
    private int currentRedstoneLevel;
    private boolean rendererInitialized = false;

    public TileEntityLargeChemicalInfuser() {
        super("cheminfuser", BlockStateMultiblockMachine.MultiblockMachineType.LARGE_CHEMICAL_INFUSER, 1, 4);
        inventory = NonNullList.withSize(5, ItemStack.EMPTY);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            if (updateDelay > 0) {
                updateDelay--;
                if (updateDelay == 0) {
                    needsPacket = true;
                }
            }
            ChargeUtils.discharge(3, this);
            TileUtils.receiveGasItem(inventory.get(0), leftTank);
            TileUtils.receiveGasItem(inventory.get(1), rightTank);
            TileUtils.drawGas(inventory.get(2), centerTank);
            ChemicalInfuserRecipe recipe = getRecipe();
            if (canOperate(recipe) && MekanismUtils.canFunction(this) && getEnergy() >= energyPerTick) {
                setActive(true);
                operatingTicks++;
                if (operatingTicks >= ticksRequired) {
                    for (int i = 0; i <= Thread(); i++) {
                        operate(recipe);
                    }
                    operatingTicks = 0;
                }
                double prev = getEnergy();
                setEnergy(getEnergy() - energyPerTick * getUpgradedUsage(recipe) * Thread());
                clientEnergyUsed = prev - getEnergy();
            } else if (prevEnergy >= getEnergy()) {
                setActive(false);
            }
            prevEnergy = getEnergy();
            handleTank(centerTank, getLeftTankside());
            handleTank(centerTank, getRightTankside());
            int newRedstoneLevel = getRedstoneLevel();
            if (newRedstoneLevel != currentRedstoneLevel) {
                world.updateComparatorOutputLevel(pos, getBlockType());
                currentRedstoneLevel = newRedstoneLevel;
            }
            if (needsPacket) {
                Mekanism.packetHandler.sendUpdatePacket(this);
            }
            needsPacket = false;
        } else if (updateDelay > 0) {
            updateDelay--;
            if (updateDelay == 0) {
                MekanismUtils.updateBlock(world, getPos());
            }
        }
    }

    private TileEntity getLeftTankside() {
        BlockPos pos = getPos().offset(facing).offset(MekanismUtils.getLeft(facing));
        if (world.getTileEntity(pos) != null) {
            return world.getTileEntity(pos);
        }
        return null;
    }

    private TileEntity getRightTankside() {
        BlockPos pos = getPos().offset(facing).offset(MekanismUtils.getRight(facing));
        if (world.getTileEntity(pos) != null) {
            return world.getTileEntity(pos);
        }
        return null;
    }

    private void handleTank(GasTank tank, TileEntity tile) {
        if (tank.getGas() != null) {
            GasStack toSend = new GasStack(tank.getGas().getGas(), Math.min(tank.getStored(), output));
            tank.draw(GasUtils.emit(toSend, tile, EnumSet.of(facing)), true);
        }
    }

    public int getUpgradedUsage(ChemicalInfuserRecipe recipe) {
        int possibleProcess = Math.min((int) Math.pow(2, upgradeComponent.getUpgrades(Upgrade.SPEED)), MekanismConfig.current().mekce.MAXspeedmachines.val());
        if (leftTank.getGasType() == recipe.recipeInput.leftGas.getGas()) {
            possibleProcess = Math.min(leftTank.getStored() / recipe.recipeInput.leftGas.amount, possibleProcess);
            possibleProcess = Math.min(rightTank.getStored() / recipe.recipeInput.rightGas.amount, possibleProcess);
        } else {
            possibleProcess = Math.min(leftTank.getStored() / recipe.recipeInput.rightGas.amount, possibleProcess);
            possibleProcess = Math.min(rightTank.getStored() / recipe.recipeInput.leftGas.amount, possibleProcess);
        }
        possibleProcess = Math.min(centerTank.getNeeded() / recipe.recipeOutput.output.amount, possibleProcess);
        possibleProcess = Math.min((int) (getEnergy() / energyPerTick), possibleProcess);
        return possibleProcess;
    }

    public ChemicalPairInput getInput() {
        return new ChemicalPairInput(leftTank.getGas(), rightTank.getGas());
    }

    public ChemicalInfuserRecipe getRecipe() {
        ChemicalPairInput input = getInput();
        if (cachedRecipe == null || !input.testEquality(cachedRecipe.getInput())) {
            cachedRecipe = RecipeHandler.getChemicalInfuserRecipe(getInput());
        }
        return cachedRecipe;
    }

    public boolean canOperate(ChemicalInfuserRecipe recipe) {
        return recipe != null && recipe.canOperate(leftTank, rightTank, centerTank);
    }


    public void operate(ChemicalInfuserRecipe recipe) {
        recipe.operate(leftTank, rightTank, centerTank, getUpgradedUsage(recipe));
    }

    @Override
    public Map<ChemicalPairInput, ChemicalInfuserRecipe> getRecipes() {
        return RecipeHandler.Recipe.CHEMICAL_INFUSER.get();
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            clientEnergyUsed = dataStream.readDouble();
            TileUtils.readTankData(dataStream, leftTank);
            TileUtils.readTankData(dataStream, rightTank);
            TileUtils.readTankData(dataStream, centerTank);
            numPowering = dataStream.readInt();
            if (updateDelay == 0) {
                updateDelay = MekanismConfig.current().general.UPDATE_DELAY.val();
                MekanismUtils.updateBlock(world, getPos());
            }
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(clientEnergyUsed);
        TileUtils.addTankData(data, leftTank);
        TileUtils.addTankData(data, rightTank);
        TileUtils.addTankData(data, centerTank);
        data.add(numPowering);
        return data;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        leftTank.read(nbtTags.getCompoundTag("leftTank"));
        rightTank.read(nbtTags.getCompoundTag("rightTank"));
        centerTank.read(nbtTags.getCompoundTag("centerTank"));
        numPowering = nbtTags.getInteger("numPowering");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setTag("leftTank", leftTank.write(new NBTTagCompound()));
        nbtTags.setTag("rightTank", rightTank.write(new NBTTagCompound()));
        nbtTags.setTag("centerTank", centerTank.write(new NBTTagCompound()));
        nbtTags.setInteger("numPowering", numPowering);
    }


    @Nonnull
    @Override
    public GasTankInfo[] getTankInfo() {
        return new GasTankInfo[]{leftTank, centerTank, rightTank};
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        if (side == MekanismUtils.getBack(facing) || side == MekanismUtils.getLeft(facing) || side == MekanismUtils.getRight(facing)) {
            return leftTank.canReceive(type) || rightTank.canReceive(type);
        }
        return false;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        return centerTank.canDraw(type) && side == facing;
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        if (canReceiveGas(side, stack != null ? stack.getGas() : null)) {
            if (stack != null && (side == MekanismUtils.getBack(facing) || side == MekanismUtils.getLeft(facing) || side == MekanismUtils.getRight(facing))) {
                if (leftTank.canReceive(stack.getGas()) && rightTank.getGasType() != stack.getGas()) {
                    return leftTank.receive(stack, doTransfer);
                }
                if (rightTank.canReceive(stack.getGas()) && leftTank.getGasType() != stack.getGas()) {
                    return rightTank.receive(stack, doTransfer);
                }
            }
        }
        return 0;
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        if (canDrawGas(side, null)) {
            return centerTank.draw(amount, doTransfer);
        }
        return null;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return false;
        }
        return capability == Capabilities.GAS_HANDLER_CAPABILITY || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return null;
        } else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
        }
        return super.getCapability(capability, side);
    }

    @Override
    public boolean isItemValidForSlot(int slotID, @Nonnull ItemStack itemstack) {
        return slotID == 3 && ChargeUtils.canBeDischarged(itemstack);
    }

    @NotNull
    @Override
    public int[] getSlotsForFace(@NotNull EnumFacing side) {
        return InventoryUtils.EMPTY;
    }

    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        if (slotID == 0 || slotID == 2) {
            return !itemstack.isEmpty() && itemstack.getItem() instanceof IGasItem && ((IGasItem) itemstack.getItem()).canReceiveGas(itemstack, null);
        } else if (slotID == 1) {
            return !itemstack.isEmpty() && itemstack.getItem() instanceof IGasItem && ((IGasItem) itemstack.getItem()).canProvideGas(itemstack, null);
        } else if (slotID == 3) {
            return ChargeUtils.canBeOutputted(itemstack, false);
        }
        return false;
    }

    @Override
    public void writeSustainedData(ItemStack itemStack) {
        if (leftTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "leftTank", leftTank.getGas().write(new NBTTagCompound()));
        }
        if (rightTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "rightTank", rightTank.getGas().write(new NBTTagCompound()));
        }
        if (centerTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "centerTank", centerTank.getGas().write(new NBTTagCompound()));
        }
    }

    @Override
    public void readSustainedData(ItemStack itemStack) {
        leftTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "leftTank")));
        rightTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "rightTank")));
        centerTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "centerTank")));
    }

    @Override
    public List<String> getInfo(Upgrade upgrade) {
        return upgrade == Upgrade.SPEED ? upgrade.getExpScaledInfo(this) : upgrade.getMultScaledInfo(this);
    }

    @Override
    public Object[] getTanks() {
        return new Object[]{leftTank, rightTank, centerTank};
    }


    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (updateDelay == 0) {
            Mekanism.packetHandler.sendUpdatePacket(this);
            updateDelay = 10;
        }
    }

    @Override
    public String[] getMethods() {
        return new String[0];
    }

    @Override
    public Object[] invoke(int method, Object[] args) throws NoSuchMethodException {
        return new Object[0];
    }

    public double getScaledLeftTankGasLevel() {
        return (double) leftTank.getStored() / leftTank.getMaxGas();
    }

    public double getScaledRightTankGasLevel() {
        return (double) rightTank.getStored() / rightTank.getMaxGas();
    }

    public double getScaledGasTankLevel() {
        return (double) centerTank.getStored() / centerTank.getMaxGas();
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(centerTank.getStored(), centerTank.getMaxGas());
    }

    @Override
    public boolean isPowered() {
        return redstone || numPowering > 0;
    }


    @Override
    public boolean canBoundReceiveEnergy(BlockPos coord, EnumFacing side) {
        EnumFacing back = MekanismUtils.getBack(facing);
        if (coord.equals(getPos().offset(back))) {
            return side == back;
        }
        return false;
    }

    @Override
    public boolean canBoundOutPutEnergy(BlockPos location, EnumFacing side) {
        return false;
    }

    @Override
    public void onPower() {
        numPowering++;
    }

    @Override
    public void onNoPower() {
        numPowering--;
    }

    @Override
    public NBTTagCompound getConfigurationData(NBTTagCompound nbtTags) {
        return nbtTags;
    }

    @Override
    public void setConfigurationData(NBTTagCompound nbtTags) {

    }

    @Override
    public String getDataType() {
        return getBlockType().getTranslationKey() + "." + fullName + ".name";
    }

    @Override
    public void onPlace() {
        for (int y = 0; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    BlockPos pos1 = getPos().add(x, y, z);
                    if (y == 0) {
                        MekanismUtils.makeAdvancedBoundingBlock(world, pos1, Coord4D.get(this));
                    } else {
                        MekanismUtils.makeBoundingBlock(world, pos1, Coord4D.get(this));
                    }
                    world.notifyNeighborsOfStateChange(pos1, getBlockType(), true);
                }
            }
        }
    }

    @Override
    public void onBreak() {
        for (int y = 0; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    world.setBlockToAir(getPos().add(x, y, z));
                }
            }
        }
    }


    @Override
    public boolean hasOffsetCapability(@NotNull Capability<?> capability, @Nullable EnumFacing side, @NotNull Vec3i offset) {
        if (isOffsetCapabilityDisabled(capability, side, offset)) {
            return false;
        }
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return true;
        } else if (isStrictEnergy(capability) || capability == CapabilityEnergy.ENERGY || isTesla(capability, side)) {
            return true;
        }
        return hasCapability(capability, side);
    }

    @Nullable
    @Override
    public <T> T getOffsetCapability(@NotNull Capability<T> capability, @Nullable EnumFacing side, @NotNull Vec3i offset) {
        if (isOffsetCapabilityDisabled(capability, side, offset)) {
            return null;
        } else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
        } else if (isStrictEnergy(capability)) {
            return (T) this;
        } else if (isTesla(capability, side)) {
            return (T) getTeslaEnergyWrapper(side);
        } else if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(getForgeEnergyWrapper(side));
        }
        return getCapability(capability, side);
    }

    @Override
    public boolean isOffsetCapabilityDisabled(@NotNull Capability<?> capability, @Nullable EnumFacing side, @NotNull Vec3i offset) {
        EnumFacing back = facing.getOpposite();
        EnumFacing left = MekanismUtils.getLeft(facing);
        EnumFacing right = MekanismUtils.getRight(facing);
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.EAST) {
                if (offset.equals(new Vec3i(1, 0, 1))) {
                    return side != facing;
                }
                if (offset.equals(new Vec3i(1, 0, -1))) {
                    return side != facing;
                }
                if (offset.equals(new Vec3i(-1, 0, 1))) {
                    return side != left && side != back;
                }
                if (offset.equals(new Vec3i(-1, 0, -1))) {
                    return side != right && side != back;
                }
            } else if (facing == EnumFacing.SOUTH) {
                if (offset.equals(new Vec3i(-1, 0, 1))) {
                    return side != facing;
                }
                if (offset.equals(new Vec3i(1, 0, 1))) {
                    return side != facing;
                }
                if (offset.equals(new Vec3i(1, 0, -1))) {
                    return side != right && side != back;
                }
                if (offset.equals(new Vec3i(-1, 0, -1))) {
                    return side != left && side != back;
                }
            } else if (facing == EnumFacing.WEST) {
                if (offset.equals(new Vec3i(-1, 0, 1))) {
                    return side != facing;
                }
                if (offset.equals(new Vec3i(-1, 0, -1))) {
                    return side != facing;
                }
                if (offset.equals(new Vec3i(1, 0, -1))) {
                    return side != left && side != back;
                }
                if (offset.equals(new Vec3i(1, 0, 1))) {
                    return side != right && side != back;
                }
            } else if (facing == EnumFacing.NORTH) {
                if (offset.equals(new Vec3i(-1, 0, -1)) || offset.equals(new Vec3i(1, 0, -1))) {
                    return side != facing;
                }
                if (offset.equals(new Vec3i(1, 0, 1))) {
                    return side != left && side != back;
                }
                if (offset.equals(new Vec3i(-1, 0, 1))) {
                    return side != right && side != back;
                }
            }
            return true;
        }

        if (isStrictEnergy(capability) || capability == CapabilityEnergy.ENERGY || isTesla(capability, side)) {
            if (offset.equals(new Vec3i(back.getXOffset(), 0, back.getZOffset()))) {
                return side != back;
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side) {
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return true;
        } else if (isStrictEnergy(capability) || capability == CapabilityEnergy.ENERGY || isTesla(capability, side)) {
            return true;
        }
        return super.isCapabilityDisabled(capability, side);
    }

    @Override
    public void validate() {
        super.validate();
        if (world.isRemote && !rendererInitialized) {
            rendererInitialized = true;
            if (Mekanism.hooks.GTCEULoaded) {
                GTECUBloom();
            } else if (Mekanism.hooks.LumenizedLoaded) {
                LumenizedBloom();
            }
        }
    }

    @Optional.Method(modid = MekanismHooks.GTCEU_MOD_ID)
    public void GTECUBloom() {
        BloomRenderLargeChemicalInfuser renderer = new BloomRenderLargeChemicalInfuser(this);
        BloomEffectUtil.registerBloomRender(renderer, BloomType.UNREAL, renderer, ticket -> !isInvalid());
    }

    @Optional.Method(modid = MekanismHooks.LUMENIZED_MOD_ID)
    public void LumenizedBloom() {
        BloomRenderLargeChemicalInfuser renderer = new BloomRenderLargeChemicalInfuser(this);
        BloomEffectUtil.registerBloomRender(renderer, BloomType.UNREAL, renderer, ticket -> !isInvalid());
    }
}
