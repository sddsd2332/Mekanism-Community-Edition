package mekanism.common.config;

import io.netty.buffer.ByteBuf;
import mekanism.common.config.options.BooleanOption;
import mekanism.common.config.options.DoubleOption;
import mekanism.common.config.options.FloatOption;
import mekanism.common.config.options.IntOption;
import net.minecraftforge.common.config.Configuration;


public class MEKCEConfig extends BaseConfig {

    public final BooleanOption EnableDiamondCompat = new BooleanOption(this, "mekce", "EnableDiamondCompat", true,
            "Allows oredict'ed diamonds to be used in the enrichment chamber, like synthetic diamonds.");

    public final BooleanOption EnablePoorOresCompat = new BooleanOption(this, "mekce", "EnablePoorOresCompat", true,
            "Allows poor ores from railcraft to be used in the purification chamber and gives one clump ie one ingot.");

    public final BooleanOption EnableQuartzCompat = new BooleanOption(this, "mekce", "EnableQuartzCompat", true,
            "Allows quartz dust to be enriched into quartz Also allows quartz ore to be enriched into quartz dust");

    public final BooleanOption EnableSiliconCompat = new BooleanOption(this, "mekce", "EnableSiliconCompat", false,
            "When a mod that adds silicon (galacticraft, enderio, projectred and ae2) is detected, recipe for control circuit is changed from using iron to silicon in the metalurgic infuser");

    //public final BooleanOption enableBoPProgression = new BooleanOption(this, "mekce", "enableBoPProgression", true,
    //        "when true and biome's o plenty is installed atomic alloy is made by using ender instead of obsidian");

    //public final BooleanOption EnableSingleUseCardboxes = new BooleanOption(this, "mekce", "EnableSingleUseCardboxes", true,
    //        "This allows to force single use on cardboxes or not");

    public final BooleanOption ShowHiddenGas = new BooleanOption(this, "mekce", "ShowHiddenGases", true, "Displays hidden gas in creative gas tanks, which is invalid if PrefilledGasTanks is not enabled");

    public final BooleanOption EmptyToCreateBin = new BooleanOption(this, "mekce", "EmptytoCreateBin", false, "Let Configurator clear Create Bin");

    public final BooleanOption EmptyToCreateGasTank = new BooleanOption(this, "mekce", "EmptyToCreateGasTank", false, "Let Configurator clear Create Gas Tank");

    public final BooleanOption EmptytoCreateFluidTank = new BooleanOption(this, "mekce", "EmptytoCreateFluidTank", false, "Let Configurator clear Create Fluid Tank");

    public final BooleanOption RotaryCondensentratorAuto = new BooleanOption(this, "mekce", "RotaryCondensentratorAuto", false, "Turn off automatic change gas and fluid ejection mode in Rotary Condensentrator?");

    public final IntOption ItemEjectionDelay = new IntOption(this, "mekce", "ItemEjectionDelay", 10, "Every how many ticks pop up an item, the default is 10 ticks", 1, Integer.MAX_VALUE);

    public final BooleanOption ItemsEjectWithoutDelay = new BooleanOption(this, "mekce", "ItemsEjectWithoutDelay", false, "If true, the Item Ejection Delay is ignored");

    public final IntOption EjectionFailureDelay = new IntOption(this, "mekce", "EjectionFailureDelay", 20, "How long to wait and retry if the machine is unable to export its own gas or fluid. Helps optimize performance.", 0, 100);

    public final IntOption MAXSpeedUpgrade = new IntOption(this, "mekce", "MAXSpeedUpgrade", 8,
            "The maximum number of speed upgrades that can be installed", 1, Integer.MAX_VALUE).setRequiresGameRestart(true);

    public final IntOption MAXEnergyUpgrade = new IntOption(this, "mekce", "MAXEnergyUpgrade", 8,
            "The maximum number of energy upgrades that can be installed", 1, Integer.MAX_VALUE).setRequiresGameRestart(true);

    public final IntOption MAXGasUpgrade = new IntOption(this, "mekce", "MAXGasUpgrade", 8,
            "The maximum number of gas upgrades that can be installed", 1, Integer.MAX_VALUE).setRequiresGameRestart(true);

    public final IntOption MAXMufflingUpgrade = new IntOption(this, "mekce", "MAXMufflingUpgrade", 4,
            "The maximum number of muffling upgrades that can be installed", 1, Integer.MAX_VALUE).setRequiresGameRestart(true);

    public final IntOption MAXSpeedUpgradeSize = new IntOption(this, "mekce", "MAXSpeedUpgradeSize", 8,
            "The maximum number of stacks that can be stacked for speed upgrades", 1, 64).setRequiresGameRestart(true);

