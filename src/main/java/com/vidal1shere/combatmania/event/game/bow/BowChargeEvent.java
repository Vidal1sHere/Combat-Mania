package com.vidal1shere.combatmania.event.game.bow;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class BowChargeEvent extends PlayerEvent implements ICancellableEvent {

    private final Level level;
    private final ItemStack weapon;
    private final int charge;
    private final boolean isCrossbow;

    public BowChargeEvent(Level level, Player player, ItemStack weapon, int charge) {
        super(player);
        this.level = level;
        this.weapon = weapon;
        this.charge = charge;
        this.isCrossbow = weapon.getItem() instanceof CrossbowItem;
    }

    public Level getLevel() { return level; }
    public ItemStack getWeapon() { return weapon; }
    public int getCharge() { return charge; }
    public boolean isCrossbow() { return isCrossbow; }
}