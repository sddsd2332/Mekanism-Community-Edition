package mekanism.common.integration.crafttweaker.gas;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidDefinition;
import stanhebben.zenscript.annotations.*;

@ZenClass("mod.mekanism.gas.IGasDefinition")
@ZenRegister
public interface IGasDefinition {

    @ZenOperator(OperatorType.MUL)
    IGasStack asStack(int mb);

    @ZenGetter("NAME")
    String getName();

    @ZenGetter("displayName")
    String getDisplayName();

    @ZenGetter("liquid")
    ILiquidDefinition getLiquid();

    @ZenSetter("liquid")
    void setLiquid(ILiquidDefinition liquid);

    @ZenGetter("tint")
    int getTint();

    @ZenSetter("tint")
    void setTint(int tint);
}
