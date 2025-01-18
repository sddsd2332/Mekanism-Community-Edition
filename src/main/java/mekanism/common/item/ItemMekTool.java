package mekanism.common.item;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mekanism.api.Coord4D;
import mekanism.api.EnumColor;
import mekanism.api.IDisableableEnum;
import mekanism.api.NBTConstants;
import mekanism.api.math.MathUtils;
import mekanism.common.Mekanism;
import mekanism.common.OreDictCache;
import mekanism.common.config.MekanismConfig;
import mekanism.common.item.ItemMekTool.MekToolMode;
import mekanism.common.item.interfaces.IItemHUDProvider;
import mekanism.common.item.interfaces.IRadialModeItem;
import mekanism.common.item.interfaces.IRadialSelectorEnum;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.TextComponentGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.WorldEvents;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BooleanSupplier;

public class ItemMekTool extends ItemEnergized implements IItemHUDProvider, IRadialModeItem<MekToolMode> {
    private Random rand = new Random();

    public ItemMekTool() {
        super(MekanismConfig.current().general.toolBatteryCapacity.val());
        setMaxStackSize(1);
        setRarity(EnumRarity.EPIC);
    }


    @Override
    public boolean canHarvestBlock(@Nonnull IBlockState state, ItemStack stack) {
        return state.getBlock() != Blocks.BEDROCK;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(stack, world, list, flag);
        list.add(LangUtils.localize("tooltip.mode") + ": " + EnumColor.INDIGO + getMode(stack).getModeName());
        list.add(LangUtils.localize("tooltip.efficiency") + ": " + EnumColor.INDIGO + getMode(stack).getEfficiency());
        list.addAll(MekanismUtils.splitTooltip(LangUtils.localize("tooltip.MekTool1"), stack));
        list.addAll(MekanismUtils.splitTooltip(LangUtils.localize("tooltip.MekTool2"), stack));
        list.addAll(MekanismUtils.splitTooltip(LangUtils.localize("tooltip.MekTool3"), stack));
        list.addAll(MekanismUtils.splitTooltip(EnumColor.RED + LangUtils.localize("tooltip.MekTool4"), stack));
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase target, EntityLivingBase attacker) {
        double energy = getEnergy(itemstack);
        int energyCost = MekanismConfig.current().general.toolEnergyUsageWeapon.val();
        int minDamage = MekanismConfig.current().general.toolDamageMin.val();
        int damageDifference = MekanismConfig.current().general.toolDamageMax.val() - minDamage;
        //If we don't have enough power use it at a reduced power level
        double percent = 1;
        if (energy < energyCost && energyCost != 0) {
            percent = energy / energyCost;
        }
        float damage = (float) (minDamage + damageDifference * percent);
        if (attacker instanceof EntityPlayer player) {
            if (energy > getMaxEnergy(itemstack) * 0.9) {
                if (target.getHealth() / target.getMaxHealth() > 0.1) {
                    if (rand.nextDouble() <= 0.1D) {
                        target.setHealth(0.0F);
                        target.getDataManager().set(EntityLivingBase.HEALTH, 0.0F);
                    } else {
                        target.setHealth(0.1F);
                        target.getDataManager().set(EntityLivingBase.HEALTH, 0.1F);
                    }
                } else {
                    target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage * 8096);
                }
            } else if (energy > getMaxEnergy(itemstack) * 0.75) {
                target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage * 4096);
            } else if (energy > getMaxEnergy(itemstack) * 0.5) {
                target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage * 2048);
            } else if (energy > getMaxEnergy(itemstack) * 0.25) {
                target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage * 1024);
            } else {
                target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
            }
        } else {
            if (energy > getMaxEnergy(itemstack) * 0.75) {
                target.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage * 4096);
            } else if (energy > getMaxEnergy(itemstack) * 0.5) {
                target.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage * 2048);
            } else if (energy > getMaxEnergy(itemstack) * 0.25) {
                target.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage * 1024);
            } else {
                target.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage);
            }
        }
        if (energy > 0) {
            setEnergy(itemstack, energy - energyCost);
        }
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack itemstack, IBlockState state) {
        return getEnergy(itemstack) != 0 ? getMode(itemstack).getEfficiency() : 1F;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityliving) {
        setEnergy(itemstack, getEnergy(itemstack) - getDestroyEnergy(itemstack, state.getBlockHardness(world, pos)));
        return true;
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

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        super.onBlockStartBreak(itemstack, pos, player);
        if (!player.world.isRemote && !player.capabilities.isCreativeMode) {
            MekToolMode mode = getMode(itemstack);
            boolean extended = mode == MekToolMode.EXTENDED_VEIN;
            if (extended || mode == MekToolMode.VEIN) {
                IBlockState state = player.world.getBlockState(pos);
                Block block = state.getBlock();
                if (block == Blocks.LIT_REDSTONE_ORE) {
                    block = Blocks.REDSTONE_ORE;
                }
                RayTraceResult raytrace = doRayTrace(state, pos, player);
                ItemStack stack = block.getPickBlock(state, raytrace, player.world, pos, player);
                List<String> names = OreDictCache.getOreDictName(stack);
                boolean isOre = false;
                for (String s : names) {
                    if (s.startsWith("ore") || s.equals("logWood")) {
                        isOre = true;
                        break;
                    }
                }
                if (isOre || extended) {
                    Coord4D orig = new Coord4D(pos, player.world);
                    Set<Coord4D> found = new Finder(player, stack, orig, raytrace, extended ? MekanismConfig.current().general.toolMiningRange.val() : -1).calc();
                    for (Coord4D coord : found) {
                        if (coord.equals(orig)) {
                            continue;
                        }
                        int destroyEnergy = getDestroyEnergy(itemstack, coord.getBlockState(player.world).getBlockHardness(player.world, coord.getPos()));
                        if (getEnergy(itemstack) < destroyEnergy) {
                            continue;
                        }
                        Block block2 = coord.getBlock(player.world);
                        block2.onBlockHarvested(player.world, coord.getPos(), state, player);
                        player.world.playEvent(WorldEvents.BREAK_BLOCK_EFFECTS, coord.getPos(), Block.getStateId(state));
                        block2.dropBlockAsItem(player.world, coord.getPos(), state, 0);
                        player.world.setBlockToAir(coord.getPos());
                        setEnergy(itemstack, getEnergy(itemstack) - destroyEnergy);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }


    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            ItemStack stack = player.getHeldItem(hand);
            int diameter = getMode(stack).getDiameter();
            if (diameter > 0) {
                Block block = world.getBlockState(pos).getBlock();
                if (block == Blocks.DIRT || block == Blocks.GRASS_PATH) {
                    return useItemAs(player, world, pos, side, stack, diameter, this::useHoe);
                } else if (block == Blocks.GRASS) {
                    return useItemAs(player, world, pos, side, stack, diameter, this::useShovel);
                }
            }
        }
        return EnumActionResult.PASS;
    }

    private EnumActionResult useItemAs(EntityPlayer player, World world, BlockPos pos, EnumFacing side, ItemStack stack, int diameter, ItemUseConsumer consumer) {
        double energy = getEnergy(stack);
        int hoeUsage = MekanismConfig.current().general.toolEnergyUsageHoe.val();
        if (energy < hoeUsage || consumer.use(stack, player, world, pos, side) == EnumActionResult.FAIL) {
            //Fail if we don't have enough energy or using the item failed
            return EnumActionResult.FAIL;
        }
        double energyUsed = hoeUsage;
        int radius = (diameter - 1) / 2;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (energyUsed + hoeUsage > energy) {
                    break;
                } else if ((x != 0 || z != 0) && consumer.use(stack, player, world, pos.add(x, 0, z), side) == EnumActionResult.SUCCESS) {
                    //Don't attempt to use it on the source location as it was already done above
                    // If we successfully used it in a spot increment how much energy we used
                    energyUsed += hoeUsage;
                }
            }
        }
        setEnergy(stack, energy - energyUsed);
        return EnumActionResult.SUCCESS;
    }

    private EnumActionResult useHoe(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing) {
        if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return EnumActionResult.FAIL;
        }
        int hook = ForgeEventFactory.onHoeUse(stack, player, world, pos);
        if (hook != 0) {
            return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
        }
        if (facing != EnumFacing.DOWN && world.isAirBlock(pos.up())) {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            IBlockState newState = null;
            if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
                newState = Blocks.FARMLAND.getDefaultState();
            } else if (block == Blocks.DIRT) {
                DirtType type = state.getValue(BlockDirt.VARIANT);
                if (type == DirtType.DIRT) {
                    newState = Blocks.FARMLAND.getDefaultState();
                } else if (type == DirtType.COARSE_DIRT) {
                    newState = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, DirtType.DIRT);
                }
            }
            if (newState != null) {
                setBlock(stack, player, world, pos, newState);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }

    private EnumActionResult useShovel(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing) {
        if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return EnumActionResult.FAIL;
        } else if (facing != EnumFacing.DOWN && world.isAirBlock(pos.up())) {
            Block block = world.getBlockState(pos).getBlock();
            if (block == Blocks.GRASS) {
                setBlock(stack, player, world, pos, Blocks.GRASS_PATH.getDefaultState());
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }

    private void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
        worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, state, 11);
            stack.damageItem(1, player);
        }
    }

    private int getDestroyEnergy(ItemStack itemStack, float hardness) {
        int destroyEnergy = MekanismConfig.current().general.toolEnergyUsage.val() * getMode(itemStack).getEfficiency();
        return hardness == 0 ? destroyEnergy / 2 : destroyEnergy;
    }

    @Override
    public Class<MekToolMode> getModeClass() {
        return MekToolMode.class;
    }

    @Override
    public MekToolMode getModeByIndex(int ordinal) {
        return MekToolMode.byIndexStatic(ordinal);
    }

    @Override
    public MekToolMode getMode(ItemStack stack) {
        return MekToolMode.byIndexStatic(ItemDataUtils.getInt(stack, NBTConstants.MODE));
    }

    @Override
    public void setMode(ItemStack stack, EntityPlayer player, MekToolMode mode) {
        ItemDataUtils.setInt(stack, NBTConstants.MODE, mode.ordinal());
    }


    @Override
    public boolean canSend(ItemStack itemStack) {
        return false;
    }

    @Nonnull
    @Override
    @Deprecated
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multiMap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multiMap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }
        return multiMap;
    }


    @Override
    public void addHUDStrings(List<String> list, EntityPlayer player, ItemStack stack, EntityEquipmentSlot slotType) {
        list.add(LangUtils.localize("tooltip.mode") + ": " + EnumColor.INDIGO + getMode(stack).getModeName());
        list.add(LangUtils.localize("tooltip.efficiency") + ": " + EnumColor.INDIGO + getMode(stack).getEfficiency());
    }

    @Override
    public void changeMode(@NotNull EntityPlayer player, @NotNull ItemStack stack, int shift, boolean displayChangeMessage) {
        MekToolMode mode = getMode(stack);
        MekToolMode newMode = mode.adjust(shift);
        if (mode != newMode) {
            setMode(stack, player, newMode);
            if (displayChangeMessage) {
                player.sendMessage(new TextComponentString(EnumColor.DARK_BLUE + Mekanism.LOG_TAG + " " + EnumColor.GREY + LangUtils.localize("tooltip.modeToggle") + " " + EnumColor.INDIGO + newMode.getModeName() + EnumColor.AQUA + " (" + newMode.getEfficiency() + ")"));
            }
        }
    }

    @Nonnull
    @Override
    public ITextComponent getScrollTextComponent(@Nonnull ItemStack stack) {
        return getMode(stack).getShortText();
    }

    public enum MekToolMode implements IDisableableEnum<MekToolMode>, IRadialSelectorEnum<MekToolMode> {
        NORMAL("normal", 20, 3, () -> true, EnumColor.BRIGHT_GREEN, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "disassembler_normal.png")),
        SLOW("slow", 8, 1, () -> MekanismConfig.current().general.disassemblerSlowMode.val(), EnumColor.BRIGHT_GREEN, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "disassembler_slow.png")),
        FAST("fast", 128, 5, () -> MekanismConfig.current().general.disassemblerFastMode.val(), EnumColor.BRIGHT_GREEN, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "disassembler_fast.png")),
        VEIN("vein", 20, 3, () -> MekanismConfig.current().general.disassemblerVeinMining.val(), EnumColor.BRIGHT_GREEN, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "disassembler_vein.png")),
        EXTENDED_VEIN("extended_vein", 20, 3, () -> MekanismConfig.current().general.disassemblerExtendedMining.val(), EnumColor.BRIGHT_GREEN, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "disassembler_vein.png")),
        OFF("off", 0, 0, () -> true, EnumColor.BRIGHT_GREEN, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "void.png"));


        private static final MekToolMode[] MODES = values();
        private final BooleanSupplier checkEnabled;
        private final String mode;
        private final int efficiency;
        //Must be odd, or zero
        private final int diameter;
        private final ResourceLocation icon;
        private final EnumColor color;

        MekToolMode(String mode, int efficiency, int diameter, BooleanSupplier checkEnabled, EnumColor color, ResourceLocation icon) {
            this.mode = mode;
            this.efficiency = efficiency;
            this.diameter = diameter;
            this.checkEnabled = checkEnabled;
            this.color = color;
            this.icon = icon;
        }

        /**
         * Gets a Mode from its ordinal. NOTE: if this mode is not enabled then it will reset to NORMAL
         */
        public static MekToolMode byIndexStatic(int index) {
            MekToolMode mode = MathUtils.getByIndexMod(MODES, index);
            return mode.isEnabled() ? mode : NORMAL;
        }


        @Override
        public MekToolMode byIndex(int index) {
            return MathUtils.getByIndexMod(MODES, index);
        }


        public int getEfficiency() {
            return efficiency;
        }

        public int getDiameter() {
            return diameter;
        }

        public boolean isEnabled() {
            return checkEnabled.getAsBoolean();
        }

        public String getModeName() {
            return LangUtils.localize("mekanism.tooltip.disassembler." + mode);
        }

        @Override
        public ITextComponent getShortText() {
            return new TextComponentGroup(color.textFormatting).translation("mekanism.tooltip.mektool." + mode);
        }

        @Override
        public EnumColor getColor() {
            return color;
        }

        @Override
        public ResourceLocation getIcon() {
            return icon;
        }
    }

    @FunctionalInterface
    interface ItemUseConsumer {

        //Used to reference useHoe and useShovel via lambda references
        EnumActionResult use(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing);
    }

    public static class Finder {

        public static Map<Block, List<Block>> ignoreBlocks = new Object2ObjectOpenHashMap<>();

        static {
            ignoreBlocks.put(Blocks.REDSTONE_ORE, Arrays.asList(Blocks.REDSTONE_ORE, Blocks.LIT_REDSTONE_ORE));
            ignoreBlocks.put(Blocks.LIT_REDSTONE_ORE, Arrays.asList(Blocks.REDSTONE_ORE, Blocks.LIT_REDSTONE_ORE));
        }

        public final World world;
        public final ItemStack stack;
        public final Coord4D location;
        public final Set<Coord4D> found = new ObjectOpenHashSet<>();
        private final EntityPlayer player;
        private final RayTraceResult rayTraceResult;
        private final Block startBlock;
        private final boolean isWood;
        private final int maxRange;
        private final int maxCount;

        public Finder(EntityPlayer p, ItemStack s, Coord4D loc, RayTraceResult traceResult, int range) {
            player = p;
            world = p.world;
            stack = s;
            location = loc;
            startBlock = loc.getBlock(world);
            rayTraceResult = traceResult;
            isWood = OreDictCache.getOreDictName(stack).contains("logWood");
            maxRange = range;
            maxCount = MekanismConfig.current().general.toolMiningCount.val() - 1;
        }

        public void loop(Coord4D pointer) {
            if (found.contains(pointer) || found.size() > maxCount) {
                return;
            }
            found.add(pointer);
            for (EnumFacing side : EnumFacing.VALUES) {
                Coord4D coord = pointer.offset(side);
                if (maxRange > 0 && location.distanceTo(coord) > maxRange) {
                    continue;
                }
                if (coord.exists(world)) {
                    Block block = coord.getBlock(world);
                    if (checkID(block)) {
                        ItemStack blockStack = block.getPickBlock(coord.getBlockState(world), rayTraceResult, world, coord.getPos(), player);
                        if (ItemHandlerHelper.canItemStacksStack(stack, blockStack) || (block == startBlock && isWood && coord.getBlockMeta(world) % 4 == stack.getItemDamage() % 4)) {
                            loop(coord);
                        }
                    }
                }
            }
        }

        public Set<Coord4D> calc() {
            loop(location);
            return found;
        }

        public boolean checkID(Block b) {
            Block origBlock = location.getBlock(world);
            List<Block> ignored = ignoreBlocks.get(origBlock);
            return ignored == null ? b == origBlock : ignored.contains(b);
        }
    }


}