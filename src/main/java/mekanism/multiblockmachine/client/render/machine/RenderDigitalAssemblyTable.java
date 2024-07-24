package mekanism.multiblockmachine.client.render.machine;

import mekanism.api.gas.GasStack;
import mekanism.client.Utils.RenderTileEntityTime;
import mekanism.client.render.FluidRenderMap;
import mekanism.client.render.GasRenderMap;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.MekanismRenderer.DisplayInteger;
import mekanism.client.render.MekanismRenderer.Model3D;
import mekanism.common.Mekanism;
import mekanism.multiblockmachine.client.model.machine.ModelDigitalAssemblyTable;
import mekanism.multiblockmachine.common.block.states.BlockStateMultiblockMachineGenerator;
import mekanism.multiblockmachine.common.tile.machine.TileEntityDigitalAssemblyTable;
import mekanism.multiblockmachine.common.util.MekanismMultiblockMachineUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.EnumMap;
import java.util.Map;

import static mekanism.multiblockmachine.common.MultiblockMachineBlocks.MultiblockGenerator;
import static mekanism.multiblockmachine.common.MultiblockMachineBlocks.MultiblockMachine;

@SideOnly(Side.CLIENT)
public class RenderDigitalAssemblyTable extends RenderTileEntityTime<TileEntityDigitalAssemblyTable> {
    public static final RenderDigitalAssemblyTable INSTANCE = new RenderDigitalAssemblyTable();
    private static final int stages = 3600;
    private static Map<EnumFacing, GasRenderMap<DisplayInteger[]>> cachedCenterInputGas = new EnumMap<>(EnumFacing.class);
    private static Map<EnumFacing, GasRenderMap<DisplayInteger[]>> cachedCenterOutputGas = new EnumMap<>(EnumFacing.class);
    private static Map<EnumFacing, DisplayInteger[]> energyDisplays1 = new EnumMap<>(EnumFacing.class);
    private static Map<EnumFacing, DisplayInteger[]> energyDisplays2 = new EnumMap<>(EnumFacing.class);
    private static Map<EnumFacing, FluidRenderMap<DisplayInteger[]>> cachedCenterInputFluids = new EnumMap<>(EnumFacing.class);
    private static Map<EnumFacing, FluidRenderMap<DisplayInteger[]>> cachedCenterOutputFluids = new EnumMap<>(EnumFacing.class);
    private ModelDigitalAssemblyTable model = new ModelDigitalAssemblyTable();

    public static void resetDisplayInts() {
        cachedCenterInputGas.clear();
        cachedCenterOutputGas.clear();
        energyDisplays1.clear();
        energyDisplays2.clear();
        cachedCenterInputFluids.clear();
        cachedCenterOutputFluids.clear();
    }

    public ModelDigitalAssemblyTable getModel(){
        return model;
    }

