package mekanism.common.content.gear.mekatool;

import mekanism.api.EnumColor;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.api.gear.config.IModuleConfigItem;
import mekanism.api.gear.config.ModuleConfigItemCreator;
import mekanism.api.gear.config.ModuleEnumData;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.TextComponentGroup;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class ModuleVeinMiningUnit implements ICustomModule<ModuleVeinMiningUnit> {

    private IModuleConfigItem<Boolean> extendedMode;
    private IModuleConfigItem<ExcavationRange> excavationRange;

    @Override
    public void init(IModule<ModuleVeinMiningUnit> module, ModuleConfigItemCreator configItemCreator) {
        extendedMode = configItemCreator.createDisableableConfigItem("extended_mode", MekanismLang.MODULE_EXTENDED_MODE, false, MekanismConfig.current().meka.mekaToolExtendedMining);
        excavationRange = configItemCreator.createConfigItem("excavation_range", MekanismLang.MODULE_EXCAVATION_RANGE, new ModuleEnumData<>(ExcavationRange.class, module.getInstalledCount() + 1, ExcavationRange.LOW));
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
