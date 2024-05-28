package mekanism.smartfactory.common;

import mekanism.common.base.IGuiProvider;
import mekanism.smartfactory.common.inventory.container.ContainerAEElectricMachine;
import mekanism.smartfactory.common.tile.machine.TileEntityAEEnergizedSmelter;
import mekanism.smartfactory.common.tile.prefab.TileEntityAEElectricMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SmartFactoryCommonProxy  implements IGuiProvider {

    private static void registerTileEntity(Class<? extends TileEntity> clazz, String name) {
        GameRegistry.registerTileEntity(clazz, new ResourceLocation(MekanismSmartFactory.MODID, name));
    }

    public void registerTileEntities() {
        registerTileEntity(TileEntityAEEnergizedSmelter.class,"ae_energized_smelter");
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

    }


    @Override
    public Container getServerGui(int ID, EntityPlayer player, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return switch (ID) {
            case 0 -> new ContainerAEElectricMachine<>(player.inventory, (TileEntityAEElectricMachine)tileEntity);
            default -> null;
        };
    }

    @Override
    public Object getClientGui(int ID, EntityPlayer player, World world, BlockPos pos) {
        return null;
    }
}
