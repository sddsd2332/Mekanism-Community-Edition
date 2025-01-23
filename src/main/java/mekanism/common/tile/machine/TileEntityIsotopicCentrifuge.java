package mekanism.common.tile.machine;

import io.netty.buffer.ByteBuf;
import mekanism.api.Coord4D;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.*;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.Mekanism;
import mekanism.common.SideData;
import mekanism.common.Upgrade;
import mekanism.common.Upgrade.IUpgradeInfoHandler;
import mekanism.common.base.IBoundingBlock;
import mekanism.common.base.ISustainedData;
import mekanism.common.base.ITankManager;
import mekanism.common.block.states.BlockStateMachine;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.inputs.GasInput;
import mekanism.common.recipe.machines.IsotopicRecipe;
import mekanism.common.recipe.outputs.GasOutput;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.prefab.TileEntityBasicMachine;
import mekanism.common.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class TileEntityIsotopicCentrifuge extends TileEntityBasicMachine<GasInput, GasOutput, IsotopicRecipe> implements ISustainedData, IBoundingBlock, IGasHandler, IUpgradeInfoHandler, ITankManager {
    public static final int MAX_GAS = 10000;
    public GasTank inputTank = new GasTank(MAX_GAS);
    public GasTank outputTank = new GasTank(MAX_GAS);
    public IsotopicRecipe cachedRecipe;
    public double clientEnergyUsed;
    private int currentRedstoneLevel;
    public float prevScale;
    public int updateDelay;
    public boolean needsPacket;

    public TileEntityIsotopicCentrifuge() {
        super("washer", BlockStateMachine.MachineType.ISOTOPIC_CENTRIFUGE, 3, 1);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY, TransmissionType.GAS);

        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.NONE, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.INPUT, new int[]{0}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.OUTPUT, new int[]{1}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.ENERGY, new int[]{2}));
        configComponent.setConfig(TransmissionType.ITEM, new byte[]{1, -1, 2, 3, 1, 1});
        configComponent.setCanEject(TransmissionType.ITEM, false);

        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.NONE, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.INPUT, new int[]{0}));
        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.OUTPUT, new int[]{1}));
        configComponent.addOutput(TransmissionType.GAS, new SideData(new int[]{0, 1},new boolean[]{false,true}));
        configComponent.setConfig(TransmissionType.GAS, new byte[]{1, -1, 2, 1, 1, 1});

        configComponent.addOutput(TransmissionType.ENERGY, new SideData(DataType.NONE, SideData.IOState.OFF));
        configComponent.addOutput(TransmissionType.ENERGY, new SideData(DataType.INPUT, SideData.IOState.INPUT));
        configComponent.setConfig(TransmissionType.ENERGY, new byte[]{1, -1, 1, 1, 1, 1});
        configComponent.setCanEject(TransmissionType.ENERGY, false);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(TransmissionType.GAS, configComponent.getOutputs(TransmissionType.GAS).get(2));
        ejectorComponent.setInputOutputData(TransmissionType.GAS, configComponent.getOutputs(TransmissionType.GAS).get(3));
        inventory = NonNullListSynchronized.withSize(4, ItemStack.EMPTY);
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
            ChargeUtils.discharge(2, this);
            if (!inventory.get(0).isEmpty() && inventory.get(0).getItem() instanceof IGasItem  gasItem&& gasItem.getGas(inventory.get(0)) != null && RecipeHandler.Recipe.ISOTOPIC_CENTRIFUGE.containsRecipe(gasItem.getGas(inventory.get(0)).getGas())) {
                TileUtils.receiveGasItem(inventory.get(0), inputTank);
            }
            TileUtils.drawGas(inventory.get(1), outputTank);
            IsotopicRecipe recipe = getRecipe();
            if (canOperate(recipe) && getEnergy() >= energyPerTick && MekanismUtils.canFunction(this)) {
                setActive(true);
                operatingTicks++;
                if (operatingTicks >= ticksRequired) {
                    operate(recipe);
                    operatingTicks = 0;
                }
                double prev = getEnergy();
                setEnergy(getEnergy() - energyPerTick * getUpgradedUsage(recipe));
                clientEnergyUsed = prev - getEnergy();
            } else if (prevEnergy >= getEnergy()) {
                setActive(false);
            }
            prevEnergy = getEnergy();
            int newRedstoneLevel = getRedstoneLevel();
            if (newRedstoneLevel != currentRedstoneLevel) {
                updateComparatorOutputLevelSync();
                currentRedstoneLevel = newRedstoneLevel;
            }
            if (needsPacket) {
                Mekanism.packetHandler.sendUpdatePacket(this);
            }
            needsPacket = false;
        }else {
            if (updateDelay > 0) {
                updateDelay--;
                if (updateDelay == 0) {
                    MekanismUtils.updateBlock(world, getPos());
                }
            }
            float targetScale = (float) (outputTank.getGas() != null ? outputTank.getGas().amount : 0) / outputTank.getMaxGas();
            if (Math.abs(prevScale - targetScale) > 0.01) {
                prevScale = (9 * prevScale + targetScale) / 10;
            }
        }
    }

    @Override
    public IsotopicRecipe getRecipe() {
        GasInput input = getInput();
        if (cachedRecipe == null || !input.testEquality(cachedRecipe.getInput())) {
            cachedRecipe = RecipeHandler.getIsotopicRecipe(getInput());
        }
        return cachedRecipe;
    }

    @Override
    public GasInput getInput() {
        return new GasInput(inputTank.getGas());
    }

    @Override
    public boolean canOperate(IsotopicRecipe recipe) {
        return recipe != null && recipe.canOperate(inputTank, outputTank);
    }

    @Override
    public void operate(IsotopicRecipe recipe) {
        recipe.operate(inputTank, outputTank, getUpgradedUsage(recipe));
    }


    @Override
    public Map<GasInput, IsotopicRecipe> getRecipes() {
        return RecipeHandler.Recipe.ISOTOPIC_CENTRIFUGE.get();
    }

    public int getUpgradedUsage(IsotopicRecipe recipe) {
        int possibleProcess = Math.min((int) Math.pow(2, upgradeComponent.getUpgrades(Upgrade.SPEED)), MekanismConfig.current().mekce.MAXspeedmachines.val());
        possibleProcess = Math.min(Math.min(inputTank.getStored(), outputTank.getNeeded()), possibleProcess);
        possibleProcess = Math.min((int) (getEnergy() / energyPerTick), possibleProcess);
        return Math.min(inputTank.getStored() / recipe.recipeInput.ingredient.amount, possibleProcess);
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            clientEnergyUsed = dataStream.readDouble();
            TileUtils.readTankData(dataStream, inputTank);
            TileUtils.readTankData(dataStream, outputTank);
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
        TileUtils.addTankData(data, inputTank);
        TileUtils.addTankData(data, outputTank);
        return data;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        inputTank.read(nbtTags.getCompoundTag("inputTank"));
        outputTank.read(nbtTags.getCompoundTag("outputTank"));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setTag("inputTank", inputTank.write(new NBTTagCompound()));
        nbtTags.setTag("outputTank", outputTank.write(new NBTTagCompound()));
    }


    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        return configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(0)
                && inputTank.canReceive(type) && RecipeHandler.Recipe.ISOTOPIC_CENTRIFUGE.containsRecipe(type);
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        if (canReceiveGas(side, stack.getGas())) {
            int recipeAmount = RecipeHandler.Recipe.ISOTOPIC_CENTRIFUGE.get().get(new GasInput(stack)).recipeInput.ingredient.amount;
            int receivable = inputTank.receive(stack, false);
            int stored = inputTank.stored != null ? inputTank.stored.amount : 0;
            int newStored = stored + receivable;
            int amount = newStored - stored - newStored % recipeAmount;
            return inputTank.receive(stack.copy().withAmount(amount), doTransfer);
        }
        return 0;
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        if (canDrawGas(side, null)) {
            return outputTank.draw(amount, doTransfer);
        }
        return null;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        return configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(1) && outputTank.canDraw(type);
    }

    @Nonnull
    @Override
    public GasTankInfo[] getTankInfo() {
        return new GasTankInfo[]{inputTank, outputTank};
    }

    @Override
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack) {
        return stack.getItem() instanceof IGasItem;
    }

    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        if (slotID == 1) {
            return !itemstack.isEmpty() && itemstack.getItem() instanceof IGasItem gasItem && gasItem.canProvideGas(itemstack, null);
        } else if (slotID == 2) {
            return ChargeUtils.canBeOutputted(itemstack, false);
        }
        return false;
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
    public void writeSustainedData(ItemStack itemStack) {
        if (inputTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "inputTank", inputTank.getGas().write(new NBTTagCompound()));
        }
        if (outputTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "outputTank", outputTank.getGas().write(new NBTTagCompound()));
        }
    }

    @Override
    public void readSustainedData(ItemStack itemStack) {
        inputTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "inputTank")));
        outputTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "outputTank")));
    }

    @Override
    public List<String> getInfo(Upgrade upgrade) {
        return upgrade == Upgrade.SPEED ? upgrade.getExpScaledInfo(this) : upgrade.getMultScaledInfo(this);
    }

    @Override
    public Object[] getTanks() {
        return new Object[]{inputTank, outputTank};
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(inputTank.getStored(), inputTank.getMaxGas());
    }

    @Override
    public void onPlace() {
        MekanismUtils.makeBoundingBlock(world, Coord4D.get(this).offset(EnumFacing.UP).getPos(), Coord4D.get(this));
    }

    @Override
    public void onBreak() {
        world.setBlockToAir(getPos().up());
        world.setBlockToAir(getPos());
    }

    @Override
    public String[] getMethods() {
        return new String[0];
    }

    @Override
    public Object[] invoke(int method, Object[] args) throws NoSuchMethodException {
        return new Object[0];
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (updateDelay == 0) {
            Mekanism.packetHandler.sendUpdatePacket(this);
            updateDelay = 10;
        }
    }
}
