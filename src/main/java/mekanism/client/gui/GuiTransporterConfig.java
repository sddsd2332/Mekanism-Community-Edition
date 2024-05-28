package mekanism.client.gui;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mekanism.api.Coord4D;
import mekanism.api.EnumColor;
import mekanism.api.RelativeSide;
import mekanism.api.transmitters.TransmissionType;
import mekanism.client.Utils.ClientUtil;
import mekanism.client.gui.GuiSideConfiguration.GuiPos;
import mekanism.client.gui.button.GuiColorButton;
import mekanism.client.gui.button.GuiDisableableButton;
import mekanism.client.gui.button.GuiSideDataButton;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.sound.SoundHandler;
import mekanism.common.Mekanism;
import mekanism.common.SideData;
import mekanism.common.base.ISideConfiguration;
import mekanism.common.inventory.container.ContainerNull;
import mekanism.common.network.PacketConfigurationUpdate.ConfigurationPacket;
import mekanism.common.network.PacketConfigurationUpdate.ConfigurationUpdateMessage;
import mekanism.common.network.PacketSimpleGui.SimpleGuiMessage;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.prefab.TileEntityContainerBlock;
import mekanism.common.util.LangUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SideOnly(Side.CLIENT)
public class GuiTransporterConfig extends GuiMekanismTile<TileEntityContainerBlock> {
    private Int2ObjectMap<GuiPos> slotPosMap = new Int2ObjectOpenHashMap<>();
    private ISideConfiguration configurable;
    private List<GuiSideDataButton> sideDataButtons = new ArrayList<>();
    private GuiButton backButton;
    private GuiButton strictInputButton;
    private GuiButton colorButton;
    private int buttonID = 0;

