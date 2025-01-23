package mekanism.common.content.gear.mekasuit;

import mekanism.api.gas.GasStack;
import mekanism.common.MekanismFluids;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.Modules;
import mekanism.common.item.armor.ItemMekaSuitBodyArmor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModuleElectrolyticBreathingUnit extends Module {

    private ModuleConfigItem<Boolean> fillHeld;

    @Override
    public void init() {
        fillHeld = addConfigItem(new ModuleConfigItem<>(this, "fill_held", MekanismLang.MODULE_BREATHING_HELD, new ModuleConfigItem.BooleanData(), false));
    }

    @Override
    public void tickServer(EntityPlayer player) {
        super.tickServer(player);
        int productionRate = 0;
        //Check if the mask is underwater
        //Note: Being in water is checked first to ensure that if it is raining and the player is in water
        // they get the full strength production
        if (player.isInsideOfMaterial(Material.WATER) && player.getEyeHeight() >= 0.11) {
            //If the position the bottom of the mask is almost entirely in water set the production rate to our max rate
            // if the mask is only partially in water treat it as not being in it enough to actually function
            productionRate = getMaxRate();
        } else if (isInRain(player)) {
            //If the player is not in water but is in rain set the production to half power
            productionRate = getMaxRate() / 2;
        }
        if (productionRate > 0) {
            double usage = MekanismConfig.current().general.FROM_H2.val() * 2;
            int maxRate = Math.min(productionRate, (int) (getContainerEnergy() / usage));
            int hydrogenUsed = 0;
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (chestStack.getItem() instanceof ItemMekaSuitBodyArmor armour && Modules.load(chestStack, Modules.JETPACK_UNIT) != null) {
                if (armour.getStored(chestStack) < armour.getMaxGas(chestStack)) {
                    hydrogenUsed = maxRate * 2 - armour.getStored(chestStack);
                    GasStack hydrogenStack = new GasStack(MekanismFluids.Hydrogen, hydrogenUsed);
                    armour.addGas(chestStack, hydrogenStack);
                }
            }
            int oxygenUsed = Math.min(maxRate, 300 - player.getAir());
            long used = Math.max((int) Math.ceil(hydrogenUsed / 2D), oxygenUsed);
            useEnergy(player, usage * used);
            player.setAir(player.getAir() + oxygenUsed);
        }
    }

    private boolean isInRain(EntityPlayer player) {
        BlockPos blockpos = new BlockPos(player.posX, player.posY, player.posZ);
        return player.world.isRainingAt(blockpos) || player.world.isRainingAt(new BlockPos(blockpos.getX(), player.getEntityBoundingBox().maxY, blockpos.getZ()));
    }


    private int getMaxRate() {
        return (int) Math.pow(2, getInstalledCount());
    }
}