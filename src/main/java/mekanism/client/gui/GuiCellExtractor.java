package mekanism.client.gui;

import mekanism.common.recipe.machines.CellExtractorRecipe;
import mekanism.common.tile.prefab.TileEntityChanceMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCellExtractor extends GuiChanceMachine<CellExtractorRecipe> {

    public GuiCellExtractor(InventoryPlayer inventory, TileEntityChanceMachine<CellExtractorRecipe> tile) {
        super(inventory, tile);
    }
}
