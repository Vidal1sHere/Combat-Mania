package com.vidal1shere.combatmania.particle.type;

import com.mojang.serialization.MapCodec;
import com.vidal1shere.combatmania.particle.options.CustomParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CustomParticleType extends ParticleType<CustomParticleOptions> {
    public CustomParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
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
