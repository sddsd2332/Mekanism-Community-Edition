package mekanism.common.integration.lookingat.theoneprobe;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mekanism.common.integration.TOPProvider;
import mekanism.common.integration.lookingat.EnergyElement;
import net.minecraft.network.PacketBuffer;

public class TOPEnergyElement extends EnergyElement implements IElement {

    public TOPEnergyElement(double energy, double maxEnergy) {
        super(energy, maxEnergy);
    }


    public TOPEnergyElement(ByteBuf buf) {
        this(buf.readDouble(), buf.readDouble());
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeDouble(energy);
        byteBuf.writeDouble(maxEnergy);
    }

    @Override
    public int getID() {
        return TOPProvider.ENERGY_ELEMENT_ID;
    }
}
