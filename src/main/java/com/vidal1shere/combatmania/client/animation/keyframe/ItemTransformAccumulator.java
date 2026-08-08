package com.vidal1shere.combatmania.client.animation.keyframe;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Accumulates translation, rotation and scale values from all animation channels
 * and applies them to a PoseStack in a single pass, respecting an optional pivot point.
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

    public static void debugRenderPivot(PoseStack poseStack, MultiBufferSource bufferSource, Vector3f pivot) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugLineStrip(1.0));
        Matrix4f matrix = poseStack.last().pose();
        float r = 1.0f, g = 0.0f, b = 0.0f, a = 1.0f;
        float size = 1.0f;

        consumer.addVertex(matrix, pivot.x() - size, pivot.y(), pivot.z()).setColor(r, g, b, a).setNormal(1, 0, 0);
        consumer.addVertex(matrix, pivot.x() + size, pivot.y(), pivot.z()).setColor(r, g, b, a).setNormal(1, 0, 0);
        consumer.addVertex(matrix, pivot.x(), pivot.y() - size, pivot.z()).setColor(r, g, b, a).setNormal(0, 1, 0);
        consumer.addVertex(matrix, pivot.x(), pivot.y() + size, pivot.z()).setColor(r, g, b, a).setNormal(0, 1, 0);
        consumer.addVertex(matrix, pivot.x(), pivot.y(), pivot.z() - size).setColor(r, g, b, a).setNormal(0, 0, 1);
        consumer.addVertex(matrix, pivot.x(), pivot.y(), pivot.z() + size).setColor(r, g, b, a).setNormal(0, 0, 1);
    }

    public void applyTo(PoseStack poseStack, Vector3f pivot) {
        if (this.tx != 0.0f || this.ty != 0.0f || this.tz != 0.0f) {
            poseStack.translate(this.tx, this.ty, this.tz);
        }

        boolean hasPivot = pivot.x() != 0.0f || pivot.y() != 0.0f || pivot.z() != 0.0f;
        boolean hasRotation = !this.rotation.equals(new Quaternionf());
        boolean hasScale = this.sx != 1.0f || this.sy != 1.0f || this.sz != 1.0f;

        if (hasRotation || hasScale) {
            if (hasPivot) poseStack.translate(pivot.x(), pivot.y(), pivot.z());
            if (hasRotation) poseStack.mulPose(this.rotation);
            if (hasScale) poseStack.scale(this.sx, this.sy, this.sz);
            if (hasPivot) poseStack.translate(-pivot.x(), -pivot.y(), -pivot.z());
        }
    }
}