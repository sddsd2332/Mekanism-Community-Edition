package mekanism.client.gui.chemical;

import mekanism.api.gas.Gas;
import mekanism.api.gas.OreGas;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.*;
import mekanism.client.gui.element.GuiProgress.IProgressInfoHandler;
import mekanism.client.gui.element.GuiProgress.ProgressBar;
import mekanism.client.gui.element.GuiSlot.SlotOverlay;
import mekanism.client.gui.element.GuiSlot.SlotType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.client.gui.element.tab.GuiSideConfigurationTab;
import mekanism.client.gui.element.tab.GuiTransporterConfigTab;
import mekanism.client.gui.element.tab.GuiUpgradeTab;
import mekanism.common.inventory.container.ContainerChemicalCrystallizer;
import mekanism.common.recipe.machines.CrystallizerRecipe;
import mekanism.common.tile.machine.TileEntityChemicalCrystallizer;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiChemicalCrystallizer extends GuiMekanismTile<TileEntityChemicalCrystallizer> {

    private List<ItemStack> iterStacks = new ArrayList<>();
    private ItemStack renderStack = ItemStack.EMPTY;
    private int stackSwitch = 0;
    private int stackIndex = 0;
    private Gas prevGas;

    public GuiChemicalCrystallizer(InventoryPlayer inventory, TileEntityChemicalCrystallizer tile) {
        super(tile, new ContainerChemicalCrystallizer(inventory, tile));
        ResourceLocation resource = getGuiLocation();
        addGuiElement(new GuiSecurityTab(this, tileEntity, resource));
        addGuiElement(new GuiRedstoneControl(this, tileEntity, resource));
        addGuiElement(new GuiUpgradeTab(this, tileEntity, resource));
        addGuiElement(new GuiPowerBar(this, tileEntity, resource, 160, 23));
        addGuiElement(new GuiSideConfigurationTab(this, tileEntity, resource));
        addGuiElement(new GuiTransporterConfigTab(this, 34, tileEntity, resource));
        addGuiElement(new GuiEnergyInfo(() -> {
            String multiplier = MekanismUtils.getEnergyDisplay(tileEntity.energyPerTick);
            return Arrays.asList(LangUtils.localize("gui.using") + ": " + multiplier + "/t",
                    LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxEnergy() - tileEntity.getEnergy()));
        }, this, resource));
        addGuiElement(new GuiGasGauge(() -> tileEntity.inputTank, GuiGauge.Type.STANDARD, this, resource, 5, 4).withColor(GuiGauge.TypeColor.RED));
        addGuiElement(new GuiSlot(SlotType.INPUT, this, resource, 5, 64).with(SlotOverlay.PLUS));
        addGuiElement(new GuiSlot(SlotType.POWER, this, resource, 154, 4).with(SlotOverlay.POWER));
        addGuiElement(new GuiSlot(SlotType.OUTPUT, this, resource, 130, 56));
        addGuiElement(new GuiProgress(new IProgressInfoHandler() {
            @Override
            public double getProgress() {
                return tileEntity.getScaledProgress();
            }
        }, ProgressBar.LARGE_RIGHT, this, resource, 51, 60));
        addGuiElement(new GuiInnerScreen(this, resource, 27, 13, 121, 42).with(true));
        addGuiElement(new GuiPlayerSlot(this, resource));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        if (tileEntity.inputTank.getGas() != null) {
            fontRenderer.drawString(tileEntity.inputTank.getGas().getGas().getLocalizedName(), 29, 15, 0x00CD00);
            if (tileEntity.inputTank.getGas().getGas() instanceof OreGas oreGas) {
                fontRenderer.drawString("(" + oreGas.getOreName() + ")", 29, 24, 0x00CD00);
            } else {
                CrystallizerRecipe recipe = tileEntity.getRecipe();
                if (recipe == null) {
                    fontRenderer.drawString("(" + LangUtils.localize("gui.noRecipe") + ")", 29, 24, 0x00CD00);
                } else {
                    fontRenderer.drawString("(" + recipe.recipeOutput.output.getDisplayName() + ")", 29, 24, 0x00CD00);
                }
            }
        }
        renderItem(renderStack, 131, 14);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        if (xAxis >= -21 && xAxis <= -3 && yAxis >= 116 && yAxis <= 134) {
            List<String> info = new ArrayList<>();
            boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
            boolean outslot = tileEntity.inventory.get(1).getCount() == tileEntity.inventory.get(1).getMaxStackSize();
            if (energy) {
                info.add(LangUtils.localize("gui.no_energy"));
            }
            if (outslot) {
                info.add(LangUtils.localize("gui.item_no_space"));
            }
            if (outslot || energy) {
                this.displayTooltips(info, xAxis, yAxis);
            }
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
        boolean outslot = tileEntity.inventory.get(1).getCount() == tileEntity.inventory.get(1).getMaxStackSize();
        boolean energy = tileEntity.getEnergy() < tileEntity.energyPerTick || tileEntity.getEnergy() == 0;
        if (outslot) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(ResourceType.SLOT, "Slot_Icon.png"));
            drawTexturedModalRect(guiLeft + 130, guiTop + 56, 158, 0, 18, 18);
        }
        if (outslot || energy) {
            mc.getTextureManager().bindTexture(MekanismUtils.getResource(ResourceType.TAB, "Warning_Info.png"));
            drawTexturedModalRect(guiLeft - 26, guiTop + 112, 0, 0, 26, 26);
            addGuiElement(new GuiWarningInfo(this, getGuiLocation(), false));
        }
    }


    private Gas getInputGas() {
        return tileEntity.inputTank.getGas() != null ? tileEntity.inputTank.getGas().getGas() : null;
    }

    private void resetStacks() {
        iterStacks.clear();
        renderStack = ItemStack.EMPTY;
        stackSwitch = 0;
        stackIndex = -1;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if (prevGas != getInputGas()) {
            prevGas = getInputGas();
            boolean reset = false;
            if (prevGas == null || !(prevGas instanceof OreGas oreGas) || !oreGas.isClean()) {
                reset = true;
                resetStacks();
            }
            if (!reset) {
                OreGas gas = (OreGas) prevGas;
                String oreDictName = "ore" + gas.getName().substring(5);
                updateStackList(oreDictName);
            }
        }

        if (stackSwitch > 0) {
            stackSwitch--;
        }
        if (stackSwitch == 0 && iterStacks != null && iterStacks.size() > 0) {
            stackSwitch = 20;
            if (stackIndex == -1 || stackIndex == iterStacks.size() - 1) {
                stackIndex = 0;
            } else if (stackIndex < iterStacks.size() - 1) {
                stackIndex++;
            }
            renderStack = iterStacks.get(stackIndex);
        } else if (iterStacks != null && iterStacks.size() == 0) {
            renderStack = ItemStack.EMPTY;
        }
    }

    private void updateStackList(String oreName) {
        if (iterStacks == null) {
            iterStacks = new ArrayList<>();
        } else {
            iterStacks.clear();
        }

        List<String> keys = new ArrayList<>();
        for (String s : OreDictionary.getOreNames()) {
            if (oreName.equals(s) || oreName.equals("*")) {
                keys.add(s);
            } else {
                boolean endsWith = oreName.endsWith("*");
                boolean startsWith = oreName.startsWith("*");
                if (endsWith && !startsWith) {
                    if (s.startsWith(oreName.substring(0, oreName.length() - 1))) {
                        keys.add(s);
                    }
                } else if (startsWith && !endsWith) {
                    if (s.endsWith(oreName.substring(1))) {
                        keys.add(s);
                    }
                } else if (startsWith) {
                    if (s.contains(oreName.substring(1, oreName.length() - 1))) {
                        keys.add(s);
                    }
                }
            }
        }

        for (String key : keys) {
            for (ItemStack stack : OreDictionary.getOres(key, false)) {
                ItemStack toAdd = stack.copy();
                if (!iterStacks.contains(stack) && toAdd.getItem() instanceof ItemBlock) {
                    iterStacks.add(stack.copy());
                }
            }
        }
        stackSwitch = 0;
        stackIndex = -1;
    }
}
