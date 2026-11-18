package com.vidal1shere.combatmania.particle.type;

import com.mojang.serialization.MapCodec;
import com.vidal1shere.combatmania.particle.options.CustomParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CustomParticleType extends ParticleType<CustomParticleOptions> {
    private final int defaultLifetime;

    public CustomParticleType(boolean overrideLimitter) {
        this(overrideLimitter, CustomParticleOptions.DEFAULT_LIFETIME);
    }

    public CustomParticleType(boolean overrideLimitter, int defaultLifetime) {
        super(overrideLimitter);
        this.defaultLifetime = defaultLifetime;
    }

    public int defaultLifetime() {
        return defaultLifetime;
    }

    @Override
    public MapCodec<CustomParticleOptions> codec() {
        return CustomParticleOptions.codec(this);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CustomParticleOptions> streamCodec() {
        return CustomParticleOptions.streamCodec(this);
    }
}
