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
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NonNullListSynchronized;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

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
            ItemStack UpgradeStack = inventory.get(3);
            ItemStack moduleStack = inventory.get(2);
            if (!UpgradeStack.isEmpty() && UpgradeStack.getItem() instanceof IModuleUpgrade module) {
                if (ItemDataUtils.hasData(UpgradeStack, "module")) {
                    Map<moduleUpgrade, Integer> upgrade = moduleUpgrade.buildMap(ItemDataUtils.getDataMap(UpgradeStack));
                    module.upgrades.putAll(upgrade);
                } else {
                    module.upgrades.clear();
                }
            }

            if (moduleStack.getItem() instanceof IModuleUpgradeItem item) {
                if (!UpgradeStack.isEmpty() && UpgradeStack.getItem() instanceof IModuleUpgrade module && ItemDataUtils.hasData(UpgradeStack, "module")) {
                    if (module.upgrades.containsKey(item.getmoduleUpgrade(moduleStack))) {
                        if (module.upgrades.get(item.getmoduleUpgrade(moduleStack)) >= item.getmoduleUpgrade(moduleStack).getMax()) {
                            return;
                        }
                    }
                }

                if (getActive() && !UpgradeStack.isEmpty() && UpgradeStack.getItem() instanceof IModuleUpgrade module) {
                    electricityStored.addAndGet(-energyPerTick);
                    if ((operatingTicks + 1) < ticksRequired) {
                        operatingTicks++;
                    } else if ((operatingTicks + 1) >= ticksRequired) {
                        operatingTicks = 0;
                        addUpgrades(item.getmoduleUpgrade(moduleStack), moduleStack.getCount(), module);
                        moduleUpgrade.saveMap(module.upgrades, ItemDataUtils.getDataMap(UpgradeStack));
                    }
                }
            }
            if (MekanismUtils.canFunction(this) && getEnergy() >= energyPerTick && !moduleStack.isEmpty() && !UpgradeStack.isEmpty()) {
                if (moduleStack.getItem() instanceof IModuleUpgradeItem item && UpgradeStack.getItem() instanceof IModuleUpgrade upgrade) {
                    setActive(upgrade.getValidModule(UpgradeStack).contains(item.getmoduleUpgrade(moduleStack)));
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

    public void addUpgrades(moduleUpgrade upgrade, int maxAvailable, IModuleUpgrade stack) {
        int installed = stack.getUpgrades(upgrade);
        if (installed < upgrade.getMax()) {
            int toAdd = Math.min(upgrade.getMax() - installed, maxAvailable);
            if (toAdd > 0) {
                stack.upgrades.put(upgrade, installed + toAdd);
                inventory.get(2).shrink(toAdd);
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
