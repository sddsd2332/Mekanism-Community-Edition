package mekanism.common.content.miner;

import io.netty.buffer.ByteBuf;
import mekanism.api.TileNetworkList;
import mekanism.common.content.filter.IMaterialFilter;
import mekanism.common.content.transporter.Finder.MaterialFinder;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class MMaterialFilter extends MinerFilter implements IMaterialFilter {

    private ItemStack materialItem = ItemStack.EMPTY;

    public Material getMaterial() {
        return Block.getBlockFromItem(materialItem.getItem()).getStateFromMeta(materialItem.getItemDamage()).getMaterial();
    }

    @Override
    public boolean canFilter(ItemStack itemStack) {
        if (itemStack.isEmpty() || !(itemStack.getItem() instanceof ItemBlock)) {
            return false;
        }
        return new MaterialFinder(getMaterial()).modifies(itemStack);
    }

    @Override
    public NBTTagCompound write(NBTTagCompound nbtTags) {
        super.write(nbtTags);
        nbtTags.setInteger("type", 2);
        materialItem.writeToNBT(nbtTags);
        return nbtTags;
    }

    @Override
    protected void read(NBTTagCompound nbtTags) {
        super.read(nbtTags);
        materialItem = new ItemStack(nbtTags);
    }

    @Override
    public void write(TileNetworkList data) {
        data.add(2);
        super.write(data);
        data.add(MekanismUtils.getID(materialItem));
        data.add(materialItem.getCount());
        data.add(materialItem.getItemDamage());
    }

    @Override
    protected void read(ByteBuf dataStream) {
        super.read(dataStream);
        materialItem = new ItemStack(Item.getItemById(dataStream.readInt()), dataStream.readInt(), dataStream.readInt());
    }

    @Override
    public int hashCode() {
        int code = 1;
        code = 31 * code + MekanismUtils.getID(materialItem);
        code = 31 * code + materialItem.getCount();
        code = 31 * code + materialItem.getItemDamage();
        return code;
    }

    @Override
    public boolean equals(Object filter) {
        return super.equals(filter) && filter instanceof MMaterialFilter materialFilter && materialFilter.materialItem.isItemEqual(materialItem);
    }

    @Override
    public MMaterialFilter clone() {
        MMaterialFilter filter = new MMaterialFilter();
        filter.replaceStack = replaceStack;
        filter.requireStack = requireStack;
        filter.materialItem = materialItem;
        return filter;
    }

    @Nonnull
    @Override
    public ItemStack getMaterialItem() {
        return materialItem;
    }

    @Override
    public void setMaterialItem(@Nonnull ItemStack stack) {
        materialItem = stack;
    }
}
