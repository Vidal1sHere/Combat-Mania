package com.vidal1shere.combatmania.client.old_animation.data;

/** A single keyframe in a first-person item animation. */
public record Keyframe(
        float time,
        float translateX,
        float translateY,
        float translateZ,
        float rotateX,
        float rotateY,
        float rotateZ,
        float scale
) {
    public static Keyframe translate(float time, float x, float y, float z) {
        return new Keyframe(time, x, y, z, 0, 0, 0, 1f);
    }

    public static Keyframe rotate(float time, float rx, float ry, float rz) {
        return new Keyframe(time, 0, 0, 0, rx, ry, rz, 1f);
    }

    public static Keyframe identity(float time) {
        return new Keyframe(time, 0, 0, 0, 0, 0, 0, 1f);
    }

    public static Keyframe lerp(Keyframe a, Keyframe b, float t) {
        return new Keyframe(
                lerp(a.time, b.time, t),
                lerp(a.translateX, b.translateX, t),
                lerp(a.translateY, b.translateY, t),
                lerp(a.translateZ, b.translateZ, t),
                lerpAngle(a.rotateX, b.rotateX, t),
                lerpAngle(a.rotateY, b.rotateY, t),
                lerpAngle(a.rotateZ, b.rotateZ, t),
                lerp(a.scale, b.scale, t)
        );
    }

    public static Keyframe smoothStep(Keyframe a, Keyframe b, float t) {
        return lerp(a, b, t * t * (3f - 2f * t));
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static float lerpAngle(float a, float b, float t) {
        float diff = ((b - a) % 360f + 540f) % 360f - 180f;
        return a + diff * t;
    }
}