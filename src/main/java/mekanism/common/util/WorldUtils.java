package mekanism.common.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;

public class WorldUtils {

    /**
     * Checks to see if the block at the position can see the sky, and it is daytime.
     *
     * @param world World to check in.
     * @param pos   Position to check.
     *
     * @return {@code true} if it can.
     */
    @Contract("null, _ -> false")
    public static boolean canSeeSun(@Nullable World world, BlockPos pos) {
        //Note: We manually handle the world#isDaytime check by just checking the subtracted skylight
        // as vanilla returns false if the world's time is set to a fixed value even if that time
        // would effectively be daytime
        return world != null && world.provider.hasSkyLight() && world.getSkylightSubtracted() < 4 && world.canSeeSky(pos);
    }

    /**
     * Vanilla copy of {@link net.minecraft.client.world.ClientWorld#getSkyDarken(float)} used to be World#getSunBrightness
     */
    public static float getSunBrightness(World world, float partialTicks) {
        float f = world.getCelestialAngle(partialTicks);
        float f1 = 1.0F - (MathHelper.cos(f * ((float) Math.PI * 2F)) * 2.0F + 0.2F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        f1 = (float) (f1 * (1.0D - world.getRainStrength(partialTicks) * 5.0F / 16.0D));
        f1 = (float) (f1 * (1.0D - world.getThunderStrength(partialTicks) * 5.0F / 16.0D));
        return f1 * 0.8F + 0.2F;
    }
}
