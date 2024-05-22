package mekanism.common.tile.machine;

import io.netty.buffer.ByteBuf;
import mekanism.api.TileNetworkList;
import mekanism.api.infuse.InfuseObject;
import mekanism.api.infuse.InfuseRegistry;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.InfuseStorage;
import mekanism.common.MekanismItems;
import mekanism.common.PacketHandler;
import mekanism.common.SideData;
import mekanism.common.base.ISustainedData;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.InfusionInput;
import mekanism.common.recipe.machines.MetallurgicInfuserRecipe;
import mekanism.common.recipe.outputs.ItemStackOutput;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.prefab.TileEntityUpgradeableMachine;
import mekanism.common.util.ChargeUtils;
import mekanism.common.util.InventoryUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.Map;

public class TileEntityMetallurgicInfuser extends TileEntityUpgradeableMachine<InfusionInput, ItemStackOutput, MetallurgicInfuserRecipe> implements ISustainedData {

    private static final String[] methods = new String[]{"getEnergy", "getProgress", "facing", "canOperate", "getMaxEnergy", "getEnergyNeeded", "getInfuse",
            "getInfuseNeeded"};
    /**
     * The maxiumum amount of infuse this machine can store.
     */
    public int MAX_INFUSE = 1000;
    /**
     * The amount of infuse this machine has stored.
     */
    public InfuseStorage infuseStored = new InfuseStorage();

