package mekanism.common;

import mekanism.api.EnumColor;
import mekanism.common.content.gear.Modules;
import mekanism.common.item.*;
import mekanism.common.item.armor.ItemMekaSuitBodyArmor;
import mekanism.common.item.armor.ItemMekaSuitBoots;
import mekanism.common.item.armor.ItemMekaSuitHelmet;
import mekanism.common.item.armor.ItemMekaSuitPants;
import mekanism.common.tier.AlloyTier;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.LinkedHashMap;
import java.util.Map;

@ObjectHolder(Mekanism.MODID)
public class MekanismItems {

    public static final Item EnrichedAlloy = new ItemAlloy(AlloyTier.INFUSED).setRarity(EnumRarity.UNCOMMON);
    public static final Item ReinforcedAlloy = new ItemAlloy(AlloyTier.REINFORCED).setRarity(EnumRarity.RARE);
    public static final Item AtomicAlloy = new ItemAlloy(AlloyTier.ATOMIC).setRarity(EnumRarity.EPIC);
    public static final Item TeleportationCore = new ItemMekanism().setRarity(EnumRarity.RARE);
    public static final Item ElectrolyticCore = new ItemMekanism().setRarity(EnumRarity.UNCOMMON);
    public static final Item Substrate = new ItemMekanism();
    public static final Item Polyethene = new ItemHDPE();
    public static final Item BioFuel = new ItemMekanism();
    public static final Item ItemProxy = new ItemProxy();
    public static final Item EnrichedIron = new ItemMekanism();
    public static final Item CompressedCarbon = new ItemMekanism();
    public static final Item CompressedRedstone = new ItemMekanism();
    public static final Item CompressedDiamond = new ItemMekanism();
    public static final Item CompressedObsidian = new ItemMekanism();
    public static final Item SpeedUpgrade = new ItemUpgrade(Upgrade.SPEED);
    public static final Item EnergyUpgrade = new ItemUpgrade(Upgrade.ENERGY);
    public static final Item FilterUpgrade = new ItemUpgrade(Upgrade.FILTER);
    public static final Item MufflingUpgrade = new ItemUpgrade(Upgrade.MUFFLING);
    public static final Item GasUpgrade = new ItemUpgrade(Upgrade.GAS);
    public static final Item StoneGeneratorUpgrade = new ItemUpgrade(Upgrade.STONE_GENERATOR);
    public static final Item AnchorUpgrade = new ItemUpgrade(Upgrade.ANCHOR);
    public static final Item ThreadUpgrade = new ItemUpgrade(Upgrade.THREAD);
    public static final Item TierInstaller = new ItemTierInstaller();
    public static final ItemEnergized EnergyTablet = new ItemEnergized();
    public static final ItemRobit Robit = new ItemRobit();
    public static final ItemAtomicDisassembler AtomicDisassembler = new ItemAtomicDisassembler();
    public static final ItemPortableTeleporter PortableTeleporter = new ItemPortableTeleporter();
    public static final ItemConfigurator Configurator = new ItemConfigurator();
    public static final ItemNetworkReader NetworkReader = new ItemNetworkReader();
    public static final Item WalkieTalkie = new ItemWalkieTalkie();
    public static final ItemElectricBow ElectricBow = new ItemElectricBow();
    public static final ItemFlamethrower Flamethrower = new ItemFlamethrower();
    public static final ItemSeismicReader SeismicReader = new ItemSeismicReader();
    public static final Item Dictionary = new ItemDictionary();
    public static final ItemGaugeDropper GaugeDropper = new ItemGaugeDropper();
    public static final Item ConfigurationCard = new ItemConfigurationCard();
    public static final Item CraftingFormula = new ItemCraftingFormula();
    public static final ItemScubaTank ScubaTank = new ItemScubaTank();
    public static final ItemGasMask GasMask = new ItemGasMask();
    public static final ItemJetpack Jetpack = new ItemJetpack();
    public static final ItemJetpack ArmoredJetpack = new ItemJetpack();
    public static final ItemFreeRunners FreeRunners = new ItemFreeRunners();
    public static final Item Balloon = new ItemBalloon();

