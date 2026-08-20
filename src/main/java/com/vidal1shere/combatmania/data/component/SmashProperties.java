package com.vidal1shere.combatmania.data.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

import static com.vidal1shere.combatmania.util.CustomProperties.*;

public record SmashProperties(float lightThreshold, float heavyThreshold, float lowFallMultiplier,
                              float mediumFallMultiplier, float highFallMultiplier, float maxDamage,
                              float knockbackRadius, float knockbackPower) {

    public static final PropertyKey<SmashProperties> KEY = new PropertyKey<>(new SmashProperties());

    public static final Codec<SmashProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.FLOAT.fieldOf("light_threshold").forGetter(SmashProperties::lightThreshold), Codec.FLOAT.fieldOf("heavy_threshold").forGetter(SmashProperties::heavyThreshold), Codec.FLOAT.fieldOf("low_fall_multiplier").forGetter(SmashProperties::lowFallMultiplier), Codec.FLOAT.fieldOf("medium_fall_multiplier").forGetter(SmashProperties::mediumFallMultiplier), Codec.FLOAT.fieldOf("high_fall_multiplier").forGetter(SmashProperties::highFallMultiplier), Codec.FLOAT.fieldOf("max_damage").forGetter(SmashProperties::maxDamage), Codec.FLOAT.fieldOf("knockback_radius").forGetter(SmashProperties::knockbackRadius), Codec.FLOAT.fieldOf("knockback_power").forGetter(SmashProperties::knockbackPower)).apply(instance, SmashProperties::new));

    public static final StreamCodec<ByteBuf, SmashProperties> STREAM_CODEC = StreamCodec.of((buf, data) -> {
        buf.writeFloat(data.lightThreshold);
        buf.writeFloat(data.heavyThreshold);
        buf.writeFloat(data.lowFallMultiplier);
        buf.writeFloat(data.mediumFallMultiplier);
        buf.writeFloat(data.highFallMultiplier);
        buf.writeFloat(data.maxDamage);
        buf.writeFloat(data.knockbackRadius);
        buf.writeFloat(data.knockbackPower);
    }, buf -> new SmashProperties(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat()));

    public SmashProperties() {
        this(1.5F, 5.0F, 4.0F, 2.0F, 1.0F, 0.0F, 3.5F, 0.7F);
    }

    public static PropertySet<SmashProperties> of(float lightThreshold, float heavyThreshold, float lowFallMultiplier, float mediumFallMultiplier, float highFallMultiplier, float maxDamage, float knockbackRadius, float knockbackPower) {
        return new PropertySet<>(KEY, new SmashProperties(lightThreshold, heavyThreshold, lowFallMultiplier, mediumFallMultiplier, highFallMultiplier, maxDamage, knockbackRadius, knockbackPower));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof SmashProperties other))
            return false;

        return Float.compare(lightThreshold, other.lightThreshold) == 0 && Float.compare(heavyThreshold, other.heavyThreshold) == 0 && Float.compare(lowFallMultiplier, other.lowFallMultiplier) == 0 && Float.compare(knockbackRadius, other.knockbackRadius) == 0 && Float.compare(knockbackPower, other.knockbackPower) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lightThreshold, heavyThreshold, lowFallMultiplier, knockbackRadius, knockbackPower);
    }
}
