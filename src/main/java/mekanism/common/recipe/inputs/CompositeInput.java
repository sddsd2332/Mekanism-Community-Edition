package mekanism.common.recipe.inputs;

import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTank;
import mekanism.common.util.StackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.oredict.OreDictionary;

/**
 * An input of a gas, a fluid and an item for the pressurized reaction chamber
 */
public class CompositeInput extends MachineInput<CompositeInput> implements IWildInput<CompositeInput> {

    public ItemStack itemInput = ItemStack.EMPTY;
    public ItemStack itemInput2 = ItemStack.EMPTY;
    public ItemStack itemInput3 = ItemStack.EMPTY;
    public ItemStack itemInput4 = ItemStack.EMPTY;
    public ItemStack itemInput5 = ItemStack.EMPTY;
    public ItemStack itemInput6 = ItemStack.EMPTY;
    public ItemStack itemInput7 = ItemStack.EMPTY;
    public ItemStack itemInput8 = ItemStack.EMPTY;
    public ItemStack itemInput9 = ItemStack.EMPTY;


    public FluidStack fluidInput;
    public GasStack gasInput;

    public CompositeInput(ItemStack item, ItemStack item2, ItemStack item3, ItemStack item4, ItemStack item5, ItemStack item6, ItemStack item7, ItemStack item8, ItemStack item9,
                          FluidStack fluid, GasStack gas) {
        itemInput = item;
        itemInput2 = item2;
        itemInput3 = item3;
        itemInput4 = item4;
        itemInput5 = item5;
        itemInput6 = item6;
        itemInput7 = item7;
        itemInput8 = item8;
        itemInput9 = item9;
        fluidInput = fluid;
        gasInput = gas;
    }

    public CompositeInput() {
    }

    @Override
    public void load(NBTTagCompound nbtTags) {
        itemInput = new ItemStack(nbtTags.getCompoundTag("itemInput"));
        itemInput2 = new ItemStack(nbtTags.getCompoundTag("itemInput2"));
        itemInput3 = new ItemStack(nbtTags.getCompoundTag("itemInput3"));
        itemInput4 = new ItemStack(nbtTags.getCompoundTag("itemInput4"));
        itemInput5 = new ItemStack(nbtTags.getCompoundTag("itemInput5"));
        itemInput6 = new ItemStack(nbtTags.getCompoundTag("itemInput6"));
        itemInput7 = new ItemStack(nbtTags.getCompoundTag("itemInput7"));
        itemInput8 = new ItemStack(nbtTags.getCompoundTag("itemInput8"));
        itemInput9 = new ItemStack(nbtTags.getCompoundTag("itemInput9"));
        fluidInput = FluidStack.loadFluidStackFromNBT(nbtTags.getCompoundTag("fluidInput"));
        gasInput = GasStack.readFromNBT(nbtTags.getCompoundTag("gasInput"));
    }


    @Override
    public boolean isValid() {
        return !itemInput.isEmpty() &&
                !itemInput2.isEmpty() &&
                !itemInput3.isEmpty() &&
                !itemInput4.isEmpty() &&
                !itemInput5.isEmpty() &&
                !itemInput6.isEmpty() &&
                !itemInput7.isEmpty() &&
                !itemInput8.isEmpty() &&
                !itemInput9.isEmpty() &&
                fluidInput != null &&
                gasInput != null;
    }

