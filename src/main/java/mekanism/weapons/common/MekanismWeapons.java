package mekanism.weapons.common;

import io.netty.buffer.ByteBuf;
import mekanism.common.Mekanism;
import mekanism.common.Version;
import mekanism.common.base.IModule;
import mekanism.common.config.MekanismConfig;
import mekanism.weapons.common.entity.EntityMekaArrow;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid =MekanismWeapons.MODID,useMetadata = true,guiFactory = "mekanism.weapons.client.gui.WeaponsGuiFactory")
@Mod.EventBusSubscriber()
public class MekanismWeapons implements IModule {

    public static final String MODID = "mekaweapons";
    public static final int DATA_VERSION = 1;
    public static Version versionNumber = new Version(999, 999, 999);

    @SidedProxy(clientSide = "mekanism.weapons.client.MekanismWeaponsClientProxy",serverSide = "mekanism.weapons.common.MekanismWeaponsCommonProxy")
    public static  MekanismWeaponsCommonProxy proxy;

    @Mod.Instance(MODID)
    public static MekanismWeapons instance;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {

    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        MekanismWeaponsItems.registerItems(event.getRegistry());
    }


    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        proxy.registerItemRenders();
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        EntityRegistry.registerModEntity(new ResourceLocation(MODID, "meka_arrow"), EntityMekaArrow.class, "meka_arrow",0 ,MekanismWeapons.instance, 4, 20,true);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.loadConfiguration();
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Mekanism.modulesLoaded.add(this);
        MinecraftForge.EVENT_BUS.register(this);
        Mekanism.logger.info("Loaded 'Mekanism: Weapons' module.");
    }

    @Override
    public Version getVersion() {
        return versionNumber;
    }

    @Override
    public String getName() {
        return "Weapons";
    }

    @Override
    public void writeConfig(ByteBuf dataStream, MekanismConfig config) {
        config.weapons.write(dataStream);
    }

    @Override
    public void readConfig(ByteBuf dataStream, MekanismConfig destConfig) {
        destConfig.weapons.read(dataStream);
    }

    @Override
    public void resetClient() {

    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MekanismWeapons.MODID) || event.getModID().equals(Mekanism.MODID)) {
            proxy.loadConfiguration();
        }
    }


}
