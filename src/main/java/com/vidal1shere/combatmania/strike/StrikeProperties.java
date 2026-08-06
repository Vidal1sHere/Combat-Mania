package com.vidal1shere.combatmania.strike;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;
import java.util.Map;

public class StrikeProperties {

    private final Holder<Attribute> speedAttribute;
    private final float minStrengthScale;
    private final int maxDuration;
    private final boolean syncAnimationSpeed;
    private final boolean overlap;
    private final boolean swingHand;

    private final Map<Key<?>, Object> extras;

    protected StrikeProperties(Builder builder) {
        this.speedAttribute = builder.speedAttribute;
        this.minStrengthScale = builder.minStrengthScale;
        this.maxDuration = builder.maxDuration;
        this.syncAnimationSpeed = builder.syncAnimationSpeed;
        this.overlap = builder.overlap;
        this.swingHand = builder.swingHand;
        this.extras = Map.copyOf(builder.extras);
    }

    public Holder<Attribute> getSpeedAttribute() {
        return speedAttribute;
    }

    public float getMinStrengthScale() {
        return minStrengthScale;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public boolean syncAnimationSpeed() {
        return syncAnimationSpeed;
    }

    public boolean overlap() {
        return overlap;
    }

    public boolean swingHand() {
        return swingHand;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Key<T> key) {
        return (T) extras.getOrDefault(key, key.defaultValue());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(StrikeProperties properties) {
        return new Builder(properties);
    }

    public record Key<T>(T defaultValue) {}

    public static class Builder {

        private Holder<Attribute> speedAttribute = Attributes.ATTACK_SPEED;
        private float minStrengthScale = 1.0F;
        private int maxDuration = 0;
        private boolean syncAnimationSpeed = true;
        private boolean overlap = false;
        private boolean swingHand = true;

        private final Map<Key<?>, Object> extras = new HashMap<>();

        public Builder() {}

        public Builder(StrikeProperties properties) {
            this.speedAttribute = properties.speedAttribute;
            this.minStrengthScale = properties.minStrengthScale;
            this.maxDuration = properties.maxDuration;
            this.syncAnimationSpeed = properties.syncAnimationSpeed;
            this.overlap = properties.overlap;
            this.swingHand = properties.swingHand;
            this.extras.putAll(properties.extras);
        }

        public Builder speedAttribute(Holder<Attribute> attr) {
            this.speedAttribute = attr;
            return this;
        }

        public Builder minStrengthScale(float value) {
            this.minStrengthScale = value;
            return this;
        }

        public Builder maxDuration(int value) {
            this.maxDuration = value;
            return this;
        }

        public Builder syncAnimSpeed(boolean value) {
            this.syncAnimationSpeed = value;
            return this;
        }

        public Builder overlap(boolean value) {
            this.overlap = value;
            return this;
        }

        public Builder swingHand(boolean value) {
            this.swingHand = value;
            return this;
        }

        public <T> Builder set(Key<T> key, T value) {
            extras.put(key, value);
            return this;
        }

        public StrikeProperties build() {
            return new StrikeProperties(this);
        }
    }
}