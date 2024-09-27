package mekanism.common.multiblock;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mekanism.api.Coord4D;
import mekanism.common.tile.multiblock.TileEntityMultiblock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.*;
import java.util.Map.Entry;

public class MultiblockManager<T extends SynchronizedData<T>> {

    private static final Set<MultiblockManager<?>> MANAGERS = new ObjectOpenHashSet<>();

    public String name;

    /**
     * A map containing references to all multiblock inventory caches.
     */
    public Map<String, MultiblockCache<T>> inventories = new Object2ObjectOpenHashMap<>();

    public MultiblockManager(String s) {
        name = s;
        MANAGERS.add(this);
    }

    public static void tick(World world) {
        for (MultiblockManager<?> manager : MANAGERS) {
            manager.tickSelf(world);
        }
    }

    public static String getStructureId(TileEntityMultiblock<?> tile) {
        return tile.structure != null ? tile.getSynchronizedData().inventoryID : null;
    }

    public static boolean areEqual(TileEntity tile1, TileEntity tile2) {
        if (tile1 instanceof TileEntityMultiblock<?> multiblock1 && tile2 instanceof TileEntityMultiblock<?> multiblock2) {
            return multiblock1.getManager() == multiblock2.getManager();
        }
        return false;
    }

    public static void reset() {
        for (MultiblockManager<?> manager : MANAGERS) {
            manager.inventories.clear();
        }
    }

    /**
     * Grabs an inventory from the world's caches, and removes all the world's references to it.
     *
     * @param world - world the cache is stored in
     * @param id    - inventory ID to pull
     * @return correct multiblock inventory cache
     */
    public MultiblockCache<T> pullInventory(World world, String id) {
        MultiblockCache<T> toReturn = inventories.get(id);
        for (Coord4D obj : inventories.get(id).locations) {
            TileEntityMultiblock<T> tileEntity = (TileEntityMultiblock<T>) obj.getTileEntity(world);
            if (tileEntity != null) {
                tileEntity.cachedData = tileEntity.getNewCache();
                tileEntity.cachedID = null;
            }
        }
        inventories.remove(id);
        return toReturn;
    }

    /**
     * Grabs a unique inventory ID for a multiblock.
     *
     * @return unique inventory ID
     */
    public static String getUniqueInventoryID() {
        return UUID.randomUUID().toString();
    }

    public void tickSelf(World world) {
        ArrayList<String> idsToKill = new ArrayList<>();
        Map<String, Set<Coord4D>> tilesToKill = new Object2ObjectOpenHashMap<>();
        inventories.entrySet().parallelStream().forEach(entry -> {
            String inventoryID = entry.getKey();
            for (Coord4D obj : entry.getValue().locations) {
                if (obj.dimensionId != world.provider.getDimension() || !obj.exists(world)) {
                    continue;
                }
                TileEntity tileEntity = obj.getTileEntity(world);
                if (!(tileEntity instanceof TileEntityMultiblock<?> multiblock) || multiblock.getManager() != this ||
                    (getStructureId(multiblock) != null && !Objects.equals(getStructureId(multiblock), inventoryID))) {
                    if (!tilesToKill.containsKey(inventoryID)) {
                        tilesToKill.put(inventoryID, new ObjectOpenHashSet<>());
                    }
                    tilesToKill.get(inventoryID).add(obj);
                }
            }
            if (entry.getValue().locations.isEmpty()) {
                idsToKill.add(inventoryID);
            }
        });
        for (Entry<String, Set<Coord4D>> entry : tilesToKill.entrySet()) {
            for (Coord4D obj : entry.getValue()) {
                inventories.get(entry.getKey()).locations.remove(obj);
            }
        }
        for (String inventoryID : idsToKill) {
            inventories.remove(inventoryID);
        }
    }

    public void updateCache(TileEntityMultiblock<T> tile) {
        if (!inventories.containsKey(tile.cachedID)) {
            tile.cachedData.locations.add(Coord4D.get(tile));
            inventories.put(tile.cachedID, tile.cachedData);
        } else {
            inventories.get(tile.cachedID).locations.add(Coord4D.get(tile));
        }
    }
}
