package mekanism.common.content.gear.mekasuit;

import mekanism.api.gear.IHUDElement;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.ModuleConfigItem.EnumData;
import mekanism.common.content.gear.Modules;
import mekanism.common.item.armor.ItemMekaSuitBodyArmor;
import mekanism.common.item.interfaces.IJetpackItem.JetpackMode;
import mekanism.common.util.StorageUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ModuleJetpackUnit extends Module {

    private ModuleConfigItem<JetpackMode> jetpackMode;

    @Override
    public void init() {
        jetpackMode = addConfigItem(new ModuleConfigItem<>(this, "jetpack_mode", MekanismLang.MODULE_JETPACK_MODE, new EnumData<>(JetpackMode.class), JetpackMode.NORMAL));
    }

    @Override
    public void addHUDElements(EntityPlayer player, List<IHUDElement> hudElementAdder) {
        if (isEnabled()) {
            ItemStack container = getContainer();
            if (container.getItem() instanceof ItemMekaSuitBodyArmor armour) {
                double ratio = StorageUtils.getRatio(armour.getStored(container), MekanismConfig.current().meka.mekaSuitJetpackMaxStorage.val());
                hudElementAdder.add(Modules.hudElementPercent(jetpackMode.get().getHUDIcon(), ratio));
            }
        }
    }

    @Override
    public void changeMode(EntityPlayer player, ItemStack stack, int shift, boolean displayChangeMessage) {
        if (isEnabled()) {
            JetpackMode currentMode = getMode();
            JetpackMode newMode = currentMode.adjust(shift);
            if (jetpackMode.get() != newMode) {
                jetpackMode.set(newMode, null);
                if (displayChangeMessage) {
                    displayModeChange(player, MekanismLang.MODULE_JETPACK_MODE.translate(), newMode);
                }
            }
        }
    }

    public JetpackMode getMode() {
        return jetpackMode.get();
    }
}
