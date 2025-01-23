package mekanism.common.content.gear.mekasuit;


import mekanism.common.MekanismLang;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.ModuleConfigItem.*;
import mekanism.common.item.armor.ItemMekaSuitArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModuleChargeDistributionUnit extends Module {

    private ModuleConfigItem<Boolean> chargeSuit;
    //   private IModuleConfigItem<Boolean> chargeInventory;

    @Override
    public void init() {
        chargeSuit = addConfigItem(new ModuleConfigItem<>(this, "charge_suit", MekanismLang.MODULE_CHARGE_SUIT, new BooleanData(), true));
        //  chargeInventory = configItemCreator.createConfigItem("charge_inventory", MekanismLang.MODULE_CHARGE_INVENTORY, new ModuleBooleanData(false));
    }

    @Override
    public void tickServer(EntityPlayer player) {
        super.tickServer(player);
        // charge inventory first
        /*
        if (chargeInventory.get()) {
            chargeInventory(module, player);
        }


         */
        // distribute suit charge next
        if (chargeSuit.get()) {
            chargeSuit(player);
        }
    }

    private void chargeSuit(EntityPlayer player) {
        double total = 0;
        for (ItemStack stack : player.getArmorInventoryList()) {
            if (stack.getItem() instanceof ItemMekaSuitArmor armour && armour.getEnergy(stack) < armour.getMaxEnergy(stack)) {
                total += armour.getEnergy(stack);
            }
        }
        for (ItemStack stack : player.getArmorInventoryList()) {
            if (stack.getItem() instanceof ItemMekaSuitArmor armour && armour.getEnergy(stack) < armour.getMaxEnergy(stack)) {
                armour.setEnergy(stack, total);
            }
        }
    }

    //TODO
    /*
    private void chargeInventory(IModule<ModuleChargeDistributionUnit> module, EntityPlayer player) {
        double toCharge = MekanismConfig.current().meka.mekaSuitInventoryChargeRate.val();
        // first try to charge mainhand/offhand item
        toCharge = charge(module, player, player.getHeldItemMainhand(), toCharge);
        toCharge = charge(module, player, player.getHeldItemOffhand(), toCharge);
        if (toCharge != 0) {
            for (ItemStack stack : player.inventory.mainInventory) {
                if (stack != player.getHeldItemMainhand() && stack != player.getHeldItemOffhand()) {
                    toCharge = charge(module, player, stack, toCharge);
                    if (toCharge ==0) {
                        break;
                    }
                }
            }
        }
    }
    */
    /** return rejects */
    /*
    private double charge(IModule<ModuleChargeDistributionUnit> module, EntityPlayer player, ItemStack stack, double amount) {
        if (!stack.isEmpty() && amount != 0) {
            IStrictEnergyHandler handler = EnergyCompatUtils.getStrictEnergyHandler(stack);
            if (handler != null) {
                FloatingLong remaining = handler.insertEnergy(amount, Action.SIMULATE);
                if (remaining.smallerThan(amount)) {
                    //If we can actually insert any energy into
                    return handler.insertEnergy(module.useEnergy(player, amount.subtract(remaining), false), Action.EXECUTE).add(remaining);
                }
            }
        }
        return amount;
    }

     */

}