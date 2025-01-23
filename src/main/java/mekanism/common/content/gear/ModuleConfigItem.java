package mekanism.common.content.gear;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.gear.config.IModuleConfigItem;
import mekanism.api.gear.config.ModuleBooleanData;
import mekanism.api.gear.config.ModuleConfigData;
import mekanism.api.text.ILangEntry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;

public class ModuleConfigItem<TYPE> implements IModuleConfigItem<TYPE> {

    private final Module<?> module;
    private final String name;
    private final ILangEntry description;
    private final ModuleConfigData<TYPE> data;

    public ModuleConfigItem(Module<?> module, String name, ILangEntry description, ModuleConfigData<TYPE> data) {
        this.module = module;
        this.name = name;
        this.description = description;
        this.data = data;
    }

    public ITextComponent getDescription() {
        return description.translate();
    }

    public ModuleConfigData<TYPE> getData() {
        return data;
    }

    @Nonnull
    @Override
    public TYPE get() {
        return data.get();
    }

    @Override
    public void set(@Nonnull TYPE val) {
        set(val, null);
    }

    public void set(@Nonnull TYPE val, @Nullable Runnable callback) {
        Objects.requireNonNull(val, "Value cannot be null.");
        data.set(val);
        // perform any validity checks such as disabling conflicting modules
        checkValidity(val, callback);
        // finally, save this specific module with the callback (to send a packet)
        module.save(callback);
    }

    protected void checkValidity(@NotNull TYPE val, @org.jetbrains.annotations.Nullable Runnable callback) {
    }

    public void read(NBTTagCompound tag) {
        if (tag.hasKey(name)) {
            data.read(name, tag);
        }
    }

    public void write(NBTTagCompound tag) {
        data.write(name, tag);
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    public static class DisableableModuleConfigItem extends ModuleConfigItem<Boolean> {

        private final BooleanSupplier isConfigEnabled;

        public DisableableModuleConfigItem(Module<?> module, String name, ILangEntry description, boolean def, BooleanSupplier isConfigEnabled) {
            super(module, name, description, new ModuleBooleanData(def));
            this.isConfigEnabled = isConfigEnabled;
        }

        @Nonnull
        @Override
        public Boolean get() {
            return isConfigEnabled() && super.get();
        }

        public boolean isConfigEnabled() {
            return isConfigEnabled.getAsBoolean();
        }
    }
}