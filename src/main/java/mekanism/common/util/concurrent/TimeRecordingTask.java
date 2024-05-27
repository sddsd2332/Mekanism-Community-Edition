package mekanism.common.util.concurrent;

import io.netty.util.internal.ThrowableUtil;
import mekanism.common.Mekanism;

import java.util.concurrent.RecursiveTask;

public abstract class TimeRecordingTask<V> extends RecursiveTask<V> {
    public volatile int usedTime = 0;

    @Override
    protected final V compute() {
        long start = System.nanoTime() / 1000;

        V result = null;
        try {
            result = computeTask();
        } catch (Throwable e) {
            Mekanism.logger.warn("An error occurred during fork join task execution!");
            Mekanism.logger.warn(ThrowableUtil.stackTraceToString(e));
        }

        usedTime = (int) (System.nanoTime() / 1000 - start);
        return result;
    }

    protected abstract V computeTask();
}
