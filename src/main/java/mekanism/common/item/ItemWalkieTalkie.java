package mekanism.common.item;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mekanism.api.EnumColor;
import mekanism.api.NBTConstants;
import mekanism.common.Mekanism;
import mekanism.common.config.MekanismConfig;
import mekanism.common.item.interfaces.IModeItem;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemWalkieTalkie extends ItemMekanismAddition implements IModeItem {

    public static ModelResourceLocation OFF_MODEL = new ModelResourceLocation(new ResourceLocation(Mekanism.MODID, "WalkieTalkie"), "inventory");

    public static Int2ObjectMap<ModelResourceLocation> CHANNEL_MODELS = new Int2ObjectOpenHashMap<>();

    public ItemWalkieTalkie() {
        super();
        setMaxStackSize(1);
    }

    public static ModelResourceLocation getModel(int channel) {
        CHANNEL_MODELS.computeIfAbsent(channel, c -> new ModelResourceLocation(new ResourceLocation(Mekanism.MODID, "WalkieTalkie_ch" + c), "inventory"));
        return CHANNEL_MODELS.get(channel);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(itemstack, world, list, flag);
        list.add((getOn(itemstack) ? EnumColor.DARK_GREEN : EnumColor.DARK_RED) + LangUtils.localize("gui." + (getOn(itemstack) ? "on" : "off")));
        list.add(EnumColor.DARK_AQUA + LangUtils.localize("tooltip.channel") + ": " + EnumColor.GREY + getChannel(itemstack));
        if (!MekanismConfig.current().general.voiceServerEnabled.val()) {
            list.add(EnumColor.DARK_RED + LangUtils.localize("tooltip.walkie_disabled"));
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (player.isSneaking()) {
            setOn(itemStack, !getOn(itemStack));
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
        }
        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
        return !ItemStack.areItemsEqual(oldStack, newStack);
    }

    public void setOn(ItemStack itemStack, boolean on) {
        ItemDataUtils.setBoolean(itemStack, NBTConstants.RUNNING, on);
    }

    public boolean getOn(ItemStack itemStack) {
        return ItemDataUtils.getBoolean(itemStack, NBTConstants.RUNNING);
    }

    public void setChannel(ItemStack itemStack, int channel) {
        ItemDataUtils.setInt(itemStack, NBTConstants.CHANNEL, channel);
    }

    public int getChannel(ItemStack itemStack) {
        int channel = ItemDataUtils.getInt(itemStack, NBTConstants.CHANNEL);
        if (channel == 0) {
            setChannel(itemStack, 1);
            channel = 1;
        }
        return channel;
    }


    @Override
    public void changeMode(@NotNull EntityPlayer player, @NotNull ItemStack stack, int shift, boolean displayChangeMessage) {
        if (getOn(stack)) {
            int channel = getChannel(stack);
            int newChannel = Math.floorMod(channel + shift - 1, 8) + 1;
            if (channel != newChannel) {
                setChannel(stack, newChannel);
                if (displayChangeMessage) {
                    player.sendMessage(new TextComponentString(EnumColor.DARK_BLUE + Mekanism.LOG_TAG + " " + EnumColor.GREY + LangUtils.localize("tooltip.channel") + ": " + EnumColor.GREY + getChannel(stack)));
                }
            }
        }
    }

    @Nonnull
    @Override
    public ITextComponent getScrollTextComponent(@Nonnull ItemStack stack) {
        return new TextComponentString(LangUtils.localize("tooltip.channel") + ": " + EnumColor.GREY + getChannel(stack));
    }
}
