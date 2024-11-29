package mekanism.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


public final class ItemNBTHelper {


    // GETTERS ///////////////////////////////////////////////////////////////////

    public static boolean verifyExistance(ItemStack stack, String tag) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) return false;
        else return stack.getTagCompound().hasKey(tag);
    }

    public static byte getByte(ItemStack stack, String tag, byte defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTagCompound().getByte(tag) : defaultExpected;
    }

    public static boolean getBoolean(ItemStack stack, String tag, boolean defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTagCompound().getBoolean(tag) : defaultExpected;
    }

    public static short getShort(ItemStack stack, String tag, short defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTagCompound().getShort(tag) : defaultExpected;
    }

    public static int getInteger(ItemStack stack, String tag, int defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTagCompound().getInteger(tag) : defaultExpected;
    }

    public static long getLong(ItemStack stack, String tag, long defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTagCompound().getLong(tag) : defaultExpected;
    }

    public static float getFloat(ItemStack stack, String tag, float defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTagCompound().getFloat(tag) : defaultExpected;
    }

    public static double getDouble(ItemStack stack, String tag, double defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTagCompound().getDouble(tag) : defaultExpected;
    }

    public static String getString(ItemStack stack, String tag, String defaultExpected) {
        return verifyExistance(stack, tag) ? stack.getTagCompound().getString(tag) : defaultExpected;
    }
}