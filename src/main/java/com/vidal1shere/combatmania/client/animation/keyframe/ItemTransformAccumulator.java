package com.vidal1shere.combatmania.client.animation.keyframe;

import com.mojang.blaze3d.vertex.PoseStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Accumulates translation, rotation and scale values from all animation channels
 * and applies them to a PoseStack in a single pass.
 */
@OnlyIn(Dist.CLIENT)
public class ItemTransformAccumulator {

    private float tx, ty, tz;
    private final Quaternionf rotation = new Quaternionf();
    private float sx = 1.0f, sy = 1.0f, sz = 1.0f;

    public void reset() {
        this.tx = 0.0f;
        this.ty = 0.0f;
        this.tz = 0.0f;
        this.rotation.identity();
        this.sx = 1.0f;
        this.sy = 1.0f;
        this.sz = 1.0f;
    }

    public void addTranslation(float x, float y, float z) {
        this.tx += x;
        this.ty += y;
        this.tz += z;
    }

    public void addRotation(Quaternionf q) {
        this.rotation.mul(q);
    }

    public void addScale(float x, float y, float z) {
        this.sx *= x;
        this.sy *= y;
        this.sz *= z;
    }

    public void applyTo(PoseStack poseStack) {
        if (this.tx != 0.0f || this.ty != 0.0f || this.tz != 0.0f) {
            poseStack.translate(this.tx, this.ty, this.tz);
        }
        if (!this.rotation.equals(new Quaternionf())) {
            poseStack.mulPose(this.rotation);
        }
        if (this.sx != 1.0f || this.sy != 1.0f || this.sz != 1.0f) {
            poseStack.scale(this.sx, this.sy, this.sz);
        }
    }

    public Vector3f getTranslation() {
        return new Vector3f(this.tx, this.ty, this.tz);
    }

    public Quaternionf getRotation() {
        return new Quaternionf(this.rotation);
    }

    public Vector3f getScale() {
        return new Vector3f(this.sx, this.sy, this.sz);
    }
}