package mekanism.common.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MixinConfig {

    public static boolean GaiaIIIenabledBaubles, VoidHerrscherenabledBaubles;
    public static String[] GaiaIIIValidMods;
    public static String[] VoidHerrscherValidItem = new String[] {};
    public static String[] VoidHerrscherValidMods;

    public static Configuration config;
    public static final Set<String> GaiaIIImatchModId = new HashSet<>(), VoidHerrschermatchModId = new HashSet<>();

    public static void initConfig(File file) {
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig() {
        Property property;
        config.load();
        //盖亚三缴械modid白名单
        property = config.get(Configuration.CATEGORY_GENERAL, "GaiaIII Disarm Whitelist Mod id", new String[]{"botania", "extrabotany", "minecraft"}, "Whitelist the Mod Id associated with the disarming of ExtraBotany.");
        GaiaIIIValidMods = property.getStringList();
        GaiaIIImatchModId.addAll(Arrays.stream(GaiaIIIValidMods).collect(Collectors.toSet()));

        //盖亚三饰品栏相关
        property = config.get(Configuration.CATEGORY_GENERAL,"GaiaIII Baubles checked",true,"Gaia Guardian III was allowed to inspect Baubles");
        GaiaIIIenabledBaubles = property.getBoolean();

        //空律缴械物品白名单
        property = config.get(Configuration.CATEGORY_GENERAL, "Void Herrscher Whitelist Item", new String[]{}, "Herrscher of the Void disarming items whitelist,syntax: modid:name or modid:name:meta");
        VoidHerrscherValidItem = property.getStringList();
        //空律缴械modid白名单
        property = config.get(Configuration.CATEGORY_GENERAL, "Herrscher of the Void Whitelist Mod id", new String[]{"botania", "extrabotany", "minecraft"}, "Whitelist the Mod Id associated with the disarming of ExtraBotany.");
        VoidHerrscherValidMods = property.getStringList();
        VoidHerrschermatchModId.addAll(Arrays.stream(VoidHerrscherValidMods).collect(Collectors.toSet()));
        //空律饰品栏相关
        property = config.get(Configuration.CATEGORY_GENERAL,"Herrscher of the Void Baubles checked",true,"Herrscher of the Void was allowed to inspect Baubles");
        VoidHerrscherenabledBaubles = property.getBoolean();

        config.save();
    }
}
