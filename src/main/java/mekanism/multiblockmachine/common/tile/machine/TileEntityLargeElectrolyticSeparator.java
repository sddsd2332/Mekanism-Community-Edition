package mekanism.multiblockmachine.common.tile.machine;

import io.netty.buffer.ByteBuf;
import mekanism.api.Coord4D;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.*;
import mekanism.common.Mekanism;
import mekanism.common.MekanismFluids;
import mekanism.common.Upgrade;
import mekanism.common.Upgrade.IUpgradeInfoHandler;
import mekanism.common.base.*;
import mekanism.common.block.states.BlockStateMachine;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.inputs.FluidInput;
import mekanism.common.recipe.machines.SeparatorRecipe;
import mekanism.common.recipe.outputs.ChemicalPairOutput;
import mekanism.common.tier.FluidTankTier;
import mekanism.common.tier.GasTankTier;
import mekanism.common.tile.TileEntityGasTank.GasMode;
import mekanism.common.util.*;
import mekanism.multiblockmachine.client.render.bloom.machine.BloomRenderLargeElectrolyticSeparator;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class TileEntityLargeElectrolyticSeparator extends TileEntityMultiblockBasicMachine<FluidInput, ChemicalPairOutput, SeparatorRecipe>
        implements IFluidHandlerWrapper, ISustainedData, IGasHandler, IUpgradeInfoHandler, ITankManager, IAdvancedBoundingBlock {

    private static final String[] methods = new String[]{"getEnergy", "getOutput", "getMaxEnergy", "getEnergyNeeded", "getWater", "getWaterNeeded", "getHydrogen",
            "getHydrogenNeeded", "getOxygen", "getOxygenNeeded"};

    public FluidTank fluidTank = new FluidTankSync(FluidTankTier.ULTIMATE.getStorage());

    public GasTank leftTank = new GasTank(GasTankTier.ULTIMATE.getStorage());

    public GasTank rightTank = new GasTank(GasTankTier.ULTIMATE.getStorage());

    public int output = 512;

    public GasMode dumpLeft = GasMode.IDLE;

    public GasMode dumpRight = GasMode.IDLE;

    public SeparatorRecipe cachedRecipe;
    public double clientEnergyUsed;
    public int updateDelay;
    public boolean needsPacket;
    public int numPowering;
    private int currentRedstoneLevel;
    private boolean rendererInitialized = false;

    public TileEntityLargeElectrolyticSeparator() {
        super("electrolyticseparator", BlockStateMultiblockMachine.MultiblockMachineType.LARGE_ELECTROLYTIC_SEPARATOR, 1, 4);
        inventory = NonNullListSynchronized.withSize(5, ItemStack.EMPTY);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            ChargeUtils.discharge(3, this);
            if (updateDelay > 0) {
                updateDelay--;
                if (updateDelay == 0) {
                    needsPacket = true;
                }
            }
            if (!inventory.get(0).isEmpty()) {
                if (RecipeHandler.Recipe.ELECTROLYTIC_SEPARATOR.containsRecipe(inventory.get(0))) {
                    if (FluidContainerUtils.isFluidContainer(inventory.get(0))) {
                        fluidTank.fill(FluidContainerUtils.extractFluid(fluidTank, this, 0), true);
                    }
                }
            }

            if (!inventory.get(1).isEmpty() && leftTank.getStored() > 0) {
                leftTank.draw(GasUtils.addGas(inventory.get(1), leftTank.getGas()), true);
                MekanismUtils.saveChunk(this);
            }
            if (!inventory.get(2).isEmpty() && rightTank.getStored() > 0) {
                rightTank.draw(GasUtils.addGas(inventory.get(2), rightTank.getGas()), true);
                MekanismUtils.saveChunk(this);
            }
            SeparatorRecipe recipe = getRecipe();

            if (canOperate(recipe) && getEnergy() >= energyPerTick && MekanismUtils.canFunction(this)) {
                setActive(true);
                int thread = 1;
                if (upgradeComponent.isUpgradeInstalled(Upgrade.THREAD)) {
                    thread = upgradeComponent.getUpgrades(Upgrade.THREAD) + 1;
                }
                boolean update = BASE_ENERGY_PER_TICK != recipe.energyUsage;
                BASE_ENERGY_PER_TICK = recipe.energyUsage;
                if (update) {
                    recalculateUpgradables(Upgrade.ENERGY);
                }
                operatingTicks++;
                if (operatingTicks >= ticksRequired) {
                    for (int i = 0; i <= thread; i++) {
                        operate(recipe);
                    }
                    operatingTicks = 0;
                }
                double prev = getEnergy();
                setEnergy(getEnergy() - energyPerTick * getUpgradedUsage(recipe) * thread);
                clientEnergyUsed = prev - getEnergy();
            } else if (prevEnergy >= getEnergy()) {
                setActive(false);
            }
            int dumpAmount = 8 * Math.min((int) Math.pow(2, upgradeComponent.getUpgrades(Upgrade.SPEED)), MekanismConfig.current().mekce.MAXspeedmachines.val());
            handleTank(leftTank, dumpLeft, getLeftTankside(), dumpAmount);
            handleTank(rightTank, dumpRight, getRightTankside(), dumpAmount);
            prevEnergy = getEnergy();

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

    private void handleTank(GasTank tank, GasMode mode, TileEntity tile, int dumpAmount) {
        if (tank.getGas() != null) {
            if (mode != GasMode.DUMPING) {
                GasStack toSend = new GasStack(tank.getGas().getGas(), Math.min(tank.getStored(), output));
                tank.draw(GasUtils.emit(toSend, tile, EnumSet.of(facing)), true);
            } else {
                tank.draw(dumpAmount, true);
            }
            if (mode == GasMode.DUMPING_EXCESS && tank.getNeeded() < output) {
                tank.draw(output - tank.getNeeded(), true);
            }
        }
    }

    public int getUpgradedUsage(SeparatorRecipe recipe) {
        int possibleProcess;
        if (leftTank.getGasType() == recipe.recipeOutput.leftGas.getGas()) {
            possibleProcess = leftTank.getNeeded() / recipe.recipeOutput.leftGas.amount;
            possibleProcess = Math.min(rightTank.getNeeded() / recipe.recipeOutput.rightGas.amount, possibleProcess);
        } else {
            possibleProcess = leftTank.getNeeded() / recipe.recipeOutput.rightGas.amount;
            possibleProcess = Math.min(rightTank.getNeeded() / recipe.recipeOutput.leftGas.amount, possibleProcess);
        }
        possibleProcess = Math.min(Math.min((int) Math.pow(2, upgradeComponent.getUpgrades(Upgrade.SPEED)), MekanismConfig.current().mekce.MAXspeedmachines.val()), possibleProcess);
        possibleProcess = Math.min((int) (getEnergy() / energyPerTick), possibleProcess);
        return Math.min(fluidTank.getFluidAmount() / recipe.recipeInput.ingredient.amount, possibleProcess);
    }

    public SeparatorRecipe getRecipe() {
        FluidInput input = getInput();
        if (cachedRecipe == null || !input.testEquality(cachedRecipe.getInput())) {
            cachedRecipe = RecipeHandler.getElectrolyticSeparatorRecipe(getInput());
        }
        return cachedRecipe;
    }

    public FluidInput getInput() {
        return new FluidInput(fluidTank.getFluid());
    }


    public boolean canOperate(SeparatorRecipe recipe) {
        return recipe != null && recipe.canOperate(fluidTank, leftTank, rightTank);
    }

    @Override
    public void operate(SeparatorRecipe recipe) {
        recipe.operate(fluidTank, leftTank, rightTank, getUpgradedUsage(recipe));
    }

    @Override
    public Map<FluidInput, SeparatorRecipe> getRecipes() {
        return RecipeHandler.Recipe.ELECTROLYTIC_SEPARATOR.get();
    }

    public boolean canFill(ChemicalPairOutput gases) {
        return leftTank.canReceive(gases.leftGas.getGas()) && leftTank.getNeeded() >= gases.leftGas.amount
                && rightTank.canReceive(gases.rightGas.getGas()) && rightTank.getNeeded() >= gases.rightGas.amount;
    }


    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        if (slotID == 3) {
            return ChargeUtils.canBeOutputted(itemstack, false);
        } else if (slotID == 0) {
            return FluidUtil.getFluidContained(itemstack) == null;
        } else if (slotID == 1 || slotID == 2) {
            return itemstack.getItem() instanceof IGasItem && ((IGasItem) itemstack.getItem()).getGas(itemstack) != null
                    && ((IGasItem) itemstack.getItem()).getGas(itemstack).amount == ((IGasItem) itemstack.getItem()).getMaxGas(itemstack);
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, @Nonnull ItemStack itemstack) {
        if (slotID == 0) {
            return RecipeHandler.Recipe.ELECTROLYTIC_SEPARATOR.containsRecipe(itemstack);
        } else if (slotID == 1) {
            return itemstack.getItem() instanceof IGasItem &&
                    (((IGasItem) itemstack.getItem()).getGas(itemstack) == null || ((IGasItem) itemstack.getItem()).getGas(itemstack).getGas() == MekanismFluids.Hydrogen);
        } else if (slotID == 2) {
            return itemstack.getItem() instanceof IGasItem &&
                    (((IGasItem) itemstack.getItem()).getGas(itemstack) == null || ((IGasItem) itemstack.getItem()).getGas(itemstack).getGas() == MekanismFluids.Oxygen);
        } else if (slotID == 3) {
            return ChargeUtils.canBeDischarged(itemstack);
        }
        return true;
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            byte type = dataStream.readByte();
            if (type == 0) {
                dumpLeft = GasMode.values()[dumpLeft.ordinal() == GasMode.values().length - 1 ? 0 : dumpLeft.ordinal() + 1];
            } else if (type == 1) {
                dumpRight = GasMode.values()[dumpRight.ordinal() == GasMode.values().length - 1 ? 0 : dumpRight.ordinal() + 1];
            }
            return;
        }

        super.handlePacketData(dataStream);

        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            TileUtils.readTankData(dataStream, fluidTank);
            TileUtils.readTankData(dataStream, leftTank);
            TileUtils.readTankData(dataStream, rightTank);
            dumpLeft = GasMode.values()[dataStream.readInt()];
            dumpRight = GasMode.values()[dataStream.readInt()];
            clientEnergyUsed = dataStream.readDouble();
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
        TileUtils.addTankData(data, fluidTank);
        TileUtils.addTankData(data, leftTank);
        TileUtils.addTankData(data, rightTank);
        data.add(dumpLeft.ordinal());
        data.add(dumpRight.ordinal());
        data.add(clientEnergyUsed);
        data.add(numPowering);
        return data;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        if (nbtTags.hasKey("fluidTank")) {
            fluidTank.readFromNBT(nbtTags.getCompoundTag("fluidTank"));
        }
        leftTank.read(nbtTags.getCompoundTag("leftTank"));
        rightTank.read(nbtTags.getCompoundTag("rightTank"));
        dumpLeft = GasMode.values()[nbtTags.getInteger("dumpLeft")];
        dumpRight = GasMode.values()[nbtTags.getInteger("dumpRight")];
        numPowering = nbtTags.getInteger("numPowering");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        if (fluidTank.getFluid() != null) {
            nbtTags.setTag("fluidTank", fluidTank.writeToNBT(new NBTTagCompound()));
        }
        nbtTags.setTag("leftTank", leftTank.write(new NBTTagCompound()));
        nbtTags.setTag("rightTank", rightTank.write(new NBTTagCompound()));
        nbtTags.setInteger("dumpLeft", dumpLeft.ordinal());
        nbtTags.setInteger("dumpRight", dumpRight.ordinal());
        nbtTags.setInteger("numPowering", numPowering);
    }

    @Override
    public String[] getMethods() {
        return methods;
    }

    @Override
    public Object[] invoke(int method, Object[] arguments) throws NoSuchMethodException {
        return switch (method) {
            case 0 -> new Object[]{electricityStored};
            case 1 -> new Object[]{output};
            case 2 -> new Object[]{BASE_MAX_ENERGY};
            case 3 -> new Object[]{BASE_MAX_ENERGY - electricityStored.get()};
            case 4 -> new Object[]{fluidTank.getFluid() != null ? fluidTank.getFluid().amount : 0};
            case 5 ->
                    new Object[]{fluidTank.getFluid() != null ? (fluidTank.getCapacity() - fluidTank.getFluid().amount) : 0};
            case 6 -> new Object[]{leftTank.getStored()};
            case 7 -> new Object[]{leftTank.getNeeded()};
            case 8 -> new Object[]{rightTank.getStored()};
            case 9 -> new Object[]{rightTank.getNeeded()};
            default -> throw new NoSuchMethodException();
        };
    }

    @Override
    public void writeSustainedData(ItemStack itemStack) {
        if (fluidTank.getFluid() != null) {
            ItemDataUtils.setCompound(itemStack, "fluidTank", fluidTank.getFluid().writeToNBT(new NBTTagCompound()));
        }
        if (leftTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "leftTank", leftTank.getGas().write(new NBTTagCompound()));
        }
        if (rightTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "rightTank", rightTank.getGas().write(new NBTTagCompound()));
        }
    }

    @Override
    public void readSustainedData(ItemStack itemStack) {
        fluidTank.setFluid(FluidStack.loadFluidStackFromNBT(ItemDataUtils.getCompound(itemStack, "fluidTank")));
        leftTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "leftTank")));
        rightTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "rightTank")));
    }

    @Override
    public boolean canFill(EnumFacing from, @Nonnull FluidStack fluid) {
        return RecipeHandler.Recipe.ELECTROLYTIC_SEPARATOR.containsRecipe(fluid.getFluid());
    }

    @Override
    public int fill(EnumFacing from, @Nonnull FluidStack resource, boolean doFill) {
        return fluidTank.fill(resource, doFill);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[]{fluidTank.getInfo()};
    }

    @Override
    public FluidTankInfo[] getAllTanks() {
        return getTankInfo(null);
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        return 0;
    }


    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        if (side == MekanismUtils.getLeft(facing)) {
            return leftTank.draw(amount, doTransfer);
        } else if (side == MekanismUtils.getRight(facing)) {
            return rightTank.draw(amount, doTransfer);
        }
        return null;
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        return false;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        if (side == MekanismUtils.getLeft(facing)) {
            return leftTank.getGas() != null && leftTank.getGas().getGas() == type;
        } else if (side == MekanismUtils.getRight(facing)) {
            return rightTank.getGas() != null && rightTank.getGas().getGas() == type;
        }
        return false;
    }

    @Nonnull
    @Override
    public GasTankInfo[] getTankInfo() {
        return new GasTankInfo[]{leftTank, rightTank};
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return false;
        }
        return capability == Capabilities.GAS_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return null;
        } else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FluidHandlerWrapper(this, side));
        }
        return super.getCapability(capability, side);
    }

    @Override
    public List<String> getInfo(Upgrade upgrade) {
        return upgrade == Upgrade.SPEED ? upgrade.getExpScaledInfo(this) : upgrade.getMultScaledInfo(this);
    }

    @Override
    public Object[] getTanks() {
        return new Object[]{fluidTank, leftTank, rightTank};
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
    public void recalculateUpgradables(Upgrade upgrade) {
        super.recalculateUpgradables(upgrade);
        if (upgrade == Upgrade.ENERGY) {
            maxEnergy = MekanismUtils.getMaxEnergy(this, BASE_MAX_ENERGY);
            energyPerTick = BlockStateMachine.MachineType.ELECTROLYTIC_SEPARATOR.getUsage();
            setEnergy(Math.min(getMaxEnergy(), getEnergy()));
        }
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return InventoryUtils.EMPTY;
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(fluidTank.getFluidAmount(), fluidTank.getCapacity());
    }

    public double getScaledLeftTankGasLevel() {
        return (double) leftTank.getStored() / leftTank.getMaxGas();
    }

    public double getScaledRightTankGasLevel() {
        return (double) rightTank.getStored() / rightTank.getMaxGas();
    }

    public double getScaledFluidTankLevel() {
        return (double) fluidTank.getFluidAmount() / fluidTank.getCapacity();
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
    public boolean sideIsConsumer(EnumFacing side) {
        return side == MekanismUtils.getBack(facing);
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
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
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
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new FluidHandlerWrapper(this, side));
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
    public boolean isOffsetCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side, @Nonnull Vec3i offset) {
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
            } else if (facing == EnumFacing.SOUTH) {
                if (offset.equals(new Vec3i(-1, 0, 1))) {
                    return side != facing;
                }
                if (offset.equals(new Vec3i(1, 0, 1))) {
                    return side != facing;
                }
            } else if (facing == EnumFacing.WEST) {
                if (offset.equals(new Vec3i(-1, 0, 1))) {
                    return side != facing;
                }
                if (offset.equals(new Vec3i(-1, 0, -1))) {
                    return side != facing;
                }
            } else if (facing == EnumFacing.NORTH) {
                if (offset.equals(new Vec3i(-1, 0, -1))) {
                    return side != facing;
                }
                if (offset.equals(new Vec3i(1, 0, -1))) {
                    return side != facing;
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

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.EAST) {
                if (offset.equals(new Vec3i(-1, 0, 1))) {
                    return side != left && side != back;
                }
                if (offset.equals(new Vec3i(-1, 0, -1))) {
                    return side != right && side != back;
                }
            } else if (facing == EnumFacing.SOUTH) {
                if (offset.equals(new Vec3i(1, 0, -1))) {
                    return side != right && side != back;
                }
                if (offset.equals(new Vec3i(-1, 0, -1))) {
                    return side != left && side != back;
                }
            } else if (facing == EnumFacing.WEST) {
                if (offset.equals(new Vec3i(1, 0, -1))) {
                    return side != left && side != back;
                }
                if (offset.equals(new Vec3i(1, 0, 1))) {
                    return side != right && side != back;
                }
            } else if (facing == EnumFacing.NORTH) {
                if (offset.equals(new Vec3i(1, 0, 1))) {
                    return side != left && side != back;
                }
                if (offset.equals(new Vec3i(-1, 0, 1))) {
                    return side != right && side != back;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side) {
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return true;
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
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
            if (Mekanism.hooks.Bloom) {
                new BloomRenderLargeElectrolyticSeparator(this);
            }
        }
    }



}
