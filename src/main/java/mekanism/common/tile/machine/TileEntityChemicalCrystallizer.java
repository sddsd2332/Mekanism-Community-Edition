package mekanism.common.tile.machine;

import io.netty.buffer.ByteBuf;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.*;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.Mekanism;
import mekanism.common.SideData;
import mekanism.common.base.ISustainedData;
import mekanism.common.base.ITankManager;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.GasInput;
import mekanism.common.recipe.machines.CrystallizerRecipe;
import mekanism.common.recipe.outputs.ItemStackOutput;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.prefab.TileEntityUpgradeableMachine;
import mekanism.common.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import java.util.Map;

public class TileEntityChemicalCrystallizer extends TileEntityUpgradeableMachine<GasInput, ItemStackOutput, CrystallizerRecipe> implements IGasHandler, ISustainedData, ITankManager {

    public static final int MAX_GAS = 10000;
    public GasTank inputTank = new GasTank(MAX_GAS);
    public CrystallizerRecipe cachedRecipe;
    public float prevScale;
    public int updateDelay;
    public boolean needsPacket;

    public TileEntityChemicalCrystallizer() {
        super("crystallizer", MachineType.CHEMICAL_CRYSTALLIZER, 3, 200);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY, TransmissionType.GAS);

        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.NONE, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.INPUT, new int[]{0}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.OUTPUT, new int[]{1}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.ENERGY, new int[]{2}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(new int[]{0, 1}, new boolean[]{false, true}));
        configComponent.setConfig(TransmissionType.ITEM, new byte[]{1, 1, 1, 3, 1, 2});

        configComponent.setInputConfig(TransmissionType.GAS);

        configComponent.setInputConfig(TransmissionType.ENERGY);

        inventory = NonNullListSynchronized.withSize(4, ItemStack.EMPTY);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(TransmissionType.ITEM, configComponent.getOutputs(TransmissionType.ITEM).get(2));
        ejectorComponent.setInputOutputData(TransmissionType.ITEM, configComponent.getOutputs(TransmissionType.ITEM).get(4));
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
            ItemStack stack = inventory.get(0);
            if (!stack.isEmpty() && stack.getItem() instanceof IGasItem item && item.getGas(stack) != null &&
                    Recipe.CHEMICAL_CRYSTALLIZER.containsRecipe(item.getGas(stack).getGas())) {
                TileUtils.receiveGasItem(inventory.get(0), inputTank);
            }
            CrystallizerRecipe recipe = getRecipe();
            if (canOperate(recipe) && MekanismUtils.canFunction(this) && getEnergy() >= energyPerTick) {
                setActive(true);
                setEnergy(getEnergy() - energyPerTick);
                if ((operatingTicks + 1) < ticksRequired) {
                    operatingTicks++;
                } else {
                    operate(recipe);
                    operatingTicks = 0;
                }
            } else if (prevEnergy >= getEnergy()) {
                setActive(false);
            }
            if (!canOperate(recipe)) {
                operatingTicks = 0;
            }
            prevEnergy = getEnergy();
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
            float targetScale = (float) (inputTank.getGas() != null ? inputTank.getGas().amount : 0) / inputTank.getMaxGas();
            if (Math.abs(prevScale - targetScale) > 0.01) {
                prevScale = (9 * prevScale + targetScale) / 10;
            }
        }
    }

    @Override
    protected void upgradeInventory(TileEntityFactory factory) {
        factory.gasTank.setGas(inputTank.getGas());
        factory.inventory.set(1, inventory.get(2));
        factory.inventory.set(5 + 3, inventory.get(1));
        factory.inventory.set(0, inventory.get(3));
        factory.inventory.set(4, inventory.get(0));
    }

    public GasInput getInput() {
        return new GasInput(inputTank.getGas());
    }

    public CrystallizerRecipe getRecipe() {
        GasInput input = getInput();
        if (cachedRecipe == null || !input.testEquality(cachedRecipe.getInput())) {
            cachedRecipe = RecipeHandler.getChemicalCrystallizerRecipe(getInput());
        }
        return cachedRecipe;
    }

    public boolean canOperate(CrystallizerRecipe recipe) {
        return recipe != null && recipe.canOperate(inputTank, inventory, 1);
    }

    public void operate(CrystallizerRecipe recipe) {
        recipe.operate(inputTank, inventory, 1);
        markForUpdateSync();
    }

    @Override
    public Map<GasInput, CrystallizerRecipe> getRecipes() {
        return Recipe.CHEMICAL_CRYSTALLIZER.get();
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            TileUtils.readTankData(dataStream, inputTank);
            if (updateDelay == 0) {
                updateDelay = MekanismConfig.current().general.UPDATE_DELAY.val();
                MekanismUtils.updateBlock(world, getPos());
            }
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        TileUtils.addTankData(data, inputTank);
        return data;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        inputTank.read(nbtTags.getCompoundTag("rightTank"));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setTag("rightTank", inputTank.write(new NBTTagCompound()));
        nbtTags.setBoolean("sideDataStored", true);
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        return configComponent.getOutput(TransmissionType.GAS, side, facing).ioState == SideData.IOState.INPUT && inputTank.canReceive(type) && Recipe.CHEMICAL_CRYSTALLIZER.containsRecipe(type);
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        if (canReceiveGas(side, stack.getGas())) {
            int recipeAmount = Recipe.CHEMICAL_CRYSTALLIZER.get().get(new GasInput(stack)).recipeInput.ingredient.amount;
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
        return null;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        return false;
    }

    @Override
    @Nonnull
    public GasTankInfo[] getTankInfo() {
        return new GasTankInfo[]{inputTank};
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
        }
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return (T) this;
        }
        return super.getCapability(capability, side);
    }


    @Override
    public boolean isItemValidForSlot(int slotID, @Nonnull ItemStack itemstack) {
        if (slotID == 0) {
            return !itemstack.isEmpty() && itemstack.getItem() instanceof IGasItem  gasItem && gasItem.getGas(itemstack) != null &&
                    Recipe.CHEMICAL_CRYSTALLIZER.containsRecipe(gasItem.getGas(itemstack).getGas());
        } else if (slotID == 2) {
            return ChargeUtils.canBeDischarged(itemstack);
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        if (slotID == 0) {
            return !itemstack.isEmpty() && itemstack.getItem() instanceof IGasItem gasItem && gasItem.getGas(itemstack) == null;
        } else if (slotID == 1) {
            return true;
        } else if (slotID == 2) {
            return ChargeUtils.canBeOutputted(itemstack, false);
        }
        return false;
    }

    @Override
    public TileComponentConfig getConfig() {
        return configComponent;
    }

    @Override
    public EnumFacing getOrientation() {
        return facing;
    }

    @Override
    public TileComponentEjector getEjector() {
        return ejectorComponent;
    }

    @Override
    public void writeSustainedData(ItemStack itemStack) {
        if (inputTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "inputTank", inputTank.getGas().write(new NBTTagCompound()));
        }
    }

    @Override
    public void readSustainedData(ItemStack itemStack) {
        inputTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "inputTank")));
    }


    @Override
    public Object[] getTanks() {
        return new Object[]{inputTank};
    }

    public int getScaledFuelLevel(int i) {
        return inputTank.getStored() * i / inputTank.getMaxGas();
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
