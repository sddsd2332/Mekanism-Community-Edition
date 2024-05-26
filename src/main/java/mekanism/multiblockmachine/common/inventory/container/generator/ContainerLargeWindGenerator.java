package mekanism.multiblockmachine.common.inventory.container.generator;

import mekanism.common.inventory.slot.SlotEnergy;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeWindGenerator;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerLargeWindGenerator extends ContainerPassiveMultiBlockGenerator<TileEntityLargeWindGenerator> {

    public ContainerLargeWindGenerator(InventoryPlayer inventory, TileEntityLargeWindGenerator generator) {
        super(inventory, generator);
    }

    @Override
    protected int getInventorYOffset() {
        return super.getInventorYOffset() + 9;
    }


    @Override
    protected void addSlots() {
        addSlotToContainer(new SlotEnergy.SlotCharge(tileEntity, 0, 143, 35 + 2));
    }
}
