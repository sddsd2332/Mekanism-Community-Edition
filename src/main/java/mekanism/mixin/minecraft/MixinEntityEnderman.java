package mekanism.mixin.minecraft;

import mekanism.api.mixninapi.EnderMaskMixinHelp;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityEnderman.class)
public class MixinEntityEnderman extends EntityMob {

    public MixinEntityEnderman(World worldIn) {
        super(worldIn);
    }

    /**
     * @author sddsd2332
     * @reason 覆写来实现当玩家穿戴指定物品时，且玩家看向末影人时，末影人不会主动攻击玩家
     */
    @Overwrite
    private boolean shouldAttackPlayer(EntityPlayer player) {
        ItemStack stack = player.inventory.armorInventory.get(3);
        if (stack.getItem() instanceof EnderMaskMixinHelp help) {
            return !help.isEnderMask(stack, player, ((EntityEnderman) (Object) this));
        } else if (stack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
            return false;
        } else {
            Vec3d vec3d = player.getLook(1.0F).normalize();
            Vec3d vec3d1 = new Vec3d(this.posX - player.posX, this.getEntityBoundingBox().minY + (double) this.getEyeHeight() - (player.posY + (double) player.getEyeHeight()), this.posZ - player.posZ);
            double d0 = vec3d1.length();
            vec3d1 = vec3d1.normalize();
            double d1 = vec3d.dotProduct(vec3d1);
            return d1 > 1.0D - 0.025D / d0 ? player.canEntityBeSeen(this) : false;
        }

    }

}
