package mekanism.generators.common.block.states;

import mekanism.common.base.IBlockType;
import mekanism.common.block.states.BlockStateFacing;
import mekanism.common.block.states.BlockStateUtils;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.LangUtils;
import mekanism.generators.common.GeneratorsBlocks;
import mekanism.generators.common.MekanismGenerators;
import mekanism.generators.common.block.BlockGenerator;
import mekanism.generators.common.tile.*;
import mekanism.generators.common.tile.turbine.*;
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
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockStateGenerator extends ExtendedBlockState {

    public static final PropertyBool activeProperty = PropertyBool.create("active");

    public BlockStateGenerator(BlockGenerator block, PropertyEnum<?> typeProperty) {
        super(block, new IProperty[]{BlockStateFacing.facingProperty, typeProperty, activeProperty}, new IUnlistedProperty[]{});
    }

    public enum GeneratorBlock {
        GENERATOR_BLOCK_1;
      //  GENERATOR_BLOCK_2;

        PropertyEnum<GeneratorType> generatorTypeProperty;

        public PropertyEnum<GeneratorType> getProperty() {
            if (generatorTypeProperty == null) {
                generatorTypeProperty = PropertyEnum.create("type", GeneratorType.class, input -> input != null && input.blockType == this);
            }
            return generatorTypeProperty;
        }

        public Block getBlock() {
            return switch (this) {
                case GENERATOR_BLOCK_1 -> GeneratorsBlocks.Generator;
              //  case GENERATOR_BLOCK_2 -> GeneratorsBlocks.Generator2;
            };
        }
    }

    public enum GeneratorType implements IStringSerializable, IBlockType {
        HEAT_GENERATOR(GeneratorBlock.GENERATOR_BLOCK_1, 0, "HeatGenerator", 0, 160000, TileEntityHeatGenerator::new, true, Plane.HORIZONTAL, false, true),
        SOLAR_GENERATOR(GeneratorBlock.GENERATOR_BLOCK_1, 1, "SolarGenerator", 1, 96000, TileEntitySolarGenerator::new, true, Plane.HORIZONTAL, false),
        GAS_GENERATOR(GeneratorBlock.GENERATOR_BLOCK_1, 3, "GasGenerator", 3, -1/*uses config, set after generators config loaded*/, TileEntityGasGenerator::new, true, Plane.HORIZONTAL, false, true),
        BIO_GENERATOR(GeneratorBlock.GENERATOR_BLOCK_1, 4, "BioGenerator", 4, 160000, TileEntityBioGenerator::new, true, Plane.HORIZONTAL, false, true),
        ADVANCED_SOLAR_GENERATOR(GeneratorBlock.GENERATOR_BLOCK_1, 5, "AdvancedSolarGenerator", 1, 200000, TileEntityAdvancedSolarGenerator::new, true, Plane.HORIZONTAL, false),
        WIND_GENERATOR(GeneratorBlock.GENERATOR_BLOCK_1, 6, "WindGenerator", 5, 200000, TileEntityWindGenerator::new, true, Plane.HORIZONTAL, false),
        TURBINE_ROTOR(GeneratorBlock.GENERATOR_BLOCK_1, 7, "TurbineRotor", -1, -1, TileEntityTurbineRotor::new, false, BlockStateUtils.NO_ROTATION, false),
        ROTATIONAL_COMPLEX(GeneratorBlock.GENERATOR_BLOCK_1, 8, "RotationalComplex", -1, -1, TileEntityRotationalComplex::new, false, BlockStateUtils.NO_ROTATION, false),
        ELECTROMAGNETIC_COIL(GeneratorBlock.GENERATOR_BLOCK_1, 9, "ElectromagneticCoil", -1, -1, TileEntityElectromagneticCoil::new, false, BlockStateUtils.NO_ROTATION, false),
        TURBINE_CASING(GeneratorBlock.GENERATOR_BLOCK_1, 10, "TurbineCasing", -1, -1, TileEntityTurbineCasing::new, false, BlockStateUtils.NO_ROTATION, false),
        TURBINE_VALVE(GeneratorBlock.GENERATOR_BLOCK_1, 11, "TurbineValve", -1, -1, TileEntityTurbineValve::new, false, BlockStateUtils.NO_ROTATION, false, true),
        TURBINE_VENT(GeneratorBlock.GENERATOR_BLOCK_1, 12, "TurbineVent", -1, -1, TileEntityTurbineVent::new, false, BlockStateUtils.NO_ROTATION, false),
        SATURATING_CONDENSER(GeneratorBlock.GENERATOR_BLOCK_1, 13, "SaturatingCondenser", -1, -1, TileEntitySaturatingCondenser::new, false, BlockStateUtils.NO_ROTATION, false),
       /* FISSION_REACTOR_CASING(GeneratorBlock.GENERATOR_BLOCK_2,0,"fission_reactor_casing",-1,-1, TileEntityFissionCasing::new,false,BlockStateUtils.NO_ROTATION, false),
        FISSION_REACTOR_PORT(GeneratorBlock.GENERATOR_BLOCK_2,1,"fission_reactor_port",-1,-1, TileEntityFissionValve::new,false,BlockStateUtils.NO_ROTATION, false),
        FISSION_FUEL_ASSEMBLY(GeneratorBlock.GENERATOR_BLOCK_2,2,"fission_fuel_assembly",-1,-1, TileEntityFissionFuelAssembly::new,false,BlockStateUtils.NO_ROTATION, false),
        CONTROL_ROD_ASSEMBLY(GeneratorBlock.GENERATOR_BLOCK_2,3,"control_rod_assembly",-1,-1, TileEntityControlRodAssembly::new,false,BlockStateUtils.NO_ROTATION, false)*/;


        private static final List<GeneratorType> GENERATORS_FOR_CONFIG;

        static {
            GENERATORS_FOR_CONFIG = new ArrayList<>();

            for (GeneratorType type : GeneratorType.values()) {
                if (type.ordinal() <= 5) {
                    GENERATORS_FOR_CONFIG.add(type);
                }
            }
        }

        public GeneratorBlock blockType;
        public int meta;
        public String blockName;
        public int guiId;
        public double maxEnergy;
        public Supplier<TileEntity> tileEntitySupplier;
        public boolean hasModel;
        public Predicate<EnumFacing> facingPredicate;
        public boolean activable;
        public boolean hasRedstoneOutput;

        GeneratorType(GeneratorBlock block, int m, String name, int gui, double energy, Supplier<TileEntity> tileClass, boolean model, Predicate<EnumFacing> predicate,
                      boolean hasActiveTexture) {
            this(block, m, name, gui, energy, tileClass, model, predicate, hasActiveTexture, false);
        }

        GeneratorType(GeneratorBlock block, int m, String name, int gui, double energy, Supplier<TileEntity> tileClass, boolean model, Predicate<EnumFacing> predicate,
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

        public static List<GeneratorType> getGeneratorsForConfig() {
            return GENERATORS_FOR_CONFIG;
        }

        public static GeneratorType get(IBlockState state) {
            if (state.getBlock() instanceof BlockGenerator generator) {
                return state.getValue(generator.getTypeProperty());
            }
            return null;
        }

        public static GeneratorType get(Block block, int meta) {
            if (block instanceof BlockGenerator generator) {
                return get(generator.getGeneratorBlock(), meta);
            }
            return null;
        }

        public static GeneratorType get(GeneratorBlock block, int meta) {
            for (GeneratorType type : values()) {
                if (type.meta == meta && type.blockType == block) {
                    return type;
                }
            }
            return null;
        }

        public static GeneratorType get(ItemStack stack) {
            return get(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
        }

        @Override
        public String getBlockName() {
            return blockName;
        }

        @Override
        public boolean isEnabled() {
            if (meta > WIND_GENERATOR.meta) {
                return true;
            }
            return MekanismConfig.current().generators.generatorsManager.isEnabled(this);
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
            return new ItemStack(GeneratorsBlocks.Generator, 1, meta);
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

    public static class GeneratorBlockStateMapper extends StateMapperBase {

        @Nonnull
        @Override
        protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
            BlockGenerator block = (BlockGenerator) state.getBlock();
            GeneratorType type = state.getValue(block.getTypeProperty());
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
            ResourceLocation baseLocation = new ResourceLocation(MekanismGenerators.MODID, nameOverride != null ? nameOverride : type.getName());

            return new ModelResourceLocation(baseLocation, builder.toString());
        }
    }
}
