package mekanism.api;

import net.minecraft.util.math.ChunkPos;

public class ChunkPosExpand extends ChunkPos {
    public ChunkPosExpand(int x, int z) {
        super(x, z);
    }

    public static int getX(long pChunkAsLong) {
        return (int) (pChunkAsLong & 4294967295L);
    }

    public static int getZ(long pChunkAsLong) {
        return (int) (pChunkAsLong >>> 32 & 4294967295L);
    }
}
