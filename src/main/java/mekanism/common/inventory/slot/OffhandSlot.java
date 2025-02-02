package mekanism.common.inventory.slot;

import mekanism.common.inventory.container.slot.InsertableSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OffhandSlot extends InsertableSlot {

    public OffhandSlot(InventoryPlayer inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @SideOnly(Side.CLIENT)
    public String getSlotTexture() {
        return "minecraft:items/empty_armor_slot_shield";
    }
}


