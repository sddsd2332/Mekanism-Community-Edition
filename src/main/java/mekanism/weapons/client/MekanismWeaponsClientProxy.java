package mekanism.weapons.client;

import mekanism.client.render.MekanismRenderer;
import mekanism.weapons.common.MekanismWeapons;
import mekanism.weapons.common.MekanismWeaponsCommonProxy;
import mekanism.weapons.common.MekanismWeaponsItems;
import mekanism.weapons.common.entity.EntityMekaArrow;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

@SideOnly(Side.CLIENT)
public class MekanismWeaponsClientProxy extends MekanismWeaponsCommonProxy {

    @Override
    public void registerItemRenders() {
        registerItemRender(MekanismWeaponsItems.mekaBow);
    }

    public void registerItemRender(Item item) {
        MekanismRenderer.registerItemRender(MekanismWeapons.MODID,item);
    }

    @Override
    public void preInit(){
        RenderingRegistry.registerEntityRenderingHandler(EntityMekaArrow.class,manager -> new RenderArrow<>(manager) {
            @Nullable
            @Override
            protected ResourceLocation getEntityTexture(EntityMekaArrow entity) {
                return new ResourceLocation(MekanismWeapons.MODID,"textures/entities/meka_arrow.png");
            }
        });
    }

}
