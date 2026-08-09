package com.vidal1shere.combatmania.strike;

import com.vidal1shere.combatmania.util.CustomProperties;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiFunction;

public class StrikeProperties {

    private final Holder<Attribute> speedAttribute;
    private final float minStrengthScale;
    private final BiFunction<ItemStack, LivingEntity, Integer> maxDuration;
    private final boolean syncAnimationSpeed;
    private final boolean overlap;
    private final boolean swingHand;
    private final boolean lowerHand;
    private final CustomProperties customProperties;
    
    protected StrikeProperties(Builder builder) {
        this.speedAttribute = builder.speedAttribute;
        this.minStrengthScale = builder.minStrengthScale;
        this.maxDuration = builder.maxDuration;
        this.syncAnimationSpeed = builder.syncAnimationSpeed;
        this.overlap = builder.overlap;
        this.swingHand = builder.swingHand;
        this.lowerHand = builder.lowerHand;
        this.customProperties = builder.customProperties;
    }

    public Holder<Attribute> getSpeedAttribute() {
        return speedAttribute;
    }

    public float getMinStrengthScale() {
        return minStrengthScale;
    }

    public int getMaxDuration(ItemStack stack, LivingEntity entity) {
        return maxDuration.apply(stack, entity);
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

    public boolean lowerHand() {
        return lowerHand;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(CustomProperties.Key<T> key) {
        return customProperties.get(key);
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
        private BiFunction<ItemStack, LivingEntity, Integer> maxDuration = (s, e) -> 0;
        private boolean syncAnimationSpeed = true;
        private boolean overlap = false;
        private boolean swingHand = true;
        private boolean lowerHand = false;
        private CustomProperties customProperties = CustomProperties.of();
        
        public Builder() {}

        public Builder(StrikeProperties customProperties) {
            this.speedAttribute = customProperties.speedAttribute;
            this.minStrengthScale = customProperties.minStrengthScale;
            this.maxDuration = customProperties.maxDuration;
            this.syncAnimationSpeed = customProperties.syncAnimationSpeed;
            this.overlap = customProperties.overlap;
            this.swingHand = customProperties.swingHand;
            this.customProperties.copy(customProperties.customProperties);
        }

        public Builder speedAttribute(Holder<Attribute> attr) {
            this.speedAttribute = attr;
            return this;
        }

        public Builder minStrengthScale(float value) {
            this.minStrengthScale = value;
            return this;
        }

        public Builder maxDuration(BiFunction<ItemStack, LivingEntity, Integer> value) {
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

        public Builder lowerHand(boolean value) {
            this.lowerHand = value;
            return this;
        }

        public <T> Builder set(CustomProperties.Key<T> key, T value) {
            customProperties.set(key, value);
            return this;
        }

        public StrikeProperties build() {
            return new StrikeProperties(this);
        }
    }
}