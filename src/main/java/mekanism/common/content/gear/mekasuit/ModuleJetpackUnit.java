package mekanism.common.content.gear.mekasuit;

import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IHUDElement;
import mekanism.api.gear.IModule;
import mekanism.api.gear.config.IModuleConfigItem;
import mekanism.api.gear.config.ModuleConfigItemCreator;
import mekanism.api.gear.config.ModuleEnumData;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.item.armor.ItemMekaSuitBodyArmor;
import mekanism.common.item.interfaces.IJetpackItem.JetpackMode;
import mekanism.common.util.StorageUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

@ParametersAreNotNullByDefault
public class ModuleJetpackUnit implements ICustomModule<ModuleJetpackUnit> {

    private IModuleConfigItem<JetpackMode> jetpackMode;

    @Override
    public void init(IModule<ModuleJetpackUnit> module, ModuleConfigItemCreator configItemCreator) {
        jetpackMode = configItemCreator.createConfigItem("jetpack_mode", MekanismLang.MODULE_JETPACK_MODE, new ModuleEnumData<>(JetpackMode.NORMAL));
    }

    @Override
    public void addHUDElements(IModule<ModuleJetpackUnit> module, EntityPlayer player, Consumer<IHUDElement> hudElementAdder) {
        if (module.isEnabled()) {
            ItemStack container = module.getContainer();
            if (container.getItem() instanceof ItemMekaSuitBodyArmor armour) {
                double ratio = StorageUtils.getRatio(armour.getStored(container), MekanismConfig.current().meka.mekaSuitJetpackMaxStorage.val());
                hudElementAdder.accept(ModuleHelper.get().hudElementPercent(jetpackMode.get().getHUDIcon(), ratio));
            }
        }
    }

    @Override
    public void changeMode(IModule<ModuleJetpackUnit> module, EntityPlayer player, ItemStack stack, int shift, boolean displayChangeMessage) {
        JetpackMode currentMode = getMode();
        JetpackMode newMode = currentMode.adjust(shift);
        if (currentMode != newMode) {
            jetpackMode.set(newMode);
            if (displayChangeMessage) {
                module.displayModeChange(player, MekanismLang.MODULE_JETPACK_MODE.translate(), newMode);
            }
        }
    }

    public JetpackMode getMode() {
        return jetpackMode.get();
    }
}
