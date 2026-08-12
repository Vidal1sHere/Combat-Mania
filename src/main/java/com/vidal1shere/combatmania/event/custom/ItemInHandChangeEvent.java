package com.vidal1shere.combatmania.event.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class ItemInHandChangeEvent extends PlayerEvent {

    private final InteractionHand hand;
    private final ItemStack previousStack;
    private final ItemStack currentStack;
    private final int previousSlot;
    private final int currentSlot;
    private final boolean sameComponents;

    public ItemInHandChangeEvent(Player player, InteractionHand hand, 
                                 ItemStack previousStack, ItemStack currentStack, 
                                 int previousSlot, int currentSlot, boolean sameComponents) {
        super(player);
        this.hand = hand;
        this.previousStack = previousStack;
        this.currentStack = currentStack;
        this.previousSlot = previousSlot;
        this.currentSlot = currentSlot;
        this.sameComponents = sameComponents;
    }
    
    public InteractionHand getHand() {
        return hand;
    }

    public ItemStack getPreviousStack() {
        return previousStack;
    }

    public ItemStack getCurrentStack() {
        return currentStack;
    }

    public int getPreviousSlot() {
        return previousSlot;
    }

    public int getCurrentSlot() {
        return currentSlot;
    }
    
    public boolean isSameComponents() {
        return sameComponents;
    }
}