package mekanism.common.inventory.container;

import mekanism.common.tile.prefab.TileEntityContainerBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerSPS extends ContainerMekanism<TileEntityContainerBlock>{


    public ContainerSPS(InventoryPlayer inventory, TileEntityContainerBlock tile) {
        super(tile, inventory);
    }

    @Override
    protected int getInventorYOffset() {
        return 89;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void addSlots() {

    }
}
