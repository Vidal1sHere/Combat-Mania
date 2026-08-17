package com.vidal1shere.combatmania.event.game;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class StaggerAttackEvent extends LivingEvent implements ICancellableEvent {

    private final LivingEntity attacker;
    private float strength;
    private int duration;

    public StaggerAttackEvent(LivingEntity target, LivingEntity attacker, float strength, int duration) {
        super(target);

        this.attacker = attacker;
        this.strength = strength;
        this.duration = duration;
    }

    public LivingEntity getAttacker() {
        return attacker;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}