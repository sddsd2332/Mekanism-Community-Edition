package mekanism.multiblockmachine.common.fixers;

import mekanism.common.fixers.MekanismDataFixers.*;
import mekanism.common.fixers.TEFixer;
import mekanism.multiblockmachine.common.MekanismMultiblockMachine;

public class MultiblockMachineTEFixer extends TEFixer {


    public MultiblockMachineTEFixer(MekFixers fixer) {
        super(MekanismMultiblockMachine.MODID, fixer);
        putEntry("LargeWindGenerator","large_wind_Generator");
        putEntry("LargeHeatGenerator","large_heat_Generator");
    }
}
