package mekanism.common;

import mekanism.api.EnumColor;
import mekanism.api.gas.GasTank;
import mekanism.common.base.ITankManager;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.util.LangUtils;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import java.util.ArrayList;
import java.util.List;

public class SideData {

    /**
     * The color of this SideData
     */
    public EnumColor color;

    /**
     * The name of this SideData
     */
    public String name;

    /**
     * int[] of available side slots, can be used for items, gases, or items
     */
    public int[] availableSlots;
    public boolean[] allowExtractionSlot;

    /**
     * IOState representing this SideData
     */
    public IOState ioState;


    public SideData(int[] slots, boolean[] extractionSlot) {
        this(DataType.INPUT_OUTPUT, slots,extractionSlot);
    }

    public SideData(DataType dataType, int[] slots, boolean[] extractionSlot) {
        this(dataType, slots);
        allowExtractionSlot = extractionSlot;
    }

    public SideData(DataType dataType, int[] slots) {
        name = dataType.getName();
        color = dataType.getColor();
        availableSlots = slots;
    }

    public SideData(DataType dataType, IOState state) {
        name = dataType.getName();
        color = dataType.getColor();
        ioState = state;
    }

    public String localize() {
        return LangUtils.localize("sideData." + name);
    }

    public boolean hasSlot(int... slots) {
        for (int i : availableSlots) {
            for (int slot : slots) {
                if (i == slot) {
                    return true;
                }
            }
        }
        return false;
    }


    public FluidTankInfo[] getFluidTankInfo(ITankManager manager) {
        Object[] tanks = manager.getTanks();
        List<FluidTankInfo> infos = new ArrayList<>();
        if (tanks == null) {
            return infos.toArray(new FluidTankInfo[]{});
        }
        for (int slot : availableSlots) {
            if (slot <= tanks.length - 1 && tanks[slot] instanceof IFluidTank tank) {
                infos.add(tank.getInfo());
            }
        }
        return infos.toArray(new FluidTankInfo[]{});
    }

    public GasTank getGasTank(ITankManager manager) {
        Object[] tanks = manager.getTanks();
        if (tanks == null || tanks.length < 1 || !(tanks[0] instanceof GasTank gasTank)) {
            return null;
        }
        return gasTank;
    }

    public enum IOState {
        INPUT,
        OUTPUT,
        OFF
    }
}
