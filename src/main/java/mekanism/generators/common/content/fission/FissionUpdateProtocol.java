package mekanism.generators.common.content.fission;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mekanism.api.Coord4D;
import mekanism.common.content.tank.SynchronizedTankData;
import mekanism.common.multiblock.MultiblockCache;
import mekanism.common.multiblock.MultiblockManager;
import mekanism.common.multiblock.UpdateProtocol;
import mekanism.generators.common.MekanismGenerators;
import mekanism.generators.common.block.states.BlockStateGenerator.GeneratorType;
import mekanism.generators.common.tile.fission.TileEntityControlRodAssembly;
import mekanism.generators.common.tile.fission.TileEntityFissionCasing;
import mekanism.generators.common.tile.fission.TileEntityFissionFuelAssembly;
import mekanism.generators.common.tile.fission.TileEntityFissionValve;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Set;

public class FissionUpdateProtocol extends UpdateProtocol<SynchronizedFissionData> {

    public static final int WATER_PER_TANK = 16000;
    public static final int STEAM_PER_TANK = 160000;

    public FissionUpdateProtocol(TileEntityFissionCasing tileEntity) {
        super(tileEntity);
    }


    @Override
    protected boolean isValidFrame(int x, int y, int z) {
        return false;
   //     return GeneratorType.get(pointer.getWorld().getBlockState(new BlockPos(x, y, z))) == GeneratorType.FISSION_REACTOR_CASING;
    }

    @Override
    protected boolean isValidInnerNode(int x, int y, int z) {
        if (super.isValidInnerNode(x, y, z)) {
            return true;
        }
        TileEntity tile = pointer.getWorld().getTileEntity(new BlockPos(x, y, z));
        return tile instanceof TileEntityFissionFuelAssembly || tile instanceof TileEntityControlRodAssembly;
    }

    @Override
    protected boolean canForm(SynchronizedFissionData structure) {
        if (structure.volHeight < 3) {
            return false;
        }
        Set<Coord4D> dispersers = new ObjectOpenHashSet<>();
        Set<Coord4D> elements = new ObjectOpenHashSet<>();
        for (Coord4D coord : innerNodes) {
            TileEntity tile = coord.getTileEntity(pointer.getWorld());
            if (tile instanceof TileEntityControlRodAssembly) {
                dispersers.add(coord);
            } else if (tile instanceof TileEntityFissionFuelAssembly) {
                structure.internalLocations.add(coord);
                elements.add(coord);
            }
        }
        if (dispersers.isEmpty()) {
            return false;
        }
        final Coord4D initDisperser = dispersers.iterator().next();

        Coord4D pos = new Coord4D(structure.renderLocation.x, initDisperser.y, structure.renderLocation.z, pointer.getWorld().provider.getDimension());
        for (int x = 1; x < structure.volLength - 1; x++) {
            for (int z = 1; z < structure.volWidth - 1; z++) {
                Coord4D coord4D = pos.translate(x, 0, z);
                TileEntity tile = coord4D.getTileEntity(pointer.getWorld());
                if (!(tile instanceof TileEntityControlRodAssembly)) {
                    return false;
                }
                dispersers.remove(coord4D);
            }
        }

        if (!dispersers.isEmpty()) {
            return false;
        }

        if (elements.size() > 0) {
            structure.superheatingElements = new NodeCounter(new NodeChecker() {
                @Override
                public boolean isValid(Coord4D coord) {
                    return coord.getTileEntity(pointer.getWorld()) instanceof TileEntityFissionFuelAssembly;
                }
            }).calculate(elements.iterator().next());
        }

        if (elements.size() > structure.superheatingElements) {
            return false;
        }
        Coord4D initAir = null;
        int totalAir = 0;
        for (int x = structure.renderLocation.x; x < structure.renderLocation.x + structure.volLength; x++) {
            for (int y = structure.renderLocation.y; y < initDisperser.y; y++) {
                for (int z = structure.renderLocation.z; z < structure.renderLocation.z + structure.volWidth; z++) {
                    if (pointer.getWorld().isAirBlock(new BlockPos(x, y, z)) || isViableNode(x, y, z)) {
                        initAir = new Coord4D(x, y, z, pointer.getWorld().provider.getDimension());
                        totalAir++;
                    }
                }
            }
        }
        if (initAir == null) {
            return false;
        }

        final Coord4D renderLocation = structure.renderLocation;
        final int volLength = structure.volLength;
        final int volWidth = structure.volWidth;
        structure.waterVolume = new NodeCounter(new NodeChecker() {
            @Override
            public final boolean isValid(Coord4D coord) {
                return coord.y >= renderLocation.y - 1 && coord.y < initDisperser.y &&
                        coord.x >= renderLocation.x && coord.x < renderLocation.x + volLength &&
                        coord.z >= renderLocation.z && coord.z < renderLocation.z + volWidth &&
                        (coord.isAirBlock(pointer.getWorld()) || isViableNode(coord.getPos()));
            }
        }).calculate(initAir);


        //Make sure all air blocks are connected
        if (totalAir > structure.waterVolume) {
            return false;
        }

        int steamHeight = (structure.renderLocation.y + structure.volHeight - 2) - initDisperser.y;
        structure.steamVolume = structure.volWidth * structure.volLength * steamHeight;
        structure.upperRenderLocation = new Coord4D(structure.renderLocation.x, initDisperser.y + 1, structure.renderLocation.z, pointer.getWorld().provider.getDimension());
        return true;

    }

