package mekanism.common.interfaces;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IOverlayRenderAware {
    boolean renderItemOverlayIntoGUI(@Nonnull ItemStack stack, int xPosition, int yPosition);
}
