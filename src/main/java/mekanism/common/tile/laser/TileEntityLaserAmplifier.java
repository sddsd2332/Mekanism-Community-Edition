package mekanism.common.tile.laser;

import io.netty.buffer.ByteBuf;
import mekanism.api.Coord4D;
import mekanism.api.TileNetworkList;
import mekanism.api.energy.IStrictEnergyOutputter;
import mekanism.api.energy.IStrictEnergyStorage;
import mekanism.api.lasers.ILaserReceptor;
import mekanism.common.LaserManager;
import mekanism.common.LaserManager.LaserInfo;
import mekanism.common.Mekanism;
import mekanism.common.base.IRedstoneControl;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.computer.IComputerIntegration;
import mekanism.common.security.ISecurityTile;
import mekanism.common.tile.component.TileComponentSecurity;
import mekanism.common.tile.prefab.TileEntityContainerBlock;
import mekanism.common.util.InventoryUtils;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NonNullListSynchronized;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class TileEntityLaserAmplifier extends TileEntityContainerBlock implements ILaserReceptor, IRedstoneControl, IStrictEnergyOutputter, IStrictEnergyStorage,
        IComputerIntegration, ISecurityTile {

    public static final double MAX_ENERGY = 5E9;
    private static final String[] methods = new String[]{"getEnergy", "getMaxEnergy"};
    public double collectedEnergy = 0;
    public double lastFired = 0;
    public double minThreshold = 0;
    public double maxThreshold = 5E9;
    public int ticks = 0;
    public int time = 0;
    public RedstoneControl controlType = RedstoneControl.DISABLED;
    public boolean on = false;
    public Coord4D digging;
    public double diggingProgress;
    public boolean emittingRedstone;
    public int currentRedstoneLevel;
    public RedstoneOutput outputMode = RedstoneOutput.OFF;
    public TileComponentSecurity securityComponent = new TileComponentSecurity(this);

    public TileEntityLaserAmplifier() {
        super("LaserAmplifier");
        inventory = NonNullListSynchronized.withSize(0, ItemStack.EMPTY);
    }

    @Override
    public void receiveLaserEnergy(double energy, EnumFacing side) {
        setEnergy(getEnergy() + energy);
    }

    @Override
    public boolean canLasersDig() {
        return false;
    }

    @Override
    public void onUpdate() {
        if (world.isRemote) {
            if (on) {
                RayTraceResult mop = LaserManager.fireLaserClient(this, facing, lastFired, world);
                Coord4D hitCoord = mop == null ? null : new Coord4D(mop, world);
                if (hitCoord == null || !hitCoord.equals(digging)) {
                    digging = hitCoord;
                    diggingProgress = 0;
                }

                if (hitCoord != null) {
                    IBlockState blockHit = hitCoord.getBlockState(world);
                    TileEntity tileHit = hitCoord.getTileEntity(world);
                    float hardness = blockHit.getBlockHardness(world, hitCoord.getPos());

                    if (!(hardness < 0 || (LaserManager.isReceptor(tileHit, mop.sideHit) && !LaserManager.getReceptor(tileHit, mop.sideHit).canLasersDig()))) {
                        diggingProgress += lastFired;
                        if (diggingProgress < hardness * MekanismConfig.current().general.laserEnergyNeededPerHardness.val()) {
                            Mekanism.proxy.addHitEffects(hitCoord, mop);
                        }
                    }
                }

            }
        } else {
            boolean prevRedstone = emittingRedstone;
            emittingRedstone = false;
            if (ticks < time) {
                ticks++;
            } else {
                ticks = 0;
            }

            if (toFire() > 0) {
                double firing = toFire();
                if (!on || firing != lastFired) {
                    on = true;
                    lastFired = firing;
                    Mekanism.packetHandler.sendUpdatePacket(this);
                }

                LaserInfo info = LaserManager.fireLaser(this, facing, firing, world);
                Coord4D hitCoord = info.movingPos == null ? null : new Coord4D(info.movingPos, world);

                if (hitCoord == null || !hitCoord.equals(digging)) {
                    digging = hitCoord;
                    diggingProgress = 0;
                }

                if (hitCoord != null) {
                    IBlockState blockHit = hitCoord.getBlockState(world);
                    TileEntity tileHit = hitCoord.getTileEntity(world);
                    float hardness = blockHit.getBlockHardness(world, hitCoord.getPos());
                    if (!(hardness < 0 || (LaserManager.isReceptor(tileHit, info.movingPos.sideHit) && !LaserManager.getReceptor(tileHit, info.movingPos.sideHit).canLasersDig()))) {
                        diggingProgress += firing;
                        if (diggingProgress >= hardness * MekanismConfig.current().general.laserEnergyNeededPerHardness.val()) {
                            LaserManager.breakBlock(hitCoord, true, world, pos);
                            diggingProgress = 0;
                        }
                    }
                }
                emittingRedstone = info.foundEntity;
                setEnergy(getEnergy() - firing);
            } else if (on) {
                on = false;
                diggingProgress = 0;
                Mekanism.packetHandler.sendUpdatePacket(this);
            }

            if (outputMode != RedstoneOutput.ENTITY_DETECTION) {
                emittingRedstone = false;
            }
            int newRedstoneLevel = getRedstoneLevel();
            if (newRedstoneLevel != currentRedstoneLevel) {
                markNoUpdateSync();
                currentRedstoneLevel = newRedstoneLevel;
            }
            if (emittingRedstone != prevRedstone) {
                world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
            }
        }
    }

    @Override
    public boolean supportsAsync() {
        return false;
    }

    @Override
    public double pullEnergy(EnumFacing side, double amount, boolean simulate) {
        double toGive = Math.min(getEnergy(), amount);
        if (toGive < 0.0001) {
            return 0;
        }
        if (!simulate) {
            setEnergy(getEnergy() - toGive);
        }
        return toGive;
    }

    @Override
    public double getEnergy() {
        return collectedEnergy;
    }

    @Override
    public void setEnergy(double energy) {
        collectedEnergy = Math.max(0, Math.min(energy, MAX_ENERGY));
    }

    public boolean shouldFire() {
        return collectedEnergy >= minThreshold && ticks >= time && MekanismUtils.canFunction(this);
    }

    public double toFire() {
        return shouldFire() ? Math.min(collectedEnergy, maxThreshold) : 0;
    }

    public int getRedstoneLevel() {
        if (outputMode != RedstoneOutput.ENERGY_CONTENTS) {
            return 0;
        }
        return MekanismUtils.redstoneLevelFromContents(getEnergy(), getMaxEnergy());
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(on);
        data.add(minThreshold);
        data.add(maxThreshold);
        data.add(time);
        data.add(collectedEnergy);
        data.add(lastFired);
        data.add(controlType.ordinal());
        data.add(emittingRedstone);
        data.add(outputMode.ordinal());
        return data;
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            switch (dataStream.readInt()) {
                case 0 -> minThreshold = Math.min(MAX_ENERGY, MekanismUtils.convertToJoules(dataStream.readDouble()));
                case 1 -> maxThreshold = Math.min(MAX_ENERGY, MekanismUtils.convertToJoules(dataStream.readDouble()));
                case 2 -> time = dataStream.readInt();
                case 3 ->
                        outputMode = RedstoneOutput.values()[outputMode.ordinal() == RedstoneOutput.values().length - 1 ? 0 : outputMode.ordinal() + 1];
            }
            return;
        }

        super.handlePacketData(dataStream);

        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            on = dataStream.readBoolean();
            minThreshold = dataStream.readDouble();
            maxThreshold = dataStream.readDouble();
            time = dataStream.readInt();
            collectedEnergy = dataStream.readDouble();
            lastFired = dataStream.readDouble();
            controlType = RedstoneControl.values()[dataStream.readInt()];
            emittingRedstone = dataStream.readBoolean();
            outputMode = RedstoneOutput.values()[dataStream.readInt()];
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        on = nbtTags.getBoolean("on");
        minThreshold = nbtTags.getDouble("minThreshold");
        maxThreshold = nbtTags.getDouble("maxThreshold");
        time = nbtTags.getInteger("time");
        collectedEnergy = nbtTags.getDouble("collectedEnergy");
        lastFired = nbtTags.getDouble("lastFired");
        controlType = RedstoneControl.values()[nbtTags.getInteger("controlType")];
        outputMode = RedstoneOutput.values()[nbtTags.getInteger("outputMode")];
    }


    @Override
   public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setBoolean("on", on);
        nbtTags.setDouble("minThreshold", minThreshold);
        nbtTags.setDouble("maxThreshold", maxThreshold);
        nbtTags.setInteger("time", time);
        nbtTags.setDouble("collectedEnergy", collectedEnergy);
        nbtTags.setDouble("lastFired", lastFired);
        nbtTags.setInteger("controlType", controlType.ordinal());
        nbtTags.setInteger("outputMode", outputMode.ordinal());
    }

    @Override
    public RedstoneControl getControlType() {
        return controlType;
    }

    @Override
    public void setControlType(RedstoneControl type) {
        controlType = type;
    }

    @Override
    public boolean canPulse() {
        return true;
    }

    @Override
    public boolean canOutputEnergy(EnumFacing side) {
        return true;
    }

    @Override
    public double getMaxEnergy() {
        return MAX_ENERGY;
    }

    @Override
    public String[] getMethods() {
        return methods;
    }

    @Override
    public Object[] invoke(int method, Object[] arguments) throws NoSuchMethodException {
        return switch (method) {
            case 0 -> new Object[]{getEnergy()};
            case 1 -> new Object[]{getMaxEnergy()};
            default -> throw new NoSuchMethodException();
        };
    }

    @Override
    public TileComponentSecurity getSecurity() {
        return securityComponent;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == Capabilities.ENERGY_STORAGE_CAPABILITY || capability == Capabilities.ENERGY_OUTPUTTER_CAPABILITY
                || capability == Capabilities.LASER_RECEPTOR_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == Capabilities.ENERGY_STORAGE_CAPABILITY) {
            return Capabilities.ENERGY_STORAGE_CAPABILITY.cast(this);
        }
        if (capability == Capabilities.ENERGY_OUTPUTTER_CAPABILITY) {
            return Capabilities.ENERGY_OUTPUTTER_CAPABILITY.cast(this);
        }
        if (capability == Capabilities.LASER_RECEPTOR_CAPABILITY) {
            return Capabilities.LASER_RECEPTOR_CAPABILITY.cast(this);
        }
        return super.getCapability(capability, facing);
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return InventoryUtils.EMPTY;
    }

    @Override
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.isCapabilityDisabled(capability, side);
    }

    public enum RedstoneOutput {
        OFF("off"),
        ENTITY_DETECTION("entityDetection"),
        ENERGY_CONTENTS("energyContents");

        private String unlocalizedName;

        RedstoneOutput(String name) {
            unlocalizedName = name;
        }

        public String getName() {
            return LangUtils.localize("gui." + unlocalizedName);
        }
    }
}
