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
        registerItemRender(MekanismWeaponsItems.mekaArrow);
        registerItemRender(MekanismWeaponsItems.mekaTana);
        registerItemRender(MekanismWeaponsItems.katana_blade);
        registerItemRender(MekanismWeaponsItems.bow_riser);
        registerItemRender(MekanismWeaponsItems.bow_limb);
    }

    public void registerItemRender(Item item) {
        MekanismRenderer.registerItemRender(MekanismWeapons.MODID, item);
    }

    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityMekaArrow.class,  RenderMekaArrow :: new);
    }

}
