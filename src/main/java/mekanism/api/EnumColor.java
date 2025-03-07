package mekanism.api;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import java.util.Locale;

/**
 * Simple color enum for adding colors to in-game GUI strings of text.
 *
 * @author AidanBrady
 */
public enum EnumColor implements IStringSerializable {
    BLACK("§0", "black", "Black", new int[]{0, 0, 0}, 0, TextFormatting.BLACK),
    DARK_BLUE("§1", "darkBlue", "Blue", new int[]{0, 0, 170}, 4, TextFormatting.DARK_BLUE),
    DARK_GREEN("§2", "darkGreen", "Green", new int[]{0, 170, 0}, 2, TextFormatting.DARK_GREEN),
    DARK_AQUA("§3", "darkAqua", "Cyan", new int[]{0, 255, 255}, 6, TextFormatting.DARK_AQUA),
    DARK_RED("§4", "darkRed", null, new int[]{170, 0, 0}, -1, TextFormatting.DARK_RED),
    PURPLE("§5", "purple", "Purple", new int[]{170, 0, 170}, 5, TextFormatting.DARK_PURPLE),
    ORANGE("§6", "orange", "Orange", new int[]{255, 170, 0}, 14, TextFormatting.GOLD),
    GREY("§7", "grey", "LightGray", new int[]{170, 170, 170}, 7, TextFormatting.GRAY),
    DARK_GREY("§8", "darkGrey", "Gray", new int[]{85, 85, 85}, 8, TextFormatting.DARK_GRAY),
    INDIGO("§9", "indigo", "LightBlue", new int[]{85, 85, 255}, 12, TextFormatting.BLUE),
    BRIGHT_GREEN("§a", "brightGreen", "Lime", new int[]{85, 255, 85}, 10, TextFormatting.GREEN),
    AQUA("§b", "aqua", null, new int[]{85, 255, 255}, -1, TextFormatting.AQUA),
    RED("§c", "red", "Red", new int[]{255, 0, 0}, 1, TextFormatting.RED),
    PINK("§d", "pink", "Magenta", new int[]{255, 85, 255}, 13, TextFormatting.LIGHT_PURPLE),
    YELLOW("§e", "yellow", "Yellow", new int[]{255, 255, 85}, 11, TextFormatting.YELLOW),
    WHITE("§f", "white", "White", new int[]{255, 255, 255}, 15, TextFormatting.WHITE),
    //Extras for dye-completeness
    BROWN("§6", "brown", "Brown", new int[]{150, 75, 0}, 3, TextFormatting.GOLD),
    BRIGHT_PINK("§d", "brightPink", "Pink", new int[]{255, 192, 203}, 9, TextFormatting.LIGHT_PURPLE);

    public static EnumColor[] DYES = new EnumColor[]{BLACK, RED, DARK_GREEN, BROWN, DARK_BLUE, PURPLE, DARK_AQUA, GREY, DARK_GREY, BRIGHT_PINK, BRIGHT_GREEN, YELLOW,
            INDIGO, PINK, ORANGE, WHITE};

    /**
     * The color code that will be displayed
     */
    public final String code;

    public final int[] rgbCode;

    public final int mcMeta;
    public final TextFormatting textFormatting;
    /**
     * A friendly name of the color.
     */
    public String unlocalizedName;
    public String dyeName;

    EnumColor(String s, String n, String dye, int[] rgb, int meta, TextFormatting tf) {
        code = s;
        unlocalizedName = n;
        dyeName = dye;
        rgbCode = rgb;
        mcMeta = meta;
        textFormatting = tf;
    }

    public static EnumColor getFromDyeName(String s) {
        for (EnumColor c : values()) {
            if (c.dyeName.equalsIgnoreCase(s)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Gets the localized name of this color by translating the unlocalized name.
     *
     * @return localized name
     */
    public String getLocalizedName() {
        return I18n.translateToLocal("color." + unlocalizedName);
    }

    @Deprecated
    public String getUnlocalizedName() {
        return getTranslationKey();
    }

    public String getTranslationKey() {
        return "color." + unlocalizedName;
    }

    public String getDyeName() {
        return I18n.translateToLocal("dye." + unlocalizedName);
    }

    public String getOreDictName() {
        return dyeName;
    }

    /**
     * Gets the name of this color with it's color prefix code.
     *
     * @return the color's name and color prefix
     */
    public String getColoredName() {
        return code + getLocalizedName();
    }

    public ITextComponent getTranslatedColouredComponent() {
        ITextComponent t = new TextComponentTranslation(getTranslationKey());
        t.getStyle().setColor(textFormatting);
        return t;
    }

    public String getDyedName() {
        return code + getDyeName();
    }

    @Override
    public String getName() {
        return unlocalizedName.toLowerCase(Locale.ROOT);
    }

    /**
     * Gets the 0-1 of this color's RGB value by dividing by 255 (used for OpenGL coloring).
     *
     * @param index - R:0, G:1, B:2
     * @return the color value
     */
    public float getColor(int index) {
        return (float) rgbCode[index] / 255F;
    }

    /**
     * Gets the value of this color mapped to MC in-game item colors present in dyes and wool.
     *
     * @return mc meta value
     */
    public int getMetaValue() {
        return mcMeta;
    }

    @Override
    public String toString() {
        return code;
    }
}