    public boolean use(NonNullList<ItemStack> inventory, int index, int index2, int index3, int index4, int index5, int index6, int index7, int index8, int index9,
                       FluidTank fluidTank, GasTank gasTank, boolean deplete) {
        if (meets(new CompositeInput(inventory.get(index), inventory.get(index2), inventory.get(index3), inventory.get(index4), inventory.get(index5), inventory.get(index6), inventory.get(index7), inventory.get(index8), inventory.get(index9), fluidTank.getFluid(), gasTank.getGas()))) {
            if (deplete) {
                inventory.set(index, StackUtils.subtract(inventory.get(index), itemInput));
                inventory.set(index2, StackUtils.subtract(inventory.get(index2), itemInput2));
                inventory.set(index3, StackUtils.subtract(inventory.get(index3), itemInput3));
                inventory.set(index4, StackUtils.subtract(inventory.get(index4), itemInput4));
                inventory.set(index5, StackUtils.subtract(inventory.get(index5), itemInput5));
                inventory.set(index6, StackUtils.subtract(inventory.get(index6), itemInput6));
                inventory.set(index7, StackUtils.subtract(inventory.get(index7), itemInput7));
                inventory.set(index8, StackUtils.subtract(inventory.get(index8), itemInput8));
                inventory.set(index9, StackUtils.subtract(inventory.get(index9), itemInput9));
                fluidTank.drain(fluidInput.amount, true);
                gasTank.draw(gasInput.amount, true);
            }
            return true;
        }
        return false;
    }