    public final IntOption MAXEnergyUpgradeSize = new IntOption(this, "mekce", "MAXEnergyUpgradeSize", 8,
            "The maximum number of stacks that can be stacked for energy upgrades", 1, 64).setRequiresGameRestart(true);

    public final IntOption MAXGasUpgradeSize = new IntOption(this, "mekce", "MAXGasUpgradeSize", 8,
            "The maximum number of stacks that can be stacked for gas upgrades", 1, 64).setRequiresGameRestart(true);

    public final IntOption MAXMufflingUpgradeSize = new IntOption(this, "mekce", "MAXMufflingUpgradeSize", 4,
            "The maximum number of stacks that can be stacked for muffling upgrades", 1, 64).setRequiresGameRestart(true);

    public final IntOption MAXspeedmachines = new IntOption(this, "mekce", "Maximumspeedmultiplierforsomemachines", 256,
            "Modify the maximum speed multiplier for some machines", 1, Integer.MAX_VALUE);

    public final BooleanOption EnableBuff = new BooleanOption(this, "mekce", "EnableBuff", false, "If true, a buff effect will be added to the player each time Canteen is used");

    public final DoubleOption seed = new DoubleOption(this, "mekce", "seed", 1D,
            "When turning seeds into crops, the chance to produce seeds for each operation in Organic Farm").setRequiresGameRestart(true);

    public final DoubleOption log = new DoubleOption(this, "mekce", "log", 1D,
            "When turning seeds into crops, the opportunity to produce log in each operation in Organic Farm.").setRequiresGameRestart(true);

    public final BooleanOption EnableGlassInThermal = new BooleanOption(this, "mekce", "EnableGlassInThermal", false,
            "Enabling Structural Glass for the Thermal Evaporation Plant may contain some issues");

    public final BooleanOption EnableConfiguratorWrench = new BooleanOption(this, "mekce", "EnableConfiguratorWrench", true, "Enable the configurator's wrench mode");

    public final IntOption MAXTierSize = new IntOption(this, "mekce", "MAXTierSize", 8,
            "The maximum number of stacks that can be stacked in Tier Instale", 1, 64).setRequiresGameRestart(true);

    public final BooleanOption EnableUpgradeConfigure = new BooleanOption(this, "mekce", "EnableUpgradeConfigure", false, "Enable an upgrade similar to IC2");


    public final IntOption MAXThreadUpgrade = new IntOption(this, "mekce", "MAXThreadUpgrade", 8,
            "The maximum number of thread upgrades that can be installed", 1, Integer.MAX_VALUE).setRequiresGameRestart(true);

    public final IntOption MAXThreadUpgradeSize = new IntOption(this, "mekce", "MAXThreadUpgradeSize", 8,
            "The maximum number of stacks that can be stacked for thread upgrades", 1, 64).setRequiresGameRestart(true);

    public final BooleanOption BinRecipeClosed = new BooleanOption(this, "mekce", "BinRecipeClosed", false, "Turn off the BIN synthesis recipe").setRequiresGameRestart(true);

    public final BooleanOption BinRecipeRemovesItem = new BooleanOption(this, "mekce", "BinRecipeRemovesItem", false, "Close Bin to remove items").setRequiresGameRestart(true);
    public final BooleanOption MekAsuitOverloadProtection = new BooleanOption(this, "mekce", "MekAsuitOverloadProtection", true, "Allows MekAsuit to intercept direct setHealth with Emergency Rescue installed");

    public final IntOption MaximumEjectionDelay = new IntOption(this, "mekce", "MaximumEjectionDelay", 40, "Maximum ejection delay for gases and fluids");
    public final IntOption TankChangeSpeed = new IntOption(this, "mekce", "TankChangeSpeed", 20, "Record changes in the contents of the tank,Affects the ejection speed");
    public final FloatOption LowEjectionThreshold = new FloatOption(this, "mekce", "LowEjectionThreshold", 0.5F, "Low ejection thresholds for gases and fluids, below which the MaximumEjectionDelay is waited for to be met before output", 0, 1);
    public final FloatOption HighEjectionThreshold = new FloatOption(this, "mekce", "HighEjectionThreshold", 0.85F, "High ejection thresholds for gases and fluids, above which they are immediately output", 0, 1);
    public final BooleanOption PlasticWrench = new BooleanOption(this,"mekce","PlasticWrench",false,"If true, allow the plastic to fall through the wrench");
    @Override
    public void load(Configuration config) {
        super.load(config);
        validate();
    }

    @Override
    public void read(ByteBuf config) {
        super.read(config);
        validate();
    }

    private void validate() {
        //ensure HighEjectionThreshold is > LowEjectionThreshold
        HighEjectionThreshold.set(Math.max(LowEjectionThreshold.val(), HighEjectionThreshold.val()));
    }
}
