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

    private final StrikeProperties properties;

    public StrikeStyle(StrikeProperties properties) {
        this.properties = properties;
    }

    public Holder<Attribute> getSpeedAttribute() {
        return properties.getSpeedAttribute();
    }

    public float getMinStrengthScale() {
        return properties.getMinStrengthScale();
    }

    public int getMaxDuration(ItemStack stack, Player player) {
        return properties.getMaxDuration(stack, player);
    }

    public boolean syncAnimationSpeed() {
        return properties.syncAnimationSpeed();
    }

    public boolean overlap() {
        return properties.overlap();
    }

    public boolean swingHand() {
        return properties.swingHand();
    }

    public boolean lowerHand() {
        return properties.lowerHand();
    }
    
    public ItemAnimationDefinition supplyAnimation(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        return properties.supplyAnimation(stack, hand, player, state);
    }

    protected StrikeProperties properties() {
        return properties;
    }

    public void onStart(StrikeContext ctx) {}
    public void onTick(StrikeContext ctx, int ticksHeld) {}
    public void onStop(StrikeContext ctx, int ticksHeld) {}

    @OnlyIn(Dist.CLIENT)
    public void onAnimStart(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {}

    @OnlyIn(Dist.CLIENT)
    public void onAnimStop(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {}

    @OnlyIn(Dist.CLIENT)
    public void setupAnim(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, float ageInTicks, float partialTick) {}

    @OnlyIn(Dist.CLIENT)
    public ItemAnimationDefinition getAnimation(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        return state.getActiveDefinition();
    }
    
    public float getAnimationSpeed(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        if (!syncAnimationSpeed()) return 1.0F;
        return (float) player.getAttributeValue(getSpeedAttribute());
    }

    public InteractionHand[] animatedHands(ItemStack stack, Player player) {
        return new InteractionHand[]{InteractionHand.MAIN_HAND};
    }
}