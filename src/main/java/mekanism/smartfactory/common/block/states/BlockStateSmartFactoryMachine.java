package mekanism.smartfactory.common.block.states;

import mekanism.common.base.IBlockType;
import mekanism.common.block.states.BlockStateFacing;
import mekanism.common.block.states.BlockStateUtils;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.LangUtils;
import mekanism.smartfactory.common.MekanismSmartFactory;
import mekanism.smartfactory.common.SmartFactoryBlocks;
import mekanism.smartfactory.common.block.BlockSmartFactoryMachine;
import mekanism.smartfactory.common.tile.machine.TileEntityAEEnergizedSmelter;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockStateSmartFactoryMachine extends ExtendedBlockState {

    public static final PropertyBool activeProperty = PropertyBool.create("active");


    public BlockStateSmartFactoryMachine(BlockSmartFactoryMachine block, PropertyEnum<?> typeProperty) {
        super(block, new IProperty[]{BlockStateFacing.facingProperty, typeProperty, activeProperty}, new IUnlistedProperty[]{});
    }

    public enum SmartFactoryMachineBlock {
        SMART_FACTORY_MACHINE_BLOCK_1;

        PropertyEnum<SmartFactoryMachineType> machineTypeProperty;

        public PropertyEnum<SmartFactoryMachineType> getProperty() {
            if (machineTypeProperty == null) {
                machineTypeProperty = PropertyEnum.create("type", SmartFactoryMachineType.class, input -> input != null && input.typeBlock == this && input.isValidMachine());
            }
            return machineTypeProperty;
        }

        public Block getBlock() {
            return switch (this) {
                case SMART_FACTORY_MACHINE_BLOCK_1 -> SmartFactoryBlocks.SmartFactoryMachine;
            };
        }
    }

    public enum SmartFactoryMachineType implements IStringSerializable, IBlockType {
        AE_ENERGIZED_SMELTER(SmartFactoryMachineBlock.SMART_FACTORY_MACHINE_BLOCK_1, 0, "AEEnergizedSmelter", 0, TileEntityAEEnergizedSmelter::new, true, false, true, EnumFacing.Plane.HORIZONTAL, true);

        private static final List<SmartFactoryMachineType> VALID_MACHINES = new ArrayList<>();

        static {
            Arrays.stream(SmartFactoryMachineType.values()).filter(SmartFactoryMachineType::isValidMachine).forEach(VALID_MACHINES::add);
        }

        public SmartFactoryMachineBlock typeBlock;
        public int meta;
        public String blockName;
        public int guiId;
        public Supplier<TileEntity> tileEntitySupplier;
        public boolean isElectric;
        public boolean hasModel;
        public boolean supportsUpgrades;
        public Predicate<EnumFacing> facingPredicate;
        public boolean activable;

        SmartFactoryMachineType(SmartFactoryMachineBlock block, int m, String name, int gui, Supplier<TileEntity> tileClass, boolean electric, boolean model, boolean upgrades, Predicate<EnumFacing> predicate, boolean hasActiveTexture) {
            typeBlock = block;
            meta = m;
            blockName = name;
            guiId = gui;
            tileEntitySupplier = tileClass;
            isElectric = electric;
            hasModel = model;
            supportsUpgrades = upgrades;
            facingPredicate = predicate;
            activable = hasActiveTexture;
        }

        public static List<SmartFactoryMachineType> getValidMachines() {
            return VALID_MACHINES;
        }

        public static SmartFactoryMachineType get(Block block, int meta) {
            if (block instanceof BlockSmartFactoryMachine) {
                return get(((BlockSmartFactoryMachine) block).getMachineBlock(), meta);
            }
            return null;
        }

        public static SmartFactoryMachineType get(SmartFactoryMachineBlock block, int meta) {
            for (SmartFactoryMachineType type : values()) {
                if (type.meta == meta && type.typeBlock == block) {
                    return type;
                }
            }
            return null;
        }

        public static SmartFactoryMachineType get(ItemStack stack) {
            return get(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
        }

        @Override
        public String getBlockName() {
            return blockName;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        public boolean isValidMachine() {
            return true;
        }


        public TileEntity create() {
            return this.tileEntitySupplier != null ? this.tileEntitySupplier.get() : null;
        }

        public double getUsage() {
            return switch (this) {
                case AE_ENERGIZED_SMELTER -> MekanismConfig.current().usage.energizedSmelter.val();
                default -> 0;
            };
        }

        private double getConfigStorage() {
            return switch (this) {
                case AE_ENERGIZED_SMELTER -> MekanismConfig.current().storage.energizedSmelter.val();
                default -> 400 * getUsage();
            };
        }

        public double getStorage() {
            return Math.max(getConfigStorage(), getUsage());
        }

        public String getDescription() {
            return LangUtils.localize("tooltip." + blockName);
        }

        public ItemStack getStack() {
            return new ItemStack(SmartFactoryBlocks.SmartFactoryMachine, 1, meta);
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Override
        public String toString() {
            return getName();
        }

        public boolean canRotateTo(EnumFacing side) {
            return facingPredicate.test(side);
        }

        public boolean hasRotations() {
            return !facingPredicate.equals(BlockStateUtils.NO_ROTATION);
        }

        public boolean hasActiveTexture() {
            return activable;
        }
    }

    public static class SmartFactoryMachineBlockStateMapper extends StateMapperBase {

        @Nonnull
        @Override
        protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
            BlockSmartFactoryMachine block = (BlockSmartFactoryMachine) state.getBlock();
            SmartFactoryMachineType type = state.getValue(block.getTypeProperty());
            StringBuilder builder = new StringBuilder();


            if (type.hasActiveTexture()) {
                builder.append(activeProperty.getName());
                builder.append("=");
                builder.append(state.getValue(activeProperty));
            }

            if (type.hasRotations()) {
                EnumFacing facing = state.getValue(BlockStateFacing.facingProperty);
                if (!type.canRotateTo(facing)) {
                    facing = EnumFacing.NORTH;
                }
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append(BlockStateFacing.facingProperty.getName());
                builder.append("=");
                builder.append(facing.getName());
            }

            if (builder.length() == 0) {
                builder.append("normal");
            }
            ResourceLocation baseLocation = new ResourceLocation(MekanismSmartFactory.MODID, type.getName());
            return new ModelResourceLocation(baseLocation, builder.toString());
        }
    }
}
