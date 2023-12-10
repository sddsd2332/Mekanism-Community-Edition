package mekanism.api.radiation.capability;

public interface IRadiationShielding {

    /**
     * Gets a percentage representing how much radiation shielding this capability provides.
     *
     * @return Radiation shielding (0.0 to 1.0).
     */
    double getRadiationShielding();
}
