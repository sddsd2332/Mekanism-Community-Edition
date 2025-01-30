package mekanism.common.content.gear.shared;

import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.api.gear.config.IModuleConfigItem;
import mekanism.api.gear.config.ModuleBooleanData;
import mekanism.api.gear.config.ModuleConfigItemCreator;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.MekanismHooks;
import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.source.IRadiationSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Optional;

public class ModuleRadiationShieldingUnit implements ICustomModule<ModuleRadiationShieldingUnit> {


    private IModuleConfigItem<Boolean> RadiationClearance;
    private IModuleConfigItem<Boolean> RadiationImmunity;
    private IModuleConfigItem<Boolean> RadiationRangeClearing;

    @Override
    public void init(IModule<ModuleRadiationShieldingUnit> module, ModuleConfigItemCreator configItemCreator) {
        if (Mekanism.hooks.NuclearCraft) {
            RadiationClearance = configItemCreator.createConfigItem("radiation_clearance", MekanismLang.MODULE_RADIATION_CLEARANCE, new ModuleBooleanData());
            RadiationImmunity = configItemCreator.createConfigItem("radiation_immunity", MekanismLang.MODULE_RADIATION_IMMUNITY, new ModuleBooleanData());
            RadiationRangeClearing = configItemCreator.createConfigItem("radiation_range_clearing", MekanismLang.MODULE_RADIATION_RANGE_CLEARING, new ModuleBooleanData(false));
        }
    }

    @Override
    public void onAdded(IModule<ModuleRadiationShieldingUnit> module, boolean first) {
        double ncRadiationResistance = MekanismConfig.current().meka.mekaSuitModuleRadiationresistance.val();
        ItemStack stack = module.getContainer();
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (first) {
            if (!tag.hasKey("ncRadiationResistance")) {
                tag.setDouble("ncRadiationResistance", ncRadiationResistance);
            }
        } else {
            if (tag != null) {
                tag.setDouble("ncRadiationResistance", ncRadiationResistance * module.getInstalledCount());
            }
        }
    }

    @Override
    public void onRemoved(IModule<ModuleRadiationShieldingUnit> module, boolean last) {
        double ncRadiationResistance = MekanismConfig.current().meka.mekaSuitModuleRadiationresistance.val();
        ItemStack stack = module.getContainer();
        NBTTagCompound tag = stack.getTagCompound();
        if (last) {
            if (tag != null) {
                tag.removeTag("ncRadiationResistance");
            }
        } else {
            if (tag != null) {
                tag.setDouble("ncRadiationResistance", ncRadiationResistance * module.getInstalledCount());
            }
        }
    }

    @Override
    public void tickServer(IModule<ModuleRadiationShieldingUnit> module, EntityPlayer player) {
        if (Mekanism.hooks.NuclearCraft) {
            if (RadiationImmunity.get()) {
                double RadImmunityTime = getRadiationImmunityTime(player);
                if (RadImmunityTime < MekanismConfig.current().meka.mekaSuitMinimumRadiationTime.val() && module.hasEnoughEnergy(MekanismConfig.current().meka.mekaSuitEnergyUsageMinimumRadiationImmunity.val())) {
                    module.useEnergy(player, MekanismConfig.current().meka.mekaSuitEnergyUsageMinimumRadiationImmunity.val());
                    setRadiationImmunityTime(player, MekanismConfig.current().meka.mekaSuitRadiationImmunityTime.val() * 20D);
                }
            }
            if (RadiationClearance.get()) {
                //当前玩家辐射量
                double removeRad = getRadiation(player);
                if (removeRad > 0) {
                    //如果辐射量大于物品能量
                    if (removeRad > module.getContainerEnergy()) {
                        //辐射量减去剩余能量
                        removeRad = removeRad - module.getContainerEnergy();
                        module.useEnergy(player, removeRad);
                        //重新设置玩家辐射
                        setRadiation(player, removeRad);
                    } else {
                        module.useEnergy(player, removeRad);
                        setRadiation(player, 0);
                    }
                }

                double removeRadLevel = getRadiationLevel(player);
                if (removeRadLevel > 0) {
                    if (removeRadLevel > module.getContainerEnergy()) {
                        removeRadLevel = removeRadLevel - module.getContainerEnergy();
                        module.useEnergy(player, removeRadLevel);
                        setRadiationLevel(player, removeRadLevel);
                    } else {
                        module.useEnergy(player, removeRadLevel);
                        setRadiationLevel(player, 0);
                    }
                }
            }
            if (RadiationRangeClearing.get()) {
                double removeRadChunk = getChunkRadiation(player);
                if (removeRadChunk > 0) {
                    if (removeRadChunk > module.getContainerEnergy()) {
                        removeRadChunk = removeRadChunk - module.getContainerEnergy();
                        module.useEnergy(player, removeRadChunk);
                        setChunkRadiation(player, removeRadChunk);
                    } else {
                        module.useEnergy(player, removeRadChunk);
                        setChunkRadiation(player, 0);
                    }
                }
            }
        }
    }


