package mekanism.client.gui;

import mekanism.common.recipe.machines.SawmillRecipe;
import mekanism.common.tile.prefab.TileEntityChanceMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPrecisionSawmill extends GuiChanceMachine<SawmillRecipe> {

    public GuiPrecisionSawmill(InventoryPlayer inventory, TileEntityChanceMachine<SawmillRecipe> tile) {
        super(inventory, tile);
    }

}
