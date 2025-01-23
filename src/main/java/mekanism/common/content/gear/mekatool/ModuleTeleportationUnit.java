package mekanism.common.content.gear.mekatool;

import mekanism.common.MekanismLang;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.ModuleConfigItem.BooleanData;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModuleTeleportationUnit extends Module {

    private ModuleConfigItem<Boolean> requiresBlockTarget;

    @Override
    public void init() {
        requiresBlockTarget = addConfigItem(new ModuleConfigItem<>(this, "require_block_target", MekanismLang.MODULE_TELEPORT_REQUIRES_BLOCK, new BooleanData(), true));
    }

    public boolean requiresBlockTarget() {
        return requiresBlockTarget.get();
    }
}