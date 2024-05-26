package mekanism.common.tile;

import io.netty.buffer.ByteBuf;
import mekanism.api.Coord4D;
import mekanism.api.TileNetworkList;
import mekanism.common.Mekanism;
import mekanism.common.base.IBoundingBlock;
import mekanism.common.base.ITileNetwork;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.network.PacketDataRequest.DataRequestMessage;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.tile.prefab.TileEntityBasicBlock;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;

/**
 * Multi-block used by wind turbines, solar panels, and other machines
 */
public class TileEntityBoundingBlock extends TileEntity implements ITileNetwork, ITickable {

    private BlockPos mainPos = BlockPos.ORIGIN;

    public boolean receivedCoords;

    public int prevPower;


    @Override
    public void update() {
        if (!world.isRemote) {
            final TileEntity tile = getMainTile();
            if (!(tile instanceof IBoundingBlock)) {
                if (MekanismConfig.current().mekce.VirtualErrors.val()) {
                    Mekanism.logger.error("Found tile {} instead of an IBoundingBlock, at {}. Multiblock cannot function, Blocks to be removed {}", tile, getMainPos(), getPos());
                }
                world.setBlockToAir(getPos());
            }
        }
    }

    public void setMainLocation(BlockPos pos) {
        receivedCoords = pos != null;
        if (!world.isRemote) {
            mainPos = pos;
            Mekanism.packetHandler.sendUpdatePacket(this);
        }
    }

    public BlockPos getMainPos() {
        if (mainPos == null) {
            mainPos = BlockPos.ORIGIN;
        }
        return mainPos;
    }


    @Override
    public void validate() {
        super.validate();
        if (world.isRemote) {
            Mekanism.packetHandler.sendToServer(new DataRequestMessage(Coord4D.get(this)));
        }
    }

    public TileEntity getMainTile() {
        if (receivedCoords && world.isBlockLoaded(getMainPos())) {
            return world.getTileEntity(getMainPos());
        }
        return null;
    }

    public void onNeighborChange(Block block) {
        final TileEntity tile = getMainTile();
        if (tile instanceof TileEntityBasicBlock tileEntity) {
            int power = world.getRedstonePowerFromNeighbors(getPos());
            if (prevPower != power) {
                if (power > 0) {
                    onPower();
                } else {
                    onNoPower();
                }
                prevPower = power;
                Mekanism.packetHandler.sendToAllTracking(new TileEntityMessage(tileEntity), this);
            }
        }
    }

    public void onPower() {
    }

    public void onNoPower() {
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        if (world.isRemote) {
            mainPos = new BlockPos(dataStream.readInt(), dataStream.readInt(), dataStream.readInt());
            prevPower = dataStream.readInt();
            receivedCoords = dataStream.readBoolean();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTags) {
        super.readFromNBT(nbtTags);
        mainPos = new BlockPos(nbtTags.getInteger("mainX"), nbtTags.getInteger("mainY"), nbtTags.getInteger("mainZ"));
        prevPower = nbtTags.getInteger("prevPower");
        receivedCoords = nbtTags.getBoolean("receivedCoords");
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTags) {
        super.writeToNBT(nbtTags);
        nbtTags.setInteger("mainX", getMainPos().getX());
        nbtTags.setInteger("mainY", getMainPos().getY());
        nbtTags.setInteger("mainZ", getMainPos().getZ());
        nbtTags.setInteger("prevPower", prevPower);
        nbtTags.setBoolean("receivedCoords", receivedCoords);
        return nbtTags;
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        data.add(getMainPos().getX());
        data.add(getMainPos().getY());
        data.add(getMainPos().getZ());
        data.add(prevPower);
        data.add(receivedCoords);
        return data;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == Capabilities.TILE_NETWORK_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == Capabilities.TILE_NETWORK_CAPABILITY) {
            return Capabilities.TILE_NETWORK_CAPABILITY.cast(this);
        }
        return super.getCapability(capability, facing);
    }

}
