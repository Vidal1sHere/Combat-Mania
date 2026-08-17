package com.vidal1shere.combatmania.event.game;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class BowChargeEvent extends Event implements ICancellableEvent {

    private final Level level;
    private final Player player;
    private final ItemStack weapon;
    private final int charge;
    private final boolean isCrossbow;

    public BowChargeEvent(Level level, Player player, ItemStack weapon, int charge) {
        this.level = level;
        this.player = player;
        this.weapon = weapon;
        this.charge = charge;
        this.isCrossbow = weapon.getItem() instanceof CrossbowItem;
    }

    public Level getLevel() { return level; }
    public Player getPlayer() { return player; }
    public ItemStack getWeapon() { return weapon; }
    public int getCharge() { return charge; }
    public boolean isCrossbow() { return isCrossbow; }
}