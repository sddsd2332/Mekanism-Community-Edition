package mekanism.common.content.gear.mekatool;

import mekanism.api.EnumColor;
import mekanism.api.IIncrementalEnum;
import mekanism.api.math.MathUtils;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.TextComponentGroup;
import mekanism.common.MekanismLang;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.ModuleConfigItem.EnumData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class ModuleExcavationEscalationUnit extends Module {

    private ModuleConfigItem<ExcavationMode> excavationMode;

    @Override
    public void init() {
        excavationMode = addConfigItem(new ModuleConfigItem<>(this, "excavation_mode", MekanismLang.MODULE_EFFICIENCY,
                new EnumData<>(ExcavationMode.class, getInstalledCount() + 2), ExcavationMode.NORMAL));
    }

    @Override
    public void changeMode(EntityPlayer player, ItemStack stack, int shift, boolean displayChangeMessage) {
        if (isEnabled()) {
            ExcavationMode newMode = excavationMode.get().adjust(shift, v -> v.ordinal() < getInstalledCount() + 2);
            if (excavationMode.get() != newMode) {
                excavationMode.set(newMode,null);
                if (displayChangeMessage) {
                    displayModeChange(player, MekanismLang.MODULE_EFFICIENCY.translate(), newMode);
                }
            }
        }
    }

    @Override
    public void addHUDStrings(EntityPlayer player, List<String> hudStringAdder) {
        if (isEnabled()) {
            hudStringAdder.add(MekanismLang.DISASSEMBLER_EFFICIENCY.translateColored(EnumColor.DARK_GREY, EnumColor.INDIGO, excavationMode.get().getEfficiency()).getFormattedText());
        }
    }

    public float getEfficiency() {
        return excavationMode.get().getEfficiency();
    }

    public enum ExcavationMode implements IIncrementalEnum<ExcavationMode>, IHasTextComponent {
        OFF(0),
        SLOW(4),
        NORMAL(16),
        FAST(32),
        SUPER_FAST(64),
        EXTREME(128);

        private static final ExcavationMode[] MODES = values();

        private final ITextComponent label;
        private final int efficiency;

        ExcavationMode(int efficiency) {
            this.efficiency = efficiency;
            this.label = new TextComponentGroup().getString(Integer.toString(efficiency));
        }

        @Nonnull
        @Override
        public ExcavationMode byIndex(int index) {
            return MathUtils.getByIndexMod(MODES, index);
        }

        @Override
        public ITextComponent getTextComponent() {
            return label;
        }

        public int getEfficiency() {
            return efficiency;
        }
    }
}
