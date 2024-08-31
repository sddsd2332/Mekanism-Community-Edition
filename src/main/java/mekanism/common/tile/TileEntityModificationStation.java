package mekanism.common.tile;

import mekanism.api.Coord4D;
import mekanism.common.Upgrade;
import mekanism.common.base.IBoundingBlock;
import mekanism.common.base.IModuleUpgrade;
import mekanism.common.base.IModuleUpgradeItem;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.moduleUpgrade;
import mekanism.common.tile.prefab.TileEntityOperationalMachine;
import mekanism.common.util.ChargeUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NonNullListSynchronized;
import mekanism.common.util.UpgradeHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

import static mekanism.common.util.ItemDataUtils.DATA_ID;

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
            if (canOperate() && MekanismUtils.canFunction(this) && getEnergy() >= energyPerTick && !moduleSlot.isEmpty() && !containerSlot.isEmpty() && moduleSlot.getItem() instanceof IModuleUpgradeItem module && containerSlot.getItem() instanceof IModuleUpgrade item) {
                if (UpgradeHelper.getUpgradeLevel(containerSlot, module.getmoduleUpgrade(moduleSlot)) >= module.getmoduleUpgrade(moduleSlot).getMax()) {
                    return;
                }
                setActive(true);
                electricityStored.addAndGet(-energyPerTick);
                if ((operatingTicks + 1) < ticksRequired) {
                    operatingTicks++;
                } else if ((operatingTicks + 1) >= ticksRequired) {
                    operatingTicks = 0;
                    addUpgrades(module.getmoduleUpgrade(moduleSlot), moduleSlot.getCount());
                }
            } else if (prevEnergy >= getEnergy()) {
                setActive(false);
            }
            if (!getActive()) {
                operatingTicks = 0;
            }
            prevEnergy = getEnergy();
        }
    }


    public boolean canOperate() {
        ItemStack moduleSlot = inventory.get(2);
        ItemStack containerSlot = inventory.get(3);
        if (!moduleSlot.isEmpty() && !containerSlot.isEmpty() && moduleSlot.getItem() instanceof IModuleUpgradeItem item && containerSlot.getItem() instanceof IModuleUpgrade upgrade) {
            return upgrade.getValidModule(containerSlot).contains(item.getmoduleUpgrade(moduleSlot));
        }
        return false;
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

    public void addUpgrades(moduleUpgrade upgrade, int maxAvailable) {
        ItemStack containerSlot = inventory.get(3);
        if (!containerSlot.isEmpty()) {
            int installed = UpgradeHelper.getUpgradeLevel(containerSlot, upgrade);
            if (installed < upgrade.getMax()) {
                int toAdd = Math.min(upgrade.getMax() - installed, maxAvailable);
                if (toAdd > 0) {
                    UpgradeHelper.setUpgradeLevel(containerSlot, upgrade, installed + toAdd);
                    inventory.get(2).shrink(toAdd);
                }
            }
        }
    }

    public void removeUpgrade(moduleUpgrade upgrade, boolean removeAll) {
        ItemStack containerSlot = inventory.get(3);
        if (!containerSlot.isEmpty() && containerSlot.getTagCompound() != null) {
            int installed = UpgradeHelper.getUpgradeLevel(containerSlot, upgrade);
            if (installed > 0) {
                int toRemove = removeAll ? installed : 1;
                UpgradeHelper.setUpgradeLevel(containerSlot, upgrade, installed - toRemove);
            }
            if (UpgradeHelper.getUpgradeLevel(containerSlot, upgrade) == 0) {
                NBTTagCompound upgradeTag = containerSlot.getOrCreateSubCompound(DATA_ID);
                upgradeTag.removeTag(upgrade.getName());
            }
        }
    }

    @Override
    public void recalculateUpgradables(Upgrade upgrade) {
    }

    @Override
    public boolean supportsAsync() {
        return false;
    }

}
