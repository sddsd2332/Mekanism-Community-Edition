package mekanism.common.item;

import mcmultipart.api.multipart.IMultipart;
import mekanism.api.EnumColor;
import mekanism.api.transmitters.TransmissionType;
import mekanism.client.MekKeyHandler;
import mekanism.client.MekanismKeyHandler;
import mekanism.client.gui.element.GuiUtils;
import mekanism.common.Mekanism;
import mekanism.common.base.ITierItem;
import mekanism.common.block.states.BlockStateTransmitter.TransmitterType;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.integration.multipart.MultipartMekanism;
import mekanism.common.interfaces.IOverlayRenderAware;
import mekanism.common.tier.*;
import mekanism.common.tile.transmitter.TileEntitySidedPipe;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemBlockTransmitter extends ItemBlockMultipartAble implements ITierItem, IOverlayRenderAware {

    public Block metaBlock;

    public ItemBlockTransmitter(Block block) {
        super(block);
        metaBlock = block;
        setHasSubtypes(true);
        setCreativeTab(Mekanism.tabMekanism);
    }

    @Override
    public int getMetadata(int i) {
        return i;
    }

    @Override
    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY,
                                float hitZ, @Nonnull IBlockState state) {
        boolean place = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state);
        if (place) {
            TileEntitySidedPipe tileEntity = (TileEntitySidedPipe) world.getTileEntity(pos);
            tileEntity.setBaseTier(getBaseTier(stack));
            if (!world.isRemote) {
                Mekanism.packetHandler.sendUpdatePacket(tileEntity);
            }
        }
        return place;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack itemstack, World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flag) {
        if (!MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.sneakKey)) {
            TransmissionType transmission = TransmitterType.values()[itemstack.getItemDamage()].getTransmission();
            BaseTier tier = getBaseTier(itemstack);
            if (transmission == TransmissionType.ENERGY) {
                list.add(EnumColor.INDIGO + LangUtils.localize("tooltip.capacity") + ": " + EnumColor.GREY +
                        MekanismUtils.getEnergyDisplay(CableTier.get(tier).getCableCapacity()) + "/t");
            } else if (transmission == TransmissionType.FLUID) {
                list.add(EnumColor.INDIGO + LangUtils.localize("tooltip.capacity") + ": " + EnumColor.GREY + PipeTier.get(tier).getPipeCapacity() + "mB/t");
                list.add(EnumColor.INDIGO + LangUtils.localize("tooltip.pumpRate") + ": " + EnumColor.GREY + PipeTier.get(tier).getPipePullAmount() + "mB/t");
            } else if (transmission == TransmissionType.GAS) {
                list.add(EnumColor.INDIGO + LangUtils.localize("tooltip.capacity") + ": " + EnumColor.GREY + TubeTier.get(tier).getTubeCapacity() + "mB/t");
                list.add(EnumColor.INDIGO + LangUtils.localize("tooltip.pumpRate") + ": " + EnumColor.GREY + TubeTier.get(tier).getTubePullAmount() + "mB/t");
            } else if (transmission == TransmissionType.ITEM) {
                list.add(EnumColor.INDIGO + LangUtils.localize("tooltip.speed") + ": " + EnumColor.GREY + (TransporterTier.get(tier).getSpeed() / (100 / 20)) + " m/s");
                list.add(EnumColor.INDIGO + LangUtils.localize("tooltip.pumpRate") + ": " + EnumColor.GREY + TransporterTier.get(tier).getPullAmount() * 2 + "/s");
            } else if (transmission == TransmissionType.HEAT) {
                list.add(EnumColor.INDIGO + LangUtils.localize("tooltip.conduction") + ": " + EnumColor.GREY + ConductorTier.get(tier).getInverseConduction());
                list.add(EnumColor.INDIGO + LangUtils.localize("tooltip.insulation") + ": " + EnumColor.GREY + ConductorTier.get(tier).getBaseConductionInsulation());
                list.add(EnumColor.INDIGO + LangUtils.localize("tooltip.heatCapacity") + ": " + EnumColor.GREY + ConductorTier.get(tier).getInverseHeatCapacity());
            }
            list.add(LangUtils.localize("tooltip.hold") + " " + EnumColor.AQUA + GameSettings.getKeyDisplayString(MekanismKeyHandler.sneakKey.getKeyCode()) +
                    EnumColor.GREY + " " + LangUtils.localize("tooltip.forDetails"));
        } else {
            TransmitterType type = TransmitterType.values()[itemstack.getItemDamage()];
            switch (type) {
                case UNIVERSAL_CABLE -> {
                    list.add(EnumColor.DARK_GREY + LangUtils.localize("tooltip.capableTrans") + ":");
                    list.add("- " + EnumColor.PURPLE + "RF " + EnumColor.GREY + "(ThermalExpansion)");
                    list.add("- " + EnumColor.PURPLE + "EU " + EnumColor.GREY + "(IndustrialCraft)");
                    list.add("- " + EnumColor.PURPLE + "Joules " + EnumColor.GREY + "(Mekanism)");
                }
                case MECHANICAL_PIPE -> {
                    list.add(EnumColor.DARK_GREY + LangUtils.localize("tooltip.capableTrans") + ":");
                    list.add("- " + EnumColor.PURPLE + LangUtils.localize("tooltip.fluids") + " " + EnumColor.GREY + "(MinecraftForge)");
                }
                case PRESSURIZED_TUBE -> {
                    list.add(EnumColor.DARK_GREY + LangUtils.localize("tooltip.capableTrans") + ":");
                    list.add("- " + EnumColor.PURPLE + LangUtils.localize("tooltip.gasses") + " (Mekanism)");
                }
                case LOGISTICAL_TRANSPORTER -> {
                    list.add(EnumColor.DARK_GREY + LangUtils.localize("tooltip.capableTrans") + ":");
                    list.add("- " + EnumColor.PURPLE + LangUtils.localize("tooltip.items") + " (" + LangUtils.localize("tooltip.universal") + ")");
                    list.add("- " + EnumColor.PURPLE + LangUtils.localize("tooltip.blocks") + " (" + LangUtils.localize("tooltip.universal") + ")");
                }
                case RESTRICTIVE_TRANSPORTER -> {
                    list.add(EnumColor.DARK_GREY + LangUtils.localize("tooltip.capableTrans") + ":");
                    list.add("- " + EnumColor.PURPLE + LangUtils.localize("tooltip.items") + " (" + LangUtils.localize("tooltip.universal") + ")");
                    list.add("- " + EnumColor.PURPLE + LangUtils.localize("tooltip.blocks") + " (" + LangUtils.localize("tooltip.universal") + ")");
                    list.add("- " + EnumColor.DARK_RED + LangUtils.localize("tooltip.restrictiveDesc"));
                }
                case DIVERSION_TRANSPORTER -> {
                    list.add(EnumColor.DARK_GREY + LangUtils.localize("tooltip.capableTrans") + ":");
                    list.add("- " + EnumColor.PURPLE + LangUtils.localize("tooltip.items") + " (" + LangUtils.localize("tooltip.universal") + ")");
                    list.add("- " + EnumColor.PURPLE + LangUtils.localize("tooltip.blocks") + " (" + LangUtils.localize("tooltip.universal") + ")");
                    list.add("- " + EnumColor.DARK_RED + LangUtils.localize("tooltip.diversionDesc"));
                }
                case THERMODYNAMIC_CONDUCTOR -> {
                    list.add(EnumColor.DARK_GREY + LangUtils.localize("tooltip.capableTrans") + ":");
                    list.add("- " + EnumColor.PURPLE + LangUtils.localize("tooltip.heat") + " (Mekanism)");
                }
            }
        }
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack stack) {
        TransmitterType type = TransmitterType.get(stack.getItemDamage());
        String name = type.getTranslationKey();
        if (type.hasTiers()) {
            BaseTier tier = getBaseTier(stack);
            name = tier.getSimpleName() + name;
        }
        return getTranslationKey() + "." + name;
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        TransmitterType type = TransmitterType.get(stack.getItemDamage());
        String name = type.getTranslationKey();
        if (type.hasTiers()) {
            BaseTier tier = getBaseTier(stack);
            name = tier.getSimpleName() + name + ".name";
            return getBaseTier(stack).getColor() + LangUtils.localize(getTranslationKey() + "." + name);
        } else {
            return LangUtils.localize(getTranslationKey() + "." + name + ".name");
        }
    }

    @Override
    public BaseTier getBaseTier(ItemStack itemstack) {
        if (!itemstack.hasTagCompound()) {
            return BaseTier.BASIC;
        }
        return BaseTier.values()[itemstack.getTagCompound().getInteger("tier")];
    }

    @Override
    public void setBaseTier(ItemStack itemstack, BaseTier tier) {
        if (!itemstack.hasTagCompound()) {
            itemstack.setTagCompound(new NBTTagCompound());
        }
        itemstack.getTagCompound().setInteger("tier", tier.ordinal());
    }

    @Override
    @Optional.Method(modid = MekanismHooks.MCMULTIPART_MOD_ID)
    protected IMultipart getMultiPart() {
        return MultipartMekanism.TRANSMITTER_MP;
    }

    @Override
    public void renderItemOverlayIntoGUI(@NotNull ItemStack stack, int xPosition, int yPosition) {
        if (!Mekanism.hooks.MekanismMixinHelp && !Mekanism.hooks.JEI) {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemBlockTransmitter transmitter) {
                TransmissionType transmission = TransmitterType.values()[stack.getItemDamage()].getTransmission();
                if (transmission == TransmissionType.GAS || transmission == TransmissionType.HEAT || transmission == TransmissionType.ENERGY) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0, 0, 200);
                    TransmitterType type = TransmitterType.get(stack.getItemDamage());
                    String name = type.getTranslationKey();
                    if (type.hasTiers()) {
                        BaseTier tier = transmitter.getBaseTier(stack);
                        name = tier.getSimpleName() + name;
                    }
                    Minecraft.getMinecraft().renderEngine.bindTexture(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_ICONS, name.toLowerCase(Locale.ROOT) + ".png"));
                    GuiUtils.blit(xPosition, yPosition, 0, 0, 16, 16, 16, 16);
                    GlStateManager.popMatrix();
                }
            }
        }
    }
}
