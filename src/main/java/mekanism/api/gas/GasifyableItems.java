package mekanism.api.gas;

import mekanism.common.util.MekanismUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class GasifyableItems {

    static HashMap<String, GasStack> gasifyableItems = new HashMap<String, GasStack>();
    static List<Gas> validGases = new ArrayList<>();

    /**
     * Registers a new item to gas conversion at a Chemical Injection Chamber.
     * @param oredict - oredicted item to add
     * @param gas gas output from oredict item
     * @param amount amount to create
     */
    public static void registerGasifyables(String oredict, Gas gas, Integer amount) {
        GasStack gasifyEntry = new GasStack(gas, amount);

        if (oredict != null)
            gasifyableItems.put(oredict, gasifyEntry);
        validGases.add(gas);
    }

    /**
     * Registers a new gas to be used at the Chemical Injection Chamber.
     * @param gas gases that can be used and accepted
     */
    public static void registerGasifyables(Gas gas) {
        validGases.add(gas);
    }

    /**
     * Returns gas associated to that item's oredict.
     * @param itemstack to be converted to gas
     * @return Gas output
     */
    public static GasStack getGasFromItem(ItemStack itemstack) {

        List<String> oredict = MekanismUtils.getOreDictName(itemstack);

        for (String s : oredict) {
            if (gasifyableItems.containsKey(s))
                return gasifyableItems.get(s);
        }
        return null;
    }


    /**
     * defines which gases the Chemical Injection Chamber can accept.
     * @param gas type of gas to check validity
     * @return if block accepts gas
     */
    public static boolean isGasValidGasifyable(Gas gas) {
        return validGases.contains(gas);
    }
}
