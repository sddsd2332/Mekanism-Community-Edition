package mekanism.api.energy;

import net.minecraft.item.ItemStack;

/**
 * Implement this in an item's class if it should be able to store electricity.
 *
 * @author aidancbrady
 */
public interface IEnergizedItem {

    /**
     * Gets and returns the amount of energy stored in this item.
     *
     * @param itemStack - the ItemStack to check
     * @return energy stored
     */
    double getEnergy(ItemStack itemStack);

    /**
     * Sets this item's stored energy value to a new amount.
     *
     * @param itemStack - the ItemStack who's energy value is to be change
     * @param amount    - new amount of energy
     */
    void setEnergy(ItemStack itemStack, double amount);

    /**
     * Gets and returns this item's maximum amount of energy that can be stored.
     *
     * @param itemStack - the ItemStack to check
     * @return maximum energy
     */
    double getMaxEnergy(ItemStack itemStack);

    /**
     * Gets and returns how much energy this item can transfer to and from charging slots.
     *
     * @param itemStack - the ItemStack to check
     * @return transfer amount
     */
    double getMaxTransfer(ItemStack itemStack);

    /**
     * Gets and returns whether or not this item can receive energy from a charging slot.
     *
     * @param itemStack - the ItemStack to check
     * @return if the item can receive energy
     */
    boolean canReceive(ItemStack itemStack);

    /**
     * Gets and returns whether or not this item can send energy to a charging slot.
     *
     * @param itemStack - the ItemStack to check
     * @return if the item can send energy
     */
    boolean canSend(ItemStack itemStack);

    default double getEnergyRatio(ItemStack stack) {
        return getEnergy(stack) / getMaxEnergy(stack);
    }

    default double getNeeded(ItemStack stack) {
        return getMaxEnergy(stack) - getEnergy(stack);
    }

    default double insert(ItemStack stack, double amount, boolean action) {
        if (amount == 0) {
            //"Fail quick" if the given amount is empty
            return amount;
        }
        double needed = getNeeded(stack);
        if (needed == 0) {
            //Fail if we are a full container
            return amount;
        }
        double toAdd = Math.min(amount, needed);
        if (toAdd != 0 && action) {
            //If we want to actually insert the energy, then update the current energy
            // Note: this also will mark that the contents changed
            setEnergy(stack, getEnergy(stack) + (toAdd));
        }
        return amount - toAdd;
    }

    default double extract(ItemStack stack, double amount, boolean action) {
        if (getEnergy(stack) == 0 || amount == 0) {
            return 0;
        }
        double ret = Math.min(getEnergy(stack), amount);
        if (ret != 0 && action) {
            // Note: this also will mark that the contents changed
            setEnergy(stack, getEnergy(stack) - (ret));
        }
        return ret;
    }



}
