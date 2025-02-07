package mekanism.common.content.gear.mekasuit;

import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.api.gear.config.IModuleConfigItem;
import mekanism.api.gear.config.ModuleBooleanData;
import mekanism.api.gear.config.ModuleConfigItemCreator;
import mekanism.common.MekanismLang;
import mekanism.common.item.armor.ItemMekaSuitBodyArmor;
import mekanism.common.item.armor.ItemMekaSuitBoots;
import mekanism.common.item.armor.ItemMekaSuitHelmet;
import mekanism.common.item.armor.ItemMekaSuitPants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

@ParametersAreNotNullByDefault
public class ModuleChargeDistributionUnit implements ICustomModule<ModuleChargeDistributionUnit> {

    private IModuleConfigItem<Boolean> chargeSuit;
    // private IModuleConfigItem<Boolean> chargeInventory;

    @Override
    public void init(IModule<ModuleChargeDistributionUnit> module, ModuleConfigItemCreator configItemCreator) {
        chargeSuit = configItemCreator.createConfigItem("charge_suit", MekanismLang.MODULE_CHARGE_SUIT, new ModuleBooleanData());
        //      chargeInventory = configItemCreator.createConfigItem("charge_inventory", MekanismLang.MODULE_CHARGE_INVENTORY, new ModuleBooleanData(false));
    }

    @Override
    public void tickServer(IModule<ModuleChargeDistributionUnit> module, EntityPlayer player) {
        /*
        // charge inventory first
        if (chargeInventory.get()) {
            chargeInventory(module, player);
        }
        */

        // distribute suit charge next
        if (chargeSuit.get()) {
            chargeSuit(player);
        }
    }

    //TODO
    private void chargeSuit(EntityPlayer player) {
        double energy;
        double energyMax;
        double headEnergy = 0;
        double headEnergyMax = 0;
        double BodyEnergy = 0;
        double BodyEnergyMax = 0;
        double LegsEnergy = 0;
        double LegsEnergyMax = 0;
        double FeetEnergy = 0;
        double FeetEnergyMax = 0;
        for (ItemStack stack : player.getArmorInventoryList()) {
            if (stack.getItem() instanceof ItemMekaSuitHelmet armour && armour.getNeeded(stack) > 0) {
                headEnergy = armour.getEnergy(stack);
                headEnergyMax = armour.getMaxEnergy(stack);
            }
            if (stack.getItem() instanceof ItemMekaSuitBodyArmor armour) {
                BodyEnergy = armour.getEnergy(stack);
                BodyEnergyMax = armour.getMaxEnergy(stack);
            }
            if (stack.getItem() instanceof ItemMekaSuitPants armour && armour.getNeeded(stack) > 0) {
                LegsEnergy = armour.getEnergy(stack);
                LegsEnergyMax = armour.getMaxEnergy(stack);
            }
            if (stack.getItem() instanceof ItemMekaSuitBoots armour && armour.getNeeded(stack) > 0) {
                FeetEnergy = armour.getEnergy(stack);
                FeetEnergyMax = armour.getMaxEnergy(stack);
            }

        }
        energy = headEnergy + BodyEnergy + LegsEnergy + FeetEnergy;
        energyMax = headEnergyMax + BodyEnergyMax + LegsEnergyMax + FeetEnergyMax;
        double FinalEnergy = energy / energyMax;
        for (ItemStack stack : player.getArmorInventoryList()) {
            if (stack.getItem() instanceof ItemMekaSuitHelmet armour && armour.getNeeded(stack) > 0) {
                armour.setEnergy(stack, FinalEnergy * headEnergyMax);
            }
            if (stack.getItem() instanceof ItemMekaSuitBodyArmor armour) {
                armour.setEnergy(stack, FinalEnergy * BodyEnergyMax);
            }
            if (stack.getItem() instanceof ItemMekaSuitPants armour && armour.getNeeded(stack) > 0) {
                armour.setEnergy(stack, FinalEnergy * LegsEnergyMax);
            }
            if (stack.getItem() instanceof ItemMekaSuitBoots armour && armour.getNeeded(stack) > 0) {
                armour.setEnergy(stack, FinalEnergy * FeetEnergyMax);
            }
        }
    }

    /*
    private void chargeInventory(IModule<ModuleChargeDistributionUnit> module, EntityPlayer player) {
        charge(module, player.getHeldItemMainhand());
        charge(module, player.getHeldItemOffhand());
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack != player.getHeldItemMainhand() && stack != player.getHeldItemOffhand()) {
                charge(module, stack);
            }
        }
        if (Mekanism.hooks.Baubles) {
            chargeBaubles(player, module);
        }
    }

    @Optional.Method(modid = MekanismHooks.Baubles_MOD_ID)
    public void chargeBaubles(EntityPlayer player, IModule<ModuleChargeDistributionUnit> module) {
        IItemHandler baubles = BaublesApi.getBaublesHandler(player);
        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            charge(module, stack);
        }
    }

    private void charge(IModule<ModuleChargeDistributionUnit> module, ItemStack stack) {
        if (!stack.isEmpty() && module.getEnergyContainer() != null && module.getEnergyContainer().getEnergy(stack) > 0) {
            if (stack.getItem() instanceof IEnergizedItem) {
                module.getEnergyContainer().setEnergy(stack, module.getEnergyContainer().getEnergy(stack) - EnergizedItemManager.charge(stack, module.getEnergyContainer().getEnergy(stack)));
            } else if (MekanismUtils.useTesla() && stack.hasCapability(Capabilities.TESLA_CONSUMER_CAPABILITY, null)) {
                ITeslaConsumer consumer = stack.getCapability(Capabilities.TESLA_CONSUMER_CAPABILITY, null);
                long stored = TeslaIntegration.toTesla(module.getEnergyContainer().getEnergy(stack));
                module.getEnergyContainer().setEnergy(stack, module.getEnergyContainer().getEnergy(stack) - TeslaIntegration.fromTesla(consumer.givePower(stored, false)));
            } else if (MekanismUtils.useForge() && stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
                IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
                if (storage != null && storage.canReceive()) {
                    int stored = ForgeEnergyIntegration.toForge(module.getEnergyContainer().getEnergy(stack));
                    module.getEnergyContainer().setEnergy(stack, module.getEnergyContainer().getEnergy(stack) - ForgeEnergyIntegration.fromForge(storage.receiveEnergy(stored, false)));
                }
            } else if (MekanismUtils.useRF() && stack.getItem() instanceof IEnergyContainerItem item) {
                int toTransfer = RFIntegration.toRF(module.getEnergyContainer().getEnergy(stack));
                module.getEnergyContainer().setEnergy(stack, module.getEnergyContainer().getEnergy(stack) - RFIntegration.fromRF(item.receiveEnergy(stack, toTransfer, false)));
            } else if (MekanismUtils.useIC2() && isIC2Chargeable(stack)) {
                double sent = IC2Integration.fromEU(ElectricItem.manager.charge(stack, IC2Integration.toEU(module.getEnergyContainer().getEnergy(stack)), 4, true, false));
                module.getEnergyContainer().setEnergy(stack, module.getEnergyContainer().getEnergy(stack) - sent);
            }
        }
    }

     */

}
