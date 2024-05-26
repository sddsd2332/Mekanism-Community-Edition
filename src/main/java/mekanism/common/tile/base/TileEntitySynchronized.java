package mekanism.common.tile.base;

import mekanism.common.Mekanism;
import mekanism.common.base.IActiveState;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * 可异步类型tile方块。
 */
//TODO：需要让它正确的运行，虽然可以运行
public class TileEntitySynchronized extends TileEntity {

    private boolean inUpdateTask = false;
    private boolean inMarkTask = false;

    private boolean requireUpdateLight = false;

    private long lastUpdateTick = 0;

    public final void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readCustomNBT(compound);
    }

    public void readCustomNBT(NBTTagCompound compound) {
    }

    public void readNetNBT(NBTTagCompound compound) {
    }

    @Nonnull
    public final NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        NBTTagCompound writeToNBT = super.writeToNBT(compound);
        writeCustomNBT(writeToNBT);
        return writeToNBT;
    }

    public void writeCustomNBT(NBTTagCompound compound) {
    }

    public void writeNetNBT(NBTTagCompound compound) {
    }


    @Nonnull
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        super.writeToNBT(compound);
        writeCustomNBT(compound);
        writeNetNBT(compound);
        return new SPacketUpdateTileEntity(getPos(), 255, compound);
    }


    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = new NBTTagCompound();
        super.writeToNBT(compound);
        writeCustomNBT(compound);
        return compound;
    }

    public final void onDataPacket(@Nonnull NetworkManager manager, @Nonnull SPacketUpdateTileEntity packet) {
        super.onDataPacket(manager, packet);
        readCustomNBT(packet.getNbtCompound());
        readNetNBT(packet.getNbtCompound());
    }


    @SuppressWarnings("ConstantValue")
    public void markNoUpdate() {
        World world = getWorld();
        if (world == null) {
            return;
        }
        markDirty();
        updateLight();
        inMarkTask = false;
        lastUpdateTick = world.getTotalWorldTime();
    }

    @SuppressWarnings("ConstantValue")
    public void updateLight() {
        if (!requireUpdateLight) {
            return;
        }
        World world = getWorld();
        if (world == null) {
            return;
        }
        world.markBlockRangeForRenderUpdate(pos, pos);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof IActiveState) || ((IActiveState) tileEntity).lightUpdate() && MekanismConfig.current().client.machineEffects.val()) {
            MekanismUtils.updateAllLightTypes(world, pos);
        }
        requireUpdateLight = false;
    }

    public void markForUpdate() {
        markNoUpdate();
        notifyUpdate();
    }

    public void notifyUpdate() {
        World world = getWorld();
        if (world == null) { //防止世界是无
            return;
        }
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        inUpdateTask = false;
    }


    public void markNoUpdateSync() {
        if (inMarkTask) {
            return;
        }
        Mekanism.EXECUTE_MANAGER.addTEMarkNoUpdateTask(this);
        inMarkTask = true;
    }

    public void markChunkDirty() {
        if (world == null) {
            return;
        }
        world.markChunkDirty(pos, this);
    }

    /**
     * <p>markForUpdate 的同步实现，防止意料之外的 CME。</p>
     * <p>*** 只能保证 mekceu 自身对世界的线程安全 ***</p>
     */
    public void markForUpdateSync() {
        if (inUpdateTask) {
            return;
        }
        Mekanism.EXECUTE_MANAGER.addTEUpdateTask(this);
        inUpdateTask = true;
        inMarkTask = true;
    }

    public boolean isInUpdateTask() {
        return inUpdateTask;
    }

    public long getLastUpdateTick() {
        return lastUpdateTick;
    }

    public boolean isRequireUpdateLight() {
        return requireUpdateLight;
    }

    public void setRequireUpdateLight(final boolean requireUpdateLight) {
        this.requireUpdateLight = requireUpdateLight;
    }
}
