package mekanism.common.util;

public class StorageUtils {

    public static double getRatio(int amount, int capacity) {
        return capacity == 0 ? 1 : (double) amount / capacity;
    }
}
