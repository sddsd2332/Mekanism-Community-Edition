package mekanism.common.util;

import mekanism.common.base.target.FluidHandlerTarget;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class PipeUtils {

    public static final FluidTankInfo[] EMPTY = new FluidTankInfo[]{};

    public static boolean isValidAcceptorOnSide(TileEntity tile, EnumFacing side) {
        if (tile == null || CapabilityUtils.hasCapability(tile, Capabilities.GRID_TRANSMITTER_CAPABILITY, side.getOpposite()) ||
                !CapabilityUtils.hasCapability(tile, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite())) {
            return false;
        }

        IFluidHandler container = CapabilityUtils.getCapability(tile, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
        if (container == null) {
            return false;
        }

        IFluidTankProperties[] infoArray = container.getTankProperties();
        if (infoArray != null && infoArray.length > 0) {
            for (IFluidTankProperties info : infoArray) {
                if (info != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets all the acceptors around a tile entity.
     *
     * @return array of IFluidHandlers
     */
    public static IFluidHandler[] getConnectedAcceptors(BlockPos pos, World world) {
        final IFluidHandler[] acceptors = new IFluidHandler[6];
        EmitUtils.forEachSide(world, pos, EnumSet.allOf(EnumFacing.class), (tile, side) ->
                acceptors[side.ordinal()] = CapabilityUtils.getCapability(tile, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()));
        return acceptors;
    }

    public static IFluidHandler[] getConnectedAcceptors(Collection<EnumFacing> sides, BlockPos pos, World world) {
        final IFluidHandler[] acceptors = new IFluidHandler[6];
        EmitUtils.forEachSide(world, pos, sides, (tile, side) ->
                acceptors[side.ordinal()] = CapabilityUtils.getCapability(tile, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()));
        return acceptors;
    }

    /**
     * Emits fluid from a central block by splitting the received stack among the sides given.
     *
     * @param sides - the list of sides to output from
     * @param stack - the stack to output
     * @param from  - the TileEntity to output from
     * @return the amount of gas emitted
     */
    public static int emit(Set<EnumFacing> sides, FluidStack stack, TileEntity from) {
        if (stack == null || stack.amount == 0) {
            return 0;
        }
        //Fake that we have one target given we know that no sides will overlap
        // This allows us to have slightly better performance
        FluidHandlerTarget target = new FluidHandlerTarget(stack);
        EmitUtils.forEachSide(from.getWorld(), from.getPos(), sides, (acceptor, side) -> {
            //Insert to access side
            EnumFacing accessSide = side.getOpposite();
            //Collect cap
            CapabilityUtils.runIfCap(acceptor, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, accessSide, (handler) -> {
                if (canFill(handler, stack)) {
                    target.addHandler(accessSide, handler);
                }
            });
        });

        int curHandlers = target.getHandlers().size();
        if (curHandlers == 0) {
            return 0;
        }
        return EmitUtils.sendToAcceptors(Collections.singleton(target), curHandlers, stack.amount, stack);
    }

    public static FluidStack copy(FluidStack fluid, int amount) {
        FluidStack ret = fluid.copy();
        ret.amount = amount;
        return ret;
    }

    public static boolean canFill(IFluidHandler handler, FluidStack stack) {
        for (IFluidTankProperties props : handler.getTankProperties()) {
            if (props.canFillFluidType(stack)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canDrain(IFluidHandler handler, FluidStack stack) {
        for (IFluidTankProperties props : handler.getTankProperties()) {
            if (props.canDrainFluidType(stack)) {
                return true;
            }
        }
        return false;
    }
}
