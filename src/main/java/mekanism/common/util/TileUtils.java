package mekanism.common.util;

import io.netty.buffer.ByteBuf;
import mekanism.api.TileNetworkList;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTank;
import mekanism.api.gas.IGasItem;
import mekanism.common.PacketHandler;
import mekanism.common.tile.prefab.TileEntityBasicBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.EnumSet;

//TODO: Move this and factor out the parts into proper classes. This is mainly just temp to make organization not as needed
public class TileUtils {

    // N.B. All the tank I/O functions rely on the fact that an empty NBT Compound is a singular
    // byte and that the Gas/Fluid Stacks initialize to null if they are de-serialized from an
    // empty tag.
    private static final NBTTagCompound EMPTY_TAG_COMPOUND = new NBTTagCompound();

    public static void addTankData(TileNetworkList data, GasTank tank) {
        if (tank.getGas() != null) {
            data.add(tank.getGas().write(new NBTTagCompound()));
        } else {
            data.add(EMPTY_TAG_COMPOUND);
        }
    }

    public static void addTankData(TileNetworkList data, FluidTank tank) {
        addFluidStack(data, tank.getFluid());
    }

    public static void addFluidStack(TileNetworkList data, FluidStack stack) {
        if (stack != null) {
            data.add(stack.writeToNBT(new NBTTagCompound()));
        } else {
            data.add(EMPTY_TAG_COMPOUND);
        }
    }

    public static void addGasStack(TileNetworkList data, GasStack stack) {
        if (stack != null) {
            data.add(stack.write(new NBTTagCompound()));
        } else {
            data.add(EMPTY_TAG_COMPOUND);
        }
    }

    public static void readTankData(ByteBuf dataStream, GasTank tank) {
        tank.setGas(GasStack.readFromNBT(PacketHandler.readNBT(dataStream)));
    }

    public static void readTankData(ByteBuf dataStream, FluidTank tank) {
        tank.setFluid(readFluidStack(dataStream));
    }

    public static FluidStack readFluidStack(ByteBuf dataStream) {
        return FluidStack.loadFluidStackFromNBT(PacketHandler.readNBT(dataStream));
    }

    public static GasStack readGasStack(ByteBuf dataStream) {
        return GasStack.readFromNBT(PacketHandler.readNBT(dataStream));
    }

    //Returns true if it entered the if statement, basically for use by TileEntityGasTank
    public static boolean receiveGas(ItemStack stack, GasTank tank) {
        if (!stack.isEmpty() && (tank.getGas() == null || tank.getStored() < tank.getMaxGas())) {
            tank.receive(GasUtils.removeGas(stack, tank.getGasType(), tank.getNeeded()), true);
            return true;
        }
        return false;
    }

    public static void receiveGasItem(ItemStack stack, GasTank tank) {
        if (!stack.isEmpty() && tank.getNeeded() > 0 && stack.getItem() instanceof IGasItem item) {
            GasStack gasStack = item.getGas(stack);
            if (gasStack != null && item.canProvideGas(stack, gasStack.getGas())) {
                Gas gas = gasStack.getGas();
                if (gas != null && tank.canReceive(gas)) {
                    tank.receive(GasUtils.removeGas(stack, gas, tank.getNeeded()), true);
                }
            }
        }
    }

    public static void receiveGasItem(ItemStack stack, GasTank tank, Gas isValidGas) {
        if (!stack.isEmpty() && tank.getNeeded() > 0 && stack.getItem() instanceof IGasItem item) {
            GasStack gasStack = item.getGas(stack);
            if (gasStack != null && item.canProvideGas(stack, gasStack.getGas())) {
                Gas gas = gasStack.getGas();
                if (gas != null && tank.canReceive(gas) && gas == isValidGas) {
                    tank.receive(GasUtils.removeGas(stack, gas, tank.getNeeded()), true);
                }
            }
        }
    }

    public static void drawGas(ItemStack stack, GasTank tank) {
        drawGas(stack, tank, true);
    }

    public static void drawGas(ItemStack stack, GasTank tank, boolean doDraw) {
        if (!stack.isEmpty() && tank.getGas() != null) {
            tank.draw(GasUtils.addGas(stack, tank.getGas()), doDraw);
        }
    }

    public static void emitGas(TileEntityBasicBlock tile, GasTank tank, int gasOutput, EnumFacing facing) {
        if (tank.getGas() != null) {
            GasStack toSend = new GasStack(tank.getGas().getGas(), Math.min(tank.getStored(), gasOutput));
            tank.draw(GasUtils.emit(toSend, tile, EnumSet.of(facing)), true);
        }
    }
}
