package mekanism.api;


import net.minecraft.entity.Entity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Chunk3D extends ChunkPosExpand {

    public int dimensionId;

    /**
     * Creates a Chunk3D from the defined chunk x, chunk z, and dimension values.
     *
     * @param dimension Dimension ID
     * @param x         Chunk X coordinate
     * @param z         Chunk Z coordinate
     */
    public Chunk3D(int x, int z, int dimension) {
        super(x, z);
        this.dimensionId = dimension;
    }

    public Chunk3D(Entity entity) {
        super((int) entity.posX >> 4, (int) entity.posZ >> 4);
        dimensionId = entity.dimension;
    }

    public Chunk3D(Coord4D coord) {
        super(coord.x>> 4, coord.z >> 4);
        dimensionId = coord.dimensionId;
    }

    /**
     * Whether or not this chunk exists in the given world.
     *
     * @param world - the world to check in
     * @return if the chunk exists
     */
    public boolean exists(World world) {
        return world.getChunkProvider().getLoadedChunk(x, z) != null;
    }

    /**
     * Gets a Chunk object corresponding to this Chunk3D's coordinates.
     *
     * @param world - the world to get the Chunk object from
     * @return the corresponding Chunk object
     */
    public Chunk getChunk(World world) {
        return world.getChunk(x, z);
    }

    /**
     * Creates a Chunk3D from the defined chunk position, and dimension values.
     *
     * @param dimension Dimension ID
     * @param chunkPos  Long representation of the chunk position
     * @since 10.3.2
     */


    public Chunk3D(long chunkPos, int dimension) {
        this(ChunkPosExpand.getX(chunkPos), ChunkPosExpand.getZ(chunkPos), dimension);
    }

    /**
     * Returns this Chunk3D in the Minecraft-based ChunkCoordIntPair format.
     *
     * @return this Chunk3D as a ChunkCoordIntPair
     */
    public ChunkPosExpand getPos() {
        return new ChunkPosExpand(x, z);
    }

    @Override
    public Chunk3D clone() {
        return new Chunk3D(x, z, dimensionId);
    }


    /**
     * Creates a Chunk3D from the defined chunk position, and dimension values.
     *
     * @param dimension Dimension ID
     * @param chunkPos  Chunk position
     */
    public Chunk3D(ChunkPosExpand chunkPos, int dimension) {
        this(chunkPos.x, chunkPos.z, dimension);
    }


    public Set<Chunk3D> expand(int chunkRadius) {
        if (chunkRadius < 0) {
            throw new IllegalArgumentException("Chunk radius cannot be negative.");
        } else if (chunkRadius == 1) {
            return Collections.singleton(this);
        }
        Set<Chunk3D> ret = new HashSet<>();
        for (int i = x - chunkRadius; i <= x + chunkRadius; i++) {
            for (int j = z - chunkRadius; j <= z + chunkRadius; j++) {
                ret.add(new Chunk3D(dimensionId, i, j));
            }
        }
        return ret;
    }

    @NotNull
    @Override
    public String toString() {
        return "[Chunk3D: " + x + ", " + z + ", dim=" + dimensionId + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Chunk3D other && other.x == x && other.z == z && other.dimensionId == dimensionId;
    }


    @Override
    public int hashCode() {
        int code = 1;
        code = 31 * code + x;
        code = 31 * code + z;
        code = 31 * code + dimensionId;
        return code;
    }

}
