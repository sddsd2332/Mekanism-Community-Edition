package mekanism.multiblockmachine.common.inventory.container.generator;

import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import mekanism.common.FuelHandler;
import mekanism.common.inventory.slot.SlotEnergy;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeGasGenerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLargeGasGenerator extends ContainerFuelMultiBlockGenerator<TileEntityLargeGasGenerator> {

    public ContainerLargeGasGenerator(InventoryPlayer inventory, TileEntityLargeGasGenerator generator) {
        super(inventory, generator);
    }

    @Override
    protected boolean tryFuel(ItemStack slotStack) {
        if (slotStack.getItem() instanceof IGasItem) {
            GasStack gasStack = ((IGasItem) slotStack.getItem()).getGas(slotStack);
            return gasStack != null && FuelHandler.getFuel(gasStack.getGas()) != null;
        }
        return false;
    }

    @Override
    protected void addSlots() {
        addSlotToContainer(new Slot(tileEntity, 0, 17, 35));
        addSlotToContainer(new SlotEnergy.SlotCharge(tileEntity, 1, 143, 35));
    }
}
