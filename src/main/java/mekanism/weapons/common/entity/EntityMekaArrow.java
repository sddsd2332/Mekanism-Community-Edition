package mekanism.weapons.common.entity;

import mekanism.weapons.common.MekanismWeaponsItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityMekaArrow extends EntityArrow{

    public boolean isPicked;

    public EntityMekaArrow(World worldIn) {
        super(worldIn);
    }

    public EntityMekaArrow(World worldIn, EntityLivingBase shooter){
        super(worldIn, shooter);
    }


    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(MekanismWeaponsItems.mekaArrow);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityPlayer){
        if (isPicked){
            super.onCollideWithPlayer(entityPlayer);
        }
    }

    public void setPicked(boolean picked){
        isPicked = picked;
    }
}
