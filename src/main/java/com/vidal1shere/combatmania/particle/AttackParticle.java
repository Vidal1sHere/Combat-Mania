package com.vidal1shere.combatmania.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.AttackSweepParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class AttackParticle extends AttackSweepParticle {
    protected AttackParticle(ClientLevel level, double x, double y, double z, double quadSizeMultiplier, SpriteSet sprites) {
        super(level, x, y, z, quadSizeMultiplier, sprites);
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new AttackParticle(level, x, y, z, xSpeed, this.sprites);
        }
    }
}
