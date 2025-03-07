package mekanism.api;

public enum Action {

    EXECUTE(true),
    SIMULATE(false);
    private final boolean fluidAction;

    Action(boolean fluidAction) {
        this.fluidAction = fluidAction;
    }

    /**
     * @return {@code true} if this action represents execution.
     */
    public boolean execute() {
        return this == EXECUTE;
    }

    /**
     * @return {@code true} if this action represents simulation.
     */
    public boolean simulate() {
        return this == SIMULATE;
    }

    /**
     * Helper to combines this action with a boolean based execution. This allows easily compounding actions.
     *
     * @param execute {@code true} if it should execute if this action already is an execute action.
     *
     * @return Compounded action.
     */
    public Action combine(boolean execute) {
        return get(execute && execute());
    }

    /**
     * Helper to get an action based on a boolean representing execution.
     *
     * @param execute {@code true} for {@link #EXECUTE}.
     *
     * @return Action.
     */
    public static Action get(boolean execute) {
        return execute ? EXECUTE : SIMULATE;
    }


    /**
     * Helper ot get an action from the corresponding FluidAction.
     *
     * @param action FluidAction.
     *
     * @return Action.
     */
    public static Action fromFluidAction(boolean action) {
        if (action) {
            return EXECUTE;
        } //else FluidAction.SIMULATE
        return SIMULATE;
    }
}
