package mekanism.common.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMekaSuitArmor extends EntityItem {

    public EntityMekaSuitArmor(World world, Entity original, ItemStack stack) {
        this(world, original.posX, original.posY, original.posZ, stack);
        setPickupDelay(40);
        motionX = original.motionX;
        motionY = original.motionY;
        motionZ = original.motionZ;
        setItem(stack);
    }

    public EntityMekaSuitArmor(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z);
        setItem(stack);
    }

    @Override
    protected void dealFireDamage(int damage) {
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        return source.getDamageType().equals("outOfWorld");
    }

    @Override
    public void onUpdate() {
        ItemStack stack = getDataManager().get(ITEM);
        if (!stack.isEmpty()) {
            if (stack.getItem().onEntityItemUpdate(this)) {
                return;
            }
        }
        if (getItem().isEmpty()) {
            setDead();
        } else {
            super.onUpdate();

            if (pickupDelay > 0) {
                --pickupDelay;
            }

            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;
            motionY -= 0.03999999910593033D;
            noClip = pushOutOfBlocks(posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, posZ);
            move(MoverType.SELF, motionX, motionY, motionZ);
            boolean flag = (int) prevPosX != (int) posX || (int) prevPosY != (int) posY || (int) prevPosZ != (int) posZ;

            if (flag || ticksExisted % 25 == 0) {
                if (world.getBlockState(new BlockPos(posX, posY, posZ)).getMaterial() == Material.LAVA) {
                    motionY = 0.20000000298023224D;
                    motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
                    motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
                    //this.playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
                }

                if (!world.isRemote) {
                    searchForOtherItemsNearby();
                }
            }

            float f = 0.98F;

            if (onGround) {
                f = world.getBlockState(new BlockPos(posX, getEntityBoundingBox().minY - 1, posZ)).getBlock().slipperiness * 0.98F;
            }

            motionX *= f;
            motionY *= 0.9800000190734863D;
            motionZ *= f;

            if (onGround) {
                motionY *= -0.5D;
            }

            ++age;

            ItemStack item = getDataManager().get(ITEM);

            if (!world.isRemote && age >= lifespan) {
                if (item.isEmpty()) {
                    setDead();
                }

            }

            if (!item.isEmpty() && item.getCount() <= 0) {
                setDead();
            }
        }
    }
}
