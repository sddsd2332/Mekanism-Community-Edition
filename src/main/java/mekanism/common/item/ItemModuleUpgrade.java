package mekanism.common.item;

import mekanism.api.EnumColor;
import mekanism.common.base.IMetaItem;
import mekanism.common.base.IModuleUpgradeItem;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemModuleUpgrade extends ItemMekanism implements IMetaItem, IModuleUpgradeItem {

    public ItemModuleUpgrade() {
        super();
        setHasSubtypes(true);
    }


    @Override
    public String getTexture(int meta) {
        return "Module" + moduleUpgrade.values()[meta].getName();
    }

    @Override
    public int getVariants() {
        return moduleUpgrade.values().length;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tabs, @Nonnull NonNullList<ItemStack> itemList) {
        if (isInCreativeTab(tabs)) {
            for (moduleUpgrade tier : moduleUpgrade.values()) {
                itemList.add(new ItemStack(this, 1, tier.ordinal()));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.add(LangUtils.localize("tooltip.hold") + " " + EnumColor.AQUA + "shift" + EnumColor.GREY + " " + LangUtils.localize("tooltip.forDetails"));
        } else {
            list.addAll(MekanismUtils.splitTooltip(moduleUpgrade.values()[itemstack.getItemDamage()].getDescription(), itemstack));
        }
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack item) {
        return "item.module." + moduleUpgrade.values()[item.getItemDamage()].getName().toLowerCase(Locale.ROOT);
    }

    @Override
    public moduleUpgrade getmoduleUpgrade(ItemStack stack) {
        return moduleUpgrade.values()[stack.getItemDamage()];
    }
}