    public TileEntityMetallurgicInfuser() {
        super("metalinfuser", MachineType.METALLURGIC_INFUSER, 0, 200);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.ENERGY);
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.NONE, InventoryUtils.EMPTY));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.INPUT, new int[]{2}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.OUTPUT, new int[]{3}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.ENERGY, new int[]{4}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.EXTRA, new int[]{1}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(DataType.INPUT_EXTRA, new int[]{1, 2}));
        configComponent.addOutput(TransmissionType.ITEM, new SideData(new int[]{2, 3}, new boolean[]{false, true}));
        configComponent.setConfig(TransmissionType.ITEM, new byte[]{4, 1, 1, 3, 1, 2});

        configComponent.setInputConfig(TransmissionType.ENERGY);
        inventory = NonNullList.withSize(5, ItemStack.EMPTY);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(TransmissionType.ITEM, configComponent.getOutputs(TransmissionType.ITEM).get(2));
        ejectorComponent.setInputOutputData(TransmissionType.ITEM,configComponent.getOutputs(TransmissionType.ITEM).get(6));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            ChargeUtils.discharge(4, this);
            ItemStack infuseInput = inventory.get(1);
            if (!infuseInput.isEmpty()) {
                InfuseObject pendingInfuseInput = InfuseRegistry.getObject(infuseInput);
                if (pendingInfuseInput != null) {
                    if (infuseStored.getType() == null || infuseStored.getType() == pendingInfuseInput.type) {
                        if (infuseStored.getAmount() + pendingInfuseInput.stored <= MAX_INFUSE) {
                            infuseStored.increase(pendingInfuseInput);
                            infuseInput.shrink(1);
                        }
                    }
                }
            }

            MetallurgicInfuserRecipe recipe = RecipeHandler.getMetallurgicInfuserRecipe(getInput());
            if (canOperate(recipe) && MekanismUtils.canFunction(this) && getEnergy() >= energyPerTick) {
                setActive(true);
                setEnergy(getEnergy() - energyPerTick);
                if ((operatingTicks + 1) < ticksRequired) {
                    operatingTicks++;
                } else {
                    operate(recipe);
                    operatingTicks = 0;
                }
            } else if (prevEnergy >= getEnergy()) {
                setActive(false);
            }
            if (!canOperate(recipe)) {
                operatingTicks = 0;
            }
            prevEnergy = getEnergy();
        }
    }

    @Override
    protected void upgradeInventory(TileEntityFactory factory) {
        factory.inventory.set(5, inventory.get(2));
        factory.inventory.set(1, inventory.get(4));
        factory.inventory.set(5 + 3, inventory.get(3));
        factory.inventory.set(0, inventory.get(0));
        factory.inventory.set(4, inventory.get(1));
    }

    @Override
    public boolean canExtractItem(int slotID, @Nonnull ItemStack itemstack, @Nonnull EnumFacing side) {
        if (slotID == 4) {
            return ChargeUtils.canBeOutputted(itemstack, false);
        }
        return slotID == 3;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, @Nonnull ItemStack itemstack) {
        if (slotID == 3) {
            return false;
        } else if (slotID == 1) {
            InfuseObject infuseObject = InfuseRegistry.getObject(itemstack);
            return infuseObject != null && (infuseStored.getType() == null || infuseStored.getType() == infuseObject.type);
        } else if (slotID == 0) {
            return itemstack.getItem() == MekanismItems.SpeedUpgrade || itemstack.getItem() == MekanismItems.EnergyUpgrade;
        } else if (slotID == 2) {
            if (infuseStored.getType() != null) {
                return RecipeHandler.getMetallurgicInfuserRecipe(new InfusionInput(infuseStored, itemstack)) != null;
            }
            for (InfusionInput input : Recipe.METALLURGIC_INFUSER.get().keySet()) {
                if (ItemHandlerHelper.canItemStacksStack(input.inputStack, itemstack)) {
                    return true;
                }
            }
        } else if (slotID == 4) {
            return ChargeUtils.canBeDischarged(itemstack);
        }
        return false;
    }

    public InfusionInput getInput() {
        return new InfusionInput(infuseStored, inventory.get(2));
    }

    public void operate(MetallurgicInfuserRecipe recipe) {
        recipe.output(inventory, 2, 3, infuseStored);
        markDirty();
    }

    @Override
    public Map<InfusionInput, MetallurgicInfuserRecipe> getRecipes() {
        return Recipe.METALLURGIC_INFUSER.get();
    }

    @Override
    public MetallurgicInfuserRecipe getRecipe() {
        InfusionInput input = getInput();
        if (cachedRecipe == null || !input.testEquality(cachedRecipe.getInput())) {
            cachedRecipe = RecipeHandler.getMetallurgicInfuserRecipe(input);
        }
        return cachedRecipe;
    }

    @Override
    public boolean canOperate(MetallurgicInfuserRecipe recipe) {
        return recipe != null && recipe.canOperate(inventory, 2, 3, infuseStored);
    }

    public int getScaledInfuseLevel(int i) {
        return infuseStored.getAmount() * i / MAX_INFUSE;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        int amount = nbtTags.getInteger("infuseStored");
        if (amount != 0) {
            infuseStored.setAmount(amount);
            infuseStored.setType(InfuseRegistry.get(nbtTags.getString("type")));
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        ;
        if (infuseStored.getType() != null) {
            nbtTags.setString("type", infuseStored.getType().name);
            nbtTags.setInteger("infuseStored", infuseStored.getAmount());
        } else {
            nbtTags.setString("type", "null");
        }
        nbtTags.setBoolean("sideDataStored", true);
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            int amount = dataStream.readInt();
            if (amount == 0) {
                infuseStored.setEmpty();
            } else {
                infuseStored.setAmount(amount);
            }
            return;
        }

        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            int amount = dataStream.readInt();
            if (amount > 0) {
                infuseStored.setAmount(amount);
                infuseStored.setType(InfuseRegistry.get(PacketHandler.readString(dataStream)));
            } else {
                infuseStored.setEmpty();
            }
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(infuseStored.getAmount());
        if (infuseStored.getAmount() > 0) {
            data.add(infuseStored.getType().name);
        }
        return data;
    }

    @Override
    public String[] getMethods() {
        return methods;
    }

    @Override
    public Object[] invoke(int method, Object[] arguments) throws NoSuchMethodException {
        return switch (method) {
            case 0 -> new Object[]{getEnergy()};
            case 1 -> new Object[]{operatingTicks};
            case 2 -> new Object[]{facing};
            case 3 -> new Object[]{canOperate(RecipeHandler.getMetallurgicInfuserRecipe(getInput()))};
            case 4 -> new Object[]{getMaxEnergy()};
            case 5 -> new Object[]{getMaxEnergy() - getEnergy()};
            case 6 -> new Object[]{infuseStored};
            case 7 -> new Object[]{MAX_INFUSE - infuseStored.getAmount()};
            default -> throw new NoSuchMethodException();
        };
    }

    @Override
    public void writeSustainedData(ItemStack itemStack) {
        infuseStored.writeSustainedData(itemStack);
    }

    @Override
    public void readSustainedData(ItemStack itemStack) {
        infuseStored.readSustainedData(itemStack);
    }
}
