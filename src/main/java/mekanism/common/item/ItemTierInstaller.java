package mekanism.common.item;

import mekanism.common.base.IMetaItem;
import mekanism.common.base.ITierUpgradeable;
import mekanism.common.config.MekanismConfig;
import mekanism.common.tier.BaseTier;
import mekanism.common.tile.prefab.TileEntityBasicBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemTierInstaller extends ItemMekanism implements IMetaItem {

    public ItemTierInstaller() {
        super();
        setMaxStackSize(MekanismConfig.current().mekce.MAXTierSize.val());
        setHasSubtypes(true);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote) {
            return EnumActionResult.PASS;
        }
        TileEntity tile = world.getTileEntity(pos);
        ItemStack stack = player.getHeldItem(hand);
        BaseTier tier = BaseTier.values()[stack.getItemDamage()];
        if (tile instanceof ITierUpgradeable upgradeable) {
            if (tile instanceof TileEntityBasicBlock basicBlock && !basicBlock.playersUsing.isEmpty()) {
                return EnumActionResult.FAIL;
            }
            if (upgradeable.upgrade(tier) && upgradeable.UpgradeAmount() <= stack.getCount()) {
                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(upgradeable.UpgradeAmount());
                }
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.PASS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public String getTexture(int meta) {
        return BaseTier.values()[meta].getSimpleName() + "TierInstaller";
    }

    @Override
    public int getVariants() {
        return BaseTier.values().length;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tabs, @Nonnull NonNullList<ItemStack> itemList) {
        if (isInCreativeTab(tabs)) {
            for (BaseTier tier : BaseTier.values()) {
                itemList.add(new ItemStack(this, 1, tier.ordinal()));
            }
        }
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return BaseTier.values()[stack.getItemDamage()].getColor() + super.getItemStackDisplayName(stack);
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack stack) {
        return "item." + BaseTier.values()[stack.getItemDamage()].getSimpleName().toLowerCase(Locale.ROOT) + "TierInstaller";
    }
}
