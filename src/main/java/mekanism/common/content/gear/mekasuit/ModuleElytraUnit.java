package mekanism.common.content.gear.mekasuit;

import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.api.text.TextComponentGroup;
import mekanism.common.MekanismModules;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;;

@ParametersAreNotNullByDefault
public class ModuleElytraUnit implements ICustomModule<ModuleElytraUnit> {

    @Override
    public boolean canChangeModeWhenDisabled(IModule<ModuleElytraUnit> module) {
        return true;
    }

    @Override
    public void changeMode(IModule<ModuleElytraUnit> module, EntityPlayer player, ItemStack stack, int shift, boolean displayChangeMessage) {
        module.toggleEnabled(player, new TextComponentGroup().string(MekanismModules.ELYTRA_UNIT.getTranslationKey()));
    }
}