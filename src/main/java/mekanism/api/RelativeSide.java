package mekanism.api;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.math.MathUtils;
import mekanism.api.text.APILang;
import mekanism.api.text.IHasTranslationKey;
import mekanism.api.text.ILangEntry;
import net.minecraft.util.EnumFacing;

@NothingNullByDefault
public enum RelativeSide implements IHasTranslationKey {
    FRONT(APILang.FRONT),
    LEFT(APILang.LEFT),
    RIGHT(APILang.RIGHT),
    BACK(APILang.BACK),
    TOP(APILang.TOP),
    BOTTOM(APILang.BOTTOM);

    private static final RelativeSide[] SIDES = values();

    /**
     * Gets a side by index.
     *
     * @param index Index of the side.
     */
    public static RelativeSide byIndex(int index) {
        return MathUtils.getByIndexMod(SIDES, index);
    }


    private final ILangEntry langEntry;

    RelativeSide(ILangEntry langEntry) {
        this.langEntry = langEntry;
    }

    @Override
    public String getTranslationKey() {
        return langEntry.getTranslationKey();
    }

    /**
     * Gets the {@link EnumFacing} from the block based on what side it is facing.
     *
     * @param facing The direction the block is facing.
     *
     * @return The direction representing which side of the block this RelativeSide is actually representing based on the direction it is facing.
     */
    public EnumFacing getDirection(EnumFacing facing) {
        return switch (this) {
            case FRONT -> facing;
            case BACK -> facing.getOpposite();
            case LEFT -> facing == EnumFacing.DOWN || facing == EnumFacing.UP ? EnumFacing.EAST : facing.rotateY();
            case RIGHT -> facing == EnumFacing.DOWN || facing == EnumFacing.UP ? EnumFacing.WEST : facing.rotateYCCW();
            case TOP -> switch (facing) {
                case DOWN -> EnumFacing.NORTH;
                case UP -> EnumFacing.SOUTH;
                default -> EnumFacing.UP;
            };
            case BOTTOM -> switch (facing) {
                case DOWN -> EnumFacing.SOUTH;
                case UP -> EnumFacing.NORTH;
                default -> EnumFacing.DOWN;
            };
        };
    }

    /**
     * Gets the {@link RelativeSide} based on a side, and the facing direction of a block.
     *
     * @param facing The direction the block is facing.
     * @param side   The side of the block we want to know what {@link RelativeSide} it is.
     *
     * @return the {@link RelativeSide} based on a side, and the facing direction of a block.
     *
     * @apiNote The calculations for what side is what when facing upwards or downwards, is done as if it was facing NORTH and rotated around the X-axis
     */
    public static RelativeSide fromDirections(EnumFacing facing, EnumFacing side) {
        if (side == facing) {
            return FRONT;
        } else if (side == facing.getOpposite()) {
            return BACK;
        } else if (facing == EnumFacing.DOWN || facing == EnumFacing.UP) {
            return switch (side) {
                case NORTH -> facing == EnumFacing.DOWN ? TOP : BOTTOM;
                case SOUTH -> facing == EnumFacing.DOWN ? BOTTOM : TOP;
                case WEST -> RIGHT;
                case EAST -> LEFT;
                default -> throw new IllegalStateException("Case should have been caught earlier.");
            };
        } else if (side == EnumFacing.DOWN) {
            return BOTTOM;
        } else if (side == EnumFacing.UP) {
            return TOP;
        } else if (side == facing.rotateYCCW()) {
            return RIGHT;
        } else if (side == facing.rotateY()) {
            return LEFT;
        }
        //Fall back to front, should never get here
        return FRONT;
    }

}
