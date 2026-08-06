package com.vidal1shere.combatmania.client.animation.keyframe;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationChannel;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

/**
 * Applies an ItemAnimationDefinition to a PoseStack for first-person item rendering.
 */
@OnlyIn(Dist.CLIENT)
public final class ItemKeyframeAnimations {

    private ItemKeyframeAnimations() {}

    public static void animate(
            PoseStack poseStack,
            ItemAnimationDefinition definition,
            ItemAnimationState state,
            ItemTransformAccumulator accumulator,
            float ageInTicks,
            float speed
    ) {
        state.updateTime(ageInTicks, speed);

        if (state.getLoopType() == ItemAnimationState.LoopType.PLAY_ONCE) {
            if (state.getAccumulatedTime() / 1000.0f >= definition.lengthInSeconds()) {
                state.stop();
                return;
            }
        }

        long accumulatedTime = state.getAccumulatedTime();
        float elapsedSeconds = getElapsedSeconds(definition, accumulatedTime, state.getLoopType());

        accumulator.reset();

        for (Map.Entry<String, List<ItemAnimationChannel>> entry : definition.channels().entrySet()) {
            for (ItemAnimationChannel channel : entry.getValue()) {
                Keyframe[] keyframes = channel.keyframes();
                if (keyframes.length == 0) continue;

                int i = Math.max(0, Mth.binarySearch(0, keyframes.length,
                        idx -> elapsedSeconds <= keyframes[idx].timestamp()) - 1);
                int j = Math.min(keyframes.length - 1, i + 1);

                Keyframe from = keyframes[i];
                Keyframe to = keyframes[j];

                float delta = elapsedSeconds - from.timestamp();
                float t = (i == j) ? 0.0f
                        : Mth.clamp(delta / (to.timestamp() - from.timestamp()), 0.0f, 1.0f);

                Vector3f result = new Vector3f();
                to.interpolation().apply(result, t, keyframes, i, j, 1.0f);

                channel.target().apply(accumulator, result);
            }
        }

        accumulator.applyTo(poseStack);
    }

    private static float getElapsedSeconds(
            ItemAnimationDefinition definition,
            long accumulatedTime,
            ItemAnimationState.LoopType loopType
    ) {
        float seconds = accumulatedTime / 1000.0f;
        float length = definition.lengthInSeconds();

        return switch (loopType) {
            case LOOP -> seconds % length;
            case HOLD_ON_LAST_FRAME -> Math.min(seconds, length);
            case PLAY_ONCE -> seconds;
        };
    }
}