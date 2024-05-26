package mekanism.client.gui;

import mekanism.client.gui.element.GuiPlayerSlot;
import mekanism.client.gui.element.GuiSlot;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.common.inventory.container.ContainerLaserTractorBeam;
import mekanism.common.tile.laser.TileEntityLaserTractorBeam;
import mekanism.common.util.LangUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLaserTractorBeam extends GuiMekanismTile<TileEntityLaserTractorBeam> {

    public GuiLaserTractorBeam(InventoryPlayer inventory, TileEntityLaserTractorBeam tile) {
        super(tile, new ContainerLaserTractorBeam(inventory, tile));
        addGuiElement(new GuiSecurityTab(this, tileEntity, getGuiLocation()));
        addGuiElement(new GuiPlayerSlot(this, getGuiLocation()));
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addGuiElement(new GuiSlot(GuiSlot.SlotType.NORMAL, this, getGuiLocation(), 7 + x * 18, 15 + y * 18));
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tileEntity.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tileEntity.getName()) / 2), 4, 0x404040);
        fontRenderer.drawString(LangUtils.localize("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

}
