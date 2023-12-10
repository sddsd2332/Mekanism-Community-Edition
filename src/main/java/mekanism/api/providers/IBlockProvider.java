package mekanism.api.providers;


import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@MethodsReturnNonnullByDefault
public interface IBlockProvider extends IItemProvider {

    Block getBlock();

    @Override
    default ResourceLocation getRegistryName() {
        //Make sure to use the block's registry name in case it somehow doesn't match
        return ForgeRegistries.BLOCKS.getKey(getBlock());
    }

    @Override
    default String getTranslationKey() {
        return getBlock().getTranslationKey();
    }
}