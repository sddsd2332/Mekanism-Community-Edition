package mekanism.common;

import com.brandon3055.draconicevolution.DEFeatures;
import com.brandon3055.draconicevolution.api.fusioncrafting.IFusionRecipe;
import com.brandon3055.draconicevolution.api.fusioncrafting.SimpleFusionRecipe;
import com.brandon3055.draconicevolution.api.itemupgrade.IUpgradableItem;
import com.brandon3055.draconicevolution.api.itemupgrade.UpgradeHelper;
import com.brandon3055.draconicevolution.lib.RecipeManager;
import mekanism.api.EnumColor;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.OreGas;
import mekanism.api.infuse.InfuseObject;
import mekanism.api.infuse.InfuseRegistry;
import mekanism.api.infuse.InfuseType;
import mekanism.common.block.states.BlockStateMachine;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.tier.InductionCellTier;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.StackUtils;
import net.foxmcloud.draconicadditions.DAFeatures;
import net.foxmcloud.draconicadditions.items.armor.ChaoticArmor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class MekanismRecipe {

    /**
     * Adds all in-game crafting, smelting and machine recipes.
     */
    public static void addRecipes() {
        //Furnace Recipes
        GameRegistry.addSmelting(new ItemStack(MekanismBlocks.OreBlock, 1, 0), new ItemStack(MekanismItems.Ingot, 1, 1), 1.0F);
        GameRegistry.addSmelting(new ItemStack(MekanismBlocks.OreBlock, 1, 1), new ItemStack(MekanismItems.Ingot, 1, 5), 1.0F);
        GameRegistry.addSmelting(new ItemStack(MekanismBlocks.OreBlock, 1, 2), new ItemStack(MekanismItems.Ingot, 1, 6), 1.0F);
        GameRegistry.addSmelting(new ItemStack(MekanismBlocks.OreBlock, 1, 4), new ItemStack(MekanismItems.Ingot, 1, 7), 1.0F);
        GameRegistry.addSmelting(new ItemStack(MekanismBlocks.OreBlock, 1, 5), new ItemStack(MekanismItems.Ingot, 1, 8), 1.0F);
        GameRegistry.addSmelting(new ItemStack(MekanismItems.Dust, 1, Resource.OSMIUM.ordinal()), new ItemStack(MekanismItems.Ingot, 1, 1), 0.0F);
        GameRegistry.addSmelting(new ItemStack(MekanismItems.Dust, 1, Resource.IRON.ordinal()), new ItemStack(Items.IRON_INGOT), 0.0F);
        GameRegistry.addSmelting(new ItemStack(MekanismItems.Dust, 1, Resource.GOLD.ordinal()), new ItemStack(Items.GOLD_INGOT), 0.0F);
        GameRegistry.addSmelting(new ItemStack(MekanismItems.OtherDust, 1, 1), new ItemStack(MekanismItems.Ingot, 1, 4), 0.0F);
        GameRegistry.addSmelting(new ItemStack(MekanismItems.Dust, 1, Resource.COPPER.ordinal()), new ItemStack(MekanismItems.Ingot, 1, 5), 0.0F);
        GameRegistry.addSmelting(new ItemStack(MekanismItems.Dust, 1, Resource.TIN.ordinal()), new ItemStack(MekanismItems.Ingot, 1, 6), 0.0F);
        GameRegistry.addSmelting(new ItemStack(MekanismItems.Dust, 1, Resource.LEAD.ordinal()), new ItemStack(MekanismItems.Ingot, 1, 7), 0.0F);
        GameRegistry.addSmelting(new ItemStack(MekanismItems.Dust, 1, Resource.URANIUM.ordinal()), new ItemStack(MekanismItems.Ingot, 1, 8), 0.0F);

        //Enrichment Chamber Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.ENRICHMENT_CHAMBER)) {
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Blocks.OBSIDIAN), new ItemStack(MekanismItems.OtherDust, 4, 6));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Items.COAL, 1, 0), new ItemStack(MekanismItems.CompressedCarbon));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Items.COAL, 1, 1), new ItemStack(MekanismItems.CompressedCarbon));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Items.REDSTONE), new ItemStack(MekanismItems.CompressedRedstone));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Blocks.MOSSY_COBBLESTONE), new ItemStack(Blocks.COBBLESTONE));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Blocks.STONE), new ItemStack(Blocks.STONEBRICK, 1, 2));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Blocks.SAND), new ItemStack(Blocks.GRAVEL));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.COBBLESTONE));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Items.GUNPOWDER), new ItemStack(Items.FLINT));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Blocks.STONEBRICK, 1, 2), new ItemStack(Blocks.STONEBRICK, 1, 0));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Blocks.STONEBRICK, 1, 0), new ItemStack(Blocks.STONEBRICK, 1, 3));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Blocks.STONEBRICK, 1, 1), new ItemStack(Blocks.STONEBRICK, 1, 0));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Blocks.GLOWSTONE), new ItemStack(Items.GLOWSTONE_DUST, 4));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Blocks.CLAY), new ItemStack(Items.CLAY_BALL, 4));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(MekanismBlocks.SaltBlock), new ItemStack(MekanismItems.Salt, 4));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Items.DIAMOND), new ItemStack(MekanismItems.CompressedDiamond));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(MekanismItems.Polyethene, 3, 0), new ItemStack(MekanismItems.Polyethene, 1, 2));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(MekanismItems.OtherDust, 1, 7), new ItemStack(MekanismItems.FluoriteClump, 1));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(Items.COAL, 1, 1), new ItemStack(MekanismItems.OtherDust, 1, 8));
            RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(MekanismBlocks.OreBlock, 1, 3), new ItemStack(MekanismItems.FluoriteClump, 6));
            for (ItemStack ingotUranium : OreDictionary.getOres("ingotUranium", false)) {
                RecipeHandler.addEnrichmentChamberRecipe(StackUtils.size(ingotUranium, 1), new ItemStack(MekanismItems.YellowCakeUranium, 2));
            }

            for (int i = 0; i < EnumColor.DYES.length; i++) {
                RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(MekanismBlocks.PlasticBlock, 1, i), new ItemStack(MekanismBlocks.SlickPlasticBlock, 1, i));
            }
        }

        //Combiner recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.COMBINER)) {
            RecipeHandler.addCombinerRecipe(new ItemStack(Items.FLINT), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.GRAVEL));
            RecipeHandler.addCombinerRecipe(new ItemStack(Items.COAL, 3), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.COAL_ORE));
        }

        //Osmium Compressor Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.OSMIUM_COMPRESSOR)) {
            RecipeHandler.addOsmiumCompressorRecipe(new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(MekanismItems.Ingot, 1, 3));
            RecipeHandler.addOsmiumCompressorRecipe(new ItemStack(MekanismItems.Scrap, 64), new ItemStack(MekanismItems.ScrapBox, 1));
        }

        //Crusher Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.CRUSHER)) {
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(MekanismItems.Dust, 1, Resource.IRON.ordinal()));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.GOLD_INGOT), new ItemStack(MekanismItems.Dust, 1, Resource.GOLD.ordinal()));
            RecipeHandler.addCrusherRecipe(new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SAND));
            RecipeHandler.addCrusherRecipe(new ItemStack(Blocks.STONE), new ItemStack(Blocks.COBBLESTONE));
            RecipeHandler.addCrusherRecipe(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.GRAVEL));
            RecipeHandler.addCrusherRecipe(new ItemStack(Blocks.STONEBRICK, 1, 2), new ItemStack(Blocks.STONE));
            RecipeHandler.addCrusherRecipe(new ItemStack(Blocks.STONEBRICK, 1, 0), new ItemStack(Blocks.STONEBRICK, 1, 2));
            RecipeHandler.addCrusherRecipe(new ItemStack(Blocks.STONEBRICK, 1, 3), new ItemStack(Blocks.STONEBRICK, 1, 0));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.FLINT), new ItemStack(Items.GUNPOWDER));
            RecipeHandler.addCrusherRecipe(new ItemStack(Blocks.SANDSTONE), new ItemStack(Blocks.SAND, 2));
            RecipeHandler.addCrusherRecipe(new ItemStack(Blocks.RED_SANDSTONE), new ItemStack(Blocks.SAND, 2, 1));
            RecipeHandler.addCrusherRecipe(new ItemStack(MekanismItems.FluoriteClump, 1), new ItemStack(MekanismItems.OtherDust, 1, 7));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.COAL, 1, 1), new ItemStack(MekanismItems.OtherDust, 1, 8));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.DIAMOND, 1), new ItemStack(MekanismItems.OtherDust, 1, 0));

            for (int i = 0; i < 16; i++) {
                RecipeHandler.addCrusherRecipe(new ItemStack(Blocks.WOOL, 1, i), new ItemStack(Items.STRING, 4));
            }

            //BioFuel Crusher Recipes
            RecipeHandler.addCrusherRecipe(new ItemStack(Blocks.TALLGRASS), new ItemStack(MekanismItems.BioFuel, 4));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.REEDS), new ItemStack(MekanismItems.BioFuel, 2));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(MekanismItems.BioFuel, 2));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.WHEAT), new ItemStack(MekanismItems.BioFuel, 4));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.PUMPKIN_SEEDS), new ItemStack(MekanismItems.BioFuel, 2));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.MELON_SEEDS), new ItemStack(MekanismItems.BioFuel, 2));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.APPLE), new ItemStack(MekanismItems.BioFuel, 4));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.BREAD), new ItemStack(MekanismItems.BioFuel, 4));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.POTATO), new ItemStack(MekanismItems.BioFuel, 4));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.CARROT), new ItemStack(MekanismItems.BioFuel, 4));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.ROTTEN_FLESH), new ItemStack(MekanismItems.BioFuel, 2));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.MELON), new ItemStack(MekanismItems.BioFuel, 4));
            RecipeHandler.addCrusherRecipe(new ItemStack(Blocks.PUMPKIN), new ItemStack(MekanismItems.BioFuel, 6));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.BAKED_POTATO), new ItemStack(MekanismItems.BioFuel, 4));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.POISONOUS_POTATO), new ItemStack(MekanismItems.BioFuel, 4));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.BEETROOT), new ItemStack(MekanismItems.BioFuel, 4));
            RecipeHandler.addCrusherRecipe(new ItemStack(Items.BEETROOT_SEEDS), new ItemStack(MekanismItems.BioFuel, 2));
            RecipeHandler.addCrusherRecipe(new ItemStack(Blocks.CACTUS), new ItemStack(MekanismItems.BioFuel, 2));
        }

        //Purification Chamber Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.PURIFICATION_CHAMBER)) {
            RecipeHandler.addPurificationChamberRecipe(new ItemStack(Blocks.GRAVEL), new ItemStack(Items.FLINT));
        }

        //Chemical Injection Chamber Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.CHEMICAL_INJECTION_CHAMBER)) {
            RecipeHandler.addChemicalInjectionChamberRecipe(new ItemStack(Blocks.DIRT), MekanismFluids.Water, new ItemStack(Blocks.CLAY));
            RecipeHandler.addChemicalInjectionChamberRecipe(new ItemStack(Blocks.HARDENED_CLAY), MekanismFluids.Water, new ItemStack(Blocks.CLAY));
            RecipeHandler.addChemicalInjectionChamberRecipe(new ItemStack(Items.BRICK), MekanismFluids.Water, new ItemStack(Items.CLAY_BALL));
            RecipeHandler.addChemicalInjectionChamberRecipe(new ItemStack(Items.GUNPOWDER), MekanismFluids.HydrogenChloride, new ItemStack(MekanismItems.OtherDust, 1, 3));
            RecipeHandler.addChemicalInjectionChamberRecipe(new ItemStack(MekanismItems.PlutoniumPellet, 1), MekanismFluids.HydrogenChloride, new ItemStack(MekanismItems.ReprocessedFissileFragment, 1));
        }

        //Precision Sawmill Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.PRECISION_SAWMILL)) {
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.LADDER, 3), new ItemStack(Items.STICK, 7));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.TORCH, 4), new ItemStack(Items.STICK), new ItemStack(Items.COAL), 1);
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.JUKEBOX), new ItemStack(Blocks.PLANKS, 8), new ItemStack(Items.DIAMOND), 1);
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.BOOKSHELF), new ItemStack(Blocks.PLANKS, 6), new ItemStack(Items.BOOK, 3), 1);
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.WOODEN_PRESSURE_PLATE), new ItemStack(Blocks.PLANKS, 2));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.NOTEBLOCK), new ItemStack(Blocks.PLANKS, 8), new ItemStack(Items.REDSTONE), 1);
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.REDSTONE_TORCH), new ItemStack(Items.STICK), new ItemStack(Items.REDSTONE), 1);
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.CRAFTING_TABLE), new ItemStack(Blocks.PLANKS, 4));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.CHEST), new ItemStack(Blocks.PLANKS, 8));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.TRAPPED_CHEST), new ItemStack(Blocks.PLANKS, 8), new ItemStack(Blocks.TRIPWIRE_HOOK), 0.75);
            //Boats
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Items.BOAT), new ItemStack(Blocks.PLANKS, 5));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Items.SPRUCE_BOAT), new ItemStack(Blocks.PLANKS, 5, 1));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Items.BIRCH_BOAT), new ItemStack(Blocks.PLANKS, 5, 2));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Items.JUNGLE_BOAT), new ItemStack(Blocks.PLANKS, 5, 3));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.PLANKS, 5, 4));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Items.DARK_OAK_BOAT), new ItemStack(Blocks.PLANKS, 5, 5));
            //Beds
            for (int i = 0; i < 16; i++) {
                RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Items.BED, 1, i), new ItemStack(Blocks.PLANKS, 3), new ItemStack(Blocks.WOOL, 3, i), 1);
            }
            //Doors
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.TRAPDOOR), new ItemStack(Blocks.PLANKS, 1));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Items.OAK_DOOR), new ItemStack(Blocks.PLANKS, 2, 0));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Items.SPRUCE_DOOR), new ItemStack(Blocks.PLANKS, 2, 1));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Items.BIRCH_DOOR), new ItemStack(Blocks.PLANKS, 2, 2));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Items.JUNGLE_DOOR), new ItemStack(Blocks.PLANKS, 2, 3));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Items.ACACIA_DOOR), new ItemStack(Blocks.PLANKS, 2, 4));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Items.DARK_OAK_DOOR), new ItemStack(Blocks.PLANKS, 2, 5));
            //Fences
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.OAK_FENCE), new ItemStack(Items.STICK, 3));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.SPRUCE_FENCE), new ItemStack(Items.STICK, 3));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.BIRCH_FENCE), new ItemStack(Items.STICK, 3));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.JUNGLE_FENCE), new ItemStack(Items.STICK, 3));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.ACACIA_FENCE), new ItemStack(Items.STICK, 3));
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.DARK_OAK_FENCE), new ItemStack(Items.STICK, 3));
            //Fence Gates
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.OAK_FENCE_GATE), new ItemStack(Blocks.PLANKS, 2, 0), new ItemStack(Items.STICK, 4), 1);
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.SPRUCE_FENCE_GATE), new ItemStack(Blocks.PLANKS, 2, 1), new ItemStack(Items.STICK, 4), 1);
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.BIRCH_FENCE_GATE), new ItemStack(Blocks.PLANKS, 2, 2), new ItemStack(Items.STICK, 4), 1);
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.JUNGLE_FENCE_GATE), new ItemStack(Blocks.PLANKS, 2, 3), new ItemStack(Items.STICK, 4), 1);
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.ACACIA_FENCE_GATE), new ItemStack(Blocks.PLANKS, 2, 4), new ItemStack(Items.STICK, 4), 1);
            RecipeHandler.addPrecisionSawmillRecipe(new ItemStack(Blocks.DARK_OAK_FENCE_GATE), new ItemStack(Blocks.PLANKS, 2, 5), new ItemStack(Items.STICK, 4), 1);
        }

        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.METALLURGIC_INFUSER)) {
            InfuseType carbon = InfuseRegistry.get("CARBON");
            InfuseType bio = InfuseRegistry.get("BIO");
            InfuseType redstone = InfuseRegistry.get("REDSTONE");
            InfuseType fungi = InfuseRegistry.get("FUNGI");
            InfuseType diamond = InfuseRegistry.get("DIAMOND");
            InfuseType obsidian = InfuseRegistry.get("OBSIDIAN");

            //Infuse objects
            InfuseRegistry.registerInfuseObject(new ItemStack(MekanismItems.BioFuel), new InfuseObject(bio, 5));
            InfuseRegistry.registerInfuseObject(new ItemStack(Items.COAL, 1, 0), new InfuseObject(carbon, 10));
            InfuseRegistry.registerInfuseObject(new ItemStack(Items.COAL, 1, 1), new InfuseObject(carbon, 20));
            InfuseRegistry.registerInfuseObject(new ItemStack(MekanismItems.OtherDust, 1, 8), new InfuseObject(carbon, 20));
            InfuseRegistry.registerInfuseObject(new ItemStack(Blocks.COAL_BLOCK, 1, 0), new InfuseObject(carbon, 90));
            InfuseRegistry.registerInfuseObject(new ItemStack(MekanismBlocks.BasicBlock, 1, 3), new InfuseObject(carbon, 180));
            InfuseRegistry.registerInfuseObject(new ItemStack(MekanismItems.CompressedCarbon), new InfuseObject(carbon, 80));
            InfuseRegistry.registerInfuseObject(new ItemStack(Items.REDSTONE), new InfuseObject(redstone, 10));
            InfuseRegistry.registerInfuseObject(new ItemStack(Blocks.REDSTONE_BLOCK), new InfuseObject(redstone, 90));
            InfuseRegistry.registerInfuseObject(new ItemStack(MekanismItems.CompressedRedstone), new InfuseObject(redstone, 80));
            InfuseRegistry.registerInfuseObject(new ItemStack(Blocks.RED_MUSHROOM), new InfuseObject(fungi, 10));
            InfuseRegistry.registerInfuseObject(new ItemStack(Blocks.BROWN_MUSHROOM), new InfuseObject(fungi, 10));
            InfuseRegistry.registerInfuseObject(new ItemStack(MekanismItems.CompressedDiamond), new InfuseObject(diamond, 80));
            InfuseRegistry.registerInfuseObject(new ItemStack(MekanismItems.CompressedObsidian), new InfuseObject(obsidian, 80));

            //Metallurgic Infuser Recipes
            RecipeHandler.addMetallurgicInfuserRecipe(carbon, 10, new ItemStack(Items.IRON_INGOT), new ItemStack(MekanismItems.EnrichedIron));
            RecipeHandler.addMetallurgicInfuserRecipe(carbon, 10, new ItemStack(MekanismItems.EnrichedIron), new ItemStack(MekanismItems.OtherDust, 1, 1));
            for (ItemStack steel : OreDictionary.getOres("ingotSteel", false)) {
                RecipeHandler.addMetallurgicInfuserRecipe(redstone, 10, StackUtils.size(steel, 1), new ItemStack(MekanismItems.EnrichedAlloy));
            }
            RecipeHandler.addMetallurgicInfuserRecipe(fungi, 10, new ItemStack(Blocks.DIRT), new ItemStack(Blocks.MYCELIUM));
            RecipeHandler.addMetallurgicInfuserRecipe(bio, 10, new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.MOSSY_COBBLESTONE));
            RecipeHandler.addMetallurgicInfuserRecipe(bio, 10, new ItemStack(Blocks.STONEBRICK, 1, 0), new ItemStack(Blocks.STONEBRICK, 1, 1));
            RecipeHandler.addMetallurgicInfuserRecipe(bio, 10, new ItemStack(Blocks.SAND), new ItemStack(Blocks.DIRT));
            RecipeHandler.addMetallurgicInfuserRecipe(bio, 10, new ItemStack(Blocks.DIRT), new ItemStack(Blocks.DIRT, 1, 2));
            RecipeHandler.addMetallurgicInfuserRecipe(diamond, 10, new ItemStack(MekanismItems.EnrichedAlloy), new ItemStack(MekanismItems.ReinforcedAlloy));
            RecipeHandler.addMetallurgicInfuserRecipe(obsidian, 10, new ItemStack(MekanismItems.ReinforcedAlloy), new ItemStack(MekanismItems.AtomicAlloy));
        }

        //Chemical Infuser Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.CHEMICAL_INFUSER)) {
            RecipeHandler.addChemicalInfuserRecipe(new GasStack(MekanismFluids.Oxygen, 1), new GasStack(MekanismFluids.SulfurDioxide, 2), new GasStack(MekanismFluids.SulfurTrioxide, 2));
            RecipeHandler.addChemicalInfuserRecipe(new GasStack(MekanismFluids.SulfurTrioxide, 1), new GasStack(MekanismFluids.Water, 1), new GasStack(MekanismFluids.SulfuricAcid, 1));
            RecipeHandler.addChemicalInfuserRecipe(new GasStack(MekanismFluids.Hydrogen, 1), new GasStack(MekanismFluids.Chlorine, 1), new GasStack(MekanismFluids.HydrogenChloride, 1));
            RecipeHandler.addChemicalInfuserRecipe(new GasStack(MekanismFluids.Deuterium, 1), new GasStack(MekanismFluids.Tritium, 1), new GasStack(MekanismFluids.FusionFuel, 2));
            RecipeHandler.addChemicalInfuserRecipe(new GasStack(MekanismFluids.HydrofluoricAcid, 1), new GasStack(MekanismFluids.Uraniumoxide, 1), new GasStack(MekanismFluids.UraniumHexafluoride, 1));
            RecipeHandler.addChemicalInfuserRecipe(new GasStack(MekanismFluids.Oxygen, 2), new GasStack(MekanismFluids.Water, 1), new GasStack(MekanismFluids.OxygenEnrichedWater, 1));
            RecipeHandler.addChemicalInfuserRecipe(new GasStack(MekanismFluids.OxygenEnrichedWater, 1), new GasStack(MekanismFluids.NutritionalPaste, 10), new GasStack(MekanismFluids.NutrientSolution, 1));
        }

        //Electrolytic Separator Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.ELECTROLYTIC_SEPARATOR)) {
            RecipeHandler.addElectrolyticSeparatorRecipe(FluidRegistry.getFluidStack("water", 2), 2 * MekanismConfig.current().general.FROM_H2.val(),
                    new GasStack(MekanismFluids.Hydrogen, 2), new GasStack(MekanismFluids.Oxygen, 1));
            RecipeHandler.addElectrolyticSeparatorRecipe(FluidRegistry.getFluidStack("brine", 10), 2 * MekanismConfig.current().general.FROM_H2.val(),
                    new GasStack(MekanismFluids.Sodium, 1), new GasStack(MekanismFluids.Chlorine, 1));
            RecipeHandler.addElectrolyticSeparatorRecipe(FluidRegistry.getFluidStack("heavywater", 2), MekanismConfig.current().usage.heavyWaterElectrolysis.val(),
                    new GasStack(MekanismFluids.Deuterium, 2), new GasStack(MekanismFluids.Oxygen, 1));
        }

        //Thermal Evaporation Plant Recipes
        RecipeHandler.addThermalEvaporationRecipe(FluidRegistry.getFluidStack("water", 10), FluidRegistry.getFluidStack("brine", 1));
        RecipeHandler.addThermalEvaporationRecipe(FluidRegistry.getFluidStack("brine", 10), FluidRegistry.getFluidStack("liquidlithium", 1));
        RecipeHandler.addThermalEvaporationRecipe(FluidRegistry.getFluidStack("liquidsodium", 1000), FluidRegistry.getFluidStack("liquidsuperheatedsodium", 1));

        //Chemical Crystallizer Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.CHEMICAL_CRYSTALLIZER)) {
            RecipeHandler.addChemicalCrystallizerRecipe(new GasStack(MekanismFluids.Lithium, 100), new ItemStack(MekanismItems.OtherDust, 1, 4));
            RecipeHandler.addChemicalCrystallizerRecipe(new GasStack(MekanismFluids.Brine, 15), new ItemStack(MekanismItems.Salt));
            RecipeHandler.addChemicalCrystallizerRecipe(new GasStack(MekanismFluids.Antimatter, 1000), new ItemStack(MekanismItems.AntimatterPellet, 1));
        }
        //Chemical Washer Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.CHEMICAL_WASHER)) {
            RecipeHandler.addChemicalWasherRecipe(new GasStack(MekanismFluids.FissileFuel, 1000), new GasStack(MekanismFluids.NuclearWaste, 1));
        }
        //Chemical Dissolution Chamber Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.CHEMICAL_DISSOLUTION_CHAMBER)) {
            for (ItemStack ore : OreDictionary.getOres("dustFluorite")) {
                RecipeHandler.addChemicalDissolutionChamberRecipe(StackUtils.size(ore, 1), new GasStack(MekanismFluids.HydrofluoricAcid, 100));
            }
        }

        //T4 Processing Recipes
        for (Gas gas : GasRegistry.getRegisteredGasses()) {
            if (gas instanceof OreGas oreGas && !oreGas.isClean()) {
                if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.CHEMICAL_WASHER)) {
                    RecipeHandler.addChemicalWasherRecipe(new GasStack(oreGas, 1), new GasStack(oreGas.getCleanGas(), 1));
                }

                //do the crystallizer only if it's one of ours!
                Resource gasResource = Resource.getFromName(oreGas.getName());
                if (gasResource != null && MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.CHEMICAL_CRYSTALLIZER)) {
                    RecipeHandler.addChemicalCrystallizerRecipe(new GasStack(oreGas.getCleanGas(), 200), new ItemStack(MekanismItems.Crystal, 1, gasResource.ordinal()));
                }
            }
        }

        //Pressurized Reaction Chamber Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.PRESSURIZED_REACTION_CHAMBER)) {
            RecipeHandler.addPRCRecipe(new ItemStack(MekanismItems.BioFuel, 2), new FluidStack(FluidRegistry.WATER, 10), new GasStack(MekanismFluids.Hydrogen, 100),
                    new ItemStack(MekanismItems.Substrate), new GasStack(MekanismFluids.Ethene, 100), 0, 100);
            RecipeHandler.addPRCRecipe(new ItemStack(MekanismItems.Substrate), new FluidStack(MekanismFluids.Ethene.getFluid(), 50),
                    new GasStack(MekanismFluids.Oxygen, 10), new ItemStack(MekanismItems.Polyethene), new GasStack(MekanismFluids.Oxygen, 5), 1000, 60);
            RecipeHandler.addPRCRecipe(new ItemStack(MekanismItems.Substrate), new FluidStack(FluidRegistry.WATER, 200), new GasStack(MekanismFluids.Ethene, 100),
                    new ItemStack(MekanismItems.Substrate, 8), new GasStack(MekanismFluids.Oxygen, 10), 200, 400);
            RecipeHandler.addPRCRecipe(new ItemStack(Items.COAL, 1, OreDictionary.WILDCARD_VALUE), new FluidStack(FluidRegistry.WATER, 100), new GasStack(MekanismFluids.Oxygen, 100),
                    new ItemStack(MekanismItems.OtherDust, 1, 3), new GasStack(MekanismFluids.Hydrogen, 100), 0, 100);
            RecipeHandler.addPRCRecipe(new ItemStack(MekanismItems.OtherDust, 1, 7), new FluidStack(FluidRegistry.WATER, 10000), new GasStack(MekanismFluids.Plutonium, 1000),
                    new ItemStack(MekanismItems.PlutoniumPellet, 1), new GasStack(MekanismFluids.SpentNuclearWaste, 1000), 100000, 2000);
            RecipeHandler.addPRCRecipe(new ItemStack(MekanismItems.OtherDust, 1, 7), new FluidStack(FluidRegistry.WATER, 10000), new GasStack(MekanismFluids.Polonium, 1000),
                    new ItemStack(MekanismItems.PoloniumPellet, 1), new GasStack(MekanismFluids.SpentNuclearWaste, 1000), 100000, 2000);
            RecipeHandler.addPRCRecipe(new ItemStack(MekanismItems.CosmicMatter, 64), FluidRegistry.getFluidStack("liquidsuperheatedsodium", 10000), new GasStack(MekanismFluids.UnstableDimensional, 10000),
                    ItemStack.EMPTY, new GasStack(MekanismFluids.Antimatter, 100), 100000, 24000);
            RecipeHandler.addPRCRecipe(new ItemStack(MekanismItems.ScrapBox, 64), FluidRegistry.getFluidStack("liquidfusionfuel", 10000), new GasStack(MekanismFluids.UnstableDimensional, 10000),
                    new ItemStack(MekanismItems.EmptyCrystals, 1), new GasStack(MekanismFluids.SpentNuclearWaste, 1000), 100000, 10000);
            RecipeHandler.addPRCRecipe(new ItemStack(MekanismItems.EmptyCrystals, 64), FluidRegistry.getFluidStack("liquidfusionfuel", 10000), new GasStack(MekanismFluids.UnstableDimensional, 10000), new ItemStack(MekanismItems.CosmicMatter), new GasStack(MekanismFluids.NuclearWaste, 10), 80000, 2000);
        }

        //Solar Neutron Activator Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.SOLAR_NEUTRON_ACTIVATOR)) {
            RecipeHandler.addSolarNeutronRecipe(new GasStack(MekanismFluids.Lithium, 1), new GasStack(MekanismFluids.Tritium, 1));
            RecipeHandler.addSolarNeutronRecipe(new GasStack(MekanismFluids.NuclearWaste, 10), new GasStack(MekanismFluids.Polonium, 1));
        }

        //Fuel Gases
        FuelHandler.addGas(MekanismFluids.Hydrogen, 1, MekanismConfig.current().general.FROM_H2.val());

        //Chemical Oxidizer Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.CHEMICAL_OXIDIZER)) {
            RecipeHandler.addChemicalOxidizerRecipe(new ItemStack(MekanismItems.ReprocessedFissileFragment, 1), new GasStack(MekanismFluids.FissileFuel, 2000));
            RecipeHandler.addChemicalOxidizerRecipe(new ItemStack(MekanismItems.YellowCakeUranium, 1), new GasStack(MekanismFluids.Uraniumoxide, 250));
        }

        /**
         * ADD START
         */
        //Isotopic Centrifuge Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.ISOTOPIC_CENTRIFUGE)) {
            RecipeHandler.addIsotopicRecipe(new GasStack(MekanismFluids.UraniumHexafluoride, 1), new GasStack(MekanismFluids.FissileFuel, 1));
            RecipeHandler.addIsotopicRecipe(new GasStack(MekanismFluids.NuclearWaste, 10), new GasStack(MekanismFluids.Plutonium, 1));
        }

        //Nutritional Liquifier Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.NUTRITIONAL_LIQUIFIER)) {
            for (Item item : ForgeRegistries.ITEMS) {
                if (item instanceof ItemFood itemFood) {
                    try {
                        ItemStack stack = new ItemStack(itemFood, 1, OreDictionary.WILDCARD_VALUE);
                        if (!stack.isEmpty() && itemFood.getHealAmount(stack) > 0) {
                            RecipeHandler.addNutritionalLiquifierRecipe(stack, new GasStack(MekanismFluids.NutritionalPaste, itemFood.getHealAmount(stack) * 50));
                        }
                    } catch (Exception ignored) {
                        Mekanism.logger.error("Unable to add recipe for Nutritional Liquifier because {} is entered incorrectly", itemFood);
                    }
                }
            }
            RecipeHandler.addNutritionalLiquifierRecipe(new ItemStack(Items.CAKE), new GasStack(MekanismFluids.NutritionalPaste, 6 * 50));
        }


        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.ORGANIC_FARM)) {
            //Farm log
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Blocks.SAPLING, 1, 0), MekanismFluids.NutrientSolution, new ItemStack(Blocks.LOG, 6, 0), new ItemStack(Blocks.SAPLING, 1, 0), MekanismConfig.current().mekce.log.val());
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Blocks.SAPLING, 1, 1), MekanismFluids.NutrientSolution, new ItemStack(Blocks.LOG, 6, 1), new ItemStack(Blocks.SAPLING, 1, 1), MekanismConfig.current().mekce.log.val());
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Blocks.SAPLING, 1, 2), MekanismFluids.NutrientSolution, new ItemStack(Blocks.LOG, 6, 2), new ItemStack(Blocks.SAPLING, 1, 2), MekanismConfig.current().mekce.log.val());
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Blocks.SAPLING, 1, 3), MekanismFluids.NutrientSolution, new ItemStack(Blocks.LOG, 6, 3), new ItemStack(Blocks.SAPLING, 1, 3), MekanismConfig.current().mekce.log.val());
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Blocks.SAPLING, 1, 4), MekanismFluids.NutrientSolution, new ItemStack(Blocks.LOG2, 6, 0), new ItemStack(Blocks.SAPLING, 1, 4), MekanismConfig.current().mekce.log.val());
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Blocks.SAPLING, 1, 5), MekanismFluids.NutrientSolution, new ItemStack(Blocks.LOG2, 6, 1), new ItemStack(Blocks.SAPLING, 1, 5), MekanismConfig.current().mekce.log.val());
            // Farm seed
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Items.NETHER_WART, 1), MekanismFluids.NutrientSolution, new ItemStack(Items.NETHER_WART, 3));
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Items.REEDS, 1), MekanismFluids.NutrientSolution, new ItemStack(Items.REEDS, 3));
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Items.CARROT, 1), MekanismFluids.NutrientSolution, new ItemStack(Items.CARROT, 3));
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Items.DYE, 1, 3), MekanismFluids.NutrientSolution, new ItemStack(Items.DYE, 3, 3));
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Items.POTATO, 1), MekanismFluids.NutrientSolution, new ItemStack(Items.POTATO, 3), new ItemStack(Items.POISONOUS_POTATO, 1), 0.15);
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Items.PUMPKIN_SEEDS, 1), MekanismFluids.NutrientSolution, new ItemStack(Blocks.PUMPKIN, 1), new ItemStack(Items.PUMPKIN_SEEDS, 1), MekanismConfig.current().mekce.seed.val());
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Items.MELON_SEEDS, 1), MekanismFluids.NutrientSolution, new ItemStack(Blocks.MELON_BLOCK, 1), new ItemStack(Items.MELON_SEEDS, 1), MekanismConfig.current().mekce.seed.val());
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Items.WHEAT_SEEDS, 1), MekanismFluids.NutrientSolution, new ItemStack(Items.WHEAT, 3), new ItemStack(Items.WHEAT_SEEDS, 1), MekanismConfig.current().mekce.seed.val());
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Items.BEETROOT_SEEDS, 1), MekanismFluids.NutrientSolution, new ItemStack(Items.BEETROOT, 3), new ItemStack(Items.BEETROOT_SEEDS, 1), MekanismConfig.current().mekce.seed.val());
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Blocks.CHORUS_FLOWER, 1), MekanismFluids.NutrientSolution, new ItemStack(Items.CHORUS_FRUIT, 3), new ItemStack(Blocks.CHORUS_FLOWER, 1), MekanismConfig.current().mekce.log.val());
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Blocks.BROWN_MUSHROOM, 1), MekanismFluids.NutrientSolution, new ItemStack(Blocks.BROWN_MUSHROOM, 3));
            RecipeHandler.addOrganicFarmRecipe(new ItemStack(Blocks.RED_MUSHROOM, 1), MekanismFluids.NutrientSolution, new ItemStack(Blocks.RED_MUSHROOM, 3));
        }

        //Antiprotonic Nucleosynthesizer Recipes
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.ANTIPROTONIC_NUCLEOSYNTHESIZER)) {
            RecipeHandler.addNucleosynthesizerRecipe(new ItemStack(Blocks.WOOL, 1, 8), new GasStack(MekanismFluids.Antimatter, 500), new ItemStack(Blocks.QUARTZ_BLOCK), 0, 500);
            RecipeHandler.addNucleosynthesizerRecipe(new ItemStack(Blocks.WOOL, 1, 4), new GasStack(MekanismFluids.Antimatter, 500), new ItemStack(Blocks.GLOWSTONE), 0, 500);
            RecipeHandler.addNucleosynthesizerRecipe(new ItemStack(MekanismItems.Ingot, 1, 6), new GasStack(MekanismFluids.Antimatter, 500), new ItemStack(Items.IRON_INGOT), 0, 500);
            RecipeHandler.addNucleosynthesizerRecipe(new ItemStack(Items.COAL), new GasStack(MekanismFluids.Antimatter, 500), new ItemStack(Items.DIAMOND), 0, 1000);
            RecipeHandler.addNucleosynthesizerRecipe(new ItemStack(Items.DIAMOND), new GasStack(MekanismFluids.Antimatter, 500), new ItemStack(Items.EMERALD), 0, 1000);
            RecipeHandler.addNucleosynthesizerRecipe(new ItemStack(Blocks.WOOL, 1, 14), new GasStack(MekanismFluids.Antimatter, 500), new ItemStack(Blocks.REDSTONE_BLOCK), 0, 500);
            RecipeHandler.addNucleosynthesizerRecipe(new ItemStack(Blocks.WOOL, 1, 11), new GasStack(MekanismFluids.Antimatter, 500), new ItemStack(Blocks.LAPIS_BLOCK), 0, 500);
            RecipeHandler.addNucleosynthesizerRecipe(new ItemStack(MekanismItems.EmptyCrystals, 64), new GasStack(MekanismFluids.UnstableDimensional, 10000), new ItemStack(MekanismItems.CosmicMatter), 80000, 2000);
        }

        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.STAMPING)) {
            RecipeHandler.addStampingRecipe(new ItemStack(Blocks.DIRT), new ItemStack(Blocks.SAND, 2));
        }

        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.ROLLING)) {
            RecipeHandler.addRollingRecipe(new ItemStack(Blocks.DIRT), new ItemStack(Blocks.SAND, 2));
        }

        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.BRUSHED)) {
            RecipeHandler.addBrushedRecipe(new ItemStack(Blocks.DIRT), new ItemStack(Blocks.SAND, 2));
        }
        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.TURNING)) {
            RecipeHandler.addTurningRecipe(new ItemStack(Blocks.DIRT), new ItemStack(Blocks.SAND, 2));
        }

        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.ALLOY)) {
            RecipeHandler.addAlloyRecipe(new ItemStack(MekanismItems.Ingot, 3, 5), new ItemStack(MekanismItems.Ingot, 1, 6), new ItemStack(MekanismItems.Ingot, 4, 2));
        }

        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.CELL_EXTRACTOR)) {
            RecipeHandler.addCellExtractorRecipe(new ItemStack(Blocks.IRON_ORE), new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Items.IRON_INGOT), 1);
        }

        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.CELL_SEPARATOR)) {
            RecipeHandler.addCellSeparatorRecipe(new ItemStack(Blocks.IRON_ORE), new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Items.IRON_INGOT), 1);
        }

        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.RECYCLER)) {
            RecipeHandler.addRecyclerRecipe(new ItemStack(Blocks.DIRT), new ItemStack(MekanismItems.Scrap, 1), 1F / 6F);
        }

        if (MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.AMBIENT_ACCUMULATOR) || MekanismConfig.current().general.machinesManager.isEnabled(BlockStateMachine.MachineType.AMBIENT_ACCUMULATOR_ENERGY)) {
            RecipeHandler.addAmbientGas(0, new GasStack(MekanismFluids.UnstableDimensional, 1), 1F / 5F);
        }

        /**
         * ADD END
         */
        if (Mekanism.hooks.DraconicEvolution) {
            addDRrecipes();
            if (Mekanism.hooks.DraconicAdditions) {
                addDRAdditionsrecipes();
            }
        }
    }

    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public static void addDRrecipes() {
        addDRSimpleFusionRecipe(new ItemStack(MekanismItems.ModuleUpgrade, 1, 24), new ItemStack(MekanismItems.ModuleBase), 256000000L, 3,
                DEFeatures.chaoticCore, "PoloniumPellet", new ItemStack(MekanismItems.Polyethene, 1, 2), DEFeatures.chaosShard, new ItemStack(MekanismItems.Polyethene, 1, 2), "PoloniumPellet", DEFeatures.chaoticCore,
                "PoloniumPellet", MekanismItems.CosmicMatter, "alloyUltimate", "circuitUltimate", "alloyUltimate", MekanismItems.CosmicMatter, "PoloniumPellet",
                new ItemStack(MekanismItems.Polyethene, 1, 2), "alloyUltimate", "PlutoniumPellet", new ItemStack(DEFeatures.toolUpgrade, 1, 8), "PlutoniumPellet", "alloyUltimate", new ItemStack(MekanismItems.Polyethene, 1, 2),
                DEFeatures.chaosShard, "circuitUltimate", new ItemStack(DEFeatures.toolUpgrade, 1, 9), new ItemStack(DEFeatures.toolUpgrade, 1, 9), "circuitUltimate", DEFeatures.chaosShard,
                new ItemStack(MekanismItems.Polyethene, 1, 2), "alloyUltimate", "PlutoniumPellet", new ItemStack(DEFeatures.toolUpgrade, 1, 8), "PlutoniumPellet", "alloyUltimate", new ItemStack(MekanismItems.Polyethene, 1, 2),
                "PoloniumPellet", MekanismItems.CosmicMatter, "alloyUltimate", "circuitUltimate", "alloyUltimate", MekanismItems.CosmicMatter, "PoloniumPellet",
                DEFeatures.chaoticCore, "PoloniumPellet", new ItemStack(MekanismItems.Polyethene, 1, 2), DEFeatures.chaosShard, new ItemStack(MekanismItems.Polyethene, 1, 2), "PoloniumPellet", DEFeatures.chaoticCore);
    }

    @Optional.Method(modid = MekanismHooks.DraconicAdditions_MOD_ID)
    public static void addDRAdditionsrecipes() {
        ItemStack inductionCell = new ItemStack(MekanismBlocks.BasicBlock2, 1, 3);
        if (!inductionCell.hasTagCompound()) {
            inductionCell.setTagCompound(new NBTTagCompound());
        }
        inductionCell.getTagCompound().setInteger("tier", 3);
        inductionCell.getTagCompound().setTag("mekData", new NBTTagCompound());
        ItemDataUtils.setDouble(inductionCell, "energyStored", InductionCellTier.ULTIMATE.getMaxEnergy());


        ItemStack chaoticHelm = new ItemStack(DAFeatures.chaoticHelm);
        for (String upgrade :  DAFeatures.chaoticHelm.getValidUpgrades(chaoticHelm)) {
            UpgradeHelper.setUpgradeLevel(chaoticHelm, upgrade, DAFeatures.chaoticHelm.getMaxUpgradeLevel(chaoticHelm, upgrade));
        }
        DAFeatures.chaoticHelm.modifyEnergy(chaoticHelm, DAFeatures.chaoticHelm.getMaxEnergyStored(chaoticHelm));
        chaoticHelm.getTagCompound().setBoolean("isStable", true);

        ItemStack chaoticChest = new ItemStack(DAFeatures.chaoticChest);
        for (String upgrade :  DAFeatures.chaoticChest.getValidUpgrades(chaoticChest)) {
            UpgradeHelper.setUpgradeLevel(chaoticChest, upgrade, DAFeatures.chaoticChest.getMaxUpgradeLevel(chaoticChest, upgrade));
        }
        DAFeatures.chaoticChest.modifyEnergy(chaoticChest, DAFeatures.chaoticChest.getMaxEnergyStored(chaoticChest));
        chaoticChest.getTagCompound().setBoolean("isStable", true);

        ItemStack chaoticLegs = new ItemStack(DAFeatures.chaoticLegs);
        for (String upgrade :  DAFeatures.chaoticLegs.getValidUpgrades(chaoticLegs)) {
            UpgradeHelper.setUpgradeLevel(chaoticLegs, upgrade, DAFeatures.chaoticLegs.getMaxUpgradeLevel(chaoticLegs, upgrade));
        }
        DAFeatures.chaoticLegs.modifyEnergy(chaoticLegs, DAFeatures.chaoticLegs.getMaxEnergyStored(chaoticLegs));
        chaoticLegs.getTagCompound().setBoolean("isStable", true);

        ItemStack chaoticBoots = new ItemStack(DAFeatures.chaoticBoots);
        for (String upgrade :  DAFeatures.chaoticBoots.getValidUpgrades(chaoticBoots)) {
            UpgradeHelper.setUpgradeLevel(chaoticBoots, upgrade, DAFeatures.chaoticBoots.getMaxUpgradeLevel(chaoticBoots, upgrade));
        }
        DAFeatures.chaoticBoots.modifyEnergy(chaoticBoots, DAFeatures.chaoticBoots.getMaxEnergyStored(chaoticBoots));
        chaoticBoots.getTagCompound().setBoolean("isStable", true);

        addDRSimpleFusionRecipe(new ItemStack(MekanismItems.ModuleUpgrade, 1, 25), new ItemStack(MekanismItems.ModuleBase), Integer.MAX_VALUE * 53L, 3,
                DAFeatures.chaosHeart, inductionCell, DAFeatures.chaosStabilizerCore, inductionCell, DAFeatures.chaosHeart,
                DAFeatures.chaosHeart, chaoticHelm, "PlutoniumPellet", chaoticChest, DAFeatures.chaosHeart,
                inductionCell, MekanismItems.CosmicMatter, "PoloniumPellet", new ItemStack(MekanismItems.ModuleUpgrade, 1, 23), "PoloniumPellet", MekanismItems.CosmicMatter, inductionCell,
                "PoloniumPellet", new ItemStack(MekanismItems.ModuleUpgrade, 1, 23), new ItemStack(MekanismItems.ModuleUpgrade, 1, 24), new ItemStack(MekanismItems.ModuleUpgrade, 1, 23), "PoloniumPellet",
                DAFeatures.chaosStabilizerCore, "PlutoniumPellet", new ItemStack(MekanismItems.ModuleUpgrade, 1, 23), new ItemStack(MekanismItems.ModuleUpgrade, 1, 24), "circuitUltimate", new ItemStack(MekanismItems.ModuleUpgrade, 1, 24), new ItemStack(MekanismItems.ModuleUpgrade, 1, 23), "PlutoniumPellet", DAFeatures.chaosStabilizerCore,
                "PoloniumPellet", new ItemStack(MekanismItems.ModuleUpgrade, 1, 23), new ItemStack(MekanismItems.ModuleUpgrade, 1, 24), new ItemStack(MekanismItems.ModuleUpgrade, 1, 23), "PoloniumPellet",
                inductionCell, MekanismItems.CosmicMatter, "PoloniumPellet", new ItemStack(MekanismItems.ModuleUpgrade, 1, 23), "PoloniumPellet", MekanismItems.CosmicMatter, inductionCell,
                DAFeatures.chaosHeart, chaoticLegs, "PlutoniumPellet", chaoticBoots, DAFeatures.chaosHeart,
                DAFeatures.chaosHeart, inductionCell, DAFeatures.chaosStabilizerCore, inductionCell, DAFeatures.chaosHeart
        );
    }


    @Optional.Method(modid = MekanismHooks.DraconicEvolution_MOD_ID)
    public static void addDRSimpleFusionRecipe(ItemStack out, ItemStack input, long energyCost, int craftingTier, Object... ingredients) {
        IFusionRecipe recipe = new SimpleFusionRecipe(out, input, energyCost, craftingTier, ingredients);
        RecipeManager.FUSION_REGISTRY.add(recipe);
    }


}
