package mekanism.common.tile.multiblock;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mekanism.api.Coord4D;
import mekanism.api.TileNetworkList;
import mekanism.common.Mekanism;
import mekanism.common.base.IFluidContainerManager;
import mekanism.common.block.BlockBasic;
import mekanism.common.content.tank.SynchronizedTankData;
import mekanism.common.content.tank.SynchronizedTankData.ValveData;
import mekanism.common.content.tank.TankCache;
import mekanism.common.content.tank.TankUpdateProtocol;
import mekanism.common.integration.computer.IComputerIntegration;
import mekanism.common.multiblock.MultiblockManager;
import mekanism.common.util.FluidContainerUtils;
import mekanism.common.util.FluidContainerUtils.ContainerEditMode;
import mekanism.common.util.InventoryUtils;
import mekanism.common.util.NonNullListSynchronized;
import mekanism.common.util.TileUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import java.util.Set;

public class TileEntityDynamicTank extends TileEntityMultiblock<SynchronizedTankData> implements IComputerIntegration, IFluidContainerManager {

    protected static final int[] SLOTS = {0, 1};

    public static final String[] methods = new String[]{"getAmount", "getCapacity", "getLiquidType"};

    /**
     * A client-sided set of valves on this tank's structure that are currently active, used on the client for rendering fluids.
     */
    public Set<ValveData> valveViewing = new ObjectOpenHashSet<>();

    /**
     * The capacity this tank has on the client-side.
     */
    public int clientCapacity;

    public float prevScale;

    public TileEntityDynamicTank() {
        super("DynamicTank");
    }

