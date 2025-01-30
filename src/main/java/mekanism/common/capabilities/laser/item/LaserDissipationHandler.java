package mekanism.common.capabilities.laser.item;

import mekanism.api.lasers.ILaserDissipation;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.ItemCapabilityWrapper.ItemCapability;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Objects;
import java.util.function.ToDoubleFunction;

public class LaserDissipationHandler extends ItemCapability implements ILaserDissipation {

    public static LaserDissipationHandler create(ToDoubleFunction<ItemStack> dissipationFunction, ToDoubleFunction<ItemStack> refractionFunction) {
        Objects.requireNonNull(dissipationFunction, "Dissipation function cannot be null");
        Objects.requireNonNull(refractionFunction, "Refraction function cannot be null");
        return new LaserDissipationHandler(dissipationFunction, refractionFunction);
    }

    private final ToDoubleFunction<ItemStack> dissipationFunction;
    private final ToDoubleFunction<ItemStack> refractionFunction;

    private LaserDissipationHandler(ToDoubleFunction<ItemStack> dissipationFunction, ToDoubleFunction<ItemStack> refractionFunction) {
        this.dissipationFunction = dissipationFunction;
        this.refractionFunction = refractionFunction;
    }

    @Override
    public double getDissipationPercent() {
        return dissipationFunction.applyAsDouble(getStack());
    }

    @Override
    public double getRefractionPercent() {
        return refractionFunction.applyAsDouble(getStack());
    }


    @Override
    public boolean canProcess(Capability<?> capability) {
        return capability == Capabilities.LASER_DISSIPATION_CAPABILITY;
    }
}