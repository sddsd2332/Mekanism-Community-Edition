package mekanism.multiblockmachine.common.fixers;

import mekanism.common.fixers.MekanismDataFixers.MekFixers;
import mekanism.common.fixers.TEFixer;
import mekanism.multiblockmachine.common.MekanismMultiblockMachine;

public class MultiblockMachineTEFixer extends TEFixer {


    public MultiblockMachineTEFixer(MekFixers fixer) {
        super(MekanismMultiblockMachine.MODID, fixer);
        putEntry("LargeWindGenerator","large_wind_generator");
        putEntry("LargeHeatGenerator","large_heat_generator");
        putEntry("LargeGasGenerator","large_gas_generator");
        putEntry("LargeElectrolyticSeparator","large_electrolytic_separator");
    }
}
