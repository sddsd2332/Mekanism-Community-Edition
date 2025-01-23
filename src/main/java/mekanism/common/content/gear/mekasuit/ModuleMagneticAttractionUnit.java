package mekanism.common.content.gear.mekasuit;

import mekanism.api.energy.IEnergizedItem;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.TextComponentGroup;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.ModuleConfigItem.EnumData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ModuleMagneticAttractionUnit extends Module {

    private ModuleConfigItem<Range> range;

    @Override
    public void init() {
        range = addConfigItem(new ModuleConfigItem<>(this, "range", MekanismLang.MODULE_RANGE, new EnumData<>(Range.class, getInstalledCount() + 1), Range.LOW));
    }

    @Override
    public void tickServer(EntityPlayer player) {
        super.tickServer(player);
        if (range.get() != Range.OFF) {
            float size = 4 + range.get().getRange();
            double usage = MekanismConfig.current().meka.mekaSuitEnergyUsageItemAttraction.val() * (range.get().getRange());
            boolean free = usage == 0 || player.isCreative();
            IEnergizedItem energyContainer = free ? null : getEnergyContainer();
            if (free || (energyContainer != null && energyContainer.getEnergy(getContainer()) >= (usage))) {
                //If the energy cost is free, or we have enough energy for at least one pull grab all the items that can be picked up.
                //Note: We check distance afterwards so that we aren't having to calculate a bunch of distances when we may run out
                // of energy, and calculating distance is a bit more expensive than just checking if it can be picked up
                List<EntityItem> items = player.world.getEntitiesWithinAABB(EntityItem.class, player.getEntityBoundingBox().grow(size, size, size), item -> !item.cannotPickup());
                for (EntityItem item : items) {
                    if (item.getDistance(player) > 0.001) {
                        if (free) {
                            pullItem(player, item);
                        } else if (useEnergy(player, energyContainer, usage, true) == 0) {
                            //If we can't actually extract energy, exit
                            break;
                        } else {
                            pullItem(player, item);
                            if (energyContainer.getEnergy(getContainer()) < (usage)) {
                                //If after using energy, our energy is now smaller than how much we need to use, exit
                                break;
                            }
                        }

                    }
                }
            }
        }
    }

    private void pullItem(EntityPlayer player, EntityItem item) {
        Vec3d diff = new Vec3d(player.posX - item.posX, player.posY - item.posY, player.posZ - item.posZ);
        Vec3d motionNeeded = new Vec3d(Math.min(diff.x, 1), Math.min(diff.y, 1), Math.min(diff.z, 1));
        Vec3d motionDiff = motionNeeded.subtract(new Vec3d(player.motionX, player.motionY, player.motionZ));
        item.motionX = motionDiff.scale(0.2).x;
        item.motionY = motionDiff.scale(0.2).y;
        item.motionZ = motionDiff.scale(0.2).z;
        //todo
        /*
        Mekanism.packetHandler.sendTo(new PacketLightningRender(LightningPreset.MAGNETIC_ATTRACTION, Objects.hash(player.getUUID(), item),
                player.position().add(0, 0.2, 0), item.position(), (int) (diff.length() * 4)), player);


         */
    }

    @Override
    public boolean canChangeModeWhenDisabled() {
        return true;
    }

    @Override
    public void changeMode( EntityPlayer player, ItemStack stack, int shift, boolean displayChangeMessage) {
        toggleEnabled(player, MekanismLang.MODULE_MAGNETIC_ATTRACTION.translate());
    }

    public enum Range implements IHasTextComponent {
        OFF(0),
        LOW(1F),
        MED(3F),
        HIGH(5),
        ULTRA(10);

        private final float range;
        private final ITextComponent label;

        Range(float boost) {
            this.range = boost;
            this.label = new TextComponentGroup().getString(Float.toString(boost));
        }

        @Override
        public ITextComponent getTextComponent() {
            return label;
        }

        public float getRange() {
            return range;
        }
    }
}