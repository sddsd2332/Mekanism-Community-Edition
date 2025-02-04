package mekanism.common.util;

import mekanism.api.gas.*;
import mekanism.common.base.target.GasHandlerTarget;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A handy class containing several utilities for efficient gas transfer.
 *
 * @author AidanBrady
 */
public final class GasUtils {

    public static IGasHandler[] getConnectedAcceptors(BlockPos pos, World world, Collection<EnumFacing> sides) {
        final IGasHandler[] acceptors = {null, null, null, null, null, null};
        EmitUtils.forEachSide(world, pos, sides, (tile, side) ->
                acceptors[side.ordinal()] = CapabilityUtils.getCapability(tile, Capabilities.GAS_HANDLER_CAPABILITY, side.getOpposite()));
        return acceptors;
    }

    /**
     * Gets all the acceptors around a tile entity.
     *
     * @return array of IGasAcceptors
     */
    public static IGasHandler[] getConnectedAcceptors(BlockPos pos, World world) {
        return getConnectedAcceptors(pos, world, EnumSet.allOf(EnumFacing.class));
    }

    public static boolean isValidAcceptorOnSide(TileEntity tile, EnumFacing side) {
        if (CapabilityUtils.hasCapability(tile, Capabilities.GRID_TRANSMITTER_CAPABILITY, side.getOpposite())) {
            return false;
        }
        return CapabilityUtils.hasCapability(tile, Capabilities.GAS_HANDLER_CAPABILITY, side.getOpposite());
    }

    public static void clearIfInvalid(GasTank tank, Predicate<Gas> isValid) {
        if (MekanismConfig.current().general.voidInvalidGases.val()) {
            Gas gas = tank.getGasType();
            if (gas != null && !isValid.test(gas)) {
                tank.setGas(null);
            }
        }
    }

    /**
     * Removes a specified amount of gas from an IGasItem.
     *
     * @param itemStack - ItemStack of the IGasItem
     * @param type      - type of gas to remove from the IGasItem, null if it doesn't matter
     * @param amount    - amount of gas to remove from the ItemStack
     * @return the GasStack removed by the IGasItem
     */
    public static GasStack removeGas(ItemStack itemStack, Gas type, int amount) {
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IGasItem item) {
            if (type != null && item.getGas(itemStack) != null && item.getGas(itemStack).getGas() != type || !item.canProvideGas(itemStack, type)) {
                return null;
            }
            return item.removeGas(itemStack, amount);
        }
        return null;
    }

    /**
     * Adds a specified amount of gas to an IGasItem.
     *
     * @param itemStack - ItemStack of the IGasItem
     * @param stack     - stack to add to the IGasItem
     * @return amount of gas accepted by the IGasItem
     */
    public static int addGas(ItemStack itemStack, GasStack stack) {
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IGasItem gasItem && gasItem.canReceiveGas(itemStack, stack.getGas()) && (gasItem.getGas(itemStack) == null || (gasItem.getGas(itemStack) != null && gasItem.getGas(itemStack).amount != gasItem.getMaxGas(itemStack)))) {
            return gasItem.addGas(itemStack, stack.copy());
        }
        return 0;
    }

    /**
     * Emits gas from a central block by splitting the received stack among the sides given.
     *
     * @param stack - the stack to output
     * @param from  - the TileEntity to output from
     * @param sides - the list of sides to output from
     * @return the amount of gas emitted
     */
    public static int emit(GasStack stack, TileEntity from, Set<EnumFacing> sides) {
        if (stack == null || stack.amount == 0) {
            return 0;
        }

        //Fake that we have one target given we know that no sides will overlap
        // This allows us to have slightly better performance
        final GasHandlerTarget target = new GasHandlerTarget(stack);
        if (from != null) {
            EmitUtils.forEachSide(from.getWorld(), from.getPos(), sides, (acceptor, side) -> {
                //Invert to get access side
                final EnumFacing accessSide = side.getOpposite();
                //Collect cap
                CapabilityUtils.runIfCap(acceptor, Capabilities.GAS_HANDLER_CAPABILITY, accessSide,
                        (handler) -> {
                            if (handler.canReceiveGas(accessSide, stack.getGas())) {
                                target.addHandler(accessSide, handler);
                            }
                        });
            });
        }
        int curHandlers = target.getHandlers().size();
        if (curHandlers > 0) {
            return EmitUtils.sendToAcceptors(java.util.Collections.singleton(target), curHandlers, stack.amount, stack);
        }
        return 0;
    }

    public static void writeSustainedData(GasTank gasTank, ItemStack itemStack) {
        if (gasTank.stored != null && gasTank.stored.getGas() != null) {
            ItemDataUtils.setCompound(itemStack, "gasStored", gasTank.stored.write(new NBTTagCompound()));
        }
    }

    public static void readSustainedData(GasTank gasTank, ItemStack itemStack) {
        if (ItemDataUtils.hasData(itemStack, "gasStored")) {
            gasTank.stored = GasStack.readFromNBT(ItemDataUtils.getCompound(itemStack, "gasStored"));
        } else {
            gasTank.stored = null;
        }
    }

    public static boolean canDrain(@Nullable GasStack tankGas, @Nullable Gas outGas) {
        return tankGas != null && (outGas == null || tankGas.isGasEqual(outGas));
    }
}
