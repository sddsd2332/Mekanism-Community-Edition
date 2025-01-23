package mekanism.common.content.gear.mekasuit;

import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.TextComponentGroup;
import mekanism.common.MekanismLang;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.ModuleConfigItem.EnumData;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModuleHydraulicPropulsionUnit extends Module {

    private ModuleConfigItem<JumpBoost> jumpBoost;
    private ModuleConfigItem<StepAssist> stepAssist;

    @Override
    public void init() {
        jumpBoost = addConfigItem(new ModuleConfigItem<>(this, "jump_boost", MekanismLang.MODULE_JUMP_BOOST, new EnumData<>(JumpBoost.class, getInstalledCount() + 1), JumpBoost.LOW));
        stepAssist = addConfigItem(new ModuleConfigItem<>(this, "step_assist", MekanismLang.MODULE_STEP_ASSIST, new EnumData<>(StepAssist.class, getInstalledCount() + 1), StepAssist.LOW));
    }

    public float getBoost() {
        return jumpBoost.get().getBoost();
    }

    public float getStepHeight() {
        return stepAssist.get().getHeight();
    }

    public enum JumpBoost implements IHasTextComponent {
        OFF(0),
        LOW(0.5F),
        MED(1),
        HIGH(3),
        ULTRA(5);

        private final float boost;
        private final ITextComponent label;

        JumpBoost(float boost) {
            this.boost = boost;
            this.label = new TextComponentGroup().getString(Float.toString(boost));
        }

        @Override
        public ITextComponent getTextComponent() {
            return label;
        }

        public float getBoost() {
            return boost;
        }
    }

    public enum StepAssist implements IHasTextComponent {
        OFF(0),
        LOW(0.5F),
        MED(1),
        HIGH(1.5F),
        ULTRA(2);

        private final float height;
        private final ITextComponent label;

        StepAssist(float height) {
            this.height = height;
            this.label = new TextComponentGroup().getString(Float.toString(height));
        }

        @Override
        public ITextComponent getTextComponent() {
            return label;
        }

        public float getHeight() {
            return height;
        }
    }
}