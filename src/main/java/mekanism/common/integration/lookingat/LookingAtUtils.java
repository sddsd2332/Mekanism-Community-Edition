package mekanism.common.integration.lookingat;

import mekanism.api.energy.IStrictEnergyStorage;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.util.CapabilityUtils;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

public class LookingAtUtils {

    private LookingAtUtils() {
    }


    private static void displayEnergy(LookingAtHelper info, IStrictEnergyStorage energyHandler) {
        info.addEnergyElement(energyHandler.getEnergy(), energyHandler.getMaxEnergy());
    }

    public static void addInfo(LookingAtHelper info, @Nonnull TileEntity tile) {
        IStrictEnergyStorage energyCapability  = CapabilityUtils.getCapability(tile, Capabilities.ENERGY_STORAGE_CAPABILITY,null);
        if (energyCapability != null){
            displayEnergy(info, energyCapability);
        }
    }


}
