package mekanism.common.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * TODO: Should use ReadWriteLock?
 */
public class FluidTankSync extends FluidTank {

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public FluidTankSync(final int capacity) {
        super(capacity);
    }

    public FluidTankSync(@Nullable final FluidStack fluidStack, final int capacity) {
        super(fluidStack, capacity);
    }

    public FluidTankSync(final Fluid fluid, final int amount, final int capacity) {
        super(fluid, amount, capacity);
    }

    @Override
    public void setFluid(@Nullable final FluidStack fluid) {
        try {
            rwLock.writeLock().lock();
            super.setFluid(fluid);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void setTileEntity(final TileEntity tile) {
        try {
            rwLock.writeLock().lock();
            super.setTileEntity(tile);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void setCapacity(final int capacity) {
        try {
            rwLock.writeLock().lock();
            super.setCapacity(capacity);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public int fill(final FluidStack resource, final boolean doFill) {
        try {
            rwLock.writeLock().lock();
            return super.fill(resource, doFill);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public int fillInternal(final FluidStack resource, final boolean doFill) {
        try {
            rwLock.writeLock().lock();
            return super.fillInternal(resource, doFill);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public FluidStack drain(final FluidStack resource, final boolean doDrain) {
        try {
            rwLock.writeLock().lock();
            return super.drain(resource, doDrain);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public FluidStack drain(final int maxDrain, final boolean doDrain) {
        try {
            rwLock.writeLock().lock();
            return super.drain(maxDrain, doDrain);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Nullable
    @Override
    public FluidStack drainInternal(final FluidStack resource, final boolean doDrain) {
        try {
            rwLock.writeLock().lock();
            return super.drainInternal(resource, doDrain);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Nullable
    @Override
    public FluidStack drainInternal(final int maxDrain, final boolean doDrain) {
        try {
            rwLock.writeLock().lock();
            return super.drainInternal(maxDrain, doDrain);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Nullable
    @Override
    public FluidStack getFluid() {
        try {
            rwLock.readLock().lock();
            return super.getFluid();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public int getFluidAmount() {
        try {
            rwLock.readLock().lock();
            return super.getFluidAmount();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public int getCapacity() {
        try {
            rwLock.readLock().lock();
            return super.getCapacity();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public FluidTankInfo getInfo() {
        try {
            rwLock.readLock().lock();
            return super.getInfo();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        try {
            rwLock.readLock().lock();
            return super.getTankProperties();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public ReadWriteLock getRwLock() {
        return rwLock;
    }

}
