package mekanism.common.content.tank;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mekanism.api.Coord4D;
import mekanism.api.gas.GasStack;
import mekanism.common.multiblock.SynchronizedData;
import mekanism.common.util.FluidContainerUtils.ContainerEditMode;
import mekanism.common.util.NonNullListSynchronized;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.Set;

public class SynchronizedTankData extends SynchronizedData<SynchronizedTankData> {

    public FluidStack fluidStored;

    public GasStack gasstored;

    /**
     * For use by rendering segment
     */
    public FluidStack prevFluid;
    public GasStack prevGas;
    public int prevFluidStage = 0;
    public int prevGasStage = 0;
    public ContainerEditMode editMode = ContainerEditMode.BOTH;

    public NonNullListSynchronized<ItemStack> inventory = NonNullListSynchronized.withSize(2, ItemStack.EMPTY);

    public Set<ValveData> valves = new ObjectOpenHashSet<>();

    public boolean needsRenderUpdate() {
        if ((fluidStored == null && prevFluid != null) || (fluidStored != null && prevFluid == null)) {
            return true;
        } else if ((gasstored == null && prevGas != null) || (gasstored != null && prevGas == null)){
            return true;
        }
        if (fluidStored != null) {
            int totalStage = (volHeight - 2) * (TankUpdateProtocol.FLUID_PER_TANK / 100);
            int currentStage = (int) ((fluidStored.amount / (float) (volume * TankUpdateProtocol.FLUID_PER_TANK)) * totalStage);
            boolean stageChanged = currentStage != prevFluidStage;
            prevFluidStage = currentStage;
            return (fluidStored.getFluid() != prevFluid.getFluid()) || stageChanged;
        } else if (gasstored != null){
            int totalStage = (volHeight - 2) * (TankUpdateProtocol.FLUID_PER_TANK / 100);
            int currentStage = (int) ((gasstored.amount / (float) (volume * TankUpdateProtocol.FLUID_PER_TANK)) * totalStage);
            boolean stageChanged = currentStage != prevGasStage;
            prevGasStage = currentStage;
            return (gasstored.getGas() != prevGas.getGas()) || stageChanged;
        }
        return false;
    }

    @Override
    public NonNullListSynchronized<ItemStack> getInventory() {
        return inventory;
    }

    public static class ValveData {

        public EnumFacing side;
        public Coord4D location;

        public boolean prevActive;
        public int activeTicks;

        public void onTransfer() {
            activeTicks = 30;
        }

        @Override
        public int hashCode() {
            int code = 1;
            code = 31 * code + side.ordinal();
            code = 31 * code + location.hashCode();
            return code;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ValveData && ((ValveData) obj).side == side && ((ValveData) obj).location.equals(location);
        }
    }
}
