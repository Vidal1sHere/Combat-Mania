package com.vidal1shere.combatmania.api;

import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;

import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IAnimatedItem {
    /**
     * Called every frame to drive animation state for this item.
     * Implementations should call start(), stop(), or animateWhen() on the provided state.
     */
    void setupAnim(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, float ageInTicks, float partialTick);

    /**
     * Returns the animation definition associated with the current state.
     * Return null to skip rendering the animation this frame.
     */
    ItemAnimationDefinition getAnimation(ItemStack stack, InteractionHand hand, Player player);
    
    default float getAnimationSpeed(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        return 1.0f;
    }
}