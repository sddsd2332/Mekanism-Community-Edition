package mekanism.api;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mekanism.api.gear.IModuleHelper;
import mekanism.api.gear.ModuleData;
import mekanism.api.util.BlockInfo;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;

public class MekanismAPI {

    /**
     * The version of the api classes - may not always match the mod's version
     */
    public static final String API_VERSION = "9.8.1";
    public static final String MEKANISM_MODID = "mekanism";
    public static final Logger logger = LogManager.getLogger(MEKANISM_MODID + "_api");
    /**
     * Mekanism debug mode
     */
    public static boolean debug = false;
    //Add a BlockInfo value here if you don't want a certain block to be picked up by cardboard boxes
    private static Set<BlockInfo> cardboardBoxIgnore = new ObjectOpenHashSet<>();
    //Ignore all mod blocks
    private static Set<String> cardboardBoxModIgnore = new ObjectOpenHashSet<>();
    private static MekanismRecipeHelper helper = null;
    private static IForgeRegistry<ModuleData<?>> MODULE_REGISTRY;

    public static boolean isBlockCompatible(@Nonnull Block block, int meta) {
        if (cardboardBoxModIgnore.contains(Objects.requireNonNull(block.getRegistryName()).getNamespace())) {
            return false;
        }
        return cardboardBoxIgnore.stream().noneMatch(i -> i.block == block && (i.meta == OreDictionary.WILDCARD_VALUE || i.meta == meta));
    }

    public static void addBoxBlacklist(@Nullable Block block, int meta) {
        //Allow block to be null but don't do anything if it is
        if (block != null) {
            cardboardBoxIgnore.add(new BlockInfo(block, meta));
        }
    }

    public static void removeBoxBlacklist(@Nonnull Block block, int meta) {
        cardboardBoxIgnore.remove(new BlockInfo(block, meta));
    }

    public static Set<BlockInfo> getBoxIgnore() {
        return cardboardBoxIgnore;
    }

    /**
     * Get the instance of the recipe helper to directly add recipes.
     * <p>
     * Do NOT copy/repackage this method into your package, nor use the class directly as it may change.
     *
     * @return {@link MekanismRecipeHelper} The handler.
     */
    public static MekanismRecipeHelper recipeHelper() {
        if (helper == null) {
            try {
                helper = (MekanismRecipeHelper) Class.forName("mekanism.common.recipe.APIHandler").newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                LogManager.getLogger("MekanismAPI").error("Could not find API Handler", e);
            }
        }
        return helper;
    }

    public static void addBoxBlacklistMod(@Nonnull String modid) {
        cardboardBoxModIgnore.add(modid);
    }

    public static void removeBoxBlacklistMod(@Nonnull String modid) {
        cardboardBoxModIgnore.remove(modid);
    }

    public static Set<String> getBoxModIgnore() {
        return cardboardBoxModIgnore;
    }

    public static class BoxBlacklistEvent extends Event {

        public void blacklist(@Nonnull ResourceLocation blockLocation, int meta) {
            blacklist(ForgeRegistries.BLOCKS.getValue(blockLocation), meta);
        }

        public void blacklist(@Nullable Block block, int meta) {
            addBoxBlacklist(block, meta);
        }

        public void blacklistWildcard(@Nonnull ResourceLocation blockLocation) {
            blacklistWildcard(ForgeRegistries.BLOCKS.getValue(blockLocation));
        }

        public void blacklistWildcard(@Nullable Block block) {
            addBoxBlacklist(block, OreDictionary.WILDCARD_VALUE);
        }

        public void blacklistMod(@Nonnull String modid) {
            addBoxBlacklistMod(modid);
        }

        public void removeBlacklist(@Nonnull Block block, int meta) {
            removeBoxBlacklist(block, meta);
        }

        public void removeWildcardBlacklist(@Nonnull Block block) {
            removeBoxBlacklist(block, OreDictionary.WILDCARD_VALUE);
        }

        public void removeModBlacklist(@Nonnull String modid) {
            removeBoxBlacklistMod(modid);
        }
    }
    private static IModuleHelper MODULE_HELPER;

    /**
     * Gets Mekanism's {@link IModuleHelper} that provides various utility methods for implementing custom modules.
     */
    public static IModuleHelper getModuleHelper() {
        // Harmless race
        if (MODULE_HELPER == null) {
            try {
                Class<?> clazz = Class.forName("mekanism.common.content.gear.ModuleHelper");
                MODULE_HELPER = (IModuleHelper) clazz.getField("INSTANCE").get(null);
            } catch (ReflectiveOperationException ex) {
                logger.fatal("Error retrieving RadiationManager, Mekanism may be absent, damaged, or outdated.");
            }
        }
        return MODULE_HELPER;
    }
}
