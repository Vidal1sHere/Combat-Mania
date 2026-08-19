package com.vidal1shere.combatmania.event.game;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;

public class CrossbowLoadEvent extends PlayerEvent implements ICancellableEvent {
    private final ItemStack weapon;
    private final ItemStack ammo;
    private List<ItemStack> projectiles;

    public CrossbowLoadEvent(Player player, ItemStack ammo, ItemStack weapon) {
        super(player);
        this.weapon = weapon;
        this.ammo = ammo;
    }

    public ItemStack getWeapon() { return weapon; }
    public ItemStack getAmmo() { return ammo; }

    public List<ItemStack> getProjectiles() { return projectiles; }
    public void setProjectiles(List<ItemStack> projectiles) { this.projectiles = projectiles; }
}