package com.vidal1shere.combatmania.client.animation.humanoid.keyframe;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class HumanoidKeyframeAnimations {
    private static final Vector3f CACHE = new Vector3f();

    private HumanoidKeyframeAnimations() {}

    public static void animate(Function<String, ModelPart> resolver, AnimationDefinition animationDefinition, long accumulatedTime, float scale) {
        float f = getElapsedSeconds(animationDefinition, accumulatedTime);

        for (Map.Entry<String, List<AnimationChannel>> entry : animationDefinition.boneAnimations().entrySet()) {
            ModelPart part = resolver.apply(entry.getKey());
            if (part == null) continue;

            for (AnimationChannel channel : entry.getValue()) {
                Keyframe[] keyframes = channel.keyframes();

                int i = Math.max(0, Mth.binarySearch(0, keyframes.length, idx -> f <= keyframes[idx].timestamp()) - 1);
                int j = Math.min(keyframes.length - 1, i + 1);

                Keyframe keyframe = keyframes[i];
                Keyframe keyframe1 = keyframes[j];
                float delta = f - keyframe.timestamp();

                float t = (j != i)
                    ? Mth.clamp(delta / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F)
                    : 0.0F;

                keyframe1.interpolation().apply(CACHE, t, keyframes, i, j, scale);
                channel.target().apply(part, CACHE);
            }
        }
    }

    public static void applyStatic(Function<String, ModelPart> resolver, AnimationDefinition animationDefinition) {
        animate(resolver, animationDefinition, 0L, 1.0F);
    }

    private static float getElapsedSeconds(AnimationDefinition def, long accumulatedTime) {
        float f = (float) accumulatedTime / 1000.0F;
        return def.looping() ? f % def.lengthInSeconds() : f;
    }
}