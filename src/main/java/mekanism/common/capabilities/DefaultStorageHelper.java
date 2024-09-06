package mekanism.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by ben on 03/05/16.
 */
public class DefaultStorageHelper {

    public static class DefaultStorage<T> implements IStorage<T> {

        @Override
        public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
            if (instance instanceof INBTSerializable<?> serializable) {
                return serializable.serializeNBT();
            }
            return new NBTTagCompound();
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
            if (instance instanceof INBTSerializable<?> serializable) {
                Class<? extends NBTBase> nbtClass = serializable.serializeNBT().getClass();
                if (nbtClass.isInstance(nbt)) {
                    ((INBTSerializable) instance).deserializeNBT(nbtClass.cast(nbt));
                }
            }
        }
    }

    public static class NullStorage<T> implements IStorage<T> {

        @Override
        public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
            return new NBTTagCompound();
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
        }
    }
}
