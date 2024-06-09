package mekanism.common.recipe.outputs;

import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTank;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;

public class CompositeOutput extends MachineOutput<CompositeOutput>{

    public ItemStack itemOutput = ItemStack.EMPTY;
    public GasStack gasOutput;
    public FluidStack fluidOutput;

    public CompositeOutput(ItemStack itemStack , FluidStack fluidStack,GasStack gasStack){
        itemOutput= itemStack;
        fluidOutput = fluidStack;
        gasOutput = gasStack;
    }

    public CompositeOutput(){
    }


    @Override
    public void load(NBTTagCompound nbtTags) {
        itemOutput = new ItemStack(nbtTags.getCompoundTag("itemOutput"));
        fluidOutput = FluidStack.loadFluidStackFromNBT(nbtTags.getCompoundTag("fluidOutput"));
        gasOutput = GasStack.readFromNBT(nbtTags.getCompoundTag("gasOutput"));
    }

    public boolean canFillGasTank(GasTank tank) {
        return tank.canReceive(gasOutput.getGas()) && tank.getNeeded() >= gasOutput.amount;
    }

    public boolean canAddProducts(NonNullList<ItemStack> inventory, int index) {
        ItemStack stack = inventory.get(index);
        return stack.isEmpty() || (ItemHandlerHelper.canItemStacksStack(stack, itemOutput) && stack.getCount() + itemOutput.getCount() <= stack.getMaxStackSize());
    }

    public void fillTank(GasTank tank) {
        tank.receive(gasOutput, true);
    }

    public void addProducts(NonNullList<ItemStack> inventory, int index) {
        ItemStack stack = inventory.get(index);
        if (stack.isEmpty()) {
            inventory.set(index, itemOutput.copy());
        } else if (ItemHandlerHelper.canItemStacksStack(stack, itemOutput)) {
            stack.grow(itemOutput.getCount());
        }
    }

    @Override
    public CompositeOutput copy() {
        return new CompositeOutput(itemOutput.copy(),fluidOutput.copy(),gasOutput.copy());
    }

    public boolean applyOutputs(NonNullList<ItemStack> inventory, int index, GasTank gasTank, boolean doEmit) {
        if (canFillGasTank(gasTank) && canAddProducts(inventory, index)) {
            if (doEmit) {
                fillTank(gasTank);
                addProducts(inventory, index);
            }
            return true;
        }
        return false;
    }

    public boolean applyFluidOutputs(FluidTank fluidTank, boolean doEmit) {
        if (fluidTank.fill(fluidOutput, false) > 0) {
            fluidTank.fill(fluidOutput, doEmit);
            return true;
        }
        return false;
    }


}
