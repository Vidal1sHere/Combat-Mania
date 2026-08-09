package com.vidal1shere.combatmania.registry.particle;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.particle.type.CustomParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CMParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, CombatMania.MOD_ID);

    public static final Supplier<CustomParticleType> LEFT_SWEEP_ATTACK =
            PARTICLE_TYPES.register("left_sweep_attack", () -> new CustomParticleType(true));

    public static final Supplier<CustomParticleType> LEFT_SHORT_SWEEP_ATTACK =
            PARTICLE_TYPES.register("left_short_sweep_attack", () -> new CustomParticleType(true));

    public static final Supplier<CustomParticleType> RIGHT_SWEEP_ATTACK =
            PARTICLE_TYPES.register("right_sweep_attack", () -> new CustomParticleType(true));

    public static final Supplier<CustomParticleType> RIGHT_SHORT_SWEEP_ATTACK =
            PARTICLE_TYPES.register("right_short_sweep_attack", () -> new CustomParticleType(true));

    public static final Supplier<CustomParticleType> VERTICAL_SWEEP_ATTACK =
            PARTICLE_TYPES.register("vertical_sweep_attack", () -> new CustomParticleType(true));

    public static final Supplier<CustomParticleType> SLASH_ATTACK =
            PARTICLE_TYPES.register("slash_attack", () -> new CustomParticleType(true));
    
    public static void register(IEventBus bus) {
        PARTICLE_TYPES.register(bus);
    }
}