    @Override
    public void render(TileEntityDigitalAssemblyTable tileEntity, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "DigitalAssemblyTable/DigitalAssemblyTable.png"));
        MekanismRenderer.rotate(tileEntity.facing, 0, 180, 90, 270);
        float actualRate = tileEntity.DoorHeight / 16F;
        GlStateManager.rotate(180, 0, 0, 1);
        model.renderWithPiston(Math.max(0, actualRate), 0.0625F, tileEntity.getEnergy() != 0, tileEntity.getActive(), rendererDispatcher.renderEngine, getTime()); // 先渲染本体
        GlStateManager.popMatrix();


        if (tileEntity.getEnergy() > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableCull();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
            GlStateManager.translate((float) x, (float) y, (float) z);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            DisplayInteger[] displayEnergyList1 = getEnergy1DisplayList(tileEntity.facing);
            displayEnergyList1[tileEntity.getScaledEnergyLevel(stages - 1)].render();
            DisplayInteger[] displayEnergyList2 = getEnergy2DisplayList(tileEntity.facing);
            displayEnergyList2[tileEntity.getScaledEnergyLevel(stages - 1)].render();
            MekanismRenderer.disableGlow(glowInfo);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            GlStateManager.popMatrix();
        }

        if (tileEntity.inputGasTank.getStored() > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableCull();
            GlStateManager.disableLighting();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.translate((float) x, (float) y, (float) z);
            MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
            DisplayInteger[] displayList = getInputGasListAndRender(tileEntity.inputGasTank.getGas(), tileEntity.facing);
            MekanismRenderer.color(tileEntity.inputGasTank.getGas());
            displayList[Math.min(stages - 1, (int) (tileEntity.inputGasScale * ((float) stages - 1)))].render();
            MekanismRenderer.resetColor();
            MekanismRenderer.disableGlow(glowInfo);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableLighting();
            GlStateManager.disableCull();
            GlStateManager.popMatrix();
        }

        if (tileEntity.outputGasTank.getStored() > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableCull();
            GlStateManager.disableLighting();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.translate((float) x, (float) y, (float) z);
            MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow();
            DisplayInteger[] displayList = getOutputGasListAndRender(tileEntity.outputGasTank.getGas(), tileEntity.facing);
            MekanismRenderer.color(tileEntity.outputGasTank.getGas());
            displayList[Math.min(stages - 1, (int) (tileEntity.outputGasScale * ((float) stages - 1)))].render();
            MekanismRenderer.resetColor();
            MekanismRenderer.disableGlow(glowInfo);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableLighting();
            GlStateManager.disableCull();
            GlStateManager.popMatrix();
        }

        if (tileEntity.inputFluidTank.getFluidAmount() > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableCull();
            GlStateManager.disableLighting();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.translate((float) x, (float) y, (float) z);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow(tileEntity.inputFluidTank.getFluid());
            DisplayInteger[] displayList = getInputFluidRender(tileEntity.inputFluidTank.getFluid(), tileEntity.facing);
            MekanismRenderer.color(tileEntity.inputFluidTank.getFluid());
            displayList[Math.min(stages - 1, (int) (tileEntity.inputFluidScale * ((float) stages - 1)))].render();
            MekanismRenderer.resetColor();
            MekanismRenderer.disableGlow(glowInfo);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableLighting();
            GlStateManager.disableCull();
            GlStateManager.popMatrix();
        }

        if (tileEntity.outputFluidTank.getFluidAmount() > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableCull();
            GlStateManager.disableLighting();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.translate((float) x, (float) y, (float) z);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            MekanismRenderer.GlowInfo glowInfo = MekanismRenderer.enableGlow(tileEntity.outputFluidTank.getFluid());
            DisplayInteger[] displayList = getOutputFluidRender(tileEntity.outputFluidTank.getFluid(), tileEntity.facing);
            MekanismRenderer.color(tileEntity.outputFluidTank.getFluid());
            displayList[Math.min(stages - 1, (int) (tileEntity.outputFluidScale * ((float) stages - 1)))].render();
            MekanismRenderer.resetColor();
            MekanismRenderer.disableGlow(glowInfo);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableLighting();
            GlStateManager.disableCull();
            GlStateManager.popMatrix();
        }

        ItemStack stack = tileEntity.getStackInSlot(14);
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            if (stack.getItem() == Item.getItemFromBlock(MultiblockMachine)) {
                GlStateManager.translate((float) x + 0.5F, (float) y + 2.5F, (float) z + 0.5F);
                GlStateManager.scale(2.8375F, 2.8375F, 2.8375F);
            } else if (stack.getItem() == Item.getItemFromBlock(MultiblockGenerator) && !stack.isItemEqual(BlockStateMultiblockMachineGenerator.MultiblockMachineGeneratorType.LARGE_WIND_GENERATOR.getStack())) {
                GlStateManager.translate((float) x + 0.5F, (float) y + 2.5F, (float) z + 0.5F);
                GlStateManager.scale(3.125F, 3.125F, 3.125F);
            } else {
                GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
                GlStateManager.scale(1.0F, 1.0F, 1.0F);
            }
            MekanismRenderer.rotate(tileEntity.facing, 270, 90, 0, 180);
            GlStateManager.disableLighting();
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }


        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        bindTexture(MekanismMultiblockMachineUtils.getResource(MekanismMultiblockMachineUtils.ResourceType.RENDER_MACHINE, "DigitalAssemblyTable/DigitalAssemblyTable.png"));
        MekanismRenderer.rotate(tileEntity.facing, 0, 180, 90, 270);
        GlStateManager.rotate(180, 0, 0, 1);
        model.renderGlass(0.0625F);// 最后渲染玻璃
        GlStateManager.popMatrix();

    }


    private DisplayInteger[] getInputGasListAndRender(GasStack gasStack, EnumFacing side) {
        if (cachedCenterInputGas.containsKey(side) && cachedCenterInputGas.get(side).containsKey(gasStack)) {
            return cachedCenterInputGas.get(side).get(gasStack);
        }
        MekanismRenderer.Model3D toReturn = new MekanismRenderer.Model3D();
        toReturn.baseBlock = Blocks.WATER;
        toReturn.setTexture(gasStack.getGas().getSprite());
        DisplayInteger[] displays = new DisplayInteger[stages];
        for (int i = 0; i < stages; i++) {
            displays[i] = MekanismRenderer.DisplayInteger.createAndStart();
            switch (side) {
                case NORTH:
                    toReturn.minZ = 1.22;
                    toReturn.maxZ = 1.78;
                    toReturn.minX = -4.775;
                    toReturn.maxX = -4.15;
                    break;
                case SOUTH:
                    toReturn.minZ = -0.78;
                    toReturn.maxZ = -0.22;
                    toReturn.maxX = 5.775;
                    toReturn.minX = 5.15;
                    break;
                case WEST:
                    toReturn.minX = 1.22;
                    toReturn.maxX = 1.78;
                    toReturn.maxZ = 5.775;
                    toReturn.minZ = 5.15;
                    break;
                case EAST:
                    toReturn.minX = -0.78;
                    toReturn.maxX = -0.22;
                    toReturn.minZ = -4.775;
                    toReturn.maxZ = -4.15;
                    break;
            }
            toReturn.minY = 1.3015625;
            toReturn.maxY = 1.3015625 + ((float) i / stages) * 2.25 - 0.001;
            MekanismRenderer.renderObject(toReturn);
            MekanismRenderer.DisplayInteger.endList();
        }
        if (cachedCenterInputGas.containsKey(side)) {
            cachedCenterInputGas.get(side).put(gasStack, displays);
        } else {
            GasRenderMap<DisplayInteger[]> map = new GasRenderMap<>();
            map.put(gasStack, displays);
            cachedCenterInputGas.put(side, map);
        }
        return displays;
    }

    private DisplayInteger[] getOutputGasListAndRender(GasStack gasStack, EnumFacing side) {
        if (cachedCenterOutputGas.containsKey(side) && cachedCenterOutputGas.get(side).containsKey(gasStack)) {
            return cachedCenterOutputGas.get(side).get(gasStack);
        }
        MekanismRenderer.Model3D toReturn = new MekanismRenderer.Model3D();
        toReturn.baseBlock = Blocks.WATER;
        toReturn.setTexture(gasStack.getGas().getSprite());
        DisplayInteger[] displays = new DisplayInteger[stages];
        for (int i = 0; i < stages; i++) {
            displays[i] = MekanismRenderer.DisplayInteger.createAndStart();
            switch (side) {
                case NORTH:
                    toReturn.minZ = 1.22;
                    toReturn.maxZ = 1.78;
                    toReturn.maxX = 5.775;
                    toReturn.minX = 5.15;
                    break;
                case SOUTH:
                    toReturn.minZ = -0.78;
                    toReturn.maxZ = -0.22;
                    toReturn.minX = -4.775;
                    toReturn.maxX = -4.15;
                    break;
                case WEST:
                    toReturn.minX = 1.22;
                    toReturn.maxX = 1.78;
                    toReturn.minZ = -4.775;
                    toReturn.maxZ = -4.15;
                    break;
                case EAST:
                    toReturn.minX = -0.78;
                    toReturn.maxX = -0.22;
                    toReturn.maxZ = 5.775;
                    toReturn.minZ = 5.15;
                    break;
            }
            toReturn.minY = 1.4375;
            toReturn.maxY = 1.4375 + ((float) i / stages) * 2.25 - 0.001;
            MekanismRenderer.renderObject(toReturn);
            MekanismRenderer.DisplayInteger.endList();
        }
        if (cachedCenterOutputGas.containsKey(side)) {
            cachedCenterOutputGas.get(side).put(gasStack, displays);
        } else {
            GasRenderMap<DisplayInteger[]> map = new GasRenderMap<>();
            map.put(gasStack, displays);
            cachedCenterOutputGas.put(side, map);
        }
        return displays;
    }


    private DisplayInteger[] getInputFluidRender(FluidStack fluid, EnumFacing side) {
        if (cachedCenterInputFluids.containsKey(side) && cachedCenterInputFluids.get(side).containsKey(fluid)) {
            return cachedCenterInputFluids.get(side).get(fluid);
        }
        Model3D toReturn = new Model3D();
        toReturn.baseBlock = Blocks.WATER;
        MekanismRenderer.prepFlowing(toReturn, fluid);
        DisplayInteger[] displays = new DisplayInteger[stages];
        for (int i = 0; i < stages; i++) {
            displays[i] = DisplayInteger.createAndStart();
            if (fluid.getFluid().getStill(fluid) != null) {
                switch (side) {
                    case NORTH:
                        toReturn.minZ = 3.22;
                        toReturn.maxZ = 3.78;
                        toReturn.minX = -4.775;
                        toReturn.maxX = -4.15;
                        break;
                    case SOUTH:
                        toReturn.minZ = -2.78;
                        toReturn.maxZ = -2.22;
                        toReturn.maxX = 5.775;
                        toReturn.minX = 5.15;
                        break;
                    case WEST:
                        toReturn.minX = 3.22;
                        toReturn.maxX = 3.78;
                        toReturn.maxZ = 5.775;
                        toReturn.minZ = 5.15;
                        break;
                    case EAST:
                        toReturn.minX = -2.78;
                        toReturn.maxX = -2.22;
                        toReturn.minZ = -4.775;
                        toReturn.maxZ = -4.15;
                        break;
                }
                toReturn.minY = 1.4375;
                toReturn.maxY = 1.4375 + ((float) i / stages) * 2.25 - 0.001;
                MekanismRenderer.renderObject(toReturn);
            }
            DisplayInteger.endList();
        }
        if (cachedCenterInputFluids.containsKey(side)) {
            cachedCenterInputFluids.get(side).put(fluid, displays);
        } else {
            FluidRenderMap<DisplayInteger[]> map = new FluidRenderMap<>();
            map.put(fluid, displays);
            cachedCenterInputFluids.put(side, map);
        }
        return displays;
    }

    private DisplayInteger[] getOutputFluidRender(FluidStack fluid, EnumFacing side) {
        if (cachedCenterOutputFluids.containsKey(side) && cachedCenterOutputFluids.get(side).containsKey(fluid)) {
            return cachedCenterOutputFluids.get(side).get(fluid);
        }
        Model3D toReturn = new Model3D();
        toReturn.baseBlock = Blocks.WATER;
        MekanismRenderer.prepFlowing(toReturn, fluid);
        DisplayInteger[] displays = new DisplayInteger[stages];
        for (int i = 0; i < stages; i++) {
            displays[i] = DisplayInteger.createAndStart();
            if (fluid.getFluid().getStill(fluid) != null) {
                switch (side) {
                    case NORTH:
                        toReturn.minZ = 3.22;
                        toReturn.maxZ = 3.78;
                        toReturn.maxX = 5.775;
                        toReturn.minX = 5.15;
                        break;
                    case SOUTH:
                        toReturn.minZ = -2.78;
                        toReturn.maxZ = -2.22;
                        toReturn.minX = -4.775;
                        toReturn.maxX = -4.15;
                        break;
                    case WEST:
                        toReturn.minX = 3.22;
                        toReturn.maxX = 3.78;
                        toReturn.minZ = -4.775;
                        toReturn.maxZ = -4.15;
                        break;
                    case EAST:
                        toReturn.minX = -2.78;
                        toReturn.maxX = -2.22;
                        toReturn.maxZ = 5.775;
                        toReturn.minZ = 5.15;
                        break;
                }
                toReturn.minY = 1.3015625;
                toReturn.maxY = 1.3015625 + ((float) i / stages) * 2.25 - 0.001;
                MekanismRenderer.renderObject(toReturn);
            }
            DisplayInteger.endList();
        }
        if (cachedCenterOutputFluids.containsKey(side)) {
            cachedCenterOutputFluids.get(side).put(fluid, displays);
        } else {
            FluidRenderMap<DisplayInteger[]> map = new FluidRenderMap<>();
            map.put(fluid, displays);
            cachedCenterOutputFluids.put(side, map);
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

    @Override
    public boolean isGlobalRenderer(TileEntityDigitalAssemblyTable te) {
        return true;
    }

}