    public GuiTransporterConfig(EntityPlayer player, ISideConfiguration tile) {
        super((TileEntityContainerBlock) tile, new ContainerNull(player, (TileEntityContainerBlock) tile));
        xSize = 156;
        //ySize = 95;
        ySize = 119;
        configurable = tile;
        ResourceLocation resource = getGuiLocation();
        slotPosMap.put(0, new GuiPos(41, 64 + 16, RelativeSide.BOTTOM));
        slotPosMap.put(1, new GuiPos(41, 34, RelativeSide.TOP));
        slotPosMap.put(2, new GuiPos(41, 57, RelativeSide.FRONT));
        slotPosMap.put(3, new GuiPos(18, 64 + 16, RelativeSide.BACK));
        slotPosMap.put(4, new GuiPos(18, 57, RelativeSide.LEFT));
        slotPosMap.put(5, new GuiPos(64, 57, RelativeSide.RIGHT));
        addGuiElement(new GuiInnerScreen(this, resource, 41, 15, 74, 12));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(backButton = new GuiDisableableButton(buttonID++, guiLeft + 6, guiTop + 6, 14).with(GuiDisableableButton.ImageOverlay.BACK));
        buttonList.add(strictInputButton = new GuiDisableableButton(buttonID++, guiLeft + 136, guiTop + 6, 14).with(GuiDisableableButton.ImageOverlay.EXCLAMATION));
        buttonList.add(colorButton = new GuiColorButton(buttonID++, guiLeft + 122, guiTop + 49, () -> configurable.getEjector().getOutputColor()));
        for (int i = 0; i < slotPosMap.size(); i++) {
            GuiPos guiPos = slotPosMap.get(i);
            EnumFacing facing = EnumFacing.byIndex(i);
            RelativeSide side = RelativeSide.bydex(i);
            GuiSideDataButton button = new GuiSideDataButton(buttonID++, guiLeft + guiPos.xPos, guiTop + guiPos.yPos, i, () -> configurable.getConfig().getOutput(TransmissionType.ITEM, facing), () -> configurable.getEjector().getInputColor(facing), tileEntity, side);
            buttonList.add(button);
            sideDataButtons.add(button);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        TileEntity tile = (TileEntity) configurable;
        if (guibutton.id == backButton.id) {
            int guiId = Mekanism.proxy.getGuiId(tile.getBlockType(), tile.getBlockMetadata());
            Mekanism.packetHandler.sendToServer(new SimpleGuiMessage(Coord4D.get(tile), 0, guiId));
        } else if (guibutton.id == strictInputButton.id) {
            Mekanism.packetHandler.sendToServer(new ConfigurationUpdateMessage(ConfigurationPacket.STRICT_INPUT, Coord4D.get(tile), 0, 0, null));
        } else if (guibutton.id == colorButton.id) {
            Mekanism.packetHandler.sendToServer(new ConfigurationUpdateMessage(ConfigurationPacket.EJECT_COLOR, Coord4D.get(tile), Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 2 : 0, 0, null));
        } else {
            for (GuiSideDataButton button : sideDataButtons) {
                if (guibutton.id == button.id) {
                    Mekanism.packetHandler.sendToServer(new ConfigurationUpdateMessage(ConfigurationPacket.INPUT_COLOR, Coord4D.get(tile),
                            Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 2 : 0, button.getSlotPosMapIndex(), null));
                    break;
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String text = LangUtils.localize("gui.configuration.transporter");
        fontRenderer.drawString(text, (xSize / 2) - (fontRenderer.getStringWidth(text) / 2), 5, 0x404040);
        text = LangUtils.localize("gui.strictInput") + " (" + LangUtils.transOnOff(configurable.getEjector().hasStrictInput()) + ")";
        renderScaledText(text, 43, 17, 0x00CD00, 70);
        fontRenderer.drawString(LangUtils.localize("gui.input"), 51, 105, 0x787878);
        fontRenderer.drawString(LangUtils.localize("gui.output"), 121, 68, 0x787878);
        int xAxis = mouseX - guiLeft;
        int yAxis = mouseY - guiTop;
        for (GuiSideDataButton button : sideDataButtons) {
            if (button.isMouseOver()) {
                SideData data = button.getSideData();
                if (data != TileComponentConfig.EMPTY) {
                    EnumColor color = button.getColor();
                    List<String> info = new ArrayList<>();
                    for (int i = 0; i < slotPosMap.size(); i++) {
                        GuiPos guiPos = slotPosMap.get(i);
                        String FacingName = guiPos.FacingName.getTranslationKey();
                        if (button.getSlotPosMapIndex() == i) {
                            info.add(FacingName);
                        }
                    }
                    info.add(color != null ? color.getColoredName() : LangUtils.localize("gui.none"));
                    if (button.getItem() != ItemStack.EMPTY) {
                        if (button.getItem().getItem() != Items.AIR) {
                            info.add(button.getItem().getItem().getItemStackDisplayName(button.getItem()));
                        }
                    }
                    displayTooltips(info, xAxis, yAxis);
                }
                break;
            }
        }
        if (strictInputButton.isMouseOver()) {
            displayTooltip(LangUtils.localize("gui.configuration.strictInput"), xAxis, yAxis);
        } else if (colorButton.isMouseOver()) {
            if (configurable.getEjector().getOutputColor() != null) {
                displayTooltip(configurable.getEjector().getOutputColor().getColoredName(), xAxis, yAxis);
            } else {
                displayTooltip(LangUtils.localize("gui.none"), xAxis, yAxis);
            }
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int xAxis, int yAxis) {
        for (int i = 0; i < slotPosMap.size(); i++) {
            GuiPos guiPos = slotPosMap.get(i);
            if (sideDataButtons.get(i).getItem() != ItemStack.EMPTY) {
                ClientUtil.renderItem(sideDataButtons.get(i).getItem(), guiLeft + guiPos.xPos + 3, guiTop + guiPos.yPos + 3);
            }
        }
        super.drawGuiContainerBackgroundLayer(xAxis, yAxis);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        TileEntity tile = (TileEntity) configurable;
        if (tile == null || mc.world.getTileEntity(tile.getPos()) == null) {
            mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);
        if (button == 1) {
            TileEntity tile = (TileEntity) configurable;
            if (colorButton.isMouseOver()) {
                //Allow going backwards
                SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
                Mekanism.packetHandler.sendToServer(new ConfigurationUpdateMessage(ConfigurationPacket.EJECT_COLOR, Coord4D.get(tile), 1, 0, null));
            } else {
                //Handle right clicking the side data buttons
                for (GuiSideDataButton sideDataButton : sideDataButtons) {
                    if (sideDataButton.isMouseOver()) {
                        SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
                        Mekanism.packetHandler.sendToServer(new ConfigurationUpdateMessage(ConfigurationPacket.INPUT_COLOR, Coord4D.get(tile), 1, sideDataButton.getSlotPosMapIndex(), null));
                        break;
                    }
                }
            }
        }
    }

}
