package mekanism.multiblockmachine.common.tile.generator;

import io.netty.buffer.ByteBuf;
import mekanism.api.Coord4D;
import mekanism.api.TileNetworkList;
import mekanism.common.Mekanism;
import mekanism.common.Upgrade;
import mekanism.common.base.IRedstoneControl;
import mekanism.common.base.IUpgradeTile;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.computer.IComputerIntegration;
import mekanism.common.security.ISecurityTile;
import mekanism.common.tile.component.TileComponentSecurity;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.tile.prefab.TileEntityEffectsBlock;
import mekanism.common.util.CableUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.multiblockmachine.common.MekanismMultiblockMachine;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachineGenerator.MultiblockMachineGeneratorType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public abstract class TileEntityMultiblockGenerator extends TileEntityEffectsBlock implements IComputerIntegration, IRedstoneControl, ISecurityTile, IUpgradeTile {

    /**
     * Output per tick this generator can transfer.
     */
    public double output;

    /**
     * This machine's current RedstoneControl type.
     */
    public RedstoneControl controlType;

    public TileComponentSecurity securityComponent = new TileComponentSecurity(this);
    public TileComponentUpgrade upgradeComponent;

    /**
     * Generator -- a block that produces energy. It has a certain amount of fuel it can store as well as an output rate.
     *
     * @param name      - full name of this generator
     * @param maxEnergy - how much energy this generator can store
     */
    public TileEntityMultiblockGenerator(String soundPath, MultiblockMachineGeneratorType type, double maxEnergy, double out, int slot) {
        super("gen." + soundPath, type.getBlockName(), maxEnergy);
        output = out;
        controlType = RedstoneControl.DISABLED;
        upgradeComponent = new TileComponentUpgrade(this, slot, Upgrade.THREAD, MekanismMultiblockMachine.proxy, type.blockType.getBlock(), type.meta,type.guiId);
    }

    public int Thread() {
        int thread = 1;
        if (upgradeComponent.isUpgradeInstalled(Upgrade.THREAD)) {
            thread = upgradeComponent.getUpgrades(Upgrade.THREAD) + 1;
        }
        return thread;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            if (MekanismConfig.current().multiblock.destroyDisabledBlocks.val()) {
                MultiblockMachineGeneratorType type = MultiblockMachineGeneratorType.get(getBlockType(), getBlockMetadata());
                if (type != null && !type.isEnabled()) {
                    Mekanism.logger.info("Destroying generator of type '" + type.getBlockName() + "' at coords " + Coord4D.get(this) + " as according to config.");
                    world.setBlockToAir(getPos());
                    return;
                }
            }
            if (MekanismUtils.canFunction(this)) {
                Mekanism.EXECUTE_MANAGER.addSyncTask(() -> CableUtils.emit(this));
            }
        }
    }

    @Override
    public boolean supportsAsync() {
        return false;
    }

    @Override
    public double getMaxOutput() {
        return output;
    }

    @Override
    public boolean sideIsConsumer(EnumFacing side) {
        return false;
    }

    @Override
    public boolean sideIsOutput(EnumFacing side) {
        return side == facing;
    }

    /**
     * Whether or not this generator can operate.
     *
     * @return if the generator can operate
     */
    public abstract boolean canOperate();

    @Override
    public boolean canSetFacing(@Nonnull EnumFacing facing) {
        return facing != EnumFacing.DOWN && facing != EnumFacing.UP;
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            controlType = RedstoneControl.values()[dataStream.readInt()];
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(controlType.ordinal());
        return data;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        controlType = RedstoneControl.values()[nbtTags.getInteger("controlType")];
    }


    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setInteger("controlType", controlType.ordinal());

    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public boolean renderUpdate() {
        return true;
    }

    @Override
    public boolean lightUpdate() {
        return true;
    }

    @Override
    public RedstoneControl getControlType() {
        return controlType;
    }

    @Override
    public void setControlType(RedstoneControl type) {
        controlType = type;
        MekanismUtils.saveChunk(this);
    }

    @Override
    public boolean canPulse() {
        return false;
    }

    @Override
    public TileComponentSecurity getSecurity() {
        return securityComponent;
    }

    @Override
    public TileComponentUpgrade getComponent() {
        return upgradeComponent;
    }


    @Override
    public void recalculateUpgradables(Upgrade upgrade) {
        super.recalculateUpgradables(upgrade);
        if (upgrade == Upgrade.ENERGY) {
            maxEnergy = MekanismUtils.getMaxEnergy(this, BASE_MAX_ENERGY);
            setEnergy(Math.min(getMaxEnergy(), getEnergy()));
        }
    }
}
