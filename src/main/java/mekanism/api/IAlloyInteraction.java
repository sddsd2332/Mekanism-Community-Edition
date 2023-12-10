package mekanism.api;

import mekanism.api.tier.AlloyTier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.jetbrains.annotations.NotNull;

/**
 * Implement this class in your TileEntity if it can interact with Mekanism alloys.
 *
 * @author aidancbrady
 */
public interface IAlloyInteraction {

    /**
     * Called when a player right-clicks this block with an alloy.
     *
     * @param player      - the player right-clicking the block
     * @param hand        - the hand this alloy was right-clicked with
     * @param stack       - the stack of alloy being right-clicked
     * @param tier        - the tier of the alloy
     */
    void onAlloyInteraction(EntityPlayer player, EnumHand hand, ItemStack stack, @NotNull AlloyTier tier);
}
