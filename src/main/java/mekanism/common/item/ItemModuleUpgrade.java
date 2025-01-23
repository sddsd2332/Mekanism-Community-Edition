package mekanism.common.item;

import mekanism.api.EnumColor;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.base.IMetaItem;
import mekanism.common.base.IModuleUpgrade;
import mekanism.common.base.IModuleUpgradeItem;
import mekanism.common.moduleUpgrade;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.add(LangUtils.localize("tooltip.hold") + " " + EnumColor.AQUA + "shift" + EnumColor.GREY + " " + LangUtils.localize("tooltip.forDetails"));
        } else {
            list.addAll(MekanismUtils.splitTooltip(moduleUpgrade.values()[stack.getItemDamage()].getDescription(), stack));
            list.add(MekanismLang.MODULE_SUPPORTED.translateColored(EnumColor.BRIGHT_GREEN).getFormattedText());
            for (Item item : ForgeRegistries.ITEMS) {
                if (item instanceof IModuleUpgrade upgrade && upgrade.getValidModule(new ItemStack(item)).contains(moduleUpgrade.values()[stack.getItemDamage()])) {
                    list.add("- " + item.getItemStackDisplayName(new ItemStack(item)));
                }
            }
            if (!Mekanism.hooks.DraconicEvolution && moduleUpgrade.values()[stack.getItemDamage()] == moduleUpgrade.ENERGY_SHIELD_UNIT) {
                list.add(EnumColor.AQUA + LangUtils.localize("tooltip.install.DR"));
            }
            if (!Mekanism.hooks.DraconicEvolution && !Mekanism.hooks.DraconicAdditions && moduleUpgrade.values()[stack.getItemDamage()] == moduleUpgrade.ADVANCED_INTERCEPTION_SYSTEM_UNIT) {
                list.add(EnumColor.AQUA + LangUtils.localize("tooltip.install.DR"));
                list.add(EnumColor.AQUA + LangUtils.localize("tooltip.install.DR2"));
            }
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

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return moduleUpgrade.values()[stack.getItemDamage()].getRarity();
    }

}
