package mekanism.common.capabilities;

import mekanism.api.IAlloyInteraction;
import mekanism.common.tier.AlloyTier;
import mekanism.common.capabilities.DefaultStorageHelper.NullStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DefaultAlloyInteraction implements IAlloyInteraction {

    public static void register() {
        CapabilityManager.INSTANCE.register(IAlloyInteraction.class, new NullStorage<>(), DefaultAlloyInteraction::new);
    }

    @Override
    public void onAlloyInteraction(EntityPlayer player, ItemStack stack, AlloyTier tierOrdinal) {
    }
}
