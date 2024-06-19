package mekanism.common.tile;

import mekanism.api.Coord4D;
import mekanism.common.MekanismItems;
import mekanism.common.base.IBoundingBlock;
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

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class TileEntityModificationStation extends TileEntityOperationalMachine implements IBoundingBlock {

    private Map<moduleUpgrade, Integer> upgrades = new EnumMap<>(moduleUpgrade.class);
    private Set<moduleUpgrade> supported = EnumSet.noneOf(moduleUpgrade.class);

    public TileEntityModificationStation() {
        super("null", MachineType.MODIFICATION_STATION, 0, 40);
        inventory = NonNullListSynchronized.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            ChargeUtils.discharge(1, this);
            if (MekanismUtils.canFunction(this) && getEnergy() >= energyPerTick && !inventory.get(2).isEmpty() && !inventory.get(3).isEmpty()) {
                moduleUpgrade type = ((IModuleUpgradeItem) inventory.get(2).getItem()).getmoduleUpgrade(inventory.get(2));
                if (inventory.get(2).getItem() == MekanismItems.ModuleUpgrade && (inventory.get(3).getItem() == MekanismItems.MekAsuitBoots || inventory.get(3).getItem() == MekanismItems.MekAsuitHelmet || inventory.get(3).getItem() == MekanismItems.MekAsuitChestplate || inventory.get(3).getItem() == MekanismItems.MekAsuitLeggings)) {

                    setActive(true);
                    electricityStored.addAndGet(-energyPerTick);
                    if ((operatingTicks + 1) < ticksRequired) {
                        operatingTicks++;
                    } else if ((operatingTicks + 1) >= ticksRequired) {
                        operatingTicks = 0;
                        ItemStack stack = inventory.get(3);
                        Map<moduleUpgrade, Integer> stackMap = moduleUpgrade.buildMap(ItemDataUtils.getDataMapIfPresent(stack));
                        int added = addUpgrades(type, inventory.get(2).getCount());
                        moduleUpgrade.saveMap(stackMap,ItemDataUtils.getDataMap(stack));
                        ItemDataUtils.setCompound(stack, "module", moduleUpgrade.getTagFor(type, added));
                        if (added > 0) {
                            inventory.get(2).shrink(added);
                        }
                    }

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

    public int getUpgrades(moduleUpgrade upgrade) {
        return upgrades.getOrDefault(upgrade, 0);
    }

    public int addUpgrades(moduleUpgrade upgrade, int maxAvailable) {
        int installed = getUpgrades(upgrade);
        if (installed < upgrade.getMax()) {
            int toAdd = Math.min(upgrade.getMax() - installed, maxAvailable);
            if (toAdd > 0) {
                this.upgrades.put(upgrade, installed + toAdd);
                return toAdd;
            }
        }
        return 0;
    }

    public void removeUpgrade(moduleUpgrade upgrade, boolean removeAll) {
        int installed = getUpgrades(upgrade);
        if (installed > 0) {
            int toRemove = removeAll ? installed : 1;
            upgrades.put(upgrade, Math.max(0, getUpgrades(upgrade) - toRemove));
        }
        if (upgrades.get(upgrade) == 0) {
            upgrades.remove(upgrade);
        }
    }
}
