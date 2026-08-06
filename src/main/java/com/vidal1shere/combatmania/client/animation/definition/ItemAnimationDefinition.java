package com.vidal1shere.combatmania.client.animation.definition;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines a first-person item animation with its channels and length.
 * Mirrors AnimationDefinition but uses ItemAnimationChannel instead of AnimationChannel.
 */
@OnlyIn(Dist.CLIENT)
public record ItemAnimationDefinition(float lengthInSeconds, Map<String, List<ItemAnimationChannel>> channels) {

    @OnlyIn(Dist.CLIENT)
    public static class Builder {

        private final float length;
        private final Map<String, List<ItemAnimationChannel>> channels = new HashMap<>();

        public static Builder withLength(float lengthInSeconds) {
            return new Builder(lengthInSeconds);
        }

        private Builder(float lengthInSeconds) {
            this.length = lengthInSeconds;
        }

        public Builder addChannel(String name, ItemAnimationChannel channel) {
            this.channels.computeIfAbsent(name, k -> new ArrayList<>()).add(channel);
            return this;
        }

        public ItemAnimationDefinition build() {
            return new ItemAnimationDefinition(this.length, this.channels);
        }
    }
}