package com.vidal1shere.combatmania.particle;

import com.vidal1shere.combatmania.particle.options.CustomParticleOptions;
import com.vidal1shere.combatmania.util.Color;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class CustomParticle extends TextureSheetParticle {

    private final SpriteSet sprites;
    private final float scaleStart;
    private final float scaleEnd;
    private final float rotationStart;
    private final float rotationEnd;
    private final Color colorStart;
    private final Color colorEnd;

    protected CustomParticle(ClientLevel level, double x, double y, double z, CustomParticleOptions options, SpriteSet sprites) {
        super(level, x, y, z);

        this.sprites = sprites;
        this.scaleStart = options.getScale();
        this.scaleEnd = options.getScaleEnd();
        this.rotationStart = options.getRotation() * (float) Math.PI / 180f;
        this.rotationEnd = options.getRotationEnd() * (float) Math.PI / 180f;
        this.colorStart = options.getColor();
        this.colorEnd = options.getColorEnd();
        this.lifetime = options.getLifetime();

        this.quadSize = scaleStart;
        this.oRoll = rotationStart;
        this.roll = rotationStart;

        applyColor(colorStart);
        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }

        float fraction = (float) age / (float) lifetime;

        applyColor(colorStart.lerpRGBA(colorEnd, fraction));
        this.quadSize = scaleStart + (scaleEnd - scaleStart) * fraction;
        this.oRoll = this.roll;
        this.roll = rotationStart + (rotationEnd - rotationStart) * fraction;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    private void applyColor(Color c) {
        this.rCol = c.getRedF();
        this.gCol = c.getGreenF();
        this.bCol = c.getBlueF();
        this.alpha = c.alpha;
    }

    public static class Provider implements ParticleProvider<CustomParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public CustomParticle createParticle(CustomParticleOptions options, ClientLevel level,
                                             double x, double y, double z,
                                             double dx, double dy, double dz) {
            return new CustomParticle(level, x, y, z, options, sprites);
        }
    }
}