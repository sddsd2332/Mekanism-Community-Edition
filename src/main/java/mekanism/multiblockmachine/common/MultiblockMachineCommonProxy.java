package mekanism.multiblockmachine.common;

import mekanism.common.Mekanism;
import mekanism.common.base.IGuiProvider;
import mekanism.common.config.MekanismConfig;
import mekanism.multiblockmachine.common.inventory.container.ContainerMidsizeGasTank;
import mekanism.multiblockmachine.common.inventory.container.generator.ContainerLargeGasGenerator;
import mekanism.multiblockmachine.common.inventory.container.generator.ContainerLargeHeatGenerator;
import mekanism.multiblockmachine.common.inventory.container.generator.ContainerLargeWindGenerator;
import mekanism.multiblockmachine.common.inventory.container.machine.ContainerDigitalAssemblyTable;
import mekanism.multiblockmachine.common.inventory.container.machine.ContainerLargeChemicalInfuser;
import mekanism.multiblockmachine.common.inventory.container.machine.ContainerLargeChemicalWasher;
import mekanism.multiblockmachine.common.inventory.container.machine.ContainerLargeElectrolyticSeparator;
import mekanism.multiblockmachine.common.tile.TileEntityMidsizeGasTank;
import mekanism.multiblockmachine.common.tile.TileEntityMultiblockGasTank;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeGasGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeHeatGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeWindGenerator;
import mekanism.multiblockmachine.common.tile.machine.TileEntityDigitalAssemblyTable;
import mekanism.multiblockmachine.common.tile.machine.TileEntityLargeChemicalInfuser;
import mekanism.multiblockmachine.common.tile.machine.TileEntityLargeChemicalWasher;
import mekanism.multiblockmachine.common.tile.machine.TileEntityLargeElectrolyticSeparator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MultiblockMachineCommonProxy implements IGuiProvider {


    private static void registerTileEntity(Class<? extends TileEntity> clazz, String name) {
        GameRegistry.registerTileEntity(clazz, new ResourceLocation(MekanismMultiblockMachine.MODID, name));
    }


    public void registerTileEntities() {
        registerTileEntity(TileEntityLargeWindGenerator.class, "large_wind_Generator");
        registerTileEntity(TileEntityLargeHeatGenerator.class, "large_heat_Generator");
        registerTileEntity(TileEntityLargeGasGenerator.class, "large_gas_Generator");
        registerTileEntity(TileEntityLargeElectrolyticSeparator.class, "large_electrolytic_separator");
        registerTileEntity(TileEntityLargeChemicalInfuser.class, "large_chemical_infuser");
        registerTileEntity(TileEntityLargeChemicalWasher.class, "large_chemical_washer");
        registerTileEntity(TileEntityDigitalAssemblyTable.class, "digital_assembly_table");
        registerTileEntity(TileEntityMidsizeGasTank.class,"midsize_gas_tank");
        registerTileEntity(TileEntityMultiblockGasTank.class,"multiblock_gas_tank");
    }

    public void registerTESRs() {
    }

    public void registerItemRenders() {
    }

    public void registerBlockRenders() {
    }

    public void preInit() {
    }

    public void loadConfiguration() {
        MekanismConfig.local().multiblock.load(Mekanism.configurationMultiblockMachine);
        if (Mekanism.configurationMultiblockMachine.hasChanged()) {
            Mekanism.configurationMultiblockMachine.save();
        }
    }

    @Override
    public Object getClientGui(int ID, EntityPlayer player, World world, BlockPos pos) {
        return null;
    }

    @Override
    public Container getServerGui(int ID, EntityPlayer player, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return switch (ID) {
            case 0 -> new ContainerLargeWindGenerator(player.inventory, (TileEntityLargeWindGenerator) tileEntity);
            case 1 -> new ContainerLargeHeatGenerator(player.inventory, (TileEntityLargeHeatGenerator) tileEntity);
            case 2 -> new ContainerLargeGasGenerator(player.inventory, (TileEntityLargeGasGenerator) tileEntity);
            case 3 -> new ContainerLargeElectrolyticSeparator(player.inventory, (TileEntityLargeElectrolyticSeparator) tileEntity);
            case 4 -> new ContainerLargeChemicalInfuser(player.inventory, (TileEntityLargeChemicalInfuser) tileEntity);
            case 5 -> new ContainerLargeChemicalWasher(player.inventory, (TileEntityLargeChemicalWasher) tileEntity);
            case 6 -> new ContainerDigitalAssemblyTable(player.inventory, (TileEntityDigitalAssemblyTable) tileEntity);
            case 7 -> new ContainerMidsizeGasTank(player.inventory,(TileEntityMidsizeGasTank) tileEntity);
            default -> null;
        };
    }


}
