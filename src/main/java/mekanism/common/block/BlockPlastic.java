package mekanism.common.block;

import mekanism.api.EnumColor;
import mekanism.api.IMekWrench;
import mekanism.common.Mekanism;
import mekanism.common.block.states.BlockStatePlastic;
import mekanism.common.block.states.BlockStatePlastic.PlasticBlockType;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.wrenches.Wrenches;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockPlastic extends Block {

    public static final Material PLASTIC = new Material(MapColor.CLAY);

    public PlasticBlockType type;

    public BlockPlastic(PlasticBlockType blockType) {
        super(PLASTIC);
        type = blockType;
        setHardness(type == PlasticBlockType.REINFORCED ? 50F : 5F);
        setResistance(type == PlasticBlockType.REINFORCED ? 2000F : 10F);
        setCreativeTab(Mekanism.tabMekanismAddition);
        if (type == PlasticBlockType.SLICK) {
            slipperiness = 0.98F;
        }
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStatePlastic(this);
    }

    @Nonnull
    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockStatePlastic.colorProperty, EnumDyeColor.byDyeDamage(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockStatePlastic.colorProperty).getDyeDamage();
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (type == PlasticBlockType.ROAD) {
            double boost = 1.6;
            double a = Math.atan2(entityIn.motionX, entityIn.motionZ);
            entityIn.motionX += Math.sin(a) * boost * slipperiness;
            entityIn.motionZ += Math.cos(a) * boost * slipperiness;
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(CreativeTabs creativetabs, NonNullList<ItemStack> list) {
        for (int i = 0; i < EnumColor.DYES.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (type == PlasticBlockType.GLOW) {
            return 10;
        }
        return 0;
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
