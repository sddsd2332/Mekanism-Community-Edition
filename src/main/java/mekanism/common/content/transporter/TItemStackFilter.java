package mekanism.common.content.transporter;

import io.netty.buffer.ByteBuf;
import mekanism.api.TileNetworkList;
import mekanism.common.content.filter.IItemStackFilter;
import mekanism.common.content.transporter.Finder.ItemStackFinder;
import mekanism.common.util.MekanismUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class TItemStackFilter extends TransporterFilter implements IItemStackFilter {

    public boolean sizeMode;

    public int min;
    public int max;

    private ItemStack itemType = ItemStack.EMPTY;

    @Override
    public boolean canFilter(ItemStack itemStack, boolean strict) {
        return super.canFilter(itemStack, strict) &&
                !(strict && sizeMode && (max == 0 || itemStack.getCount() < min)) &&
                ItemHandlerHelper.canItemStacksStackRelaxed(itemType, itemStack);
    }

    @Override
    public InvStack getStackFromInventory(StackSearcher searcher, boolean singleItem) {
        if (sizeMode && !singleItem) {
            return searcher.takeDefinedItem(itemType, min, max);
        }
        return super.getStackFromInventory(searcher, singleItem);
    }

    @Override
    public Finder getFinder() {
        return new ItemStackFinder(itemType);
    }

    @Override
    public void write(NBTTagCompound nbtTags) {
        super.write(nbtTags);
        nbtTags.setInteger("type", 0);
        nbtTags.setBoolean("sizeMode", sizeMode);
        nbtTags.setInteger("min", min);
        nbtTags.setInteger("max", max);
        itemType.writeToNBT(nbtTags);
    }

    @Override
    protected void read(NBTTagCompound nbtTags) {
        super.read(nbtTags);
        sizeMode = nbtTags.getBoolean("sizeMode");
        min = nbtTags.getInteger("min");
        max = nbtTags.getInteger("max");
        itemType = new ItemStack(nbtTags);
    }

    @Override
    public void write(TileNetworkList data) {
        data.add(0);

        super.write(data);

        data.add(sizeMode);
        data.add(min);
        data.add(max);

        data.add(MekanismUtils.getID(itemType));
        data.add(itemType.getCount());
        data.add(itemType.getItemDamage());
    }

    @Override
    protected void read(ByteBuf dataStream) {
        super.read(dataStream);
        sizeMode = dataStream.readBoolean();
        min = dataStream.readInt();
        max = dataStream.readInt();
        itemType = new ItemStack(Item.getItemById(dataStream.readInt()), dataStream.readInt(), dataStream.readInt());
    }

    @Override
    public int hashCode() {
        int code = 1;
        code = 31 * code + super.hashCode();
        code = 31 * code + MekanismUtils.getID(itemType);
        code = 31 * code + itemType.getCount();
        code = 31 * code + itemType.getItemDamage();
        code = 31 * code + (sizeMode ? 1 : 0);
        code = 31 * code + min;
        code = 31 * code + max;
        return code;
    }

    @Override
    public boolean equals(Object filter) {
        return super.equals(filter) && filter instanceof TItemStackFilter stackFilter && stackFilter.itemType.isItemEqual(itemType)
                && stackFilter.sizeMode == sizeMode && stackFilter.min == min && stackFilter.max == max;
    }

    @Override
    public TItemStackFilter clone() {
        TItemStackFilter filter = new TItemStackFilter();
        filter.allowDefault = allowDefault;
        filter.color = color;
        filter.itemType = itemType.copy();
        filter.sizeMode = sizeMode;
        filter.min = min;
        filter.max = max;
        return filter;
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        return itemType;
    }

    @Override
    public void setItemStack(@Nonnull ItemStack stack) {
        itemType = stack;
    }
}
