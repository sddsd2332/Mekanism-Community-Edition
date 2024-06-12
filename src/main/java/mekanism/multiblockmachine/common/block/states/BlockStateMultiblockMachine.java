package mekanism.multiblockmachine.common.block.states;

import mekanism.common.base.IBlockType;
import mekanism.common.block.states.BlockStateFacing;
import mekanism.common.block.states.BlockStateUtils;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.LangUtils;
import mekanism.multiblockmachine.common.MekanismMultiblockMachine;
import mekanism.multiblockmachine.common.MultiblockMachineBlocks;
import mekanism.multiblockmachine.common.block.BlockMultiblockMachine;
import mekanism.multiblockmachine.common.tile.machine.TileEntityDigitalAssemblyTable;
import mekanism.multiblockmachine.common.tile.machine.TileEntityLargeChemicalInfuser;
import mekanism.multiblockmachine.common.tile.machine.TileEntityLargeChemicalWasher;
import mekanism.multiblockmachine.common.tile.machine.TileEntityLargeElectrolyticSeparator;
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

public class BlockStateMultiblockMachine extends ExtendedBlockState {
    public static final PropertyBool activeProperty = PropertyBool.create("active");

    public BlockStateMultiblockMachine(BlockMultiblockMachine block, PropertyEnum<?> typeProperty) {
        super(block, new IProperty[]{BlockStateFacing.facingProperty, typeProperty, activeProperty}, new IUnlistedProperty[]{});
    }

    public enum MultiblockMachineBlock {
        MULTI_BLOCK_MACHINE_BLOCK_1;

        PropertyEnum<MultiblockMachineType> machineTypeProperty;

        public PropertyEnum<MultiblockMachineType> getProperty() {
            if (machineTypeProperty == null) {
                machineTypeProperty = PropertyEnum.create("type", MultiblockMachineType.class, input -> input != null && input.typeBlock == this && input.isValidMachine());
            }
            return machineTypeProperty;
        }

        public Block getBlock() {
            return switch (this) {
                case MULTI_BLOCK_MACHINE_BLOCK_1 -> MultiblockMachineBlocks.MultiblockMachine;
            };
        }
    }

    public enum MultiblockMachineType implements IStringSerializable, IBlockType {
        LARGE_ELECTROLYTIC_SEPARATOR(MultiblockMachineBlock.MULTI_BLOCK_MACHINE_BLOCK_1, 0, "LargeElectrolyticSeparator", 3, TileEntityLargeElectrolyticSeparator::new, true, true, true, Plane.HORIZONTAL, false),
        LARGE_CHEMICAL_INFUSER(MultiblockMachineBlock.MULTI_BLOCK_MACHINE_BLOCK_1, 1, "LargeChemicalInfuser", 4, TileEntityLargeChemicalInfuser::new, true, true, true, Plane.HORIZONTAL, false),
        LARGE_CHEMICAL_WASHER(MultiblockMachineBlock.MULTI_BLOCK_MACHINE_BLOCK_1, 2,"LargeChemicalWasher",5, TileEntityLargeChemicalWasher::new,true,true,true, Plane.HORIZONTAL, false),
        DIGITAL_ASSEMBLY_TABLE(MultiblockMachineBlock.MULTI_BLOCK_MACHINE_BLOCK_1,3,"DigitalAssemblyTable",6, TileEntityDigitalAssemblyTable::new,true,true,true, Plane.HORIZONTAL, false);

        private static final List<MultiblockMachineType> VALID_MACHINES = new ArrayList<>();

        static {
            Arrays.stream(MultiblockMachineType.values()).filter(MultiblockMachineType::isValidMachine).forEach(VALID_MACHINES::add);
        }

        public MultiblockMachineBlock typeBlock;
        public int meta;
        public String blockName;
        public int guiId;
        public Supplier<TileEntity> tileEntitySupplier;
        public boolean isElectric;
        public boolean hasModel;
        public boolean supportsUpgrades;
        public Predicate<EnumFacing> facingPredicate;
        public boolean activable;

        MultiblockMachineType(MultiblockMachineBlock block, int m, String name, int gui, Supplier<TileEntity> tileClass, boolean electric, boolean model, boolean upgrades, Predicate<EnumFacing> predicate, boolean hasActiveTexture) {
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

        public static List<MultiblockMachineType> getValidMachines() {
            return VALID_MACHINES;
        }


        public static MultiblockMachineType get(Block block, int meta) {
            if (block instanceof BlockMultiblockMachine) {
                return get(((BlockMultiblockMachine) block).getMachineBlock(), meta);
            }
            return null;
        }

        public static MultiblockMachineType get(MultiblockMachineBlock block, int meta) {
            for (MultiblockMachineType type : values()) {
                if (type.meta == meta && type.typeBlock == block) {
                    return type;
                }
            }
            return null;
        }

        public static MultiblockMachineType get(ItemStack stack) {
            return get(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
        }

        @Override
        public String getBlockName() {
            return blockName;
        }

        @Override
        public boolean isEnabled() {
            return MekanismConfig.current().multiblock.multiblockmachinesManager.isEnabled(this);
        }

        public boolean isValidMachine() {
            return true;
        }

        public TileEntity create() {
            return this.tileEntitySupplier != null ? this.tileEntitySupplier.get() : null;
        }

        public double getUsage() {
            return switch (this) {
                case LARGE_ELECTROLYTIC_SEPARATOR -> MekanismConfig.current().general.FROM_H2.val() * 2;
                case LARGE_CHEMICAL_INFUSER -> MekanismConfig.current().multiblock.largechemicalInfuserUsage.val();
                case LARGE_CHEMICAL_WASHER -> MekanismConfig.current().multiblock.LargeChemicalWasherUsage.val();
                case DIGITAL_ASSEMBLY_TABLE -> MekanismConfig.current().multiblock.DigitalAssemblyTableUsage.val();
                default -> 0;
            };
        }

        private double getConfigStorage() {
            return switch (this) {
                case LARGE_ELECTROLYTIC_SEPARATOR -> MekanismConfig.current().multiblock.largelectrolyticSeparator.val();
                case LARGE_CHEMICAL_INFUSER -> MekanismConfig.current().multiblock.largechemicalInfuserStorage.val();
                case LARGE_CHEMICAL_WASHER -> MekanismConfig.current().multiblock.LargeChemicalWasherStorage.val();
                case DIGITAL_ASSEMBLY_TABLE -> MekanismConfig.current().multiblock.DigitalAssemblyTableStorage.val();
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
            return new ItemStack(MultiblockMachineBlocks.MultiblockMachine, 1, meta);
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

    public static class MultiblockMachineBlockStateMapper extends StateMapperBase {

        @Nonnull
        @Override
        protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
            BlockMultiblockMachine block = (BlockMultiblockMachine) state.getBlock();
            MultiblockMachineType type = state.getValue(block.getTypeProperty());
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
            ResourceLocation baseLocation = new ResourceLocation(MekanismMultiblockMachine.MODID, type.getName());
            return new ModelResourceLocation(baseLocation, builder.toString());
        }
    }

}
