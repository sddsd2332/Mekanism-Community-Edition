package mekanism.client.render.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;


public class ScrollIncrementer {

    private final boolean discrete;
    private long lastScrollTime = -1;
    private double scrollDelta;

    public ScrollIncrementer(boolean discrete) {
        this.discrete = discrete;
    }

    private long getTime() {
        World level = Minecraft.getMinecraft().world;
        return level == null ? -1 : level.getWorldTime();
    }

    public int scroll(double delta) {
        long time = getTime();
        if (time - lastScrollTime > 20) {
            scrollDelta = 0;
        }
        lastScrollTime = time;
        scrollDelta += delta;
        int shift = (int) scrollDelta;
        scrollDelta %= 1;
        if (discrete) {
            shift = clamp(shift, -1, 1);
        }
        return shift;
    }

    public static int clamp(int pValue, int pMin, int pMax) {
        return Math.min(Math.max(pValue, pMin), pMax);
    }
}