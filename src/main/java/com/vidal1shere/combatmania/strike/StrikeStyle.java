package com.vidal1shere.combatmania.strike;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class StrikeStyle {

    private final StrikeProperties properties;

    public StrikeStyle(StrikeProperties properties) {
        this.properties = properties;
    }

    public Holder<Attribute> getSpeedAttribute() {
        return properties.getSpeedAttribute();
    }

    public float getMinStrengthScale() {
        return properties.getMinStrengthScale();
    }

    public int getMaxDuration(ItemStack stack, Player player) {
        return properties.getMaxDuration(stack, player);
    }

    public boolean syncAnimationSpeed() {
        return properties.syncAnimationSpeed();
    }

    public boolean overlap() {
        return properties.overlap();
    }

    public boolean swingHand() {
        return properties.swingHand();
    }

    public boolean lowerHand() {
        return properties.lowerHand();
    }

    protected StrikeProperties properties() {
        return properties;
    }

    public void onStart(StrikeContext ctx) {}
    public void onTick(StrikeContext ctx, int ticksHeld) {}
    public void onStop(StrikeContext ctx, int ticksHeld) {}
}