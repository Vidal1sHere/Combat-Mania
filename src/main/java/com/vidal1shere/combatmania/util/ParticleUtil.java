package com.vidal1shere.combatmania.util;

import com.vidal1shere.combatmania.particle.options.CustomParticleOptions;
import com.vidal1shere.combatmania.registry.particle.CMParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public final class ParticleUtil {

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

    public static CustomParticleOptions.Builder build(ParticleType<CustomParticleOptions> type) {
        return CustomParticleOptions.builder(type);
    }

    public static void spawnSmashAttackParticles(LevelAccessor level, BlockPos pos, int power, double radius, double spacing) {
        Vec3 center = pos.getCenter().add(0.0, 0.5, 0.0);
        BlockParticleOption particle = new BlockParticleOption(ParticleTypes.DUST_PILLAR, level.getBlockState(pos));

        for (double ringRadius = 0.0; ringRadius <= radius; ringRadius += spacing) {
            int particleCount = ringRadius == 0.0 ? Math.max(1, (int) (power / 1.5)) : Math.max(1, (int) (power / 1.5 * ringRadius / radius));

            for (int i = 0; i < particleCount; i++) {
                double angle = Math.PI * 2.0 * i / particleCount;
                double x = center.x + ringRadius * Math.cos(angle);
                double z = center.z + ringRadius * Math.sin(angle);

                level.addParticle(
                        particle,
                        x + level.getRandom().nextGaussian() * 0.5,
                        center.y,
                        z + level.getRandom().nextGaussian() * 0.5,
                        level.getRandom().nextGaussian() * 0.05,
                        level.getRandom().nextGaussian() * 0.05,
                        level.getRandom().nextGaussian() * 0.05
                );
            }
        }
    }
}