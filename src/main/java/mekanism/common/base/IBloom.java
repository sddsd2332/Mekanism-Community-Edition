package mekanism.common.base;

import gregtech.client.renderer.IRenderSetup;
import gregtech.client.shader.postprocessing.BloomType;
import gregtech.client.utils.BloomEffectUtil;
import gregtech.client.utils.IBloomEffect;
import mekanism.common.Mekanism;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.tile.prefab.TileEntityBasicBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.common.Optional.Method;

public interface IBloom {

    default void Bloom(TileEntityBasicBlock tile, IRenderSetup setup, IBloomEffect render) {
        if (Mekanism.hooks.GTCEULoaded) {
            GTECUBloom(tile, setup, render);
        } else if (Mekanism.hooks.LumenizedLoaded) {
            LumenizedBloom(tile, setup, render);
        }
    }

    @Method(modid = MekanismHooks.GTCEU_MOD_ID)
    default void GTECUBloom(TileEntityBasicBlock tile, IRenderSetup setup, IBloomEffect render) {
        BloomEffectUtil.registerBloomRender(setup, BloomType.UNREAL, render, ticket -> {
            WorldClient mcWorld = Minecraft.getMinecraft().world;
            return !tile.isInvalid() && tile.getWorld() != null && tile.getWorld() == mcWorld && mcWorld.getTileEntity(tile.getPos()) == tile;
        });
    }

    @Method(modid = MekanismHooks.LUMENIZED_MOD_ID)
    default void LumenizedBloom(TileEntityBasicBlock tile, IRenderSetup setup, IBloomEffect render) {
        BloomEffectUtil.registerBloomRender(setup, BloomType.UNREAL, render, ticket -> {
            WorldClient mcWorld = Minecraft.getMinecraft().world;
            return !tile.isInvalid() && tile.getWorld() != null && tile.getWorld() == mcWorld && mcWorld.getTileEntity(tile.getPos()) == tile;
        });
    }

}
