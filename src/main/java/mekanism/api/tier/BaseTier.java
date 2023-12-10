package mekanism.api.tier;

import mekanism.api.EnumColor;
import mekanism.api.SupportsColorMap;
import mekanism.api.math.MathUtils;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.Locale;

/**
 * The default tiers used in Mekanism.
 *
 * @author aidancbrady
 */
public enum BaseTier implements IStringSerializable, SupportsColorMap {
    BASIC("Basic", new int[]{95, 255, 184}, EnumColor.BRIGHT_GREEN),
    ADVANCED("Advanced", new int[]{255, 128, 106}, EnumColor.DARK_RED),
    ELITE("Elite", new int[]{75, 248, 255}, EnumColor.DARK_BLUE),
    ULTIMATE("Ultimate", new int[]{247, 135, 255}, EnumColor.PURPLE),
    CREATIVE("Creative", new int[]{88, 88, 88}, EnumColor.YELLOW);


    private static final BaseTier[] TIERS = values();

    private String name;
    private EnumColor color;
    private TextFormatting textColor;
    private int[] rgbCode;

    BaseTier(String s, int[] rgbCode, EnumColor c) {
        name = s;
        color = c;
        setColorFromAtlas(rgbCode);
    }

    public String getSimpleName() {
        return name;
    }

    public String getLocalizedName() {
        return getSimpleName().toLowerCase(Locale.ROOT);
    }

    public EnumColor getColor() {
        return color;
    }


    /**
     * {@inheritDoc}
     *
     * @apiNote This method is mostly for <strong>INTERNAL</strong> usage.
     * @since 10.4.0
     */
    @Override
    public void setColorFromAtlas(int[] color) {
        this.rgbCode = color;
        ITextComponent t = new TextComponentTranslation(getSimpleName());
        t.getStyle().setColor(textColor);
    }

    public TextFormatting getTextColor() {
        return this.textColor;
    }

    @Override
    public int[] getRgbCode() {
        return rgbCode;
    }


    public boolean isObtainable() {
        return this != CREATIVE;
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    /**
     * Gets a tier by index.
     *
     * @param index Index of the tier.
     */
    public static BaseTier byIndexStatic(int index) {
        return MathUtils.getByIndexMod(TIERS, index);
    }
}
