package com.vidal1shere.combatmania.constants;

public enum StrikeSlot {
    PRIMARY,
    SECONDARY;

    public StrikeSlot other() {
        return this == PRIMARY ? SECONDARY : PRIMARY;
    }
}