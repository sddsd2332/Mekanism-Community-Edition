package mekanism.common.content.gear.mekasuit;

import mekanism.api.gear.IHUDElement;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.Modules;
import mekanism.common.item.armor.ItemMekaSuitHelmet;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import mekanism.common.util.StorageUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ModuleNutritionalInjectionUnit extends Module {

    private static final ResourceLocation icon = MekanismUtils.getResource(ResourceType.GUI_HUD, "nutritional_injection_unit.png");


    @Override
    public void tickServer(EntityPlayer player) {
        super.tickServer(player);
        double usage = MekanismConfig.current().meka.mekaSuitEnergyUsageNutritionalInjection.val();
        if (MekanismUtils.isPlayingMode(player) && player.canEat(false)) {
            //Check if we can use a single iteration of it
            ItemStack container = getContainer();
            if (container.getItem() instanceof ItemMekaSuitHelmet armour) {
                int needed = Math.min(20 - player.getFoodStats().getFoodLevel(), armour.getStored(container) / MekanismConfig.current().general.nutritionalPasteMBPerFood.val());
                int toFeed = Math.min((int) (getContainerEnergy() / usage), needed);
                if (toFeed > 0) {
                    useEnergy(player, usage * toFeed);
                    armour.useGas(container, toFeed * MekanismConfig.current().general.nutritionalPasteMBPerFood.val());
                    player.getFoodStats().addStats((int) needed, needed * MekanismConfig.current().general.nutritionalPasteSaturation.val());
                }
            }
        }
    }

    @Override
    public void addHUDElements(EntityPlayer player, List<IHUDElement> hudElementAdder) {
        if (isEnabled()) {
            ItemStack container = getContainer();
            if (container.getItem() instanceof ItemMekaSuitHelmet armour) {
                double ratio = StorageUtils.getRatio(armour.getStored(container), MekanismConfig.current().meka.mekaSuitNutritionalMaxStorage.val());
                hudElementAdder.add(Modules.hudElementPercent(icon, ratio));
            }
        }
    }
}