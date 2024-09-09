package mekanism.common.tile.factory;

import mekanism.api.transmitters.TransmissionType;
import mekanism.common.SideData;
import mekanism.common.block.states.BlockStateMachine;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.util.InventoryUtils;

public class TileEntityUltimateFactory extends TileEntityFactory {
    private static final int[] Input_Output = new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
    private static final boolean[] Input_Output_Enable = new boolean[]{false, false, false, false, false, false, false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};

    public TileEntityUltimateFactory() {
        super(FactoryTier.ULTIMATE, BlockStateMachine.MachineType.ULTIMATE_FACTORY);

        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY, TransmissionType.GAS, TransmissionType.FLUID);

        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.NONE, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.INPUT, getSlotsWithTier(tier)));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.OUTPUT, getOutputSlotsWithTier(tier)));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.ENERGY, new int[]{1}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.EXTRA, new int[]{4}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.INPUT_EXTRA, new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(Input_Output, Input_Output_Enable));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.OUTPUT_ENHANCED, getOutputSlotsWithTier(tier)));
        configComponent.setConfig(TransmissionType.ITEM, new byte[]{4, 1, 1, 3, 1, 2});
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.INPUT_OUTPUT_ENHANCED, Input_Output, Input_Output_Enable));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.INPUT_ENHANCED, getSlotsWithTier(tier)));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.INPUT_OUTPUT_ENHANCED, Input_Output, Input_Output_Enable));
        configComponent.setInputConfig(TransmissionType.FLUID);

        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.NONE, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.INPUT, new int[]{1}));
        configComponent.addOutput(TransmissionType.GAS, new SideData(DataType.OUTPUT, new int[]{2}));
        configComponent.setConfig(TransmissionType.GAS, new byte[]{1, 1, 1, 1, 1, 2});

        configComponent.setInputConfig(TransmissionType.ENERGY);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(TransmissionType.ITEM, configComponent.getOutputs(TransmissionType.ITEM).get(2));
        ejectorComponent.setInputOutputData(TransmissionType.ITEM, configComponent.getOutputs(TransmissionType.ITEM).get(6));
        ejectorComponent.setOutputData(TransmissionType.GAS, configComponent.getOutputs(TransmissionType.GAS).get(2));
    }
}
