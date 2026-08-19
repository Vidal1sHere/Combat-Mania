package com.vidal1shere.combatmania.event.game.bow;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class QuiverArrowCycleEvent extends PlayerEvent implements ICancellableEvent {
    private final ItemStack quiver;
    private final ItemStack from;
    private final ItemStack to;
    
    public QuiverArrowCycleEvent(Player player, ItemStack quiver, ItemStack from, ItemStack to) {
        super(player);
        this.quiver = quiver;
        this.from = from;
        this.to = to;
    }

    public ItemStack getQuiver() { return quiver; }
    public ItemStack getFromArrow() { return from; }
    public ItemStack getToArrow() { return to; }
}
