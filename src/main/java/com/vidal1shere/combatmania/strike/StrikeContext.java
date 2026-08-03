package com.vidal1shere.combatmania.strike;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class StrikeContext {

    private final Player player;
    private final ItemStack itemStack;
    private final StrikeStyle style;

    @Nullable
    private final Entity target;

    private final float baseDamage;
    private final float baseReach;
    private final float baseKnockback;
    private final float strengthScale;

    private final boolean isSprinting;
    private final boolean isOnGround;
    private final float fallDistance;

    private final Level level;
    
    public StrikeContext(Player player, ItemStack itemStack, StrikeStyle style,
                         @Nullable Entity target, float baseDamage, float baseReach,
                         float baseKnockback, float strengthScale, Level level) {
        this.player = player;
        this.itemStack = itemStack;
        this.style = style;
        this.target = target;
        this.baseDamage = baseDamage;
        this.baseReach = baseReach;
        this.baseKnockback = baseKnockback;
        this.strengthScale = strengthScale;
        this.isSprinting = player.isSprinting();
        this.isOnGround = player.onGround();
        this.fallDistance = player.fallDistance;
        this.level = level;
    }

    public Player player() { return player; }
    public ItemStack itemStack() { return itemStack; }
    public StrikeStyle style() { return style; }

    @Nullable
    public Entity target() { return target; }
    public boolean hasTarget() { return target != null; }

    public float baseDamage() { return baseDamage; }
    public float baseReach() { return baseReach; }
    public float reach() { return baseReach * baseReach; }
    public float baseKnockback() { return baseKnockback; }
    public float strengthScale() { return strengthScale; }

    public boolean isSprinting() { return isSprinting; }
    public boolean isOnGround() { return isOnGround; }
    public float fallDistance() { return fallDistance; }

    public Level level() { return level; }

    public Vec3 defaultKnockbackDirection() {
        double yRad = player.getYRot() * (Math.PI / 180.0);
        return new Vec3(-Math.sin(yRad), 0.1, Math.cos(yRad));
    }
}