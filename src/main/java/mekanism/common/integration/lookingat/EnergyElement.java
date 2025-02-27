package mekanism.common.integration.lookingat;

import mekanism.api.text.TextComponentGroup;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.ITextComponent;

public class EnergyElement extends LookingAtElement {

    protected final double energy;
    protected final double maxEnergy;

    public EnergyElement(double energy, double maxEnergy) {
        super(0xFF000000, 0xFFFFFF);
        this.energy = energy;
        this.maxEnergy = maxEnergy;
    }

    @Override
    public int getScaledLevel(int level) {
        if (energy == Double.MAX_VALUE) {
            return level;
        }
        return (int) (level * (energy / maxEnergy));
    }

    @Override
    public TextureAtlasSprite getIcon() {
        return MekanismRenderer.energyIcon;
    }

    @Override
    public ITextComponent getText() {
        return new TextComponentGroup().translation(MekanismUtils.getEnergyDisplay(energy, maxEnergy));
    }
}
