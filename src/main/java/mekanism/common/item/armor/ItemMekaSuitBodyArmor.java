package mekanism.common.item.armor;


import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import mekanism.api.gear.IModule;
import mekanism.api.gear.ModuleData;
import mekanism.api.mixninapi.ElytraMixinHelp;
import mekanism.client.gui.element.GuiUtils;
import mekanism.client.model.mekasuitarmour.ModelMekAsuitBody;
import mekanism.client.model.mekasuitarmour.ModuleElytra;
import mekanism.client.model.mekasuitarmour.ModuleGravitational;
import mekanism.client.model.mekasuitarmour.ModuleJetpack;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.MekanismFluids;
import mekanism.common.MekanismModules;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.mekasuit.ModuleJetpackUnit;
import mekanism.common.interfaces.IOverlayRenderAware;
import mekanism.common.item.interfaces.IJetpackItem;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemMekaSuitBodyArmor extends ItemMekaSuitArmor implements IGasItem, IJetpackItem, ElytraMixinHelp, IOverlayRenderAware {

    public ItemMekaSuitBodyArmor() {
        super(1, EntityEquipmentSlot.CHEST);
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip) {
        if (hasModule(stack, MekanismModules.JETPACK_UNIT)) {
            GasStack gasStack = getGas(stack);
            if (gasStack == null) {
                tooltip.add(LangUtils.localize("tooltip.noGas") + ".");
            } else {
                tooltip.add(LangUtils.localize("tooltip.stored") + " " + gasStack.getGas().getLocalizedName() + ": " + gasStack.amount);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        ModelMekAsuitBody armorModel = ModelMekAsuitBody.armorModel;
        ModuleJetpack jetpack = ModuleJetpack.jetpacks;
        ModuleGravitational gravitational = ModuleGravitational.gravitational;
        ModuleElytra elytra = ModuleElytra.Elytra;
        if (isModuleEnabled(itemStack, MekanismModules.JETPACK_UNIT)) {
            if (!armorModel.bipedBody.childModels.contains(jetpack.jetpack)) {
                armorModel.bipedBody.addChild(jetpack.jetpack);
            }
        } else {
            armorModel.bipedBody.childModels.remove(jetpack.jetpack);
        }
        if (isModuleEnabled(itemStack, MekanismModules.GRAVITATIONAL_MODULATING_UNIT)) {
            if (!armorModel.bipedBody.childModels.contains(gravitational.gravitational_modulator)) {
                armorModel.bipedBody.addChild(gravitational.gravitational_modulator);
            }
        } else {
            armorModel.bipedBody.childModels.remove(gravitational.gravitational_modulator);
        }

        if (isModuleEnabled(itemStack, MekanismModules.ELYTRA_UNIT) && !entityLiving.isElytraFlying()) {
            if (!armorModel.bipedBody.childModels.contains(elytra.elytra)) {
                armorModel.bipedBody.addChild(elytra.elytra);
            }
        } else {
            armorModel.bipedBody.childModels.remove(elytra.elytra);
        }
        return armorModel;
    }


    @Override
    public int getRate(ItemStack itemstack) {
        return MekanismConfig.current().meka.mekaSuitJetpackTransferRate.val();
    }

    @Override
    public int addGas(ItemStack itemstack, GasStack stack) {
        if (!hasModule(itemstack, MekanismModules.JETPACK_UNIT)) {
            return 0;
        }
        if (getGas(itemstack) != null && getGas(itemstack).getGas() != stack.getGas()) {
            return 0;
        }
        if (stack.getGas() != MekanismFluids.Hydrogen) {
            return 0;
        }
        int toUse = Math.min(getMaxGas(itemstack) - getStored(itemstack), Math.min(getRate(itemstack), stack.amount));
        setGas(itemstack, new GasStack(stack.getGas(), getStored(itemstack) + toUse));
        return toUse;
    }

    public int getStored(ItemStack itemstack) {
        return getGas(itemstack) != null ? getGas(itemstack).amount : 0;
    }

    @Override
    public GasStack removeGas(ItemStack itemstack, int amount) {
        return null;
    }

    @Override
    public boolean canReceiveGas(ItemStack itemstack, Gas type) {
        return type == MekanismFluids.Hydrogen;
    }

    @Override
    public boolean canProvideGas(ItemStack itemstack, Gas type) {
        return false;
    }

    @Override
    public GasStack getGas(ItemStack itemstack) {
        return GasStack.readFromNBT(ItemDataUtils.getCompound(itemstack, "stored"));
    }

    @Override
    public void setGas(ItemStack itemstack, GasStack stack) {
        if (stack == null || stack.amount == 0) {
            ItemDataUtils.removeData(itemstack, "stored");
        } else {
            int amount = Math.max(0, Math.min(stack.amount, getMaxGas(itemstack)));
            GasStack gasStack = new GasStack(stack.getGas(), amount);
            ItemDataUtils.setCompound(itemstack, "stored", gasStack.write(new NBTTagCompound()));
        }
    }

    @Override
    public int getMaxGas(ItemStack stack) {
        IModule<ModuleJetpackUnit> module = getModule(stack, MekanismModules.JETPACK_UNIT);
        return module != null ? MekanismConfig.current().meka.mekaSuitJetpackMaxStorage.val() * module.getInstalledCount() : 0;
    }


    @Override
    public boolean canUseJetpack(ItemStack stack) {
        return armorType == EntityEquipmentSlot.CHEST && isModuleEnabled(stack, MekanismModules.JETPACK_UNIT) ? getStored(stack) > 0 : getModules(stack).stream().allMatch(module -> module.isEnabled() && module.getData().isExclusive(ModuleData.ExclusiveFlag.OVERRIDE_JUMP.getMask()));
    }

    @Override
    public JetpackMode getJetpackMode(ItemStack stack) {
        IModule<ModuleJetpackUnit> module = getModule(stack, MekanismModules.JETPACK_UNIT);
        if (module != null && module.isEnabled()) {
            return module.getCustomInstance().getMode();
        }
        return JetpackMode.DISABLED;
    }

    @Override
    public void useJetpackFuel(ItemStack stack) {
        IModule<ModuleJetpackUnit> module = getModule(stack, MekanismModules.JETPACK_UNIT);
        if (module != null && module.isEnabled()) {
            GasStack gas = getGas(stack);
            if (gas != null) {
                int amount = ceil(module.getCustomInstance().getThrustMultiplier());
                setGas(stack, new GasStack(gas.getGas(), gas.amount - amount));
            }
        }
    }

    @Override
    public double getJetpackThrust(ItemStack stack) {
        IModule<ModuleJetpackUnit> module = getModule(stack, MekanismModules.JETPACK_UNIT);
        if (module != null && module.isEnabled()) {
            float thrustMultiplier = module.getCustomInstance().getThrustMultiplier();
            int neededGas = ceil(thrustMultiplier);
            //Note: We verified we have at least one mB of gas before we get to the point of getting the thrust,
            // so we only need to do extra validation if we need more than a single mB of hydrogen
            if (neededGas > 1) {
                if (neededGas > getStored(stack)) {
                    //If we don't have enough gas stored to go at the set thrust, scale down the thrust
                    // to be whatever gas we have remaining
                    thrustMultiplier = getStored(stack);
                }
            }
            return 0.15 * thrustMultiplier;
        }
        return 0;
    }

    public static int ceil(float pValue) {
        int i = (int) pValue;
        return pValue > (float) i ? i + 1 : i;
    }


    @Override
    public boolean canElytraFly(ItemStack stack, EntityLivingBase entity) {
        if (armorType == EntityEquipmentSlot.CHEST && !entity.isSneaking()) {
            //Don't allow elytra flight if the player is sneaking. This lets the player exit elytra flight early
            IModule<?> module = getModule(stack, MekanismModules.ELYTRA_UNIT);
            if (module != null && module.isEnabled() && module.canUseEnergy(entity, MekanismConfig.current().meka.mekaSuitElytraEnergyUsage.val())) {
                //If we can use the elytra, check if the jetpack unit is also installed, and if it is,
                // only mark that we can use the elytra if the jetpack is not set to hover or if it is if it has no hydrogen stored
                IModule<ModuleJetpackUnit> jetpack = getModule(stack, MekanismModules.JETPACK_UNIT);
                return jetpack == null || !jetpack.isEnabled() || jetpack.getCustomInstance().getMode() != JetpackMode.HOVER || getGas(stack) == null;
            }
        }
        return false;
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, EntityLivingBase entity, int flightTicks) {
        //Note: As canElytraFly is checked just before this we don't bother validating ahead of time we have the energy
        // or that we are the correct slot
        if (!entity.world.isRemote && (flightTicks + 1) % 20 == 0) {
            IModule<?> module = getModule(stack, MekanismModules.ELYTRA_UNIT);
            if (module != null && module.isEnabled()) {
                module.useEnergy(entity, MekanismConfig.current().meka.mekaSuitElytraEnergyUsage.val());
            }
        }
        return true;
    }


    @Override
    public void renderItemOverlayIntoGUI(@NotNull ItemStack stack, int xPosition, int yPosition) {
        if (!stack.isEmpty() && hasModule(stack, MekanismModules.JETPACK_UNIT)) {
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            double health = getDurabilityForDisplayGas(stack);
            int rgbfordisplay = getGASRGBDurabilityForDisplay(stack);
            int i = Math.round(13.0F - (float) health * 13.0F);
            GuiUtils.draw(bufferbuilder, xPosition + 2, yPosition + 12, 13, 1, 0, 0, 0, 255);
            GuiUtils.draw(bufferbuilder, xPosition + 2, yPosition + 12, i, 1, rgbfordisplay >> 16 & 255, rgbfordisplay >> 8 & 255, rgbfordisplay & 255, 255);
            MekanismRenderer.resetColor();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }
    }

    private double getDurabilityForDisplayGas(ItemStack stack) {
        return 1D - ((getGas(stack) != null ? (double) getGas(stack).amount : 0D) / (double) getMaxGas(stack));
    }


    public int getGASRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
        GasStack gas = getGas(stack);
        if (gas != null) {
            MekanismRenderer.color(gas);
            return gas.getGas().getTint();
        } else {
            return MathHelper.hsvToRGB(Math.max(0.0F, (float) (1 - getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
        }
    }
}
