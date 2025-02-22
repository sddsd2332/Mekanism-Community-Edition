package mekanism.common.item.armor;

import mekanism.api.gear.IModule;
import mekanism.api.gear.SwiftSneakHelp;
import mekanism.client.model.mekasuitarmour.ModelMekAsuitLeg;
import mekanism.common.MekanismModules;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMekaSuitPants extends ItemMekaSuitArmor implements SwiftSneakHelp {

    public ItemMekaSuitPants() {
        super(2,EntityEquipmentSlot.LEGS);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "mekanism:render/MekaSuitPants.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return ModelMekAsuitLeg.leg;
    }

    @Override
    public float getSneakingSpeedBonusLevel(ItemStack base) {
        IModule<?> MotorizedServo = getModule(base, MekanismModules.MOTORIZED_SERVO_UNIT);
        if (MotorizedServo != null && MotorizedServo.isEnabled()) {
            return MotorizedServo.getInstalledCount();
        }
        return 0;
    }
}
