package com.vidal1shere.combatmania.util;

import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface AnimationSupplier {
    ItemAnimationDefinition get(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state);
}
