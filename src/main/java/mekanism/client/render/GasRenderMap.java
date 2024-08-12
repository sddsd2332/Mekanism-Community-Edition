package mekanism.client.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import mekanism.api.gas.GasStack;


/**
 * Map which uses FluidStacks as keys, ignoring amount. Primary use: caching FluidStack aware fluid rendering (NBT, yay)
 */
public class GasRenderMap<V> extends Object2ObjectOpenCustomHashMap<GasStack, V> {

    public GasRenderMap() {
        super(GasHashStrategy.INSTANCE);
    }

    /**
     * Implements equals & hashCode that ignore FluidStack#amount
     */
    public static class GasHashStrategy implements Strategy<GasStack> {

        public static GasHashStrategy INSTANCE = new GasHashStrategy();

        @Override
        public int hashCode(GasStack stack) {
            if (stack == null) {
                return 0;
            }
            int code = 1;
            code = 31 * code + stack.getGas().hashCode();
            if (stack.getGas() != null) {
                code = 31 * code + stack.getGas().hashCode();
            }
            return code;
        }

        @Override
        public boolean equals(GasStack a, GasStack b) {
            return a == null ? b == null : a.isGasEqual(b);
        }
    }
}
