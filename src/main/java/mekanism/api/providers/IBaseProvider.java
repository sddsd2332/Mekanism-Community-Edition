package mekanism.api.providers;


import mcp.MethodsReturnNonnullByDefault;
import mekanism.api.text.IHasTranslationKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

@MethodsReturnNonnullByDefault
public interface IBaseProvider extends IHasTranslationKey {

    /**
     * Gets the registry name of the element represented by this provider.
     *
     * @return Registry name.
     */
    ResourceLocation getRegistryName();

    /**
     * Gets the "name" or "path" of the registry name.
     */
    default String getName() {
        return getRegistryName().getPath();
    }


    default String getTextComponent() {
        return I18n.translateToLocal(getTranslationKey());
    }
}