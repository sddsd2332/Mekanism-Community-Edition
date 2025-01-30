package mekanism.common.content.gear.mekasuit;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.api.gear.config.IModuleConfigItem;
import mekanism.api.gear.config.ModuleConfigItemCreator;
import mekanism.api.gear.config.ModuleEnumData;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.TextComponentGroup;
import mekanism.common.MekanismLang;
import net.minecraft.util.text.ITextComponent;

@ParametersAreNotNullByDefault
public class ModuleHydraulicPropulsionUnit implements ICustomModule<ModuleHydraulicPropulsionUnit> {

    private IModuleConfigItem<JumpBoost> jumpBoost;
    private IModuleConfigItem<StepAssist> stepAssist;

    @Override
    public void init(IModule<ModuleHydraulicPropulsionUnit> module, ModuleConfigItemCreator configItemCreator) {
        jumpBoost = configItemCreator.createConfigItem("jump_boost", MekanismLang.MODULE_JUMP_BOOST, new ModuleEnumData<>(JumpBoost.LOW, module.getInstalledCount() + 1));
        stepAssist = configItemCreator.createConfigItem("step_assist", MekanismLang.MODULE_STEP_ASSIST, new ModuleEnumData<>(StepAssist.LOW, module.getInstalledCount() + 1));
    }

    public float getBoost() {
        return jumpBoost.get().getBoost();
    }

    public float getStepHeight() {
        return stepAssist.get().getHeight();
    }

    @NothingNullByDefault
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

    @NothingNullByDefault
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