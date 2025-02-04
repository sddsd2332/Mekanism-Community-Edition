package mekanism.common.inventory.slot;

import mekanism.common.inventory.container.slot.InsertableSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class HotBarSlot extends InsertableSlot {

    public HotBarSlot(InventoryPlayer inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

}
