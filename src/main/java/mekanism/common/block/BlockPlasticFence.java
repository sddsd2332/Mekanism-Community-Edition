package mekanism.common.block;

import mekanism.api.EnumColor;
import mekanism.api.IMekWrench;
import mekanism.common.Mekanism;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.wrenches.Wrenches;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static mekanism.common.block.states.BlockStatePlastic.colorProperty;

public class BlockPlasticFence extends BlockFence {

    public BlockPlasticFence() {
        super(BlockPlastic.PLASTIC, BlockPlastic.PLASTIC.getMaterialMapColor());
        setHardness(5F);
        setResistance(10F);
        setCreativeTab(Mekanism.tabMekanismAddition);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, EAST, WEST, SOUTH, colorProperty);
    }

    @Nonnull
    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(colorProperty, EnumDyeColor.byDyeDamage(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(colorProperty).getDyeDamage();
    }

    @Override
    public void getSubBlocks(CreativeTabs creativetabs, NonNullList<ItemStack> list) {
        for (int i = 0; i < EnumColor.DYES.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    public static class PlasticFenceStateMapper extends StateMapperBase {

        @Nonnull
        @Override
        protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
            String properties = "east=" + state.getValue(EAST) + ",";
            properties += "north=" + state.getValue(NORTH) + ",";
            properties += "south=" + state.getValue(SOUTH) + ",";
            properties += "west=" + state.getValue(WEST);
            ResourceLocation baseLocation = new ResourceLocation(Mekanism.MODID, "PlasticFence");
            return new ModelResourceLocation(baseLocation, properties);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }
        ItemStack stack = entityplayer.getHeldItem(hand);
        if (!stack.isEmpty() && MekanismConfig.current().mekce.PlasticWrench.val()) {
            IMekWrench wrenchHandler = Wrenches.getHandler(stack);
            if (wrenchHandler != null) {
                RayTraceResult raytrace = new RayTraceResult(new Vec3d(hitX, hitY, hitZ), side, pos);
                if (wrenchHandler.canUseWrench(entityplayer, hand, stack, raytrace)) {
                    wrenchHandler.wrenchUsed(entityplayer, hand, stack, raytrace);
                    if (entityplayer.isSneaking()) {
                        MekanismUtils.dismantleBlock(this, state, world, pos);
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
