package com.vidal1shere.combatmania.util;

import com.vidal1shere.combatmania.particle.options.CustomParticleOptions;
import com.vidal1shere.combatmania.registry.particle.CMParticles;
import net.minecraft.core.particles.ParticleType;

public final class VFX {

    public static CustomParticleOptions sweep() {
        return sweep(false, false, false, false);
    }
    
    public static CustomParticleOptions sweep(boolean right, boolean shortened, boolean vertical, boolean mirrored) {
        float rotation;
        
        if (vertical && mirrored) {
            rotation = -90f;
        } else if (vertical) {
            rotation = 90f;
        } else if (mirrored) {
            rotation = 180f;
        } else {
            rotation = 0;
        }
        
        return sweep(right, shortened, rotation);
    }

    public static CustomParticleOptions sweep(boolean right, boolean shortened, float rotation) {
        ParticleType<CustomParticleOptions> type;
        
        if (right && shortened) {
            type = CMParticles.RIGHT_SHORT_SWEEP_ATTACK.get();
        } else if (shortened) {
            type = CMParticles.LEFT_SHORT_SWEEP_ATTACK.get();
        } else if (right) {
            type = CMParticles.RIGHT_SWEEP_ATTACK.get();
        } else {
            type = CMParticles.LEFT_SWEEP_ATTACK.get();
        }
        
        return build(type).lifetime(4).rotation(rotation).rotationEnd(rotation).build();
    }

    public static CustomParticleOptions simple(ParticleType<CustomParticleOptions> type) {
        return CustomParticleOptions.builder(type).build();
    }

    public static CustomParticleOptions colored(ParticleType<CustomParticleOptions> type, int lifetime, Color color) {
        return CustomParticleOptions.builder(type).color(color).lifetime(lifetime).colorEnd(color).build();
    }

    public static CustomParticleOptions colored(ParticleType<CustomParticleOptions> type, int lifetime, Color color, Color colorEnd) {
        return CustomParticleOptions.builder(type).color(color).lifetime(lifetime).colorEnd(colorEnd).build();
    }

    public static CustomParticleOptions scaled(ParticleType<CustomParticleOptions> type, int lifetime, float scale) {
        return CustomParticleOptions.builder(type).scale(scale).lifetime(lifetime).scaleEnd(scale).build();
    }

    public static CustomParticleOptions scaled(ParticleType<CustomParticleOptions> type, int lifetime, float scale, float scaleEnd) {
        return CustomParticleOptions.builder(type).scale(scale).lifetime(lifetime).scaleEnd(scaleEnd).build();
    }

    public static CustomParticleOptions rotating(ParticleType<CustomParticleOptions> type, int lifetime, float rotation) {
        return CustomParticleOptions.builder(type).lifetime(lifetime).rotation(rotation).rotationEnd(rotation).build();
    }

    public static CustomParticleOptions rotating(ParticleType<CustomParticleOptions> type, int lifetime, float rotation, float rotationEnd) {
        return CustomParticleOptions.builder(type).lifetime(lifetime).rotation(rotation).rotationEnd(rotationEnd).build();
    }

    public static CustomParticleOptions timed(ParticleType<CustomParticleOptions> type, int lifetime) {
        return CustomParticleOptions.builder(type).lifetime(lifetime).build();
    }

    public static CustomParticleOptions full(ParticleType<CustomParticleOptions> type,
                                             Color color, Color colorEnd,
                                             float scale, float scaleEnd,
                                             float rotation, float rotationEnd,
                                             int lifetime) {
        return CustomParticleOptions.builder(type)
                .color(color).colorEnd(colorEnd)
                .scale(scale).scaleEnd(scaleEnd)
                .rotation(rotation).rotationEnd(rotationEnd)
                .lifetime(lifetime)
                .build();
    }

    public static CustomParticleOptions.Builder build(ParticleType<CustomParticleOptions> type) {
        return CustomParticleOptions.builder(type);
    }
}