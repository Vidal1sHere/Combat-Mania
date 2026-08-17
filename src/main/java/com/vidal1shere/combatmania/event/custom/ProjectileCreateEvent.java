package com.vidal1shere.combatmania.event.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class ProjectileCreateEvent extends Event implements ICancellableEvent {

    private final LivingEntity shooter;
    private final ItemStack weapon;
    private final ItemStack ammo;
    private final Projectile original;
    private Projectile projectile;
    private float velocity;
    private boolean modified = false;

    public ProjectileCreateEvent(LivingEntity shooter, ItemStack weapon, ItemStack ammo, Projectile projectile, float velocity) {
        this.shooter = shooter;
        this.weapon = weapon;
        this.ammo = ammo;
        this.original = projectile;
        this.projectile = projectile;
        this.velocity = velocity;
    }

    public LivingEntity getShooter() { return shooter; }
    public ItemStack getWeapon() { return weapon; }
    public ItemStack getAmmo() { return ammo; }
    public Projectile getOriginal() { return original; }

    public Projectile getProjectile() { return projectile; }
    public void setProjectile(Projectile projectile) {
        this.modified = true;
        this.projectile = projectile;
    }

    public float getVelocity() { return velocity; }
    public void setVelocity(float velocity) {
        this.modified = true;
        this.velocity = velocity;
    }

    public boolean isModified() {
        return modified || projectile != original;
    }
}