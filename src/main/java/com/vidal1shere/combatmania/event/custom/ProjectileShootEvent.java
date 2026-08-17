package com.vidal1shere.combatmania.event.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class ProjectileShootEvent extends Event implements ICancellableEvent {

    private final LivingEntity shooter;
    private final ItemStack weapon;
    private final ItemStack ammo;
    private final Projectile projectile;
    private final float velocity;
    private final boolean modified;

    public ProjectileShootEvent(LivingEntity shooter, ItemStack weapon, ItemStack ammo, Projectile projectile, float velocity, boolean modified) {
        this.shooter = shooter;
        this.weapon = weapon;
        this.ammo = ammo;
        this.projectile = projectile;
        this.velocity = velocity;
        this.modified = modified;
    }

    public LivingEntity getShooter() { return shooter; }
    public ItemStack getWeapon() { return weapon; }
    public ItemStack getAmmo() { return ammo; }
    public Projectile getProjectile() { return projectile; }
    public float getVelocity() { return velocity; }
    public boolean isModified() { return modified; }
}