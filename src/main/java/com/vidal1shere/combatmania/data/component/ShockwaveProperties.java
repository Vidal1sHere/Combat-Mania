package com.vidal1shere.combatmania.data.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

import static com.vidal1shere.combatmania.util.CustomProperties.*;

public record ShockwaveProperties(int maxLiftTicks, float knockbackRadius, float knockbackPower,
                                  float fallRadiusMultiplier, float maxRadius, float maxDamage) {

    public static final PropertyKey<ShockwaveProperties> KEY = new PropertyKey<>(new ShockwaveProperties());

    public static final Codec<ShockwaveProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("max_lift_ticks").forGetter(ShockwaveProperties::maxLiftTicks),
            Codec.FLOAT.fieldOf("knockback_radius").forGetter(ShockwaveProperties::knockbackRadius),
            Codec.FLOAT.fieldOf("knockback_power").forGetter(ShockwaveProperties::knockbackPower),
            Codec.FLOAT.fieldOf("fall_radius_multiplier").forGetter(ShockwaveProperties::fallRadiusMultiplier),
            Codec.FLOAT.fieldOf("max_radius").forGetter(ShockwaveProperties::maxRadius),
            Codec.FLOAT.fieldOf("max_damage").forGetter(ShockwaveProperties::maxDamage)
    ).apply(instance, ShockwaveProperties::new));

    public static final StreamCodec<ByteBuf, ShockwaveProperties> STREAM_CODEC = StreamCodec.of(
            (buf, data) -> {
                buf.writeInt(data.maxLiftTicks);
                buf.writeFloat(data.knockbackRadius);
                buf.writeFloat(data.knockbackPower);
                buf.writeFloat(data.fallRadiusMultiplier);
                buf.writeFloat(data.maxRadius);
                buf.writeFloat(data.maxDamage);
            },
            buf -> new ShockwaveProperties(
                    buf.readInt(),
                    buf.readFloat(),
                    buf.readFloat(),
                    buf.readFloat(),
                    buf.readFloat(),
                    buf.readFloat()
            )
    );

    public ShockwaveProperties() {
        this(40, 3.5F, 0.7F, 0.5F, 8.0F, 0.0F);
    }

    public static PropertySet<ShockwaveProperties> of(int maxLiftTicks, float knockbackRadius, float knockbackPower,
                                                       float fallRadiusMultiplier, float maxRadius, float maxDamage) {
        return new PropertySet<>(KEY, new ShockwaveProperties(maxLiftTicks, knockbackRadius, knockbackPower,
                fallRadiusMultiplier, maxRadius, maxDamage));
    }

    public float getEffectiveRadius(float fallDistance, int liftTicks) {
        float liftRatio = maxLiftTicks > 0 ? Math.min(1.0F, (float) liftTicks / maxLiftTicks) : 1.0F;
        float radius = knockbackRadius * liftRatio + fallDistance * fallRadiusMultiplier;
        return maxRadius > 0 ? Math.min(maxRadius, radius) : radius;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ShockwaveProperties other)) return false;
        return maxLiftTicks == other.maxLiftTicks
                && Float.compare(knockbackRadius, other.knockbackRadius) == 0
                && Float.compare(knockbackPower, other.knockbackPower) == 0
                && Float.compare(fallRadiusMultiplier, other.fallRadiusMultiplier) == 0
                && Float.compare(maxRadius, other.maxRadius) == 0
                && Float.compare(maxDamage, other.maxDamage) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxLiftTicks, knockbackRadius, knockbackPower, fallRadiusMultiplier, maxRadius, maxDamage);
    }
}