    //Multi-ID Items
    public static final Item OtherDust = new ItemOtherDust();
    public static final Item Dust = new ItemDust();
    public static final Item Sawdust = new ItemMekanism();
    public static final Item Salt = new ItemMekanism();
    public static final Item Ingot = new ItemIngot();
    public static final Item Nugget = new ItemNugget();
    public static final Item Clump = new ItemClump();
    public static final Item DirtyDust = new ItemDirtyDust();
    public static final Item Shard = new ItemShard();
    public static final Item Crystal = new ItemCrystal();
    public static final Item ControlCircuit = new ItemControlCircuit();

    /**
     * ADD START
     */

    public static final ItemMekTool MekTool = new ItemMekTool();
    public static final ItemCanteen Canteen = new ItemCanteen();
    public static final Item CosmicAlloy = new ItemMekanism();
    public static final Item PlutoniumPellet = new ItemMekanism().setNameColor(EnumColor.GREY);
    public static final Item AntimatterPellet = new ItemMekanism().setNameColor(EnumColor.PURPLE);
    public static final Item ReprocessedFissileFragment = new ItemMekanism().setRarity(EnumRarity.RARE);
    public static final Item YellowCakeUranium = new ItemMekanism().setRarity(EnumRarity.UNCOMMON);
    public static final Item PoloniumPellet = new ItemMekanism().setNameColor(EnumColor.INDIGO);
    public static final Item CosmicMatter = new ItemMekanism();
    public static final Item Scrap = new ItemMekanism();
    public static final Item ScrapBox = new ItemMekanism();
    public static final Item EmptyCrystals = new ItemMekanism();
    public static final Item FluoriteClump = new ItemMekanism();
    public static final ItemFreeRunners ArmoredFreeRunners = new ItemFreeRunners();
    //  public static final Item MekAsuitHelmet = new ItemOldMekAsuitHeadArmour();
    //public static final Item MekAsuitChestplate = new ItemOldMekAsuitBodyArmour();
    //   public static final Item MekAsuitLeggings = new ItemOldMekAsuitLegsArmour();
    //   public static final Item MekAsuitBoots = new ItemOldMekAsuitFeetArmour();

    public static final Item ModuleBase = new ItemMekanism();
    public static final Item ModuleUpgrade = new ItemModuleUpgrade();
    public static final Item MEKASUIT_HELMET = new ItemMekaSuitHelmet();
    public static final Item MEKASUIT_BODYARMOR = new ItemMekaSuitBodyArmor();
    public static final Item MEKASUIT_PANTS = new ItemMekaSuitPants();
    public static final Item MEKASUIT_BOOTS = new ItemMekaSuitBoots();

    public static final ItemModule MODULE_ENERGY = new ItemModule(Modules.ENERGY_UNIT);
    // public static final Item MODULE_LASER_DISSIPATION = new ItemModule(MekanismModules.LASER_DISSIPATION_UNIT);
    public static final ItemModule MODULE_RADIATION_SHIELDING = new ItemModule(Modules.RADIATION_SHIELDING_UNIT);
    public static final ItemModule MODULE_EXCAVATION_ESCALATION = new ItemModule(Modules.EXCAVATION_ESCALATION_UNIT);
    public static final ItemModule MODULE_ATTACK_AMPLIFICATION = new ItemModule(Modules.ATTACK_AMPLIFICATION_UNIT);
    public static final ItemModule MODULE_SILK_TOUCH = new ItemModule(Modules.SILK_TOUCH_UNIT);
    public static final ItemModule MODULE_TELEPORTATION = new ItemModule(Modules.TELEPORTATION_UNIT);
    public static final ItemModule MODULE_ELECTROLYTIC_BREATHING = new ItemModule(Modules.ELECTROLYTIC_BREATHING_UNIT);
    public static final ItemModule MODULE_INHALATION_PURIFICATION = new ItemModule(Modules.INHALATION_PURIFICATION_UNIT);
    public static final ItemModule MODULE_VISION_ENHANCEMENT = new ItemModule(Modules.VISION_ENHANCEMENT_UNIT);
    public static final ItemModule MODULE_SOLAR_RECHARGING = new ItemModule(Modules.SOLAR_RECHARGING_UNIT);
    public static final ItemModule MODULE_NUTRITIONAL_INJECTION = new ItemModule(Modules.NUTRITIONAL_INJECTION_UNIT);

