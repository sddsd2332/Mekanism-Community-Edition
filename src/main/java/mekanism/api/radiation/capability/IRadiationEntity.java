package mekanism.api.radiation.capability;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

/**
 * Base capability definition for handling radiation for entities.
 */
public interface IRadiationEntity extends INBTSerializable<NBTTagCompound> {

    /**
     * Gets the radiation dosage (Sv) of the entity.
     *
     * @return radiation dosage
     */
    double getRadiation();

    /**
     * Applies an additional magnitude of radiation to the entity (Sv).
     *
     * @param magnitude dosage of radiation to apply (Sv)
     */
    void radiate(double magnitude);

    /**
     * Decays the entity's radiation dosage.
     */
    void decay();

    /**
     * Applies radiation effects to the entity, and syncs the capability if needed.
     */
    void update(@NotNull EntityLiving entity);

    /**
     * Sets the radiation level of the entity to a new value.
     *
     * @param magnitude value to set radiation dosage to
     */
    void set(double magnitude);
}
