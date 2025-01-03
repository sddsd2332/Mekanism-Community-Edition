package mekanism.common.item;

import mekanism.api.EnumColor;
import mekanism.api.MekanismAPI;
import mekanism.api.gear.ModuleData;
import mekanism.api.providers.IModuleDataProvider;
import mekanism.client.MekKeyHandler;
import mekanism.client.MekanismKeyHandler;
import mekanism.common.content.gear.IModuleItem;
import mekanism.common.util.LangUtils;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ItemModule extends ItemMekanism implements IModuleItem {

    private final IModuleDataProvider<?> moduleData;

    public ItemModule(IModuleDataProvider<?> moduleData) {
        super();
        this.moduleData = moduleData;
    }

    @Override
    @Deprecated
    public int getItemStackLimit() {
        return getModuleData().getMaxStackSize();
    }

    @Override
    public ModuleData<?> getModuleData() {
        return moduleData.getModuleData();
    }

    @Deprecated
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return getModuleData().getRarity();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(itemstack, world, list, flag);
        if (MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.sneakKey)) {
            list.add(EnumColor.BRIGHT_GREEN + LangUtils.localize("module.mekanism.supported"));
            for (Item item : MekanismAPI.getModuleHelper().getSupported(getModuleData())) {
                list.add(item.getTranslationKey(new ItemStack(item)));
            }
            Set<ModuleData<?>> conflicting = MekanismAPI.getModuleHelper().getConflicting(getModuleData());
            if (!conflicting.isEmpty()) {
                list.add(EnumColor.RED + LangUtils.localize("module.mekanism.conflicting"));
                for (ModuleData<?> module : conflicting) {
                    list.add(module + LangUtils.localize("generic.mekanism.list"));
                }
            }
        } else {
            ModuleData<?> moduleData = getModuleData();
            list.add(LangUtils.localize(getTranslationKey(itemstack) + ".description"));
            list.add(EnumColor.GREY + LangUtils.localize("module.mekanism.stackable") + EnumColor.AQUA + moduleData.getMaxStackSize());
            list.add(EnumColor.GREY + LangUtils.localize("tooltip.hold") + " " + EnumColor.INDIGO + GameSettings.getKeyDisplayString(MekanismKeyHandler.sneakKey.getKeyCode()) + EnumColor.GREY + " " + LangUtils.localize("tooltip.mekanism.hold_for_supported_items"));
        }
    }

}
