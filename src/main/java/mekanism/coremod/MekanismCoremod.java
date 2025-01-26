package mekanism.coremod;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SortingIndex(9999)//must be > 1000 so we're after the srg transformer
public class MekanismCoremod implements IFMLLoadingPlugin, IEarlyMixinLoader {


    private static final String[] transformers = {
            "mekanism.coremod.KeybindingMigrationHelper"
    };

    @Override
    public String[] getASMTransformerClass() {
        return transformers;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public List<String> getMixinConfigs() {
        return Arrays.asList(
                "mixins.mekanism.json"
        );
    }
}
