package mekanism.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import mekanism.api.EnumColor;
import mekanism.api.energy.IEnergizedItem;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.api.gear.Magnetic;
import mekanism.api.gear.ModuleData;
import mekanism.client.MekKeyHandler;
import mekanism.client.MekanismKeyHandler;
import mekanism.common.MekanismModules;
import mekanism.common.OreDictCache;
import mekanism.common.block.BlockBounding;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.IModuleContainerItem;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.content.gear.mekatool.ModuleAttackAmplificationUnit;
import mekanism.common.content.gear.mekatool.ModuleExcavationEscalationUnit;
import mekanism.common.content.gear.mekatool.ModuleTeleportationUnit;
import mekanism.common.content.gear.mekatool.ModuleVeinMiningUnit;
import mekanism.common.content.gear.shared.ModuleEnergyUnit;
import mekanism.common.entity.EntityMeka;
import mekanism.common.item.interfaces.IModeItem;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class ItemMekaTool extends ItemEnergized implements IModuleContainerItem, IModeItem, Magnetic {

    private final Multimap<String, AttributeModifier> attributes;

    public ItemMekaTool() {
        super(MekanismConfig.current().general.toolBatteryCapacity.val());
        setRarity(EnumRarity.EPIC);
        setMaxStackSize(1);
        setNoRepair();
        ImmutableMultimap.Builder<String, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4D, 0));
        this.attributes = builder.build();
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tabs, @Nonnull NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tabs)) {
            return;
        }
        ItemStack discharged = new ItemStack(this);
        list.add(discharged);
        ItemStack charged = new ItemStack(this);
        setEnergy(charged, ((IEnergizedItem) charged.getItem()).getMaxEnergy(charged));
        list.add(charged);
        ItemStack FullStack = new ItemStack(this);
        setAllModule(FullStack);
        setEnergy(FullStack, ((IEnergizedItem) FullStack.getItem()).getMaxEnergy(FullStack));
        list.add(FullStack);
    }


    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        //Allow harvesting everything, things that are unbreakable are caught elsewhere
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.sneakKey)) {
            addModuleDetails(stack, tooltip);
        } else {
            tooltip.add(EnumColor.AQUA + LangUtils.localize("tooltip.storedEnergy") + ": " + EnumColor.GREY + MekanismUtils.getEnergyDisplay(getEnergy(stack), getMaxEnergy(stack)));
            tooltip.add(LangUtils.localize("tooltip.hold") + " " + EnumColor.INDIGO + GameSettings.getKeyDisplayString(MekanismKeyHandler.sneakKey.getKeyCode()) + EnumColor.GREY + " " + LangUtils.localize("tooltip.forDetails") + ".");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return !stack.isEmpty() && super.hasEffect(stack) && IModuleContainerItem.hasOtherEnchants(stack);
    }


    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        for (Module<?> module : getModules(player.getHeldItem(hand))) {
            if (module.isEnabled()) {
                EnumActionResult result = onModuleUse(module, player, world, pos, hand, side, hitX, hitY, hitZ);
                if (result != EnumActionResult.PASS) {
                    return result;
                }
            }
        }
        return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
    }


    private <MODULE extends ICustomModule<MODULE>> EnumActionResult onModuleUse(IModule<MODULE> module, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return module.getCustomInstance().onItemUse(module, player, world, pos, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
        for (Module<?> module : getModules(player.getHeldItem(hand))) {
            if (module.isEnabled()) {
                return onModuleInteract(module, player, entity, hand);
            }
        }
        return super.itemInteractionForEntity(stack, player, entity, hand);
    }


    private <MODULE extends ICustomModule<MODULE>> boolean onModuleInteract(IModule<MODULE> module, @Nonnull EntityPlayer player, @Nonnull EntityLivingBase entity, @Nonnull EnumHand hand) {
        return module.getCustomInstance().onInteract(module, player, entity, hand);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        IEnergizedItem energyContainer = this;
        if (energyContainer == null) {
            return 0;
        }

        //Use raw hardness to get the best guess of if it is zero or not
        double energyRequired = getDestroyEnergy(stack, state.getBlock().blockHardness, isModuleEnabled(stack, MekanismModules.SILK_TOUCH_UNIT));
        double energyAvailable = energyContainer.extract(stack, energyRequired, false);
        if (energyAvailable > (energyRequired)) {
            //If we can't extract all the energy we need to break it go at base speed reduced by how much we actually have available
            return (float) (MekanismConfig.current().meka.mekaToolBaseEfficiency.val() * (energyAvailable / (energyRequired)));
        }
        IModule<ModuleExcavationEscalationUnit> module = getModule(stack, MekanismModules.EXCAVATION_ESCALATION_UNIT);
        return module == null || !module.isEnabled() ? MekanismConfig.current().meka.mekaToolBaseEfficiency.val() : module.getCustomInstance().getEfficiency();
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {
        IEnergizedItem energyContainer = this;
        if (energyContainer != null) {
            double energyRequired = getDestroyEnergy(stack, state.getBlock().getBlockHardness(state, world, pos), isModuleEnabled(stack, MekanismModules.SILK_TOUCH_UNIT));
            double extractedEnergy = energyContainer.extract(stack, energyRequired, true);
            if (extractedEnergy == (energyRequired) || entity instanceof EntityPlayer player && player.isCreative()) {
                //Only disarm tripwires if we had all the energy we tried to use (or are creative). Otherwise, treat it as if we may have failed to disarm it
                if (state.getBlock() == Blocks.TRIPWIRE && !state.getValue(BlockTripWire.DISARMED) && isModuleEnabled(stack, MekanismModules.SHEARING_UNIT)) {
                    world.setBlockState(pos, state.withProperty(BlockTripWire.DISARMED, Boolean.valueOf(true)), Constants.BlockFlags.NO_RERENDER);
                }
            }

        }
        return true;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        IEnergizedItem energyContainer = this;
        double energy = energyContainer == null ? 0 : energyContainer.getEnergy(stack);
        double energyCost = 0;
        int minDamage = MekanismConfig.current().meka.mekaToolBaseDamage.val(), maxDamage = minDamage;
        IModule<ModuleAttackAmplificationUnit> attackAmplificationUnit = getModule(stack, MekanismModules.ATTACK_AMPLIFICATION_UNIT);
        if (attackAmplificationUnit != null && attackAmplificationUnit.isEnabled()) {
            maxDamage = attackAmplificationUnit.getCustomInstance().getDamage();
            if (maxDamage > minDamage) {
                energyCost = MekanismConfig.current().meka.mekaToolEnergyUsageWeapon.val() * ((maxDamage - minDamage) / 4F);
            }
            minDamage = Math.min(minDamage, maxDamage);
        }
        int damageDifference = maxDamage - minDamage;
        //If we don't have enough power use it at a reduced power level
        double percent = 1;
        if (energy > (energyCost)) {
            if (energyCost == 0) {
                energyCost = 1;
            }
            percent = energy / (energyCost);
        }
        float damage = (float) (minDamage + damageDifference * percent);
        if (attacker instanceof EntityPlayer player) {
            target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
        } else {
            target.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage);
        }
        if (energyContainer != null && energy != 0) {
            energyContainer.extract(stack, energyCost, true);
        }
        return false;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        if (player.world.isRemote || player.isCreative()) {
            return super.onBlockStartBreak(itemstack, pos, player);
        }
        IEnergizedItem energyContainer = this;
        if (energyContainer != null) {
            World world = player.world;
            IBlockState state = world.getBlockState(pos);
            boolean silk = isModuleEnabled(itemstack, MekanismModules.SILK_TOUCH_UNIT);
            double energyRequired = getDestroyEnergy(itemstack, state.getBlock().getBlockHardness(state, world, pos), silk);
            if (energyContainer.extract(itemstack, energyRequired, false) >= (energyRequired)) {
                 IModule<ModuleVeinMiningUnit> veinMiningUnit = getModule(itemstack, MekanismModules.VEIN_MINING_UNIT);
                //Even though we now handle breaking bounding blocks properly, don't allow vein mining them
                if (veinMiningUnit != null && veinMiningUnit.isEnabled() && !(state.getBlock() instanceof BlockBounding)) {
                    Block block = state.getBlock();
                    if (block == Blocks.LIT_REDSTONE_ORE) {
                        block = Blocks.REDSTONE_ORE;
                    }
                    RayTraceResult raytrace = doRayTrace(state, pos, player);
                    ItemStack stack = block.getPickBlock(state, raytrace, player.world, pos, player);
                    List<String> names = OreDictCache.getOreDictName(stack);
                    boolean isOre = names.stream().anyMatch(s -> s.startsWith("ore") || s.equals("logWood"));
                    if (isOre || veinMiningUnit.getCustomInstance().isExtended()) {
                        double baseDestroyEnergy = getDestroyEnergy(silk);
                        Set<BlockPos> found = ModuleVeinMiningUnit.findPositions(state, pos, world, isOre ? -1 : veinMiningUnit.getCustomInstance().getExcavationRange());
                        MekanismUtils.veinMineArea(energyContainer, world, pos, (EntityPlayerMP) player, itemstack, this, found, isModuleEnabled(stack, MekanismModules.SHEARING_UNIT), hardness -> getDestroyEnergy(baseDestroyEnergy, hardness), distance -> 0.5 * Math.pow(distance, isOre ? 1.5 : 2), state);
                    }
                }
            }
        }
        return super.onBlockStartBreak(itemstack, pos, player);
    }


    private RayTraceResult doRayTrace(IBlockState state, BlockPos pos, EntityPlayer player) {
        Vec3d positionEyes = player.getPositionEyes(1.0F);
        Vec3d playerLook = player.getLook(1.0F);
        double blockReachDistance = player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        Vec3d maxReach = positionEyes.add(playerLook.x * blockReachDistance, playerLook.y * blockReachDistance, playerLook.z * blockReachDistance);
        RayTraceResult res = state.collisionRayTrace(player.world, pos, playerLook, maxReach);
        //noinspection ConstantConditions - idea thinks it's nonnull due to package level annotations, but it's not
        return res != null ? res : new RayTraceResult(RayTraceResult.Type.MISS, Vec3d.ZERO, EnumFacing.UP, pos);
    }

    private double getDestroyEnergy(boolean silk) {
        return silk ? MekanismConfig.current().meka.mekaToolEnergyUsageSilk.val() : MekanismConfig.current().meka.mekaToolEnergyUsage.val();
    }

    private double getDestroyEnergy(ItemStack itemStack, float hardness, boolean silk) {
        return getDestroyEnergy(getDestroyEnergy(itemStack, silk), hardness);
    }

    private double getDestroyEnergy(double baseDestroyEnergy, float hardness) {
        return hardness == 0 ? baseDestroyEnergy / (2) : baseDestroyEnergy;
    }


    private double getDestroyEnergy(ItemStack itemStack, boolean silk) {
        double destroyEnergy = getDestroyEnergy(silk);
        IModule<ModuleExcavationEscalationUnit> module = getModule(itemStack, MekanismModules.EXCAVATION_ESCALATION_UNIT);
        float efficiency = module == null || !module.isEnabled() ? MekanismConfig.current().meka.mekaToolBaseEfficiency.val() : module.getCustomInstance().getEfficiency();
        return destroyEnergy * (efficiency);
    }


    @Nonnull
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return slot == EntityEquipmentSlot.MAINHAND ? attributes : super.getAttributeModifiers(slot, stack);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            IModule<ModuleTeleportationUnit> module = getModule(stack, MekanismModules.TELEPORTATION_UNIT);
            if (module != null && module.isEnabled()) {
                RayTraceResult result = MekanismUtils.rayTrace(world, player, MekanismConfig.current().meka.mekaToolMaxTeleportReach.val());
                //If we don't require a block target or are not a miss, allow teleporting
                if (!module.getCustomInstance().requiresBlockTarget() || result.typeOfHit != RayTraceResult.Type.MISS) {
                    BlockPos pos = result.getBlockPos();
                    // make sure we fit
                    if (isValidDestinationBlock(world, pos.up()) && isValidDestinationBlock(world, pos.up(2))) {
                        double distance = player.getDistanceSq(pos.getX(), pos.getY(), pos.getZ());
                        if (distance < 5) {
                            return new ActionResult<>(EnumActionResult.PASS, stack);
                        }
                        IEnergizedItem energyContainer = this;
                        double energyNeeded = MekanismConfig.current().meka.mekaToolEnergyUsageTeleport.val() * (distance / 10D);
                        if (energyContainer == null || energyContainer.getEnergy(stack) < (energyNeeded)) {
                            return new ActionResult<>(EnumActionResult.FAIL, stack);
                        }
                        energyContainer.extract(stack, energyNeeded, true);
                        if (player.isRiding()) {
                            player.dismountRidingEntity();
                        }
                        player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
                        player.fallDistance = 0.0F;
                        //  Mekanism.packetHandler.sendToAllTracking(new PacketPortalFX(pos.above()), world, pos); 传送粒子
                        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
                    }
                }
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    private boolean isValidDestinationBlock(World world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        //Allow teleporting into air or fluids
        return blockState.getBlock().isAir(blockState, world, pos) || blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() instanceof IFluidBlock;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean supportsSlotType(ItemStack stack, @Nonnull EntityEquipmentSlot slotType) {
        return IModeItem.super.supportsSlotType(stack, slotType) && getModules(stack).stream().anyMatch(Module::handlesModeChange);
    }

    @Override
    public void changeMode(@NotNull EntityPlayer player, @NotNull ItemStack stack, int shift, boolean displayChangeMessage) {
        for (Module<?> module : getModules(stack)) {
            if (module.handlesModeChange()) {
                module.changeMode(player, stack, shift, displayChangeMessage);
                return;
            }
        }
    }

    @Override
    public double getMaxEnergy(ItemStack stack) {
        IModule<ModuleEnergyUnit> module = getModule(stack, MekanismModules.ENERGY_UNIT);
        return module == null ? MekanismConfig.current().meka.mekaToolBaseEnergyCapacity.val() : module.getCustomInstance().getEnergyCapacity(module);
    }


    @Override
    public double getMaxTransfer(ItemStack stack) {
        IModule<ModuleEnergyUnit> module = getModule(stack, MekanismModules.ENERGY_UNIT);
        return module == null ? MekanismConfig.current().meka.mekaToolBaseChargeRate.val() : module.getCustomInstance().getChargeRate(module);
    }


    @Override
    public boolean isMagnetic(ItemStack stack) {
        return isModuleEnabled(stack,MekanismModules.MAGNETIC_UNIT);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        EntityItem item = new EntityMeka(world, location, itemstack);

        if (isModuleEnabled(itemstack, MekanismModules.MAGNETIC_UNIT)) {
            item.setNoPickupDelay();
        }
        return item;
    }
}
