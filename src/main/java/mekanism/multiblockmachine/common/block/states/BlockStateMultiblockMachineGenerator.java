package mekanism.multiblockmachine.common.block.states;

import mekanism.common.base.IBlockType;
import mekanism.common.block.states.BlockStateFacing;
import mekanism.common.block.states.BlockStateUtils;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.LangUtils;
import mekanism.multiblockmachine.common.MekanismMultiblockMachine;
import mekanism.multiblockmachine.common.MultiblockMachineBlocks;
import mekanism.multiblockmachine.common.block.BlockMultiblockMachineGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeGasGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeHeatGenerator;
import mekanism.multiblockmachine.common.tile.generator.TileEntityLargeWindGenerator;
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
import net.minecraft.util.EnumFacing.Plane;
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

public class BlockStateMultiblockMachineGenerator extends ExtendedBlockState {
    public static final PropertyBool activeProperty = PropertyBool.create("active");

    public BlockStateMultiblockMachineGenerator(BlockMultiblockMachineGenerator block, PropertyEnum<?> typeProperty) {
        super(block, new IProperty[]{BlockStateFacing.facingProperty, typeProperty, activeProperty}, new IUnlistedProperty[]{});
    }

    public enum MultiblockMachineGeneratorBlock {
        MULTIBLOCK_MACHINE_GENERATOR_BLOCK_1;
        PropertyEnum<MultiblockMachineGeneratorType> generatorTypeProperty;

        public PropertyEnum<MultiblockMachineGeneratorType> getProperty() {
            if (generatorTypeProperty == null) {
                generatorTypeProperty = PropertyEnum.create("type", MultiblockMachineGeneratorType.class, input -> input != null && input.blockType == this);
            }
            return generatorTypeProperty;
        }


        public Block getBlock() {
            if (this == MultiblockMachineGeneratorBlock.MULTIBLOCK_MACHINE_GENERATOR_BLOCK_1) {
                return MultiblockMachineBlocks.MultiblockGenerator;
            }
            return null;
        }
    }

    public enum MultiblockMachineGeneratorType implements IStringSerializable, IBlockType {
        LARGE_WIND_GENERATOR(MultiblockMachineGeneratorBlock.MULTIBLOCK_MACHINE_GENERATOR_BLOCK_1, 0, "LargeWindGenerator", 0, 200000, TileEntityLargeWindGenerator::new, true, Plane.HORIZONTAL, false),
        LARGE_HEAT_GENERATOR(MultiblockMachineGeneratorBlock.MULTIBLOCK_MACHINE_GENERATOR_BLOCK_1, 1, "LargeHeatGenerator", 1, 160000, TileEntityLargeHeatGenerator::new, true, Plane.HORIZONTAL, false),
        LARGE_GAS_GENERATOR(MultiblockMachineGeneratorBlock.MULTIBLOCK_MACHINE_GENERATOR_BLOCK_1, 2, "LargeGasGenerator", 2, 160000, TileEntityLargeGasGenerator::new, true, Plane.HORIZONTAL, false);


        public MultiblockMachineGeneratorBlock blockType;
        public int meta;
        public String blockName;
        public int guiId;
        public double maxEnergy;
        public Supplier<TileEntity> tileEntitySupplier;
        public boolean hasModel;
        public Predicate<EnumFacing> facingPredicate;
        public boolean activable;
        public boolean hasRedstoneOutput;

        MultiblockMachineGeneratorType(MultiblockMachineGeneratorBlock block, int m, String name, int gui, double energy, Supplier<TileEntity> tileClass, boolean model, Predicate<EnumFacing> predicate,
                                       boolean hasActiveTexture) {
            this(block, m, name, gui, energy, tileClass, model, predicate, hasActiveTexture, false);
        }

        MultiblockMachineGeneratorType(MultiblockMachineGeneratorBlock block, int m, String name, int gui, double energy, Supplier<TileEntity> tileClass, boolean model, Predicate<EnumFacing> predicate,
                                       boolean hasActiveTexture, boolean hasRedstoneOutput) {
            blockType = block;
            meta = m;
            blockName = name;
            guiId = gui;
            maxEnergy = energy;
            tileEntitySupplier = tileClass;
            hasModel = model;
            facingPredicate = predicate;
            activable = hasActiveTexture;
            this.hasRedstoneOutput = hasRedstoneOutput;
        }

        private static final List<MultiblockMachineGeneratorType> GENERATORS_FOR_CONFIG  = new ArrayList<>();

        static {
            Arrays.stream(MultiblockMachineGeneratorType.values()).filter(MultiblockMachineGeneratorType :: isValidMachine).forEach(GENERATORS_FOR_CONFIG::add);
        }

        public static List<MultiblockMachineGeneratorType> getGeneratorsForConfig() {
            return GENERATORS_FOR_CONFIG;
        }

        public static MultiblockMachineGeneratorType get(IBlockState state) {
            if (state.getBlock() instanceof BlockMultiblockMachineGenerator) {
                return state.getValue(((BlockMultiblockMachineGenerator) state.getBlock()).getTypeProperty());
            }
            return null;
        }

        public static MultiblockMachineGeneratorType get(Block block, int meta) {
            if (block instanceof BlockMultiblockMachineGenerator) {
                return get(((BlockMultiblockMachineGenerator) block).getGeneratorBlock(), meta);
            }
            return null;
        }

        public static MultiblockMachineGeneratorType get(MultiblockMachineGeneratorBlock block, int meta) {
            for (MultiblockMachineGeneratorType type : values()) {
                if (type.meta == meta && type.blockType == block) {
                    return type;
                }
            }
            return null;
        }

        public static MultiblockMachineGeneratorType get(ItemStack stack) {
            return get(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
        }

        @Override
        public String getBlockName() {
            return blockName;
        }

        @Override
        public boolean isEnabled() {
            return MekanismConfig.current().multiblock.multiblockmachinegeneratorsManager.isEnabled(this);
        }

        public boolean isValidMachine() {
            return true;
        }

        public TileEntity create() {
            return this.tileEntitySupplier != null ? this.tileEntitySupplier.get() : null;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public String getDescription() {
            return LangUtils.localize("tooltip." + blockName);
        }

        public ItemStack getStack() {
            return new ItemStack(MultiblockMachineBlocks.MultiblockGenerator, 1, meta);
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

    public static class MultiblockMachineGeneratorBlockStateMapper extends StateMapperBase {
        @Nonnull
        @Override
        protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
            BlockMultiblockMachineGenerator block = (BlockMultiblockMachineGenerator) state.getBlock();
            MultiblockMachineGeneratorType type = state.getValue(block.getTypeProperty());
            StringBuilder builder = new StringBuilder();
            String nameOverride = null;

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
            ResourceLocation baseLocation = new ResourceLocation(MekanismMultiblockMachine.MODID, nameOverride != null ? nameOverride : type.getName());
            return new ModelResourceLocation(baseLocation, builder.toString());
        }
    }

}
