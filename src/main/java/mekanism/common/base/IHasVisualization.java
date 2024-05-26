package mekanism.common.base;

public interface IHasVisualization {

    boolean isClientRendering();

    void toggleClientRendering();

    default boolean canDisplayVisuals() {
        return true;
    }
}
