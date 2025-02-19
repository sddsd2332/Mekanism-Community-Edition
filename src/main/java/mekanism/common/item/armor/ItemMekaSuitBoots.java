package mekanism.common.item.armor;

import mekanism.client.model.mekasuitarmour.ModelMekAsuitBoot;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMekaSuitBoots extends ItemMekaSuitArmor {

    public ItemMekaSuitBoots() {
        super(3, EntityEquipmentSlot.FEET);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return ModelMekAsuitBoot.boot;
    }


}
