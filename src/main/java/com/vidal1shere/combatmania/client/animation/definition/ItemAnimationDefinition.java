package com.vidal1shere.combatmania.client.animation.definition;

import com.vidal1shere.combatmania.client.animation.keyframe.ItemAnimationTarget;
import com.vidal1shere.combatmania.client.animation.keyframe.KeyframeVecs;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.Keyframe;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines a first-person item animation with its channels, length, and optional pivot point.
 */
@OnlyIn(Dist.CLIENT)
public record ItemAnimationDefinition(float lengthInSeconds, Map<String, List<ItemAnimationChannel>> channels, Vector3f pivot, boolean debug) {
    public static final Vector3f NO_PIVOT = new Vector3f(0, 0, 0);

    public static ItemAnimationDefinition identity(float length) {
        return ItemAnimationDefinition.Builder
                .withLength(length)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(length, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(length, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }

    public static ItemAnimationDefinition debug(float length) {
        return ItemAnimationDefinition.Builder
                .withLength(length)
                .pivot(0.63f, -0.5f, -0.72f)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(length, KeyframeVecs.posVec(0.0f, 0.0f, -2.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(length, KeyframeVecs.degreeVec(0.0f, 0, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }
    
    @OnlyIn(Dist.CLIENT)
    public static class Builder {

        private final float length;
        private final Map<String, List<ItemAnimationChannel>> channels = new HashMap<>();
        private Vector3f pivot = NO_PIVOT;
        private boolean debug = false;

        public static Builder withLength(float lengthInSeconds) {
            return new Builder(lengthInSeconds);
        }

        private Builder(float lengthInSeconds) {
            this.length = lengthInSeconds;
        }

        public Builder pivot(Vector3f pivot) {
            this.pivot = pivot;
            return this;
        }

        public Builder pivot(float x, float y, float z) {
            return pivot(new Vector3f(x, y, z));
        }
        
        public Builder debug() {
            this.debug = true;
            return this;
        }

        public Builder addChannel(String name, ItemAnimationChannel channel) {
            this.channels.computeIfAbsent(name, k -> new ArrayList<>()).add(channel);
            return this;
        }

        public ItemAnimationDefinition build() {
            return new ItemAnimationDefinition(this.length, this.channels, this.pivot, this.debug);
        }
    }
}