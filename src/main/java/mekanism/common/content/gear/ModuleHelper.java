package mekanism.common.content.gear;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import mcp.MethodsReturnNonnullByDefault;
import mekanism.api.gear.*;
import mekanism.api.providers.IModuleDataProvider;
import mekanism.common.item.ItemModule;
import mekanism.common.util.LangUtils;
import mekanism.common.util.text.TextUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModuleHelper implements IModuleHelper {

    public static final ModuleHelper INSTANCE = new ModuleHelper();
    private final Map<Item, Set<ModuleData<?>>> supportedModules = new Object2ObjectOpenHashMap<>(5);
    private final Map<ModuleData<?>, Set<Item>> supportedContainers = new Object2ObjectOpenHashMap<>();
    private final Map<ModuleData<?>, Set<ModuleData<?>>> conflictingModules = new IdentityHashMap<>();

    @Override
    public Item createModuleItem(IModuleDataProvider<?> moduleDataProvider) {
        return new ItemModule(moduleDataProvider);
    }

    @Override
    public Set<ModuleData<?>> getSupported(ItemStack container) {
        return supportedModules.getOrDefault(container.getItem(), Collections.emptySet());
    }

    @Override
    public Set<Item> getSupported(IModuleDataProvider<?> typeProvider) {
        return supportedContainers.getOrDefault(typeProvider.getModuleData(), Collections.emptySet());
    }

    private Set<ModuleData<?>> getSupported(Item item) {
        return supportedModules.getOrDefault(item, Collections.emptySet());
    }

    @Override
    public Set<ModuleData<?>> getConflicting(IModuleDataProvider<?> typeProvider) {
        return conflictingModules.computeIfAbsent(typeProvider.getModuleData(), moduleType -> {
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

    @Override
    public boolean isEnabled(ItemStack container, IModuleDataProvider<?> typeProvider) {
        IModule<?> m = load(container, typeProvider);
        return m != null && m.isEnabled();
    }

    @Override
    public @Nullable <MODULE extends ICustomModule<MODULE>> IModule<MODULE> load(ItemStack container, IModuleDataProvider<MODULE> typeProvider) {
        return null;
    }

    @Override
    public List<? extends IModule<?>> loadAll(ItemStack container) {
        return Arrays.asList();
    }

    @Override
    public <MODULE extends ICustomModule<?>> List<? extends IModule<? extends MODULE>> loadAll(ItemStack container, Class<MODULE> moduleClass) {
        return Arrays.asList();
    }

    @Override
    public List<ModuleData<?>> loadAllTypes(ItemStack container) {
        return Arrays.asList();
    }

    @Override
    public IHUDElement hudElement(ResourceLocation icon, String text, IHUDElement.HUDColor color) {
        return HUDElement.of(icon, text, HUDElement.HUDColor.from(color));
    }

    @Override
    public IHUDElement hudElementEnabled(ResourceLocation icon, boolean enabled) {
        return hudElement(icon, LangUtils.transOnOff(enabled), enabled ? IHUDElement.HUDColor.REGULAR : IHUDElement.HUDColor.FADED);
    }

    @Override
    public IHUDElement hudElementPercent(ResourceLocation icon, double ratio) {
        return hudElement(icon, TextUtils.getPercent(ratio), ratio > 0.2 ? IHUDElement.HUDColor.REGULAR : (ratio > 0.1 ? IHUDElement.HUDColor.WARNING : IHUDElement.HUDColor.DANGER));
    }


}
