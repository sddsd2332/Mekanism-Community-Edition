package mekanism.common.util;

import mekanism.api.energy.IEnergizedItem;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import mekanism.common.Upgrade;
import mekanism.common.base.IFactory;
import mekanism.common.base.IFactory.RecipeType;
import mekanism.common.base.ITierItem;
import mekanism.common.block.states.BlockStateBasic.BasicBlockType;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.inventory.InventoryBin;
import mekanism.common.security.ISecurityItem;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

public class RecipeUtils {

    public static boolean areItemsEqualForCrafting(ItemStack target, ItemStack input) {
        if (target.isEmpty() && !input.isEmpty() || !target.isEmpty() && input.isEmpty()) {
            return false;
        } else if (target.isEmpty()) {
            return true;
        }
        if (target.getItem() != input.getItem()) {
            return false;
        }
        if (target.getItemDamage() != input.getItemDamage() && target.getItemDamage() != OreDictionary.WILDCARD_VALUE) {
            return false;
        }
        if (target.getItem() instanceof ITierItem targetItem && input.getItem() instanceof ITierItem inputItem) {
            if (targetItem.getBaseTier(target) != inputItem.getBaseTier(input)) {
                return false;
            }
        }

        if (target.getItem() instanceof IFactory targetFactory && input.getItem() instanceof IFactory inputFactory) {
            if (isFactory(target) && isFactory(input)) {
                RecipeType recipeTypeInput = inputFactory.getRecipeTypeOrNull(input);
                //If either factory has invalid NBT don't crash it
                return recipeTypeInput != null && targetFactory.getRecipeTypeOrNull(target) == recipeTypeInput;
            }
        }
        return true;
    }

    private static boolean isFactory(ItemStack stack) {
        return MachineType.get(stack) == MachineType.BASIC_FACTORY || MachineType.get(stack) == MachineType.ADVANCED_FACTORY || MachineType.get(stack) == MachineType.ELITE_FACTORY || MachineType.get(stack) == MachineType.ULTIMATE_FACTORY || MachineType.get(stack) == MachineType.CREATIVE_FACTORY;
    }

    public static ItemStack getCraftingResult(InventoryCrafting inv, ItemStack toReturn) {
        int invLength = inv.getSizeInventory();
        if (toReturn.getItem() instanceof IEnergizedItem toReturnItem) {
            double energyFound = 0;
            for (int i = 0; i < invLength; i++) {
                ItemStack itemstack = inv.getStackInSlot(i);
                if (!itemstack.isEmpty() && itemstack.getItem() instanceof IEnergizedItem item) {
                    energyFound += item.getEnergy(itemstack);
                }
            }
            double energyToSet = Math.min(toReturnItem.getMaxEnergy(toReturn), energyFound);
            if (energyToSet > 0) {
                toReturnItem.setEnergy(toReturn, energyToSet);
            }
        }

        if (toReturn.getItem() instanceof IGasItem toReturnItem) {
            GasStack gasFound = null;
            for (int i = 0; i < invLength; i++) {
                ItemStack itemstack = inv.getStackInSlot(i);
                if (!itemstack.isEmpty() && itemstack.getItem() instanceof IGasItem item) {
                    GasStack stored = item.getGas(itemstack);
                    if (stored != null) {
                        if (!toReturnItem.canReceiveGas(toReturn, stored.getGas())) {
                            return ItemStack.EMPTY;
                        }
                        if (gasFound == null) {
                            gasFound = stored;
                        } else {
                            if (gasFound.getGas() != stored.getGas()) {
                                return ItemStack.EMPTY;
                            }
                            gasFound.amount += stored.amount;
                        }
                    }
                }
            }

            if (gasFound != null) {
                gasFound.amount = Math.min(toReturnItem.getMaxGas(toReturn), gasFound.amount);
                toReturnItem.setGas(toReturn, gasFound);
            }
        }

        if (toReturn.getItem() instanceof ISecurityItem toReturnItem) {
            for (int i = 0; i < invLength; i++) {
                ItemStack itemstack = inv.getStackInSlot(i);
                if (!itemstack.isEmpty() && itemstack.getItem() instanceof ISecurityItem item) {
                    toReturnItem.setOwnerUUID(toReturn, item.getOwnerUUID(itemstack));
                    toReturnItem.setSecurity(toReturn, item.getSecurity(itemstack));
                    break;
                }
            }
        }

        if (FluidContainerUtils.isFluidContainer(toReturn)) {
            FluidStack fluidFound = null;
            for (int i = 0; i < invLength; i++) {
                ItemStack itemstack = inv.getStackInSlot(i);
                if (FluidContainerUtils.isFluidContainer(itemstack)) {
                    FluidStack stored = FluidUtil.getFluidContained(itemstack);
                    if (stored != null) {
                        if (FluidUtil.getFluidHandler(itemstack).fill(stored, false) == 0) {
                            return ItemStack.EMPTY;
                        }
                        if (fluidFound == null) {
                            fluidFound = stored;
                        } else {
                            if (fluidFound.getFluid() != stored.getFluid()) {
                                return ItemStack.EMPTY;
                            }
                            fluidFound.amount += stored.amount;
                        }
                    }
                }
            }

            if (fluidFound != null) {
                FluidUtil.getFluidHandler(toReturn).fill(fluidFound, true);
            }
        }

        if (BasicBlockType.get(toReturn) == BasicBlockType.BIN) {
            int foundCount = 0;
            ItemStack foundType = ItemStack.EMPTY;
            for (int i = 0; i < invLength; i++) {
                ItemStack itemstack = inv.getStackInSlot(i);
                if (!itemstack.isEmpty() && BasicBlockType.get(itemstack) == BasicBlockType.BIN) {
                    InventoryBin binInv = new InventoryBin(itemstack);
                    foundCount = binInv.getItemCount();
                    foundType = binInv.getItemType();
                }
            }

            if (foundCount > 0 && !foundType.isEmpty()) {
                InventoryBin binInv = new InventoryBin(toReturn);
                binInv.setItemCount(foundCount);
                binInv.setItemType(foundType);
            }
        }

        if (MachineType.get(toReturn) != null && MachineType.get(toReturn).supportsUpgrades) {
            Map<Upgrade, Integer> upgrades = new EnumMap<>(Upgrade.class);
            for (int i = 0; i < invLength; i++) {
                ItemStack itemstack = inv.getStackInSlot(i);
                if (!itemstack.isEmpty() && MachineType.get(itemstack) != null && MachineType.get(itemstack).supportsUpgrades) {
                    Map<Upgrade, Integer> stackMap = Upgrade.buildMap(ItemDataUtils.getDataMapIfPresent(itemstack));
                    for (Entry<Upgrade, Integer> entry : stackMap.entrySet()) {
                        if (entry != null && entry.getKey() != null && entry.getValue() != null) {
                            upgrades.compute(entry.getKey(), (k, val) -> Math.min(entry.getKey().getMax(), (val != null ? val : 0) + entry.getValue()));
                        }
                    }
                }
            }
            if (ItemDataUtils.hasData(toReturn, "upgrades")){
                Upgrade.saveMap(upgrades, ItemDataUtils.getDataMap(toReturn));
            }
        }

        return toReturn;
    }

    public static IRecipe getRecipeFromGrid(InventoryCrafting inv, World world) {
        return CraftingManager.findMatchingRecipe(inv, world);
    }
}
