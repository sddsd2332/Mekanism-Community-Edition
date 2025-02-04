package mekanism.common.tile;

import mekanism.api.Coord4D;
import mekanism.api.gear.IModule;
import mekanism.api.gear.ModuleData;
import mekanism.common.Upgrade;
import mekanism.common.base.IBoundingBlock;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.content.gear.IModuleContainerItem;
import mekanism.common.content.gear.IModuleItem;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.item.ItemModule;
import mekanism.common.tile.prefab.TileEntityOperationalMachine;
import mekanism.common.util.ChargeUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NonNullListSynchronized;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

public class TileEntityModificationStation extends TileEntityOperationalMachine implements IBoundingBlock {


    public TileEntityModificationStation() {
        super("null", MachineType.MODIFICATION_STATION, 0, 40);
        inventory = NonNullListSynchronized.withSize(4, ItemStack.EMPTY);
        upgradeComponent.setSupported(Upgrade.MUFFLING, false);
        upgradeComponent.setSupported(Upgrade.SPEED, false);
        upgradeComponent.setSupported(Upgrade.ENERGY, false);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            ChargeUtils.discharge(1, this);
            ItemStack moduleSlot = inventory.get(2);
            ItemStack containerSlot = inventory.get(3);
            if (MekanismUtils.canFunction(this)) {
                boolean operated = false;
                if (getEnergy() >= energyPerTick && !moduleSlot.isEmpty() && !containerSlot.isEmpty()) {
                    ModuleData<?> data = ((IModuleItem) moduleSlot.getItem()).getModuleData();
                    // make sure the container supports this module
                    if (ModuleHelper.get().getSupported(containerSlot).contains(data)) {
                        // make sure we can still install more of this module
                        IModule<?> module = ModuleHelper.get().load(containerSlot, data);
                        if (module == null || module.getInstalledCount() < data.getMaxStackSize()) {
                            operated = true;
                            operatingTicks++;
                            electricityStored.addAndGet(-energyPerTick);
                            if (operatingTicks == ticksRequired) {
                                operatingTicks = 0;
                                ((IModuleContainerItem) containerSlot.getItem()).addModule(containerSlot, data);
                                moduleSlot.shrink(1);
                            }
                        }
                    }

                }
                if (!operated) {
                    operatingTicks = 0;
                }
            }
            prevEnergy = getEnergy();
        }
    }

    public void removeModule(EntityPlayer player, ModuleData<?> type) {
        ItemStack stack = inventory.get(3);
        if (!stack.isEmpty()) {
            IModuleContainerItem container = (IModuleContainerItem) stack.getItem();
            if (container.hasModule(stack, type) && player.inventory.add(1, type.getStack())) {
                container.removeModule(stack, type);
            }
        }
    }

    @Override
    public boolean sideIsConsumer(EnumFacing side) {
        return side == facing.getOpposite();
    }


    @Override
    public boolean renderUpdate() {
        return false;
    }

    @Override
    public boolean lightUpdate() {
        return false;
    }

    @NotNull
    @Override
    public int[] getSlotsForFace(@NotNull EnumFacing side) {
        return new int[0];
    }

    @Override
    public void onPlace() {
        EnumFacing right = MekanismUtils.getRight(facing);
        MekanismUtils.makeBoundingBlock(world, getPos().up(), Coord4D.get(this));
        MekanismUtils.makeBoundingBlock(world, getPos().offset(right), Coord4D.get(this));
        MekanismUtils.makeBoundingBlock(world, getPos().offset(right).up(), Coord4D.get(this));
    }

    @Override
    public void onBreak() {
        EnumFacing right = MekanismUtils.getRight(facing);
        world.setBlockToAir(getPos().offset(right).up());
        world.setBlockToAir(getPos().offset(right));
        world.setBlockToAir(getPos().up());
        world.setBlockToAir(getPos());
    }

    @Override
    public void recalculateUpgradables(Upgrade upgrade) {
    }

    @Override
    public boolean supportsAsync() {
        return false;
    }

}
