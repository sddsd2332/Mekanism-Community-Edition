package mekanism.multiblockmachine.client.render.machine;

import mekanism.api.gas.GasStack;
import mekanism.client.Utils.RenderTileEntityTime;
import mekanism.client.render.FluidRenderMap;
import mekanism.client.render.GasRenderMap;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.MekanismRenderer.DisplayInteger;
import mekanism.client.render.MekanismRenderer.Model3D;
import mekanism.multiblockmachine.client.model.machine.ModelDigitalAssemblyTable;
import mekanism.multiblockmachine.common.tile.machine.TileEntityDigitalAssemblyTable;
import mekanism.multiblockmachine.common.util.MekanismMultiblockMachineUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderDigitalAssemblyTable extends RenderTileEntityTime<TileEntityDigitalAssemblyTable> {
    public static final RenderDigitalAssemblyTable INSTANCE = new RenderDigitalAssemblyTable();
    private ModelDigitalAssemblyTable model = new ModelDigitalAssemblyTable();

    private static GasRenderMap<DisplayInteger[]> cachedCenterInputGas = new GasRenderMap<>();
    private static GasRenderMap<DisplayInteger[]> cachedCenterOutputGas = new GasRenderMap<>();
    private static Map<EnumFacing, DisplayInteger[]> energyDisplays1 = new EnumMap<>(EnumFacing.class);
    private static Map<EnumFacing, DisplayInteger[]> energyDisplays2 = new EnumMap<>(EnumFacing.class);
    private static FluidRenderMap<DisplayInteger[]> cachedCenterInputFluids = new FluidRenderMap<>();
    private static FluidRenderMap<DisplayInteger[]> cachedCenterOutputFluids = new FluidRenderMap<>();
    private static final int stages = 3600;

    public static void resetDisplayInts() {
        cachedCenterInputGas.clear();
        cachedCenterOutputGas.clear();
        energyDisplays1.clear();
        energyDisplays2.clear();
        cachedCenterInputFluids.clear();
        cachedCenterOutputFluids.clear();
    }

    @Override
    public void render(TileEntityDigitalAssemblyTable tileEntity, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
        if (tileEntity.getEnergy() > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableCull();
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
            GlStateManager.translate((float) x, (float) y, (float) z);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            DisplayInteger[] displayEnergyList1 = getEnergy1DisplayList(tileEntity.facing);
            displayEnergyList1[Math.min(stages - 1, (int) (tileEntity.prevEnergyScale * ((float) stages - 1)))].render();
            DisplayInteger[] displayEnergyList2 = getEnergy2DisplayList(tileEntity.facing);
            displayEnergyList2[Math.min(stages - 1, (int) (tileEntity.prevEnergyScale * ((float) stages - 1)))].render();
            MekanismRenderer.disableGlow(glowInfo);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.disableCull();
            GlStateManager.popMatrix();
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "DigitalAssemblyTable/DigitalAssemblyTable.png"));
        MekanismRenderer.rotate(tileEntity.facing, 0, 180, 90, 270);
        float actualRate = tileEntity.DoorHeight / 16F;
        GlStateManager.rotate(180, 0, 0, 1);
        model.renderWithPiston(Math.max(0, actualRate), 0.0625F, true);
        GlStateManager.popMatrix();
    }

    private DisplayInteger[] getInputGasListAndRender(GasStack gasStack, EnumFacing side) {
        if (cachedCenterInputGas.containsKey(gasStack)) {
            return cachedCenterInputGas.get(gasStack);
        }

        MekanismRenderer.Model3D toReturn = new MekanismRenderer.Model3D();
        toReturn.baseBlock = Blocks.WATER;
        toReturn.setTexture(gasStack.getGas().getSprite());
        DisplayInteger[] displays = new DisplayInteger[stages];
        cachedCenterInputGas.put(gasStack, displays);
        for (int i = 0; i < stages; i++) {
            displays[i] = MekanismRenderer.DisplayInteger.createAndStart();
            switch (side) {
                case NORTH:
                    break;
                case SOUTH:
                    break;
                case WEST:
                    break;
                case EAST:
                    break;
            }
            toReturn.minZ = 20.825 + .01;
            toReturn.maxZ = 0.7375 - .01;
            toReturn.minX = 0.325 + .01;
            toReturn.maxX = 0.7375 - .01;
            toReturn.minY = 1.3015625;
            toReturn.maxY = 1.3015625 + ((float) i / stages) * 2.25 - 0.001;
            MekanismRenderer.renderObject(toReturn);
            MekanismRenderer.DisplayInteger.endList();

        }
        return displays;
    }

    private DisplayInteger[] getOutputGasListAndRender(GasStack gasStack, EnumFacing side) {
        if (cachedCenterOutputGas.containsKey(gasStack)) {
            return cachedCenterOutputGas.get(gasStack);
        }
        MekanismRenderer.Model3D toReturn = new MekanismRenderer.Model3D();
        toReturn.baseBlock = Blocks.WATER;
        toReturn.setTexture(gasStack.getGas().getSprite());
        DisplayInteger[] displays = new DisplayInteger[stages];
        cachedCenterOutputGas.put(gasStack, displays);
        for (int i = 0; i < stages; i++) {
            displays[i] = MekanismRenderer.DisplayInteger.createAndStart();
            switch (side) {
                case NORTH:
                    break;
                case SOUTH:
                    break;
                case WEST:
                    break;
                case EAST:
                    break;
            }
            toReturn.minZ = 0.325 + .01;
            toReturn.maxZ = 0.7375 - .01;
            toReturn.minX = 0.325 + .01;
            toReturn.maxX = 0.7375 - .01;
            toReturn.minY = 1.3015625;
            toReturn.maxY = 1.3015625 + ((float) i / stages) * 2.25 - 0.001;
            MekanismRenderer.renderObject(toReturn);
            MekanismRenderer.DisplayInteger.endList();
        }
        return displays;
    }


    private DisplayInteger[] getInputFluidRender(FluidStack fluid, EnumFacing side) {
        if (cachedCenterInputFluids.containsKey(fluid)) {
            return cachedCenterInputFluids.get(fluid);
        }
        Model3D toReturn = new Model3D();
        toReturn.baseBlock = Blocks.WATER;
        MekanismRenderer.prepFlowing(toReturn, fluid);
        DisplayInteger[] displays = new DisplayInteger[stages];
        cachedCenterInputFluids.put(fluid, displays);
        for (int i = 0; i < stages; i++) {
            displays[i] = DisplayInteger.createAndStart();
            if (fluid.getFluid().getStill(fluid) != null) {
                switch (side) {
                    case NORTH:
                        break;
                    case SOUTH:
                        break;
                    case WEST:
                        break;
                    case EAST:
                        break;
                }
                toReturn.minX = 0.3125 + .01;
                toReturn.minZ = 0.3125 + .01;
                toReturn.maxX = 0.6875 - .01;
                toReturn.maxZ = 0.6875 - .01;
                toReturn.minY = 1.3015625;
                toReturn.maxY = 1.3015625 + ((float) i / stages) * 2.25 - 0.001;
                MekanismRenderer.renderObject(toReturn);
            }
            DisplayInteger.endList();
        }

        return displays;
    }

    private DisplayInteger[] getOutputFluidRender(FluidStack fluid, EnumFacing side) {
        if (cachedCenterOutputFluids.containsKey(fluid)) {
            return cachedCenterOutputFluids.get(fluid);
        }
        Model3D toReturn = new Model3D();
        toReturn.baseBlock = Blocks.WATER;
        MekanismRenderer.prepFlowing(toReturn, fluid);
        DisplayInteger[] displays = new DisplayInteger[stages];
        cachedCenterOutputFluids.put(fluid, displays);
        for (int i = 0; i < stages; i++) {
            displays[i] = DisplayInteger.createAndStart();
            if (fluid.getFluid().getStill(fluid) != null) {
                switch (side) {
                    case NORTH:
                        break;
                    case SOUTH:
                        break;
                    case WEST:
                        break;
                    case EAST:
                        break;
                }
                toReturn.minX = 0.3125 + .01;
                toReturn.minZ = 0.3125 + .01;
                toReturn.maxX = 0.6875 - .01;
                toReturn.maxZ = 0.6875 - .01;
                toReturn.minY = 1.3015625;
                toReturn.maxY = 1.3015625 + ((float) i / stages) * 2.25 - 0.001;
                MekanismRenderer.renderObject(toReturn);
            }
            DisplayInteger.endList();
        }

        return displays;
    }

    @SuppressWarnings("incomplete-switch")
    private DisplayInteger[] getEnergy1DisplayList(EnumFacing side) {
        if (energyDisplays1.containsKey(side)) {
            return energyDisplays1.get(side);
        }
        Model3D model3D = new Model3D();
        model3D.baseBlock = Blocks.WATER;
        model3D.setTexture(MekanismRenderer.energyIcon);
        DisplayInteger[] displays = new DisplayInteger[stages];
        energyDisplays1.put(side, displays);
        for (int i = 0; i < stages; i++) {
            displays[i] = DisplayInteger.createAndStart();
            switch (side) {
                case NORTH: //ok
                    model3D.minZ = 5.195 + 0.01;
                    model3D.maxZ = 5.785 - 0.01;
                    model3D.minX = 3.22 + 0.01;
                    model3D.maxX = 3.78 - 0.01;
                    break;
                case SOUTH:
                    model3D.minZ = -4.7625 + 0.01;
                    model3D.maxZ = -4.2 - 0.01;
                    model3D.minX = -2.72 + 0.01;
                    model3D.maxX = -2.22 - 0.01;
                    break;
                case WEST:
                    model3D.minX = 5.195 + 0.01;
                    model3D.maxX = 5.785 - 0.01;
                    model3D.minZ = -2.72 + 0.01;
                    model3D.maxZ = -2.22 - 0.01;
                    break;
                case EAST://OK
                    model3D.minX = -4.7625 + 0.01;
                    model3D.maxX = -4.2 - 0.01;
                    model3D.minZ = 3.22 - 0.01;
                    model3D.maxZ = 3.72 + 0.01;
                    break;
            }
            model3D.minY = 1.3015625;
            model3D.maxY = 1.3015625 + ((float) i / stages) * 2.25 - 0.001;
            MekanismRenderer.renderObject(model3D);
            DisplayInteger.endList();
        }
        return displays;
    }


    @SuppressWarnings("incomplete-switch")
    private DisplayInteger[] getEnergy2DisplayList(EnumFacing side) {
        if (energyDisplays2.containsKey(side)) {
            return energyDisplays2.get(side);
        }
        Model3D model3D = new Model3D();
        model3D.baseBlock = Blocks.WATER;
        model3D.setTexture(MekanismRenderer.energyIcon);
        DisplayInteger[] displays = new DisplayInteger[stages];
        energyDisplays2.put(side, displays);
        for (int i = 0; i < stages; i++) {
            displays[i] = DisplayInteger.createAndStart();
            switch (side) {
                case NORTH:
                    model3D.minZ = 5.195 + 0.01;
                    model3D.maxZ = 5.785 - 0.01;
                    model3D.minX = -2.78 + 0.01;
                    model3D.maxX = -2.22 - 0.01;
                    break;
                case SOUTH:
                    model3D.minZ = -4.7625 + 0.01;
                    model3D.maxZ = -4.2 - 0.01;
                    model3D.minX = 3.28 + 0.01;
                    model3D.maxX = 3.78 - 0.01;
                    break;
                case WEST:
                    model3D.minX = 5.195 + 0.01;
                    model3D.maxX = 5.785 - 0.01;
                    model3D.minZ = 3.28 + 0.01;
                    model3D.maxZ = 3.78 - 0.01;
                    break;
                case EAST:
                    model3D.minX = -4.7625 + 0.01;
                    model3D.maxX = -4.2 - 0.01;
                    model3D.minZ = -2.78 + 0.01;
                    model3D.maxZ = -2.22 - 0.01;
                    break;
            }
            model3D.minY = 1.3015625;
            model3D.maxY = 1.3015625 + ((float) i / stages) * 2.25 - 0.001;
            MekanismRenderer.renderObject(model3D);
            DisplayInteger.endList();
        }
        return displays;
    }
}
