package mekanism.client.gui;

import mekanism.common.recipe.machines.CellSeparatorRecipe;
import mekanism.common.tile.prefab.TileEntityChanceMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCellSeparator extends GuiChanceMachine<CellSeparatorRecipe> {

    public GuiCellSeparator(InventoryPlayer inventory, TileEntityChanceMachine<CellSeparatorRecipe> tile) {
        super(inventory, tile);
    }
}
