package mekanism.common.util;

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.function.Supplier;

public class EjectSpeedController {

    private final List<TankProvider> tanks = new ObjectArrayList<>(2);
    private final List<TankRecord> tankRecords = new ObjectArrayList<>(2);

    public void record(int tankIdx) {
        TankProvider provider = tanks.get(tankIdx);
        TankRecord record = tankRecords.get(tankIdx);
        record.record(provider);
    }

    public void eject(int tankIdx, int amount) {
        TankRecord record = tankRecords.get(tankIdx);
        record.eject(amount);
    }

    public int getEjectSpeed(int tankIdx) {
        return tankRecords.get(tankIdx).getEjectSpeed(tanks.get(tankIdx));
    }

    public boolean canEject(int tankIdx) {
        return tankRecords.get(tankIdx).canEject(tanks.get(tankIdx));
    }

    public void ensureSize(int size, Supplier<List<TankProvider>> tanksProvider) {
        if (size != tanks.size()) {
            tanks.clear();
            tanks.addAll(tanksProvider.get());
            tankRecords.clear();
            for (int i = 0; i < tanks.size(); i++) {
                tankRecords.add(new TankRecord());
            }
        }
    }

    public static final class TankRecord {

        public static final int MAX_WORK_DELAY = 40;
        public static final int MAX_TANK_RECORDS = 20;

        public static final float LOW_THRESHOLD = 0.5F;
        public static final float HIGH_THRESHOLD = 0.85F;

        private final IntArrayFIFOQueue tankRecords = new IntArrayFIFOQueue();

        private int lastTankAmount = 0;
        private int tankChangeSpeedTotal = 0;
        private int tankChangeSpeedAvg = 0;

        private int lastEjectAmount = 0;
        private int ejectSpeedMax = 0;

        private int ejectDelay = -1;

        private TankRecord() {
        }

        public void record(final TankProvider tank) {
            if (tankRecords.size() >= MAX_TANK_RECORDS) {
                int last = tankRecords.dequeueInt();
                tankChangeSpeedTotal -= last;
            }

            int tankAmount = tank.getTankAmount();
            int changed = (tankAmount + lastEjectAmount) - lastTankAmount;
            tankRecords.enqueue(changed);
            tankChangeSpeedTotal += changed;
            tankChangeSpeedAvg = tankChangeSpeedTotal / tankRecords.size();
            lastTankAmount = tankAmount;
            lastEjectAmount = 0;

            if ((float) tank.getTankAmount() / tank.getTankCapacity() >= HIGH_THRESHOLD) {
                // faster?
                ejectDelay = 0;
                ejectSpeedMax = 0;
            }
        }

        public void eject(final int amount) {
            ejectSpeedMax = Math.max(ejectSpeedMax, amount);
            lastEjectAmount = amount;
        }

        public int getEjectSpeed(final TankProvider tank) {
            if (tankChangeSpeedAvg <= 0) {
                return MAX_WORK_DELAY;
            }
            return (int) MathHelper.clamp(
                    (float) Math.min(ejectSpeedMax, tank.getTankCapacity() - tank.getTankAmount()) / tankChangeSpeedAvg,
                    1, MAX_WORK_DELAY
            );
        }

        public boolean canEject(final TankProvider tank) {
            if (ejectDelay == -1) {
                ejectDelay = getEjectSpeed(tank);
                return false;
            }

            --ejectDelay;

            if (ejectDelay <= 0) {
                if ((float) tank.getTankAmount() / tank.getTankCapacity() <= LOW_THRESHOLD) {
                    // slower?
                    ejectSpeedMax = (int) (ejectSpeedMax * 1.2);
                }
                ejectDelay = getEjectSpeed(tank);
                return true;
            }

            return false;
        }

    }

}
