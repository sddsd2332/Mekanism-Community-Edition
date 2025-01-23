package mekanism.common.content.gear;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import mekanism.api.NBTConstants;
import mekanism.api.gear.IHUDElement;
import mekanism.api.text.IHasTranslationKey;
import mekanism.api.text.ILangEntry;
import mekanism.api.text.TextComponentGroup;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.content.gear.mekasuit.*;
import mekanism.common.content.gear.mekatool.ModuleAttackAmplificationUnit;
import mekanism.common.content.gear.mekatool.ModuleExcavationEscalationUnit;
import mekanism.common.content.gear.mekatool.ModuleSilkTouchUnit;
import mekanism.common.content.gear.mekatool.ModuleTeleportationUnit;
import mekanism.common.content.gear.shared.ModuleEnergyUnit;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import mekanism.common.util.text.TextUtils;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Modules {

    private static final Map<String, ModuleData<?>> MODULES = new Object2ObjectOpenHashMap<>();
    private static final Map<Item, Set<ModuleData<?>>> SUPPORTED_MODULES = new Object2ObjectOpenHashMap<>();
    private static final Map<ModuleData<?>, Set<Item>> SUPPORTED_CONTAINERS = new Object2ObjectOpenHashMap<>();
    private static final Map<ModuleData<?>, Set<ModuleData<?>>> conflictingModules = new IdentityHashMap<>();

    // Shared
    public static final ModuleData<ModuleEnergyUnit> ENERGY_UNIT = register("energy_unit", MekanismLang.MODULE_ENERGY_UNIT, MekanismLang.DESCRIPTION_ENERGY_UNIT, ModuleEnergyUnit::new, 8).rarity(EnumRarity.UNCOMMON).setNoDisable();

    //Shared Armor
    public static final ModuleData<?> RADIATION_SHIELDING_UNIT = register("radiation_shielding_unit", MekanismLang.MODULE_RADIATION_SHIELDING_UNIT, MekanismLang.DESCRIPTION_RADIATION_SHIELDING_UNIT, ModuleRadiationShieldingUnit::new);

    // Meka-Tool
    public static final ModuleData<ModuleExcavationEscalationUnit> EXCAVATION_ESCALATION_UNIT = register("excavation_escalation_unit", MekanismLang.MODULE_EXCAVATION_ESCALATION_UNIT, MekanismLang.DESCRIPTION_EXCAVATION_ESCALATION_UNIT, ModuleExcavationEscalationUnit::new, 4).rarity(EnumRarity.UNCOMMON).setHandlesModeChange().setRendersHUD();
    public static final ModuleData<ModuleAttackAmplificationUnit> ATTACK_AMPLIFICATION_UNIT = register("attack_amplification_unit", MekanismLang.MODULE_ATTACK_AMPLIFICATION_UNIT, MekanismLang.DESCRIPTION_ATTACK_AMPLIFICATION_UNIT, ModuleAttackAmplificationUnit::new, 4).rarity(EnumRarity.UNCOMMON);
    public static final ModuleData<ModuleSilkTouchUnit> SILK_TOUCH_UNIT = register("silk_touch_unit", MekanismLang.MODULE_SILK_TOUCH_UNIT, MekanismLang.DESCRIPTION_SILK_TOUCH_UNIT, ModuleSilkTouchUnit::new).rarity(EnumRarity.RARE);
    public static final ModuleData<ModuleTeleportationUnit> TELEPORTATION_UNIT = register("teleportation_unit", MekanismLang.MODULE_TELEPORTATION_UNIT, MekanismLang.DESCRIPTION_TELEPORTATION_UNIT, ModuleTeleportationUnit::new).rarity(EnumRarity.EPIC).exclusive(ExclusiveFlag.INTERACT_ANY);

    // Helmet
    public static final ModuleData<ModuleElectrolyticBreathingUnit> ELECTROLYTIC_BREATHING_UNIT = register("electrolytic_breathing_unit", MekanismLang.MODULE_ELECTROLYTIC_BREATHING_UNIT, MekanismLang.DESCRIPTION_ELECTROLYTIC_BREATHING_UNIT, ModuleElectrolyticBreathingUnit::new, 4).rarity(EnumRarity.UNCOMMON);
    public static final ModuleData<ModuleInhalationPurificationUnit> INHALATION_PURIFICATION_UNIT = register("inhalation_purification_unit", MekanismLang.MODULE_INHALATION_PURIFICATION_UNIT, MekanismLang.DESCRIPTION_INHALATION_PURIFICATION_UNIT, ModuleInhalationPurificationUnit::new).rarity(EnumRarity.RARE);
    public static final ModuleData<ModuleVisionEnhancementUnit> VISION_ENHANCEMENT_UNIT = register("vision_enhancement_unit", MekanismLang.MODULE_VISION_ENHANCEMENT_UNIT, MekanismLang.DESCRIPTION_VISION_ENHANCEMENT_UNIT, ModuleVisionEnhancementUnit::new, 4).rarity(EnumRarity.RARE).setHandlesModeChange().setRendersHUD().setDisabledByDefault();
    public static final ModuleData<ModuleSolarRechargingUnit> SOLAR_RECHARGING_UNIT = register("solar_recharging_unit", MekanismLang.MODULE_SOLAR_RECHARGING_UNIT, MekanismLang.DESCRIPTION_SOLAR_RECHARGING_UNIT, ModuleSolarRechargingUnit::new, 8).rarity(EnumRarity.RARE);
    public static final ModuleData<ModuleNutritionalInjectionUnit> NUTRITIONAL_INJECTION_UNIT = register("nutritional_injection_unit", MekanismLang.MODULE_NUTRITIONAL_INJECTION_UNIT, MekanismLang.DESCRIPTION_NUTRITIONAL_INJECTION_UNIT, ModuleNutritionalInjectionUnit::new).rarity(EnumRarity.RARE).setRendersHUD();

    // Chestplate
    public static final ModuleData<ModuleJetpackUnit> JETPACK_UNIT = register("jetpack_unit", MekanismLang.MODULE_JETPACK_UNIT, MekanismLang.DESCRIPTION_JETPACK_UNIT, ModuleJetpackUnit::new).rarity(EnumRarity.RARE).setHandlesModeChange().setRendersHUD().exclusive(ExclusiveFlag.OVERRIDE_JUMP);
    public static final ModuleData<ModuleChargeDistributionUnit> CHARGE_DISTRIBUTION_UNIT = register("charge_distribution_unit", MekanismLang.MODULE_CHARGE_DISTRIBUTION_UNIT, MekanismLang.DESCRIPTION_CHARGE_DISTRIBUTION_UNIT, ModuleChargeDistributionUnit::new).rarity(EnumRarity.RARE);
    public static final ModuleData<ModuleGravitationalModulatingUnit> GRAVITATIONAL_MODULATING_UNIT = register("gravitational_modulating_unit", MekanismLang.MODULE_GRAVITATIONAL_MODULATING_UNIT, MekanismLang.DESCRIPTION_GRAVITATIONAL_MODULATING_UNIT, ModuleGravitationalModulatingUnit::new).rarity(EnumRarity.EPIC).setHandlesModeChange().setRendersHUD().exclusive(ExclusiveFlag.OVERRIDE_JUMP);

    // Pants
    public static final ModuleData<ModuleLocomotiveBoostingUnit> LOCOMOTIVE_BOOSTING_UNIT = register("locomotive_boosting_unit", MekanismLang.MODULE_LOCOMOTIVE_BOOSTING_UNIT, MekanismLang.DESCRIPTION_LOCOMOTIVE_BOOSTING_UNIT, ModuleLocomotiveBoostingUnit::new, 4).rarity(EnumRarity.RARE);
    public static final ModuleData<?> GYROSCOPIC_STABILIZATION_UNIT = register("gyroscopic_stabilization_unit", MekanismLang.MODULE_LOCOMOTIVE_BOOSTING_UNIT, MekanismLang.DESCRIPTION_LOCOMOTIVE_BOOSTING_UNIT, ModuleGyroscopicStabilizationUnit::new).rarity(EnumRarity.RARE);

    // Boots
    public static final ModuleData<ModuleHydraulicPropulsionUnit> HYDRAULIC_PROPULSION_UNIT = register("hydraulic_propulsion_unit", MekanismLang.MODULE_HYDRAULIC_PROPULSION_UNIT, MekanismLang.DESCRIPTION_HYDRAULIC_PROPULSION_UNIT, ModuleHydraulicPropulsionUnit::new, 4).rarity(EnumRarity.RARE);
    public static final ModuleData<ModuleMagneticAttractionUnit> MAGNETIC_ATTRACTION_UNIT = register("magnetic_attraction_unit", MekanismLang.MODULE_MAGNETIC_ATTRACTION_UNIT, MekanismLang.DESCRIPTION_MAGNETIC_ATTRACTION_UNIT, ModuleMagneticAttractionUnit::new, 4).rarity(EnumRarity.RARE);
    //  public static final ModuleData<ModuleFrostWalkerUnit> FROST_WALKER_UNIT = register("frost_walker_unit", ModuleFrostWalkerUnit::new, builder -> builder.maxStackSize(2).rarity(EnumRarity.RARE));

    public static void setSupported(Item containerItem, ModuleData<?>... types) {
        for (ModuleData<?> module : types) {
            SUPPORTED_MODULES.computeIfAbsent(containerItem, item -> new HashSet<>()).add(module);
        }
    }

    public static ModuleData<?> get(String name) {
        return MODULES.get(name);
    }

    public static Set<ModuleData<?>> getSupported(ItemStack container) {
        return getSupported(container.getItem());
    }

    private static Set<ModuleData<?>> getSupported(Item item) {
        return SUPPORTED_MODULES.getOrDefault(item, Collections.emptySet());
    }

    public static Set<Item> getSupported(ModuleData<?> type) {
        return SUPPORTED_CONTAINERS.getOrDefault(type, new HashSet<>());
    }

    public static Set<ModuleData<?>> getConflicting(ModuleData<?> typeProvider) {
        return conflictingModules.computeIfAbsent(typeProvider, moduleType -> {
            Set<ModuleData<?>> conflicting = new ReferenceOpenHashSet<>();
            for (Item item : getSupported(moduleType)) {
                for (ModuleData<?> other : getSupported(item)) {
                    if (moduleType != other && moduleType.isExclusive(other.getExclusiveFlags())) {
                        conflicting.add(other);
                    }
                }
            }
            return conflicting;
        });
    }

    public static boolean isEnabled(ItemStack container, ModuleData<?> type) {
        Module m = load(container, type);
        return m != null && m.isEnabled();
    }

    public static <MODULE extends Module> MODULE load(ItemStack container, ModuleData<MODULE> type) {
        if (!(container.getItem() instanceof IModuleContainerItem)) {
            return null;
        }

        NBTTagCompound modulesTag = ItemDataUtils.getCompound(container, NBTConstants.MODULES);
        return load(container, type, modulesTag);
    }

    private static <MODULE extends Module> MODULE load(ItemStack container, ModuleData<MODULE> type, NBTTagCompound modulesTag) {
        if (type == null || !modulesTag.hasKey(type.getName())) {
            return null;
        }

        MODULE module = type.get(container);
        if (module == null) {
            Mekanism.logger.error("Attempted to load unknown module type '{}' from container {}", type, container.getItem());
        } else {
            module.read(modulesTag.getCompoundTag(type.getName()));
        }
        return module;
    }

    public static List<Module> loadAll(ItemStack container) {
        if (!(container.getItem() instanceof IModuleContainerItem)) {
            return new ArrayList<>();
        }

        NBTTagCompound modulesTag = ItemDataUtils.getCompound(container, NBTConstants.MODULES);
        return modulesTag.getKeySet().stream().map(name -> load(container, MODULES.get(name), modulesTag)).collect(Collectors.toList());
    }

    public static <M extends Module> ModuleData<M> register(String name, ILangEntry langEntry, ILangEntry description, Supplier<M> moduleSupplier) {
        return register(name, langEntry, description, moduleSupplier, 1);
    }

    public static <M extends Module> ModuleData<M> register(String name, ILangEntry langEntry, ILangEntry description, Supplier<M> moduleSupplier, int maxStackSize) {
        ModuleData<M> data = new ModuleData<>(name, langEntry, description, moduleSupplier, maxStackSize);
        MODULES.put(name, data);
        return data;
    }

    public static Collection<ModuleData<?>> getAll() {
        return MODULES.values();
    }

    public static void processSupportedContainers() {
        for (Map.Entry<Item, Set<ModuleData<?>>> entry : SUPPORTED_MODULES.entrySet()) {
            for (ModuleData<?> data : entry.getValue()) {
                SUPPORTED_CONTAINERS.computeIfAbsent(data, d -> new HashSet<>()).add(entry.getKey());
            }
        }
    }

    public static void resetSupportedContainers() {
        SUPPORTED_CONTAINERS.clear();
    }


    public static IHUDElement hudElementEnabled(ResourceLocation icon, boolean enabled) {
        return hudElement(icon, LangUtils.transOnOffcap(enabled), enabled ? IHUDElement.HUDColor.REGULAR : IHUDElement.HUDColor.FADED);
    }


    public static IHUDElement hudElementPercent(ResourceLocation icon, double ratio) {
        return hudElement(icon,TextUtils.getPercent(ratio), ratio > 0.2 ? IHUDElement.HUDColor.REGULAR : (ratio > 0.1 ? IHUDElement.HUDColor.WARNING : IHUDElement.HUDColor.DANGER));
    }


    public static IHUDElement hudElement(ResourceLocation icon, String text, IHUDElement.HUDColor color) {
        return HUDElement.of(icon, text, HUDElement.HUDColor.from(color));
    }

    public static class ModuleData<MODULE extends Module> implements IHasTranslationKey {

        private final String name;
        private final ILangEntry langEntry;
        private final ILangEntry description;
        private final Supplier<MODULE> supplier;
        private final int maxStackSize;
        private ItemStack stack;
        private EnumRarity rarity = EnumRarity.COMMON;

        /**
         * Exclusive modules only work one-at-a-time; when one is enabled, others will be automatically disabled.
         */
        private int exclusive;
        private boolean handlesModeChange;
        private boolean rendersHUD;
        private boolean noDisable;
        private boolean disabledByDefault;

        private ModuleData(String name, ILangEntry langEntry, ILangEntry description, Supplier<MODULE> supplier, int maxStackSize) {
            this.name = name;
            this.langEntry = langEntry;
            this.description = description;
            this.supplier = supplier;
            this.maxStackSize = maxStackSize;
        }

        public int getMaxStackSize() {
            return maxStackSize;
        }

        public EnumRarity getRarity() {
            return rarity;
        }

        public ModuleData<MODULE> rarity(EnumRarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public MODULE get(ItemStack container) {
            MODULE module = supplier.get();
            module.init(this, container);
            return module;
        }

        public void setStack(Item item) {
            this.stack = new ItemStack(item);
        }

        public ItemStack getStack() {
            return stack;
        }

        public String getName() {
            return name;
        }

        public ITextComponent getDescription() {
            return new TextComponentGroup().translation(description.getTranslationKey());
        }

        public ILangEntry getLangEntry() {
            return langEntry;
        }

        public ModuleData<MODULE> exclusive(int mask) {
            exclusive = mask;
            return this;
        }

        public ModuleData<MODULE> exclusive(ExclusiveFlag... flags) {
            return exclusive(flags.length == 0 ? ExclusiveFlag.ANY : ExclusiveFlag.getCompoundMask(flags));
        }

        public boolean isExclusive(int mask) {
            return (exclusive & mask) != 0;
        }

        public final int getExclusiveFlags() {
            return exclusive;
        }

        public ModuleData<MODULE> setHandlesModeChange() {
            handlesModeChange = true;
            return this;
        }

        public boolean handlesModeChange() {
            return handlesModeChange;
        }

        public ModuleData<MODULE> setRendersHUD() {
            rendersHUD = true;
            return this;
        }

        public boolean rendersHUD() {
            return rendersHUD;
        }

        public ModuleData<MODULE> setNoDisable() {
            noDisable = true;
            return this;
        }

        public boolean isNoDisable() {
            return noDisable;
        }

        public ModuleData<MODULE> setDisabledByDefault() {
            disabledByDefault = true;
            return this;
        }

        public boolean isDisabledByDefault() {
            return disabledByDefault;
        }

        @Override
        public String getTranslationKey() {
            return langEntry.getTranslationKey();
        }
    }

    public enum ExclusiveFlag {
        /**
         * This flag indicates that this module uses interaction without a target
         */
        INTERACT_EMPTY,
        /**
         * This flag indicates that this module uses interaction with an entity
         */
        INTERACT_ENTITY,
        /**
         * This flag indicates that this module uses interaction with a block
         */
        INTERACT_BLOCK,
        /**
         * This flag indicates that this module changes what pressing jump does
         */
        OVERRIDE_JUMP,
        /**
         * This flag indicates that this module changes what blocks drop
         */
        OVERRIDE_DROPS;

        /**
         * Gets the mask for this flag
         */
        public int getMask() {
            return 1 << ordinal();
        }

        /**
         * Helper to get the mask of the combination of the given input flags
         *
         * @param flags {@link ExclusiveFlag Flags} to combine into a mask.
         *
         * @return Mask representing all the given {@link ExclusiveFlag flags}.
         */
        public static int getCompoundMask(ExclusiveFlag... flags) {
            return Arrays.stream(flags).mapToInt(ExclusiveFlag::getMask).reduce(NONE, (result, mask) -> result | mask);
        }

        /**
         * The mask for no flags
         */
        public static final int NONE = 0;

        /**
         * The mask for the combination of all flags
         */
        public static final int ANY = -1;

        /**
         * The mask for the combination of all interact flags
         */
        public static final int INTERACT_ANY = getCompoundMask(INTERACT_EMPTY, INTERACT_ENTITY, INTERACT_BLOCK);
    }
}
