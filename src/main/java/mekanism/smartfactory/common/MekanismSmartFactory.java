package mekanism.smartfactory.common;

import io.netty.buffer.ByteBuf;
import mekanism.common.Mekanism;
import mekanism.common.Version;
import mekanism.common.base.IModule;
import mekanism.common.config.MekanismConfig;
import mekanism.common.network.PacketSimpleGui;
import mekanism.multiblockmachine.common.MekanismMultiblockMachine;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = MekanismSmartFactory.MODID)
@Mod.EventBusSubscriber()
public class MekanismSmartFactory implements IModule {
    public static final String MODID = "mekanismsmartfactory";
    //TODO:通过AE获取各种物品和流体给机器生产

    @SidedProxy(clientSide = "mekanism.smartfactory.client.SmartFactoryClientProxy", serverSide = "mekanism.smartfactory.common.SmartFactoryCommonProxy")
    public static SmartFactoryCommonProxy proxy;

    @Mod.Instance(MekanismSmartFactory.MODID)
    public static MekanismSmartFactory instance;

    public static Version versionNumber = new Version(999, 999, 999);

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        SmartFactoryBlocks.registerBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        SmartFactoryItems.registerItems(event.getRegistry());
        SmartFactoryBlocks.registerItemBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        proxy.registerBlockRenders();
        proxy.registerItemRenders();
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Mekanism.modulesLoaded.add(this);
        PacketSimpleGui.handlers.add(proxy);
        NetworkRegistry.INSTANCE.registerGuiHandler(this,new SmartFactoryGuiHandler());
        MinecraftForge.EVENT_BUS.register(this);
        proxy.registerTileEntities();
        proxy.registerTESRs();
        Mekanism.logger.info("Loaded Mekanism Smart Factory Machine module.");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
        proxy.loadConfiguration();
    }

    @Override
    public Version getVersion() {
        return versionNumber;
    }

    @Override
    public String getName() {
        return "SmartFactory";
    }

    @Override
    public void writeConfig(ByteBuf dataStream, MekanismConfig config) {

    }

    @Override
    public void readConfig(ByteBuf dataStream, MekanismConfig destConfig) {

    }

    @Override
    public void resetClient() {

    }
}
