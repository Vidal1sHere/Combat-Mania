package com.vidal1shere.combatmania.strike;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

public abstract class StrikeStyle {

    public abstract Holder<Attribute> getSpeedAttribute();
    public float getMinStrengthScale() { return 1.0F; }
    public int getMaxDuration() { return 0; }
    public boolean overlap() { return false; }
    public boolean swingHand() { return true; }
    public boolean lowerHand() { return true; }

    public void onStart(StrikeContext ctx) {}
    public void onTick(StrikeContext ctx, int ticksHeld) {}
    public void onStop(StrikeContext ctx, int ticksHeld) {}
}