    @Override
    protected FissionCache getNewCache() {
        return new FissionCache();
    }

    @Override
    protected SynchronizedFissionData getNewStructure() {
        return new SynchronizedFissionData();
    }

    @Override
    protected MultiblockManager<SynchronizedFissionData> getManager() {
        return MekanismGenerators.fissionMangaer;
    }

    @Override
    protected void mergeCaches(List<ItemStack> rejectedItems, MultiblockCache<SynchronizedFissionData> cache, MultiblockCache<SynchronizedFissionData> merge) {
        FissionCache boilerCache = (FissionCache) cache;
        FissionCache mergeCache = (FissionCache) merge;
        if (boilerCache.water == null) {
            boilerCache.water = mergeCache.water;
        } else if (mergeCache.water != null && boilerCache.water.isFluidEqual(mergeCache.water)) {
            boilerCache.water.amount += mergeCache.water.amount;
        }

        if (boilerCache.steam == null) {
            boilerCache.steam = mergeCache.steam;
        } else if (mergeCache.steam != null && boilerCache.steam.isFluidEqual(mergeCache.steam)) {
            boilerCache.steam.amount += mergeCache.steam.amount;
        }

        if (boilerCache.input == null){
            boilerCache.input = mergeCache.input;
        }else if (mergeCache.input != null && boilerCache.input.isGasEqual(mergeCache.input)) {
            boilerCache.input.amount += mergeCache.input.amount;
        }

        if (boilerCache.output == null){
            boilerCache.output = mergeCache.output;
        }else if (mergeCache.output != null && boilerCache.output.isGasEqual(mergeCache.output)) {
            boilerCache.output.amount += mergeCache.output.amount;
        }

        boilerCache.temperature = Math.max(boilerCache.temperature, mergeCache.temperature);
    }

    @Override
    protected void onFormed() {
        super.onFormed();
        if (structureFound.waterStored != null) {
            structureFound.waterStored.amount = Math.min(structureFound.waterStored.amount, structureFound.waterVolume * WATER_PER_TANK);
        }
        if (structureFound.steamStored != null) {
            structureFound.steamStored.amount = Math.min(structureFound.steamStored.amount, structureFound.steamVolume * STEAM_PER_TANK);
        }
        if (structureFound.InputGas != null) {
            structureFound.InputGas.amount = Math.min(structureFound.InputGas.amount, structureFound.waterVolume * WATER_PER_TANK);
        }
        if (structureFound.OutputGas != null) {
            structureFound.OutputGas.amount = Math.min(structureFound.OutputGas.amount, structureFound.steamVolume * STEAM_PER_TANK);
        }
    }


    @Override
    protected void onStructureCreated(SynchronizedFissionData structure, int origX, int origY, int origZ, int xmin, int xmax, int ymin, int ymax, int zmin, int zmax) {
        for (Coord4D obj : structure.locations) {
            if (obj.getTileEntity(pointer.getWorld()) instanceof TileEntityFissionValve){
                SynchronizedTankData.ValveData data = new SynchronizedTankData.ValveData();
                data.location = obj;
                data.side = getSide(obj, origX + xmin, origX + xmax, origY + ymin, origY + ymax, origZ + zmin, origZ + zmax);
                structure.valves.add(data);
            }
        }
    }
}