    /**
     * Whether or not this PressurizedReactants's ItemStack entry's item type is equal to the item type of the given item.
     *
     * @param stack - stack to check
     * @return if the stack's item type is contained in this PressurizedReactants
     */
    public boolean containsType(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() == 0) {
            return false;
        }
        return MachineInput.inputItemMatches(stack, itemInput);
    }


    public boolean containsType2(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() == 0) {
            return false;
        }
        return MachineInput.inputItemMatches(stack, itemInput2);
    }

    public boolean containsType3(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() == 0) {
            return false;
        }
        return MachineInput.inputItemMatches(stack, itemInput3);
    }


    public boolean containsType4(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() == 0) {
            return false;
        }
        return MachineInput.inputItemMatches(stack, itemInput4);
    }

    public boolean containsType5(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() == 0) {
            return false;
        }
        return MachineInput.inputItemMatches(stack, itemInput5);
    }

    public boolean containsType6(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() == 0) {
            return false;
        }
        return MachineInput.inputItemMatches(stack, itemInput6);
    }

    public boolean containsType7(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() == 0) {
            return false;
        }
        return MachineInput.inputItemMatches(stack, itemInput7);
    }

    public boolean containsType8(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() == 0) {
            return false;
        }
        return MachineInput.inputItemMatches(stack, itemInput8);
    }

    public boolean containsType9(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() == 0) {
            return false;
        }
        return MachineInput.inputItemMatches(stack, itemInput9);
    }

    /**
     * Whether or not this PressurizedReactants's FluidStack entry's fluid type is equal to the fluid type of the given fluid.
     *
     * @param stack - stack to check
     * @return if the stack's fluid type is contained in this PressurizedReactants
     */
    public boolean containsType(FluidStack stack) {
        if (stack == null || stack.amount == 0) {
            return false;
        }
        return stack.isFluidEqual(fluidInput);
    }

    /**
     * Whether or not this PressurizedReactants's GasStack entry's gas type is equal to the gas type of the given gas.
     *
     * @param stack - stack to check
     * @return if the stack's gas type is contained in this PressurizedReactants
     */
    public boolean containsType(GasStack stack) {
        if (stack == null || stack.amount == 0) {
            return false;
        }
        return stack.isGasEqual(gasInput);
    }

    /**
     * Actual implementation of meetsInput(), performs the checks.
     *
     * @param input - input to check
     * @return if the input meets this input's requirements
     */
    public boolean meets(CompositeInput input) {
        if (input == null || !input.isValid()) {
            return false;
        }
        if (!(getItem(input) && input.fluidInput.isFluidEqual(fluidInput) && input.gasInput.isGasEqual(gasInput))) {
            return false;
        }
        return getItemCount(input) && input.fluidInput.amount >= fluidInput.amount && input.gasInput.amount >= gasInput.amount;
    }


    public boolean getItemCount(CompositeInput input) {
        return input.itemInput.getCount() >= itemInput.getCount() &&
                input.itemInput2.getCount() >= itemInput2.getCount() &&
                input.itemInput3.getCount() >= itemInput3.getCount() &&
                input.itemInput4.getCount() >= itemInput4.getCount() &&
                input.itemInput5.getCount() >= itemInput5.getCount() &&
                input.itemInput6.getCount() >= itemInput6.getCount() &&
                input.itemInput7.getCount() >= itemInput7.getCount() &&
                input.itemInput8.getCount() >= itemInput8.getCount() &&
                input.itemInput9.getCount() >= itemInput9.getCount();
    }

    public boolean getItem(CompositeInput input) {
        return StackUtils.equalsWildcardWithNBT(input.itemInput, itemInput) &&
                StackUtils.equalsWildcardWithNBT(input.itemInput2, itemInput2) &&
                StackUtils.equalsWildcardWithNBT(input.itemInput3, itemInput3) &&
                StackUtils.equalsWildcardWithNBT(input.itemInput4, itemInput4) &&
                StackUtils.equalsWildcardWithNBT(input.itemInput5, itemInput5) &&
                StackUtils.equalsWildcardWithNBT(input.itemInput6, itemInput6) &&
                StackUtils.equalsWildcardWithNBT(input.itemInput7, itemInput7) &&
                StackUtils.equalsWildcardWithNBT(input.itemInput8, itemInput8) &&
                StackUtils.equalsWildcardWithNBT(input.itemInput9, itemInput9);
    }

    @Override
    public CompositeInput copy() {
        return new CompositeInput(
                itemInput.copy(),
                itemInput2.copy(),
                itemInput3.copy(),
                itemInput4.copy(),
                itemInput5.copy(),
                itemInput6.copy(),
                itemInput7.copy(),
                itemInput8.copy(),
                itemInput9.copy(),
                fluidInput.copy(),
                gasInput.copy());
    }


    @Override
    public int hashIngredients() {
        return (StackUtils.hashItemStack(itemInput) +
                StackUtils.hashItemStack(itemInput2) +
                StackUtils.hashItemStack(itemInput3) +
                StackUtils.hashItemStack(itemInput4) +
                StackUtils.hashItemStack(itemInput5) +
                StackUtils.hashItemStack(itemInput6) +
                StackUtils.hashItemStack(itemInput7) +
                StackUtils.hashItemStack(itemInput8) +
                StackUtils.hashItemStack(itemInput9)) << 16
                | (fluidInput.getFluid() != null ? fluidInput.getFluid().hashCode() : 0) << 8 | gasInput.hashCode();
    }


    @Override
    public boolean testEquality(CompositeInput other) {
        return testItemEquality(other) && other.containsType(fluidInput) && other.containsType(gasInput);
    }

    public boolean testItemEquality(CompositeInput other) {
        return other.containsType(itemInput) &&
                other.containsType2(itemInput2) &&
                other.containsType3(itemInput3) &&
                other.containsType4(itemInput4) &&
                other.containsType5(itemInput5) &&
                other.containsType6(itemInput6) &&
                other.containsType7(itemInput7) &&
                other.containsType8(itemInput8) &&
                other.containsType9(itemInput9);
    }

    @Override
    public boolean isInstance(Object other) {
        return other instanceof CompositeInput;
    }

    @Override
    public CompositeInput wildCopy() {
        return new CompositeInput(
                new ItemStack(itemInput.getItem(), itemInput.getCount(), OreDictionary.WILDCARD_VALUE),
                new ItemStack(itemInput2.getItem(), itemInput2.getCount(), OreDictionary.WILDCARD_VALUE),
                new ItemStack(itemInput3.getItem(), itemInput3.getCount(), OreDictionary.WILDCARD_VALUE),
                new ItemStack(itemInput4.getItem(), itemInput4.getCount(), OreDictionary.WILDCARD_VALUE),
                new ItemStack(itemInput5.getItem(), itemInput5.getCount(), OreDictionary.WILDCARD_VALUE),
                new ItemStack(itemInput6.getItem(), itemInput6.getCount(), OreDictionary.WILDCARD_VALUE),
                new ItemStack(itemInput7.getItem(), itemInput7.getCount(), OreDictionary.WILDCARD_VALUE),
                new ItemStack(itemInput8.getItem(), itemInput8.getCount(), OreDictionary.WILDCARD_VALUE),
                new ItemStack(itemInput9.getItem(), itemInput9.getCount(), OreDictionary.WILDCARD_VALUE),
                fluidInput, gasInput);
    }
}
