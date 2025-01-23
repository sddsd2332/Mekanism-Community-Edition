package mekanism.common.content.gear;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import mekanism.api.MekanismAPI;
import mekanism.api.NBTConstants;
import mekanism.api.gear.IHUDElement;
import mekanism.api.text.TextComponentGroup;
import mekanism.common.base.IModule;
import mekanism.common.content.gear.Modules.ModuleData;
import mekanism.common.item.interfaces.IItemHUDProvider;
import mekanism.common.util.ItemDataUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

public interface IModuleContainerItem extends IItemHUDProvider {

    default List<Module> getModules(ItemStack stack) {
        return Modules.loadAll(stack);
    }

    default <MODULE extends Module> MODULE getModule(ItemStack stack, ModuleData<MODULE> type) {
        return Modules.load(stack, type);
    }

    default boolean hasModule(ItemStack stack, ModuleData<?> type) {
        return ItemDataUtils.getCompound(stack, NBTConstants.MODULES).hasKey(type.getName());
    }

    default boolean isModuleEnabled(ItemStack stack, ModuleData<?> type) {
        return hasModule(stack, type) && getModule(stack, type).isEnabled();
    }

    default void removeModule(ItemStack stack, ModuleData<?> type) {
        if (hasModule(stack, type)) {
            Module module = getModule(stack, type);
            if (module.getInstalledCount() > 1) {
                module.setInstalledCount(module.getInstalledCount() - 1);
                module.save(null);
                module.onRemoved(false);
            } else {
                ItemDataUtils.getCompound(stack, NBTConstants.MODULES).removeTag(type.getName());
                module.onRemoved(true);
            }
        }
    }

    default void addModule(ItemStack stack, ModuleData<?> type) {
        if (hasModule(stack, type)) {
            Module module = getModule(stack, type);
            module.setInstalledCount(module.getInstalledCount() + 1);
            module.save(null);
            module.onAdded(false);
        } else {
            if (!ItemDataUtils.hasData(stack, NBTConstants.MODULES, NBT.TAG_COMPOUND)) {
                ItemDataUtils.setCompound(stack, NBTConstants.MODULES, new NBTTagCompound());
            }
            ItemDataUtils.getCompound(stack, NBTConstants.MODULES).setTag(type.getName(), new NBTTagCompound());
            Modules.load(stack, type).onAdded(true);
        }
    }

    default void setModule(ItemStack stack, ModuleData<?> type) {
            if (!ItemDataUtils.hasData(stack, NBTConstants.MODULES, NBT.TAG_COMPOUND)) {
                ItemDataUtils.setCompound(stack, NBTConstants.MODULES, new NBTTagCompound());
            }
            ItemDataUtils.getCompound(stack, NBTConstants.MODULES).setTag(type.getName(), new NBTTagCompound());
            ItemDataUtils.getCompound(stack, NBTConstants.MODULES).getCompoundTag(type.getName()).setInteger(NBTConstants.AMOUNT, type.getMaxStackSize());
            Modules.load(stack, type).save(null);

    }

    @Override
    default void addHUDStrings(List<String> list, EntityPlayer player, ItemStack stack, EntityEquipmentSlot slotType) {
        for (Module module : Modules.loadAll(stack)) {
            if (module.renderHUD()) {
                module.addHUDStrings(player, list);
            }
        }
    }

    default List<IHUDElement> getHUDElements(EntityPlayer player, ItemStack stack) {
        List<IHUDElement> ret = new ArrayList<>();
        for (Module module : Modules.loadAll(stack)) {
            if (module.renderHUD()) {
                module.addHUDElements(player, ret);
            }
        }
        return ret;
    }

    static boolean hasOtherEnchants(ItemStack stack) {
        MatchedEnchants enchants = new MatchedEnchants(stack);
        IModuleContainerItem.forMatchingEnchants(stack, enchants, (e, module) -> e.matchedCount++);
        return enchants.enchantments == null || enchants.matchedCount < enchants.enchantments.size();
    }

    default void filterTooltips(ItemStack stack, List<String> tooltips) {
        List<String> enchantsToRemove = new ArrayList<>();
        IModuleContainerItem.forMatchingEnchants(stack, new MatchedEnchants(stack), (e, module) -> enchantsToRemove.add(module.getEnchantment().getTranslatedName(module.getInstalledCount())));
        tooltips.removeAll(enchantsToRemove);
    }


    static void forMatchingEnchants(ItemStack stack, MatchedEnchants enchants, BiConsumer<MatchedEnchants,  EnchantmentBasedModule> consumer) {
        for ( Module modules :Modules.loadAll(stack)) {
            if (modules instanceof  EnchantmentBasedModule module){
                if (module.isEnabled() && enchants.getEnchantments().getOrDefault(module.getEnchantment(), 0) == module.getInstalledCount()) {
                    consumer.accept(enchants, module);
                }
            }
        }
    }

    class MatchedEnchants {

        private final ItemStack stack;
        private Map<Enchantment, Integer> enchantments;
        private int matchedCount;

        public MatchedEnchants(ItemStack stack) {
            this.stack = stack;
        }

        public Map<Enchantment, Integer> getEnchantments() {
            if (enchantments == null) {
                enchantments = EnchantmentHelper.getEnchantments(stack);
            }
            return enchantments;
        }
    }
}
