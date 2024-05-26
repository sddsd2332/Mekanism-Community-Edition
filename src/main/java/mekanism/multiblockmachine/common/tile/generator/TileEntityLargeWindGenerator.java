package mekanism.multiblockmachine.common.tile.generator;

import io.netty.buffer.ByteBuf;
import mekanism.api.Coord4D;
import mekanism.api.TileNetworkList;
import mekanism.common.base.IAdvancedBoundingBlock;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.CableUtils;
import mekanism.common.util.ChargeUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class TileEntityLargeWindGenerator extends TileEntityMultiblockGenerator implements IAdvancedBoundingBlock {

    public static final float SPEED = 32F;
    public static final float SPEED_SCALED = 256F / SPEED;
    static final String[] methods = new String[]{"getEnergy", "getOutput", "getMaxEnergy", "getEnergyNeeded", "getMultiplier"};
    private static final int[] SLOTS = {0};
    public int numPowering;
    private double angle;
    private float currentMultiplier;
    private boolean isBlacklistDimension = false;
    private int explode;
    private boolean machineStop;
    private boolean machineStop2;
    private boolean bladeDamage;

    public TileEntityLargeWindGenerator() {
        super("wind", "LargeWindGenerator", MekanismConfig.current().multiblock.largewindGeneratorStorage.val(), MekanismConfig.current().multiblock.largewindGenerationMax.val() * 2);
        inventory = NonNullList.withSize(SLOTS.length, ItemStack.EMPTY);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        // Check the blacklist and force an update if we're in the blacklist. Otherwise, we'll never send
        // an initial activity status and the client (in MP) will show the windmills turning while not
        // generating any power
        isBlacklistDimension = MekanismConfig.current().generators.windGenerationDimBlacklist.val().contains(world.provider.getDimension());
        if (isBlacklistDimension) {
            setActive(false);
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            ChargeUtils.charge(0, this);
            // If we're in a blacklisted dimension, there's nothing more to do
            if (isBlacklistDimension) {
                return;
            }
            RangeStops();
            if (ticker % 20 == 0) {
                currentMultiplier = getMultiplier();
                setActive(MekanismUtils.canFunction(this) && currentMultiplier > 0 && !machineStop && !machineStop2);
            }
            if (getActive()) {
                setEnergy(electricityStored + (MekanismConfig.current().multiblock.largewindGenerationMin.val() * currentMultiplier));
                if (MekanismConfig.current().multiblock.largewindGenerationDamage.val()) {
                    kill();
                }
            }
            if (explode != 0) {
                bladeDamage = true;
            }
            if (explode >= MekanismConfig.current().multiblock.largewindGenerationExplodeCount.val() && MekanismConfig.current().multiblock.largewindGenerationExplode.val()) {
                explode();
            }
            if (MekanismUtils.canFunction(this)) {
                CableUtils.emit(this, 4);
            }
        } else if (getActive()) {
            angle = (angle + (getPos().getY() + 46F) / SPEED_SCALED) % 360;
        }
    }

    private void RangeStops() {
        //If there is a large wind turbine within a radius of 50 tiles, the machine will stop working
        //It shouldn't happen
        BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();
        for (int yPos = 0; yPos <= 50; yPos++) {
            for (int xPos = -50; xPos <= 50; xPos++) {
                for (int zPos = -50; zPos <= 50; zPos++) {
                    if (yPos == 0 && xPos == 0 && zPos == 0) {
                        break;
                    }
                    testPos.setPos(getPos().getX() + xPos, getPos().getY() + yPos, getPos().getZ() + zPos);
                    if (getWorld().getTileEntity(testPos) instanceof TileEntityLargeWindGenerator) {
                        machineStop2 = true;
                    }
                }
            }
        }
    }


    @Override
    public void handlePacketData(ByteBuf dataStream) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            int type = dataStream.readInt();
            if (type == 1) {
                machineStop = !machineStop;
            }else if (type == 2){
                machineStop2 = !machineStop2;
            }
        }
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            currentMultiplier = dataStream.readFloat();
            isBlacklistDimension = dataStream.readBoolean();
            numPowering = dataStream.readInt();
            machineStop = dataStream.readBoolean();
            machineStop2 = dataStream.readBoolean();
            explode = dataStream.readInt();
            bladeDamage = dataStream.readBoolean();
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(currentMultiplier);
        data.add(isBlacklistDimension);
        data.add(numPowering);
        data.add(machineStop);
        data.add(machineStop2);
        data.add(explode);
        data.add(bladeDamage);
        return data;
    }

    public float getMultiplier() {
        //Wind turbine head and tail
        BlockPos head = getPos().up(46);
        BlockPos head2 = getPos().up(46);
        if (facing == EnumFacing.NORTH) {
            head = getPos().up(46).north(3);
            head2 = getPos().up(46).north(4);
        } else if (facing == EnumFacing.SOUTH) {
            head = getPos().up(46).south(3);
            head2 = getPos().up(46).south(4);
        } else if (facing == EnumFacing.WEST) {
            head = getPos().up(46).west(3);
            head2 = getPos().up(46).west(4);
        } else if (facing == EnumFacing.EAST) {
            head = getPos().up(46).east(3);
            head2 = getPos().up(46).east(4);
        }


        if (world.canSeeSky(head) && world.canSeeSky(head2)) {
            int minY = MekanismConfig.current().multiblock.largewindGenerationMinY.val();
            int maxY = MekanismConfig.current().multiblock.largewindGenerationMaxY.val();
            float clampedY = Math.min(maxY, Math.max(minY, head.getY()));
            float minG = (float) MekanismConfig.current().multiblock.largewindGenerationMin.val();
            float maxG = (float) MekanismConfig.current().multiblock.largewindGenerationMax.val();
            int rangeY = maxY < minY ? minY - maxY : maxY - minY;
            float rangG = maxG < minG ? minG - maxG : maxG - minG;
            float slope = rangG / rangeY;
            float toGen = minG + (slope * (clampedY - minY));
            return (toGen / minG) * 45 - (explode * 0.01F);
        }

        return 0;
    }

    @Override
    public String[] getMethods() {
        return methods;
    }


    @Override
    public Object[] invoke(int method, Object[] arguments) throws NoSuchMethodException {
        return switch (method) {
            case 0 -> new Object[]{electricityStored};
            case 1 -> new Object[]{output};
            case 2 -> new Object[]{BASE_MAX_ENERGY};
            case 3 -> new Object[]{BASE_MAX_ENERGY - electricityStored};
            case 4 -> new Object[]{getMultiplier()};
            default -> throw new NoSuchMethodException();
        };
    }

    @Override
    public boolean canOperate() {
        return electricityStored < BASE_MAX_ENERGY && getMultiplier() > 0 && MekanismUtils.canFunction(this);
    }

    @Override
    public void onPlace() {

        //bottom
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }
                MekanismUtils.makeAdvancedBoundingBlock(world, getPos().add(x, 0, z), Coord4D.get(this));
                world.notifyNeighborsOfStateChange(getPos().add(x, 0, z), getBlockType(), true);
            }
        }
        //Second floor
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                MekanismUtils.makeBoundingBlock(world, getPos().add(x, 1, z), Coord4D.get(this));
                world.notifyNeighborsOfStateChange(getPos().add(x, 1, z), getBlockType(), true);
            }
        }
        if (facing == EnumFacing.WEST) {
            for (int z = -1; z <= 1; z++) {
                MekanismUtils.makeBoundingBlock(world, getPos().add(3, 1, z), Coord4D.get(this));
                world.notifyNeighborsOfStateChange(getPos().add(3, 1, z), getBlockType(), true);
            }
        } else if (facing == EnumFacing.EAST) {
            for (int z = -1; z <= 1; z++) {
                MekanismUtils.makeBoundingBlock(world, getPos().add(-3, 1, z), Coord4D.get(this));
                world.notifyNeighborsOfStateChange(getPos().add(-3, 1, z), getBlockType(), true);
            }
        } else if (facing == EnumFacing.NORTH) {
            for (int x = -1; x <= 1; x++) {
                MekanismUtils.makeBoundingBlock(world, getPos().add(x, 1, 3), Coord4D.get(this));
                world.notifyNeighborsOfStateChange(getPos().add(x, 1, 3), getBlockType(), true);
            }
        } else if (facing == EnumFacing.SOUTH) {
            for (int x = -1; x <= 1; x++) {
                MekanismUtils.makeBoundingBlock(world, getPos().add(x, 1, -3), Coord4D.get(this));
                world.notifyNeighborsOfStateChange(getPos().add(x, 1, -3), getBlockType(), true);
            }
        }
        //Wind turbine tower height (including head)
        for (int y = 2; y <= 47; y++) {
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    MekanismUtils.makeBoundingBlock(world, getPos().add(x, y, z), Coord4D.get(this));
                    world.notifyNeighborsOfStateChange(getPos().add(x, y, z), getBlockType(), true);
                }
            }
        }

        //Wind turbine head and tail
        for (int y = 43; y <= 47; y++) {
            if (facing == EnumFacing.SOUTH) {
                for (int x = -2; x <= 2; x++) {
                    for (int z = -5; z <= -3; z++) {
                        MekanismUtils.makeBoundingBlock(world, getPos().add(x, y, z), Coord4D.get(this));
                        world.notifyNeighborsOfStateChange(getPos().add(x, y, z), getBlockType(), true);
                    }
                    for (int z = 3; z <= 4; z++) {
                        MekanismUtils.makeBoundingBlock(world, getPos().add(x, y, z), Coord4D.get(this));
                        world.notifyNeighborsOfStateChange(getPos().add(x, y, z), getBlockType(), true);
                    }
                }
            } else if (facing == EnumFacing.NORTH) {
                for (int x = -2; x <= 2; x++) {
                    for (int z = 3; z <= 5; z++) {
                        MekanismUtils.makeBoundingBlock(world, getPos().add(x, y, z), Coord4D.get(this));
                        world.notifyNeighborsOfStateChange(getPos().add(x, y, z), getBlockType(), true);
                    }
                    for (int z = -4; z <= -3; z++) {
                        MekanismUtils.makeBoundingBlock(world, getPos().add(x, y, z), Coord4D.get(this));
                        world.notifyNeighborsOfStateChange(getPos().add(x, y, z), getBlockType(), true);
                    }
                }
            } else if (facing == EnumFacing.EAST) {
                for (int z = -2; z <= 2; z++) {
                    for (int x = -5; x <= -3; x++) {
                        MekanismUtils.makeBoundingBlock(world, getPos().add(x, y, z), Coord4D.get(this));
                        world.notifyNeighborsOfStateChange(getPos().add(x, y, z), getBlockType(), true);
                    }
                    for (int x = 3; x <= 4; x++) {
                        MekanismUtils.makeBoundingBlock(world, getPos().add(x, y, z), Coord4D.get(this));
                        world.notifyNeighborsOfStateChange(getPos().add(x, y, z), getBlockType(), true);
                    }
                }
            } else if (facing == EnumFacing.WEST) {
                for (int z = -2; z <= 2; z++) {
                    for (int x = 3; x <= 5; x++) {
                        MekanismUtils.makeBoundingBlock(world, getPos().add(x, y, z), Coord4D.get(this));
                        world.notifyNeighborsOfStateChange(getPos().add(x, y, z), getBlockType(), true);
                    }
                    for (int x = -4; x <= -3; x++) {
                        MekanismUtils.makeBoundingBlock(world, getPos().add(x, y, z), Coord4D.get(this));
                        world.notifyNeighborsOfStateChange(getPos().add(x, y, z), getBlockType(), true);
                    }
                }
            }
        }


        // Check to see if the placement is happening in a blacklisted dimension
        isBlacklistDimension = MekanismConfig.current().generators.windGenerationDimBlacklist.val().contains(world.provider.getDimension());
    }


    @Override
    public boolean sideIsOutput(EnumFacing side) {
        return side == MekanismUtils.getLeft(facing) || side == MekanismUtils.getRight(facing) || side == facing;
    }

    @Override
    public void onBreak() {
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }
                world.setBlockToAir(getPos().add(x, 0, z));
            }
        }
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                world.setBlockToAir(getPos().add(x, 1, z));
            }
        }
        if (facing == EnumFacing.WEST) {
            for (int z = -1; z <= 1; z++) {
                world.setBlockToAir(getPos().add(3, 1, z));
            }
        } else if (facing == EnumFacing.EAST) {
            for (int z = -1; z <= 1; z++) {
                world.setBlockToAir(getPos().add(-3, 1, z));
            }
        } else if (facing == EnumFacing.NORTH) {
            for (int x = -1; x <= 1; x++) {
                world.setBlockToAir(getPos().add(x, 1, 3));
            }
        } else if (facing == EnumFacing.SOUTH) {
            for (int x = -1; x <= 1; x++) {
                world.setBlockToAir(getPos().add(x, 1, -3));
            }
        }
        for (int y = 2; y <= 47; y++) {
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    world.setBlockToAir(getPos().add(x, y, z));
                }
            }
        }
        for (int y = 43; y <= 47; y++) {
            if (facing == EnumFacing.SOUTH) {
                for (int x = -2; x <= 2; x++) {
                    for (int z = -5; z <= -3; z++) {
                        world.setBlockToAir(getPos().add(x, y, z));
                    }
                    for (int z = 3; z <= 4; z++) {
                        world.setBlockToAir(getPos().add(x, y, z));
                    }
                }
            } else if (facing == EnumFacing.NORTH) {
                for (int x = -2; x <= 2; x++) {
                    for (int z = 3; z <= 5; z++) {
                        world.setBlockToAir(getPos().add(x, y, z));
                    }
                    for (int z = -4; z <= -3; z++) {
                        world.setBlockToAir(getPos().add(x, y, z));
                    }
                }
            } else if (facing == EnumFacing.EAST) {
                for (int z = -2; z <= 2; z++) {
                    for (int x = -5; x <= -3; x++) {
                        world.setBlockToAir(getPos().add(x, y, z));
                    }
                    for (int x = 3; x <= 4; x++) {
                        world.setBlockToAir(getPos().add(x, y, z));
                    }
                }
            } else if (facing == EnumFacing.WEST) {
                for (int z = -2; z <= 2; z++) {
                    for (int x = 3; x <= 5; x++) {
                        world.setBlockToAir(getPos().add(x, y, z));
                    }
                    for (int x = -4; x <= -3; x++) {
                        world.setBlockToAir(getPos().add(x, y, z));
                    }
                }
            }
        }


        invalidate();
        world.setBlockToAir(getPos());
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        numPowering = nbtTags.getInteger("numPowering");
        explode = nbtTags.getInteger("explode");
        machineStop = nbtTags.getBoolean("machineStop");
        machineStop2 = nbtTags.getBoolean("machineStop2");
        bladeDamage = nbtTags.getBoolean("bladeDamage");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setInteger("numPowering", numPowering);
        nbtTags.setInteger("explode", explode);
        nbtTags.setBoolean("machineStop", machineStop);
        nbtTags.setBoolean("machineStop2", machineStop2);
        nbtTags.setBoolean("bladeDamage", bladeDamage);
    }


    @Override
    public boolean renderUpdate() {
        return false;
    }

    @Override
    public boolean lightUpdate() {
        return false;
    }

    public float getCurrentMultiplier() {
        return currentMultiplier;
    }

    public double getAngle() {
        return angle;
    }

    public boolean getMachineStop2() {
        return machineStop2;
    }

    public boolean getMachineStop() {
        return machineStop;
    }

    public boolean getBladeDamage() {
        return bladeDamage;
    }

    public int getBladeDamageNumber() {
        return explode;
    }

    public boolean isBlacklistDimension() {
        return isBlacklistDimension;
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return SLOTS;
    }

    @Override
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack) {
        return ChargeUtils.canBeCharged(stack);
    }

    @Override
    public boolean canBoundReceiveEnergy(BlockPos location, EnumFacing side) {
        return false;
    }

    public void kill() {
        AxisAlignedBB death_zone = new AxisAlignedBB(getPos());
        if (facing == EnumFacing.NORTH) {
            death_zone = new AxisAlignedBB(
                    getPos().up(46).north(3).getX() + 22, getPos().up(46).north(3).getY() + 22, getPos().up(46).north(3).getZ(),
                    getPos().up(46).north(4).getX() - 22, getPos().up(46).north(4).getY() - 22, getPos().up(46).north(4).getZ());
        } else if (facing == EnumFacing.SOUTH) {
            death_zone = new AxisAlignedBB(
                    getPos().up(46).south(3).getX() + 22, getPos().up(46).south(3).getY() + 22, getPos().up(46).south(3).getZ(),
                    getPos().up(46).south(4).getX() - 22, getPos().up(46).south(4).getY() - 22, getPos().up(46).south(4).getZ());
        } else if (facing == EnumFacing.WEST) {
            death_zone = new AxisAlignedBB(
                    getPos().up(46).west(3).getX(), getPos().up(46).west(3).getY() + 22, getPos().up(46).west(3).getZ() + 22,
                    getPos().up(46).west(4).getX(), getPos().up(46).west(4).getY() - 22, getPos().up(46).west(4).getZ() - 22);
        } else if (facing == EnumFacing.EAST) {
            death_zone = new AxisAlignedBB(
                    getPos().up(46).east(3).getX(), getPos().up(46).east(3).getY() + 22, getPos().up(46).east(3).getZ() + 22,
                    getPos().up(46).east(4).getX(), getPos().up(46).east(4).getY() - 22, getPos().up(46).east(4).getZ() - 22);
        }

        List<Entity> entitiesToDie = getWorld().getEntitiesWithinAABB(Entity.class, death_zone);

        for (Entity entity : entitiesToDie) {
            if (entity instanceof EntityPlayer player && player.capabilities.isCreativeMode) {
                return;
            }
            entity.attackEntityFrom(DamageSource.FLY_INTO_WALL, Float.MAX_VALUE);
            machineStop = true;
            explode += 1;
        }
    }


    private void explode() {
        if (facing == EnumFacing.NORTH) {
            world.createExplosion(null, getPos().up(46).north(4).getX(), getPos().up(46).north(4).getY(), getPos().up(46).north(4).getZ(), MekanismConfig.current().multiblock.largewindGenerationBlastRadius.val(), true);
            world.spawnParticle(EnumParticleTypes.LAVA, getPos().up(46).north(3).getX(), getPos().up(46).north(3).getY(), getPos().up(46).north(3).getZ(), 0, 0, 0);
        } else if (facing == EnumFacing.SOUTH) {
            world.createExplosion(null, getPos().up(46).south(4).getX(), getPos().up(46).south(4).getY(), getPos().up(46).south(4).getZ(), MekanismConfig.current().multiblock.largewindGenerationBlastRadius.val(), true);
            world.spawnParticle(EnumParticleTypes.LAVA, getPos().up(46).south(3).getX(), getPos().up(46).south(3).getY(), getPos().up(46).south(3).getZ(), 0, 0, 0);
        } else if (facing == EnumFacing.WEST) {
            world.createExplosion(null, getPos().up(46).west(4).getX(), getPos().up(46).west(4).getY(), getPos().up(46).west(4).getZ(), MekanismConfig.current().multiblock.largewindGenerationBlastRadius.val(), true);
            world.spawnParticle(EnumParticleTypes.LAVA, getPos().up(46).west(3).getX(), getPos().up(46).west(3).getY(), getPos().up(46).west(3).getZ(), 0, 0, 0);
        } else if (facing == EnumFacing.EAST) {
            world.createExplosion(null, getPos().up(46).east(4).getX(), getPos().up(46).east(4).getY(), getPos().up(46).east(4).getZ(), MekanismConfig.current().multiblock.largewindGenerationBlastRadius.val(), true);
            world.spawnParticle(EnumParticleTypes.LAVA, getPos().up(46).east(3).getX(), getPos().up(46).east(3).getY(), getPos().up(46).east(3).getZ(), 0, 0, 0);
        }
    }


    @Override
    public boolean canBoundOutPutEnergy(BlockPos coord, EnumFacing side) {
        EnumFacing left = MekanismUtils.getLeft(facing);
        EnumFacing right = MekanismUtils.getRight(facing);
        if (coord.equals(getPos().offset(left, 3))) {
            return side == left;
        } else if (coord.equals(getPos().offset(right, 3))) {
            return side == right;
        } else if (coord.equals(getPos().offset(facing, 3))) {
            return side == facing;
        }
        return false;
    }

    @Override
    public void onPower() {
        numPowering++;
    }

    @Override
    public void onNoPower() {
        numPowering--;
    }

    @Override
    public boolean isPowered() {
        return redstone || numPowering > 0;
    }

    @Override
    public NBTTagCompound getConfigurationData(NBTTagCompound nbtTags) {
        return nbtTags;
    }

    @Override
    public void setConfigurationData(NBTTagCompound nbtTags) {

    }

    @Override
    public String getDataType() {
        return getBlockType().getTranslationKey() + "." + fullName + ".name";
    }


    @Override
    public boolean hasOffsetCapability(@NotNull Capability<?> capability, @Nullable EnumFacing side, @NotNull Vec3i offset) {
        if (isOffsetCapabilityDisabled(capability, side, offset)) {
            return false;
        }
        if (isStrictEnergy(capability) || capability == CapabilityEnergy.ENERGY || isTesla(capability, side)) {
            return true;
        }
        return hasCapability(capability, side);
    }

    @Override
    public <T> T getOffsetCapability(@Nonnull Capability<T> capability, EnumFacing side, @Nonnull Vec3i offset) {
        if (isOffsetCapabilityDisabled(capability, side, offset)) {
            return null;
        } else if (isStrictEnergy(capability)) {
            return (T) this;
        } else if (isTesla(capability, side)) {
            return (T) getTeslaEnergyWrapper(side);
        } else if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(getForgeEnergyWrapper(side));
        }
        return getCapability(capability, side);
    }

    @Override
    public boolean isOffsetCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side, @Nonnull Vec3i offset) {
        if (isStrictEnergy(capability) || capability == CapabilityEnergy.ENERGY || isTesla(capability, side)) {
            EnumFacing left = MekanismUtils.getLeft(facing);
            EnumFacing right = MekanismUtils.getRight(facing);
            if (offset.equals(new Vec3i(left.getXOffset() * 3, 0, left.getZOffset() * 3))) {
                //Disable if left power port but wrong side of the port
                return side != left;
            } else if (offset.equals(new Vec3i(right.getXOffset() * 3, 0, right.getZOffset() * 3))) {
                //Disable if right power port but wrong side of the port
                return side != right;
            } else if (offset.equals(new Vec3i(facing.getXOffset() * 3, 0, facing.getZOffset() * 3))) {
                return side != facing;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side) {
        if (isStrictEnergy(capability) || capability == CapabilityEnergy.ENERGY || isTesla(capability, side)) {
            return true;
        }
        return super.isCapabilityDisabled(capability, side);
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 16384.0D;
    }
}