    public static final ItemModule MODULE_JETPACK = new ItemModule(Modules.JETPACK_UNIT);
    public static final ItemModule MODULE_CHARGE_DISTRIBUTION = new ItemModule(Modules.CHARGE_DISTRIBUTION_UNIT);
    public static final ItemModule MODULE_GRAVITATIONAL_MODULATING = new ItemModule(Modules.GRAVITATIONAL_MODULATING_UNIT);
  //  public static final ItemModule MODULE_ELYTRA = new ItemModule(Modules.ELYTRA_UNIT);
    public static final ItemModule MODULE_LOCOMOTIVE_BOOSTING = new ItemModule(Modules.LOCOMOTIVE_BOOSTING_UNIT);
    public static final ItemModule MODULE_GYROSCOPIC_STABILIZATION = new ItemModule(Modules.GYROSCOPIC_STABILIZATION_UNIT);
    public static final ItemModule MODULE_HYDRAULIC_PROPULSION = new ItemModule(Modules.HYDRAULIC_PROPULSION_UNIT);
    public static final ItemModule MODULE_MAGNETIC_ATTRACTION = new ItemModule(Modules.MAGNETIC_ATTRACTION_UNIT);
  //  public static final ItemModule MODULE_FROST_WALKER = new ItemModule(Modules.FROST_WALKER_UNIT);
    /**
     * ADD END
     */

    /**
     * Adds and registers all items.
     *
     * @param registry Forge registry to add the items to
     */
    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.register(init(ElectricBow, "ElectricBow"));
        registry.register(init(Canteen, "Canteen"));
        registry.register(init(Dust, "Dust"));
        registry.register(init(Ingot, "Ingot"));
        registry.register(init(Nugget, "Nugget"));
        registry.register(init(EnergyTablet, "EnergyTablet"));
        registry.register(init(SpeedUpgrade, "SpeedUpgrade"));
        registry.register(init(EnergyUpgrade, "EnergyUpgrade"));
        registry.register(init(FilterUpgrade, "FilterUpgrade"));
        registry.register(init(MufflingUpgrade, "MufflingUpgrade"));
        registry.register(init(GasUpgrade, "GasUpgrade"));
        registry.register(init(AnchorUpgrade, "AnchorUpgrade"));
        registry.register(init(StoneGeneratorUpgrade, "StoneGeneratorUpgrade"));
        registry.register(init(ThreadUpgrade, "ThreadUpgrade"));
        registry.register(init(Robit, "Robit"));
        registry.register(init(AtomicDisassembler, "AtomicDisassembler"));
        registry.register(init(EnrichedAlloy, "EnrichedAlloy"));
        registry.register(init(ReinforcedAlloy, "ReinforcedAlloy"));
        registry.register(init(AtomicAlloy, "AtomicAlloy"));
        registry.register(init(CosmicAlloy, "CosmicAlloy"));
        registry.register(init(ItemProxy, "ItemProxy"));
        registry.register(init(ControlCircuit, "ControlCircuit"));
        registry.register(init(EnrichedIron, "EnrichedIron"));
        registry.register(init(CompressedCarbon, "CompressedCarbon"));
        registry.register(init(CompressedRedstone, "CompressedRedstone"));
        registry.register(init(CompressedDiamond, "CompressedDiamond"));
        registry.register(init(CompressedObsidian, "CompressedObsidian"));
        registry.register(init(PortableTeleporter, "PortableTeleporter"));
        registry.register(init(TeleportationCore, "TeleportationCore"));
        registry.register(init(Clump, "Clump"));
        registry.register(init(DirtyDust, "DirtyDust"));
        registry.register(init(Configurator, "Configurator"));
        registry.register(init(NetworkReader, "NetworkReader"));
        registry.register(init(WalkieTalkie, "WalkieTalkie"));
        registry.register(init(Jetpack, "Jetpack"));
        registry.register(init(Dictionary, "Dictionary"));
        registry.register(init(GasMask, "GasMask"));
        registry.register(init(ScubaTank, "ScubaTank"));
        registry.register(init(Balloon, "Balloon"));
        registry.register(init(Shard, "Shard"));
        registry.register(init(ElectrolyticCore, "ElectrolyticCore"));
        registry.register(init(Sawdust, "Sawdust"));
        registry.register(init(Salt, "Salt"));
        registry.register(init(Crystal, "Crystal"));
        registry.register(init(FreeRunners, "FreeRunners"));
        registry.register(init(ArmoredJetpack, "ArmoredJetpack"));
        registry.register(init(ConfigurationCard, "ConfigurationCard"));
        registry.register(init(CraftingFormula, "CraftingFormula"));
        registry.register(init(SeismicReader, "SeismicReader"));
        registry.register(init(Substrate, "Substrate"));
        registry.register(init(Polyethene, "Polyethene"));
        registry.register(init(BioFuel, "BioFuel"));
        registry.register(init(Flamethrower, "Flamethrower"));
        registry.register(init(GaugeDropper, "GaugeDropper"));
        registry.register(init(TierInstaller, "TierInstaller"));
        registry.register(init(OtherDust, "OtherDust"));

