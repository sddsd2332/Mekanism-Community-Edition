package mekanism.client.gui;

import mekanism.api.Coord4D;
import mekanism.client.Utils.ClientUtil;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SideOnly(Side.CLIENT)
public class GuiSeismicReader extends GuiScreen {

    private final ItemStack itemStack;
    private Coord4D pos;
    private int xSize = 147;
    private int ySize = 182;
    private World worldObj;
    private List<Pair<Integer, Block>> blockList = new ArrayList<>();
    private Rectangle tooltip;
    private GuiDisableableButton upButton;
    private GuiDisableableButton downButton;
    private int currentLayer;

    public GuiSeismicReader(World world, Coord4D coord, ItemStack stack) {
        pos = new Coord4D(coord.x, Math.min(255, coord.y), coord.z, world.provider.getDimension());
        worldObj = world;
        itemStack = stack;
        calculate();
        currentLayer = Math.max(0, blockList.size() - 1);
    }

    @Override
    public void initGui() {
        super.initGui();
        int guiLeft = (width - xSize) / 2;
        int guiTop = (height - ySize) / 2;
        buttonList.add(upButton = new GuiDisableableButton(0, guiLeft + 57, guiTop + 74, 14, 14).with(GuiDisableableButton.ImageOverlay.UP));
        buttonList.add(downButton = new GuiDisableableButton(1, guiLeft + 57, guiTop + 92, 14, 14).with(GuiDisableableButton.ImageOverlay.DOWN));
        tooltip = new Rectangle(guiLeft + 92, guiTop + 82, 16, 16);
        updateEnabledButtons();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        updateEnabledButtons();
    }

    private void updateEnabledButtons() {
        upButton.enabled = currentLayer + 1 <= blockList.size() - 1;
        downButton.enabled = currentLayer - 1 >= 1;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
        int guiLeft = (width - xSize) / 2;
        int guiTop = (height - ySize) / 2;
        mc.renderEngine.bindTexture(getGuiLocation());
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        // Fix the overlapping if > 100
        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft + 111, guiTop + 87, 0);

        if (currentLayer >= 100) {
            GlStateManager.translate(0, 1, 0);
            GlStateManager.scale(0.7F, 0.7F, 0.7F);
        }

        fontRenderer.drawString(String.format("%s", currentLayer), 0, 0, 0x3BFB98);
        GlStateManager.popMatrix();

        // Render the item stacks
        for (int i = 0; i < 9; i++) {
            int centralX = guiLeft + 94, centralY = guiTop + 102;
            int layer = currentLayer + (i - 5);
            if (0 <= layer && layer < blockList.size()) {
                Pair<Integer, Block> integerBlockPair = blockList.get(layer);
                ItemStack stack = new ItemStack(integerBlockPair.getRight(), 1, integerBlockPair.getLeft());
                GlStateManager.pushMatrix();
                GlStateManager.translate(centralX - 2, centralY - i * 16 + (22 * 2), 0);
                if (i < 4) {
                    GlStateManager.translate(0.2F, 2.5F, 0);
                }
                if (i != 4) {
                    GlStateManager.translate(1.5F, 0, 0);
                    GlStateManager.scale(0.8F, 0.8F, 0.8F);
                }
                ClientUtil.renderItem(stack, 0, 0);
                GlStateManager.popMatrix();
            }
        }

        int frequency = 0;
        // Get the name from the stack and render it
        if (currentLayer - 1 >= 0) {
            Pair<Integer, Block> integerBlockPair = blockList.get(currentLayer - 1);
            ItemStack nameStack = new ItemStack(integerBlockPair.getRight(), 1, integerBlockPair.getLeft());
            String renderString = nameStack.getDisplayName();

            String capitalised = renderString.substring(0, 1).toUpperCase() + renderString.substring(1);
            int lengthX = fontRenderer.getStringWidth(capitalised);
            float renderScale = lengthX > 53 ? 53f / lengthX : 1.0f;

            GlStateManager.pushMatrix();
            GlStateManager.translate(guiLeft + 10, guiTop + 16, 0);
            GlStateManager.scale(renderScale, renderScale, renderScale);
            fontRenderer.drawString(capitalised, 0, 0, 0x3BFB98);
            GlStateManager.popMatrix();
            MekanismRenderer.resetColor();

            if (tooltip.intersects(new Rectangle(mouseX, mouseY, 1, 1))) {
                mc.renderEngine.bindTexture(MekanismUtils.getResource(ResourceType.GUI_ELEMENT, "GuiTooltips.png"));
                int fontLengthX = fontRenderer.getStringWidth(capitalised) + 5;
                int renderX = mouseX + 10, renderY = mouseY - 5;
                GlStateManager.pushMatrix();
                drawTexturedModalRect(renderX, renderY, 0, 0, fontLengthX, 16);
                drawTexturedModalRect(renderX + fontLengthX, renderY, 0, 16, 2, 16);
                fontRenderer.drawString(capitalised, renderX + 4, renderY + 4, 0x3BFB98);
                GlStateManager.popMatrix();
                MekanismRenderer.resetColor();
            }

            for (Pair<Integer, Block> pair : blockList) {
                Block block = integerBlockPair.getRight();
                if (pair.getRight() == block && Objects.equals(pair.getLeft(), integerBlockPair.getLeft())) {
                    frequency++;
                }
            }
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft + 10, guiTop + 26, 0);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        fontRenderer.drawString(LangUtils.localize("gui.abundancy") + ": " + frequency, 0, 0, 0x3BFB98);
        GlStateManager.popMatrix();
        MekanismRenderer.resetColor();
        super.drawScreen(mouseX, mouseY, partialTick);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        blockList.clear();
    }

    public void calculate() {
        for (BlockPos p = new BlockPos(pos.x, 0, pos.z); p.getY() < pos.y; p = p.up()) {
            IBlockState state = worldObj.getBlockState(p);
            Block block = state.getBlock();
            int metadata = block.getMetaFromState(state);
            blockList.add(Pair.of(metadata, block));
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton.id == upButton.id) {
            currentLayer++;
        } else if (guibutton.id == downButton.id) {
            currentLayer--;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


    protected ResourceLocation getGuiLocation() {
        return MekanismUtils.getResource(ResourceType.GUI, "GuiSeismicReader.png");
    }
}
