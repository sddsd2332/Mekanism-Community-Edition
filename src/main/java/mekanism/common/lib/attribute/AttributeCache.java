package mekanism.common.lib.attribute;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import mekanism.common.config.listener.ConfigBasedCachedSupplier;
import net.minecraft.entity.ai.attributes.AttributeModifier;

public class AttributeCache extends ConfigBasedCachedSupplier<Multimap<String, AttributeModifier>> {

    public AttributeCache(IAttributeRefresher attributeRefresher) {
        super(() -> {
            Builder<String, AttributeModifier> builder = ImmutableMultimap.builder();
            attributeRefresher.addToBuilder(builder);
            return builder.build();
        });
    }
}