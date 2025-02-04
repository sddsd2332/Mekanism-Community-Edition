package mekanism.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import mekanism.api.gear.ModuleData;
import mekanism.api.gear.config.ModuleBooleanData;
import mekanism.api.gear.config.ModuleConfigData;
import mekanism.api.gear.config.ModuleEnumData;
import mekanism.api.math.MathUtils;
import mekanism.common.PacketHandler;
import mekanism.common.content.gear.IModuleContainerItem;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.network.PacketUpdateModuleSettings.UpdateModuleSettingsMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class PacketUpdateModuleSettings implements IMessageHandler<UpdateModuleSettingsMessage, IMessage> {


    @Override
    public IMessage onMessage(UpdateModuleSettingsMessage message, MessageContext context) {
        EntityPlayer player = PacketHandler.getPlayer(context);
        if (player == null) {
            return null;
        }
        PacketHandler.handlePacket(() -> {
            if (message.dataIndex >= 0) {
                ItemStack stack = player.inventory.getStackInSlot(message.slotId);
                if (!stack.isEmpty() && stack.getItem() instanceof IModuleContainerItem) {
                    Module<?> module = ModuleHelper.get().load(stack, message.moduleType);
                    if (module != null) {
                        List<ModuleConfigItem<?>> configItems = module.getConfigItems();
                        if (message.dataIndex < configItems.size()) {
                            setValue(configItems.get(message.dataIndex), message);
                        }
                    }
                }
            }
        }, player);
        return null;
    }


    //Very dirty in terms of the unchecked casts but the various details are actually checked relatively accurately
    private <TYPE> void setValue(ModuleConfigItem<TYPE> moduleConfigItem, UpdateModuleSettingsMessage message) {
        ModuleConfigData<TYPE> configData = moduleConfigItem.getData();
        if (configData instanceof ModuleBooleanData && message.dataType == ModuleDataType.BOOLEAN) {
            ((ModuleConfigItem<Boolean>) moduleConfigItem).set((boolean) message.value);
        } else if (configData instanceof ModuleEnumData && message.dataType == ModuleDataType.ENUM) {
            moduleConfigItem.set((TYPE) MathUtils.getByIndexMod(((ModuleEnumData<?>) configData).getEnums(), (int) message.value));
        }
    }


    public static UpdateModuleSettingsMessage create(int slotId, ModuleData<?> moduleType, int dataIndex, ModuleConfigData<?> configData) {
        if (configData instanceof ModuleBooleanData data) {
            return new UpdateModuleSettingsMessage(slotId, moduleType, dataIndex, ModuleDataType.BOOLEAN, data.get());
        } else if (configData instanceof ModuleEnumData<?> data) {
            return new UpdateModuleSettingsMessage(slotId, moduleType, dataIndex, ModuleDataType.ENUM, data.get().ordinal());
        }
        throw new IllegalArgumentException("Unknown config data type.");
    }


    public enum ModuleDataType {
        BOOLEAN,
        ENUM
    }

    public static class UpdateModuleSettingsMessage implements IMessage {

        private ModuleData<?> moduleType;
        private int slotId;
        private int dataIndex;
        private ModuleDataType dataType;
        private Object value;

        public UpdateModuleSettingsMessage() {
        }

        private UpdateModuleSettingsMessage(int slotId, ModuleData<?> moduleType, int dataIndex, ModuleDataType dataType, Object value) {
            this.slotId = slotId;
            this.moduleType = moduleType;
            this.dataIndex = dataIndex;
            this.dataType = dataType;
            this.value = value;
        }

        @Override
        public void toBytes(ByteBuf dataStream) {
            dataStream.writeInt(slotId);
            new PacketBuffer(dataStream).writeString(moduleType.getModuleData().getName());
            dataStream.writeInt(dataIndex);
            new PacketBuffer(dataStream).writeEnumValue(dataType);
            if (dataType == ModuleDataType.BOOLEAN) {
                dataStream.writeBoolean((boolean) value);
            } else if (dataType == ModuleDataType.ENUM) {
                dataStream.writeInt((int) value);
            }
        }

        @Override
        public void fromBytes(ByteBuf dataStream) {
            slotId = dataStream.readInt();
            moduleType = ModuleHelper.get().getModuleTypeFromName(new PacketBuffer(dataStream).readString(32767));
            dataIndex = dataStream.readInt();
            dataType = new PacketBuffer(dataStream).readEnumValue(ModuleDataType.class);
            if (dataType == ModuleDataType.BOOLEAN) {
                value = dataStream.readBoolean();
            } else if (dataType == ModuleDataType.ENUM) {
                value = dataStream.readInt();
            }
        }
    }


}
