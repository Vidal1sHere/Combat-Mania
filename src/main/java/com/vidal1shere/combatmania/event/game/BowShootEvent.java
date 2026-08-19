package com.vidal1shere.combatmania.event.game;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class BowShootEvent extends LivingEvent implements ICancellableEvent {
    private final ItemStack weapon;
    private final ItemStack ammo;
    private final Projectile original;
    private final boolean isCrossbow;
    private Projectile projectile;
    private float velocity;
    
    public BowShootEvent(LivingEntity entity, ItemStack weapon, ItemStack ammo, Projectile projectile, float velocity) {
        super(entity);
        this.weapon = weapon;
        this.ammo = ammo;
        this.original = this.projectile = projectile;
        this.isCrossbow = weapon.getItem() instanceof CrossbowItem;
        this.velocity = velocity;
    }

    public ItemStack getWeapon() { return weapon; }
    public ItemStack getAmmo() { return ammo; }
    public Projectile getOriginal() { return original; }
    public boolean isCrossbow() { return isCrossbow; }

    public Projectile getProjectile() { return projectile; }
    public void setProjectile(Projectile projectile) { this.projectile = projectile; }

    public float getVelocity() { return velocity; }
    public void setVelocity(float velocity) { this.velocity = velocity; }

    public boolean isVanilla() { return this.projectile == this.original; }
}
