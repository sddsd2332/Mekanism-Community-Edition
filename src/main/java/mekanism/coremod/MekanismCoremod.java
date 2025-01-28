package mekanism.coremod;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;

@SortingIndex(9999)//must be > 1000 so we're after the srg transformer
public class MekanismCoremod implements IFMLLoadingPlugin {

    static final Logger mainLogger = LogManager.getLogger("Mekanism Core ASM");
    public static boolean runtimeDeobfEnabled = false;

    private static final String[] transformers = {
            "mekanism.coremod.KeybindingMigrationHelper",
            "mekanism.coremod.MekanismCoreTransformer"

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
        runtimeDeobfEnabled = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
