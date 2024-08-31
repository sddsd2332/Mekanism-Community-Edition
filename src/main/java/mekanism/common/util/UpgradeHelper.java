package mekanism.common.util;

import mekanism.api.EnumColor;
import mekanism.common.base.IModuleUpgrade;
import mekanism.common.moduleUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class UpgradeHelper extends ItemDataUtils {

    public static int getUpgradeLevel(ItemStack stack, moduleUpgrade upgrade) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(DATA_ID)) {
            return 0;
        }

        NBTTagCompound upgradeTag = stack.getOrCreateSubCompound(DATA_ID);
        return upgradeTag.getByte(upgrade.getName());
    }

    public static void setUpgradeLevel(ItemStack stack, moduleUpgrade upgrade, int level) {
        NBTTagCompound upgradeTag = stack.getOrCreateSubCompound(DATA_ID);
        upgradeTag.setByte(upgrade.getName(), (byte) level);
    }


    @SideOnly(Side.CLIENT)
    public static List<String> getUpgradeStats(ItemStack stack) {
        ArrayList<String> list = new ArrayList<>();
        if (stack.getItem() instanceof IModuleUpgrade ModuleUpgrade) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                list.add(LangUtils.localize("tooltip.hold") + " " + EnumColor.AQUA + "shift" + EnumColor.GREY + " " + LangUtils.localize("tooltip.forDetails"));
            } else {
                list.add(EnumColor.ORANGE + LangUtils.localize("tooltip.hold_for_modules") + ": ");
                for (moduleUpgrade upgrade : ModuleUpgrade.getValidModule(stack)) {
                    if (getUpgradeLevel(stack, upgrade) > 0){
                        list.add("- " + upgrade.getLangName() + (upgrade.canMultiply() ? ": " + EnumColor.GREY + "x" + getUpgradeLevel(stack, upgrade) : ""));
                    }
                }
            }
        }
        return list;
    }

    public static boolean isUpgradeInstalled(ItemStack stack, moduleUpgrade upgrade) {
        return getUpgradeLevel(stack, upgrade) > 0;
    }
}