    public TileEntityDynamicTank(String name) {
        super(name);
        inventory = NonNullListSynchronized.withSize(SLOTS.length, ItemStack.EMPTY);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) {
            if (clientHasStructure && isRendering) {
                if (structure != null) {
                    float targetScale = (float) (structure.fluidStored != null ? structure.fluidStored.amount :structure.gasstored != null ? structure.gasstored.amount : 0) / clientCapacity;
                    if (Math.abs(prevScale - targetScale) > 0.01) {
                        prevScale = (9 * prevScale + targetScale) / 10;
                    }
                }
            } else {
                for (ValveData data : valveViewing) {
                    TileEntityDynamicTank tileEntity = (TileEntityDynamicTank) data.location.getTileEntity(world);
                    if (tileEntity != null) {
                        tileEntity.clientHasStructure = false;
                    }
                }
                valveViewing.clear();
            }
        } else if (structure != null) {
            if (structure.fluidStored != null && structure.fluidStored.amount <= 0) {
                structure.fluidStored = null;
                markNoUpdateSync();
            } else if (structure.gasstored != null && structure.gasstored.amount <= 0) {
                structure.gasstored = null;
                markNoUpdateSync();
            }
            if (isRendering) {
                boolean needsValveUpdate = false;
                for (ValveData data : structure.valves) {
                    if (data.activeTicks > 0) {
                        data.activeTicks--;
                    }
                    if (data.activeTicks > 0 != data.prevActive) {
                        needsValveUpdate = true;
                    }
                    data.prevActive = data.activeTicks > 0;
                }
                if (needsValveUpdate || structure.needsRenderUpdate()) {
                    sendPacketToRenderer();
                }
                structure.prevFluid = structure.fluidStored != null ? structure.fluidStored.copy() : null;
                structure.prevGas = structure.gasstored != null ? structure.gasstored.copy() : null;
                manageInventory();
            }
        }
    }

    //todo
    public void manageInventory() {
        int neededFluid = (structure.volume * TankUpdateProtocol.FLUID_PER_TANK) - (structure.fluidStored != null ? structure.fluidStored.amount : 0);
        if (FluidContainerUtils.isFluidContainer(structure.inventory.get(0))) {
            structure.fluidStored = FluidContainerUtils.handleContainerItem(this, structure.inventory, structure.editMode, structure.fluidStored, neededFluid, 0, 1, null);
            Mekanism.packetHandler.sendUpdatePacket(this);
        }
    }

    @Override
    public boolean onActivate(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (!player.isSneaking() && structure != null) {
            if (!BlockBasic.manageInventory(player, this, hand, stack)) {
                Mekanism.packetHandler.sendUpdatePacket(this);
                player.openGui(Mekanism.instance, 18, world, getPos().getX(), getPos().getY(), getPos().getZ());
            } else {
                player.inventory.markDirty();
                sendPacketToRenderer();
            }
            return true;
        }
        return false;
    }

    @Override
    protected SynchronizedTankData getNewStructure() {
        return new SynchronizedTankData();
    }

    @Override
    public TankCache getNewCache() {
        return new TankCache();
    }

    @Override
    protected TankUpdateProtocol getProtocol() {
        return new TankUpdateProtocol(this);
    }

    @Override
    public MultiblockManager<SynchronizedTankData> getManager() {
        return Mekanism.tankManager;
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        if (structure != null) {
            data.add(structure.volume * TankUpdateProtocol.FLUID_PER_TANK);
            data.add(structure.editMode.ordinal());
            TileUtils.addFluidStack(data, structure.fluidStored);
            TileUtils.addGasStack(data, structure.gasstored);
            if (isRendering) {
                Set<ValveData> toSend = new ObjectOpenHashSet<>();

                for (ValveData valveData : structure.valves) {
                    if (valveData.activeTicks > 0) {
                        toSend.add(valveData);
                    }
                }
                data.add(toSend.size());
                for (ValveData valveData : toSend) {
                    valveData.location.write(data);
                    data.add(valveData.side);
                }
            }
        }
        return data;
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            if (clientHasStructure) {
                clientCapacity = dataStream.readInt();
                structure.editMode = ContainerEditMode.values()[dataStream.readInt()];
                structure.fluidStored = TileUtils.readFluidStack(dataStream);
                structure.gasstored = TileUtils.readGasStack(dataStream);
                if (isRendering) {
                    int size = dataStream.readInt();
                    valveViewing.clear();
                    for (int i = 0; i < size; i++) {
                        ValveData data = new ValveData();
                        data.location = Coord4D.read(dataStream);
                        data.side = EnumFacing.byIndex(dataStream.readInt());
                        valveViewing.add(data);
                        TileEntityDynamicTank tileEntity = (TileEntityDynamicTank) data.location.getTileEntity(world);
                        if (tileEntity != null) {
                            tileEntity.clientHasStructure = true;
                        }
                    }
                }
            }
        }
    }

    public int getScaledFluidLevel(long i) {
        if (clientCapacity == 0 || structure.fluidStored == null) {
            return 0;
        }
        return (int) (structure.fluidStored.amount * i / clientCapacity);
    }

    @Override
    public ContainerEditMode getContainerEditMode() {
        if (structure != null) {
            return structure.editMode;
        }
        return ContainerEditMode.BOTH;
    }

    @Override
    public void setContainerEditMode(ContainerEditMode mode) {
        if (structure == null) {
            return;
        }
        structure.editMode = mode;
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

    @Override
    public String[] getMethods() {
        return methods;
    }

    @Override
    public Object[] invoke(int method, Object[] args) throws NoSuchMethodException {
        return switch (method) {
            case 0 ->
                    new Object[]{structure != null ? structure.fluidStored != null ? structure.fluidStored.amount : structure.gasstored != null ? structure.gasstored.amount : 0 : 0};
            case 1 -> new Object[]{structure != null ? structure.volume : 0};
            case 2 ->
                    new Object[]{structure != null ? structure.fluidStored != null ? structure.fluidStored.getLocalizedName() : structure.gasstored != null ? structure.gasstored.getGas().getLocalizedName() : null : null};
            default -> throw new NoSuchMethodException();
        };
    }
}
