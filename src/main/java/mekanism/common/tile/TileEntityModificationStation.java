package mekanism.common.tile;

import mekanism.common.block.states.BlockStateMachine.*;
import mekanism.common.tile.prefab.TileEntityOperationalMachine;
import mekanism.common.util.NonNullListSynchronized;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

public class TileEntityModificationStation extends TileEntityOperationalMachine {

    public TileEntityModificationStation() {
        super("null", MachineType.MODIFICATION_STATION, 0, 40);
        inventory = NonNullListSynchronized.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public boolean sideIsConsumer(EnumFacing side) {
        return side == facing.getOpposite();
    }


    @Override
    public boolean renderUpdate() {
        return false;
    }

    @Override
    public boolean lightUpdate() {
        return false;
    }

    @NotNull
    @Override
    public int[] getSlotsForFace(@NotNull EnumFacing side) {
        return new int[0];
    }
}
