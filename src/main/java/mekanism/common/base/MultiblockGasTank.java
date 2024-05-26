package mekanism.common.base;

import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTankInfo;
import mekanism.api.gas.IGasTank;
import mekanism.common.tile.multiblock.TileEntityMultiblock;
import mekanism.common.util.MekanismUtils;

import javax.annotation.Nullable;

public abstract class MultiblockGasTank<MULTIBLOCK extends TileEntityMultiblock> implements IGasTank {

    protected final MULTIBLOCK multiblock;

    protected MultiblockGasTank(MULTIBLOCK multiblock) {
        this.multiblock = multiblock;
    }

    public abstract void setGas(GasStack stack);

    protected abstract void updateValveData();

    @Override
    public int input(@Nullable GasStack resource, boolean doFill) {
        if (multiblock.structure != null && !multiblock.getWorld().isRemote) {
            if (resource == null) {
                return 0;
            }
            GasStack gasStack = getGas();
            if (gasStack != null && !gasStack.isGasEqual(resource)) {
                return 0;
            }
            if (gasStack == null) {
                if (resource.amount <= getMaxGas()) {
                    if (doFill) {
                        setGas(gasStack = resource.copy());
                        if (resource.amount > 0) {
                            MekanismUtils.saveChunk(multiblock);
                            updateValveData();
                        }
                    }
                    return resource.amount;
                }
                if (doFill) {
                    setGas(gasStack = resource.copy());
                    gasStack.amount = getMaxGas();
                    if (getMaxGas() > 0) {
                        MekanismUtils.saveChunk(multiblock);
                        updateValveData();
                    }
                }
                return getMaxGas();
            }
            int needed = getMaxGas() - gasStack.amount;
            if (resource.amount <= needed) {
                if (doFill) {
                    gasStack.amount += resource.amount;
                    if (resource.amount > 0) {
                        MekanismUtils.saveChunk(multiblock);
                        updateValveData();
                    }
                }
                return resource.amount;
            }
            if (doFill) {
                gasStack.amount = getMaxGas();
                if (needed > 0) {
                    MekanismUtils.saveChunk(multiblock);
                    updateValveData();
                }
            }
            return needed;
        }
        return 0;
    }

    @Override
    public GasStack output(int maxDrain, boolean doDrain) {
        if (multiblock.structure != null && !multiblock.getWorld().isRemote) {
            GasStack gasStack = getGas();
            if (gasStack == null || gasStack.amount <= 0) {
                return null;
            }
            int used = maxDrain;
            if (gasStack.amount < used) {
                used = gasStack.amount;
            }
            if (doDrain) {
                gasStack.amount -= used;
            }
            GasStack drained = new GasStack(gasStack.getGas(), used);
            if (gasStack.amount <= 0) {
                setGas(null);
            }
            if (drained.amount > 0 && doDrain) {
                MekanismUtils.saveChunk(multiblock);
                multiblock.sendPacketToRenderer();
            }
            return drained;
        }
        return null;
    }

    @Override
    public int getGasAmount() {
        if (multiblock.structure != null) {
            GasStack fluid = getGas();
            return fluid == null ? 0 : fluid.amount;
        }
        return 0;
    }

    @Override
    public GasTankInfo getInfo() {
        return new GasTankInfo() {
            @org.jetbrains.annotations.Nullable
            @Override
            public GasStack getGas() {
                return MultiblockGasTank.this.getGas();
            }

            @Override
            public int getStored() {
                return MultiblockGasTank.this.getGasAmount();
            }

            @Override
            public int getMaxGas() {
                return MultiblockGasTank.this.getMaxGas();
            }
        };
    }
}
