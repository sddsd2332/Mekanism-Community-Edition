package mekanism.weapons.client;

import mekanism.client.render.MekanismRenderer;
import mekanism.weapons.client.renderer.entity.RenderMekaArrow;
import mekanism.weapons.common.MekanismWeapons;
import mekanism.weapons.common.MekanismWeaponsCommonProxy;
import mekanism.weapons.common.MekanismWeaponsItems;
import mekanism.weapons.common.entity.EntityMekaArrow;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MekanismWeaponsClientProxy extends MekanismWeaponsCommonProxy {

    @Override
    public void registerItemRenders() {
        registerItemRender(MekanismWeaponsItems.mekaBow);
        registerItemRender(MekanismWeaponsItems.mekArrow);
    }

    public void registerItemRender(Item item) {
        MekanismRenderer.registerItemRender(MekanismWeapons.MODID, item);
    }

    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityMekaArrow.class,  RenderMekaArrow :: new);
    }

}
