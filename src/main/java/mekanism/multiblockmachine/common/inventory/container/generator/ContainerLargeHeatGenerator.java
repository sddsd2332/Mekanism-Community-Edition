package mekanism.multiblockmachine.common.inventory.container.generator;


import mekanism.common.inventory.slot.SlotEnergy;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeHeatGenerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLargeHeatGenerator extends ContainerFuelMultiBlockGenerator<TileEntityLargeHeatGenerator> {

    public ContainerLargeHeatGenerator(InventoryPlayer inventory, TileEntityLargeHeatGenerator generator) {
        super(inventory, generator);
    }

    @Override
    protected void addSlots() {
        addSlotToContainer(new Slot(tileEntity, 0, 17, 35));
        addSlotToContainer(new SlotEnergy.SlotCharge(tileEntity, 1, 143, 35));
    }

    @Override
    protected boolean tryFuel(ItemStack slotStack) {
        return tileEntity.getFuel(slotStack) > 0;
    }
}
