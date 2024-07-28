package mekanism.multiblockmachine.client;

import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.item.ItemLayerWrapper;
import mekanism.multiblockmachine.client.gui.GuiMidsizeGasTank;
import mekanism.multiblockmachine.client.gui.generator.GuiLargeGasGenerator;
import mekanism.multiblockmachine.client.gui.generator.GuiLargeHeatGenerator;
import mekanism.multiblockmachine.client.gui.generator.GuiLargeWindGenerator;
import mekanism.multiblockmachine.client.gui.machine.GuiDigitalAssemblyTable;
import mekanism.multiblockmachine.client.gui.machine.GuiLargeChemicalInfuser;
import mekanism.multiblockmachine.client.gui.machine.GuiLargeChemicalWasher;
import mekanism.multiblockmachine.client.gui.machine.GuiLargeElectrolyticSeparator;
import mekanism.multiblockmachine.client.render.generator.RenderLargeGasGenerator;
import mekanism.multiblockmachine.client.render.generator.RenderLargeHeatGenerator;
import mekanism.multiblockmachine.client.render.generator.RenderLargeWindGenerator;
import mekanism.multiblockmachine.client.render.item.generator.RenderMultiblockGeneratorItem;
import mekanism.multiblockmachine.client.render.item.machine.RenderMultiblockMachineItem;
import mekanism.multiblockmachine.client.render.machine.RenderDigitalAssemblyTable;
import mekanism.multiblockmachine.client.render.machine.RenderLargeChemicalInfuser;
import mekanism.multiblockmachine.client.render.machine.RenderLargeChemicalWasher;
import mekanism.multiblockmachine.client.render.machine.RenderLargeElectrolyticSeparator;
import mekanism.multiblockmachine.common.MekanismMultiblockMachine;
import mekanism.multiblockmachine.common.MultiblockMachineBlocks;
import mekanism.multiblockmachine.common.MultiblockMachineCommonProxy;
import mekanism.multiblockmachine.common.MultiblockMachineItems;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine.MultiblockMachineBlockStateMapper;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine.MultiblockMachineType;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachineGenerator.MultiblockMachineGeneratorBlockStateMapper;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachineGenerator.MultiblockMachineGeneratorType;
import mekanism.multiblockmachine.common.tile.TileEntityMidsizeGasTank;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeGasGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeHeatGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeWindGenerator;
import mekanism.multiblockmachine.common.tile.machine.TileEntityDigitalAssemblyTable;
import mekanism.multiblockmachine.common.tile.machine.TileEntityLargeChemicalInfuser;
import mekanism.multiblockmachine.common.tile.machine.TileEntityLargeChemicalWasher;
import mekanism.multiblockmachine.common.tile.machine.TileEntityLargeElectrolyticSeparator;
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
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MultiblockMachineClientProxy extends MultiblockMachineCommonProxy {

    private static final IStateMapper generatorMapper = new MultiblockMachineGeneratorBlockStateMapper();
    private static final IStateMapper machineMapper = new MultiblockMachineBlockStateMapper();

    @Override
    public void registerTESRs() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLargeWindGenerator.class, new RenderLargeWindGenerator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLargeHeatGenerator.class, new RenderLargeHeatGenerator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLargeGasGenerator.class, new RenderLargeGasGenerator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLargeElectrolyticSeparator.class, new RenderLargeElectrolyticSeparator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLargeChemicalInfuser.class, new RenderLargeChemicalInfuser());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLargeChemicalWasher.class, new RenderLargeChemicalWasher());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDigitalAssemblyTable.class,RenderDigitalAssemblyTable.INSTANCE);
    }

    @Override
    public void registerItemRenders() {
        registerItemRender(MultiblockMachineItems.gas_adsorption_fractionation_module);
        registerItemRender(MultiblockMachineItems.high_frequency_fusion_molding_module);
        registerItemRender(MultiblockMachineItems.LaserLenses);
        registerItemRender(MultiblockMachineItems.advanced_electrolysis_core);
        Item.getItemFromBlock(MultiblockMachineBlocks.MultiblockGenerator).setTileEntityItemStackRenderer(new RenderMultiblockGeneratorItem());
        Item.getItemFromBlock(MultiblockMachineBlocks.MultiblockMachine).setTileEntityItemStackRenderer(new RenderMultiblockMachineItem());
    }

    @Override
    public void registerBlockRenders() {
        ModelLoader.setCustomStateMapper(MultiblockMachineBlocks.MultiblockGenerator, generatorMapper);
        ModelLoader.setCustomStateMapper(MultiblockMachineBlocks.MultiblockMachine, machineMapper);

        for (MultiblockMachineGeneratorType type : MultiblockMachineGeneratorType.values()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(type.blockType.getBlock()), type.meta, getInventoryMRL(type.getName()));
        }

        for (MultiblockMachineType type : MultiblockMachineType.values()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(type.typeBlock.getBlock()), type.meta, getInventoryMRL(type.getName()));
        }

        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(MultiblockMachineBlocks.MidsizeGasTank), stack -> {
            ResourceLocation baseLocation = new ResourceLocation(MekanismMultiblockMachine.MODID, "MidsizeGasTank");
            return new ModelResourceLocation(baseLocation, "facing=north");
        });

        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(MultiblockMachineBlocks.MultiblockGasTank), stack -> {
            ResourceLocation baseLocation = new ResourceLocation(MekanismMultiblockMachine.MODID, "MultiblockGasTank");
            return new ModelResourceLocation(baseLocation, "facing=north");
        });
    }

    private ModelResourceLocation getInventoryMRL(String type) {
        return new ModelResourceLocation(new ResourceLocation(MekanismMultiblockMachine.MODID, type), "inventory");
    }


    public void registerItemRender(Item item) {
        MekanismRenderer.registerItemRender(MekanismMultiblockMachine.MODID, item);
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        IRegistry<ModelResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
        generatorModelBake(modelRegistry, "large_wind_generator", MultiblockMachineGeneratorType.LARGE_WIND_GENERATOR);
        generatorModelBake(modelRegistry, "large_heat_generator", MultiblockMachineGeneratorType.LARGE_HEAT_GENERATOR);
        generatorModelBake(modelRegistry, "large_gas_generator", MultiblockMachineGeneratorType.LARGE_GAS_GENERATOR);
        machineModelBake(modelRegistry, "large_electrolytic_separator", MultiblockMachineType.LARGE_ELECTROLYTIC_SEPARATOR);
        machineModelBake(modelRegistry, "large_chemical_infuser", MultiblockMachineType.LARGE_CHEMICAL_INFUSER);
        machineModelBake(modelRegistry, "large_chemical_washer", MultiblockMachineType.LARGE_CHEMICAL_WASHER);
        machineModelBake(modelRegistry,"digital_assembly_table",MultiblockMachineType.DIGITAL_ASSEMBLY_TABLE);
    }


    private void generatorModelBake(IRegistry<ModelResourceLocation, IBakedModel> modelRegistry, String type, MultiblockMachineGeneratorType generatorType) {
        ModelResourceLocation modelResourceLocation = getInventoryMRL(type);
        ItemLayerWrapper itemLayerWrapper = new ItemLayerWrapper(modelRegistry.getObject(modelResourceLocation));
        RenderMultiblockGeneratorItem.modelMap.put(generatorType, itemLayerWrapper);
        modelRegistry.putObject(modelResourceLocation, itemLayerWrapper);
    }

    private void machineModelBake(IRegistry<ModelResourceLocation, IBakedModel> modelRegistry, String type, MultiblockMachineType machineType) {
        ModelResourceLocation modelResourceLocation = getInventoryMRL(type);
        ItemLayerWrapper itemLayerWrapper = new ItemLayerWrapper(modelRegistry.getObject(modelResourceLocation));
        RenderMultiblockMachineItem.modelMap.put(machineType, itemLayerWrapper);
        modelRegistry.putObject(modelResourceLocation, itemLayerWrapper);
    }

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public GuiScreen getClientGui(int ID, EntityPlayer player, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return switch (ID) {
            case 0 -> new GuiLargeWindGenerator(player.inventory, (TileEntityLargeWindGenerator) tileEntity);
            case 1 -> new GuiLargeHeatGenerator(player.inventory, (TileEntityLargeHeatGenerator) tileEntity);
            case 2 -> new GuiLargeGasGenerator(player.inventory, (TileEntityLargeGasGenerator) tileEntity);
            case 3 -> new GuiLargeElectrolyticSeparator(player.inventory, (TileEntityLargeElectrolyticSeparator) tileEntity);
            case 4 -> new GuiLargeChemicalInfuser(player.inventory, (TileEntityLargeChemicalInfuser) tileEntity);
            case 5 -> new GuiLargeChemicalWasher(player.inventory, (TileEntityLargeChemicalWasher) tileEntity);
            case 6 -> new GuiDigitalAssemblyTable(player.inventory,(TileEntityDigitalAssemblyTable) tileEntity);
            case 7 -> new GuiMidsizeGasTank(player.inventory,(TileEntityMidsizeGasTank) tileEntity);
            default -> null;
        };
    }

    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Pre event) {
        RenderDigitalAssemblyTable.resetDisplayInts();
    }

}
