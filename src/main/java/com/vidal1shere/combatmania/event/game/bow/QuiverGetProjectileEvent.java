package com.vidal1shere.combatmania.event.game.bow;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class QuiverGetProjectileEvent extends PlayerEvent implements ICancellableEvent {
    private final ItemStack weaponItemStack;
    private final ItemStack quiverItemStack;
    private ItemStack projectileItemStack;
    
    public QuiverGetProjectileEvent(Player player, ItemStack quiver, ItemStack weapon, ItemStack ammo) {
        super(player);
        this.weaponItemStack = weapon;
        this.projectileItemStack = ammo;
        this.quiverItemStack = quiver;
    }

    public ItemStack getQuiverItemStack() {
        return quiverItemStack;
    }

    public ItemStack getWeapon() {
        return this.weaponItemStack;
    }

    public ItemStack getProjectile() {
        return this.projectileItemStack;
    }

    public void setProjectile(ItemStack projectileItemStack) {
        this.projectileItemStack = projectileItemStack;
    }
}
