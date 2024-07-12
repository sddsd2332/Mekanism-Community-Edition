package mekanism.common.config;

import mekanism.common.config.options.DoubleOption;
import mekanism.common.config.options.FloatOption;
import mekanism.common.config.options.IntOption;

public class WeaponsConfig  extends BaseConfig {

    public final IntOption mekaTanaBaseDamage = new IntOption(this,"mekatana","baseDamage",50,"Base damage of the Meka-Tana, multiply it with Attack Amplification Units.");
    public final FloatOption mekaTanaAttackSpeed = new FloatOption(this,"mekatana","attackSpeed",-2.4F,"Attack speed of the Meka-Tana.",-4,100);
    public final DoubleOption mekaTanaEnergyUsage = new DoubleOption(this,"mekatana","energyUsage",625000D,"Cost in Joules of using the Meka-Tana to deal damage.");
    //public final DoubleOption mekaTanaTeleportUsage = new DoubleOption(this,"mekatana","teleportEnergyUsage",5000D,"Cost in Joules of using the Meka-Tana to teleport 10 blocks.");
   // public final IntOption mekaTanaMaxTeleportReach= new IntOption(this,"mekatana","maxTeleportReach",100,"Maximum distance a player can teleport with the Meka-Tana.",3,1024);
    public final DoubleOption mekaTanaBaseEnergyCapacity = new DoubleOption(this,"mekatana","baseEnergyCapacity",16000000D,"Base energy capacity of the Meka-Tana.");
    public final DoubleOption mekaTanaBaseChargeRate = new DoubleOption(this,"mekatana","baseChargeRate",350000D,"Base charge rate of the Meka-Tana.");

    public final IntOption mekaBowBaseDamage = new IntOption(this,"mekabow","baseDamage",50,"Attention: The final damage of Meka-Bow is based on how fast the arrow is going when hits, multiply it with Attack Amplification Units.");
    public final DoubleOption mekaBowEnergyUsage = new DoubleOption(this,"mekabow","energyUsage",625000D,"Cost in Joules of using the Meka-Bow.");
    public final DoubleOption mekaBowBaseEnergyCapacity = new DoubleOption(this,"mekabow","baseEnergyCapacity",16000000D,"Base energy capacity of Meka-Bow.");
    public final DoubleOption mekaBowBaseChargeRate = new DoubleOption(this,"mekabow","baseChargeRate",350000D,"Base charge rate of Meka-Bow.");
}
