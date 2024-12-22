package mekanism.mixin.extrabotany;

import baubles.api.BaublesApi;
import com.meteor.extrabotany.api.entity.IEntityWithShield;
import com.meteor.extrabotany.common.core.config.ConfigHandler;
import com.meteor.extrabotany.common.entity.gaia.EntityGaiaIII;
import mekanism.api.gear.Magnetic;
import mekanism.common.config.MixinConfig;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.api.boss.IBotaniaBoss;

import java.util.HashMap;
import java.util.Map;

@Mixin(EntityGaiaIII.class)
public abstract class MixinEntityGaiaIII extends EntityLiving implements IBotaniaBoss, IEntityWithShield, IEntityAdditionalSpawnData {


    public MixinEntityGaiaIII(World worldIn) {
        super(worldIn);
    }


    //库存掉落修改
    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lcom/meteor/extrabotany/common/entity/gaia/EntityGaiaIII;disarm(Lnet/minecraft/entity/player/EntityPlayer;)V", remap = false))
    public void disarm(EntityGaiaIII instance, EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            ItemStack stackAt = player.inventory.getStackInSlot(i);
            if (stackAt.isEmpty()) {
                continue;
            }
            if (!match(stackAt)) {
                if (stackAt.getItem() instanceof Magnetic magnetic && magnetic.isMagnetic(stackAt)) {
                    continue;
                } else {
                    EntityItem item = player.entityDropItem(stackAt, 0.0F);
                    item.setPickupDelay(90);
                }
                player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
            }
        }

        if (MixinConfig.GaiaIIIenabledBaubles){
            BaublesDisarmInventory(player);
        }

    }


    @Unique
    public void BaublesDisarmInventory(EntityPlayer player) {
        IItemHandler baubles = BaublesApi.getBaublesHandler(player);
        for (int i = 0; i < baubles.getSlots(); i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (!match(stack)) {
                if (stack.getItem() instanceof Magnetic magnetic && magnetic.isMagnetic(stack)) {
                    continue;
                } else {
                    EntityItem item = player.entityDropItem(stack, 0.0F);
                    item.setPickupDelay(90);
                }
                BaublesApi.getBaublesHandler(player).setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }


    /**
     * @author sddsd2332
     * @reason 修改盖亚三的生成检查 同时检查背包和饰品栏 如果物品类型是磁吸的 则跳过该物品
     */
    @Overwrite(remap = false)
    private static boolean check(EntityPlayer player) {
        if (player.isCreative()) {
            return true;
        } else if (!ConfigHandler.GAIA_DISARM){ //缴械是关闭的情况下 ,不应该继续检查背包
            return true;
        }else if (!match(player.getHeldItemMainhand())) {
            return false;
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack stackAt = player.inventory.getStackInSlot(i);
                if (stackAt.isEmpty()) {
                    continue;
                }
                if (!match(stackAt)) {
                    if (stackAt.getItem() instanceof Magnetic magnetic && magnetic.isMagnetic(stackAt)) {
                        continue;
                    }
                    return false;
                }
            }

            if (MixinConfig.GaiaIIIenabledBaubles) {
                IItemHandler baubles = BaublesApi.getBaublesHandler(player);
                for (int i = 0; i < baubles.getSlots(); i++) {
                    ItemStack stackAt = baubles.getStackInSlot(i);
                    if (stackAt.isEmpty()) {
                        continue;
                    }
                    if (!match(stackAt)) {
                        if (stackAt.getItem() instanceof Magnetic magnetic && magnetic.isMagnetic(stackAt)) {
                            continue;
                        }
                        return false;
                    }
                }
            }
            return true;
        }
    }

    @Unique
    private static final Map<Item, Boolean> ITEMS_CACHE = new HashMap<>();


    /**
     * @author sddsd2332
     * @reason 扩展白名单，现在可以获取modid来添加白名单
     */
    @Overwrite(remap = false)
    public static boolean match(ItemStack stack) {
        String[] whitelist = ConfigHandler.WHITELIST;
        for (String s : whitelist) {
            ItemStack compared = parseItems(s);
            compared.setCount(stack.getCount());
            if (stack.areItemStacksEqual(stack, compared)) {
                return true;
            }
        }
        return ITEMS_CACHE.computeIfAbsent(stack.getItem(), item -> MixinConfig.GaiaIIImatchModId.contains(item.delegate.name().getNamespace()));
    }


    @Shadow(remap = false)
    private static ItemStack parseItems(String str) {
        String[] entry = str.replace(" ", "").split(":");
        int meta = entry.length > 2 ? Integer.valueOf(entry[2]) : 0;
        ItemStack stack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(entry[0], entry[1])), 1, meta);
        return stack;
    }
}
