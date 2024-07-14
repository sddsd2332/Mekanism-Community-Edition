package mekanism.weapons.client.renderer.entity;

import mekanism.weapons.common.MekanismWeapons;
import mekanism.weapons.common.entity.EntityMekaArrow;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

@SideOnly(Side.CLIENT)
public class RenderMekaArrow extends RenderArrow<EntityMekaArrow> {

    public static final ResourceLocation arrow = new ResourceLocation(MekanismWeapons.MODID, "textures/entities/meka_arrow.png");

    public RenderMekaArrow(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityMekaArrow entity) {
        return arrow;
    }
}
