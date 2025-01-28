package mekanism.common.item;


import mekanism.api.EnumColor;
import mekanism.api.gear.ModuleData;
import mekanism.client.MekKeyHandler;
import mekanism.client.MekanismKeyHandler;
import mekanism.common.MekanismLang;
import mekanism.common.content.gear.IModuleItem;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.util.LangUtils;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class ItemModule extends Item implements IModuleItem {

    private final ModuleData<?> moduleData;

    public ItemModule(ModuleData<?> moduleData) {
        super();
        this.moduleData = moduleData;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return getModuleData().getMaxStackSize();
    }

    @Override
    public ModuleData<?> getModuleData() {
        return moduleData;
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return getModuleData().getRarity();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flag) {
        if (MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.sneakKey)) {
            tooltip.add(MekanismLang.MODULE_SUPPORTED.translateColored(EnumColor.BRIGHT_GREEN).getFormattedText());
            for (Item item : ModuleHelper.get().getSupported(getModuleData())) {
                tooltip.add(item.getItemStackDisplayName(new ItemStack(item)));
            }
            Set<ModuleData<?>> conflicting = ModuleHelper.get().getConflicting(getModuleData());
            if (!conflicting.isEmpty()) {
                tooltip.add(MekanismLang.MODULE_CONFLICTING.translateColored(EnumColor.RED).getFormattedText());
                for (ModuleData<?> module : conflicting) {
                    tooltip.add(MekanismLang.GENERIC_LIST.translate().getFormattedText() + module.getTranslationKey());
                }
            }
        } else {
            ModuleData<?> moduleData = getModuleData();
            tooltip.add(LangUtils.localize(moduleData.getDescriptionTranslationKey()));
            tooltip.add(MekanismLang.MODULE_STACKABLE.translateColored(EnumColor.GREY).getFormattedText() + EnumColor.AQUA + moduleData.getMaxStackSize());
            tooltip.add(LangUtils.localize("tooltip.hold") + " " + EnumColor.INDIGO + GameSettings.getKeyDisplayString(MekanismKeyHandler.sneakKey.getKeyCode()) + EnumColor.GREY + " " + LangUtils.localize("tooltip.forDetails") + ".");
        }
    }

}
