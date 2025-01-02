package mekanism.common.tile;

import baubles.api.BaublesApi;
import io.netty.buffer.ByteBuf;
import mekanism.api.TileNetworkList;
import mekanism.common.Mekanism;
import mekanism.common.block.states.BlockStateMachine.MachineType;
import mekanism.common.entity.EntityRobit;
import mekanism.common.integration.MekanismHooks;
import mekanism.common.tile.prefab.TileEntityEffectsBlock;
import mekanism.common.util.ChargeUtils;
import mekanism.common.util.InventoryUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NonNullListSynchronized;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityChargepad extends TileEntityEffectsBlock {

    public boolean isActive;
    public boolean clientActive;

    public Random random = new Random();

    public TileEntityChargepad() {
        super("machine.chargepad", "Chargepad", MachineType.CHARGEPAD.getStorage());
        inventory = NonNullListSynchronized.withSize(0, ItemStack.EMPTY);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            isActive = false;
            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class,
                    new AxisAlignedBB(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX() + 1, getPos().getY() + 0.2, getPos().getZ() + 1));
            for (EntityLivingBase entity : entities) {
                if (entity instanceof EntityPlayer || entity instanceof EntityRobit) {
                    isActive = getEnergy() > 0;
                }
                if (isActive) {
                    if (entity instanceof EntityRobit robit) {
                        double canGive = Math.min(getEnergy(), 1000);
                        double toGive = Math.min(robit.MAX_ELECTRICITY - robit.getEnergy(), canGive);
                        robit.setEnergy(robit.getEnergy() + toGive);
                        setEnergy(getEnergy() - toGive);
                    } else if (entity instanceof EntityPlayer player) {
                        double prevEnergy = getEnergy();
                        List<ItemStack> stacks = new ArrayList<>();
                        stacks.addAll(player.inventory.offHandInventory);
                        stacks.addAll(player.inventory.mainInventory);
                        stacks.addAll(player.inventory.armorInventory);
                        if (Mekanism.hooks.Baubles){
                            stacks.addAll(chargeBaublesInventory(player));
                        }
                        for(ItemStack stack : stacks) {
                            ChargeUtils.charge(stack, this);
                            if (prevEnergy != getEnergy()) {
                                break;
                            }
                        }

                    }
                }
            }

            if (clientActive != isActive) {
                if (isActive) {
                    world.playSound(null, getPos().getX() + 0.5, getPos().getY() + 0.1, getPos().getZ() + 0.5,
                            SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.8F);
                } else {
                    world.playSound(null, getPos().getX() + 0.5, getPos().getY() + 0.1, getPos().getZ() + 0.5,
                            SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.7F);
                }
                setActive(isActive);
            }
        } else if (isActive) {
            world.spawnParticle(EnumParticleTypes.REDSTONE, getPos().getX() + random.nextDouble(), getPos().getY() + 0.15,
                    getPos().getZ() + random.nextDouble(), 0, 0, 0);
        }
    }

    @Override
    public boolean sideIsConsumer(EnumFacing side) {
        return side == EnumFacing.DOWN || side == facing.getOpposite();
    }

    @Override
    public boolean getActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
        if (clientActive != active) {
            Mekanism.packetHandler.sendUpdatePacket(this);
        }
        clientActive = active;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTags) {
        super.readCustomNBT(nbtTags);
        clientActive = isActive = nbtTags.getBoolean("isActive");
    }


    @Optional.Method(modid = MekanismHooks.Baubles_MOD_ID)
    public List<ItemStack> chargeBaublesInventory(EntityPlayer player){
        IItemHandler baubles = BaublesApi.getBaublesHandler(player);
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < baubles.getSlots(); i++){
            stacks.add(baubles.getStackInSlot(i));
        }
        return stacks;
    }


    @Override
   public void writeCustomNBT(NBTTagCompound nbtTags) {
        super.writeCustomNBT(nbtTags);
        nbtTags.setBoolean("isActive", isActive);

    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            clientActive = dataStream.readBoolean();
            if (clientActive != isActive) {
                isActive = clientActive;
                MekanismUtils.updateBlock(world, getPos());
            }
        }
    }

    @Override
    public TileNetworkList getNetworkedData(TileNetworkList data) {
        super.getNetworkedData(data);
        data.add(isActive);
        return data;
    }

    @Override
    public boolean canSetFacing(@Nonnull EnumFacing facing) {
        return facing != EnumFacing.DOWN && facing != EnumFacing.UP;
    }

    @Override
    public boolean renderUpdate() {
        return false;
    }

    @Override
    public boolean lightUpdate() {
        return true;
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return InventoryUtils.EMPTY;
    }

    @Override
    public boolean isCapabilityDisabled(@Nonnull Capability<?> capability, EnumFacing side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.isCapabilityDisabled(capability, side);
    }

    @Override
    public boolean supportsAsync() {
        return false;
    }
}
