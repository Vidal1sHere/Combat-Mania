package com.vidal1shere.combatmania.strike;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class StrikeActiveData {

    public static final Codec<StrikeActiveData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("primary_active").forGetter(d -> d.primaryActive),
            Codec.BOOL.fieldOf("secondary_active").forGetter(d -> d.secondaryActive),
            Codec.INT.fieldOf("primary_ticks").forGetter(d -> d.primaryTicks),
            Codec.INT.fieldOf("secondary_ticks").forGetter(d -> d.secondaryTicks)
    ).apply(instance, StrikeActiveData::new));

    private boolean primaryActive;
    private boolean secondaryActive;

    private int primaryTicks;
    private int secondaryTicks;

    public StrikeActiveData() {
        this.primaryActive = false;
        this.secondaryActive = false;
        this.primaryTicks = 0;
        this.secondaryTicks = 0;
    }

    public StrikeActiveData(boolean primaryActive, boolean secondaryActive, int primaryTicks, int secondaryTicks) {
        this.primaryActive = primaryActive;
        this.secondaryActive = secondaryActive;
        this.primaryTicks = primaryTicks;
        this.secondaryTicks = secondaryTicks;
    }

    public void setActive(boolean primary, boolean active) {
        if (primary) {
            primaryActive = active;
            if (!active) primaryTicks = 0;
        } else {
            secondaryActive = active;
            if (!active) secondaryTicks = 0;
        }
    }

    public boolean isActive(boolean primary) {
        return primary ? primaryActive : secondaryActive;
    }

    public int getTicks(boolean primary) {
        return primary ? primaryTicks : secondaryTicks;
    }

    public void tick(boolean primary) {
        if (primary && primaryActive) primaryTicks++;
        else if (!primary && secondaryActive) secondaryTicks++;
    }

    public boolean isBlocked(boolean primary) {
        return primary ? secondaryActive : primaryActive;
    }
}