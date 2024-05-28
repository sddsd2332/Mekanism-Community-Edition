package mekanism.smartfactory.client.gui;

import mekanism.common.recipe.machines.SmeltingRecipe;
import mekanism.smartfactory.common.tile.prefab.TileEntityAEElectricMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAEEnergizedSmelter  extends  GuiAEElectricMachine<SmeltingRecipe> {

    public GuiAEEnergizedSmelter(InventoryPlayer inventory, TileEntityAEElectricMachine<SmeltingRecipe> tile){
        super(inventory, tile);
    }
}
