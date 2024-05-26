package mekanism.multiblockmachine.client.render.item.generator;

import mekanism.client.render.item.ItemLayerWrapper;
import mekanism.client.render.item.SubTypeItemRenderer;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachineGenerator.MultiblockMachineGeneratorType;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderMultiblockGeneratorItem extends SubTypeItemRenderer<MultiblockMachineGeneratorType> {

    public static Map<MultiblockMachineGeneratorType, ItemLayerWrapper> modelMap = new EnumMap<>(MultiblockMachineGeneratorType.class);

    @Override
    protected boolean earlyExit() {
        return true;
    }

    @Override
    protected void renderBlockSpecific(@Nonnull ItemStack stack, ItemCameraTransforms.TransformType transformType) {
        MultiblockMachineGeneratorType type = MultiblockMachineGeneratorType.get(stack);
        if (type != null) {
            if (type == MultiblockMachineGeneratorType.LARGE_WIND_GENERATOR) {
                RenderLargeWindGeneratorItem.renderStack(stack, transformType);
            }else if (type == MultiblockMachineGeneratorType.LARGE_HEAT_GENERATOR){
                RenderLargeHeatGeneratorItem.renderStack(stack,transformType);
            }else if (type == MultiblockMachineGeneratorType.LARGE_GAS_GENERATOR){
                RenderLargeGasGeneratorItem.renderStack(stack,transformType);
            }
        }
    }

    @Override
    protected void renderItemSpecific(@Nonnull ItemStack stack, ItemCameraTransforms.TransformType transformType) {
    }

    @Nullable
    @Override
    protected ItemLayerWrapper getModel(MultiblockMachineGeneratorType type) {
        return modelMap.get(type);
    }

    @Nullable
    @Override
    protected MultiblockMachineGeneratorType getType(@Nonnull ItemStack stack) {
        return MultiblockMachineGeneratorType.get(stack);
    }

}
