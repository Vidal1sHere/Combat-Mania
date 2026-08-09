package com.vidal1shere.combatmania.particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vidal1shere.combatmania.util.Color;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CustomParticleOptions implements ParticleOptions {

    public static final float DEFAULT_SCALE = 1.0f;
    public static final float DEFAULT_SCALE_END = 1.0f;
    public static final float DEFAULT_ROTATION = 0.0f;
    public static final float DEFAULT_ROTATION_END = 0.0f;
    public static final int DEFAULT_LIFETIME = 20;
    public static final Color DEFAULT_COLOR = new Color(0xFFFFFF, 1.0f);
    public static final Color DEFAULT_COLOR_END = new Color(0xFFFFFF, 1.0f);

    private final float scale;
    private final float scaleEnd;
    private final float rotation;
    private final float rotationEnd;
    private final int lifetime;
    private final Color color;
    private final Color colorEnd;

    private final ParticleType<CustomParticleOptions> type;

    private CustomParticleOptions(ParticleType<CustomParticleOptions> type, Builder b) {
        this.type = type;
        this.scale = b.scale;
        this.scaleEnd = b.scaleEnd;
        this.rotation = b.rotation;
        this.rotationEnd = b.rotationEnd;
        this.lifetime = b.lifetime;
        this.color = b.color.clone();
        this.colorEnd = b.colorEnd.clone();
    }

    @Override
    public ParticleType<CustomParticleOptions> getType() {
        return type;
    }

    public float getScale() { return scale; }
    public float getScaleEnd() { return scaleEnd; }
    public float getRotation() { return rotation; }
    public float getRotationEnd() { return rotationEnd; }
    public int getLifetime() { return lifetime; }
    public Color getColor() { return color.clone(); }
    public Color getColorEnd() { return colorEnd.clone(); }

    public Color lerpColor(float fraction) {
        return color.lerpRGBA(colorEnd, fraction);
    }

    public static Builder builder(ParticleType<CustomParticleOptions> type) {
        return new Builder(type);
    }

    public static MapCodec<CustomParticleOptions> codec(ParticleType<CustomParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
                Codec.FLOAT.optionalFieldOf("scale", DEFAULT_SCALE).forGetter(o -> o.scale),
                Codec.FLOAT.optionalFieldOf("scale_end", DEFAULT_SCALE_END).forGetter(o -> o.scaleEnd),
                Codec.FLOAT.optionalFieldOf("rotation", DEFAULT_ROTATION).forGetter(o -> o.rotation),
                Codec.FLOAT.optionalFieldOf("rotation_end", DEFAULT_ROTATION_END).forGetter(o -> o.rotationEnd),
                Codec.INT.optionalFieldOf("lifetime", DEFAULT_LIFETIME).forGetter(o -> o.lifetime),
                Codec.INT.optionalFieldOf("color", DEFAULT_COLOR.color).forGetter(o -> o.color.color),
                Codec.FLOAT.optionalFieldOf("alpha", DEFAULT_COLOR.alpha).forGetter(o -> o.color.alpha),
                Codec.INT.optionalFieldOf("color_end", DEFAULT_COLOR_END.color).forGetter(o -> o.colorEnd.color),
                Codec.FLOAT.optionalFieldOf("alpha_end", DEFAULT_COLOR_END.alpha).forGetter(o -> o.colorEnd.alpha)
        ).apply(i, (scale, scaleEnd, rot, rotEnd, life, color, alpha, colorEnd, alphaEnd) ->
                new Builder(type)
                        .scale(scale).scaleEnd(scaleEnd)
                        .rotation(rot).rotationEnd(rotEnd).lifetime(life)
                        .color(new Color(color, alpha))
                        .colorEnd(new Color(colorEnd, alphaEnd))
                        .build()
        ));
    }

    public static StreamCodec<RegistryFriendlyByteBuf, CustomParticleOptions> streamCodec(ParticleType<CustomParticleOptions> type) {
        return new StreamCodec<>() {
            @Override
            public CustomParticleOptions decode(RegistryFriendlyByteBuf buf) {
                return new Builder(type)
                        .scale(buf.readFloat())
                        .scaleEnd(buf.readFloat())
                        .rotation(buf.readFloat())
                        .rotationEnd(buf.readFloat())
                        .lifetime(buf.readInt())
                        .color(new Color(buf.readInt(), buf.readFloat()))
                        .colorEnd(new Color(buf.readInt(), buf.readFloat()))
                        .build();
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, CustomParticleOptions o) {
                buf.writeFloat(o.scale);
                buf.writeFloat(o.scaleEnd);
                buf.writeFloat(o.rotation);
                buf.writeFloat(o.rotationEnd);
                buf.writeInt(o.lifetime);
                buf.writeInt(o.color.color);
                buf.writeFloat(o.color.alpha);
                buf.writeInt(o.colorEnd.color);
                buf.writeFloat(o.colorEnd.alpha);
            }
        };
    }

    public static class Builder {
        private final ParticleType<CustomParticleOptions> type;
        private float scale = DEFAULT_SCALE;
        private float scaleEnd = DEFAULT_SCALE_END;
        private float rotation = DEFAULT_ROTATION;
        private float rotationEnd = DEFAULT_ROTATION_END;
        private int lifetime = DEFAULT_LIFETIME;
        private Color color = DEFAULT_COLOR.clone();
        private Color colorEnd = DEFAULT_COLOR_END.clone();

        private Builder(ParticleType<CustomParticleOptions> type) {
            this.type = type;
        }

        public Builder scale(float v) { scale = v; return this; }
        public Builder scaleEnd(float v) { scaleEnd = v; return this; }
        public Builder rotation(float v) { rotation = v; return this; }
        public Builder rotationEnd(float v) { rotationEnd = v; return this; }
        public Builder lifetime(int v) { lifetime = v; return this; }
        public Builder color(Color v) { color = v.clone(); return this; }
        public Builder colorEnd(Color v) { colorEnd = v.clone(); return this; }

        public CustomParticleOptions build() {
            return new CustomParticleOptions(type, this);
        }
    }
}