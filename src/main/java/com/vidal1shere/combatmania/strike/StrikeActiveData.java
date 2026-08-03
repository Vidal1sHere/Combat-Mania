package com.vidal1shere.combatmania.strike;

public class StrikeActiveData {

    private boolean primaryActive = false;
    private boolean secondaryActive = false;

    private int primaryTicks = 0;
    private int secondaryTicks = 0;

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