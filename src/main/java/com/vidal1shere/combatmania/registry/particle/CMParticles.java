package com.vidal1shere.combatmania.registry.particle;

import com.vidal1shere.combatmania.CombatMania;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CMParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, CombatMania.MOD_ID);

    public static final Supplier<SimpleParticleType> CLEAVE_ATTACK =
            PARTICLE_TYPES.register("cleave_attack", () -> new SimpleParticleType(true));
    
    public static void register(IEventBus bus) {
        PARTICLE_TYPES.register(bus);
    }
}
