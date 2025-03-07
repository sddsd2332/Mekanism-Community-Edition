package mekanism.client.render.lib;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import mekanism.common.Mekanism;
import mekanism.common.lib.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class ColorAtlas {

    private static final int ATLAS_SIZE = 16;

    private final String name;
    private final List<ColorRegistryObject> colors = new ArrayList<>();

    public ColorAtlas(String name) {
        this.name = name;
    }

    public ColorRegistryObject register() {
        //Default to white as the fallback
        return register(0xFFFFFFFF);
    }

    public ColorRegistryObject register(int defaultARGB) {
        ColorRegistryObject obj = new ColorRegistryObject(defaultARGB);
        colors.add(obj);
        return obj;
    }

    public void parse(ResourceLocation rl) {
        List<Color> parsed = load(rl, colors.size());
        if (parsed.size() < colors.size()) {
            Mekanism.logger.error("Failed to parse '{}' color atlas.", name);
            return;
        }
        for (int i = 0; i < parsed.size(); i++) {
            colors.get(i).setColor(parsed.get(i));
        }
    }

    public static List<Color> load(ResourceLocation rl, int count) {
        List<Color> ret = new ArrayList<>();
        try {
            loadColorAtlas(rl, count, ret);
        } catch (Exception e) {
            Mekanism.logger.error("Failed to load color atlas: {}", rl, e);
        }
        return ret;
    }

    private static void loadColorAtlas(ResourceLocation rl, int count, List<Color> ret) throws IOException {
        IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(rl);
        BufferedImage img = ImageIO.read(resource.getInputStream());
        for (int i = 0; i < count; i++) {
            int rgb = img.getRGB(i % ATLAS_SIZE, i / ATLAS_SIZE);
            if (rgb >> 24 == 0) {
                //Don't allow fully transparent colors, fallback to default color.
                // Mark as null for now so that it can default to the proper color
                ret.add(null);
                Mekanism.logger.warn("Unable to retrieve color marker: '{}' for atlas: '{}'. This is likely due to an out of date resource pack.", count, rl);
            } else {
                ret.add(Color.argb(rgb));
            }
        }
    }

    public static class ColorRegistryObject implements Supplier<Color> {

        private final int defaultARGB;
        private Color color;
        private int argb;

        private ColorRegistryObject(int defaultARGB) {
            this.defaultARGB = defaultARGB;
            //Initialize the color to the baseline color
            setColor(null);
        }

        private void setColor(@Nullable Color color) {
            if (color == null) {
                //If there is no color set it to the default
                color = Color.argb(this.defaultARGB);
            }
            this.color = color;
            this.argb = color.argb();
        }

        @Override
        public Color get() {
            return color;
        }

        public int argb() {
            return argb;
        }
    }
}
