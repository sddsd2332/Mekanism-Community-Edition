package mekanism.common.tile;

import mekanism.api.Coord4D;
import mekanism.common.Upgrade;
import mekanism.common.base.IBoundingBlock;
import mekanism.common.base.IModuleUpgradeItem;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.item.armour.ItemMekAsuitArmour;
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

    public ItemMekAsuitArmour armour;

    public TileEntityModificationStation() {
        super("null", MachineType.MODIFICATION_STATION, 0, 40);
        inventory = NonNullListSynchronized.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            ChargeUtils.discharge(1, this);
            ItemStack armourStack = inventory.get(3);
            ItemStack moduleStack = inventory.get(2);
            if (armourStack.getItem() instanceof ItemMekAsuitArmour itemMekAsuitArmour) {
                armour = itemMekAsuitArmour;
            }
            if (!armourStack.isEmpty() && armourStack.getItem() instanceof ItemMekAsuitArmour && armour != null) {
                if (ItemDataUtils.hasData(armourStack, "module")) {
                    Map<moduleUpgrade, Integer> upgrade = moduleUpgrade.buildMap(ItemDataUtils.getDataMap(armourStack));
                    armour.upgrades.putAll(upgrade);
                } else {
                    armour.upgrades.clear();
                }
            }

            if (moduleStack.getItem() instanceof IModuleUpgradeItem item) {
                if (!armourStack.isEmpty() && ItemDataUtils.hasData(armourStack, "module") && armour != null) {
                    if (armour.upgrades.containsKey(item.getmoduleUpgrade(moduleStack))) {
                        if (armour.upgrades.get(item.getmoduleUpgrade(moduleStack)) >= item.getmoduleUpgrade(moduleStack).getMax()) {
                            return;
                        }
                    }
                }
                if (getActive() && armour != null) {
                    electricityStored.addAndGet(-energyPerTick);
                    if ((operatingTicks + 1) < ticksRequired) {
                        operatingTicks++;
                    } else if ((operatingTicks + 1) >= ticksRequired) {
                        operatingTicks = 0;
                        addUpgrades(item.getmoduleUpgrade(moduleStack), moduleStack.getCount());
                        moduleUpgrade.saveMap(armour.upgrades, ItemDataUtils.getDataMap(armourStack));
                    }
                }
            }
            if (MekanismUtils.canFunction(this) && getEnergy() >= energyPerTick && !moduleStack.isEmpty() && !armourStack.isEmpty() && armour != null) {
                if (moduleStack.getItem() instanceof IModuleUpgradeItem item) {
                    setActive(armour.supports(item.getmoduleUpgrade(moduleStack)));
                }
                setActive(false);
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

    public void addUpgrades(moduleUpgrade upgrade, int maxAvailable) {
        int installed = armour.getUpgrades(upgrade);
        if (installed < upgrade.getMax()) {
            int toAdd = Math.min(upgrade.getMax() - installed, maxAvailable);
            if (toAdd > 0) {
                armour.upgrades.put(upgrade, installed + toAdd);
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
