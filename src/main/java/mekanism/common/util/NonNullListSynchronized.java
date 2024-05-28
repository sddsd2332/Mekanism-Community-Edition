package mekanism.common.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.NonNullList;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NonNullListSynchronized<E> extends NonNullList<E> {

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    @NotNull
    public static <E> NonNullListSynchronized<E> create() {
        return new NonNullListSynchronized<>();
    }

    @NotNull
    public static <E> NonNullListSynchronized<E> withSize(int size, @NotNull E fill) {
        Validate.notNull(fill);
        E[] filled = (E[]) new Object[size];
        Arrays.fill(filled, fill);
        return new NonNullListSynchronized<>(new ObjectArrayList<>(filled), fill);
    }

    protected NonNullListSynchronized() {
        this(new ObjectArrayList<>(), null);
    }

    protected NonNullListSynchronized(final List<E> delegateIn, @Nullable final E listType) {
        super(delegateIn, listType);
    }

    @Override
    public int size() {
        try {
            rwLock.readLock().lock();
            return super.size();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        try {
            rwLock.writeLock().lock();
            super.clear();
        }  finally {
            rwLock.writeLock().unlock();
        }
    }

    @NotNull
    @Override
    public E get(final int idx) {
        try {
            rwLock.readLock().lock();
            return super.get(idx);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @NotNull
    @Override
    public E set(final int idx, final @NotNull E element) {
        try {
            rwLock.writeLock().lock();
            return super.set(idx, element);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void add(final int idx, final @NotNull E element) {
        try {
            rwLock.writeLock().lock();
            super.add(idx, element);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public boolean add(final E e) {
        try {
            rwLock.writeLock().lock();
            return super.add(e);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public E remove(final int idx) {
        try {
            rwLock.writeLock().lock();
            return super.remove(idx);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

}
