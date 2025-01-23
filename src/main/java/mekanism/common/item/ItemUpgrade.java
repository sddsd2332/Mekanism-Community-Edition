package mekanism.common.item;

import mekanism.api.EnumColor;
import mekanism.common.Upgrade;
import mekanism.common.base.IUpgradeItem;
import mekanism.common.base.IUpgradeTile;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemUpgrade extends ItemMekanism implements IUpgradeItem {

    private Upgrade upgrade;

    public ItemUpgrade(Upgrade type) {
        super();
        upgrade = type;
        setMaxStackSize(type.getItemMax());
        setRarity(EnumRarity.UNCOMMON);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.add(LangUtils.localize("tooltip.hold") + " " + EnumColor.AQUA + "shift" + EnumColor.GREY + " " + LangUtils.localize("tooltip.forDetails"));
        } else {
            list.addAll(MekanismUtils.splitTooltip(getUpgradeType(itemstack).getDescription(), itemstack));
        }
    }

    @Override
    public Upgrade getUpgradeType(ItemStack stack) {
        return upgrade;
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (player.isSneaking()) {
            TileEntity tile = world.getTileEntity(pos);
            ItemStack stack = player.getHeldItem(hand);
            Upgrade type = getUpgradeType(stack);
            if (tile instanceof IUpgradeTile upgradeTile) {
                TileComponentUpgrade component = upgradeTile.getComponent();
                if (component.supports(type)) {
                    if (!world.isRemote && component.getUpgrades(type) < type.getMax()) {
                        int added = component.addUpgrades(type, stack.getCount());
                        if (added > 0) {
                            stack.shrink(added);
                        }
                    }
                }
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }
}