    //获取玩家已有的辐射
    private double getRadiation(EntityPlayer player) {
        if (Mekanism.hooks.NuclearCraft) {
            return getNCRadiation(player);
        }
        return 0;
    }


    @Optional.Method(modid = MekanismHooks.NuclearCraft_MOD_ID)
    private double getNCRadiation(EntityPlayer player) {
        if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
            return 0;
        }
        IEntityRads Rads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
        if (Rads != null) {
            return Rads.getTotalRads();
        } else {
            return 0;
        }
    }

    //获取玩家已有的辐射水平
    private double getRadiationLevel(EntityPlayer player) {
        if (Mekanism.hooks.NuclearCraft) {
            return getNCRadiationLevel(player);
        }
        return 0;
    }

    @Optional.Method(modid = MekanismHooks.NuclearCraft_MOD_ID)
    private double getNCRadiationLevel(EntityPlayer player) {
        if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
            return 0;
        }
        IEntityRads Rads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
        if (Rads != null) {
            return Rads.getRadiationLevel();
        } else {
            return 0;
        }
    }

    //获取玩家已有的辐射无敌时间
    private double getRadiationImmunityTime(EntityPlayer player) {
        if (Mekanism.hooks.NuclearCraft) {
            return getNCRadiationImmunityTime(player);
        } else {
            return 0;
        }
    }

    @Optional.Method(modid = MekanismHooks.NuclearCraft_MOD_ID)
    private double getNCRadiationImmunityTime(EntityPlayer player) {
        if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
            return 0;
        }
        IEntityRads Rads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
        if (Rads != null) {
            return Rads.getRadiationImmunityTime();
        } else {
            return 0;
        }
    }


    //清除玩家的辐射量
    private void setRadiation(EntityPlayer player, double radiation) {
        if (Mekanism.hooks.NuclearCraft) {
            setNCRadiation(player, radiation);
        }
    }

    @Optional.Method(modid = MekanismHooks.NuclearCraft_MOD_ID)
    private void setNCRadiation(EntityPlayer player, double radiation) {
        if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
            return;
        }
        IEntityRads Rads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
        if (Rads != null) {
            Rads.setTotalRads(radiation, false);
        }
    }

    //清除玩家的辐射量水平
    private void setRadiationLevel(EntityPlayer player, double radiation) {
        if (Mekanism.hooks.NuclearCraft) {
            setNCRadiationLevel(player, radiation);
        }
    }

    @Optional.Method(modid = MekanismHooks.NuclearCraft_MOD_ID)
    private void setNCRadiationLevel(EntityPlayer player, double radiation) {
        if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
            return;
        }
        IEntityRads Rads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
        if (Rads != null) {
            Rads.setRadiationLevel(radiation);
        }
    }

    //设置免疫NC辐射时间
    private void setRadiationImmunityTime(EntityPlayer player, double time) {
        if (Mekanism.hooks.NuclearCraft) {
            setNCRadiationImmunityTime(player, time);
        }
    }

    @Optional.Method(modid = MekanismHooks.NuclearCraft_MOD_ID)
    private void setNCRadiationImmunityTime(EntityPlayer player, double time) {
        if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) {
            return;
        }
        IEntityRads Rads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
        if (Rads != null) {
            Rads.setRadiationImmunityTime(time);
        }
    }

    //获取玩家所在区块的辐射
    private double getChunkRadiation(EntityPlayer player) {
        if (Mekanism.hooks.NuclearCraft) {
            return getNCChunkRadiation(player);
        } else {
            return 0;
        }
    }

    @Optional.Method(modid = MekanismHooks.NuclearCraft_MOD_ID)
    private double getNCChunkRadiation(EntityPlayer player) {
        Chunk chunk = player.getEntityWorld().getChunk(new BlockPos(player));
        if (chunk != null && chunk.isLoaded()) {
            if (!chunk.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) {
                return 0;
            }
            IRadiationSource source = chunk.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
            if (source != null) {
                return source.getRadiationLevel();
            }
        }
        return 0;
    }

    //设置玩家所在区块的辐射
    private void setChunkRadiation(EntityPlayer player, double rad) {
        if (Mekanism.hooks.NuclearCraft) {
            setNCChunkRadiation(player, rad);
        }
    }

    @Optional.Method(modid = MekanismHooks.NuclearCraft_MOD_ID)
    private void setNCChunkRadiation(EntityPlayer player, double rad) {
        Chunk chunk = player.getEntityWorld().getChunk(new BlockPos(player));
        if (chunk != null && chunk.isLoaded()) {
            if (!chunk.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) {
                return;
            }
            IRadiationSource source = chunk.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
            if (source != null) {
                source.setRadiationLevel(rad);
            }
        }
    }
}
