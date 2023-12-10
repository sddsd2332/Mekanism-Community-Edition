package mekanism.api.text;

import mekanism.api.IIncrementalEnum;
import mekanism.api.SupportsColorMap;
import mekanism.api.math.MathUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public enum EnumColor implements IIncrementalEnum<EnumColor>, SupportsColorMap {

    BLACK("\u00a70", APILang.COLOR_BLACK, "Black", "black", new int[]{64, 64, 64}, 0, TextFormatting.BLACK),
    DARK_BLUE("\u00a71", APILang.COLOR_DARK_BLUE, "Blue", "blue", new int[]{54, 107, 208}, 4, TextFormatting.DARK_BLUE),
    DARK_GREEN("\u00a72", APILang.COLOR_DARK_GREEN, "Green", "green", new int[]{89, 193, 95}, 2, TextFormatting.DARK_GREEN),
    DARK_AQUA("\u00a73", APILang.COLOR_DARK_AQUA, "Cyan", "cyan", new int[]{0, 243, 208}, 6, TextFormatting.DARK_AQUA),
    DARK_RED("\u00a74", APILang.COLOR_DARK_RED, "Dark Red", "dark_red", new int[]{201, 7, 31}, -1, TextFormatting.DARK_RED),
    PURPLE("\u00a75", APILang.COLOR_PURPLE, "Purple", "purple", new int[]{164, 96, 217}, 5, TextFormatting.DARK_PURPLE),
    ORANGE("\u00a76", APILang.COLOR_ORANGE, "Orange", "orange", new int[]{255, 161, 96}, 14, TextFormatting.GOLD),
    GRAY("\u00a77", APILang.COLOR_GRAY, "Light Gray", "light_gray", new int[]{207, 207, 207}, 7, TextFormatting.GRAY),
    DARK_GRAY("\u00a78", APILang.COLOR_DARK_GRAY, "Gray", "gray", new int[]{122, 122, 122}, 8, TextFormatting.DARK_GRAY),
    INDIGO("\u00a79", APILang.COLOR_INDIGO, "Light Blue", "light_blue", new int[]{85, 158, 255}, 12, TextFormatting.BLUE),
    BRIGHT_GREEN("\u00a7a", APILang.COLOR_BRIGHT_GREEN, "Lime", "lime", new int[]{117, 255, 137}, 10, TextFormatting.GREEN),
    AQUA("\u00a7b", APILang.COLOR_AQUA, "Aqua", "aqua", new int[]{48, 255, 249}, -1, TextFormatting.AQUA),
    RED("\u00a7c", APILang.COLOR_RED, "Red", "red", new int[]{255, 56, 60}, 1, TextFormatting.RED),
    PINK("\u00a7d", APILang.COLOR_PINK, "Magenta", "magenta", new int[]{213, 94, 203}, 13, TextFormatting.LIGHT_PURPLE),
    YELLOW("\u00a7e", APILang.COLOR_YELLOW, "Yellow", "yellow", new int[]{255, 221, 79}, 11, TextFormatting.YELLOW),
    WHITE("\u00a7f", APILang.COLOR_WHITE, "White", "white", new int[]{255, 255, 255}, 15, TextFormatting.WHITE),
    //Extras for dye-completeness
    BROWN("\u00a76", APILang.COLOR_BROWN, "Brown", "brown", new int[]{161, 118, 73}, 3, TextFormatting.GOLD),
    BRIGHT_PINK("\u00a7d", APILang.COLOR_BRIGHT_PINK, "Pink", "pink", new int[]{255, 188, 196}, 9, TextFormatting.LIGHT_PURPLE);

    private static final EnumColor[] COLORS = values();


    /**
     * The color code that will be displayed
     */
    public final String code;

    private int[] rgbCode;

    public final int mcMeta;

    public final TextFormatting textFormatting;

    private final APILang langEntry;
    private final String englishName;
    private final String registryPrefix;


    EnumColor(String code, APILang langEntry, String englishName, String registryPrefix, int[] rgb, int meta, TextFormatting ft) {
        this.code = code;
        this.langEntry = langEntry;
        this.englishName = englishName;
        this.registryPrefix = registryPrefix;
        this.rgbCode = rgb;
        this.mcMeta = meta;
        this.textFormatting = ft;
    }


    public static EnumColor getFromDyeName(String s) {
        for (EnumColor c : values()) {
            if (c.langEntry.getTranslationKey().equalsIgnoreCase(s)) {
                return c;
            }
        }
        return null;
    }


    /**
     * Gets the prefix to use in registry names for this color.
     */
    public String getRegistryPrefix() {
        return registryPrefix;
    }

    /**
     * Gets the English name of this color.
     */
    public String getEnglishName() {
        return englishName;
    }


    /**
     * Gets the name of this color with its color prefix code.
     *
     * @return the color's name and color prefix
     */


    public ITextComponent getColoredName() {
        ITextComponent t = new TextComponentTranslation(getLangEntry().getTranslationKey());
        t.getStyle().setColor(textFormatting);
        return t;
    }

    public int getMetaValue() {
        return mcMeta;
    }


    public String getName() {
        return langEntry.translate();
    }

    public APILang getLangEntry() {
        return langEntry;
    }


    /**
     * Gets a color by index.
     *
     * @param index Index of the color.
     */
    public static EnumColor byIndexStatic(int index) {
        return MathUtils.getByIndexMod(COLORS, index);
    }


    @Override
    public EnumColor byIndex(int index) {
        return byIndexStatic(index);
    }

    @Override
    public int[] getRgbCode() {
        return rgbCode;
    }

    @Override
    public void setColorFromAtlas(int[] color) {
        this.rgbCode = color;
        ITextComponent t = new TextComponentTranslation(getRegistryPrefix());
        t.getStyle().setColor(textFormatting);
    }


    @Override
    public String toString() {
        return code;
    }

}


