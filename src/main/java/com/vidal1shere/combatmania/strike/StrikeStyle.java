package com.vidal1shere.combatmania.strike;

import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class StrikeStyle {

    public abstract Holder<Attribute> getSpeedAttribute();
    public float getMinStrengthScale() { return 1.0F; }
    public int getMaxDuration() { return 0; }
    public boolean syncAnimationSpeed() { return true; }
    public boolean overlap() { return false; }
    public boolean swingHand() { return true; }

    public void onStart(StrikeContext ctx) {}
    public void onTick(StrikeContext ctx, int ticksHeld) {}
    public void onStop(StrikeContext ctx, int ticksHeld) {}

    @OnlyIn(Dist.CLIENT)
    public void onAnimStart(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {}

    @OnlyIn(Dist.CLIENT)
    public void onAnimStop(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {}

    @OnlyIn(Dist.CLIENT)
    public ItemAnimationDefinition getAnimation(ItemStack stack, InteractionHand hand, Player player) { return null; }
}