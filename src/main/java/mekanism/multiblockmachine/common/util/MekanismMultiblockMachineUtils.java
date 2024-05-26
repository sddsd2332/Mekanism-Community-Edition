package mekanism.multiblockmachine.common.util;


import mekanism.multiblockmachine.common.MekanismMultiblockMachine;
import net.minecraft.util.ResourceLocation;

public class MekanismMultiblockMachineUtils {
    public static ResourceLocation getResource(ResourceType type, String name) {
        return new ResourceLocation(MekanismMultiblockMachine.MODID, type.getPrefix() + name);
    }


    public enum ResourceType {

        GUI("gui"),
        SOUND("sound"),
        RENDER("render"),
        RENDER_MACHINE("render/machine"),
        TEXTURE_BLOCKS("textures/blocks"),
        TEXTURE_ITEMS("textures/items"),
        MODEL("models");

        private String prefix;

        ResourceType(String s) {
            prefix = s;
        }

        public String getPrefix() {
            return prefix + "/";
        }
    }
}
