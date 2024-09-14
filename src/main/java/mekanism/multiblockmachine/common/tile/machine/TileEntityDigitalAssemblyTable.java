package mekanism.multiblockmachine.common.tile.machine;

import io.netty.buffer.ByteBuf;
import mekanism.api.Coord4D;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.*;
import mekanism.common.Mekanism;
import mekanism.common.MekanismItems;
import mekanism.common.Upgrade;
import mekanism.common.base.*;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.inputs.CompositeInput;
import mekanism.common.recipe.machines.DigitalAssemblyTableRecipe;
import mekanism.common.recipe.outputs.CompositeOutput;
import mekanism.common.tier.FluidTankTier;
import mekanism.common.tier.GasTankTier;
import mekanism.common.util.*;
import mekanism.multiblockmachine.client.render.bloom.machine.BloomRenderDigitalAssemblyTable;
import mekanism.multiblockmachine.common.MultiblockMachineItems;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine;
import mekanism.multiblockmachine.common.tile.machine.prefab.TileEntityMultiblockBasicMachine;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class TileEntityDigitalAssemblyTable extends TileEntityMultiblockBasicMachine<CompositeInput, CompositeOutput, DigitalAssemblyTableRecipe>
        implements IGasHandler, IFluidHandlerWrapper, ITankManager, ISustainedData, IAdvancedBoundingBlock {

    private static Random Rand = new Random();
    public FluidTank inputFluidTank = new FluidTankSync(FluidTankTier.ULTIMATE.getStorage());
    public FluidTank outputFluidTank = new FluidTankSync(FluidTankTier.ULTIMATE.getStorage());
    public GasTank inputGasTank = new GasTank(GasTankTier.ULTIMATE.getStorage());
    public GasTank outputGasTank = new GasTank(GasTankTier.ULTIMATE.getStorage());
    public DigitalAssemblyTableRecipe cachedRecipe;
    public int updateDelay;
    public boolean needsPacket;
    public int numPowering;
    public int DoorHeight;
    public float inputGasScale;
    public float outputGasScale;
    public float inputFluidScale;
    public float outputFluidScale;
    public int lastInputGas;
    public int lastInputFluid;
    public int lastOutputGas;
    public int lastOutputFluid;
    public int lastoperatingTicks;
    private int currentRedstoneLevel;
    private boolean rendererInitialized = false;

    public TileEntityDigitalAssemblyTable() {
        super("digitalassemblytable", BlockStateMultiblockMachine.MultiblockMachineType.DIGITAL_ASSEMBLY_TABLE, 200, 0);
        inventory = NonNullListSynchronized.withSize(15, ItemStack.EMPTY);
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
            DigitalAssemblyTableRecipe recipe = getRecipe();
            ChargeUtils.discharge(1, this);
            if (canOperate(recipe) && MekanismUtils.canFunction(this) && getEnergy() >= MekanismUtils.getEnergyPerTick(this, BASE_ENERGY_PER_TICK + recipe.extraEnergy) && isMachiningTools()) {
                boolean update = BASE_TICKS_REQUIRED != recipe.ticks;
                BASE_TICKS_REQUIRED = recipe.ticks;
                if (update) {
                    recalculateUpgradables(Upgrade.SPEED);
                }
                setActive(true);
                for (int i = 11; i < 14; i++) {
                    if (inventory.get(i).attemptDamageItem(1, Rand, null)) {
                        inventory.set(i, ItemStack.EMPTY);
                    }
                }
                if ((operatingTicks + 1) < ticksRequired) {
                    operatingTicks++;
                    electricityStored.addAndGet(-MekanismUtils.getEnergyPerTick(this, BASE_ENERGY_PER_TICK + recipe.extraEnergy));
                } else if ((operatingTicks + 1) >= ticksRequired && getEnergy() >= MekanismUtils.getEnergyPerTick(this, BASE_ENERGY_PER_TICK + recipe.extraEnergy)) {
                    operate(recipe);
                    operatingTicks = 0;
                    electricityStored.addAndGet(-MekanismUtils.getEnergyPerTick(this, BASE_ENERGY_PER_TICK + recipe.extraEnergy));
                }
            } else {
                BASE_TICKS_REQUIRED = 100;
                if (prevEnergy >= getEnergy()) {
                    setActive(false);
                }
            }
            if (!canOperate(recipe)) {
                operatingTicks = 0;
            }
            if (prevEnergy != getEnergy() || lastInputFluid != inputFluidTank.getFluidAmount() || lastInputGas != inputGasTank.getStored() || lastOutputGas != outputGasTank.getStored() || lastOutputFluid != outputFluidTank.getFluidAmount() || lastoperatingTicks != operatingTicks) {
                SPacketUpdateTileEntity packet = this.getUpdatePacket();
                PlayerChunkMapEntry trackingEntry = ((WorldServer) this.world).getPlayerChunkMap().getEntry(this.pos.getX() >> 4, this.pos.getZ() >> 4);
                if (trackingEntry != null) {
                    for (EntityPlayerMP player : trackingEntry.getWatchingPlayers()) {
                        player.connection.sendPacket(packet);
                    }
                }
            }
            prevEnergy = getEnergy();
            lastInputGas = inputGasTank.getStored();
            lastInputFluid = inputFluidTank.getFluidAmount();
            lastOutputGas = outputGasTank.getStored();
            lastOutputFluid = outputFluidTank.getFluidAmount();
            lastoperatingTicks = operatingTicks;
            if (needsPacket) {
                Mekanism.packetHandler.sendUpdatePacket(this);
            }
            needsPacket = false;

            Mekanism.EXECUTE_MANAGER.addSyncTask(() -> {
                handleGasTank(outputGasTank, getGasTankside());
                handleFluidTank(outputFluidTank, getFluidTankside());
                int newRedstoneLevel = getRedstoneLevel();
                if (newRedstoneLevel != currentRedstoneLevel) {
                    world.updateComparatorOutputLevel(pos, getBlockType());
                    currentRedstoneLevel = newRedstoneLevel;
                }
            });
        } else {
            if (!isActive) {
                if (DoorHeight < 16) {
                    DoorHeight++;
                }
            } else {
                if (DoorHeight > 0) {
                    DoorHeight--;
                }
            }

            float targetInputGasScale = (float) (inputGasTank.getGas() != null ? inputGasTank.getGas().amount : 0) / inputGasTank.getMaxGas();
            if (Math.abs(inputGasScale - targetInputGasScale) > 0.01) {
                inputGasScale = (9 * inputGasScale + targetInputGasScale) / 10;
            }
            float targetOutputGasScale = (float) (outputGasTank.getGas() != null ? outputGasTank.getGas().amount : 0) / outputGasTank.getMaxGas();
            if (Math.abs(outputGasScale - targetOutputGasScale) > 0.01) {
                outputGasScale = (9 * outputGasScale + targetOutputGasScale) / 10;
            }

            float targetInputFluidScale = (float) (inputFluidTank.getFluid() != null ? inputFluidTank.getFluid().amount : 0) / inputFluidTank.getCapacity();
            if (Math.abs(inputFluidScale - targetInputFluidScale) > 0.01) {
                inputFluidScale = (9 * inputFluidScale + targetInputFluidScale) / 10;
            }

            float targetOutputFluidScale = (float) (outputFluidTank.getFluid() != null ? outputFluidTank.getFluid().amount : 0) / outputFluidTank.getCapacity();
            if (Math.abs(outputFluidScale - targetOutputFluidScale) > 0.01) {
                outputFluidScale = (9 * outputFluidScale + targetOutputFluidScale) / 10;
            }


            if (updateDelay > 0) {
                updateDelay--;
                if (updateDelay == 0) {
                    MekanismUtils.updateBlock(world, getPos());
                }
            }
        }
    }

    private boolean isMachiningTools() {
        return inventory.get(11).getItem() == MultiblockMachineItems.gas_adsorption_fractionation_module && inventory.get(12).getItem() == MultiblockMachineItems.high_frequency_fusion_molding_module && inventory.get(13).getItem() == MultiblockMachineItems.LaserLenses;
    }

    private TileEntity getGasTankside() {
        BlockPos pos = getPos().offset(MekanismUtils.getLeft(facing), 5).up(3).offset(MekanismUtils.getBack(facing));
        if (world.getTileEntity(pos) != null) {
            return world.getTileEntity(pos);
        }
        return null;

    }

    private TileEntity getFluidTankside() {
        BlockPos pos = getPos().offset(MekanismUtils.getLeft(facing), 5).up().offset(MekanismUtils.getBack(facing), 3);
        if (world.getTileEntity(pos) != null) {
            return world.getTileEntity(pos);
        }
        return null;

    }

    private void handleGasTank(GasTank tank, TileEntity tile) {
        if (tank.getGas() != null && tank.getGas().getGas() != null) {
            GasStack toSend = tank.getGas().copy().withAmount(Math.min(tank.getStored(), tank.getMaxGas()));
            tank.draw(GasUtils.emit(toSend, tile, Collections.singleton(EnumFacing.UP)), true);
        }
    }

    private void handleFluidTank(FluidTank tank, TileEntity tile) {
        if (tank.getFluid() != null && tile != null) {
            FluidStack toSend = new FluidStack(tank.getFluid(), Math.min(tank.getCapacity(), tank.getFluidAmount()));
            tank.drain(PipeUtils.emit(Collections.singleton(EnumFacing.DOWN), toSend, tile), true);
        }
    }

    @Override
    public boolean canOperate(DigitalAssemblyTableRecipe recipe) {
        return recipe != null && recipe.canOperate(inventory, 2, 3, 4, 5, 6, 7, 8, 9, 10, inputFluidTank, inputGasTank, 14, outputFluidTank, outputGasTank);
    }

    @Override
    public void operate(DigitalAssemblyTableRecipe recipe) {
        recipe.operate(inventory, 2, 3, 4, 5, 6, 7, 8, 9, 10, inputFluidTank, inputGasTank, 14, outputFluidTank, outputGasTank);
        markNoUpdateSync();
    }

    @Override
    public Map<CompositeInput, DigitalAssemblyTableRecipe> getRecipes() {
        return RecipeHandler.Recipe.DIGITAL_ASSEMBLY_TABLE.get();
    }

    @Override
    public DigitalAssemblyTableRecipe getRecipe() {
        CompositeInput input = getInput();
        if (cachedRecipe == null || !input.testEquality(cachedRecipe.getInput())) {
            cachedRecipe = RecipeHandler.getDigitalAssemblyTableRecipe(input);
        }
        return cachedRecipe;
    }

    @Override
    public CompositeInput getInput() {
        return new CompositeInput(
                inventory.get(2), inventory.get(3), inventory.get(4), inventory.get(5), inventory.get(6), inventory.get(7), inventory.get(8), inventory.get(9), inventory.get(10),
                inputFluidTank.getFluid(), inputGasTank.getGas());
    }

    @Override
    public boolean isItemValidForSlot(int slotID, @Nonnull ItemStack itemstack) {
        if (slotID == 14) {
            return false;
        } else if (slotID == 0) {
            return itemstack.getItem() == MekanismItems.SpeedUpgrade || itemstack.getItem() == MekanismItems.EnergyUpgrade;
        } else if (slotID == 1) {
            return ChargeUtils.canBeDischarged(itemstack);
        } else if (slotID == 2 || slotID == 3 || slotID == 4 || slotID == 5 || slotID == 6 || slotID == 7 || slotID == 8 || slotID == 9 || slotID == 10 || slotID == 11 || slotID == 12 || slotID == 13) {
            return InputItemValidForSlot(slotID, itemstack);
        }
        return false;
    }

    private boolean InputItemValidForSlot(int slotID, ItemStack itemstack) {
        for (CompositeInput input : getRecipes().keySet()) {
            if (slotID == 2) {
                if (ItemHandlerHelper.canItemStacksStack(input.itemInput, itemstack)) {
                    return true;
                }
            } else if (slotID == 3) {
                if (ItemHandlerHelper.canItemStacksStack(input.itemInput2, itemstack)) {
                    return true;
                }
            } else if (slotID == 4) {
                if (ItemHandlerHelper.canItemStacksStack(input.itemInput3, itemstack)) {
                    return true;
                }
            } else if (slotID == 5) {
                if (ItemHandlerHelper.canItemStacksStack(input.itemInput4, itemstack)) {
                    return true;
                }
            } else if (slotID == 6) {
                if (ItemHandlerHelper.canItemStacksStack(input.itemInput5, itemstack)) {
                    return true;
                }
            } else if (slotID == 7) {
                if (ItemHandlerHelper.canItemStacksStack(input.itemInput6, itemstack)) {
                    return true;
                }
            } else if (slotID == 8) {
                if (ItemHandlerHelper.canItemStacksStack(input.itemInput7, itemstack)) {
                    return true;
                }
            } else if (slotID == 9) {
                if (ItemHandlerHelper.canItemStacksStack(input.itemInput8, itemstack)) {
                    return true;
                }
            } else if (slotID == 10) {
                if (ItemHandlerHelper.canItemStacksStack(input.itemInput9, itemstack)) {
                    return true;
                }
            } else if (slotID == 11) {
                if (ItemHandlerHelper.canItemStacksStack(MultiblockMachineItems.gas_adsorption_fractionation_module.getDefaultInstance(), itemstack)) {
                    return true;
                }
            } else if (slotID == 12) {
                if (ItemHandlerHelper.canItemStacksStack(MultiblockMachineItems.high_frequency_fusion_molding_module.getDefaultInstance(), itemstack)) {
                    return true;
                }
            } else if (slotID == 13) {
                if (ItemHandlerHelper.canItemStacksStack(MultiblockMachineItems.LaserLenses.getDefaultInstance(), itemstack)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        if (slotID == 1) {
            return ChargeUtils.canBeOutputted(itemstack, false);
        }
        return slotID == 14;
    }

    @Override
    public String[] getMethods() {
        return new String[0];
    }

    @Override
    public Object[] invoke(int method, Object[] args) throws NoSuchMethodException {
        return new Object[0];
    }

    @NotNull
    @Override
    public int[] getSlotsForFace(@NotNull EnumFacing side) {
        return InventoryUtils.EMPTY;
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            TileUtils.readTankData(dataStream, inputFluidTank);
            TileUtils.readTankData(dataStream, inputGasTank);
            TileUtils.readTankData(dataStream, outputFluidTank);
            TileUtils.readTankData(dataStream, outputGasTank);
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
        TileUtils.addTankData(data, inputFluidTank);
        TileUtils.addTankData(data, inputGasTank);
        TileUtils.addTankData(data, outputFluidTank);
        TileUtils.addTankData(data, outputGasTank);
        data.add(numPowering);
        return data;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        inputFluidTank.readFromNBT(nbtTags.getCompoundTag("inputFluidTank"));
        inputGasTank.read(nbtTags.getCompoundTag("inputGasTank"));
        outputFluidTank.readFromNBT(nbtTags.getCompoundTag("outputFluidTank"));
        outputGasTank.read(nbtTags.getCompoundTag("outputGasTank"));
        numPowering = nbtTags.getInteger("numPowering");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setTag("inputFluidTank", inputFluidTank.writeToNBT(new NBTTagCompound()));
        nbtTags.setTag("inputGasTank", inputGasTank.write(new NBTTagCompound()));
        nbtTags.setTag("outputFluidTank", outputFluidTank.writeToNBT(new NBTTagCompound()));
        nbtTags.setTag("outputGasTank", outputGasTank.write(new NBTTagCompound()));
        nbtTags.setInteger("numPowering", numPowering);
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        if (canReceiveGas(side, stack != null ? stack.getGas() : null)) {
            return inputGasTank.receive(stack, doTransfer);
        }
        return 0;
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        if (canDrawGas(side, null)) {
            return outputGasTank.draw(amount, doTransfer);
        }
        return null;
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        return inputGasTank.canReceive(type) && RecipeHandler.Recipe.DIGITAL_ASSEMBLY_TABLE.containsRecipe(type) && side == EnumFacing.DOWN;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        return outputGasTank.canDraw(type) && side == EnumFacing.UP;
    }

    @NotNull
    @Override
    public GasTankInfo[] getTankInfo() {
        return new GasTankInfo[]{inputGasTank, outputGasTank};
    }

    @Override
    public int fill(EnumFacing from, @NotNull FluidStack resource, boolean doFill) {
        return inputFluidTank.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return outputFluidTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, @NotNull FluidStack fluid) {
        return RecipeHandler.Recipe.DIGITAL_ASSEMBLY_TABLE.containsRecipe(fluid.getFluid()) && from == EnumFacing.UP;
    }

    @Override
    public boolean canDrain(EnumFacing from, @Nullable FluidStack fluid) {
        return outputFluidTank.getFluidAmount() > 0 && FluidContainerUtils.canDrain(outputFluidTank.getFluid(), fluid) && from == EnumFacing.DOWN;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[]{inputFluidTank.getInfo(), outputFluidTank.getInfo()};
    }

    @Override
    public FluidTankInfo[] getAllTanks() {
        return new FluidTankInfo[]{inputFluidTank.getInfo(), outputFluidTank.getInfo()};
    }

    @Override
    public Object[] getTanks() {
        return new Object[]{inputFluidTank, inputGasTank, outputFluidTank, outputGasTank};
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        if (isCapabilityDisabled(capability, side)) {
            return false;
        }
        return capability == Capabilities.GAS_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, side);
    }

    @Override
    public void writeSustainedData(ItemStack itemStack) {
        if (inputFluidTank.getFluid() != null) {
            ItemDataUtils.setCompound(itemStack, "inputFluidTank", inputFluidTank.getFluid().writeToNBT(new NBTTagCompound()));
        }
        if (inputGasTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "inputGasTank", inputGasTank.getGas().write(new NBTTagCompound()));
        }
        if (outputFluidTank.getFluid() != null) {
            ItemDataUtils.setCompound(itemStack, "outputFluidTank", outputFluidTank.getFluid().writeToNBT(new NBTTagCompound()));
        }
        if (outputGasTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "outputGasTank", outputGasTank.getGas().write(new NBTTagCompound()));
        }
    }

    @Override
    public void readSustainedData(ItemStack itemStack) {
        inputFluidTank.setFluid(FluidStack.loadFluidStackFromNBT(ItemDataUtils.getCompound(itemStack, "inputFluidTank")));
        inputGasTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "inputGasTank")));
        outputFluidTank.setFluid(FluidStack.loadFluidStackFromNBT(ItemDataUtils.getCompound(itemStack, "outputFluidTank")));
        outputGasTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "outputGasTank")));
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
    public boolean canBoundReceiveEnergy(BlockPos coord, EnumFacing side) {
        EnumFacing back = MekanismUtils.getBack(facing);
        EnumFacing left = MekanismUtils.getLeft(facing);
        EnumFacing right = MekanismUtils.getRight(facing);
        if (coord.equals(getPos().offset(back, 5).offset(left, 4).up()) || coord.equals(getPos().offset(back, 5).offset(right, 4).up())) {
            return side == EnumFacing.DOWN;
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
        for (int y = 0; y <= 4; y++) {
            for (int x = -4; x <= 4; x++) {
                for (int z = -4; z <= 4; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    BlockPos pos1 = getPos().add(x, y, z);
                    if (y == 1) {
                        if (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH) {
                            if ((z == 4 || z == -4) && (x == 1 || x == 0 || x == -1)) {
                                MekanismUtils.makeAdvancedBoundingBlock(world, pos1, Coord4D.get(this));
                            } else {
                                MekanismUtils.makeBoundingBlock(world, pos1, Coord4D.get(this));
                            }
                        } else if (facing == EnumFacing.WEST || facing == EnumFacing.EAST) {
                            if ((x == 4 || x == -4) && (z == 1 || z == 0 || z == -1)) {
                                MekanismUtils.makeAdvancedBoundingBlock(world, pos1, Coord4D.get(this));
                            } else {
                                MekanismUtils.makeBoundingBlock(world, pos1, Coord4D.get(this));
                            }
                        }
                    } else {
                        MekanismUtils.makeBoundingBlock(world, pos1, Coord4D.get(this));
                    }
                    world.notifyNeighborsOfStateChange(pos1, getBlockType(), true);
                }
            }
        }
        for (int y = 1; y <= 3; y++) {
            if (facing == EnumFacing.SOUTH) {
                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(5, y, -1), Coord4D.get(this));//GAS
                world.notifyNeighborsOfStateChange(getPos().add(5, y, -1), getBlockType(), true);//GAS

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(-5, y, -1), Coord4D.get(this));//GAS
                world.notifyNeighborsOfStateChange(getPos().add(-5, y, -1), getBlockType(), true);//GAS

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(5, y, -3), Coord4D.get(this));//fluid
                world.notifyNeighborsOfStateChange(getPos().add(5, y, -3), getBlockType(), true);//fluid

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(-5, y, -3), Coord4D.get(this));//fluid
                world.notifyNeighborsOfStateChange(getPos().add(-5, y, -3), getBlockType(), true);//fluid

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(3, y, -5), Coord4D.get(this));//energy
                world.notifyNeighborsOfStateChange(getPos().add(3, y, -5), getBlockType(), true);//energy

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(-3, y, -5), Coord4D.get(this));//energy
                world.notifyNeighborsOfStateChange(getPos().add(-3, y, -5), getBlockType(), true);//energy

            } else if (facing == EnumFacing.WEST) {
                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(1, y, 5), Coord4D.get(this));//GAS
                world.notifyNeighborsOfStateChange(getPos().add(1, y, 5), getBlockType(), true);//GAS

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(1, y, -5), Coord4D.get(this));//GAS
                world.notifyNeighborsOfStateChange(getPos().add(1, y, -5), getBlockType(), true);//GAS

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(3, y, 5), Coord4D.get(this));//fluid
                world.notifyNeighborsOfStateChange(getPos().add(3, y, 5), getBlockType(), true);//fluid

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(3, y, -5), Coord4D.get(this));//fluid
                world.notifyNeighborsOfStateChange(getPos().add(3, y, -5), getBlockType(), true);//fluid

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(5, y, 3), Coord4D.get(this));//energy
                world.notifyNeighborsOfStateChange(getPos().add(5, y, 3), getBlockType(), true);//energy

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(5, y, -3), Coord4D.get(this));//energy
                world.notifyNeighborsOfStateChange(getPos().add(5, y, -3), getBlockType(), true);//energy

            } else if (facing == EnumFacing.EAST) {
                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(-1, y, 5), Coord4D.get(this));//GAS
                world.notifyNeighborsOfStateChange(getPos().add(-1, y, 5), getBlockType(), true);//GAS

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(-1, y, -5), Coord4D.get(this));//GAS
                world.notifyNeighborsOfStateChange(getPos().add(-1, y, -5), getBlockType(), true);//GAS

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(-3, y, 5), Coord4D.get(this));//fluid
                world.notifyNeighborsOfStateChange(getPos().add(-3, y, 5), getBlockType(), true);//fluid

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(-3, y, -5), Coord4D.get(this));//fluid
                world.notifyNeighborsOfStateChange(getPos().add(-3, y, -5), getBlockType(), true);//fluid

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(-5, y, 3), Coord4D.get(this));//energy
                world.notifyNeighborsOfStateChange(getPos().add(-5, y, 3), getBlockType(), true);//energy

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(-5, y, -3), Coord4D.get(this));//energy
                world.notifyNeighborsOfStateChange(getPos().add(-5, y, -3), getBlockType(), true);//energy

            } else if (facing == EnumFacing.NORTH) {
                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(5, y, 1), Coord4D.get(this));//GAS
                world.notifyNeighborsOfStateChange(getPos().add(5, y, 1), getBlockType(), true);//GAS

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(-5, y, 1), Coord4D.get(this));//GAS
                world.notifyNeighborsOfStateChange(getPos().add(-5, y, 1), getBlockType(), true);//GAS

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(5, y, 3), Coord4D.get(this));//fluid
                world.notifyNeighborsOfStateChange(getPos().add(5, y, 3), getBlockType(), true);//fluid

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(-5, y, 3), Coord4D.get(this));//fluid
                world.notifyNeighborsOfStateChange(getPos().add(-5, y, 3), getBlockType(), true);//fluid

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(3, y, 5), Coord4D.get(this));//energy
                world.notifyNeighborsOfStateChange(getPos().add(3, y, 5), getBlockType(), true);//energy

                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(-3, y, 5), Coord4D.get(this));//energy
                world.notifyNeighborsOfStateChange(getPos().add(-3, y, 5), getBlockType(), true);//energy
            }
        }
    }

    @Override
    public void onBreak() {
        for (int y = 0; y <= 4; y++) {
            for (int x = -4; x <= 4; x++) {
                for (int z = -4; z <= 4; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    world.setBlockToAir(getPos().add(x, y, z));
                }
            }

        }
        for (int y = 1; y <= 3; y++) {
            if (facing == EnumFacing.SOUTH) {
                world.setBlockToAir(getPos().add(5, y, -1));//gas in ok
                world.setBlockToAir(getPos().add(-5, y, -1));//gas out
                world.setBlockToAir(getPos().add(5, y, -3));//fluid in ok
                world.setBlockToAir(getPos().add(-5, y, -3));//fluid out
                world.setBlockToAir(getPos().add(3, y, -5));//energy
                world.setBlockToAir(getPos().add(-3, y, -5));//energy
            } else if (facing == EnumFacing.WEST) {
                world.setBlockToAir(getPos().add(1, y, 5));//gas in ok
                world.setBlockToAir(getPos().add(1, y, -5));//gas out ok
                world.setBlockToAir(getPos().add(3, y, 5));//fluid in
                world.setBlockToAir(getPos().add(3, y, -5));//fluid out
                world.setBlockToAir(getPos().add(5, y, 3));//energy
                world.setBlockToAir(getPos().add(5, y, -3));//energy
            } else if (facing == EnumFacing.EAST) {
                world.setBlockToAir(getPos().add(-1, y, -5));//gas in ok
                world.setBlockToAir(getPos().add(-1, y, 5));//gas out ok
                world.setBlockToAir(getPos().add(-3, y, -5));//fluid in
                world.setBlockToAir(getPos().add(-3, y, 5));//fluid out
                world.setBlockToAir(getPos().add(-5, y, 3));//energy
                world.setBlockToAir(getPos().add(-5, y, -3));//energy
            } else if (facing == EnumFacing.NORTH) {
                world.setBlockToAir(getPos().add(-5, y, 1));//gas in ok
                world.setBlockToAir(getPos().add(5, y, 1));//gas out ok
                world.setBlockToAir(getPos().add(-5, y, 3));//fluid in
                world.setBlockToAir(getPos().add(5, y, 3));//fluid out
                world.setBlockToAir(getPos().add(3, y, 5));//energy
                world.setBlockToAir(getPos().add(-3, y, 5));//energy
            }
        }
        world.setBlockToAir(getPos().add(0, 0, 0));
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
    public boolean isOffsetCapabilityDisabled(@NotNull Capability<?> capability, @Nullable EnumFacing side, @NotNull Vec3i offset) {
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.SOUTH) {
                if (offset.equals(new Vec3i(5, 1, -1))) {//gas in
                    return side != EnumFacing.DOWN;
                }
                if (offset.equals(new Vec3i(-5, 3, -1))) {
                    return side != EnumFacing.UP;
                }
            } else if (facing == EnumFacing.NORTH) {
                if (offset.equals(new Vec3i(-5, 1, 1))) {//gas in
                    return side != EnumFacing.DOWN;
                }
                if (offset.equals(new Vec3i(5, 3, 1))) {
                    return side != EnumFacing.UP;
                }
            } else if (facing == EnumFacing.WEST) {
                if (offset.equals(new Vec3i(1, 1, 5))) {//gas in
                    return side != EnumFacing.DOWN;
                }
                if (offset.equals(new Vec3i(1, 3, -5))) {
                    return side != EnumFacing.UP;
                }
            } else if (facing == EnumFacing.EAST) {
                if (offset.equals(new Vec3i(-1, 1, -5))) {//gas in
                    return side != EnumFacing.DOWN;
                }
                if (offset.equals(new Vec3i(-1, 3, 5))) {
                    return side != EnumFacing.UP;
                }
            }
            return true;
        }


        if (isStrictEnergy(capability) || capability == CapabilityEnergy.ENERGY || isTesla(capability, side)) {
            if (facing == EnumFacing.SOUTH) {
                if (offset.equals(new Vec3i(3, 1, -5))) {
                    return side != EnumFacing.DOWN;
                }
                if (offset.equals(new Vec3i(-3, 1, -5))) {
                    return side != EnumFacing.DOWN;
                }
            } else if (facing == EnumFacing.NORTH) {
                if (offset.equals(new Vec3i(3, 1, 5))) {
                    return side != EnumFacing.DOWN;
                }
                if (offset.equals(new Vec3i(-3, 1, 5))) {
                    return side != EnumFacing.DOWN;
                }
            } else if (facing == EnumFacing.WEST) {
                if (offset.equals(new Vec3i(5, 1, 3))) {
                    return side != EnumFacing.DOWN;
                }
                if (offset.equals(new Vec3i(5, 1, -3))) {
                    return side != EnumFacing.DOWN;
                }
            } else if (facing == EnumFacing.EAST) {
                if (offset.equals(new Vec3i(-5, 1, 3))) {
                    return side != EnumFacing.DOWN;
                }
                if (offset.equals(new Vec3i(-5, 1, -3))) {
                    return side != EnumFacing.DOWN;
                }
            }
            return true;
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.SOUTH) {
                if (offset.equals(new Vec3i(5, 3, -3))) {//fluid in
                    return side != EnumFacing.UP;
                }
                if (offset.equals(new Vec3i(-5, 1, -3))) {
                    return side != EnumFacing.DOWN;
                }
            } else if (facing == EnumFacing.NORTH) {
                if (offset.equals(new Vec3i(-5, 3, 3))) {//fluid in
                    return side != EnumFacing.UP;
                }
                if (offset.equals(new Vec3i(5, 1, 3))) {
                    return side != EnumFacing.DOWN;
                }
            } else if (facing == EnumFacing.WEST) {
                if (offset.equals(new Vec3i(3, 3, 5))) {//fluid in
                    return side != EnumFacing.UP;
                }
                if (offset.equals(new Vec3i(3, 1, -5))) {
                    return side != EnumFacing.DOWN;
                }
            } else if (facing == EnumFacing.EAST) {
                if (offset.equals(new Vec3i(-3, 3, -5))) {//fluid in
                    return side != EnumFacing.UP;
                }
                if (offset.equals(new Vec3i(-3, 1, 5))) {
                    return side != EnumFacing.DOWN;
                }
            }
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 16384.0D;
    }

    @Override
    public void validate() {
        super.validate();
        if (world.isRemote && !rendererInitialized) {
            rendererInitialized = true;
            if (Mekanism.hooks.Bloom && MekanismConfig.current().client.enableBloom.val()) {
                new BloomRenderDigitalAssemblyTable(this);
            }
        }
    }

}
