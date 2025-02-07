package mekanism.common.content.gear.mekatool;

import mekanism.api.EnumColor;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.api.gear.config.IModuleConfigItem;
import mekanism.api.gear.config.ModuleConfigItemCreator;
import mekanism.api.gear.config.ModuleEnumData;
import mekanism.api.radial.IRadialDataHelper;
import mekanism.api.radial.RadialData;
import mekanism.api.radial.mode.BasicRadialMode;
import mekanism.api.radial.mode.IRadialMode;
import mekanism.api.radial.mode.NestedRadialMode;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.TextComponentGroup;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.item.ItemAtomicDisassembler.DisassemblerMode;
import mekanism.common.lib.radial.data.RadialDataHelper;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class ModuleVeinMiningUnit implements ICustomModule<ModuleVeinMiningUnit> {


    private static final IRadialDataHelper.BooleanRadialModes RADIAL_MODES = new IRadialDataHelper.BooleanRadialModes(
            new BasicRadialMode(MekanismLang.RADIAL_VEIN_NORMAL, DisassemblerMode.VEIN.icon(), EnumColor.AQUA),
            new BasicRadialMode(MekanismLang.RADIAL_VEIN_EXTENDED, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_RADIAL, "vein_extended.png"), EnumColor.PINK)
    );
    private static final RadialData<IRadialMode> RADIAL_DATA = RadialDataHelper.INSTANCE.booleanBasedData(Mekanism.rl("vein_mining_mode"), RADIAL_MODES);
    private static final NestedRadialMode NESTED_RADIAL_MODE = new NestedRadialMode(RADIAL_DATA, MekanismLang.RADIAL_VEIN, DisassemblerMode.VEIN.icon(), EnumColor.AQUA);

    private IModuleConfigItem<Boolean> extendedMode;
    private IModuleConfigItem<ExcavationRange> excavationRange;

    @Override
    public void init(IModule<ModuleVeinMiningUnit> module, ModuleConfigItemCreator configItemCreator) {
        extendedMode = configItemCreator.createDisableableConfigItem("extended_mode", MekanismLang.MODULE_EXTENDED_MODE, false, MekanismConfig.current().meka.mekaToolExtendedMining);
        excavationRange = configItemCreator.createConfigItem("excavation_range", MekanismLang.MODULE_EXCAVATION_RANGE, new ModuleEnumData<>(ExcavationRange.class, module.getInstalledCount() + 1, ExcavationRange.LOW));
    }

    @Override
    public void addRadialModes(IModule<ModuleVeinMiningUnit> module, @NotNull ItemStack stack, Consumer<NestedRadialMode> adder) {
        if (MekanismConfig.current().meka.mekaToolExtendedMining.val()) {
            adder.accept(NESTED_RADIAL_MODE);
        }
    }

    @Nullable
    @Override
    public <MODE extends IRadialMode> MODE getMode(IModule<ModuleVeinMiningUnit> module, ItemStack stack, RadialData<MODE> radialData) {
        if (radialData == RADIAL_DATA && MekanismConfig.current().meka.mekaToolExtendedMining.val()) {
            return (MODE) RADIAL_MODES.get(isExtended());
        }
        return null;
    }

    @Override
    public <MODE extends IRadialMode> boolean setMode(IModule<ModuleVeinMiningUnit> module, EntityPlayer player, ItemStack stack, RadialData<MODE> radialData, MODE mode) {
        if (radialData == RADIAL_DATA && MekanismConfig.current().meka.mekaToolExtendedMining.val()) {
            boolean extended = mode == RADIAL_MODES.trueMode();
            if (isExtended() != extended) {
                extendedMode.set(extended);
            }
        }
        return false;
    }

    @Nullable
    @Override
    public ITextComponent getModeScrollComponent(IModule<ModuleVeinMiningUnit> module, ItemStack stack) {
        if (isExtended()) {
            return MekanismLang.RADIAL_VEIN_EXTENDED.translateColored(EnumColor.PINK);
        }
        return MekanismLang.RADIAL_VEIN_NORMAL.translateColored(EnumColor.AQUA);
    }

    @Override
    public void changeMode(IModule<ModuleVeinMiningUnit> module, EntityPlayer player, ItemStack stack, int shift, boolean displayChangeMessage) {
        if (Math.abs(shift) % 2 == 1) {
            //We are changing by an odd amount, so toggle the mode
            boolean newState = !isExtended();
            extendedMode.set(newState);
            if (displayChangeMessage) {
                player.sendMessage(new TextComponentGroup(TextFormatting.GRAY).string(Mekanism.LOG_TAG, TextFormatting.DARK_BLUE).string(" ").translation(MekanismLang.MODULE_MODE_CHANGE.getTranslationKey()).translation(MekanismLang.MODULE_EXTENDED_MODE.getTranslationKey(), LangUtils.onOffColoured(newState)));
            }
        }
    }

    public boolean isExtended() {
        return extendedMode.get();
    }

    public int getExcavationRange() {
        return excavationRange.get().getRange();
    }

    public static Set<BlockPos> findPositions(IBlockState state, BlockPos location, World world, int maxRange) {
        Set<BlockPos> found = new LinkedHashSet<>();
        Set<BlockPos> openSet = new LinkedHashSet<>();
        openSet.add(location);
        Block startBlock = state.getBlock();
        int maxCount = MekanismConfig.current().general.disassemblerMiningCount.val() - 1;
        while (!openSet.isEmpty()) {
            BlockPos blockPos = openSet.iterator().next();
            found.add(blockPos);
            openSet.remove(blockPos);
            if (found.size() > maxCount) {
                return found;
            }
            for (BlockPos pos : BlockPos.getAllInBoxMutable(blockPos.add(-1, -1, -1), blockPos.add(1, 1, 1))) {
                //We can check contains as mutable
                if (!found.contains(pos) && (maxRange == -1 || WorldUtils.distanceBetween(location, pos) <= maxRange)) {
                    Optional<IBlockState> blockState = WorldUtils.getBlockState(world, pos);
                    if (blockState.isPresent() && startBlock == blockState.get().getBlock()) {
                        if (!openSet.contains(pos)) {
                            openSet.add(pos.toImmutable());
                            //渲染？
                        }
                    }
                }
            }
        }
        return found;
    }


    @Override
    public void addHUDStrings(IModule<ModuleVeinMiningUnit> module, EntityPlayer player, Consumer<String> hudStringAdder) {
        //Only add hud string for extended vein mining if enabled in config
        if (module.isEnabled() && MekanismConfig.current().meka.mekaToolExtendedMining.getAsBoolean()) {
            hudStringAdder.accept(MekanismLang.MODULE_EXTENDED_ENABLED.translateColored(EnumColor.DARK_GREY).getFormattedText() + (isExtended() ? EnumColor.BRIGHT_GREEN : EnumColor.DARK_RED) + (isExtended() ? MekanismLang.MODULE_ENABLED_LOWER.getTranslationKey() : MekanismLang.MODULE_DISABLED_LOWER.getTranslationKey()));
        }
    }

    public enum ExcavationRange implements IHasTextComponent {
        OFF(0),
        LOW(2),
        MED(4),
        HIGH(6),
        EXTREME(8);

        private final int range;
        private final ITextComponent label;

        ExcavationRange(int range) {
            this.range = range;
            this.label = new TextComponentGroup().getString(Integer.toString(range));
        }

        @Override
        public ITextComponent getTextComponent() {
            return label;
        }

        public int getRange() {
            return range;
        }
    }
}
