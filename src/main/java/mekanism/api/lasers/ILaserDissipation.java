package mekanism.api.lasers;

public interface ILaserDissipation {

    /**
     * Gets the percentage for how much of a laser's energy this piece of armor will dissipate across it.
     *
     * @return Laser dissipation percentage (0.0 to 1.0).
     *
     * @implNote This value should be between zero and one, but values greater than one will work as well as the total percentage across the worn armor gets capped at
     * one.
     */
    double getDissipationPercent();

    /**
     * Gets the percentage for how much of a laser's energy this piece of armor will be refracted through it.
     *
     * @return Laser refraction percentage (0.0 to 1.0).
     *
     * @implNote This value should be between zero and one, but values greater than one will work as well as the total percentage across the worn armor gets capped at
     * one.
     */
    double getRefractionPercent();

}
