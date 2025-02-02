package mekanism.common.config;

import io.netty.buffer.ByteBuf;
import mekanism.common.config.options.BooleanOption;
import mekanism.common.config.options.DoubleOption;
import mekanism.common.config.options.FloatOption;
import mekanism.common.config.options.IntOption;

/**
 * Created by Thiakil on 15/03/2019.
 */
public class ClientConfig extends BaseConfig {

    public final BooleanOption enablePlayerSounds = new BooleanOption(this, "client", "EnablePlayerSounds", true,
            "Play sounds for Jetpack/Gas Mask/Flamethrower (all players).");

    public final BooleanOption enableMachineSounds = new BooleanOption(this, "client", "EnableMachineSounds", true,
            "If enabled machines play their sounds while running.");

    public final BooleanOption holidays = new BooleanOption(this, "client", "Holidays", true,
            "Christmas/New Years greetings in chat.");

    public final DoubleOption baseSoundVolume = new DoubleOption(this, "client", "SoundVolume", 1D,
            "Adjust Mekanism sounds' base volume. < 1 is softer, higher is louder.");

    public final BooleanOption machineEffects = new BooleanOption(this, "client", "MachineEffects", true,
            "Show particles when machines active.");

    public final BooleanOption enableAmbientLighting = new BooleanOption(this, "client", "EnableAmbientLighting", true,
            "Should active machines produce block light.");

    public final IntOption ambientLightingLevel = new IntOption(this, "client", "AmbientLightingLevel", 15,
            "How much light to produce if ambient lighting is enabled.", 1, 15);

    public final BooleanOption opaqueTransmitters = new BooleanOption(this, "client", "OpaqueTransmitterRender", false,
            "If true, don't render Cables/Pipes/Tubes as transparent and don't render their contents.");

    public final BooleanOption allowModeScroll = new BooleanOption(this, "client", "allowModeScroll", true,
            "Allow sneak + scroll to change item modes.");

    public final BooleanOption enableMultiblockFormationParticles = new BooleanOption(this, "client", "MultiblockFormParticles", true,
            "Set to false to prevent particle spam when loading multiblocks (notification message will display instead).");

    public final BooleanOption alignHUDLeft = new BooleanOption(this, "client", "AlignHUDLeft", true,
            "Align HUD with left (if true) or right (if false)");

    public final BooleanOption enableHUD = new BooleanOption(this, "client", "enableHUD", true,
            "Enable item information HUD during gameplay");

    public final IntOption AllMekGuiBg = new IntOption(this,"client","AllMekGuiBg",0xFFFFFFFF,
            "All mekanism GUI background colors");

    public final FloatOption hudScale = new FloatOption(this,"client","hudScale",0.6F,"Scale of the text displayed on the HUD.",0.25F,1F);

    public final IntOption hudX = new IntOption(this,"client","hudX",0,"The HUD is offset to the left and right of the screen.");

    public final IntOption hudY = new IntOption(this,"client","hudY",0,"The HUD is offset up and down the screen.");

    public final BooleanOption enableFirstPersonMekaSuitArms = new BooleanOption(this,"client","enableFirstPersonMekaSuitArms",true,"Whether to enable the first-person arm of MekaSuitArms ? (It is currently in WIP status)");

    public final BooleanOption enableBloom = new BooleanOption(this, "client", "enableBloom", false,
            "Enable the glow texture of MeKCEu, which may cause a performance penalty. (GTCEu or Lumenized installation required)");

    public final DoubleOption largeWindGeneratorMaxRenderDistanceSquared = new DoubleOption(this,"client","largeWindGeneratorMaxRenderDistanceSquared",16384.0D);

    public final BooleanOption largeWindGeneratorisGlobalRenderer = new BooleanOption(this,"client","largeWindGeneratorisGlobalRenderer",true);

    public final BooleanOption GasTOP = new BooleanOption(this, "client", "GasTop", true, "If true, the shutdown requires The One Probe item to sneak up to display the amount inside the gas tank");

    public final BooleanOption windGeneratorItem = new BooleanOption(this, "client", "WindGenerator", true, "Wind turbine blade rotation [item]");

    public final BooleanOption windGeneratorRotating = new BooleanOption(this, "client", "windGeneratorRotating", true, "Wind turbine blade rotation [block]");

    public final FloatOption hudJitter = new FloatOption(this,"client","hudJitter",6F,"Visual jitter of MekaSuit HUD, seen when moving the player's head. Bigger value = more jitter.",1F,100F);

    public final FloatOption hudOpacity = new FloatOption(this,"client","hudOpacity",0.4F,"Opacity of HUD used by MekaSuit.",0F,1F);

    public final BooleanOption largeWindGeneratorRending = new BooleanOption(this,"client","largeWindGeneratorRending",true,"Whether to render a model of a large wind turbine，If false, no rendering is done, which may optimize the fps");

    public final BooleanOption hudCompassEnabled= new BooleanOption(this,"client","mekaSuitHelmetCompass",true,"Display a fancy compass when the MekaSuit is worn.");

    public final IntOption hudColor = new IntOption(this,"client","hudColor",0x40F5F0, "Color (RGB) of HUD used by MekaSuit.",0,0xFFFFFF);

    public final IntOption hudWarningColor = new IntOption(this,"client","hudWarningColor",0xFFDD4F,"Color (RGB) of warning HUD elements used by MekaSuit.",0,0xFFFFFF);

    public final IntOption hudDangerColor = new IntOption(this,"client","hudDangerColor",0xFF383C,"Color (RGB) of danger HUD elements used by MekaSuit.",0,0xFFFFFF);

    public final IntOption energyColor = new IntOption(this,"client","energyColor",0x3CFE9A,"Color of energy in item durability display.");

    @Override
    public void write(ByteBuf config) {
        throw new UnsupportedOperationException("Client config shouldn't be synced");
    }

    @Override
    public void read(ByteBuf config) {
        throw new UnsupportedOperationException("Client config shouldn't be synced");
    }
}
