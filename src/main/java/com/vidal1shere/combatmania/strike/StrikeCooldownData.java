package com.vidal1shere.combatmania.strike;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class StrikeCooldownData {

    public static final Codec<StrikeCooldownData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("primary_ticker").forGetter(d -> d.primaryTicker),
            Codec.INT.fieldOf("secondary_ticker").forGetter(d -> d.secondaryTicker),
            Codec.INT.fieldOf("primary_duration").forGetter(d -> d.primaryDuration),
            Codec.INT.fieldOf("secondary_duration").forGetter(d -> d.secondaryDuration)
    ).apply(instance, StrikeCooldownData::new));

    private int primaryTicker;
    private int secondaryTicker;

    private int primaryDuration;
    private int secondaryDuration;

    public StrikeCooldownData() {
        this.primaryTicker = 0;
        this.secondaryTicker = 0;
        this.primaryDuration = 0;
        this.secondaryDuration = 0;
    }

    private StrikeCooldownData(int primaryTicker, int secondaryTicker, int primaryDuration, int secondaryDuration) {
        this.primaryTicker = primaryTicker;
        this.secondaryTicker = secondaryTicker;
        this.primaryDuration = primaryDuration;
        this.secondaryDuration = secondaryDuration;
    }

    public void tick() {
        if (primaryTicker < primaryDuration) primaryTicker++;
        if (secondaryTicker < secondaryDuration) secondaryTicker++;
    }

    public void exhaust(boolean primary, int duration, boolean overlap) {
        if (primary) {
            primaryTicker = 0;
            primaryDuration = duration;
            if (overlap) {
                secondaryTicker = 0;
                secondaryDuration = duration;
            }
        } else {
            secondaryTicker = 0;
            secondaryDuration = duration;
            if (overlap) {
                primaryTicker = 0;
                primaryDuration = duration;
            }
        }
    }

    public float getPrimaryScale() {
        return primaryDuration == 0 ? 1.0F : Math.min(1.0F, (float) primaryTicker / primaryDuration);
    }

    public float getSecondaryScale() {
        return secondaryDuration == 0 ? 1.0F : Math.min(1.0F, (float) secondaryTicker / secondaryDuration);
    }
}