        /**
         * ADD START
         */

        registry.register(init(CosmicMatter, "CosmicMatter"));
        registry.register(init(Scrap, "Scrap"));
        registry.register(init(ScrapBox, "ScrapBox"));
        registry.register(init(EmptyCrystals, "EmptyCrystals"));
        registry.register(init(FluoriteClump, "FluoriteClump"));
        registry.register(init(PlutoniumPellet, "PlutoniumPellet"));
        registry.register(init(AntimatterPellet, "AntimatterPellet"));
        registry.register(init(ReprocessedFissileFragment, "ReprocessedFissileFragment"));
        registry.register(init(YellowCakeUranium, "YellowCakeUranium"));
        registry.register(init(PoloniumPellet, "PoloniumPellet"));
        registry.register(init(ArmoredFreeRunners, "ArmoredFreeRunners"));
        registry.register(init(MekTool, "MekTool"));
        //  registry.register(init(MekAsuitHelmet, "MekAsuitHelmet"));
        //  registry.register(init(MekAsuitChestplate, "MekAsuitChestplate"));
        //  registry.register(init(MekAsuitLeggings, "MekAsuitLeggings"));
        //  registry.register(init(MekAsuitBoots, "MekAsuitBoots"));
        registry.register(init(ModuleBase, "ModuleBase"));
        registry.register(init(ModuleUpgrade, "ModuleUpgrade"));

        registry.register(init(MEKASUIT_HELMET, "mekasuit_helmet"));
        registry.register(init(MEKASUIT_BODYARMOR, "mekasuit_bodyarmor"));
        registry.register(init(MEKASUIT_PANTS, "mekasuit_pants"));
        registry.register(init(MEKASUIT_BOOTS, "mekasuit_boots"));
        registry.register(initModule(MODULE_ENERGY));
        registry.register(initModule(MODULE_RADIATION_SHIELDING));
        registry.register(initModule(MODULE_EXCAVATION_ESCALATION));
        registry.register(initModule(MODULE_ATTACK_AMPLIFICATION));
        registry.register(initModule(MODULE_SILK_TOUCH));
        registry.register(initModule(MODULE_TELEPORTATION));
        registry.register(initModule(MODULE_ELECTROLYTIC_BREATHING));
        registry.register(initModule(MODULE_INHALATION_PURIFICATION));
        registry.register(initModule(MODULE_VISION_ENHANCEMENT));
        registry.register(initModule(MODULE_SOLAR_RECHARGING));
        registry.register(initModule(MODULE_NUTRITIONAL_INJECTION));
        registry.register(initModule(MODULE_JETPACK));
        registry.register(initModule(MODULE_CHARGE_DISTRIBUTION));
        registry.register(initModule(MODULE_GRAVITATIONAL_MODULATING));
     //   registry.register(initModule(MODULE_ELYTRA));
        registry.register(initModule(MODULE_LOCOMOTIVE_BOOSTING));
        registry.register(initModule(MODULE_GYROSCOPIC_STABILIZATION));
        registry.register(initModule(MODULE_HYDRAULIC_PROPULSION));
        registry.register(initModule(MODULE_MAGNETIC_ATTRACTION));
    //    registry.register(initModule(MODULE_FROST_WALKER));
        /**
         * ADD END
         */

    }

    public static Item initModule(ItemModule item) {
        String name = "module_" + item.getModuleData().getName();
        return item.setTranslationKey(name).setRegistryName(new ResourceLocation(Mekanism.MODID, name));
    }


    public static Item init(Item item, String name) {
        return item.setTranslationKey(name).setRegistryName(new ResourceLocation(Mekanism.MODID, name));
    }
}
