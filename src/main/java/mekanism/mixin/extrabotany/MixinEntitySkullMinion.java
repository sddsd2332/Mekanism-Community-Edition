package mekanism.mixin.extrabotany;

import com.meteor.extrabotany.api.entity.IEntityWithShield;
import com.meteor.extrabotany.common.entity.gaia.EntitySkullMinion;
import mekanism.api.gear.Magnetic;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntitySkullMinion.class)
public abstract class MixinEntitySkullMinion extends EntityLiving implements IEntityWithShield {

    public MixinEntitySkullMinion(World worldIn) {
        super(worldIn);
    }


    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;dropItem(Z)Lnet/minecraft/entity/item/EntityItem;"))
    public EntityItem dropItem(EntityPlayer player, boolean dropAll) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack.isEmpty()) {
            return null;
        }
        if (stack.getItem() instanceof Magnetic magnetic && magnetic.isMagnetic(stack)) {
            return null;
        }
        if (stack.getItem().onDroppedByPlayer(stack, player)) {
            int count = dropAll ? player.inventory.getCurrentItem().getCount() : 1;
            return net.minecraftforge.common.ForgeHooks.onPlayerTossEvent(player, player.inventory.decrStackSize(player.inventory.currentItem, count), true);
        }
        return null;
    }

}

