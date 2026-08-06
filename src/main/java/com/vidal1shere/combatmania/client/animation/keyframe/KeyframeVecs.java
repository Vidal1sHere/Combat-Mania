package com.vidal1shere.combatmania.client.animation.keyframe;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public final class KeyframeVecs {

    private KeyframeVecs() {}

    public static Vector3f posVec(float x, float y, float z) {
        return new Vector3f(x, -y, z);
    }

    public static Vector3f degreeVec(float xDeg, float yDeg, float zDeg) {
        return new Vector3f(
            xDeg * ((float) Math.PI / 180f),
            yDeg * ((float) Math.PI / 180f),
            zDeg * ((float) Math.PI / 180f)
        );
    }

    public static Vector3f scaleVec(float x, float y, float z) {
        return new Vector3f(x - 1.0f, y - 1.0f, z - 1.0f);
    }
}