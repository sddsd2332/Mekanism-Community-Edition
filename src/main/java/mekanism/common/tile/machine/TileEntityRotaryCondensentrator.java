package mekanism.common.tile.machine;

import io.netty.buffer.ByteBuf;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.*;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.Mekanism;
import mekanism.common.SideData;
import mekanism.common.Upgrade;
import mekanism.common.Upgrade.IUpgradeInfoHandler;
import mekanism.common.base.*;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.prefab.TileEntityMachine;
import mekanism.common.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileEntityRotaryCondensentrator extends TileEntityMachine implements ISustainedData, IFluidHandlerWrapper, IGasHandler, IUpgradeInfoHandler, ITankManager,
        IComparatorSupport, ISideConfiguration {

    public static final int MAX_FLUID = 10000;
    public GasTank gasTank = new GasTank(MAX_FLUID);
    public FluidTank fluidTank = new FluidTank(MAX_FLUID);
    /**
     * 0: gas -> fluid; 1: fluid -> gas
     */
    public int mode;

    public int gasOutput = 256;

    public double clientEnergyUsed;

    public TileComponentEjector ejectorComponent;

    public TileComponentConfig configComponent;

    private int currentRedstoneLevel;

    public TileEntityRotaryCondensentrator() {
        super("machine.rotarycondensentrator", MachineType.ROTARY_CONDENSENTRATOR, 5);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY, TransmissionType.GAS, TransmissionType.FLUID);

        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.NONE, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.ENERGY, new int[]{4}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.FLUID, new int[]{2, 3}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.GAS, new int[]{0, 1}));
        configComponent.setConfig(TransmissionType.ITEM, new byte[]{2, 1, 2, 2, 3, 2});

        configComponent.addOutput(TransmissionType.FLUID, new SideData(DataType.NONE, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.FLUID, new SideData(DataType.FLUID, new int[]{0}));
        configComponent.setConfig(TransmissionType.FLUID, new byte[]{0, 0, 0, 0, 0, 1});

        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.NONE, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.GAS, new int[]{1}));
        configComponent.setConfig(TransmissionType.GAS, new byte[]{0, 0, 0, 0, 1, 0});

        configComponent.setInputConfig(TransmissionType.ENERGY);

        inventory = NonNullList.withSize(6, ItemStack.EMPTY);

        ejectorComponent = new TileComponentEjector(this);

        ejectorComponent.setOutputData(TransmissionType.GAS, configComponent.getOutputs(TransmissionType.GAS).get(1));
        ejectorComponent.setOutputData(TransmissionType.FLUID, configComponent.getOutputs(TransmissionType.FLUID).get(1));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            ChargeUtils.discharge(4, this);
            if (!MekanismConfig.current().mekce.RotaryCondensentratorAuto.val()) {
                Autoeject(); //Used to deal with the problem that gas/fluid will automatically return to the tank when the ejection mode is on.
            }
            if (mode == 0) {
                TileUtils.receiveGasItem(inventory.get(1), gasTank);

                if (FluidContainerUtils.isFluidContainer(inventory.get(2))) {
                    FluidContainerUtils.handleContainerItemFill(this, fluidTank, 2, 3);
                    //I don't know why, mode 0 [gas-> liquid] causes the fluid to be transplanted into the tank using a slot, which retains the liquid but no quantity, and here it is repaired by setting the fluid to null when it is judged that there is fluid but the quantity is 0
                    if (fluidTank.getFluid() != null && fluidTank.getFluidAmount() == 0) {
                        fluidTank.setFluid(null);
                    }
                }

                if (getEnergy() >= energyPerTick && MekanismUtils.canFunction(this) && isValidGas(gasTank.getGas()) &&
                        (fluidTank.getFluid() == null || (fluidTank.getFluidAmount() < MAX_FLUID && gasEquals(gasTank.getGas(), fluidTank.getFluid())))) {
                    int operations = getUpgradedUsage();
                    double prev = getEnergy();

                    setActive(true);
                    fluidTank.fill(new FluidStack(gasTank.stored.getGas().getFluid(), operations), true);
                    gasTank.draw(operations, true);
                    setEnergy(getEnergy() - energyPerTick * operations);
                    clientEnergyUsed = prev - getEnergy();
                } else if (prevEnergy >= getEnergy()) {
                    setActive(false);
                }
            } else if (mode == 1) {
                TileUtils.drawGas(inventory.get(0), gasTank);
                if (FluidContainerUtils.isFluidContainer(inventory.get(2)) && fluidTank.getFluidAmount() != fluidTank.getCapacity()) {
                    FluidContainerUtils.handleContainerItemEmpty(this, fluidTank, 2, 3);
                }

                if (getEnergy() >= energyPerTick && MekanismUtils.canFunction(this) && isValidFluid(fluidTank.getFluid()) &&
                        (gasTank.getGas() == null || (gasTank.getStored() < MAX_FLUID && gasEquals(gasTank.getGas(), fluidTank.getFluid())))) {
                    int operations = getUpgradedUsage();
                    double prev = getEnergy();

                    setActive(true);
                    gasTank.receive(new GasStack(GasRegistry.getGas(fluidTank.getFluid().getFluid()), operations), true);
                    fluidTank.drain(operations, true);
                    setEnergy(getEnergy() - energyPerTick * operations);
                    clientEnergyUsed = prev - getEnergy();
                } else if (prevEnergy >= getEnergy()) {
                    setActive(false);
                }
            }
            prevEnergy = getEnergy();
            int newRedstoneLevel = getRedstoneLevel();
            if (newRedstoneLevel != currentRedstoneLevel) {
                world.updateComparatorOutputLevel(pos, getBlockType());
                currentRedstoneLevel = newRedstoneLevel;

            }
        }
    }

    public void Autoeject() {
        if (mode == 0) {
            configComponent.setEjecting(TransmissionType.GAS, false);
            configComponent.setEjecting(TransmissionType.FLUID, true);
        } else if (mode == 1) {
            configComponent.setEjecting(TransmissionType.GAS, true);
            configComponent.setEjecting(TransmissionType.FLUID, false);
        }
    }

    public int getUpgradedUsage() {
        int possibleProcess = Math.min((int) Math.pow(2, upgradeComponent.getUpgrades(Upgrade.SPEED)), MekanismConfig.current().mekce.MAXspeedmachines.val());
        if (mode == 0) { //Gas to fluid
            possibleProcess = Math.min(Math.min(gasTank.getStored(), fluidTank.getCapacity() - fluidTank.getFluidAmount()), possibleProcess);
        } else { //Fluid to gas
            possibleProcess = Math.min(Math.min(fluidTank.getFluidAmount(), gasTank.getNeeded()), possibleProcess);
        }
        possibleProcess = Math.min((int) (getEnergy() / energyPerTick), possibleProcess);
        return Math.min(mode == 0 ? gasTank.getStored() : fluidTank.getFluidAmount(), possibleProcess);
    }

    public boolean isValidGas(GasStack g) {
        return g != null && g.getGas().hasFluid();

    }

    public boolean gasEquals(GasStack gas, FluidStack fluid) {
        return fluid != null && gas != null && gas.getGas().hasFluid() && gas.getGas().getFluid() == fluid.getFluid();

    }

    public boolean isValidFluid(@Nonnull Fluid f) {
        return GasRegistry.getGas(f) != null;
    }

    public boolean isValidFluid(FluidStack f) {
        return f != null && isValidFluid(f.getFluid());
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            int type = dataStream.readInt();
            if (type == 0) {
                mode = mode == 0 ? 1 : 0;
            }
            for (EntityPlayer player : playersUsing) {
                Mekanism.packetHandler.sendTo(new TileEntityMessage(this), (EntityPlayerMP) player);
            }

            return;
        }

        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            mode = dataStream.readInt();
            clientEnergyUsed = dataStream.readDouble();
            TileUtils.readTankData(dataStream, fluidTank);
            TileUtils.readTankData(dataStream, gasTank);
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(mode);
        data.add(clientEnergyUsed);
        TileUtils.addTankData(data, fluidTank);
        TileUtils.addTankData(data, gasTank);
        return data;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        mode = nbtTags.getInteger("mode");
        gasTank.read(nbtTags.getCompoundTag("gasTank"));
        if (nbtTags.hasKey("fluidTank")) {
            fluidTank.readFromNBT(nbtTags.getCompoundTag("fluidTank"));
        }
    }


    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setInteger("mode", mode);
        nbtTags.setTag("gasTank", gasTank.write(new NBTTagCompound()));
        if (fluidTank.getFluid() != null) {
            nbtTags.setTag("fluidTank", fluidTank.writeToNBT(new NBTTagCompound()));
        }

    }

    @Override
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer) {
        if (canReceiveGas(side, stack.getGas())) {
            return gasTank.receive(stack, doTransfer);
        }
        return 0;
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        if (canDrawGas(side, null)) {
            return gasTank.draw(amount, doTransfer);
        }
        return null;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        return mode == 1 && configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(1) && gasTank.canDraw(type);
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        return mode == 0 && configComponent.getOutput(TransmissionType.GAS, side, facing).hasSlot(1) && gasTank.canReceive(type);
    }

    @Nonnull
    @Override
    public GasTankInfo[] getTankInfo() {
        return new GasTankInfo[]{gasTank};
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
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side) {
        return configComponent.isCapabilityDisabled(capability, side, facing) || super.isCapabilityDisabled(capability, side);
    }

    @Override
    public void writeSustainedData(ItemStack itemStack) {
        if (fluidTank.getFluid() != null) {
            ItemDataUtils.setCompound(itemStack, "fluidTank", fluidTank.getFluid().writeToNBT(new NBTTagCompound()));
        }
        if (gasTank.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "gasTank", gasTank.getGas().write(new NBTTagCompound()));
        }
    }

    @Override
    public void readSustainedData(ItemStack itemStack) {
        fluidTank.setFluid(FluidStack.loadFluidStackFromNBT(ItemDataUtils.getCompound(itemStack, "fluidTank")));
        gasTank.setGas(GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "gasTank")));
    }

    @Override
    public int fill(EnumFacing from, @Nonnull FluidStack resource, boolean doFill) {
        return fluidTank.fill(resource, doFill);
    }

    @Override
    @Nullable
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return fluidTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, @Nonnull FluidStack fluid) {
        return mode == 1 && configComponent.getOutput(TransmissionType.FLUID, from, facing).hasSlot(0) && FluidContainerUtils.canFill(fluidTank.getFluid(), fluid);
    }

    @Override
    public boolean canDrain(EnumFacing from, @Nullable FluidStack fluid) {
        return mode == 0 && configComponent.getOutput(TransmissionType.FLUID, from, facing).hasSlot(0) && FluidContainerUtils.canDrain(fluidTank.getFluid(), fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {

        SideData data = configComponent.getOutput(TransmissionType.FLUID, from, facing);
        return data.getFluidTankInfo(this);
    }

    @Override
    public FluidTankInfo[] getAllTanks() {
        return new FluidTankInfo[]{fluidTank.getInfo()};
    }

    @Override
    public List<String> getInfo(Upgrade upgrade) {
        return upgrade == Upgrade.SPEED ? upgrade.getExpScaledInfo(this) : upgrade.getMultScaledInfo(this);
    }


    @Override
    public Object[] getTanks() {
        return new Object[]{fluidTank,gasTank};
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return configComponent.getOutput(TransmissionType.ITEM, side, facing).availableSlots;
    }

    @Override
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack) {
        if (slot == 0) {
            //Gas
            return stack.getItem() instanceof IGasItem;
        } else if (slot == 2) {
            //Fluid
            return FluidContainerUtils.isFluidContainer(stack);
        } else if (slot == 4) {
            return ChargeUtils.canBeDischarged(stack);
        }
        return false;
    }

    @Override
    public int getRedstoneLevel() {
        if (mode == 0) {
            return MekanismUtils.redstoneLevelFromContents(gasTank.getStored(), gasTank.getMaxGas());
        }
        return MekanismUtils.redstoneLevelFromContents(fluidTank.getFluidAmount(), fluidTank.getCapacity());
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
}
