package mekanism.common.interfaces;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IRenderEffectIntoGUI {
    void renderItemAndEffectIntoGUI(@Nonnull ItemStack stack, int xPosition, int yPosition);
}
