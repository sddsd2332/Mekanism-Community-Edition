package mekanism.smartfactory.client;

import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.item.machine.RenderMachineItem;
import mekanism.client.render.tileentity.RenderConfigurableMachine;
import mekanism.smartfactory.client.gui.GuiAEEnergizedSmelter;
import mekanism.smartfactory.common.MekanismSmartFactory;
import mekanism.smartfactory.common.SmartFactoryBlocks;
import mekanism.smartfactory.common.SmartFactoryCommonProxy;
import mekanism.smartfactory.common.block.states.BlockStateSmartFactoryMachine.SmartFactoryMachineBlockStateMapper;
import mekanism.smartfactory.common.tile.machine.TileEntityAEEnergizedSmelter;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SmartFactoryClientProxy extends SmartFactoryCommonProxy {
    private static final IStateMapper machineMapper = new SmartFactoryMachineBlockStateMapper();

    @Override
    public void registerTESRs() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAEEnergizedSmelter.class, new RenderConfigurableMachine<>());
    }

    @Override
    public void registerItemRenders() {
        Item.getItemFromBlock(SmartFactoryBlocks.SmartFactoryMachine).setTileEntityItemStackRenderer(new RenderMachineItem());
    }


    @Override
    public void registerBlockRenders() {
        ModelLoader.setCustomStateMapper(SmartFactoryBlocks.SmartFactoryMachine, machineMapper);
    }

    private ModelResourceLocation getInventoryMRL(String type) {
        return new ModelResourceLocation(new ResourceLocation(MekanismSmartFactory.MODID, type), "inventory");
    }

    public void registerItemRender(Item item) {
        MekanismRenderer.registerItemRender(MekanismSmartFactory.MODID, item);
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        IRegistry<ModelResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
    }

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
    }


    @Override
    public GuiScreen getClientGui(int ID, EntityPlayer player, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return switch (ID) {
            case 0 -> new GuiAEEnergizedSmelter(player.inventory, (TileEntityAEEnergizedSmelter) tileEntity);
            default -> null;
        };
    }
}
