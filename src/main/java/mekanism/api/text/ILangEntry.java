package mekanism.api.text;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.text.translation.I18n;

@MethodsReturnNonnullByDefault
public interface ILangEntry extends IHasTranslationKey {

    default String translate(Object... args) {
    return I18n.translateToLocalFormatted(getTranslationKey(),args);
    }




}
