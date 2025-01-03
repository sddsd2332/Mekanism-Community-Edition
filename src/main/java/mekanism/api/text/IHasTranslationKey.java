package mekanism.api.text;


import mcp.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
public interface IHasTranslationKey {

    /**
     * Gets the translation key for this object.
     */
    String getTranslationKey();
}