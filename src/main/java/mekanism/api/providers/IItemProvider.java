package mekanism.api.providers;


import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@MethodsReturnNonnullByDefault
public interface IItemProvider extends IBaseProvider, ItemLike {

    /**
     * Creates an item stack of size one using the item this provider represents.
     */
    default ItemStack getItemStack() {
        return getItemStack(1);
    }

    /**
     * Creates an item stack of the given size using the item this provider represents.
     *
     * @param size Size of the stack.
     */
    default ItemStack getItemStack(int size) {
        return new ItemStack(asItem(), size);
    }

    @Override
    default ResourceLocation getRegistryName() {

        return ForgeRegistries.ITEMS.getKey(asItem());
    }

    @Override
    default String getTranslationKey() {
        return asItem().getTranslationKey();
    }
}