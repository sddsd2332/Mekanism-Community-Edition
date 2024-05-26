package mekanism.multiblockmachine.common;


import io.netty.buffer.ByteBuf;
import mekanism.api.MekanismAPI;
import mekanism.common.Mekanism;
import mekanism.common.Version;
import mekanism.common.base.IModule;
import mekanism.common.config.MekanismConfig;
import mekanism.common.fixers.MekanismDataFixers.MekFixers;
import mekanism.common.network.PacketSimpleGui;
import mekanism.multiblockmachine.common.fixers.MultiblockMachineTEFixer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.CompoundDataFixer;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = MekanismMultiblockMachine.MODID, useMetadata = true, guiFactory = "mekanism.multiblockmachine.client.gui.MultiblockMachineGuiFactory")
@Mod.EventBusSubscriber()
public class MekanismMultiblockMachine implements IModule {

    public static final String MODID = "mekanismmultiblockmachine";

    @SidedProxy(clientSide = "mekanism.multiblockmachine.client.MultiblockMachineClientProxy", serverSide = "mekanism.multiblockmachine.common.MultiblockMachineCommonProxy")
    public static MultiblockMachineCommonProxy proxy;

    @Mod.Instance(MekanismMultiblockMachine.MODID)
    public static MekanismMultiblockMachine instance;

    public static Version versionNumber = new Version(999, 999, 999);
    public static final int DATA_VERSION = 1;
    public static CreativeTabMekanismMultiblockMachine tabMekanismMultiblockMachine = new CreativeTabMekanismMultiblockMachine();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        MultiblockMachineBlocks.registerBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        MultiblockMachineItems.registerItems(event.getRegistry());
        MultiblockMachineBlocks.registerItemBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        proxy.registerBlockRenders();
        proxy.registerItemRenders();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
        proxy.loadConfiguration();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Mekanism.modulesLoaded.add(this);
        PacketSimpleGui.handlers.add(proxy);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new MultiblockMachineGuiHandler());
        MinecraftForge.EVENT_BUS.register(this);
        proxy.registerTileEntities();
        proxy.registerTESRs();
        CompoundDataFixer fixer = FMLCommonHandler.instance().getDataFixer();
        ModFixs fixes = fixer.init(MODID, DATA_VERSION);
        fixes.registerFix(FixTypes.BLOCK_ENTITY, new MultiblockMachineTEFixer(MekFixers.TILE_ENTITIES));
        Mekanism.logger.info("Loaded Mekanism Multi Block Machine module.");
    }


    @Override
    public Version getVersion() {
        return versionNumber;
    }

    @Override
    public String getName() {
        return "MultiblockMachine";
    }

    @Override
    public void writeConfig(ByteBuf dataStream, MekanismConfig config) {
        config.multiblock.write(dataStream);
    }

    @Override
    public void readConfig(ByteBuf dataStream, MekanismConfig destConfig) {
        destConfig.multiblock.read(dataStream);
    }

    @Override
    public void resetClient() {

    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MekanismMultiblockMachine.MODID)|| event.getModID().equals(Mekanism.MODID)) {
            proxy.loadConfiguration();
        }
    }

    @SubscribeEvent
    public void onBlacklistUpdate(MekanismAPI.BoxBlacklistEvent event) {
        MekanismAPI.addBoxBlacklist(MultiblockMachineBlocks.MultiblockGenerator,0); //Large Wind Generator
    }
}
