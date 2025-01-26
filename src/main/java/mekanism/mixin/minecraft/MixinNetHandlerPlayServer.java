package mekanism.mixin.minecraft;

import mekanism.api.mixninapi.ElytraMixinHelp;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NetHandlerPlayServer.class)
public abstract class MixinNetHandlerPlayServer implements INetHandlerPlayServer, ITickable {


    @Shadow
    public EntityPlayerMP player;

    @Shadow
    private Vec3d targetPos;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void processEntityAction(CPacketEntityAction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.player.getServerWorld());
        this.player.markPlayerActive();

        switch (packetIn.getAction()) {
            case START_SNEAKING:
                this.player.setSneaking(true);
                break;
            case STOP_SNEAKING:
                this.player.setSneaking(false);
                break;
            case START_SPRINTING:
                this.player.setSprinting(true);
                break;
            case STOP_SPRINTING:
                this.player.setSprinting(false);
                break;
            case STOP_SLEEPING:

                if (this.player.isPlayerSleeping()) {
                    this.player.wakeUpPlayer(false, true, true);
                    this.targetPos = new Vec3d(this.player.posX, this.player.posY, this.player.posZ);
                }

                break;
            case START_RIDING_JUMP:

                if (this.player.getRidingEntity() instanceof IJumpingMount) {
                    IJumpingMount ijumpingmount1 = (IJumpingMount) this.player.getRidingEntity();
                    int i = packetIn.getAuxData();

                    if (ijumpingmount1.canJump() && i > 0) {
                        ijumpingmount1.handleStartJump(i);
                    }
                }

                break;
            case STOP_RIDING_JUMP:

                if (this.player.getRidingEntity() instanceof IJumpingMount) {
                    IJumpingMount ijumpingmount = (IJumpingMount) this.player.getRidingEntity();
                    ijumpingmount.handleStopJump();
                }

                break;
            case OPEN_INVENTORY:

                if (this.player.getRidingEntity() instanceof AbstractHorse) {
                    ((AbstractHorse) this.player.getRidingEntity()).openGUI(this.player);
                }

                break;
            case START_FALL_FLYING:
                if (!tryToStartFallFlying(player)) {
                    this.player.clearElytraFlying();
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid client command!");
        }
    }

    @Unique
    public boolean tryToStartFallFlying(EntityPlayer player) {
        if (!player.onGround && !player.isElytraFlying() && !player.isInWater() && !player.isPotionActive(MobEffects.LEVITATION)) {
            ItemStack itemstack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (itemstack.getItem() instanceof ElytraMixinHelp help && help.canElytraFly(itemstack, player)) {
                this.player.setElytraFlying();
                return true;
            }
        }
        return false;
    }

}
