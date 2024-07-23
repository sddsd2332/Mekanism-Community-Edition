package mekanism.generators.common.content.fission;

import mekanism.api.gas.GasStack;
import mekanism.common.multiblock.MultiblockCache;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class FissionCache extends MultiblockCache<SynchronizedFissionData> {

    public FluidStack water;
    public FluidStack steam;
    public GasStack input;
    public GasStack output;
    public double temperature;

    @Override
    public void apply(SynchronizedFissionData data) {
        data.waterStored = water;
        data.steamStored = steam;
        data.InputGas = input;
        data.OutputGas = output;
        data.temperature = temperature;
    }

    @Override
    public void sync(SynchronizedFissionData data) {
        water = data.waterStored;
        steam = data.steamStored;
        input = data.InputGas;
        output = data.OutputGas;
        temperature = data.temperature;
    }

    @Override
    public void load(NBTTagCompound nbtTags) {
        if (nbtTags.hasKey("cachedWater")) {
            water = FluidStack.loadFluidStackFromNBT(nbtTags.getCompoundTag("cachedWater"));
        }
        if (nbtTags.hasKey("cachedSteam")) {
            steam = FluidStack.loadFluidStackFromNBT(nbtTags.getCompoundTag("cachedSteam"));
        }
        if (nbtTags.hasKey("cachedInputGas")){
            input = GasStack.readFromNBT(nbtTags.getCompoundTag("cachedInputGas"));
        }
        if (nbtTags.hasKey("cachedOutputGas")){
            output = GasStack.readFromNBT(nbtTags.getCompoundTag("cachedOutputGas"));
        }
        temperature = nbtTags.getDouble("temperature");
    }

    @Override
    public void save(NBTTagCompound nbtTags) {
        if (water != null) {
            nbtTags.setTag("cachedWater", water.writeToNBT(new NBTTagCompound()));
        }
        if (steam != null) {
            nbtTags.setTag("cachedSteam", steam.writeToNBT(new NBTTagCompound()));
        }
        if (input != null){
            nbtTags.setTag("cachedInputGas",input.write(new NBTTagCompound()));
        }
        if (output != null){
            nbtTags.setTag("cachedOutputGas",output.write(new NBTTagCompound()));
        }
        nbtTags.setDouble("temperature", temperature);
    }
}
