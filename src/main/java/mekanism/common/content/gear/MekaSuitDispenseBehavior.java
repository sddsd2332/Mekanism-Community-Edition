package mekanism.common.content.gear;

import mekanism.api.gear.ICustomModule.ModuleDispenseResult;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class MekaSuitDispenseBehavior extends ModuleDispenseBehavior {

    @Override
    protected ModuleDispenseResult performBuiltin(@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
        if (ItemArmor.dispenseArmor(source, stack) != ItemStack.EMPTY) {
            return ModuleDispenseResult.HANDLED;
        }
        return super.performBuiltin(source, stack);
    }
}