package mekanism.multiblockmachine.common.tile.machine;

import mekanism.common.base.IElectricMachine;
import mekanism.common.integration.computer.IComputerIntegration;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.recipe.outputs.MachineOutput;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine;

public abstract class TileEntityMultiblockBasicMachine<INPUT extends MachineInput<INPUT>, OUTPUT extends MachineOutput<OUTPUT>,
        RECIPE extends MachineRecipe<INPUT, OUTPUT, RECIPE>> extends TileEntityMultiblockOperationalMachine implements IElectricMachine<INPUT, OUTPUT, RECIPE>, IComputerIntegration {

    public RECIPE cachedRecipe = null;

    public TileEntityMultiblockBasicMachine(String soundPath, BlockStateMultiblockMachine.MultiblockMachineType type, int baseTicksRequired,int slot) {
        super("machine." + soundPath, type, baseTicksRequired,slot);
    }


}
