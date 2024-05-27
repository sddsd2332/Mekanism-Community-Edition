package mekanism.multiblockmachine.client.render.item.machine;

import mekanism.client.render.item.ItemLayerWrapper;
import mekanism.client.render.item.SubTypeItemRenderer;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachine.MultiblockMachineType;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderMultiblockMachineItem extends SubTypeItemRenderer<MultiblockMachineType> {

    public static Map<MultiblockMachineType, ItemLayerWrapper> modelMap = new EnumMap<>(MultiblockMachineType.class);

    @Override
    protected boolean earlyExit() {
        return true;
    }

    @Override
    protected void renderBlockSpecific(@Nonnull ItemStack stack, ItemCameraTransforms.TransformType transformType) {
        MultiblockMachineType type = MultiblockMachineType.get(stack);
        if (type != null) {
            if (type == MultiblockMachineType.LARGE_ELECTROLYTIC_SEPARATOR) {
                RenderLargeElectrolyticSeparatorItem.renderStack(stack, transformType);
            }else if (type == MultiblockMachineType.LARGE_CHEMICAL_INFUSER){
                RenderLargeChemicalInfuserItem.renderStack(stack,transformType);
            }
        }
    }

    @Override
    protected void renderItemSpecific(@Nonnull ItemStack stack, ItemCameraTransforms.TransformType transformType) {
    }


    @Nullable
    @Override
    protected ItemLayerWrapper getModel(MultiblockMachineType type) {
        return modelMap.get(type);
    }

    @Nullable
    @Override
    protected MultiblockMachineType getType(@Nonnull ItemStack stack) {
        return MultiblockMachineType.get(stack);
    }
}
