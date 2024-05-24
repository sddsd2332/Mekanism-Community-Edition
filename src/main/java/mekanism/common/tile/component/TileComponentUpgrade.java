package mekanism.common.tile.component;

import io.netty.buffer.ByteBuf;
import mekanism.api.TileNetworkList;
import mekanism.common.Mekanism;
import mekanism.common.Upgrade;
import mekanism.common.base.ITileComponent;
import mekanism.common.base.IUpgradeItem;
import mekanism.common.tile.prefab.TileEntityContainerBlock;
import net.minecraft.nbt.NBTTagCompound;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TileComponentUpgrade implements ITileComponent {

    /**
     * How long it takes this machine to install an upgrade.
     */
    public static int UPGRADE_TICKS_REQUIRED = 40;
    /**
     * How many upgrade ticks have progressed.
     */
    public int upgradeTicks;
    /**
     * TileEntity implementing this component.
     */
    public TileEntityContainerBlock tileEntity;
    private Map<Upgrade, Integer> upgrades = new EnumMap<>(Upgrade.class);
    private Set<Upgrade> supported = EnumSet.noneOf(Upgrade.class);
    /**
     * The inventory slot the upgrade slot of this component occupies.
     */
    private int upgradeSlot;
    private boolean isBack;


    public TileComponentUpgrade(TileEntityContainerBlock tile, int slot) {
        tileEntity = tile;
        upgradeSlot = slot;
        setSupported(Upgrade.SPEED);
        setSupported(Upgrade.ENERGY);
        tile.components.add(this);
    }

    public TileComponentUpgrade(TileEntityContainerBlock tile, int slot,boolean isBackButton) {
        this(tile, slot);
        tileEntity = tile;
        upgradeSlot = slot;
        isBack = isBackButton;
    }

    public TileComponentUpgrade(TileEntityContainerBlock tile, int slot, Upgrade upgrade) {
        tileEntity = tile;
        upgradeSlot = slot;
        setSupported(upgrade);
        tile.components.add(this);
    }


    public void readFrom(TileComponentUpgrade upgrade) {
        upgrades = upgrade.upgrades;
        supported = upgrade.supported;
        upgradeSlot = upgrade.upgradeSlot;
        upgradeTicks = upgrade.upgradeTicks;
    }

    // This SHOULD continue to directly use te.inventory, as it is needed for Entangleporter upgrades, since it messes with IInventory.
    @Override
    public void tick() {
        if (!tileEntity.getWorld().isRemote) {
            if (!tileEntity.inventory.get(upgradeSlot).isEmpty() && tileEntity.inventory.get(upgradeSlot).getItem() instanceof IUpgradeItem) {
                Upgrade type = ((IUpgradeItem) tileEntity.inventory.get(upgradeSlot).getItem()).getUpgradeType(tileEntity.inventory.get(upgradeSlot));

                if (supports(type) && getUpgrades(type) < type.getMax()) {
                    if (upgradeTicks < UPGRADE_TICKS_REQUIRED) {
                        upgradeTicks++;
                    } else if (upgradeTicks == UPGRADE_TICKS_REQUIRED) {
                        upgradeTicks = 0;
                        int added = addUpgrades(type, tileEntity.inventory.get(upgradeSlot).getCount());
                        if (added > 0) {
                            tileEntity.inventory.get(upgradeSlot).shrink(added);
                        }
                        Mekanism.packetHandler.sendUpdatePacket(tileEntity);
                        tileEntity.markDirty();
                    }
                } else {
                    upgradeTicks = 0;
                }
            } else {
                upgradeTicks = 0;
            }
        }
    }

    public int getUpgradeSlot() {
        return upgradeSlot;
    }


    public void setUpgradeSlot(int i) {
        upgradeSlot = i;
    }

    public boolean isUpgradeInstalled(Upgrade upgrade) {
        return upgrades.containsKey(upgrade);
    }

    public int getUpgrades(Upgrade upgrade) {
        return upgrades.getOrDefault(upgrade, 0);
    }

    public int addUpgrades(Upgrade upgrade, int maxAvailable) {
        int installed = getUpgrades(upgrade);
        if (installed < upgrade.getMax()) {
            int toAdd = Math.min(upgrade.getMax() - installed, maxAvailable);
            if (toAdd > 0) {
                this.upgrades.put(upgrade, installed + toAdd);
                tileEntity.recalculateUpgradables(upgrade);
                if (upgrade == Upgrade.MUFFLING) {
                    //Send an update packet to the client to update the number of muffling upgrades installed
                    tileEntity.doRestrictedTick();
                }
                tileEntity.markDirty();
                return toAdd;
            }
        }
        return 0;
    }

    public void removeUpgrade(Upgrade upgrade, boolean removeAll) {
        int installed = getUpgrades(upgrade);
        if (installed > 0) {
            int toRemove = removeAll ? installed : 1;
            upgrades.put(upgrade, Math.max(0, getUpgrades(upgrade) - toRemove));
        }
        if (upgrades.get(upgrade) == 0) {
            upgrades.remove(upgrade);
        }
        tileEntity.recalculateUpgradables(upgrade);
    }

    public void setSupported(Upgrade upgrade) {
        setSupported(upgrade, true);
    }

    public void setSupported(Upgrade upgrade, boolean isSupported) {
        if (isSupported) {
            supported.add(upgrade);
        } else {
            supported.remove(upgrade);
        }
    }

    public boolean supports(Upgrade upgrade) {
        return supported.contains(upgrade);
    }

    public Set<Upgrade> getInstalledTypes() {
        return upgrades.keySet();
    }

    public Set<Upgrade> getSupportedTypes() {
        return supported;
    }

    public void clearSupportedTypes() {
        supported.clear();
    }

    @Override
    public void read(ByteBuf dataStream) {
        upgrades.clear();
        int amount = dataStream.readInt();

        for (int i = 0; i < amount; i++) {
            upgrades.put(Upgrade.values()[dataStream.readInt()], dataStream.readInt());
        }
        upgradeTicks = dataStream.readInt();
        for (Upgrade upgrade : getSupportedTypes()) {
            tileEntity.recalculateUpgradables(upgrade);
        }
    }

    @Override
    public void write(TileNetworkList data) {
        data.add(upgrades.size());
        for (Entry<Upgrade, Integer> entry : upgrades.entrySet()) {
            data.add(entry.getKey().ordinal());
            data.add(entry.getValue());
        }
        data.add(upgradeTicks);
    }

    @Override
    public void read(NBTTagCompound nbtTags) {
        upgrades = Upgrade.buildMap(nbtTags);
        for (Upgrade upgrade : getSupportedTypes()) {
            tileEntity.recalculateUpgradables(upgrade);
        }
    }

    @Override
    public void write(NBTTagCompound nbtTags) {
        Upgrade.saveMap(upgrades, nbtTags);
    }

    @Override
    public void invalidate() {
    }


    public boolean getBackButton() {
        return isBack;
    }
}
